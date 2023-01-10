// ignore_for_file: library_prefixes

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:sprint1_smart_device/model/networking/tcp_client_connection.dart';

import '../model/appl_message.dart';
import '../model/networking/client_connection.dart';
import '../model/settings.dart';
import '../widgets/side_menu_widget.dart';
import 'view_request.dart';

import '../model/constants.dart' as Constants;

class ViewHome extends StatefulWidget {
  const ViewHome({Key? key}) : super(key: key);

  @override
  State<ViewHome> createState() => _ViewHomeState();
}

class _ViewHomeState extends State<ViewHome> {
  final Settings _settings = Settings();

  final _formKeyConnection = GlobalKey<FormState>();

  final TextEditingController _textControllerAddress = TextEditingController();
  final TextEditingController _textControllerPort = TextEditingController();

  late ClientConnection _tcpClientConnection;

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

    final p = int.tryParse(port) ?? Constants.defaultPort;
    if (p >= 1024 && p <= 65535) return true;

    return false;
  }

  void _connect(String ip, int port) async {
    setState(() {
      _isLoading = true;
    });
    _tcpClientConnection = TcpClientConnection();
    _tcpClientConnection
        .connect(ip, port, timeout: const Duration(seconds: 5))
        .then(
      (value) {
        setState(() {
          _isLoading = false;
        });
        Navigator.push(
          context,
          MaterialPageRoute(
            builder: (context) => ViewRequest(
                settings: _settings,
                connection: _tcpClientConnection,
                notifyParent: _showAlertDialog),
          ),
        );
      },
    ).onError((error, stackTrace) {
      setState(() {
        _isLoading = false;
      });
      _showAlertDialog(
          "Connection Failed", "Couldn't connect to server at $ip:$port.");
    });
  }

  void _showAlertDialog(String title, String message) {
    setState(() {
      _error = true;
      _errorTitle = title;
      _errorMessage = message;
    });
  }

  void _saveSettings(bool sendTypesReq, String msgID, ApplMessageType msgType,
      String msgSender, String msgReceiver) {
    setState(() {
      _settings.sendTypesRequest = sendTypesReq;
      _settings.messageID = msgID;
      _settings.messageType = msgType;
      _settings.messageSender = msgSender;
      _settings.messageReceiver = msgReceiver;
      _settings.saveSettings();
    });
  }

  @override
  void initState() {
    super.initState();

    _settings.loadSettings();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      drawer: NavDrawer(settings: _settings, saveSettings: _saveSettings),
      appBar: AppBar(
        title: const Text(Constants.titleHome),
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
          onPressed: _error
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
