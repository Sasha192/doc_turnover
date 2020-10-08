package app.customtenant.service.extapis;

import java.io.File;

public interface IMaliciousScan {
    boolean scan(File file);
}
