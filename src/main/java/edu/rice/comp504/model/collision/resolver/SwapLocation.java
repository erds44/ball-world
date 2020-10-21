package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Swap changes the location of two objects.
 */
public class SwapLocation implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * private constructor for singleton pattern.
     */
    private SwapLocation() {

    }

    /**
     * Only makes 1 stop strategy.
     *
     * @return The stop strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new SwapLocation();
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
        Point2D.Double loc = a.getLocation();
        a.setLocation(b.getLocation());
        b.setLocation(loc);
    }

}
