

# ProjectBase


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | The description of the project. The maximum length is 255 characters. |  [optional] |
|**id** | **UUID** | The unique universal identifier of this project. |  [optional] [readonly] |
|**ownerUuid** | **String** | The unique universal identifier of the project owner. |  [optional] [readonly] |
|**ownerId** | **Integer** | The integer id of the project owner. |  [optional] [readonly] |
|**name** | **String** | The human-readable name for the project. The maximum length is 175 characters and the name must be unique. |  [optional] |
|**purpose** | **String** | The purpose of the project. The maximum length is 255 characters. It can have one of the following values:  - Just trying out DigitalOcean - Class project / Educational purposes - Website or blog - Web Application - Service or API - Mobile Application - Machine learning / AI / Data processing - IoT - Operational / Developer tooling  If another value for purpose is specified, for example, \&quot;your custom purpose\&quot;, your purpose will be stored as &#x60;Other: your custom purpose&#x60;.  |  [optional] |
|**environment** | [**EnvironmentEnum**](#EnvironmentEnum) | The environment of the project&#39;s resources. |  [optional] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the project was created. |  [optional] [readonly] |
|**updatedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the project was updated. |  [optional] [readonly] |



## Enum: EnvironmentEnum

| Name | Value |
|---- | -----|
| DEVELOPMENT | &quot;Development&quot; |
| STAGING | &quot;Staging&quot; |
| PRODUCTION | &quot;Production&quot; |



