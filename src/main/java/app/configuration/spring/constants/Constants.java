package app.configuration.spring.constants;

import app.customtenant.models.basic.CoreProperty;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.ICorePropertyService;
import com.google.gson.GsonBuilder;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Constants class - store all vital constants for app.
 * Using ConcurrentHashMap is required for sync with DB and decreasing
 * number of requests to DB.
 * ConcurrentHashMap uses row-locking mechanism for updates, inserts
 * and no-locking for reading
 */
@Component("app_constants")
public class Constants {

    public static final String DOT = ".";
    public static final String SLASH = "/";
    public static final FileNameMap CONTENT_TYPE_MAP = URLConnection.getFileNameMap();
    public static final String EMPTY_STRING = "";
    public static final String IS_MALICIOUS = " is malicious or can not be uploaded";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static List<String> appImageFormats;
    public static final long DAY_IN_MS = 1000 * 60 * 60 * 24;
    public static final GsonBuilder BUILDER_BRIEF;
    public static final GsonBuilder BUILDER_DETAILS;
    public static final String MAX_FILES_UPLOAD = "max_files_upload";
    public static final String MAX_FILES_DOWNLOAD = "max_files_download";
    public static final String SPRING_SECURITY_CONTEXT_KEY = org.springframework
                    .security
                    .web.context
                    .HttpSessionSecurityContextRepository
                    .SPRING_SECURITY_CONTEXT_KEY;
    public static final String PERFORMER_SESSION_KEY =
            "PERFORMER_SESSION_KEY";
    public static final String CUSTOM_USER_SESSION_KEY =
            "CUSTOM_USER_SESSION_KEY";
    public static final String REMEMBER_ME_UUID;
    public static final String REMEMBER_ME_ID;
    public static final int MAX_INACTIVE_SESSION_INTERVAL_SECONDS = 60 * 60;
    public static final String[] RESOURCES_PATH = new String[] {
            "/css", "/scripts",
            "/fonts", "/img",
            "/libs", "/partials"
    };
    public static final int VALID_REMEMBER_ME_TOKEN_TIME_SEC =
            60 * 60 * 24 * 7;
    public static final String TENANT_SESSION_ID = "tenant_id";

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
        Base64.Encoder enc = Base64.getEncoder();
        REMEMBER_ME_UUID = enc.encodeToString(
                        "REMEMBERMEUUID"
                                .getBytes(StandardCharsets.UTF_8)
                );
        REMEMBER_ME_ID = enc.encodeToString(
                        "REMEMBERMEID"
                                .getBytes(StandardCharsets.UTF_8)
                );
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
        CoreProperty prop = properties.get(name);
        if (prop == null) {
            prop = corePropertyService.retrieveByName(name);
        }
        return prop;
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
        if (corePropertyService.retrieveByName(newProperty.getName()) == null) {
            corePropertyService.update(property);
        } else {
            corePropertyService.create(newProperty);
        }
    }

    @Autowired
    // because writing logic with BeanPostProcessors is longer
    // this approach much faster
    private void setAppImageFormats(Environment e) {
        if (e.getProperty("img_ext") != null) {
            appImageFormats = Arrays.asList(
                    e.getProperty("img_ext")
                            .split(",")
            );
        } else {
            appImageFormats = Arrays.asList(
                    ".jpeg,.jpg,.png,.gif,.tiff"
                            .split(",")
            );
        }
    }

    public void updateConstants() {
        List<CoreProperty> properties = corePropertyService.findAll();
        for (CoreProperty property : properties) {
            this.properties.put(property.getName(), property);
        }
    }

    /*public static final String BAD_REQUEST_REGEX =
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
            + "|(\\/partials.*)";*/
}
