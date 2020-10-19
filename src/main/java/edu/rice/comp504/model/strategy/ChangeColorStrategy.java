package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;

import java.util.Arrays;

/**
 * Change the color of a ball per update.
 */
public class ChangeColorStrategy implements IUpdateStrategy {
    private int frequency;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    public ChangeColorStrategy() {
        this.frequency = 0;
        this.name = Strategy.CHANGECOLORSTRATEGY;
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
        if(++this.frequency % 10 == 0) {
            int colorIndex = Arrays.asList(DispatchAdapter.availColors).indexOf(context.getColor());
            colorIndex = ++colorIndex % DispatchAdapter.availColors.length;
            context.setColor(DispatchAdapter.availColors[colorIndex]);
        }
        return false;
    }
}
