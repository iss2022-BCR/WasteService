package it.unibo.wasteservice

import it.unibo.kactor.ActorBasic
import it.unibo.kactor.MsgUtil
import it.unibo.kactor.QakContext
import it.unibo.map_editor_bcr.model.map_config.CellType
import unibo.comm22.interfaces.Interaction2021
import unibo.comm22.tcp.TcpClientSupport
import unibo.comm22.utils.ColorsOut
import unibo.comm22.utils.CommSystemConfig
import unibo.comm22.utils.CommUtils
import wasteservice.MapConfigSupport
import wasteservice.WSconstants
import wasteservice.WasteType
import kotlin.concurrent.thread
import kotlin.test.assertTrue
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class TestMapConfig {
    companion object {
        private const val filename = "mapConfigWasteService"
        private lateinit var mapConfigSupport: MapConfigSupport
    }

    @BeforeTest
    fun setup() {
        mapConfigSupport = MapConfigSupport()
    }

    @Test
    fun testLoadMap() {
        mapConfigSupport.loadMapConfig(filename)
        println("Loaded Map $filename:")
        println(mapConfigSupport.mapToFancyString())

        assertTrue { mapConfigSupport.mapToString().contains("H") }
        if(mapConfigSupport.mapToString().contains("H"))
            ColorsOut.outappl("TEST Map contains 'H': PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST Map contains 'H': FAILED", ColorsOut.RED);

        assertTrue { mapConfigSupport.mapToString().contains("I") }
        if(mapConfigSupport.mapToString().contains("I"))
            ColorsOut.outappl("TEST Map contains 'I': PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST Map contains 'I': FAILED", ColorsOut.RED);

        assertTrue { mapConfigSupport.mapToString().contains("G") }
        if(mapConfigSupport.mapToString().contains("G"))
            ColorsOut.outappl("TEST Map contains 'G': PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST Map contains 'G': FAILED", ColorsOut.RED);

        assertTrue { mapConfigSupport.mapToString().contains("P") }
        if(mapConfigSupport.mapToString().contains("P"))
            ColorsOut.outappl("TEST Map contains 'P': PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST Map contains 'P': FAILED", ColorsOut.RED);
    }

    @Test
    fun testNearestCoordinateHOME() {
        val initPos = Pair<Int, Int>(6,4)
        val testPos = Pair<Int, Int>(0,0)

        mapConfigSupport.loadMapConfig(filename)
        println("Loaded Map $filename:")
        println(mapConfigSupport.mapToFancyString())

        assertTrue { mapConfigSupport.getNearestPositionToCellType(initPos, CellType.HOME) == testPos }
        if(mapConfigSupport.getNearestPositionToCellType(initPos, CellType.HOME) == testPos)
            ColorsOut.outappl("TEST $testPos nearest HOME cell to $initPos: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST $testPos nearest HOME cell to $initPos: FAILED", ColorsOut.RED);
    }

    @Test
    fun testNearestCoordinateINDOOR() {
        val initPos = Pair<Int, Int>(1,3)
        val testPos = Pair<Int, Int>(1,4)

        mapConfigSupport.loadMapConfig(filename)
        println("Loaded Map $filename:")
        println(mapConfigSupport.mapToFancyString())

        assertTrue { mapConfigSupport.getNearestPositionToCellType(initPos, CellType.INDOOR) == testPos }
        if(mapConfigSupport.getNearestPositionToCellType(initPos, CellType.INDOOR) == testPos)
            ColorsOut.outappl("TEST $testPos nearest INDOOR cell to $initPos: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST $testPos nearest INDOOR cell to $initPos: FAILED", ColorsOut.RED);
    }

    @Test
    fun testNearestCoordinateGLASS() {
        val initPos = Pair<Int, Int>(0,0)
        val testPos = Pair<Int, Int>(4,0)

        mapConfigSupport.loadMapConfig(filename)
        println("Loaded Map $filename:")
        println(mapConfigSupport.mapToFancyString())

        assertTrue { mapConfigSupport.getNearestPositionToCellType(initPos, CellType.GLASS) == testPos }
        if(mapConfigSupport.getNearestPositionToCellType(initPos, CellType.GLASS) == testPos)
            ColorsOut.outappl("TEST $testPos is the nearest GLASS cell to $initPos: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST $testPos is the nearest GLASS cell to $initPos: FAILED", ColorsOut.RED);
    }

    @Test
    fun testNearestCoordinatePLASTIC() {
        val initPos = Pair<Int, Int>(0,0)
        val testPos = Pair<Int, Int>(4,4)

        mapConfigSupport.loadMapConfig(filename)
        println("Loaded Map $filename:")
        println(mapConfigSupport.mapToFancyString())

        assertTrue { mapConfigSupport.getNearestPositionToCellType(initPos, CellType.PLASTIC) == testPos }
        if(mapConfigSupport.getNearestPositionToCellType(initPos, CellType.PLASTIC) == testPos)
            ColorsOut.outappl("TEST $testPos nearest PLASTIC cell to $initPos: PASSED", ColorsOut.GREEN);
        else ColorsOut.outappl("TEST $testPos nearest PLASTIC cell to $initPos: FAILED", ColorsOut.RED);
    }
}