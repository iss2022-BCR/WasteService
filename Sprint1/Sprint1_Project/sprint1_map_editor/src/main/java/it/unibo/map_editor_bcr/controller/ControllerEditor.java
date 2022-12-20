package it.unibo.map_editor_bcr.controller;

import it.unibo.map_editor_bcr.model.Logger;
import it.unibo.map_editor_bcr.model.MapLoader;
import it.unibo.map_editor_bcr.model.commands.*;
import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;
import it.unibo.map_editor_bcr.model.persistence.SettingsManager;
import it.unibo.map_editor_bcr.model.room_map.RoomMapParser;
import it.unibo.map_editor_bcr.model.settings.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import unibo.planner22.model.RoomMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ControllerEditor {
    private final String SETTINGS_FILENAME = ".settings.json";
    private final double ELEMENT_SIZE = 50.0;
    private final int MODE_NONE = -1;
    private final int MODE_ADD = 0;
    private final int MODE_REMOVE = 1;
    private final int MODE_MOVE = 2;
    private final int MODE_SWAP = 3;

    // FXML components
    @FXML private AnchorPane anchorPaneRoot;
    @FXML private AnchorPane anchorPaneBase;

    // RoomMap not loaded yet
    @FXML private VBox vboxRoomMapNotLoaded;
    @FXML private Button buttonLoadRoomMap;
    @FXML private Label labelLoadRoomMapError;

    // Map elements
    private Pane paneRoomMap;
    private Pane paneCoordinates;
    private Pane paneMapConfig;
    private Label[][] labelConfig;

    // Map controls
    @FXML private HBox hboxAction;
    @FXML private ComboBox<String> comboBoxHistory;
    @FXML private VBox vboxDisplayControls;
    @FXML private VBox vboxCommandControls;
    @FXML private HBox hboxFileControls;
    @FXML private HBox hboxActionControls;
    @FXML private Button buttonNew;
    @FXML private Button buttonLoad;
    @FXML private Button buttonSave;
    @FXML private Button buttonSaveAs;
    @FXML private ToggleGroup toggleGroupMode;
    @FXML private ToggleButton buttonAdd;
    @FXML private ToggleButton buttonRemove;
    @FXML private ToggleButton buttonMove;
    @FXML private ToggleButton buttonSwap;
    @FXML private Button buttonUndo;
    @FXML private Button buttonRedo;
    @FXML private Button buttonSettings;

    @FXML private VBox vboxAddTab;
    @FXML private Button buttonOpenCloseEditTab;
    @FXML private ListView<Label> listViewCells;

    // Map layers controls
    @FXML private CheckBox checkBoxRoomMap;
    @FXML private CheckBox checkBoxMapConfig;
    @FXML private CheckBox checkBoxCoordinates;

    // Settings controls
    @FXML private VBox vboxSettings;
    @FXML private TextField textFieldRoomMapFile;
    @FXML private TextField textFieldMapConfigFile;
    @FXML private CheckBox checkboxLogStdOut;
    @FXML private CheckBox checkboxLogFile;
    @FXML private HBox hboxLogDirPath;
    @FXML private TextField textFieldLogDirPath;
    @FXML private CheckBox checkBoxConfirmations;
    @FXML private CheckBox checkBoxDarkTheme;
    @FXML private ColorPicker colorPickerCoordinates;


    private SettingsManager settings;
    private Logger logger;

    private final FileChooser fileChooserRoomMap = new FileChooser();
    private final FileChooser fileChooserMapConfig = new FileChooser();
    private final DirectoryChooser directoryChooserLog = new DirectoryChooser();

    private RoomMapParser roomMapParser;
    private MapConfig mapConfig;
    private int dimX, dimY;

    private MapConfigOperationExecutor mapConfigOperationExecutor;

    private int mode = -1;

    public ControllerEditor() {
        // Load settings
        this.settings = SettingsManager.getInstance();
        this.settings.loadSettings(".settings.json");

        // Load logger
        this.logger = Logger.getInstance();
        this.logger.setLogLevel(this.settings.getLogLevel());

        // Setup FileChoosers
        this.fileChooserRoomMap.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("RoomMap (*.bin)", "*.bin")
        );
        this.fileChooserRoomMap.setInitialDirectory(new File("."));
        this.fileChooserMapConfig.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("MapConfig (*.bin)", "*.bin")
        );
        this.fileChooserMapConfig.setInitialDirectory(new File("."));

        // Setup DirectoryChooser
        this.directoryChooserLog.setInitialDirectory(new File("."));
    }

    public void initialize() {
        this.initAddElementList();

        // Load RoomMap
        this.loadRoomMap(this.settings.getRoomMapFile());

        // Load MapConfig
        if(this.roomMapParser != null) {
            // Load MapConfig if default file path is present in settings
            this.loadMapConfig(this.settings.getMapConfigFile());
        }

        // Set controls on foreground
        this.anchorPaneBase.getChildren().remove(this.vboxDisplayControls);
        this.anchorPaneBase.getChildren().add(this.vboxDisplayControls);
        this.anchorPaneBase.getChildren().remove(this.vboxCommandControls);
        this.anchorPaneBase.getChildren().add(this.vboxCommandControls);
        this.anchorPaneBase.getChildren().remove(this.vboxAddTab);
        this.anchorPaneBase.getChildren().add(this.vboxAddTab);

        // Set visibility
        this.vboxSettings.setVisible(false);

        // Update settings GUI components according to settings values
        this.updateGUIfromSettings();

        Platform.runLater(() -> {
            this.adjustLayout();

            Scene scene = (Scene) this.vboxCommandControls.getScene();
            // Set keyboard shortcuts
            scene.getAccelerators().put(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN), () -> {this.undo(new ActionEvent());});
            scene.getAccelerators().put(new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN), () -> {this.redo(new ActionEvent());});

            Stage stage = (Stage) scene.getWindow();
            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                this.adjustLayout();
            });
            stage.heightProperty().addListener((obs, oldVal, newVal) -> {
                this.adjustLayout();
            });
            stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
                Timeline tl = new Timeline(new KeyFrame(Duration.millis(1), ae -> {
                    this.adjustLayout();
                }));
                tl.setCycleCount(1);
                tl.play();

                /* // Disable Maximize (make the button do nothing)
                if (newVal)
                    stage.setMaximized(false);*/
            });
        });
    }

    private void clearRoomMap() {
        this.roomMapParser = null;
        this.dimX = 0;
        this.dimY = 0;
        this.anchorPaneBase.getChildren().remove(this.paneRoomMap);
        this.paneRoomMap = null;

        this.textFieldRoomMapFile.setText("");
        this.settings.setRoomMapFile("");
        this.settings.saveSettings(SETTINGS_FILENAME);

        this.vboxRoomMapNotLoaded.setVisible(true);
        this.vboxDisplayControls.setVisible(false);
        this.hboxFileControls.setDisable(true);
        this.hboxActionControls.setDisable(true);
        this.hboxAction.setDisable(true);

        this.clearMapConfig();
    }
    private void clearMapConfig() {
        this.mapConfig = null;
        this.anchorPaneBase.getChildren().remove(this.paneMapConfig);
        this.paneMapConfig = null;
        this.anchorPaneBase.getChildren().remove(this.paneCoordinates);
        this.paneCoordinates = null;

        this.textFieldMapConfigFile.setText("");
        this.settings.setMapConfigFile("");
        this.settings.saveSettings(SETTINGS_FILENAME);

        this.vboxDisplayControls.setVisible(false);
        this.buttonSave.setDisable(true);
        this.buttonSaveAs.setDisable(true);
        this.hboxActionControls.setDisable(true);
        this.hboxAction.setDisable(true);
    }

    private void loadRoomMap(String filename) {
        if(filename.isEmpty()) {
            this.labelLoadRoomMapError.setText("");
            this.clearRoomMap();
            return;
        }

        try {
            this.clearMapConfig();

            RoomMap roomMap = MapLoader.loadRoomMap(filename);

            this.roomMapParser = new RoomMapParser(roomMap);
            this.dimX = this.roomMapParser.getDimX();
            this.dimY = this.roomMapParser.getDimY();

            this.initRoomMap();

            // update settings
            this.textFieldRoomMapFile.setText(filename);
            this.textFieldMapConfigFile.setText("");
            this.settings.setRoomMapFile(filename);
            this.settings.saveSettings(SETTINGS_FILENAME);

            this.vboxRoomMapNotLoaded.setVisible(false);
            this.vboxDisplayControls.setVisible(false);
            this.hboxFileControls.setDisable(false);
            this.buttonSave.setDisable(true);
            this.buttonSaveAs.setDisable(true);
            this.hboxActionControls.setDisable(true);
            this.hboxAction.setDisable(true);

            logger.logMessage("RoomMap loaded from '" + filename + "': " + this.dimX + " x " + this.dimY);
            logger.logMessage(roomMap.toString());
        } catch (Exception e) {
            logger.logMessage("RoomMap load failed. Error: " + e.getMessage());

            // if the loading from file failed -> reset settings value
            this.clearRoomMap();

            if(e instanceof FileNotFoundException) {
                this.labelLoadRoomMapError.setText("ERROR: file '" + filename + "' not found.");
            } else if (e instanceof ClassNotFoundException) {
                this.labelLoadRoomMapError.setText("ERROR: file '" + filename +
                        "' does not contain an object from class 'unibo.planner22.model.RoomMap'.");
            } else if (e instanceof IllegalArgumentException) {
                this.labelLoadRoomMapError.setText("");
            } else {
                this.labelLoadRoomMapError.setText("ERROR: " + e.getMessage());
            }

            e.printStackTrace();
        }
    }

    private void loadMapConfig(String filename) {
        if(filename.isEmpty()) {
            this.vboxDisplayControls.setVisible(false);
            this.hboxFileControls.setDisable(false);
            this.buttonSave.setDisable(true);
            this.buttonSaveAs.setDisable(true);
            this.hboxActionControls.setDisable(true);
            this.hboxAction.setDisable(true);
            return;
        }

        try {
            this.mapConfig = MapLoader.loadMapConfig(filename);

            if(mapConfig.getDimX() != roomMapParser.getDimX() ||
                    mapConfig.getDimY() != roomMapParser.getDimY()) {
                throw new Exception("MapConfig size (" + mapConfig.getDimX() + "x" + mapConfig.getDimY() +
                        ") does not match RoomMap size (" + roomMapParser.getDimX() + "x" + roomMapParser.getDimY() + ")");
            }

            this.initMapConfig();
            this.initMapConfigOperationExecutor();
            this.initCoordinates(Color.valueOf(this.settings.getCoordinateColor()));

            // update settings
            this.textFieldMapConfigFile.setText(filename);
            this.settings.setMapConfigFile(filename);
            this.settings.saveSettings(SETTINGS_FILENAME);

            this.vboxDisplayControls.setVisible(true);
            this.hboxFileControls.setDisable(false);
            this.buttonSave.setDisable(false);
            this.buttonSaveAs.setDisable(false);
            this.hboxActionControls.setDisable(false);
            this.hboxAction.setDisable(false);

            logger.logMessage("MapConfig loaded from '" + filename + "': " + this.dimX + " x " + this.dimY);
            logger.logMessage(mapConfig.toFancyString());
        } catch (Exception e) {
            logger.logMessage("MapConfig load failed. Error: " + e.getMessage());

            // if the loading from file failed -> reset settings value
            this.clearMapConfig();

            e.printStackTrace();
        }
    }

    private void saveMapConfig(String filename) {
        try {
            MapLoader.saveMapConfig(this.mapConfig, filename);

            logger.logMessage("MapConfig saved to file '" + filename + "'.");

            // update settings file
            this.textFieldMapConfigFile.setText(filename);
            this.settings.setMapConfigFile(filename);
            this.settings.saveSettings(SETTINGS_FILENAME);
        } catch (Exception e) {
            logger.logMessage("MapConfig save failed. Error: " + e.getMessage());
        }
    }

    @FXML
    public void openRoomMapFile(ActionEvent event) {
        this.fileChooserRoomMap.setTitle("Open RoomMap file (unibo.planner22.model.RoomMap)");

        File file = this.fileChooserRoomMap.showOpenDialog(null);
        if(file == null) {
            return;
        }
        this.loadRoomMap(file.getAbsolutePath());
    }

    /**
     * Adjust element positions according to window size.
     * To be called when the window gets resized.
     */
    private void adjustLayout() {
        //this.vboxRoomMapNotLoaded.setLayoutX(this.anchorPaneBase.getWidth() / 2); // TO-DO

        this.vboxRoomMapNotLoaded.setLayoutX((this.anchorPaneBase.getWidth() - this.vboxRoomMapNotLoaded.getWidth()) / 2);
        this.vboxRoomMapNotLoaded.setLayoutY((this.anchorPaneBase.getHeight() - this.vboxRoomMapNotLoaded.getHeight()) / 2);
        this.vboxCommandControls.setLayoutX((this.anchorPaneBase.getWidth() - this.vboxCommandControls.getWidth()) / 2);
        this.vboxDisplayControls.setLayoutY((this.anchorPaneBase.getHeight() - this.vboxDisplayControls.getHeight() - this.vboxCommandControls.getHeight()) / 2
            );

        if(this.roomMapParser != null) {
            this.paneRoomMap.setLayoutX((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2);
            this.paneRoomMap.setLayoutY((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4);
            }
        if(this.mapConfig != null) {
            this.paneMapConfig.setLayoutX(((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2));
            this.paneMapConfig.setLayoutY(((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4));
            this.paneCoordinates.setLayoutX((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2);
            this.paneCoordinates.setLayoutY((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4);
        }

        // adjust listview
    }

    private void initMapConfigOperationExecutor() {
        this.mapConfigOperationExecutor = new MapConfigOperationExecutor();

        this.comboBoxHistory.setValue("");
        this.comboBoxHistory.getItems().clear();
        this.buttonUndo.setDisable(true);
        this.buttonRedo.setDisable(true);
    }

    /**
     * Initialize the base map: RoomMap (the map representation we got from the mapper).
     */
    private void initRoomMap() {
        this.anchorPaneBase.getChildren().remove(this.paneRoomMap);

        this.paneRoomMap = new Pane();
        this.paneRoomMap.setVisible(false);
        this.paneRoomMap.setPrefSize(ELEMENT_SIZE * dimX, ELEMENT_SIZE * dimY);
        this.paneRoomMap.setSnapToPixel(false);
        this.paneRoomMap.setStyle("-fx-border-color: black; -fx-border-insets: -1");
        this.paneRoomMap.getChildren().clear();

        for(int y = 0; y < this.dimY; y++) {
            for (int x = 0; x < this.dimX; x++) {
                it.unibo.map_editor_bcr.model.room_map.CellType ct = this.roomMapParser.getCellType(x, y);
                this.paneRoomMap.getChildren().add(
                        this.buildRoomMapElement(ct, ELEMENT_SIZE * x, ELEMENT_SIZE * y, 0.1));
            }
        }
        this.anchorPaneBase.getChildren().add(0, this.paneRoomMap);

        this.paneRoomMap.setLayoutX((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2);
        this.paneRoomMap.setLayoutY((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4);
        this.paneRoomMap.setVisible(true);
    }

    private Label buildRoomMapElement(it.unibo.map_editor_bcr.model.room_map.CellType ct, double x, double y, double opacity) {
        Label l = new Label(ct.code);
        l.setAlignment(Pos.CENTER);
        l.setFont(Font.font("System", FontWeight.BOLD, 30.0));
        l.setPrefSize(ELEMENT_SIZE, ELEMENT_SIZE);
        l.setOpacity(opacity);
        l.setStyle("-fx-background-color: " + ct.getRGBAstring());
        l.setLayoutX(x);
        l.setLayoutY(y);

        return l;
    }

    private void initCoordinates(Color c) {
        if(this.mapConfig == null) {
            return;
        }
        double offset = ELEMENT_SIZE / 8;

        this.anchorPaneBase.getChildren().remove(this.paneCoordinates);

        this.paneCoordinates = new Pane();
        this.paneCoordinates.setStyle("-fx-border-color: rgb(" + c.getRed()*100.0 + "%, " + c.getGreen()*100.0 + "%, " + c.getBlue()*100.0 +"%);" +
                "-fx-border-width: 2 0 0 2;" +
                "-fx-border-insets: -" + offset);
        this.paneCoordinates.setVisible(false);
        this.paneCoordinates.setPrefSize(ELEMENT_SIZE * dimX, ELEMENT_SIZE * dimY);

        for(int y = 0; y < this.dimY; y++) {
            this.paneCoordinates.getChildren().add(buildCoordinateElement(y, c, -ELEMENT_SIZE - offset, ELEMENT_SIZE * y));
        }
        for (int x = 1; x < this.dimX+1; x++) {
            this.paneCoordinates.getChildren().add(buildCoordinateElement(x-1, c, ELEMENT_SIZE * (x-1), -ELEMENT_SIZE - offset));
        }

        this.anchorPaneBase.getChildren().add(1, this.paneCoordinates);

        this.paneCoordinates.setLayoutX((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2);
        this.paneCoordinates.setLayoutY((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4);
        this.paneCoordinates.setVisible(this.checkBoxCoordinates.isSelected());
    }
    private Label buildCoordinateElement(int value,  Color c, double x, double y) {
        Label l = new Label("" + value);
        l.setLayoutX(x);
        l.setLayoutY(y);
        l.setAlignment(Pos.CENTER);
        l.setStyle("-fx-text-fill: rgb(" + c.getRed()*100.0 + "%, " + c.getGreen()*100.0 + "%, " + c.getBlue()*100.0 +"%)");
        l.setFont(Font.font("System", FontWeight.BOLD, 30.0));
        l.setPrefSize(ELEMENT_SIZE, ELEMENT_SIZE);

        return l;
    }

    private void initMapConfig() {
        this.anchorPaneBase.getChildren().remove(this.paneMapConfig);

        this.labelConfig = new Label[dimY][dimX];
        this.paneMapConfig = new Pane();
        this.paneMapConfig.setVisible(false); // make it visible when it finished building
        this.paneMapConfig.setPrefSize(ELEMENT_SIZE * dimX, ELEMENT_SIZE * dimY);
        this.paneMapConfig.setSnapToPixel(false);
        this.paneMapConfig.setStyle("-fx-border-color: black; -fx-border-insets: -1");
        this.paneMapConfig.getChildren().clear();

        for(int y = 0; y < dimY; y++) {
            for(int x = 0; x < dimX; x++) {
                Label l = buildMapConfigElement(this.mapConfig.get(x, y), x * ELEMENT_SIZE, y * ELEMENT_SIZE);
                this.paneMapConfig.getChildren().add(l);
                this.putLabel(x, y, l);
            }
        }
        this.anchorPaneBase.getChildren().add(2, this.paneMapConfig);

        this.paneMapConfig.setLayoutX(((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2) + 1.0);
        this.paneMapConfig.setLayoutY(((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4) + 1.0);
        this.paneMapConfig.setVisible(true);
    }

    private Label buildMapConfigElement(CellType ct, double x, double y) {
        Label l = new Label(ct.equals(CellType.NONE) ? "" : ct.code);
        l.setLayoutX(x);
        l.setLayoutY(y);
        l.setAlignment(Pos.CENTER);
        l.setFont(Font.font("System", FontWeight.BOLD, 30.0));
        l.setPrefSize(ELEMENT_SIZE, ELEMENT_SIZE);
        l.setStyle("-fx-background-color: " + ct.getRGBAstring());

        if(!ct.equals(CellType.NONE)) {
            Tooltip t = new Tooltip(ct.name());
            t.setFont(Font.font("System", FontWeight.BOLD, 14.0));
            l.setTooltip(t);
        }

        final DropShadow dropShadow = new DropShadow();
        final Glow glow = new Glow();

        final Delta dragDelta = new Delta();
        final Transform move = new Transform();

        l.setOnMouseEntered(mouseEvent -> {
            if((!ct.equals(CellType.NONE) && (this.mode == MODE_REMOVE || this.mode == MODE_MOVE))
                    || this.mode == MODE_SWAP) {
                l.setCursor(Cursor.HAND);
                dropShadow.setInput(glow);
            }
        });
        l.setOnMouseExited(mouseEvent -> {
            if((!ct.equals(CellType.NONE) && (this.mode == MODE_REMOVE || this.mode == MODE_MOVE))
                    || this.mode == MODE_SWAP) {
                l.setCursor(Cursor.DEFAULT);
                dropShadow.setInput(null);
            }
        });
        l.setOnMousePressed(mouseEvent -> {
            if((!ct.equals(CellType.NONE) && this.mode == MODE_MOVE)
                    || this.mode == MODE_SWAP) {
                // get initial position
                move.srcX = l.getLayoutX();
                move.srcY = l.getLayoutY();

                // move at the end, so it's displayed over the others.
                this.paneMapConfig.getChildren().remove(l);
                this.paneMapConfig.getChildren().add(l);

                // select
                l.setEffect(dropShadow);

                // record a delta distance for the drag and drop operation.
                dragDelta.x = l.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = l.getLayoutY() - mouseEvent.getSceneY();
                l.setCursor(Cursor.CLOSED_HAND);
            }
        });
        l.setOnMouseReleased(mouseEvent -> {
            if ((!ct.equals(CellType.NONE) && this.mode == MODE_MOVE)
                    || this.mode == MODE_SWAP) {
                l.setEffect(null);
                l.setCursor(Cursor.HAND);

                double centerX = l.getLayoutX() + ELEMENT_SIZE / 2;
                double centerY = l.getLayoutY() + ELEMENT_SIZE / 2;

                // if not allowed -> reset to initial position
                if (centerX < 0.0 || centerX > ELEMENT_SIZE * this.dimX ||
                        centerY < 0.0 || centerY > ELEMENT_SIZE * this.dimY) {
                    l.setCursor(Cursor.DISAPPEAR);
                    // reset to initial
                    l.setLayoutX(move.srcX);
                    l.setLayoutY(move.srcY);
                } else {
                    // snap to cell
                    // get cursor position, not element!!!
                    l.setLayoutX(centerX - (centerX % ELEMENT_SIZE));
                    l.setLayoutY(centerY - (centerY % ELEMENT_SIZE));
                    move.dstX = l.getLayoutX();
                    move.dstY = l.getLayoutY();

                    switch (mode) {
                        case MODE_MOVE:
                            if (move.srcX != move.dstX || move.srcY != move.dstY) {
                                this.move(l, move.srcX, move.srcY, move.dstX, move.dstY);
                            }
                            break;
                        case MODE_SWAP:
                            if (move.srcX != move.dstX || move.srcY != move.dstY) {
                                this.swap(l, move.srcX, move.srcY, move.dstX, move.dstY);
                            }
                            break;
                        default:
                            this.logger.logMessage("Unkown operation: " + mode);
                    }
                }
            }
        });
        l.setOnMouseDragged(mouseEvent -> {
            if((!ct.equals(CellType.NONE) && this.mode == MODE_MOVE)
                    || this.mode == MODE_SWAP) {
                l.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                l.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                if (l.getLayoutX() + ELEMENT_SIZE / 2 < 0.0 || l.getLayoutX() + ELEMENT_SIZE / 2 > ELEMENT_SIZE * this.dimX ||
                        l.getLayoutY() + ELEMENT_SIZE / 2 < 0.0 || l.getLayoutY() + ELEMENT_SIZE / 2 > ELEMENT_SIZE * this.dimY) {
                    l.setCursor(Cursor.NONE);
                } else {
                    l.setCursor(Cursor.CLOSED_HAND);
                }
            }
        });
        l.setOnMouseClicked(mouseEvent -> {
            if(!ct.equals(CellType.NONE) && this.mode == MODE_REMOVE) {
                this.remove(l.getLayoutX(), l.getLayoutY());
            }
        });

        return l;
    }

    private Label getLabel(int x, int y) {
        return this.labelConfig[y][x];
    }

    private void putLabel(int x, int y, Label l) {
        this.labelConfig[y][x] = l;
    }

    // AUXILIARY CLASSES
    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
    private class Transform { double srcX, srcY, dstX, dstY; }

    /**
     * Initialize the lateral ListView that shows the CellType to add to the MapConfig
     */
    private void initAddElementList() {
        List<Label> list = new ArrayList<>();
        for(int i = 0; i < CellType.values().length; i++) {
            CellType ct = CellType.values()[i];
            list.add(buildAddElementLabel(ct, i));
        }
        this.listViewCells.setItems(FXCollections.observableList(list));
    }

    private Label buildAddElementLabel(CellType ct, int index) {
        Label l = new Label(ct.code);
        l.setAlignment(Pos.CENTER);
        l.setFont(Font.font("System", FontWeight.BOLD, 30.0));
        l.setPrefSize(ELEMENT_SIZE, ELEMENT_SIZE);
        l.setStyle("-fx-background-color: " + ct.getRGBAstring());

        Tooltip t = new Tooltip("Add " + ct.name() + " cell");
        t.setFont(Font.font("System", FontWeight.NORMAL, 14.0));
        l.setTooltip(t);

        final DropShadow dropShadow = new DropShadow();
        final Glow glow = new Glow();
        final Delta dragDelta = new Delta();
        final Transform move = new Transform();
        l.setOnMouseEntered(mouseEvent -> {
            if(this.mode == MODE_ADD) {
                l.setCursor(Cursor.HAND);
                dropShadow.setInput(glow);
            }
        });
        l.setOnMouseExited(mouseEvent -> {
            if(this.mode == MODE_ADD) {
                l.setCursor(Cursor.DEFAULT);
                dropShadow.setInput(null);
            }
        });
        l.setOnMousePressed(mouseEvent -> {
            if(this.mode == MODE_ADD) {
                // get initial position
                Bounds bounds = l.localToScene(l.getBoundsInLocal());
                move.srcX = bounds.getCenterX();
                move.srcY = bounds.getCenterY();

                // select
                l.setEffect(dropShadow);
                l.setCursor(Cursor.CLOSED_HAND);

                // record a delta distance for the drag and drop operation.
                dragDelta.x = bounds.getCenterX() - ELEMENT_SIZE / 2 - mouseEvent.getSceneX();
                dragDelta.y = bounds.getCenterY() - ELEMENT_SIZE / 2 - mouseEvent.getSceneY();

                // deselect others
                this.listViewCells.getItems().remove(l);
                this.paneMapConfig.getChildren().add(l);

                l.setLayoutX(bounds.getCenterX() - l.getLayoutX() - this.paneMapConfig.getLayoutX());
                l.setLayoutY(bounds.getCenterY() - l.getLayoutY() - this.paneMapConfig.getLayoutY());

                this.listViewCells.getItems().add(index, buildAddElementLabel(ct, index));
            }
        });
        l.setOnMouseReleased(mouseEvent -> {
            if (this.mode == MODE_ADD) {
                l.setEffect(null);
                l.setCursor(Cursor.DEFAULT);

                double centerX = l.getLayoutX() + ELEMENT_SIZE / 2;
                double centerY = l.getLayoutY() + ELEMENT_SIZE / 2;

                // if not allowed -> delete
                if (centerX < 0.0 || centerX > ELEMENT_SIZE * this.dimX ||
                        centerY < 0.0 || centerY > ELEMENT_SIZE * this.dimY) {
                    l.setCursor(Cursor.DISAPPEAR);

                    this.paneMapConfig.getChildren().remove(l);
                } else {
                    // snap to cell
                    // get cursor position, not element!!!
                    l.setLayoutX(centerX - (centerX % ELEMENT_SIZE));
                    l.setLayoutY(centerY - (centerY % ELEMENT_SIZE));

                    // remove the moved element
                    this.paneMapConfig.getChildren().remove(l);

                    // Execute add
                    this.add(ct, l.getLayoutX(), l.getLayoutY());
                }
            }
        });
        l.setOnMouseDragged(mouseEvent -> {
            if(this.mode == MODE_ADD) {
                l.setLayoutX(mouseEvent.getSceneX() + dragDelta.x - this.paneMapConfig.getLayoutX());
                l.setLayoutY(mouseEvent.getSceneY() + dragDelta.y - this.paneMapConfig.getLayoutY());
                if (l.getLayoutX() + ELEMENT_SIZE / 2 < 0.0 || l.getLayoutX() + ELEMENT_SIZE / 2 > ELEMENT_SIZE * this.dimX ||
                        l.getLayoutY() + ELEMENT_SIZE / 2 < 0.0 || l.getLayoutY() + ELEMENT_SIZE / 2 > ELEMENT_SIZE * this.dimY) {
                /*Image img = new Image(DragTest.class.getResource("cursors/pen_il.cur").toExternalForm());
                l.setCursor(new ImageCursor(img));*/
                    l.setCursor(Cursor.NONE);
                } else {
                    l.setCursor(Cursor.CLOSED_HAND);
                }
            }
        });

        return l;
    }

    // =================================================================================================================
    // CONTROLS ========================================================================================================
    // =================================================================================================================

    @FXML
    public void newMap(ActionEvent event) {
        // check if there are changes and the map config is not null
        if(this.mapConfig != null && this.settings.isConfirmations()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
            alert.setTitle("New Map Confirmation");
            alert.setHeaderText("Create a new Map Configuration?");
            alert.setContentText("Creating a new map will overwrite the current state of the map.\n" +
                    "Continue anyway?");
            alert.getDialogPane().getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                            (this.checkBoxDarkTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES) {
                return;
            }
        }

        // reset MapConfig
        this.mapConfig = new MapConfig(this.dimX, this.dimY);

        // reload paneMapConfig
        this.initMapConfig();
        this.initMapConfigOperationExecutor();
        this.initCoordinates(Color.valueOf(this.settings.getCoordinateColor()));

        // reset current MapConfig file
        this.textFieldMapConfigFile.setText("");
        this.settings.setMapConfigFile("");
        this.settings.saveSettings(SETTINGS_FILENAME);

        this.adjustLayout();

        this.vboxDisplayControls.setVisible(true);
        this.buttonSave.setDisable(false);
        this.buttonSaveAs.setDisable(false);
        this.hboxActionControls.setDisable(false);
        this.hboxAction.setDisable(false);
    }

    @FXML
    public void loadMap(ActionEvent event) {
        // check if there are changes
        if(!this.settings.getMapConfigFile().isEmpty() && this.mapConfig != null
                && this.mapConfigOperationExecutor.checkForChanges() && this.settings.isConfirmations()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Loading Confirmation");
            alert.setHeaderText("You have changes in progress.");
            alert.setContentText("Loading the map will overwrite the current state of the map.\n" +
                    "Continue anyway?");
            alert.getDialogPane().getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                            (this.checkBoxDarkTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES) {
                return;
            }
        }

        File f = new File(this.settings.getMapConfigFile());
        if(f.exists()) {
            this.loadMapConfig(this.settings.getMapConfigFile());
        }
        else {
            this.fileChooserMapConfig.setTitle("Load MapConfig file (it.unibo.map_editor_bcr.model.map_config.MapConfig)");

            File file = this.fileChooserMapConfig.showOpenDialog(null);
            if(file == null) {
                return;
            }
            this.loadMapConfig(file.getAbsolutePath());
        }

        // reload paneMapConfig
        if(this.mapConfig != null) {
            this.initMapConfig();
            this.initMapConfigOperationExecutor();
            this.initCoordinates(Color.valueOf(this.settings.getCoordinateColor()));
        } else {
            Alert alert = new Alert(AlertType.WARNING, "", ButtonType.OK);
            alert.setTitle("Loading Warning");
            alert.setHeaderText("Loading Failed.");
            alert.setContentText("The file is invalid.");
            alert.getDialogPane().getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                            (this.checkBoxDarkTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

            alert.showAndWait();
        }

        this.adjustLayout();
    }

    // TO-DO
    @FXML
    public void saveMap(ActionEvent event) {
        if(this.settings.isConfirmations()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Saving Confirmation");
            alert.setHeaderText("Do you want to save the file?");
            alert.setContentText("Saving the map will overwrite the content of the file.\n" +
                    "Continue anyway?");
            alert.getDialogPane().getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                            (this.checkBoxDarkTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES) {
                return;
            }
        }

        String filename = this.settings.getMapConfigFile();
        if(filename.isEmpty()) {
            this.fileChooserMapConfig.setTitle("Save MapConfig binary file (it.unibo.map_editor_bcr.model.map_config.MapConfig)");

            File file = this.fileChooserMapConfig.showSaveDialog(null);
            if(file == null) {
                return;
            }

            filename = file.getAbsolutePath();
        }

        this.saveMapConfig(filename);
    }
    @FXML
    public void saveMapAs(ActionEvent event) {
        this.fileChooserMapConfig.setTitle("Save as MapConfig binary file (it.unibo.map_editor_bcr.model.map_config.MapConfig)");

        File file = this.fileChooserMapConfig.showSaveDialog(null);
        if(file == null) {
            return;
        }
        // update MapConfig file
        this.textFieldMapConfigFile.setText(file.getAbsolutePath());
        this.settings.setMapConfigFile(file.getAbsolutePath());
        this.settings.saveSettings(SETTINGS_FILENAME);

        this.saveMapConfig(file.getAbsolutePath());
    }
    @FXML
    public void printMapToFile(ActionEvent event) {
        if(this.mapConfig != null) {
            // TO-DO
            String filename = this.settings.getMapConfigFile();
            if (filename.isEmpty()) {
                /*this.fileChooserMapConfig.setTitle("Save MapConfig as text representation (it.unibo.map_editor_bcr.model.map_config.MapConfig)");

                File file = this.fileChooserMapConfig.showSaveDialog(null);
                if(file == null) {
                    return;
                }*/

                // filename = file.getAbsolutePath();
            }
            MapLoader.saveMapConfigRepresentation(this.mapConfig,
                    filename.replace(".bin", ".txt"));
        }
    }

    @FXML
    public void selectAdd(ActionEvent event) {
        this.mode = this.buttonAdd.isSelected() ? MODE_ADD : MODE_NONE;
        this.logger.logMessage((this.buttonAdd.isSelected() ? "S" : "Des") + "elected Add mode.");

        // open lateral list
        this.vboxAddTab.setVisible(this.buttonAdd.isSelected());
    }
    @FXML
    public void selectRemove(ActionEvent event) {
        this.mode = this.buttonRemove.isSelected() ? MODE_REMOVE : MODE_NONE;
        this.logger.logMessage((this.buttonRemove.isSelected() ? "S" : "Des") + "elected Remove mode.");

        this.vboxAddTab.setVisible(this.buttonAdd.isSelected());
    }
    @FXML public void selectMove(ActionEvent event) {
        this.mode = this.buttonMove.isSelected() ? MODE_MOVE : MODE_NONE;
        this.logger.logMessage((this.buttonMove.isSelected() ? "S" : "Des") + "elected Move mode.");

        this.vboxAddTab.setVisible(this.buttonAdd.isSelected());
    }
    @FXML public void selectSwap(ActionEvent event) {
        this.mode = this.buttonSwap.isSelected() ? MODE_SWAP : MODE_NONE;
        this.logger.logMessage((this.buttonSwap.isSelected() ? "S" : "Des") + "elected Swap mode.");

        this.vboxAddTab.setVisible(this.buttonAdd.isSelected());
    }

    private void add(CellType ct, double posX, double posY) {
        // Create a new label and add it to the Pane
        Label newL = buildMapConfigElement(ct, posX, posY);
        this.paneMapConfig.getChildren().add(newL);

        // get indexes for the label map
        final int x = (int) (posX / ELEMENT_SIZE);
        final int y = (int) (posY / ELEMENT_SIZE);

        // remove the old label from the Pane
        this.paneMapConfig.getChildren().remove(this.getLabel(x, y));

        // add the new label
        this.putLabel(x, y, newL);

        // Execute the MapOperation
        String action = this.mapConfigOperationExecutor.executeOperation(
                new AddMapConfigOperation(this.mapConfig, x, y, ct));
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());

        // Update history
        this.comboBoxHistory.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxHistory.setValue(action);

        // print
        this.logger.logMessage(action);
        this.logger.logMessage(this.mapConfig.toFancyString());
    }

    private void remove(double posX, double posY) {
        // Create a new label and add it to the Pane
        Label newL = buildMapConfigElement(CellType.NONE, posX, posY);
        this.paneMapConfig.getChildren().add(newL);

        // get indexes for the label map
        final int x = (int) (posX / ELEMENT_SIZE);
        final int y = (int) (posY / ELEMENT_SIZE);

        // remove the old label from the Pane
        this.paneMapConfig.getChildren().remove(this.getLabel(x, y));

        // add the new label
        this.putLabel(x, y, newL);

        // Execute the MapOperation
        String action = this.mapConfigOperationExecutor.executeOperation(
                new RemoveMapConfigOperation(this.mapConfig, x, y));
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());

        // Update history
        this.comboBoxHistory.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxHistory.setValue(action);

        // print
        this.logger.logMessage(action);
        this.logger.logMessage(this.mapConfig.toFancyString());
    }

    private void move(Label l, double srcX, double srcY, double dstX, double dstY) {
        final int iSrcX = (int) (srcX / ELEMENT_SIZE);
        final int iSrcY = (int) (srcY / ELEMENT_SIZE);
        final int iDstX = (int) (dstX / ELEMENT_SIZE);
        final int iDstY = (int) (dstY / ELEMENT_SIZE);

        Label oldL = this.getLabel(iDstX, iDstY);
        /*oldL.setLayoutX(srcX);
        oldL.setLayoutY(srcY);
        oldL.setText("");
        oldL.setStyle("-fx-background-color: " + CellType.NONE.getRGBAstring());*/
        this.paneMapConfig.getChildren().remove(oldL);

        this.putLabel(iDstX, iDstY, l);
        Label newL = buildMapConfigElement(CellType.NONE, srcX, srcY);
        this.putLabel(iSrcX, iSrcY, newL);
        this.paneMapConfig.getChildren().add(newL);

        // Execute the MapOperation
        String action = this.mapConfigOperationExecutor.executeOperation(
                new MoveMapOperation(this.mapConfig, iSrcX, iSrcY, iDstX, iDstY));
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());

        // Update history
        this.comboBoxHistory.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxHistory.setValue(action);

        // print
        this.logger.logMessage(action);
        this.logger.logMessage(this.mapConfig.toFancyString());
    }

    private void swap(Label l, double srcX, double srcY, double dstX, double dstY) {
        final int iSrcX = (int) (srcX / ELEMENT_SIZE);
        final int iSrcY = (int) (srcY / ELEMENT_SIZE);
        final int iDstX = (int) (dstX / ELEMENT_SIZE);
        final int iDstY = (int) (dstY / ELEMENT_SIZE);

        Label oldL = this.getLabel(iDstX, iDstY);
        oldL.setLayoutX(srcX);
        oldL.setLayoutY(srcY);

        this.putLabel(iDstX, iDstY, l);
        this.putLabel(iSrcX, iSrcY, oldL);

        // Execute the MapOperation
        String action = this.mapConfigOperationExecutor.executeOperation(
                new SwapMapConfigOperation(this.mapConfig, iSrcX, iSrcY, iDstX, iDstY));
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());

        // Update history
        this.comboBoxHistory.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxHistory.setValue(action);

        // print
        this.logger.logMessage(action);
        this.logger.logMessage(this.mapConfig.toFancyString());
    }

    @FXML
    public void undo(ActionEvent event) {
        if(this.mapConfigOperationExecutor.canUndo()) {
            String action = this.mapConfigOperationExecutor.undoOperation();
            this.comboBoxHistory.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
            this.comboBoxHistory.setValue(action);

            this.logger.logMessage(action);

            updateLabels();

            this.logger.logMessage(this.mapConfig.toFancyString());
        }
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());
    }
    @FXML
    public void redo(ActionEvent event) {
        if(this.mapConfigOperationExecutor.canRedo()) {
            String action = this.mapConfigOperationExecutor.redoOperation();
            this.comboBoxHistory.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
            this.comboBoxHistory.setValue(action);

            this.logger.logMessage(action);

            updateLabels();

            this.logger.logMessage(this.mapConfig.toFancyString());
        }
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());
        // deselect mode?
    }

    @FXML
    public void openSettings(ActionEvent event) {
        this.logger.logMessage("Opened settings.");
        this.anchorPaneBase.setEffect(new GaussianBlur(10.0));
        this.anchorPaneBase.setDisable(true);
        this.vboxSettings.setVisible(true);
    }

    @FXML
    public void toggleDarkTheme(ActionEvent event) {
        this.changeTheme(checkBoxDarkTheme.isSelected());
    }

    @FXML
    public void toggleLogFile(ActionEvent event) {
        this.hboxLogDirPath.setDisable(!this.checkboxLogFile.isSelected());
    }

    @FXML
    public void browseLogDirectory(ActionEvent event) {
        this.directoryChooserLog.setTitle("Select the Log directory");
        File file = this.directoryChooserLog.showDialog(null);

        if (file != null) {
            this.textFieldLogDirPath.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void browseRoomMap(ActionEvent event) {
        this.fileChooserRoomMap.setTitle("Select the RoomMap file to be loaded by default");
        File file = this.fileChooserRoomMap.showOpenDialog(null);
        if(file != null) {
            this.textFieldRoomMapFile.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void saveSettings(ActionEvent event) {
        if (!this.textFieldRoomMapFile.getText().equals(this.settings.getRoomMapFile())) {
            this.loadRoomMap(textFieldRoomMapFile.getText());
        }

        // update settings
        this.settings.setRoomMapFile(this.textFieldRoomMapFile.getText());
        this.settings.setMapConfigFile(this.textFieldMapConfigFile.getText());
        this.settings.setLogLevel(this.checkboxLogStdOut.isSelected(),
                this.checkboxLogFile.isSelected(), this.textFieldLogDirPath.getText());
        this.settings.setConfirmations(this.checkBoxConfirmations.isSelected());
        this.settings.setDarkTheme(this.checkBoxDarkTheme.isSelected());
        this.settings.setCoordinateColor(this.colorPickerCoordinates.getValue().toString());

        // update GUI
        this.initCoordinates(Color.valueOf(this.settings.getCoordinateColor()));

        // update Logger
        logger.setLogLevel(this.settings.getLogLevel());

        this.settings.saveSettings(SETTINGS_FILENAME);
        this.logger.logMessage("Settings changes have been saved.");

        this.anchorPaneBase.setEffect(null);
        this.anchorPaneBase.setDisable(false);
        this.vboxSettings.setVisible(false);
    }
    @FXML
    public void cancelSettings(ActionEvent event) {
        // Restore GUI elements
        this.changeTheme(this.settings.isDarkTheme());
        this.initCoordinates(Color.valueOf(this.settings.getCoordinateColor()));

        // update settings
        this.updateGUIfromSettings();

        this.logger.logMessage("Settings changes have been discarded.");
        this.anchorPaneBase.setEffect(null);
        this.anchorPaneBase.setDisable(false);
        this.vboxSettings.setVisible(false);
    }
    @FXML
    public void resetSettings(ActionEvent event) {
        this.checkBoxDarkTheme.setSelected(Constants.DEFAULT_DARK_THEME);
        this.changeTheme(Constants.DEFAULT_DARK_THEME);
        this.checkboxLogStdOut.setSelected(Constants.DEFAULT_LOG_LEVEL_TO_STD_OUT);
        this.checkboxLogFile.setSelected(Constants.DEFAULT_LOG_LEVEL_TO_FILE);
        this.textFieldLogDirPath.setText(Constants.DEFAULT_LOG_LEVEL_DIRECTORY);
        this.textFieldRoomMapFile.setText(Constants.DEFAULT_ROOM_MAP_FILE);
        this.textFieldMapConfigFile.setText(Constants.DEFAULT_MAP_CONFIG_FILE);
        this.checkBoxConfirmations.setSelected(Constants.DEFAULT_CONFIRM_BEFORE_SAVING);
        this.colorPickerCoordinates.setValue(Color.valueOf(Constants.DEFAULT_COORDINATE_COLOR));

        this.logger.logMessage("Settings reset to default.");
    }

    @FXML
    public void openCloseEditTab(ActionEvent event) {
        this.vboxAddTab.setVisible(false);
    }

    private void changeTheme(boolean dark) {
        Scene scene = this.buttonNew.getScene();
        ObservableList<String> sheets = scene.getStylesheets();

        for(int i = 0; i < sheets.size(); i++)
        {
            if(sheets.get(i).contains("theme")) {
                sheets.remove(i);
                i--;
            }
        }
        sheets.add(ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                (dark ? "dark" : "light") + ".css").toExternalForm());
    }

    private void updateLabels() {
        for(int y = 0; y < this.dimY; y++) {
            for(int x = 0; x < this.dimX; x++) {
                if(!this.mapConfig.get(x, y).equalsCode(this.getLabel(x, y).getText())) {
                    this.changeLabel(x, y, this.mapConfig.get(x, y));
                }
            }
        }
    }

    private void changeLabel(int x, int y, CellType cell) {
        Label lToBeChanged = this.getLabel(x, y);
        Label lChanged = buildMapConfigElement(cell, lToBeChanged.getLayoutX(), lToBeChanged.getLayoutY());
        this.paneMapConfig.getChildren().remove(lToBeChanged);
        this.putLabel(x, y, lChanged);
        this.paneMapConfig.getChildren().add(lChanged);
    }

    private void printLabelMap() {
        for(int y = 0; y < this.dimY; y++) {
            for(int x = 0; x < this.dimX;x++) {
                if(!this.getLabel(x, y).getText().isEmpty()) {
                    System.out.print(this.getLabel(x, y).getText());
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }

    private void updateGUIfromSettings() {
        this.textFieldRoomMapFile.setText(this.settings.getRoomMapFile());
        this.textFieldMapConfigFile.setText(this.settings.getMapConfigFile());
        this.checkboxLogStdOut.setSelected(this.settings.getLogLevel().toStandardOutput);
        this.checkboxLogFile.setSelected(this.settings.getLogLevel().toFile);
        this.hboxLogDirPath.setDisable(!this.settings.getLogLevel().toFile);
        this.textFieldLogDirPath.setText(this.settings.getLogLevel().directory);
        this.checkBoxConfirmations.setSelected(this.settings.isConfirmations());
        this.checkBoxDarkTheme.setSelected(this.settings.isDarkTheme());
        this.colorPickerCoordinates.setValue(Color.valueOf(this.settings.getCoordinateColor()));
    }

    @FXML
    public void toggleShowRoomMap(ActionEvent event) {
        this.paneRoomMap.setVisible(this.checkBoxRoomMap.isSelected());
    }
    @FXML
    public void toggleShowMapConfig(ActionEvent event) {
        this.paneMapConfig.setVisible(this.checkBoxMapConfig.isSelected());
    }
    @FXML
    public void toggleShowCoordinates(ActionEvent event) {
        this.paneCoordinates.setVisible(this.checkBoxCoordinates.isSelected());
    }
}
