# Chaos Engineering

## Overview

Chaos engineering is the discipline of experimenting on a system to build confidence in its ability to withstand turbulent conditions in production. It involves deliberately introducing failures to discover weaknesses before they cause outages.

## Table of Contents

1. [Principles](#principles)
2. [Chaos Engineering Lifecycle](#chaos-engineering-lifecycle)
3. [Tools](#tools)
4. [Experiments](#experiments)
5. [Failure Injection](#failure-injection)
6. [Steady State Hypothesis](#steady-state-hypothesis)
7. [Blast Radius](#blast-radius)
8. [Safety Practices](#safety-practices)
9. [Implementation Guide](#implementation-guide)
10. [Best Practices](#best-practices)

---

## Principles

### Core Principles

```
1. Build a Hypothesis Around Steady State Behavior
2. Vary Real-World Events
3. Run Experiments in Production
4. Automate Experiments to Run Continuously
5. Minimize Blast Radius
```

### Steady State Definition

```java
public class SteadyStateDefinition {
    private final double errorRateThreshold = 0.01;
    private final double latencyP99Threshold = 200.0;
    private final double throughputMinThreshold = 1000.0;

    public boolean isSteadyState(SystemMetrics metrics) {
        return metrics.getErrorRate() < errorRateThreshold
            && metrics.getLatencyP99() < latencyP99Threshold
            && metrics.getThroughput() > throughputMinThreshold;
    }
}
```

### Hypothesis Structure

```java
public class ChaosHypothesis {
    private String description;
    private SteadyStateDefinition steadyState;
    private FailureMode failureMode;
    private Duration experimentDuration;
    private int blastRadiusPercentage;

    public ExperimentResult evaluate(SystemMetrics before, SystemMetrics during) {
        boolean steadyStateMaintained = steadyState.isSteadyState(during);
        return new ExperimentResult(description, steadyStateMaintained, before, during);
    }
}
```

---

## Chaos Engineering Lifecycle

### 1. Define Steady State

```java
@Component
public class SteadyStateDetector {
    private final MetricsCollector metrics;
    private final AlertManager alerts;

    public SteadyStateReport detectSteadyState(Duration window) {
        SystemMetrics sysMetrics = metrics.collect(window);
        AlertState alertState = alerts.getCurrentState();

        return SteadyStateReport.builder()
            .isHealthy(sysMetrics.getErrorRate() < 0.01)
            .latencyP99(sysMetrics.getLatencyP99())
            .throughput(sysMetrics.getThroughput())
            .errorRate(sysMetrics.getErrorRate())
            .activeAlerts(alertState.getActiveCount())
            .build();
    }
}
```

### 2. Form Hypothesis

```java
public class ChaosExperimentPlan {
    private final String hypothesis;
    private final FailureMode failure;
    private final Duration duration;
    private final int blastRadius;
    private final SteadyStateDefinition steadyState;

    public static ChaosExperimentPlan databaseFailover() {
        return ChaosExperimentPlan.builder()
            .hypothesis("System continues serving reads when primary DB fails over")
            .failure(FailureMode.DATABASE_FAILOVER)
            .duration(Duration.ofMinutes(10))
            .blastRadius(10)
            .steadyState(SteadyStateDefinition.builder()
                .maxErrorRate(0.05)
                .maxLatencyP99(500.0)
                .minThroughput(800.0)
                .build())
            .build();
    }
}
```

### 3. Run Experiment

```java
@Service
public class ChaosExperimentRunner {
    private final FailureInjector injector;
    private final MetricsCollector metrics;
    private final SteadyStateDetector steadyState;

    public ExperimentResult runExperiment(ChaosExperimentPlan plan) {
        SteadyStateReport before = steadyState.detectSteadyState(Duration.ofMinutes(5));

        ExperimentHandle handle = injector.inject(plan.getFailure(), plan.getBlastRadius());

        try {
            Thread.sleep(plan.getDuration().toMillis());
            SteadyStateReport during = steadyState.detectSteadyState(plan.getDuration());

            return ExperimentResult.builder()
                .plan(plan)
                .baseline(before)
                .during(during)
                .hypothesisValid(during.isHealthy())
                .build();
        } finally {
            handle.stop();
        }
    }
}
```

### 4. Analyze Results

```java
public class ExperimentAnalyzer {
    public AnalysisReport analyze(ExperimentResult result) {
        AnalysisReport.Builder builder = AnalysisReport.builder()
            .experimentId(result.getId())
            .hypothesisValid(result.isHypothesisValid());

        if (!result.isHypothesisValid()) {
            builder.addWeakness(Weakness.builder()
                .type(WeaknessType.PERFORMANCE_DEGRADATION)
                .severity(calculateSeverity(result.getBaseline(), result.getDuring()))
                .description("System degraded during " + result.getPlan().getFailure())
                .build());
        }

        return builder.build();
    }
}
```

### 5. Improve System

```java
public class ChaosImprovementCycle {
    private final ExperimentAnalyzer analyzer;
    private final RemediationPlanner planner;

    public ImprovementPlan improve(ExperimentResult result) {
        AnalysisReport analysis = analyzer.analyze(result);
        if (analysis.hasWeaknesses()) {
            return planner.createRemediationPlan(analysis.getWeaknesses());
        }
        return ImprovementPlan.noActionNeeded();
    }
}
```

---

## Tools

### Chaos Monkey (Netflix)

```java
@Configuration
public class ChaosMonkeyConfig {
    @Bean
    public ChaosMonkey chaosMonkey() {
        return ChaosMonkey.builder()
            .enabled(true)
            .aggression(Aggression.MEDIUM)
            .watchedServices(List.of("user-service", "payment-service", "order-service"))
            .schedule(CronExpression.everyHours())
            .build();
    }
}

@Component
public class CustomChaosMonkey {
    private final Random random = new Random();
    private final List<ChaosAction> actions;

    public void execute() {
        if (random.nextDouble() < getProbability()) {
            ChaosAction action = selectAction();
            log.warn("Chaos Monkey executing: {}", action.getDescription());
            action.execute();
        }
    }

    private ChaosAction selectAction() {
        return actions.get(random.nextInt(actions.size()));
    }
}
```

### Litmus (Kubernetes)

```yaml
apiVersion: litmuschaos.io/v1alpha1
kind: ChaosEngine
metadata:
  name: pod-kill-test
  namespace: default
spec:
  appinfo:
    appns: default
    applabel: app=myapp
    appkind: deployment
  chaosServiceAccount: litmus-admin
  experiments:
    - name: pod-delete
      spec:
        components:
          env:
            - name: TOTAL_CHAOS_DURATION
              value: '30'
            - name: CHAOS_INTERVAL
              value: '10'
            - name: FORCE
              value: 'false'
```

### Toxiproxy

```java
@Component
public class ToxiproxyClient {
    private final HttpClient client;
    private final String toxiproxyUrl;

    public void addLatency(String proxyName, Duration latency) {
        String url = toxiproxyUrl + "/proxies/" + proxyName + "/toxics";
        String body = String.format("{\"name\":\"latency\",\"type\":\"latency\","
            + "\"attributes\":{\"latency\":%d,\"jitter\":%d}}",
            latency.toMillis(), latency.toMillis() / 10);
        client.post(url, body);
    }

    public void addConnectionError(String proxyName, double probability) {
        String url = toxiproxyUrl + "/proxies/" + proxyName + "/toxics";
        String body = String.format("{\"name\":\"timeout\",\"type\":\"timeout\","
            + "\"attributes\":{\"timeout\":0},\"toxicity\":%.2f}", probability);
        client.post(url, body);
    }

    public void removeToxic(String proxyName, String toxicName) {
        String url = toxiproxyUrl + "/proxies/" + proxyName + "/toxics/" + toxicName;
        client.delete(url);
    }
}
```

### Chaos Toolkit

```json
{
  "title": "Database failover resilience",
  "description": "Verify application continues during database failover",
  "steady-state-hypothesis": {
    "title": "System is healthy",
    "probes": [
      {
        "type": "probe",
        "name": "api-responds",
        "tolerance": 200,
        "provider": {"type": "http", "url": "http://localhost:8080/health"}
      }
    ]
  },
  "method": [
    {
      "type": "action",
      "name": "stop-database",
      "provider": {"type": "process", "cmd": "docker stop postgres-primary"}
    },
    {"type": "pause", "name": "wait-for-failover", "duration": 30}
  ],
  "rollbacks": [
    {
      "type": "action",
      "name": "start-database",
      "provider": {"type": "process", "cmd": "docker start postgres-primary"}
    }
  ]
}
```

---

## Experiments

### Pod Kill Experiment

```java
@Component
public class PodKillExperiment implements ChaosExperiment {
    private final KubernetesClient k8sClient;
    private final MetricsCollector metrics;

    @Override
    public String getName() { return "pod-kill"; }

    @Override
    public ExperimentResult execute(ExperimentConfig config) {
        String namespace = config.getNamespace();
        String labelSelector = config.getLabelSelector();

        List<Pod> pods = k8sClient.pods()
            .inNamespace(namespace)
            .withLabelSelector(labelSelector)
            .list().getItems();

        SteadyStateReport before = metrics.getSteadyState();

        Pod target = pods.get(0);
        log.info("Killing pod: {}", target.getMetadata().getName());
        k8sClient.pods().inNamespace(namespace)
            .withName(target.getMetadata().getName()).delete();

        waitForRecovery(namespace, labelSelector, config.getRecoveryTimeout());

        SteadyStateReport after = metrics.getSteadyState();

        return ExperimentResult.builder()
            .experimentName(getName())
            .target(target.getMetadata().getName())
            .steadyStateBefore(before)
            .steadyStateAfter(after)
            .recovered(isRecovered(before, after))
            .build();
    }

    private void waitForRecovery(String namespace, String label, Duration timeout) {
        Instant deadline = Instant.now().plus(timeout);
        while (Instant.now().isBefore(deadline)) {
            int readyReplicas = k8sClient.apps().deployments()
                .inNamespace(namespace).withLabel("app", label)
                .list().getItems().stream()
                .mapToInt(d -> d.getStatus().getReadyReplicas() != null
                    ? d.getStatus().getReadyReplicas() : 0).sum();
            if (readyReplicas >= 1) return;
            Thread.sleep(1000);
        }
    }
}
```

### Network Latency Experiment

```java
@Component
public class NetworkLatencyExperiment implements ChaosExperiment {
    private final ToxiproxyClient toxiproxy;

    @Override
    public ExperimentResult execute(ExperimentConfig config) {
        String proxyName = config.getTargetService();
        Duration latency = config.getLatencyDuration();

        SteadyStateReport before = metrics.getSteadyState();
        toxiproxy.addLatency(proxyName, latency);
        Thread.sleep(config.getDuration().toMillis());
        toxiproxy.removeToxic(proxyName, "latency");
        SteadyStateReport after = metrics.getSteadyState();

        return ExperimentResult.builder()
            .experimentName(getName())
            .steadyStateBefore(before)
            .steadyStateAfter(after)
            .recovered(isRecovered(before, after))
            .build();
    }
}
```

### CPU/Memory Stress Experiment

```java
@Component
public class ResourceStressExperiment implements ChaosExperiment {
    private final DockerClient docker;

    @Override
    public ExperimentResult execute(ExperimentConfig config) {
        String containerId = config.getTargetContainer();
        StressParams params = config.getStressParams();
        SteadyStateReport before = metrics.getSteadyState();

        docker.exec(containerId, "stress-ng", "--cpu",
            String.valueOf(params.getCpuWorkers()),
            "--timeout", config.getDuration().toString());

        docker.updateContainer(containerId,
            ResourceConstraints.builder()
                .cpuQuota(params.getCpuQuota())
                .memoryLimit(params.getMemoryLimit())
                .build());

        SteadyStateReport after = metrics.getSteadyState();

        docker.updateContainer(containerId,
            ResourceConstraints.builder().cpuQuota(-1).memoryLimit(null).build());

        return ExperimentResult.builder()
            .experimentName(getName())
            .steadyStateBefore(before)
            .steadyStateAfter(after)
            .recovered(isRecovered(before, after))
            .build();
    }
}
```

### DNS Failure Experiment

```java
@Component
public class DnsFailureExperiment implements ChaosExperiment {
    private final NetworkManager network;

    @Override
    public ExperimentResult execute(ExperimentConfig config) {
        SteadyStateReport before = metrics.getSteadyState();
        network.blockDns(config.getTargetDnsServer());
        try {
            Thread.sleep(config.getDuration().toMillis());
        } finally {
            network.restoreDns(config.getTargetDnsServer());
        }
        SteadyStateReport after = metrics.getSteadyState();

        return ExperimentResult.builder()
            .experimentName(getName())
            .steadyStateBefore(before)
            .steadyStateAfter(after)
            .recovered(isRecovered(before, after))
            .build();
    }
}
```

---

## Steady State Hypothesis

### Metrics Collection

```java
@Component
public class MetricsCollector {
    private final MeterRegistry registry;

    public SystemMetrics collect(Duration window) {
        return SystemMetrics.builder()
            .errorRate(calculateErrorRate(window))
            .latencyP50(calculatePercentile(window, 0.50))
            .latencyP95(calculatePercentile(window, 0.95))
            .latencyP99(calculatePercentile(window, 0.99))
            .throughput(calculateThroughput(window))
            .cpuUsage(calculateCpuUsage())
            .memoryUsage(calculateMemoryUsage())
            .activeConnections(getActiveConnections())
            .build();
    }

    private double calculateErrorRate(Duration window) {
        Counter errors = registry.counter("http.server.errors");
        Counter total = registry.counter("http.server.requests");
        return errors.count() / total.count();
    }

    private double calculatePercentile(Duration window, double percentile) {
        Timer timer = registry.timer("http.server.latency");
        return timer.takeSnapshot().getValue(percentile) / 1_000_000.0;
    }
}
```

### Health Checks

```java
@Component
public class HealthChecker {
    private final List<HealthIndicator> indicators;

    public SystemHealth checkHealth() {
        List<ComponentHealth> components = indicators.stream()
            .map(this::checkComponent)
            .collect(Collectors.toList());

        return SystemHealth.builder()
            .overallStatus(determineOverallStatus(components))
            .components(components)
            .timestamp(Instant.now())
            .build();
    }

    private ComponentHealth checkComponent(HealthIndicator indicator) {
        try {
            Health health = indicator.health();
            return ComponentHealth.builder()
                .name(indicator.getClass().getSimpleName())
                .status(health.getStatus().toString())
                .details(health.getDetails())
                .build();
        } catch (Exception e) {
            return ComponentHealth.builder()
                .name(indicator.getClass().getSimpleName())
                .status("DOWN")
                .error(e.getMessage())
                .build();
        }
    }
}
```

### Automated Assertions

```java
public class SteadyStateAssertion {
    private final List<MetricAssertion> assertions;

    public AssertionResult evaluate(SystemMetrics metrics) {
        List<AssertionFailure> failures = new ArrayList<>();

        for (MetricAssertion assertion : assertions) {
            if (!assertion.test(metrics)) {
                failures.add(AssertionFailure.builder()
                    .metric(assertion.getMetricName())
                    .expected(assertion.getExpected())
                    .actual(assertion.getActual(metrics))
                    .build());
            }
        }

        return AssertionResult.builder()
            .passed(failures.isEmpty())
            .failures(failures)
            .build();
    }

    public static SteadyStateAssertion defaultAssertion() {
        return new SteadyStateAssertion(List.of(
            MetricAssertion.lessThan("errorRate", 0.01),
            MetricAssertion.lessThan("latencyP99", 200.0),
            MetricAssertion.greaterThan("throughput", 1000.0),
            MetricAssertion.equals("circuitBreakerState", "CLOSED")
        ));
    }
}
```

---

## Blast Radius

### Blast Radius Calculator

```java
public class BlastRadiusCalculator {
    private final ServiceRegistry registry;

    public BlastRadius calculate(ExperimentConfig config) {
        Set<String> affected = determineAffectedServices(config);
        double pct = (double) affected.size() / registry.totalServices() * 100;

        return BlastRadius.builder()
            .percentage(pct)
            .affectedServices(affected)
            .riskLevel(determineRiskLevel(pct))
            .affectedUsers(estimateAffectedUsers(affected))
            .build();
    }

    private RiskLevel determineRiskLevel(double pct) {
        if (pct <= 5) return RiskLevel.LOW;
        if (pct <= 20) return RiskLevel.MEDIUM;
        if (pct <= 50) return RiskLevel.HIGH;
        return RiskLevel.CRITICAL;
    }
}
```

### Canary Experiments

```java
public class CanaryExperiment {
    private final ExperimentRunner runner;

    public ExperimentResult runCanary(ChaosExperimentPlan plan) {
        BlastRadiusConfig canaryConfig = BlastRadiusConfig.builder()
            .percentage(1).targetGroup("canary").build();

        ExperimentResult canaryResult = runner.execute(plan, canaryConfig);

        if (canaryResult.isHypothesisValid()) {
            return expandExperiment(plan, canaryResult);
        } else {
            log.warn("Canary experiment failed, aborting");
            return canaryResult;
        }
    }

    private ExperimentResult expandExperiment(ChaosExperimentPlan plan,
                                              ExperimentResult canaryResult) {
        int[] stages = {5, 10, 25, 50};
        ExperimentResult lastResult = canaryResult;
        for (int stage : stages) {
            BlastRadiusConfig cfg = BlastRadiusConfig.builder().percentage(stage).build();
            lastResult = runner.execute(plan, cfg);
            if (!lastResult.isHypothesisValid()) {
                log.warn("Experiment failed at {}% blast radius", stage);
                break;
            }
            Thread.sleep(Duration.ofMinutes(5).toMillis());
        }
        return lastResult;
    }
}
```

### Scope Control

```java
@Component
public class ScopeController {
    private final ServiceRegistry registry;

    public ExperimentScope createScope(ScopeConfig config) {
        return ExperimentScope.builder()
            .namespace(config.getNamespace())
            .labelSelector(config.getLabelSelector())
            .includeServices(config.getIncludeServices())
            .excludeServices(config.getExcludeServices())
            .maxInstances(config.getMaxInstancesPerService())
            .build();
    }

    public boolean isServiceInScope(String serviceName, ExperimentScope scope) {
        if (scope.getExcludeServices().contains(serviceName)) return false;
        if (scope.getIncludeServices() != null
            && !scope.getIncludeServices().contains(serviceName)) return false;
        return registry.getInstanceCount(serviceName) <= scope.getMaxInstances();
    }
}
```

---

## Safety Practices

### Kill Switch

```java
@Component
public class ChaosKillSwitch {
    private final AtomicBoolean enabled = new AtomicBoolean(true);
    private final ExperimentRegistry registry;

    @EventListener
    public void onEmergencyStop(EmergencyStopEvent event) {
        enabled.set(false);
        log.warn("Emergency stop triggered: {}", event.getReason());
        registry.getAllRunning().forEach(experiment -> {
            try {
                experiment.abort();
                log.info("Aborted experiment: {}", experiment.getName());
            } catch (Exception e) {
                log.error("Failed to abort: {}", experiment.getName(), e);
            }
        });
    }

    public boolean isEnabled() { return enabled.get(); }
}
```

### Automatic Rollback

```java
@Component
public class AutomaticRollback {
    private final RollbackRegistry registry;

    @EventListener
    public void onExperimentFailed(ExperimentFailedEvent event) {
        RollbackPlan plan = registry.getPlan(event.getExperimentId());
        if (plan != null) {
            log.warn("Auto-rolling back: {}", event.getExperimentId());
            plan.execute();
        }
    }

    public void registerRollback(String experimentId, RollbackPlan plan) {
        registry.register(experimentId, plan);
    }
}
```

### Notification System

```java
@Component
public class ChaosNotificationSystem {
    private final SlackClient slack;
    private final PagerDutyClient pager;

    public void notifyExperimentStart(ChaosExperimentPlan plan) {
        Notification notification = Notification.builder()
            .title("Chaos Experiment Started")
            .description(plan.getDescription())
            .blastRadius(plan.getBlastRadius() + "%")
            .duration(plan.getDuration().toString())
            .severity(NotificationSeverity.INFO)
            .build();
        slack.send(notification);
        if (plan.getBlastRadius() > 20) {
            pager.sendIncident(notification);
        }
    }

    public void notifyExperimentComplete(ExperimentResult result) {
        Notification notification = Notification.builder()
            .title("Chaos Experiment Completed")
            .experimentId(result.getId())
            .hypothesisValid(result.isHypothesisValid())
            .severity(result.isHypothesisValid()
                ? NotificationSeverity.INFO
                : NotificationSeverity.WARNING)
            .build();
        slack.send(notification);
    }
}
```

---

## Implementation Guide

### Setting Up First Chaos Experiment

```java
@Configuration
public class ChaosEngineeringSetup {
    @Bean
    public ChaosEngineeringPlatform chaosPlatform() {
        return ChaosEngineeringPlatform.builder()
            .enabled(true)
            .environment(Environment.STAGING)
            .metricsEndpoint("http://prometheus:9090")
            .notificationChannel(SlackChannel.CHAOS_EXPERIMENTS)
            .safetyGuard(SafetyGuard.builder()
                .maxBlastRadius(25)
                .requireApprovalAbove(10)
                .autoRollbackEnabled(true)
                .build())
            .build();
    }
}
```

### Experiment Schedule

```java
@Component
public class ExperimentScheduler {
    private final ChaosExperimentRunner runner;

    @Scheduled(cron = "0 0 2 * * MON") // Every Monday at 2 AM
    public void runWeeklyExperiments() {
        List<ChaosExperimentPlan> experiments = List.of(
            ChaosExperimentPlan.podKill(),
            ChaosExperimentPlan.networkLatency(),
            ChaosExperimentPlan.diskPressure(),
            ChaosExperimentPlan.dnsFailure()
        );

        for (ChaosExperimentPlan plan : experiments) {
            ExperimentResult result = runner.runExperiment(plan);
            logExperimentResult(result);
        }
    }
}
```

### Integration with CI/CD

```yaml
# Jenkinsfile / GitHub Actions
chaos-experiment:
  stage: Chaos Engineering
  script:
    - kubectl apply -f chaos-experiments/
    - sleep 300
    - kubectl get chaosresults -o yaml
    - python validate_results.py
  allow_failure: true
  only:
    - main
```

---

## Best Practices

### Do's and Don'ts

```
DO:
  Start small and gradually increase blast radius
  Always have a rollback plan
  Run experiments in staging before production
  Document all experiments and results
  Involve the team in experiment design
  Use automated experiments for continuous validation

DON'T:
  Skip defining steady state
  Run experiments without approval
  Ignore experiment results
  Test in production without monitoring
  Run too many experiments simultaneously
  Forget to notify stakeholders
```

### Experiment Documentation Template

```markdown
## Experiment: [Name]

**Hypothesis**: [What you expect to happen]
**Steady State**: [Normal behavior definition]
**Failure Mode**: [What will be injected]
**Blast Radius**: [Percentage of system affected]
**Duration**: [How long the experiment runs]
**Rollback Plan**: [How to revert changes]
**Success Criteria**: [What constitutes passing]

### Results
- Baseline: [Metrics before experiment]
- During: [Metrics during experiment]
- Recovery Time: [Time to return to steady state]
- Conclusion: [Pass/Fail and learnings]
```

### Maturity Model

```
Level 1 - Ad Hoc:
  Manual experiments, no automation
  Basic failure injection
  Reactive approach

Level 2 - Repeatable:
  Scripted experiments
  Automated rollback
  Regular experiment schedule

Level 3 - Defined:
  Experiment library
  CI/CD integration
  Formal steady state definitions

Level 4 - Managed:
  Continuous experiments
  Blast radius automation
  Cross-team coordination

Level 5 - Optimized:
  AI-driven experiments
  Predictive failure analysis
  Full production chaos
```

---

## Related Topics

- Unit Testing
- Integration Testing
- Monitoring & Observability
- Incident Response
