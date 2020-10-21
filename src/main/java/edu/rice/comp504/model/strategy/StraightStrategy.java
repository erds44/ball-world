package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintobj.APaintObj;

/**
 * Straight strategy that makes a object move straight.
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
     * Only makes 1 straight strategy.
     *
     * @return The straight strategy
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
     * Update the state of the object.
     *
     * @param context The object
     * @return if the strategy change's object's internal state randomly
     */
    @Override
    public boolean updateState(APaintObj context) {
        return false;
    }
}
