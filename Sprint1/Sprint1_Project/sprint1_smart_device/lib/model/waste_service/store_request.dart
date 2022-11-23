import 'dart:convert';

import 'package:sprint1_smart_device/model/appl_message.dart';

import 'waste_type.dart';

const double minWasteWeight = 1.0;
const double maxWasteWeight = 100000.0;

class StoreRequest {
  late String wasteType;
  late double wasteWeight;

  StoreRequest(this.wasteType, this.wasteWeight);

  StoreRequest.fromString(String type, String weight) {
    wasteType = type;
    wasteWeight = double.parse(weight);
    if (wasteWeight < minWasteWeight || wasteWeight > maxWasteWeight) {
      throw Exception(
          "Invalid parameter: weight must be between $minWasteWeight and $maxWasteWeight");
    }
  }

  StoreRequest.fromJson(Map<String, dynamic> json)
      : wasteType = json['wasteType'],
        wasteWeight = json['wasteWeight'];

  StoreRequest.fromJsonString(String json) {
    final jsonObj = jsonDecode(json);
    wasteType = jsonObj['wasteType'];
    wasteWeight = jsonObj['wasteWeight'];
  }

  StoreRequest.fromQAKString(String string) {
    List<String> splitted = string.split('storerequest');
    string = splitted[splitted.length - 1];
    splitted = string.split(',');

    String type = string.split(',')[0].substring(1).trim();
    String weight = string
        .split(',')[1]
        .substring(0, string.split(',')[1].indexOf(')'))
        .trim();

    wasteType = type;
    wasteWeight = double.parse(weight);
  }

  static Map<String, dynamic> toJson(StoreRequest storeRequest) => {
        'wasteType': storeRequest.wasteType,
        'wasteWeight': storeRequest.wasteWeight
      };

  String toJsonString() {
    return '{"wasteType": "$wasteType", "wasteWeight": $wasteWeight}';
  }

  String toQAKString(String senderName, String toActorName, int seqNum) {
    ApplMessage msg = ApplMessage(
        "storerequest",
        ApplMessageType.request,
        senderName,
        toActorName,
        "storerequest($wasteType, $wasteWeight)",
        seqNum);
    return msg.toString();
  }

  bool equals(StoreRequest storeRequest) {
    return wasteType == storeRequest.wasteType &&
        wasteWeight == storeRequest.wasteWeight;
  }
}
