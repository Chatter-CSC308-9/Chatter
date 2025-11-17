package main.boundaries.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.boundaries.Boundary;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.CreateAccountController;

public class CreateAccount extends Boundary implements Navigator {


    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public Button createAccountButton;
    @FXML
    public Button backToLoginButton;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField repeatPasswordField;
    @FXML
    public CheckBox isGraderCheckbox;

    private ShellNavigateAPI shellNavigateAPI;

    CreateAccountController createAccountController;

    public CreateAccount(CreateAccountController createAccountController) {
        this.createAccountController = createAccountController;
        super.addController(this.createAccountController);
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

    @FXML
    public void handleCreateAccountButtonClick(ActionEvent actionEvent) {
        String username = this.usernameField.getText();
        String email = this.emailField.getText();
        String password = this.passwordField.getText();
        String passwordRepeat = this.repeatPasswordField.getText();
        boolean isGrader = this.isGraderCheckbox.isSelected();
        if (username == null || email == null || password == null || passwordRepeat == null) {
            registrationMessage("Fill in all fields", true);
        }
        else if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            registrationMessage("Invalid email", true);
        } else if (!password.equals(passwordRepeat)) {
            registrationMessage("Passwords do not match", true);
        }
        if (this.createAccountController.createAccount(username, email, password, isGrader)) {
            // do async stuff
        }
        this.usernameField.clear();
        this.emailField.clear();
        this.passwordField.clear();
        this.repeatPasswordField.clear();
        this.shellNavigateAPI.setContent("Login");
    }

    private void registrationMessage(String message, boolean isError) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (isError) alert.setTitle("Registration error");
        else alert.setTitle("Registration message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void handleBackToLoginButtonClick(ActionEvent actionEvent) {
        this.shellNavigateAPI.setContent("Login");
    }
}
