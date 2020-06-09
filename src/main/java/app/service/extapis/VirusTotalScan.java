package app.service.extapis;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VirusTotalScan implements IMaliciousScan {

    private static final String UPLOAD_URL = "https://www.virustotal.com/api/v3/files";
    private static final String ANALYSIS_URL = "https://www.virustotal.com/api/v3/analyses/";
    private static final String X_API_KEY_VT = "309224716a9439960a3546054af7cbf68696fcb3888b587964664caf86f0a1ed";
    private static final int THRESHOLD = 7;

    private HttpClient client;

    // @TODO Benchmarking JsonReader vs JsonParser.

    @Autowired
    public VirusTotalScan() {
        client = HttpClientFactory.getClient();
    }

    @Override
    public boolean scan(File file) {
        try {
            String id = getIdForFile(file);
            HttpGet request = new HttpGet(ANALYSIS_URL.concat(id));
            request.setHeader("x-apikey", X_API_KEY_VT);
            HttpResponse response = client.execute(request);
            JsonObject jsonObject = responseToJson(response)
                    .getAsJsonObject("data")
                    .getAsJsonObject("attributes")
                    .getAsJsonObject("stats");
            int harmless = jsonObject.get("harmless").getAsInt();
            int malicious = jsonObject.get("malicious").getAsInt();
            int suspicious = jsonObject.get("suspicious").getAsInt();
            if (harmless == 0 && malicious == 0 && (suspicious < THRESHOLD)) {
                return true;
            }
        } catch (ClientProtocolException e) {
            ;
        } catch (IOException e) {
            ;
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
        request.setHeader("x-apikey", X_API_KEY_VT);
        HttpResponse response = client.execute(request);
        return responseToJson(response)
                .get("data")
                .getAsJsonObject()
                .get("id").getAsString();
    }

    private JsonObject responseToJson(HttpResponse response) {
        if (!(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)) {
            return null;
        }
        HttpEntity entity = response.getEntity();
        JsonObject jsonObject = null;
        try {
            if (entity == null) {
                return null;
            }
            String retSrc = EntityUtils.toString(entity);
            jsonObject = (JsonObject) JsonParser.parseString(retSrc);
            return jsonObject;
        } catch (ClassCastException | IOException e) {
            return null;
        }
    }

    private boolean scanResult(String jsonString) {
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        try {
            while (jsonReader.hasNext()) {
                JsonToken nextToken = jsonReader.peek();
                System.out.println(nextToken);

                if (JsonToken.BEGIN_OBJECT.equals(nextToken)) {

                    jsonReader.beginObject();

                } else if (JsonToken.NAME.equals(nextToken)) {

                    String name = jsonReader.nextName();
                    System.out.println(name);

                } else if (JsonToken.STRING.equals(nextToken)) {

                    String value = jsonReader.nextString();
                    System.out.println(value);

                } else if (JsonToken.NUMBER.equals(nextToken)) {

                    long value = jsonReader.nextLong();
                    System.out.println(value);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
