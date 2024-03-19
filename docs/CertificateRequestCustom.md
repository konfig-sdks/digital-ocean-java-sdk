

# CertificateRequestCustom


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | A unique human-readable name referring to a certificate. |  |
|**type** | [**TypeEnum**](#TypeEnum) | A string representing the type of the certificate. The value will be &#x60;custom&#x60; for a user-uploaded certificate or &#x60;lets_encrypt&#x60; for one automatically generated with Let&#39;s Encrypt. |  [optional] |
|**privateKey** | **String** | The contents of a PEM-formatted private-key corresponding to the SSL certificate. |  |
|**leafCertificate** | **String** | The contents of a PEM-formatted public SSL certificate. |  |
|**certificateChain** | **String** | The full PEM-formatted trust chain between the certificate authority&#39;s certificate and your domain&#39;s SSL certificate. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| CUSTOM | &quot;custom&quot; |
| LETS_ENCRYPT | &quot;lets_encrypt&quot; |



