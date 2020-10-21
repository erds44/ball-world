package edu.rice.comp504.model.collision.resolver;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

import java.util.Arrays;

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

    @Override
    public void resolveCollision(APaintObj a, APaintObj b) {
       changeColor((Ball) a);
       changeColor((Ball) b);
        a.bounceOff(b);
    }

    private void changeColor(Ball ball){
        int colorIndex = Arrays.asList(DispatchAdapter.availColors).indexOf(ball.getColor());
        colorIndex = ++colorIndex % DispatchAdapter.availColors.length;
        ball.setColor(DispatchAdapter.availColors[colorIndex]);
    }
}
