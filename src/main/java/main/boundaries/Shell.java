package main.boundaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Shell implements ShellAPI {

    @FXML
    private StackPane taskbarHost;
    @FXML
    private StackPane contentHost;

    @FXML
    private void initialize() {
        // Hardcode your choices here:
        Node screen = loadNode("/ui/screens/Login.fxml");
        contentHost.getChildren().setAll(screen);
    }

    @FXML
    @Override
    public void setTaskbar(String taskbarName) {
        Node taskbar = loadNode("/ui/taskbars/" + taskbarName + ".fxml");
        taskbarHost.getChildren().setAll(taskbar);
    }

    @FXML
    @Override
    public void setContent(String screenName) {
        Node screen = loadNode("/ui/screens/" + screenName + ".fxml");
        contentHost.getChildren().setAll(screen);
    }

    private void injectShellAPI(Object boundary) {
        if (boundary instanceof Navigator navigator) {
            navigator.setShellAPI(this);
        }
    }

    private Node loadNode(String resourcePath) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url == null) {
                throw new IllegalArgumentException("FXML resource not found: " + resourcePath);
            }
            FXMLLoader loader = new FXMLLoader(url);
            Node node = loader.load();                // 1) create the controller & UI
            injectShellAPI(loader.getController());   // 2) now the controller exists
            return node;
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load: " + resourcePath, e);
        }
    }
}
