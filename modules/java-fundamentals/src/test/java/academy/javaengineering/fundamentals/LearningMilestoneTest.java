/*
 * Copyright 2026 Java Engineering Academy contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package academy.javaengineering.fundamentals;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;

class LearningMilestoneTest {

    @Test
    void createsMilestoneWithNormalizedText() {
        var milestone = new LearningMilestone(
                "  java-basics  ",
                "  Java Basics  ",
                "  Understand values, types, and methods.  ",
                Duration.ofHours(3)
        );

        assertAll(
                () -> assertEquals("java-basics", milestone.id()),
                () -> assertEquals("Java Basics", milestone.title()),
                () -> assertEquals("Understand values, types, and methods.", milestone.summary()),
                () -> assertEquals(Duration.ofHours(3), milestone.estimatedDuration())
        );
    }

    @Test
    void rejectsBlankTextFields() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new LearningMilestone(" ", "Title", "Summary", Duration.ofMinutes(30))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new LearningMilestone("id", " ", "Summary", Duration.ofMinutes(30))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new LearningMilestone("id", "Title", " ", Duration.ofMinutes(30)))
        );
    }

    @Test
    void rejectsNullFields() {
        assertAll(
                () -> assertThrows(NullPointerException.class,
                        () -> new LearningMilestone(null, "Title", "Summary", Duration.ofMinutes(30))),
                () -> assertThrows(NullPointerException.class,
                        () -> new LearningMilestone("id", null, "Summary", Duration.ofMinutes(30))),
                () -> assertThrows(NullPointerException.class,
                        () -> new LearningMilestone("id", "Title", null, Duration.ofMinutes(30))),
                () -> assertThrows(NullPointerException.class,
                        () -> new LearningMilestone("id", "Title", "Summary", null))
        );
    }

    @Test
    void rejectsNonPositiveDuration() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new LearningMilestone("id", "Title", "Summary", Duration.ZERO)),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new LearningMilestone("id", "Title", "Summary", Duration.ofMinutes(-1)))
        );
    }

    @Test
    void comparesMilestoneAgainstAvailableTime() {
        var milestone = new LearningMilestone("id", "Title", "Summary", Duration.ofMinutes(45));

        assertAll(
                () -> assertTrue(milestone.fitsWithin(Duration.ofMinutes(45))),
                () -> assertTrue(milestone.fitsWithin(Duration.ofMinutes(60))),
                () -> assertFalse(milestone.fitsWithin(Duration.ofMinutes(30)))
        );
    }

    @Test
    void requiresAvailableTimeBeforeComparingDuration() {
        var milestone = new LearningMilestone("id", "Title", "Summary", Duration.ofMinutes(45));

        assertThrows(NullPointerException.class, () -> milestone.fitsWithin(null));
    }
}

