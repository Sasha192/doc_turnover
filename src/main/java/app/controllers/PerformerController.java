package app.controllers;

import app.configuration.spring.constants.Constants;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.IPerformerService;
import app.security.models.auth.CustomUser;
import app.security.service.IUserService;
import app.security.utils.DefaultPasswordEncoder;
import app.security.wrappers.IAuthenticationWrapper;
import app.security.wrappers.IPerformerWrapper;
import app.utils.FilesUploader;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
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

    private static final Logger INTLOGGER = Logger.getLogger("intExceptionLogger");

    private IUserService userService;
    private IAuthenticationWrapper authenticationWrapper;
    private final IPerformerWrapper performerWrapper;
    private IPerformerService performerService;
    private DefaultPasswordEncoder encoder;
    private final FilesUploader filesUploader;
    private final Constants constants;

    @Autowired
    public PerformerController(IUserService userService,
                               IAuthenticationWrapper authenticationWrapper,
                               IPerformerService performerService,
                               DefaultPasswordEncoder encoder,
                               IPerformerWrapper performerWrapper,
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
                .getFile("classpath:static/img/avatars");
        if (folder.exists()) {
            String ext = Constants.DOT + FilenameUtils
                    .getExtension(mpfile.getOriginalFilename());
            if (!checkForAppropriateImageExtension(ext)) {
                sendDefaultJson(response, false, "");
                return;
            }
            updatePerformerImagePath(performer, mpfile, ext, folder);
            sendDefaultJson(response, true, Constants.EMPTY_STRING);
        } else {
            sendDefaultJson(response, false, "Server Internal Error. Please try later");
        }
    }

    private void updatePerformerImagePath(Performer performer,
                                          MultipartFile mpfile,
                                          String ext,
                                          File folder)
            throws IOException {
        String dbFilePath = "/img/avatars/";
        String fileName = generateFileName(performer, ext);
        dbFilePath = dbFilePath + fileName;
        String filePath = folder.getAbsolutePath();
        filePath = filePath + Constants.SLASH + fileName;
        File file = filesUploader.upload(filePath, mpfile);
        performer.setImgPath(dbFilePath);
        performerService.update(performer);
    }

    private boolean checkForAppropriateImageExtension(String imgExt) {
        List<String> strings = Constants.appImageFormats;
        return strings.contains(imgExt);
    }

    private String generateFileName(Performer performer, String ext) {
        return performer.getImgIdToken() + Constants.DOT + ext;
    }
}
