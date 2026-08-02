package academy.javaengineering.springboot;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BestPracticesTest {

    @Test
    void shouldConvertBetweenDTOAndEntity() {
        BestPracticesExample.UserDTO dto = new BestPracticesExample.UserDTO(1L, "John", "john@test.com");
        BestPracticesExample.UserEntity entity = BestPracticesExample.UserEntity.fromDTO(dto);
        assertEquals(dto, entity.toDTO());
    }

    @Test
    void shouldCreateUserViaController() {
        BestPracticesExample.UserController controller = new BestPracticesExample.UserController();
        BestPracticesExample.UserDTO user = new BestPracticesExample.UserDTO(1L, "John", "john@test.com");
        controller.createUser(user);
        assertEquals(1, controller.getAllUsers().size());
    }
}
