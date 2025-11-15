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
import main.controllers.ClaimUngradedProjectController;

import javax.swing.*;

public class GraderCatalog extends Boundary implements Navigator {

    ClaimUngradedProjectController claimUngradedProjectController;

    ShellNavigateAPI shellNavigateAPI;

    @FXML
    private Button refreshButton;

    @FXML
    private TilePane buttonPane;

    @FXML
    private ListView<Button> projectButtonListView;

    public GraderCatalog(ClaimUngradedProjectController cupc) {
        this.claimUngradedProjectController = cupc;
        super.addController(this.claimUngradedProjectController);
    }

    @FXML
    private void handleProjectButtonClick(String projDir) {
        int input = JOptionPane.showConfirmDialog(null, "Do you want to claim this project?");
        if (input == 0 && Boolean.FALSE.equals(this.claimUngradedProjectController.claimProject(projDir))) {
            JOptionPane.showMessageDialog(null, "Failed to claim project. Try refreshing.");
        }
    }

    @FXML
    void refreshUngradedProjectsList() {
        onShow();
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
        String[] projectNames = claimUngradedProjectController.getAvailableProjectNames();

        for (String project : projectNames) {
            Button b = new Button(project);
            projectButtons.add(b);
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleProjectButtonClick(project)));
            String title = claimUngradedProjectController.getTitle(project);
            b.setText(title);
            b.setPrefHeight(26.0);
            b.setPrefWidth(290.0);
        }

        // display buttons
        projectButtonListView.setItems(projectButtons);
    }
}
