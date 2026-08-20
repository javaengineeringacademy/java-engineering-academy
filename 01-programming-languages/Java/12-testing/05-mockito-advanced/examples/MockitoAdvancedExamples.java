package academy.javaengineering.testing.mockito.advanced.examples;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MockitoAdvancedExamples {

    static class User {
        private String name;
        private String email;
        User(String name, String email) { this.name = name; this.email = email; }
        String getName() { return name; }
        String getEmail() { return email; }
        void setName(String name) { this.name = name; }
    }

    interface UserRepository {
        User findById(Long id);
        void save(User user);
    }

    @Spy
    private UserRepository spyRepo = new UserRepository() {
        public User findById(Long id) { return new User("real", "real@test.com"); }
        public void save(User user) {}
    };

    @Mock
    private UserRepository mockRepo;

    @Nested
    class SpyExamples {
        @Test
        void shouldCallRealMethodByDefault() {
            User user = spyRepo.findById(1L);
            assertEquals("real", user.getName());
        }

        @Test
        void shouldStubSpecificMethod() {
            given(spyRepo.findById(1L)).willReturn(new User("stubbed", "stub@test.com"));
            User user = spyRepo.findById(1L);
            assertEquals("stubbed", user.getName());
        }
    }

    @Nested
    class BDDExamples {
        @Test
        void shouldUseBDDStyle() {
            given(mockRepo.findById(1L)).willReturn(new User("Alice", "alice@test.com"));
            User user = mockRepo.findById(1L);
            then(mockRepo).should().findById(1L);
            assertEquals("Alice", user.getName());
        }
    }

    @Nested
    class VoidMethodExamples {
        @Test
        void shouldStubVoidWithAnswer() {
            ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
            User user = new User("test", "test@test.com");
            mockRepo.save(user);
            verify(mockRepo).save(captor.capture());
            assertEquals("test", captor.getValue().getName());
        }
    }

    @Nested
    class CustomAnswerExamples {
        @Test
        void shouldUseCustomAnswer() {
            when(mockRepo.findById(anyLong())).thenAnswer(invocation -> {
                Long id = invocation.getArgument(0);
                return new User("User" + id, "user" + id + "@test.com");
            });
            User user = mockRepo.findById(42L);
            assertEquals("User42", user.getName());
        }
    }
}
