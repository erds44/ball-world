package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * Change the mass of a ball if there is a collision.
 */
public class ChangeMassStrategy implements IUpdateStrategy {
    private int frequency;
    private Strategy name;

    /**
     * public constructor.
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
     * Update the state of the object.
     *
     * @param context The object
     * @return if the strategy change's object's internal state randomly
     */
    @Override
    public boolean updateState(APaintObj context) {
        if (++this.frequency % 10 == 0) {
            context.setMass(DispatchAdapter.getRnd(1, 9));
        }
        return false;
    }
}
