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
    public static String moneyTrim(String text){
        text = text.replaceAll("\\.",",")
                .replaceAll("[^\\d,]","")
                .replaceAll("(\\d,\\d+),$|(\\d*),(\\d*,)", "$1$2$3")
                .replaceAll("(\\d{0,8})\\d*$|(\\d{0,8})\\d*(,\\d*)", "$1$2$3")
                .replaceAll("([,\\.]\\d{1,2})\\d*$", "$1");
        return reverse(reverse(text).replaceAll("(\\d{3})", "$1 ").trim());
    }
    private static String reverse(String text){
        String[] toReverse = text.split("");
        String[] reversed = new String[text.length()];
        for(int i = 0; i < text.length(); ++i){
            reversed[text.length()-i-1] = toReverse[i];
        }
        return String.join("", reversed);
    }
    public static String parseAccountNumber(String fulltext){
        return fulltext.replaceAll(".*- (\\d{11,14}) -.*","$1");
    }

}
