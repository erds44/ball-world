package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

public class HorizontalWall implements ICollisionResolution {
    private static ICollisionResolution singleton;

    private HorizontalWall() {
    }

    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new HorizontalWall();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        a.getStrategy().updateState(a);

        a.bounceOffHorizontalWall();
    }
}
