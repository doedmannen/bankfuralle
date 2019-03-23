package App.stages.login;

import App.BankMain;
import App.helpers.generators.BankGenerator;
import App.models.Respons;
import App.stages.StageHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class LoginController {

    @FXML
    TextField fieldSSN;

    @FXML
    PasswordField fieldPassword;

    @FXML
    Label errorLabel;

    @FXML
    private void initialize(){

    }

    @FXML
    private void loginButtonPressed() throws Exception{
        String SSN = fieldSSN.getText();
        String password = fieldPassword.getText();
        fieldSSN.clear();
        fieldPassword.clear();
        fieldSSN.requestFocus();
        BankMain.customer = LoginHelper.getUserFromDatabase(SSN, password);
        if(BankMain.customer != null){
            BankMain.bankTitle = BankMain.bankTitle.concat("Inloggad som: " + BankMain.customer.getName().concat(" - "));
            StageHandler.switchSceneTo(this, "createaccount");
        } else {
            errorLabel.setText("Inloggningen misslyckades. Vänligen kontrollera dina uppgifter och försök igen. ");
        }
    }

    @FXML
    private void checkLoginKey(KeyEvent e) throws Exception{
        if(e.getCode().toString().equals("ENTER"))
            loginButtonPressed();
    }



}
