package it.unibo.map_editor_bcr.model.settings;

public class Settings {
    private String roomMapFile;
    private String mapConfigFile;
    private final LogLevel logLevel = new LogLevel();
    private boolean confirmations;
    private boolean darkTheme;
    private String coordinateColor;

    public Settings() {
        this.darkTheme = Constants.DEFAULT_DARK_THEME;
        this.logLevel.toStandardOutput = Constants.DEFAULT_LOG_LEVEL_TO_STD_OUT;
        this.logLevel.toFile = Constants.DEFAULT_LOG_LEVEL_TO_FILE;
        this.logLevel.directory = Constants.DEFAULT_LOG_LEVEL_DIRECTORY;
        this.roomMapFile = Constants.DEFAULT_ROOM_MAP_FILE;
        this.mapConfigFile = Constants.DEFAULT_MAP_CONFIG_FILE;
        this.confirmations = Constants.DEFAULT_CONFIRM_BEFORE_SAVING;
        this.coordinateColor = Constants.DEFAULT_COORDINATE_COLOR;
    }
    public Settings(String roomMapFile, String mapConfigFile,
                    boolean logLevelToStandardOutput, boolean logLevelToFile, String logLevelDirectory,
                    boolean confirmations, boolean darkTheme, String coordinateColor) {
        this.roomMapFile = roomMapFile;
        this.mapConfigFile = mapConfigFile;
        this.logLevel.toStandardOutput = logLevelToStandardOutput;
        this.logLevel.toFile = logLevelToFile;
        this.logLevel.directory = logLevelDirectory;
        this.confirmations = confirmations;
        this.darkTheme = darkTheme;
        this.coordinateColor = coordinateColor;
    }

    public String getRoomMapFile() {return roomMapFile;}
    public void setRoomMapFile(String roomMapFile) {this.roomMapFile = roomMapFile;}
    public String getMapConfigFile() {return mapConfigFile;}
    public void setMapConfigFile(String mapConfigFile) {this.mapConfigFile = mapConfigFile;}
    public LogLevel getLogLevel() {return logLevel;}
    public void setLogLevel(boolean toStdOutput, boolean toFile, String directory) {
        logLevel.toStandardOutput = toStdOutput;
        logLevel.toFile = toFile;
        logLevel.directory = directory;
    }
    public boolean isConfirmations() {return this.confirmations;}
    public void setConfirmations(boolean confirmations) {this.confirmations = confirmations;}
    public boolean isDarkTheme() {return darkTheme;}
    public void setDarkTheme(boolean darkTheme) {this.darkTheme = darkTheme;}
    public String getCoordinateColor() {return coordinateColor;}
    public void setCoordinateColor(String coordinateColor) {this.coordinateColor = coordinateColor;}
}
