package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.boundaries.Boundary;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public class Account extends Boundary implements Navigator {

    @FXML
    public Button logoutButton;
    @FXML
    public Label greetingLabel;

    LogoutController logoutController;
    DisplayUsernameController displayUsernameController;

    private ShellNavigateAPI shellNavigateAPI;

    public Account(LogoutController logoutController, DisplayUsernameController displayUsernameController) {
        this.logoutController = logoutController;
        this.displayUsernameController = displayUsernameController;
        super.addController(this.logoutController);
        super.addController(this.displayUsernameController);
    }

    public void handleLogoutButtonClick() {
        this.logoutController.logout();
        shellNavigateAPI.setContent("Login");
        shellNavigateAPI.setTaskbar();
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

    @FXML
    @Override
    public void onShow() {
        greetingLabel.setText("Hi " + displayUsernameController.getUsername() + "!");
    }

}
