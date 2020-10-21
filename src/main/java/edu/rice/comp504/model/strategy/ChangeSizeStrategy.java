package edu.rice.comp504.model.strategy;


import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;
import edu.rice.comp504.model.paintObj.Fish;

/**
 * Change size of ball or fish randomly every 5 seconds.
 */
public class ChangeSizeStrategy implements IUpdateStrategy {
    private int frequency;
    private Strategy name;

    /**
     * Public constructor.
     */
    public ChangeSizeStrategy() {
        this.frequency = 0;
        this.name = Strategy.CHANGESIZESTRATEGY;
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
        if (++this.frequency % 50 == 0) {
            if (context instanceof Ball) {
                Ball ball = (Ball) context;
                ball.setRadius(DispatchAdapter.getRnd(15, 10));
                return true;
            } else {
                Fish fish = (Fish) context;
                fish.setScale(Math.random() * 0.05 + 0.1);
                return true;
            }
        }
        return false;
    }
}
