

# AssociatedKubernetesResources

An object containing the IDs of resources associated with a Kubernetes cluster.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**loadBalancers** | [**List&lt;AssociatedKubernetesResource&gt;**](AssociatedKubernetesResource.md) | A list of names and IDs for associated load balancers that can be destroyed along with the cluster. |  [optional] |
|**volumes** | [**List&lt;AssociatedKubernetesResource&gt;**](AssociatedKubernetesResource.md) | A list of names and IDs for associated volumes that can be destroyed along with the cluster. |  [optional] |
|**volumeSnapshots** | [**List&lt;AssociatedKubernetesResource&gt;**](AssociatedKubernetesResource.md) | A list of names and IDs for associated volume snapshots that can be destroyed along with the cluster. |  [optional] |



