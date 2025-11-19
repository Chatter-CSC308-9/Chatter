package main.boundaries.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.apis.interfaces.Navigator;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.controllers.AcceptPaymentController;
import main.controllers.DownloadGradedProjectController;

import javax.swing.*;
import java.io.File;
import java.util.function.Consumer;

public class Graded extends Boundary implements Navigator {

    DownloadGradedProjectController downloadGradedProjectController;
    AcceptPaymentController acceptPaymentController;

    @FXML
    private TilePane buttonPane;

    @FXML
    private ListView<Button> projectButtonListView;

    ShellNavigateAPI shellNavigateAPI;

    public Graded(DownloadGradedProjectController dgpc, AcceptPaymentController acceptPaymentController) {
        this.downloadGradedProjectController = dgpc;
        super.addController(this.downloadGradedProjectController);
        this.acceptPaymentController = acceptPaymentController;
        super.addController(this.acceptPaymentController);
    }

    @FXML
    private void handleProjectButtonClick(String projDir) {
        initiatePayment(projDir, ok -> {
            if (Boolean.TRUE.equals(ok)) {
                downloadGradedProject(projDir);
            }
        });
    }

    private void initiatePayment(String projDir, Consumer<Boolean> onDone) {
        int cost = acceptPaymentController.getCostInCents(projDir);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Cost: " + costAsString(cost) + ". This will open Stripe Checkout in your browser.",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirm Payment");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            if (onDone != null) onDone.accept(false);
            return;
        }

        Alert loading = new Alert(Alert.AlertType.NONE);
        loading.setTitle("Opening Checkout");
        loading.setHeaderText("Opening Stripe Checkout in your browser...");
        ProgressIndicator spinner = new ProgressIndicator();
        loading.getDialogPane().setContent(spinner);
        loading.initModality(Modality.APPLICATION_MODAL);
        loading.show();

        Task<String> task = getStringTask(projDir, onDone, loading);

        new Thread(task, "payment-task").start();
    }

    private Task<String> getStringTask(String projDir, Consumer<Boolean> onDone, Alert loading) {
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return acceptPaymentController.pay(projDir);
            }
        };

        task.setOnSucceeded(_ -> {
            closeDialog(loading);
            String sessionId = task.getValue();

            if (sessionId != null) {
                Alert waiting = new Alert(Alert.AlertType.INFORMATION);
                waiting.setTitle("Complete Payment");
                waiting.setHeaderText("Complete payment in your browser, then click OK");
                waiting.getButtonTypes().setAll(ButtonType.OK);
                waiting.showAndWait();

                verifyPaymentAndDownload(sessionId, projDir, onDone);
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to open payment checkout.").showAndWait();
                if (onDone != null) onDone.accept(false);
            }
        });
        return task;
    }


    private void verifyPaymentAndDownload(String sessionId, String projDir, Consumer<Boolean> onDone) {
        Alert verifying = new Alert(Alert.AlertType.NONE);
        verifying.setTitle("Verifying Payment");
        verifying.setHeaderText("Checking payment status...");
        ProgressIndicator spinner = new ProgressIndicator();
        verifying.getDialogPane().setContent(spinner);
        verifying.initModality(Modality.APPLICATION_MODAL);
        verifying.show();

        Task<Boolean> verifyTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return acceptPaymentController.verifyPayment(sessionId);
            }
        };

        verifyTask.setOnSucceeded(_ -> {
            closeDialog(verifying);
            Boolean verified = verifyTask.getValue();

            if (Boolean.TRUE.equals(verified)) {
                new Alert(Alert.AlertType.INFORMATION, "Payment successful!").showAndWait();
                if (onDone != null) onDone.accept(true);
            } else {
                Alert retry = new Alert(Alert.AlertType.WARNING);
                retry.setHeaderText("Payment not completed. Please complete it in your browser, then click OK");
                retry.getButtonTypes().setAll(ButtonType.OK);
                retry.showAndWait();

                verifyPaymentAndDownload(sessionId, projDir, onDone);
            }
        });

        new Thread(verifyTask, "verify-payment-task").start();
    }

    private void closeDialog(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.close();
    }


    private String costAsString(int cost) {
        String centsAsString = String.valueOf(cost);
        if (cost < 100) {
            return "$0." + centsAsString;
        }
        return "$" + centsAsString.substring(0, centsAsString.length()-2) + "." + centsAsString.substring(centsAsString.length()-2);
    }

    private void downloadGradedProject(String projDir) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (this.downloadGradedProjectController.downloadFeedback(projDir, file) == 1) {
                JOptionPane.showMessageDialog(null, "Error in downloading feedback. There may be a naming conflict in your destination folder.");
            }
            else {
                JOptionPane.showMessageDialog(null, "Successfully downloaded");
            }
        }
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

    @FXML
    @Override
    public void onShow() {
        // empty projectButtons list
        ObservableList<Button> projectButtons = FXCollections.observableArrayList();

        // create one button per project
        String[] projectNames = downloadGradedProjectController.getGradedProjectNames();

        for (String project : projectNames) {
            Button b = new Button(project);
            projectButtons.add(b);
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> handleProjectButtonClick(project));
            String title = downloadGradedProjectController.getTitle(project);
            b.setText("Download Feedback for: " + title);
            b.setPrefHeight(26.0);
            b.setPrefWidth(290.0);
        }

        // display buttons
        projectButtonListView.setItems(projectButtons);
    }
}
