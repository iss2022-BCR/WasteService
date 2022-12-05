package it.unibo.map_editor_bcr;

import it.unibo.map_editor_bcr.controller.ControllerEditor;
import it.unibo.map_editor_bcr.model.Logger;
import it.unibo.map_editor_bcr.model.persistence.SettingsManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MapEditor extends Application {
    public final static double WINDOW_WIDTH = 600.0;
    public final static double WINDOW_HEIGHT = 400.0;

    @Override
    public void start(Stage stage) throws IOException {
        SettingsManager settings = SettingsManager.getInstance();

        FXMLLoader fxmlLoader = new FXMLLoader(MapEditor.class.getResource("views/view-map-editor.fxml"));
        ControllerEditor controller = new ControllerEditor();
        fxmlLoader.setController(controller);
        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(MapEditor.class.getResource("/it/unibo/map_editor_bcr/styles/theme-" +
                (settings.isDarkTheme() ? "dark" : "light") + ".css").toExternalForm());
        stage.setTitle("Map Editor");
        //stage.setResizable(false);

        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void stop(){
        Logger logger = Logger.getInstance();
        logger.closeLogWriter();
    }

    public static void main(String[] args) {
        launch();
    }
}
