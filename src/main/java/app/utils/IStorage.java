package app.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface IStorage {

    boolean store(File... files);

    boolean isExist(String filePath);

    File retrieve(String filePath);

    void remove(File file) throws IOException;

    void remove(List<File> files) throws IOException;
}
