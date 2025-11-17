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

    LoginController loginController = new LoginController();
    EditProjectController editProjectController = new EditProjectController();
    UserIdApiController userIdApiController = new UserIdApiController();
    LogoutController logoutController = new LogoutController();
    DisplayUsernameController displayUsernameController = new DisplayUsernameController();
    AcceptPaymentController acceptPaymentController = new AcceptPaymentController();
    SubmitProjectController submitProjectController = new SubmitProjectController();
    ClaimUngradedProjectController claimUngradedProjectController = new ClaimUngradedProjectController();
    SubmitGradedProjectController submitGradedProjectController = new SubmitGradedProjectController();
    DownloadGradedProjectController downloadGradedProjectController = new DownloadGradedProjectController();
    CreateAccountController createAccountController = new CreateAccountController();

    List<Controller> controllers = new ArrayList<>(Arrays.asList(
            loginController,
            editProjectController,
            userIdApiController,
            logoutController,
            displayUsernameController,
            submitProjectController,
            claimUngradedProjectController,
            submitGradedProjectController,
            downloadGradedProjectController,
            acceptPaymentController,
            submitProjectController,
            createAccountController));

    Login login = new Login(loginController);
    Current current = new Current(editProjectController);
    CurrentEdit currentEdit = new CurrentEdit(editProjectController, submitProjectController);
    Pending pending = new Pending(submitProjectController);
    Account account = new Account(logoutController, displayUsernameController);
    GraderAccount graderAccount = new GraderAccount(logoutController, displayUsernameController);
    GraderCatalog graderCatalog = new GraderCatalog(claimUngradedProjectController);
    GraderClaimed graderClaimed = new GraderClaimed(submitGradedProjectController);
    Graded graded = new Graded(downloadGradedProjectController, acceptPaymentController);
    CreateAccount createAccount = new CreateAccount(createAccountController);

    List<Boundary> boundaries = new ArrayList<>(Arrays.asList(
            login,
            current,
            currentEdit,
            pending,
            account,
            graderAccount,
            graderCatalog,
            graderClaimed,
            graded,
            createAccount));

    Map<Class<?>, Object> boundaryInstantiations = new HashMap<>();

    public static void main(String[] args) {
        Stripe.apiKey = System.getenv("STRIPE_SECRET_KEY");
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