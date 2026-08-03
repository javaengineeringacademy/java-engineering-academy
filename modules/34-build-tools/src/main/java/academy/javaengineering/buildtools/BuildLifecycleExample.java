package academy.javaengineering.buildtools;

import java.util.ArrayList;
import java.util.List;

/**
 * Build Lifecycle - Clean, Compile, Test, Package, Install, Deploy.
 */
public class BuildLifecycleExample {

    public enum Phase {
        CLEAN, VALIDATE, COMPILE, TEST, PACKAGE, VERIFY, INSTALL, DEPLOY
    }

    public static class BuildExecution {
        private final List<Phase> completedPhases = new ArrayList<>();
        private final List<String> logs = new ArrayList<>();

        public void execute(Phase phase) {
            completedPhases.add(phase);
            logs.add("Executed: " + phase.name().toLowerCase());
        }

        public void executeLifecycle() {
            for (Phase phase : Phase.values()) {
                execute(phase);
            }
        }

        public List<String> getLogs() { return logs; }
        public List<Phase> getCompletedPhases() { return completedPhases; }
    }

    public static void main(String[] args) {
        BuildExecution execution = new BuildExecution();
        execution.executeLifecycle();
        execution.getLogs().forEach(System.out::println);
    }
}
