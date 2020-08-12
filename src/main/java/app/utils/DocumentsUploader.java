package app.utils;

import app.models.basic.BriefDocument;
import app.models.basic.Performer;
import app.service.impl.ExecutionService;
import app.service.interfaces.IBriefDocumentService;
import app.utils.exceptions.MaliciousFoundException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("doc_uploader")
public class DocumentsUploader {

    private static final Logger EXCEPTION_LOGGER =
            Logger.getLogger("intExceptionLogger");

    private final IBriefDocumentService documentService;
    private final ExecutionService executionService;
    private final FilesUploader filesUploader;
    private final TodayFolderArchivePathGenerator folderPathGenerator;

    @Autowired
    public DocumentsUploader(IBriefDocumentService documentService,
                             ExecutionService executionService,
                             @Qualifier("files_uploader") FilesUploader filesUploader,
                             @Qualifier("date_file_path_generator")
                                     TodayFolderArchivePathGenerator folderPathGenerator) {
        this.documentService = documentService;
        this.executionService = executionService;
        this.filesUploader = filesUploader;
        this.folderPathGenerator = folderPathGenerator;
    }

    public boolean upload(Performer performer, MultipartFile... mfiles)
            throws IOException, MaliciousFoundException {
        String folderPath = folderPathGenerator.getFolderArchivePath();
        List<File> files = filesUploader.upload(mfiles);
        List<BriefDocument> documents =
                FileDocumentsMapperUtil.map(files, folderPath, performer);
        documentService.create(documents);
        return true;
    }
}
