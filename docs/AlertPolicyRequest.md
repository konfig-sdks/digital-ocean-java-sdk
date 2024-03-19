

# AlertPolicyRequest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** |  |  |
|**description** | **String** |  |  |
|**alerts** | [**Alerts**](Alerts.md) |  |  |
|**compare** | [**CompareEnum**](#CompareEnum) |  |  |
|**enabled** | **Boolean** |  |  |
|**entities** | **List&lt;String&gt;** |  |  |
|**type** | [**TypeEnum**](#TypeEnum) |  |  |
|**value** | **Float** |  |  |
|**window** | [**WindowEnum**](#WindowEnum) |  |  |



## Enum: CompareEnum

| Name | Value |
|---- | -----|
| GREATERTHAN | &quot;GreaterThan&quot; |
| LESSTHAN | &quot;LessThan&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| INSIGHTS_DROPLET_LOAD_1 | &quot;v1/insights/droplet/load_1&quot; |
| INSIGHTS_DROPLET_LOAD_5 | &quot;v1/insights/droplet/load_5&quot; |
| INSIGHTS_DROPLET_LOAD_15 | &quot;v1/insights/droplet/load_15&quot; |
| INSIGHTS_DROPLET_MEMORY_UTILIZATION_PERCENT | &quot;v1/insights/droplet/memory_utilization_percent&quot; |
| INSIGHTS_DROPLET_DISK_UTILIZATION_PERCENT | &quot;v1/insights/droplet/disk_utilization_percent&quot; |
| INSIGHTS_DROPLET_CPU | &quot;v1/insights/droplet/cpu&quot; |
| INSIGHTS_DROPLET_DISK_READ | &quot;v1/insights/droplet/disk_read&quot; |
| INSIGHTS_DROPLET_DISK_WRITE | &quot;v1/insights/droplet/disk_write&quot; |
| INSIGHTS_DROPLET_PUBLIC_OUTBOUND_BANDWIDTH | &quot;v1/insights/droplet/public_outbound_bandwidth&quot; |
| INSIGHTS_DROPLET_PUBLIC_INBOUND_BANDWIDTH | &quot;v1/insights/droplet/public_inbound_bandwidth&quot; |
| INSIGHTS_DROPLET_PRIVATE_OUTBOUND_BANDWIDTH | &quot;v1/insights/droplet/private_outbound_bandwidth&quot; |
| INSIGHTS_DROPLET_PRIVATE_INBOUND_BANDWIDTH | &quot;v1/insights/droplet/private_inbound_bandwidth&quot; |
| INSIGHTS_LBAAS_AVG_CPU_UTILIZATION_PERCENT | &quot;v1/insights/lbaas/avg_cpu_utilization_percent&quot; |
| INSIGHTS_LBAAS_CONNECTION_UTILIZATION_PERCENT | &quot;v1/insights/lbaas/connection_utilization_percent&quot; |
| INSIGHTS_LBAAS_DROPLET_HEALTH | &quot;v1/insights/lbaas/droplet_health&quot; |
| INSIGHTS_LBAAS_TLS_CONNECTIONS_PER_SECOND_UTILIZATION_PERCENT | &quot;v1/insights/lbaas/tls_connections_per_second_utilization_percent&quot; |
| INSIGHTS_LBAAS_INCREASE_IN_HTTP_ERROR_RATE_PERCENTAGE_5XX | &quot;v1/insights/lbaas/increase_in_http_error_rate_percentage_5xx&quot; |
| INSIGHTS_LBAAS_INCREASE_IN_HTTP_ERROR_RATE_PERCENTAGE_4XX | &quot;v1/insights/lbaas/increase_in_http_error_rate_percentage_4xx&quot; |
| INSIGHTS_LBAAS_INCREASE_IN_HTTP_ERROR_RATE_COUNT_5XX | &quot;v1/insights/lbaas/increase_in_http_error_rate_count_5xx&quot; |
| INSIGHTS_LBAAS_INCREASE_IN_HTTP_ERROR_RATE_COUNT_4XX | &quot;v1/insights/lbaas/increase_in_http_error_rate_count_4xx&quot; |
| INSIGHTS_LBAAS_HIGH_HTTP_REQUEST_RESPONSE_TIME | &quot;v1/insights/lbaas/high_http_request_response_time&quot; |
| INSIGHTS_LBAAS_HIGH_HTTP_REQUEST_RESPONSE_TIME_50P | &quot;v1/insights/lbaas/high_http_request_response_time_50p&quot; |
| INSIGHTS_LBAAS_HIGH_HTTP_REQUEST_RESPONSE_TIME_95P | &quot;v1/insights/lbaas/high_http_request_response_time_95p&quot; |
| INSIGHTS_LBAAS_HIGH_HTTP_REQUEST_RESPONSE_TIME_99P | &quot;v1/insights/lbaas/high_http_request_response_time_99p&quot; |
| DBAAS_ALERTS_LOAD_15_ALERTS | &quot;v1/dbaas/alerts/load_15_alerts&quot; |
| DBAAS_ALERTS_MEMORY_UTILIZATION_ALERTS | &quot;v1/dbaas/alerts/memory_utilization_alerts&quot; |
| DBAAS_ALERTS_DISK_UTILIZATION_ALERTS | &quot;v1/dbaas/alerts/disk_utilization_alerts&quot; |
| DBAAS_ALERTS_CPU_ALERTS | &quot;v1/dbaas/alerts/cpu_alerts&quot; |



## Enum: WindowEnum

| Name | Value |
|---- | -----|
| _5M | &quot;5m&quot; |
| _10M | &quot;10m&quot; |
| _30M | &quot;30m&quot; |
| _1H | &quot;1h&quot; |



