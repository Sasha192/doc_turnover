package app.utils;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface IUploader {

    boolean upload(MultipartFile... files) throws IOException;

}
