package app;

public class Main {
    public static boolean test(String regEx, String word) {
        System.out.println();
        return word.matches(regEx);
    }
}
