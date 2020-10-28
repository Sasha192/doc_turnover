package app.customtenant.service.extapis;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientFactory {

    public static HttpClient client;

    public static HttpClient getClient() {
        return HttpClients.createDefault();
    }

}
