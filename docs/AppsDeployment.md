

# AppsDeployment


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cause** | **String** |  |  [optional] |
|**clonedFrom** | **String** |  |  [optional] |
|**createdAt** | **OffsetDateTime** |  |  [optional] |
|**id** | **String** |  |  [optional] |
|**jobs** | [**List&lt;AppsDeploymentJob&gt;**](AppsDeploymentJob.md) |  |  [optional] |
|**functions** | [**List&lt;AppsDeploymentFunctions&gt;**](AppsDeploymentFunctions.md) |  |  [optional] |
|**phase** | **AppsDeploymentPhase** |  |  [optional] |
|**phaseLastUpdatedAt** | **OffsetDateTime** |  |  [optional] |
|**progress** | [**AppsDeploymentProgress**](AppsDeploymentProgress.md) |  |  [optional] |
|**services** | [**List&lt;AppsDeploymentService&gt;**](AppsDeploymentService.md) |  |  [optional] |
|**spec** | [**AppSpec**](AppSpec.md) |  |  [optional] |
|**staticSites** | [**List&lt;AppsDeploymentStaticSite&gt;**](AppsDeploymentStaticSite.md) |  |  [optional] |
|**tierSlug** | **String** |  |  [optional] [readonly] |
|**updatedAt** | **OffsetDateTime** |  |  [optional] |
|**workers** | [**List&lt;AppsDeploymentWorker&gt;**](AppsDeploymentWorker.md) |  |  [optional] |



