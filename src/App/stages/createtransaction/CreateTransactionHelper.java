package App.stages.createtransaction;

import App.BankMain;

import java.util.List;

public class CreateTransactionHelper {
    static List getMyAccounts(){
        return BankMain.sqlHelper.getListFromQuery("allMyAccountsQuery", BankMain.customer.getId());
    }

    void createTransaction(){

    }

    String parseAccountNumber(String fulltext){
        return fulltext.replaceAll(".*- (\\d{11,14}) -.*","$1");
    }
}
