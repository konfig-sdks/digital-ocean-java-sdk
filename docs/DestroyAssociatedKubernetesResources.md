

# DestroyAssociatedKubernetesResources

An object containing the IDs of resources to be destroyed along with their associated with a Kubernetes cluster.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**loadBalancers** | **List&lt;String&gt;** | A list of IDs for associated load balancers to destroy along with the cluster. |  [optional] |
|**volumes** | **List&lt;String&gt;** | A list of IDs for associated volumes to destroy along with the cluster. |  [optional] |
|**volumeSnapshots** | **List&lt;String&gt;** | A list of IDs for associated volume snapshots to destroy along with the cluster. |  [optional] |



