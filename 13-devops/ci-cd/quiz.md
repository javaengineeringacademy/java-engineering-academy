# CI/CD Quiz

## Question 1
What is the difference between Continuous Delivery and Continuous Deployment?
- A) They are the same thing
- B) Delivery requires manual approval before production; deployment automatically releases to production
- C) Deployment is slower than delivery
- D) Delivery only works with Java applications

**Answer: B**
**Explanation:** Continuous Delivery ensures code is always in a deployable state and ready for production, but requires manual approval. Continuous Deployment goes further by automatically deploying every change that passes all stages to production.

## Question 2
What is a quality gate in a CI/CD pipeline?
- A) A physical gate at the office
- B) A set of criteria (like test coverage, build success, security scans) that must pass before proceeding to the next stage
- C) A manual code review step
- D) A deployment approval

**Answer: B**
**Explanation:** A quality gate is a checkpoint in the pipeline that enforces quality standards. If the criteria aren't met (e.g., tests fail, coverage is too low, vulnerabilities found), the pipeline stops and the issue must be fixed.

## Question 3
What is a blue-green deployment?
- A) Deploying to blue and green servers simultaneously
- B) Running two identical production environments, switching traffic from the old (blue) to new (green) version
- C) Deploying only on Tuesdays (blue) and Thursdays (green)
- D) A backup strategy for databases

**Answer: B**
**Explanation:** Blue-green deployment maintains two identical environments. The blue environment runs the current version, while green hosts the new version. Traffic is switched instantly, enabling zero-downtime deployments and easy rollback.

## Question 4
What is the purpose of caching dependencies in a CI/CD pipeline?
- A) To store source code
- B) To speed up builds by reusing downloaded dependencies instead of fetching them each time
- C) To create deployment artifacts
- D) To run security scans

**Answer: B**
**Explanation:** Caching dependencies (e.g., Maven repository, npm modules) between pipeline runs significantly reduces build time since packages don't need to be re-downloaded. This is especially valuable for large projects with many dependencies.

## Question 5
What is the "fail fast" principle in CI/CD?
- A) Failing the build quickly to save resources
- B) Running the fastest tests first so developers get immediate feedback on obvious issues
- C) Deploying to production immediately
- D) Ignoring test failures

**Answer: B**
**Explanation:** Fail fast means running quick checks (compilation, unit tests, linting) early in the pipeline. If these basic checks fail, the pipeline stops immediately, giving developers fast feedback without wasting time on expensive integration or deployment steps.