package app.utils;

import app.configuration.spring.constants.Constants;
import app.models.basic.Performer;
import app.service.extapis.VirusTotalScan;
import app.service.impl.ExecutionService;
import app.service.interfaces.IBriefDocumentService;
import app.utils.exceptions.MaliciousFoundException;
import app.utils.tasks.FileRecordToDatabase;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("doc_uploader")
public class DocumentsUploader {

    private static final Logger EXCEPTION_LOGGER =
            Logger.getLogger("intExceptionLogger");

    private final Constants constants;
    private final VirusTotalScan virusTotalScan;
    private final IBriefDocumentService documentService;
    private final ExecutionService executionService;
    private final IStorage storage;

    @Autowired
    public DocumentsUploader(Constants constants,
                             VirusTotalScan virusTotalScan,
                             IBriefDocumentService documentService,
                             ExecutionService executionService, IStorage storage) {
        this.constants = constants;
        this.virusTotalScan = virusTotalScan;
        this.documentService = documentService;
        this.executionService = executionService;
        this.storage = storage;
    }

    public boolean upload(Performer performer, MultipartFile... mfiles)
            throws IOException, MaliciousFoundException {
        final String folderPath = getFolderPath();
        List<File> files = checkMalicious(mfiles, folderPath);
        Runnable runnable = new FileRecordToDatabase(
                files, documentService,
                folderPath, performer
        );
        this.executionService.pushTask(runnable);
        return true;
    }

    private String getFolderPath()
            throws IOException {
        final LocalDate now = LocalDate.now();
        final int year = now.getYear();
        final int month = now.getMonthValue();
        final int day = now.getDayOfMonth();
        final String filePath;
        filePath = this.constants.get("path_to_archive")
                .getStringValue()
                + (Constants.SLASH + year)
                + (Constants.SLASH + month)
                + (Constants.SLASH + day);
        File fileFolder = new File(filePath);
        boolean mkdirs = false;
        if (!fileFolder.exists()) {
            mkdirs = fileFolder.mkdirs();
        }
        return filePath;
    }

    private List<File> checkMalicious(MultipartFile[] mfiles,
                                      String folderPath)
            throws IOException, MaliciousFoundException {
        final List<File> files = new LinkedList<>();
        boolean success = true;
        // @TODO : success = false vs = true. What impact will be ?
        String msg = Constants.EMPTY_STRING;
        for (final MultipartFile mfile : mfiles) {
            String fileName = new String(
                    mfile.getOriginalFilename().getBytes(
                            StandardCharsets.ISO_8859_1
                    ), StandardCharsets.UTF_8
            );
            // @TODO : Change this for different Charsets.
            //  e.g. There was problem with CharsetDetector(CD)
            //  CD works incorrectly!!!
            // @TODO :
            final File fileToSave = new File(
                    folderPath + Constants.SLASH.concat(fileName)
            );
            mfile.transferTo(fileToSave);
            files.add(fileToSave);
            if (!this.virusTotalScan.scan(fileToSave)) {
                success = false;
                msg = "File : ".concat(mfile.getOriginalFilename())
                        .concat(Constants.IS_MALICIOUS);
                break;
            }
        }
        if (!success) {
            storage.remove(files);
            throw new MaliciousFoundException(msg);
        }
        return files;
    }
}
