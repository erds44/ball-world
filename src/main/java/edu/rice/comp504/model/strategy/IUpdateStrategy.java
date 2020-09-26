package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.ball.Ball;

/**
 * An interface for ball strategies that determine the ball behavior.
 */
public interface IUpdateStrategy {
    /**
     * The name of the strategy.
     * @return strategy name
     */
    String getName();

    /**
     * Update the state of the ball.
     * @param context The ball.
     */
    void updateState(Ball context);
}
