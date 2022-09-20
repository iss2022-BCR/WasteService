import 'dart:io';
import 'dart:convert';
import 'dart:async';
import 'dart:typed_data';

void sendPacket(String msg) async {
  Socket socket = await Socket.connect('192.168.1.46', 9000);
  print('connected');

  await Future.delayed(Duration(seconds: 5));

  socket.write("msg");

  // listen to the received data event stream
  /*socket.listen((List<int> event) {
    print(utf8.decode(event));
  });

  // send hello
  socket.add(utf8.encode(msg));*/

  // wait 5 seconds
  await Future.delayed(Duration(seconds: 5));

  // .. and close the socket
  socket.close();
}

void main() async {
  // connect to the socket server
  final socket = await Socket.connect('localhost', 4001);
  print('Connected to: ${socket.remoteAddress.address}:${socket.remotePort}');

  // listen for responses from the server
  socket.listen(
    // handle data from the server
    (Uint8List data) {
      final serverResponse = String.fromCharCodes(data);
      print('Server: $serverResponse');
    },

    // handle errors
    onError: (error) {
      print(error);
      socket.destroy();
    },

    // handle server ending connection
    onDone: () {
      print('Server left.');
      socket.destroy();
    },
  );

  // send some messages to the server
  await sendMessage(socket, '{"distance": 100, "angle": 90}');
}

Future<void> sendMessage(Socket socket, String message) async {
  print('Client: $message');
  socket.write(message);
  await Future.delayed(Duration(seconds: 2));
}
