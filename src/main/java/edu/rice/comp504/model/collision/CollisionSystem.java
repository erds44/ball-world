package edu.rice.comp504.model.collision;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.strategy.Strategy;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Collision system used for ball-ball collision and ball-wall collision resolution.
 */
public class CollisionSystem {
    private static CollisionSystem singleton;
    private PriorityQueue<Event> minPQ;
    private double time;
    private Map<Map.Entry<Strategy, Strategy>, ICollisionHandler> map = new HashMap<>();

    {


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
        // ball - ball collisions
//        for (APaintObj obj : objs) {
//            double dt = a.timeToHit(obj);
//            this.minPQ.add(new Event(time + dt, a, obj));
//        }
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
            ICollisionHandler handler = map.get(Map.entry(a.getStrategy().getName(), b.getStrategy().getName()));
            if (handler == null) {
                handler = map.get(Map.entry(b.getStrategy().getName(), a.getStrategy().getName()));
                if (handler == null) {
                    BounceOff.makeOnly().handleCollision(a, b); //default handler if no resolution found
                } else {
                    handler.handleCollision(b, a);
                }
            } else {
                handler.handleCollision(a, b);
            }
        } else if (a != null && b == null) {
            VerticalWall.makeOnly().handleCollision(a, b);   // particle-wall collision
        } else if (a == null && b != null) {
            HorizontalWall.makeOnly().handleCollision(b, a); // particle-wall collision
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


}
