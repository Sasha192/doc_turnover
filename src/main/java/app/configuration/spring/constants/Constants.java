package app.configuration.spring.constants;

import app.models.basic.CoreProperty;
import app.models.serialization.ExcludeStrategies;
import app.service.interfaces.ICorePropertyService;
import com.google.gson.GsonBuilder;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 *
 *     Constants class - store all vital constants for app.
 *     Using ConcurrentHashMap is required for sync with DB and decreasing
 *     number of requests to DB.
 *     ConcurrentHashMap uses row-locking mechanism for updates, inserts
 *     and no-locking for reading
 *
 *
 */
@Component("app_constants")
public class Constants {

    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final FileNameMap CONTENT_TYPE_MAP = URLConnection.getFileNameMap();
    public static final String EMPTY_STRING = "";
    public static final String IS_MALICIOUS = " is malicious or can not be uploaded";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    public static final GsonBuilder BUILDER_BRIEF;

    public static final GsonBuilder BUILDER_DETAILS;

    public static final String MAX_FILES_UPLOAD = "max_files_upload";

    public static final String MAX_FILES_DOWNLOAD = "max_files_download";

    public static final String SPRING_SECURITY_CONTEXT_KEY =
            org
            .springframework
            .security
            .web.context
            .HttpSessionSecurityContextRepository
            .SPRING_SECURITY_CONTEXT_KEY;

    public static final String PERFORMER_SESSION_KEY = "PERFORMER_SESSION_KEY";

    public static final String BAD_REQUEST_REGEX =
            "(?!"
                    + "(\\/[a-zA-Z]+)+"
                    + "(\\?"
                    + "(\\&{0,1}"
                    + "[a-zA-Z]+"
                    + "\\={1}"
                    + "[a-zA-Zа-яА-ЯЁІіЙйЪъЇї0-9" + "\\s" + "]+"
                    + ")+"
                    + "){0,1}"
                    + "\\/{0,1}"
                    + "$).*";

    public static final String RESOURCES_PATH_REGEX = "(\\/css.*)"
            + "|(\\/scripts.*)"
            + "|(\\/fonts.*)"
            + "|(\\/img.*)"
            + "|(\\/libs.*)"
            + "|(\\/partials.*)";

    static {
        BUILDER_BRIEF = new GsonBuilder().setPrettyPrinting()
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_COMMENT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_REPORT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_BRIEF_TASK)
                .setDateFormat(Constants.DATE_FORMAT.toPattern());
        BUILDER_DETAILS = new GsonBuilder().setPrettyPrinting()
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_COMMENT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_REPORT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .setDateFormat(Constants.DATE_FORMAT.toPattern());
    }

    private ICorePropertyService corePropertyService;
    private ConcurrentHashMap<String, CoreProperty> properties;

    @Autowired
    public Constants(ICorePropertyService corePropertyService) {
        this.corePropertyService = corePropertyService;
    }

    @PostConstruct
    private void init() {
        if (properties == null) {
            properties = new ConcurrentHashMap<>();
        }
        updateConstants();
    }

    public CoreProperty get(String name) {
        return properties.get(name);
    }

    public void put(CoreProperty newProperty) {
        CoreProperty property = corePropertyService
                .retrieveByName(newProperty.getName());
        if (property != null) {
            if (!(property.equals(newProperty))) {
                switch (property.getTypeValue()) {
                    case NUMBER: {
                        property.setIntValue(
                                newProperty.getIntValue()
                        );
                        break;
                    }
                    case STRING: {
                        property.setStringValue(
                                newProperty.getStringValue()
                        );
                        break;
                    }
                    case DECIMAL: {
                        property.setFloatValue(
                                newProperty.getFloatValue()
                        );
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        }
        corePropertyService.update(property);
    }

    @Scheduled(fixedDelay = 1_000 * 60 * 10)
    public void updateConstants() {
        List<CoreProperty> properties = corePropertyService.findAll();
        for (CoreProperty property : properties) {
            this.properties.put(property.getName(), property);
        }
    }
}
