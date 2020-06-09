package app;

import com.cloudmersive.client.*;
import com.cloudmersive.client.invoker.ApiClient;
import com.cloudmersive.client.invoker.ApiException;
import com.cloudmersive.client.invoker.Configuration;
import com.cloudmersive.client.invoker.auth.ApiKeyAuth;
import com.cloudmersive.client.model.VirusScanAdvancedResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;

public class Main {

    private static final String UPLOAD_URL = "https://www.virustotal.com/api/v3/files";
    private static final String ANALYSIS_URL = "https://www.virustotal.com/api/v3/analyses/";
    private static final String X_API_KEY_VT = "309224716a9439960a3546054af7cbf68696fcb3888b587964664caf86f0a1ed";
    private static final int THRESHOLD = 7;

    private static String apiKey = "27ea15ab-149f-462c-85f4-c2a0fc566b65";

    public static void main(String[] args) {
        /*ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");
        Apikey.setApiKey(apiKey);
        ScanApi apiInstance = new ScanApi();
        File inputFile = new File("/home/kolmogorov/Java_Practice/bcrew/doc_turnover/src/main/webapp/archive/2020/5/27/10.1007_978-3-540-25937-4_25.bib"); // File | Input file to perform the operation on.
        Boolean allowExecutables = true; // Boolean | Set to false to block executable files (program code) from being allowed in the input file.  Default is false (recommended).
        Boolean allowInvalidFiles = true; // Boolean | Set to false to block invalid files, such as a PDF file that is not really a valid PDF file, or a Word Document that is not a valid Word Document.  Default is false (recommended).
        Boolean allowScripts = true; // Boolean | Set to false to block script files, such as a PHP files, Pythong scripts, and other malicious content or security threats that can be embedded in the file.  Set to true to allow these file types.  Default is false (recommended).
        Boolean allowPasswordProtectedFiles = true; // Boolean | Set to false to block password protected and encrypted files, such as encrypted zip and rar files, and other files that seek to circumvent scanning through passwords.  Set to true to allow these file types.  Default is false (recommended).
        String restrictFileTypes = "restrictFileTypes_example"; // String | Specify a restricted set of file formats to allow as clean as a comma-separated list of file formats, such as .pdf,.docx,.png would allow only PDF, PNG and Word document files.  All files must pass content verification against this list of file formats, if they do not, then the result will be returned as CleanResult=false.  Set restrictFileTypes parameter to null or empty string to disable; default is disabled.
        try {
            VirusScanAdvancedResult result = apiInstance.scanFileAdvanced(inputFile, allowExecutables, allowInvalidFiles, allowScripts, restrictFileTypes);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ScanApi#scanFileAdvanced");
            e.printStackTrace();
        }*/

        try {
            HttpClient client = new DefaultHttpClient();
            HttpEntity entity = MultipartEntityBuilder
                    .create()
                    .addBinaryBody("file",
                            new File(
                                    "/home/kolmogorov/Java_Practice/"
                                            + "bcrew/doc_turnover/src/main/webap"
                                            + "p/archive/2020/5/27/10.1007_978-3"
                                            + "-540-25937-4_25.bib"),
                            ContentType.create("application/octet-stream"), "10.1007_978-3"
                                    + "-540-25937-4_25.bib")
                    .build();
            HttpPost request = new HttpPost(UPLOAD_URL);
            request.setEntity(entity);
            request.setHeader("x-apikey", X_API_KEY_VT);
            HttpResponse response = client.execute(request);

        } catch (ClientProtocolException e) {
            ;
        } catch (IOException e) {
            ;
        }

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
}
