# Gatling - Performance Testing

## Overview

Gatling is a modern load testing framework based on Scala, Akka, and Netty. It provides excellent performance, detailed reports, and supports various protocols.

## Setup

### Maven

```xml
<plugin>
    <groupId>io.gatling</groupId>
    <artifactId>gatling-maven-plugin</artifactId>
    <version>3.9.5</version>
    <configuration>
        <simulationFolder>src/test/scala/com/example/simulations</simulationFolder>
        <resultsFolder>target/gatling/results</resultsFolder>
    </configuration>
</plugin>
```

### Gradle

```groovy
plugins {
    id 'io.gatling.gradle' version '3.9.5'
}

dependencies {
    testImplementation 'io.gatling.highcharts:gatling-charts-highcharts:3.9.5'
}
```

## Basic Simulation

```scala
package com.example.simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class UserSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val scn = scenario("User Simulation")
    .exec(
      http("Get Users")
        .get("/api/users")
        .check(status.is(200))
        .check(jsonPath("$[0].id").saveAs("userId"))
    )
    .pause(1 second)
    .exec(
      http("Get User")
        .get("/api/users/${userId}")
        .check(status.is(200))
    )

  setUp(
    scn.inject(atOnceUsers(10))
  ).protocols(httpProtocol)
}
```

## Injection Profiles

### At Once

```scala
setUp(
  scn.inject(atOnceUsers(100))
)
```

### Ramp Users

```scala
setUp(
  scn.inject(rampUsers(100).during(30 seconds))
)
```

### Constant Rate

```scala
setUp(
  scn.inject(constantUsersPerSec(10).during(1 minute))
)
```

### Incremental Rate

```scala
setUp(
  scn.inject(
    nothingFor(5 seconds),
    atOnceUsers(10),
    rampUsers(50).during(20 seconds),
    constantUsersPerSec(5).during(30 seconds)
  )
)
```

### Stress Test

```scala
setUp(
  scn.inject(
    rampUsers(100).during(30 seconds),
    constantUsersPerSec(20).during(1 minute),
    rampUsers(100).during(30 seconds)
  )
)
```

## HTTP Requests

### GET

```scala
exec(
  http("Get Users")
    .get("/api/users")
    .queryParam("page", "1")
    .queryParam("size", "10")
    .check(status.is(200))
    .check(jsonPath("$.total").saveAs("total"))
)
```

### POST

```scala
exec(
  http("Create User")
    .post("/api/users")
    .body(StringBody("""{"name":"John","email":"john@example.com"}""")).asJSON
    .check(status.is(201))
    .check(jsonPath("$.id").saveAs("userId"))
)
```

### PUT

```scala
exec(
  http("Update User")
    .put("/api/users/${userId}")
    .body(StringBody("""{"name":"John Updated"}""")).asJSON
    .check(status.is(200))
)
```

### DELETE

```scala
exec(
  http("Delete User")
    .delete("/api/users/${userId}")
    .check(status.is(204))
)
```

## Checks

### Status Check

```scala
.check(status.is(200))
.check(status.in(200 to 299))
```

### JSON Path Check

```scala
.check(jsonPath("$.id").saveAs("id"))
.check(jsonPath("$.name").is("John"))
.check(jsonPath("$.email").contains("@"))
```

### XPath Check

```scala
.check(xpath("//user/name").is("John"))
```

### Regex Check

```scala
.check(regex("token=(.*)").saveAs("token"))
```

### Header Check

```scala
.check(header("Content-Type").is("application/json"))
.check(header("X-Request-Id").exists)
```

## Assertions

### Global

```scala
setUp(scn.inject(atOnceUsers(10)))
  .assertions(
    global.responseTime.max.lt(5000),
    global.successfulRequests.percent.gt(95.0)
  )
```

### For Specific Requests

```scala
setUp(scn.inject(atOnceUsers(10)))
  .assertions(
    details("Get Users").responseTime.mean.lt(1000),
    details("Create User").successfulRequests.percent.is(100.0)
  )
```

### Custom Assertions

```scala
setUp(scn.inject(atOnceUsers(10)))
  .assertions(
    global.allRequests.count.gt(100),
    forAll.responseTime.percentile3.lt(3000)
  )
```

## Feeder

### CSV Feeder

```scala
val csvFeeder = csv("users.csv").circular

val scn = scenario("CSV Scenario")
  .feed(csvFeeder)
  .exec(
    http("Get User")
      .get("/api/users/${userId}")
  )
```

### JSON Feeder

```scala
val jsonFeeder = jsonFile("users.json").circular

val scn = scenario("JSON Scenario")
  .feed(jsonFeeder)
  .exec(
    http("Create User")
      .post("/api/users")
      .body(StringBody("""{"name":"${name}","email":"${email}"}""")).asJSON
  )
```

### Batch Feeder

```scala
val batchFeeder = csv("users.csv").batch(10)

val scn = scenario("Batch Scenario")
  .feed(batchFeeder)
  .exec(
    http("Get User")
      .get("/api/users/${userId}")
  )
```

## Protocol Configuration

### HTTP

```scala
val httpProtocol = http
  .baseUrl("http://localhost:8080")
  .acceptHeader("application/json")
  .contentTypeHeader("application/json")
  .authorizationHeader("Bearer ${token}")
  .userAgentHeader("Gatling/3.9")
```

### WebSocket

```scala
val wsProtocol = websocket
  .baseUrl("ws://localhost:8080")

val scn = scenario("WebSocket Scenario")
  .exec(ws("Connect").connect("/ws"))
  .exec(ws("Send Message").sendText("Hello"))
  .exec(ws("Receive Message").checkTextMessage("Hello Back"))
```

### JMS

```scala
val jmsProtocol = jms
  .connectionFactory("ConnectionFactory")
  .url("tcp://localhost:61616")
  .usePooledConnection
  .reconnectNoWait
```

## Helpers

### Checks

```scala
val customCheck: Response => Boolean = response => {
  response.body.string.contains("expected")
}

exec(
  http("Custom Check")
    .get("/api/data")
    .check(bodyString.transform(string => string.length).gt(0))
)
```

### Transformers

```scala
val toUpper = jsonPath("$.name").transform(name => name.toUpperCase)

exec(
  http("Get User")
    .get("/api/users/1")
    .check(jsonPath("$.name").transform(_.toUpperCase).saveAs("upperName"))
)
```

## Best Practices

### Use Appropriate Injection Profile

```scala
// Good: Ramp up gradually
setUp(
  scn.inject(rampUsers(100).during(30 seconds))
)

// Bad: Spike load
setUp(
  scn.inject(atOnceUsers(100))
)
```

### Use Assertions

```scala
setUp(scn.inject(atOnceUsers(10)))
  .assertions(
    global.responseTime.max.lt(5000),
    global.successfulRequests.percent.gt(95.0)
  )
```

### Clean Up Between Tests

```scala
setUp(
  scn.inject(atOnceUsers(10))
).protocols(
  httpProtocol
    .header("Authorization", "Bearer ${token}")
)

// Use before hook
before {
  println("Test starting")
}

after {
  println("Test complete")
}
```

## Resources

- [Gatling Documentation](https://gatling.io/docs/)
- [Gatling GitHub](https://github.com/gatling/gatling)
- [Gatling Quickstart](https://gatling.io/docs/gatling/tutorials/quickstart.html)
