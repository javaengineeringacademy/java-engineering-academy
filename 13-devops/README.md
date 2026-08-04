# Module 13: DevOps

## Overview

DevOps is a set of practices, tools, and cultural philosophies that automate and integrate the processes between software development and IT teams. This module covers CI/CD pipelines, containerization, orchestration, infrastructure as code, configuration management, monitoring, and more.

## Table of Contents

### CI/CD Tools
| Topic | Description |
|-------|-------------|
| [Jenkins](ci-cd/jenkins/README.md) | Pipeline, agents, shared libraries |
| [GitHub Actions](ci-cd/github-actions/README.md) | Workflows, reusable actions |
| [GitLab CI](ci-cd/gitlab-ci/README.md) | Stages, runners, pipelines |
| [CircleCI](ci-cd/circle-ci/README.md) | Orbs, workflows |
| [Azure DevOps](ci-cd/azure-devops/README.md) | Pipelines, boards, repos |
| [Travis CI](ci-cd/travis-ci/README.md) | Build stages, matrix builds |
| [Bamboo](ci-cd/bamboo/README.md) | Plans, deployment projects |
| [TeamCity](ci-cd/teamcity/README.md) | Build configurations |

### Docker
| Topic | Description |
|-------|-------------|
| [Docker Fundamentals](docker/fundamentals/README.md) | Images, containers, Dockerfile |
| [Multi-stage Builds](docker/multi-stage/README.md) | Optimization techniques |
| [Docker Compose](docker/compose/README.md) | Services, networking |
| [Docker Optimization](docker/optimization/README.md) | Layer caching, image size |
| [Docker Security](docker/security/README.md) | Scanning, best practices |
| [Docker Registry](docker/registry/README.md) | Harbor, ECR, registries |

### Kubernetes
| Topic | Description |
|-------|-------------|
| [K8s Fundamentals](kubernetes/fundamentals/README.md) | Architecture, pods, services |
| [Deployments](kubernetes/deployments/README.md) | ReplicaSets, rollout strategies |
| [Services](kubernetes/services/README.md) | ClusterIP, NodePort, LoadBalancer |
| [Ingress](kubernetes/ingress/README.md) | Controllers, routing |
| [ConfigMaps & Secrets](kubernetes/configmaps-secrets/README.md) | Configuration management |
| [RBAC](kubernetes/rbac/README.md) | Roles, bindings, policies |
| [Helm](kubernetes/helm/README.md) | Charts, values, releases |
| [Operators](kubernetes/operators/README.md) | CRDs, custom controllers |
| [Persistence](kubernetes/persistence/README.md) | PV, PVC, StorageClasses |
| [Monitoring](kubernetes/monitoring/README.md) | Prometheus, metrics |
| [Security](kubernetes/security/README.md) | Pod security, network policies |
| [Gateway API](kubernetes/gateway-api/README.md) | Routes, HTTP routing |
| [Networking](kubernetes/networking/README.md) | DNS, CNI, network policies |

### Terraform
| Topic | Description |
|-------|-------------|
| [Terraform Fundamentals](terraform/fundamentals/README.md) | Providers, resources |
| [Modules](terraform/modules/README.md) | Composition, reuse |
| [State Management](terraform/state/README.md) | Backends, locking |
| [Workspaces](terraform/workspaces/README.md) | Environment management |
| [Best Practices](terraform/best-practices/README.md) | File structure, patterns |
| [Terraform Cloud](terraform/cloud/README.md) | Remote state, collaboration |

### Ansible
| Topic | Description |
|-------|-------------|
| [Ansible Fundamentals](ansible/fundamentals/README.md) | Playbooks, inventory |
| [Roles](ansible/roles/README.md) | Galaxy, reuse |
| [Vault](ansible/vault/README.md) | Secrets management |
| [Modules](ansible/modules/README.md) | Custom modules |
| [Best Practices](ansible/best-practices/README.md) | Testing, patterns |

### Puppet
| Topic | Description |
|-------|-------------|
| [Puppet Fundamentals](puppet/fundamentals/README.md) | Manifests, modules |
| [Manifests](puppet/manifests/README.md) | Classes, defined types |
| [Modules](puppet/modules/README.md) | Forge, reuse |
| [Hiera](puppet/hiera/README.md) | Data hierarchy |
| [Best Practices](puppet/best-practices/README.md) | Patterns |

### Chef
| Topic | Description |
|-------|-------------|
| [Chef Fundamentals](chef/fundamentals/README.md) | Recipes, cookbooks |
| [Recipes](chef/recipes/README.md) | Resources, guards |
| [Cookbooks](chef/cookbooks/README.md) | Dependencies, Berkshelf |
| [Best Practices](chef/best-practices/README.md) | Patterns |

### Salt
| Topic | Description |
|-------|-------------|
| [Salt Fundamentals](salt/fundamentals/README.md) | States, pillars |
| [Salt States](salt/states/README.md) | SLS files |
| [Formulas](salt/formulas/README.md) | Reusable formulas |
| [Best Practices](salt/best-practices/README.md) | Patterns |

### Packer
| Topic | Description |
|-------|-------------|
| [Packer Fundamentals](packer/fundamentals/README.md) | Builders, provisioners |
| [Builders](packer/builders/README.md) | AMI, Docker, VMware |
| [Provisioners](packer/provisioners/README.md) | Shell, Ansible, Chef |
| [Templates](packer/templates/README.md) | Variables, functions |
| [Best Practices](packer/best-practices/README.md) | Patterns |

### Vagrant
| Topic | Description |
|-------|-------------|
| [Vagrant Fundamentals](vagrant/fundamentals/README.md) | Boxes, synced folders |
| [Providers](vagrant/providers/README.md) | VirtualBox, VMware |
| [Provisioning](vagrant/provisioning/README.md) | Shell, Ansible |
| [Networking](vagrant/networking/README.md) | Port forwarding |
| [Best Practices](vagrant/best-practices/README.md) | Patterns |

### Infrastructure as Code
| Topic | Description |
|-------|-------------|
| [IaC Tools](infrastructure-as-code/tools/README.md) | Tool comparison |
| [IaC Patterns](infrastructure-as-code/patterns/README.md) | Design patterns |
| [IaC Testing](infrastructure-as-code/testing/README.md) | Terratest, validation |
| [Best Practices](infrastructure-as-code/best-practices/README.md) | Patterns |

### Configuration Management
| Topic | Description |
|-------|-------------|
| [CM Tools](configuration-management/tools/README.md) | Tool comparison |
| [CM Patterns](configuration-management/patterns/README.md) | Design patterns |
| [Drift Detection](configuration-management/drift/README.md) | Drift detection |
| [Best Practices](configuration-management/best-practices/README.md) | Patterns |

### Monitoring
| Topic | Description |
|-------|-------------|
| [Prometheus](monitoring/prometheus/README.md) | Setup, queries |
| [Grafana](monitoring/grafana/README.md) | Dashboards |
| [Datadog](monitoring/datadog/README.md) | APM |
| [New Relic](monitoring/new-relic/README.md) | Observability |
| [Nagios](monitoring/nagios/README.md) | Monitoring |
| [Elastic Stack](monitoring/elastic-stack/README.md) | ELK/EFK |
| [Splunk](monitoring/splunk/README.md) | Logging |
| [Graylog](monitoring/graylog/README.md) | Centralization |
| [Best Practices](monitoring/best-practices/README.md) | Patterns |

## Key Concepts

### DevOps Lifecycle
```
Plan → Code → Build → Test → Release → Deploy → Operate → Monitor
  ↑                                                              |
  └──────────────────────────────────────────────────────────────┘
```

### Core Principles
1. **Automation** - Automate everything from builds to deployments
2. **Continuous Integration** - Merge code changes frequently
3. **Continuous Delivery** - Always be ready to deploy to production
4. **Infrastructure as Code** - Manage infrastructure through code
5. **Monitoring & Observability** - Track system health and performance
6. **Shift Left** - Move testing and security earlier in the pipeline

### Tool Categories
- **CI/CD**: Jenkins, GitHub Actions, GitLab CI, CircleCI
- **Containers**: Docker, Podman, containerd
- **Orchestration**: Kubernetes, Docker Swarm, Nomad
- **IaC**: Terraform, CloudFormation, Pulumi
- **Config Management**: Ansible, Puppet, Chef, Salt
- **Monitoring**: Prometheus, Grafana, Datadog, ELK Stack

## Learning Path

1. Start with CI/CD fundamentals (Jenkins or GitHub Actions)
2. Learn Docker containerization
3. Master Kubernetes orchestration
4. Explore Infrastructure as Code (Terraform)
5. Study Configuration Management (Ansible)
6. Implement Monitoring and Observability
7. Integrate security throughout (DevSecOps)
