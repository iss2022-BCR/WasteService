import 'dart:convert';

import 'waste_type.dart';

const double minWasteWeight = 1.0;
const double maxWasteWeight = 100000.0;

class StoreRequest {
  late WasteType wasteType;
  late double wasteWeight;

  StoreRequest(this.wasteType, this.wasteWeight);

  StoreRequest.fromString(String type, String weight) {
    try {
      wasteType = WasteType.values.byName(type);
    } catch (e) {
      throw Exception("Illegal argument: ${e.toString()}");
    }
    wasteWeight = double.parse(weight);
    if (wasteWeight < minWasteWeight || wasteWeight > maxWasteWeight) {
      throw Exception(
          "Invalid parameter: weight must be between $minWasteWeight and $maxWasteWeight");
    }
  }

  StoreRequest.fromStringExceptionSafe(String type, String weight) {
    try {
      wasteType = WasteType.values.byName(type);
    } catch (e) {
      wasteType = WasteType.values.first;
    }
    wasteWeight = double.tryParse(weight) ?? 1.0;
    if (wasteWeight < minWasteWeight || wasteWeight > maxWasteWeight) {
      wasteWeight = 1.0;
    }
  }

  StoreRequest.fromJsonString(String json) {
    final jsonObj = jsonDecode(json);
    wasteType = WasteType.values.byName(jsonObj['wasteType']);
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

    wasteType = WasteType.values.byName(type);
    wasteWeight = double.parse(weight);
  }

  StoreRequest.fromJson(Map<String, dynamic> json)
      : wasteType = WasteType.values.byName(json['wasteType']),
        wasteWeight = json['wasteWeight'];

  static Map<String, dynamic> toJson(StoreRequest storeRequest) => {
        'wasteType': storeRequest.wasteType,
        'wasteWeight': storeRequest.wasteWeight
      };

  String toJsonString() {
    return '{"wasteType": "${wasteType.name}", "wasteWeight": $wasteWeight}';
  }

  String toQAKString(String actorName) {
    return 'msg(storerequest, request, test_smartdevice, $actorName, storerequest(${wasteType.name}, $wasteWeight), 1)\n';
  }

  bool equals(StoreRequest storeRequest) {
    return wasteType == storeRequest.wasteType &&
        wasteWeight == storeRequest.wasteWeight;
  }
}
