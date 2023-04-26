package wasteservice.gui;

import wasteservice.WSconstants;

public class StatusUpdate {
    private double maxPlastic = WSconstants.DEFAULT_MAX;
    private double maxGlass = WSconstants.DEFAULT_MAX;
    private int roomRows = 5;
    private int roomColumns = 7;
    private int[] locationHome = { 0, 0 };
    private int[][] locationIndoor = { { 4, 0 }, { 4, 1 }, { 4, 2 } };
    private int[][] locationPlastic = { { 2, 6 }, { 3, 6 }, { 4, 6 } };
    private int[][] locationGlass = { { 0, 4 }, { 0, 5 }, { 0, 6 } };

    private double currentPlastic = 0.0;
    private double currentGlass = 0.0;
    private String ledState = "OFF";

    private int[] transportTrolleyPosition = { 0, 0 };
    private String transportTrolleyState = "HOME";

    public StatusUpdate() {}


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

    public double getCurrentPlastic() { return currentPlastic; }
    public void setCurrentPlastic(double currentPlastic) { this.currentPlastic = currentPlastic; }
    public double getCurrentGlass() { return currentGlass; }
    public void setCurrentGlass(double currentGlass) { this.currentGlass = currentGlass; }
    public String getLedState() { return ledState; }
    public void setLedState(String ledState) { this.ledState = ledState; }
    public int[] getTransportTrolleyPosition() { return transportTrolleyPosition; }
    public void setTransportTrolleyPosition(int[] transportTrolleyPosition) { this.transportTrolleyPosition = transportTrolleyPosition; }
    public void setTransportTrolleyPosition(int x, int y) { this.transportTrolleyPosition = new int[]{ x, y }; }
    public String getTransportTrolleyState() { return transportTrolleyState; }
    public void setTransportTrolleyState(String transportTrolleyState) { this.transportTrolleyState = transportTrolleyState; }
}
