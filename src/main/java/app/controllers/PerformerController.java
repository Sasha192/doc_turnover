package app.controllers;

import app.configuration.spring.constants.Constants;
import app.models.basic.CustomUser;
import app.models.basic.Performer;
import app.models.serialization.ExcludeStrategies;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.wrappers.AuthenticationWrapper;
import app.security.wrappers.PerformerWrapper;
import app.service.interfaces.IPerformerService;
import app.utils.FilesUploader;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/performer")
public class PerformerController extends JsonSupportController {

    // @TODO : What should we do if connection is not via http ??? Could it be ???
    // @TODO : any operations to DB perform in queue???

    private static final Logger LOGGER = Logger.getLogger("intExceptionLogger");

    private IUserService userService;
    private AuthenticationWrapper authenticationWrapper;
    private final PerformerWrapper performerWrapper;
    private IPerformerService performerService;
    private DefaultPasswordEncoder encoder;
    private final FilesUploader filesUploader;
    private final Constants constants;

    @Autowired
    public PerformerController(IUserService userService,
                               AuthenticationWrapper authenticationWrapper,
                               IPerformerService performerService,
                               DefaultPasswordEncoder encoder,
                               PerformerWrapper performerWrapper,
                               @Qualifier("files_uploader")
                                           FilesUploader filesUploader,
                               @Qualifier("app_constants")
                                           Constants constants) {
        this.userService = userService;
        this.authenticationWrapper = authenticationWrapper;
        this.performerService = performerService;
        this.encoder = encoder;
        this.performerWrapper = performerWrapper;
        this.filesUploader = filesUploader;
        this.constants = constants;
    }

    @RequestMapping("/my/info")
    public void myInfo(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        GsonBuilder builder = new GsonBuilder()
                .addSerializationExclusionStrategy(
                        ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .setPrettyPrinting();
        writeToResponse(response, builder, performer);
    }

    @RequestMapping("my/password")
    public void changeMyPassword(@RequestParam(value = "new_password", required = false)
                                         String newPassword,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        if (!newPassword.matches(".{8,}")) {
            sendDefaultJson(response, false, "Weak password!");
            return;
        }
        Principal principal = (Principal) authenticationWrapper.getPrincipal(request);
        if (principal != null) {
            CustomUser user = userService.retrieveByName(principal.getName());
            newPassword = encoder.encode(newPassword);
            user.setPassword(newPassword);
            userService.update(user);
        }
        sendDefaultJson(response, true, "");
    }

    @RequestMapping("my/img")
    public void changeMyImage(@RequestParam(value = "img")
                                          MultipartFile mpfile,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException {
        Performer performer = performerWrapper.retrievePerformer(request);
        File folder = ResourceUtils
                .getFile("classpath:static/img/performers_avatars");
        String dbFilePath = "/img/performers_avatars/";
        if (folder.exists()) {
            String filePath = folder.getAbsolutePath();
            int hash = performer.getName().hashCode();
            hash = hash < 0 ? hash * -1 : hash;
            String ext = FilenameUtils
                    .getExtension(mpfile.getOriginalFilename());
            // @TODO : check for executed file. i.e. .exe
            String fileName =
                    hash + "u" + performer.getId()
                    + Constants.DOT + ext;
            dbFilePath = dbFilePath + fileName;
            filePath = filePath + Constants.SLASH + fileName;
            File file = filesUploader.upload(filePath, mpfile);
            performer.setImgPath(dbFilePath);
            performerService.update(performer);
            sendDefaultJson(response, true, Constants.EMPTY_STRING);
        } else {
            sendDefaultJson(response, false, "Server Internal Error. Please try later");
        }
    }
}
