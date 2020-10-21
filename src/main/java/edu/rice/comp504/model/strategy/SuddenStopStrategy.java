package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Fish;

/**
 * Object has a chance to stop moving.
 */
public class SuddenStopStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private Strategy name;

    /**
     * Private constructor for singleton pattern.
     */

    private SuddenStopStrategy() {
        this.name = Strategy.SUDDENSTOPSTRATEGY;
    }

    /**
     * Only makes 1 suddenStop strategy.
     *
     * @return The suddenStop strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new SuddenStopStrategy();
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
        if (DispatchAdapter.getRnd(1, 10) < 4) {
            ((Fish) context).setStop(true);
        } else {
            ((Fish) context).setStop(false);
        }
        context.incrementCount();
        return true;
    }
}
