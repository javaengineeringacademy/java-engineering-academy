# Kubernetes Cheat Sheet

## Cluster Information

```bash
kubectl cluster-info
kubectl cluster-info dump
kubectl api-resources
kubectl api-versions
kubectl config view
kubectl config get-contexts
kubectl config use-context <context>
```

## Node Operations

```bash
kubectl get nodes
kubectl describe node <node>
kubectl top nodes
kubectl cordon <node>
kubectl uncordon <node>
kubectl drain <node> --ignore-daemonsets --delete-emptydir-data
kubectl label nodes <node> key=value
kubectl taint nodes <node> key=value:NoSchedule
```

## Pod Operations

```bash
kubectl get pods
kubectl get pods -n <namespace>
kubectl get pods -o wide
kubectl get pods --all-namespaces
kubectl describe pod <pod>
kubectl logs <pod>
kubectl logs -f <pod>
kubectl logs <pod> --previous
kubectl exec -it <pod> -- /bin/bash
kubectl exec <pod> -- <command>
kubectl port-forward <pod> 8080:80
kubectl cp <pod>:/path/file ./local-file
kubectl delete pod <pod>
kubectl get pod <pod> -o yaml
```

## Deployment Operations

```bash
kubectl get deployments
kubectl describe deployment <deployment>
kubectl create deployment <name> --image=<image>
kubectl scale deployment <deployment> --replicas=<n>
kubectl set image deployment/<deployment> <container>=<image>
kubectl rollout status deployment/<deployment>
kubectl rollout history deployment/<deployment>
kubectl rollout undo deployment/<deployment>
kubectl rollout undo deployment/<deployment> --to-revision=<n>
kubectl rollout restart deployment/<deployment>
```

## Service Operations

```bash
kubectl get services
kubectl describe service <service>
kubectl expose deployment <deployment> --port=<port> --type=<type>
kubectl delete service <service>
kubectl get endpoints <service>
```

## Ingress Operations

```bash
kubectl get ingress
kubectl describe ingress <ingress>
kubectl create ingress <name> --rule=<host>=<service>:<port>
```

## ConfigMap Operations

```bash
kubectl get configmaps
kubectl describe configmap <configmap>
kubectl create configmap <name> --from-literal=key=value
kubectl create configmap <name> --from-file=<file>
kubectl create configmap <name> --from-env-file=<env-file>
kubectl delete configmap <configmap>
kubectl get configmap <configmap> -o yaml
```

## Secret Operations

```bash
kubectl get secrets
kubectl describe secret <secret>
kubectl create secret generic <name> --from-literal=key=value
kubectl create secret generic <name> --from-file=<file>
kubectl create secret tls <name> --cert=<cert-file> --key=<key-file>
kubectl delete secret <secret>
kubectl get secret <secret> -o jsonpath='{.data.key}' | base64 -d
```

## Namespace Operations

```bash
kubectl get namespaces
kubectl create namespace <namespace>
kubectl delete namespace <namespace>
kubectl config set-context --current --namespace=<namespace>
kubectl get all -n <namespace>
```

## RBAC Operations

```bash
kubectl get roles
kubectl get rolebindings
kubectl get clusterroles
kubectl get clusterrolebindings
kubectl create role <role> --verb=get,list,watch --resource=pods
kubectl create rolebinding <binding> --role=<role> --serviceaccount=<namespace>:<sa>
kubectl auth can-i <verb> <resource>
```

## Resource Management

```bash
kubectl top pods
kubectl top pods --sort-by=memory
kubectl top pods --sort-by=cpu
kubectl top nodes
kubectl get resourcequotas
kubectl describe resourcequota <quota>
```

## Storage Operations

```bash
kubectl get persistentvolumeclaims
kubectl get persistentvolumes
kubectl describe pvc <pvc>
kubectl create -f pvc.yaml
kubectl delete pvc <pvc>
kubectl get storageclass
```

## Network Policies

```bash
kubectl get networkpolicies
kubectl describe networkpolicy <policy>
kubectl delete networkpolicy <policy>
```

## Events and Debugging

```bash
kubectl get events
kubectl get events --sort-by='.lastTimestamp'
kubectl get events --field-selector type=Warning
kubectl describe <resource> <name>
kubectl diff -f <manifest>
kubectl debug -it <pod> --image=busybox --target=<container>
kubectl debug node/<node> -it --image=busybox
```

## Copy and Transfer

```bash
kubectl cp <namespace>/<pod>:/path/file ./local-file
kubectl cp ./local-file <namespace>/<pod>:/path/file
```

## Port Forwarding

```bash
kubectl port-forward <pod> 8080:80
kubectl port-forward svc/<service> 8080:80
kubectl port-forward <pod> 8080:80 -n <namespace>
```

## Apply and Delete

```bash
kubectl apply -f <file.yaml>
kubectl apply -f <directory>
kubectl apply -k <kustomize-directory>
kubectl delete -f <file.yaml>
kubectl delete -f <directory>
kubectl delete <resource> <name>
kubectl delete <resource> --all
```

## Output Formats

```bash
kubectl get pods -o wide
kubectl get pods -o yaml
kubectl get pods -o json
kubectl get pods -o jsonpath='{.items[*].metadata.name}'
kubectl get pods -o custom-columns=NAME:.metadata.name,STATUS:.status.phase
kubectl get pods -o table
```

## Watch and Follow

```bash
kubectl get pods -w
kubectl logs -f <pod>
kubectl get events -w
kubectl get deployments -w
```

## Dry Run and Validation

```bash
kubectl apply -f <file> --dry-run=client
kubectl apply -f <file> --dry-run=server
kubectl validate -f <file>
kubectl diff -f <file>
```

## Scale and Autoscale

```bash
kubectl scale deployment <deployment> --replicas=<n>
kubectl autoscale deployment <deployment> --min=<n> --max=<n> --cpu-percent=<n>
kubectl get hpa
kubectl describe hpa <hpa>
```

## Patch and Update

```bash
kubectl patch deployment <deployment> -p '{"spec":{"replicas":3}}'
kubectl patch service <service> -p '{"spec":{"type":"LoadBalancer"}}'
kubectl label pod <pod> key=value
kubectl annotate pod <pod> key=value
```
