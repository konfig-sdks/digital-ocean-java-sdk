

# LoadBalancersUpdateResponseLoadBalancer


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
|**forwardingRules** | [**List&lt;LoadBalancersUpdateResponseLoadBalancerForwardingRulesInner&gt;**](LoadBalancersUpdateResponseLoadBalancerForwardingRulesInner.md) |  |  [optional] |
|**healthCheck** | [**LoadBalancersUpdateResponseLoadBalancerHealthCheck**](LoadBalancersUpdateResponseLoadBalancerHealthCheck.md) |  |  [optional] |
|**stickySessions** | [**LoadBalancersUpdateResponseLoadBalancerStickySessions**](LoadBalancersUpdateResponseLoadBalancerStickySessions.md) |  |  [optional] |
|**region** | [**LoadBalancersUpdateResponseLoadBalancerRegion**](LoadBalancersUpdateResponseLoadBalancerRegion.md) |  |  [optional] |
|**tag** | **String** |  |  [optional] |
|**dropletIds** | **List&lt;Double&gt;** |  |  [optional] |
|**redirectHttpToHttps** | **Boolean** |  |  [optional] |
|**enableProxyProtocol** | **Boolean** |  |  [optional] |
|**enableBackendKeepalive** | **Boolean** |  |  [optional] |
|**vpcUuid** | **String** |  |  [optional] |
|**disableLetsEncryptDnsRecords** | **Boolean** |  |  [optional] |
|**projectId** | **String** |  |  [optional] |
|**httpIdleTimeoutSeconds** | **Double** |  |  [optional] |
|**firewall** | [**LoadBalancersUpdateResponseLoadBalancerFirewall**](LoadBalancersUpdateResponseLoadBalancerFirewall.md) |  |  [optional] |



