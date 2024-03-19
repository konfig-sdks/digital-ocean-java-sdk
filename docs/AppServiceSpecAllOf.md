

# AppServiceSpecAllOf


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cors** | [**AppsCorsPolicy**](AppsCorsPolicy.md) |  |  [optional] |
|**healthCheck** | [**AppServiceSpecHealthCheck**](AppServiceSpecHealthCheck.md) |  |  [optional] |
|**httpPort** | **Long** | The internal port on which this service&#39;s run command will listen. Default: 8080 If there is not an environment variable with the name &#x60;PORT&#x60;, one will be automatically added with its value set to the value of this field. |  [optional] |
|**internalPorts** | **List&lt;Long&gt;** | The ports on which this service will listen for internal traffic. |  [optional] |
|**routes** | [**List&lt;AppRouteSpec&gt;**](AppRouteSpec.md) | (Deprecated - Use Ingress Rules instead). A list of HTTP routes that should be routed to this component. |  [optional] |



