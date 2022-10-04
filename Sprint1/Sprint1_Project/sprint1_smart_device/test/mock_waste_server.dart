import 'dart:convert';
import 'dart:io';
import 'dart:math';

import 'package:flutter/services.dart';
import 'package:intl/intl.dart';

const String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";

class MockWasteServer {
  late double _max_plastic;
  late double _max_glass;
  late bool _enableLog;

  late double _plastic;
  late double _glass;

  late ServerSocket _serverSocket;
  int _connectedSocket = 0;

  MockWasteServer(double max_plastic, double max_glass) {
    _max_plastic = max_plastic;
    _max_glass = max_glass;
    _plastic = 0.0;
    _glass = 0.0;
  }

  // start listening on every address
  Future<void> startServer(
      int port, Function(Uint8List, Socket, int) messageHandler,
      {Function? onConnect, Function? onDisconnect}) async {
    _serverSocket = await ServerSocket.bind("0.0.0.0", port).then((server) {
      _logMessage(
          "Server listening on: ${server.address.address}:${server.port}");
      server.listen((socket) {
        _connectedSocket++;
        int iSock = _connectedSocket;
        _logMessage(
            "Accepted connection #$_connectedSocket: ${socket.remoteAddress.address}:${socket.remotePort}");

        socket.listen((Uint8List data) {
          messageHandler(data, socket, iSock);
        }, onError: (error) {
          _logMessage("Error from #$iSock: ${error.toString()}");
          _connectedSocket--;
        }, onDone: () {
          _logMessage("Client #$iSock disconnected.");
          _connectedSocket--;
        }, cancelOnError: true);
      });
      return server;
    });
  }

  String _getTimeStamp() {
    final DateTime timestamp = DateTime.now();

    final String result = "[${DateFormat(dateTimeFormat).format(timestamp)}]";

    return result;
  }

  void _logMessage(String message) {
    if (_enableLog) {
      print("${_getTimeStamp()} $message");
    }
  }

  Future<void> startServerDefault(int port) async {
    _serverSocket = await ServerSocket.bind("0.0.0.0", port).then((server) {
      _logMessage(
          "Server listening on: ${server.address.address}:${server.port}");
      server.listen((socket) {
        _connectedSocket++;
        int iSock = _connectedSocket;
        _logMessage(
            "Accepted connection #$_connectedSocket: ${socket.remoteAddress.address}:${socket.remotePort}");

        socket.listen((Uint8List data) {
          _handleMessage(data, socket, iSock, _enableLog);
        }, onError: (error) {
          _logMessage("Error from #$iSock: ${error.toString()}");
          _connectedSocket--;
        }, onDone: () {
          _logMessage("Client #$iSock disconnected.");
          _connectedSocket--;
        }, cancelOnError: true);
      });
      return server;
    });
  }

  void _handleMessage(
      Uint8List data, Socket socket, int iSock, bool enableLog) {
    String result = String.fromCharCodes(data);

    final jsonObj = jsonDecode(result);
    //print(jsonObj['wasteWeight']); // test
    //print(jsonObj['wasteType']); // test

    final wasteWeight =
        double.tryParse(jsonObj['wasteWeight'].toString()) ?? -1.0;
    final wasteType = jsonObj['wasteType'].toString().toUpperCase();

    if (wasteWeight == -1.0) {
      return;
    }

    switch (wasteType) {
      case "PLASTIC":
        if (_plastic + wasteWeight <= _max_plastic) {
          _plastic += wasteWeight;
          _logMessage("Sent LoadAccepted to #$iSock");
          socket.write("loadaccepted");
        } else {
          _logMessage("Sent LoadRejected to #$iSock");
          socket.write("loadrejected");
        }
        break;
      case "GLASS":
        if (_glass + wasteWeight <= _max_glass) {
          _glass += wasteWeight;
          _logMessage("Sent LoadAccepted to #$iSock");
          socket.write("loadaccepted");
        } else {
          _logMessage("Sent LoadRejected to #$iSock");
          socket.write("loadrejected");
        }
        break;
      default:
        break;
    }
  }
}

/*void main() {
  MockWasteServer mockServer = MockWasteServer(100.0, 100.0);
  mockServer.startServer(4001, _handleMessage)
}*/
