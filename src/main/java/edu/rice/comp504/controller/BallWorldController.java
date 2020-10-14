package edu.rice.comp504.controller;

import com.google.gson.Gson;
import edu.rice.comp504.model.DispatchAdapter;
import edu.rice.comp504.model.ball.Ball;
import edu.rice.comp504.model.strategy.RotatingStrategy;

import java.beans.PropertyChangeListener;
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
            Ball ball = dis.loadBall(request.queryParams("strategy"), request.queryParams("switchable"));
            return gson.toJson(ball);
        });

        post("/switch", (request, response) -> {
            Collection<Ball> balls = dis.switchStrategy(request.queryParams("id"), request.queryParams("strategy"));
            return gson.toJson(balls);
        });

        post("/strategy", (request, response) -> {
            Ball b = dis.findStrategy(request.queryParams("id"));
            return gson.toJson(b);
        });

        get("/update", (request, response) -> {
            Collection<Ball> p = dis.updateBallWorld();
            return gson.toJson(p);
        });

        post("/canvas/dims", (request, response) -> {
            String dim = request.queryParams("height") + " " + request.queryParams("width");
            dis.setCanvasDims(dim);
            return gson.toJson(dim);
        });

        get("/clear", (request, response) -> {
            dis.RemoveBalls(-1);
            ((RotatingStrategy) RotatingStrategy.makeStrategy()).clear();
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
