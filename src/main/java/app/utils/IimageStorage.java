package app.utils;

import app.security.models.auth.UserInfo;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface IimageStorage {

    public void upload(MultipartFile file, UserInfo info) throws IOException;

}
