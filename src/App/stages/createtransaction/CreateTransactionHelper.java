package App.stages.createtransaction;

import App.BankMain;
import App.helpers.string.Replacer;
import App.models.Respons;
import App.stages.confirmation.ConfirmationController;
import javafx.scene.control.CheckBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class CreateTransactionHelper {
    private CreateTransactionHelper(){}
    
    static List getMyAccounts(){
        return BankMain.sqlHelper.getListFromQuery("allMyAccountsQuery", BankMain.customer.getId());
    }

    static void createTransaction(boolean repeat, String fromAccount, String toAccount, String amount, String date, String message){
        if(repeat){
            if(LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE).isEqual(LocalDate.now())){
                BankMain.sqlHelper.runQueryWithData("createTransaction", fromAccount, toAccount, parseAmount(amount), message, date);
            }
            BankMain.sqlHelper.runQueryWithData("createMonthlyTransaction", fromAccount, toAccount, parseAmount(amount), message, date);
            ConfirmationController.setMessage("Transaktionen är inlagd i systemet och kommer att behandlas på utsatt tid samt upprepas varje månad");
        } else {
            BankMain.sqlHelper.runQueryWithData("createTransaction", fromAccount, toAccount, parseAmount(amount), message, date);
            ConfirmationController.setMessage("Transaktionen är inlagd i systemet och kommer att behandlas på utsatt tid");
        }
    }


    static boolean incorrectBalance(String accountFulltext, String amount){
        double testAmount = parseAmount(amount);
        String account = Replacer.parseAccountNumber(accountFulltext);
        Respons respons = (Respons) BankMain.sqlHelper.getObjectFromQuery("accountHasBalance", account, testAmount);
        return (boolean)respons.getAnswer();
    }

    private static double parseAmount(String amount){
        return Double.parseDouble(Replacer.numberTrimmer(amount,10));
    }



}
