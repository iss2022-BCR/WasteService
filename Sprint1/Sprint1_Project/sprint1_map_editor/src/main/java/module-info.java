module it.unibo.map_editor_bcr {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires unibo.planner22;
    requires com.google.gson;

    opens it.unibo.map_editor_bcr to javafx.fxml;
    opens it.unibo.map_editor_bcr.controller to javafx.fxml;
    opens it.unibo.map_editor_bcr.model.settings to com.google.gson;
    exports it.unibo.map_editor_bcr;
}