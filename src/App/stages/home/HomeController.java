package App.stages.home;

import App.BankMain;
import App.helpers.string.Replacer;
import App.models.Account;
import App.models.Transaction;
import App.stages.StageHandler;
import App.stages.createaccount.CreateAccountHelper;
import App.stages.editaccount.EditAccountHelper;
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

    @FXML
    Label errorLabelDelete;

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
        menuCreateAutogiro.setOnAction(event -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "createautogiro")));
        menuCreateTransaction.setOnAction(event -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "createtransaction")));
        menuCreatePayment.setOnAction(event -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "createpayment")));
        menuDeleteAccount.setOnAction(event -> deleteSelectedAccount());
        menuEditAccount.setOnAction(event -> editSelectedAccount());
        menuViewAccount.setOnAction(event -> viewSelectedAccount());
    }

    private void editSelectedAccount(){
        String accountNumber = Replacer.parseAccountNumber(accountList.getSelectionModel().getSelectedItem().toString());
        EditAccountHelper.setAccountNumber(accountNumber);
        Platform.runLater(()-> StageHandler.switchSceneTo(this, "editaccount"));
    }

    private void viewSelectedAccount(){
        String accountNumber = Replacer.parseAccountNumber(accountList.getSelectionModel().getSelectedItem().toString());
        EditAccountHelper.setAccountNumber(accountNumber);
        Platform.runLater(()-> StageHandler.switchSceneTo(this, "viewaccount"));
    }

    private void deleteSelectedAccount(){
        String accountNumber = Replacer.parseAccountNumber(accountList.getSelectionModel().getSelectedItem().toString());
        if(HomeHelper.accountHasBalance(accountNumber)) {
            errorLabelDelete.setText("Konton med balans kan ej raderas ");
        }else if(HomeHelper.isLastAccount()){
            errorLabelDelete.setText("Sista kontot kan ej raderas ");
        } else {

        }
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
