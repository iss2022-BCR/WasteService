import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:sprint1_smart_device/model/networking/client_connection.dart';
import '../model/waste_service/store_request.dart';
import '../model/waste_service/waste_type.dart';

const String dateTimeFormat = "yyyy/MM/dd HH:mm:ss";

const double maxWasteWeight = 100000.0;

const int timeoutSeconds = 5;

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

  WasteType _currentWasteType = WasteType.PLASTIC;
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

  Future<void> _sendStoreRequest({timeout = Duration}) async {
    StoreRequest req = StoreRequest(
        double.parse(_textControllerWeight.text), _currentWasteType);

    String msg = req.toJsonString();
    _logMessage("Store request: $msg");
    widget.connection.sendMessage(msg);

    if (timeout != null) {
      _startTimer();
    }
  }

  void _messageHandler(Uint8List data) {
    final serverResponse = String.fromCharCodes(data);
    _stopTimer();
    setState(() {
      _response = serverResponse;
    });
    _logMessage(serverResponse);
  }

  void _errorHandler(error) {
    widget.notifyParent("Disconnected", error.toString());
    //print("Test error: ${error.toString()}"); // test

    widget.connection.destroy();
    if (Navigator.canPop(context)) {
      Navigator.pop(context);
    }
  }

  void _doneHandler() {
    widget.notifyParent("Disconnected", "Connection closed by the server.");

    widget.connection.close();
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
          _stopTimer();
          setState(() {
            _response = "Timeout expired.";
          });
        }
      });
    });
  }

  void _stopTimer() {
    _timer.isActive ? _timer.cancel() : null;
    setState(() {
      _waitingResponse = false;
    });
  }

  @override
  void didChangeDependencies() {
    Locale myLocale = Localizations.localeOf(context);
    Intl.defaultLocale = myLocale.toString();
    _logMessage(
        "Connected to ${widget.connection.address.address}:${widget.connection.remotePort}");

    super.didChangeDependencies();
  }

  @override
  void initState() {
    super.initState();

    // listen for responses from the server
    widget.connection.listen(_messageHandler,
        onError: _errorHandler, onDone: _doneHandler, cancelOnError: true);
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        widget.notifyParent(
            "Disconnected", "You disconnecred from the server.");
        widget.connection.close();

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
                widget.connection.close();

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
                    DropdownButtonFormField<WasteType>(
                      value: _currentWasteType,
                      items: WasteType.values
                          .map<DropdownMenuItem<WasteType>>((WasteType value) {
                        return DropdownMenuItem(
                            value: value, child: Text(value.name));
                      }).toList(),
                      onChanged: (WasteType? newValue) {
                        setState(() {
                          _currentWasteType = newValue!;
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
                      _sendStoreRequest(timeout: 10);
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
