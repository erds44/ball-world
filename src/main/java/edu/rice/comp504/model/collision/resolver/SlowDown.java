package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintobj.APaintObj;

import java.awt.geom.Point2D;

/**
 * SlowDown reduces the first object's velocity with min threshold.
 */
public class SlowDown implements ICollisionResolution {
    private static ICollisionResolution singleton;
    private int decrement;
    private int threshold;

    /**
     * private constructor for singleton pattern.
     */
    private SlowDown() {
        this.decrement = 5;
        this.threshold = 10;
    }

    /**
     * Only makes 1 slowDown strategy.
     *
     * @return The slowDown strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new SlowDown();
        }
        return singleton;
    }

    /**
     * Implementation on how to handle two APaintObj.
     *
     * @param a first object in collision
     * @param b second object in collision
     */
    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        a.bounceOff(b);
        double velX = decrementVelocity(a.getVelocity().x);
        double velY = decrementVelocity(a.getVelocity().y);
        a.setVelocity(new Point2D.Double(velX, velY));
    }

    /**
     * Help method reduces the velocity of first object.
     *
     * @param vel velocity to be decreased
     * @return the decreased velocity
     */

    private double decrementVelocity(double vel) {
        if (Math.abs(vel) >= this.threshold) {
            vel -= Math.signum(vel) * this.decrement;
        }
        return vel;
    }
}
