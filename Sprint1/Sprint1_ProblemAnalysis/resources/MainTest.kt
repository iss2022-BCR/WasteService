import kotlinx.coroutines.runBlocking
import wasteservice.WasteService
import wasteservice.WasteType
import wasteservice.Utils.getWasteTypesList

fun main() = runBlocking {
    var ct = CellType.EMPTY;
    println(ct.name);
    CellType.values().toString()

    println(getWasteTypesList("PLASTIC:GLASS", ":"))

    var vs = WasteService()
    println(vs.getCurrentPreStorageString())
    vs.addToPreStorage(WasteType.PLASTIC, 10.0)
    println(vs.getCurrentPreStorageString())

}

