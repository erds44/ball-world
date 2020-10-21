package edu.rice.comp504.model.collision;

import edu.rice.comp504.model.collision.handler.BallCollisionHandler;
import edu.rice.comp504.model.collision.handler.FishCollisionHandler;
import edu.rice.comp504.model.collision.handler.ICollisionHandler;
import edu.rice.comp504.model.collision.resolver.HorizontalWall;
import edu.rice.comp504.model.collision.resolver.VerticalWall;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Fish;
import edu.rice.comp504.model.paintObj.Object;
import edu.rice.comp504.model.strategy.Strategy;

import java.util.*;

/***************************************************************************************
 *    Title: MinPQ.java
 *    Author: Robert Sedgewick & Kevin Wayne
 *    Date: Oct 15 2020
 *    Availability: https://algs4.cs.princeton.edu/61event/MinPQ.java.html\
 *    Collision system used for any object collision management.
 ***************************************************************************************/
public class CollisionSystem {
    private static CollisionSystem singleton;
    private PriorityQueue<Event> minPQ;
    private double time;
    private Map<Map.Entry<Object, Object>, ICollisionHandler> map = new HashMap<>();

    {
        map.put(Map.entry(Object.Ball, Object.Ball), BallCollisionHandler.makeOnly());
        map.put(Map.entry(Object.Ball, Object.Fish), FishCollisionHandler.makeOnly());
        map.put(Map.entry(Object.Fish, Object.Fish), FishCollisionHandler.makeOnly());

    }

    private final ArrayList<Strategy> nonCollidableStrategy = new ArrayList<>();

    {
        nonCollidableStrategy.add(Strategy.NULLSTRATEGY);
        nonCollidableStrategy.add(Strategy.RANDOMLOCATIONSTRATEGY);
        nonCollidableStrategy.add(Strategy.RANDOMWALKSTRATEGY);
        nonCollidableStrategy.add(Strategy.ROTATINGSTRATEGY);
        nonCollidableStrategy.add(Strategy.SHAKINGSTRATEGY);
    }

    /**
     * private constructor for singleton pattern.
     */
    private CollisionSystem() {
        this.minPQ = new PriorityQueue<>();
        time = 0;
    }

    /**
     * singleton pattern.
     *
     * @return singleton
     */

    public static CollisionSystem makeOnly() {
        if (singleton == null) {
            singleton = new CollisionSystem();
        }
        return singleton;
    }

    /**
     * Predict future collision events.
     *
     * @param objs all objects in canvas
     * @param a    the object to check collision
     */
    public void predict(Collection<APaintObj> objs, APaintObj a) {
        if (a == null) {
            return;
        }
        // obj - obj collisions
        if (!nonCollidableStrategy.contains(a.getStrategy().getName()) && !(a instanceof Fish)) { // do not count fish in collision
            for (APaintObj obj : objs) {
                if (!nonCollidableStrategy.contains(obj.getStrategy().getName()) && !(obj instanceof Fish)) {
                    double dt = a.timeToHit(obj);
                    this.minPQ.add(new Event(time + dt, a, obj));
                }
            }

        }
        // obj - wall collisions
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        this.minPQ.add(new Event(time + dtX, a, null));
        this.minPQ.add(new Event(time + dtY, null, a));
    }

    /**
     * Update all the ball location after collision (if applicable).
     *
     * @param objs all the objects in the canvas
     */
    public synchronized void update(Collection<APaintObj> objs) {
        double limit = this.time + 1;
        Event e = this.minPQ.peek();
        while (e != null && e.getTime() <= limit) {
            e = this.minPQ.poll();
            if (e.isValidCollision()) {
                for (APaintObj obj : objs) {
                    obj.updateLocation(e.getTime() - time);
                }
                time = e.getTime();
                resolveCollision(objs, e.getA(), e.getB());
            }
            e = this.minPQ.peek();
        }
        for (APaintObj ball : objs) { // in case the event time in PQ does not reach the limit
            ball.updateLocation(limit - time);
        }
        this.time = limit;
        objStrategyUpdate(objs);
    }


    /**
     * The resolveCollision resolves the collision based on the object type.
     *
     * @param objs all the objects in canvas
     * @param a    first object
     * @param b    second object
     */
    private void resolveCollision(Collection<APaintObj> objs, APaintObj a, APaintObj b) {
        if (a != null && b != null) {
            handleCollision(a, b);                            // obj-obj collision
        } else if (a != null) {
            VerticalWall.makeOnly().resolveCollision(a, b);   // obj-wall collision
        } else if (b != null) {
            HorizontalWall.makeOnly().resolveCollision(b, a); // obj-wall collision
        }
        // re-predict time event since collision may change velocity, location, or radius.
        predict(objs, a);
        predict(objs, b);
    }

    /**
     * Help method for resolveCollision for the specific case where two objects are not null.
     *
     * @param a fist object
     * @param b second object
     */
    public void handleCollision(APaintObj a, APaintObj b) {
        Object na = a.getName();
        Object nb = b.getName();
        ICollisionHandler handler = map.get(Map.entry(na, nb));
        if (handler == null) {
            handler = map.get(Map.entry(nb, na));
            handler.handleCollision(b, a);
        } else {
            handler.handleCollision(a, b);
        }

    }

    /**
     * Update the object strategy if applicable, like change color, location, etc.
     *
     * @param objs all the objects in canvas
     */
    private void objStrategyUpdate(Collection<APaintObj> objs) {
        for (APaintObj obj : objs) {
            if (obj.getStrategy().updateState(obj)) {
                predict(objs, obj);  //re-predict time event if velocity or radius changes
            }
        }
    }

    /**
     * Clear the PQ and reset time, used for clear canvas functionality.
     */
    public void clear() {
        this.minPQ.clear();
        this.time = 0;
    }


}
