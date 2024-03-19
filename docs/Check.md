

# Check


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | A unique ID that can be used to identify and reference the check. |  [optional] [readonly] |
|**name** | **String** | A human-friendly display name. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | The type of health check to perform. |  [optional] |
|**target** | **String** | The endpoint to perform healthchecks on. |  [optional] |
|**regions** | [**List&lt;RegionsEnum&gt;**](#List&lt;RegionsEnum&gt;) | An array containing the selected regions to perform healthchecks from. |  [optional] |
|**enabled** | **Boolean** | A boolean value indicating whether the check is enabled/disabled. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| PING | &quot;ping&quot; |
| HTTP | &quot;http&quot; |
| HTTPS | &quot;https&quot; |



## Enum: List&lt;RegionsEnum&gt;

| Name | Value |
|---- | -----|
| US_EAST | &quot;us_east&quot; |
| US_WEST | &quot;us_west&quot; |
| EU_WEST | &quot;eu_west&quot; |
| SE_ASIA | &quot;se_asia&quot; |



