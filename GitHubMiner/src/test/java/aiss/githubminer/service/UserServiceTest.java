package aiss.githubminer.service;

import aiss.githubminer.exception.GitHubMinerException;
import aiss.githubminer.model.User;
import aiss.githubminer.utils.JsonUtils;
import aiss.githubminer.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    @DisplayName("Get user with valid username")
    void getUser() {
        String username = "spring-projects";

        User user = userService.getUser(username);
        testUser(user);
        assertEquals(username, user.getUsername(), "User login should match the requested username");
        assertEquals(317776, user.getId(), "User ID should match");
        assertEquals("https://avatars.githubusercontent.com/u/317776?v=4", user.getAvatar_url(), "User avatar URL should match");
        assertEquals("Spring", user.getName(), "User name should match");
        assertEquals("https://github.com/spring-projects", user.getWeb_url(), "User web URL should match");

        System.out.println(JsonUtils.toJson(user));
    }

    @Test
    @DisplayName("Get user with invalid username")
    void getUserInvalid() {
        String username = "invalid-usernamevygbhuygvfytfvcdf";

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () -> userService.getUser(username), "Should throw GitHubMinerException for invalid username");

        TestUtils.assertException(ex, HttpStatus.NOT_FOUND);
        assertEquals("User not found: " + username, ex.getMessage(), "Exception message should indicate user not found");

        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get user with empty username")
    void getUserEmpty() {
        String username = "";

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () -> userService.getUser(username), "Should throw GitHubMinerException for empty username");

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Username is empty", ex.getMessage(), "Exception message should indicate empty username");

        System.out.println(ex.getMessage());
    }

    @Test
    @DisplayName("Get user with null username")
    void getUserNull() {
        String username = null;

        GitHubMinerException ex = assertThrows(GitHubMinerException.class, () -> userService.getUser(username), "Should throw GitHubMinerException for null username");

        TestUtils.assertException(ex, HttpStatus.BAD_REQUEST);
        assertEquals("Username is null", ex.getMessage(), "Exception message should indicate null username");

        System.out.println(ex.getMessage());
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