package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.boundaries.Boundary;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public abstract class AbstractAccount extends Boundary implements Navigator {

    @FXML
    public Label greetingLabel;
    @FXML
    public Button logoutButton;

    LogoutController logoutController;
    DisplayUsernameController displayUsernameController;

    private ShellNavigateAPI shellNavigateAPI;

    protected AbstractAccount(LogoutController logoutController, DisplayUsernameController displayUsernameController) {
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
