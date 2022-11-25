package wasteservice

import alice.tuprologx.pj.model.Bool
import unibo.comm22.utils.ColorsOut


object Utils {
    private const val wasteTypeSeparator = "_"

    fun isValidWasteType(str: String): Boolean {
        try {
            WasteType.valueOf(str)
        } catch(e: IllegalArgumentException) {
            return false
        }
        return true
    }

    fun isValidWasteWeight(str: String): Boolean {
        try {
            str.toDouble()
        } catch(e: NumberFormatException) {
            return false
        }
        return true
    }

    fun getWasteTypesString(separator: String= wasteTypeSeparator): String {
        var res = "";
        for(i in 0..WasteType.values().lastIndex) {
            res += WasteType.values()[i];
            if(i < WasteType.values().lastIndex) {
                res += separator;
            }
        }
        return res;
    }

    fun getWasteTypesList(types: String, separator: String= wasteTypeSeparator): ArrayList<WasteType> {
        var res = arrayListOf<WasteType>()

        for(s in types.split(separator)) {
            try {
                res.add(WasteType.valueOf(s))
            } catch(e: java.lang.IllegalArgumentException) {}
        }
        return res;
    }

    fun simulateAction(load: Double) {
        Thread.sleep(load.toLong() * 100)
    }

    fun printCorrect(msg: String) {
        ColorsOut.outappl(msg, ColorsOut.GREEN)
    }
    fun printFail(msg: String) {
        ColorsOut.outappl(msg, ColorsOut.RED)
    }
}