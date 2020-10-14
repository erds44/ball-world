package edu.rice.comp504;

import edu.rice.comp504.model.ball.Ball;

/**
 * Event describes a collision between balls and walls, balls and balls.
 * let a, b be the two balls:
 * -  a null, b not null:     collision with vertical wall
 * -  a not null, b null:     collision with horizontal wall
 * -  a and b both not null:  collision between a and b
 */
public class Event implements Comparable<Event> {
    private double time;
    private final Ball a;
    private final Ball b;
    private int countA;
    private int countB;

    /**
     * Collision event.
     *
     * @param t time to happen the event
     * @param a first ball
     * @param b second ball
     */
    public Event(double t, Ball a, Ball b) {
        this.time = t;
        this.a = a;
        this.b = b;
        if (a != null) {
            this.countA = a.count();
        } else {
            this.countA = -1;
        }
        if (b != null) {
            this.countB = b.count();
        } else {
            this.countB = -1;
        }
    }

    /**
     * Check if it is a valid collision.
     * @return if it is valid
     */
    public boolean isValidCollision() {
        if (a != null && a.count() != countA) {
            return false;
        }
        if (b != null && b.count() != countB) {
            return false;
        }
        return true;
    }

    public double getTime() {
        return this.time;
    }

    public Ball getA() {
        return this.a;
    }

    public Ball getB() {
        return this.b;
    }


    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}
