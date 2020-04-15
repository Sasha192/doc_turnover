package app.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public abstract class JsonSupportController {

    protected void writeToResponseComplexMapKeySerialization(HttpServletResponse response,
                                                             Map<String, Object> map)
            throws IOException {
        GsonBuilder builder = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .setPrettyPrinting();
        writeToResponse(response, builder, map);
    }


    protected void writeToResponse(HttpServletResponse response,
                                   Map<String, Object> map)
            throws IOException {
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        writeToResponse(response, builder, map);
    }

    protected void writeToResponse(HttpServletResponse response,
                                   GsonBuilder builder,
                                   Object data)
            throws IOException {
        Gson gson = builder.create();
        try (BufferedWriter out
                     = new BufferedWriter(response.getWriter())) {
            gson.toJson(data, out);
        }
    }

    protected void writeToResponse(HttpServletResponse response,
                                   TypeAdapter adapter, TypeToken typeToken,
                                   Object data)
            throws IOException {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(typeToken.getType(), adapter).setPrettyPrinting();
        writeToResponse(response, builder, data);

    }

    protected void writeToResponse(HttpServletResponse response,
                                   TypeAdapterFactory factory,
                                   Object data)
            throws IOException {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapterFactory(factory).setPrettyPrinting();
        writeToResponse(response, builder, data);
    }
}
