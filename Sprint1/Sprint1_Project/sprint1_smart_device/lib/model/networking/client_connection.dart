import 'package:flutter/services.dart';

class ClientConnection {
  get address {}
  get remoteAddress {}
  get port {}
  get remotePort {}

  // Tries to enstablish a connection an host
  Future<void> connect(String host, int port, {Duration? timeout}) async {}

  // Close this endpoint of the connection
  Future<void> close() async {}

  // Close both the endpoints of the connection
  Future<void> destroy() async {}

  // Starts listening for messages on the connection and assign a function to be called when a message is received.
  listen(Function(Uint8List) onMsg,
      {Function(Error error)? onError,
      Function()? onDone,
      bool? cancelOnError}) {}

  // Send a message to the other endpoint
  sendMessage(String message) {}
}
