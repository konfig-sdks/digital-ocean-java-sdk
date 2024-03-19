

# AppDatabaseSpec


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**version** | **String** | The version of the database engine |  [optional] |
|**clusterName** | **String** | The name of the underlying DigitalOcean DBaaS cluster. This is required for production databases. For dev databases, if cluster_name is not set, a new cluster will be provisioned. |  [optional] |
|**dbName** | **String** | The name of the MySQL or PostgreSQL database to configure. |  [optional] |
|**dbUser** | **String** | The name of the MySQL or PostgreSQL user to configure. |  [optional] |
|**engine** | [**EngineEnum**](#EngineEnum) | - MYSQL: MySQL - PG: PostgreSQL - REDIS: Redis |  [optional] |
|**name** | **String** | The name. Must be unique across all components within the same app. |  |
|**production** | **Boolean** | Whether this is a production or dev database. |  [optional] |



## Enum: EngineEnum

| Name | Value |
|---- | -----|
| UNSET | &quot;UNSET&quot; |
| MYSQL | &quot;MYSQL&quot; |
| PG | &quot;PG&quot; |
| REDIS | &quot;REDIS&quot; |



