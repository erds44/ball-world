package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * BounceOff strategy bounces two objects based on the elastic collision.
 */
public class BounceOff implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * Private constructor for singleton pattern.
     */
    private BounceOff() {
    }

    /**
     * Only makes 1 BounceOff strategy.
     *
     * @return The BounceOff strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new BounceOff();
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
    }
}
