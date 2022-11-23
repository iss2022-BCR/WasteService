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

    wasteservice.MapConfigUtils.loadMapConfig("mapConfigWasteService")
    wasteservice.MapConfigUtils.showFancyMap()
    println(wasteservice.MapConfigUtils.getMapConfigCoordinates())

    println(wasteservice.MapConfigUtils.getNearestPositionToCellType(0,0, "INDOOR"))

    println(wasteservice.MapConfigUtils.getNearestPositionToCellType(0, 0, CellType.GLASS))
    println(wasteservice.MapConfigUtils.getNearestPositionToCellType(0, 0, CellType.PLASTIC))
    println(wasteservice.MapConfigUtils.getNearestPositionToCellType(0, 0, CellType.INDOOR))
    println(wasteservice.MapConfigUtils.getNearestPositionToCellType(0, 0, CellType.HOME))
}

