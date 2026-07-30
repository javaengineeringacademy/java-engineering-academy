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

import java.time.Duration;
import java.util.Objects;

/**
 * Describes a measurable learning milestone in a Java curriculum module.
 *
 * @param id stable milestone identifier
 * @param title learner-facing title
 * @param summary short explanation of the milestone
 * @param estimatedDuration expected focused study time
 */
public record LearningMilestone(
        String id,
        String title,
        String summary,
        Duration estimatedDuration
) {

    /**
     * Creates a milestone and normalizes text fields.
     */
    public LearningMilestone {
        id = requireText(id, "id");
        title = requireText(title, "title");
        summary = requireText(summary, "summary");
        estimatedDuration = Objects.requireNonNull(estimatedDuration, "estimatedDuration");

        if (estimatedDuration.isZero() || estimatedDuration.isNegative()) {
            throw new IllegalArgumentException("estimatedDuration must be positive");
        }
    }

    /**
     * Returns whether the milestone can fit inside the provided available study time.
     *
     * @param availableTime available focused study time
     * @return true when the milestone can fit in the available time
     */
    public boolean fitsWithin(Duration availableTime) {
        Objects.requireNonNull(availableTime, "availableTime");
        return estimatedDuration.compareTo(availableTime) <= 0;
    }

    private static String requireText(String value, String fieldName) {
        var normalized = Objects.requireNonNull(value, fieldName).trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return normalized;
    }
}

