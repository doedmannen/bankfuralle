# bankfuralle
School assignment

This project is a simple banking application in Java using JavaFX. The application is connected to a SQL-database. There is no backend in
the middle of things to handle stuff properly.

### Accounts for testing:
Login user 1: 0101012020 pw: test

Login user 2: 0202020101 pw: test


### Finished userstories
I think that I have accomplished all the userstories for the assignment. There all in there. See notes below for some implementations.

### Notes
* Three users are hardcoded into the projects database, one of them is a dummy costumer that holds all accounts that leads nowhere
* If a new transaction is made where the to_account is null, a new account is created and connected to the dummy costumer. It is handled this way because I want the "real" experience and not having to create thousands of accounts for testing.
* The users cards are auto-connected to any account of the enum type 'CARD' in the users name that has a balance that covers the transaction amount
* A card payment will be refused if all the users card accounts have less balance than the amount that is charged
* If the total sum of withdrawals on all the card accounts exceeds the chosen limit for the users cards, it will be denied
* The user can update the max withdraw card limit on the first screen after logging in
* There is a built in limit of 99 999 999,99 to all input fields and the database stores transaction amounts as double (10,2)
* All input fields for money automatically forced to follow the pattern 99 999 999,99
* The salary accounts are handled much like the card accounts. When a salary transaction is done it is automatically deposited into one of the users salary accounts,
if the costumer doesn't have a salary account, no money will be transferred.
* Salary is always withdrawn from a null account so that there will be no "denied"-status of those transactions,
it was done this way to handle the simulations and not because it would be a "real" way to handle it.

#### Transactions
* The transactions are handled like this: A transaction is added to the transactions table with the status WAITING. A schedule event (1 second sleep) UPDATES all the transactions
where the status is WAITING and the datetime is before or equal to the current_timestamp. Once the rows have been updated a trigger performs the actual transaction.
This way I handle immediate and future transactions the same way. Immediate transactions are handled within a second so they are not actually nano second "immediate".
* Monthly transactions are handled in a similar way. Every night at 00:00 a schedule checks if there are monthly transactions that should be handled today. All rows affected by
this rule are updated and a trigger adds new transactions to the transactions table, which is then performed according to the explanation above.
* If a monthly transaction has for example the 31st as day for transaction, the schedule always checks if the current day is the last day in the month.
This way no transactions are lost because of shorter months.
* If a transaction is added today as an immediate transaction and the monthly transaction checkbox is checked, it is added to transactions table and monthly transactions.
If today is not the day for the transaction it is just added to monthly and handled accordingly.
* There might be an edge case bug where this might fail if the user adds a monthly transaction which also should be handled immediate exactly at 00:00:00. I haven't been able to test this.

#### Autogiro
If the costumer wants to sign up to autogiro I have chosen to just make a table where the from account and to account is stored. The user can add autogiros
to the list from inside the application and then press the simulation button. Then 299 SEK is withdrawn from all autogiros (for all users). I chose to not place it as a "monthly transaction"
since the autogiro actually is something that is performed once the company charges the costumer for something (the company might do this on a monthly basis but that is not the banks business to care for).
