package wasteservice

import java.util.*

class WasteService {
    // The content of the containers (to handle the requests and be able to respond instantly if its a loadrejecteed)
    private var preStorage = EnumMap<WasteType, Double>(WasteType.values().associateWith { 0.0 })
    private var storage = EnumMap<WasteType, Double>(WasteType.values().associateWith { 0.0 })
    private var storageCapacity = EnumMap<WasteType, Double>(WasteType.values().associateWith { WSconstants.DEFAULT_MAX })

    // fun canStore // check if there is enough space
    // fun store // store the waste

    fun setCapacity(wType: WasteType, wCapacity: Double) {
        storageCapacity[wType] = wCapacity;
    }
    fun getCurrentPreStorageString(): String {
        var res: String = ""

        for(wt in preStorage.keys) {
            res += "${wt.name}: ${preStorage[wt]}\n"
        }

        return res
    }
    fun getCurrentStorageString(): String {
        var res: String = ""

        for(wt in storage.keys) {
            res += "${wt.name}: ${storage[wt]}\n"
        }

        return res
    }
    fun getFullStatusString(): String {
        var res: String = ""
        for(wt in storage.keys) {
            res += "${wt.name}: ${preStorage[wt]}/${storage[wt]}/${storageCapacity[wt]}\n"
        }

        return res
    }
    fun canPreStore(wType: WasteType, wWeight: Double): Boolean {
        if(!preStorage.keys.contains(wType))
            return false
        if(wWeight <= 0.0)
            return false
        if(preStorage[wType]!! + wWeight <= storageCapacity[wType]!!)
            return true
        return false;
    }
    fun canStore(wType: WasteType, wWeight: Double): Boolean {
        if(!storage.keys.contains(wType))
            return false
        if(wWeight <= 0.0)
            return false
        if(storage[wType]!! + wWeight <= storageCapacity[wType]!!)
            return true
        return false;
    }
    fun addToPreStorage(wType: WasteType, wWeight: Double) {
        if(canPreStore(wType, wWeight)) {
            var oldLoad = preStorage[wType]
            preStorage[wType] = oldLoad!! + wWeight
        }
    }
    fun addToStorage(wType: WasteType, wWeight: Double) {
        if(canStore(wType, wWeight)){
            var oldLoad = storage[wType]
            storage[wType] = oldLoad!! + wWeight
        }
    }
}