package App.stages.editaccount;

import App.helpers.string.Replacer;
import App.stages.StageHandler;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class EditAccountController {

    @FXML
    Label labelNumber;

    @FXML
    TextField fieldName;

    @FXML
    ComboBox comboType;

    @FXML
    Button buttonAbort;

    @FXML
    Button buttonConfirm;

    @FXML
    private void initialize(){
        setAccountInfo();
        buttonAbort.setOnAction(event -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "home")));
        buttonConfirm.setOnAction(event -> confirmChanges());
        fieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            ((StringProperty)observable).setValue(Replacer.superTrimFixed(newValue, 20));
        });
    }

    private void setAccountInfo(){
        ObservableList<String> options = FXCollections.observableArrayList(
                "Kortkonto",
                "Sparkonto",
                "Lönekonto"
        );
        comboType.setItems(options);
        comboType.setValue(EditAccountHelper.account.getType());
        labelNumber.setText(EditAccountHelper.account.getNumber());
        fieldName.setText(EditAccountHelper.account.getName());
    }

    private void confirmChanges(){
        EditAccountHelper.updateAccount(fieldName.getText(), comboType.getValue().toString());
        Platform.runLater(()-> StageHandler.switchSceneTo(this, "home"));
    }
}
