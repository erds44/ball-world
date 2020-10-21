package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * ICollisionResolution tells specifically a collision strategy.
 */
public interface ICollisionResolution {
    /**
     * Implementation on how to handle two APaintObj.
     *
     * @param a first object in collision
     * @param b second object in collision
     */
    void resolveCollision(APaintObj a, APaintObj b);
}
