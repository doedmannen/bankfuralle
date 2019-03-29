package App.models;

import App.helpers.database.annotations.DBCol;

public class Customer {

    @DBCol
    private long id;

    @DBCol
    private String firstname;

    @DBCol
    private String lastname;

    @DBCol("SSN")
    private String ssn;

    @DBCol("max_withdraw")
    private double maxWithdraw;


    public double getMaxWithdraw() {
        return maxWithdraw;
    }

    public String getSsn() {
        return ssn;
    }

    public String getName(){
        return firstname.concat(" " + lastname);
    }

    public long getId() {
        return id;
    }

}
