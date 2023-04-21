const stompClient = null;
let plastic_counter = 0.0;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    const stompClient = Stomp.client('ws://localhost:8085/websocket');
    stompClient.connect({}, () => {
        console.log('Connected to WebSocket server using STOMP');

        // Subscribe to the /topic/plastic topic
        stompClient.subscribe('/topic/plastic', (message) => {
            console.log(`Ricevuta plasticaccia: ${message.body}`);
            plastic_counter += parseFloat(message.body);

            // JQuery magic
            $("#plastic_current").html(plastic_counter);

            // The aria-valuenow attribute defines the current value of progress bar
            // In this way, we update the increment of the progress bar (depending on the quantity of plastic added)
            $("#progress_bar")
                .css({
                    "aria-valuenow": (plastic_counter / 10).toString(),
                    "width": (plastic_counter / 10).toString() + "%"
                });
        });
        // Subscribe to the /topic/led topic
        stompClient.subscribe('/topic/led', (message) => {
            console.log(`Cambio stato led: ${message.body}`);
            // Change text and background + border color of the led button
            $("#led")
                .html(message.body.toString())
                .css({
                    "background-color": message.body.toString() === "ON" ? "#1cc88a" : "#858796",
                    "border-color": message.body.toString() === "ON" ? "#1cc88a" : "#858796"
                });
        });
        stompClient.subscribe('/topic/grid', (message) => {
            console.log(`Robot in movimento, posizione: ${message.body}`);
            console.log(message.body.split(","))
            let num_cols = parseInt($("#num_cols").html(), 10)
            console.log("aaaa"+ num_cols);
            // Change background color of the cell
            let coords = message.body
                .split(",")
                .map((s) => parseInt(s, 10));
            console.log(coords);
            let idx = coords[0] * num_cols + coords[1]
            idx = parseInt(idx, 10)
            console.log(idx);
            $("#cell_" + idx).append("<div class='circle'></div>")
            console.log(typeof coords[0])
            console.log(typeof coords[1])
            console.log(typeof num_cols)
            console.log(typeof idx)


        });

    });
}

/*
Initialize the grid of the Transport Trolley, depending on the type of cell.
The cells are initialized as "empty cells". This function assigns the correct value of the cells, as specified
in application.properties file.
The cells are identified by their index (idx). The conversion from cartesian coordinates to index is done in index.html
with Thymeleaf.
*/
function initialize_grid() {
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
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

/*
function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}*/