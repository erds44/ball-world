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

    /**
     * private constructor for singleton pattern.
     */
    private CollisionSystem() {
        this.minPQ = new PriorityQueue<>();
    }


    public static CollisionSystem makeOnly() {
        if (singleton == null) {
            singleton = new CollisionSystem();
        }
        return singleton;
    }

    // updates priority queue with all new events for ball a
    public void predict(Collection<Ball> balls, Ball a, double time) {
        if (a == null) return;

//        // particle-particle collisions
//        for (int i = 0; i < balls.length; i++) {
//            double dt = a.timeToHit(particles[i]);
//            if (t + dt <= limit)
//                pq.insert(new Event(t + dt, a, particles[i]));
//        }

        // particle-wall collisions
        double dtX = a.timeToHitVerticalWall();
        double dtY = a.timeToHitHorizontalWall();
        this.minPQ.add(new Event(time + dtX, a, null));
        this.minPQ.add(new Event(time + dtY, null, a));
        //System.out.println("At time: "+ time + "   " + dtX + " " + dtY);
    }

    /**
     * Simulates the system of particles for the specified amount of time.
     */
    public void update(Collection<Ball> balls, int time) {
        updateBalls(balls, time);
        //System.out.println(time);
        if (!this.minPQ.isEmpty()) {
            // get impending event, discard if invalidated

            Event e = this.minPQ.peek();
            //System.out.println("C: " + e.getTime());
            while (e.getTime() < (time + 1)) {
                this.minPQ.poll();
                if (e.isValidCollision()) {
                    e.resolveCollision();
                    predict(balls, e.getA(), time + 1);
                    predict(balls, e.getB(), time + 1);
                }
                e = this.minPQ.peek();
            }
        }
    }

    private void updateBalls(Collection<Ball> balls, int time) {
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
                    if (b) predict(balls, ball, time);
            }
        }
    }

    public void Clear(){
        this.minPQ.clear();
    }

}
