package aiss.githubminer.utils;

import aiss.githubminer.exception.GitHubMinerException;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public final class TestUtils {

    private TestUtils() {}

    public static <T> void assertMapContains(Map<?,?> parameters, String key, T expectedValue) {
        assertTrue(parameters.containsKey(key), "Parameters should contain '" + key + "'");
        assertNotNull(parameters.get(key), key + " should not be null");
        assertEquals(expectedValue, parameters.get(key), key + " should match expected");
    }

    public static Map<?,?> assertParametersInMap(Map<String,?> reason) {
        assertTrue(reason.containsKey("parameters"), "Parameters should be present in the error reason");
        assertNotNull(reason.get("parameters"), "Parameters should not be null");
        assertInstanceOf(Map.class, reason.get("parameters"), "Parameters should be a Map");
        return (Map<?,?>) reason.get("parameters");
    }

    public static void assertException(GitHubMinerException ex, HttpStatus expectedStatus) {
        assertNotNull(ex, "Exception should not be null");
        assertNotNull(ex.getMessage(), "Exception message should not be null");
        assertFalse(ex.getMessage().isEmpty(), "Exception message should not be empty");
        assertNotNull(ex.getReason(), "Exception reason should not be null");
        assertNotNull(ex.getStatus(), "Exception status should not be null");
        assertEquals(expectedStatus, ex.getStatus(), "Exception status should match expected status");
    }
}
