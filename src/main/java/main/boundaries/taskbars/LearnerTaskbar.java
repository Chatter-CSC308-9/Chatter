package main.boundaries.taskbars;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;

public class LearnerTaskbar implements Navigator {

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

    private ShellAPI shellAPI;

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }

    @FXML
    private void handleChatterButtonClick() {
        shellAPI.setContent("ChatterHome");
        setToggledButton(chatterButton);
    }

    @FXML
    private void handleCurrentButtonClick() {
        shellAPI.setContent("Current");
        setToggledButton(currentButton);
    }

    @FXML
    private void handlePendingButtonClick() {
        shellAPI.setContent("Pending");
        setToggledButton(pendingButton);
    }

    @FXML
    void handleGradedButtonClick() {
        shellAPI.setContent("Graded");
        setToggledButton(gradedButton);
    }

    @FXML
    void handleAccountButtonClick() {
        shellAPI.setContent("Account");
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