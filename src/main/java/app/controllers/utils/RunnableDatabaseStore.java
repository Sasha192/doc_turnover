package app.controllers.utils;

import app.configuration.spring.constants.Constants;
import app.models.basic.BriefDocument;
import app.models.basic.Performer;
import app.service.IBriefDocumentService;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class RunnableDatabaseStore implements Runnable {

    private List<File> files;
    private IBriefDocumentService service;
    private String filePath;
    private Performer performer;

    public RunnableDatabaseStore(final List<File> files,
                                 final IBriefDocumentService service,
                                 final String filePath, final Performer performer) {
        this.files = files;
        this.service = service;
        this.filePath = filePath;
        this.performer = performer;
    }

    @Override
    public void run() {
        LocalDate now = LocalDate.now();
        for (File file : files) {
            BriefDocument briefDocument = new BriefDocument();
            briefDocument.setPath(filePath);
            String fileName = file.getName();
            String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
            briefDocument.setName(fileNameWithoutExtension);
            String filExtName = FilenameUtils.getExtension(fileName);
            briefDocument.setExtName(Constants.DOT.concat(filExtName));
            briefDocument.setDate(Date.valueOf(now));
            briefDocument.setPerformer(performer);
            service.create(briefDocument);
        }
    }
}
