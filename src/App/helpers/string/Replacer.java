package App.helpers.string;

public class Replacer {
    public static String superTrim(String text){
        return text.replaceAll("^ +([^ ]+)|( ?) *([^ ]+)( ) *| *$","$1$2$3$4");
    }
    public static String superTrimFixed(String text, int length){
        return Replacer.superTrim(text).replaceAll("(.{"+length+"}).*","$1");
    }
    public static String numberTrimmer(String text, int length){
        return Replacer.superTrimFixed(text.replaceAll("[^\\d]()", "$1"), length);
    }
    public static String moneyTrim(String text, int length){
        text = reverse(numberTrimmer(text, length));
        return reverse(text.replaceAll("(\\d{3})", "$1 ")).trim();

    }
    private static String reverse(String text){
        String[] toReverse = text.split("");
        String[] reversed = new String[text.length()];
        for(int i = 0; i < text.length(); ++i){
            reversed[text.length()-i-1] = toReverse[i];
        }
        return String.join("", reversed);
    }
}
