

# ClusterlintResults


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**runId** | **String** | Id of the clusterlint run that can be used later to fetch the diagnostics. |  [optional] |
|**requestedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the schedule clusterlint run request was made. |  [optional] |
|**completedAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the schedule clusterlint run request was completed. |  [optional] |
|**diagnostics** | [**List&lt;ClusterlintResultsDiagnosticsInner&gt;**](ClusterlintResultsDiagnosticsInner.md) | An array of diagnostics reporting potential problems for the given cluster. |  [optional] |



