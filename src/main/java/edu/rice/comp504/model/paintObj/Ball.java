package edu.rice.comp504.model.paintObj;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.strategy.IUpdateStrategy;
import edu.rice.comp504.model.strategy.RotatingStrategy;
import edu.rice.comp504.model.strategy.Strategy;

import java.awt.geom.Point2D;

/**
 * The balls that will be drawn in the ball world.
 */
public class Ball implements APaintObj {
    public static final double INFINITY = Double.POSITIVE_INFINITY;
    private Object name;
    private Point2D.Double loc;
    private int radius;
    private Point2D.Double vel;
    private IUpdateStrategy strategy;
    private String color;
    private boolean switchable;
    private int id;
    private int count;  // number of collision such that an event can tell if there is any other collision happening before it.
    private double mass;

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
    public Ball(Point2D.Double loc, int radius, Point2D.Double vel, String color, boolean switchable, IUpdateStrategy strategy, int id) {
        this.loc = loc;
        this.radius = radius;
        this.vel = vel;
        this.color = color;
        this.switchable = switchable;
        this.strategy = strategy;
        this.strategy.updateState(this);
        this.id = id;
        this.count = 0;
        this.mass = radius * 0.1;
        this.name = Object.Ball;
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
    public Point2D.Double getLocation() {
        return this.loc;
    }


    /**
     * Set the ball location in the canvas.  The origin (0,0) is the top left corner of the canvas.
     *
     * @param loc The ball coordinate.
     */
    public void setLocation(Point2D.Double loc) {
        this.loc = loc;
    }

    /**
     * Get the velocity of the ball.
     *
     * @return The ball velocity
     */
    public Point2D.Double getVelocity() {
        return this.vel;
    }

    /**
     * Set the velocity of the ball.
     *
     * @param vel The new ball velocity
     */
    public void setVelocity(Point2D.Double vel) {
        this.vel = vel;
        this.incrementCount();
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
        this.incrementCount();
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
            if (this.strategy.getName() == Strategy.ROTATINGSTRATEGY) {
                ((RotatingStrategy) this.strategy).notRotating(this.getID());
            } else if (this.strategy.getName() == Strategy.NULLSTRATEGY) { // give some random velocity if switching from null
                this.setVelocity(new Point2D.Double(DispatchAdapter.getRnd(10, 10), DispatchAdapter.getRnd(10, 10)));
            }
            this.strategy = strategy;
        }
    }


    /**
     * Update ball location based on the current velocity.
     */
    public void updateLocation(double time) {
        if (this.strategy.getName() != Strategy.ROTATINGSTRATEGY && this.strategy.getName() != Strategy.RANDOMLOCATIONSTRATEGY) {
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
        int r = this.getRadius();
        if (vx > 0) {
            return (1d * DispatchAdapter.getCanvasDims().x - locX - r) / vx;
        } else if (vx < 0) {
            // notice vx is negative
            return (1d * r - locX) / vx;
        } else {
            return INFINITY;
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
        int r = this.getRadius();
        if (vy > 0) {
            return (1d * DispatchAdapter.getCanvasDims().y - locY - r) / vy;
        } else if (vy < 0) {
            return (1d * r - locY) / vy;
        } else {
            return INFINITY;
        }
    }

    /**
     * Updates the velocity of this particle upon collision with a vertical wall.
     */
    public void bounceOffVerticalWall() {
        double vx = this.getVelocity().x;
        this.setVelocity(new Point2D.Double(-vx, this.getVelocity().y));
    }

    /**
     * Updates the velocity of this particle upon collision with a horizontal wall.
     */
    public void bounceOffHorizontalWall() {
        double vy = this.getVelocity().y;
        this.setVelocity(new Point2D.Double(this.getVelocity().x, -vy));
    }

    /**
     * Returns the number of collisions involving this particle with
     * vertical walls, horizontal walls, or other balls.
     *
     * @return the number of collisions.
     */
    public int count() {
        return this.count;
    }

    public void incrementCount() {
        this.count++;
    }

    /**
     * Returns the amount of time for this particle to collide with the specified particle, assuming no interening collisions.
     *
     * @param obj the other particle
     * @return the amount of time for this particle to collide with the specified particle, assuming no interening collisions;
     * {@code Double.POSITIVE_INFINITY} if the particles will not collide
     */
    public double timeToHit(APaintObj obj) {
        Ball b = (Ball) obj;
        if (this == b) {
            return INFINITY;
        }
        double dx = b.getLocation().x - this.getLocation().x;
        double dy = b.getLocation().y - this.getLocation().y;
        double dvx = b.getVelocity().x - this.getVelocity().x;
        double dvy = b.getVelocity().y - this.getVelocity().y;
        double dvdr = dx * dvx + dy * dvy;
        if (dvdr > 0) {
            return INFINITY;
        }
        double dvdv = dvx * dvx + dvy * dvy;
        if (dvdv == 0) {
            return INFINITY;
        }
        double drdr = dx * dx + dy * dy;
        double sigma = this.radius + b.radius;
        double d = (dvdr * dvdr) - dvdv * (drdr - sigma * sigma);
        if (d < 0) {
            return INFINITY;
        }
        return -(dvdr + Math.sqrt(d)) / dvdv;
    }

    /**
     * Updates the velocities of this particle and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at this instant.
     *
     * @param obj the other particle
     */
    public void bounceOff(APaintObj obj) {
        Ball b = (Ball) obj;
        double dx = b.getLocation().x - this.getLocation().x;
        double dy = b.getLocation().y - this.getLocation().y;
        double dvx = b.getVelocity().x - this.getVelocity().x;
        double dvy = b.getVelocity().y - this.getVelocity().y;
        double dvdr = dx * dvx + dy * dvy;             // dv dot dr
        double dist = this.getRadius() + b.getRadius();   // distance between particle centers at collison
        double massA = this.getMass();
        double massB = b.getMass();

        // magnitude of normal force
        double magnitude = 2 * massA * massB * dvdr / ((massA + massB) * dist);

        // normal force, and in x and y directions
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;

        // update velocities according to normal force
        double vx = this.getVelocity().x;
        double vy = this.getVelocity().y;
        double vbx = b.getVelocity().x;
        double vby = b.getVelocity().y;


        vx += fx / massA;
        vy += fy / massA;
        vbx -= fx / massB;
        vby -= fy / massB;

        this.setVelocity(new Point2D.Double(vx, vy));
        b.setVelocity(new Point2D.Double(vbx, vby));
    }

    public void setMass(double mass) {
        this.mass = mass;

    }

    public double getMass() {
        return this.mass;
    }

    public Object getName(){
        return this.name;
    }


}
