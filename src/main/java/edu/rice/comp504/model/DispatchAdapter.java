package edu.rice.comp504.model;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;
import edu.rice.comp504.model.cmd.SwitchCmd;
import edu.rice.comp504.model.collision.CollisionSystem;
import edu.rice.comp504.model.paintObj.Fish;
import edu.rice.comp504.model.strategy.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This adapter interfaces with the view (paint objects) and the controller.
 */
public class DispatchAdapter {
    private int objID = 0;
    public static Point dims;
    public static String[] availColors = {"red", "blue", "green", "black", "purple", "orange", "gray", "brown"};
    private Map<Integer, APaintObj> objs = new HashMap<>();
    private Map<Integer, APaintObj> newObjs = new HashMap<>();
    public static Map<String, IUpdateStrategy> map = new HashMap<>();

    {
        map.put("StraightStrategy", StraightStrategy.makeStrategy());
        // map.put("ChangeColorStrategy", new ChangeColorStrategy());
        // map.put("ChangeSizeStrategy", new ChangeSizeStrategy());
        //map.put("GravityStrategy", GravityStrategy.makeStrategy());
        // map.put("ShakingStrategy", new ShakingStrategy());
        //map.put("ChangeColorAfterCollisionStrategy", new ChangeMassStrategy());
        map.put("NullStrategy", NullStrategy.makeStrategy());
        map.put("RotatingStrategy", RotatingStrategy.makeStrategy());
        //map.put("ReverseVelocityStrategy", new ReverseVelocityStrategy());
        //map.put("ChangeMassStrategy", new ChangeMassStrategy());
        // map.put("RandomLocationStrategy", new RandomLocationStrategy());
        map.put("RandomWalkStrategy", RandomWalkStrategy.makeStrategy());
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

    public APaintObj loadAPaintObj(String type, String switchable, String strategy) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        APaintObj obj;
        IUpdateStrategy s = this.map.get(strategy);
        boolean b = Boolean.parseBoolean(switchable);
        if (s == null) {    // In case a strategy is not in the dictionary
            try {
                Class name = Class.forName("edu.rice.comp504.model.strategy." + strategy);
                Constructor c = name.getConstructor();
                s = (IUpdateStrategy) c.newInstance();
            } catch (ClassNotFoundException e) {
                s = NullStrategy.makeStrategy();
            }
        }
        if (type.equals("Fish")) {
            obj = loadFish(b, s);
        } else {
            obj = loadBall(b, s);
        }
        this.newObjs.put(obj.getID(), obj);
        return obj;
    }

    private APaintObj loadFish(boolean switchable, IUpdateStrategy strategy) {
        int locX = getRnd(60, DispatchAdapter.dims.x - 2 * 60);
        int locY = getRnd(60, DispatchAdapter.dims.y - 2 * 60);
        int velX = getRnd(10, 10);
        int velY = getRnd(10, 10);
        return new Fish(new Point2D.Double(locX, locY), new Point2D.Double(velX, velY), switchable, strategy, ++this.objID);
    }

    private APaintObj loadBall(boolean switchable, IUpdateStrategy strategy) {
        int r = getRnd(15, 10);
        int locX = getRnd(r, DispatchAdapter.dims.x - 2 * r);
        int locY = getRnd(r, DispatchAdapter.dims.y - 2 * r);
        int velX = getRnd(10, 10);
        int velY = getRnd(10, 10);
        int colorIndex = getRnd(0, availColors.length);
        return new Ball(new Point2D.Double(locX, locY), r, new Point2D.Double(velX, velY), availColors[colorIndex], switchable, strategy, ++this.objID);
    }

    /**
     * Call the update method on all the ball observers to update their position in the ball world.
     */
    public Collection<APaintObj> updateBallWorld() {
        Collection<APaintObj> objs = this.objs.values();
        this.objs.putAll(this.newObjs);
        for (APaintObj obj : this.newObjs.values()) {
            CollisionSystem.makeOnly().predict(objs, obj);
        }
        this.newObjs.clear();
        CollisionSystem.makeOnly().update(objs);
        return objs;
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
    public APaintObj switchStrategy(String id, String strategy) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        IUpdateStrategy s = this.map.get(strategy);
        if (s == null) {    // In case a strategy is not in the dictionary
            try {
                Class name = Class.forName("edu.rice.comp504.model.strategy." + strategy);
                Constructor c = name.getConstructor();
                s = (IUpdateStrategy) c.newInstance();
            } catch (ClassNotFoundException e) {
                s = NullStrategy.makeStrategy();
            }
        }
        APaintObj obj = this.objs.get(Integer.parseInt(id));
        new SwitchCmd(s).execute(obj);
        CollisionSystem.makeOnly().predict(this.objs.values(), obj);
        return obj;
    }

    /**
     * Find the ball given the ball id.
     *
     * @param id The REST request body of ball id
     * @return the ball with corresponding id
     */
    public APaintObj getObj(String id) {
        return this.objs.get(Integer.parseInt(id));
    }


    /**
     * Remove all balls from listening for property change events for a particular property.
     */
    public void removeBalls(int id) {
        if (id == -1) {
            this.newObjs.clear();
            this.objs.clear();
            CollisionSystem.makeOnly().clear();
            ((RotatingStrategy) RotatingStrategy.makeStrategy()).clear();
            this.objID = 0;
        } else {
            APaintObj obj = this.objs.remove(id);
            obj.incrementCount(); // invalidate time event in PQ
        }
    }

}
