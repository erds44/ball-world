package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Ball;

import java.util.Arrays;

/**
 *  ChangeColor of both objects in a collision.
 */
public class ChangeColor implements ICollisionResolution {
    private static ICollisionResolution singleton;

    /**
     * private constructor for singleton pattern.
     */
    private ChangeColor() {

    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static ICollisionResolution makeOnly() {
        if (singleton == null) {
            singleton = new ChangeColor();
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
        changeColor((Ball) a);
        changeColor((Ball) b);
        a.bounceOff(b);
    }

    /**
     * Help method for change a ball's color.
     *
     * @param ball ball
     */
    private void changeColor(Ball ball) {
        int colorIndex = Arrays.asList(DispatchAdapter.availColors).indexOf(ball.getColor());
        colorIndex = ++colorIndex % DispatchAdapter.availColors.length;
        ball.setColor(DispatchAdapter.availColors[colorIndex]);
    }
}
