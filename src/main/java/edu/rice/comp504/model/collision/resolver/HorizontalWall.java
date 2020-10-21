package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * Horizontal Wall reflects the object's x direction velocity.
 */
public class HorizontalWall implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * Private constructor for singleton pattern.
     */
    private HorizontalWall() {
    }

    /**
     * Only makes 1 horizontalWall strategy.
     *
     * @return The horizontalWall strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new HorizontalWall();
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
        a.getStrategy().updateState(a);

        a.bounceOffHorizontalWall();
    }
}
