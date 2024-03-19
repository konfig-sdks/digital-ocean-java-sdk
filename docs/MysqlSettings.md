

# MysqlSettings


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**authPlugin** | [**AuthPluginEnum**](#AuthPluginEnum) | A string specifying the authentication method to be used for connections to the MySQL user account. The valid values are &#x60;mysql_native_password&#x60; or &#x60;caching_sha2_password&#x60;. If excluded when creating a new user, the default for the version of MySQL in use will be used. As of MySQL 8.0, the default is &#x60;caching_sha2_password&#x60;.  |  |



## Enum: AuthPluginEnum

| Name | Value |
|---- | -----|
| MYSQL_NATIVE_PASSWORD | &quot;mysql_native_password&quot; |
| CACHING_SHA2_PASSWORD | &quot;caching_sha2_password&quot; |



