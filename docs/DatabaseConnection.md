

# DatabaseConnection


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**uri** | **String** | A connection string in the format accepted by the &#x60;psql&#x60; command. This is provided as a convenience and should be able to be constructed by the other attributes. |  [optional] [readonly] |
|**database** | **String** | The name of the default database. |  [optional] [readonly] |
|**host** | **String** | The FQDN pointing to the database cluster&#39;s current primary node. |  [optional] [readonly] |
|**port** | **Integer** | The port on which the database cluster is listening. |  [optional] [readonly] |
|**user** | **String** | The default user for the database. |  [optional] [readonly] |
|**password** | **String** | The randomly generated password for the default user. |  [optional] [readonly] |
|**ssl** | **Boolean** | A boolean value indicating if the connection should be made over SSL. |  [optional] [readonly] |



