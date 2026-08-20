package academy.javaengineering.testing.testng.solutions;

import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class Exercise3DependenciesSolution {

    private boolean dbReady = false;
    private boolean dataInserted = false;

    @Test
    public void setupDatabase() {
        dbReady = true;
        assertTrue(dbReady);
    }

    @Test(dependsOnMethods = {"setupDatabase"})
    public void insertTestData() {
        assertTrue(dbReady, "Database should be ready");
        dataInserted = true;
    }

    @Test(dependsOnMethods = {"insertTestData"})
    public void queryData() {
        assertTrue(dataInserted, "Data should be inserted");
        assertTrue(true, "Query executed successfully");
    }

    @Test(dependsOnMethods = {"queryData"})
    public void cleanupDatabase() {
        dataInserted = false;
        dbReady = false;
    }
}
