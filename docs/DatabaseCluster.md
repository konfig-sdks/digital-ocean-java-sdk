

# DatabaseCluster


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | An array of tags that have been applied to the database cluster. |  [optional] |
|**version** | **String** | A string representing the version of the database engine in use for the cluster. |  [optional] |
|**id** | **UUID** | A unique ID that can be used to identify and reference a database cluster. |  [optional] [readonly] |
|**name** | **String** | A unique, human-readable name referring to a database cluster. |  |
|**engine** | [**EngineEnum**](#EngineEnum) | A slug representing the database engine used for the cluster. The possible values are: \&quot;pg\&quot; for PostgreSQL, \&quot;mysql\&quot; for MySQL, \&quot;redis\&quot; for Redis, \&quot;mongodb\&quot; for MongoDB, and \&quot;kafka\&quot; for Kafka. |  |
|**semanticVersion** | **String** | A string representing the semantic version of the database engine in use for the cluster. |  [optional] [readonly] |
|**numNodes** | **Integer** | The number of nodes in the database cluster. |  |
|**size** | **String** | The slug identifier representing the size of the nodes in the database cluster. |  |
|**region** | **String** | The slug identifier for the region where the database cluster is located. |  |
|**status** | [**StatusEnum**](#StatusEnum) | A string representing the current status of the database cluster. |  [optional] [readonly] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the database cluster was created. |  [optional] [readonly] |
|**privateNetworkUuid** | **String** | A string specifying the UUID of the VPC to which the database cluster will be assigned. If excluded, the cluster when creating a new database cluster, it will be assigned to your account&#39;s default VPC for the region. |  [optional] |
|**dbNames** | **List&lt;String&gt;** | An array of strings containing the names of databases created in the database cluster. |  [optional] [readonly] |
|**connection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |
|**privateConnection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |
|**standbyConnection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |
|**standbyPrivateConnection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |
|**users** | [**List&lt;DatabaseUser&gt;**](DatabaseUser.md) |  |  [optional] [readonly] |
|**maintenanceWindow** | [**DatabaseClusterMaintenanceWindow**](DatabaseClusterMaintenanceWindow.md) |  |  [optional] |
|**projectId** | **UUID** | The ID of the project that the database cluster is assigned to. If excluded when creating a new database cluster, it will be assigned to your default project. |  [optional] |
|**rules** | [**List&lt;FirewallRule&gt;**](FirewallRule.md) |  |  [optional] |
|**versionEndOfLife** | **String** | A timestamp referring to the date when the particular version will no longer be supported. If null, the version does not have an end of life timeline. |  [optional] [readonly] |
|**versionEndOfAvailability** | **String** | A timestamp referring to the date when the particular version will no longer be available for creating new clusters. If null, the version does not have an end of availability timeline. |  [optional] [readonly] |
|**storageSizeMib** | **Integer** | Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the &#39;size&#39; and any previously added additional storage. |  [optional] |
|**metricsEndpoints** | [**List&lt;DatabaseServiceEndpoint&gt;**](DatabaseServiceEndpoint.md) | Public hostname and port of the cluster&#39;s metrics endpoint(s). Includes one record for the cluster&#39;s primary node and a second entry for the cluster&#39;s standby node(s). |  [optional] |



## Enum: EngineEnum

| Name | Value |
|---- | -----|
| PG | &quot;pg&quot; |
| MYSQL | &quot;mysql&quot; |
| REDIS | &quot;redis&quot; |
| MONGODB | &quot;mongodb&quot; |
| KAFKA | &quot;kafka&quot; |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| CREATING | &quot;creating&quot; |
| ONLINE | &quot;online&quot; |
| RESIZING | &quot;resizing&quot; |
| MIGRATING | &quot;migrating&quot; |
| FORKING | &quot;forking&quot; |



