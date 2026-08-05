# Kubernetes Corner Cases

## Pod Eviction

Pods are evicted when node resources are exhausted. The kubelet evicts pods based on `PriorityClass` and resource requests. Lowest priority pods are evicted first. Critical system pods are not evicted.

Eviction is not graceful by default. Use `preStop` hooks and `terminationGracePeriodSeconds` for cleanup. Check `kubectl describe node` to see eviction reasons.

## Image Pull Backoff

`ImagePullBackOff` means the kubelet cannot pull the image. Causes include wrong image name, missing registry credentials, or network issues. The backoff increases with each failed attempt.

Check `kubectl describe pod` for the exact error. Verify image name and tag. Ensure `imagePullSecrets` are configured for private registries.

## OOMKilled

`OOMKilled` occurs when a container exceeds its memory limit. The kernel kills the process. Check `kubectl describe pod` for `Last State` reason.

Increase `resources.limits.memory` or optimize the application. Monitor memory usage with `kubectl top pod`.

## CrashLoopBackOff

`CrashLoopBackOff` means the container is starting and crashing repeatedly. The kubelet applies exponential backoff between restarts. Check logs with `kubectl logs <pod>`.

Common causes: missing environment variables, configuration errors, dependency failures, or application bugs.

## Pod Stuck in Pending

`Pending` status means the pod is not scheduled. Check `kubectl describe pod` for scheduling failures. Common causes: insufficient resources, node selectors that match no nodes, or taints that prevent scheduling.

Use `kubectl get events` to see scheduler decisions. Check if nodes are `Ready` with `kubectl get nodes`.

## Service External Traffic Policy

`externalTrafficPolicy: Local` preserves client source IP but may cause uneven load distribution. Some nodes receive more traffic than others. Use `Cluster` for even distribution but lose source IP.

For NodePort services, `Local` ensures traffic goes to a local pod, avoiding an extra hop.

## ConfigMap and Secret Updates

ConfigMap and Secret changes are not automatically reflected in running pods. Pods must be restarted or use `subPath` with `auto` update policy.

Use `kubectl rollout restart deployment` to apply changes. Or use environment variables from ConfigMaps, which update on pod restart.

## Resource Requests and Limits

Requests are used for scheduling; limits are enforced at runtime. If a pod exceeds its memory limit, it is OOMKilled. If it exceeds its CPU limit, it is throttled.

Setting requests equal to limits guarantees resources but reduces cluster utilization. Set requests based on actual usage and limits based on peak usage.

## Node Affinity and Taints

Node selectors are a simple form of affinity. Use `nodeAffinity` for more complex rules. Taints repel pods unless they have matching tolerations.

System-critical pods have tolerations for node taints. Application pods do not by default, preventing them from running on system nodes.

## Pod Disruption Budgets

A PodDisruptionBudget limits the number of pods that can be voluntarily disrupted. `minAvailable` or `maxUnavailable` controls the policy. PDBs only apply to voluntary disruptions, not involuntary evictions.

Check PDB status with `kubectl get pdb`. Ensure PDBs do not prevent node maintenance.

## Network Policies

NetworkPolicies control traffic between pods. By default, all pods can communicate. A NetworkPolicy with an empty `podSelector` affects all pods in the namespace.

Use `ingress` and `egress` rules to restrict traffic. Without explicit rules, all traffic is allowed. With rules, only matching traffic is permitted.

## RBAC and Service Accounts

Service accounts have default tokens mounted as Secrets. These tokens grant access to the API server. Rotate tokens periodically.

RBAC roles are additive. There is no deny rule. If a role grants access, it cannot be revoked by another role. Review RBAC policies regularly.

## DaemonSet Scheduling

DaemonSets ensure a pod runs on every node (or a subset). If a node is not `Ready`, the pod is not scheduled. DaemonSet pods are not affected by default scheduler.

Use `tolerations` to run DaemonSet pods on tainted nodes. Use `nodeSelector` or `affinity` to limit DaemonSet to specific nodes.
