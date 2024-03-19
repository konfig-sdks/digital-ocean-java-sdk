

# NullProperty1

If the reserved IP is not assigned to a Droplet, the value will be null.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | An array of Tags the Droplet has been tagged with. |  [optional] |
|**id** | **Integer** | A unique identifier for each Droplet instance. This is automatically generated upon Droplet creation. |  [optional] |
|**name** | **String** | The human-readable name set for the Droplet instance. |  [optional] |
|**memory** | **Integer** | Memory of the Droplet in megabytes. |  [optional] |
|**vcpus** | **Integer** | The number of virtual CPUs. |  [optional] |
|**disk** | **Integer** | The size of the Droplet&#39;s disk in gigabytes. |  [optional] |
|**locked** | **Boolean** | A boolean value indicating whether the Droplet has been locked, preventing actions by users. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | A status string indicating the state of the Droplet instance. This may be \&quot;new\&quot;, \&quot;active\&quot;, \&quot;off\&quot;, or \&quot;archive\&quot;. |  [optional] |
|**kernel** | [**Kernel**](Kernel.md) |  |  [optional] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the Droplet was created. |  [optional] |
|**features** | **List&lt;String&gt;** | An array of features enabled on this Droplet. |  [optional] |
|**backupIds** | **List&lt;Integer&gt;** | An array of backup IDs of any backups that have been taken of the Droplet instance.  Droplet backups are enabled at the time of the instance creation. |  [optional] |
|**nextBackupWindow** | [**DropletNextBackupWindow**](DropletNextBackupWindow.md) |  |  [optional] |
|**snapshotIds** | **List&lt;Integer&gt;** | An array of snapshot IDs of any snapshots created from the Droplet instance. |  [optional] |
|**image** | [**Image**](Image.md) |  |  [optional] |
|**volumeIds** | **List&lt;String&gt;** | A flat array including the unique identifier for each Block Storage volume attached to the Droplet. |  [optional] |
|**size** | [**Size**](Size.md) |  |  [optional] |
|**sizeSlug** | **String** | The unique slug identifier for the size of this Droplet. |  [optional] |
|**networks** | [**DropletNetworks**](DropletNetworks.md) |  |  [optional] |
|**region** | [**Region**](Region.md) |  |  [optional] |
|**vpcUuid** | **String** | A string specifying the UUID of the VPC to which the Droplet is assigned. |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| NEW | &quot;new&quot; |
| ACTIVE | &quot;active&quot; |
| FALSE | &quot;false&quot; |
| ARCHIVE | &quot;archive&quot; |



