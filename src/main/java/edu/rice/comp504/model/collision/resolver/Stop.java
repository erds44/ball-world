package edu.rice.comp504.model.collision.resolver;


import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Stop makes the fist object velocity to 0.
 */
public class Stop implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * private constructor for singleton pattern.
     */
    private Stop() {

    }

    /**
     * Only makes 1 stop strategy.
     *
     * @return The stop strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Stop();
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
        b.setVelocity(new Point2D.Double(-b.getVelocity().x, -b.getVelocity().y));
        a.setVelocity(new Point2D.Double(0, 0));
    }

}
