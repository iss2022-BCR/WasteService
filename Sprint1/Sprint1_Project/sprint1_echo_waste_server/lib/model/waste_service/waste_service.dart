const double defaultMax = 100.0;

class WasteService {
  Map<String, double> _preStorage = {};
  Map<String, double> _storage = {};
  Map<String, double> _storageCapacity = {};

  // canStore
  WasteService(List<String> wasteTypes, {double? capacity}) {
    for (String s in wasteTypes) {
      _preStorage[s] = 0.0;
      _storage[s] = 0.0;
      _storageCapacity[s] = capacity ?? defaultMax;
    }
  }

  void resetStorage(String type) {
    if (_storage.containsKey(type)) {
      _preStorage[type] = 0.0;
      _storage[type] = 0.0;
    }
  }

  double getCapacity(String type) {
    return _storageCapacity[type] ?? -1.0;
  }

  void setCapacity(String type, double maxCapacity) {
    if (_storageCapacity.containsKey(type)) {
      _storageCapacity[type] = maxCapacity;
    }
  }

  String getTypesListString(String separator) {
    String res = "";

    for (String s in _storage.keys) {
      res += s;
      if (s != _storage.keys.last) {
        res += separator;
      }
    }

    return res;
  }

  bool canPreStore(String type, double weight) {
    if (!_preStorage.containsKey(type)) {
      return false;
    }
    return _preStorage[type]! + weight <= _storageCapacity[type]!;
  }

  bool canStore(String type, double weight) {
    if (!_storage.containsKey(type)) {
      return false;
    }
    return _storage[type]! + weight <= _storageCapacity[type]!;
  }

  // pre deposit
  void addToPreStorage(String type, double weight) {
    if (canPreStore(type, weight)) {
      _preStorage[type] = _preStorage[type]! + weight;
    }
  }

  // deposit
  void addToStorage(String type, double weight) {
    if (canStore(type, weight)) {
      _storage[type] = _storage[type]! + weight;
    }
  }

  String getStatusString(String type) {
    String res = "";

    if (_storage.containsKey(type)) {
      res =
          "$type: ${_preStorage[type]}/${_storage[type]}/${_storageCapacity[type]} KG";
    }

    return res;
  }

  String getFullStatusString() {
    String res = "";

    for (String type in _preStorage.keys) {
      res +=
          "$type: ${_preStorage[type]}/${_storage[type]}/${_storageCapacity[type]} KG\n";
    }

    return res;
  }
}
