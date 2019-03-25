package App.stages.home;

import App.BankMain;
import App.models.Account;
import App.models.Transaction;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.util.List;

public class HomeController {

    @FXML
    Label helloWorld;

    @FXML
    ListView accountList;

    @FXML
    ListView transactionList;

    // Menu buttons
    @FXML
    Button menuOpenAccount;
    @FXML
    Button menuCreateTransaction;
    @FXML
    Button menuCreatePayment;
    @FXML
    Button menuCreateAutogiro;
    @FXML
    Button menuEditAccount;
    @FXML
    Button menuViewAccount;
    @FXML
    Button menuDeleteAccount;
    @FXML
    Button menuButtonChangeMaxCharge;

    // Simulations
    @FXML
    Button simulationBuyHorse;
    @FXML
    Button simulationBuyFood;
    @FXML
    Button simulationPayday;
    @FXML
    Button simulationAutogiro;

    @FXML
    private void initialize(){
        BankMain.stage.setTitle(BankMain.bankTitle + "Mina konton");
        setAccountList();
        setTransactionList();
        preSelectAccount();
    }

    private void preSelectAccount(){
        Platform.runLater(()->accountList.getSelectionModel().selectFirst());
    }

    private void setAccountList(){
        ObservableList<Account> accounts = FXCollections.observableArrayList((List<Account>) HomeHelper.getAccounts());
        accountList.setItems(accounts);
    }
    private void setTransactionList(){
        ObservableList<Transaction> transactions = FXCollections.observableArrayList((List<Transaction>) HomeHelper.getLatestTransactions());
        transactionList.setItems(transactions);
    }





}
