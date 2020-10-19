package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;


/**
 * Update command for ball.
 */
public class UpdateCmd implements IBallObjCmd {
    private static IBallObjCmd singleton;

    /**
     * Private constructor.
     */
    private UpdateCmd() {
    }

    /**
     * Singleton pattern to get the instance.
     *
     * @return singleton
     */

    public static IBallObjCmd makeStrategy() {
        if (singleton == null) {
            singleton = new UpdateCmd();
        }
        return singleton;
    }

    /**
     * Execute the command.
     *
     * @param context The receiver paint object on which the command is executed.
     */
    @Override
    public void execute(APaintObj context) {
        context.getStrategy().updateState(context);
    }
}
