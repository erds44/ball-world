package edu.rice.comp504.model;

import edu.rice.comp504.model.ball.Ball;
import edu.rice.comp504.model.cmd.SwitchCmd;
import edu.rice.comp504.model.strategy.*;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    public int time; // time unit is 0.1s
    private int ballID = 0;
    public static Point dims;
    public static String[] availColors = {"red", "blue", "green", "black", "purple", "orange", "gray", "brown"};
    private Map<Integer, Ball> balls = new HashMap<>();
    private Map<Integer, Ball> newBalls = new HashMap<>();
    public static Map<String, IUpdateStrategy> map = new HashMap<>();

    {
        map.put("StraightStrategy", StraightStrategy.makeStrategy());
        map.put("ChangeColorStrategy", ChangeColorStrategy.makeStrategy());
        map.put("ChangeSizeStrategy", ChangeSizeStrategy.makeStrategy());
        map.put("GravityStrategy", GravityStrategy.makeStrategy());
        map.put("ShakingStrategy", ShakingStrategy.makeStrategy());
        map.put("ChangeColorAfterCollisionStrategy", ChangeColorAfterCollisionStrategy.makeStrategy());
        map.put("NullStrategy", NullStrategy.makeStrategy());
        map.put("RotatingStrategy", RotatingStrategy.makeStrategy());
    }

    /**
     * Constructor call.
     */
    public DispatchAdapter() {
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
     * Load a ball into the paint world.
     *
     * @param body The REST request body has the strategy names.
     * @return A paint object
     */
    public Ball loadBall(String body, String switchable) {
        int r = getRnd(15, 15);
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
        this.newBalls.put(this.ballID, ball);
        return ball;
    }

    /**
     * Call the update method on all the ball observers to update their position in the ball world.
     */
    public Collection<Ball> updateBallWorld() {
        this.time++;
        Collection<Ball> balls = this.balls.values();
        for (Ball ball : this.newBalls.values()) {
            CollisionSystem.makeOnly().predict(balls, ball, this.time);
        }
        this.balls.putAll(this.newBalls);
        this.newBalls.clear();
        CollisionSystem.makeOnly().update(balls, this.time);
        return balls;
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
     * Switch the strategy for some of the switcher balls.
     *
     * @param id       Ball id
     * @param strategy strategy to change for the ball
     */
    public Collection<Ball> switchStrategy(String id, String strategy) {
        IUpdateStrategy s = this.map.get(strategy);
        if (s == null) s = NullStrategy.makeStrategy();
        Ball ball = this.balls.get(Integer.parseInt(id));
        new SwitchCmd(s).execute(ball);
        CollisionSystem.makeOnly().predict(this.balls.values(), ball, this.time);
        return this.balls.values();
    }

    /**
     * Find the ball given the ball id.
     *
     * @param id The REST request body of ball id
     * @return the ball with corresponding id
     */
    public Ball findStrategy(String id) {
        return this.balls.get(Integer.parseInt(id));
    }


    /**
     * Remove all balls from listening for property change events for a particular property.
     */
    public void RemoveBalls(int id) {
        if (id == -1) {
            this.newBalls.clear();
            this.balls.clear();
            CollisionSystem.makeOnly().Clear();
            this.ballID = 0;
        } else {
            Ball ball = this.balls.remove(id);
            ball.incrementCount(); // invalidate time event
        }
    }

}
