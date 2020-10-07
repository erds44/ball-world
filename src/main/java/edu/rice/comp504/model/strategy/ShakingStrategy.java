package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.ball.Ball;

import java.awt.*;

/**
 * Add up and down displacement for a horizontal moving ball.
 */
public class ShakingStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;
    private int dy;

    /**
     * private constructor for singleton pattern.
     */
    private ShakingStrategy() {
        this.name = "ShakingStrategy";
        this.dy = 10;
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new ShakingStrategy();
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
        context.setVelocity(new Point(context.getVelocity().x, dy));
        dy *= -1;
        context.updateLocation();
        context.detectCollision();
    }
}
