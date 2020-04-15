package app.service.serialization;

import app.models.serialization.NoOneToManySerialization;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import javax.persistence.OneToMany;

import org.apache.log4j.Logger;

public class NoOneToManySerializationFactory
        implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<?> clazz = typeToken.getRawType();
        if (!NoOneToManySerialization.class.isAssignableFrom(clazz)) {
            return null;
        }
        TypeAdapter<T> typeAdapter = new TypeAdapter<>() {

            private final Logger logger = Logger.getLogger(TypeAdapter.class);

            @Override
            public void write(JsonWriter out, T t) {
                Field[] fields = t.getClass().getDeclaredFields();
                try {
                    out.beginObject();
                    for (Field field : fields) {
                        if (field.getAnnotation(OneToMany.class) != null) {
                            continue;
                        }
                        field.setAccessible(true);
                        out.name(field.getName()).value(gson.toJson(field.get(t)));
                    }
                    out.endObject();
                } catch (IllegalAccessException | IOException e) {
                    logger.error(e.getMessage());
                    logger.error(Arrays.toString(e.getStackTrace()));
                }
            }

            @Override
            public T read(JsonReader in) {
                return null;
            }
        };
        return typeAdapter;
    }
}
