package App.models;

import App.helpers.database.annotations.DBCol;

public class Customer {

    @DBCol
    long id;

    @DBCol
    String firstname;

    @DBCol
    String lastname;

    @DBCol("SSN")
    String ssn;


    @Override
    public String toString() {
        return "IAMA user with id " + id + " and name " + firstname.concat(" " + lastname);
    }
}
