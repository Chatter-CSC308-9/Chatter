package main.boundaries.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import main.boundaries.Boundary;
import main.boundaries.apis.interfaces.Navigator;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.controllers.SubmitProjectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Pending extends Boundary implements Navigator {

    private static final Logger logger = LoggerFactory.getLogger(Pending.class);

    SubmitProjectController submitProjectController;

    @FXML
    private ListView<Button> completedProjectButtonListView;

    private ShellNavigateAPI shellNavigateAPI;

    public Pending(SubmitProjectController spc) {
        this.submitProjectController = spc;
        super.addController(this.submitProjectController);
    }

    @FXML
    private void handleProjectButtonClick(String projDir) {
        logger.info("project clicked");
        shellNavigateAPI.setContent("Pending"); // use shellNavigate for now to get rid of sonarqube issue
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
        String[] projectNames = submitProjectController.getCompletedProjectNames();
        for (String project : projectNames) {
            Button b = new Button(project);
            projectButtons.add(b);
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleProjectButtonClick(project)));
            String title = submitProjectController.getTitle(project);
            b.setText(title);
            b.setPrefHeight(26.0);
            b.setPrefWidth(290.0);
        }

        // display buttons
        completedProjectButtonListView.setItems(projectButtons);
    }
}
