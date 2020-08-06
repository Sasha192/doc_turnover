package app.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IUploader {

    boolean upload(MultipartFile... files) throws IOException;

}
