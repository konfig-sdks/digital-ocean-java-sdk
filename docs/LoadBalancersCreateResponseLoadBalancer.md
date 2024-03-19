

# LoadBalancersCreateResponseLoadBalancer


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** |  |  [optional] |
|**name** | **String** |  |  [optional] |
|**ip** | **String** |  |  [optional] |
|**size** | **String** |  |  [optional] |
|**algorithm** | **String** |  |  [optional] |
|**status** | **String** |  |  [optional] |
|**createdAt** | **String** |  |  [optional] |
|**forwardingRules** | [**List&lt;LoadBalancersCreateResponseLoadBalancerForwardingRulesInner&gt;**](LoadBalancersCreateResponseLoadBalancerForwardingRulesInner.md) |  |  [optional] |
|**healthCheck** | [**LoadBalancersCreateResponseLoadBalancerHealthCheck**](LoadBalancersCreateResponseLoadBalancerHealthCheck.md) |  |  [optional] |
|**stickySessions** | [**LoadBalancersCreateResponseLoadBalancerStickySessions**](LoadBalancersCreateResponseLoadBalancerStickySessions.md) |  |  [optional] |
|**region** | [**LoadBalancersCreateResponseLoadBalancerRegion**](LoadBalancersCreateResponseLoadBalancerRegion.md) |  |  [optional] |
|**tag** | **String** |  |  [optional] |
|**dropletIds** | **List&lt;Double&gt;** |  |  [optional] |
|**redirectHttpToHttps** | **Boolean** |  |  [optional] |
|**enableProxyProtocol** | **Boolean** |  |  [optional] |
|**enableBackendKeepalive** | **Boolean** |  |  [optional] |
|**vpcUuid** | **String** |  |  [optional] |
|**disableLetsEncryptDnsRecords** | **Boolean** |  |  [optional] |
|**projectId** | **String** |  |  [optional] |
|**httpIdleTimeoutSeconds** | **Double** |  |  [optional] |
|**firewall** | [**LoadBalancersCreateResponseLoadBalancerFirewall**](LoadBalancersCreateResponseLoadBalancerFirewall.md) |  |  [optional] |



