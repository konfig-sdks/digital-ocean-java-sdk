

# NamespaceInfo


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**apiHost** | **String** | The namespace&#39;s API hostname. Each function in a namespace is provided an endpoint at the namespace&#39;s hostname. |  [optional] |
|**namespace** | **String** | A unique string format of UUID with a prefix fn-. |  [optional] |
|**createdAt** | **String** | UTC time string. |  [optional] |
|**updatedAt** | **String** | UTC time string. |  [optional] |
|**label** | **String** | The namespace&#39;s unique name. |  [optional] |
|**region** | **String** | The namespace&#39;s datacenter region. |  [optional] |
|**uuid** | **String** | The namespace&#39;s Universally Unique Identifier. |  [optional] |
|**key** | **String** | A random alpha numeric string. This key is used in conjunction with the namespace&#39;s UUID to authenticate  a user to use the namespace via &#x60;doctl&#x60;, DigitalOcean&#39;s official CLI. |  [optional] |



