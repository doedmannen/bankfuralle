package App;

import App.helpers.database.SQLHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class BankMain extends Application {

    public static Stage stage;
    public static SQLHelper sqlHelper;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Setup helper for SQL and initialize queries needed for the banking business
        sqlHelper = new SQLHelper("localhost", "Europe/Stockholm", "bankfuralle", "test", "password123");
        ApplicationQueryInitializer.runStart(sqlHelper);

        // Set the stage
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/App/stages/login/login.fxml"));
        primaryStage.setTitle("Schwedenbank - Ein Bank für alle");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}