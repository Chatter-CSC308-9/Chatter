package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;
import main.controllers.EditProjectController;

public class CurrentEdit implements Navigator {
    EditProjectController editProjectController;

    @FXML
    private TextArea projectField;

    @FXML
    private TextField titleField;

    @FXML
    private Button saveAndExitButton;

    @FXML
    private ToggleButton textEntryButton;

    @FXML
    private ToggleButton uploadButton;

    private ShellAPI shellAPI;

    public CurrentEdit(EditProjectController ewc) {
        this.editProjectController = ewc;
    }

    @FXML
    @Override
    public void onShow() {
        String title = editProjectController.getTitle();
        String work = editProjectController.getWork();
        titleField.setText(title);
        projectField.setText(work);
    }

    @FXML
    void handleSaveAndExitButtonClick() {
        // save work
        String title = titleField.getText();
        String work = projectField.getText();
        editProjectController.saveWork(title, work);

        // empty title and project fields
        titleField.setText("");
        projectField.setText("");

        // back to Current screen
        shellAPI.setContent("Current");
    }

    @FXML
    void handleTextEntryButtonClick() {
        System.out.println("text entry button clicked");
    }

    @FXML
    void handleUploadButtonClick() {
        System.out.println("upload button clicked");
    }

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }
}
