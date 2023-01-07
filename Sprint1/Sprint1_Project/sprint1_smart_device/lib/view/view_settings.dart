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
  ApplMessageType _messageType = Settings.DEFAULT_MESSAGE_TYPE;

  bool _isDefault() {
    return _typesRequestAtConnect == Settings.DEFAULT_SEND_TYPES_REQUEST &&
        _textMessageIDController.text == Settings.DEFAULT_MESSAGE_ID &&
        _messageType == Settings.DEFAULT_MESSAGE_TYPE &&
        _textSenderController.text == Settings.DEFAULT_MESSAGE_SENDER &&
        _textReceiverController.text == Settings.DEFAULT_MESSAGE_RECEIVER;
  }

  void _restore() {
    setState(() {
      _typesRequestAtConnect = Settings.DEFAULT_SEND_TYPES_REQUEST;
      _textMessageIDController.text = Settings.DEFAULT_MESSAGE_ID;
      _messageType = Settings.DEFAULT_MESSAGE_TYPE;
      _textSenderController.text = Settings.DEFAULT_MESSAGE_SENDER;
      _textReceiverController.text = Settings.DEFAULT_MESSAGE_RECEIVER;
    });
  }

  @override
  void initState() {
    super.initState();

    setState(() {
      _typesRequestAtConnect = widget.settings.sendTypesRequest;
      _textMessageIDController.text = widget.settings.messageID;
      _messageType = widget.settings.messageType;
      _textSenderController.text = widget.settings.messageSender;
      _textReceiverController.text = widget.settings.messageReceiver;
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
                    if (value == null ||
                        !RegExp(r'^[a-zA-Z0-9]+$').hasMatch(value)) {
                      return 'Invalid Message ID';
                    }
                    return null;
                  },
                  controller: _textMessageIDController,
                  decoration: const InputDecoration(
                    hintText: 'storerequest',
                    border: UnderlineInputBorder(),
                    labelText: 'Message ID',
                  ),
                ),
                DropdownButtonFormField<String>(
                  key: const ValueKey('parameterWasteType'),
                  value: ApplMessageType.request.name,
                  items: ApplMessageType.values
                      .map<DropdownMenuItem<String>>((ApplMessageType value) {
                    return DropdownMenuItem(
                        value: value.name, child: Text(value.name));
                  }).toList(),
                  onChanged: (newValue) {
                    setState(() {
                      //_currentWasteType = newValue!;
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
                    if (value == null ||
                        !RegExp(r'^[a-zA-Z0-9]+$').hasMatch(value)) {
                      return 'Invalid Sender Name';
                    }
                    return null;
                  },
                  controller: _textSenderController,
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
                    if (value == null ||
                        !RegExp(r'^[a-zA-Z0-9]+$').hasMatch(value)) {
                      return 'Invalid Receiver Name';
                    }
                    return null;
                  },
                  controller: _textReceiverController,
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
          onPressed: _isDefault()
              ? null
              : () {
                  _restore();
                },
          backgroundColor:
              _isDefault() ? const Color.fromRGBO(220, 220, 220, 1.0) : null,
          foregroundColor:
              _isDefault() ? const Color.fromRGBO(136, 136, 136, 1.0) : null,
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
                  //_themeProvider.toggleTheme(_darkTheme);

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
