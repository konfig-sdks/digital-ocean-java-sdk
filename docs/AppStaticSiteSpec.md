

# AppStaticSiteSpec


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
|**indexDocument** | **String** | The name of the index document to use when serving this static site. Default: index.html |  [optional] |
|**errorDocument** | **String** | The name of the error document to use when serving this static site. Default: 404.html. If no such file exists within the built assets, App Platform will supply one. |  [optional] |
|**catchallDocument** | **String** | The name of the document to use as the fallback for any requests to documents that are not found when serving this static site. Only 1 of &#x60;catchall_document&#x60; or &#x60;error_document&#x60; can be set. |  [optional] |
|**outputDir** | **String** | An optional path to where the built assets will be located, relative to the build context. If not set, App Platform will automatically scan for these directory names: &#x60;_static&#x60;, &#x60;dist&#x60;, &#x60;public&#x60;, &#x60;build&#x60;. |  [optional] |
|**cors** | [**AppsCorsPolicy**](AppsCorsPolicy.md) |  |  [optional] |
|**routes** | [**List&lt;AppRouteSpec&gt;**](AppRouteSpec.md) | (Deprecated - Use Ingress Rules instead). A list of HTTP routes that should be routed to this component. |  [optional] |



