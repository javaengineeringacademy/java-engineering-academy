"""
Module 16: Best Practices - Code Review Solutions
Practice code review and best practices.
"""

from typing import List, Dict, Any, Optional
from dataclasses import dataclass
from abc import ABC, abstractmethod


class CodeReviewer:
    """Code review checklist and best practices."""

    def __init__(self):
        self.checklist = {
            "naming": ["Use descriptive names", "Follow conventions", "Avoid abbreviations"],
            "structure": ["Single responsibility", "DRY principle", "KISS principle"],
            "documentation": ["Add docstrings", "Comment complex logic", "Type hints"],
            "testing": ["Unit tests", "Edge cases", "Error handling"],
            "performance": ["Avoid premature optimization", "Profile before optimizing"]
        }

    def review(self, code: str) -> Dict[str, List[str]]:
        """Review code and return suggestions."""
        suggestions = {}

        # Check for common issues
        if "TODO" in code:
            suggestions.setdefault("general", []).append("Contains TODO comments")

        if "pass" in code:
            suggestions.setdefault("general", []).append("Contains pass statements")

        if len(code.split('\n')) > 100:
            suggestions.setdefault("structure", []).append("Consider breaking into smaller functions")

        return suggestions


@dataclass
class ReviewResult:
    """Result of a code review."""

    file: str
    score: int
    issues: List[str]
    suggestions: List[str]

    def __post_init__(self):
        if self.score < 0 or self.score > 100:
            raise ValueError("Score must be between 0 and 100")

    def is_passing(self) -> bool:
        """Check if review passed."""
        return self.score >= 70

    def summary(self) -> str:
        """Get review summary."""
        status = "PASS" if self.is_passing() else "FAIL"
        return f"[{status}] Score: {self.score}/100, Issues: {len(self.issues)}"


class CodeStyleChecker:
    """Check code style compliance."""

    def __init__(self):
        self.rules = {
            "max_line_length": 79,
            "max_function_length": 50,
            "max_class_methods": 20
        }

    def check_line_length(self, code: str) -> List[str]:
        """Check for lines exceeding max length."""
        issues = []
        for i, line in enumerate(code.split('\n'), 1):
            if len(line) > self.rules["max_line_length"]:
                issues.append(f"Line {i} exceeds {self.rules['max_line_length']} characters")
        return issues

    def check_function_length(self, code: str) -> List[str]:
        """Check for functions exceeding max length."""
        issues = []
        in_function = False
        function_name = ""
        function_length = 0

        for line in code.split('\n'):
            if line.strip().startswith('def '):
                if in_function and function_length > self.rules["max_function_length"]:
                    issues.append(f"Function '{function_name}' exceeds {self.rules['max_function_length']} lines")
                in_function = True
                function_name = line.strip().split('(')[0].replace('def ', '')
                function_length = 0
            elif in_function:
                function_length += 1

        if in_function and function_length > self.rules["max_function_length"]:
            issues.append(f"Function '{function_name}' exceeds {self.rules['max_function_length']} lines")

        return issues


class DocumentationChecker:
    """Check documentation quality."""

    def check_docstrings(self, code: str) -> List[str]:
        """Check for missing docstrings."""
        issues = []
        lines = code.split('\n')

        for i, line in enumerate(lines):
            if line.strip().startswith('def ') and not line.strip().startswith('def __'):
                # Check if next non-empty line is a docstring
                j = i + 1
                while j < len(lines) and not lines[j].strip():
                    j += 1
                if j < len(lines) and not lines[j].strip().startswith('"""'):
                    issues.append(f"Function at line {i+1} missing docstring")

        return issues


class TestCoverageChecker:
    """Check test coverage."""

    def __init__(self):
        self.coverage = {}

    def track_function(self, function_name: str):
        """Track that a function has been tested."""
        self.coverage[function_name] = True

    def get_coverage(self) -> Dict[str, bool]:
        """Get coverage status."""
        return self.coverage.copy()

    def get_coverage_percentage(self) -> float:
        """Get coverage percentage."""
        if not self.coverage:
            return 0.0
        tested = sum(1 for v in self.coverage.values() if v)
        return (tested / len(self.coverage)) * 100


class PerformanceProfiler:
    """Profile code performance."""

    def __init__(self):
        self.measurements = {}

    def measure(self, name: str, func, *args, **kwargs):
        """Measure function execution time."""
        start = time.time()
        result = func(*args, **kwargs)
        end = time.time()

        self.measurements[name] = {
            "time": end - start,
            "result": result
        }
        return result

    def get_slowest(self, n: int = 5) -> List[str]:
        """Get n slowest functions."""
        sorted_measurements = sorted(
            self.measurements.items(),
            key=lambda x: x[1]["time"],
            reverse=True
        )
        return [name for name, _ in sorted_measurements[:n]]


import time

if __name__ == "__main__":
    print("Testing Code Review Solutions...")

    # Test CodeReviewer
    reviewer = CodeReviewer()
    suggestions = reviewer.review("def func():\n    pass\n    # TODO")
    assert "general" in suggestions
    print("✓ Exercise 1 passed: code reviewer works")

    # Test ReviewResult
    result = ReviewResult(
        file="test.py",
        score=85,
        issues=["Minor style issues"],
        suggestions=["Add more comments"]
    )
    assert result.is_passing() is True
    assert "PASS" in result.summary()
    print("✓ Exercise 2 passed: review result works")

    # Test CodeStyleChecker
    checker = CodeStyleChecker()
    issues = checker.check_line_length("a" * 100)
    assert len(issues) > 0
    print("✓ Exercise 3 passed: style checker works")

    # Test DocumentationChecker
    doc_checker = DocumentationChecker()
    issues = doc_checker.check_docstrings("def func():\n    pass")
    assert len(issues) > 0
    print("✓ Exercise 4 passed: documentation checker works")

    # Test TestCoverageChecker
    coverage = TestCoverageChecker()
    coverage.track_function("func1")
    coverage.track_function("func2")
    assert coverage.get_coverage_percentage() == 100.0
    print("✓ Exercise 5 passed: test coverage checker works")

    # Test PerformanceProfiler
    profiler = PerformanceProfiler()
    profiler.measure("fast", lambda: sum(range(100)))
    profiler.measure("slow", lambda: time.sleep(0.01))
    assert "slow" in profiler.get_slowest(1)
    print("✓ Exercise 6 passed: performance profiler works")

    print("All Code Review solutions passed!")
