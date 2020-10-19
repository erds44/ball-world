package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

/**
 * Change the color of a ball if there is a collision.
 */
public class ChangeMassStrategy implements IUpdateStrategy {
    private int frequency;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    public ChangeMassStrategy() {
        this.frequency = 0;
        this.name = Strategy.CHANGEMASSSTRATEGY;
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
        if (++this.frequency % 10 == 0) {
            context.setMass(DispatchAdapter.getRnd(1, 9));
        }
        return false;
    }
}
