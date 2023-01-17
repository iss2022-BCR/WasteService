import 'package:shared_preferences/shared_preferences.dart';
import 'package:sprint1_smart_device/model/appl_message.dart';

class Settings {
  static const bool DEFAULT_SEND_TYPES_REQUEST = true;
  static const String DEFAULT_MESSAGE_ID = "storerequest";
  static const ApplMessageType DEFAULT_MESSAGE_TYPE = ApplMessageType.request;
  static const String DEFAULT_MESSAGE_SENDER = "smartdevice";
  static const String DEFAULT_MESSAGE_RECEIVER = "wasteservice";

  bool sendTypesRequest = DEFAULT_SEND_TYPES_REQUEST;
  String messageID = DEFAULT_MESSAGE_ID;
  ApplMessageType messageType = DEFAULT_MESSAGE_TYPE;
  String messageSender = DEFAULT_MESSAGE_SENDER;
  String messageReceiver = DEFAULT_MESSAGE_RECEIVER;

  void loadSettings() async {
    final prefs = await SharedPreferences.getInstance();

    sendTypesRequest =
        prefs.getBool("sendTypesRequest") ?? DEFAULT_SEND_TYPES_REQUEST;
    messageID = prefs.getString("messageID") ?? DEFAULT_MESSAGE_ID;
    String mType = prefs.getString("messageType") ?? DEFAULT_MESSAGE_TYPE.name;
    try {
      messageType = ApplMessageType.values.byName(mType);
    } catch (e) {
      messageType = DEFAULT_MESSAGE_TYPE;
    }
    messageSender = prefs.getString("messageSender") ?? DEFAULT_MESSAGE_SENDER;
    messageReceiver =
        prefs.getString("messageReceiver") ?? DEFAULT_MESSAGE_RECEIVER;
  }

  void saveSettings() async {
    final prefs = await SharedPreferences.getInstance();

    prefs.setBool("sendTypesRequest", sendTypesRequest);
    prefs.setString("messageID", messageID);
    prefs.setString("messageType", messageType.name);
    prefs.setString("messageSender", messageSender);
    prefs.setString("messageReceiver", messageReceiver);
  }

  void resetSettings() async {
    final prefs = await SharedPreferences.getInstance();

    prefs.setBool("sendTypesRequest", DEFAULT_SEND_TYPES_REQUEST);
    prefs.setString("messageID", DEFAULT_MESSAGE_ID);
    prefs.setString("messageType", DEFAULT_MESSAGE_TYPE.name);
    prefs.setString("messageSender", DEFAULT_MESSAGE_SENDER);
    prefs.setString("messageReceiver", DEFAULT_MESSAGE_RECEIVER);
  }

  bool isDefault() {
    return sendTypesRequest == DEFAULT_SEND_TYPES_REQUEST &&
        messageID == DEFAULT_MESSAGE_ID &&
        messageType == DEFAULT_MESSAGE_TYPE &&
        messageSender == DEFAULT_MESSAGE_SENDER &&
        messageReceiver == DEFAULT_MESSAGE_RECEIVER;
  }
}
