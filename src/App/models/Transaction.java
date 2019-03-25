package App.models;

import App.helpers.database.annotations.DBCol;

import java.sql.Timestamp;

public class Transaction {

    @DBCol("number")
    String account_number;

    @DBCol
    Timestamp time_of_transaction;

    @DBCol
    double amount;

    @DBCol
    String message;

    private String getTime(){
        return time_of_transaction.toString().substring(0,10);
    }

    @Override
    public String toString() {
        return String.format("%s : %s : %.2f : %s", getTime(), account_number, amount, message);
    }
}
