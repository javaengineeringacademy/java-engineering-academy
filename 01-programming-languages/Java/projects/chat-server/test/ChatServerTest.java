import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;

/**
 * Unit and integration tests for the Chat Server project.
 * Tests Message class functionality and ChatRoom operations.
 */
public class ChatServerTest {

    private Message testMessage;
    private ChatRoom testRoom;

    @Before
    public void setUp() {
        testMessage = new Message("alice", "bob", "Hello!", Message.Type.CHAT);
        testRoom = new ChatRoom("TestRoom");
    }

    /**
     * Tests Message creation and basic properties.
     */
    @Test
    public void testMessageCreation() {
        assertNotNull("Message should not be null", testMessage);
        assertEquals("alice", testMessage.getSender());
        assertEquals("bob", testMessage.getRecipient());
        assertEquals("Hello!", testMessage.getContent());
        assertEquals(Message.Type.CHAT, testMessage.getType());
        assertNotNull("Timestamp should not be null", testMessage.getTimestamp());
    }

    /**
     * Tests Message formatting for different types.
     */
    @Test
    public void testMessageFormatting() {
        String formatted = testMessage.getFormatted();
        assertNotNull(formatted);
        assertTrue("Should contain sender", formatted.contains("alice"));
        assertTrue("Should contain content", formatted.contains("Hello!"));

        Message pm = new Message("alice", "bob", "Secret", Message.Type.PRIVATE);
        assertTrue("PM should contain [PM]", pm.getFormatted().contains("[PM]"));

        Message sys = new Message("System", "alice", "Welcome", Message.Type.SYSTEM);
        assertTrue("System should contain [SYSTEM]", sys.getFormatted().contains("[SYSTEM]"));
    }

    /**
     * Tests Message type detection methods.
     */
    @Test
    public void testMessageTypes() {
        assertFalse("CHAT should not be system", testMessage.isSystem());
        assertFalse("CHAT should not be private", testMessage.isPrivate());

        Message pm = new Message("a", "b", "msg", Message.Type.PRIVATE);
        assertTrue("PRIVATE should be private", pm.isPrivate());

        Message sys = new Message("a", "b", "msg", Message.Type.SYSTEM);
        assertTrue("SYSTEM should be system", sys.isSystem());
    }

    /**
     * Tests ChatRoom participant management.
     */
    @Test
    public void testChatRoomParticipants() {
        assertEquals(0, testRoom.getParticipantCount());

        testRoom.addParticipant("alice");
        assertEquals(1, testRoom.getParticipantCount());
        assertTrue(testRoom.hasParticipant("alice"));

        testRoom.addParticipant("bob");
        assertEquals(2, testRoom.getParticipantCount());

        // Adding same user should not duplicate
        testRoom.addParticipant("alice");
        assertEquals(2, testRoom.getParticipantCount());

        testRoom.removeParticipant("alice");
        assertEquals(1, testRoom.getParticipantCount());
        assertFalse(testRoom.hasParticipant("alice"));
    }

    /**
     * Tests ChatRoom message history.
     */
    @Test
    public void testChatRoomHistory() {
        List<Message> history = testRoom.getHistory(10);
        assertTrue("Initial history should be empty", history.isEmpty());

        // Note: In full implementation, would test with actual broadcasting
        // This tests the data structure behavior
        Message msg = new Message("alice", "TestRoom", "Hello", Message.Type.CHAT);
        assertEquals("TestRoom", testRoom.getName());
    }

    /**
     * Tests ChatRoom properties.
     */
    @Test
    public void testChatRoomProperties() {
        assertEquals("TestRoom", testRoom.getName());
        assertNotNull(testRoom.getParticipants());
        assertEquals(0, testRoom.getParticipants().size());
    }
}
