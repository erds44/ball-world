package edu.rice.comp504.model.collision.handler;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * ICollisionHandler determines what collision strategies to use.
 */
public interface ICollisionHandler {
    /**
     * Decision on how to handle two APaintObj.
     *
     * @param a first object in collision
     * @param b second object in collision
     */
    void handleCollision(APaintObj a, APaintObj b);
}
