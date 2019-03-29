package App.stages.home;

import App.BankMain;
import App.helpers.string.Replacer;
import App.models.Account;
import App.models.Transaction;
import App.stages.StageHandler;
import App.stages.confirmation.ConfirmationController;
import App.stages.createtransaction.CreateTransactionHelper;
import App.stages.editaccount.EditAccountHelper;
import App.stages.viewaccount.ViewAccountHelper;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    @FXML
    TextField fieldMaxCard;

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
        setCardMax();
        menuOpenAccount.setOnAction(event -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "createaccount")));
        menuCreateAutogiro.setOnAction(event -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "createautogiro")));
        menuCreateTransaction.setOnAction(event -> {
            CreateTransactionHelper.paymentState = false;
            Platform.runLater(()-> StageHandler.switchSceneTo(this, "createtransaction"));
        });
        menuCreatePayment.setOnAction(event -> {
            CreateTransactionHelper.paymentState = true;
            Platform.runLater(()-> StageHandler.switchSceneTo(this, "createtransaction"));
        });
        menuButtonChangeMaxCharge.setOnAction(event -> updateCardMax());
        menuDeleteAccount.setOnAction(event -> deleteSelectedAccount());
        menuEditAccount.setOnAction(event -> editSelectedAccount());
        menuViewAccount.setOnAction(event -> viewSelectedAccount());
        fieldMaxCard.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(()->{
                ((StringProperty)observable).setValue(Replacer.moneyTrim(newValue));
                for(int i = 0; i < newValue.length(); ++i)
                    fieldMaxCard.forward();
            });
        });
        simulationBuyFood.setOnAction(event -> {
            HomeSimulator.runCardSimulation(559.95, "ICA Maxi");
            Platform.runLater(()-> StageHandler.switchSceneTo(this, "confirmation"));
        });
        simulationBuyHorse.setOnAction(event ->{
            HomeSimulator.runCardSimulation(12000, "Elgiganten");
            Platform.runLater(()-> StageHandler.switchSceneTo(this, "confirmation"));
        });
        simulationPayday.setOnAction(event -> {
            HomeSimulator.runPaydaySimulation(25000);
            Platform.runLater(()-> StageHandler.switchSceneTo(this, "confirmation"));
        });
        simulationAutogiro.setOnAction(event -> {
            HomeSimulator.runAutogiroSimulation();
            Platform.runLater(()-> StageHandler.switchSceneTo(this, "confirmation"));
        });
    }

    private void updateCardMax(){
        HomeHelper.updateMax(fieldMaxCard.getText());
        HomeHelper.reloadUserFromDatabase();
        ConfirmationController.setMessage("Ditt maxbelopp för kortköp är nu ändrat");
        Platform.runLater(()-> StageHandler.switchSceneTo(this, "confirmation"));
    }

    private void setCardMax(){
        fieldMaxCard.setText(Replacer.moneyTrim(""+BankMain.card.getLimit()+"0"));
    }

    private void editSelectedAccount(){
        String accountNumber = Replacer.parseAccountNumber(accountList.getSelectionModel().getSelectedItem().toString());
        EditAccountHelper.setAccount(accountNumber);
        Platform.runLater(()-> StageHandler.switchSceneTo(this, "editaccount"));
    }

    private void viewSelectedAccount(){
        String accountNumber = Replacer.parseAccountNumber(accountList.getSelectionModel().getSelectedItem().toString());
        ViewAccountHelper.setAccountNumber(accountNumber);
        Platform.runLater(()-> StageHandler.switchSceneTo(this, "viewaccount"));
    }

    private void deleteSelectedAccount(){
        String accountNumber = Replacer.parseAccountNumber(accountList.getSelectionModel().getSelectedItem().toString());
        if(HomeHelper.accountHasBalance(accountNumber)) {
            errorLabelDelete.setText("Konton med balans kan ej raderas ");
        }else if(HomeHelper.isLastAccount()){
            errorLabelDelete.setText("Sista kontot kan ej raderas ");
        } else {
            BankMain.sqlHelper.runQueryWithData("deleteAccount", accountNumber);
            Platform.runLater(()->StageHandler.switchSceneTo(this, "home"));
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
