package app.utils;

import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IBriefDocumentService;
import app.tenantdefault.models.DocumentEntity;
import app.tenantdefault.service.IDocumentStorage;
import dev.morphia.Datastore;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("doc_uploader")
public class DocumentsUploader {

    private static final Logger EXCEPTION_LOGGER =
            Logger.getLogger("intExceptionLogger");

    private final IBriefDocumentService documentService;
    private final IDocumentStorage datastorage;
    private final MaliciousDocumentsScanUtil scan;

    @Autowired
    public DocumentsUploader(IBriefDocumentService documentService,
                             IDocumentStorage datastorage,
                             MaliciousDocumentsScanUtil scan) {
        this.documentService = documentService;
        this.datastorage = datastorage;
        this.scan = scan;
    }

    public boolean upload(Performer performer, MultipartFile... mfiles)
            throws IOException {
        List<DocumentEntity> documents = scan.checkAndGet(mfiles);
        for (DocumentEntity entity : documents) {
            datastorage.save(entity);
            BriefDocument doc = new BriefDocument(entity, performer.getId());
            documentService.create(doc);
        }
        return true;
    }
}
