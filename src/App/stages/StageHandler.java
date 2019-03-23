package App.stages;

import App.BankMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class StageHandler {
    private StageHandler(){}

    public static void switchSceneTo(Object s, String stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(s.getClass().getResource("/App/stages/"+stage+"/"+stage+".fxml"));
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene(fxmlInstance, 800,600);
        BankMain.stage.setScene(scene);
    }
}
