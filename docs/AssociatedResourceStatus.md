

# AssociatedResourceStatus

An objects containing information about a resources scheduled for deletion.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**droplet** | [**DestroyedAssociatedResource**](DestroyedAssociatedResource.md) |  |  [optional] |
|**resources** | [**AssociatedResourceStatusResources**](AssociatedResourceStatusResources.md) |  |  [optional] |
|**completedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format indicating when the requested action was completed. |  [optional] |
|**failures** | **Integer** | A count of the associated resources that failed to be destroyed, if any. |  [optional] |



