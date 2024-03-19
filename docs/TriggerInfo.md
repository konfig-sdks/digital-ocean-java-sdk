

# TriggerInfo


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**namespace** | **String** | A unique string format of UUID with a prefix fn-. |  [optional] |
|**name** | **String** | The trigger&#39;s unique name within the namespace. |  [optional] |
|**function** | **String** | Name of function(action) that exists in the given namespace. |  [optional] |
|**type** | **String** | String which indicates the type of trigger source like SCHEDULED. |  [optional] |
|**isEnabled** | **Boolean** | Indicates weather the trigger is paused or unpaused. |  [optional] |
|**createdAt** | **String** | UTC time string. |  [optional] |
|**updatedAt** | **String** | UTC time string. |  [optional] |
|**scheduledDetails** | [**ScheduledDetails**](ScheduledDetails.md) |  |  [optional] |
|**scheduledRuns** | [**TriggerInfoScheduledRuns**](TriggerInfoScheduledRuns.md) |  |  [optional] |



