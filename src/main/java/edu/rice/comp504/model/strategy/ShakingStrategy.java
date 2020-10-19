package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Add up and down displacement for a horizontal moving ball.
 */
public class ShakingStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private int dy;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    public ShakingStrategy() {
        this.dy = 10;
        this.name = Strategy.SHAKINGSTRATEGY;
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
        context.setVelocity(new Point2D.Double(context.getVelocity().x, dy));
        dy *= -1;
        return true;
    }
}
