package main.boundaries.screens;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.TilePane;
import main.boundaries.Navigator;
import main.boundaries.ShellAPI;
import main.controllers.EditWorkController;

public class Current implements Navigator {
    EditWorkController editWorkController;

    @FXML
    private Button createNewWorkButton;

    @FXML
    private Button perryThePlatypusButton;

    @FXML
    private TilePane buttonPane;

    private ShellAPI shellAPI;

    /*public Current(EditWorkController ewc) {
        this.editWorkController = ewc;
    }*/

    /*public void initialize() {
        for (int i = 1 ; i <= 3 ; i++) {
            String buttonText = "Button "+i ;
            Button button = new Button(buttonText);
            buttonPane.getChildren().add(button);
            button.setOnAction(e -> {
                // whatever you need here: you know the button pressed is the
                // one and only button the handler is registered with
                System.out.println(buttonText + " clicked");
            });
        }
    }*/

    @FXML
    private void handleCreateNewWorkButtonClick() {
        System.out.println("Create new work");
        //editWorkController.openAssignment("name");
        //shellAPI.setContent("CurrentEdit");
    }

    @FXML
    private void handleAssignmentButtonClick() {
        System.out.println("Assignment clicked");
        shellAPI.setContent("CurrentEdit");

    }

    @Override
    public void setShellAPI(ShellAPI shellAPI) {
        this.shellAPI = shellAPI;
    }
}
