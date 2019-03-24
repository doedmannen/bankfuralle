package App.stages.createtransaction;

import App.BankMain;
import App.helpers.generators.BankGenerator;
import App.helpers.string.Replacer;
import App.models.Account;
import App.stages.StageHandler;
import App.stages.createaccount.CreateAccountHelper;
import App.stages.home.HomeHelper;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
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

    @FXML
    Label errorLabelAccount;

    @FXML
    Label errorLabelDate;

    ObservableList<String> allAcounts;


    @FXML
    private void initialize(){
        BankMain.stage.setTitle(BankMain.bankTitle + "Ny transaktion");
        setAllAccounts();
        setAccountCombos();
        transactionDate.setText(LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

        externalAccount.textProperty().addListener((observable, oldValue, newValue) ->
                ((StringProperty)observable).setValue(Replacer.numberTrimmer(newValue,14)));
        transactionDate.textProperty().addListener((observable, oldValue, newValue) ->
                ((StringProperty)observable).setValue(Replacer.numberTrimmer(newValue,8)));
        transactionMessage.textProperty().addListener((observable, oldValue, newValue) ->
                ((StringProperty)observable).setValue(Replacer.superTrimFixed(newValue,10)));

        abortTransaction.setOnAction(event -> goHome());
        createTransaction.setOnAction(event -> performTransaction());
    }

    private void goHome(){
        StageHandler.switchSceneTo(this, "home");
    }

    private void performTransaction(){
        if(validateInputFields()){

        }
    }

    private void clearErrors(){
        Platform.runLater(()->{
            errorLabelDate.setText("");
            errorLabelAccount.setText("");
        });

    }

    private boolean validateInputFields(){
        boolean valid = true;
        clearErrors();

        if(toAccountCombo.getValue().toString().equals("Överföring till externt konto (fylls i manuellt nedan)")
                && externalAccount.getText().length() < 11){
            Platform.runLater(()->errorLabelAccount.setText("Externt konto måste vara 11-14 siffor enligt Schwedenbanks standard"));
            valid = false;
        }

        if(transactionDate.getText().length() > 0){
            try{
                LocalDate.parse(transactionDate.getText(), DateTimeFormatter.BASIC_ISO_DATE);
            }catch (Exception e){
                valid = false;
                Platform.runLater(()->errorLabelDate.setText("Felaktigt formulerat eller ogiltigt datum, måste anges som ex 20190101"));
            }
        }

        return valid;
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
