'use strict';

//app to draw polymorphic shapes on canvas
var app;

var img = new Image();
// img.src = 'https://freesvg.org/img/CartoonFish.png';

//id of current interval
let intervalID;

let ballStrategiesMap = new Map();
let fishStrategiesMap = new Map();


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
     * @param color The circle color
     */
    let drawCircle = function (x, y, radius, color, id) {
        c.fillStyle = color;
        c.beginPath();
        c.arc(x, y, radius, 0, 2 * Math.PI, false);
        c.closePath();
        c.fill();
        if (id) {
            c.fillStyle = 'white';
            c.font = "15px Arial";
            c.textAlign = "center"
            c.fillText(id, x, y);
        }

    };

    /**
     * Draw a fish
     * @param x The x location
     * @param y The y location
     * @param scaleX The x scale
     * @param scaleY The y scale
     * @param width The width
     * @param height The height
     * @param angle The angle
     * @param id ID
     */

    let drawFish = function (x, y, scaleX, scaleY, width, height, angle, id) {
        c.save();
        // Translate to the center point of our image
        let centerX = x + Math.sign(scaleX) * width / 2;
        let centerY = y + height / 2;
        c.translate(centerX, centerY);
        // Perform the rotation
        c.rotate(angle);
        // Translate back to the top left of our image
        c.translate(-centerX, -centerY);
        c.scale(scaleX, scaleY);
        // Finally we draw the image
        c.drawImage(img, x / scaleX, y / scaleY);
        // And restore the context ready for the next loop
        c.restore();
        if (id) {
            c.fillStyle = 'white';
            c.font = "15px Arial";
            c.textAlign = "center"
            c.fillText(id, x + img.width * scaleX / 2, y + img.height * scaleY / 2);
        }
    }

    /**
     * Clear the canvas.
     */
    let clear = function () {
        c.clearRect(0, 0, canvas.width, canvas.height);
    };


    return {
        drawCircle,
        clear,
        drawFish,
        dims: {height: canvas.height, width: canvas.width}
    }
}


window.onload = function () {
    app = createApp(document.querySelector("canvas"));
    canvasDims();
    fishURL();
    setUpdateFreq();
    initializeMap();
    setUpUI();
    $("#btn-load").click(loadObject);
    $("#btn-clear").click(clear);
    $("#btn-switchStrategy").click(switchStrategy);
    $("#btn-remove").click(removeObj)
};

window.onbeforeunload = function () {
    clear();
}

/**
 * Pass along the canvas dimensions
 */
function canvasDims() {
    $.post("/canvas/dims", {height: app.dims.height, width: app.dims.width});
}

/**
 * Set what fish img to use
 */
function fishURL() {
    $.get("/fishURL", function (data) {
        img.src = data
    }, "json");
}

/**
 * Determine how often line updates occur
 */
function setUpdateFreq() {
    intervalID = setInterval(updateBallWorld, 100);
}

/**
 * Method initializes the UI.
 */

function setUpUI() {
    appendStrategy();
    $("#object").change(function () {
        appendStrategy()
    });
    $("#availableObjs").change(function () {
        appendSwitchStrategy();
    });

}

/**
 *  Help method to add the strategy.
 */
function appendStrategy() {
    let map;
    let obj = $("#object").find(":selected").val();
    if (obj === "Ball") {
        map = ballStrategiesMap;
    } else {
        map = fishStrategiesMap;
    }
    $("#strategy option").remove();
    for (let [key, value] of map.entries()) {
        $("#strategy").append(new Option(value, key));
    }
}

/**
 * Help method to add the switchable strategy.
 */

function appendSwitchStrategy() {
    let id = $("#availableObjs").find(":selected").val();
    $("#availableStrategy option").remove();
    if (id !== "null") {
        $("#btn-remove").removeAttr("disabled");
        $.post("/strategy", {id: id}, function (data) {
            if (data.switchable === true) {
                $('#btn-switchStrategy').removeAttr('disabled');
                let map;
                if (data.name === "Ball") {
                    map = ballStrategiesMap;
                } else {
                    map = fishStrategiesMap;
                }
                for (let [key, value] of map.entries()) {
                    if (data.strategy.name !== key.toUpperCase()) {
                        $("#availableStrategy").append(new Option(value, key));
                    }
                }
            } else {
                $('#btn-switchStrategy').attr('disabled', true);
                $("#availableStrategy").append(new Option("--- No Available Strategy ---", "null"))
            }
        }, "json");
    } else {
        $('#btn-switchStrategy').attr('disabled', true);
        $("#availableStrategy").append(new Option("--- No Available Strategy ---", "null"))
        $('#btn-remove').attr('disabled', true);
    }
}

/**
 * load ball at a location on the canvas
 */
function loadObject() {
    let switchable = $("#switchable").find(":selected").val();
    let object = $("#object").find(":selected").val();
    let strategy = $("#strategy").find(":selected").val();
    $.post("/load", {object: object, switchable: switchable, strategy: strategy}, function (data) {
        if (data.name === "Ball") {
            app.drawCircle(data.loc.x, data.loc.y, data.radius, data.color, data.id);
        } else {
            app.drawFish(data.loc.x, data.loc.y, data.scale.x, data.scale.y, data.width, data.height, data.angel, data.id);
        }
        addToAvailableObjs(data);
    }, "json");
}

/**
 * Help method for display available objects.
 * @param data the data from controller
 */
function addToAvailableObjs(data) {
    var p = new Option(data.name + ' ' + data.id, data.id);
    if (!data.switchable) {
        $(p).css("color", "red")
    }
    $('#availableObjs').append(p);
}

/**
 * Switch ball strategies.
 */
function switchStrategy() {
    let id = $("#availableObjs").find(":selected").val();
    let strategy = $("#availableStrategy").find(":selected").val();
    $.post("/switch", {id: id, strategy: strategy}, function (data) {
        updateAvailableStrategy(data.name, data.strategy.name);
    }, "json");
}

/**
 * Help method for switch strategy.
 * @param name the name of object
 * @param strategy the strategy name
 */

function updateAvailableStrategy(name, strategy) {
    $("#availableStrategy option").remove();
    let map;
    if (name === "Ball") {
        map = ballStrategiesMap;
    } else {
        map = fishStrategiesMap;
    }
    for (let [key, value] of map.entries()) {
        if (strategy !== key.toUpperCase()) {
            $("#availableStrategy").append(new Option(value, key))
        }
    }
}

/**
 * Help method to show the popup for rules.
 */
function popup() {
    var popup = document.getElementById("popup");
    popup.classList.toggle("show");
}

/**
 * Update the canvas.
 */
function updateBallWorld() {
    $.get("/update", function (data) {
        app.clear();
        let str = "";
        data.forEach(function (data) {
            if (data.name === "Ball") {
                app.drawCircle(data.loc.x, data.loc.y, data.radius, data.color, data.id);
            } else {
                app.drawFish(data.loc.x, data.loc.y, data.scale.x, data.scale.y, data.width, data.height, data.angle, data.id);
            }
            if (data.switchable === true) {
                str += "<p>" + data.name + data.id + ": " + data.strategy.name + "</p>";
            } else {
                str += "<p style=\"color:red;\">" + data.name + data.id + ": " + data.strategy.name + "</p>";
            }

        })
        $("#ballInfo").html(str);
    }, "json");

}

/**
 * Clear the canvas.
 */
function clear() {
    $.post("/clear", {id: -1});
    $('#availableObjs option').remove();
    $("#availableObjs").append(new Option("--- Objects ---", "null"))
    $("#availableStrategy option").remove();
    $("#availableStrategy").append(new Option("--- Available Strategies ---", "null"))
    $("#btn-switchStrategy").attr("disabled", true);
    $("#btn-remove").attr("disabled", true);
    app.clear();
}

/**
 * Remove the object by id.
 */
function removeObj() {
    let id = $("#availableObjs").find(":selected")
    $.post("/clear", {id: id.val()});
    id.remove();
    appendSwitchStrategy();
}

function initializeMap() {
    ballStrategiesMap.set("NullStrategy", "Null Strategy - black color and 0 velocity");
    ballStrategiesMap.set("StraightStrategy", "Straight Strategy - move straight");
    ballStrategiesMap.set("RotatingStrategy", "Rotating Strategy - rotate around a fixed point");
    ballStrategiesMap.set("RandomVelocityStrategy", "Random Velocity Strategy - random velocity every second");
    ballStrategiesMap.set("ChangeSizeStrategy", "Change Size Strategy - random size every 5 seconds");
    ballStrategiesMap.set("ChangeMassStrategy", "Change Mass Strategy - change mass every second");
    ballStrategiesMap.set("ChangeColorStrategy", "Change Color Strategy - random color every 5 seconds");
    ballStrategiesMap.set("ShakingStrategy", "Shaking Strategy - move up and down while moving horizontally");
    ballStrategiesMap.set("ReverseVelocityStrategy", "Reverse Velocity Strategy - reverse velocity direction every 2 seconds");
    ballStrategiesMap.set("RandomLocationStrategy", "Random Location Strategy - randomize location every update");
    ballStrategiesMap.set("RandomWalkStrategy", "Random Walk Strategy - reverse direction with equal probability");
    fishStrategiesMap.set("NullStrategy", "Null Strategy - black color and 0 velocity");
    fishStrategiesMap.set("StraightStrategy", "Straight Strategy - move straight");
    fishStrategiesMap.set("RotatingStrategy", "Rotating Strategy - rotate around a fixed point");
    fishStrategiesMap.set("ChangeSizeStrategy", "Change Size Strategy - random size every 5 seconds");
    fishStrategiesMap.set("ShakingStrategy", "Shaking Strategy - move up and down while moving horizontally");
    fishStrategiesMap.set("ReverseVelocityStrategy", "Reverse Velocity Strategy - reverse velocity direction every 2 seconds");
    fishStrategiesMap.set("RandomLocationStrategy", "Random Location Strategy - randomize location every update");
    fishStrategiesMap.set("RandomWalkStrategy", "Random Walk Strategy - reverse direction with equal probability");
    fishStrategiesMap.set("RandomVelocityStrategy", "Random Velocity Strategy - random velocity every second");
    fishStrategiesMap.set("SwingStrategy", "Swing Strategy - swing 20 degree every update");
    fishStrategiesMap.set("SuddenStopStrategy", "Sudden Stop Strategy - sudden stop moving with 30% chance");
}


