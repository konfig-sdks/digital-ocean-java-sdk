

# KubernetesNodePoolBase


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | An array containing the tags applied to the node pool. All node pools are automatically tagged &#x60;k8s&#x60;, &#x60;k8s-worker&#x60;, and &#x60;k8s:$K8S_CLUSTER_ID&#x60;. |  [optional] |
|**id** | **UUID** | A unique ID that can be used to identify and reference a specific node pool. |  [optional] [readonly] |
|**name** | **String** | A human-readable name for the node pool. |  [optional] |
|**count** | **Integer** | The number of Droplet instances in the node pool. |  [optional] |
|**labels** | **Object** | An object of key/value mappings specifying labels to apply to all nodes in a pool. Labels will automatically be applied to all existing nodes and any subsequent nodes added to the pool. Note that when a label is removed, it is not deleted from the nodes in the pool. |  [optional] |
|**taints** | [**List&lt;KubernetesNodePoolTaint&gt;**](KubernetesNodePoolTaint.md) | An array of taints to apply to all nodes in a pool. Taints will automatically be applied to all existing nodes and any subsequent nodes added to the pool. When a taint is removed, it is deleted from all nodes in the pool. |  [optional] |
|**autoScale** | **Boolean** | A boolean value indicating whether auto-scaling is enabled for this node pool. |  [optional] |
|**minNodes** | **Integer** | The minimum number of nodes that this node pool can be auto-scaled to. The value will be &#x60;0&#x60; if &#x60;auto_scale&#x60; is set to &#x60;false&#x60;. |  [optional] |
|**maxNodes** | **Integer** | The maximum number of nodes that this node pool can be auto-scaled to. The value will be &#x60;0&#x60; if &#x60;auto_scale&#x60; is set to &#x60;false&#x60;. |  [optional] |
|**nodes** | [**List&lt;Node&gt;**](Node.md) | An object specifying the details of a specific worker node in a node pool. |  [optional] [readonly] |



