package app.configuration.spring.constants;

import app.models.CoreProperty;
import app.service.ICorePropertyService;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("app_constants")
public class Constants {

    public static final String DOT = ".".intern();
    public static final String SLASH = "/".intern();
    public static final FileNameMap CONTENT_TYPE_MAP = URLConnection.getFileNameMap();
    public static final String EMPTY_STRING = "".intern();
    public static final String IS_MALICIOUS = " is malicious or can not be uploaded";
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");

    private String pathToArchive;

    private ICorePropertyService corePropertyService;

    private Map<String, CoreProperty> properties;

    @Autowired
    public Constants(ICorePropertyService corePropertyService) {
        this.corePropertyService = corePropertyService;
    }

    @PostConstruct
    private void init() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        List<CoreProperty> properties = corePropertyService.findAll();
        for (CoreProperty property : properties) {
            this.properties.put(property.getName(), property);
        }
        pathToArchive = this.properties.get("path_to_archive").getStringValue();
    }

    public CoreProperty retrieveByName(String name) {
        return properties.get(name);
    }

    public String getPathToArchive() {
        return this.pathToArchive;
    }

    // @TODO refresh values and names every __what time__ ????
}
