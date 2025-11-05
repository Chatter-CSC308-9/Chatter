package main.boundaries.taskbars;

import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import main.boundaries.Boundary;
import main.boundaries.shell_apis.interfaces.Navigator;
import main.boundaries.shell_apis.hooks.ShellNavigateAPI;

public class GraderTaskbar extends Boundary implements Navigator {

    @FXML
    private ToggleButton accountButton;

    @FXML
    private ToggleButton chatterButton;

    @FXML
    private ToggleButton catalogButton;

    @FXML
    private ToggleButton claimedButton;

    private ShellNavigateAPI shellNavigateAPI;

    @Override
    public void setNavigateAPI(ShellNavigateAPI shellNavigateAPI) {
        this.shellNavigateAPI = shellNavigateAPI;
    }

    @FXML
    private void handleChatterButtonClick() {
        shellNavigateAPI.setContent("GraderChatterHome");
        setToggledButton(chatterButton);
    }

    @FXML
    private void handleCatalogButtonClick() {
        shellNavigateAPI.setContent("GraderCatalog");
        setToggledButton(catalogButton);
    }

    @FXML
    private void handleClaimedButtonClick() {
        shellNavigateAPI.setContent("GraderClaimed");
        setToggledButton(claimedButton);
    }

    @FXML
    void handleAccountButtonClick() {
        shellNavigateAPI.setContent("GraderAccount");
        setToggledButton(accountButton);
    }

    // toggle designated taskbar button, untoggle others
    void setToggledButton(ToggleButton button) {
        ToggleButton[] buttons = {chatterButton, catalogButton, claimedButton, accountButton};
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
