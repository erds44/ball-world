package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

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
     * Update the state of the ball.
     *
     * @param context The ball.
     * @return if the strategy change's ball's internal state randomly.
     */
    boolean updateState(APaintObj context);
}

