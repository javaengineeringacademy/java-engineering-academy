# Code Coverage Quiz

## Question 1
What does line coverage measure?

- A) Methods executed
- B) Lines executed
- C) Branches taken
- D) Classes loaded

**Answer: B**
**Explanation:** Line coverage measures the percentage of source code lines executed.

---

## Question 2
What is JaCoCo?

- A) Test framework
- B) Code coverage tool
- C) Mocking library
- D) Build tool

**Answer: B**
**Explanation:** JaCoCo is a Java code coverage tool.

---

## Question 3
What does branch coverage measure?

- A) Lines executed
- B) Decision paths taken
- C) Methods called
- D) Classes loaded

**Answer: B**
**Explanation:** Branch coverage measures how many decision paths (if/else, switch) are exercised.

---

## Question 4
How do you generate a JaCoCo report?

- A) mvn jacoco:report
- B) mvn test
- C) mvn compile
- D) mvn package

**Answer: A**
**Explanation:** mvn jacoco:report generates the coverage report.

---

## Question 5
What is a good coverage target?

- A) 100%
- B) 70-80%
- C) 50-60%
- D) 30-40%

**Answer: B**
**Explanation:** 70-80% is a reasonable target; 100% is often impractical.

---

## Question 6
How do you exclude classes from coverage?

- A) @Ignore annotation
- B) JaCoCo exclude configuration
- C) @Generated annotation
- D) Both B and C

**Answer: D**
**Explanation:** Both JaCoCo configuration and @Generated annotation can exclude code.

---

## Question 7
What is instruction coverage?

- A) Source lines
- B) Bytecode instructions
- C) Decision paths
- D) Method calls

**Answer: B**
**Explanation:** Instruction coverage measures executed bytecode instructions.

---

## Question 8
How do you set coverage thresholds?

- A) In test code
- B) In JaCoCo configuration
- C) In pom.xml only
- D) In reports

**Answer: B**
**Explanation:** Coverage thresholds are configured in JaCoCo plugin configuration.

---

## Question 9
What happens when coverage threshold is not met?

- A) Build fails
- B) Warning only
- C) Tests skip
- D) Report not generated

**Answer: A**
**Explanation:** With check goal configured, build fails when threshold is not met.

---

## Question 10
What is the relationship between coverage and quality?

- A) Higher coverage = better quality
- B) Coverage is one quality indicator
- C) Coverage guarantees quality
- D) Coverage is unrelated to quality

**Answer: B**
**Explanation:** Coverage is a useful metric but doesn't guarantee test quality.
