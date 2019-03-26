package App.stages.viewaccount;

import App.stages.StageHandler;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ViewAccountController {

    @FXML
    Label labelName;

    @FXML
    Label labelType;

    @FXML
    Label labelNumber;

    @FXML
    Label labelBalance;

    @FXML
    Button buttonGoHome;

    @FXML
    Button buttonLoadMore;

    @FXML
    ListView listTransactions;

    @FXML
    private void initialize(){
        ViewAccountHelper.initLoader();
        listTransactions.setItems(FXCollections.observableArrayList(ViewAccountHelper.transactionList));
        setAccountInfo();
        buttonGoHome.setOnAction(event -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "home")));
        buttonLoadMore.setOnAction(event -> loadMore());
    }

    private void setAccountInfo(){
        labelNumber.setText(ViewAccountHelper.currentAccount.getNumber());
        labelName.setText(ViewAccountHelper.currentAccount.getName());
        labelBalance.setText(String.format("%.2f",ViewAccountHelper.currentAccount.getBalance()));
        labelType.setText(ViewAccountHelper.currentAccount.getType());
    }

    private void loadMore(){
        ViewAccountHelper.loadMoreTransactions();
        listTransactions.setItems(FXCollections.observableArrayList(ViewAccountHelper.transactionList));
    }

}
