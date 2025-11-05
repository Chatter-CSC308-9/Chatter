package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.boundaries.Boundary;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;
import main.boundaries.shell_apis.interfaces.Navigator;
import main.controllers.LogoutController;

public class Account extends Boundary implements Navigator {

    @FXML
    public Button logoutButton;

    LogoutController logoutController;

    private ShellNavigateAPI shellNavigateAPI;

    public Account(LogoutController logoutController) {
        this.logoutController = logoutController;
        super.addController(this.logoutController);
    }

    public void handleLogoutButtonClick() {
        this.logoutController.logout();
        shellNavigateAPI.setContent("Login");
        shellNavigateAPI.setTaskbar("EmptyTaskbar");
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }
}
