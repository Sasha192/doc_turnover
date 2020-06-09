package app.service.extapis;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpClientFactory {

    // @TODO Create more advanced configuration : managing connection pool etc..

    public static HttpClient getClient() {
        return new DefaultHttpClient();
    }

}
