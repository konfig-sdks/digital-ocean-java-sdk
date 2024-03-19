

# ConnectionPool


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | A unique name for the connection pool. Must be between 3 and 60 characters. |  |
|**mode** | **String** | The PGBouncer transaction mode for the connection pool. The allowed values are session, transaction, and statement. |  |
|**size** | **Integer** | The desired size of the PGBouncer connection pool. The maximum allowed size is determined by the size of the cluster&#39;s primary node. 25 backend server connections are allowed for every 1GB of RAM. Three are reserved for maintenance. For example, a primary node with 1 GB of RAM allows for a maximum of 22 backend server connections while one with 4 GB would allow for 97. Note that these are shared across all connection pools in a cluster. |  |
|**db** | **String** | The database for use with the connection pool. |  |
|**user** | **String** | The name of the user for use with the connection pool. When excluded, all sessions connect to the database as the inbound user. |  [optional] |
|**connection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |
|**privateConnection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |
|**standbyConnection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |
|**standbyPrivateConnection** | [**DatabaseClusterConnection**](DatabaseClusterConnection.md) |  |  [optional] |



