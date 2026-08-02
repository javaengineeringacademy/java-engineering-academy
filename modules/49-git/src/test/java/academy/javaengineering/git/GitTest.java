package academy.javaengineering.git;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Git Tests")
class GitTest {

    @Test
    @DisplayName("CommitInfo should be created correctly")
    void testCommitInfo() {
        var commit = new GitOperations.CommitInfo("abc123", "John", "2024-01-01", "Initial commit");
        
        assertEquals("abc123", commit.hash());
        assertEquals("John", commit.author());
        assertEquals("Initial commit", commit.message());
    }

    @Test
    @DisplayName("BranchInfo should be created correctly")
    void testBranchInfo() {
        var branch = new GitOperations.BranchInfo("main", true, "abc123");
        
        assertEquals("main", branch.name());
        assertTrue(branch.isCurrent());
        assertEquals("abc123", branch.lastCommit());
    }

    @Test
    @DisplayName("DiffEntry should be created correctly")
    void testDiffEntry() {
        var diff = new GitDiff.DiffEntry("README.md", "modified", 10, 5);
        
        assertEquals("README.md", diff.file());
        assertEquals("modified", diff.status());
        assertEquals(10, diff.additions());
        assertEquals(5, diff.deletions());
    }
}
