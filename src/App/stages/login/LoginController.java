package App.stages.login;

import App.BankMain;
import com.sun.tools.javac.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    TextField fieldSSN;

    @FXML
    TextField fieldPassword;

    @FXML
    private void initialize(){
        System.out.println("init");
    }

    @FXML
    private void loginButtonPressed(){
        String SSN = fieldSSN.getText();
        String password = fieldPassword.getText();
        BankMain.customer = LoginHelper.getUserFromDatabase(SSN, password);
        if(BankMain.customer != null){
            try{
                switchSceneToLoggedIn();
            }catch (Exception e){}
        } else {

        }
    }

    private void switchSceneToLoggedIn() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/stages/home/home.fxml"));
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene(fxmlInstance, 800,600);
        BankMain.stage.setScene(scene);
    }

}
