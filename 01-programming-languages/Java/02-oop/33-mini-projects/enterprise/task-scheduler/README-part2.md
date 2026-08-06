# Task Scheduler — Part 2: Implementation Guide

**[← Part 1: Project Overview & Design](README.md)** | **[Part 3: Tests & Challenges →](README-part3.md)**

---

## Implementation Guide

### Step 1: Implement Cron Parser

```java
package com.academy.scheduler.cron;

import java.time.LocalDateTime;
import java.util.*;

public class CronExpression {
    private final String expression;
    private final CronField seconds;
    private final CronField minutes;
    private final CronField hours;
    private final CronField dayOfMonth;
    private final CronField month;
    private final CronField dayOfWeek;

    public CronExpression(String expression) {
        this.expression = expression;
        String[] parts = expression.split("\\s+");
        validate(parts);
        
        this.seconds = new CronField(parts[0], 0, 59);
        this.minutes = new CronField(parts[1], 0, 59);
        this.hours = new CronField(parts[2], 0, 23);
        this.dayOfMonth = new CronField(parts[3], 1, 31);
        this.month = new CronField(parts[4], 1, 12);
        this.dayOfWeek = new CronField(parts[5], 0, 7);
    }

    public LocalDateTime getNextExecution(LocalDateTime from) {
        LocalDateTime next = from.plusSeconds(1);
        next = next.withSecond(0).withNano(0);

        while (!isMatching(next)) {
            next = next.plusMinutes(1);
            if (next.getYear() > from.getYear() + 1) {
                throw new RuntimeException("No valid execution time found");
            }
        }

        return next;
    }

    public boolean isMatching(LocalDateTime dateTime) {
        return seconds.matches(dateTime.getSecond()) &&
               minutes.matches(dateTime.getMinute()) &&
               hours.matches(dateTime.getHour()) &&
               dayOfMonth.matches(dateTime.getDayOfMonth()) &&
               month.matches(dateTime.getMonthValue()) &&
               dayOfWeek.matches(dateTime.getDayOfWeek().getValue() % 7);
    }
}

class CronField {
    private final Set<Integer> values;
    private final int min;
    private final int max;

    public CronField(String field, int min, int max) {
        this.min = min;
        this.max = max;
        this.values = parseField(field);
    }

    private Set<Integer> parseField(String field) {
        Set<Integer> result = new TreeSet<>();
        
        if (field.equals("*")) {
            for (int i = min; i <= max; i++) {
                result.add(i);
            }
        } else if (field.contains(",")) {
            for (String part : field.split(",")) {
                result.addAll(parsePart(part));
            }
        } else if (field.contains("-")) {
            String[] range = field.split("-");
            int start = Integer.parseInt(range[0]);
            int end = Integer.parseInt(range[1]);
            for (int i = start; i <= end; i++) {
                result.add(i);
            }
        } else if (field.contains("/")) {
            String[] parts = field.split("/");
            int start = Integer.parseInt(parts[0]);
            int increment = Integer.parseInt(parts[1]);
            for (int i = start; i <= max; i += increment) {
                result.add(i);
            }
        } else {
            result.add(Integer.parseInt(field));
        }
        
        return result;
    }

    public boolean matches(int value) {
        return values.contains(value);
    }
}
```

### Step 2: Implement Scheduler with Strategy Pattern

```java
package com.academy.scheduler.scheduler;

import java.time.LocalDateTime;

public interface SchedulerStrategy {
    LocalDateTime calculateNextExecution(Task task);
    boolean shouldExecute(Task task);
}

package com.academy.scheduler.scheduler;

public class CronStrategy implements SchedulerStrategy {
    private final CronParser parser;

    @Override
    public LocalDateTime calculateNextExecution(Task task) {
        CronExpression cron = parser.parse(task.getCronExpression());
        return cron.getNextExecution(LocalDateTime.now());
    }

    @Override
    public boolean shouldExecute(Task task) {
        if (task.getNextExecution() == null) {
            return true;
        }
        return LocalDateTime.now().isAfter(task.getNextExecution());
    }
}

public class FixedRateStrategy implements SchedulerStrategy {
    private final Duration interval;

    public FixedRateStrategy(Duration interval) {
        this.interval = interval;
    }

    @Override
    public LocalDateTime calculateNextExecution(Task task) {
        LocalDateTime lastExec = task.getLastExecution();
        if (lastExec == null) {
            return LocalDateTime.now();
        }
        return lastExec.plus(interval);
    }

    @Override
    public boolean shouldExecute(Task task) {
        LocalDateTime next = calculateNextExecution(task);
        return LocalDateTime.now().isAfter(next);
    }
}

package com.academy.scheduler.scheduler;

public class TaskScheduler {
    private final TaskQueue queue;
    private final WorkerManager workerManager;
    private final TaskEventManager eventManager;
    private final Map<String, SchedulerStrategy> strategies;
    private final ScheduledExecutorService schedulerExecutor;

    public TaskScheduler() {
        this.queue = new PriorityBlockingQueue<>();
        this.workerManager = new WorkerManager();
        this.eventManager = new TaskEventManager();
        this.strategies = new HashMap<>();
        this.schedulerExecutor = Executors.newScheduledThreadPool(1);
        
        strategies.put("cron", new CronStrategy());
        strategies.put("fixed-rate", new FixedRateStrategy(Duration.ofMinutes(5)));
        strategies.put("fixed-delay", new FixedDelayStrategy(Duration.ofMinutes(5)));
        
        startSchedulerLoop();
    }

    private void startSchedulerLoop() {
        schedulerExecutor.scheduleAtFixedRate(() -> {
            List<Task> readyTasks = getReadyTasks();
            for (Task task : readyTasks) {
                dispatchTask(task);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private List<Task> getReadyTasks() {
        return queue.stream()
            .filter(task -> {
                SchedulerStrategy strategy = strategies.get(task.getScheduleType());
                return strategy != null && strategy.shouldExecute(task);
            })
            .collect(Collectors.toList());
    }

    private void dispatchTask(Task task) {
        Worker worker = workerManager.getAvailableWorker();
        if (worker == null) {
            return;
        }

        try {
            TaskExecution execution = worker.assignTask(task);
            eventManager.notifyTaskStarted(task, execution);
            
            task.setNextExecution(strategies.get(task.getScheduleType())
                .calculateNextExecution(task));
            
        } catch (Exception e) {
            eventManager.notifyTaskFailed(task, null);
            if (task.shouldRetry()) {
                scheduleRetry(task);
            }
        }
    }

    public void scheduleTask(Task task) {
        SchedulerStrategy strategy = strategies.get(task.getScheduleType());
        task.setNextExecution(strategy.calculateNextExecution(task));
        queue.enqueue(task);
        eventManager.notifyTaskScheduled(task);
    }

    public void unscheduleTask(String taskId) {
        queue.remove(taskId);
    }
}
```

### Step 3: Implement Worker Manager

```java
package com.academy.scheduler.worker;

import java.util.concurrent.ConcurrentHashMap;

public class WorkerManager {
    private final ConcurrentHashMap<String, Worker> workers;
    private final LoadBalancer loadBalancer;
    private final ScheduledExecutorService heartbeatChecker;

    public WorkerManager() {
        this.workers = new ConcurrentHashMap<>();
        this.loadBalancer = new RoundRobinLoadBalancer();
        this.heartbeatChecker = Executors.newSingleThreadScheduledExecutor();
        
        startHeartbeatChecker();
    }

    private void startHeartbeatChecker() {
        heartbeatChecker.scheduleAtFixedRate(() -> {
            LocalDateTime threshold = LocalDateTime.now().minusSeconds(30);
            workers.values().stream()
                .filter(w -> w.getLastHeartbeat().isBefore(threshold))
                .forEach(w -> {
                    w.setStatus(WorkerStatus.OFFLINE);
                    redistributeTasks(w);
                });
        }, 30, 30, TimeUnit.SECONDS);
    }

    public void registerWorker(Worker worker) {
        workers.put(worker.getWorkerId(), worker);
        worker.setStatus(WorkerStatus.ONLINE);
    }

    public Worker getAvailableWorker() {
        List<Worker> available = workers.values().stream()
            .filter(Worker::isAvailable)
            .filter(Worker::canAcceptTask)
            .collect(Collectors.toList());
        
        if (available.isEmpty()) {
            return null;
        }
        
        return loadBalancer.selectWorker(available);
    }

    public TaskExecution distributeTask(Task task) {
        Worker worker = getAvailableWorker();
        if (worker == null) {
            throw new WorkerUnavailableException("No available workers");
        }
        return worker.assignTask(task);
    }

    public void updateWorkerStatus(String workerId, WorkerStatus status) {
        Worker worker = workers.get(workerId);
        if (worker != null) {
            worker.setStatus(status);
            if (status == WorkerStatus.OFFLINE) {
                redistributeTasks(worker);
            }
        }
    }
}

interface LoadBalancer {
    Worker selectWorker(List<Worker> workers);
}

class RoundRobinLoadBalancer implements LoadBalancer {
    private final AtomicInteger index = new AtomicInteger(0);

    @Override
    public Worker selectWorker(List<Worker> workers) {
        int idx = Math.abs(index.getAndIncrement() % workers.size());
        return workers.get(idx);
    }
}
```

### Step 4: Implement Dependency Resolver

```java
package com.academy.scheduler.dependency;

import java.util.*;

public class DependencyGraph {
    private final Map<String, Set<String>> adjacencyList;
    private final Map<String, Set<String>> reverseAdjacencyList;

    public DependencyGraph() {
        this.adjacencyList = new HashMap<>();
        this.reverseAdjacencyList = new HashMap<>();
    }

    public void addDependency(String taskId, String dependsOn) {
        adjacencyList.computeIfAbsent(dependsOn, k -> new HashSet<>()).add(taskId);
        reverseAdjacencyList.computeIfAbsent(taskId, k -> new HashSet<>()).add(dependsOn);
    }

    public Set<String> getDependencies(String taskId) {
        return reverseAdjacencyList.getOrDefault(taskId, Collections.emptySet());
    }

    public List<String> topologicalSort() {
        List<String> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> recursionStack = new HashSet<>();

        for (String taskId : adjacencyList.keySet()) {
            if (!visited.contains(taskId)) {
                if (topologicalSortUtil(taskId, visited, recursionStack, result)) {
                    throw new DependencyCycleException("Cycle detected involving task: " + taskId);
                }
            }
        }

        return result;
    }

    private boolean topologicalSortUtil(String taskId, Set<String> visited, 
                                       Set<String> recursionStack, List<String> result) {
        visited.add(taskId);
        recursionStack.add(taskId);

        Set<String> dependencies = getDependencies(taskId);
        for (String dep : dependencies) {
            if (!visited.contains(dep)) {
                if (topologicalSortUtil(dep, visited, recursionStack, result)) {
                    return true;
                }
            } else if (recursionStack.contains(dep)) {
                return true;
            }
        }

        recursionStack.remove(taskId);
        result.add(taskId);
        return false;
    }
}

public class DependencyResolver {
    private final DependencyGraph graph;
    private final Set<String> completedTasks;

    public DependencyResolver() {
        this.graph = new DependencyGraph();
        this.completedTasks = new HashSet<>();
    }

    public List<Task> getReadyTasks(List<Task> allTasks) {
        return allTasks.stream()
            .filter(task -> !completedTasks.contains(task.getTaskId()))
            .filter(task -> areDependenciesMet(task))
            .collect(Collectors.toList());
    }

    private boolean areDependenciesMet(Task task) {
        Set<String> dependencies = graph.getDependencies(task.getTaskId());
        return completedTasks.containsAll(dependencies);
    }

    public void markComplete(String taskId) {
        completedTasks.add(taskId);
    }

    public void addDependency(String taskId, String dependsOn) {
        graph.addDependency(taskId, dependsOn);
    }
}
```

---

**[← Part 1: Project Overview & Design](README.md)** | **[Part 3: Tests & Challenges →](README-part3.md)**