package App.stages.home;

import App.ApplicationQueryInitializer;
import App.BankMain;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class HomeController {

    @FXML
    Label helloWorld;

    @FXML
    private void initialize(){
        BankMain.stage.setTitle(BankMain.bankTitle + "Mitt konto");
        System.out.println(BankMain.customer.toString());
        HomeHelper.getAccounts();
        HomeHelper.accountList.forEach(System.out::println);
    }





}
