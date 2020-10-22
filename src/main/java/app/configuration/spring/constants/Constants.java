package app.configuration.spring.constants;

import app.customtenant.models.basic.CoreProperty;
import app.customtenant.models.serialization.ExcludeStrategies;
import app.customtenant.service.interfaces.ICorePropertyService;
import app.tenantconfiguration.TenantContext;
import com.google.gson.GsonBuilder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
    public static final String EMPTY_STRING = "";
    public static final String IS_MALICIOUS = " is malicious or can not be uploaded";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter
            .ofPattern(DATE_FORMAT.toPattern());
    public static final GsonBuilder BUILDER_JSON_EVENTS;
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
    public static final int DEFAULT_PAGE_SIZE = 30;

    static {
        BUILDER_BRIEF = new GsonBuilder().setPrettyPrinting()
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_BRIEF_TASK)
                .setDateFormat(Constants.DATE_FORMAT.toPattern());
        BUILDER_DETAILS = new GsonBuilder().setPrettyPrinting()
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_COMMENT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_REPORT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_BRIEF_TASK)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_BDOCS)
                .setDateFormat(Constants.DATE_FORMAT.toPattern());
        BUILDER_JSON_EVENTS = new GsonBuilder()
                .setExclusionStrategies(ExcludeStrategies.EXCLUDE_FOR_JSON_EVENT)
                .setDateFormat(Constants.DATE_FORMAT.toPattern())
                .setPrettyPrinting();
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
        return properties.get(name);
    }

    @Autowired
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
        String tenant = TenantContext.getTenant();
        TenantContext.setTenant(TenantContext.DEFAULT_TENANT_IDENTIFIER);
        List<CoreProperty> properties = corePropertyService.findAll();
        for (CoreProperty property : properties) {
            this.properties.put(property.getName(), property);
        }
        if (tenant == null) {
            TenantContext.reset(tenant);
        } else {
            TenantContext.setTenant(tenant);
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
