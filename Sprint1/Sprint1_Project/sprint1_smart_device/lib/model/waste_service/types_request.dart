import 'package:sprint1_smart_device/model/appl_message.dart';

class TypesRequest {
  TypesRequest();

  String toQAKString(String senderName, String toActorName) {
    ApplMessage msg = ApplMessage("typesrequest", ApplMessageType.request,
        senderName, toActorName, "_", 1);
    return msg.toString();
  }
}
