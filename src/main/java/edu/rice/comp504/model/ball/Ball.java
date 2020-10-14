package edu.rice.comp504.model.ball;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.strategy.IUpdateStrategy;
import edu.rice.comp504.model.strategy.RotatingStrategy;

import java.awt.*;

/**
 * The balls that will be drawn in the ball world.
 */
public class Ball {
    private static final double INFINITY = Double.POSITIVE_INFINITY;
    private Point loc;
    private int radius;
    private Point vel;
    private IUpdateStrategy strategy;
    private String color;
    private boolean switchable;
    private int id;
    private int count;  // number of collision such that an event can tell if there is any other collision happening before it.
    public double mass;

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
        this.count = 0;
        this.mass = 1;
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
            } else if (this.strategy.getName().equals("NullStrategy")) { // give some random velocity if switching from null
                this.setVelocity(new Point(DispatchAdapter.getRnd(10, 40), DispatchAdapter.getRnd(10, 40)));
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
        //this.setVelocity(new Point(dx, dy));
        return isCollided;
    }

    /**
     * Update ball location based on the current velocity.
     */
    public void updateLocation() {
        this.setLocation(new Point(this.getVelocity().x + this.getLocation().x, this.getVelocity().y + this.getLocation().y));
    }

    /**
     * Update ball location based on the current velocity.
     */
    public void updateLocation(double time) {
        double x = this.getLocation().x + this.getVelocity().x * time;
        double y = this.getLocation().y + this.getVelocity().y * time;

        this.setLocation(new Point((int) x, (int) y));
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
        detectCollision();
        int vx = this.getVelocity().x;
        this.setVelocity(new Point(-vx, this.getVelocity().y));
        this.count++;
    }

    /**
     * Updates the velocity of this particle upon collision with a horizontal wall.
     */
    public void bounceOffHorizontalWall() {
        detectCollision();
        int vy = this.getVelocity().y;
        this.setVelocity(new Point(this.getVelocity().x, -vy));
        this.count++;
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
     * @param b the other particle
     * @return the amount of time for this particle to collide with the specified particle, assuming no interening collisions;
     * {@code Double.POSITIVE_INFINITY} if the particles will not collide
     */
    public double timeToHit(Ball b) {
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
     * @param b the other particle
     */
    public void bounceOff(Ball b) {
//        this.updateLocation(time);
//        b.updateLocation(time);


        double dx = b.getLocation().x - this.getLocation().x;
        double dy = b.getLocation().y - this.getLocation().y;
        double dvx = b.getVelocity().x - this.getVelocity().x;
        double dvy = b.getVelocity().y - this.getVelocity().y;
        double dvdr = dx * dvx + dy * dvy;             // dv dot dr
        double dist = this.getRadius() + b.getRadius();   // distance between particle centers at collison

        // magnitude of normal force
        double magnitude = 2 * this.mass * b.mass * dvdr / ((this.mass + b.mass) * dist);

        // normal force, and in x and y directions
        double fx = magnitude * dx / dist;
        double fy = magnitude * dy / dist;

        // update velocities according to normal force
        double vx = this.getVelocity().x;
        double vy = this.getVelocity().y;
        double vbx = b.getVelocity().x;
        double vby = b.getVelocity().y;


        vx += fx / this.mass;
        vy += fy / this.mass;
        vbx -= fx / b.mass;
        vby -= fy / b.mass;

        this.setVelocity(new Point((int) vx, (int) vy));
        b.setVelocity(new Point((int) vbx, (int) vby));
//
//        this.setVelocity(new Point(-this.getVelocity().x, -this.getVelocity().y));
//        b.setVelocity(new Point(-b.getVelocity().x, -b.getVelocity().y));
//        // update collision counts
        this.incrementCount();
        b.incrementCount();
    }


}
