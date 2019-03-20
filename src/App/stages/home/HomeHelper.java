package App.stages.home;

import App.BankMain;

import java.util.ArrayList;
import java.util.List;

public final class HomeHelper {
    private HomeHelper(){}


    static List getAccounts(){
        return BankMain.sqlHelper.getListFromQuery("myAccountsQuery", BankMain.customer.getId());
    }
}
