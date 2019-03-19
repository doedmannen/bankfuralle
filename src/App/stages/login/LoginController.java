package App.stages.login;

import App.BankMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class LoginController {

    @FXML
    Button loginButton;

    @FXML
    private void initialize(){
        System.out.println("init");
    }

    @FXML
    private void loginButtonPressed(){

    }

    private void switchSceneToLoggedIn() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/App/stages/home/home.fxml"));
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene(fxmlInstance, 800,600);
        BankMain.stage.setScene(scene);
    }

}
