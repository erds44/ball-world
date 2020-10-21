package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

/**
 * ChangeColorOnRadius changes one object's color to the other one with the larger radius.
 */
public class ChangeColorOnRadius implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * Private constructor for singleton pattern.
     */
    private ChangeColorOnRadius() {
    }

    /**
     * Only makes 1 changeColor strategy.
     *
     * @return The changeColor strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new ChangeColorOnRadius();
        }
        return singleton;
    }

    /**
     * Implementation on how to handle two APaintObj.
     *
     * @param a first object in collision
     * @param b second object in collision
     */
    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        Ball ballA = (Ball) a;
        Ball ballB = (Ball) b;
        if (ballA.getRadius() >= ballB.getRadius()) {
            b.setColor(a.getColor());
        } else {
            a.setColor(b.getColor());
        }
        a.bounceOff(b);
    }
}
