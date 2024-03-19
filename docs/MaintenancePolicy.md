

# MaintenancePolicy

An object specifying the maintenance window policy for the Kubernetes cluster.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**startTime** | **String** | The start time in UTC of the maintenance window policy in 24-hour clock format / HH:MM notation (e.g., &#x60;15:00&#x60;). |  [optional] |
|**duration** | **String** | The duration of the maintenance window policy in human-readable format. |  [optional] [readonly] |
|**day** | [**DayEnum**](#DayEnum) | The day of the maintenance window policy. May be one of &#x60;monday&#x60; through &#x60;sunday&#x60;, or &#x60;any&#x60; to indicate an arbitrary week day. |  [optional] |



## Enum: DayEnum

| Name | Value |
|---- | -----|
| ANY | &quot;any&quot; |
| MONDAY | &quot;monday&quot; |
| TUESDAY | &quot;tuesday&quot; |
| WEDNESDAY | &quot;wednesday&quot; |
| THURSDAY | &quot;thursday&quot; |
| FRIDAY | &quot;friday&quot; |
| SATURDAY | &quot;saturday&quot; |
| SUNDAY | &quot;sunday&quot; |



