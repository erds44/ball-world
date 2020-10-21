package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * Vertical wall changes the object's vertical velocity.
 */
public class VerticalWall implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * Private constructor for singleton pattern.
     */
    private VerticalWall() {
    }

    /**
     * Only makes 1 vertical wall strategy.
     *
     * @return The vertical wall strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new VerticalWall();
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
        a.bounceOffVerticalWall();
    }
}
