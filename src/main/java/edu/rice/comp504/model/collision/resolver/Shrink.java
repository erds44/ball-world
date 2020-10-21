package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Ball;

/**
 * Shrink decreases the radius of both objects with min threshold.
 */
public class Shrink implements ICollisionResolution {
    private static ICollisionResolution singleton;
    private int shrink;
    private int threshold;

    /**
     * Private constructor for singleton pattern.
     */
    private Shrink() {
        this.shrink = 2;
        this.threshold = 15;
    }

    /**
     * Only makes 1 BounceOff strategy.
     *
     * @return The BounceOff strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Shrink();
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
        shrinkSize(a);
        shrinkSize(b);
        a.bounceOff(b);
    }

    /**
     * Help method for shrink radius.
     *
     * @param a the object to be shrunk
     */
    private void shrinkSize(APaintObj a) {
        Ball ball = (Ball) a;
        int ra = ball.getRadius();
        if (ra >= (this.threshold + this.shrink)) {
            ball.setRadius(ra - this.shrink);
        }
    }
}
