import '../appl_message.dart';

class TypesRequest {
  TypesRequest();

  String toQAKString(String senderName, String toActorName) {
    ApplMessage msg = ApplMessage("typesrequest", ApplMessageType.request,
        senderName, toActorName, "typesrequest", 1);
    return msg.toString();
  }
}
