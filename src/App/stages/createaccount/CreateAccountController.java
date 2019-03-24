package App.stages.createaccount;

import App.BankMain;
import App.helpers.generators.BankGenerator;
import App.helpers.string.Replacer;
import App.stages.StageHandler;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class CreateAccountController {

    @FXML
    Button createAccountButton;

    @FXML
    Button abortCreateAccount;

    @FXML
    TextField accountName;

    @FXML
    ComboBox accountType;

    @FXML
    private void initialize(){
        BankMain.stage.setTitle(BankMain.bankTitle + "Öppna nytt konto");
        createTextForComboBox();
        createAccountButton.setOnAction(e -> createAccount());
        abortCreateAccount.setOnAction(e -> abort());
        accountName.textProperty().addListener((observable, oldValue, newValue) ->
            ((StringProperty)observable).setValue(Replacer.superTrimFixed(newValue, 10)));
    }

    @FXML
    private void createAccount(){
        String name = accountName.getText().equals("") ? accountType.getValue().toString() : accountName.getText();
        String type = accountType.getValue().toString();
        CreateAccountHelper.createNewAccount(name, type);
    }

    @FXML
    private void abort(){
        StageHandler.switchSceneTo(this, "home");
    }


    private void createTextForComboBox(){
        ObservableList<String> options = FXCollections.observableArrayList(
                "Kortkonto",
                "Sparkonto",
                "Lönekonto"
        );
        accountType.setItems(options);
        accountType.setValue("Sparkonto");
    }
}
