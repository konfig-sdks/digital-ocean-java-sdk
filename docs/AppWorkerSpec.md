

# AppWorkerSpec


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name. Must be unique across all components within the same app. |  |
|**git** | [**AppsGitSourceSpec**](AppsGitSourceSpec.md) |  |  [optional] |
|**github** | [**AppsGithubSourceSpec**](AppsGithubSourceSpec.md) |  |  [optional] |
|**gitlab** | [**AppsGitlabSourceSpec**](AppsGitlabSourceSpec.md) |  |  [optional] |
|**image** | [**AppsImageSourceSpec**](AppsImageSourceSpec.md) |  |  [optional] |
|**dockerfilePath** | **String** | The path to the Dockerfile relative to the root of the repo. If set, it will be used to build this component. Otherwise, App Platform will attempt to build it using buildpacks. |  [optional] |
|**buildCommand** | **String** | An optional build command to run while building this component from source. |  [optional] |
|**runCommand** | **String** | An optional run command to override the component&#39;s default. |  [optional] |
|**sourceDir** | **String** | An optional path to the working directory to use for the build. For Dockerfile builds, this will be used as the build context. Must be relative to the root of the repo. |  [optional] |
|**envs** | [**List&lt;AppVariableDefinition&gt;**](AppVariableDefinition.md) | A list of environment variables made available to the component. |  [optional] |
|**environmentSlug** | **String** | An environment slug describing the type of this app. For a full list, please refer to [the product documentation](https://www.digitalocean.com/docs/app-platform/). |  [optional] |
|**logDestinations** | [**AppLogDestinationDefinition**](AppLogDestinationDefinition.md) |  |  [optional] |
|**instanceCount** | **Long** | The amount of instances that this component should be scaled to. Default: 1. Must not be set if autoscaling is used. |  [optional] |
|**instanceSizeSlug** | [**InstanceSizeSlugEnum**](#InstanceSizeSlugEnum) | The instance size to use for this component. Default: &#x60;basic-xxs&#x60; |  [optional] |
|**autoscaling** | [**AppComponentInstanceBaseAutoscaling**](AppComponentInstanceBaseAutoscaling.md) |  |  [optional] |



## Enum: InstanceSizeSlugEnum

| Name | Value |
|---- | -----|
| BASIC_XXS | &quot;basic-xxs&quot; |
| BASIC_XS | &quot;basic-xs&quot; |
| BASIC_S | &quot;basic-s&quot; |
| BASIC_M | &quot;basic-m&quot; |
| PROFESSIONAL_XS | &quot;professional-xs&quot; |
| PROFESSIONAL_S | &quot;professional-s&quot; |
| PROFESSIONAL_M | &quot;professional-m&quot; |
| PROFESSIONAL_1L | &quot;professional-1l&quot; |
| PROFESSIONAL_L | &quot;professional-l&quot; |
| PROFESSIONAL_XL | &quot;professional-xl&quot; |



