package App.models;

import App.helpers.database.annotations.DBCol;
import javafx.fxml.FXML;

public class Account {

    @DBCol
    private long id;

    @DBCol
    private String name;

    @DBCol
    private String number;

    @DBCol
    private double balance;

    @DBCol
    private String type;

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public String getType() {
        String accountType = "";
        switch (type){
            case "SALARY":
                accountType = "Lönekonto: ";
                break;
            case "SAVING":
                accountType = "Sparkonto: ";
                break;
            case "CARD":
                accountType = "Kortkonto: ";
                break;
        }
        return accountType;
    }


    @Override
    public String toString() {
        return String.format("%s %s - %s - %f SEK", getType(),name,number,balance);
    }
}
