

# Certificate


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | A unique ID that can be used to identify and reference a certificate. |  [optional] [readonly] |
|**name** | **String** | A unique human-readable name referring to a certificate. |  [optional] |
|**notAfter** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents the certificate&#39;s expiration date. |  [optional] [readonly] |
|**sha1Fingerprint** | **String** | A unique identifier generated from the SHA-1 fingerprint of the certificate. |  [optional] [readonly] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the certificate was created. |  [optional] [readonly] |
|**dnsNames** | **List&lt;String&gt;** | An array of fully qualified domain names (FQDNs) for which the certificate was issued. |  [optional] |
|**state** | [**StateEnum**](#StateEnum) | A string representing the current state of the certificate. It may be &#x60;pending&#x60;, &#x60;verified&#x60;, or &#x60;error&#x60;. |  [optional] [readonly] |
|**type** | [**TypeEnum**](#TypeEnum) | A string representing the type of the certificate. The value will be &#x60;custom&#x60; for a user-uploaded certificate or &#x60;lets_encrypt&#x60; for one automatically generated with Let&#39;s Encrypt. |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| PENDING | &quot;pending&quot; |
| VERIFIED | &quot;verified&quot; |
| ERROR | &quot;error&quot; |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| CUSTOM | &quot;custom&quot; |
| LETS_ENCRYPT | &quot;lets_encrypt&quot; |



