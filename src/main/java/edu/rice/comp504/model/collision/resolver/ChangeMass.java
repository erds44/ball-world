package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

/**
 * ChangeMass randomizes the mass of the first object.
 */
public class ChangeMass implements ICollisionResolution {
    private static ICollisionResolution singleton;
    private int threshold;

    /**
     * private constructor for singleton pattern.
     */
    private ChangeMass() {
        this.threshold = 15;
    }

    /**
     * Only makes 1 changeMass strategy.
     *
     * @return The changeMass strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new ChangeMass();
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
        a.setMass(DispatchAdapter.getRnd(1, this.threshold));
    }
}
