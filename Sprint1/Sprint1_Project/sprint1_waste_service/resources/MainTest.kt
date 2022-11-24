import it.unibo.map_editor_bcr.model.map_config.CellType
import it.unibo.map_editor_bcr.model.map_config.MapConfig
import kotlinx.coroutines.runBlocking
import wasteservice.WasteService
import wasteservice.WasteType
import wasteservice.Utils.getWasteTypesList

fun main() = runBlocking {
    /*plannerBCR.initAI();
    plannerBCR.loadRoomMap("mapRoomEmpty")

    plannerBCR.showFancyMap()

    plannerBCR.setGoal(4,3)
    plannerBCR.doPlan()

    var actions = plannerBCR.getActionsString()

    plannerBCR.test()

    println(actions)*/

    plannerBCR.loadMapConfig("mapConfigWasteService")
    plannerBCR.showFancyMapConfig()
    println(plannerBCR.getMapConfigCoordinates())

    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "INDOOR"))

    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "GLASS"))
    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "PLASTIC"))
    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "INDOOR"))
    println(plannerBCR.getNearestPositionToCellType(Pair<Int, Int>(0,0), "HOME"))
}

