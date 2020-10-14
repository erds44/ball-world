package edu.rice.comp504.model.strategy;


import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;

/**
 * Change Size strategy changes ball's radius per update randomly.
 */
public class ChangeSizeStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;

    /**
     * private constructor for singleton pattern.
     */
    private ChangeSizeStrategy() {
        this.name = "ChangeSizeStrategy";
    }

    /**
     * Only makes 1 change size strategy.
     *
     * @return The change size strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new ChangeSizeStrategy();
        }
        return singleton;
    }


    /**
     * The name of the strategy.
     *
     * @return strategy name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Update the state of the ball.
     *
     * @param context The ball.
     */
    @Override
    public boolean updateState(Ball context) {
        context.setRadius(DispatchAdapter.getRnd(15, 15));
        context.incrementCount();
        return true;
    }
}
