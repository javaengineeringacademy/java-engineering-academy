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

import java.util.Objects;

/**
 * Employee subtype that can approve loans up to a configured limit.
 */
public final class Manager extends Employee {

    private final Money approvalLimit;

    /**
     * Creates a manager.
     *
     * @param employeeId stable employee id
     * @param fullName manager name
     * @param approvalLimit maximum principal this manager can approve
     */
    public Manager(String employeeId, String fullName, Money approvalLimit) {
        super(employeeId, fullName);
        this.approvalLimit = Objects.requireNonNull(approvalLimit, "approvalLimit");
    }

    @Override
    public boolean canApprove(Loan loan) {
        Objects.requireNonNull(loan, "loan");
        return approvalLimit.isGreaterThanOrEqualTo(loan.principal());
    }

    @Override
    public String role() {
        return "MANAGER";
    }

    /**
     * Approves the loan if it is inside the manager approval limit.
     *
     * @param loan loan to approve
     * @return whether approval succeeded
     */
    public boolean approve(Loan loan) {
        if (!canApprove(loan)) {
            return false;
        }
        loan.approveBy(this);
        return true;
    }

    /**
     * Returns the approval limit.
     *
     * @return approval limit
     */
    public Money approvalLimit() {
        return approvalLimit;
    }
}

