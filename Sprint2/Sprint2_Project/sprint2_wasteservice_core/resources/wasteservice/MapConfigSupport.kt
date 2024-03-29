package wasteservice

import it.unibo.map_editor_bcr.model.MapLoader
import it.unibo.map_editor_bcr.model.map_config.CellType
import it.unibo.map_editor_bcr.model.map_config.MapConfig

class MapConfigSupport {
    private var mapConfig: MapConfig? = null

    /**
     * Load the MapConfig from a file
     */
    fun loadMapConfig(filename: String) {
        mapConfig = MapLoader.loadMapConfig(filename)
    }

    fun mapToString(): String {
        return mapConfig?.toString()?: ""
    }

    fun mapToFancyString(): String {
        return mapConfig?.toFancyString()?: ""
    }

    /**
     * Returns the CellType corresponding to wasteType
     */
    fun parseCellTypeFromWasteType(wasteType: WasteType): CellType {
        return CellType.valueOf(wasteType.name)
    }

    /**
     * Return the element of the MapConfig located in coord: Pair<X, Y>
     */
    fun getCell(coord: Pair<Int, Int>): CellType {
        return mapConfig?.get(coord.first, coord.second)?: CellType.NONE
    }

    /**
     * Return a list containing all the coordinates for the MapConfig elements
     */
    fun getMapConfigCoordinates(): List<Pair<Int, Int>>{
        if(mapConfig == null) {
            throw Exception("MapConfig not initialized")
        }

        var res: MutableList<Pair<Int, Int>> = mutableListOf()

        for(x in 0 until(mapConfig?.dimX ?:0)) {
            for(y in 0 until (mapConfig?.dimY ?:0)) {
                res.add(Pair<Int, Int>(x, y))
            }
        }

        return res.toList()
    }

    /**
     * Return a list containing all the coordinates for the given CellType.
     */
    fun getCoordsListFromCellType(cellType: CellType): List<Pair<Int, Int>> {
        var res: MutableList<Pair<Int, Int>> = mutableListOf()

        for(coord in getMapConfigCoordinates()) {
            if(getCell(coord).equals(cellType)) {
                res.add(coord)
            }
        }

        return res.toList()
    }

    /**
     * Return the coordinate (Pair<X, Y>) of the nearest cellType from a given coordinate
     */
    fun getNearestPositionToCellType(startX: Int, startY: Int, cellType: CellType): Pair<Int, Int> {
        val list = getCoordsListFromCellType(cellType)
        if(list.isEmpty())
            throw Exception("The MapConfig does not contain $cellType")
        if(list.size == 1)
            return list[0]

        var result: Pair<Int, Int> = Pair<Int, Int>(-1, -1)
        var best: Int = Int.MAX_VALUE

        for(coord in list) {
            val distX = kotlin.math.abs(startX - coord.first)
            val distY = kotlin.math.abs(startY - coord.second)

            if(distX + distY < best) {
                best = distX + distY
                result = coord
            }
        }
        return result
    }
    fun getNearestPositionToCellType(startX: Int, startY: Int, cellType: String): Pair<Int, Int> {
        return getNearestPositionToCellType(startX, startY, CellType.valueOf(cellType))
    }
    fun getNearestPositionToCellType(startCoords: Pair<Int, Int>, cellType: CellType): Pair<Int, Int> {
        return getNearestPositionToCellType(startCoords.first, startCoords.second, cellType)
    }
    fun getNearestPositionToCellType(startCoords: Pair<Int, Int>, cellType: String): Pair<Int, Int> {
        return getNearestPositionToCellType(startCoords.first, startCoords.second, CellType.valueOf(cellType))
    }

    /**
     * Return a random coordinate (Pair<X, Y>) of the cellTypes to a specific coordinate
     */
    fun getRandomPositionFromCellType(cellType: CellType): Pair<Int, Int> {
        val list = getCoordsListFromCellType(cellType)
        if(list.isEmpty())
            throw Exception("The MapConfig does not contain $cellType")

        return list.random()
    }
    fun getRandomPositionFromCellType(cellType: String): Pair<Int, Int> {
        return getRandomPositionFromCellType(CellType.valueOf(cellType))
    }
}