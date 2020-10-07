package edu.rice.comp504.model;

import edu.rice.comp504.model.ball.Ball;
import edu.rice.comp504.model.cmd.SwitchCmd;
import edu.rice.comp504.model.cmd.UpdateCmd;
import edu.rice.comp504.model.strategy.*;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    private PropertyChangeSupport pcs;
    public static Point dims;
    public static String[] availColors = {"red", "blue", "green", "black", "purple", "orange", "gray", "brown"};
    public static Map<String, IUpdateStrategy> map = new HashMap<>();

    {
        map.put("HorizontalStrategy", HorizontalStrategy.makeStrategy());
        map.put("ChangeColorStrategy", ChangeColorStrategy.makeStrategy());
        map.put("ChangeSizeStrategy", ChangeSizeStrategy.makeStrategy());
        map.put("GravityStrategy", GravityStrategy.makeStrategy());
        map.put("RandomLocationStrategy", RandomLocationStrategy.makeStrategy());
        map.put("ShakingStrategy", ShakingStrategy.makeStrategy());
        map.put("ChangeColorAfterCollisionStrategy", ChangeColorAfterCollisionStrategy.makeStrategy());
        map.put("SuddenStopStrategy", SuddenStopStrategy.makeStrategy());
        map.put("ChangeVelocityAfterCollisionStrategy", ChangeVelocityAfterCollisionStrategy.makeStrategy());
        map.put("NullStrategy", NullStrategy.makeStrategy());
        map.put("RotatingStrategy", RotatingStrategy.makeStrategy());
    }

    private int ballID = 0;

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Get the canvas dimensions.
     *
     * @return The canvas dimensions
     */
    public static Point getCanvasDims() {
        return dims;
    }

    /**
     * Set the canvas dimensions.
     *
     * @param dims The canvas width (x) and height (y).
     */
    public static void setCanvasDims(String dims) {
        try {
            String[] split = dims.split("\\s+"); // assume the string is in the format "800 600", so split by space
            DispatchAdapter.dims = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        } catch (RuntimeException e) {
            throw e;
        }
    }

    /**
     * Call the update method on all the ball observers to update their position in the ball world.
     */
    public PropertyChangeListener[] updateBallWorld() {
        PropertyChangeListener[] pcl = pcs.getPropertyChangeListeners();
        pcs.firePropertyChange("theClock", null, UpdateCmd.makeStrategy());
        return pcl;
    }


    /**
     * Generate a random number.
     *
     * @param base  The minimum value
     * @param limit The maximum number from the base
     * @return A randomly generated number
     */
    public static int getRnd(int base, int limit) {
        return (int) Math.floor(Math.random() * limit + base);
    }

    /**
     * Load a ball into the paint world.
     *
     * @param body The REST request body has the strategy names.
     * @return A paint object
     */
    public Ball loadBall(String body, String switchable) {
        int r = getRnd(10, 40);
        int locX = getRnd(r, DispatchAdapter.dims.x - 2 * r);
        int locY = getRnd(r, DispatchAdapter.dims.y - 2 * r);
        int velX = getRnd(10, 40);
        int velY = getRnd(10, 40);
        int colorIndex = getRnd(0, this.availColors.length);
        IUpdateStrategy s = this.map.get(body);
        // In case a strategy is not in the dictionary
        if (s == null) {
            s = NullStrategy.makeStrategy();
        }
        Ball ball = new Ball(new Point(locX, locY), r, new Point(velX, velY), this.availColors[colorIndex], Boolean.parseBoolean(switchable), s, ++this.ballID);
        addListener(ball, "theClock");
        addListener(ball, String.valueOf(this.ballID));
        return ball;
    }


    /**
     * Switch the strategy for some of the switcher balls.
     *
     * @param id       Ball id
     * @param strategy strategy to change for the ball
     */
    public PropertyChangeListener[] switchStrategy(String id, String strategy) {
        IUpdateStrategy s = this.map.get(strategy);
        if (s == null) {
            s = NullStrategy.makeStrategy();
        }
        pcs.firePropertyChange(id, null, new SwitchCmd(s));
        return this.pcs.getPropertyChangeListeners(id);
    }

    /**
     * Find the ball given the ball id.
     *
     * @param body The REST request body of ball id
     * @return the ball with corresponding id
     */
    public PropertyChangeListener findStrategy(String body) {
        return this.pcs.getPropertyChangeListeners(body)[0];
    }

    /**
     * Add a ball that will listen for a property change (i.e. time elapsed)
     *
     * @param pcl  The ball
     * @param type Trigger type
     */
    private void addListener(PropertyChangeListener pcl, String type) {
        this.pcs.addPropertyChangeListener(type, pcl);
    }

    /**
     * Remove all balls from listening for property change events for a particular property.
     */
    public void removeListeners() {
        for (PropertyChangeListener pcl : pcs.getPropertyChangeListeners()) {
            this.pcs.removePropertyChangeListener(pcl);
        }
        this.ballID = 0;
    }

}
