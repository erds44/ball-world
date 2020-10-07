package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;

import java.util.Arrays;

/**
 * Change the color of a ball if there is a collision.
 */
public class ChangeColorAfterCollisionStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;

    /**
     * private constructor for singleton pattern.
     */
    private ChangeColorAfterCollisionStrategy() {
        this.name = "ChangeColorAfterCollisionStrategy";
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new ChangeColorAfterCollisionStrategy();
        }
        return singleton;
    }

    /**
     * The name of the strategy.
     *
     * @return strategy name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Update the state of the ball.
     *
     * @param context The ball.
     */
    @Override
    public void updateState(Ball context) {
        context.updateLocation();
        if (context.detectCollision()) {
            int colorIndex = Arrays.asList(DispatchAdapter.availColors).indexOf(context.getColor());
            colorIndex = ++colorIndex % DispatchAdapter.availColors.length;
            context.setColor(DispatchAdapter.availColors[colorIndex]);
        }
    }
}
