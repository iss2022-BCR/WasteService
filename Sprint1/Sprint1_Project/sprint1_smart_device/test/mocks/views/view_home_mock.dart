// ignore_for_file: library_prefixes

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:sprint1_smart_device/model/constants.dart' as Constants;

class ViewHomeMock extends StatefulWidget {
  const ViewHomeMock(
      {Key? key,
      required this.correctIP,
      required this.correctPort,
      required this.timeoutSeconds})
      : super(key: key);

  final String correctIP;
  final int correctPort;
  final int timeoutSeconds;

  @override
  State<ViewHomeMock> createState() => _ViewHomeMockState();
}

class _ViewHomeMockState extends State<ViewHomeMock> {
  final _formKeyConnection = GlobalKey<FormState>();

  final TextEditingController _textControllerAddress = TextEditingController();
  final TextEditingController _textControllerPort = TextEditingController();

  late Timer _timer;
  late int _timeoutCounter;

  bool _isLoading = false;

  bool _alert = false;
  String _alertTitle = "";
  String _alertMessage = "";

  bool _checkIPaddress(String address) {
    if (address.isEmpty) return true;

    RegExp regexIPv4 = RegExp(r'^((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.?\b){4}$');

    return regexIPv4.hasMatch(address);
  }

  bool _checkPort(String port) {
    if (port.isEmpty) return true;

    final p = int.tryParse(port) ?? Constants.defaultPort;
    if (p >= 1024 && p <= 65535) return true;

    return false;
  }

  void _connect(String ip, int port) async {
    if (ip == widget.correctIP && port == widget.correctPort) {
      _showAlertDialog(
          "Connection Succeeded", "Successfully connected to $ip:$port");
    } else {
      _startTimer(ip, port);
    }
  }

  void _showAlertDialog(String title, String message) {
    setState(() {
      _alert = true;
      _alertTitle = title;
      _alertMessage = message;
    });
  }

  void _startTimer(String ip, int port) {
    setState(() {
      _isLoading = true;
      _timeoutCounter = widget.timeoutSeconds;
    });
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        if (_isLoading && _timeoutCounter > 0) {
          setState(() {
            _timeoutCounter--;
          });
        } else {
          _stopTimer();
          setState(() {
            _showAlertDialog("Connection Failed",
                "Couldn't connect to server at $ip:$port.");
          });
        }
      });
    });
  }

  void _stopTimer() {
    _timer.isActive ? _timer.cancel() : null;
    setState(() {
      _isLoading = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text(Constants.titleHome),
        centerTitle: true,
        automaticallyImplyLeading: true,
      ),
      body: Center(
        child: _alert
            ? AlertDialog(
                title: Center(child: Text(_alertTitle)),
                content: Text(_alertMessage),
                actions: <Widget>[
                  TextButton(
                    onPressed: () {
                      setState(() {
                        _alert = false;
                        _alertTitle = "";
                        _alertMessage = "";
                      });
                    },
                    child: const Text('OK'),
                  ),
                ],
              )
            : Padding(
                padding: const EdgeInsets.symmetric(horizontal: 50.0),
                child: Form(
                  key: _formKeyConnection,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      const Text(
                        'Connection',
                        style: TextStyle(fontSize: 24),
                      ),
                      TextFormField(
                        key: const ValueKey('connectionIP'),
                        controller: _textControllerAddress,
                        validator: (value) {
                          if (value == null || !_checkIPaddress(value)) {
                            return 'Invalid IPv4 address';
                          }
                          return null;
                        },
                        keyboardType: const TextInputType.numberWithOptions(
                            decimal: true),
                        inputFormatters: <TextInputFormatter>[
                          FilteringTextInputFormatter.allow(
                            RegExp(r'[0-9]+[.]{0,1}'),
                          )
                        ],
                        decoration: const InputDecoration(
                          border: UnderlineInputBorder(),
                          labelText: 'IP address',
                          hintText: Constants.defaultIP,
                        ),
                      ),
                      TextFormField(
                        key: const ValueKey('connectionPort'),
                        controller: _textControllerPort,
                        validator: (value) {
                          if (value == null || !_checkPort(value)) {
                            return 'Invalid port';
                          }
                          return null;
                        },
                        keyboardType: const TextInputType.numberWithOptions(
                            decimal: false),
                        inputFormatters: <TextInputFormatter>[
                          FilteringTextInputFormatter.allow(
                            RegExp(r'[0-9]+'),
                          )
                        ],
                        decoration: const InputDecoration(
                          border: UnderlineInputBorder(),
                          labelText: 'Port',
                          hintText: "${Constants.defaultPort}",
                        ),
                      ),
                      const SizedBox(height: 100.0),
                    ],
                  ),
                ),
              ),
      ),
      // CONNECT
      floatingActionButton: Padding(
        padding: const EdgeInsets.only(top: 32.0, left: 32.0, right: 32.0),
        child: ElevatedButton(
          key: const ValueKey('connect'),
          onPressed: _alert
              ? null
              : () {
                  if (_formKeyConnection.currentState!.validate()) {
                    String ip = _textControllerAddress.text;
                    String port = _textControllerPort.text;

                    if (ip.isEmpty) {
                      setState(() {
                        _textControllerAddress.text = Constants.defaultIP;
                      });
                      ip = Constants.defaultIP;
                    }
                    if (port.isEmpty) {
                      setState(() {
                        _textControllerPort.text =
                            Constants.defaultPort.toString();
                      });
                      port = Constants.defaultPort.toString();
                    }
                    _connect(ip, int.parse(port));
                  }
                },
          style: ElevatedButton.styleFrom(
              textStyle: const TextStyle(fontSize: 22),
              minimumSize: const Size.fromHeight(56.0),
              shape: const StadiumBorder()),
          child: _isLoading
              ? Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: const [
                    CircularProgressIndicator(color: Colors.white),
                    Padding(
                      padding: EdgeInsets.only(left: 16.0, right: 8.0),
                      child: Text(
                        'Connection Attempt...',
                      ),
                    ),
                  ],
                )
              : const Text(
                  'Connect',
                ),
        ),
      ),
      floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
    );
  }
}

// DEBUG TEST
void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'SmartDevice Simulator',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const ViewHomeMock(
          correctIP: Constants.defaultIP,
          correctPort: Constants.defaultPort,
          timeoutSeconds: 5),
    );
  }
}
