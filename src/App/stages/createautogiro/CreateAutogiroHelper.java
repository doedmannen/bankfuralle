package App.stages.createautogiro;

import App.BankMain;

import java.util.List;

public final class CreateAutogiroHelper {
    private CreateAutogiroHelper(){}

    static List getAccounts(){
        return BankMain.sqlHelper.getListFromQuery("allMyAccountsQuery", BankMain.customer.getId());
    }
}
