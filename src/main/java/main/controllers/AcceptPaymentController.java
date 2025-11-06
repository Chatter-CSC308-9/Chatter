package main.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcceptPaymentController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(AcceptPaymentController.class);

    public void payServer(long cents, String currency) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(cents)
                .setCurrency(currency)
                .addPaymentMethodType("card")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        logger.debug("Client secret: {}", paymentIntent.getClientSecret());
    }
}
