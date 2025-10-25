package main.boundaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Shell implements ShellAPI {

    private Map<String, Node> nodes = new HashMap<>();
    private Map<Class<?>, Object> boundaryInstantiations;

    @FXML
    private StackPane taskbarHost;
    @FXML
    private StackPane contentHost; // The screen to be displayed under the taskbar

    public Shell(Map<Class<?>, Object> boundaryInstantiations) {
        this.boundaryInstantiations = boundaryInstantiations;
    }

    // Starts here
    @FXML
    private void initialize() {
        Node screen = getNode("/ui/screens/Login.fxml");
        contentHost.getChildren().setAll(screen); // renders the Login Node
    }

    @FXML
    @Override
    public void setTaskbar(String taskbarName) {
        Node taskbar = getNode("/ui/taskbars/" + taskbarName + ".fxml");
        taskbarHost.getChildren().setAll(taskbar);
    }

    @FXML
    @Override
    public void setContent(String screenName) {
        Node screen = getNode("/ui/screens/" + screenName + ".fxml");
        contentHost.getChildren().setAll(screen);
    }

    private Node getNode(String resourcePath) {
        if (!nodes.containsKey(resourcePath)) {
            Node node = loadNode(resourcePath);
            nodes.put(resourcePath, node);
        }
        return nodes.get(resourcePath);
    }

    // Takes a resource path and creates associated java object (Boundary)
    private Node loadNode(String resourcePath) {
        try {
            var url = getClass().getResource(resourcePath);
            if (url == null) {
                throw new IllegalArgumentException("FXML resource not found: " + resourcePath);
            }
            FXMLLoader loader = new FXMLLoader(url); // gets FXML
            loader.setControllerFactory(type -> {
                Object existing = boundaryInstantiations.get(type);
                if (existing != null) return existing;
                try {
                    // fallback for controllers you didn’t prebuild (e.g., simple taskbars)
                    return type.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException(
                            "No controller provided for " + type.getName() +
                                    " and failed to construct via no-arg constructor.", e);
                }
            });
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
