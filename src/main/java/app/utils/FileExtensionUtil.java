package app.utils;

import app.configuration.spring.constants.Constants;
import java.io.File;
import org.apache.commons.io.FilenameUtils;

public class FileExtensionUtil {

    public static String getExtension(String filename) {
        return Constants.DOT + FilenameUtils.getExtension(filename);
    }

    public static String getExtension(File file) {
        return Constants.DOT + FilenameUtils.getExtension(file.getName());
    }

}
