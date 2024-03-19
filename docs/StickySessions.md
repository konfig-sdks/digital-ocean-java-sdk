

# StickySessions

An object specifying sticky sessions settings for the load balancer.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**type** | [**TypeEnum**](#TypeEnum) | An attribute indicating how and if requests from a client will be persistently served by the same backend Droplet. The possible values are &#x60;cookies&#x60; or &#x60;none&#x60;. |  [optional] |
|**cookieName** | **String** | The name of the cookie sent to the client. This attribute is only returned when using &#x60;cookies&#x60; for the sticky sessions type. |  [optional] |
|**cookieTtlSeconds** | **Integer** | The number of seconds until the cookie set by the load balancer expires. This attribute is only returned when using &#x60;cookies&#x60; for the sticky sessions type. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| COOKIES | &quot;cookies&quot; |
| NONE | &quot;none&quot; |



