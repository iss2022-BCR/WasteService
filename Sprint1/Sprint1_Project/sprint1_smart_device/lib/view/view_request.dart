// ignore_for_file: library_prefixes

import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:sprint1_smart_device/model/appl_message.dart';
import 'package:sprint1_smart_device/model/networking/client_connection.dart';
import 'package:sprint1_smart_device/model/waste_service/types_request.dart';
import '../model/waste_service/store_request.dart';

import '../model/constants.dart' as Constants;

class ViewRequest extends StatefulWidget {
  ViewRequest({Key? key, required this.connection, required this.notifyParent})
      : super(key: key);

  ClientConnection connection;
  final Function(String, String) notifyParent;

  @override
  State<ViewRequest> createState() => _ViewRequestState();
}

class _ViewRequestState extends State<ViewRequest> {
  final _formKeyRequest = GlobalKey<FormState>();
  final TextEditingController _textControllerWeight = TextEditingController();
  final ScrollController _scrollController = ScrollController();

  bool _debug = false;

  List<String> _wasteTypes = [];

  String _currentWasteType = "";
  String _reply = "";

  bool _waitingReply = false;
  String _waitingDots = "Waiting.";
  Timer? _timer;

  // Message list
  final List<String> _messages = [];

  bool _checkWeight(String weight) {
    if (weight.isEmpty) return false;

    final w = double.tryParse(weight) ?? -1.0;
    if (w >= 1.0 && w <= maxWasteWeight) return true;

    return false;
  }

  void _logMessage(String msg) {
    setState(() {
      _messages.add("${_getTimeStamp()} $msg");
    });
    if (_debug) {
      WidgetsBinding.instance.addPostFrameCallback((_) {
        _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
      });
    }
  }

  String _getTimeStamp() {
    final DateTime timestamp = DateTime.now();

    final String result =
        "[${DateFormat(Constants.dateTimeFormat).format(timestamp)}]";

    return result;
  }

  Future<void> _sendTypesRequest() async {
    setState(() {
      _waitingReply = true;
      _reply = "";
    });
    TypesRequest req = TypesRequest();

    String msg = req.toQAKString("smartdevice", "typesprovider");
    _logMessage("Types Request: $msg");
    widget.connection.sendMessage(msg);

    _startTimer();
  }

  Future<void> _sendStoreRequest({timeout = Duration}) async {
    setState(() {
      _waitingReply = true;
      _reply = "";
    });
    StoreRequest req = StoreRequest(
        _currentWasteType, double.parse(_textControllerWeight.text));

    String msg = req.toQAKString("smartdevice", "wasteservice", 3);
    _logMessage("Store Request: $msg");
    widget.connection.sendMessage(msg);

    if (timeout != null) {
      _startTimer();
    }
  }

  List<String> _parseTypes(String types, String sep) {
    return types.split(sep);
  }

  void _messageHandler(Uint8List data) {
    final serverReply = String.fromCharCodes(data);
    ApplMessage msg = ApplMessage.fromString(serverReply);

    setState(() {
      // TypesRequest Reply
      if (serverReply.contains('typesreply')) {
        _waitingReply = false;
        _wasteTypes = _parseTypes(msg.getContentWithoutId(), '_');
        _wasteTypes.sort();
        _currentWasteType = _wasteTypes[0];
      } else
      // StoreRequest Reply
      if (serverReply.contains("load")) {
        _stopTimer();
        _reply = msg.msgId.toLowerCase().contains('accepted')
            ? "Accepted"
            : "Rejected";
      }
    });
    _logMessage(serverReply);
  }

  void _errorHandler(error) {
    _stopTimer();
    widget.notifyParent(
        "Disconnected", "You have been disconnected by the server.");
    //widget.notifyParent("Disconnected", error.toString());

    widget.connection.destroy();
    if (Navigator.canPop(context)) {
      Navigator.pop(context);
    }
  }

  void _doneHandler() {
    _stopTimer();
    widget.notifyParent("Disconnected", "Connection closed by the server.");

    widget.connection.destroy();
    if (Navigator.canPop(context)) {
      Navigator.pop(context);
    }
  }

  void _startTimer() {
    setState(() {
      _waitingDots = "Waiting.";
      //_timeoutCounter = Constants.timeoutSeconds;
      _waitingReply = true;
    });
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        if (_waitingReply) {
          setState(() {
            if (_waitingDots == "Waiting...") {
              _waitingDots = "Waiting.";
            } else {
              _waitingDots += ".";
            }
          });
        } else {
          _stopTimer();
          setState(() {
            _reply = "";
          });
        }
      });
    });
  }

  void _stopTimer() {
    if (_timer == null) {
      return;
    }
    _timer!.isActive ? _timer!.cancel() : null;
    setState(() {
      _waitingReply = false;
    });
  }

  @override
  void didChangeDependencies() {
    Locale myLocale = Localizations.localeOf(context);
    Intl.defaultLocale = myLocale.toString();
    _logMessage(
        "Connected to ${widget.connection.address.address}:${widget.connection.remotePort}");

    // Send TypesRequest
    _sendTypesRequest();
    // Loading...
    // Can send StoreRequest

    super.didChangeDependencies();
  }

  @override
  void initState() {
    super.initState();

    // listen for replies from the server
    widget.connection.listen(_messageHandler,
        onError: _errorHandler, onDone: _doneHandler, cancelOnError: true);
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        _stopTimer();
        widget.notifyParent(
            "Disconnected", "You disconnecred from the server.");
        widget.connection.close();

        return true;
      },
      child: Scaffold(
        appBar: AppBar(
            title: const Text(Constants.titleRequest),
            centerTitle: true,
            automaticallyImplyLeading: true,
            leading: IconButton(
              icon: const Icon(Icons.arrow_back_ios),
              onPressed: () {
                //_stopTimer();
                widget.notifyParent(
                    "Disconnected", "You disconnecred from the server.");
                widget.connection.close();

                if (Navigator.canPop(context)) {
                  Navigator.pop(context);
                }
              },
            ),
            actions: [
              Column(
                children: [
                  const Text('Debug'),
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
                        if (_debug) {
                          WidgetsBinding.instance.addPostFrameCallback((_) {
                            _scrollController.jumpTo(
                                _scrollController.position.maxScrollExtent);
                          });
                        }
                      }),
                ],
              )
            ]),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.symmetric(horizontal: 50.0),
            child: Form(
              key: _formKeyRequest,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  const Spacer(flex: 1),
                  _wasteTypes.isEmpty
                      ? const Center(child: CircularProgressIndicator())
                      : Column(
                          crossAxisAlignment: CrossAxisAlignment.center,
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            const Text(
                              'Request Parameters',
                              style: TextStyle(fontSize: 26.0),
                            ),
                            DropdownButtonFormField<String>(
                              key: const ValueKey('parameterWasteType'),
                              value: _currentWasteType,
                              items: _wasteTypes.map<DropdownMenuItem<String>>(
                                  (String value) {
                                return DropdownMenuItem(
                                    value: value, child: Text(value));
                              }).toList(),
                              onChanged: (newValue) {
                                setState(() {
                                  _currentWasteType = newValue!;
                                });
                              },
                              decoration: const InputDecoration(
                                border: UnderlineInputBorder(),
                                labelText: 'Waste Type',
                              ),
                            ),
                            TextFormField(
                              key: const ValueKey('parameterWasteWeight'),
                              validator: (value) {
                                if (value == null || !_checkWeight(value)) {
                                  return 'Invalid weight';
                                }
                                return null;
                              },
                              controller: _textControllerWeight,
                              keyboardType:
                                  const TextInputType.numberWithOptions(
                                      decimal: true),
                              inputFormatters: <TextInputFormatter>[
                                FilteringTextInputFormatter.allow(
                                  RegExp(
                                      r'[0-9]+[,.]{0,1}[0-9]*'), // this regex allows only decimal numbers
                                )
                              ],
                              decoration: const InputDecoration(
                                suffixText: 'KG',
                                hintText: 'Weight',
                                border: UnderlineInputBorder(),
                                labelText: 'Waste Weight',
                              ),
                            ),
                          ],
                        ),
                  const SizedBox(height: 20.0),
                  Text(
                    _debug ? 'Log' : 'Reply',
                    style: const TextStyle(fontSize: 26.0),
                  ),
                  Padding(
                    padding: const EdgeInsets.symmetric(vertical: 10.0),
                    child: _debug
                        ?
                        // Log
                        Container(
                            width: double.infinity,
                            height: 150.0,
                            decoration: BoxDecoration(
                              color: Colors.black.withOpacity(0.7),
                              border: Border.all(
                                color: Colors.black.withOpacity(0.7),
                              ),
                            ),
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
                                              .contains('loadaccepted')
                                          ? Colors.green
                                          : _messages[index]
                                                  .contains('loadrejected')
                                              ? Colors.red
                                              : Colors.white,
                                      fontFeatures: const [
                                        FontFeature.tabularFigures()
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          )
                        // Reply
                        : Container(
                            width: double.infinity,
                            height: 40.0,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8.0),
                              color: _reply.toLowerCase().contains("accepted")
                                  ? Colors.green
                                  : _reply.toLowerCase().contains("rejected")
                                      ? Colors.red
                                      : Colors.grey,
                            ),
                            alignment: Alignment.center,
                            child: Text(_waitingReply ? _waitingDots : _reply,
                                style: const TextStyle(fontSize: 16.0))),
                  ),
                  const Spacer(flex: 2),
                ],
              ),
            ),
          ),
        ),
        floatingActionButton: Padding(
          padding: const EdgeInsets.only(top: 32.0, left: 32.0, right: 32.0),
          child: ElevatedButton(
            key: const ValueKey('sendStoreRequest'),
            onPressed: _waitingReply
                ? null
                : () {
                    if (_formKeyRequest.currentState!.validate()) {
                      // Add '.0' or '0' to the weight, in case it's missing
                      setState(() {
                        if (!_textControllerWeight.text.contains('.')) {
                          _textControllerWeight.text += '.0';
                        }
                        if (_textControllerWeight.text.endsWith('.')) {
                          _textControllerWeight.text += '0';
                        }
                      });

                      _sendStoreRequest();
                    }
                  },
            style: ElevatedButton.styleFrom(
                textStyle: const TextStyle(fontSize: 22),
                minimumSize: const Size.fromHeight(56.0),
                shape: const StadiumBorder()),
            child: const Padding(
              padding: EdgeInsets.all(8.0),
              child: Text('Send Store Request'),
            ),
          ),
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
      ),
    );
  }
}
