# bankfuralle
School assignment

This project is a simple banking application in Java using JavaFX. The application is connected to a SQL-database. There is no backend in
the middle of things to handle stuff properly (was never intended to have this since the project time limit was 6 days)

### Notes
* Three users are hardcoded into the projects database, one of them is a dummy costumer that holds all accounts that leads nowhere
* If a new transaction is made where the to_account is null, a new account is created and connected to the dummy costumer
* The users cards are auto-connected to any account of the enum type 'CARD' in the users name that cover the transaction
* If the costumers cardlimit is overcharged, the costumer doesn't have a card account or no account covers the transaction amount then card_payment will deny payments
