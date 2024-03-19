

# App

An application's configuration and status.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**activeDeployment** | [**AppsDeployment**](AppsDeployment.md) |  |  [optional] |
|**createdAt** | **OffsetDateTime** |  |  [optional] [readonly] |
|**defaultIngress** | **String** |  |  [optional] [readonly] |
|**domains** | [**List&lt;AppsDomain&gt;**](AppsDomain.md) |  |  [optional] [readonly] |
|**id** | **String** |  |  [optional] [readonly] |
|**inProgressDeployment** | [**AppsDeployment**](AppsDeployment.md) |  |  [optional] |
|**lastDeploymentCreatedAt** | **OffsetDateTime** |  |  [optional] [readonly] |
|**liveDomain** | **String** |  |  [optional] [readonly] |
|**liveUrl** | **String** |  |  [optional] [readonly] |
|**liveUrlBase** | **String** |  |  [optional] [readonly] |
|**ownerUuid** | **String** |  |  [optional] [readonly] |
|**pendingDeployment** | [**AppPendingDeployment**](AppPendingDeployment.md) |  |  [optional] |
|**projectId** | **String** |  |  [optional] [readonly] |
|**region** | [**AppsRegion**](AppsRegion.md) |  |  [optional] |
|**spec** | [**AppSpec**](AppSpec.md) |  |  |
|**tierSlug** | **String** |  |  [optional] [readonly] |
|**updatedAt** | **OffsetDateTime** |  |  [optional] [readonly] |
|**pinnedDeployment** | [**AppPinnedDeployment**](AppPinnedDeployment.md) |  |  [optional] |



