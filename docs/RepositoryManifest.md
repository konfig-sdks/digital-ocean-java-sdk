

# RepositoryManifest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | **List&lt;String&gt;** | All tags associated with this manifest |  [optional] |
|**registryName** | **String** | The name of the container registry. |  [optional] |
|**repository** | **String** | The name of the repository. |  [optional] |
|**digest** | **String** | The manifest digest |  [optional] |
|**compressedSizeBytes** | **Integer** | The compressed size of the manifest in bytes. |  [optional] |
|**sizeBytes** | **Integer** | The uncompressed size of the manifest in bytes (this size is calculated asynchronously so it may not be immediately available). |  [optional] |
|**updatedAt** | **OffsetDateTime** | The time the manifest was last updated. |  [optional] |
|**blobs** | [**List&lt;RepositoryBlob&gt;**](RepositoryBlob.md) | All blobs associated with this manifest |  [optional] |



