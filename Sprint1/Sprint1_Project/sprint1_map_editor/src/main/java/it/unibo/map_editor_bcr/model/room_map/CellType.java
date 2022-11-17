package it.unibo.map_editor_bcr.model.room_map;

import javafx.scene.paint.Color;

public enum CellType {
    EMPTY("1", Color.TRANSPARENT), // the cell has already been explored by the robot
    DIRTY("0", Color.TRANSPARENT), // the cell hasn't been explored yet
    ROBOT("r", Color.TRANSPARENT), // the robot is located in that cell
    OBSTACLE("X", Color.TRANSPARENT); // the cell is an obstacle

    public final String code;
    public final Color color;

    private CellType(String code, Color color) {
        this.code = code;
        this.color = color;
    }

    /**
     * Given a string, return the CellType corresponding to its code.
     * @param str string to be parsed, representing the CellType code.
     * @return the CellType corresponding to str.
     */
    public static CellType fromCode(String str) {
        switch (str)
        {
            case "1":
                return EMPTY;
            case "O":
                return DIRTY;
            case "r":
                return ROBOT;
            case "X":
                return OBSTACLE;
            default:
                return DIRTY;
        }
    }

    /**
     * Provide the RGB format of the color of the CellType
     * @return a String in the format rgb(%, %, %)
     */
    public String getRGBstring() {
        return "rgb(" + this.color.getRed()*100.0 + "%, " +
                this.color.getGreen()*100.0 + "%, " +
                this.color.getBlue()*100.0 + "%)";
    }

    /**
     * Provide the RGBA format of the color of the CellType
     * @return a String in the format rgba(%, %, %, #.#)
     */
    public String getRGBAstring() {
        return "rgba(" + this.color.getRed()*100.0 + "%, " +
                this.color.getGreen()*100.0 + "%, " +
                this.color.getBlue()*100.0 + "%, " +
                this.color.getOpacity() + ")";
    }
}
