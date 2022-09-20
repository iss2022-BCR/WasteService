import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'view_request.dart';

const String defaultAddress = "127.0.0.1";
const int defaultPort = 4001;

class ViewHome extends StatefulWidget {
  const ViewHome({Key? key}) : super(key: key);

  @override
  State<ViewHome> createState() => _ViewHomeState();
}

class _ViewHomeState extends State<ViewHome> {
  final _formKeyConnection = GlobalKey<FormState>();

  final TextEditingController _textControllerAddress = TextEditingController();
  final TextEditingController _textControllerPort = TextEditingController();

  bool _isLoading = false;

  bool _error = false;
  String _errorTitle = "";
  String _errorMessage = "";

  bool _checkIPaddress(String address) {
    if (address.isEmpty) return true;

    RegExp regexIPv4 = RegExp(r'^((25[0-5]|(2[0-4]|1\d|[1-9]|)\d)\.?\b){4}$');

    return regexIPv4.hasMatch(address);
  }

  bool _checkPort(String port) {
    if (port.isEmpty) return true;

    final p = int.tryParse(port) ?? defaultPort;
    if (p >= 1024 && p <= 65535) return true;

    return false;
  }

  Future<Socket?> _connect(String ip, int port) async {
    Socket? res;
    setState(() {
      _error = false;
    });

    try {
      res = await Socket.connect(ip, port, timeout: const Duration(seconds: 5));
    } catch (e) {
      _showAlertDialog("Connection Failed", e.toString());
      res = null;
    }
    return res;
  }

  void _showAlertDialog(String title, String message) {
    setState(() {
      _error = true;
      _errorTitle = title;
      _errorMessage = message;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Smart Device Simulator'),
        centerTitle: true,
        automaticallyImplyLeading: true,
      ),
      body: Center(
        child: _error
            ? AlertDialog(
                title: Center(child: Text(_errorTitle)),
                content: Text(_errorMessage),
                actions: <Widget>[
                  TextButton(
                    onPressed: () {
                      setState(() {
                        _error = false;
                        _errorTitle = "";
                        _errorMessage = "";
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
                          hintText: defaultAddress,
                        ),
                      ),
                      TextFormField(
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
                          hintText: "$defaultPort",
                        ),
                      ),
                      const SizedBox(height: 100.0),
                    ],
                  ),
                ),
              ),
      ),
      floatingActionButton: Padding(
        padding: const EdgeInsets.only(top: 32.0, left: 32.0, right: 32.0),
        child: ElevatedButton(
          onPressed: _error
              ? null
              : () {
                  if (_formKeyConnection.currentState!.validate()) {
                    String ip = _textControllerAddress.text;
                    String port = _textControllerPort.text;

                    setState(() {
                      _isLoading = true;

                      if (ip.isEmpty) {
                        _textControllerAddress.text = defaultAddress;
                        ip = defaultAddress;
                      }
                      if (port.isEmpty) {
                        _textControllerPort.text = defaultPort.toString();
                        port = defaultPort.toString();
                      }
                    });
                    _connect(ip, int.tryParse(port) ?? defaultPort)
                        .then((socket) {
                      setState(() {
                        _isLoading = false;
                      });
                      if (socket != null) {
                        Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => ViewRequest(
                                    socket: socket,
                                    notifyParent: _showAlertDialog)));
                      } else {
                        setState(() {
                          _error = true;
                          _errorTitle = "Connection failed";
                          _errorMessage =
                              "Couldn't connect to server at $ip:$port";
                        });
                      }
                    });
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
                        'Connecting Attempt...',
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
