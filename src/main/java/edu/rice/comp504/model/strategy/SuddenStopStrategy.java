package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Fish;

import java.awt.geom.Point2D;

/**
 * Ball can rotate around a fixed point.
 */
public class SuddenStopStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private Strategy name;
    private int frequency;

    private SuddenStopStrategy() {
        this.name = Strategy.SUDDENSTOPSTRATEGY;
    }

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
     * Update the state of the ball.
     *
     * @param context The ball.
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
