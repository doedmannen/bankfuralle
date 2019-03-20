package App.stages.home;

import App.BankMain;
import App.models.Account;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.util.List;

public class HomeController {

    @FXML
    Label helloWorld;

    @FXML
    ListView accountList;

    @FXML
    private void initialize(){
        BankMain.stage.setTitle(BankMain.bankTitle + "Mitt konto");
        setAccountList();
    }

    private void setAccountList(){
        ObservableList<Account> accounts = FXCollections.observableArrayList((List<Account>) HomeHelper.getAccounts());
        accountList.setItems(accounts);
    }





}
