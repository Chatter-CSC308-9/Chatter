package main.boundaries.screens;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.CreateAccountController;

import java.awt.*;
import java.net.URI;
import java.util.Optional;

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
        Optional<Long> userIdIfGrader = this.createAccountController.createAccount(username, email, password, isGrader);
        userIdIfGrader.ifPresent(this::stripeOnboard);
        this.usernameField.clear();
        this.emailField.clear();
        this.passwordField.clear();
        this.repeatPasswordField.clear();
        this.shellNavigateAPI.setContent("Login");
    }

    private void stripeOnboard(long userId) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                String link = createAccountController.stripeOnboard(userId);
                Desktop.getDesktop().browse(new URI(link));
                return true;
            }
        };

        task.setOnSucceeded(_ -> {
            Alert waiting = new Alert(Alert.AlertType.INFORMATION);
            waiting.setTitle("Stripe onboarding");
            waiting.setHeaderText("Complete onboarding in your browser, then click OK");
            waiting.getButtonTypes().setAll(ButtonType.OK);
            waiting.showAndWait();

            verifyOnboarding(userId);
        });

        new Thread(task, "onboard-task").start();
    }

    private void verifyOnboarding(long userId) {
        Alert verifying = new Alert(Alert.AlertType.NONE);
        verifying.setTitle("Verifying");
        verifying.setHeaderText("Checking onboarding status...");
        ProgressIndicator spinner = new ProgressIndicator();
        verifying.getDialogPane().setContent(spinner);
        verifying.initModality(Modality.APPLICATION_MODAL);
        verifying.show();

        Task<Boolean> verifyTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return createAccountController.checkOnboardingComplete(userId);
            }
        };

        verifyTask.setOnSucceeded(_ -> {
            closeDialog(verifying);
            Boolean complete = verifyTask.getValue();

            if (Boolean.TRUE.equals(complete)) {
                new Alert(Alert.AlertType.INFORMATION, "Registration successful!").showAndWait();
            } else {
                Alert retry = new Alert(Alert.AlertType.WARNING);
                retry.setHeaderText("Onboarding not completed yet. Please complete it in your browser, then click OK");
                retry.getButtonTypes().setAll(ButtonType.OK);
                retry.showAndWait();

                verifyOnboarding(userId); // Recursively check again
            }
        });

        new Thread(verifyTask, "verify-task").start();
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
