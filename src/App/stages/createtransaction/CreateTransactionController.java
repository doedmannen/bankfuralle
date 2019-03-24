package App.stages.createtransaction;

import App.BankMain;
import App.models.Account;
import App.stages.createaccount.CreateAccountHelper;
import App.stages.home.HomeHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CreateTransactionController {

    @FXML
    ComboBox fromAccountCombo;

    @FXML
    ComboBox toAccountCombo;

    @FXML
    TextField externalAccount;

    @FXML
    Button createTransaction;

    @FXML
    Button abortTransaction;

    @FXML
    TextField transactionDate;

    @FXML
    TextField transactionMessage;

    ObservableList<String> allAcounts;

    @FXML
    private void initialize(){
        BankMain.stage.setTitle(BankMain.bankTitle + "Ny transaktion");
        setAllAccounts();
        setAccountCombos();

    }

    private void setAllAccounts(){
        allAcounts = FXCollections.observableArrayList((List<String>) CreateTransactionHelper.getMyAccounts()
                .stream()
                .map(account -> account.toString())
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    private void setAccountCombos(){
        // Set the from account combo
        fromAccountCombo.setItems(allAcounts);

        ObservableList<String> toAccounts = allAcounts.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
        toAccounts.add("Överföring till externt konto (fylls i manuellt nedan)");
        toAccountCombo.setItems(toAccounts);
    }

}
