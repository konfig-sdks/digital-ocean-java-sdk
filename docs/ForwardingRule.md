

# ForwardingRule

An object specifying a forwarding rule for a load balancer.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**entryProtocol** | [**EntryProtocolEnum**](#EntryProtocolEnum) | The protocol used for traffic to the load balancer. The possible values are: &#x60;http&#x60;, &#x60;https&#x60;, &#x60;http2&#x60;, &#x60;http3&#x60;, &#x60;tcp&#x60;, or &#x60;udp&#x60;. If you set the  &#x60;entry_protocol&#x60; to &#x60;udp&#x60;, the &#x60;target_protocol&#x60; must be set to &#x60;udp&#x60;.  When using UDP, the load balancer requires that you set up a health  check with a port that uses TCP, HTTP, or HTTPS to work properly.  |  |
|**entryPort** | **Integer** | An integer representing the port on which the load balancer instance will listen. |  |
|**targetProtocol** | [**TargetProtocolEnum**](#TargetProtocolEnum) | The protocol used for traffic from the load balancer to the backend Droplets. The possible values are: &#x60;http&#x60;, &#x60;https&#x60;, &#x60;http2&#x60;, &#x60;tcp&#x60;, or &#x60;udp&#x60;. If you set the &#x60;target_protocol&#x60; to &#x60;udp&#x60;, the &#x60;entry_protocol&#x60; must be set to  &#x60;udp&#x60;. When using UDP, the load balancer requires that you set up a health  check with a port that uses TCP, HTTP, or HTTPS to work properly.  |  |
|**targetPort** | **Integer** | An integer representing the port on the backend Droplets to which the load balancer will send traffic. |  |
|**certificateId** | **String** | The ID of the TLS certificate used for SSL termination if enabled. |  [optional] |
|**tlsPassthrough** | **Boolean** | A boolean value indicating whether SSL encrypted traffic will be passed through to the backend Droplets. |  [optional] |



## Enum: EntryProtocolEnum

| Name | Value |
|---- | -----|
| HTTP | &quot;http&quot; |
| HTTPS | &quot;https&quot; |
| HTTP2 | &quot;http2&quot; |
| HTTP3 | &quot;http3&quot; |
| TCP | &quot;tcp&quot; |
| UDP | &quot;udp&quot; |



## Enum: TargetProtocolEnum

| Name | Value |
|---- | -----|
| HTTP | &quot;http&quot; |
| HTTPS | &quot;https&quot; |
| HTTP2 | &quot;http2&quot; |
| TCP | &quot;tcp&quot; |
| UDP | &quot;udp&quot; |



