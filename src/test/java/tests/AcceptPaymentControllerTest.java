package tests;

import main.controllers.AcceptPaymentController;
import main.controllers.apis.UserIdApiController;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AcceptPaymentControllerTest {
    @Test
    public void checkIfPaidTest() {
        var getUserApi = new UserIdApiController();
        var acceptPaymentController = new AcceptPaymentController();
        acceptPaymentController.setGetUserAPI(getUserApi);
        assertTrue(acceptPaymentController.checkIfPaid("D1E6E4E93"));
        assertFalse(acceptPaymentController.checkIfPaid("D1E6E4E94"));
    }
}
