package edu.rice.comp504.model;

import edu.rice.comp504.Event;
import edu.rice.comp504.model.ball.Ball;

import java.util.Collection;
import java.util.PriorityQueue;

/**
 * Collision system used for ball-ball collision and ball-wall collision resolution.
 */
public class CollisionSystem {
    private static CollisionSystem singleton;
    private PriorityQueue<Event> minPQ;
    private double time;

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
     * @param balls all balls in canvas
     * @param a     the ball to check collision
     */
    // updates priority queue with all new events for ball a
    public void predict(Collection<Ball> balls, Ball a) {
        if (a == null) {
            return;
        }
        // ball - ball collisions
        for (Ball ball : balls) {
            double dt = a.timeToHit(ball);
            this.minPQ.add(new Event(time + dt, a, ball));
            //System.out.println("At time: "+ time + "   " + (dt + time));
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
    public synchronized void update(Collection<Ball> balls) {
        double limit = this.time + 1;
        //updateBalls(balls);
        // get impending event, discard if invalidated
        //System.out.println("C: " + e.getTime());
        Event e = this.minPQ.peek();
        while (e != null && e.getTime() <= limit) {
            e = this.minPQ.poll();
            if (e.isValidCollision()) {
                for (Ball ball : balls) {
                    ball.updateLocation(e.getTime() - time);
                }
                time = e.getTime();
                Ball a = e.getA();
                Ball b = e.getB();
                if (a != null && b != null) {
                    a.bounceOff(b);              // particle-particle collision
                } else if (a != null && b == null) {
                    a.bounceOffVerticalWall();   // particle-wall collision
                } else if (a == null && b != null) {
                    b.bounceOffHorizontalWall(); // particle-wall collision
                }
                predict(balls, e.getA());
                predict(balls, e.getB());
            }
            e = this.minPQ.peek();
        }
        for (Ball ball : balls) {
            ball.updateLocation(limit - time);
        }
        this.time = limit;
    }

    private void updateBalls(Collection<Ball> balls) {
        for (Ball ball : balls) {
            switch (ball.getStrategy().getName()) {
                case "ChangeColorAfterCollisionStrategy":
                    ball.updateLocation();
                    break;
                case "RotatingStrategy":
                    ball.getStrategy().updateState(ball);
                    break;
                default:
                    boolean b = ball.getStrategy().updateState(ball);
                    ball.updateLocation();
                    if (b) {
                        predict(balls, ball);
                    }
            }
        }
    }

    public void clear() {
        this.minPQ.clear();
        this.time = 0;
    }

    public double getTime() {
        return this.time;
    }

}
