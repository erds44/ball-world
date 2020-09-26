package edu.rice.comp504.model;

import edu.rice.comp504.model.ball.Ball;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    private PropertyChangeSupport pcs;
    private static Point dims;

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Get the canvas dimensions.
     * @return The canvas dimensions
     */
    public static Point getCanvasDims() {
        return dims;
    }

    /**
     * Set the canvas dimensions.
     * @param dims The canvas width (x) and height (y).
     */
    public static void setCanvasDims(String dims) {

    }

    /**
     * Call the update method on all the ball observers to update their position in the ball world.
     */
    public PropertyChangeListener[] updateBallWorld() {
        // TODO: fill in
        return null;
    }


    /**
     * Generate a random number.
     * @param base  The mininum value
     * @param limit The maximum number from the base
     * @return A randomly generated number
     */
    private static int getRnd(int base, int limit) {
        return (int)Math.floor(Math.random() * limit + base);
    }

    /**
     * Load a ball into the paint world.
     * @param body  The REST request body has the strategy names.
     * @return A paint object
     */
    public Ball loadBall(String body, String type) {
        // TODO: fill in
        return null;
    }


    /**
     * Switch the strategy for some of the switcher balls
     * @param body  The REST request body containing the new strategy.
     */
    public PropertyChangeListener[] switchStrategy(String body) {
        // TODO: fill in
        return null;
    }

    /**
     * Add a ball that will listen for a property change (i.e. time elapsed)
     * @param pcl  The ball
     */
    private void addListener(PropertyChangeListener pcl) {
        // TODO: fill in
    }

    /**
     * Remove all balls from listening for property change events for a particular property.
     */
    public void removeListeners() {
        // TODO: fill in
    }

}
