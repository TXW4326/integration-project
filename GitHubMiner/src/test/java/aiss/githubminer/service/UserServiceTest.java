package aiss.githubminer.service;

import aiss.githubminer.model.User;
import aiss.githubminer.utils.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    @DisplayName("Test getUser() method")
    void getUser() throws Exception {
        String username = "octocat";

        User user = userService.getUser(username);
        testUser(user);
        assertEquals(username, user.getUsername(), "User login should match the requested username");
        assertEquals(583231, user.getId(), "User ID should match");
        assertEquals("https://avatars.githubusercontent.com/u/583231?v=4", user.getAvatar_url(), "User avatar URL should match");
        assertEquals("The Octocat", user.getName(), "User name should match");
        assertEquals("https://github.com/octocat", user.getWeb_url(), "User web URL should match");

        System.out.println(JsonUtils.toJson(user));
    }

    public static void testUser(User user) {
        assertNotNull(user, "User should not be null");
        assertNotNull(user.getUsername(), "User login should not be null");
        assertFalse(user.getUsername().isEmpty(), "User login should not be empty");
        assertTrue(user.getId() >= 0, "User ID should be a non-negative integer");
        assertNotNull(user.getAvatar_url(), "User avatar URL should not be null");
        assertFalse(user.getAvatar_url().isEmpty(), "User avatar URL should not be empty");
        assertNotNull(user.getWeb_url(), "User web URL should not be null");
        assertFalse(user.getWeb_url().isEmpty(), "User web URL should not be empty");
        assertTrue(user.getName() == null || !user.getName().isEmpty(), "User name should not be empty");
    }
}