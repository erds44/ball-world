package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.ball.Ball;
import edu.rice.comp504.model.strategy.IUpdateStrategy;

public class SwitchCmd implements IBallObjCmd {
    private IUpdateStrategy strategy;

    /**
     * Public constructor.
     *
     * @param strategy The strategy
     */
    public SwitchCmd(IUpdateStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Execute the command.
     *
     * @param context The receiver paint object on which the command is executed.
     */
    @Override
    public void execute(Ball context) {
        context.setStrategy(this.strategy);
    }
}
