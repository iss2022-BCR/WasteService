package it.unibo.map_editor_bcr.model.commands;

import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;

public class MoveMapOperation implements MapConfigOperation {
    private MapConfig mapConfig;
    private int xSrc, ySrc, xDst, yDst;

    private CellType cellDstBefore; // NB the cellSrcBefore is obviously empty
    private CellType cellInvolved;

    public MoveMapOperation(MapConfig mapConfig, int xSrc, int ySrc, int xDst, int yDst) {
        this.mapConfig = mapConfig;
        this.xSrc = xSrc;
        this.ySrc = ySrc;
        this.xDst = xDst;
        this.yDst = yDst;

        cellInvolved = mapConfig.get(xSrc, ySrc);

        this.cellDstBefore = CellType.NONE;
    }

    @Override
    public void execute() {
        this.cellDstBefore = this.mapConfig.get(xDst, yDst);

        CellType tmp = this.mapConfig.get(xSrc, ySrc);
        this.mapConfig.put(xDst, yDst, tmp);
        this.mapConfig.put(xSrc, ySrc, CellType.NONE);
    }

    @Override
    public void undo() {
        CellType tmp = this.mapConfig.get(xDst, yDst);
        this.mapConfig.put(xDst, yDst, this.cellDstBefore);
        this.mapConfig.put(xSrc, ySrc, tmp);
    }

    @Override
    public String getDescription() {
        return "Move " + this.cellInvolved.code + " from ["+xSrc+","+ySrc+"] to ["+xDst+","+yDst+"]";
    }
}
