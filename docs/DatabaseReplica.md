

# DatabaseReplica


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | A flat array of tag names as strings to apply to the read-only replica after it is created. Tag names can either be existing or new tags. |  [optional] |
|**id** | **UUID** | A unique ID that can be used to identify and reference a database replica. |  [optional] [readonly] |
|**name** | **String** | The name to give the read-only replicating |  |
|**region** | **String** | A slug identifier for the region where the read-only replica will be located. If excluded, the replica will be placed in the same region as the cluster. |  [optional] |
|**size** | **String** | A slug identifier representing the size of the node for the read-only replica. The size of the replica must be at least as large as the node size for the database cluster from which it is replicating. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | A string representing the current status of the database cluster. |  [optional] [readonly] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the database cluster was created. |  [optional] [readonly] |
|**privateNetworkUuid** | **String** | A string specifying the UUID of the VPC to which the read-only replica will be assigned. If excluded, the replica will be assigned to your account&#39;s default VPC for the region. |  [optional] |
|**connection** | [**DatabaseReplicaConnection**](DatabaseReplicaConnection.md) |  |  [optional] |
|**privateConnection** | [**DatabaseReplicaConnection**](DatabaseReplicaConnection.md) |  |  [optional] |
|**storageSizeMib** | **Integer** | Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the &#39;size&#39; and any previously added additional storage. |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| CREATING | &quot;creating&quot; |
| ONLINE | &quot;online&quot; |
| RESIZING | &quot;resizing&quot; |
| MIGRATING | &quot;migrating&quot; |
| FORKING | &quot;forking&quot; |



