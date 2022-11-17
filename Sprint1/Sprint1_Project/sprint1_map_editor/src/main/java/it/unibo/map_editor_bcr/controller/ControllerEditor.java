package it.unibo.map_editor_bcr.controller;

import it.unibo.map_editor_bcr.MapEditor;
import it.unibo.map_editor_bcr.model.MapLoader;
import it.unibo.map_editor_bcr.model.commands.*;
import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;
import it.unibo.map_editor_bcr.model.room_map.RoomMapParser;
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
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
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
    @FXML private ComboBox<String> comboBoxAction;

    //@FXML private TilePane tilePaneMap;

    @FXML private CheckBox checkBoxTheme;

    @FXML private HBox hboxCommands;
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

    private RoomMapParser roomMapParser;
    private MapConfig mapConfig;
    private int dimX, dimY;


    private MapConfigOperationExecutor mapConfigOperationExecutor;

    private Pane paneRoomMap;
    private Pane paneMapConfig;

    private Label[][] labelConfig;
    private int rows, columns;

    private int mode = -1;

    private int iEdit = -1;

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

        mapConfigOperationExecutor = new MapConfigOperationExecutor();
    }

    public void initialize() {
        // Setup and Add File Chooser
        //FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("BIN files")

        this.initRoomMap();
        this.initMapConfig();

        this.initAddElementList();

        this.comboBoxAction.getItems();

        this.buttonUndo.setDisable(true);
        this.buttonRedo.setDisable(true);

        Platform.runLater(() -> {
            this.adjustLayout();

            Stage stage = (Stage) this.hboxCommands.getScene().getWindow();
            stage.widthProperty().addListener((obs, oldVal, newVal) -> {
                this.adjustLayout();
            });

            stage.heightProperty().addListener((obs, oldVal, newVal) -> {
                this.adjustLayout();
            });
        });

        // TO-DO: fix move and swap (exec/undo/redo)
        this.buttonMove.setDisable(true);
        this.buttonSwap.setDisable(true);
    }

    /**
     * Adjust element positions according to window size.
     * To be called when the window gets resized.
     */
    private void adjustLayout() {
        this.hboxCommands.setLayoutX((MapEditor.WINDOW_WIDTH - this.hboxCommands.getWidth()) / 2);
        this.paneRoomMap.setLayoutX((MapEditor.WINDOW_WIDTH - ELEMENT_SIZE * dimX) / 2);
        this.paneRoomMap.setLayoutY((MapEditor.WINDOW_HEIGHT - ELEMENT_SIZE * dimY) / 4);
        this.paneMapConfig.setLayoutX(((MapEditor.WINDOW_WIDTH - ELEMENT_SIZE * dimX) / 2) + 1.0);
        this.paneMapConfig.setLayoutY(((MapEditor.WINDOW_HEIGHT - ELEMENT_SIZE * dimY) / 4) + 1.0);

        // adjust listview
    }

    /**
     * Initialize the base map: RoomMap (the map representation we got from the mapper).
     */
    private void initRoomMap() {
        this.anchorPaneRoot.getChildren().remove(this.paneRoomMap);

        this.paneRoomMap = new Pane();
        this.paneRoomMap.setPrefSize(ELEMENT_SIZE * dimX + 2, ELEMENT_SIZE * dimY + 2);
        this.paneRoomMap.setSnapToPixel(false);
        this.paneRoomMap.setStyle("-fx-border-color: black");
        this.paneRoomMap.getChildren().clear();

        for(int y = 0; y < this.dimY; y++) {
            for (int x = 0; x < this.dimX; x++) {
                it.unibo.map_editor_bcr.model.room_map.CellType ct = this.roomMapParser.getCellType(x, y);
                this.paneRoomMap.getChildren().add(
                        this.buildRoomMapElement(ct, (ELEMENT_SIZE * x) + 1.0, (ELEMENT_SIZE * y) + 1.0, 0.1));
            }
        }
        this.anchorPaneRoot.getChildren().add(this.paneRoomMap);
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
    private void initMapConfig() {
        this.anchorPaneRoot.getChildren().remove(this.paneMapConfig);

        this.labelConfig = new Label[dimY][dimX];
        this.paneMapConfig = new Pane();
        this.paneMapConfig.setPrefSize(ELEMENT_SIZE * rows, ELEMENT_SIZE * columns);
        this.paneMapConfig.setSnapToPixel(false);
        this.paneMapConfig.getChildren().clear();

        for(int y = 0; y < dimY; y++) {
            for(int x = 0; x < dimX; x++) {
                Label l = buildMapConfigElement(this.mapConfig.get(x, y), x * ELEMENT_SIZE, y * ELEMENT_SIZE);
                this.paneMapConfig.getChildren().add(l);
                this.putLabel(x, y, l);
            }
        }
        this.anchorPaneRoot.getChildren().add(this.paneMapConfig);
    }

    private Label buildMapConfigElement(CellType ct, double x, double y) {
        Label l = new Label(ct.equals(CellType.NONE) ? "" : ct.code);
        l.setLayoutX(x);
        l.setLayoutY(y);
        l.setAlignment(Pos.CENTER);
        l.setFont(Font.font("System", FontWeight.BOLD, 30.0));
        l.setPrefSize(50.0, 50.0);
        l.setStyle("-fx-background-color: " + ct.getRGBAstring());
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
                    l.setLayoutX(x);
                    l.setLayoutY(y);
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
        l.setPrefSize(50.0, 50.0);
        l.setStyle("-fx-background-color: " + ct.getRGBAstring());

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
        Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Dialog window");
        alert.setHeaderText("You have changes in progress.");
        alert.setContentText("Creating a new map will overwrite the current state of the map.\n" +
                "Continue anyway?");
        alert.getDialogPane().getStylesheets().add(
                ControllerEditor.class.getResource("/it/unibo/sprint1_map_editor/styles/theme-" +
                        (this.checkBoxTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            /*this.map = new Map(this.map.getRowsSize(), this.map.getColumnsSize());
            //this.map.setBorders(CellType.BORDER);
            this.rows = map.getRowsSize();
            this.columns = map.getColumnsSize();
            this.initMapConfig();*/

            // TO-DO: load pane
        }
    }

    @FXML
    public void loadMap(ActionEvent event) {
        // TO-DO: check if there are changes
        /*this.map = MapLoader.loadMap("mapRoomEmpty.txt");
        this.rows = map.getRowsSize();
        this.columns = map.getColumnsSize();*/

        // TO-DO: load pane
        this.initRoomMap();
        this.initMapConfig();
        this.adjustLayout();
    }
    @FXML
    public void saveMap(ActionEvent event) {
        String filename = "mapRoomEmpty.txt";

        // TO-DO: check if there already is a file and ask if we want to overwrite it
        ButtonType bt = new ButtonType("Change name");
        Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, bt);
        alert.setTitle("Dialog Window");
        alert.setHeaderText("The file '" + filename + "' already exists.");
        alert.setContentText("Do you want to overwrite it?");
        alert.getDialogPane().getStylesheets().add(
                ControllerEditor.class.getResource("/it/unibo/sprint1_map_editor/styles/theme-" +
                        (this.checkBoxTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());

        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            // TO-DO: fix
            //MapLoader.saveMap("mapRoomEmpty.txt", this.mapConfig);
            System.out.println("Map saved to file '" + filename + "'");
        }
        else if (alert.getResult() == bt) {
            TextInputDialog dialog = new TextInputDialog(filename);
            dialog.setTitle("Text Input Dialog");
            dialog.setHeaderText("Enter the name of the file to save the map into.");
            dialog.setContentText("File name: ");
            // TO-DO: style?
            TextField tf = dialog.getEditor();
            tf.getStylesheets().add(
                    ControllerEditor.class.getResource("/it/unibo/sprint1_map_editor/styles/theme-" +
                            (this.checkBoxTheme.isSelected() ? "dark" : "light") +".css").toExternalForm());
            // validate filename TO-DO
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                // TO-DO: fix
                //MapLoader.saveMap(result.get(), this.map);
                System.out.println("Map saved to file '" + result.get() + "'");
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
        oldL.setLayoutX(srcX);
        oldL.setLayoutY(srcY);
        oldL.setText("");
        oldL.setStyle("-fx-background-color: " + CellType.NONE.getRGBAstring());

        this.putLabel(iDstX, iDstY, l);
        this.putLabel(iSrcX, iSrcY, buildMapConfigElement(CellType.NONE, srcX, srcY));

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
            System.out.println("Undo last command");

            String action = this.mapConfigOperationExecutor.undoOperation();
            this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
            this.comboBoxAction.setValue(action);

            updateLabels();

            System.out.println(this.mapConfig.toFancyString());
        }
        this.buttonUndo.setDisable(!this.mapConfigOperationExecutor.canUndo());
        this.buttonRedo.setDisable(!this.mapConfigOperationExecutor.canRedo());
        // deselect mode?

        this.printLabelMap();
    }
    @FXML
    public void redo(ActionEvent event) {
        if(this.mapConfigOperationExecutor.canRedo()) {
            System.out.println("Redo last undone command");

            String action = this.mapConfigOperationExecutor.redoOperation();
            this.comboBoxAction.setItems(FXCollections.observableList(this.mapConfigOperationExecutor.getOperationsList()));
            this.comboBoxAction.setValue(action);

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
    }

    @FXML
    public void openCloseEditTab(ActionEvent event) {
        this.vboxEditTab.setVisible(false);
    }

    @FXML
    public void toggleDarkTheme(ActionEvent event) {
        CheckBox cb = (CheckBox) event.getSource();

        Scene scene = this.buttonNew.getScene();
        ObservableList<String> sheets = scene.getStylesheets();

        for(int i = 0; i < sheets.size(); i++)
        {
            if(sheets.get(i).contains("theme")) {
                sheets.remove(i);
                i--;
            }
        }
        sheets.add(ControllerEditor.class.getResource("/it/unibo/sprint1_map_editor/styles/theme-" + (cb.isSelected() ? "dark" : "light") + ".css").toExternalForm());
    }

    private void updateLabels() {
        for(int y = 0; y < this.dimY; y++) {
            for(int x = 0; x < this.dimX; x++) {
                if(!this.mapConfig.get(x, y).code.equals(this.getLabel(x, y))) {
                    this.changeLabel(x, y, this.mapConfig.get(x, y));
                }
            }
        }
        printLabelMap();
    }

    private void changeLabel(int x, int y, CellType cell) {
        Label l = this.getLabel(x, y);
        l.setText(cell.equals(CellType.NONE) ? "" : cell.code);
        //l.setOpacity((cell.equals(CellType.EMPTY) || cell.equals(CellType.BORDER))? 0.2 : 1.0);
        l.setStyle("-fx-background-color: " + cell.getRGBAstring());
    }

    private void printLabelMap() {
        for(int y = 0; y < this.dimY; y++) {
            for(int x = 0; x < this.dimX; x++) {
                if(!this.getLabel(x, y).getText().isEmpty()) {
                    System.out.print(this.getLabel(x, y).getText());
                } else {
                    System.out.print("-");
                }
            }
            System.out.print("\n");
        }
    }
}
