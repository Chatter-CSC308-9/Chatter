package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.boundaries.Boundary;
import main.boundaries.shell_apis.interfaces.Navigator;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;
import main.controllers.LoginController;

import java.io.IOException;
import java.util.Optional;

public class Login extends Boundary implements Navigator {

    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;

    private ShellNavigateAPI shellNavigateAPI;

    LoginController loginController;

    public Login(LoginController loginController) {
        this.loginController = loginController;
        super.addController(this.loginController);
    }

    @FXML
    private void handleButtonClick() throws IOException {
        Optional<Boolean> verifyCredentials = loginController.verifyCredentials(usernameField.getText(), passwordField.getText());
        if (verifyCredentials.isPresent() && Boolean.TRUE.equals(verifyCredentials.get())) {
            shellNavigateAPI.setTaskbar("GraderTaskbar");
            shellNavigateAPI.setContent("GraderChatterHome");
        } else if (verifyCredentials.isPresent() && Boolean.FALSE.equals(verifyCredentials.get())) {
            shellNavigateAPI.setTaskbar("LearnerTaskbar");
            shellNavigateAPI.setContent("ChatterHome");
        }
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

}
