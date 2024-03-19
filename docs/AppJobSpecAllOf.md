

# AppJobSpecAllOf


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**kind** | [**KindEnum**](#KindEnum) | - UNSPECIFIED: Default job type, will auto-complete to POST_DEPLOY kind. - PRE_DEPLOY: Indicates a job that runs before an app deployment. - POST_DEPLOY: Indicates a job that runs after an app deployment. - FAILED_DEPLOY: Indicates a job that runs after a component fails to deploy. |  [optional] |



## Enum: KindEnum

| Name | Value |
|---- | -----|
| UNSPECIFIED | &quot;UNSPECIFIED&quot; |
| PRE_DEPLOY | &quot;PRE_DEPLOY&quot; |
| POST_DEPLOY | &quot;POST_DEPLOY&quot; |
| FAILED_DEPLOY | &quot;FAILED_DEPLOY&quot; |



