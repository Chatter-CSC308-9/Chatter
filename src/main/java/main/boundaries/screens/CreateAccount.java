package main.boundaries.screens;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
            return;
        }
        else if (!email.matches("(?i)^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) {
            registrationMessage("Invalid email", true);
            return;
        } else if (!password.equals(passwordRepeat)) {
            registrationMessage("Passwords do not match", true);
            return;
        }
        if (this.createAccountController.createAccount(username, email, password, isGrader)) {
            stripeOnboard();
        }
        this.usernameField.clear();
        this.emailField.clear();
        this.passwordField.clear();
        this.repeatPasswordField.clear();
        this.shellNavigateAPI.setContent("Login");
    }

    private void stripeOnboard() {
        Alert loading = new Alert(Alert.AlertType.NONE);
        loading.setTitle("Stripe onboarding");
        loading.setHeaderText("Follow the instructions on your web browser");
        ProgressIndicator spinner = new ProgressIndicator();
        loading.getDialogPane().setContent(spinner);
        loading.initModality(Modality.APPLICATION_MODAL);
        loading.show();

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return createAccountController.stripeOnboard();
            }
        };

        task.setOnSucceeded(_ -> {
            closeDialog(loading);
            Boolean ok = task.getValue();
            new Alert(Boolean.TRUE.equals(ok) ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    Boolean.TRUE.equals(ok) ? "Registration success!" : "Registration failure.").showAndWait();
        });

        new Thread(task, "onboard-task").start();
    }

    private void closeDialog(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.close();
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
