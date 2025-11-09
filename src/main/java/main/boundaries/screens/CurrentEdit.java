package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.apis.interfaces.Navigator;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.controllers.EditProjectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CurrentEdit extends Boundary implements Navigator {

    private static final Logger logger = LoggerFactory.getLogger(CurrentEdit.class);

    EditProjectController editProjectController;

    @FXML
    private TextArea projectField;

    @FXML
    private TextField titleField;

    @FXML
    private Button saveAndExitButton;

    @FXML
    private Button submitButton;


    @FXML
    private ToggleButton textEntryButton;

    @FXML
    private ToggleButton uploadButton;

    @FXML
    private VBox uploadInformation;

    private ShellNavigateAPI shellNavigateAPI;

    public CurrentEdit(EditProjectController ewc) {
        this.editProjectController = ewc;
        super.addController(this.editProjectController);
    }

    @FXML
    @Override
    public void onShow() {
        String title = editProjectController.getTitle();
        String work = editProjectController.getWork();
        titleField.setText(title);
        projectField.setText(work);
        projectField.setManaged(true);
        projectField.setVisible(true);
        uploadInformation.setManaged(false);
        uploadInformation.setVisible(false);
    }

    // save and exit
    @FXML
    void handleSaveAndExitButtonClick() {
        saveTitleAndWork();
        exitCurrentEdit();
    }

    private void saveTitleAndWork() {
        String title = titleField.getText();
        String work = projectField.getText();
        editProjectController.saveWork(title, work);
    }

    private void exitCurrentEdit() {
        // empty title and project fields
        titleField.setText("");
        projectField.setText("");

        // back to Current screen
        shellNavigateAPI.setContent("Current");
    }

    @FXML
    void handleTextEntryButtonClick() {
        logger.debug("text entry button clicked");
        // toggle buttons
        textEntryButton.setSelected(true);
        uploadButton.setSelected(false);

        // show text field, hide upload info
        projectField.setManaged(true);
        projectField.setVisible(true);
        uploadInformation.setManaged(false);
        uploadInformation.setVisible(false);
    }

    @FXML
    void handleUploadButtonClick() {
        logger.debug("upload button clicked");

        // toggle buttons and save work
        uploadButton.setSelected(true);
        textEntryButton.setSelected(false);
        saveTitleAndWork();

        // show upload info, hide text field
        uploadInformation.setManaged(true);
        uploadInformation.setVisible(true);
        projectField.setVisible(false);
        projectField.setManaged(false);

        // let user try to upload file
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            this.editProjectController.uploadFile(file);
        }
    }

    @FXML
    void handleSubmitButtonClick() {
        this.editProjectController.submitProject();
        shellNavigateAPI.setContent("Current");
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }
}
