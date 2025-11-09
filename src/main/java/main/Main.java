package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.Shell;
import main.boundaries.screens.*;
import main.controllers.*;

import java.util.*;

public class Main extends Application {

    LoginController loginController = new LoginController();
    EditProjectController editProjectController = new EditProjectController();
    APIController apiController = new APIController();
    LogoutController logoutController = new LogoutController();
    DisplayUsernameController displayUsernameController = new DisplayUsernameController();
    SubmitProjectController submitProjectController = new SubmitProjectController();

    List<Controller> controllers = new ArrayList<>(Arrays.asList(
            loginController,
            editProjectController,
            apiController,
            logoutController,
            displayUsernameController,
            submitProjectController));

    Login login = new Login(loginController);
    Current current = new Current(editProjectController);
    CurrentEdit currentEdit = new CurrentEdit(editProjectController, submitProjectController);
    Pending pending = new Pending(submitProjectController);
    Account account = new Account(logoutController, displayUsernameController);
    GraderAccount graderAccount = new GraderAccount(logoutController, displayUsernameController);

    List<Boundary> boundaries = new ArrayList<>(Arrays.asList(
            login,
            current,
            currentEdit,
            pending,
            account,
            graderAccount));

    Map<Class<?>, Object> boundaryInstantiations = new HashMap<>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        for (Boundary boundary : boundaries) {
            boundaryInstantiations.put(boundary.getClass(), boundary);
        }

        for (Controller controller : controllers) {
            apiController.injectControllerAPIs(controller);
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Shell.fxml"));
        loader.setControllerFactory(_ -> new Shell(boundaryInstantiations, apiController));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Chatter");
        stage.setScene(scene);
        stage.show();
    }
}