package App.stages.confirmation;

import App.stages.StageHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConfirmationController {
    private static String message = "";

    @FXML
    Label messageLabel;

    @FXML
    Button goHomeButton;

    @FXML
    private void initialize(){
        messageLabel.setText(message);
        goHomeButton.setOnAction(e -> Platform.runLater(()-> StageHandler.switchSceneTo(this, "home")));
    }

    public static void setMessage(String msg){
        message = msg;
    }
}
