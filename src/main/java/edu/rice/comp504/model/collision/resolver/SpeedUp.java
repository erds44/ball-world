package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

public class SpeedUp implements ICollisionResolution {
    private static ICollisionResolution singleton;
    private int increment;
    private int threshold;
    /**
     * private constructor for singleton pattern.
     */
    private SpeedUp() {
        this.increment = 5;
        this.threshold = 30;
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new SpeedUp();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
       a.bounceOff(b);
       double velX = incrementVelocity(a.getVelocity().x);
       double velY = incrementVelocity(a.getVelocity().y);
       a.setVelocity(new Point2D.Double(velX, velY));
    }

    private double incrementVelocity(double vel){
        if(Math.abs(vel) < (this.threshold - this.increment)){
            vel += Math.signum(vel) * this.increment;
        }
        return vel;
    }
}
