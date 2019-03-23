package App.stages.createaccount;

import App.BankMain;
import App.helpers.generators.BankGenerator;

public class CreateAccountHelper {
    static void createNewAccount(String name, String type){
        BankMain.sqlHelper.runQueryWithData("openNewAccount",
                BankMain.customer.getId(), BankGenerator.generateAccountNumber(), parseAccountType(type), name);
    }

    static String parseAccountType(String type){
        String returnValue;
        switch (type){
            case "Kortkonto":
                returnValue = "CARD";
                break;
            case "Sparkonto":
                returnValue = "SAVING";
                break;
            default:
                returnValue = "SALARY";
                break;
        }
        return returnValue;
    }
}
