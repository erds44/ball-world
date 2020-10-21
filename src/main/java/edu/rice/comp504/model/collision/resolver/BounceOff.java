package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

public class BounceOff implements ICollisionResolution {
    private static ICollisionResolution singleton;

    private BounceOff() {
    }

    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new BounceOff();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        a.bounceOff(b);
    }
}
