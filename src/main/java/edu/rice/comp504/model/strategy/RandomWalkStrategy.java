package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

import java.awt.geom.Point2D;

public class RandomWalkStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    private RandomWalkStrategy() {
        this.name = Strategy.RANDOMWALKSTRATEGY;
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new RandomWalkStrategy();
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
        int rand = DispatchAdapter.getRnd(1, 4);
        switch (rand) {
            case 1:
                context.setVelocity(new Point2D.Double(-5, 0));
                break;
            case 2:
                context.setVelocity(new Point2D.Double(5, 0));
                break;
            case 3:
                context.setVelocity(new Point2D.Double(0, -5));
                break;
            case 4:
                context.setVelocity(new Point2D.Double(0, 5));
                break;
        }
        return true;
    }
}
