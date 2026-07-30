package com.javaacademy.sprint1.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class HelloWorldTest {

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> HelloWorld.main(new String[]{}));
    }
}

class CommentsExampleTest {

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> CommentsExample.main(new String[]{}));
    }
}

class VariablesExampleTest {

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> VariablesExample.main(new String[]{}));
    }
}

class ProgramStructureTest {

    @Test
    void testMainMethodExists() {
        assertDoesNotThrow(() -> ProgramStructure.main(new String[]{}));
    }
}