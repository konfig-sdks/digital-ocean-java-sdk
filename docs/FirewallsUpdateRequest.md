

# FirewallsUpdateRequest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | [**List**](List.md) |  |  [optional] |
|**id** | **String** | A unique ID that can be used to identify and reference a firewall. |  [optional] [readonly] |
|**status** | [**StatusEnum**](#StatusEnum) | A status string indicating the current state of the firewall. This can be \&quot;waiting\&quot;, \&quot;succeeded\&quot;, or \&quot;failed\&quot;. |  [optional] [readonly] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the firewall was created. |  [optional] [readonly] |
|**pendingChanges** | [**List&lt;FirewallAllOfPendingChanges&gt;**](FirewallAllOfPendingChanges.md) | An array of objects each containing the fields \&quot;droplet_id\&quot;, \&quot;removing\&quot;, and \&quot;status\&quot;. It is provided to detail exactly which Droplets are having their security policies updated. When empty, all changes have been successfully applied. |  [optional] [readonly] |
|**name** | **String** | A human-readable name for a firewall. The name must begin with an alphanumeric character. Subsequent characters must either be alphanumeric characters, a period (.), or a dash (-). |  |
|**dropletIds** | **List&lt;Integer&gt;** | An array containing the IDs of the Droplets assigned to the firewall. |  [optional] |
|**inboundRules** | [**List&lt;FirewallRulesInboundRulesInner&gt;**](FirewallRulesInboundRulesInner.md) |  |  |
|**outboundRules** | [**List&lt;FirewallRulesOutboundRulesInner&gt;**](FirewallRulesOutboundRulesInner.md) |  |  |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| WAITING | &quot;waiting&quot; |
| SUCCEEDED | &quot;succeeded&quot; |
| FAILED | &quot;failed&quot; |



