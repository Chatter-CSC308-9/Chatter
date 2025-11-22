package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.apis.interfaces.Navigator;
import main.boundaries.apis.hooks.ShellNavigateAPI;
import main.controllers.EditProjectController;
import main.controllers.SubmitProjectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;

public class CurrentEdit extends Boundary implements Navigator {

    private static final Logger logger = LoggerFactory.getLogger(CurrentEdit.class);

    EditProjectController editProjectController;
    SubmitProjectController submitProjectController;

    @FXML
    private TextArea projectField;

    @FXML
    private TextField titleField;

    @FXML
    private Button saveAndExitButton;

    @FXML
    private Button submitButton;

    @FXML
    private Button submitToAIButton;

    @FXML
    private Button deleteMP3Button;

    @FXML
    private Button deletePNGButton;

    @FXML
    private Button deleteTXTButton;

    @FXML
    private Button uploadFileButton;

    @FXML
    private Button deleteProjectButton;

    @FXML
    private Label noUploadedFilesLabel;

    @FXML
    private HBox uploadedMP3Box;

    @FXML
    private HBox uploadedPNGBox;

    @FXML
    private HBox uploadedTXTBox;

    @FXML
    private ToggleButton textEntryButton;

    @FXML
    private ToggleButton uploadButton;

    @FXML
    private VBox uploadInformation;

    private ShellNavigateAPI shellNavigateAPI;

    private static final String CURRENT = "Current";

    public CurrentEdit(EditProjectController ewc, SubmitProjectController spc) {
        this.editProjectController = ewc;
        this.submitProjectController = spc;
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
        uploadButton.setSelected(false);
        textEntryButton.setSelected(true);
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
        shellNavigateAPI.setContent(CURRENT);
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

        noUploadedFilesLabel.setVisible(true);
        noUploadedFilesLabel.setManaged(true);
        uploadedTXTBox.setVisible(false);
        uploadedTXTBox.setManaged(false);
        uploadedMP3Box.setVisible(false);
        uploadedMP3Box.setManaged(false);
        uploadedPNGBox.setVisible(false);
        uploadedPNGBox.setManaged(false);

        if (Boolean.TRUE.equals(editProjectController.hasUploadedTXT())) {
            noUploadedFilesLabel.setVisible(false);
            noUploadedFilesLabel.setManaged(false);

            uploadedTXTBox.setVisible(true);
            uploadedTXTBox.setManaged(true);
        }
        if (Boolean.TRUE.equals(editProjectController.hasUploadedMP3())) {
            noUploadedFilesLabel.setVisible(false);
            noUploadedFilesLabel.setManaged(false);

            uploadedMP3Box.setVisible(true);
            uploadedMP3Box.setManaged(true);
        }
        if (Boolean.TRUE.equals(editProjectController.hasUploadedPNG())) {
            noUploadedFilesLabel.setVisible(false);
            noUploadedFilesLabel.setManaged(false);

            uploadedPNGBox.setVisible(true);
            uploadedPNGBox.setManaged(true);
        }
    }

    @FXML
    void handleUploadFileButtonClick() {
        // let user try to upload file
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            this.editProjectController.uploadFile(file);
        }
        handleUploadButtonClick();
    }

    @FXML
    void handleDeleteUploadedTXTButtonClick() {
        this.editProjectController.deleteUploadedTXT();
        handleUploadButtonClick();
    }

    @FXML
    void handleDeleteUploadedMP3ButtonClick() {
        this.editProjectController.deleteUploadedMP3();
        handleUploadButtonClick();
    }

    @FXML
    void handleDeleteUploadedPNGButtonClick() {
        this.editProjectController.deleteUploadedPNG();
        handleUploadButtonClick();
    }

    @FXML
    void handleSubmitButtonClick() {
        saveTitleAndWork();
        this.editProjectController.submitProject(submitProjectController);
        shellNavigateAPI.setContent(CURRENT);
    }

    @FXML
    void handleAISubmitButtonClick() {
        logger.debug("AI submitting button clicked");
        this.editProjectController.submitAIProject(submitProjectController);
        shellNavigateAPI.setContent(CURRENT);
    }

    @FXML
    void handleDeleteProjectButtonClick() {
        int input = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this project?");
        if (input == 0) {
            this.editProjectController.deleteProject();
        }
        shellNavigateAPI.setContent(CURRENT);
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }
}
