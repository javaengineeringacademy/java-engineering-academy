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
package academy.javaengineering.oop.bank;

/**
 * Bank employee with shared identity and role behavior.
 */
public class Employee {

    private final String employeeId;
    private final String fullName;

    /**
     * Creates an employee.
     *
     * @param employeeId stable employee id
     * @param fullName employee name
     */
    public Employee(String employeeId, String fullName) {
        this.employeeId = Text.require(employeeId, "employeeId");
        this.fullName = Text.require(fullName, "fullName");
    }

    /**
     * Returns whether this employee can approve the supplied loan.
     *
     * @param loan loan to evaluate
     * @return false for a base employee
     */
    public boolean canApprove(Loan loan) {
        return false;
    }

    /**
     * Returns the employee id.
     *
     * @return employee id
     */
    public final String employeeId() {
        return employeeId;
    }

    /**
     * Returns the employee name.
     *
     * @return employee name
     */
    public final String fullName() {
        return fullName;
    }

    /**
     * Returns the role name used by branch operations.
     *
     * @return role name
     */
    public String role() {
        return "EMPLOYEE";
    }
}

