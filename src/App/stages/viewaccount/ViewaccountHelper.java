package App.stages.viewaccount;

import App.models.Account;

public final class ViewaccountHelper {
    private ViewaccountHelper(){}

    private static String accountNumber;

    public static void setAccountNumber(String accountNumber) {
        ViewaccountHelper.accountNumber = accountNumber;
    }

    static Account getAccount(){
        return new Account();
    }
}
