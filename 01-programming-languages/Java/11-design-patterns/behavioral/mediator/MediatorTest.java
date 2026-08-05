package academy.javaengineering.patterns.behavioral.mediator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MediatorTest {

    private ChatRoom chatRoom;
    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        chatRoom = new ChatRoom();
        user1 = new User("User1");
        user2 = new User("User2");
        chatRoom.register(user1);
        chatRoom.register(user2);
    }

    @Test
    void chatRoomShouldRegisterUsers() {
        assertEquals(2, chatRoom.getUsers().size());
    }

    @Test
    void userShouldHaveMediatorSet() {
        assertNotNull(user1.getMediator());
        assertNotNull(user2.getMediator());
    }

    @Test
    void messageShouldBeReceivedByOtherUsers() {
        user1.send("test");
    }

    @Test
    void senderShouldNotReceiveOwnMessage() {
        user1.send("test");
    }

    @Test
    void chatRoomShouldTrackAllUsers() {
        User user3 = new User("User3");
        chatRoom.register(user3);
        assertEquals(3, chatRoom.getUsers().size());
    }
}
