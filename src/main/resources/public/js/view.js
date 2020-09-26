'use strict';

//app to draw polymorphic shapes on canvas
var app;

/**
 * Create the ball world app for a canvas
 * @param canvas The canvas to draw balls on
 * @returns {{drawBall: drawBall, clear: clear}}
 */
function createApp(canvas) {
    let c = canvas.getContext("2d");

    /**
     * Draw a circle
     * @param x  The x location coordinate
     * @param y  The y location coordinate
     * @param radius  The circle radius
     * @param color The circl color
     */
    let drawCircle = function(x, y, radius, color) {
        c.fillStyle = color;
        c.beginPath();
        c.arc(x, y, radius, 0, 2 * Math.PI, false);
        c.closePath();
        c.fill();
    };

    let clear = function() {
        c.clearRect(0,0, canvas.width, canvas.height);
    };


    return {
        drawCircle,
        clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}


window.onload = function() {
    app = createApp(document.querySelector("canvas"));

    $("#btn-load").click(loadBall);
    $("#btn-clear").click(clear);
};

/**
 * load ball at a location on the canvas
 */
function loadBall() {
    let values = "";

    $.post("/load", { strategies: values}, function (data) {
    }, "json");
}

/**
 * Switch ball strategies
 */
function switchStrategy() {
    let values = "";

    $.post("/switch", { strategies: values}, function (data) {

    }, "json");
}

function updateBallWorld() {
    $.get("/update", function(data) {

    }, "json");
}

/**
 * Pass along the canvas dimensions
 */
function canvasDims() {
    $.post("/canvas/dims", {height: app.dims.height, width: app.dims.width});
}

/**
 * Clear the canvas
 */
function clear() {
    $.get("/clear");
    app.clear();
}