

# DestroyedAssociatedResource

An object containing information about a resource scheduled for deletion.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The unique identifier for the resource scheduled for deletion. |  [optional] |
|**name** | **String** | The name of the resource scheduled for deletion. |  [optional] |
|**destroyedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format indicating when the resource was destroyed if the request was successful. |  [optional] |
|**errorMessage** | **String** | A string indicating that the resource was not successfully destroyed and providing additional information. |  [optional] |



