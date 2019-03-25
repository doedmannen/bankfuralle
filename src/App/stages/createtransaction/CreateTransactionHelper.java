package App.stages.createtransaction;

import App.BankMain;
import App.helpers.string.Replacer;
import App.models.Respons;

import java.util.List;

public class CreateTransactionHelper {
    static List getMyAccounts(){
        return BankMain.sqlHelper.getListFromQuery("allMyAccountsQuery", BankMain.customer.getId());
    }

    static void createTransaction(){

    }

    private static void createMonthlyTransaction(){

    }

    static boolean incorrectBalance(String accountFulltext, String amount){
        double testAmount = parseAmount(amount);
        String account = parseAccountNumber(accountFulltext);
        Respons respons = (Respons) BankMain.sqlHelper.getObjectFromQuery("accountHasBalance", account, testAmount);
        return (boolean)respons.getAnswer();
    }

    private static double parseAmount(String amount){
        return Double.parseDouble(Replacer.numberTrimmer(amount,10));
    }

    private static String parseAccountNumber(String fulltext){
        return fulltext.replaceAll(".*- (\\d{11,14}) -.*","$1");
    }


}
