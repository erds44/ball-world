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

/**
 * Collision system used for ball-ball collision and ball-wall collision resolution.
 */
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
     * @param objs all balls in canvas
     * @param a    the ball to check collision
     */
    // updates priority queue with all new events for ball a
    public void predict(Collection<APaintObj> objs, APaintObj a) {
        if (a == null) {
            return;
        }
        if (!nonCollidableStrategy.contains(a.getStrategy().getName()) && !(a instanceof Fish)) {
            // ball - ball collisions
            for (APaintObj obj : objs) {
                if (!nonCollidableStrategy.contains(obj.getStrategy().getName()) && !(obj instanceof Fish)) {
                    double dt = a.timeToHit(obj);
                    this.minPQ.add(new Event(time + dt, a, obj));
                }
            }

        }
        // ball - wall collisions
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        this.minPQ.add(new Event(time + dtX, a, null));
        this.minPQ.add(new Event(time + dtY, null, a));
    }

    /**
     * Simulates the system of particles for the specified amount of time.
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
        for (APaintObj ball : objs) {
            ball.updateLocation(limit - time);
        }
        this.time = limit;
        objStrategyUpdate(objs);
    }

    private void objStrategyUpdate(Collection<APaintObj> objs) {
        for (APaintObj obj : objs) {
            if (obj.getStrategy().updateState(obj)) {
                predict(objs, obj); //re-predict time event if velocity or radius changes
            }
        }
    }


    private void resolveCollision(Collection<APaintObj> objs, APaintObj a, APaintObj b) {
        if (a != null && b != null) {
            handleCollision(a, b);                            // obj-obj collision
        } else if (a != null) {
            VerticalWall.makeOnly().resolveCollision(a, b);   // obj-wall collision
        } else if (b != null) {
            HorizontalWall.makeOnly().resolveCollision(b, a); // obj-wall collision
        }
        predict(objs, a);
        predict(objs, b);
    }

    public void clear() {
        this.minPQ.clear();
        this.time = 0;
    }

    public double getTime() {
        return this.time;
    }

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

}
