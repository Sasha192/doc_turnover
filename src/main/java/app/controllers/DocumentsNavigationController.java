package app.controllers;

import app.controllers.responses.ResponseJsonText;
import app.models.BriefDocument;
import app.models.Performer;
import app.models.serialization.ExcludeStrategies;
import app.service.IBriefDocumentService;
import app.service.ICorePropertyService;
import app.service.IPerformerService;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
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
public class DocumentsNavigationController extends JsonSupportController {

    private static final String ARCHIVE_PATH_NAME = "ARCHIVE_PATH";

    private static final String EXTENSION_FILE = ".docx";

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("dd_MM_yyyy");

    private static final String SEPARATOR_FILE_SYSTEM = null;

    private static final String FILE_PATH_SEPARATOR = File.separator;

    private static final String UNDERSCORE = "_";

    private static final Logger LOGGER = Logger.getLogger(DocumentsNavigationController.class);

    private static final String FILE_TRANSFER_ERROR =
            "==================FILE TRANSFER ERROR==================";

    private static String ARCHIVE_PATH;

    @Autowired
    private ICorePropertyService corePropertyService;

    @Autowired
    private IBriefDocumentService briefDocumentService;

    @Autowired
    private IPerformerService performerService;

    @PostConstruct
    public void setConstants() {
        // ARCHIVE_PATH = corePropertyService.retrieveByName(ARCHIVE_PATH_NAME).getValue();
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public void list(HttpServletResponse response,
                     @RequestParam(name = "page_number", defaultValue = "0") String pageNumber,
                     @RequestParam(name = "page_size", defaultValue = "20") String pageSize) throws IOException {
        List<BriefDocument> briefDocuments = briefDocumentService.findAll();
        GsonBuilder builder = new GsonBuilder()
                .setExclusionStrategies(ExcludeStrategies.ONE_TO_MANY)
                .setPrettyPrinting();
        writeToResponse(response, builder, briefDocuments);
    }

    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    public void listFilter(@RequestBody Map<String, Object> filters,
                           ModelMap modelMap) {
        //retrieve Filter

    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseJsonText upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam("performer") String performerStr,
                                   @RequestParam("department") String departmentStr,
                                   ModelMap modelMap) {
        String date = LocalDate.now().format(DATE_TIME_FORMATTER);
        String filePath = ARCHIVE_PATH
                .concat(FILE_PATH_SEPARATOR)
                .concat(departmentStr)
                .concat(UNDERSCORE)
                .concat(performerStr)
                .concat(UNDERSCORE)
                .concat(date)
                .concat(EXTENSION_FILE);
        File fileToSave = new File(filePath);
        //Performer performer = performerService.retrieveByName(performerStr);
        try {
            if (fileToSave.createNewFile()) {
                file.transferTo(fileToSave);
                BriefDocument briefDocument = new BriefDocument();
                briefDocument.setModificationDate(Date.valueOf(LocalDate.now()));
                briefDocument.setModificationDate(Date.valueOf(LocalDate.now()));
                briefDocument.setName(fileToSave.getName());
                briefDocument.setPath(fileToSave.getAbsolutePath());
                briefDocument.setDeadline(false);
                //briefDocument.setPerformer(performer);
                briefDocumentService.create(briefDocument);
            }
        } catch (IOException e) {
            LOGGER.error(FILE_TRANSFER_ERROR);
            LOGGER.error(e.getMessage());
        }
        return new ResponseJsonText(false, "Please restart page\nOr it is server-side problem");
    }

    @RequestMapping(path = "/download",
            method = RequestMethod.GET)
    public void download(@RequestParam("doc_id") String docId,
                         HttpServletResponse response) throws Exception {
        Long id = Long.valueOf(docId);
        BriefDocument briefDocument = briefDocumentService.findOne(id);
        File doc = new File(briefDocument.getPath());
        InputStream in = new FileInputStream(doc);
        toResponseDocxData(response, in.readAllBytes(), doc.getName());
    }

    private void toResponseDocxData(HttpServletResponse response, byte[] data, String name) throws IOException {
        response.setContentType("application/msword");
        response.setHeader("Content-disposition", "inline; filename=" + name);
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }
}
