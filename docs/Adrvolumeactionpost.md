

# Adrvolumeactionpost


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**type** | [**TypeEnum**](#TypeEnum) | The volume action to initiate. |  |
|**region** | **RegionSlug** |  |  [optional] |
|**tags** | **List&lt;String&gt;** | A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags. |  [optional] |
|**dropletId** | **Integer** | The unique identifier for the Droplet the volume will be attached or detached from. |  |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| ATTACH | &quot;attach&quot; |
| DETACH | &quot;detach&quot; |
| RESIZE | &quot;resize&quot; |



