package app.utils;

public class ImgToken {

    private static final int GEN = 0b1000000 + 0b1;

    public static String generate(String name) {
        int hash = name.hashCode();
        return hash + "u" + (hash % GEN) + System.currentTimeMillis();
    }
}
