

# Image


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags. |  [optional] |
|**description** | **String** | An optional free-form text field to describe an image. |  [optional] |
|**id** | **Integer** | A unique number that can be used to identify and reference a specific image. |  [optional] [readonly] |
|**name** | **String** | The display name that has been given to an image.  This is what is shown in the control panel and is generally a descriptive title for the image in question. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | Describes the kind of image. It may be one of &#x60;base&#x60;, &#x60;snapshot&#x60;, &#x60;backup&#x60;, &#x60;custom&#x60;, or &#x60;admin&#x60;. Respectively, this specifies whether an image is a DigitalOcean base OS image, user-generated Droplet snapshot, automatically created Droplet backup, user-provided virtual machine image, or an image used for DigitalOcean managed resources (e.g. DOKS worker nodes). |  [optional] |
|**distribution** | **Distribution** |  |  [optional] |
|**slug** | **String** | A uniquely identifying string that is associated with each of the DigitalOcean-provided public images. These can be used to reference a public image as an alternative to the numeric id. |  [optional] |
|**_public** | **Boolean** | This is a boolean value that indicates whether the image in question is public or not. An image that is public is available to all accounts. A non-public image is only accessible from your account. |  [optional] |
|**regions** | **List&lt;RegionSlug&gt;** | This attribute is an array of the regions that the image is available in. The regions are represented by their identifying slug values. |  [optional] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the image was created. |  [optional] |
|**minDiskSize** | **Integer** | The minimum disk size in GB required for a Droplet to use this image. |  [optional] |
|**sizeGigabytes** | **Float** | The size of the image in gigabytes. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | A status string indicating the state of a custom image. This may be &#x60;NEW&#x60;,  &#x60;available&#x60;, &#x60;pending&#x60;, &#x60;deleted&#x60;, or &#x60;retired&#x60;. |  [optional] |
|**errorMessage** | **String** | A string containing information about errors that may occur when importing  a custom image. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| BASE | &quot;base&quot; |
| SNAPSHOT | &quot;snapshot&quot; |
| BACKUP | &quot;backup&quot; |
| CUSTOM | &quot;custom&quot; |
| ADMIN | &quot;admin&quot; |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| NEW | &quot;NEW&quot; |
| AVAILABLE | &quot;available&quot; |
| PENDING | &quot;pending&quot; |
| DELETED | &quot;deleted&quot; |
| RETIRED | &quot;retired&quot; |



