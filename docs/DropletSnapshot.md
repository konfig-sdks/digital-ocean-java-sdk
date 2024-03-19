

# DropletSnapshot


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **Integer** | The unique identifier for the snapshot or backup. |  |
|**name** | **String** | A human-readable name for the snapshot. |  |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the snapshot was created. |  |
|**regions** | **List&lt;String&gt;** | An array of the regions that the snapshot is available in. The regions are represented by their identifying slug values. |  |
|**minDiskSize** | **Integer** | The minimum size in GB required for a volume or Droplet to use this snapshot. |  |
|**sizeGigabytes** | **Float** | The billable size of the snapshot in gigabytes. |  |
|**type** | [**TypeEnum**](#TypeEnum) | Describes the kind of image. It may be one of &#x60;snapshot&#x60; or &#x60;backup&#x60;. This specifies whether an image is a user-generated Droplet snapshot or automatically created Droplet backup. |  |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| SNAPSHOT | &quot;snapshot&quot; |
| BACKUP | &quot;backup&quot; |



