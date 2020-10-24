package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Fish;

import java.awt.geom.Point2D;

/**
 * Reverse the velocity of a object every 5 seconds.
 */
public class ReverseVelocityStrategy implements IUpdateStrategy {
    private int frequency;
    private Strategy name;

    /**
     * private constructor.
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
     * Update the state of the object.
     *
     * @param context The object
     * @return if the strategy change's object's internal state randomly
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

    /**
     * Adjust fish location in case fish is near the walls.
     *
     * @param context fish
     */

    private void adjustFishLoc(Fish context) {
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
