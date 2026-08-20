# Test Containers Quiz

## Question 1
What does @Container do?

- A) Creates a Docker container
- B) Manages container lifecycle
- C) Both A and B
- D) Neither

**Answer: C**
**Explanation:** @Container creates and manages the container lifecycle automatically.

---

## Question 2
What is a wait strategy?

- A) How long to wait for tests
- B) Condition for container readiness
- C) Docker timeout
- D) Test timeout

**Answer: B**
**Explanation:** Wait strategies determine when a container is ready to accept connections.

---

## Question 3
How do you specify a Docker image?

- A) With DockerImageName.parse()
- B) With image() method
- C) Both A and B
- D) In configuration file

**Answer: C**
**Explanation:** Both approaches work; DockerImageName.parse() is more explicit.

---

## Question 4
What happens to containers after tests?

- A) They persist
- B) They are automatically stopped and removed
- C) They are archived
- D) They restart

**Answer: B**
**Explanation:** Testcontainers automatically cleans up containers after test completion.

---

## Question 5
What does withDatabaseName() do?

- A) Creates a database
- B) Sets the database name
- C) Connects to database
- D) Drops database

**Answer: B**
**Explanation:** withDatabaseName() configures the database name for the container.

---

## Question 6
How do you get the JDBC URL?

- A) postgres.getJdbcUrl()
- B) postgres.getUrl()
- C) postgres.getConnectionUrl()
- D) Hardcode it

**Answer: A**
**Explanation:** getJdbcUrl() returns the dynamically assigned JDBC URL.

---

## Question 7
What is GenericContainer used for?

- A) Only databases
- B) Any Docker image
- C) Only web servers
- D) Only message brokers

**Answer: B**
**Explanation:** GenericContainer supports any Docker image with custom configuration.

---

## Question 8
How do you share containers across tests?

- A) Use @Container on static field
- B) Use singleton pattern
- C) Use Spring context
- D) Not possible

**Answer: A**
**Explanation:** Static @Container fields are shared across all test methods in the class.

---

## Question 9
What is the benefit of Testcontainers?

- A) Faster tests
- B) Real environment testing
- C) No Docker needed
- D) No cleanup needed

**Answer: B**
**Explanation:** Testcontainers provides real, production-like environments for integration testing.

---

## Question 10
How do you mount volumes?

- A) withFileSystemBind()
- B) withVolume()
- C) mount()
- D) Both A and B

**Answer: D**
**Explanation:** Both withFileSystemBind() and withVolume() support volume mounting.
