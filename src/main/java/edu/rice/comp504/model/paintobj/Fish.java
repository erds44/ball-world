package edu.rice.comp504.model.paintobj;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.strategy.IUpdateStrategy;
import edu.rice.comp504.model.strategy.RotatingStrategy;
import edu.rice.comp504.model.strategy.Strategy;

import java.awt.geom.Point2D;

/**
 * The fish that will be drawn in the object world.
 */
public class Fish implements APaintObj {
    private Object name;
    private Point2D.Double loc;
    private Point2D.Double vel;
    private IUpdateStrategy strategy;
    private boolean switchable;
    private int id;
    private int count; // number of collision such that an event can tell if there is any other collision happening before it.
    private Point2D.Double scale;
    private double width;
    private double height;
    private String color;
    private double mass;
    private double angle;
    private boolean isStop;
    public static String URL = "https://freesvg.org/img/CartoonFish.png";

    /**
     * Constructor.
     *
     * @param loc        The location of fish
     * @param vel        The fish velocity
     * @param switchable Is the fish switchable
     * @param strategy   The fish strategy
     * @param id         The fish id
     */
    public Fish(Point2D.Double loc, Point2D.Double vel, boolean switchable, IUpdateStrategy strategy, int id) {
        this.loc = loc;
        this.vel = vel;
        this.strategy = strategy;
        this.switchable = switchable;
        this.id = id;
        this.count = 0;
        double scaleX = 0.1;
        double scaleY = 0.1;
        if (vel.getX() >= 0) {
            scaleX *= -1;
        }
        this.scale = new Point2D.Double(scaleX, scaleY);
        this.width = 60;
        this.height = 50;
        this.color = "black";
        this.angle = 0;
        this.isStop = false;
        this.strategy.updateState(this);
        this.name = Object.Fish;
    }

    /**
     * Get the fish color.
     *
     * @return fish color
     */

    public String getColor() {
        return this.color;
    }

    /**
     * Set the fish color.
     *
     * @param c The new fish color
     */

    public void setColor(String c) {
        this.color = c;
    }

    /**
     * Set the object mass.
     *
     * @param mass the mass
     */

    public void setMass(double mass) {
        this.mass = mass;
    }

    /**
     * Get the object mass.
     *
     * @return the object mass
     */
    public double getMass() {
        return this.mass;
    }

    /**
     * Set the object scale.
     */

    public void setScale(double scale) {
        double ratio = scale / 0.1;
        this.width = 60 * ratio;
        this.height = 50 * ratio;
        double scaleX = Math.signum(this.scale.x) * scale;
        this.scale = new Point2D.Double(scaleX, scale);
        incrementCount();
    }

    /**
     * Get the object scale.
     *
     * @return object scale
     */
    public double getScale() {
        return this.scale.x;
    }

    /**
     * Get the object name.
     *
     * @return object name
     */
    public Object getName() {
        return this.name;
    }

    /**
     * Get the object width.
     *
     * @return object width
     */
    public double getWidth() {
        return this.width;
    }

    /**
     * Get the object height.
     *
     * @return object height
     */
    public double getHeight() {
        return this.height;
    }

    /**
     * Get the object angle.
     *
     * @return object angle
     */
    public double getAngle() {
        return this.angle;
    }

    /**
     * Set the object angle.
     *
     * @param angle object angle
     */

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Set is object stop.
     *
     * @param stop isStop
     */

    public void setStop(boolean stop) {
        this.isStop = stop;
    }

    /**
     * Get the fish id.
     *
     * @return The fish id.
     */

    public int getID() {
        return this.id;
    }

    /**
     * Get the fish strategy.
     *
     * @return The fish strategy.
     */

    public IUpdateStrategy getStrategy() {
        return this.strategy;
    }


    /**
     * Set the strategy if the fish can switch strategies.
     *
     * @param strategy The new strategy
     */

    public void setStrategy(IUpdateStrategy strategy) {
        if (switchable) {
            switch (this.getStrategy().getName()) {
                case ROTATINGSTRATEGY:
                    ((RotatingStrategy) this.strategy).notRotating(this.getID());
                    this.angle = 0;
                    break;
                case SWINGSTRATEGY:
                    this.angle = 0;
                    break;
                case NULLSTRATEGY:
                    this.setVelocity(new Point2D.Double(DispatchAdapter.getRnd(-10, 20), DispatchAdapter.getRnd(-10, 20)));
                    break;
                case SUDDENSTOPSTRATEGY:
                    this.isStop = false;
                    break;
                default:
                    break;
            }
            this.strategy = strategy;
        }
    }

    /**
     * Get the fish location in the fish world.
     *
     * @return The fish location.
     */
    public Point2D.Double getLocation() {
        return this.loc;
    }

    /**
     * Set the fish location in the canvas.  The origin (0,0) is the top left corner of the canvas.
     *
     * @param loc The fish coordinate.
     */
    public void setLocation(Point2D.Double loc) {
        this.loc = loc;
    }

    /**
     * Get the velocity of the fish.
     *
     * @return The fish velocity
     */
    public Point2D.Double getVelocity() {
        return this.vel;
    }

    /**
     * Set the velocity of the fish.
     *
     * @param vel The new fish velocity
     */
    public void setVelocity(Point2D.Double vel) {
        if (vel.x > 0) {
            this.scale = new Point2D.Double(-Math.abs(this.scale.x), this.scale.y);
        } else if (vel.x < 0) {
            this.scale = new Point2D.Double(Math.abs(this.scale.x), this.scale.y);
        }
        this.vel = vel;
        incrementCount();
    }

    /**
     * Returns the number of collisions involving this particle with
     * vertical walls, horizontal walls, or other fishs.
     *
     * @return the number of collisions.
     */

    public int count() {
        return this.count;
    }

    /**
     * Increase the count.
     */
    public void incrementCount() {
        this.count++;
    }


    /**
     * Update fish location based on the current velocity.
     */
    public void updateLocation(double time) {
        if (this.strategy.getName() != Strategy.ROTATINGSTRATEGY && this.strategy.getName() != Strategy.RANDOMLOCATIONSTRATEGY && !isStop) {
            double x = this.getLocation().x + this.getVelocity().x * time;
            double y = this.getLocation().y + this.getVelocity().y * time;
            this.setLocation(new Point2D.Double(x, y));
        }
    }


    /**
     * Returns the amount of time to collide with a vertical
     * wall, assuming no intermediate collisions.
     *
     * @return the amount of time for this particle to collide with a vertical wall
     */

    public double timeToHitVerticalWall() {
        double vx = this.getVelocity().x;
        double locX = this.getLocation().x;
        if (vx > 0) {
            return (1d * DispatchAdapter.getCanvasDims().x - locX) / vx;
        } else if (vx < 0) {
            return -locX / vx;
        } else {
            return Ball.INFINITY;
        }
    }

    /**
     * Returns the amount of time for this particle to collide with a horizontal
     * wall, assuming no intermediate collisions.
     *
     * @return the amount of time for this particle to collide with a horizontal wall
     */

    public double timeToHitHorizontalWall() {
        double vy = this.getVelocity().y;
        double locY = this.getLocation().y;
        if (vy > 0) {
            return (1d * DispatchAdapter.getCanvasDims().y - locY - this.height) / vy;
        } else if (vy < 0) {
            return -locY / vy;
        } else {
            return Ball.INFINITY;
        }
    }

    /**
     * Updates the velocity of this particle upon collision with a vertical wall.
     */

    public void bounceOffVerticalWall() {
        this.setVelocity(new Point2D.Double(-this.getVelocity().x, this.getVelocity().y));
        this.setLocation(new Point2D.Double(this.getLocation().x + Math.signum(this.getVelocity().x) * this.width, this.getLocation().y));
    }

    /**
     * Updates the velocity of this particle upon collision with a horizontal wall.
     */

    public void bounceOffHorizontalWall() {
        double vy = this.getVelocity().y;
        this.setVelocity(new Point2D.Double(this.getVelocity().x, -vy));
    }


    /**
     * Returns the amount of time for this particle to collide with the specified particle, assuming no interening collisions.
     *
     * @param b the other particle
     * @return the amount of time for this particle to collide with the specified particle, assuming no interening collisions;
     * {@code Double.POSITIVE_INFINITY} if the particles will not collide
     */

    public double timeToHit(APaintObj b) {
        // add the extensibility if considering fish is collidable 
        return 0;
    }

    /**
     * Updates the velocities of this particle and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at this instant.
     *
     * @param b the other particle
     */

    public void bounceOff(APaintObj b) {
        // add the extensibility if considering fish is collidable 
    }


}
