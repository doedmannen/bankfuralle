package App.stages.createautogiro;

import App.BankMain;
import App.helpers.string.Replacer;
import App.models.Account;
import App.stages.StageHandler;
import App.stages.confirmation.ConfirmationController;
import App.stages.createtransaction.CreateTransactionHelper;
import App.stages.home.HomeHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.List;


public class CreateAutogiroController {

    @FXML
    Button confirmButton;

    @FXML
    Button cancelButton;

    @FXML
    TextField toAccount;

    @FXML
    ComboBox fromAccount;

    @FXML
    Label errorLabel;

    @FXML
    private void initialize(){
        confirmButton.setOnAction(e -> confirmAutogiro());
        cancelButton.setOnAction(e-> Platform.runLater(()-> StageHandler.switchSceneTo(this, "home")));
        setAccountList();
    }

    private void setAccountList(){
        ObservableList<Account> accounts = FXCollections.observableArrayList((List<Account>) CreateAutogiroHelper.getAccounts());
        fromAccount.setItems(accounts);
        fromAccount.getSelectionModel().select(0);
    }

    private void confirmAutogiro(){
        errorLabel.setText("");
        String from = Replacer.parseAccountNumber(fromAccount.getSelectionModel().getSelectedItem().toString());
        String to = toAccount.getText();
        if(to.length() > 1){
            BankMain.sqlHelper.runQueryWithData("createAutogiro", from, to);
            ConfirmationController.setMessage(String.format("Autogiro från konto %s till konto %s är nu aktivt", from, to));
            Platform.runLater(()->{
                StageHandler.switchSceneTo(this, "confirmation");
            });
        } else {
            errorLabel.setText("Ogiltig längd på BG/PG-nummer, måste vara minst 2 siffror");
        }
    }
}
