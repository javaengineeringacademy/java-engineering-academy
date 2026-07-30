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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Optional;

/**
 * Loan request with approval workflow and simple interest projection.
 */
public final class Loan {

    private static final BigDecimal MONTHS_IN_YEAR = BigDecimal.valueOf(12);

    private final String loanId;
    private final Customer customer;
    private final Money principal;
    private final BigDecimal annualInterestRate;
    private final int termMonths;
    private LoanStatus status;
    private Manager approvedBy;

    /**
     * Creates a loan request.
     *
     * @param loanId stable loan id
     * @param customer borrower
     * @param principal requested principal
     * @param annualInterestRate annual simple interest rate
     * @param termMonths repayment term in months
     */
    public Loan(
            String loanId,
            Customer customer,
            Money principal,
            BigDecimal annualInterestRate,
            int termMonths
    ) {
        this.loanId = Text.require(loanId, "loanId");
        this.customer = Objects.requireNonNull(customer, "customer");
        this.principal = Objects.requireNonNull(principal, "principal");
        this.annualInterestRate = validateRate(annualInterestRate);
        this.termMonths = validateTerm(termMonths);
        this.status = LoanStatus.REQUESTED;
    }

    /**
     * Calculates a simple fixed monthly payment for learning purposes.
     *
     * @return projected monthly payment
     */
    public Money monthlyPayment() {
        var termYears = BigDecimal.valueOf(termMonths).divide(MONTHS_IN_YEAR, 10, RoundingMode.HALF_EVEN);
        var totalInterest = principal.multipliedBy(annualInterestRate.multiply(termYears));
        var totalRepayment = principal.plus(totalInterest);
        var monthlyAmount = totalRepayment.amount().divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_EVEN);
        return new Money(monthlyAmount, principal.currency());
    }

    /**
     * Returns the approved manager when the loan is approved.
     *
     * @return approving manager
     */
    public Optional<Manager> approvedBy() {
        return Optional.ofNullable(approvedBy);
    }

    /**
     * Returns whether the loan is approved.
     *
     * @return approval state
     */
    public boolean isApproved() {
        return status == LoanStatus.APPROVED;
    }

    /**
     * Returns the loan id.
     *
     * @return loan id
     */
    public String loanId() {
        return loanId;
    }

    /**
     * Returns the borrower.
     *
     * @return customer
     */
    public Customer customer() {
        return customer;
    }

    /**
     * Returns the principal.
     *
     * @return principal
     */
    public Money principal() {
        return principal;
    }

    /**
     * Returns the annual interest rate.
     *
     * @return annual rate
     */
    public BigDecimal annualInterestRate() {
        return annualInterestRate;
    }

    /**
     * Returns the term in months.
     *
     * @return term months
     */
    public int termMonths() {
        return termMonths;
    }

    /**
     * Returns the current status.
     *
     * @return loan status
     */
    public LoanStatus status() {
        return status;
    }

    void approveBy(Manager manager) {
        approvedBy = Objects.requireNonNull(manager, "manager");
        status = LoanStatus.APPROVED;
    }

    private static BigDecimal validateRate(BigDecimal rate) {
        Objects.requireNonNull(rate, "annualInterestRate");
        if (rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("annualInterestRate must be between 0 and 1");
        }
        return rate;
    }

    private static int validateTerm(int termMonths) {
        if (termMonths <= 0) {
            throw new IllegalArgumentException("termMonths must be positive");
        }
        return termMonths;
    }
}

