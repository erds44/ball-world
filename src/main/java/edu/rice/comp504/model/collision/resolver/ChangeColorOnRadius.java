package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

public class ChangeColorOnRadius implements ICollisionResolution {
    private static ICollisionResolution singleton;

    private ChangeColorOnRadius() {
    }

    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new ChangeColorOnRadius();
        }
        return singleton;
    }

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
