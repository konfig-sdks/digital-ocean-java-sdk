

# Account


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**dropletLimit** | **Integer** | The total number of Droplets current user or team may have active at one time. |  |
|**floatingIpLimit** | **Integer** | The total number of Floating IPs the current user or team may have. |  |
|**email** | **String** | The email address used by the current user to register for DigitalOcean. |  |
|**name** | **String** | The display name for the current user. |  [optional] |
|**uuid** | **String** | The unique universal identifier for the current user. |  |
|**emailVerified** | **Boolean** | If true, the user has verified their account via email. False otherwise. |  |
|**status** | [**StatusEnum**](#StatusEnum) | This value is one of \&quot;active\&quot;, \&quot;warning\&quot; or \&quot;locked\&quot;. |  |
|**statusMessage** | **String** | A human-readable message giving more details about the status of the account. |  |
|**team** | [**AccountTeam**](AccountTeam.md) |  |  [optional] |



## Enum: StatusEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;active&quot; |
| WARNING | &quot;warning&quot; |
| LOCKED | &quot;locked&quot; |



