

# KubernetesNodePool


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** |  |  [optional] |
|**size** | **String** |  |  |
|**count** | **Double** |  |  |
|**name** | **String** |  |  |
|**autoScale** | **Boolean** |  |  [optional] |
|**minNodes** | **Double** |  |  [optional] |
|**maxNodes** | **Double** |  |  [optional] |
|**id** | **UUID** | A unique ID that can be used to identify and reference a specific node pool. |  [optional] [readonly] |
|**labels** | **Object** | An object of key/value mappings specifying labels to apply to all nodes in a pool. Labels will automatically be applied to all existing nodes and any subsequent nodes added to the pool. Note that when a label is removed, it is not deleted from the nodes in the pool. |  [optional] |
|**taints** | [**List&lt;KubernetesNodePoolTaint&gt;**](KubernetesNodePoolTaint.md) | An array of taints to apply to all nodes in a pool. Taints will automatically be applied to all existing nodes and any subsequent nodes added to the pool. When a taint is removed, it is deleted from all nodes in the pool. |  [optional] |
|**nodes** | [**List&lt;Node&gt;**](Node.md) | An object specifying the details of a specific worker node in a node pool. |  [optional] [readonly] |



