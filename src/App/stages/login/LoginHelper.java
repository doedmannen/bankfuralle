package App.stages.login;

import App.BankMain;
import App.models.Customer;
import App.stages.StageHandler;

import java.util.ArrayList;
import java.util.Calendar;

public final class LoginHelper {
    private LoginHelper(){}

    static Customer getUserFromDatabase(String SSN, String password){
        Customer customer = null;
        try{
            customer = (Customer) BankMain.sqlHelper.getObjectFromQuery("loginQuery", SSN, password);
        }catch (Exception e){}
        return customer;
    }


}
