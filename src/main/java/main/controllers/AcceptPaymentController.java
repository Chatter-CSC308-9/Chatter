package main.controllers;

import main.adapters.ProjectHydratinator;
import main.adapters.UserHydratinator;
import main.controllers.apis.hooks.GetUserAPI;
import main.controllers.apis.interfaces.NeedsUser;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.net.URI;

public class AcceptPaymentController implements Controller, NeedsUser {

    private static final Logger logger = LoggerFactory.getLogger(AcceptPaymentController.class);

    GetUserAPI getUserAPI;

    public String pay(String projectDirectory) {
        try {
            var projectHydratinator = new ProjectHydratinator();
            var project = projectHydratinator.getProject(projectDirectory);
            var grader = (new UserHydratinator()).getUser(project.graderID);

            if (grader.stripeId == null || grader.stripeId.isEmpty()) {
                logger.error("Grader has not completed Stripe onboarding");
                return null;
            }

            int costInCents = getCostInCents(projectDirectory);
            int applicationFee = (int) (costInCents * 0.10); // 10% platform fee

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl("https://example.com/payment-success")
                    .setCancelUrl("https://example.com/payment-cancel")
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency("eur")
                                                    .setUnitAmount((long) costInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName("Graded Project: " + project.projectTitle)
                                                                    .setDescription("Feedback for project")
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .setQuantity(1L)
                                    .build()
                    )
                    .setPaymentIntentData(
                            SessionCreateParams.PaymentIntentData.builder()
                                    .setApplicationFeeAmount((long) applicationFee)
                                    .setTransferData(
                                            SessionCreateParams.PaymentIntentData.TransferData.builder()
                                                    .setDestination(grader.stripeId)
                                                    .build()
                                    )
                                    .putMetadata("project_dir", projectDirectory)
                                    .putMetadata("learner_id", String.valueOf(getUserAPI.getUserID()))
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            logger.info("Checkout session created: {}", session.getId());

            Desktop.getDesktop().browse(new URI(session.getUrl()));
            return session.getId();

        } catch (StripeException e) {
            logger.error("Stripe payment error", e);
            return null;
        } catch (Exception e) {
            logger.error("Error opening browser", e);
            return null;
        }
    }

    public boolean verifyPayment(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            return "complete".equals(session.getStatus()) && "paid".equals(session.getPaymentStatus());
        } catch (StripeException e) {
            logger.error("Error verifying payment", e);
            return false;
        }
    }

    public int getCostInCents(String projDir) {
        var projectHydratinator = new ProjectHydratinator();
        return projectHydratinator.getProject(projDir).getCostInCents();
    }

    @Override
    public void setGetUserAPI(GetUserAPI getUserAPI) {
        this.getUserAPI = getUserAPI;
    }
}
