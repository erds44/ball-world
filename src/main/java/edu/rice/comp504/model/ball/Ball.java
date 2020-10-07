package edu.rice.comp504.model.ball;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.cmd.SwitchCmd;
import edu.rice.comp504.model.cmd.UpdateCmd;
import edu.rice.comp504.model.strategy.HorizontalStrategy;
import edu.rice.comp504.model.strategy.IUpdateStrategy;
import edu.rice.comp504.model.strategy.RotatingStrategy;
import edu.rice.comp504.model.strategy.SuddenStopStrategy;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The balls that will be drawn in the ball world.
 */
public class Ball implements PropertyChangeListener {
    private Point loc;
    private int radius;
    private Point vel;
    private IUpdateStrategy strategy;
    private String color;
    private boolean switchable;
    private int id;

    /**
     * Constructor.
     *
     * @param loc        The location of the ball on the canvas
     * @param radius     The ball radius
     * @param vel        The ball velocity
     * @param color      The ball color
     * @param switchable Determines if the object can switch strategies
     * @param strategy   The object strategy
     */
    public Ball(Point loc, int radius, Point vel, String color, boolean switchable, IUpdateStrategy strategy, int id) {
        this.loc = loc;
        this.radius = radius;
        this.vel = vel;
        this.color = color;
        this.switchable = switchable;
        this.strategy = strategy;
        this.strategy.updateState(this);
        this.id = id;
    }

    /**
     * Get the ball color.
     *
     * @return ball color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * Set the ball color.
     *
     * @param c The new ball color
     */
    public void setColor(String c) {
        this.color = c;
    }


    /**
     * Get the ball location in the ball world.
     *
     * @return The ball location.
     */
    public Point getLocation() {
        return this.loc;
    }


    /**
     * Set the ball location in the canvas.  The origin (0,0) is the top left corner of the canvas.
     *
     * @param loc The ball coordinate.
     */
    public void setLocation(Point loc) {
        this.loc = loc;
    }

    /**
     * Get the velocity of the ball.
     *
     * @return The ball velocity
     */
    public Point getVelocity() {
        return this.vel;
    }

    /**
     * Set the velocity of the ball.
     *
     * @param vel The new ball velocity
     */
    public void setVelocity(Point vel) {
        this.vel = vel;
    }


    /**
     * Get the radius of the ball.
     *
     * @return The ball radius.
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Set the radius of the ball.
     *
     * @param r The ball radius.
     */
    public void setRadius(int r) {
        this.radius = r;
    }

    /**
     * Get the ball id.
     *
     * @return The ball id.
     */

    public int getID() {
        return this.id;
    }

    /**
     * Get the ball strategy.
     *
     * @return The ball strategy.
     */
    public IUpdateStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * Set the strategy if the ball can switch strategies.
     *
     * @param strategy The new strategy
     */
    public void setStrategy(IUpdateStrategy strategy) {
        if (switchable) {
            if (this.strategy.getName().equals("RotatingStrategy")) {
                ((RotatingStrategy) this.strategy).notRotating(this.getID());
            }
            this.strategy = strategy;
        }
    }

    /**
     * Detects collision between a ball and a wall in the ball world.  Change direction if ball collides with a wall.
     *
     * @return True if there was a collision and false otherwise.
     */
    public boolean detectCollision() {
        int xBound = DispatchAdapter.getCanvasDims().x;
        int yBound = DispatchAdapter.getCanvasDims().y;
        int x = this.getLocation().x;
        int y = this.getLocation().y;
        int dx = this.getVelocity().x;
        int dy = this.getVelocity().y;
        int r = this.getRadius();
        boolean isCollided = false;
        // ball has collision within right wall
        if (Math.abs(x - DispatchAdapter.dims.x) < r) {
            x = DispatchAdapter.dims.x - r;
            dx *= -1;
            isCollided = true;
            // ball entirely travel outside the right wall
        } else if (x > DispatchAdapter.dims.x + r) {
            x = 2 * DispatchAdapter.dims.x - x;
            dx *= -1;
            isCollided = true;
        }
        // ball has collision within left wall
        if (Math.abs(x) < r) {
            x = r;
            dx *= -1;
            isCollided = true;
        } else if (x < -r) {
            // ball entirely travel outside the right wall
            x = -x;
            dx *= -1;
            isCollided = true;
        }
        // similar case for y direction
        if (Math.abs(y - DispatchAdapter.dims.y) < r) {
            y = DispatchAdapter.dims.y - r;
            dy *= -1;
            isCollided = true;
            // ball entirely travel outside the right wall
        } else if (y > DispatchAdapter.dims.y + r) {
            y = 2 * DispatchAdapter.dims.y - y;
            dy *= -1;
            isCollided = true;
        }
        // ball has collision within left wall
        if (Math.abs(y) < r) {
            y = r;
            dy *= -1;
            isCollided = true;
        } else if (y < -r) {
            // ball entirely travel outside the left wall
            y = -y;
            dy *= -1;
            isCollided = true;
        }
        this.setLocation(new Point(x, y));
        this.setVelocity(new Point(dx, dy));
        return isCollided;
    }

    /**
     * Update ball location based on the current velocity.
     */
    public void updateLocation() {
        this.setLocation(new Point(this.getVelocity().x + this.getLocation().x, this.getVelocity().y + this.getLocation().y));
    }

    @Override
    /**
     *  Ball responds to the property change support indicating that the a property has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("theClock")) {
            ((UpdateCmd) evt.getNewValue()).execute(this);
        } else {
            ((SwitchCmd) evt.getNewValue()).execute(this);
        }
    }
}
