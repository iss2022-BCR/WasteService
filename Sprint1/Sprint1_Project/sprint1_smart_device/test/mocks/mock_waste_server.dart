import 'dart:io';

import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:sprint1_smart_device/model/appl_message.dart';
import 'package:sprint1_smart_device/model/networking/client_connection.dart';
import 'package:sprint1_smart_device/model/networking/tcp_client_connection.dart';
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';
import 'package:sprint1_smart_device/model/waste_service/types_request.dart';
import 'package:sprint1_smart_device/model/waste_service/waste_type.dart';

const String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";

const double defaultMax = 100.0;
const int defaultPickupDelay = 3;
const int defaultDepositDelay = 5;

class MockWasteServer {
  Map<String, double> _preStorage = {};
  Map<String, double> _storage = {};
  Map<String, double> _storageCapacity = {};

  int _pickupDelay = 0;
  int _depositDelay = 0;

  bool _enableLog = false;

  late ServerSocket _serverSocket;
  int _connectedSocket = 0;

  MockWasteServer({int? pickupDelay, int? depositDelay, bool? enableLog}) {
    for (WasteType type in WasteType.values) {
      _preStorage[type.name] = 0.0;
      _storage[type.name] = 0.0;
      _storageCapacity[type.name] = defaultMax;
    }

    _pickupDelay = pickupDelay ?? defaultPickupDelay;
    _depositDelay = depositDelay ?? defaultDepositDelay;
    _enableLog = enableLog ?? false;
  }
  MockWasteServer.withCapacities(Map<String, double> storageCapacity,
      {int? pickupDelay, int? depositDelay, bool? enableLog}) {
    for (WasteType type in WasteType.values) {
      _storage[type.name] = 0.0;
      _storageCapacity[type.name] = storageCapacity[type.name] ?? defaultMax;
    }

    _pickupDelay = pickupDelay ?? defaultPickupDelay;
    _depositDelay = depositDelay ?? defaultDepositDelay;
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

  void handleMessage(
      Uint8List data, Socket socket, int iSock, bool enableLog) async {
    String result = String.fromCharCodes(data);

    if (result.contains("typesrequest")) {
      ApplMessage req = ApplMessage.fromString(result);
      // reply with types
      _logMessage("Sent TypesList to #$iSock: ${_storage.keys}");
      ApplMessage msg = ApplMessage.fromString(
          "msg(typesreply, reply, typesprovider, smartdevice, typesreply(${getTypesListString("_")}), ${req.msgNum + 1})");
      socket.write(msg.toString());
    } else {
      StoreRequest sr = StoreRequest.fromQAKString(result);

      // pre store
      if (canPreStore(sr.wasteType, sr.wasteWeight)) {
        // pickup
        await Future.delayed(Duration(seconds: _pickupDelay));
        _logMessage("Pickup completed.");
        _logMessage("Sent LoadAccepted to #$iSock");
        socket.write("LoadAccepted");

        // deposit
        await Future.delayed(Duration(seconds: _depositDelay));
        _logMessage("Deposit Completed.");
      } else {
        _logMessage("Sent LoadRejected to #$iSock");
        socket.write("LoadRejected");
      }
    }
  }

  String getTypesListString(String separator) {
    String res = "";

    for (String s in _storage.keys) {
      res += s;
      if (s != _storage.keys.last) {
        res += separator;
      }
    }

    return res;
  }

  bool canPreStore(String type, double weight) {
    if (!_preStorage.containsKey(type)) {
      return false;
    }
    return _preStorage[type]! + weight <= _storageCapacity[type]!;
  }

  bool canStore(String type, double weight) {
    if (!_storage.containsKey(type)) {
      return false;
    }
    return _storage[type]! + weight <= _storageCapacity[type]!;
  }

  // pre deposit
  void addToPreStorage(String type, double weight) {
    if (canPreStore(type, weight)) {
      _preStorage[type] = _preStorage[type]! + weight;
    }
  }

  // deposit
  void addToStorage(String type, double weight) {
    if (canStore(type, weight)) {
      _storage[type] = _storage[type]! + weight;
    }
  }

  String getStatusString(String type) {
    String res = "";

    if (_storage.containsKey(type)) {
      res =
          "$type: ${_preStorage[type]}/${_storage[type]}/${_storageCapacity[type]} KG\n";
    }

    return res;
  }

  String getFullStatusString() {
    String res = "";

    for (String type in _preStorage.keys) {
      res +=
          "$type: ${_preStorage[type]}/${_storage[type]}/${_storageCapacity[type]} KG\n";
    }

    return res;
  }
}

void main() async {
  MockWasteServer mockServer = MockWasteServer();

  print(mockServer.getFullStatusString());

  mockServer.startServer(11800, (data, sock, iSock) async {
    String result = String.fromCharCodes(data);
    print("[Mock_WasteServer] Received: $result");

    if (result.contains("typesrequest")) {
      ApplMessage req = ApplMessage.fromString(result);
      // reply with types
      print("[Mock_WasteServer] Sent TypesList to #$iSock");
      ApplMessage msg = ApplMessage.fromString(
          "msg(typesreply, reply, typesprovider, smartdevice, typesreply(${mockServer.getTypesListString("_")}), ${req.msgNum + 1})");
      sock.write(msg.toString());
    } else {
      StoreRequest sr = StoreRequest.fromQAKString(result);

      if (mockServer.canPreStore(sr.wasteType, sr.wasteWeight)) {
        // pre store & pickup
        mockServer.addToPreStorage(sr.wasteType, sr.wasteWeight);
        await Future.delayed(const Duration(seconds: defaultDepositDelay));
        print("[Mock_WasteServer] Load accepted.");
        mockServer.addToStorage(sr.wasteType, sr.wasteWeight);
        sock.write("LoadAccepted");

        // deposit
      } else {
        print("[Mock_WasteServer] Load rejected.");
        sock.write("LoadRejected");
      }
      print(mockServer.getFullStatusString());
    }
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
  await conn.connect("127.0.0.1", 11800);
  conn.listen((data) {
    print("  [Mock_SmartDevice] Received: ${String.fromCharCodes(data)}");
  });
  conn.sendMessage(TypesRequest().toQAKString("smartdevice", "typesprovider"));
  await Future.delayed(const Duration(seconds: defaultPickupDelay + 1));
  conn.sendMessage(StoreRequest("PLASTIC", 10.0)
      .toQAKString("smartdevice", "wasteservice", 3));
  await Future.delayed(const Duration(seconds: defaultPickupDelay + 1));
  conn.sendMessage(StoreRequest("GLASS", 1000.0)
      .toQAKString("smartdevice", "wasteservice", 5));
}
