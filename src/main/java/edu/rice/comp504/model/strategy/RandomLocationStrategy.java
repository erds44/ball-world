package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Randomize the location every second.
 */
public class RandomLocationStrategy implements IUpdateStrategy {
    private int frequency;
    private Strategy name;

    /**
     * Public constructor.
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
     * Update the state of the object.
     *
     * @param context The object
     * @return if the strategy change's object's internal state randomly
     */
    @Override
    public boolean updateState(APaintObj context) {
        if (++this.frequency % 10 == 0) {
            int locX = DispatchAdapter.getRnd(60, DispatchAdapter.dims.x - 2 * 60);
            int locY = DispatchAdapter.getRnd(60, DispatchAdapter.dims.y - 2 * 60);
            context.setLocation(new Point2D.Double(locX, locY));
            context.incrementCount();
            return true;
        }
        return false;
    }
}
