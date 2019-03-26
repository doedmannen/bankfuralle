package App.stages.viewaccount;

import App.BankMain;
import App.models.Account;
import App.models.Transaction;

import java.util.List;

public final class ViewAccountHelper {
    private ViewAccountHelper(){}

    private static String accountNumber;
    private static int offsetCount;
    static Account currentAccount;
    static List<Transaction> transactionList;

    public static void setAccountNumber(String accountNumber) {
        ViewAccountHelper.accountNumber = accountNumber;
    }

    static void initLoader(){
        offsetCount = 0;
        currentAccount = (Account) BankMain.sqlHelper.getObjectFromQuery("getAccount", accountNumber);
        transactionList = (List<Transaction>) BankMain.sqlHelper.getListFromQuery("getAccountTransactions", accountNumber, offsetCount);
    }

    static void loadMoreTransactions(){
        offsetCount += 10;
        transactionList.addAll((List<Transaction>) BankMain.sqlHelper.getListFromQuery("getAccountTransactions", accountNumber, offsetCount));
    }

}
