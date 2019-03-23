package App.stages.createaccount;

public class CreateAccountHelper {
    static void createNewaccount(Object ... data){

    }

    static String parseAccountType(String type){
        String returnValue;
        switch (type){
            case "Kortkonto":
                returnValue = "CARD";
                break;
            case "Sparkonto":
                returnValue = "SAVING";
                break;
            default:
                returnValue = "Lönekonto";
                break;
        }
        return returnValue;
    }
}
