package App.helpers.generators;

import App.BankMain;
import App.models.Respons;

public class BankGenerator {
    public static String generateAccountNumber(){
        String rnd = "8997";
        Respons respons;
        do {
            for (int i = 0; i < 10; ++i)
                rnd = rnd + ((int) (Math.random() * 10));
            respons = (Respons) BankMain.sqlHelper.getObjectFromQuery("accountNumberIsFree", rnd);
        }while (!(Boolean) respons.getAnswer());
        return rnd;
    }

    public static String generateCardNumber(){
        String rnd = "6";
        do
            for(int i = 0; i < 15; ++i)
                rnd = rnd+((int)(Math.random()*10));
        while (!(boolean)BankMain.sqlHelper.getObjectFromQuery("cardNumberIsFree", rnd));
        return rnd;
    }
}
