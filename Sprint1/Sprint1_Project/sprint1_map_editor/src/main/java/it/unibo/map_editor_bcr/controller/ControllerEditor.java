package it.unibo.map_editor_bcr.controller;

import it.unibo.map_editor_bcr.model.MapLoader;
import it.unibo.map_editor_bcr.model.commands.*;
import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;
import it.unibo.map_editor_bcr.model.room_map.RoomMapParser;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import unibo.planner22.model.RoomMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TO-DO: invertire rows/cols
 */
public class ControllerEditor {
    private final double ELEMENT_SIZE = 50.0;
    private final int MODE_NONE = -1;
    private final int MODE_ADD = 0;
    private final int MODE_REMOVE = 1;
    private final int MODE_MOVE = 2;
    private final int MODE_SWAP = 3;


    // FXML components
    @FXML private AnchorPane anchorPaneRoot;

    @FXML private AnchorPane anchorPaneBase;
    @FXML private ComboBox<String> comboBoxAction;

    @FXML private VBox vboxDisplayControls;
    @FXML private VBox vboxCommandControls;
    @FXML private Button buttonNew;
    @FXML private Button buttonLoad;
    @FXML private Button buttonSave;
    private FileChooser fileChooser;

    @FXML private ToggleGroup toggleGroupMode;
    @FXML private ToggleButton buttonAdd;
    @FXML private ToggleButton buttonRemove;
    @FXML private ToggleButton buttonMove;
    @FXML private ToggleButton buttonSwap;

    @FXML private Button buttonUndo;
    @FXML private Button buttonRedo;
    @FXML private Button buttonSettings;

    @FXML private Button buttonOpenCloseEditTab;
    @FXML private VBox vboxEditTab;
    @FXML private ListView<Label> listViewCells;

    @FXML private VBox vboxSettings;

    @FXML private CheckBox checkBoxRoomMap;
    @FXML private CheckBox checkBoxMapConfig;
    @FXML private CheckBox checkBoxCoordinates;

    @FXML private CheckBox checkBoxTheme;
    @FXML private ColorPicker colorPickerCoordinates;

    private RoomMapParser roomMapParser;
    private MapConfig mapConfig;
    private int dimX, dimY;

    private MapConfigOperationExecutor mapConfigOperationExecutor;

    private Pane paneRoomMap;
    private Pane paneCoordinates;
    private Pane paneMapConfig;

    private Label[][] labelConfig;

    private int mode = -1;

    public ControllerEditor(/*Map map*/) {
        String filename = "mapRoomEmpty";
        RoomMap roomMap = MapLoader.loadRoomMap(filename);
        this.roomMapParser = new RoomMapParser(roomMap);
        this.dimX = this.roomMapParser.getDimX();
        this.dimY = this.roomMapParser.getDimY();
        System.out.println("RoomMap loaded from '" + filename + "': " + this.dimX + " x " + this.dimY);
        System.out.println(roomMap.toString());

        // Make empty
        this.mapConfig = new MapConfig(dimX, dimY);
        filename = "mapConfigWasteService";
        this.mapConfig = MapLoader.loadMapConfig(filename);


        /*this.map = MapLoader.loadMap(filename);
        this.rows = map.getColumnsSize();
        this.columns = map.getRowsSize();

        System.out.println("Load Map from '" + filename + "': " + this.rows + " x " + this.columns);
        System.out.println(map.toFancyString()); // Test
         */
    }

    public void initialize() {
        // Setup and Add File Chooser
        //FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("BIN files")

        this.initRoomMap();
        this.initCoordinates(Color.BLACK);
        this.initMapConfig();
        this.initMapConfigOperationExecutor();
        this.initAddElementList();

        this.anchorPaneBase.getChildren().remove(this.vboxDisplayControls);
        this.anchorPaneBase.getChildren().add(this.vboxDisplayControls);
        this.anchorPaneBase.getChildren().remove(this.vboxCommandControls);
        this.anchorPaneBase.getChildren().add(this.vboxCommandControls);


        // Set visibility
        this.paneCoordinates.setVisible(false);
        this.vboxSettings.setVisible(false);

        Platform.runLater(() -> {
            this.adjustLayout();

            Stage stage = (Stage) this.vboxCommandControls.getScene().getWindow();
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

    /**
     * Adjust element positions according to window size.
     * To be called when the window gets resized.
     */
    private void adjustLayout() {
        this.vboxCommandControls.setLayoutX((this.anchorPaneBase.getWidth() - this.vboxCommandControls.getWidth()) / 2);
        this.paneRoomMap.setLayoutX((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2);
        this.paneRoomMap.setLayoutY((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4);
        this.paneCoordinates.setLayoutX((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2);
        this.paneCoordinates.setLayoutY((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4);
        this.paneMapConfig.setLayoutX(((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2));
        this.paneMapConfig.setLayoutY(((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4));

        // adjust listview
    }

    private void initMapConfigOperationExecutor() {
        this.mapConfigOperationExecutor = new MapConfigOperationExecutor();

        this.comboBoxAction.setValue("");
        this.comboBoxAction.getItems().clear();
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
        this.anchorPaneBase.getChildren().add(this.paneRoomMap);

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

        this.anchorPaneBase.getChildren().add(this.paneCoordinates);

        this.paneCoordinates.setLayoutX((this.anchorPaneBase.getWidth() - ELEMENT_SIZE * dimX) / 2);
        this.paneCoordinates.setLayoutY((this.anchorPaneBase.getHeight() - ELEMENT_SIZE * dimY) / 4);
        this.paneCoordinates.setVisible(true);
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
        this.anchorPaneBase.getChildren().add(this.paneMapConfig);

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
                            System.out.print("Unkown operation: " + mode);
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
                System.out.println("X: " + (int) (centerX / ELEMENT_SIZE) + ", Y: " + (int) (centerY / ELEMENT_SIZE));

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

    @FXML
    public void newMap(ActionEvent event) {
        // check if there are changes
        if(this.mapConfigOperationExecutor.checkForChanges()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Dialog window");
            alert.setHeaderText("You have changes in progress.");
            alert.setContentText("Creating a new map will overwrite the current state of the map.\n" +
                    "Continue anyway?");
            alert.getDialogPane().getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                            (this.checkBoxTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

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
    }

    @FXML
    public void loadMap(ActionEvent event) {
        // check if there are changes
        if(this.mapConfigOperationExecutor.checkForChanges()) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Dialog window");
            alert.setHeaderText("You have changes in progress.");
            alert.setContentText("Loading the map from file will overwrite the current state of the map.\n" +
                    "Continue anyway?");
            alert.getDialogPane().getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                            (this.checkBoxTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

            alert.showAndWait();
            if (alert.getResult() != ButtonType.YES) {
                return;
            }
        }

        String filename = "mapConfig";
        this.mapConfig = MapLoader.loadMapConfig(filename);
        if(this.mapConfig.getDimX() != this.dimX || this.mapConfig.getDimY() != this.dimY) {
            System.out.println("The MapConfig '" + filename + "' must have the same size of MapRoom. " +
                    "Expected: (" + dimX + ", " + dimY + "), " +
                    "found: (" + this.mapConfig.getDimX() + ", " + this.mapConfig.getDimY() + ")");
            this.mapConfig = new MapConfig(dimX, dimY);
        }

        // reload paneMapConfig
        this.initMapConfig();
        this.initMapConfigOperationExecutor();
    }

    // TO-DO
    @FXML
    public void saveMap(ActionEvent event) {
        String filename = "mapConfigWasteService";
        String suffix = ".bin";

        // TO-DO: check if there already is a file and ask if we want to overwrite it
        ButtonType bt = new ButtonType("Change name");
        Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, bt);
        alert.setTitle("Dialog Window");
        alert.setHeaderText("The file '" + filename + ".bin' already exists.");
        alert.setContentText("Do you want to overwrite it?");
        alert.getDialogPane().getStylesheets().add(
                ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                        (this.checkBoxTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            MapLoader.saveMapConfig(this.mapConfig, filename);
            System.out.println("Map saved to file '" + filename + "'");
        }
        else if (alert.getResult() == bt) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Text Input Dialog");
            dialog.setHeaderText("Enter the name of the file to save the map into.");
            dialog.setContentText("File name (.bin): ");

            TextField tf = dialog.getEditor();
            tf.getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                            (this.checkBoxTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());
            // TO-DO: validate filename
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                filename = result.get();
                if (filename.endsWith(".bin"))
                    filename.replace(".bin", "");
                MapLoader.saveMapConfig(this.mapConfig, result.get());
                System.out.println("MapConfig saved to file '" + result.get() + "'");
            }
        }
    }

    @FXML
    public void selectAdd(ActionEvent event) {
        this.mode = this.buttonAdd.isSelected() ? MODE_ADD : MODE_NONE;
        System.out.println((this.buttonAdd.isSelected() ? "S" : "Des") + "elected Add mode.");

        // open lateral list
        this.vboxEditTab.setVisible(this.buttonAdd.isSelected());
    }
    @FXML
    public void selectRemove(ActionEvent event) {
        this.mode = this.buttonRemove.isSelected() ? MODE_REMOVE : MODE_NONE;
        System.out.println((this.buttonRemove.isSelected() ? "S" : "Des") + "elected Remove mode.");

        this.vboxEditTab.setVisible(this.buttonAdd.isSelected());
    }
    @FXML public void selectMove(ActionEvent event) {
        this.mode = this.buttonMove.isSelected() ? MODE_MOVE : MODE_NONE;
        System.out.println((this.buttonMove.isSelected() ? "S" : "Des") + "elected Move mode.");

        this.vboxEditTab.setVisible(this.buttonAdd.isSelected());
    }
    @FXML public void selectSwap(ActionEvent event) {
        this.mode = this.buttonSwap.isSelected() ? MODE_SWAP : MODE_NONE;
        System.out.println((this.buttonSwap.isSelected() ? "S" : "Des") + "elected Swap mode.");

        this.vboxEditTab.setVisible(this.buttonAdd.isSelected());
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
        this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxAction.setValue(action);

        // print
        System.out.println(action);
        System.out.println(this.mapConfig.toFancyString());
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
        this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxAction.setValue(action);

        // print
        System.out.println(action);
        System.out.println(this.mapConfig.toFancyString());
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

        //this.printLabelMap(); //test

        // Execute the MapOperation
        String action = this.mapConfigOperationExecutor.executeOperation(
                new MoveMapOperation(this.mapConfig, iSrcX, iSrcY, iDstX, iDstY));
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());

        // Update history
        this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxAction.setValue(action);

        // print
        System.out.println(action);
        System.out.println(this.mapConfig.toFancyString());
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
        this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
        this.comboBoxAction.setValue(action);

        // print
        System.out.println(action);
        System.out.println(this.mapConfig.toFancyString());
    }

    @FXML
    public void move(ActionEvent event) {
        /*if(this.operation == OP_MOVE) {
            this.operation = OP_NONE;
            // highligh button border

        } else {
            this.operation = OP_MOVE;
        }*/
    }
    @FXML
    public void swap(ActionEvent event) {
        System.out.println("Swap");
    }

    @FXML
    public void undo(ActionEvent event) {
        if(this.mapConfigOperationExecutor.canUndo()) {
            String action = this.mapConfigOperationExecutor.undoOperation();
            this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
            this.comboBoxAction.setValue(action);

            System.out.println(action);

            updateLabels();

            System.out.println(this.mapConfig.toFancyString()); // log
        }
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());
        // deselect mode?

        //this.printLabelMap(); // test
    }
    @FXML
    public void redo(ActionEvent event) {
        if(this.mapConfigOperationExecutor.canRedo()) {
            String action = this.mapConfigOperationExecutor.redoOperation();
            this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
            this.comboBoxAction.setValue(action);

            System.out.println(action);

            updateLabels();

            System.out.println(this.mapConfig.toFancyString());
        }
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());
        // deselect mode?
    }
    @FXML
    public void displaySettings(ActionEvent event) {
        System.out.println("Opened settings");
        this.anchorPaneBase.setEffect(new GaussianBlur(10.0));
        this.vboxSettings.setVisible(true);
    }

    @FXML
    public void setCoordinatesColor(ActionEvent event) {
        initCoordinates(this.colorPickerCoordinates.getValue());
    }

    @FXML
    public void cancelSettings(ActionEvent event) {
        System.out.println("Discarded settings changes");
        this.anchorPaneBase.setEffect(null);
        this.vboxSettings.setVisible(false);
    }

    @FXML
    public void openCloseEditTab(ActionEvent event) {
        this.vboxEditTab.setVisible(false);
    }

    @FXML
    public void toggleDarkTheme(ActionEvent event) {
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
                (checkBoxTheme.isSelected() ? "dark" : "light") + ".css").toExternalForm());
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
