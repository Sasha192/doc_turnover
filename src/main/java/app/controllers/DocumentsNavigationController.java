package app.controllers;

import app.controllers.responses.ResponseJsonDocumentsList;
import app.controllers.responses.ResponseJsonText;
import app.service.CorePropertySevice;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(value = "/doc")
public class DocumentsNavigationController {

    private static final String ARCHIVE_PATH_NAME = "ARCHIVE_PATH";

    private static final String EXTENSION_FILE = ".docx";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd_MM_yyyy");

    private static final String SEPARATOR_FILE_SYSTEM = null;

    private static final String FILE_PATH_SEPARATOR = File.separator;

    private static final String UNDERSCORE = "_";

    private static final Logger LOGGER = Logger.getLogger(DocumentsNavigationController.class);

    private static final String FILE_TRANSFER_ERROR = "==================FILE TRANSFER ERROR==================";

    private final String archivePathValue;

    @Autowired
    public DocumentsNavigationController(CorePropertySevice corePropertySevice) {
        this.archivePathValue = corePropertySevice.findByName(ARCHIVE_PATH_NAME).getValue();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDocumentsList list() {

    }

    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    @ResponseBody
    public ResponseJsonDocumentsList listFilter(@RequestBody Map<String, Object> filters, ModelMap modelMap) {
        return null;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonText upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("performer") String performer,
                                   @RequestParam("department") String department, ModelMap modelMap) {
        String date = LocalDate.now().format(DATE_TIME_FORMATTER);
        String filePath = archivePathValue
                .concat(FILE_PATH_SEPARATOR)
                .concat(department)
                .concat(UNDERSCORE)
                .concat(performer)
                .concat(UNDERSCORE)
                .concat(date)
                .concat(EXTENSION_FILE);
        File fileToSave = new File(filePath);
        try {
            file.transferTo(fileToSave);
            return new ResponseJsonText(true, "Uploaded");
        } catch (IOException e) {
            LOGGER.error(FILE_TRANSFER_ERROR);
            LOGGER.error(e.getMessage());
        }
        return new ResponseJsonText(false, "Please restart page\nOr it is server-side problem");
    }

}
