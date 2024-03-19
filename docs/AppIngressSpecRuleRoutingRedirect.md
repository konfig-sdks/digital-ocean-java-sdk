

# AppIngressSpecRuleRoutingRedirect

The redirect configuration for the rule. Only one of `component` or `redirect` may be set.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**uri** | **String** | An optional URI path to redirect to. Note: if this is specified the whole URI of the original request will be overwritten to this value, irrespective of the original request URI being matched. |  [optional] |
|**authority** | **String** | The authority/host to redirect to. This can be a hostname or IP address. Note: use &#x60;port&#x60; to set the port. |  [optional] |
|**port** | **Long** | The port to redirect to. |  [optional] |
|**scheme** | **String** | The scheme to redirect to. Supported values are &#x60;http&#x60; or &#x60;https&#x60;. Default: &#x60;https&#x60;. |  [optional] |
|**redirectCode** | **Long** | The redirect code to use. Defaults to &#x60;302&#x60;. Supported values are 300, 301, 302, 303, 304, 307, 308. |  [optional] |



