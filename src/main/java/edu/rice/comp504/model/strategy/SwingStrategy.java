package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Fish;

/**
 * Fish can swing up and down.
 */
public class SwingStrategy implements IUpdateStrategy {
    private double rotateAngle;
    private Strategy name;
    private int frequency;

    /**
     * Public constructor.
     */
    public SwingStrategy() {
        this.rotateAngle = 20d / 180 * Math.PI;
        this.name = Strategy.SWINGSTRATEGY;
        this.frequency = 0;
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
        if (context instanceof Fish) {
            Fish fish = (Fish) context;
            if (++this.frequency % 2 == 0) {
                fish.setAngle(-1 * this.rotateAngle);
            } else {
                fish.setAngle(0);
            }
        }
        return false;
    }
}
