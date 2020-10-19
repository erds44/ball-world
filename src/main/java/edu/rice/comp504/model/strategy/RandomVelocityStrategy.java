package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Add gravity for a ball.
 */
public class RandomVelocityStrategy implements IUpdateStrategy {
    private Strategy name;
    private int frequency;

    /**
     * private constructor for singleton pattern.
     */
    public RandomVelocityStrategy() {
        this.name = Strategy.RANDOMVELOCITYSTRATEGY;
        this.frequency = 0;
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
        if(++this.frequency % 10 == 0) {
            double velX = Math.signum(context.getVelocity().x) * DispatchAdapter.getRnd(1, 10);
            double velY = Math.signum(context.getVelocity().y) * DispatchAdapter.getRnd(1, 10);
            context.setVelocity(new Point2D.Double(velX, velY));

            return true;
        }
        return false;
    }

}
