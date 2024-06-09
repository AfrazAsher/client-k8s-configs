
# [k8s-argocd-apps](https://github.tools.sap/sf-k8s/k8s-argocd-apps)

k8s-argocd-apps repo is a collection of all the SF applications and their pre-requisites(namespace, secrets etc.) that need to be deployed via argocd 

# Structure

### app-of-apps
app-of-apps directory is the source of truth for argocd about what application will be deployed. Argocd is configured to scan for applications under the corresponding DC and specific environment. For eg. Any application placed under ***app-of-apps/dc2/preview*** will be deployed by argocd in ***DC2***

### app-prerequisites
app-prerequisites is a kustomization set that deploys all the prerequisites for the app deployment like ***namespace***, ***secrets*** etc. from the app specific overlay in the corresponding DC.
The common resources for an capability are placed inside the `base/<capability>` directory.
The common resources for all apps like ***sfhcmartifactory*** secrets are placed inside `base/common-base`.

# Onboarding New App [tim-timeevent-processing]

To Onboard a new app refer to the below reference process for onboarding a new microservice tim-timeevent-processing.
 - DC : 2
 - Environment : sales
 - Capability : time
 - microservice: tim-timeevent-processing
 
Below are the five simple steps that needs to be followed.

# Step1

Copy the files
  
templates/app-of-apps/application-kustomize.yaml **[TO]** app-of-apps/dc2/sales/time/

templates/app-of-apps/prereq-pillar-environment.yaml **[TO]** app-of-apps/dc2/sales/time/

# Step2

Rename the file as per naming conventions standards
Please refer to the confluence document for naming conventions https://confluence.successfactors.com/display/SSCD/K8s+NameSpace+Naming+Convention

The file names should start with repo name
https://github.tools.sap/sf-k8s/tim-timeevent-processing-dev-config

[REPO-PREFIX]-[ENV]-app.yaml

app-of-apps/dc2/sales/time/tim-timeevent-processing-sales-app.yaml

https://github.tools.sap/sf-k8s/k8s-argocd-apps/blob/master/app-of-apps/dc2/sales/time/tim-timeevent-processing-sales-app.yaml
 
The below file is needed only one for capability/pillar and not for each microservice.

prereq-[PILLAR]-[ENV].yaml

app-of-apps/dc2/sales/time/prereq-tim-preview.yaml

https://github.tools.sap/sf-k8s/k8s-argocd-apps/blob/master/app-of-apps/dc2/sales/time/prereq-tim-sales.yaml

 
# Step3

 Change the below parameters in the file app-of-apps/dc2/sales/time/tim-timeevent-processing-sales-app.yaml
 
 e.g https://github.tools.sap/sf-k8s/k8s-argocd-apps/blob/master/app-of-apps/dc2/sales/time/tim-timeevent-processing-sales-app.yaml
 
 1. REPO-PREFIX  #  tim-timeevent-processing
 2. DC (To which DC the application needs to be depployed to ) # dc2
 3. ENV (The name of the environment) # sales
 4. REPO-NAME (The application deployment manifests repo that needs to be pointed to) # https://github.tools.sap/sf-k8s/time-timeevent-processing-prod-config.git
 5. CAPABILITY (The  capability that belongs to ) # time
 6. NAMESPACE (The namespace where the application will be deployed) # nsps02btim
 
    Please refer to the confluence document for naming conventions
    
    https://confluence.successfactors.com/display/SSCD/K8s+NameSpace+Naming+Convention


# Step4

Copy both kustomization.yaml and namespace.yaml files
  
templates/app-prerequisites/kustomization.yaml **[TO]** app-prerequisites/overlays/dc2/sales/time/kustomization.yaml

templates/app-prerequisites/namespace.yaml **[TO]** app-prerequisites/overlays/dc2/sales/time/namespace.yaml

```yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization
bases:
- ../../../../base/time/
- ../../../../base/common-base
- ../../dc-common-base
resources:
- namespace.yaml
namespace: nsps02btim
```

All the kubernetes resources that are common to time capability can be placed in this folder
- ../../../../base/time/

All the kubernetes resources that are configured in this folder will be applied to all namespaces for all DC's
- ../../../../base/common-base

All the kubernetes resources that are configured in this folder will be applied to all namespaces in that DC
- app-prerequisites/overlays/dc2/dc-common-base


The below file creates the namespace in kubernetes cluster

templates/app-prerequisites/namespace.yaml **[TO]** app-prerequisites/dc2/sales/time/namespace.yaml

```yaml
apiVersion: v1
kind: Namespace
metadata:
 name: nsps02btim
```

# Step5

Change the below parameters in the files  
prerequisites/dc2/sales/time/kustomization.yaml 
prerequisites/dc2/sales/time/namespace.yaml

Please refer to the confluence document for naming conventions https://confluence.successfactors.com/display/SSCD/K8s+NameSpace+Naming+Convention

 1. NAMESPACE # nsps02btim
 2. CAPABILITY# time
