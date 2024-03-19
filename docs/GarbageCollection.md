

# GarbageCollection


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**uuid** | **String** | A string specifying the UUID of the garbage collection. |  [optional] |
|**registryName** | **String** | The name of the container registry. |  [optional] |
|**status** | [**StatusEnum**](#StatusEnum) | The current status of this garbage collection. |  [optional] |
|**createdAt** | **OffsetDateTime** | The time the garbage collection was created. |  [optional] |
|**updatedAt** | **OffsetDateTime** | The time the garbage collection was last updated. |  [optional] |
|**blobsDeleted** | **Integer** | The number of blobs deleted as a result of this garbage collection. |  [optional] |
|**freedBytes** | **Integer** | The number of bytes freed as a result of this garbage collection. |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| REQUESTED | &quot;requested&quot; |
| WAITING_FOR_WRITE_JWTS_TO_EXPIRE | &quot;waiting for write JWTs to expire&quot; |
| SCANNING_MANIFESTS | &quot;scanning manifests&quot; |
| DELETING_UNREFERENCED_BLOBS | &quot;deleting unreferenced blobs&quot; |
| CANCELLING | &quot;cancelling&quot; |
| FAILED | &quot;failed&quot; |
| SUCCEEDED | &quot;succeeded&quot; |
| CANCELLED | &quot;cancelled&quot; |



