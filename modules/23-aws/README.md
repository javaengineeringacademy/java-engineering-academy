# Module 23: AWS

## Overview

This module covers Amazon Web Services (AWS) for Java developers. Students will learn core AWS services including compute, storage, databases, and messaging, along with best practices for building scalable, resilient cloud-native applications using Spring Boot.

## Learning Objectives

By the end of this module, you will be able to:

- Navigate AWS console and CLI
- Deploy applications on EC2 instances
- Store and retrieve objects using S3
- Connect to managed databases with RDS
- Implement message queuing with SQS
- Build serverless functions with Lambda
- Integrate AWS services with Spring Boot

## Prerequisites

- [Module 22: Kubernetes](../22-kubernetes/)

## Topics

| # | Topic | Duration | Description |
|---|-------|----------|-------------|
| 01 | [AWS Fundamentals](01-aws-fundamentals/) | 2 hours | AWS ecosystem, regions, IAM |
| 02 | [EC2](02-aws-ec2/) | 2 hours | Virtual servers, security groups, key pairs |
| 03 | [S3](03-aws-s3/) | 2 hours | Object storage, buckets, lifecycle policies |
| 04 | [RDS](04-aws-rds/) | 2 hours | Managed databases, multi-AZ, read replicas |
| 05 | [SQS](05-aws-sqs/) | 2 hours | Message queues, dead letter queues |
| 06 | [Lambda](06-aws-lambda/) | 2 hours | Serverless functions, event triggers |
| 07 | [Spring Boot AWS](07-aws-spring-boot/) | 2 hours | AWS SDK, Spring Cloud AWS integration |

## Key Concepts

- Infrastructure as Code (IaC)
- High availability and fault tolerance
- Cost optimization strategies
- Security best practices
- Event-driven architecture on AWS

## Enterprise Applications

AWS provides the foundation for cloud-native enterprise applications, offering scalable infrastructure, managed services, and global reach for deploying Java applications with reliability and performance.

## Estimated Total Time

**14 hours**

## Module Project

Build a **Cloud-Native File Processing System** that:
- Stores uploaded files in S3
- Processes files using Lambda functions
- Queues tasks with SQS
- Stores metadata in RDS
- Deploys with Spring Boot on EC2

## Resources

- [AWS Documentation](https://docs.aws.amazon.com/)
- [AWS SDK for Java](https://docs.aws.amazon.com/sdk-for-java/)

**Previous Module**: [Module 22: Kubernetes](../22-kubernetes/)
**Next Module**: [Module 24: System Design](../24-system-design/)