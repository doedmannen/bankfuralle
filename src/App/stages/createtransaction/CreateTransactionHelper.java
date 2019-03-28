package App.stages.createtransaction;

import App.BankMain;
import App.helpers.moneyhelper.MoneyHelper;
import App.helpers.string.Replacer;
import App.models.Respons;
import App.stages.confirmation.ConfirmationController;
import javafx.scene.control.CheckBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class CreateTransactionHelper {
    private CreateTransactionHelper(){}

    public static boolean paymentState = false;

    static List getMyAccounts(){
        return BankMain.sqlHelper.getListFromQuery("allMyAccountsQuery", BankMain.customer.getId());
    }

    static void createTransaction(
            boolean repeat,
            String fromAccount,
            String toAccount,
            String amount,
            String date,
            String messageWithdrawal,
            String messageDeposit){
        if(repeat){
            if(LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE).isEqual(LocalDate.now())){
                BankMain.sqlHelper.runQueryWithData("createTransaction", fromAccount, toAccount, MoneyHelper.parseAmount(amount), messageWithdrawal, messageDeposit, date);
            }
            BankMain.sqlHelper.runQueryWithData("createMonthlyTransaction", fromAccount, toAccount, MoneyHelper.parseAmount(amount), messageWithdrawal, messageDeposit, date);
            ConfirmationController.setMessage("Transaktionen är inlagd i systemet och kommer att behandlas på utsatt tid samt upprepas varje månad");
        } else {
            BankMain.sqlHelper.runQueryWithData("createTransaction", fromAccount, toAccount, MoneyHelper.parseAmount(amount), messageWithdrawal, messageDeposit, date);
            ConfirmationController.setMessage((paymentState ? "Betalningen":"Transaktionen")+ " är inlagd i systemet och kommer att behandlas på utsatt tid");
        }
    }

}
