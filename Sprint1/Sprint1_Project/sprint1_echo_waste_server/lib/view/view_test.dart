import 'dart:io';
import 'dart:typed_data';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const List<String> materialTypes = <String>['PLASTIC', 'GLASS'];

class ViewTest extends StatefulWidget {
  const ViewTest({Key? key}) : super(key: key);

  @override
  State<ViewTest> createState() => _ViewTestState();
}

class _ViewTestState extends State<ViewTest> {
  String _distance = "";
  String _angle = "";
  String _status = "";
  String _message = "";

  Socket? _socket;
  final String _host = "192.168.1.46";
  final int _port = 4001;

  Future<void> _sendMessage(String message) async {
    print("Sent message $message");
    _socket!.write(message);
    await Future.delayed(const Duration(seconds: 1));
  }

  void _connect(String ip, int port) async {
    Socket? sock;

    sock = await Socket.connect(ip, port, timeout: const Duration(seconds: 3));

    _socket = sock;
    setState(() {
      _status = "Connected to $ip:$port.";
      _message = "";
    });

    // listen for responses from the server
    _socket!.listen(
      // handle data from the server
      (Uint8List data) {
        final serverResponse = String.fromCharCodes(data);
        setState(() {
          _message = serverResponse;
        });
        print(serverResponse);
      },

      // handle errors
      onError: (error) {
        setState(() {
          _status = "Disconnected.";
          _message = "Error: $error";
        });
        print("Error: $error");
        //_socket!.destroy();
        //_socket = null;
      },

      // handle server ending connection
      onDone: () {
        setState(() {
          _status = "Disconnected.";
          _message = 'Server left.';
        });
        print('Server left.');
        //_socket!.destroy();
        //_socket = null;
      },
    );
  }

  void _disconnect() {
    if (_socket != null) _socket!.destroy();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Center(child: Text("Radar Test")),
      ),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.symmetric(horizontal: 50.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              ElevatedButton(
                onPressed: () {
                  setState(() {
                    _status = "";
                    _message = "";
                  });
                  _disconnect();
                  _connect(_host, _port);
                },
                child: Padding(
                  padding: const EdgeInsets.all(8.0),
                  child: Text(
                    _socket == null ? 'Connect' : 'Reconnect',
                    style: const TextStyle(fontSize: 22.0),
                  ),
                ),
              ),
              const SizedBox(height: 50.0),
              TextField(
                onChanged: (text) {
                  _distance = text;
                },
                keyboardType:
                    const TextInputType.numberWithOptions(decimal: false),
                inputFormatters: <TextInputFormatter>[
                  FilteringTextInputFormatter.allow(
                    RegExp(r'[0-9]+'), // this regex allows only decimal numbers
                  )
                ],
                decoration: const InputDecoration(
                  hintText: '100',
                  border: UnderlineInputBorder(),
                  labelText: 'Distance',
                ),
              ),
              TextField(
                onChanged: (text) {
                  _angle = text;
                },
                keyboardType:
                    const TextInputType.numberWithOptions(decimal: false),
                inputFormatters: <TextInputFormatter>[
                  FilteringTextInputFormatter.allow(
                    RegExp(r'[0-9]+'), // this regex allows only decimal numbers
                  )
                ],
                decoration: const InputDecoration(
                  hintText: '90',
                  border: UnderlineInputBorder(),
                  labelText: 'Angle',
                ),
              ),
              const SizedBox(height: 50.0),
              Text("Status: $_status"),
              Text("Message: $_message"),
            ],
          ),
        ),
      ),
      floatingActionButton: ElevatedButton(
        onPressed: _socket == null
            ? null
            : () {
                // test
                _sendMessage(
                    '{"distance": ${_distance.isEmpty ? 100 : int.parse(_distance)}, "angle": ${_angle.isEmpty ? 90 : int.parse(_angle)}}');
              },
        child: const Padding(
          padding: EdgeInsets.all(8.0),
          child: Text(
            'Send Radar Distance',
            style: TextStyle(fontSize: 22.0),
          ),
        ),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
    );
  }
}
