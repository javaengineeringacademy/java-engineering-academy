# Task Scheduler — Part 3: Tests & Challenges

**[← Part 2: Implementation Guide](README-part2.md)**

---

## Unit Tests

```java
package com.academy.scheduler;

import com.academy.scheduler.model.*;
import com.academy.scheduler.service.TaskScheduler;
import com.academy.scheduler.cron.CronExpression;
import com.academy.scheduler.worker.WorkerManager;
import com.academy.scheduler.dependency.DependencyResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.*;

public class TaskSchedulerTest {
    private TaskScheduler scheduler;
    private WorkerManager workerManager;

    @BeforeEach
    void setUp() {
        scheduler = new TaskScheduler();
        workerManager = new WorkerManager();
    }

    @Test
    void testScheduleTask() {
        Task task = Task.builder()
            .taskId("task1")
            .name("Test Task")
            .cronExpression("0 * * * * *")
            .build();

        scheduler.scheduleTask(task);
        
        assertNotNull(scheduler.getTask("task1"));
        assertEquals(TaskStatus.SCHEDULED, scheduler.getTask("task1").getStatus());
    }

    @Test
    void testCronParsing() {
        CronExpression cron = new CronExpression("0 0 12 * * ?");
        
        LocalDateTime next = cron.getNextExecution(LocalDateTime.now());
        assertEquals(12, next.getHour());
        assertEquals(0, next.getMinute());
    }

    @Test
    void testWorkerRegistration() {
        Worker worker = new Worker("worker1", "host1", 10);
        workerManager.registerWorker(worker);
        
        Worker available = workerManager.getAvailableWorker();
        assertNotNull(available);
        assertEquals("worker1", available.getWorkerId());
    }

    @Test
    void testTaskPriority() {
        Task highPriority = Task.builder()
            .taskId("high")
            .priority(TaskPriority.HIGH)
            .build();
        
        Task lowPriority = Task.builder()
            .taskId("low")
            .priority(TaskPriority.LOW)
            .build();

        scheduler.scheduleTask(lowPriority);
        scheduler.scheduleTask(highPriority);
        
        Task next = scheduler.getNextTask();
        assertEquals("high", next.getTaskId());
    }

    @Test
    void testDependencyResolution() {
        DependencyResolver resolver = new DependencyResolver();
        
        resolver.addDependency("task2", "task1");
        resolver.addDependency("task3", "task2");
        
        List<Task> allTasks = Arrays.asList(
            createTask("task1"),
            createTask("task2"),
            createTask("task3")
        );
        
        List<Task> ready = resolver.getReadyTasks(allTasks);
        assertEquals(1, ready.size());
        assertEquals("task1", ready.get(0).getTaskId());
        
        resolver.markComplete("task1");
        ready = resolver.getReadyTasks(allTasks);
        assertEquals(1, ready.size());
        assertEquals("task2", ready.get(0).getTaskId());
    }

    @Test
    void testTaskRetry() {
        Task task = Task.builder()
            .taskId("retry-task")
            .maxRetries(3)
            .retryCount(0)
            .build();
        
        assertTrue(task.shouldRetry());
        task.incrementRetry();
        assertEquals(1, task.getRetryCount());
        assertTrue(task.shouldRetry());
        
        task.incrementRetry();
        task.incrementRetry();
        assertFalse(task.shouldRetry());
    }

    @Test
    void testCronExpressionMatching() {
        CronExpression everyMinute = new CronExpression("0 * * * * ?");
        
        LocalDateTime now = LocalDateTime.now();
        assertTrue(everyMinute.isMatching(now.withSecond(0)));
        assertFalse(everyMinute.isMatching(now.withSecond(30)));
    }
}
```

## Extension Challenges

1. **Workflow Engine**: Implement DAG-based task workflows with conditional branching
2. **Resource Quotas**: Limit concurrent tasks per user/organization
3. **Task Chaining**: Pass output of one task as input to the next
4. **Distributed Locking**: Implement distributed locks for singleton tasks
5. **Metrics Dashboard**: Real-time monitoring of task execution

## Interview Questions

1. **How would you ensure exactly-once task execution?**
   - Discuss idempotency keys, distributed locks, deduplication

2. **How would you handle task dependencies at scale?**
   - Discuss DAG representation, topological sorting, cycle detection

3. **What are the trade-offs of different scheduling strategies?**
   - Discuss cron vs fixed-rate vs event-driven scheduling

4. **How would you implement graceful shutdown?**
   - Discuss task completion, drain queues, worker coordination

5. **How would you design for multi-region deployment?**
   - Discuss data consistency, failover, region-aware scheduling

## References

- [Cron Expression Format](https://www.quartz-scheduler.org/documentation/quartz-2.3.0/crontrigger.html)
- [Distributed Task Scheduling](https://aws.amazon.com/blogs/compute/using-amazon-cloudwatch-events-to-schedule-tasks/)
- [Worker Pool Pattern](https://developer.confluent.io/patterns/worker-pool/)