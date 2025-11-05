package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.boundaries.Boundary;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.FinanceController;
import main.controllers.LogoutController;

public class GraderAccount extends Boundary implements Navigator {

    @FXML
    public Button logoutButton;
    @FXML
    public Label greetingLabel;

    LogoutController logoutController;
    FinanceController financeController;

    private ShellNavigateAPI shellNavigateAPI;

    public GraderAccount(LogoutController logoutController, FinanceController financeController) {
        this.logoutController = logoutController;
        this.financeController = financeController;
        super.addController(this.logoutController);
        super.addController(this.financeController);
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
        greetingLabel.setText("Hi " + financeController.getUserID() + "!");
    }

}
