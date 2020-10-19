package edu.rice.comp504.model.collision;

import edu.rice.comp504.model.paintObj.APaintObj;

public class BounceOff implements ICollisionHandler {
    private static ICollisionHandler singleton;

    private BounceOff() {
    }

    public static ICollisionHandler makeOnly() {
        if (singleton == null) {
            singleton = new BounceOff();
        }
        return singleton;
    }

    @Override
    public void handleCollision(APaintObj a, APaintObj b) {
        a.bounceOff(b);
    }
}
