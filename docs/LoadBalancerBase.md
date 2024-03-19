

# LoadBalancerBase


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | A unique ID that can be used to identify and reference a load balancer. |  [optional] [readonly] |
|**name** | **String** | A human-readable name for a load balancer instance. |  [optional] |
|**projectId** | **String** | The ID of the project that the load balancer is associated with. If no ID is provided at creation, the load balancer associates with the user&#39;s default project. If an invalid project ID is provided, the load balancer will not be created. |  [optional] |
|**ip** | **String** | An attribute containing the public-facing IP address of the load balancer. |  [optional] [readonly] |
|**sizeUnit** | **Integer** | How many nodes the load balancer contains. Each additional node increases the load balancer&#39;s ability to manage more connections. Load balancers can be scaled up or down, and you can change the number of nodes after creation up to once per hour. This field is currently not available in the AMS2, NYC2, or SFO1 regions. Use the &#x60;size&#x60; field to scale load balancers that reside in these regions. |  [optional] |
|**size** | [**SizeEnum**](#SizeEnum) | This field has been replaced by the &#x60;size_unit&#x60; field for all regions except in AMS2, NYC2, and SFO1. Each available load balancer size now equates to the load balancer having a set number of nodes. * &#x60;lb-small&#x60; &#x3D; 1 node * &#x60;lb-medium&#x60; &#x3D; 3 nodes * &#x60;lb-large&#x60; &#x3D; 6 nodes  You can resize load balancers after creation up to once per hour. You cannot resize a load balancer within the first hour of its creation. |  [optional] |
|**algorithm** | [**AlgorithmEnum**](#AlgorithmEnum) | This field has been deprecated. You can no longer specify an algorithm for load balancers. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | A status string indicating the current state of the load balancer. This can be &#x60;new&#x60;, &#x60;active&#x60;, or &#x60;errored&#x60;. |  [optional] [readonly] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the load balancer was created. |  [optional] [readonly] |
|**forwardingRules** | [**List&lt;ForwardingRule&gt;**](ForwardingRule.md) | An array of objects specifying the forwarding rules for a load balancer. |  |
|**healthCheck** | [**HealthCheck**](HealthCheck.md) |  |  [optional] |
|**stickySessions** | [**StickySessions**](StickySessions.md) |  |  [optional] |
|**redirectHttpToHttps** | **Boolean** | A boolean value indicating whether HTTP requests to the load balancer on port 80 will be redirected to HTTPS on port 443. |  [optional] |
|**enableProxyProtocol** | **Boolean** | A boolean value indicating whether PROXY Protocol is in use. |  [optional] |
|**enableBackendKeepalive** | **Boolean** | A boolean value indicating whether HTTP keepalive connections are maintained to target Droplets. |  [optional] |
|**httpIdleTimeoutSeconds** | **Integer** | An integer value which configures the idle timeout for HTTP requests to the target droplets. |  [optional] |
|**vpcUuid** | **UUID** | A string specifying the UUID of the VPC to which the load balancer is assigned. |  [optional] |
|**disableLetsEncryptDnsRecords** | **Boolean** | A boolean value indicating whether to disable automatic DNS record creation for Let&#39;s Encrypt certificates that are added to the load balancer. |  [optional] |
|**firewall** | [**LbFirewall**](LbFirewall.md) |  |  [optional] |



## Enum: SizeEnum

| Name | Value |
|---- | -----|
| SMALL | &quot;lb-small&quot; |
| MEDIUM | &quot;lb-medium&quot; |
| LARGE | &quot;lb-large&quot; |



## Enum: AlgorithmEnum

| Name | Value |
|---- | -----|
| ROUND_ROBIN | &quot;round_robin&quot; |
| LEAST_CONNECTIONS | &quot;least_connections&quot; |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| NEW | &quot;new&quot; |
| ACTIVE | &quot;active&quot; |
| ERRORED | &quot;errored&quot; |



