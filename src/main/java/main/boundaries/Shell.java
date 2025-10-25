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
    private StackPane contentHost; // The screen to be displayed under the taskbar

    // Starts here
    @FXML
    private void initialize() {
        Node screen = loadNode("/ui/screens/Login.fxml");
        contentHost.getChildren().setAll(screen); // renders the Login Node
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

    // Takes a resource path and creates associated java object (Boundary)
    private Node loadNode(String resourcePath) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url == null) {
                throw new IllegalArgumentException("FXML resource not found: " + resourcePath);
            }
            FXMLLoader loader = new FXMLLoader(url); // gets FXML
            Node node = loader.load(); // turns FXML into Java
            injectShellAPI(loader.getController()); // adds ShellAPI
            return node;
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to load: " + resourcePath, e);
        }
    }

    // Adds the Shell as a ShellAPI to the Boundary, allowing it to use ShellAPI methods only
    private void injectShellAPI(Object boundary) {
        if (boundary instanceof Navigator navigator) {
            navigator.setShellAPI(this);
        }
    }
}
