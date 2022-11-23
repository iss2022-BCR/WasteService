package it.unibo.map_editor_bcr.model.map_config;

import javafx.scene.paint.Color;

public enum CellType {
    NONE("-",Color.TRANSPARENT),
    HOME("H", Color.RED),
    INDOOR("I", Color.CYAN),
    GLASS("G", Color.GREEN),
    PLASTIC("P", Color.YELLOW),
    PAPER("p", Color.BLUE),
    METAL("M", Color.GRAY),
    ORGANIC("O", Color.BROWN);

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
            case "H":
                return CellType.HOME;
            case "I":
                return CellType.INDOOR;
            case "G":
                return CellType.GLASS;
            case "P":
                return CellType.PLASTIC;
            case "p":
                return CellType.PAPER;
            case "M":
                return CellType.METAL;
            case "O":
                return CellType.ORGANIC;
            default:
                return CellType.NONE;
        }
    }

    /**
     * Given a string, return the CellType corresponding to its code, but substituting each occurrence of CellType
     * contained in toExcludeCT with defaultCT.
     * @param str string to be parsed, representing the CellType code.
     * @param defaultCT default CellType.
     * @param toExcludeCT list of CellType to substitute with the defaultCT.
     * @return the CellType corresponding to str, or defaultCT if str is contained in toExcludeCT.
     */
    public static CellType fromCodeExclude(String str, CellType defaultCT, CellType... toExcludeCT) {
        CellType result;

        switch (str)
        {
            case "H":
                result = CellType.HOME;
                break;
            case "I":
                result = CellType.INDOOR;
                break;
            case "G":
                result = CellType.GLASS;
                break;
            case "P":
                result = CellType.PLASTIC;
                break;
            case "p":
                result = CellType.PAPER;
                break;
            case "M":
                result = CellType.METAL;
                break;
            case "O":
                result = CellType.ORGANIC;
                break;
            default:
                result = CellType.NONE;
        }

        for(CellType ct : toExcludeCT) {
            if(result.equals(ct)) {
                result = defaultCT;
                break;
            }
        }

        return result;
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

    public boolean equalsCode(String cellTypeCode) {
        return this.code.equals(cellTypeCode) || (this.code.equals("-") && cellTypeCode.isEmpty());
    }
}
