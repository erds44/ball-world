package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * An interface for ball strategies that determine the ball behavior.
 */
public interface IUpdateStrategy {

    /**
     * The name of the strategy.
     *
     * @return strategy name
     */
    Strategy getName();

    /**
     * Update the state of the object.
     *
     * @param context The object
     * @return if the strategy change's object's internal state randomly
     */
    boolean updateState(APaintObj context);
}

