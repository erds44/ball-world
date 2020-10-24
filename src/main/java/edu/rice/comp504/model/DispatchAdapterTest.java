package edu.rice.comp504.model;


import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Ball;
import edu.rice.comp504.model.paintobj.Fish;

import junit.framework.TestCase;

import java.awt.geom.Point2D;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.junit.Assert.assertNotEquals;

public class DispatchAdapterTest extends TestCase {
    private String[] fishCollidableStrategy = {"StraightStrategy", "ChangeSizeStrategy", "ShakingStrategy", "ReverseVelocityStrategy", "SwingStrategy"};
    private String[] ballStrategy = {"NullStrategy", "StraightStrategy", "RotatingStrategy", "RandomVelocityStrategy", "ChangeSizeStrategy", "ChangeMassStrategy", "ChangeColorStrategy", "ShakingStrategy", "ReverseVelocityStrategy", "RandomLocationStrategy", "RandomWalkStrategy"};
    private String[] fishStrategy = {"NullStrategy", "StraightStrategy", "RotatingStrategy", "RandomVelocityStrategy", "ChangeSizeStrategy", "ShakingStrategy", "ReverseVelocityStrategy", "RandomLocationStrategy", "RandomWalkStrategy", "SwingStrategy", "SuddenStopStrategy"};
    private boolean[] switchable = {true, false};
    private Point2D.Double[] vel = {new Point2D.Double(20, 20), new Point2D.Double(-20, 20)};
    private Point2D.Double[] fishLoc = {new Point2D.Double(790, 100), new Point2D.Double(10, 100)};
    private Point2D.Double[] ballVel = {new Point2D.Double(20, 0), new Point2D.Double(-20, 0)};
    private Point2D.Double[] ballLoc = {new Point2D.Double(380, 100), new Point2D.Double(420, 100)};
    private double[] fishScale = {0.1, -0.1};
    private ArrayList<Ball> balls = new ArrayList<>();
    private DispatchAdapter da = new DispatchAdapter();

    {
        da.setCanvasDims("height=800&width=800");
    }

    /**
     * Test fish properly change direction when hitting a wall.
     */
    public void testFishCollision() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (int i = 0; i < 2; i++) {
            Point2D.Double velocity = this.vel[i];
            Point2D.Double location = this.fishLoc[i];
            for (boolean b : this.switchable) {
                for (String s : this.fishCollidableStrategy) {
                    da.testLoadFish(s, b, location, velocity);
                    da.updateBallWorld();
                    APaintObj fish = da.getObj("id=1");
                    assertEquals("Test fish velocity", -velocity.x, fish.getVelocity().x);
                    assertEquals("Test fish picture direction", this.fishScale[i], ((Fish) fish).getScale());
                    da.removeObject("id=-1");
                }
            }
        }
    }

    /**
     * Help method to load two balls.
     *
     * @param ballA ballA strategy
     * @param ballB ballB strategy
     */
    private void loadBalls(String ballA, String ballB) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        da.removeObject("id=-1");
        this.balls.clear();
        this.balls.add(da.testLoadBall(ballA, true, this.ballLoc[0], this.ballVel[0]));
        this.balls.add(da.testLoadBall(ballB, true, this.ballLoc[1], this.ballVel[1]));
    }

    /**
     * Test Absorb collision strategy.
     */

    public void testBallAbsorbStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("ChangeMassStrategy", "ChangeSizeStrategy");
        int rA = this.balls.get(0).getRadius() + (int) (this.balls.get(1).getRadius() * 0.5);
        int rB = (int) (this.balls.get(1).getRadius() * 0.5);
        da.updateBallWorld();
        assertEquals("Test first ball radius", rA, this.balls.get(0).getRadius());
        assertEquals("Test second ball radius", rB, this.balls.get(1).getRadius());
    }

    /**
     * Test changeColor collision strategy.
     */

    public void testBallChangeColorStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("StraightStrategy", "StraightStrategy");
        String colorA = this.balls.get(0).getColor();
        String colorB = this.balls.get(1).getColor();
        da.updateBallWorld();
        assertNotEquals("Test first ball color", this.balls.get(0).getColor(), colorA);
        assertNotEquals("Test second ball color", this.balls.get(1).getColor(), colorB);

    }

    /**
     * Test changeColorOnRadius strategy.
     */
    public void testBallChangeColorOnRadiusStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("StraightStrategy", "ReverseVelocityStrategy");
        String colorA = this.balls.get(0).getColor();
        String colorB = this.balls.get(1).getColor();
        if (this.balls.get(0).getRadius() >= this.balls.get(1).getRadius()) {
            colorB = colorA;
        } else {
            colorA = colorB;
        }
        da.updateBallWorld();
        assertEquals("Test first ball color", this.balls.get(0).getColor(), colorA);
        assertEquals("Test second ball color", this.balls.get(1).getColor(), colorB);

    }

    /**
     * Test changeMass Strategy.
     */
    public void testBallChangeMassStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("ChangeSizeStrategy", "RandomVelocityStrategy");
        double mass = this.balls.get(0).getMass();
        da.updateBallWorld();
        assertNotEquals("Test ball mass", mass, this.balls.get(0).getMass());
    }

    /**
     * Test Shrink Strategy.
     */

    public void testBallShrinkStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("StraightStrategy", "ChangeColorStrategy");
        int rA = this.balls.get(0).getRadius() - 2;
        int rB = this.balls.get(1).getRadius() - 2;
        da.updateBallWorld();
        assertEquals("Test first ball radius", rA, this.balls.get(0).getRadius());
        assertEquals("Test second ball radius", rB, this.balls.get(1).getRadius());
    }

    /**
     * Test Slow Down Strategy.
     */
    public void testBallSlowDownStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("StraightStrategy", "ChangeSizeStrategy");
        da.updateBallWorld();
        double vx = this.balls.get(0).getVelocity().x;
        double vy = this.balls.get(1).getVelocity().y;
        vx -= Math.signum(vx) * 5;
        vy -= Math.signum(vy) * 5;
        assertNotEquals("Test ball velocity", new Point2D.Double(vx, vy), this.balls.get(0).getVelocity());
    }

    /**
     * Test ball Speed Up Strategy.
     */
    public void testBallSpeedUpStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("StraightStrategy", "ChangeMassStrategy");
        da.updateBallWorld();
        double vx = this.balls.get(0).getVelocity().x;
        double vy = this.balls.get(1).getVelocity().y;
        vx += Math.signum(vx) * 5;
        vy += Math.signum(vy) * 5;
        assertNotEquals("Test ball velocity", new Point2D.Double(vx, vy), this.balls.get(0).getVelocity());
    }

    /**
     * Test Stick Strategy.
     */
    public void testBallStickStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("ChangeMassStrategy", "ChangeColorStrategy");
        Point2D.Double vel = this.balls.get(1).getVelocity();
        da.updateBallWorld();
        assertEquals("Test ball velocity", vel, this.balls.get(0).getVelocity());
    }

    /**
     * Test Stop Strategy.
     */
    public void testBallStopStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("ChangeSizeStrategy", "ChangeColorStrategy");
        Point2D.Double vel = this.balls.get(1).getVelocity();
        vel = new Point2D.Double(-vel.x, -vel.y);
        da.updateBallWorld();
        assertEquals("Test first ball velocity", new Point2D.Double(0, 0), this.balls.get(0).getVelocity());
        assertEquals("Test second ball velocity", vel, this.balls.get(1).getVelocity());
    }

    /**
     * Test Stop Strategy.
     */
    public void testBallSwapLocationStrategy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        loadBalls("ChangeSizeStrategy", "ReverseVelocity");
        Point2D.Double locA = this.balls.get(0).getLocation();
        Point2D.Double locB = this.balls.get(1).getLocation();
        da.updateBallWorld();
        assertNotEquals("Test first ball location", locB, this.balls.get(0).getLocation());
        assertNotEquals("Test second ball location", locA, this.balls.get(1).getLocation());
    }


    /**
     * Test switch strategies should cause a switchable ball to switch to the expected strategy.
     */
    public void testSwitchBall() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        for (String strategy : this.ballStrategy) {
            String body = "switchable=true&object=ball&strategy=" + strategy;
            APaintObj ball = da.loadAPaintObj(body);
            da.updateBallWorld();
            for (String s : this.ballStrategy) {
                String b = "id=" + ball.getID() + "&strategy=" + s;
                da.switchStrategy(b);
                IntStream.range(0, 200).forEach(i -> da.updateBallWorld()); // simulate 20s
                assertEquals("Test ball strategy", s.toUpperCase(), ball.getStrategy().getName().toString());
            }
        }
        this.da.removeObject("id=-1");
    }

    /**
     * Test switch strategies should cause a switchable fish to switch to the expected strategy.
     */
    public void testSwitchFish() throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        for (String strategy : this.fishStrategy) {
            String body = "switchable=true&object=Fish&strategy=" + strategy;
            APaintObj fish = da.loadAPaintObj(body);
            da.updateBallWorld();
            for (String s : this.fishStrategy) {
                String b = "id=" + fish.getID() + "&strategy=" + s;
                da.switchStrategy(b);
                IntStream.range(0, 200).forEach(i -> da.updateBallWorld()); // simulate 20s
                assertEquals("Test fish strategy", s.toUpperCase(), fish.getStrategy().getName().toString());
            }
        }
        this.da.removeObject("id=-1");
    }
}