/*
* Utility script to ease the update of Dashboard components
*/

let maxPlastic = 100.0;
let maxGlass = 100.0;
let idx_previous_cell = -1;
let hasWaste = false;
log = false

// SETUP ======================================================================
function setPlasticMax(maxPlasticAmount)
{
    maxPlastic = maxPlasticAmount;
    $("#plastic_max").html("/ " + maxPlasticAmount.toFixed(1));
}
function setGlassMax(maxGlassAmount)
{
    maxGlass = maxGlassAmount;
    $("#glass_max").html("/ " + maxGlassAmount.toFixed(1));
}
/*
Initialize the grid of the Transport Trolley, depending on the type of cell.
The cells are initialized as "empty cells". This function assigns the correct value of the cells, as specified
in application.properties file.
The cells are identified by their index (idx). The conversion from cartesian coordinates to index is done in dashboard.html
with Thymeleaf.
*/
function initialize_grid(rows, columns) {


    /*
    For each cell identified as "indoor_cells", we:
    - retrieve the index of the cell
    - remove the "empty-cell" class
    - add the "indoor-cell" class
    The same logic is applied for the other types of cells as well.
    */
    $("#indoor_cells").children().each(function () {
        let idx = $(this).html();
        $("#cell_" + idx).removeClass("empty-cell").addClass("indoor-cell");
    });
    $("#home_cells").children().each(function () {
        let idx = $(this).html();
        $("#cell_" + idx).removeClass("empty-cell").addClass("home-cell");
    });
    $("#plastic_cells").children().each(function () {
        let idx = $(this).html();
        $("#cell_" + idx).removeClass("empty-cell").addClass("plastic-cell");
    });
    $("#glass_cells").children().each(function () {
        let idx = $(this).html();
        $("#cell_" + idx).removeClass("empty-cell").addClass("glass-cell");
    });
}

// UPDATE =====================================================================

function updatePlasticCounter(currentPlasticAmount)
{
    $("#plastic_current").html(currentPlasticAmount.toFixed(1));

    // The aria-valuenow attribute defines the current value of progress bar
    // In this way, we update the increment of the progress bar (depending on the quantity of plastic added)
    $("#progress_bar_plastic")
        .css({
            "aria-valuenow": (currentPlasticAmount / maxPlastic * 100).toString(),
            "width": (currentPlasticAmount / maxPlastic * 100).toString() + "%"
        });
}

function updateGlassCounter(currentGlassAmount)
{
    $("#glass_current").html(currentGlassAmount.toFixed(1));

    // The aria-valuenow attribute defines the current value of progress bar
    // In this way, we update the increment of the progress bar (depending on the quantity of plastic added)
    $("#progress_bar_glass")
        .css({
            "aria-valuenow": (currentGlassAmount / maxGlass * 100).toString(),
            "width": (currentGlassAmount / maxGlass * 100).toString() + "%"
        });
}

function updateLed(ledState)
{
    if(ledState.toUpperCase() === "OFF")
    {
        $("#led")
            .removeClass("led-on")
            .removeClass("led-blink")
            .addClass("led-off")
    }
    if(ledState.toUpperCase() === "ON")
    {
        $("#led")
            .removeClass("led-off")
            .removeClass("led-blink")
            .addClass("led-on")
    }
    if(ledState.toUpperCase() === "BLINKING")
    {
        $("#led")
            .removeClass("led-off")
            .removeClass("led-on")
            .addClass("led-blink")
    }
}

function updateTransportTrolley(newTTposition, newTTstate)
{
    $("#trolley_status").html(newTTstate);

    if (idx_previous_cell !== -1)
        $("#cell_"+ idx_previous_cell).empty();

    $("#trolley_position").html("[ " + newTTposition[0] + ", " + newTTposition[1] + " ]");
    let num_cols = parseInt($("#num_cols").html(), 10)
    // Change background color of the cell

    let idx = newTTposition[0] * num_cols + newTTposition[1]
    idx = parseInt(idx, 10)

    let tt_img = "ddr_robot.png"
    if(newTTstate.toUpperCase() === "HOME" || newTTstate.toUpperCase() === "STOPPED")
        if(hasWaste)
            tt_img = "ddr_robot_trash.png"
        else tt_img = "ddr_robot.png"
    if(newTTstate.toUpperCase() === "MOVING")
        if(hasWaste)
            tt_img = "ddr_robot_trash_moving.gif"
        else tt_img = "ddr_robot_moving.gif"
    if(newTTstate.toUpperCase() === "PICKUP")
    {
        tt_img = "ddr_robot_pickup.gif"
        hasWaste = true;
    }
    if(newTTstate.toUpperCase() === "DUMP")
    {
        tt_img = "ddr_robot_dump.gif"
        hasWaste = false;
    }
    $("#cell_" + idx).append("<img src='img/" + tt_img + "' width='90%'/>")
    //$("#cell_" + idx).append("<div class='circle'></div>")
    idx_previous_cell = idx;
}