package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.ball.Ball;

import java.awt.*;

/**
 * Horizontal strategy that makes a ball move horizontally.
 */
public class StraightStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;

    /**
     * Private Constructor for singleton pattern.
     */
    private StraightStrategy() {
        this.name = "StraightStrategy";
    }

    /**
     * Only makes 1 horizontal strategy.
     *
     * @return The horizontal strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new StraightStrategy();
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
     * Set the y velocity of a ball to 0 if applicable.
     *
     * @param context The ball.
     */
    @Override
    public boolean updateState(Ball context) {
        Boolean b = context.getVelocity().x == context.getVelocity().y;
        if (!b) {
            context.setVelocity(new Point(context.getVelocity().x, context.getVelocity().y));
        }
        return b;
    }
}
