package tests;

import main.boundaries.screens.Pay;
import main.controllers.AcceptPaymentController;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class PayBoundaryTests {
    @Test
    public void centsFromStringsTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var acceptPaymentController = new AcceptPaymentController();
        var pay = new Pay(acceptPaymentController);
        Method centsFromString = Pay.class.getDeclaredMethod("centsFromString", String.class);
        centsFromString.setAccessible(true);

        // Test 1
        String input1 = "3.80";
        Optional<Integer> expected1 = Optional.of(380);
        Optional<?> actual1 = centsFromStringObjectToOptional(centsFromString, pay, input1);
        assertEquals(expected1, actual1);

        // Test 2
        String input2 = "24533.00";
        Optional<Integer> expected2 = Optional.of(2453300);
        Optional<?> actual2 = centsFromStringObjectToOptional(centsFromString, pay, input2);
        assertEquals(expected2, actual2);

        // Test 3
        String input3 = ".32";
        Optional<Integer> expected3 = Optional.empty();
        Optional<?> actual3 = centsFromStringObjectToOptional(centsFromString, pay, input3);
        assertEquals(expected3, actual3);

        // Test 4
        String input4 = "24.2";
        Optional<Integer> expected4 = Optional.empty();
        Optional<?> actual4 = centsFromStringObjectToOptional(centsFromString, pay, input4);
        assertEquals(expected4, actual4);

        // Test 5
        String input5 = "2942";
        Optional<Integer> expected5 = Optional.empty();
        Optional<?> actual5 = centsFromStringObjectToOptional(centsFromString, pay, input5);
        assertEquals(expected5, actual5);

        // Test 6
        String input6 = "$34.87";
        Optional<Integer> expected6 = Optional.empty();
        Optional<?> actual6 = centsFromStringObjectToOptional(centsFromString, pay, input6);
        assertEquals(expected6, actual6);

    }

    public Optional<?> centsFromStringObjectToOptional(Method centsFromString, Pay pay, String input) throws InvocationTargetException, IllegalAccessException {
        Optional<?> actual;
        Object actual1asObject = centsFromString.invoke(pay, input);
        if (actual1asObject instanceof Optional<?> actual1AsOptional) {
            actual = actual1AsOptional;
        } else {
            actual = Optional.empty();
            fail("centsFromString returned a non-Optional");
        }
        return actual;
    }
}
