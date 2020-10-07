package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.ball.Ball;

import java.awt.*;

/**
 * Horizontal strategy that makes a ball move horizontally.
 */
public class HorizontalStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;

    /**
     * Private Constructor for singleton pattern.
     */
    private HorizontalStrategy() {
        this.name = "HorizontalStrategy";
    }

    /**
     * Only makes 1 horizontal strategy.
     *
     * @return The horizontal strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new HorizontalStrategy();
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
    public void updateState(Ball context) {
        // By not setting velocity directly, ball can restore its velocity once switching to another strategy
        context.setLocation(new Point(context.getVelocity().x + context.getLocation().x, context.getLocation().y));
        context.detectCollision();
    }
}
