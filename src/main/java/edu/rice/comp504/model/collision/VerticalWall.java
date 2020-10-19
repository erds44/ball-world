package edu.rice.comp504.model.collision;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;
import edu.rice.comp504.model.strategy.Strategy;

public class VerticalWall implements ICollisionHandler {
    private static ICollisionHandler singleton;

    private VerticalWall() {
    }

    public static ICollisionHandler makeOnly() {
        if (singleton == null) {
            singleton = new VerticalWall();
        }
        return singleton;
    }

    @Override
    public void handleCollision(APaintObj a, APaintObj b) {
        a.getStrategy().updateState(a);
        a.bounceOffVerticalWall();
    }
}
