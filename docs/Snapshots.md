

# Snapshots


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | The unique identifier for the snapshot. |  |
|**name** | **String** | A human-readable name for the snapshot. |  |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the snapshot was created. |  |
|**regions** | **List&lt;String&gt;** | An array of the regions that the snapshot is available in. The regions are represented by their identifying slug values. |  |
|**minDiskSize** | **Integer** | The minimum size in GB required for a volume or Droplet to use this snapshot. |  |
|**sizeGigabytes** | **Float** | The billable size of the snapshot in gigabytes. |  |
|**tags** | **List&lt;String&gt;** | An array of Tags the snapshot has been tagged with. |  |
|**resourceId** | **String** | The unique identifier for the resource that the snapshot originated from. |  |
|**resourceType** | [**ResourceTypeEnum**](#ResourceTypeEnum) | The type of resource that the snapshot originated from. |  |



## Enum: ResourceTypeEnum

| Name | Value |
|---- | -----|
| DROPLET | &quot;droplet&quot; |
| VOLUME | &quot;volume&quot; |



