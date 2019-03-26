package App.stages.editaccount;

import App.BankMain;
import App.models.Account;
import App.stages.StageHandler;
import javafx.application.Platform;

public class EditAccountHelper {
    static Account account;
    public static void setAccount(String accountNumber) {
        account = (Account) BankMain.sqlHelper.getObjectFromQuery("getAccount", accountNumber);
    }

    static void updateAccount(String name, String type){
        BankMain.sqlHelper.runQueryWithData("editAccount", name, parseType(type), account.getNumber());
    }

    private static String parseType(String typeText){
        String returnType;
        switch (typeText){
            case "Kortkonto":
                returnType = "CARD";
                break;
            case "Lönekonto":
                returnType = "SALARY";
                break;
            default:
                returnType = "SAVING";
                break;
        }
        return returnType;
    }

}
