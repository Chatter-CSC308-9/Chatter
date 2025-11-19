package main.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import main.adapters.CredentialsRepository;
import main.adapters.UserHydratinator;
import main.entities.User;
import main.entities.UserCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class CreateAccountController implements Controller {

    private static final Logger logger = LoggerFactory.getLogger(CreateAccountController.class);

    @SuppressWarnings("java:S2245") // Non-security randomness for uid generation
    private static final Random random = new Random();

    // returns true if async steps required, false otherwise
    public Optional<Long> createAccount(String username, String email, String password, boolean isGrader) {
        String userId = generateUserId();
        while (!verifyValidUserId(userId)) userId = generateUserId();
        var user = new User();
        user.username = username;
        user.userID = Long.parseLong(userId,16);
        user.passwordplaintext = password;
        user.isGrader = isGrader;
        user.email = email;
        user.emailIsHidden = false;
        user.projects = new String[0];
        user.completedProjects = new String[0];
        user.numProjects = 0;
        (new UserHydratinator()).setUser(user);
        var userCredentials = new UserCredentials(username, password, Long.parseLong(userId,16), isGrader);
        (new CredentialsRepository()).addUserCredential(userCredentials);

        Optional<Long> userIdIfGrader = Optional.empty();
        if (isGrader) userIdIfGrader = Optional.of(user.userID);

        return userIdIfGrader;
    }

    public String stripeOnboard(long userId) throws StripeException {
            // stripe onboarding goes here
            logger.debug("start");

            // create account
            var accountCreateParams = AccountCreateParams.builder()
                    .setType(AccountCreateParams.Type.EXPRESS)
                    .setCountry("IT")
                    .build();
            Account account = Account.create(accountCreateParams);

            logger.debug("account creation success");
            // save info to account
            var userHydratinator = new UserHydratinator();
            var user = userHydratinator.getUser(userId);
            user.stripeId = account.getId();
            userHydratinator.setUser(user);

            logger.debug("info save success");

            // generate links
            var link = AccountLink.create(
                    AccountLinkCreateParams.builder()
                            .setAccount(user.stripeId)
                            .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                            .setReturnUrl("https://www.calpoly.edu/")
                            .setRefreshUrl("https://www.calpoly.edu/")
                            .build()
            );
            logger.debug("link gen success");
            return link.getUrl();

    }

    public boolean checkOnboardingComplete(long userId) throws StripeException {
        var user = (new UserHydratinator()).getUser(userId);

        if (user.stripeId == null || user.stripeId.isEmpty()) {
            return false;
        }

        Account account = Account.retrieve(user.stripeId);
        return Boolean.TRUE.equals(account.getChargesEnabled());
    }

    private String generateUserId() {
        long userId = 0;
        for (int i = 0; i < 8; i++) {
            userId = userId * 16 + random.nextInt(16);
        }
        StringBuilder userIdAsString = new StringBuilder(Long.toHexString(userId));
        while (userIdAsString.length()<8) {
            userIdAsString.insert(0, "0");
        }
        return userIdAsString.toString();
    }
    private boolean verifyValidUserId(String userId) {
        long userIdAsLong = Long.parseLong(userId,16);
        List<UserCredentials> userCredentials = (new CredentialsRepository()).getAllUserCredentials();
        for (var userCredential : userCredentials) {
            if (userCredential.userID== userIdAsLong) return false;
        }
        return true;
    }
}
