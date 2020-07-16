package app.utils;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.StringReader;

public class JsonUtil {

    /**
     * Motivation : there could be large Json and we need only specific values.
     * I helps me to retrieve value from it.
     * <p>
     * <p>
     * This methods dive into jsonString and slices jsonObject, that you want :
     * e.g names = { "name3", "name3.1" } jsonString = {
     * name1 : val1,
     * name2 : val2,
     * name3 : {
     * name3.1 : val 3.1
     * }
     * name4 : {
     * ...
     * }
     * name5 : [
     * ...
     * ]
     * }
     * returns -> JsonElement val3.1 (it could be another JsonObject or JsonPrimitive etc)
     * <p>
     * if u have : jsonString = {
     * {
     * *      name1 : val1,
     * *      name2 : val2,
     * *      name3 : {
     * *          name3.1 : val_3.1
     * *      }
     * *     name4 : {
     * *         ...
     * *     }
     * *     name5 : [
     * *         ...
     * *     ]
     * * }
     * <p>
     * {
     * *      name12 : val1,
     * *      name22. : val2,
     * *      name3 : {
     * *          name3.1 : another_val_3.1
     * *      }
     * *     name4 : {
     * *         ...
     * *     }
     * *     name5 : [
     * *         ...
     * *     ]
     * * }
     * <p>
     * }
     * returns -> val_3.1
     *
     * @param jsonString : input data
     * @param names      : array of nested names, see example above
     * @return JsonElement : value of the last nested JsonToken.NAME
     * @throws IOException :
     */
    public static JsonElement findRecursively(String jsonString, String[] names)
            throws IOException {
        return findRecursively(jsonString, names, 0);
    }

    private static JsonElement findRecursively(String jsonString, String[] names, int iter)
            throws IOException {
        if (iter < 0) {
            return JsonParser.parseString(jsonString);
        }
        JsonReader jsonReader = new JsonReader(new StringReader(jsonString));
        jsonReader.setLenient(true);
        JsonToken nextToken = JsonToken.END_DOCUMENT;
        loop:
        while ((nextToken = jsonReader.peek()) != JsonToken.END_DOCUMENT) {
            switch (nextToken) {
                case BEGIN_OBJECT: {
                    jsonReader.beginObject();
                    break;
                }
                case NAME: {
                    String name = jsonReader.nextName();
                    if (name.equals(names[iter])) {
                        iter++;
                        if (!(iter < names.length)) {
                            return sliceJsonObject(jsonReader);
                        }
                    } else {
                        jsonReader.skipValue();
                    }
                    break;
                }
                case STRING: {
                    jsonReader.nextString();
                    break;
                }
                case NUMBER: {
                    jsonReader.nextLong();
                    break;
                }
                case NULL: {
                    jsonReader.nextNull();
                    break;
                }
                case END_OBJECT: {
                    jsonReader.endObject();
                    break;
                }
                case BEGIN_ARRAY: {
                    jsonReader.beginArray();
                    break;
                }
                case END_ARRAY: {
                    jsonReader.endArray();
                    break;
                }
                case BOOLEAN: {
                    jsonReader.nextBoolean();
                    break;
                }
                default: {
                    break loop;
                }
            }
        }
        return null;
    }

    private static JsonElement sliceJsonObject(JsonReader jsonReader) throws IOException {
        JsonObject jsonObject = new JsonObject();
        String prevName = null;
        JsonElement prevValue = null;
        JsonToken nextToken = JsonToken.END_DOCUMENT;
        loop:
        while ((nextToken = jsonReader.peek()) != JsonToken.END_DOCUMENT) {
            switch (nextToken) {
                case BEGIN_OBJECT: {
                    jsonReader.beginObject();
                    if (prevName != null) {
                        jsonObject.add(prevName, sliceJsonObject(jsonReader));
                    }
                    break;
                }
                case NAME: {
                    prevName = jsonReader.nextName();
                    break;
                }
                case NULL: {
                    jsonReader.nextNull();
                    if (prevName != null) {
                        jsonObject.add(prevName, null);
                    } else {
                        return prevValue;
                    }
                    break;
                }
                case NUMBER: {
                    String number = jsonReader.nextString();
                    if (number.matches("([0-9]+)\\.([0-9]+)")) {
                        prevValue = new JsonPrimitive(Double.parseDouble(number));
                    } else {
                        prevValue = new JsonPrimitive(Long.parseLong(number));
                    }
                    if (prevName != null) {
                        jsonObject.add(prevName, prevValue);
                    } else {
                        return prevValue;
                    }
                    break;
                }
                case STRING: {
                    String nextString = jsonReader.nextString();
                    prevValue = new JsonPrimitive(nextString);
                    if (prevName != null) {
                        jsonObject.add(prevName, prevValue);
                    } else {
                        return prevValue;
                    }
                    break;
                }
                case BOOLEAN: {
                    final boolean bool = jsonReader.nextBoolean();
                    prevValue = new JsonPrimitive(bool);
                    if (prevName != null) {
                        jsonObject.add(prevName, prevValue);
                    } else {
                        return prevValue;
                    }
                    break;
                }
                case BEGIN_ARRAY: {
                    jsonReader.beginArray();
                    if (prevName != null) {
                        jsonObject.add(prevName, sliceArray(jsonReader));
                    } else {
                        return sliceArray(jsonReader);
                    }
                    break;
                }
                case END_ARRAY: {
                    jsonReader.endArray();
                    break;
                }
                case END_OBJECT: {
                    jsonReader.endObject();
                    return jsonObject;
                }
                default: {
                    break loop;
                }
            }
        }
        return jsonObject;
    }

    private static JsonArray sliceArray(JsonReader jsonReader) throws IOException {
        JsonArray jsonArray = new JsonArray();
        JsonToken nextToken = null;
        String prevName = null;
        JsonElement prevValue = null;
        loop:
        while ((nextToken = jsonReader.peek()) != JsonToken.END_DOCUMENT) {
            switch (nextToken) {
                case BEGIN_OBJECT: {
                    jsonReader.beginObject();
                    jsonArray.add(sliceJsonObject(jsonReader));
                    break;
                }
                case NULL: {
                    jsonReader.nextNull();
                    break;
                }
                case NUMBER: {
                    String number = jsonReader.nextString();
                    if (number.matches("([0-9]+)\\.([0-9]+)")) {
                        prevValue = new JsonPrimitive(Double.parseDouble(number));
                    } else {
                        prevValue = new JsonPrimitive(Long.parseLong(number));
                    }
                    jsonArray.add(prevValue);
                    break;
                }
                case STRING: {
                    String nextString = jsonReader.nextString();
                    prevValue = new JsonPrimitive(nextString);
                    jsonArray.add(prevValue);
                    break;
                }
                case BOOLEAN: {
                    boolean bool = jsonReader.nextBoolean();
                    prevValue = new JsonPrimitive(bool);
                    jsonArray.add(prevValue);
                    break;
                }
                case BEGIN_ARRAY: {
                    jsonReader.beginArray();
                    jsonArray.add(sliceArray(jsonReader));
                    break;
                }
                case END_ARRAY: {
                    jsonReader.endArray();
                    return jsonArray;
                }
                default: {
                    break loop;
                }
            }
        }
        return jsonArray;
    }
}
