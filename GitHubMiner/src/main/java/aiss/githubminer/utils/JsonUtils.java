package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;

//Class to handle JSON serialization using Jackson
public final class JsonUtils {

    private JsonUtils() {}

    private static final ObjectWriter ow = new ObjectMapper().registerModule(new JavaTimeModule()).writer().withDefaultPrettyPrinter();

    public static String toJson(Object obj) {
        try {
            return ow.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new GitHubMinerException(HttpStatus.INTERNAL_SERVER_ERROR, "Error serializing object to JSON: " + obj);
        }

    }
}
