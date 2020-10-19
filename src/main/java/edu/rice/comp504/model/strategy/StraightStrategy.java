package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Horizontal strategy that makes a ball move horizontally.
 */
public class StraightStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private Strategy name;

    /**
     * Private Constructor for singleton pattern.
     */
    private StraightStrategy() {
        this.name = Strategy.STRAIGHTSTRATEGY;
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
    public Strategy getName() {
        return this.name;
    }

    /**
     * Set the y velocity of a ball to 0 if applicable.
     *
     * @param context The ball.
     */
    @Override
    public boolean updateState(APaintObj context) {
        Boolean b = context.getVelocity().x == context.getVelocity().y;
        if (!b) {
            context.setVelocity(new Point2D.Double(context.getVelocity().x, context.getVelocity().y));
        }
        return !b;
    }
}
