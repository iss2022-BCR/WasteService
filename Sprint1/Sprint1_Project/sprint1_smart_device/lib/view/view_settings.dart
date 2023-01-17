import 'package:flutter/material.dart';
import 'package:sprint1_smart_device/model/appl_message.dart';
import 'package:sprint1_smart_device/model/settings.dart';

import '../model/constants.dart' as Constants;

class ViewSettings extends StatefulWidget {
  const ViewSettings(
      {Key? key, required this.settings, required this.saveSettings})
      : super(key: key);

  final Settings settings;
  final Function(bool, String, ApplMessageType, String, String) saveSettings;

  @override
  State<ViewSettings> createState() => _ViewSettingsState();
}

class _ViewSettingsState extends State<ViewSettings> {
  final _formKeyRequest = GlobalKey<FormState>();
  final TextEditingController _textMessageIDController =
      TextEditingController();
  final TextEditingController _textSenderController = TextEditingController();
  final TextEditingController _textReceiverController = TextEditingController();

  bool _typesRequestAtConnect = Settings.DEFAULT_SEND_TYPES_REQUEST;
  String _messageID = Settings.DEFAULT_MESSAGE_ID;
  ApplMessageType _messageType = Settings.DEFAULT_MESSAGE_TYPE;
  String _messageSender = Settings.DEFAULT_MESSAGE_SENDER;
  String _messageReceiver = Settings.DEFAULT_MESSAGE_RECEIVER;

  void _restore() {
    setState(() {
      _typesRequestAtConnect = Settings.DEFAULT_SEND_TYPES_REQUEST;
      _messageID = Settings.DEFAULT_MESSAGE_ID;
      _textMessageIDController.text = _messageID;
      _messageType = Settings.DEFAULT_MESSAGE_TYPE;
      _messageSender = Settings.DEFAULT_MESSAGE_SENDER;
      _textSenderController.text = _messageSender;
      _messageReceiver = Settings.DEFAULT_MESSAGE_RECEIVER;
      _textReceiverController.text = _messageReceiver;
    });
  }

  bool _isValid(String s) {
    return RegExp(r'^[a-zA-Z0-9]+$').hasMatch(s);
  }

  @override
  void initState() {
    super.initState();

    setState(() {
      _typesRequestAtConnect = widget.settings.sendTypesRequest;
      _messageID = widget.settings.messageID;
      _textMessageIDController.text = _messageID;
      _messageType = widget.settings.messageType;
      _messageSender = widget.settings.messageSender;
      _textSenderController.text = _messageSender;
      _messageReceiver = widget.settings.messageReceiver;
      _textReceiverController.text = _messageReceiver;
    });
  }

  @override
  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        return true;
      },
      child: Scaffold(
        appBar: AppBar(
          title: const Text(Constants.titleSettings),
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
        ),
        body: Form(
          key: _formKeyRequest,
          child: Center(
            child: ListView(
              shrinkWrap: true,
              padding: const EdgeInsets.all(50.0),
              children: [
                const Center(
                  child: Text(
                    'Type Request',
                    style: TextStyle(fontSize: 24.0),
                  ),
                ),
                Row(
                  children: [
                    const Expanded(
                        child: Text("Send upon Connect",
                            style: TextStyle(fontSize: 18))),
                    Transform.scale(
                      scale: 1.3,
                      child: Checkbox(
                        value: _typesRequestAtConnect,
                        onChanged: (value) {
                          setState(() {
                            _typesRequestAtConnect = value!;
                          });
                        },
                      ),
                    ),
                  ],
                ),
                const SizedBox(height: 30),
                const Center(
                  child: Text(
                    'Message Parameters',
                    style: TextStyle(fontSize: 24.0),
                  ),
                ),
                TextFormField(
                  key: const ValueKey('parameterMessageID'),
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  validator: (value) {
                    if (value ==
                            null /*||
                        !_isValid(value)*/
                        ) {
                      return 'Invalid Message ID';
                    }
                    return null;
                  },
                  controller: _textMessageIDController,
                  onChanged: (value) {
                    setState(() {
                      _messageID = value;
                    });
                  },
                  decoration: const InputDecoration(
                    hintText: 'storerequest',
                    border: UnderlineInputBorder(),
                    labelText: 'Message ID',
                  ),
                ),
                DropdownButtonFormField<ApplMessageType>(
                  key: const ValueKey('parameterMessageType'),
                  value: _messageType,
                  items: ApplMessageType.values
                      .map<DropdownMenuItem<ApplMessageType>>(
                          (ApplMessageType value) {
                    return DropdownMenuItem(
                        value: value, child: Text(value.name));
                  }).toList(),
                  onChanged: (newValue) {
                    setState(() {
                      _messageType = newValue!;
                    });
                  },
                  decoration: const InputDecoration(
                    border: UnderlineInputBorder(),
                    labelText: 'Waste Type',
                  ),
                ),
                TextFormField(
                  key: const ValueKey('parameterMessageSender'),
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  validator: (value) {
                    if (value ==
                            null /*||
                        !_isValid(value)*/
                        ) {
                      return 'Invalid Sender Name';
                    }
                    return null;
                  },
                  controller: _textSenderController,
                  onChanged: (value) {
                    setState(() {
                      _messageSender = value;
                    });
                  },
                  decoration: const InputDecoration(
                    hintText: 'smartdevice',
                    border: UnderlineInputBorder(),
                    labelText: 'Sender Name',
                  ),
                ),
                TextFormField(
                  key: const ValueKey('parameterMessageReceiver'),
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  validator: (value) {
                    if (value ==
                            null /*||
                        !_isValid(value)*/
                        ) {
                      return 'Invalid Receiver Name';
                    }
                    return null;
                  },
                  controller: _textReceiverController,
                  onChanged: (value) {
                    setState(() {
                      _messageReceiver = value;
                    });
                  },
                  decoration: const InputDecoration(
                    hintText: 'wasteservice',
                    border: UnderlineInputBorder(),
                    labelText: 'Receiver Name',
                  ),
                ),
              ],
            ),
          ),
        ),
        floatingActionButton: FloatingActionButton(
          onPressed: widget.settings.isDefault()
              ? null
              : () {
                  _restore();
                },
          backgroundColor: widget.settings.isDefault()
              ? const Color.fromRGBO(220, 220, 220, 1.0)
              : null,
          foregroundColor: widget.settings.isDefault()
              ? const Color.fromRGBO(136, 136, 136, 1.0)
              : null,
          child: const Icon(
            Icons.refresh,
            size: 40.0,
          ),
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.endFloat,
        persistentFooterButtons: [
          Row(
            children: [
              const Spacer(flex: 1),
              ElevatedButton(
                onPressed: () {
                  widget.saveSettings(
                      _typesRequestAtConnect,
                      _textMessageIDController.text,
                      _messageType,
                      _textSenderController.text,
                      _textReceiverController.text);

                  Navigator.pop(context);
                },
                style: ElevatedButton.styleFrom(
                    textStyle: const TextStyle(fontSize: 22),
                    shape: const StadiumBorder()),
                child: Container(
                  alignment: Alignment.center,
                  height: 50.0,
                  child: const Text(
                    "  Save  ",
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
              const Spacer(flex: 4),
              ElevatedButton(
                onPressed: () {
                  Navigator.pop(context);
                },
                style: ElevatedButton.styleFrom(
                    textStyle: const TextStyle(fontSize: 22),
                    shape: const StadiumBorder()),
                child: Container(
                  alignment: Alignment.center,
                  height: 50.0,
                  child: const Text(
                    " Cancel ",
                    style: TextStyle(
                      fontSize: 24,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ),
              const Spacer(flex: 1),
            ],
          ),
        ],
      ),
    );
  }
}
