

# Registry


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | A globally unique name for the container registry. Must be lowercase and be composed only of numbers, letters and &#x60;-&#x60;, up to a limit of 63 characters. |  [optional] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the registry was created. |  [optional] [readonly] |
|**region** | **String** | Slug of the region where registry data is stored |  [optional] |
|**storageUsageBytes** | **Integer** | The amount of storage used in the registry in bytes. |  [optional] [readonly] |
|**storageUsageBytesUpdatedAt** | **OffsetDateTime** | The time at which the storage usage was updated. Storage usage is calculated asynchronously, and may not immediately reflect pushes to the registry. |  [optional] [readonly] |
|**subscription** | [**RegistrySubscription**](RegistrySubscription.md) |  |  [optional] |



