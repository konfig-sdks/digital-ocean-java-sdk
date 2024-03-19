

# AppVariableDefinition


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**key** | **String** | The variable name |  |
|**scope** | [**ScopeEnum**](#ScopeEnum) | - RUN_TIME: Made available only at run-time - BUILD_TIME: Made available only at build-time - RUN_AND_BUILD_TIME: Made available at both build and run-time |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | - GENERAL: A plain-text environment variable - SECRET: A secret encrypted environment variable |  [optional] |
|**value** | **String** | The value. If the type is &#x60;SECRET&#x60;, the value will be encrypted on first submission. On following submissions, the encrypted value should be used. |  [optional] |



## Enum: ScopeEnum

| Name | Value |
|---- | -----|
| UNSET | &quot;UNSET&quot; |
| RUN_TIME | &quot;RUN_TIME&quot; |
| BUILD_TIME | &quot;BUILD_TIME&quot; |
| RUN_AND_BUILD_TIME | &quot;RUN_AND_BUILD_TIME&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| GENERAL | &quot;GENERAL&quot; |
| SECRET | &quot;SECRET&quot; |



