package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Fish;

import java.awt.geom.Point2D;

public class ReverseVelocityStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private int frequency;
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    public ReverseVelocityStrategy() {
        this.frequency = 0;
        this.name = Strategy.REVERSEVELOCITYSTRATEGY;
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
        if (++this.frequency % 50 == 0) {
            context.setVelocity(new Point2D.Double(-context.getVelocity().x, -context.getVelocity().y));
            if (context instanceof Fish) {
                adjustFishLoc((Fish) context);
            }
            context.incrementCount();
            return true;
        }
        return false;
    }

    public void adjustFishLoc(Fish context) {
        double locX = context.getLocation().x;
        double locY = context.getLocation().y;
        int xBound = DispatchAdapter.getCanvasDims().x;
        double width = context.getWidth();
        if (locX < width) {
            locX = width;
        } else if (locX > xBound - width) {
            locX = xBound - width;
        }
        context.setLocation(new Point2D.Double(locX, locY));
    }
}
