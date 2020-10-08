package app.utils;

import app.security.models.auth.UserInfo;
import java.util.UUID;

public class ImgToken {

    private static final int GEN = 0b1000000 + 0b1;

    public static String generate(String name) {
        int hash = name.hashCode();
        return hash + "u" + (hash % GEN) + System.currentTimeMillis();
    }

    public static String generate(UserInfo info) {
        String name = null;
        if (info.getFirstName() != null
                && info.getMiddleName() != null
                && info.getLastName() != null) {
            name = info.getFirstName()
                    + info.getLastName()
                    + info.getMiddleName();
        } else {
            name = UUID.randomUUID()
                    .toString()
                    .replace("-", "");
        }
        int hash = name.hashCode();
        hash = hash < 0 ? hash * -1 : hash;
        return hash + "u" + (hash % GEN) + System.currentTimeMillis();
    }
}
