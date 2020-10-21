package edu.rice.comp504.model.collision.handler;

import edu.rice.comp504.model.collision.resolver.*;
import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.strategy.Strategy;

import java.util.HashMap;
import java.util.Map;

/**
 * The BallCollisionHandler makes decisions on how to handle ball-ball collision.
 */
public class BallCollisionHandler implements ICollisionHandler {
    private static BallCollisionHandler singleton;
    private Map<Map.Entry<Strategy, Strategy>, ICollisionResolution> map = new HashMap<>();

    {
        map.put(Map.entry(Strategy.STRAIGHTSTRATEGY, Strategy.STRAIGHTSTRATEGY), ChangeColor.makeOnly());
        map.put(Map.entry(Strategy.STRAIGHTSTRATEGY, Strategy.REVERSEVELOCITYSTRATEGY), ChangeColorOnRadius.makeOnly());
        map.put(Map.entry(Strategy.STRAIGHTSTRATEGY, Strategy.CHANGECOLORSTRATEGY), Shrink.makeOnly());
        map.put(Map.entry(Strategy.STRAIGHTSTRATEGY, Strategy.CHANGEMASSSTRATEGY), SpeedUp.makeOnly());
        map.put(Map.entry(Strategy.STRAIGHTSTRATEGY, Strategy.CHANGESIZESTRATEGY), SlowDown.makeOnly());
        map.put(Map.entry(Strategy.CHANGEMASSSTRATEGY, Strategy.CHANGECOLORSTRATEGY), Stick.makeOnly());
        map.put(Map.entry(Strategy.CHANGEMASSSTRATEGY, Strategy.CHANGESIZESTRATEGY), Absorb.makeOnly());
        map.put(Map.entry(Strategy.CHANGESIZESTRATEGY, Strategy.RANDOMVELOCITYSTRATEGY), ChangeMass.makeOnly());
        map.put(Map.entry(Strategy.CHANGESIZESTRATEGY, Strategy.CHANGECOLORSTRATEGY), Stop.makeOnly());
        map.put(Map.entry(Strategy.CHANGESIZESTRATEGY, Strategy.REVERSEVELOCITYSTRATEGY), SwapLocation.makeOnly());


    }

    /**
     * Private constructor for singleton pattern.
     */
    private BallCollisionHandler() {
    }


    /**
     * Only makes 1 BallCollisionHandler.
     *
     * @return The BallCollisionHandler
     */

    public static BallCollisionHandler makeOnly() {
        if (singleton == null) {
            singleton = new BallCollisionHandler();
        }
        return singleton;
    }

    /**
     * Decision on how to handle two APaintObj.
     *
     * @param a first object in collision
     * @param b second object in collision
     */

    @Override
    public void handleCollision(APaintObj a, APaintObj b) {
        Strategy sa = a.getStrategy().getName();
        Strategy sb = b.getStrategy().getName();
        ICollisionResolution resolver = map.get(Map.entry(sa, sb));
        if (resolver == null) {
            resolver = map.get(Map.entry(sb, sa));
            if (resolver == null) {
                BounceOff.makeOnly().resolveCollision(a, b); //default resolver if no resolution found
                return;
            }
            resolver.resolveCollision(b, a);
            return;
        }
        resolver.resolveCollision(a, b);
    }

}
