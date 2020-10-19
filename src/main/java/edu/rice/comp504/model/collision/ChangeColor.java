package edu.rice.comp504.model.collision;

import edu.rice.comp504.model.paintObj.APaintObj;

public class ChangeColor implements ICollisionHandler {
    private static ICollisionHandler singleton;

    /**
     * private constructor for singleton pattern.
     */
    private ChangeColor() {

    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionHandler makeOnly() {
        if (singleton == null) {
            singleton = new ChangeColor();
        }
        return singleton;
    }

    @Override
    public void handleCollision(APaintObj a, APaintObj b) {
        b.getStrategy().updateState(b);
        a.bounceOff(b);
    }
}
