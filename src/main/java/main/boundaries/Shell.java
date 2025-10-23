package main.boundaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Shell {

    @FXML
    private StackPane taskbarHost;
    @FXML
    private StackPane contentHost;

    @FXML
    private void initialize() {
        // Hardcode your choices here:
        Node taskbar = load("/ui/taskbars/GraderTaskbar.fxml");
        Node screen = load("/ui/screens/CurrentEdit.fxml");

        taskbarHost.getChildren().setAll(taskbar);
        contentHost.getChildren().setAll(screen);
    }

    private Node load(String resourcePath) {
        try {
            return FXMLLoader.load(getClass().getResource(resourcePath));
        } catch (NullPointerException | IOException e) {
            throw new IllegalArgumentException("FXML resource not found: " + resourcePath, e);
        }
    }
}
