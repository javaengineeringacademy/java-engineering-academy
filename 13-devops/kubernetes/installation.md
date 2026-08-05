# Kubernetes Installation

## Local Development

### minikube

A tool that runs a single-node Kubernetes cluster locally. Best for development and testing on macOS, Linux, and Windows.

```bash
# Install
brew install minikube

# Start cluster
minikube start --driver=docker

# Enable addons
minikube addons enable metrics-server
minikube addons enable ingress

# Access dashboard
minikube dashboard

# Stop cluster
minikube stop

# Delete cluster
minikube delete
```

### kind (Kubernetes IN Docker)

Runs Kubernetes using Docker containers as nodes. Excellent for CI/CD pipelines and testing multi-node clusters locally.

```bash
# Install
brew install kind

# Create cluster
kind create cluster --name my-cluster

# Create multi-node cluster
cat <<EOF | kind create cluster --config=-
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
- role: worker
- role: worker
EOF

# Load local image into cluster
kind load docker-image my-app:latest

# Delete cluster
kind delete cluster --name my-cluster
```

### k3s

Lightweight Kubernetes distribution designed for resource-constrained environments. Single binary, built-in SQLite (or external DB), perfect for IoT and edge computing.

```bash
# Install
curl -sfL https://get.k3s.io | sh -

# Check installation
kubectl get nodes

# Uninstall
/usr/local/bin/k3s-uninstall.sh
```

## Managed Kubernetes Services

### Amazon EKS (Elastic Kubernetes Service)

AWS managed Kubernetes control plane. You manage worker nodes; AWS manages the control plane. Integrates with IAM, VPC, and other AWS services.

```bash
# Install eksctl
brew tap weaveworks/tap
brew install weaveworks/tap/eksctl

# Create cluster
eksctl create cluster \
  --name my-cluster \
  --region us-west-2 \
  --nodes 3 \
  --node-type t3.medium

# Update kubeconfig
aws eks update-kubeconfig --name my-cluster --region us-west-2

# Delete cluster
eksctl delete cluster --name my-cluster --region us-west-2
```

### Google GKE (Google Kubernetes Engine)

Google Cloud managed Kubernetes with autopilot and standard modes. Autopilot manages node pools automatically; standard gives full control.

```bash
# Install gcloud CLI
brew install --cask google-cloud-sdk

# Create cluster
gcloud container clusters create my-cluster \
  --zone us-central1-a \
  --num-nodes 3 \
  --machine-type e2-medium

# Get credentials
gcloud container clusters get-credentials my-cluster --zone us-central1-a

# Delete cluster
gcloud container clusters delete my-cluster --zone us-central1-a
```

### Azure AKS (Azure Kubernetes Service)

Azure managed Kubernetes with Azure AD integration, virtual nodes, and Azure Policy. Supports both Linux and Windows nodes.

```bash
# Install Azure CLI
brew install azure-cli

# Create cluster
az aks create \
  --resource-group myResourceGroup \
  --name myCluster \
  --node-count 3 \
  --node-vm-size Standard_B2s

# Get credentials
az aks get-credentials --resource-group myResourceGroup --name myCluster

# Delete cluster
az aks delete --resource-group myResourceGroup --name myCluster
```

## Production Setup

### kubeadm

Official Kubernetes tool for bootstrapping clusters. Requires manual node setup and configuration. Full control over cluster components.

```bash
# On control plane node
kubeadm init --pod-network-cidr=10.244.0.0/16

# Set up kubectl
mkdir -p $HOME/.kube
sudo cp /etc/kubernetes/admin.conf $HOME/.kube/config

# Install CNI plugin (Calico)
kubectl apply -f https://docs.projectcalico.org/manifests/calico.yaml

# Join worker nodes
kubeadm join <control-plane-ip>:6443 --token <token> --discovery-token-ca-cert-hash <hash>
```

### kOps (Kubernetes Operations)

Production-grade Kubernetes provisioning on AWS, GCP, and other clouds. Manages the full lifecycle of clusters including upgrades and scaling.

```bash
# Install kOps
brew install kops

# Create cluster
kops create cluster \
  --name=my-cluster.k8s.local \
  --cloud=aws \
  --zones=us-west-2a,us-west-2b,us-west-2c \
  --node-count=3

# Build cluster
kops update cluster --name my-cluster.k8s.local --yes --admin

# Validate cluster
kops validate cluster --name my-cluster.k8s.local

# Delete cluster
kops delete cluster --name my-cluster.k8s.local --yes
```

## Post-Installation

### Install Metrics Server

```bash
kubectl apply -f https://github.com/kubernetes-sigs/metrics-server/releases/latest/download/components.yaml
```

### Install Helm

```bash
brew install helm

# Verify installation
helm version
```

### Install kubectl

```bash
brew install kubectl

# Verify installation
kubectl version --client
```

### Verify Cluster

```bash
kubectl cluster-info
kubectl get nodes
kubectl get pods -A
kubectl run test --image=nginx --rm -it -- /bin/bash
```
