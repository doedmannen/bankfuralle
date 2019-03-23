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
}
