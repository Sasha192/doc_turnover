package app.utils;

import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.extapis.IMaliciousScan;
import app.customtenant.service.interfaces.IBriefDocumentService;
import app.tenantconfiguration.TenantContext;
import app.tenantdefault.models.DocumentEntity;
import dev.morphia.Datastore;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("doc_uploader")
public class DocumentsUploader {

    private static final Logger EXCEPTION_LOGGER =
            Logger.getLogger("intExceptionLogger");

    private final IBriefDocumentService documentService;
    private final Datastore datastore;
    private final IMaliciousScan scan;

    @Autowired
    public DocumentsUploader(IBriefDocumentService documentService,
                             Datastore datastore,
                             IMaliciousScan scan) {
        this.documentService = documentService;
        this.datastore = datastore;
        this.scan = scan;
    }

    public boolean upload(Performer performer, MultipartFile... mfiles)
            throws IOException {
        List<DocumentEntity> documents = new LinkedList<>();
        for (MultipartFile mfile : mfiles) {
            String fileName = new String(mfile
                    .getOriginalFilename()
                    .getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8
            );
            String ext = FilenameUtils.getExtension(fileName);
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
                return false;
            }
        }
        for (DocumentEntity entity : documents) {
            datastore.save(entity);
            BriefDocument doc = new BriefDocument(entity, performer.getId());
            documentService.create(doc);
        }
        return true;
    }
}
