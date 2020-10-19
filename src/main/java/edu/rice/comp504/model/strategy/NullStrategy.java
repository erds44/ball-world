package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * A Ball with black color and 0 velocity.
 */
public class NullStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    private NullStrategy() {
        this.name = Strategy.NULLSTRATEGY;
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new NullStrategy();
        }
        return singleton;
    }

    /**
     * The name of the strategy.
     *
     * @return strategy name
     */
    @Override
    public Strategy getName() {
        return this.name;
    }

    /**
     * Update the state of the ball.
     *
     * @param context The ball.
     */
    @Override
    public boolean updateState(APaintObj context) {
        if (context.getColor() != "black") {
            context.setColor("black");
        }
        if (context.getVelocity().x != 0 || context.getVelocity().y != 0) {
            context.setVelocity(new Point2D.Double(0, 0));
        }
        return false;
    }
}
