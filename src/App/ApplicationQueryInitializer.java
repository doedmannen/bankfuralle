package App;

import App.helpers.database.SQLHelper;
import App.helpers.database.enums.SQLTypes;
import App.models.Account;
import App.models.Customer;
import App.models.Respons;
import App.models.Transaction;

import java.lang.annotation.Repeatable;

public final class ApplicationQueryInitializer {
    private ApplicationQueryInitializer(){}

    static void runStart(SQLHelper sqlHelper){
        // Login user
        sqlHelper.createQuery("loginQuery","SELECT * FROM costumers WHERE SSN = ? AND password = ?",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING}, Customer.class);

        // Reload user
        sqlHelper.createQuery("reloadUser","SELECT * FROM costumers WHERE id = ?",
                new SQLTypes[]{SQLTypes.LONG}, Customer.class);

        // Update costumers max withdraw
        sqlHelper.createQuery("updateMaxWithdraw","UPDATE costumers SET max_withdraw = ? WHERE id = ?",
                new SQLTypes[]{SQLTypes.DOUBLE, SQLTypes.LONG}, Customer.class);

        // Get accounts for logged in customer
        sqlHelper.createQuery("allMyAccountsQuery","SELECT * FROM balance_accounts WHERE owner_id = ?",
                new SQLTypes[]{SQLTypes.LONG}, Account.class);

        // Get one specific account
        sqlHelper.createQuery("getAccount","SELECT * FROM balance_accounts WHERE number = ?",
                new SQLTypes[]{SQLTypes.STRING}, Account.class);

        // Get latest 10 transactions for specific account with offset
        sqlHelper.createQuery("getAccountTransactions","SELECT * FROM total_transactions " +
                        "WHERE number = ? AND `status` = 'DONE' ORDER BY time_of_transaction DESC LIMIT 10 OFFSET ?",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.INT}, Transaction.class);

        // Get latest 5 transactions for logged in customer
        sqlHelper.createQuery("allMyLatestTransactionsQuery","SELECT * FROM total_transactions " +
                        "WHERE owner = ? AND `status` = 'DONE' ORDER BY time_of_transaction DESC LIMIT 5",
                new SQLTypes[]{SQLTypes.LONG}, Transaction.class);

        // Validate if an account number is free
        sqlHelper.createQuery("accountNumberIsFree", "SELECT account_number_is_free(?) as answer",
                new SQLTypes[]{SQLTypes.STRING}, Respons.class);

        // Validate if an account has a certain balance
        sqlHelper.createQuery("accountHasBalance", "SELECT has_balance(?, ?) as answer",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.DOUBLE}, Respons.class);

        // Create new account
        sqlHelper.createQuery("openNewAccount", "INSERT INTO accounts SET `owner_id` = ?, `number` = ?, `type` = ?, `name` = ?",
                new SQLTypes[]{SQLTypes.LONG, SQLTypes.STRING, SQLTypes.STRING, SQLTypes.STRING});

        // Create new transaction
        sqlHelper.createQuery("createTransaction", "CALL create_transaction(?, ?, ?, ?, ?, ?)",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING, SQLTypes.DOUBLE, SQLTypes.STRING, SQLTypes.STRING, SQLTypes.STRING});

        // Create new monthly transaction
        sqlHelper.createQuery("createMonthlyTransaction", "CALL create_monthly_transaction(?, ?, ?, ?, ?, ?)",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING, SQLTypes.DOUBLE, SQLTypes.STRING, SQLTypes.STRING, SQLTypes.STRING});

        // Create new monthly transaction
        sqlHelper.createQuery("createAutogiro", "CALL create_autogiro(?, ?)",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING});

        // Delete account
        sqlHelper.createQuery("deleteAccount", "DELETE FROM `accounts` WHERE number = ?",
                new SQLTypes[]{SQLTypes.STRING});

        // Edit account
        sqlHelper.createQuery("editAccount", "UPDATE `accounts` SET `name` = ?, `type` = ? WHERE `number` = ?",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING, SQLTypes.STRING});

        // Pay with card
        sqlHelper.createQuery("payWithCard", "SELECT card_pay(?, ?, ?)",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.DOUBLE, SQLTypes.STRING}, Respons.class);

        // Pay salary
        sqlHelper.createQuery("paySalary", "SELECT pay_salary(?, ?, ?, '0000-00-00') AS answer",
                new SQLTypes[]{SQLTypes.STRING, SQLTypes.STRING, SQLTypes.DOUBLE}, Respons.class);


    }
}
