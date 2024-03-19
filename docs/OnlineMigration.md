

# OnlineMigration


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The ID of the most recent migration. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | The current status of the migration. |  [optional] |
|**createdAt** | **String** | The time the migration was initiated, in ISO 8601 format. |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| RUNNING | &quot;running&quot; |
| CANCELED | &quot;canceled&quot; |
| ERROR | &quot;error&quot; |
| DONE | &quot;done&quot; |



