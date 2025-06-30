package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;

//Class to handle JSON serialization using Jackson
public final class JsonUtils {

    private JsonUtils() {}

    private static final ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();
    private static final ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String toJson(Object obj) {
        try {
            return ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Error serializing object to JSON: " + obj);
        }

    }

    public static <T> T convertToObject(Object value, Class<T> clazz) {
        try {
            return om.convertValue(value, clazz);
        } catch (IllegalArgumentException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deserializing object to JSON: " + value);
        }
    }

    public static <T> T convertToObject(Object value, TypeReference<T> typeReference) {
        try {
            return om.convertValue(value, typeReference);
        } catch (IllegalArgumentException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deserializing object to JSON: " + value);
        }
    }
}
