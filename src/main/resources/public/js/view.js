'use strict';

//app to draw polymorphic shapes on canvas
var app;

//id of current interval
let intervalID;

// all strategies
const strategies = ["HorizontalStrategy", "ChangeColorStrategy", "ChangeSizeStrategy",
    "GravityStrategy", "RandomLocationStrategy", "ShakingStrategy", "ChangeColorAfterCollisionStrategy",
    "SuddenStopStrategy", "ChangeVelocityAfterCollisionStrategy", "NullStrategy", "RotatingStrategy"];

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

    let clear = function () {
        c.clearRect(0, 0, canvas.width, canvas.height);
    };


    return {
        drawCircle,
        clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}


window.onload = function () {
    app = createApp(document.querySelector("canvas"));
    canvasDims();
    setUpdateFreq();
    $("#btn-load").click(function () {
        let switchable = $("#switchable").find(":selected").val();
        let strategy = $("#strategy").find(":selected").val();
        loadBall(strategy, switchable);
        if ($("#btn-switchStrategy").val() == null) {
            $('#btn-switchStrategy').removeAttr('disabled');
        }
    });
    $("#btn-clear").click(clear);
    $("#switchableBalls").change(function () {
        setStrategy($("#switchableBalls").find(":selected").val());
    });
    $("#btn-switchStrategy").click(switchStrategy);
};

window.onbeforeunload = function () {
    clear();
}

/**
 * Determine how often line updates occur
 */
function setUpdateFreq() {
    intervalID = setInterval(updateBallWorld, 100);
}

/**
 * load ball at a location on the canvas
 */
function loadBall(strategy, switchable) {
    $.post("/load", {strategy: strategy, switchable: switchable}, function (data) {
        app.drawCircle(data.loc.x, data.loc.y, data.radius, data.color);
        var p = new Option('Ball' + ' ' + data.id, data.id);
        if (!data.switchable) {
            $(p).css("color", "red")
        }
        $('#switchableBalls').append(p);
    }, "json");
}

/**
 * Switch ball strategies
 */
function switchStrategy() {
    let ball = $("#switchableBalls").find(":selected").val();
    let strategy = $("#availableStrategy").find(":selected").val();
    $.post("/switch", {id: ball, strategy: strategy}, function (data) {
        setStrategy(ball);
    }, "json");
}

/**
 * Set available ball strategy.
 */
function setStrategy(id) {
    if (id != "balls") {
        $.post("/strategy", {id: id}, function (data) {
            $("#availableStrategy option").remove();
            if (data.switchable == true) {
                $('#btn-switchStrategy').removeAttr('disabled');
                for (let i = 0; i < strategies.length; i++) {
                    if (data.strategy.name != strategies[i]) {
                        $("#availableStrategy").append(new Option(strategies[i], strategies[i]));
                    }
                }
            } else {
                $('#btn-switchStrategy').attr('disabled', true);
                $("#availableStrategy").append(new Option("--- No Available Strategy ---", "noAvailableStrategy"))
            }
        }, "json");
    } else {
        $('#btn-switchStrategy').attr('disabled', true);
        $("#availableStrategy option").remove();
        $("#availableStrategy").append(new Option("--- No Available Strategy ---", "noAvailableStrategy"))
    }
}

function updateBallWorld() {
    $.get("/update", function (data) {
        app.clear();
        data.forEach(function (element) {
            element = element.listener;
            app.drawCircle(element.loc.x, element.loc.y, element.radius, element.color, element.id);
        });
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
    initSwitch();
    app.clear();
}

/**
 * Initialize the switch drop downs
 */
function initSwitch() {
    $('#switchableBalls option').remove();
    $("#switchableBalls").append(new Option("--- Balls ---", "balls"))
    $("#availableStrategy option").remove();
    $("#availableStrategy").append(new Option("--- Available Strategies ---", "availableStrategy"))
    $("#btn-switchStrategy").attr("disabled", true);
}