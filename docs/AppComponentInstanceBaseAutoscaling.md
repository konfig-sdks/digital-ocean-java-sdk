

# AppComponentInstanceBaseAutoscaling

Configuration for automatically scaling this component based on metrics.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**minInstanceCount** | **Integer** | The minimum amount of instances for this component. Must be less than max_instance_count. |  [optional] |
|**maxInstanceCount** | **Integer** | The maximum amount of instances for this component. Must be more than min_instance_count. |  [optional] |
|**metrics** | [**AppComponentInstanceBaseAutoscalingMetrics**](AppComponentInstanceBaseAutoscalingMetrics.md) |  |  [optional] |



