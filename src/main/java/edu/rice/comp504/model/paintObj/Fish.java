package edu.rice.comp504.model.paintObj;

import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.strategy.IUpdateStrategy;
import edu.rice.comp504.model.strategy.Strategy;

import java.awt.geom.Point2D;

public class Fish implements APaintObj {
    private String name = "Fish";
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
    private double angel;

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
        this.angel = 0;
    }


    /**
     * Update ball location based on the current velocity.
     */
    @Override
    public void updateLocation(double time) {
        if (this.strategy.getName() != Strategy.ROTATINGSTRATEGY && this.strategy.getName() != Strategy.RANDOMLOCATIONSTRATEGY) {
            double x = this.getLocation().x + this.getVelocity().x * time;
            double y = this.getLocation().y + this.getVelocity().y * time;
            this.setLocation(new Point2D.Double(x, y));
        }
    }

    /**
     * Get the ball location in the ball world.
     *
     * @return The ball location.
     */
    @Override
    public Point2D.Double getLocation() {
        return this.loc;
    }

    /**
     * Set the ball location in the canvas.  The origin (0,0) is the top left corner of the canvas.
     *
     * @param loc The ball coordinate.
     */
    @Override
    public void setLocation(Point2D.Double loc) {
        this.loc = loc;
    }

    /**
     * Get the velocity of the ball.
     *
     * @return The ball velocity
     */
    @Override
    public Point2D.Double getVelocity() {
        return this.vel;
    }

    /**
     * Set the velocity of the ball.
     *
     * @param vel The new ball velocity
     */
    @Override
    public void setVelocity(Point2D.Double vel) {
        if (Math.signum(this.vel.x) != Math.signum(vel.x) && Math.signum(vel.x) != 0) {
            this.scale = new Point2D.Double(this.scale.x * -1, this.scale.y);
        }
        incrementCount();
        this.vel = vel;
    }

    /**
     * Get the ball id.
     *
     * @return The ball id.
     */
    @Override
    public int getID() {
        return this.id;
    }

    /**
     * Get the ball strategy.
     *
     * @return The ball strategy.
     */
    @Override
    public IUpdateStrategy getStrategy() {
        return this.strategy;
    }

    /**
     * Set the strategy if the ball can switch strategies.
     *
     * @param strategy The new strategy
     */
    @Override
    public void setStrategy(IUpdateStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Returns the amount of time to collide with a vertical
     * wall, assuming no intermediate collisions.
     *
     * @return the amount of time for this particle to collide with a vertical wall
     */
    @Override
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
    @Override
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
    @Override
    public void bounceOffVerticalWall() {
        //this.scale = new Point2D.Double(-this.scale.x, this.scale.y);
        this.setVelocity(new Point2D.Double(-this.getVelocity().x, this.getVelocity().y));
        this.setLocation(new Point2D.Double(this.getLocation().x + Math.signum(this.getVelocity().x) * this.width, this.getLocation().y));
    }

    /**
     * Updates the velocity of this particle upon collision with a horizontal wall.
     */
    @Override
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
    @Override
    public int count() {
        return this.count;
    }

    @Override
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
    @Override
    public double timeToHit(APaintObj b) {
        return 0;
    }

    /**
     * Updates the velocities of this particle and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at this instant.
     *
     * @param b the other particle
     */
    @Override
    public void bounceOff(APaintObj b) {

    }

    /**
     * Get the ball color.
     *
     * @return ball color
     */
    @Override
    public String getColor() {
        return this.color;
    }

    /**
     * Set the ball color.
     *
     * @param c The new ball color
     */
    @Override
    public void setColor(String c) {
        this.color = c;
    }

    @Override
    public void setMass(double mass) {
        this.mass = mass;
    }

    @Override
    public double getMass() {
        return this.mass;
    }

    public void setScale(double scale) {
        double ratio = scale / 0.1;
        this.width = 60 * ratio;
        this.height = 50 * ratio;
        double scaleX = Math.signum(this.scale.x) * scale;
        this.scale = new Point2D.Double(scaleX, scale);
        incrementCount();
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getAngel() {
        return this.angel;
    }

    public void setAngel(double angel) {
        this.angel = angel;
    }
}
