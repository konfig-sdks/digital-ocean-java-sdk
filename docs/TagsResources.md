

# TagsResources

An embedded object containing key value pairs of resource type and resource statistics. It also includes a count of the total number of resources tagged with the current tag as well as a `last_tagged_uri` attribute set to the last resource tagged with the current tag.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**count** | **Integer** | The number of tagged objects for this type of resource. |  [optional] |
|**lastTaggedUri** | **String** | The URI for the last tagged object for this type of resource. |  [optional] |
|**droplets** | [**TagsMetadata**](TagsMetadata.md) |  |  [optional] |
|**imgages** | [**TagsMetadata**](TagsMetadata.md) |  |  [optional] |
|**volumes** | [**TagsMetadata**](TagsMetadata.md) |  |  [optional] |
|**volumeSnapshots** | [**TagsMetadata**](TagsMetadata.md) |  |  [optional] |
|**databases** | [**TagsMetadata**](TagsMetadata.md) |  |  [optional] |



