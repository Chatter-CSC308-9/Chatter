package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;

public class Current implements Navigator {
    @FXML
    public Button loginButton;

    private ShellAPI shellAPI;

    @FXML
    private void handleButtonClick() {
        shellAPI.setTaskbar("LearnerTaskbar");
        shellAPI.setContent("ChatterHome");
    }

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }
}
