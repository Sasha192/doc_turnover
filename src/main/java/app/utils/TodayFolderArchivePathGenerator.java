package app.utils;

import app.configuration.spring.constants.Constants;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component("date_file_path_generator")
public class TodayFolderArchivePathGenerator {

    private static final Logger LOGGER = Logger.getLogger("infoLooger");

    private static final Logger EXC_LOGGER = Logger.getLogger("intExceptionLogger");

    private volatile String folderPath;

    private volatile String pathToArchive;

    private Constants constants;

    public TodayFolderArchivePathGenerator(Constants constants) {
        this.constants = constants;
        this.pathToArchive = this.constants.get("path_to_archive")
                .getStringValue();
        this.folderPath = generate();
    }

    /**
     *
     *
     * @return String folderPath to Today Archive Folders
     * i.e. /archive/2020/08/20
     * @see TodayFolderArchivePathGenerator#generate()
     */
    public String getFolderArchivePath() {
        return folderPath;
    }

    @Scheduled(cron = "0 0 0 * * *")
    private synchronized String generate() {
        LOGGER.debug("START TODAY FOLDER GENERATOR : " + LocalDateTime.now());
        final LocalDate now = LocalDate.now();
        final int year = now.getYear();
        final int month = now.getMonthValue();
        final int day = now.getDayOfMonth();
        final String filePath = pathToArchive
                + (Constants.SLASH + year)
                + (Constants.SLASH + month)
                + (Constants.SLASH + day);
        File fileFolder = new File(filePath);
        boolean mkdirs = false;
        if (!fileFolder.exists()) {
            mkdirs = fileFolder.mkdirs();
        }
        if (mkdirs) {
            this.folderPath = filePath;
        } else {
            EXC_LOGGER.error("COULD NOT SET FOLDER PATH = " + filePath);
        }
        LOGGER.debug("FOLDER_PATH = " + this.folderPath);
        LOGGER.debug("ENDING TODAY FOLDER GENERATOR : " + LocalDateTime.now());
        return filePath;
    }

    @Scheduled(fixedDelay = (1_000 * 60 * 10 + 1_000 * 30))
    public void updatePathToArchive() {
        this.pathToArchive = this.constants.get("path_to_archive")
                .getStringValue();
    }

}
