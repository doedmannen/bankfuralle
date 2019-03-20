package App.stages;

import App.BankMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class StageHandler {
    private StageHandler(){}

    public static void switchSceneTo(Object stage,String path) throws Exception{
        FXMLLoader loader = new FXMLLoader(stage.getClass().getResource("/App/stages/"+path));
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene(fxmlInstance, 800,600);
        BankMain.stage.setScene(scene);
    }
}
