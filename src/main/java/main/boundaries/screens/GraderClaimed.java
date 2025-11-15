package main.boundaries.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.apis.interfaces.Navigator;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.controllers.SubmitGradedProjectController;

import javax.swing.*;
import java.io.File;

public class GraderClaimed extends Boundary implements Navigator {

    SubmitGradedProjectController submitGradedProjectController;

    @FXML
    private TilePane buttonPane;

    @FXML
    private ListView<VBox> projectButtonListView;

    ShellNavigateAPI shellNavigateAPI;

    public GraderClaimed(SubmitGradedProjectController sgpc) {
        this.submitGradedProjectController = sgpc;
        super.addController(this.submitGradedProjectController);
    }

    @FXML
    private void handleProjectButtonClick(VBox v, Boolean show) {
        for (int i = 1; i < v.getChildren().size(); i++) {
            Node n = v.getChildren().get(i);
            if (Boolean.TRUE.equals(show)) {
                n.setVisible(true);
                n.setManaged(true);
            }
            else {
                n.setVisible(false);
                n.setManaged(false);
            }
        }
    }

    // download files
    @FXML
    private void handleDownloadTXTButtonClick(String projDir) {
        downloadFile(projDir, "/work.txt");
    }
    @FXML
    private void handleDownloadUploadedTXTButtonClick(String projDir) {
        downloadFile(projDir, "/uploaded_work.txt");
    }
    @FXML
    private void handleDownloadMP3ButtonClick(String projDir) {
        downloadFile(projDir, "/uploaded_work.mp3");
    }
    @FXML
    private void handleDownloadPNGButtonClick(String projDir) {
        downloadFile(projDir, "/uploaded_work.png");
    }

    // let user upload file and submit
    @FXML
    private void handleUploadAndSubmitButtonClick(String projDir) {
        // let user try to upload file
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null && this.submitGradedProjectController.uploadFile(file, projDir) == 0) {
            this.submitGradedProjectController.submitGradedProject(projDir);
            JOptionPane.showMessageDialog(null, "Successfully submitted!");
            onShow();
        }
    }

    // let user download a file
    void downloadFile(String projDir, String fileName) {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (this.submitGradedProjectController.download(projDir, fileName, file) == 1) {
                JOptionPane.showMessageDialog(null, "Error in downloading project. There may be a naming conflict in your destination folder.");
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
        ObservableList<VBox> projectList = FXCollections.observableArrayList();

        // create one button per project
        String[] projectNames = submitGradedProjectController.getProjectNames();

        for (String project : projectNames) {
            VBox v = new VBox();

            // hbox to store project button and submit button
            HBox h = new HBox();

            // add button for whole project
            ToggleButton b = new ToggleButton(project);
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleProjectButtonClick(v, b.isSelected())));
            String title = submitGradedProjectController.getTitle(project);
            b.setText(title);
            b.setPrefHeight(26.0);
            b.setPrefWidth(290.0);
            h.getChildren().add(b);

            // add upload and submit file button
            Button b0 = new Button("Upload and Submit");
            b0.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleUploadAndSubmitButtonClick(project)));
            h.getChildren().add(b0);

            v.getChildren().add(h);

            // add and hide buttons to download each file
            Button b1 = new Button("Download In-App TXT");
            b1.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleDownloadTXTButtonClick(project)));
            v.getChildren().add(b1);
            b1.setVisible(false);
            b1.setManaged(false);
            if (Boolean.TRUE.equals(submitGradedProjectController.hasUploadedTXT(project))) {
                Button b2 = new Button("Download TXT");
                b2.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleDownloadUploadedTXTButtonClick(project)));
                v.getChildren().add(b2);
                b2.setVisible(false);
                b2.setManaged(false);
            }
            if (Boolean.TRUE.equals(submitGradedProjectController.hasUploadedMP3(project))) {
                Button b3 = new Button("Download MP3");
                b3.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleDownloadMP3ButtonClick(project)));
                v.getChildren().add(b3);
                b3.setVisible(false);
                b3.setManaged(false);
            }
            if (Boolean.TRUE.equals(submitGradedProjectController.hasUploadedPNG(project))) {
                Button b4 = new Button("Download PNG");
                b4.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleDownloadPNGButtonClick(project)));
                v.getChildren().add(b4);
                b4.setVisible(false);
                b4.setManaged(false);
            }

            projectList.add(v);
        }

        // display buttons
        projectButtonListView.setItems(projectList);
    }
}
