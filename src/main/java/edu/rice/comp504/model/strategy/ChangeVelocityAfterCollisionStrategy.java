package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;

import java.awt.*;

/**
 * Change the velocity of a ball after collision with a wall.
 */
public class ChangeVelocityAfterCollisionStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;

    /**
     * private constructor for singleton pattern.
     */
    private ChangeVelocityAfterCollisionStrategy() {
        this.name = "ChangeVelocityAfterCollisionStrategy";
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new ChangeVelocityAfterCollisionStrategy();
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
    public void updateState(Ball context) {
        context.updateLocation();
        if (context.detectCollision()) {
            int velX = DispatchAdapter.getRnd(10, 40) * Integer.signum(context.getVelocity().x);
            int velY = DispatchAdapter.getRnd(10, 40) * Integer.signum(context.getVelocity().y);
            context.setVelocity(new Point(velX, velY));
        }

    }
}
