package edu.rice.comp504.controller;

import com.google.gson.Gson;
import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.paintobj.APaintObj;
import edu.rice.comp504.model.paintobj.Fish;


import java.util.Collection;

import static spark.Spark.*;


/**
 * The paint world controller creates the adapter(s) that communicate with the view.
 * The controller responds to requests from the view after contacting the adapter(s).
 */
public class BallWorldController {

    /**
     * The main entry point into the program.
     *
     * @param args The program arguments normally specified on the cmd line
     */
    public static void main(String[] args) {
        staticFiles.location("/public");
        port(getHerokuAssignedPort());
        Gson gson = new Gson();
        DispatchAdapter dis = new DispatchAdapter();

        post("/load", (request, response) -> {
            APaintObj obj = dis.loadAPaintObj(request.body());
            return gson.toJson(obj);
        });

        post("/switch", (request, response) -> {
            APaintObj obj = dis.switchStrategy(request.body());
            return gson.toJson(obj);
        });

        post("/strategy", (request, response) -> {
            APaintObj obj = dis.getObj(request.body());
            return gson.toJson(obj);
        });

        get("/update", (request, response) -> {
            Collection<APaintObj> objs = dis.updateBallWorld();
            return gson.toJson(objs);
        });

        post("/canvas/dims", (request, response) -> {
            dis.setCanvasDims(request.body());
            return gson.toJson(dis);
        });

        get("/fishURL", (request, response) -> gson.toJson(Fish.URL));

        post("/clear", (request, response) -> {
            dis.removeObject(request.body());
            return gson.toJson("remove");
        });

    }

    /**
     * Configuration on port.
     *
     * @return default port if heroku-port isn't set (i.e. on localhost)
     */

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567;
    }
}
