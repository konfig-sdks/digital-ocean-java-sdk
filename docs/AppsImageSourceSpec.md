

# AppsImageSourceSpec


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**registry** | **String** | The registry name. Must be left empty for the &#x60;DOCR&#x60; registry type. |  [optional] |
|**registryType** | [**RegistryTypeEnum**](#RegistryTypeEnum) | - DOCKER_HUB: The DockerHub container registry type. - DOCR: The DigitalOcean container registry type. - GHCR: The Github container registry type. |  [optional] |
|**registryCredentials** | **String** | The credentials to be able to pull the image. The value will be encrypted on first submission. On following submissions, the encrypted value should be used. - \&quot;$username:$access_token\&quot; for registries of type &#x60;DOCKER_HUB&#x60;. - \&quot;$username:$access_token\&quot; for registries of type &#x60;GHCR&#x60;. |  [optional] |
|**repository** | **String** | The repository name. |  [optional] |
|**tag** | **String** | The repository tag. Defaults to &#x60;latest&#x60; if not provided and no digest is provided. Cannot be specified if digest is provided. |  [optional] |
|**digest** | **String** | The image digest. Cannot be specified if tag is provided. |  [optional] |
|**deployOnPush** | [**AppsImageSourceSpecDeployOnPush**](AppsImageSourceSpecDeployOnPush.md) |  |  [optional] |



## Enum: RegistryTypeEnum

| Name | Value |
|---- | -----|
| DOCKER_HUB | &quot;DOCKER_HUB&quot; |
| DOCR | &quot;DOCR&quot; |
| GHCR | &quot;GHCR&quot; |



