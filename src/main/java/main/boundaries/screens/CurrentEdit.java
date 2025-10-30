package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import main.boundaries.BoundaryWithController;
import main.boundaries.shell_apis.interfaces.Navigator;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;
import main.controllers.EditProjectController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentEdit extends BoundaryWithController implements Navigator {

    private static final Logger logger = LoggerFactory.getLogger(CurrentEdit.class);

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
        shellNavigateAPI.setContent("Current");
    }

    @FXML
    void handleTextEntryButtonClick() {
        logger.debug("text entry button clicked");
    }

    @FXML
    void handleUploadButtonClick() {
        logger.debug("upload button clicked");
    }

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }
}
