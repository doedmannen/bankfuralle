package App.stages.home;

import App.BankMain;
import App.helpers.generators.BankGenerator;
import App.models.Card;
import App.models.Respons;
import App.stages.confirmation.ConfirmationController;

public final class HomeSimulator {
    private HomeSimulator(){}

    static void runCardSimulation(double amount, String msg){
        Respons respons = (Respons) BankMain.sqlHelper.getObjectFromQuery("payWithCard", BankMain.card.getNumber(), amount, msg);
        ConfirmationController.setMessage(respons.getAnswer().equals("DONE") ?
                "Köp på " + amount + "SEK godkändes" :
                "Köp " + amount + " SEK nekades. Du har antingen överstigit ditt maxbelopp, saknar balans på dina kortkonton, eller saknar kortkonto.");
    }
    static void runPaydaySimulation(double amount){
        Respons respons = (Respons) BankMain.sqlHelper.getObjectFromQuery("paySalary",
                BankGenerator.generateAccountNumber(), BankMain.customer.getSsn(), amount);
        ConfirmationController.setMessage((boolean) respons.getAnswer() ?
                "Du har fått lön på 25 000 SEK" : "Löneutbetalning misslyckades, har du ett lönekonto?");
    }
    static void runAutogiroSimulation(){
        BankMain.sqlHelper.runQueryWithoutData("chargeAutogiro");
        ConfirmationController.setMessage("Alla aktiva autogiron har dragit 299 SEK");
    }
}
