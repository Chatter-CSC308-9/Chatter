import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

public class Main extends Application {

    Label label1;
    Button button1;
    int i = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Chatter");
        label1 = new Label("Behold the VBox");
        VBox root = new VBox();

        Scene scene = new Scene(root, 400, 400);
        stage.setScene(scene);

        button1 = new Button("Push me");
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                label1.setText("Pushes: " + i);
                i++;
            }
        });

        root.getChildren().addAll(label1, button1);
        stage.show();
    }
}