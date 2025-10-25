package main.boundaries.screens;

import javafx.fxml.FXML;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;
import main.controllers.EditWorkController;

public class Current implements Navigator {

    private ShellAPI shellAPI;

    EditWorkController editWorkController = new EditWorkController();

    public Current(EditWorkController editWorkController) {
        this.editWorkController = editWorkController;
    }

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
