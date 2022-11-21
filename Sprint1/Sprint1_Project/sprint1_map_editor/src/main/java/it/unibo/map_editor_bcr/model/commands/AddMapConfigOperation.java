package it.unibo.map_editor_bcr.model.commands;

import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;

public class AddMapConfigOperation implements MapConfigOperation {
    private MapConfig mapConfig;
    private int x, y;
    private CellType cell;
    private CellType cellBefore;

    public AddMapConfigOperation(MapConfig mapConfig, int x, int y, CellType cell) {
        this.mapConfig = mapConfig;
        this.x = x;
        this.y = y;
        this.cell = cell;

        this.cellBefore = CellType.NONE;
    }

    @Override
    public void execute() {
        this.cellBefore = this.mapConfig.get(this.x, this.y);
        this.mapConfig.put(this.x, this.y, this.cell);
    }

    @Override
    public void undo() {
        this.mapConfig.put(this.x, this.y, this.cellBefore);
    }

    @Override
    public String getDescription() {
        return "Add element " + this.cell.code + " in ["+x+","+y+"]";
    }
}
