package main.boundaries.screens;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;
import main.controllers.EditProjectController;

import java.util.ArrayList;

public class Current implements Navigator {
    EditProjectController editProjectController;

    @FXML
    private Button createNewProjectButton;

    @FXML
    private Button perryThePlatypusButton;

    @FXML
    private TilePane buttonPane;

    @FXML
    private ListView<Button> projectButtonListView;

    private ObservableList<Button> projectButtons = FXCollections.observableArrayList();

    private ShellAPI shellAPI;

    public Current(EditProjectController ewc) {
        this.editProjectController = ewc;
    }

    @FXML
    private void handleCreateNewProjectButtonClick() {
        System.out.println("Create new project");
        editProjectController.editProject("name");
        //shellAPI.setContent("CurrentEdit");
    }

    @FXML
    private void handleProjectButtonClick(String projDir) {
        editProjectController.editProject(projDir);
        shellAPI.setContent("CurrentEdit");
    }

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }

    @FXML
    @Override
    public void onShow() {
        // empty projectButtons list
        projectButtons = FXCollections.observableArrayList();

        // create one button per project
        ArrayList<String> projectNames = editProjectController.getProjectNames();
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
