package edu.rice.comp504.model.collision.handler;

import edu.rice.comp504.model.collision.resolver.*;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.strategy.Strategy;

import java.util.HashMap;
import java.util.Map;


public class FishCollisionHandler implements ICollisionHandler {
    private static FishCollisionHandler singleton;

    private FishCollisionHandler() {
    }

    public static FishCollisionHandler makeOnly() {
        if (singleton == null) {
            singleton = new FishCollisionHandler();
        }
        return singleton;
    }

    @Override
    public void handleCollision(APaintObj a, APaintObj b) {
       // do nothing
    }

}
