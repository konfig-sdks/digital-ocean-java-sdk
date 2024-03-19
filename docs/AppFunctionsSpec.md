

# AppFunctionsSpec


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cors** | [**AppFunctionsSpecCors**](AppFunctionsSpecCors.md) |  |  [optional] |
|**routes** | [**List&lt;AppRouteSpec&gt;**](AppRouteSpec.md) | (Deprecated - Use Ingress Rules instead). A list of HTTP routes that should be routed to this component. |  [optional] |
|**name** | **String** | The name. Must be unique across all components within the same app. |  |
|**sourceDir** | **String** | An optional path to the working directory to use for the build. For Dockerfile builds, this will be used as the build context. Must be relative to the root of the repo. |  [optional] |
|**alerts** | [**List&lt;AppAlertSpec&gt;**](AppAlertSpec.md) |  |  [optional] |
|**envs** | [**List&lt;AppVariableDefinition&gt;**](AppVariableDefinition.md) | A list of environment variables made available to the component. |  [optional] |
|**git** | [**AppsGitSourceSpec**](AppsGitSourceSpec.md) |  |  [optional] |
|**github** | [**AppsGithubSourceSpec**](AppsGithubSourceSpec.md) |  |  [optional] |
|**gitlab** | [**AppsGitlabSourceSpec**](AppsGitlabSourceSpec.md) |  |  [optional] |
|**logDestinations** | [**AppLogDestinationDefinition**](AppLogDestinationDefinition.md) |  |  [optional] |



