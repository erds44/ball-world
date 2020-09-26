package edu.rice.comp504.controller;

import com.google.gson.Gson;
import edu.rice.comp504.model.DispatchAdapter;

import static spark.Spark.*;


/**
 * The paint world controller creates the adapter(s) that communicate with the view.
 * The controller responds to requests from the view after contacting the adapter(s).
 */
public class BallWorldController {

    /**
     * The main entry point into the program.
     * @param args  The program arguments normally specified on the cmd line
     */
    public static void main(String[] args) {
        staticFiles.location("/public");

        Gson gson = new Gson();
        DispatchAdapter dis = new DispatchAdapter();

        post("/load", (request, response) -> {
            return "new ball";
        });

        post("/switch", (request, response) -> {
            return "switch strategies";
        });

        get("/update", (request, response) -> {
            return "update paint world";
        });

        post("/canvas/dims", (request, response) -> {
            return "set canvas dimensions";
        });

        get("/remove", (request, response) -> {
            return "removed some balls in paint world";
        });

        get("/clear", (request, response) -> {
            return "removed all balls in paint world";
        });

    }
}
