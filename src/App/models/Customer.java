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



    public String getName(){
        return firstname.concat(" " + lastname);
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "IAMA user with id " + id + " and name " + firstname.concat(" " + lastname);
    }
}
