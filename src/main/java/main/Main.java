package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.boundaries.Boundary;
import main.boundaries.Shell;
import main.boundaries.screens.Current;
import main.boundaries.screens.CurrentEdit;
import main.boundaries.screens.Login;
import main.controllers.APIController;
import main.controllers.Controller;
import main.controllers.EditProjectController;
import main.controllers.LoginController;

import java.util.*;

public class Main extends Application {

    LoginController loginController = new LoginController();
    EditProjectController editProjectController = new EditProjectController();
    APIController apiController = new APIController();

    List<Controller> controllers = new ArrayList<>(Arrays.asList(
            loginController,
            editProjectController,
            apiController));

    Login login = new Login(loginController);
    Current current = new Current(editProjectController);
    CurrentEdit currentEdit = new CurrentEdit(editProjectController);

    List<Boundary> boundaries = new ArrayList<>(Arrays.asList(
            login,
            current,
            currentEdit));

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

        editProjectController.setCurrentEditBoundary(currentEdit);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/Shell.fxml"));
        loader.setControllerFactory(_ -> new Shell(boundaryInstantiations, apiController));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Chatter");
        stage.setScene(scene);
        stage.show();
    }
}