class ApplMessage {
  late String msgId;
  late ApplMessageType msgType;
  late String msgSender;
  late String msgReceiver;
  late String msgContent;
  late int msgNum;

  // msg(msgId, msgType, msgSender, msgReceiver, msgId(msgContent), msgNum)
  ApplMessage(this.msgId, this.msgType, this.msgSender, this.msgReceiver,
      this.msgContent, this.msgNum);

  ApplMessage.fromStringParams(this.msgId, String msgType, this.msgSender,
      this.msgReceiver, this.msgContent, String msgNum) {
    try {
      this.msgType = ApplMessageType.values.byName(msgType);
    } catch (_) {
      this.msgType = ApplMessageType.dispatch;
    }
    this.msgNum = int.tryParse(msgNum) ?? 0;
  }

  // msg(msgId, msgType, msgSender, msgReceiver, msgId(msgContent), msgNum)
  ApplMessage.fromString(String message) {
    message = message.replaceAll('msg(', '');
    List<String> parsed = message.split(',');

    msgId = parsed[0].trim();
    message = message.replaceFirst(msgId, '');
    parsed.removeAt(0);

    try {
      msgType = ApplMessageType.values.byName(parsed[0].trim());
    } catch (_) {
      msgType = ApplMessageType.dispatch;
    }
    parsed.removeAt(0);

    msgSender = parsed[0].trim();
    parsed.removeAt(0);

    msgReceiver = parsed[0].trim();
    parsed.removeAt(0);

    msgNum = int.tryParse(parsed.last.replaceAll(')', '').trim()) ?? 0;

    int iFirst = message.indexOf(msgId);
    int iLast = message.indexOf(')') + 1;
    msgContent = message.substring(iFirst, iLast);
  }

  String getContentWithoutId() {
    int iFirst = msgContent.indexOf(msgId) + msgId.length + 1;
    int iLast = msgContent.indexOf(')');
    return msgContent.substring(iFirst, iLast);
  }

  @override
  String toString() {
    String res, type;
    switch (msgType) {
      case ApplMessageType.event:
        type = "event";
        break;
      case ApplMessageType.dispatch:
        type = "msg";
        break;
      case ApplMessageType.request:
        type = "request";
        break;
      case ApplMessageType.reply:
        type = "reply";
        break;
      case ApplMessageType.invitation:
        type = "invitation";
        break;
      default:
        type = "msg";
    }
    res =
        "msg($msgId, $type, $msgSender, $msgReceiver, $msgId($msgContent), $msgNum)\n";

    return res;
  }
}

enum ApplMessageType { event, dispatch, request, reply, invitation }

// Test Main
void main() {
  ApplMessage msg =
      ApplMessage.fromString("msg(id, request, send, recv, id(par1, par2), 1)");
  print(msg.msgId);
  print(msg.msgType);
  print(msg.msgSender);
  print(msg.msgReceiver);
  print(msg.msgContent);
  print(msg.msgNum);

  print(msg.toString());

  print(msg.getContentWithoutId());
}
