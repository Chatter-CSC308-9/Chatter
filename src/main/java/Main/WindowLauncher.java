package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WindowLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String windowAddress = getParameters().getRaw().getFirst();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/" + windowAddress + ".fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Chatter - FXML UI");
        stage.setScene(scene);
        stage.show();
    }
}
