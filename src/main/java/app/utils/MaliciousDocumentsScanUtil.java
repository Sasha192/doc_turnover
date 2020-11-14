package app.utils;

import app.configuration.spring.constants.Constants;
import app.customtenant.service.extapis.IMaliciousScan;
import app.tenantconfiguration.TenantContext;
import app.tenantdefault.models.DocumentEntity;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MaliciousDocumentsScanUtil {

    private final IMaliciousScan scan;

    @Autowired
    public MaliciousDocumentsScanUtil(IMaliciousScan scan) {
        this.scan = scan;
    }

    public List<DocumentEntity> checkAndGet(MultipartFile... mfiles)
            throws IOException {
        List<DocumentEntity> documents = new LinkedList<>();
        for (MultipartFile mfile : mfiles) {
            String fileName = new String(mfile
                    .getOriginalFilename()
                    .getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8
            );
            String ext = Constants.DOT + FilenameUtils.getExtension(fileName);
            byte[] bytes = mfile.getBytes();
            boolean malScan = scan.scan(bytes);
            if (malScan) {
                DocumentEntity doc = new DocumentEntity();
                doc.setName(fileName);
                doc.setExtension(ext);
                doc.setTenantId(TenantContext.getTenant());
                doc.setSize(mfile.getSize());
                doc.setDocument(new Binary(bytes));
                documents.add(doc);
            } else {
                documents.clear();
                return new LinkedList<>();
            }
        }
        return documents;
    }

    public boolean check(MultipartFile... mfiles)
            throws IOException {
        for (MultipartFile mfile : mfiles) {
            String fileName = new String(mfile
                    .getOriginalFilename()
                    .getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8
            );
            String ext = FilenameUtils.getExtension(fileName);
            byte[] bytes = mfile.getBytes();
            boolean malScan = scan.scan(bytes);
            if (!malScan) {
                return false;
            }
        }
        return true;
    }
}
