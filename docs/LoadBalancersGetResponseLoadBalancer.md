

# LoadBalancersGetResponseLoadBalancer


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
|**forwardingRules** | [**List&lt;LoadBalancersGetResponseLoadBalancerForwardingRulesInner&gt;**](LoadBalancersGetResponseLoadBalancerForwardingRulesInner.md) |  |  [optional] |
|**healthCheck** | [**LoadBalancersGetResponseLoadBalancerHealthCheck**](LoadBalancersGetResponseLoadBalancerHealthCheck.md) |  |  [optional] |
|**stickySessions** | [**LoadBalancersGetResponseLoadBalancerStickySessions**](LoadBalancersGetResponseLoadBalancerStickySessions.md) |  |  [optional] |
|**region** | [**LoadBalancersGetResponseLoadBalancerRegion**](LoadBalancersGetResponseLoadBalancerRegion.md) |  |  [optional] |
|**tag** | **String** |  |  [optional] |
|**dropletIds** | **List&lt;Double&gt;** |  |  [optional] |
|**redirectHttpToHttps** | **Boolean** |  |  [optional] |
|**enableProxyProtocol** | **Boolean** |  |  [optional] |
|**enableBackendKeepalive** | **Boolean** |  |  [optional] |
|**vpcUuid** | **String** |  |  [optional] |
|**disableLetsEncryptDnsRecords** | **Boolean** |  |  [optional] |
|**projectId** | **String** |  |  [optional] |
|**httpIdleTimeoutSeconds** | **Double** |  |  [optional] |
|**firewall** | [**LoadBalancersGetResponseLoadBalancerFirewall**](LoadBalancersGetResponseLoadBalancerFirewall.md) |  |  [optional] |



