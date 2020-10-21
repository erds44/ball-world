package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

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
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new ChangeMass();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
       a.bounceOff(b);
       a.setMass(DispatchAdapter.getRnd(1, this.threshold));
    }
}
