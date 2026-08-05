# Fortify

## Overview

Fortify (Micro Focus/OpenText) is an application security platform providing static application security testing (SAST), dynamic application security testing (DAST), software composition analysis (SCA), and runtime protection. It integrates security testing throughout the software development lifecycle, from code analysis to deployment.

## Why It Matters

Fortify enables organizations to identify and remediate security vulnerabilities early in development, reducing the cost and risk of production security incidents. Its comprehensive coverage across source code, binaries, and running applications helps teams meet compliance requirements and build secure software by design.

## Key Concepts

- **SAST (Static Application Security Testing):** Source code analysis without execution to find vulnerabilities
- **DAST (Dynamic Application Security Testing):** Testing running applications for runtime vulnerabilities
- **SCA (Software Composition Analysis):** Identifying vulnerabilities in open-source dependencies
- **WebInspect:** DAST tool for web application security testing
- **Fortify SCA:** Command-line static analysis engine
- **Fortify on Demand:** Cloud-based security testing service
- **Security Audit Workbench:** IDE-based vulnerability review and remediation
- **Rulesets and Policies:** Configurable analysis rules for targeted scanning

## Core Topics

- Static code analysis for Java, C#, Python, PHP, and JavaScript
- DAST scanning with authenticated and unauthenticated testing
- SCA scanning of third-party libraries and dependencies
- Integration of Fortify SCA into CI/CD pipelines (Jenkins, Azure DevOps, GitLab)
- Vulnerability triage and remediation workflow management
- Custom ruleset creation for organization-specific security requirements
- Fortify on Demand for outsourced security testing
- Compliance reporting for OWASP Top 10, CWE, and SANS Top 25

## Best Practices

- Integrate SAST scans early in the development process for fast feedback
- Use targeted scan policies to reduce false positives and scan times
- Combine SAST, DAST, and SCA for comprehensive vulnerability coverage
- Establish remediation SLAs based on vulnerability severity ratings
- Train developers on common vulnerability patterns found by Fortify
- Use Fortify's audit workbench to streamline vulnerability review
- Maintain and update rulesets to reflect evolving security standards
- Track vulnerability trends over time to measure security program effectiveness

## Hands-on Labs

1. Install Fortify SCA and perform a static scan on a sample application
2. Configure a Fortify scan with custom rulesets for a specific vulnerability class
3. Integrate Fortify SCA into a Jenkins pipeline for automated security scanning
4. Use WebInspect to perform a DAST scan against a test web application
5. Perform an SCA scan to identify vulnerable open-source dependencies
6. Review and triage scan results using Fortify Audit Workbench
7. Generate a compliance report for OWASP Top 10 coverage

## Interview Questions

1. How does SAST differ from DAST, and when would you use each approach?
2. Explain the role of SCA in application security and why it matters for modern development.
3. How would you integrate Fortify into a CI/CD pipeline to provide fast security feedback?
4. What strategies would you use to reduce false positives in Fortify scan results?
5. Describe the workflow for triaging and remediating a critical Fortify vulnerability.
6. How does Fortify handle multi-language applications with diverse technology stacks?
7. What compliance frameworks does Fortify support, and how does it map vulnerabilities to standards?
8. Explain the difference between Fortify SCA and Fortify on Demand and when each is appropriate.

## References

- [Fortify Official Documentation](https://www.microfocus.com/documentation/fortify/)
- [Fortify SCA Command Reference](https://www.microfocus.com/documentation/fortify/fortify-sca-and-plugins/)
- [WebInspect User Guide](https://www.microfocus.com/documentation/fortify/webinspect/)
- [Fortify Integration Guides](https://www.microfocus.com/documentation/fortify/)
- [OWASP Top 10 Reference](https://owasp.org/www-project-top-ten/)
