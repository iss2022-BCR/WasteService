package wasteservice.gui;

import it.unibo.map_editor_bcr.model.map_config.MapConfig;
import wasteservice.WSconstants;

import java.util.ArrayList;

public class WasteServiceConfiguration {
    private double maxPlastic = WSconstants.DEFAULT_MAX;
    private double maxGlass = WSconstants.DEFAULT_MAX;

    private int roomRows = 5;
    private int roomColumns = 7;
    private int[] locationHome = { 0, 0 };
    private int[][] locationIndoor = { { 4, 0 }, { 4, 1 }, { 4, 2 } };
    private int[][] locationPlastic = { { 2, 6 }, { 3, 6 }, { 4, 6 } };
    private int[][] locationGlass = { { 0, 4 }, { 0, 5 }, { 0, 6 } };

    private StatusUpdate statusUpdate = new StatusUpdate();



    public double getMaxPlastic() { return maxPlastic; }
    public void setMaxPlastic(double maxPlastic) { this.maxPlastic = maxPlastic; }
    public double getMaxGlass() { return maxGlass; }
    public void setMaxGlass(double maxGlass) { this.maxGlass = maxGlass; }

    public int getRoomRows() { return roomRows; }
    public void setRoomRows(int roomRows) { this.roomRows = roomRows; }
    public int getRoomColumns() { return roomColumns; }
    public void setRoomColumns(int roomColumns) { this.roomColumns = roomColumns; }
    public int[] getLocationHome() { return locationHome; }
    public void setLocationHome(int[] locationHome) { this.locationHome = locationHome; }
    public int[][] getLocationIndoor() { return locationIndoor; }
    public void setLocationIndoor(int[][] locationIndoor) { this.locationIndoor = locationIndoor; }
    public int[][] getLocationPlastic() { return locationPlastic; }
    public void setLocationPlastic(int[][] locationPlastic) { this.locationPlastic = locationPlastic; }
    public int[][] getLocationGlass() { return locationGlass; }
    public void setLocationGlass(int[][] locationGlass) { this.locationGlass = locationGlass; }

    public StatusUpdate getStatusUpdate() { return statusUpdate; }
    public void setStatusUpdate(StatusUpdate statusUpdate) { this.statusUpdate = statusUpdate; }

    public void setMapConfig(MapConfig mapConfig)
    {
        roomColumns = mapConfig.getDimX();
        roomRows = mapConfig.getDimY();

        // TO-DO: parse to obtain cells locations according to MapConfig
    }
}
