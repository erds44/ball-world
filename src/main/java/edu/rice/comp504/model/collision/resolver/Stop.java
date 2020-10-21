package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

import java.awt.geom.Point2D;
import java.util.Arrays;

public class Stop implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * private constructor for singleton pattern.
     */
    private Stop() {

    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new Stop();
        }
        return singleton;
    }

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
        b.setVelocity(new Point2D.Double(-b.getVelocity().x, -b.getVelocity().y));
        a.setVelocity(new Point2D.Double(0, 0));
    }

}
