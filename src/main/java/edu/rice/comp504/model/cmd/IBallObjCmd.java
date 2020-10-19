package edu.rice.comp504.model.cmd;

import edu.rice.comp504.model.paintObj.APaintObj;
import edu.rice.comp504.model.paintObj.Ball;

/**
 * The IPaintObjCmd is an interface used to pass commands to objects in the PaintObjWorld.  The
 * objects must execute the command.
 */
public interface IBallObjCmd {

/**
 * Execute the command.
 * @param context The receiver paint object on which the command is executed.
 */
    public void execute(APaintObj context);
}
