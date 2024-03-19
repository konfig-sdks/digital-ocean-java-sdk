

# NetworkV4


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**ipAddress** | **String** | The IP address of the IPv4 network interface. |  [optional] |
|**netmask** | **String** | The netmask of the IPv4 network interface. |  [optional] |
|**gateway** | **String** | The gateway of the specified IPv4 network interface.  For private interfaces, a gateway is not provided. This is denoted by returning &#x60;nil&#x60; as its value.  |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The type of the IPv4 network interface. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| PUBLIC | &quot;public&quot; |
| PRIVATE | &quot;private&quot; |



