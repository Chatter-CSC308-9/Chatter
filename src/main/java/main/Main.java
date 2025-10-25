package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.boundaries.Shell;

public class Main extends Application {

    Shell shell = new Shell();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Shell.fxml"));
        loader.setController(shell);
        Scene scene = new Scene(loader.load());
        stage.setTitle("Chatter");
        stage.setScene(scene);
        stage.show();
    }
}