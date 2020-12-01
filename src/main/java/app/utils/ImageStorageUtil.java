package app.utils;

import app.configuration.spring.constants.Constants;
import app.security.models.auth.UserInfo;
import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ImageStorageUtil implements IimageStorage {

    private MaliciousDocumentsScanUtil scanUtil;

    private final Constants constants;

    @Autowired
    public ImageStorageUtil(MaliciousDocumentsScanUtil scanUtil,
                            Constants constants) {
        this.scanUtil = scanUtil;
        this.constants = constants;
    }

    @Override
    public void upload(MultipartFile mfile, UserInfo info)
            throws IOException {
        String fileName = StringToUtf8Utils
                .convertToUtf8(
                        mfile.getOriginalFilename()
                );
        String extension = FileExtensionUtil.getExtension(fileName);
        if (!Constants.appImageFormats.contains(extension)) {
            return;
        }
        fileName = info.getImgIdToken() + extension;
        String fileFullPath = new ClassPathResource("/static/img")
                .getFile()
                .getAbsolutePath();
        fileFullPath = fileFullPath
                + '/'
                + fileName;
        File file = new File(fileFullPath);
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        mfile.transferTo(file);
        if (scanUtil.check(file) && Constants
                .appImageFormats.contains(extension)) {
            String staticPath = "/img";
            staticPath = staticPath + '/'
                    + (fileName);
            info.setImgPath(staticPath);
        } else {
            file.delete();
            info.setImgPath(Constants.IMG_DEFAULT);
        }
    }
}
