import 'dart:convert';
import 'dart:io';
import 'dart:math';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:sprint1_echo_waste_server/model/appl_message.dart';
import 'package:sprint1_echo_waste_server/model/waste_service/waste_service.dart';

import '../model/waste_service/store_request.dart';

const String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";

const defaultPort = 11800;

class ViewServer extends StatefulWidget {
  const ViewServer({Key? key}) : super(key: key);

  @override
  State<ViewServer> createState() => _ViewServerState();
}

class _ViewServerState extends State<ViewServer> {
  final ScrollController _scrollController = ScrollController();

  ServerSocket? _serverSocket;
  int _connectedSocket = 0;

  final List<String> _messages = [];

  final WasteService _wasteService =
      WasteService(["PLASTIC", "GLASS"], capacity: 100.0);

  Future<void> _startServer(String address, int port) async {
    _serverSocket = await ServerSocket.bind(address, port).then((server) {
      _logMessage(
          "Server listening on: ${server.address.address}:${server.port}");
      server.listen((socket) {
        _connectedSocket++;
        int iSock = _connectedSocket;
        _logMessage(
            "Accepted connection #$_connectedSocket: ${socket.remoteAddress.address}:${socket.remotePort}");

        socket.listen((Uint8List data) {
          _handleMessage(data, socket, iSock);
        }, onError: (error) {
          _logMessage("Error from #$iSock: ${error.toString()}");
          _connectedSocket--;
          print(error.toString()); // test
        }, onDone: () {
          _logMessage("Client #$iSock disconnected.");
          _connectedSocket--;
        }, cancelOnError: true);
      });
    });
  }

  String _getTimeStamp() {
    final DateTime timestamp = DateTime.now();

    final String result = "[${DateFormat(dateTimeFormat).format(timestamp)}]";

    return result;
  }

  void _logMessage(String message) {
    setState(() {
      _messages.add("${_getTimeStamp()} $message");
    });
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
    });
  }

  void _handleMessage(Uint8List data, Socket socket, int iSock) {
    String result = String.fromCharCodes(data);
    _logMessage("Message from #$iSock: $result");

    if (result.contains("typesrequest")) {
      ApplMessage msg = ApplMessage.fromString(
          "msg(typesreply, reply, typesprovider, smartdevice, typesreply(${_wasteService.getTypesListString("_")}), ${2})");
      _logMessage("Replied to #$iSock with: ${msg.toString()}");
      socket.write(msg.toString());
    } else {
      StoreRequest receivedSR =
          StoreRequest.fromQAKString(String.fromCharCodes(data));

      if (_wasteService.canPreStore(
          receivedSR.wasteType, receivedSR.wasteWeight)) {
        setState(() {
          _wasteService.addToPreStorage(
              receivedSR.wasteType, receivedSR.wasteWeight);
          // pickup
          ApplMessage msg = ApplMessage.fromString(
              "msg(loadaccepted, reply, wasteservice, smartdevice, loadaccepted), 3)");
          _logMessage("Sent LoadAccepted to #$iSock: ${msg.toString()}");
          socket.write(msg.toString());

          // deposit
          _wasteService.addToStorage(
              receivedSR.wasteType, receivedSR.wasteWeight);
        });
      } else {
        ApplMessage msg = ApplMessage.fromString(
            "msg(loadrejected, reply, wasteservice, smartdevice, loadrejected), 3)");
        _logMessage("Sent LoadRejected to #$iSock: ${msg.toString()}");
        socket.write(msg.toString());
      }

      /*final String wasteType = receivedSR.wasteType;
      final double wasteWeight = receivedSR.wasteWeight;

      ApplMessage msg = ApplMessage.fromString(
          "msg(loadaccepted, reply, wasteservice, smartdevice, loadaccepted, ${4})");

      if (wasteWeight == -1.0) {
        _logMessage("#$iSock INVALID MESSAGE");
        return;
      }

      switch (wasteType) {
        case "PLASTIC":
          if (_plastic + wasteWeight <= _max_plastic) {
            setState(() {
              _plastic += wasteWeight;
            });
            _logMessage("Sent LoadAccepted to #$iSock");
            socket.write("LoadAccepted");
          } else {
            _logMessage("Sent LoadRejected to #$iSock");
            socket.write("LoadRejected");
          }
          break;
        case "GLASS":
          if (_glass + wasteWeight <= _max_glass) {
            setState(() {
              _glass += wasteWeight;
            });
            _logMessage("Sent LoadAccepted to #$iSock");
            socket.write("LoadAccepted");
          } else {
            _logMessage("Sent LoadRejected to #$iSock");
            socket.write("LoadRejected");
          }
          break;
        default:
          _logMessage("#$iSock INVALID MESSAGE (wasteType)");
          break;
      }
      socket.write(msg.toString());*/
    }

    /*StoreRequest receivedSR =
        StoreRequest.fromQAKString(String.fromCharCodes(data));

    //print(jsonObj['wasteWeight']); // test
    //print(jsonObj['wasteType']); // test

    final wasteWeight = receivedSR.wasteWeight;
    final wasteType = receivedSR.wasteType;

    if (wasteWeight == -1.0) {
      _logMessage("#$iSock INVALID MESSAGE");
      return;
    }

    switch (wasteType.name) {
      case "PLASTIC":
        if (_plastic + wasteWeight <= _max_plastic) {
          setState(() {
            _plastic += wasteWeight;
          });
          _logMessage("Sent LoadAccepted to #$iSock");
          socket.write("LoadAccepted");
        } else {
          _logMessage("Sent LoadRejected to #$iSock");
          socket.write("LoadRejected");
        }
        break;
      case "GLASS":
        if (_glass + wasteWeight <= _max_glass) {
          setState(() {
            _glass += wasteWeight;
          });
          _logMessage("Sent LoadAccepted to #$iSock");
          socket.write("LoadAccepted");
        } else {
          _logMessage("Sent LoadRejected to #$iSock");
          socket.write("LoadRejected");
        }
        break;
      default:
        _logMessage("#$iSock INVALID MESSAGE (wasteType)");
        break;
    }*/
  }

  /*bool canStore(WasteType type, double weight) {
    if (!_storage.containsKey(type)) {
      return false;
    }
    return _storage[type]! + weight <= _maxStorages[type]!;
  }*/

  @override
  void didChangeDependencies() {
    Locale myLocale = Localizations.localeOf(context);
    Intl.defaultLocale = myLocale.toString();
    _startServer("0.0.0.0", defaultPort);
    super.didChangeDependencies();
  }

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('WasteService Simulator'),
        centerTitle: true,
        /*actions: [
              Column(
                children: [
                  const Text('Log'),
                  Switch(
                      materialTapTargetSize: MaterialTapTargetSize.shrinkWrap,
                      inactiveTrackColor: const Color.fromARGB(255, 130, 0, 0),
                      inactiveThumbColor: Colors.red,
                      activeTrackColor: Colors.green[800],
                      activeColor: Colors.green,
                      value: _debug,
                      onChanged: (val) {
                        setState(() {
                          _debug = !_debug;
                        });
                      }),
                ],
              )
            ],*/
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(20.0),
          child: Column(
            children: [
              const Text(
                "Storage",
                style: TextStyle(fontSize: 22.0, fontWeight: FontWeight.bold),
              ),
              Row(
                children: [
                  Text(
                    "${_wasteService.getStatusString("PLASTIC")}",
                    style: TextStyle(
                        fontSize: 20.0,
                        fontFeatures: const [FontFeature.tabularFigures()],
                        color: _wasteService.canPreStore("PLASTIC", 1.0)
                            ? null
                            : Colors.red),
                  )
                ],
              ),
              Row(
                children: [
                  Text(
                    "${_wasteService.getStatusString("GLASS")}",
                    style: TextStyle(
                        fontSize: 20.0,
                        fontFeatures: const [FontFeature.tabularFigures()],
                        color: _wasteService.canPreStore("GLASS", 1.0)
                            ? null
                            : Colors.red),
                  )
                ],
              ),
              const SizedBox(height: 20.0),
              const Text(
                "Log",
                style: TextStyle(fontSize: 22.0, fontWeight: FontWeight.bold),
              ),
              Expanded(
                child: Container(
                  color: Colors.black,
                  child: Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: ListView.builder(
                      controller: _scrollController,
                      itemCount: _messages.length,
                      itemBuilder: (_, index) => Padding(
                        padding: const EdgeInsets.only(bottom: 8.0),
                        child: Text(
                          _messages[index],
                          style: TextStyle(
                            color: _messages[index]
                                    .toLowerCase()
                                    .contains('loadaccepted')
                                ? Colors.green
                                : _messages[index]
                                        .toLowerCase()
                                        .contains('loadrejected')
                                    ? Colors.red
                                    : Colors.white,
                            fontFeatures: const [FontFeature.tabularFigures()],
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ),
              const SizedBox(
                height: 20.0,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Text(
                    "Plastic: ",
                    style: TextStyle(fontSize: 20.0),
                  ),
                  Ink(
                    height: 80.0,
                    decoration: const ShapeDecoration(
                      color: Colors.blue,
                      shape: CircleBorder(),
                    ),
                    child: IconButton(
                      onPressed: () {
                        setState(() {
                          _wasteService.resetStorage("PLASTIC");
                        });
                      },
                      iconSize: 40.0,
                      icon: const Icon(Icons.refresh),
                      color: Colors.white,
                    ),
                  ),
                  Column(
                    children: [
                      Ink(
                        height: 40.0,
                        decoration: const ShapeDecoration(
                          color: Colors.blue,
                          shape: CircleBorder(),
                        ),
                        child: IconButton(
                          onPressed: () {
                            setState(() {
                              _wasteService.setCapacity(
                                  "PLASTIC",
                                  min(
                                      100000.0,
                                      _wasteService.getCapacity("PLASTIC") +
                                          10.0));
                            });
                          },
                          iconSize: 20.0,
                          icon: const Icon(Icons.add),
                          color: Colors.white,
                        ),
                      ),
                      const SizedBox(height: 5.0),
                      Ink(
                        height: 40.0,
                        decoration: const ShapeDecoration(
                          color: Colors.blue,
                          shape: CircleBorder(),
                        ),
                        child: IconButton(
                          onPressed: () {
                            setState(() {
                              _wasteService.setCapacity(
                                  "PLASTIC",
                                  max(
                                      20.0,
                                      _wasteService.getCapacity("PLASTIC") -
                                          10.0));
                            });
                          },
                          iconSize: 20.0,
                          icon: const Icon(Icons.remove),
                          color: Colors.white,
                        ),
                      ),
                    ],
                  ),
                  const Spacer(),
                  const Text(
                    "Glass: ",
                    style: TextStyle(fontSize: 20.0),
                  ),
                  Ink(
                    height: 80.0,
                    decoration: const ShapeDecoration(
                      color: Colors.blue,
                      shape: CircleBorder(),
                    ),
                    child: IconButton(
                      onPressed: () {
                        setState(() {
                          _wasteService.resetStorage("GLASS");
                        });
                      },
                      iconSize: 40.0,
                      icon: const Icon(Icons.refresh),
                      color: Colors.white,
                    ),
                  ),
                  Column(
                    children: [
                      Ink(
                        height: 40.0,
                        decoration: const ShapeDecoration(
                          color: Colors.blue,
                          shape: CircleBorder(),
                        ),
                        child: IconButton(
                          onPressed: () {
                            setState(() {
                              _wasteService.setCapacity(
                                  "GLASS",
                                  min(
                                      100000.0,
                                      _wasteService.getCapacity("GLASS") +
                                          10.0));
                            });
                          },
                          iconSize: 20.0,
                          icon: const Icon(Icons.add),
                          color: Colors.white,
                        ),
                      ),
                      const SizedBox(height: 5.0),
                      Ink(
                        height: 40.0,
                        decoration: const ShapeDecoration(
                          color: Colors.blue,
                          shape: CircleBorder(),
                        ),
                        child: IconButton(
                          onPressed: () {
                            setState(() {
                              _wasteService.setCapacity(
                                  "GLASS",
                                  max(
                                      20.0,
                                      _wasteService.getCapacity("GLASS") -
                                          10.0));
                            });
                          },
                          iconSize: 20.0,
                          icon: const Icon(Icons.remove),
                          color: Colors.white,
                        ),
                      ),
                    ],
                  )
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
