package App.stages.createaccount;

import App.helpers.generators.BankGenerator;
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
        createTextForComboBox();
    }

    private void createTextForComboBox(){
        ObservableList<String> options = FXCollections.observableArrayList(
                "Kortkonto",
                "Sparkonto",
                "Lönekonto"
        );
        accountType.setItems(options);
    }
}
