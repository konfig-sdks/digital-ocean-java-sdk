

# DatabasesAddUserRequest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name of a database user. |  |
|**role** | [**RoleEnum**](#RoleEnum) | A string representing the database user&#39;s role. The value will be either \&quot;primary\&quot; or \&quot;normal\&quot;.  |  [optional] [readonly] |
|**password** | **String** | A randomly generated password for the database user. |  [optional] [readonly] |
|**accessCert** | **String** | Access certificate for TLS client authentication. (Kafka only) |  [optional] [readonly] |
|**accessKey** | **String** | Access key for TLS client authentication. (Kafka only) |  [optional] [readonly] |
|**mysqlSettings** | [**MysqlSettings**](MysqlSettings.md) |  |  [optional] |
|**settings** | [**UserSettings**](UserSettings.md) |  |  [optional] |
|**readonly** | **Boolean** | For MongoDB clusters, set to &#x60;true&#x60; to create a read-only user. This option is not currently supported for other database engines.               |  [optional] |



## Enum: RoleEnum

| Name | Value |
|---- | -----|
| PRIMARY | &quot;primary&quot; |
| NORMAL | &quot;normal&quot; |



