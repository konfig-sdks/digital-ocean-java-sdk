

# ReservedIPsActionsPostResponseAction


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **Integer** | A unique numeric ID that can be used to identify and reference an action. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | The current status of the action. This can be \&quot;in-progress\&quot;, \&quot;completed\&quot;, or \&quot;errored\&quot;. |  [optional] |
|**type** | **String** | This is the type of action that the object represents. For example, this could be \&quot;transfer\&quot; to represent the state of an image transfer action. |  [optional] |
|**startedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the action was initiated. |  [optional] |
|**completedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the action was completed. |  [optional] |
|**resourceId** | **Integer** | A unique identifier for the resource that the action is associated with. |  [optional] |
|**resourceType** | **String** | The type of resource that the action is associated with. |  [optional] |
|**region** | [**Region**](Region.md) |  |  [optional] |
|**regionSlug** | [**String**](String.md) |  |  [optional] |
|**projectId** | **UUID** | The UUID of the project to which the reserved IP currently belongs. |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| IN_PROGRESS | &quot;in-progress&quot; |
| COMPLETED | &quot;completed&quot; |
| ERRORED | &quot;errored&quot; |



