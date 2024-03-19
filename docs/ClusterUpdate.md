

# ClusterUpdate


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | An array of tags applied to the Kubernetes cluster. All clusters are automatically tagged &#x60;k8s&#x60; and &#x60;k8s:$K8S_CLUSTER_ID&#x60;. |  [optional] |
|**name** | **String** | A human-readable name for a Kubernetes cluster. |  |
|**maintenancePolicy** | [**MaintenancePolicy**](MaintenancePolicy.md) |  |  [optional] |
|**autoUpgrade** | **Boolean** | A boolean value indicating whether the cluster will be automatically upgraded to new patch releases during its maintenance window. |  [optional] |
|**surgeUpgrade** | **Boolean** | A boolean value indicating whether surge upgrade is enabled/disabled for the cluster. Surge upgrade makes cluster upgrades fast and reliable by bringing up new nodes before destroying the outdated nodes. |  [optional] |
|**ha** | **Boolean** | A boolean value indicating whether the control plane is run in a highly available configuration in the cluster. Highly available control planes incur less downtime. The property cannot be disabled. |  [optional] |



