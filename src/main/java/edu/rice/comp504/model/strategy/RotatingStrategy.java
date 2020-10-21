package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;
import edu.rice.comp504.model.paintObj.Fish;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Object can rotate around a fixed point.
 */
public class RotatingStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private double rotateAngle;
    private Map<Integer, Point2D.Double> idToLoc = new HashMap<>();
    private Map<Integer, Boolean> idToBool = new HashMap<>();
    private Strategy name;

    /**
     * private constructor for singleton pattern.
     */
    private RotatingStrategy() {
        this.rotateAngle = 9.0 / 180 * Math.PI; // 9 degree rotation per 0.1 second
        this.name = Strategy.ROTATINGSTRATEGY;
    }

    /**
     * Only makes 1 rotating strategy.
     *
     * @return The rotating strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new RotatingStrategy();
        }
        return singleton;
    }

    /**
     * Adjust ball location such that rotating ball will not be drawn outside the canvas.
     *
     * @param context The ball
     * @return Fixed location
     */
    public Point2D.Double adjustBallLoc(Ball context) {
        double locX = context.getLocation().x;
        double locY = context.getLocation().y;
        int xBound = DispatchAdapter.getCanvasDims().x;
        int yBound = DispatchAdapter.getCanvasDims().y;
        int r = context.getRadius();
        if (locX < 3 * r) {
            locX = 3 * r;
        } else if (locX > xBound - 3 * r) {
            locX = xBound - 3 * r;
        }
        if (locY > yBound - 6 * r) {
            locY = yBound - 6 * r;
        }
        context.setLocation(new Point2D.Double(locX, locY));
        // assume fixed location is always two radius distance below the center point in Y direction
        return new Point2D.Double(locX, locY + 2 * r);
    }

    /**
     * Adjust fish location such that rotating ball will not be drawn outside the canvas.
     *
     * @param context The fish
     * @return Fixed location
     */
    public Point2D.Double adjustFishLoc(Fish context) {
        double locX = context.getLocation().x;
        double locY = context.getLocation().y;
        int xBound = DispatchAdapter.getCanvasDims().x;
        int yBound = DispatchAdapter.getCanvasDims().y;
        double height = context.getHeight();
        double width = context.getWidth();
        if (locX < 3 * width) {
            locX = 3 * width;
        } else if (locX > xBound - 3 * width) {
            locX = xBound - 3 * width;
        }
        if (locY > yBound - 6 * height) {
            locY = yBound - 6 * height;
        }
        context.setLocation(new Point2D.Double(locX, locY));
        // assume fixed location is always two height distance below the center point in Y direction
        return new Point2D.Double(locX, locY + 2 * height);
    }

    /**
     * Adjust the fish or ball location.
     *
     * @param context object
     * @return fixed position
     */

    public Point2D.Double adjustLoc(APaintObj context) {
        if (context instanceof Ball) {
            return adjustBallLoc((Ball) context);
        }
        return adjustFishLoc((Fish) context);
    }

    /**
     * Clear the dictionary if the canvas is reset.
     */
    public void clear() {
        this.idToLoc.clear();
        this.idToBool.clear();
    }

    /**
     * Set boolean flag to false if switching out rotating.
     */
    public void notRotating(int id) {
        this.idToBool.replace(id, false);
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

        if (!this.idToBool.containsKey(context.getID())) {
            this.idToLoc.put(context.getID(), this.adjustLoc(context));
            this.idToBool.put(context.getID(), true);
        } else if (!this.idToBool.get(context.getID())) {
            this.idToLoc.replace(context.getID(), this.adjustLoc(context));
            this.idToBool.replace(context.getID(), true);
        }

        double angle = this.rotateAngle;
        if (context instanceof Fish) {
            angle *= -Math.signum(((Fish) context).getScale());
        }
        Point2D.Double loc = this.idToLoc.get(context.getID());
        double dx = context.getLocation().x - loc.x;
        double dy = context.getLocation().y - loc.y;
        double rotatedX = Math.cos(angle) * (dx) - Math.sin(angle) * (dy) + loc.x;
        double rotatedY = Math.sin(angle) * (dx) + Math.cos(angle) * (dy) + loc.y;
        context.setLocation(new Point2D.Double(rotatedX, rotatedY));

        if (context instanceof Fish) {
            Fish fish = (Fish) context;
            fish.setAngle(fish.getAngle() + angle);
        }
        context.incrementCount();
        return false;
    }
}
