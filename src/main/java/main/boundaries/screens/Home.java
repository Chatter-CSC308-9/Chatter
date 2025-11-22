package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.boundaries.Boundary;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public class Home extends Boundary implements Navigator {

    @FXML
    public Label currentText;
    @FXML
    private Label pendingText;
    @FXML
    private Label gradedText;

    LogoutController logoutController;
    DisplayUsernameController displayUsernameController;

    ShellNavigateAPI shellNavigateAPI;

    public Home(DisplayUsernameController displayUsernameController) {
        this.displayUsernameController = displayUsernameController;
        super.addController(this.displayUsernameController);
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

    @FXML
    @Override
    public void onShow() {
        int[] nums = displayUsernameController.getNumOfEachProjectType();
        currentText.setText(Integer.toString(nums[0]));
        pendingText.setText(Integer.toString(nums[1]));
        gradedText.setText(Integer.toString(nums[2]));
    }
}
