

# CdnEndpoint


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **UUID** | A unique ID that can be used to identify and reference a CDN endpoint. |  [optional] [readonly] |
|**origin** | **String** | The fully qualified domain name (FQDN) for the origin server which provides the content for the CDN. This is currently restricted to a Space. |  |
|**endpoint** | **String** | The fully qualified domain name (FQDN) from which the CDN-backed content is served. |  [optional] [readonly] |
|**ttl** | [**TtlEnum**](#TtlEnum) | The amount of time the content is cached by the CDN&#39;s edge servers in seconds. TTL must be one of 60, 600, 3600, 86400, or 604800. Defaults to 3600 (one hour) when excluded. |  [optional] |
|**certificateId** | **UUID** | The ID of a DigitalOcean managed TLS certificate used for SSL when a custom subdomain is provided. |  [optional] |
|**customDomain** | **String** | The fully qualified domain name (FQDN) of the custom subdomain used with the CDN endpoint. |  [optional] |
|**createdAt** | **OffsetDateTime** | A time value given in ISO8601 combined date and time format that represents when the CDN endpoint was created. |  [optional] [readonly] |



## Enum: TtlEnum

| Name | Value |
|---- | -----|
| NUMBER_60 | 60 |
| NUMBER_600 | 600 |
| NUMBER_3600 | 3600 |
| NUMBER_86400 | 86400 |
| NUMBER_604800 | 604800 |



