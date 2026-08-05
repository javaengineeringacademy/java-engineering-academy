# Azure CLI Cheat Sheet

## Authentication

```bash
az login
az login --service-principal -u APP_ID -p PASSWORD --tenant TENANT_ID
az account set --subscription "SUBSCRIPTION_ID"
az account show
az account list --output table
```

## Resource Groups

```bash
az group create --name myRG --location eastus
az group delete --name myRG --yes --no-wait
az group list --output table
```

## Virtual Machines

```bash
az vm create -g myRG -n myVM --image Ubuntu2204 --admin-username azureuser --generate-ssh-keys
az vm start -g myRG -n myVM
az vm stop -g myRG -n myVM
az vm deallocate -g myRG -n myVM
az vm delete -g myRG -n myVM --yes
az vm list -g myRG --output table
az vm list-sizes --location eastus --output table
az vm resize -g myRG -n myVM --size Standard_D4s_v3
az vm extension set -g myRG --vm-name myVM -n VMAccessForLinux --publisher Microsoft.OSTCExtensions --protected-settings '{"username":"azureuser","ssh_key":"..."}'
```

## Storage

```bash
az storage account create -g myRG -n mystorageaccount -l eastus --sku Standard_LRS
az storage account list -g myRG --output table
az storage container create --account-name mystorageaccount --name mycontainer
az storage blob upload --account-name mystorageaccount --container-name mycontainer --file ./local.txt --name remote.txt
az storage blob list --account-name mystorageaccount --container-name mycontainer --output table
az storage account delete -g myRG -n mystorageaccount --yes
```

## Networking

```bash
az network vnet create -g myRG -n myVNet --address-prefix 10.0.0.0/16 --subnet-name mySubnet
az network nsg create -g myRG -n myNSG
az network nsg rule create -g myRG --nsg-name myNSG -n AllowSSH --priority 1000 --destination-port-ranges 22 --access Allow --protocol Tcp --direction Inbound
az network public-ip create -g myRG -n myPIP --sku Standard
az network lb create -g myRG -n myLB --frontend-ip-name myFrontend --backend-pool-name myBackend --sku Standard
```

## AKS

```bash
az aks create -g myRG -n myAKS --node-count 3 --enable-addons monitoring --generate-ssh-keys
az aks get-credentials -g myRG -n myAKS
az aks show -g myRG -n myAKS --output table
az aks nodepool add -g myRG --cluster-name myAKS -n mypool2 --node-count 2
az aks delete -g myRG -n myAKS --yes
```

## Cosmos DB

```bash
az cosmosdb create -g myRG -n mycosmosaccount --kind GlobalDocumentDB --location regionName=eastus
az cosmosdb database create -g myRG -n mycosmosaccount -d mydb
az cosmosdb collection create -g myRG -n mycosmosaccount -d mydb -c mycollection
az cosmosdb list -g myRG --output table
```

## Azure Functions

```bash
az functionapp create -g myRG -n myfunc --storage-account mystorage --consumption-plan-location eastus --runtime dotnet
az functionapp function list -g myRG -n myfunc --output table
az functionapp function show -g myRG -n myfunc --name myfunction
az functionapp delete -g myRG -n myfunc
```

## App Service

```bash
az webapp create -g myRG -n mywebapp --plan myplan --runtime "DOTNET:8.0"
az webapp deployment slot create -g myRG -n mywebapp --name staging
az webapp deployment slot swap -g myRG -n mywebapp --slot staging
az webapp config appsettings set -g myRG -n mywebapp --settings "KEY=VALUE"
az webapp list -g myRG --output table
```

## Azure SQL

```bash
az sql server create -g myRG -n myserver --location eastus --admin-user adminuser --admin-password "P@ssword123"
az sql db create -g myRG --server myserver -n mydb --service-tier Basic
az sql db list -g myRG --server myserver --output table
```

## Monitoring

```bash
az monitor diagnostic-settings create --resource /subscriptions/SUB_ID/resourceGroups/myRG/providers/Microsoft.Compute/virtualMachines/myVM --name myDiag --workspace /subscriptions/SUB_ID/resourceGroups/myRG/providers/Microsoft.OperationalInsights/workspaces/myWorkspace --logs '[{"category":"Audit","enabled":true}]'
az monitor metrics list --resource /subscriptions/SUB_ID/resourceGroups/myRG/providers/Microsoft.Compute/virtualMachines/myVM --metric "Percentage CPU" --output table
```

## Key Vault

```bash
az keyvault create -g myRG -n myvault --enable-rbac-authorization false
az keyvault secret set --vault-name myvault -n mysecret -v "P@ssword123"
az keyvault secret show --vault-name myvault -n mysecret
az keyvault secret list --vault-name myvault --output table
```

## Tags

```bash
az tag create --name Environment
az tag update --resource-id /subscriptions/SUB_ID/resourceGroups/myRG --operation merge --tags Environment=Production Owner=TeamA
az tag list --resource-id /subscriptions/SUB_ID/resourceGroups/myRG
```
