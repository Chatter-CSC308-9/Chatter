package main;

import com.stripe.Stripe;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.Shell;
import main.boundaries.screens.*;
import main.controllers.*;
import main.controllers.apis.UserIdApiController;

import java.util.*;

public class Main extends Application {

    UserIdApiController userIdApiController = new UserIdApiController();
    LoginController loginController = new LoginController();
    EditProjectController editProjectController = new EditProjectController();
    LogoutController logoutController = new LogoutController();
    DisplayUsernameController displayUsernameController = new DisplayUsernameController();
    AcceptPaymentController acceptPaymentController = new AcceptPaymentController();

    List<Controller> controllers = new ArrayList<>(Arrays.asList(
            loginController,
            editProjectController,
            userIdApiController,
            logoutController,
            displayUsernameController,
            acceptPaymentController));

    Login login = new Login(loginController);
    Current current = new Current(editProjectController);
    CurrentEdit currentEdit = new CurrentEdit(editProjectController);
    Account account = new Account(logoutController, displayUsernameController, acceptPaymentController);
    GraderAccount graderAccount = new GraderAccount(logoutController, displayUsernameController);

    List<Boundary> boundaries = new ArrayList<>(Arrays.asList(
            login,
            current,
            currentEdit,
            account,
            graderAccount));

    Map<Class<?>, Object> boundaryInstantiations = new HashMap<>();

    public static void main(String[] args) {
        Stripe.apiKey = System.getenv("STRIPE_SECRET");
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        for (Boundary boundary : boundaries) {
            boundaryInstantiations.put(boundary.getClass(), boundary);
        }

        for (Controller controller : controllers) {
            userIdApiController.injectControllerAPIs(controller);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Shell.fxml"));
        loader.setControllerFactory(_ -> new Shell(boundaryInstantiations, userIdApiController));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Chatter");
        stage.setScene(scene);
        stage.show();
    }
}