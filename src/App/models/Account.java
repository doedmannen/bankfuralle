package App.models;

import App.helpers.database.annotations.DBCol;
import javafx.fxml.FXML;

public class Account {
    @DBCol
    private String name;

    @DBCol
    private String number;

    @DBCol
    private double balance;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return name.concat(" : ").concat(number).concat(" : " + balance);
    }
}
