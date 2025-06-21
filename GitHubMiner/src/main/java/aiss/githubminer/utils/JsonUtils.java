package aiss.githubminer.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.Map;

public final class JsonUtils {

    private JsonUtils() {}

    private static final ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
    private static final ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String toJson(Object obj) {
        try {
            return ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            //TODO: Handle this exception properly
            throw new RuntimeException("Error converting object to JSON", e);
        }

    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return om.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            //TODO: Handle this exception properly
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return om.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting JSON to object", e);
        }
    }


    public static <T> T convertToObject(Object value, Class<T> clazz) {
        try {
            return om.convertValue(value, clazz);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error convirtiendo el objeto", e);
        }
    }

    public static <T> T convertToObject(Object value, TypeReference<T> typeReference) {
        try {
            return om.convertValue(value, typeReference);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error convirtiendo el objeto", e);
        }
    }
}
