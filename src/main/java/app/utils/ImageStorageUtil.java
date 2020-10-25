package app.utils;

import app.configuration.spring.constants.Constants;
import app.security.models.auth.UserInfo;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
                .encodeUtf8(
                        mfile.getOriginalFilename()
                );
        String extension = FileExtensionUtil.getExtension(fileName);
        extension = Constants.DOT + extension;
        if (scanUtil.check(mfile) && Constants
                .appImageFormats.contains(extension)) {
            String fileFullPath = constants
                    .get("full_path_to_image_storage")
                    .getStringValue();
            fileFullPath = fileFullPath + Constants.SLASH + info.getImgIdToken();
            File file = new File(fileFullPath);
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
            mfile.transferTo(file);
            String staticPath = constants
                    .get("static_resource_img")
                    .getStringValue();
            info.setImgPath(staticPath);
        }
    }
}
