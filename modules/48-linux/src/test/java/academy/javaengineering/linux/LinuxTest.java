package academy.javaengineering.linux;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Linux Tests")
class LinuxTest {

    @Test
    @DisplayName("LinuxEnvironment should return PATH")
    void testGetPath() {
        var env = new LinuxEnvironment();
        String path = env.getPath();
        assertNotNull(path);
        assertFalse(path.isEmpty());
    }

    @Test
    @DisplayName("LinuxEnvironment should return HOME")
    void testGetHome() {
        var env = new LinuxEnvironment();
        String home = env.getHomeDirectory();
        assertNotNull(home);
        assertTrue(home.startsWith("/"));
    }

    @Test
    @DisplayName("LinuxEnvironment should return USER")
    void testGetUser() {
        var env = new LinuxEnvironment();
        String user = env.getUserName();
        assertNotNull(user);
        assertFalse(user.isEmpty());
    }
}
