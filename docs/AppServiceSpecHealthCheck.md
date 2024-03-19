

# AppServiceSpecHealthCheck


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**failureThreshold** | **Integer** | The number of failed health checks before considered unhealthy. |  [optional] |
|**port** | **Long** | The port on which the health check will be performed. If not set, the health check will be performed on the component&#39;s http_port. |  [optional] |
|**httpPath** | **String** | The route path used for the HTTP health check ping. If not set, the HTTP health check will be disabled and a TCP health check used instead. |  [optional] |
|**initialDelaySeconds** | **Integer** | The number of seconds to wait before beginning health checks. |  [optional] |
|**periodSeconds** | **Integer** | The number of seconds to wait between health checks. |  [optional] |
|**successThreshold** | **Integer** | The number of successful health checks before considered healthy. |  [optional] |
|**timeoutSeconds** | **Integer** | The number of seconds after which the check times out. |  [optional] |



