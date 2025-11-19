package main.boundaries.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.boundaries.Boundary;
import main.boundaries.apis.interfaces.Navigator;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.controllers.LoginController;

import java.util.Optional;

public class Login extends Boundary implements Navigator {

    @FXML
    public Button loginButton;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button createAccountButton;

    private ShellNavigateAPI shellNavigateAPI;

    LoginController loginController;

    public Login(LoginController loginController) {
        this.loginController = loginController;
        super.addController(this.loginController);
    }

    @FXML
    private void handleLoginButtonClick() {
        Optional<Boolean> verifyCredentials = loginController.verifyCredentials(usernameField.getText(), passwordField.getText());
        if (verifyCredentials.isPresent()) {
            this.usernameField.clear();
            this.passwordField.clear();
            if (Boolean.TRUE.equals(verifyCredentials.get())) {
                shellNavigateAPI.setTaskbar("GraderTaskbar");
                shellNavigateAPI.setContent("GraderChatterHome");
            } else if (Boolean.FALSE.equals(verifyCredentials.get())) {
                shellNavigateAPI.setTaskbar("LearnerTaskbar");
                shellNavigateAPI.setContent("ChatterHome");
            }
        }
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

    @FXML
    public void handleCreateAccountButtonClick(ActionEvent actionEvent) {
        this.shellNavigateAPI.setContent("CreateAccount");
    }
}
