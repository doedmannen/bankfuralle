package App.stages.home;

import App.BankMain;

import java.util.ArrayList;
import java.util.List;

public final class HomeHelper {
    private HomeHelper(){}


    static List getAccounts(){
        return BankMain.sqlHelper.getListFromQuery("allMyAccountsQuery", BankMain.customer.getId());
    }

    static List getLatestTransactions(){
        return BankMain.sqlHelper.getListFromQuery("allMyLatestTransactionsQuery", BankMain.customer.getId());
    }
}
