

# CertificateRequestLetsEncrypt


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | A unique human-readable name referring to a certificate. |  |
|**type** | [**TypeEnum**](#TypeEnum) | A string representing the type of the certificate. The value will be &#x60;custom&#x60; for a user-uploaded certificate or &#x60;lets_encrypt&#x60; for one automatically generated with Let&#39;s Encrypt. |  [optional] |
|**dnsNames** | **List&lt;String&gt;** | An array of fully qualified domain names (FQDNs) for which the certificate was issued. A certificate covering all subdomains can be issued using a wildcard (e.g. &#x60;*.example.com&#x60;). |  |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| CUSTOM | &quot;custom&quot; |
| LETS_ENCRYPT | &quot;lets_encrypt&quot; |



