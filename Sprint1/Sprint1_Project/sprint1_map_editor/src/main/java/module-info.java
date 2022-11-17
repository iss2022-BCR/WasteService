module it.unibo.map_editor_bcr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires unibo.planner22;

    opens it.unibo.map_editor_bcr to javafx.fxml;
    opens it.unibo.map_editor_bcr.controller to javafx.fxml;
    exports it.unibo.map_editor_bcr;
}