package App;

import App.helpers.database.SQLHelper;
import App.helpers.database.enums.SQLTypes;
import App.models.Account;
import App.models.Customer;
import App.models.Transaction;

public final class ApplicationQueryInitializer {
    private ApplicationQueryInitializer(){}

    static void runStart(SQLHelper sqlHelper){
        // Login user
        sqlHelper.createQuery("loginQuery","SELECT * FROM costumers WHERE SSN = ? AND password = ?",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING}, Customer.class);

        // Get accounts for logged in customer
        sqlHelper.createQuery("myAccountsQuery","SELECT * FROM accounts WHERE owner_id = ?",
                new SQLTypes[]{SQLTypes.LONG}, Account.class);

        // Get latest 10 transactions for logged in customer
        sqlHelper.createQuery("myLatestTransactionsQuery","SELECT * FROM total_transactions " +
                        "WHERE account_owner_id = ? ORDER BY time_of_transaction",
                new SQLTypes[]{SQLTypes.LONG}, Transaction.class);





    }
}
