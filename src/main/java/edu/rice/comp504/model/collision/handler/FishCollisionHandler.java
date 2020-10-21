package edu.rice.comp504.model.collision.handler;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * The FishCollisionHandler makes decisions on how to handle collision involving fish.
 */
public class FishCollisionHandler implements ICollisionHandler {
    private static FishCollisionHandler singleton;

    /**
     * Private constructor for singleton pattern.
     */
    private FishCollisionHandler() {
    }

    /**
     * Only makes 1 FishCollisionHandler.
     *
     * @return The FishCollisionHandler
     */
    public static FishCollisionHandler makeOnly() {
        if (singleton == null) {
            singleton = new FishCollisionHandler();
        }
        return singleton;
    }

    /**
     * Decision on how to handle two APaintObj.
     *
     * @param a first object in collision
     * @param b second object in collision
     */
    @Override
    public void handleCollision(APaintObj a, APaintObj b) {
        // This method add the extensibility if fish collision is considered in the future
    }

}
