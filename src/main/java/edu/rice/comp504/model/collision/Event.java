package edu.rice.comp504.model.collision;

import edu.rice.comp504.model.paintObj.APaintObj;

/***************************************************************************************
 *    Title: MinPQ.java
 *    Author: Robert Sedgewick & Kevin Wayne
 *    Date: Oct 15 2020
 *    Availability: https://algs4.cs.princeton.edu/61event/MinPQ.java.html\
 *    Event describes a collision between balls and walls, balls and balls.
 *    let a, b be the two balls:
 *    -  a null, b not null:     collision with vertical wall
 *    -  a not null, b null:     collision with horizontal wall
 *    -  a and b both not null:  collision between a and b
 ***************************************************************************************/

public class Event implements Comparable<Event> {
    private double time;
    private final APaintObj a;
    private final APaintObj b;
    private int countA;
    private int countB;

    /**
     * Collision event.
     *
     * @param t time to happen the event
     * @param a first ball
     * @param b second ball
     */
    public Event(double t, APaintObj a, APaintObj b) {
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
     * Get the time.
     *
     * @return the time
     */
    public double getTime() {
        return this.time;
    }

    /**
     * Get the first object.
     *
     * @return the first object
     */
    public APaintObj getA() {
        return this.a;
    }

    /**
     * Get the second object.
     *
     * @return the second object
     */
    public APaintObj getB() {
        return this.b;
    }

    /**
     * Check if it is a valid collision.
     *
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

    /**
     * Override the compare method based on time.
     *
     * @param e event
     * @return 1, 0, -1 based on comparison
     */
    @Override
    public int compareTo(Event e) {
        return Double.compare(this.time, e.time);
    }
}
