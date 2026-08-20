package academy.javaengineering.testing.unit.solutions;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class Exercise2StatefulObjectSolution {

    enum TaskStatus { PENDING, IN_PROGRESS, COMPLETED }

    static class Task {
        private final String id;
        private String title;
        private TaskStatus status;
        private final List<String> comments = new ArrayList<>();

        Task(String id, String title) {
            this.id = id;
            this.title = title;
            this.status = TaskStatus.PENDING;
        }

        void start() {
            if (status != TaskStatus.PENDING) throw new IllegalStateException("Cannot start from " + status);
            status = TaskStatus.IN_PROGRESS;
        }

        void complete() {
            if (status != TaskStatus.IN_PROGRESS) throw new IllegalStateException("Cannot complete from " + status);
            status = TaskStatus.COMPLETED;
        }

        void addComment(String comment) { comments.add(comment); }
        String getId() { return id; }
        TaskStatus getStatus() { return status; }
        List<String> getComments() { return Collections.unmodifiableList(comments); }
    }

    @Test
    void shouldCreateTask() {
        Task task = new Task("1", "Write tests");
        assertEquals(TaskStatus.PENDING, task.getStatus());
        assertEquals("1", task.getId());
    }

    @Test
    void shouldStartTask() {
        Task task = new Task("1", "Write tests");
        task.start();
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
    }

    @Test
    void shouldNotStartCompletedTask() {
        Task task = new Task("1", "Write tests");
        task.start();
        task.complete();
        assertThrows(IllegalStateException.class, task::start);
    }

    @Test
    void shouldCompleteTask() {
        Task task = new Task("1", "Write tests");
        task.start();
        task.complete();
        assertEquals(TaskStatus.COMPLETED, task.getStatus());
    }

    @Test
    void shouldAddComments() {
        Task task = new Task("1", "Write tests");
        task.addComment("First comment");
        task.addComment("Second comment");
        assertEquals(2, task.getComments().size());
    }
}
