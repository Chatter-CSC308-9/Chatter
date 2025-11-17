package main.boundaries.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import main.boundaries.Boundary;
import main.boundaries.apis.interfaces.Navigator;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.controllers.DownloadGradedProjectController;

import javax.swing.*;
import java.io.File;

public class Graded extends Boundary implements Navigator {

    DownloadGradedProjectController downloadGradedProjectController;

    @FXML
    private TilePane buttonPane;

    @FXML
    private ListView<Button> projectButtonListView;

    ShellNavigateAPI shellNavigateAPI;

    public Graded(DownloadGradedProjectController dgpc) {
        this.downloadGradedProjectController = dgpc;
        super.addController(this.downloadGradedProjectController);
    }

    @FXML
    private void handleProjectButtonClick(String projDir) {
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
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleProjectButtonClick(project)));
            String title = downloadGradedProjectController.getTitle(project);
            b.setText("Download Feedback for: " + title);
            b.setPrefHeight(26.0);
            b.setPrefWidth(290.0);
        }

        // display buttons
        projectButtonListView.setItems(projectButtons);
    }
}
