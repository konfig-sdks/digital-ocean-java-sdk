

# DatabaseClusterResize


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**size** | **String** | A slug identifier representing desired the size of the nodes in the database cluster. |  |
|**numNodes** | **Integer** | The number of nodes in the database cluster. Valid values are are 1-3. In addition to the primary node, up to two standby nodes may be added for highly available configurations. |  |
|**storageSizeMib** | **Integer** | Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the &#39;size&#39; and any previously added additional storage. |  [optional] |



