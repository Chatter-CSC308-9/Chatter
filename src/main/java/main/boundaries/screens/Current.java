package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;
import main.controllers.EditProjectController;

public class Current implements Navigator {
    EditProjectController editProjectController;

    @FXML
    private Button createNewProjectButton;

    @FXML
    private Button perryThePlatypusButton;

    @FXML
    private TilePane buttonPane;

    private ShellAPI shellAPI;

    public Current(EditProjectController ewc) {
        this.editProjectController = ewc;
    }

    @FXML
    private void handleCreateNewProjectButtonClick() {
        System.out.println("Create new project");
        editProjectController.editProject("name");
        //shellAPI.setContent("CurrentEdit");
    }

    // NOTE: THIS IS HARD-CODED TO WORK ONLY WITH PERRY BUTTON AND OPEN PROJECT0 FOLDER FROM SERVER
    @FXML
    private void handlePerryThePlatypusButtonClick() {
        editProjectController.editProject("project0");
        shellAPI.setContent("CurrentEdit");
    }

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }
}
