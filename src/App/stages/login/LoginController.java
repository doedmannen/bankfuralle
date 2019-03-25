package App.stages.login;

import App.BankMain;
import App.helpers.generators.BankGenerator;
import App.helpers.string.Replacer;
import App.models.Respons;
import App.stages.StageHandler;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.Calendar;

public class LoginController {

    @FXML
    TextField fieldSSN;

    @FXML
    PasswordField fieldPassword;

    @FXML
    Label errorLabel;

    @FXML
    private void initialize(){
        fieldSSN.textProperty().addListener((observable, oldValue, newValue) ->
            ((StringProperty)observable).setValue(Replacer.numberTrimmer(newValue,10)));
        checkCostumerRegion();
    }

    private void checkCostumerRegion(){
        Platform.runLater(() -> {
            fieldSSN.requestFocus();
            if(!Calendar.getInstance().getTimeZone().getID().equals("Europe/Berlin"))
                StageHandler.switchSceneTo(this, "illegalregion");
        });
    }

    @FXML
    private void loginButtonPressed(){
        errorLabel.setText("");
        String SSN = fieldSSN.getText();
        String password = fieldPassword.getText();
        fieldSSN.clear();
        fieldPassword.clear();
        fieldSSN.requestFocus();
        BankMain.customer = LoginHelper.getUserFromDatabase(SSN, password);
        if(BankMain.customer != null){
            BankMain.bankTitle = BankMain.bankTitle.concat("Inloggad som: " + BankMain.customer.getName().concat(" - "));
            StageHandler.switchSceneTo(this, "home");
        } else {
            errorLabel.setText("Inloggningen misslyckades. Vänligen kontrollera dina uppgifter och försök igen. ");
        }
    }

    @FXML
    private void checkLoginKey(KeyEvent e){
        if(e.getCode().toString().equals("ENTER"))
            loginButtonPressed();
    }



}
