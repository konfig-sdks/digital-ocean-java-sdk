

# FirewallRule


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**uuid** | **String** | A unique ID for the firewall rule itself. |  [optional] |
|**clusterUuid** | **String** | A unique ID for the database cluster to which the rule is applied. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The type of resource that the firewall rule allows to access the database cluster. |  |
|**value** | **String** | The ID of the specific resource, the name of a tag applied to a group of resources, or the IP address that the firewall rule allows to access the database cluster. |  |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the firewall rule was created. |  [optional] [readonly] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| DROPLET | &quot;droplet&quot; |
| K8S | &quot;k8s&quot; |
| IP_ADDR | &quot;ip_addr&quot; |
| TAG | &quot;tag&quot; |
| APP | &quot;app&quot; |



