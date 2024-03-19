

# ContainerRegistryListOptionsResponseOptionsSubscriptionTiersInner


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name of the subscription tier. |  [optional] |
|**slug** | **String** | The slug identifier of the subscription tier. |  [optional] |
|**includedRepositories** | **Integer** | The number of repositories included in the subscription tier. &#x60;0&#x60; indicates that the subscription tier includes unlimited repositories. |  [optional] |
|**includedStorageBytes** | **Integer** | The amount of storage included in the subscription tier in bytes. |  [optional] |
|**allowStorageOverage** | **Boolean** | A boolean indicating whether the subscription tier supports additional storage above what is included in the base plan at an additional cost per GiB used. |  [optional] |
|**includedBandwidthBytes** | **Integer** | The amount of outbound data transfer included in the subscription tier in bytes. |  [optional] |
|**monthlyPriceInCents** | **Integer** | The monthly cost of the subscription tier in cents. |  [optional] |
|**storageOveragePriceInCents** | **Integer** | The price paid in cents per GiB for additional storage beyond what is included in the subscription plan. |  [optional] |
|**eligible** | **Boolean** | A boolean indicating whether your account it eligible to use a certain subscription tier. |  [optional] |
|**eligibilityReasons** | [**List&lt;EligibilityReasonsEnum&gt;**](#List&lt;EligibilityReasonsEnum&gt;) | If your account is not eligible to use a certain subscription tier, this will include a list of reasons that prevent you from using the tier. |  [optional] |



## Enum: List&lt;EligibilityReasonsEnum&gt;

| Name | Value |
|---- | -----|
| OVERREPOSITORYLIMIT | &quot;OverRepositoryLimit&quot; |
| OVERSTORAGELIMIT | &quot;OverStorageLimit&quot; |



