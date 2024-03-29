

# FirewallRulesOutboundRulesInner


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**protocol** | [**ProtocolEnum**](#ProtocolEnum) | The type of traffic to be allowed. This may be one of &#x60;tcp&#x60;, &#x60;udp&#x60;, or &#x60;icmp&#x60;. |  |
|**ports** | **String** | The ports on which traffic will be allowed specified as a string containing a single port, a range (e.g. \&quot;8000-9000\&quot;), or \&quot;0\&quot; when all ports are open for a protocol. For ICMP rules this parameter will always return \&quot;0\&quot;. |  |
|**destinations** | [**FirewallRulesOutboundRulesInnerAllOfDestinations**](FirewallRulesOutboundRulesInnerAllOfDestinations.md) |  |  |



## Enum: ProtocolEnum

| Name | Value |
|---- | -----|
| TCP | &quot;tcp&quot; |
| UDP | &quot;udp&quot; |
| ICMP | &quot;icmp&quot; |



