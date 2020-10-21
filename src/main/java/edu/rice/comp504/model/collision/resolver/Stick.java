package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

/**
 * Stick changes the velocity of first object to the same as the second object.
 */
public class Stick implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * private constructor for singleton pattern.
     */
    private Stick() {

    }

    /**
     * Only makes 1 stick strategy.
     *
     * @return The stick strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Stick();
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
        b.updateLocation(-0.05); // prevent direct overlapping
        a.setVelocity(b.getVelocity());
    }


}
