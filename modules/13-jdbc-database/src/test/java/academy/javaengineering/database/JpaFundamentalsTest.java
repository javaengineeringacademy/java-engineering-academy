package academy.javaengineering.database;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class JpaFundamentalsTest {

    private JpaFundamentalsExample.Repository<String> repo;

    @BeforeEach
    void setUp() {
        repo = new JpaFundamentalsExample.InMemoryRepository<>();
    }

    @Test
    void shouldSaveAndFind() {
        repo.save("User1");
        assertEquals("User1", repo.findById(1L).orElse("Not found"));
    }

    @Test
    void shouldDelete() {
        repo.save("User1");
        repo.delete(1L);
        assertFalse(repo.findById(1L).isPresent());
    }

    @Test
    void shouldFindAll() {
        repo.save("User1");
        repo.save("User2");
        assertEquals(2, repo.findAll().size());
    }
}
