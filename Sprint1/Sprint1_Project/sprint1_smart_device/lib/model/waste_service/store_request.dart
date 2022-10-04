import 'waste_type.dart';

const double minWasteWeight = 1.0;
const double maxWasteWeight = 100000.0;

class StoreRequest {
  late double wasteWeight;
  late WasteType wasteType;

  StoreRequest(this.wasteWeight, this.wasteType);

  StoreRequest.fromString(String weight, String type) {
    try {
      wasteWeight = double.parse(weight);
      if (wasteWeight < minWasteWeight || wasteWeight > maxWasteWeight) {
        throw Exception(
            "Invalid parameter: weight must be between $minWasteWeight and $maxWasteWeight");
      }
      wasteType = WasteType.values.byName(type);
    } catch (e) {
      throw Exception("Illegal argument: ${e.toString()}");
    }
  }

  StoreRequest.fromStringExceptionSafe(String weight, String type) {
    wasteWeight = double.tryParse(weight) ?? 1.0;
    if (wasteWeight < minWasteWeight || wasteWeight > maxWasteWeight) {
      wasteWeight = 1.0;
    }
    try {
      wasteType = WasteType.values.byName(type);
    } catch (e) {
      wasteType = WasteType.values.first;
    }
  }

  StoreRequest.fromJson(Map<String, dynamic> json)
      : wasteWeight = json['wasteWeight'],
        wasteType = WasteType.values.byName(json['wasteType']);

  static Map<String, dynamic> toJson(StoreRequest storeRequest) => {
        'wasteWeight': storeRequest.wasteWeight,
        'wasteType': storeRequest.wasteType
      };

  String toJsonString() {
    return '{"wasteWeight": $wasteWeight, "wasteType": "${wasteType.name}"}';
  }

  bool equals(StoreRequest storeRequest) {
    return wasteWeight == storeRequest.wasteWeight &&
        wasteType == storeRequest.wasteType;
  }
}
