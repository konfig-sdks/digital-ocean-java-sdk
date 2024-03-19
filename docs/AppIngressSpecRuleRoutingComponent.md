

# AppIngressSpecRuleRoutingComponent

The component to route to. Only one of `component` or `redirect` may be set.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name of the component to route to. |  |
|**preservePathPrefix** | **String** | An optional flag to preserve the path that is forwarded to the backend service. By default, the HTTP request path will be trimmed from the left when forwarded to the component. For example, a component with &#x60;path&#x3D;/api&#x60; will have requests to &#x60;/api/list&#x60; trimmed to &#x60;/list&#x60;. If this value is &#x60;true&#x60;, the path will remain &#x60;/api/list&#x60;. Note: this is not applicable for Functions Components and is mutually exclusive with &#x60;rewrite&#x60;. |  [optional] |
|**rewrite** | **String** | An optional field that will rewrite the path of the component to be what is specified here. By default, the HTTP request path will be trimmed from the left when forwarded to the component. For example, a component with &#x60;path&#x3D;/api&#x60; will have requests to &#x60;/api/list&#x60; trimmed to &#x60;/list&#x60;. If you specified the rewrite to be &#x60;/v1/&#x60;, requests to &#x60;/api/list&#x60; would be rewritten to &#x60;/v1/list&#x60;. Note: this is mutually exclusive with &#x60;preserve_path_prefix&#x60;. |  [optional] |



