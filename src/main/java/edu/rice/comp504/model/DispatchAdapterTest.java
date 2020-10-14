package edu.rice.comp504.model;

import edu.rice.comp504.model.ball.Ball;
import junit.framework.TestCase;

public class DispatchAdapterTest extends TestCase {
    private String[] strategies = new String[]{"HorizontalStrategy", "ChangeColorStrategy", "ChangeSizeStrategy", "GravityStrategy", "RandomLocationStrategy", "ShakingStrategy", "ChangeColorAfterCollisionStrategy", "SuddenStopStrategy", "ChangeVelocityAfterCollisionStrategy", "NullStrategy", "RotatingStrategy"};
    private DispatchAdapter da = new DispatchAdapter();

    {
        DispatchAdapter.setCanvasDims("800 800");
    }

    /**
     * Test dispatch adapter loadBall method creates a ball with the expected ball strategy.
     */
    public void testLoadBall() {
        for (String strategy : this.strategies) {
            testBall(strategy);
        }
        this.da.RemoveBalls(-1);
    }

    /**
     * Test switch strategies should cause a switchable ball to switch to the expected strategy,
     * and a ball to switch strategies should not affect a non-switchable ball.
     */
    public void testSwitchBall() {
        for (String strategy : this.strategies) {
            switchBall(strategy, "true");
            switchBall(strategy, "false");
        }
        this.da.RemoveBalls(-1);
    }

    /**
     * Test clear balls should cause all balls to be removed from the ball world.
     **/
    public void testClearBall() {
        int pass = 11;
        for (int i = 1; i < pass; i++) {
            createBall(i);
            this.da.RemoveBalls(-1);
            assertEquals("Test remove ball", 0, this.da.updateBallWorld().size());
        }
    }


    /**
     * Help method for test load ball.
     *
     * @param strategy Strategy to test
     */
    private void testBall(String strategy) {
        Ball ball = this.da.loadBall(strategy, "true");
        assertEquals("Load " + strategy + " test", strategy, ball.getStrategy().getName());
    }


    /**
     * Help method for switch ball.
     *
     * @param strategy   Strategy of the ball
     * @param switchable isSwitchable
     */
    private void switchBall(String strategy, String switchable) {
        Ball ball = this.da.loadBall(strategy, switchable);
        for (String item : this.strategies) {
            ball.setStrategy(DispatchAdapter.map.get(item));
            if (!item.equals(strategy)) {
                String expected = item;
                if (!Boolean.parseBoolean(switchable)) {
                    expected = strategy;
                }
                assertEquals(ball.getStrategy().getName() + " switch " + strategy + " test", expected, ball.getStrategy().getName());
            }
        }
    }


    /**
     * Help method for test clear ball.
     *
     * @param numBall Number of ball to generate
     */
    private void createBall(int numBall) {
        for (int i = 0; i < numBall; i++) {
            this.da.loadBall(this.strategies[i], "true");
        }
    }

}