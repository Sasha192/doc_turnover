package app.controllers;

import app.configuration.spring.constants.Constants;
import app.models.basic.BriefDocument;
import app.service.interfaces.IBriefDocumentService;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocRetrieveUtils {

    @Autowired
    private IBriefDocumentService docService;

    public File[] retrieveFilesByDocIds(final String[] docIds)
            throws NumberFormatException {
        final File[] files = new File[docIds.length];
        int i = 0;
        for (final String docId : docIds) {
            final Long id = Long.valueOf(docId);
            final BriefDocument briefDocument = this.docService.findOne(id);
            final String filePath = briefDocument.getPath()
                    .concat(Constants.SLASH)
                    .concat(briefDocument.getName())
                    .concat(briefDocument.getExtName());
            File file = new File(filePath);
            if (file.exists()) {
                files[i++] = new File(filePath);
            }
        }
        return files;
    }
}
