package App.models;

import App.helpers.database.annotations.DBCol;
import javafx.fxml.FXML;

public class Card {
    @DBCol("card_number")
    private String number;

    @DBCol
    private double limit;

    public double getLimit() {
        return limit;
    }

    public String getNumber() {
        return number;
    }
}
