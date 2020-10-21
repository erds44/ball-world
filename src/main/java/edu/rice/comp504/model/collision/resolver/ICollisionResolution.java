package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

public interface ICollisionResolution {
    void resolveCollision(APaintObj a, APaintObj b);
}
