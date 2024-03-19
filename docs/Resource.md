

# Resource


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**urn** | **String** | The uniform resource name (URN) for the resource in the format do:resource_type:resource_id. |  [optional] |
|**assignedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the project was created. |  [optional] |
|**links** | [**ResourceLinks**](ResourceLinks.md) |  |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | The status of assigning and fetching the resources. |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| OK | &quot;ok&quot; |
| NOT_FOUND | &quot;not_found&quot; |
| ASSIGNED | &quot;assigned&quot; |
| ALREADY_ASSIGNED | &quot;already_assigned&quot; |
| SERVICE_DOWN | &quot;service_down&quot; |



