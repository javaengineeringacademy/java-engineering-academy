package academy.javaengineering.buildtools;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BuildLifecycleTest {

    @Test
    void shouldExecuteAllPhases() {
        BuildLifecycleExample.BuildExecution execution = new BuildLifecycleExample.BuildExecution();
        execution.executeLifecycle();
        assertEquals(8, execution.getCompletedPhases().size());
    }

    @Test
    void shouldExecutePhasesInOrder() {
        BuildLifecycleExample.BuildExecution execution = new BuildLifecycleExample.BuildExecution();
        execution.executeLifecycle();
        List<BuildLifecycleExample.Phase> phases = execution.getCompletedPhases();
        assertEquals(BuildLifecycleExample.Phase.CLEAN, phases.get(0));
        assertEquals(BuildLifecycleExample.Phase.DEPLOY, phases.get(7));
    }

    @Test
    void shouldLogExecutedPhases() {
        BuildLifecycleExample.BuildExecution execution = new BuildLifecycleExample.BuildExecution();
        execution.execute(BuildLifecycleExample.Phase.COMPILE);
        assertEquals(1, execution.getLogs().size());
        assertTrue(execution.getLogs().get(0).contains("compile"));
    }
}
