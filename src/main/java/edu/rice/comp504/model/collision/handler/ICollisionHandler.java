package edu.rice.comp504.model.collision.handler;

import edu.rice.comp504.model.paintObj.APaintObj;

public interface ICollisionHandler {
    void handleCollision(APaintObj a, APaintObj b);
}
