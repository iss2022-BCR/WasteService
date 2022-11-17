package it.unibo.map_editor_bcr.model.commands;

import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;

public class SwapMapConfigOperation implements MapConfigOperation {
    private MapConfig mapConfig;
    private int x, y, xBefore, yBefore;
    private int xSrc, ySrc, xDst, yDst;

    private int xSrcBefore, ySrcBefore, xDstBefore, yDstBefore;
    private CellType cellSrcBefore;
    private CellType cellDstBefore;

    public SwapMapConfigOperation(MapConfig mapConfig, int xSrc, int ySrc, int xDst, int yDst) {
        this.x = xDst;
        this.y = yDst;
        this.xBefore = xSrc;
        this.yBefore = ySrc;

        this.mapConfig = mapConfig;
        this.xSrc = xSrc;
        this.ySrc = ySrc;
        this.xDst = xDst;
        this.yDst = yDst;

        this.xSrcBefore = -1;
        this.ySrcBefore = -1;
        this.xDstBefore = -1;
        this.yDstBefore = -1;
    }

    @Override
    public void execute() {
        this.mapConfig.swap(xBefore, yBefore, x, y);
    }

    @Override
    public void undo() {
        this.mapConfig.swap(x, y, xBefore, yBefore);
    }

    @Override
    public String getDescription() {
        return null;
    }
}
