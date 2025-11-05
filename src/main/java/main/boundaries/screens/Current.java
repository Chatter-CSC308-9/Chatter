package main.boundaries.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import main.boundaries.Boundary;
import main.boundaries.shell_apis.interfaces.Navigator;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;
import main.controllers.EditProjectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Current extends Boundary implements Navigator {

    private static final Logger logger = LoggerFactory.getLogger(Current.class);

    EditProjectController editProjectController;

    @FXML
    private Button createNewProjectButton;

    @FXML
    private Button perryThePlatypusButton;

    @FXML
    private TilePane buttonPane;

    @FXML
    private ListView<Button> projectButtonListView;

    private ShellNavigateAPI shellNavigateAPI;

    public Current(EditProjectController ewc) {
        this.editProjectController = ewc;
        super.addController(this.editProjectController);
    }

    @FXML
    private void handleCreateNewProjectButtonClick() {
        logger.debug("Create new project");
        editProjectController.editProject("name");
    }

    @FXML
    private void handleProjectButtonClick(String projDir) {
        editProjectController.editProject(projDir);
        shellNavigateAPI.setContent("CurrentEdit");
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
        String[] projectNames = editProjectController.getProjectNames();
        for (String project : projectNames) {
            Button b = new Button(project);
            projectButtons.add(b);
            b.addEventHandler(MouseEvent.MOUSE_CLICKED, (event -> handleProjectButtonClick(project)));
            editProjectController.setProject(project);
            String title = editProjectController.getTitle();
            b.setText(title);
            b.setPrefHeight(26.0);
            b.setPrefWidth(290.0);
        }

        // display buttons
        projectButtonListView.setItems(projectButtons);
    }
}
