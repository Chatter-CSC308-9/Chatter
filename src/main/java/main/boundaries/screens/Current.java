package main.boundaries.screens;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.Main;

import java.io.IOException;
import java.util.Objects;

public class Current {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private AnchorPane currentAnchorPane;

    @FXML
    private Button perryThePlatypus;

    public void switchToCurrentEdit(ActionEvent event) throws IOException {
        System.out.println("Clicked perry the platypus");
        /*Parent root = FXMLLoader.load(getClass().getResource("CurrentEdit.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
        /*AnchorPane nextAnchorPane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("screens/CurrentEdit")));
        currentAnchorPane.getChildren().removeAll();
        currentAnchorPane.getChildren().setAll(nextAnchorPane);*/
        Parent currentEditParent = FXMLLoader.load(getClass().getResource("main/resources/ui/screens/CurrentEdit.fxml"));
        Scene currentEditScene = new Scene(currentEditParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(currentEditScene);
        window.show();
    }
}