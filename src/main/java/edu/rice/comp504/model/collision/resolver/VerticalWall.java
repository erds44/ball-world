package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

public class VerticalWall implements ICollisionResolution {
    private static ICollisionResolution singleton;

    private VerticalWall() {
    }

    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new VerticalWall();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        a.getStrategy().updateState(a);
        a.bounceOffVerticalWall();
    }
}
