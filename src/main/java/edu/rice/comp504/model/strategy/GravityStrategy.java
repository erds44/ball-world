package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.ball.Ball;

import java.awt.*;
import java.util.Arrays;

/**
 * Add gravity for a ball.
 */
public class GravityStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;
    private int gravity;

    /**
     * private constructor for singleton pattern.
     */
    private GravityStrategy() {
        this.name = "GravityStrategy";
        this.gravity = 10;
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new GravityStrategy();
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
    public boolean updateState(Ball context) {
        context.setVelocity(new Point(context.getVelocity().x, context.getVelocity().y + this.gravity));
        context.incrementCount();
        return true;
    }
}
