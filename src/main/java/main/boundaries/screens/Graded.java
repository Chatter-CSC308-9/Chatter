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
                "Cost: " + costAsString(cost) + ". Confirm?",
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setTitle("Confirm");
        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            if (onDone != null) onDone.accept(false); // user cancelled
            return;
        }

        Alert loading = new Alert(Alert.AlertType.NONE);
        loading.setTitle("Processing Payment");
        loading.setHeaderText("Please wait...");
        ProgressIndicator spinner = new ProgressIndicator();
        loading.getDialogPane().setContent(spinner);
        loading.initModality(Modality.APPLICATION_MODAL);
        loading.show();

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return acceptPaymentController.pay();
            }
        };

        task.setOnSucceeded(_ -> {
            closeDialog(loading);
            Boolean ok = task.getValue();
            new Alert(Boolean.TRUE.equals(ok) ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                    Boolean.TRUE.equals(ok) ? "Payment successful!" : "Payment failed.").showAndWait();
            if (onDone != null) onDone.accept(Boolean.TRUE.equals(ok));
        });

        new Thread(task, "payment-task").start();
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
