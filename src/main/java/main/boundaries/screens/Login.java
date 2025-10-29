package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;
import main.controllers.LoginController;

public class Login implements Navigator {

    @FXML
    public Button loginButton;

    private ShellAPI shellAPI;

    LoginController loginController;// = new LoginController();

    public Login(LoginController loginController) {
        this.loginController = loginController;
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
