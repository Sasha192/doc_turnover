package app.security.utils;

import java.util.Random;

public class VerificationCodeGenerator {

    private static int CODE_LENGTH = 6; // @TODO MAKE IT CUSTOMIZABLE
    private static final Random random = new Random(123123123);

    public static String generate() {
        char[] code = new char[CODE_LENGTH];
        for (int i = 0; i < CODE_LENGTH; i++) {
            code[i] = (char) ((random.nextInt() % 10 + 10) % 10 + 48);
        }
        return new String(code);
    }

    public static String generate(String prevHash) {
        char[] code = prevHash.toCharArray();
        for (int i = 0; i < code.length; i++) {
            code[i] = (char) (((random.nextInt() ^ (int) code[i]) % 10 + 10) % 10 + 48);
        }
        return new String(code);
    }
}
