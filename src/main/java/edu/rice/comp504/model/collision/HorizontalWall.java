package edu.rice.comp504.model.collision;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.strategy.Strategy;

public class HorizontalWall implements ICollisionHandler {
    private static ICollisionHandler singleton;

    private HorizontalWall() {
    }

    public static ICollisionHandler makeOnly() {
        if (singleton == null) {
            singleton = new HorizontalWall();
        }
        return singleton;
    }

    @Override
    public void handleCollision(APaintObj a, APaintObj b) {
        a.getStrategy().updateState(a);

        a.bounceOffHorizontalWall();
    }
}
