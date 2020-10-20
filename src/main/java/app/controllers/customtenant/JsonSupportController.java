package app.controllers.customtenant;

import app.controllers.responses.ResponseJsonText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedWriter;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class JsonSupportController {

    private static final Logger EXCEPTION_LOGGER = Logger.getLogger("intExceptionLogger");
    private static final String EXCEPTION_SENDING_JSON =
            "ERROR WHILE SENDING STANDART JSON RESPONSE : ";

    private static final GsonBuilder BUILDER = new GsonBuilder();

    protected static void sendDefaultJson(HttpServletResponse response, boolean b, String s) {
        try {
            GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();
            writeToResponse(response, gsonBuilder, new ResponseJsonText(b, s));
        } catch (IOException e) {
            EXCEPTION_LOGGER.error(EXCEPTION_SENDING_JSON, e);
        }
    }

    protected static void sendDefaultJson(HttpServletResponse response, Object o)
            throws IOException {
        writeToResponse(response, BUILDER, o);
    }

    protected static void writeToResponseComplexMapKeySerialization(HttpServletResponse response,
                                                             Object obj)
            throws IOException {
        GsonBuilder builder = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting();
        writeToResponse(response, builder, obj);
    }

    protected static void writeToResponse(HttpServletResponse response,
                                   GsonBuilder builder,
                                   Object data)
            throws IOException {
        Gson gson = builder.create();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (BufferedWriter out
                     = new BufferedWriter(response.getWriter())) {
            gson.toJson(data, out);
        }
    }

    protected static void writeToResponse(HttpServletResponse response,
                                   TypeAdapter adapter, TypeToken typeToken,
                                   Object data)
            throws IOException {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(typeToken.getType(), adapter).setPrettyPrinting();
        writeToResponse(response, builder, data);

    }

    protected static void writeToResponse(HttpServletResponse response,
                                   TypeAdapterFactory factory,
                                   Object data)
            throws IOException {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapterFactory(factory).setPrettyPrinting();
        writeToResponse(response, builder, data);
    }

    public Logger getExceptionLogger() {
        return JsonSupportController.EXCEPTION_LOGGER;
    }
}
