import 'dart:async';
import 'dart:io';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';

const String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";

const double maxWasteWeight = 100000.0;
const List<String> wasteTypes = <String>['PLASTIC', 'GLASS'];

const int timeoutSeconds = 5;

class ViewRequest extends StatefulWidget {
  ViewRequest({Key? key, required this.socket, required this.notifyParent})
      : super(key: key);

  Socket? socket;
  final Function(String, String) notifyParent;

  @override
  State<ViewRequest> createState() => _ViewRequestState();
}

class _ViewRequestState extends State<ViewRequest> {
  final _formKeyRequest = GlobalKey<FormState>();
  final TextEditingController _textControllerWeight = TextEditingController();
  final ScrollController _scrollController = ScrollController();

  bool _debug = false;

  String _currentWasteType = wasteTypes.first;
  String _response = "";

  bool _waitingResponse = false;
  String _waitingDots = "Waiting.";
  late Timer _timer;
  int _timeoutCounter = timeoutSeconds;

  // Message list
  final List<String> _messages = [];

  bool _checkWeight(String weight) {
    if (weight.isEmpty) return false;

    final w = double.tryParse(weight) ?? -1.0;
    if (w >= 0.0 && w <= maxWasteWeight) return true;

    return false;
  }

  void _quit() {
    if (widget.socket != null) {
      widget.socket!.close();
      widget.socket = null;
    }
  }

  void _disconnect() {
    if (widget.socket != null) {
      widget.socket!.destroy();
      widget.socket = null;
    }
  }

  void _logMessage(String msg) {
    setState(() {
      _messages.add("${_getTimeStamp()} $msg");
    });
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollController.jumpTo(_scrollController.position.maxScrollExtent);
    });
  }

  String _getTimeStamp() {
    final DateTime timestamp = DateTime.now();

    final String result = "[${DateFormat(dateTimeFormat).format(timestamp)}]";

    return result;
  }

  Future<void> _sendMessage(String message) async {
    _logMessage("Store request: $message");
    widget.socket!.write(message);
  }

  void _messageHandler(Uint8List data) {
    final serverResponse = String.fromCharCodes(data);
    setState(() {
      _waitingResponse = false;
      _response = serverResponse;
    });
    _timer.cancel();
    _logMessage(serverResponse);
    //print("Response: $serverResponse"); // test
  }

  void _errorHandler(error) {
    widget.notifyParent("Disconnected", error.toString());
    //print("Test error: ${error.toString()}"); // test

    _disconnect();
    if (Navigator.canPop(context)) {
      Navigator.pop(context);
    }
  }

  void _doneHandler() {
    if (widget.socket != null) {
      widget.notifyParent("Disconnected", "Connection closed by the server.");
    }

    _quit();
    if (Navigator.canPop(context)) {
      Navigator.pop(context);
    }
  }

  void _startTimer() {
    setState(() {
      _waitingDots = "Waiting.";
      _timeoutCounter = timeoutSeconds;
      _waitingResponse = true;
    });
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        if (_waitingResponse && _timeoutCounter > 0) {
          setState(() {
            if (_waitingDots == "Waiting...") {
              _waitingDots = "Waiting.";
            } else {
              _waitingDots += ".";
            }
            _timeoutCounter--;
          });
        } else {
          _timer.cancel();
          setState(() {
            _waitingResponse = false;
            _response = "Timeout expired.";
          });
        }
      });
    });
  }

  @override
  void didChangeDependencies() {
    Locale myLocale = Localizations.localeOf(context);
    Intl.defaultLocale = myLocale.toString();
    _logMessage(
        "Connected to ${widget.socket!.address.address}:${widget.socket!.remotePort}");

    super.didChangeDependencies();
  }

  @override
  void initState() {
    super.initState();

    // listen for responses from the server
    widget.socket!.listen(
      // handle data from the server
      _messageHandler,

      // handle errors
      onError: _errorHandler,

      // handle server ending connection
      onDone: _doneHandler,

      cancelOnError: true,
    );
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        widget.notifyParent(
            "Disconnected", "You disconnecred from the server.");
        _quit();

        return true;
      },
      child: Scaffold(
        appBar: AppBar(
            title: const Text('Store Request'),
            centerTitle: true,
            automaticallyImplyLeading: true,
            leading: IconButton(
              icon: const Icon(Icons.arrow_back_ios),
              onPressed: () {
                widget.notifyParent(
                    "Disconnected", "You disconnecred from the server.");
                _quit();

                if (Navigator.canPop(context)) {
                  Navigator.pop(context);
                }
              },
            ),
            actions: [
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
                    const Text(
                      'Request Parameters',
                      style: TextStyle(fontSize: 26.0),
                    ),
                    TextFormField(
                      validator: (value) {
                        if (value == null || !_checkWeight(value)) {
                          return 'Invalid weight';
                        }
                        return null;
                      },
                      controller: _textControllerWeight,
                      keyboardType:
                          const TextInputType.numberWithOptions(decimal: true),
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
                        labelText: 'Load Weight',
                      ),
                    ),
                    DropdownButtonFormField(
                      value: _currentWasteType,
                      items: wasteTypes
                          .map<DropdownMenuItem<String>>((String value) {
                        return DropdownMenuItem(
                          value: value,
                          child: Text(value),
                        );
                      }).toList(),
                      onChanged: (String? value) {
                        setState(() {
                          _currentWasteType = value!;
                        });
                      },
                    ),
                    const SizedBox(height: 20.0),
                    const Text(
                      'Response',
                      style: TextStyle(fontSize: 26.0),
                    ),
                    Padding(
                      padding: const EdgeInsets.symmetric(vertical: 10.0),
                      child: Container(
                          width: double.infinity,
                          height: 40.0,
                          decoration: BoxDecoration(
                            borderRadius: BorderRadius.circular(8.0),
                            color:
                                _response.toLowerCase().contains("loadaccepted")
                                    ? Colors.green
                                    : _response
                                            .toLowerCase()
                                            .contains("loadrejected")
                                        ? Colors.red
                                        : Colors.grey,
                          ),
                          alignment: Alignment.center,
                          child: Text(
                              _waitingResponse ? _waitingDots : _response,
                              style: const TextStyle(fontSize: 16.0))),
                    ),
                    Visibility(
                      visible: _debug,
                      maintainSize: true,
                      maintainAnimation: true,
                      maintainState: true,
                      child: const Text(
                        'Log',
                        style: TextStyle(fontSize: 26.0),
                      ),
                    ),
                    Visibility(
                      visible: _debug,
                      maintainSize: true,
                      maintainAnimation: true,
                      maintainState: true,
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 10.0),
                        child: Container(
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
                        ),
                      ),
                    ),
                  ]),
            ),
          ),
        ),
        floatingActionButton: Padding(
          padding: const EdgeInsets.only(top: 32.0, left: 32.0, right: 32.0),
          child: ElevatedButton(
            onPressed: _waitingResponse
                ? null
                : () {
                    if (_formKeyRequest.currentState!.validate()) {
                      // test
                      //_sendMessage('{"distance": 100, "angle": 45}');

                      _sendMessage(
                          '{"wasteWeight": ${double.parse(_textControllerWeight.text)}, "wasteType": "$_currentWasteType"}');
                      _startTimer();
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
