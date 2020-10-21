package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintobj.APaintObj;

import java.awt.geom.Point2D;

/**
 * A Ball with black color and 0 velocity or Fish with 0 velocity.
 */
public class NullStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    private NullStrategy() {
        this.name = Strategy.NULLSTRATEGY;
    }

    /**
     * Only makes 1 null strategy.
     *
     * @return The null strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new NullStrategy();
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
        if (context.getColor() != "black") {
            context.setColor("black");
        }
        if (context.getVelocity().x != 0 || context.getVelocity().y != 0) {
            context.setVelocity(new Point2D.Double(0, 0));
        }
        return false;
    }
}
