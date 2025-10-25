package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;

public class CurrentEdit implements Navigator {
    @FXML
    private Button saveAndExitButton;

    @FXML
    private ToggleButton textEntryButton;

    @FXML
    private ToggleButton uploadButton;

    private ShellAPI shellAPI;

    @FXML
    void handleSaveAndExitButtonClick() {
        shellAPI.setContent("Current");
    }

    @FXML
    void handleTextEntryButtonClick() {
        System.out.println("text entry button clicked");
    }

    @FXML
    void handleUploadButtonClick() {
        System.out.println("upload button clicked");
    }

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }
}
