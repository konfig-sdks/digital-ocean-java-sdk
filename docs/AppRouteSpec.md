

# AppRouteSpec


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**path** | **String** | (Deprecated - Use Ingress Rules instead). An HTTP path prefix. Paths must start with / and must be unique across all components within an app. |  [optional] |
|**preservePathPrefix** | **Boolean** | An optional flag to preserve the path that is forwarded to the backend service. By default, the HTTP request path will be trimmed from the left when forwarded to the component. For example, a component with &#x60;path&#x3D;/api&#x60; will have requests to &#x60;/api/list&#x60; trimmed to &#x60;/list&#x60;. If this value is &#x60;true&#x60;, the path will remain &#x60;/api/list&#x60;. |  [optional] |



