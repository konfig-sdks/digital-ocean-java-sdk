

# DropletActionResize


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**type** | [**TypeEnum**](#TypeEnum) | The type of action to initiate for the Droplet. |  |
|**disk** | **Boolean** | When &#x60;true&#x60;, the Droplet&#39;s disk will be resized in addition to its RAM and CPU. This is a permanent change and cannot be reversed as a Droplet&#39;s disk size cannot be decreased. |  [optional] |
|**size** | **String** | The slug identifier for the size to which you wish to resize the Droplet. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| ENABLE_BACKUPS | &quot;enable_backups&quot; |
| DISABLE_BACKUPS | &quot;disable_backups&quot; |
| REBOOT | &quot;reboot&quot; |
| POWER_CYCLE | &quot;power_cycle&quot; |
| SHUTDOWN | &quot;shutdown&quot; |
| POWER_OFF | &quot;power_off&quot; |
| POWER_ON | &quot;power_on&quot; |
| RESTORE | &quot;restore&quot; |
| PASSWORD_RESET | &quot;password_reset&quot; |
| RESIZE | &quot;resize&quot; |
| REBUILD | &quot;rebuild&quot; |
| RENAME | &quot;rename&quot; |
| CHANGE_KERNEL | &quot;change_kernel&quot; |
| ENABLE_IPV6 | &quot;enable_ipv6&quot; |
| SNAPSHOT | &quot;snapshot&quot; |



