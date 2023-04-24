/*
wsminimal.js
*/

let socket;

function connect()
{
    let firstMessage = true;
    let host = document.location.host;
    let pathname = "/"
    let addr = "ws://" + host + pathname + "socket";
    // Assicura che sia aperta un unica connessione
    if (socket !== undefined && socket.readyState !== WebSocket.CLOSED) {
        alert("WARNING: Connessione WebSocket già stabilita");
    }
    socket = new WebSocket(addr);

    socket.onclose = function (event) {
        alert("Socket Closed")
        // todo
    };

    socket.onerror = function (event) {
        alert("Socket Error")
        // todo
    };

    socket.onmessage = function (event) {
        msg = event.data;

        let statusUpdate = JSON.parse(msg);

        // Some parameters are updated only the first time
        if(firstMessage)
        {
            firstMessage = false
            //alert("Received first msg: " + msg) // test

            setPlasticMax(statusUpdate['maxPlastic'])
            setGlassMax(statusUpdate['maxGlass'])


            // build grid
        }

        updatePlasticCounter(statusUpdate['currentPlastic'])
        updateGlassCounter(statusUpdate['currentGlass'])

        updateLed(statusUpdate['ledState'])

        updateTransportTrolley(statusUpdate['transportTrolleyPosition'], statusUpdate['transportTrolleyState'])
    };

}//connect

