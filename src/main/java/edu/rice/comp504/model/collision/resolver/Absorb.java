package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Ball;

/**
 * Absorb strategy makes one object absorbs the other object's radius.
 */
public class Absorb implements ICollisionResolution {
    private static ICollisionResolution singleton;
    private final int minThreshold;
    private final int maxThreshold;

    /**
     * Private constructor for singleton pattern.
     */
    private Absorb() {
        this.minThreshold = 10;
        this.maxThreshold = 30;
    }

    /**
     * Only makes 1 Absorb strategy.
     *
     * @return The Absorb strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Absorb();
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
        a.bounceOff(b);
        Ball ballA = (Ball) a;
        Ball ballB = (Ball) b;
        int ra = ballA.getRadius();
        int rb = ballB.getRadius();
        if (ra > minThreshold && ra < maxThreshold && rb > minThreshold && rb < maxThreshold) {
            ra += rb * 0.5;
            rb *= 0.5;
        }
        ballA.setRadius(ra);
        ballB.setRadius(rb);
    }
}
