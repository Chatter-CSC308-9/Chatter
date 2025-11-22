package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public class Account extends AbstractAccount implements Navigator {

    @FXML
    private Label usernameText;

    @FXML
    private Label emailText;


    public Account(LogoutController logoutController, DisplayUsernameController displayUsernameController) {
        super(logoutController, displayUsernameController);
    }
}
