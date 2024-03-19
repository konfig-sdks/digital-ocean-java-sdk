

# AppDomainSpec


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**domain** | **String** | The hostname for the domain |  |
|**type** | [**TypeEnum**](#TypeEnum) | - DEFAULT: The default &#x60;.ondigitalocean.app&#x60; domain assigned to this app - PRIMARY: The primary domain for this app that is displayed as the default in the control panel, used in bindable environment variables, and any other places that reference an app&#39;s live URL. Only one domain may be set as primary. - ALIAS: A non-primary domain |  [optional] |
|**wildcard** | **Boolean** | Indicates whether the domain includes all sub-domains, in addition to the given domain |  [optional] |
|**zone** | **String** | Optional. If the domain uses DigitalOcean DNS and you would like App Platform to automatically manage it for you, set this to the name of the domain on your account.  For example, If the domain you are adding is &#x60;app.domain.com&#x60;, the zone could be &#x60;domain.com&#x60;. |  [optional] |
|**minimumTlsVersion** | [**MinimumTlsVersionEnum**](#MinimumTlsVersionEnum) | The minimum version of TLS a client application can use to access resources for the domain.  Must be one of the following values wrapped within quotations: &#x60;\&quot;1.2\&quot;&#x60; or &#x60;\&quot;1.3\&quot;&#x60;. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| UNSPECIFIED | &quot;UNSPECIFIED&quot; |
| DEFAULT | &quot;DEFAULT&quot; |
| PRIMARY | &quot;PRIMARY&quot; |
| ALIAS | &quot;ALIAS&quot; |



## Enum: MinimumTlsVersionEnum

| Name | Value |
|---- | -----|
| _2 | &quot;1.2&quot; |
| _3 | &quot;1.3&quot; |



