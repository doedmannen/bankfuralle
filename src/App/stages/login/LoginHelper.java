package App.stages.login;

import App.BankMain;
import App.models.Customer;

import java.util.ArrayList;

public final class LoginHelper {
    private LoginHelper(){}

    static Customer getUserFromDatabase(String SSN, String password){
        System.out.println("getting user");
        Customer customer = null;
        ArrayList data = new ArrayList();
        data.add(SSN);
        data.add(password);
        try{
            customer = (Customer) BankMain.sqlHelper.getListFromQuery("loginQuery", data).get(0);
        }catch (Exception e){}
        return customer;
    }
}
