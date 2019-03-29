package App;

import App.helpers.database.SQLHelper;
import App.models.Card;
import App.models.Customer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BankMain extends Application {

    public static Stage stage;
    public static SQLHelper sqlHelper;
    public static Customer customer;
    public static Card card;
    public static String bankTitle;

    @Override
    public void start(Stage primaryStage) throws Exception{
        // Setup helper for SQL and initialize queries needed for the banking business
        sqlHelper = new SQLHelper("localhost", "Europe/Stockholm", "bankfuralle", "test", "password123");
        ApplicationQueryInitializer.runStart(sqlHelper);

        // Set the stage
        bankTitle = "Schwedenbank - ";
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/App/stages/login/login.fxml"));
        primaryStage.setTitle(bankTitle + "logga in");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}