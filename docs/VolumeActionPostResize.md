

# VolumeActionPostResize


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**type** | [**TypeEnum**](#TypeEnum) | The volume action to initiate. |  |
|**region** | **RegionSlug** |  |  [optional] |
|**sizeGigabytes** | **Integer** | The new size of the block storage volume in GiB (1024^3). |  |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| ATTACH | &quot;attach&quot; |
| DETACH | &quot;detach&quot; |
| RESIZE | &quot;resize&quot; |



