package app.utils;

import app.tenantdefault.models.DocumentEntity;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.IOUtils;

public class ZipUtils {

    public static void write(ZipArchiveOutputStream zipOut, File... files)
            throws IOException {
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

    public static void write(ZipArchiveOutputStream zipOut, List<DocumentEntity> entities)
            throws IOException {
        for (final DocumentEntity ent : entities) {
            byte[] data = ent.getDocument().getData();
            final InputStream in = new ByteArrayInputStream(data);
            final String entryname = ent.getName();
            zipOut.putArchiveEntry(new ZipArchiveEntry(entryname));
            IOUtils.copy(in, zipOut);
            zipOut.flush();
            in.close();
            zipOut.closeArchiveEntry();
        }
    }
}
