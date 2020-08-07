package app.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

public class ZipUtils {

    public static void write(ZipArchiveOutputStream zipOut, File... files) throws IOException {
        for (final File file : files) {
            final InputStream in = new FileInputStream(file);
            final String entryname = file.getName();
            zipOut.putArchiveEntry(new ZipArchiveEntry(entryname));
            IOUtils.copy(in, zipOut);
            zipOut.flush();
            in.close();
            zipOut.closeArchiveEntry();
        }
    }
}
