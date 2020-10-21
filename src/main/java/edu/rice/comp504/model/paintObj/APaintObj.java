package edu.rice.comp504.model.paintObj;

import edu.rice.comp504.model.strategy.IUpdateStrategy;


import java.awt.geom.Point2D;

public interface APaintObj {
    /**
     * Update ball location based on the current velocity.
     */
    void updateLocation(double time);

    /**
     * Get the ball location in the ball world.
     *
     * @return The ball location.
     */
    Point2D.Double getLocation();

    /**
     * Set the ball location in the canvas.  The origin (0,0) is the top left corner of the canvas.
     *
     * @param loc The ball coordinate.
     */
    void setLocation(Point2D.Double loc);

    /**
     * Get the velocity of the ball.
     *
     * @return The ball velocity
     */
    Point2D.Double getVelocity();

    /**
     * Set the velocity of the ball.
     *
     * @param vel The new ball velocity
     */
    void setVelocity(Point2D.Double vel);

    /**
     * Get the ball id.
     *
     * @return The ball id.
     */

    int getID();

    /**
     * Get the ball strategy.
     *
     * @return The ball strategy.
     */
    IUpdateStrategy getStrategy();

    /**
     * Set the strategy if the ball can switch strategies.
     *
     * @param strategy The new strategy
     */
    void setStrategy(IUpdateStrategy strategy);

    /**
     * Returns the amount of time to collide with a vertical
     * wall, assuming no intermediate collisions.
     *
     * @return the amount of time for this particle to collide with a vertical wall
     */
    double timeToHitVerticalWall();

    /**
     * Returns the amount of time for this particle to collide with a horizontal
     * wall, assuming no intermediate collisions.
     *
     * @return the amount of time for this particle to collide with a horizontal wall
     */
    double timeToHitHorizontalWall();

    /**
     * Updates the velocity of this particle upon collision with a vertical wall.
     */
    void bounceOffVerticalWall();

    /**
     * Updates the velocity of this particle upon collision with a horizontal wall.
     */
    void bounceOffHorizontalWall();

    /**
     * Returns the number of collisions involving this particle with
     * vertical walls, horizontal walls, or other balls.
     *
     * @return the number of collisions.
     */
    int count();

    void incrementCount();

    /**
     * Returns the amount of time for this particle to collide with the specified particle, assuming no interening collisions.
     *
     * @param b the other particle
     * @return the amount of time for this particle to collide with the specified particle, assuming no interening collisions;
     * {@code Double.POSITIVE_INFINITY} if the particles will not collide
     */
    double timeToHit(APaintObj b);

    /**
     * Updates the velocities of this particle and the specified particle according
     * to the laws of elastic collision. Assumes that the particles are colliding
     * at this instant.
     *
     * @param b the other particle
     */
    void bounceOff(APaintObj b);

    /**
     * Get the ball color.
     *
     * @return ball color
     */
    String getColor();

    /**
     * Set the ball color.
     *
     * @param c The new ball color
     */
    void setColor(String c);

    void setMass(double mass);

    double getMass();

    Object getName();


}
