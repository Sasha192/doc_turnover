package app.utils;

import app.configuration.spring.constants.Constants;
import app.models.basic.BriefDocument;
import app.models.basic.Performer;
import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class FileDocumentsMapperUtil {

    public static List<BriefDocument> map(final List<File> files,
                                   final String folderPath,
                                   final Performer performer) {
        LocalDate now = LocalDate.now();
        List<BriefDocument> documents = new LinkedList<>();
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
            documents.add(briefDocument);
        }
        return documents;
    }
}
