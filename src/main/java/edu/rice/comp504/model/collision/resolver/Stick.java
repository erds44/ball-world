package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

public class Stick implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * private constructor for singleton pattern.
     */
    private Stick() {

    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Stick();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        b.updateLocation(-0.05);
        a.setVelocity(b.getVelocity());
    }


}
