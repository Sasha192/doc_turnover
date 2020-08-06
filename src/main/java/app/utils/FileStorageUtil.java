package app.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.FileDeleteStrategy;
import org.springframework.stereotype.Component;

@Component
public class FileStorageUtil implements IStorage {

    @Override
    public boolean store(File... files) {
        return false;
    }

    @Override
    public boolean isExist(String filePath) {
        return false;
    }

    @Override
    public File retrieve(String filePath) {
        return null;
    }

    @Override
    public void remove(File file)
            throws IOException {
        FileDeleteStrategy.FORCE.delete(file);
    }

    @Override
    public void remove(List<File> files)
            throws IOException {
        for (final File file : files) {
            if (!file.delete()) {
                FileDeleteStrategy.FORCE.delete(file);
            }
        }
    }
}
