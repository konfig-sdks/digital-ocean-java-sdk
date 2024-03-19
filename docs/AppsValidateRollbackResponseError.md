

# AppsValidateRollbackResponseError


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**components** | **List&lt;String&gt;** |  |  [optional] |
|**code** | [**CodeEnum**](#CodeEnum) | A code identifier that represents the failing condition.  Failing conditions:   - &#x60;incompatible_phase&#x60; - indicates that the deployment&#39;s phase is not suitable for rollback.   - &#x60;incompatible_result&#x60; - indicates that the deployment&#39;s result is not suitable for rollback.   - &#x60;exceeded_revision_limit&#x60; - indicates that the app has exceeded the rollback revision limits for its tier.   - &#x60;app_pinned&#x60; - indicates that there is already a rollback in progress and the app is pinned.   - &#x60;database_config_conflict&#x60; - indicates that the deployment&#39;s database config is different than the current config.   - &#x60;region_conflict&#x60; - indicates that the deployment&#39;s region differs from the current app region.     Warning conditions:   - &#x60;static_site_requires_rebuild&#x60; - indicates that the deployment contains at least one static site that will require a rebuild.   - &#x60;image_source_missing_digest&#x60; - indicates that the deployment contains at least one component with an image source that is missing a digest.  |  [optional] |
|**message** | **String** | A human-readable message describing the failing condition. |  [optional] |



## Enum: CodeEnum

| Name | Value |
|---- | -----|
| INCOMPATIBLE_PHASE | &quot;incompatible_phase&quot; |
| INCOMPATIBLE_RESULT | &quot;incompatible_result&quot; |
| EXCEEDED_REVISION_LIMIT | &quot;exceeded_revision_limit&quot; |
| APP_PINNED | &quot;app_pinned&quot; |
| DATABASE_CONFIG_CONFLICT | &quot;database_config_conflict&quot; |
| REGION_CONFLICT | &quot;region_conflict&quot; |
| STATIC_SITE_REQUIRES_REBUILD | &quot;static_site_requires_rebuild&quot; |
| IMAGE_SOURCE_MISSING_DIGEST | &quot;image_source_missing_digest&quot; |



