

# Exvolumes


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags. |  [optional] |
|**description** | **String** | An optional free-form text field to describe a block storage volume. |  [optional] |
|**id** | **String** | The unique identifier for the block storage volume. |  [optional] [readonly] |
|**dropletIds** | **List&lt;Integer&gt;** | An array containing the IDs of the Droplets the volume is attached to. Note that at this time, a volume can only be attached to a single Droplet. |  [optional] [readonly] |
|**name** | **String** | A human-readable name for the block storage volume. Must be lowercase and be composed only of numbers, letters and \&quot;-\&quot;, up to a limit of 64 characters. The name must begin with a letter. |  |
|**sizeGigabytes** | **Integer** | The size of the block storage volume in GiB (1024^3). This field does not apply  when creating a volume from a snapshot. |  |
|**createdAt** | **String** | A time value given in ISO8601 combined date and time format that represents when the block storage volume was created. |  [optional] [readonly] |
|**snapshotId** | **String** | The unique identifier for the volume snapshot from which to create the volume. |  [optional] |
|**filesystemType** | **String** | The name of the filesystem type to be used on the volume. When provided, the volume will automatically be formatted to the specified filesystem type. Currently, the available options are &#x60;ext4&#x60; and &#x60;xfs&#x60;. Pre-formatted volumes are automatically mounted when attached to Ubuntu, Debian, Fedora, Fedora Atomic, and CentOS Droplets created on or after April 26, 2018. Attaching pre-formatted volumes to other Droplets is not recommended. |  [optional] |
|**region** | **RegionSlug** |  |  |
|**filesystemLabel** | [**String**](String.md) |  |  [optional] |



