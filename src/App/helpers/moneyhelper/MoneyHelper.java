package App.helpers.moneyhelper;

import App.BankMain;
import App.helpers.string.Replacer;
import App.models.Respons;

public final class MoneyHelper {
    private MoneyHelper(){}

    public static boolean correctBalance(String accountFulltext, String amount){
        double testAmount = parseAmount(amount);
        String account = Replacer.parseAccountNumber(accountFulltext);
        Respons respons = (Respons) BankMain.sqlHelper.getObjectFromQuery("accountHasBalance", account, testAmount);
        return (boolean)respons.getAnswer();
    }

    public static double parseAmount(String amount){
        amount = amount.replaceAll(" ", "").replaceAll(",", ".");
        if(amount.length() == 0 || amount.equals(".")){
            return 0;
	}
        return Double.parseDouble(amount);
    }
}
