

# UserSettingsAclInner


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | An identifier for the ACL. Will be computed after the ACL is created/updated. |  [optional] |
|**topic** | **String** | A regex for matching the topic(s) that this ACL should apply to. |  |
|**permission** | [**PermissionEnum**](#PermissionEnum) | Permission set applied to the ACL. &#39;consume&#39; allows for messages to be consumed from the topic. &#39;produce&#39; allows for messages to be published to the topic. &#39;produceconsume&#39; allows for both &#39;consume&#39; and &#39;produce&#39; permission. &#39;admin&#39; allows for &#39;produceconsume&#39; as well as any operations to administer the topic (delete, update). |  |



## Enum: PermissionEnum

| Name | Value |
|---- | -----|
| ADMIN | &quot;admin&quot; |
| CONSUME | &quot;consume&quot; |
| PRODUCE | &quot;produce&quot; |
| PRODUCECONSUME | &quot;produceconsume&quot; |



