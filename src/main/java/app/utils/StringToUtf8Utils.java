package app.utils;

import java.nio.charset.StandardCharsets;

public class StringToUtf8Utils {

    public static String encodeUtf8(String string) {
        string = new String(string
                .getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
        );
        return string;
    }

}
