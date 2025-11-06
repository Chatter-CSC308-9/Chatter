package main.boundaries.screens;

import com.stripe.exception.StripeException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.boundaries.apis.interfaces.Navigator;
import main.controllers.AcceptPaymentController;
import main.controllers.DisplayUsernameController;
import main.controllers.LogoutController;

public class Account extends AbstractAccount implements Navigator {

    @FXML
    public Button addFundsButton;

    AcceptPaymentController acceptPaymentController;

    public Account(LogoutController logoutController, DisplayUsernameController displayUsernameController, AcceptPaymentController acceptPaymentController) {
        super(logoutController, displayUsernameController);
        this.acceptPaymentController = acceptPaymentController;
        super.addController(this.acceptPaymentController);
    }

    public void handleAddFundsButtonClick() throws StripeException {
        this.acceptPaymentController.payServer(500, "usd");
    }

}
