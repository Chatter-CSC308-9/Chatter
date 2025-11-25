package tests;

import main.controllers.AcceptPaymentController;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AcceptPaymentControllerTest {
    @Test
    public void checkIfPaidSuccessTest() {
        var acceptPaymentController = new AcceptPaymentController();
        assertTrue(acceptPaymentController.checkIfPaid("D1E6E4E93"));
    }

    @Test
    public void checkIfPaidFailureTest() {
        var acceptPaymentController = new AcceptPaymentController();
        assertFalse(acceptPaymentController.checkIfPaid("D1E6E4E94"));
    }
}
