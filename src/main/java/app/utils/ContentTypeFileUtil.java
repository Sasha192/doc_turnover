package app.utils;

import java.io.File;
import java.net.URLConnection;

public class ContentTypeFileUtil {

    public static String getContentType(String filename) {
        return URLConnection.getFileNameMap()
                .getContentTypeFor(
                        FileExtensionUtil.getExtension(filename)
                );
    }

    public static String getContentType(File file) {
        return URLConnection.getFileNameMap()
                .getContentTypeFor(
                        FileExtensionUtil.getExtension(file.getName())
                );
    }

}
