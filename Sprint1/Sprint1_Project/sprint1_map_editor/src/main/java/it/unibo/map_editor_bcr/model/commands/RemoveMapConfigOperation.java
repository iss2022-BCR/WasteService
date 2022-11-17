package it.unibo.map_editor_bcr.model.commands;

import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;

public class RemoveMapConfigOperation implements MapConfigOperation {
    private MapConfig mapConfig;
    private int x, y;

    private CellType cellBefore;

    public RemoveMapConfigOperation(MapConfig mapConfig, int x, int y) {
        this.mapConfig = mapConfig;
        this.x = x;
        this.y = y;

        this.cellBefore = CellType.NONE;
    }

    @Override
    public void execute() {
        this.cellBefore = this.mapConfig.get(this.x, this.y);
        this.mapConfig.put(this.x, this.y, CellType.NONE);
    }

    @Override
    public void undo() {
        this.mapConfig.put(this.x, this.y, this.cellBefore);
    }

    @Override
    public String getDescription() {
        return "Remove element in ["+x+","+y+"] from " + this.cellBefore.code + " to " + CellType.NONE.code;
    }
}
