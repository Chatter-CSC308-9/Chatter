package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.boundaries.BoundaryWithController;
import main.boundaries.shell_apis.interfaces.Navigator;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;
import main.controllers.LoginController;

public class Login extends BoundaryWithController implements Navigator {

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
    private void handleButtonClick() {
        shellNavigateAPI.setTaskbar("LearnerTaskbar");
        shellNavigateAPI.setContent("ChatterHome");
        loginController.verifyCredentials(usernameField.getText(), passwordField.getText());
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }
}
