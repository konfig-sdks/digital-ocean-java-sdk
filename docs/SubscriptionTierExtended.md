

# SubscriptionTierExtended


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**eligible** | **Boolean** | A boolean indicating whether your account it eligible to use a certain subscription tier. |  [optional] |
|**eligibilityReasons** | [**List&lt;EligibilityReasonsEnum&gt;**](#List&lt;EligibilityReasonsEnum&gt;) | If your account is not eligible to use a certain subscription tier, this will include a list of reasons that prevent you from using the tier. |  [optional] |



## Enum: List&lt;EligibilityReasonsEnum&gt;

| Name | Value |
|---- | -----|
| OVERREPOSITORYLIMIT | &quot;OverRepositoryLimit&quot; |
| OVERSTORAGELIMIT | &quot;OverStorageLimit&quot; |



