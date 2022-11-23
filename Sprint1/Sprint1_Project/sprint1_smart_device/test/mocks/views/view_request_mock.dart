// ignore_for_file: library_prefixes

import 'dart:async';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:intl/intl.dart';
import 'package:sprint1_smart_device/model/waste_service/store_request.dart';
import 'package:sprint1_smart_device/model/waste_service/types_request.dart';

import 'package:sprint1_smart_device/model/constants.dart' as Constants;
import 'package:sprint1_smart_device/model/waste_service/waste_type.dart';

class ViewRequestMock extends StatefulWidget {
  const ViewRequestMock({
    Key? key,
    required this.typesReply,
    required this.typesSeparator,
    required this.typesReplyDelay,
    required this.storeRequestAccepted,
    required this.storeRequestRejected,
    required this.loadAcceptedDelay,
  }) : super(key: key);

  final String typesReply;
  final String typesSeparator;
  final int typesReplyDelay;
  final StoreRequest storeRequestAccepted;
  final StoreRequest storeRequestRejected;

  final int loadAcceptedDelay;

  @override
  State<ViewRequestMock> createState() => _ViewRequestMockState();
}

class _ViewRequestMockState extends State<ViewRequestMock> {
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
  int _timeoutCounter = Constants.timeoutSeconds;

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
      _waitingReply = false;
      _reply = "";
    });
    TypesRequest req = TypesRequest();

    String msg = req.toQAKString("smartdevice", "typesprovider");
    _logMessage("Types Request: $msg");
  }

  Future<void> _sendStoreRequestMock() async {
    setState(() {
      _waitingReply = false;
      _reply = "";
    });
    StoreRequest req = StoreRequest(
        _currentWasteType, double.parse(_textControllerWeight.text));

    String msg = req.toQAKString("smartdevice", "wasteservice", 3);
    _logMessage("Store request: $msg");

    if (req.equals(widget.storeRequestAccepted)) {
      // store request accepted: get reply after pickup is completed
      // schedule timer
      _startTimer(widget.loadAcceptedDelay);
    } else if (req.equals(widget.storeRequestRejected)) {
      // store request rejected: get reply immediately
      _messageHandlerMock(Uint8List.fromList("LoadRejected".codeUnits));
    } else {
      // store request rejected: invalid request
      _messageHandlerMock(Uint8List.fromList("LoadRejected".codeUnits));
    }
  }

  void _receiveTypesListMock(Uint8List data, String separator) {
    List<String> res = [];
    res = String.fromCharCodes(data).split(separator);

    setState(() {
      _wasteTypes = res;
      _currentWasteType = res.first;
    });
  }

  void _messageHandlerMock(Uint8List data) {
    final serverReply = String.fromCharCodes(data);
    //_stopTimer();
    setState(() {
      _reply = serverReply;
    });
    _logMessage(serverReply);
  }

  void _startTimer(int timeout) {
    setState(() {
      _waitingDots = "Waiting.";
      _timeoutCounter = timeout;
      _waitingReply = true;
    });
    _timer = Timer.periodic(const Duration(seconds: 1), (timer) {
      setState(() {
        if (_waitingReply && _timeoutCounter > 0) {
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
          if (_wasteTypes.isEmpty) {
            _receiveTypesListMock(
                Uint8List.fromList(widget.typesReply.codeUnits),
                widget.typesSeparator);
          } else {
            _messageHandlerMock(Uint8List.fromList("LoadAccepted".codeUnits));
          }
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

    _startTimer(widget.typesReplyDelay);

    super.didChangeDependencies();
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
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
                        ? Container(
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
                                              .toLowerCase()
                                              .contains('loadaccepted')
                                          ? Colors.green
                                          : _messages[index]
                                                  .toLowerCase()
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
                        : Container(
                            width: double.infinity,
                            height: 40.0,
                            decoration: BoxDecoration(
                              borderRadius: BorderRadius.circular(8.0),
                              color:
                                  _reply.toLowerCase().contains("loadaccepted")
                                      ? Colors.green
                                      : _reply
                                              .toLowerCase()
                                              .contains("loadrejected")
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
                      _sendStoreRequestMock();
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
      home: ViewRequestMock(
        typesReply: "GLASS_PLASTIC_METAL",
        typesSeparator: "_",
        typesReplyDelay: 1,
        storeRequestAccepted: StoreRequest("PLASTIC", 10.0),
        storeRequestRejected: StoreRequest("GLASS", 5000.0),
        loadAcceptedDelay: 5,
      ),
    );
  }
}
