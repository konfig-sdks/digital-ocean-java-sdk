

# Alert


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | A unique ID that can be used to identify and reference the alert. |  [optional] [readonly] |
|**name** | **String** | A human-friendly display name. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The type of alert. |  [optional] |
|**threshold** | **Integer** | The threshold at which the alert will enter a trigger state. The specific threshold is dependent on the alert type. |  [optional] |
|**comparison** | [**ComparisonEnum**](#ComparisonEnum) | The comparison operator used against the alert&#39;s threshold. |  [optional] |
|**notifications** | [**Notification**](Notification.md) |  |  [optional] |
|**period** | [**PeriodEnum**](#PeriodEnum) | Period of time the threshold must be exceeded to trigger the alert. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| LATENCY | &quot;latency&quot; |
| DOWN | &quot;down&quot; |
| DOWN_GLOBAL | &quot;down_global&quot; |
| SSL_EXPIRY | &quot;ssl_expiry&quot; |



## Enum: ComparisonEnum

| Name | Value |
|---- | -----|
| GREATER_THAN | &quot;greater_than&quot; |
| LESS_THAN | &quot;less_than&quot; |



## Enum: PeriodEnum

| Name | Value |
|---- | -----|
| _2M | &quot;2m&quot; |
| _3M | &quot;3m&quot; |
| _5M | &quot;5m&quot; |
| _10M | &quot;10m&quot; |
| _15M | &quot;15m&quot; |
| _30M | &quot;30m&quot; |
| _1H | &quot;1h&quot; |



