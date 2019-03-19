package App.stages.login;

import App.BankMain;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
        System.out.println("init");
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
            switchSceneToHome();
        } else {
            errorLabel.setText("Inloggningen misslyckades. Vänligen kontrollera dina uppgifter och försök igen. ");
        }
    }

    @FXML
    private void checkLoginKey(KeyEvent e) throws Exception{
        if(e.getCode().toString().equals("ENTER"))
            loginButtonPressed();
    }

    private void switchSceneToHome() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/stages/home/home.fxml"));
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene(fxmlInstance, 800,600);
        BankMain.stage.setScene(scene);
    }

}
