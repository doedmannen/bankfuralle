package App.stages.home;

import App.BankMain;
import App.helpers.string.Replacer;
import App.models.Customer;
import App.models.Respons;

import java.util.List;

public final class HomeHelper {
    private HomeHelper(){}


    static List getAccounts(){
        return BankMain.sqlHelper.getListFromQuery("allMyAccountsQuery", BankMain.customer.getId());
    }

    static List getLatestTransactions(){
        return BankMain.sqlHelper.getListFromQuery("allMyLatestTransactionsQuery", BankMain.customer.getId());
    }

    static boolean accountHasBalance(String accountNumber){
        Respons respons = (Respons) BankMain.sqlHelper.getObjectFromQuery("accountHasBalance", accountNumber, 0.01);
        return (boolean) respons.getAnswer();
    }

    static void reloadUserFromDatabase(){
        try{
            BankMain.customer = (Customer) BankMain.sqlHelper.getObjectFromQuery("reloadUser", BankMain.customer.getId());
        }catch (Exception e){}
    }

    static void updateMax(String max){
        double maxWithdraw = Double.parseDouble(max);
        BankMain.sqlHelper.runQueryWithData("updateMaxWithdraw", maxWithdraw, BankMain.customer.getId());
        reloadUserFromDatabase();
    }

    static boolean isLastAccount(){
        return getAccounts().size() < 2;
    }
}
