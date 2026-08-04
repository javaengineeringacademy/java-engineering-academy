# Kubernetes Operators

## Overview

Kubernetes Operators are software extensions that use Custom Resource Definitions (CRDs) to manage applications and their components.

## Custom Resource Definitions

### CRD Definition
```yaml
apiVersion: apiextensions.k8s.io/v1
kind: CustomResourceDefinition
metadata:
  name: myapps.example.com
spec:
  group: example.com
  versions:
  - name: v1
    served: true
    storage: true
    schema:
      openAPIV3Schema:
        type: object
        properties:
          spec:
            type: object
            properties:
              replicas:
                type: integer
              image:
                type: string
            required:
            - replicas
            - image
  scope: Namespaced
  names:
    plural: myapps
    singular: myapp
    kind: MyApp
    shortNames:
    - ma
```

### Custom Resource
```yaml
apiVersion: example.com/v1
kind: MyApp
metadata:
  name: my-app
spec:
  replicas: 3
  image: my-app:1.0
```

## Operator Patterns

### Reconciliation Loop
```python
def reconcile(request):
    # Get desired state
    cr = get_custom_resource(request)
    
    # Get current state
    current = get_current_state(cr)
    
    # Compare and act
    if current != cr.spec:
        # Take action to reach desired state
        update_resources(cr)
    
    # Update status
    update_status(cr, current)
```

### Controller Structure
```go
type MyController struct {
    client.Client
    Scheme *runtime.Scheme
}

func (r *MyController) Reconcile(ctx context.Context, req ctrl.Request) (ctrl.Result, error) {
    // Get custom resource
    cr := &examplev1.MyApp{}
    if err := r.Get(ctx, req.NamespacedName, cr); err != nil {
        return ctrl.Result{}, client.IgnoreNotFound(err)
    }
    
    // Implement reconciliation logic
    // ...
    
    return ctrl.Result{}, nil
}

func (r *MyController) SetupWithManager(mgr ctrl.Manager) error {
    return ctrl.NewControllerManagedBy(mgr).
        For(&examplev1.MyApp{}).
        Complete(r)
}
```

## Operator Lifecycle Manager (OLM)

```yaml
apiVersion: operators.coreos.com/v1alpha1
kind: ClusterServiceVersion
metadata:
  name: my-operator.v1.0.0
spec:
  apiservicedefinitions: {}
  customresourcedefinitions:
    owned:
    - kind: MyApp
      name: myapps.example.com
      version: v1
  description: My custom operator
  displayName: My Operator
  install:
    spec:
      deployments:
      - name: my-operator
        spec:
          replicas: 1
          template:
            spec:
              containers:
              - image: my-operator:1.0
                name: operator
    strategy: deployment
```

## Best Practices

1. **Use existing operators** - Leverage community operators
2. **Implement reconciliation** - Ensure desired state
3. **Handle errors gracefully** - Retry on failures
4. **Use finalizers** - Clean up resources properly
5. **Implement health checks** - Monitor operator health
6. **Use RBAC** - Grant minimal permissions
7. **Test operators** - Use framework testing tools
8. **Document operators** - Add README and usage examples
9. **Version operators** - Follow semver
10. **Monitor operators** - Track metrics and logs
