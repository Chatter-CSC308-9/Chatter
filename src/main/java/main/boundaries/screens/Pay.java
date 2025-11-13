package main.boundaries.screens;

import com.stripe.exception.StripeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.boundaries.Boundary;
import main.controllers.AcceptPaymentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class Pay extends Boundary {

    private static final Logger logger = LoggerFactory.getLogger(Pay.class);

    @FXML
    public Button addFundsButton;
    @FXML
    public TextField nameOnCardField;
    @FXML
    public Label greetingLabel;
    @FXML
    public TextField cardNumberField;
    @FXML
    public TextField expiryDateField;
    @FXML
    public TextField cvvField;
    @FXML
    public TextField billingAddressLine1Field;
    @FXML
    public TextField billingAddressLine2Field;
    @FXML
    public Label wordsPerTextField;
    @FXML
    public Label wordsPerHandwritten;
    @FXML
    public Label timePerSpoken;
    @FXML
    public TextField costField;

    AcceptPaymentController acceptPaymentController;

    public Pay(AcceptPaymentController acceptPaymentController) {
        this.acceptPaymentController = acceptPaymentController;
        addController(this.acceptPaymentController);
    }

    @FXML
    private void initialize() {
        costField.textProperty().addListener((_, _, newValue) ->
                onTextChanged(newValue));
    }

    private void onTextChanged(String newValue) {
        Optional<Integer> newValueCents = centsFromString(newValue);
        if (newValueCents.isPresent() && newValueCents.get() > 0) {
            updateUI(newValueCents.get());
        }
    }

    private void updateUI(int cents) {
        this.wordsPerTextField.setText(cents + " words in a text file");
        this.wordsPerHandwritten.setText(roundHandwrittenCents(cents) + " handwritten words");
        this.timePerSpoken.setText(roundSpokenText(cents) + " of spoken work");
    }

    private int roundHandwrittenCents(int cents) {
        if (cents % 2 == 0) return cents / 2;
        return cents/2+1;
    }

    private String roundSpokenText(int cents) {
        int minutes = cents / 50;
        return switch (minutes) {
            case 0 -> "0 minutes";
            case 1 -> "Up to 1 minute";
            default -> "Upo to " + minutes + " minutes";
        };
    }

    private Optional<Integer> centsFromString(String value) {
        if (value.matches("^\\d+\\.\\d{2}$"))
            return Optional.of((int) (100 * Double.parseDouble(value)));
        return Optional.empty();
    }

    @FXML
    public void handlePayButtonClick(ActionEvent actionEvent) throws StripeException {
        Optional<Integer> newValueCents = centsFromString(this.costField.getText());
        if (newValueCents.isPresent() && newValueCents.get() > 0) {
            logger.debug("PAYMENT INITIATED {}", newValueCents.get());
            acceptPaymentController.payServer(newValueCents.get(), "usd");
        }
    }
}
