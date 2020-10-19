package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Change the color of a ball per update.
 */
public class RandomLocationStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private int frequency;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    public RandomLocationStrategy() {
        this.frequency = 0;
        this.name = Strategy.RANDOMLOCATIONSTRATEGY;
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
            int locX = DispatchAdapter.getRnd(60, DispatchAdapter.dims.x - 2 * 60);
            int locY = DispatchAdapter.getRnd(60, DispatchAdapter.dims.y - 2 * 60);
            context.setLocation(new Point2D.Double(locX, locY));
            context.incrementCount();
            return true;
        }
        return false;
    }
}
