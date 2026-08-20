package academy.javaengineering.testing.testng.practices;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Exercise 3: Test Dependencies
 *
 * Tasks:
 * 1. Create dependent test methods
 * 2. Use dependsOnMethods for sequencing
 * 3. Use dependsOnGroups for group dependencies
 */
public class Exercise3Dependencies {

    @Test
    public void setupDatabase() {
        // Simulate DB setup
        assertTrue(true);
    }

    @Test(dependsOnMethods = {"setupDatabase"})
    public void insertTestData() {
        // Should run after setupDatabase
        assertTrue(true);
    }

    @Test(dependsOnMethods = {"insertTestData"})
    public void queryData() {
        // Should run after insertTestData
        assertTrue(true);
    }

    @Test(dependsOnMethods = {"queryData"})
    public void cleanupDatabase() {
        // Should run last
        assertTrue(true);
    }
}
