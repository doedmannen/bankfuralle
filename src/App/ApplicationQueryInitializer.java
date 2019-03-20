package App;

import App.helpers.database.SQLHelper;
import App.helpers.database.enums.SQLTypes;
import App.models.Account;
import App.models.Customer;

public final class ApplicationQueryInitializer {
    private ApplicationQueryInitializer(){}

    static void runStart(SQLHelper sqlHelper){
        // Login user
        sqlHelper.createQuery("loginQuery","SELECT * FROM costumers WHERE SSN = ? AND password = ?",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING}, Customer.class);

        // Get accounts for logged in customer
        sqlHelper.createQuery("myAccountsQuery","SELECT * FROM accounts WHERE owner_id = ?",
                new SQLTypes[]{SQLTypes.LONG}, Account.class);

        // Get latest transactions for logged in customer
        sqlHelper.createQuery("myLatestTransactions","SELECT * FROM transactions DESC LIMIT 10",
                new SQLTypes[]{SQLTypes.LONG}, Account.class);





    }
}
