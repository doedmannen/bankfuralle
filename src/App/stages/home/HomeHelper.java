package App.stages.home;

import App.BankMain;

import java.util.ArrayList;
import java.util.List;

public final class HomeHelper {
    private HomeHelper(){}

    static List accountList;

    static void getAccounts(){
        ArrayList data = new ArrayList();
        data.add(BankMain.customer.getId());
        accountList = BankMain.sqlHelper.getListFromQuery("myAccountsQuery", data);
    }
}
