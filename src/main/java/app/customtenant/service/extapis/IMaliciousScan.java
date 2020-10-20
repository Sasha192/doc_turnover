package app.customtenant.service.extapis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface IMaliciousScan {
    boolean scan(File file) throws FileNotFoundException;

    boolean scan(InputStream inputStream);

    boolean scan(byte[] bytes);
}
