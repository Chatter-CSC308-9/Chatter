package main.framework;

import com.stripe.Stripe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        var module = new AppModule();

        var loader = new FXMLLoader(getClass().getResource("/ui/Shell.fxml"));
        loader.setControllerFactory(module.controllerFactory());

        var scene = new Scene(loader.load());
        stage.setTitle("Chatter");
        stage.setScene(scene);
        stage.show();
    }
}
