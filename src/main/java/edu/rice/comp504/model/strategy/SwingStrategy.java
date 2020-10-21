package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Fish;

/**
 * Ball can rotate around a fixed point.
 */
public class SwingStrategy implements IUpdateStrategy {
    private double rotateAngle;
    private Strategy name;
    private int frequency;

    /**
     * private constructor for singleton pattern.
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
     * Update the state of the ball.
     *
     * @param context The ball.
     */
    @Override
    public boolean updateState(APaintObj context) {
        if (context instanceof Fish) {
            Fish fish = (Fish) context;
            if(++this.frequency % 2 == 0) {
                fish.setAngle(-1 * this.rotateAngle);
            }else{
                fish.setAngle(0);
            }
        }
        return false;
    }
}
