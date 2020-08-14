package app.utils;

import app.configuration.spring.constants.Constants;
import app.service.extapis.VirusTotalScan;
import app.service.impl.ExecutionService;
import app.service.interfaces.IBriefDocumentService;
import app.utils.exceptions.MaliciousFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component("files_uploader")
public class FilesUploader {

    private static final Logger EXCEPTION_LOGGER =
            Logger.getLogger("intExceptionLogger");

    private final Constants constants;
    private final VirusTotalScan virusTotalScan;
    private final IStorage storage;
    private final TodayFolderArchivePathGenerator pathGenerator;

    @Autowired
    public FilesUploader(Constants constants,
                         VirusTotalScan virusTotalScan,
                         IBriefDocumentService documentService,
                         ExecutionService executionService,
                         IStorage storage,
                         TodayFolderArchivePathGenerator folderPathGenerator) {
        this.constants = constants;
        this.virusTotalScan = virusTotalScan;
        this.storage = storage;
        this.pathGenerator = folderPathGenerator;
    }

    public List<File> upload(MultipartFile... mfiles)
            throws IOException, MaliciousFoundException {
        String folderPath = pathGenerator.getFolderArchivePath();
        List<File> files = checkMalicious(mfiles, folderPath);
        return files;
    }

    public List<File> upload(String folderPath, MultipartFile... mfiles)
            throws IOException, MaliciousFoundException {
        List<File> files = checkMalicious(mfiles, folderPath);
        return files;
    }

    public File upload(String filePath, MultipartFile mfile)
            throws IOException, MaliciousFoundException {
        return checkMalicious(filePath, mfile);
    }

    private File checkMalicious(String filePath,
                                MultipartFile mfile)
            throws IOException, MaliciousFoundException {
        boolean success = true;
        String msg = Constants.EMPTY_STRING;
        if (mfile == null) {
            return null;
        }
        String fileName = new String(mfile
                .getOriginalFilename()
                .getBytes(StandardCharsets.ISO_8859_1),
                StandardCharsets.UTF_8
        );
        final File fileToSave = new File(filePath);
        mfile.transferTo(fileToSave);
        if (!this.virusTotalScan.scan(fileToSave)) {
            success = false;
            msg = "File : ".concat(
                    mfile.getOriginalFilename())
                    .concat(Constants.IS_MALICIOUS);
        }
        if (!success) {
            storage.remove(fileToSave);
            throw new MaliciousFoundException(msg);
        }
        return fileToSave;
    }

    private List<File> checkMalicious(MultipartFile[] mfiles, String folderPath)
            throws IOException, MaliciousFoundException {
        if (mfiles == null || mfiles.length == 0) {
            return null;
        }
        final List<File> files = new LinkedList<>();
        boolean success = true;
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
