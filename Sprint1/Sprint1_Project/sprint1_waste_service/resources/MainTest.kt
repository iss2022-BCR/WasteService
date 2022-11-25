import it.unibo.map_editor_bcr.model.map_config.CellType
import it.unibo.map_editor_bcr.model.map_config.MapConfig
import kotlinx.coroutines.runBlocking
import wasteservice.WasteService
import wasteservice.WasteType
import wasteservice.Utils.getWasteTypesList

fun main() = runBlocking {
    /*val ws = WasteService()

    ws.printFancyStatusString()
    ws.addToPreStorage(WasteType.PLASTIC, 100.0)
    ws.printFancyStatusString()*/

    plannerBCR.loadMapConfig("mapConfigWasteService2")
    plannerBCR.showFancyMapConfig()
    println(plannerBCR.getMapConfigCoordinates())

    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "INDOOR"))

    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "GLASS"))
    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "PLASTIC"))
    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "INDOOR"))
    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "HOME"))
}

