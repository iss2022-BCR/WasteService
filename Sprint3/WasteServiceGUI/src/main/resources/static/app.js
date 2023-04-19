var stompClient = null;
var plastic_counter = 0.0;

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
    stompClient.connect({}, (frame) => {
        console.log('Connected to WebSocket server using STOMP');

        // Subscribe to the /topic/greetings topic
        stompClient.subscribe('/topic/plastic', (greeting) => {
            console.log(`Ricevuta plasticaccia: ${greeting.body}`);
            plastic_counter += JSON.parse(greeting.body).value;
            //document.getElementById("plastic_current").innerHTML = plastic_counter;
            //document.getElementById("progress_bar").setAttribute("aria-valuenow", (plastic_counter / 10).toString());
            //document.getElementById("progress_bar").setAttribute("style", `width: ${(plastic_counter / 10).toString()}%`);
            //JQuery magic
            $("#plastic_current").html(plastic_counter);
            $("#progress_bar").attr({"aria-valuenow":(plastic_counter / 10).toString(), style:`width: ${(plastic_counter / 10).toString()}%`});
        });

        // Subscribe to the /topic/wordcount topic
        stompClient.subscribe('/topic/wordcount', (wordCount) => {
            console.log(`Received word count: ${wordCount.body}`);
        });

        // // Send a message to the server
        // stompClient.send('/app/hello', {}, 'World!');
        // stompClient.send('/app/hello2', {}, 'Count the words!');
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
