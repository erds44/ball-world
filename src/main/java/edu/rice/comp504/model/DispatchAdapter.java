package edu.rice.comp504.model;

import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Ball;
import edu.rice.comp504.model.cmd.SwitchCmd;
import edu.rice.comp504.model.collision.CollisionSystem;
import edu.rice.comp504.model.paintobj.Fish;
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
    private Map<String, String> parseMap = new HashMap<>();
    private Map<Integer, APaintObj> objs = new HashMap<>();
    private Map<Integer, APaintObj> newObjs = new HashMap<>();
    public static Map<String, IUpdateStrategy> map = new HashMap<>();

    {
        map.put("StraightStrategy", StraightStrategy.makeStrategy());
        map.put("NullStrategy", NullStrategy.makeStrategy());
        map.put("RotatingStrategy", RotatingStrategy.makeStrategy());
        map.put("RandomWalkStrategy", RandomWalkStrategy.makeStrategy());
        map.put("SuddenStopStrategy", SuddenStopStrategy.makeStrategy());
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
     * @param body the request body
     */
    public void setCanvasDims(String body) {
        parseBody(body);
        String height = this.parseMap.get("height");
        String width = this.parseMap.get("width");
        DispatchAdapter.dims = new Point(Integer.parseInt(height), Integer.parseInt(width));
    }

    /**
     * Load the APaintObj.
     *
     * @param body the request body
     * @return the object
     */

    public APaintObj loadAPaintObj(String body) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        parseBody(body);
        String strategy = this.parseMap.get("strategy");
        boolean b = Boolean.parseBoolean(this.parseMap.get("switchable"));
        IUpdateStrategy s = getStrategy(strategy);
        APaintObj obj;
        if (this.parseMap.get("object").equals("Fish")) {
            obj = loadFish(b, s);
        } else {
            obj = loadBall(b, s);
        }
        this.newObjs.put(obj.getID(), obj);
        return obj;
    }

    /**
     * Help method load a fish.
     *
     * @param switchable is switchable
     * @param strategy   strategy
     * @return a fish
     */

    private APaintObj loadFish(boolean switchable, IUpdateStrategy strategy) {
        int locX = getRnd(60, DispatchAdapter.dims.x - 2 * 60);
        int locY = getRnd(60, DispatchAdapter.dims.y - 2 * 60);
        int velX = getRnd(10, 10);
        int velY = getRnd(10, 10);
        return new Fish(new Point2D.Double(locX, locY), new Point2D.Double(velX, velY), switchable, strategy, ++this.objID);
    }

    /**
     * Help method load a ball.
     *
     * @param switchable is switchable
     * @param strategy   strategy
     * @return a ball
     */
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
     * Call the update method on objects to update their position in the object world.
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
     * Switch the strategy for some of the switcher balls/fish.
     */
    public APaintObj switchStrategy(String body) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        parseBody(body);
        String strategy = this.parseMap.get("strategy");
        String id = this.parseMap.get("id");
        IUpdateStrategy s = getStrategy(strategy);
        APaintObj obj = this.objs.get(Integer.parseInt(id));
        new SwitchCmd(s).execute(obj);
        CollisionSystem.makeOnly().predict(this.objs.values(), obj);
        return obj;
    }

    /**
     * Find the object given the object id.
     *
     * @param body the request body
     * @return the object with corresponding id
     */
    public APaintObj getObj(String body) {
        parseBody(body);
        String id = this.parseMap.get("id");
        return this.objs.get(Integer.parseInt(id));
    }

    /**
     * Remove all or particular object.
     *
     * @param body the request body
     */
    public void removeObject(String body) {
        parseBody(body);
        String num = this.parseMap.get("id");
        int id = Integer.parseInt(num);
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

    /**
     * Help method to parse the request body.
     *
     * @param body the request body
     */

    private void parseBody(String body) {
        this.parseMap.clear();
        String[] params = body.split("&");
        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            this.parseMap.put(name, value);
        }
    }

    /**
     * Test loading fish method.
     * @param strategy strategy
     * @param switchable isSwitchable
     * @param loc location of fish
     * @param vel velocity of fish
     */
    public void testLoadFish(String strategy, boolean switchable, Point2D.Double loc, Point2D.Double vel) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Fish fish = new Fish(loc, vel, switchable, getStrategy(strategy), ++this.objID);
        this.newObjs.put(fish.getID(), fish);
    }

    public Ball testLoadBall(String strategy, boolean switchable, Point2D.Double loc, Point2D.Double vel) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        int r = getRnd(15, 10);
        int colorIndex = getRnd(0, availColors.length);
        Ball ball = new Ball(loc, r, vel, availColors[colorIndex], switchable, getStrategy(strategy), ++this.objID);
        this.newObjs.put(ball.getID(), ball);
        return ball;
    }
    /**
     * Help method to get a strategy.
     *
     * @param strategy String name of a strategy
     * @return the expected strategy
     */

    private IUpdateStrategy getStrategy(String strategy) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        IUpdateStrategy s = map.get(strategy);
        if (s == null) {    // In case a strategy is not in the dictionary
            try {
                Class name = Class.forName("edu.rice.comp504.model.strategy." + strategy);
                Constructor c = name.getConstructor();
                s = (IUpdateStrategy) c.newInstance();
            } catch (ClassNotFoundException e) {
                s = NullStrategy.makeStrategy();
            }
        }
        return s;
    }

}
