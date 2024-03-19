

# ClusterStatus

An object containing a `state` attribute whose value is set to a string indicating the current status of the cluster.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**state** | [**StateEnum**](#StateEnum) | A string indicating the current status of the cluster. |  [optional] |
|**message** | **String** | An optional message providing additional information about the current cluster state. |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| RUNNING | &quot;running&quot; |
| PROVISIONING | &quot;provisioning&quot; |
| DEGRADED | &quot;degraded&quot; |
| ERROR | &quot;error&quot; |
| DELETED | &quot;deleted&quot; |
| UPGRADING | &quot;upgrading&quot; |
| DELETING | &quot;deleting&quot; |



