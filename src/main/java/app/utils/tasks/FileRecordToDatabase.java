package app.utils.tasks;

import app.configuration.spring.constants.Constants;
import app.models.basic.BriefDocument;
import app.models.basic.Performer;
import app.service.interfaces.IBriefDocumentService;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class FileRecordToDatabase implements Runnable {

    private List<File> files;
    private IBriefDocumentService service;
    private String folderPath;
    private Performer performer;

    public FileRecordToDatabase(final List<File> files,
                                final IBriefDocumentService service,
                                final String folderPath, final Performer performer) {
        this.files = files;
        this.service = service;
        this.folderPath = folderPath;
        this.performer = performer;
    }

    @Override
    public void run() {
        LocalDate now = LocalDate.now();
        for (File file : files) {
            BriefDocument briefDocument = new BriefDocument();
            briefDocument.setPath(folderPath);
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
