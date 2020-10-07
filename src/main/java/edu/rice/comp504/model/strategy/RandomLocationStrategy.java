package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;

import java.awt.*;

/**
 * Randomize a ball's location per update.
 */
public class RandomLocationStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;

    /**
     * private constructor for singleton pattern.
     */
    private RandomLocationStrategy() {
        this.name = "RandomLocationStrategy";
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new RandomLocationStrategy();
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
        int r = context.getRadius();
        int locX = DispatchAdapter.getRnd(r, DispatchAdapter.dims.x - 2 * r);
        int locY = DispatchAdapter.getRnd(r, DispatchAdapter.dims.y - 2 * r);
        context.setLocation(new Point(locX, locY));
    }
}
