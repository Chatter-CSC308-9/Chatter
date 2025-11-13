package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public class Account extends AbstractAccount implements Navigator {

    @FXML
    public Button addFundsButton;



    private ShellNavigateAPI shellNavigateAPI;

    public Account(LogoutController logoutController, DisplayUsernameController displayUsernameController) {
        super(logoutController, displayUsernameController);
    }

    public void handleAddFundsButtonClick() {
        shellNavigateAPI.setContent("Pay");
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }


}
