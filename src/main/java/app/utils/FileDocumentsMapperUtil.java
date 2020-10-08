package app.utils;

import app.configuration.spring.constants.Constants;
import app.customtenant.models.basic.BriefDocument;
import app.customtenant.models.basic.Performer;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

public class FileDocumentsMapperUtil {

    public static List<BriefDocument> map(final List<File> files,
                                   final String folderPath,
                                   final Performer performer) {
        java.util.Date now = CustomAppDateTimeUtil.now();
        List<BriefDocument> documents = new LinkedList<>();
        for (File file : files) {
            BriefDocument briefDocument = new BriefDocument();
            briefDocument.setPath(folderPath);
            String fileName = file.getName();
            String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
            briefDocument.setName(fileNameWithoutExtension);
            String filExtName = FilenameUtils.getExtension(fileName);
            briefDocument.setExtName(Constants.DOT.concat(filExtName));
            briefDocument.setDate(now);
            briefDocument.setPerformer(performer);
            documents.add(briefDocument);
        }
        return documents;
    }
}
