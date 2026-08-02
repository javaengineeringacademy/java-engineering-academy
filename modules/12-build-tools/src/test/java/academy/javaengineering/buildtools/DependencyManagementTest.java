package academy.javaengineering.buildtools;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DependencyManagementTest {

    @Test
    void shouldResolveNewDependency() {
        DependencyManagementExample.DependencyResolver resolver = new DependencyManagementExample.DependencyResolver();
        resolver.resolve(new DependencyManagementExample.Dependency("org.springframework", "spring-core", "6.0.0"));
        assertEquals(1, resolver.getResolved().size());
    }

    @Test
    void shouldResolveToHigherVersion() {
        DependencyManagementExample.DependencyResolver resolver = new DependencyManagementExample.DependencyResolver();
        resolver.resolve(new DependencyManagementExample.Dependency("org.springframework", "spring-core", "6.0.0"));
        resolver.resolve(new DependencyManagementExample.Dependency("org.springframework", "spring-core", "6.1.0"));
        assertEquals("6.1.0", resolver.getResolved().get("org.springframework:spring-core").getVersion());
    }

    @Test
    void shouldKeepExistingWhenLower() {
        DependencyManagementExample.DependencyResolver resolver = new DependencyManagementExample.DependencyResolver();
        resolver.resolve(new DependencyManagementExample.Dependency("org.springframework", "spring-core", "6.1.0"));
        resolver.resolve(new DependencyManagementExample.Dependency("org.springframework", "spring-core", "6.0.0"));
        assertEquals("6.1.0", resolver.getResolved().get("org.springframework:spring-core").getVersion());
    }
}
