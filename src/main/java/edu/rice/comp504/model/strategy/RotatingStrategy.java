package edu.rice.comp504.model.strategy;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Ball can rotate around a fixed point.
 */
public class RotatingStrategy implements IUpdateStrategy {
    private static IUpdateStrategy singleton;
    private String name;
    private double rotateAngle;
    private Map<Integer, Point> idToLoc = new HashMap<>();
    private Map<Integer, Boolean> idToBool = new HashMap<>();

    /**
     * private constructor for singleton pattern.
     */
    private RotatingStrategy() {
        this.name = "RotatingStrategy";
        this.rotateAngle = 9.0 / 180 * Math.PI; // 9 degree rotation per 0.1 second
    }

    /**
     * Only makes 1 change color strategy.
     *
     * @return The change color strategy
     */
    public static IUpdateStrategy makeStrategy() {
        if (singleton == null) {
            singleton = new RotatingStrategy();
        }
        return singleton;
    }

    /**
     * adjust ball location such that rotating ball will not be drawn outside the canvas.
     *
     * @param context The ball
     * @return Fixed location
     */
    public Point adjustLoc(Ball context) {
        int locX = context.getLocation().x;
        int locY = context.getLocation().y;
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
        context.setLocation(new Point(locX, locY));
        // assume fixed location is always two radius distance below the center point in Y direction
        return new Point(locX, locY + 2 * r);
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
        if (!this.idToBool.containsKey(context.getID())) {
            this.idToLoc.put(context.getID(), this.adjustLoc(context));
            this.idToBool.put(context.getID(), true);
        } else if (!this.idToBool.get(context.getID())) {
            this.idToLoc.replace(context.getID(), this.adjustLoc(context));
            this.idToBool.replace(context.getID(), true);
        }
        Point loc = this.idToLoc.get(context.getID());
        int dx = context.getLocation().x - loc.x;
        int dy = context.getLocation().y - loc.y;
        double rotatedX = Math.cos(rotateAngle) * (dx) - Math.sin(rotateAngle) * (dy) + loc.x;
        double rotatedY = Math.sin(rotateAngle) * (dx) + Math.cos(rotateAngle) * (dy) + loc.y;
        context.setLocation(new Point((int) rotatedX, (int) rotatedY));
    }
}
