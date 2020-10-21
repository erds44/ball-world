package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

public class SwapLocation implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * private constructor for singleton pattern.
     */
    private SwapLocation() {

    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new SwapLocation();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        Point2D.Double loc = a.getLocation();
        a.setLocation(b.getLocation());
        b.setLocation(loc);
    }

}
