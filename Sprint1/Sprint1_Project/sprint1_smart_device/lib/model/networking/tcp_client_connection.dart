import 'dart:async';
import 'dart:io';

import 'package:flutter/foundation.dart';

import 'client_connection.dart';

class TcpClientConnection implements ClientConnection {
  late Socket _socket;

  @override
  InternetAddress get address {
    return _socket.address;
  }

  @override
  InternetAddress get remoteAddress {
    return _socket.remoteAddress;
  }

  @override
  int get port {
    return _socket.port;
  }

  @override
  int get remotePort {
    return _socket.remotePort;
  }

  TcpClientConnection();

  @override
  Future<void> connect(String host, int port, {Duration? timeout}) async {
    _socket = await Socket.connect(host, port, timeout: timeout);
  }

  @override
  Future<void> close() async {
    _socket.close();
  }

  @override
  Future<void> destroy() async {
    _socket.destroy();
  }

  @override
  listen(Function(Uint8List) onMsg,
      {Function(Error error)? onError,
      Function()? onDone,
      bool? cancelOnError}) {
    _socket.listen(onMsg,
        onError: onError, onDone: onDone, cancelOnError: cancelOnError);
  }

  @override
  sendMessage(String message) {
    _socket.write(message);
  }
}
