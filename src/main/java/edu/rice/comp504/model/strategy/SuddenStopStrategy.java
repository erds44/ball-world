package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;

import java.awt.*;

/**
 * Have a chance to stop moving per update.
 */
public class SuddenStopStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;
    private int threshold;

    /**
     * private constructor for singleton pattern.
     */
    private SuddenStopStrategy() {
        this.name = "SuddenStopStrategy";
        this.threshold = 1;
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new SuddenStopStrategy();
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
        if (DispatchAdapter.getRnd(0, 10) > this.threshold) {
            context.updateLocation();
            context.detectCollision();
        }
    }

}
