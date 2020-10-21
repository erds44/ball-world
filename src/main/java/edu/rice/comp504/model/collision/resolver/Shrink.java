package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

public class Shrink implements ICollisionResolution {
    private static ICollisionResolution singleton;
    private int shrink;
    private int threshold;

    private Shrink() {
        this.shrink = 2;
        this.threshold = 15;
    }

    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Shrink();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
       shrinkSize(a);
       shrinkSize(b);
       a.bounceOff(b);
    }

    private void shrinkSize(APaintObj a) {
        Ball ball = (Ball) a;
        int ra = ball.getRadius();
        if (ra >= (this.threshold + this.shrink)) {
            ball.setRadius(ra - this.shrink);
        }
    }
}
