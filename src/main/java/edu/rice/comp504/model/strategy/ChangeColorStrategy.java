package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;

import java.util.Arrays;

/**
 * Change the color of a ball per update.
 */
public class ChangeColorStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;


    /**
     * private constructor for singleton pattern.
     */
    private ChangeColorStrategy() {
        this.name = "ChangeColorStrategy";
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new ChangeColorStrategy();
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
        int colorIndex = Arrays.asList(DispatchAdapter.availColors).indexOf(context.getColor());
        colorIndex = ++colorIndex % DispatchAdapter.availColors.length;
        context.setColor(DispatchAdapter.availColors[colorIndex]);
        context.updateLocation();
        context.detectCollision();
    }
}
