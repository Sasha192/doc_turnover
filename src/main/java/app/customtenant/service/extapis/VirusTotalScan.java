package app.customtenant.service.extapis;

import app.configuration.spring.constants.Constants;
import app.utils.JsonUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.IOException;
import javax.annotation.PostConstruct;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VirusTotalScan implements IMaliciousScan {

    private static final String UPLOAD_URL = "https://www.virustotal.com/api/v3/files";
    private static final String ANALYSIS_URL = "https://www.virustotal.com/api/v3/analyses/";
    private static final int THRESHOLD = 10;
    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("intExceptionLogger");
    private final String apiKeyVt;
    private HttpClient client;

    @Autowired
    public VirusTotalScan(Constants constants) {
        this.apiKeyVt = constants.get("x_api_key_vt").getStringValue();
    }

    @PostConstruct
    public void init() {
        client = HttpClientFactory.getClient();
    }

    @Override
    public boolean scan(File file) {
        try {
            String id = getIdForFile(file);
            HttpGet request = new HttpGet(ANALYSIS_URL.concat(id));
            request.setHeader("x-apikey", apiKeyVt);
            HttpResponse response = client.execute(request);
            JsonObject jsonObject = responseToJson(response, "data", "attributes", "stats")
                    .getAsJsonObject();
            int harmless = jsonObject.get("harmless").getAsInt();
            int malicious = jsonObject.get("malicious").getAsInt();
            int suspicious = jsonObject.get("suspicious").getAsInt();
            if (harmless == 0 && malicious == 0 && (suspicious < THRESHOLD)) {
                return true;
            }
        } catch (IOException e) {
            EXCEPTIONS_LOGGER.error("IOEXCEPTION WHILE SCANNING FILE FOR VIRUSES : ", e);
        }
        return false;
    }

    private String getIdForFile(File file) throws IOException {
        HttpEntity entity = MultipartEntityBuilder
                .create()
                .addBinaryBody("file", file)
                .build();
        HttpPost request = new HttpPost(UPLOAD_URL);
        request.setEntity(entity);
        request.setHeader("x-apikey", apiKeyVt);
        HttpResponse response = null;
        try {
            response = client.execute(request);
            return responseToJson(response, "data", "id").getAsString();
        } catch (IOException e) {
            EXCEPTIONS_LOGGER.error("IOEXCEPTION : " + e.getMessage());
            return null;
        } finally {
            request.releaseConnection();
        }
    }

    private JsonElement responseToJson(HttpResponse response, String... names) {
        if (!(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
            return null;
        }
        HttpEntity entity = response.getEntity();
        try {
            if (entity == null) {
                return null;
            }
            String retSrc = EntityUtils.toString(entity);
            JsonElement jsonObject = JsonUtil.findRecursively(retSrc, names);
            return jsonObject;
        } catch (IOException e) {
            EXCEPTIONS_LOGGER.error("IOEXCEPTION WHILE WORKING WITH VIRUSTOTAL RESPONSE!!! ", e);
            return null;
        }
    }
}
