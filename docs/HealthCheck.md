

# HealthCheck

An object specifying health check settings for the load balancer.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**protocol** | [**ProtocolEnum**](#ProtocolEnum) | The protocol used for health checks sent to the backend Droplets. The possible values are &#x60;http&#x60;, &#x60;https&#x60;, or &#x60;tcp&#x60;. |  [optional] |
|**port** | **Integer** | An integer representing the port on the backend Droplets on which the health check will attempt a connection. |  [optional] |
|**path** | **String** | The path on the backend Droplets to which the load balancer instance will send a request. |  [optional] |
|**checkIntervalSeconds** | **Integer** | The number of seconds between between two consecutive health checks. |  [optional] |
|**responseTimeoutSeconds** | **Integer** | The number of seconds the load balancer instance will wait for a response until marking a health check as failed. |  [optional] |
|**unhealthyThreshold** | **Integer** | The number of times a health check must fail for a backend Droplet to be marked \&quot;unhealthy\&quot; and be removed from the pool. |  [optional] |
|**healthyThreshold** | **Integer** | The number of times a health check must pass for a backend Droplet to be marked \&quot;healthy\&quot; and be re-added to the pool. |  [optional] |



## Enum: ProtocolEnum

| Name | Value |
|---- | -----|
| HTTP | &quot;http&quot; |
| HTTPS | &quot;https&quot; |
| TCP | &quot;tcp&quot; |



