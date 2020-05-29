package app.service.extapis;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientFactory {

    public static HttpClient getClient() {
        return new DefaultHttpClient();
    }

}
