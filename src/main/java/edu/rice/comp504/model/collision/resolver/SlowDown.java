package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

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
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new SlowDown();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        a.bounceOff(b);
        double velX = decrementVelocity(a.getVelocity().x);
        double velY = decrementVelocity(a.getVelocity().y);
        a.setVelocity(new Point2D.Double(velX, velY));
    }

    private double decrementVelocity(double vel) {
        if (Math.abs(vel) > (this.threshold + this.decrement)) {
            vel -= Math.signum(vel) * this.decrement;
        }
        return vel;
    }
}
