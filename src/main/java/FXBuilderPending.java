import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXBuilderPending extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Pending.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Chatter - FXML UI");
        stage.setScene(scene);
        stage.show();
    }
}
