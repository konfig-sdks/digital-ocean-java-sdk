

# RegistryCreate


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | A globally unique name for the container registry. Must be lowercase and be composed only of numbers, letters and &#x60;-&#x60;, up to a limit of 63 characters. |  |
|**subscriptionTierSlug** | [**SubscriptionTierSlugEnum**](#SubscriptionTierSlugEnum) | The slug of the subscription tier to sign up for. Valid values can be retrieved using the options endpoint. |  |
|**region** | [**RegionEnum**](#RegionEnum) | Slug of the region where registry data is stored. When not provided, a region will be selected. |  [optional] |



## Enum: SubscriptionTierSlugEnum

| Name | Value |
|---- | -----|
| STARTER | &quot;starter&quot; |
| BASIC | &quot;basic&quot; |
| PROFESSIONAL | &quot;professional&quot; |



## Enum: RegionEnum

| Name | Value |
|---- | -----|
| NYC3 | &quot;nyc3&quot; |
| SFO3 | &quot;sfo3&quot; |
| AMS3 | &quot;ams3&quot; |
| SGP1 | &quot;sgp1&quot; |
| FRA1 | &quot;fra1&quot; |



