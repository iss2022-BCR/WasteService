import 'dart:io';

import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:sprint1_smart_device/model/networking/client_connection.dart';
import 'package:sprint1_smart_device/model/networking/tcp_client_connection.dart';
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';
import 'package:sprint1_smart_device/model/waste_service/waste_type.dart';

const String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";

const double defaultMax = 100.0;

class MockWasteServer {
  Map<WasteType, double> _storage = {};
  Map<WasteType, double> _maxStorages = {};

  bool _enableLog = false;

  late ServerSocket _serverSocket;
  int _connectedSocket = 0;

  MockWasteServer({bool? enableLog}) {
    for (WasteType type in WasteType.values) {
      _storage[type] = 0.0;
      _maxStorages[type] = defaultMax;
    }
    _enableLog = enableLog ?? false;
  }
  MockWasteServer.withCapacities(Map<WasteType, double> maxStorages,
      {bool? enableLog}) {
    for (WasteType type in WasteType.values) {
      _storage[type] = 0.0;
      _maxStorages[type] = maxStorages[type] ?? defaultMax;
    }
    _enableLog = enableLog ?? false;
  }

  // start listening on every address
  Future<void> startServer(
      int port, Function(Uint8List, Socket, int) messageHandler,
      {Function(Socket, int)? onConnect,
      Function(Socket, int)? onDisconnect}) async {
    _serverSocket = await ServerSocket.bind("0.0.0.0", port).then((server) {
      _logMessage(
          "Server listening on: ${server.address.address}:${server.port}");
      server.listen((socket) {
        if (onConnect != null) {
          onConnect(socket, _connectedSocket);
        }
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
          if (onDisconnect != null) {
            onDisconnect(socket, _connectedSocket);
          }
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
          handleMessage(data, socket, iSock, _enableLog);
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

  void handleMessage(Uint8List data, Socket socket, int iSock, bool enableLog) {
    String result = String.fromCharCodes(data);

    StoreRequest sr = StoreRequest.fromJsonString(result);
    if (canStore(sr.wasteType, sr.wasteWeight)) {
      deposit(sr.wasteWeight, sr.wasteType);
      _logMessage("Sent LoadAccepted to #$iSock");
      socket.write("LoadAccepted");
    } else {
      _logMessage("Sent LoadRejected to #$iSock");
      socket.write("LoadRejected");
    }

    /*final jsonObj = jsonDecode(result);
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
    }*/
  }

  bool canStore(WasteType type, double weight) {
    if (!_storage.containsKey(type)) {
      return false;
    }
    return _storage[type]! + weight <= _maxStorages[type]!;
  }

  void deposit(double weight, WasteType type) {
    if (canStore(type, weight)) {
      _storage[type] = _storage[type]! + weight;
    }
  }

  String getCurrentStorage() {
    String res = "";

    for (WasteType type in _storage.keys) {
      res += "${type.name}: ${_storage[type]}/${_maxStorages[type]} KG\n";
    }

    return res;
  }
}

void main() async {
  MockWasteServer mockServer = MockWasteServer();

  print(mockServer.getCurrentStorage());

  mockServer.startServer(4001, (data, sock, iSock) {
    StoreRequest receivedSR =
        StoreRequest.fromJsonString(String.fromCharCodes(data));

    if (mockServer.canStore(receivedSR.wasteType, receivedSR.wasteWeight)) {
      print("[Mock_WasteServer] Load accepted.");
      mockServer.deposit(receivedSR.wasteWeight, receivedSR.wasteType);
      sock.write("LoadAccepted");
    } else {
      print("[Mock_WasteServer] Load rejected.");
      sock.write("LoadRejected");
    }
    print(mockServer.getCurrentStorage());
  }, onConnect: (sock, iSock) {
    print(
        "[Mock_WasteServer] Accepted connection #$iSock from client ${sock.remoteAddress.address}:${sock.port}.");
  }, onDisconnect: (sock, iSock) {
    print(
        "[Mock_WasteServer] Client ${sock.remoteAddress.address}:${sock.port} disconnected.");
  }).then((_) {
    print("[Mock_WasteServer] Listening for connections...");
  });

  ClientConnection conn = TcpClientConnection();
  await conn.connect("127.0.0.1", 4001);
  conn.listen((data) {
    print(String.fromCharCodes(data));
  });
  conn.sendMessage(StoreRequest(WasteType.PLASTIC, 10.0).toJsonString());
  await Future.delayed(const Duration(seconds: 1));
  conn.sendMessage(StoreRequest(WasteType.GLASS, 1000.0).toJsonString());
}
