package tests;

import main.controllers.AcceptPaymentController;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AcceptPaymentControllerTest {
    @Test
    public void checkIfPaidTest() {
        var acceptPaymentController = new AcceptPaymentController();
        assertTrue(acceptPaymentController.checkIfPaid("D1E6E4E93"));
        assertFalse(acceptPaymentController.checkIfPaid("D1E6E4E94"));
    }
}
