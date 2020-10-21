package edu.rice.comp504.model.paintobj;

import edu.rice.comp504.model.strategy.IUpdateStrategy;


import java.awt.geom.Point2D;

/**
 * The APaintObj defines all the objects that can be painted on canvas.
 */
public interface APaintObj {
    /**
     * Get the object location in the object world.
     *
     * @return The object location.
     */
    Point2D.Double getLocation();

    /**
     * Set the object location in the canvas.  The origin (0,0) is the top left corner of the canvas.
     *
     * @param loc The object coordinate.
     */
    void setLocation(Point2D.Double loc);

    /**
     * Get the velocity of the object.
     *
     * @return The object velocity
     */
    Point2D.Double getVelocity();

    /**
     * Set the velocity of the object.
     *
     * @param vel The new object velocity
     */
    void setVelocity(Point2D.Double vel);

    /**
     * Get the object strategy.
     *
     * @return The object strategy.
     */
    IUpdateStrategy getStrategy();

    /**
     * Set the strategy if the object can switch strategies.
     *
     * @param strategy The new strategy
     */
    void setStrategy(IUpdateStrategy strategy);

    /**
     * Get the object color.
     *
     * @return object color
     */
    String getColor();

    /**
     * Set the object color.
     *
     * @param c The new object color
     */
    void setColor(String c);

    /**
     * Get the object mass.
     *
     * @return the object mass
     */
    double getMass();

    /**
     * Set the object mass.
     *
     * @param mass the mass
     */
    void setMass(double mass);

    /**
     * Get the object name.
     *
     * @return the object name
     */

    Object getName();


    /**
     * Update object location based on the current velocity.
     */
    void updateLocation(double time);


    /**
     * Get the object id.
     *
     * @return The object id.
     */

    int getID();

    /**
     * Returns the number of collisions involving this particle with
     * vertical walls, horizontal walls, or other objects.
     *
     * @return the number of collisions.
     */
    int count();

    /**
     * Increment the object count.
     */
    void incrementCount();

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


}
