

# AppComponentInstanceBase


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**instanceCount** | **Long** | The amount of instances that this component should be scaled to. Default: 1. Must not be set if autoscaling is used. |  [optional] |
|**instanceSizeSlug** | [**InstanceSizeSlugEnum**](#InstanceSizeSlugEnum) | The instance size to use for this component. Default: &#x60;basic-xxs&#x60; |  [optional] |
|**autoscaling** | [**AppComponentInstanceBaseAutoscaling**](AppComponentInstanceBaseAutoscaling.md) |  |  [optional] |



## Enum: InstanceSizeSlugEnum

| Name | Value |
|---- | -----|
| BASIC_XXS | &quot;basic-xxs&quot; |
| BASIC_XS | &quot;basic-xs&quot; |
| BASIC_S | &quot;basic-s&quot; |
| BASIC_M | &quot;basic-m&quot; |
| PROFESSIONAL_XS | &quot;professional-xs&quot; |
| PROFESSIONAL_S | &quot;professional-s&quot; |
| PROFESSIONAL_M | &quot;professional-m&quot; |
| PROFESSIONAL_1L | &quot;professional-1l&quot; |
| PROFESSIONAL_L | &quot;professional-l&quot; |
| PROFESSIONAL_XL | &quot;professional-xl&quot; |



