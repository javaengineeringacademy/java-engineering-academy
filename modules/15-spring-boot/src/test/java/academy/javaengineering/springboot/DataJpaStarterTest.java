package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DataJpaStarterTest {

    @Test
    void shouldSaveAndFindEntity() {
        DataJpaStarterExample.SimpleRepository repo = new DataJpaStarterExample.SimpleRepository();
        DataJpaStarterExample.Entity entity = new DataJpaStarterExample.Entity(1L, "Test");
        repo.save(entity);
        assertEquals("Test", repo.findById(1L).getName());
    }

    @Test
    void shouldDeleteEntity() {
        DataJpaStarterExample.SimpleRepository repo = new DataJpaStarterExample.SimpleRepository();
        repo.save(new DataJpaStarterExample.Entity(1L, "Test"));
        repo.delete(1L);
        assertNull(repo.findById(1L));
    }
}
