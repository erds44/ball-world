package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

public class Absorb implements ICollisionResolution {
    private static ICollisionResolution singleton;
    private int minThreshold;
    private int maxThreshold;

    private Absorb() {
        this.minThreshold = 10;
        this.maxThreshold = 30;
    }

    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Absorb();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        a.bounceOff(b);
        Ball ballA = (Ball) a;
        Ball ballB = (Ball) b;
        int ra = ballA.getRadius();
        int rb = ballB.getRadius();
        if (ra > minThreshold && ra < maxThreshold && rb > minThreshold && rb < maxThreshold) {
            ra += rb * 0.1;
            rb *= 0.9;
        }
        ballA.setRadius(ra);
        ballB.setRadius(rb);
    }
}
