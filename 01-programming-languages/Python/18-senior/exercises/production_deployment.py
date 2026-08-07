"""
Module 18: Senior - Production Deployment Exercises
===================================================
Practice production deployment patterns and best practices.
"""

import os
import json

# =============================================================================
# Exercise 1: Configuration Manager (★☆☆☆☆)
# =============================================================================
# TODO: Implement multi-source configuration

class ConfigManager:
    """Load config from multiple sources with precedence."""
    # TODO: Support defaults, env vars, config files
    # TODO: Merge with proper precedence
    pass

# Test Cases
def test_config_manager():
    os.environ["TEST_DB_HOST"] = "localhost"
    
    config = ConfigManager(
        defaults={"db_host": "default.com", "db_port": 5432},
        env_prefix="TEST_",
        env_mapping={"DB_HOST": "db_host", "DB_PORT": "db_port"}
    )
    
    assert config.get("db_host") == "localhost"
    assert config.get("db_port") == 5432
    print("✓ Exercise 1 passed: config loaded from env vars")

# =============================================================================
# Exercise 2: Feature Flags (★★☆☆☆)
# =============================================================================
# TODO: Implement feature flag system

class FeatureFlags:
    """Feature flag management with percentage rollout."""
    # TODO: Check if feature is enabled
    # TODO: Support percentage-based rollout
    pass

# Test Cases
def test_feature_flags():
    flags = FeatureFlags()
    flags.enable("new_checkout", percentage=50)
    
    enabled_count = sum(1 for _ in range(1000) if flags.is_enabled("new_checkout", user_id=_))
    assert 300 < enabled_count < 700  # Should be around 500
    print(f"✓ Exercise 2 passed: {enabled_count}/1000 users see feature")

# =============================================================================
# Exercise 3: Circuit Breaker (★★★☆☆)
# =============================================================================
# TODO: Implement circuit breaker pattern

class CircuitBreaker:
    """Circuit breaker for fault tolerance."""
    # TODO: Track failures
    # TODO: Open circuit after threshold
    # TODO: Reset after timeout
    pass

# Test Cases
def test_circuit_breaker():
    call_count = 0
    
    @CircuitBreaker(failure_threshold=3, recovery_timeout=1)
    def flaky_service():
        nonlocal call_count
        call_count += 1
        if call_count <= 3:
            raise ConnectionError("Service unavailable")
        return "success"
    
    # First 3 calls should fail
    for _ in range(3):
        try:
            flaky_service()
        except:
            pass
    
    # Circuit should be open, call should fail immediately
    try:
        flaky_service()
    except CircuitOpenError:
        pass
    
    print("✓ Exercise 3 passed: circuit breaker opened after failures")

# =============================================================================
# Exercise 4: Health Check System (★★★★☆)
# =============================================================================
# TODO: Implement health check system

class HealthChecker:
    """Run health checks on services."""
    # TODO: Register health checks
    # TODO: Run checks and report status
    pass

# Test Cases
def test_health_checker():
    checker = HealthChecker()
    
    @checker.register("database")
    def check_db():
        return {"status": "healthy", "latency_ms": 5}
    
    @checker.register("cache")
    def check_cache():
        return {"status": "unhealthy", "error": "connection refused"}
    
    report = checker.run_all()
    assert report["database"]["status"] == "healthy"
    assert report["cache"]["status"] == "unhealthy"
    assert report["overall"] == "degraded"
    print(f"✓ Exercise 4 passed: health check report generated")

# =============================================================================
# Exercise 5: Deployment Pipeline (★★★★★)
# =============================================================================
# TODO: Implement deployment pipeline

class DeploymentPipeline:
    """Manage deployment stages and gates."""
    # TODO: Define stages
    # TODO: Run validations between stages
    # TODO: Support rollback
    pass

# Test Cases
def test_deployment_pipeline():
    pipeline = DeploymentPipeline()
    executed_stages = []
    
    pipeline.add_stage("build", lambda: executed_stages.append("build"))
    pipeline.add_stage("test", lambda: executed_stages.append("test"))
    pipeline.add_stage("deploy", lambda: executed_stages.append("deploy"))
    pipeline.add_gate("test", lambda: True)  # Tests must pass
    
    result = pipeline.run()
    assert result["status"] == "success"
    assert executed_stages == ["build", "test", "deploy"]
    
    # Test with failing gate
    pipeline2 = DeploymentPipeline()
    pipeline2.add_stage("build", lambda: None)
    pipeline2.add_stage("test", lambda: None)
    pipeline2.add_gate("test", lambda: False)
    
    result2 = pipeline2.run()
    assert result2["status"] == "failed"
    assert result2["failed_at"] == "test"
    print(f"✓ Exercise 5 passed: pipeline executed {len(executed_stages)} stages")

if __name__ == "__main__":
    print("Running Production Deployment Exercises...")
    print("=" * 50)
    test_config_manager()
    test_feature_flags()
    test_circuit_breaker()
    test_health_checker()
    test_deployment_pipeline()
    print("=" * 50)
    print("All tests passed!")
