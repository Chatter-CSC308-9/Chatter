package main.boundaries.taskbars;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import main.boundaries.Boundary;
import main.boundaries.shell_apis.interfaces.Navigator;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;

public class LearnerTaskbar extends Boundary implements Navigator {

    @FXML
    private ToggleButton accountButton;

    @FXML
    private ToggleButton chatterButton;

    @FXML
    private ToggleButton currentButton;

    @FXML
    private ToggleButton gradedButton;

    @FXML
    private ToggleButton pendingButton;

    private ShellNavigateAPI shellNavigateAPI;

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

    @FXML
    private void handleChatterButtonClick() {
        shellNavigateAPI.setContent("ChatterHome");
        setToggledButton(chatterButton);
    }

    @FXML
    private void handleCurrentButtonClick() {
        shellNavigateAPI.setContent("Current");
        setToggledButton(currentButton);
    }

    @FXML
    private void handlePendingButtonClick() {
        shellNavigateAPI.setContent("Pending");
        setToggledButton(pendingButton);
    }

    @FXML
    void handleGradedButtonClick() {
        shellNavigateAPI.setContent("Graded");
        setToggledButton(gradedButton);
    }

    @FXML
    void handleAccountButtonClick() {
        shellNavigateAPI.setContent("Account");
        setToggledButton(accountButton);
    }

    // toggle designated taskbar button, untoggle others
    void setToggledButton(ToggleButton button) {
        ToggleButton[] buttons = {chatterButton, currentButton, pendingButton, gradedButton, accountButton};
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i] == button) {
                button.setSelected(true);
            }
            else {
                buttons[i].setSelected(false);
            }
        }
    }

}