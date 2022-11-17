package it.unibo.map_editor_bcr.model.commands;

import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;
import javafx.scene.control.Cell;

public class SwapMapConfigOperation implements MapConfigOperation {
    private MapConfig mapConfig;
    private int xSrc, ySrc, xDst, yDst;

    private CellType cellSrc;
    private CellType cellDst;

    public SwapMapConfigOperation(MapConfig mapConfig, int xSrc, int ySrc, int xDst, int yDst) {
        this.mapConfig = mapConfig;
        this.xSrc = xSrc;
        this.ySrc = ySrc;
        this.xDst = xDst;
        this.yDst = yDst;

        this.cellSrc = CellType.NONE;
        this.cellDst = CellType.NONE;
    }

    @Override
    public void execute() {
        this.cellSrc = this.mapConfig.get(xSrc, ySrc);
        this.cellDst = this.mapConfig.get(xDst, yDst);
        this.mapConfig.put(xDst, yDst, cellSrc);
        this.mapConfig.put(xSrc, ySrc, cellDst);
    }

    @Override
    public void undo() {
        this.mapConfig.put(xDst, yDst, cellDst);
        this.mapConfig.put(xSrc, ySrc, cellSrc);
    }

    @Override
    public String getDescription() {
        return "Swap " + this.cellSrc.code + " from ["+xSrc+","+ySrc+"] with " + this.cellDst.code + " from  ["+xDst+","+yDst+"]";
    }
}
