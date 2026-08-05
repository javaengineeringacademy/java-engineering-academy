# AWS Project Structure

## CDK Project Layout

```
aws-project/
├── bin/
│   └── app.ts                    # CDK app entry point
├── lib/
│   ├── stacks/
│   │   ├── vpc-stack.ts          # VPC infrastructure
│   │   ├── ecs-stack.ts          # ECS cluster and services
│   │   ├── rds-stack.ts          # Database resources
│   │   ├── monitoring-stack.ts   # CloudWatch alarms
│   │   └── ci-cd-stack.ts        # CodePipeline
│   ├── constructs/
│   │   ├── alb-construct.ts      # Load balancer construct
│   │   ├── ecs-service.ts        # ECS service construct
│   │   └── rds-instance.ts       # RDS construct
│   └── config/
│       ├── dev.ts                # Dev environment config
│       ├── staging.ts            # Staging config
│       └── prod.ts               # Production config
├── test/
│   └── stacks/
│       └── vpc-stack.test.ts     # Unit tests
├── cdk.json                      # CDK configuration
├── tsconfig.json                 # TypeScript config
├── package.json                  # Node dependencies
└── README.md
```

## CloudFormation Project Layout

```
aws-project/
├── templates/
│   ├── vpc.yaml                  # VPC template
│   ├── ecs-cluster.yaml          # ECS cluster
│   ├── rds.yaml                  # Database
│   ├── monitoring.yaml           # CloudWatch
│   └── parameters/
│       ├── dev.json              # Dev parameters
│       ├── staging.json          # Staging parameters
│       └── prod.json             # Production parameters
├── scripts/
│   ├── deploy.sh                 # Deployment script
│   ├── validate.sh               # Template validation
│   └── cleanup.sh                # Resource cleanup
├── packaged-templates/           # SAM packaged templates
└── README.md
```

## Serverless Project Layout

```
aws-project/
├── functions/
│   ├── api/
│   │   ├── get-items/
│   │   │   ├── index.ts
│   │   │   └── package.json
│   │   └── create-item/
│   │       ├── index.ts
│   │       └── package.json
│   └── events/
│       └── process-sqs/
│           ├── index.ts
│           └── package.json
├── layers/
│   └── common/
│       └── package.json
├── templates/
│   └── template.yaml             # SAM template
├── tests/
│   └── unit/
│       └── api.test.ts
├── samconfig.toml                # SAM configuration
└── README.md
```

## Multi-Account Strategy

```
aws-organization/
├── management-account/
│   ├── iam/
│   │   ├── users/
│   │   ├── groups/
│   │   └── roles/
│   └── organizations/
├── dev-account/
│   ├── vpc/
│   ├── ecs/
│   └── rds/
├── staging-account/
│   ├── vpc/
│   ├── ecs/
│   └── rds/
├── prod-account/
│   ├── vpc/
│   ├── ecs/
│   └── rds/
└── security-account/
    ├── guardduty/
    ├── security-hub/
    └── cloudtrail/
```

## File Naming Conventions

- Use kebab-case for file names
- Group by service or resource type
- Use consistent naming across environments
- Include README.md in each directory
- Separate infrastructure from application code

## Environment Configuration

```typescript
// lib/config/dev.ts
export const devConfig = {
  vpc: { cidr: '10.0.0.0/16', maxAzs: 2 },
  ecs: { instanceType: 't3.micro', minCapacity: 1, maxCapacity: 3 },
  rds: { instanceType: 'db.t3.micro', multiAz: false }
};

// lib/config/prod.ts
export const prodConfig = {
  vpc: { cidr: '10.0.0.0/16', maxAzs: 3 },
  ecs: { instanceType: 'm5.large', minCapacity: 3, maxCapacity: 10 },
  rds: { instanceType: 'db.r5.large', multiAz: true }
};
```

## Best Practices

1. Separate infrastructure and application code
2. Use separate AWS accounts for dev/staging/prod
3. Implement least privilege IAM policies
4. Store state remotely (S3 for Terraform)
5. Use parameter files for environment-specific values
6. Include tests for infrastructure code
7. Document architecture and dependencies
8. Use CI/CD for automated deployments
