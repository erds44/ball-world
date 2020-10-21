package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintobj.APaintObj;

import java.awt.geom.Point2D;

/**
 * Add up and down displacement for a horizontal moving object.
 */
public class ShakingStrategy implements IUpdateStrategy {
    private int dy;
    private Strategy name;

    /**
     * Public constructor.
     */
    public ShakingStrategy() {
        this.dy = 10;
        this.name = Strategy.SHAKINGSTRATEGY;
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
        context.setVelocity(new Point2D.Double(context.getVelocity().x, dy));
        dy *= -1;
        return true;
    }
}
