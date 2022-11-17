package it.unibo.map_editor_bcr.model.commands;

import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;

public class MoveMapOperation implements MapConfigOperation {
    private MapConfig mapConfig;
    private int xSrc, ySrc, xDst, yDst;

    private int xSrcBefore, ySrcBefore, xDstBefore, yDstBefore;
    private CellType cellDstBefore; // NB the cellSrcBefore is obviously empty
    private CellType cellInvolved;



    public MoveMapOperation(MapConfig mapConfig, int xSrc, int ySrc, int xDst, int yDst) {
        this.mapConfig = mapConfig;
        this.xSrc = xSrc;
        this.ySrc = ySrc;
        this.xDst = xDst;
        this.yDst = yDst;

        cellInvolved = mapConfig.get(xSrc, ySrc);

        this.xSrcBefore = 0;
        this.ySrcBefore = 0;
        this.xDstBefore = 0;
        this.yDstBefore = 0;
        this.cellDstBefore = CellType.NONE;
    }

    @Override
    public void execute() {
        this.xSrcBefore = xSrc;
        this.ySrcBefore = ySrc;
        this.xDstBefore = xDst;
        this.yDstBefore = yDst;
        this.cellDstBefore = this.mapConfig.get(xDst, yDst);

        this.mapConfig.move(xSrc, ySrc, xDst, yDst);

        System.out.println("EXEC VALUES: \n" +
                "xSrcBefore: " + xSrcBefore + "\n" +
                "ySrcBefore: " + ySrcBefore + "\n" +
                "cellSrc: " + cellInvolved + "\n" +
                "xDstBefore: " + xDstBefore + "\n" +
                "yDstBefore: " + yDstBefore + "\n" +
                "cellDstBefore: " + cellDstBefore + "\n"); // TEST
    }

    @Override
    public void undo() {
        this.mapConfig.put(xSrcBefore, ySrcBefore, cellInvolved);
        this.mapConfig.put(xDstBefore, yDstBefore, cellDstBefore);
        //this.mapConfig.move(cellDstBefore, xSrcBefore, ySrcBefore, xDstBefore, yDstBefore);
    }

    @Override
    public String getDescription() {
        return "Moved " + this.cellInvolved.code + " from ["+xSrc+","+ySrc+"] to ["+xDst+","+yDst+"]";
    }
}
