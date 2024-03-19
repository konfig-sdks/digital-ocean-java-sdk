

# Droplet


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | An array of Tags the Droplet has been tagged with. |  |
|**id** | **Integer** | A unique identifier for each Droplet instance. This is automatically generated upon Droplet creation. |  |
|**name** | **String** | The human-readable name set for the Droplet instance. |  |
|**memory** | **Integer** | Memory of the Droplet in megabytes. |  |
|**vcpus** | **Integer** | The number of virtual CPUs. |  |
|**disk** | **Integer** | The size of the Droplet&#39;s disk in gigabytes. |  |
|**locked** | **Boolean** | A boolean value indicating whether the Droplet has been locked, preventing actions by users. |  |
|**status** | [**StatusEnum**](#StatusEnum) | A status string indicating the state of the Droplet instance. This may be \&quot;new\&quot;, \&quot;active\&quot;, \&quot;off\&quot;, or \&quot;archive\&quot;. |  |
|**kernel** | [**Kernel**](Kernel.md) |  |  [optional] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the Droplet was created. |  |
|**features** | **List&lt;String&gt;** | An array of features enabled on this Droplet. |  |
|**backupIds** | **List&lt;Integer&gt;** | An array of backup IDs of any backups that have been taken of the Droplet instance.  Droplet backups are enabled at the time of the instance creation. |  |
|**nextBackupWindow** | [**DropletNextBackupWindow**](DropletNextBackupWindow.md) |  |  |
|**snapshotIds** | **List&lt;Integer&gt;** | An array of snapshot IDs of any snapshots created from the Droplet instance. |  |
|**image** | [**Image**](Image.md) |  |  |
|**volumeIds** | **List&lt;String&gt;** | A flat array including the unique identifier for each Block Storage volume attached to the Droplet. |  |
|**size** | [**Size**](Size.md) |  |  |
|**sizeSlug** | **String** | The unique slug identifier for the size of this Droplet. |  |
|**networks** | [**DropletNetworks**](DropletNetworks.md) |  |  |
|**region** | [**Region**](Region.md) |  |  |
|**vpcUuid** | **String** | A string specifying the UUID of the VPC to which the Droplet is assigned. |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| NEW | &quot;new&quot; |
| ACTIVE | &quot;active&quot; |
| FALSE | &quot;false&quot; |
| ARCHIVE | &quot;archive&quot; |



