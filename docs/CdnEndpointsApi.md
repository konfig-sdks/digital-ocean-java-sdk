# CdnEndpointsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createNewEndpoint**](CdnEndpointsApi.md#createNewEndpoint) | **POST** /v2/cdn/endpoints | Create a New CDN Endpoint |
| [**deleteEndpoint**](CdnEndpointsApi.md#deleteEndpoint) | **DELETE** /v2/cdn/endpoints/{cdn_id} | Delete a CDN Endpoint |
| [**getExistingEndpoint**](CdnEndpointsApi.md#getExistingEndpoint) | **GET** /v2/cdn/endpoints/{cdn_id} | Retrieve an Existing CDN Endpoint |
| [**listAll**](CdnEndpointsApi.md#listAll) | **GET** /v2/cdn/endpoints | List All CDN Endpoints |
| [**purgeCache**](CdnEndpointsApi.md#purgeCache) | **DELETE** /v2/cdn/endpoints/{cdn_id}/cache | Purge the Cache for an Existing CDN Endpoint |
| [**updateEndpoint**](CdnEndpointsApi.md#updateEndpoint) | **PUT** /v2/cdn/endpoints/{cdn_id} | Update a CDN Endpoint |


<a name="createNewEndpoint"></a>
# **createNewEndpoint**
> CdnEndpointsCreateNewEndpointResponse createNewEndpoint(cdnEndpoint).execute();

Create a New CDN Endpoint

To create a new CDN endpoint, send a POST request to &#x60;/v2/cdn/endpoints&#x60;. The origin attribute must be set to the fully qualified domain name (FQDN) of a DigitalOcean Space. Optionally, the TTL may be configured by setting the &#x60;ttl&#x60; attribute.  A custom subdomain may be configured by specifying the &#x60;custom_domain&#x60; and &#x60;certificate_id&#x60; attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CdnEndpointsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://api.digitalocean.com";
    
    // Configure HTTP bearer authorization: bearer_auth
    configuration.token = "BEARER TOKEN";
    DigitalOcean client = new DigitalOcean(configuration);
    String origin = "origin_example"; // The fully qualified domain name (FQDN) for the origin server which provides the content for the CDN. This is currently restricted to a Space.
    UUID id = UUID.randomUUID(); // A unique ID that can be used to identify and reference a CDN endpoint.
    String endpoint = "endpoint_example"; // The fully qualified domain name (FQDN) from which the CDN-backed content is served.
    Integer ttl = 60; // The amount of time the content is cached by the CDN's edge servers in seconds. TTL must be one of 60, 600, 3600, 86400, or 604800. Defaults to 3600 (one hour) when excluded.
    UUID certificateId = UUID.randomUUID(); // The ID of a DigitalOcean managed TLS certificate used for SSL when a custom subdomain is provided.
    String customDomain = "customDomain_example"; // The fully qualified domain name (FQDN) of the custom subdomain used with the CDN endpoint.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the CDN endpoint was created.
    try {
      CdnEndpointsCreateNewEndpointResponse result = client
              .cdnEndpoints
              .createNewEndpoint(origin)
              .id(id)
              .endpoint(endpoint)
              .ttl(ttl)
              .certificateId(certificateId)
              .customDomain(customDomain)
              .createdAt(createdAt)
              .execute();
      System.out.println(result);
      System.out.println(result.getEndpoint());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#createNewEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CdnEndpointsCreateNewEndpointResponse> response = client
              .cdnEndpoints
              .createNewEndpoint(origin)
              .id(id)
              .endpoint(endpoint)
              .ttl(ttl)
              .certificateId(certificateId)
              .customDomain(customDomain)
              .createdAt(createdAt)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#createNewEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cdnEndpoint** | [**CdnEndpoint**](CdnEndpoint.md)|  | |

### Return type

[**CdnEndpointsCreateNewEndpointResponse**](CdnEndpointsCreateNewEndpointResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteEndpoint"></a>
# **deleteEndpoint**
> deleteEndpoint(cdnId).execute();

Delete a CDN Endpoint

To delete a specific CDN endpoint, send a DELETE request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CdnEndpointsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://api.digitalocean.com";
    
    // Configure HTTP bearer authorization: bearer_auth
    configuration.token = "BEARER TOKEN";
    DigitalOcean client = new DigitalOcean(configuration);
    UUID cdnId = UUID.fromString("19f06b6a-3ace-4315-b086-499a0e521b76"); // A unique identifier for a CDN endpoint.
    try {
      client
              .cdnEndpoints
              .deleteEndpoint(cdnId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#deleteEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .cdnEndpoints
              .deleteEndpoint(cdnId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#deleteEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cdnId** | **UUID**| A unique identifier for a CDN endpoint. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getExistingEndpoint"></a>
# **getExistingEndpoint**
> CdnEndpointsCreateNewEndpointResponse getExistingEndpoint(cdnId).execute();

Retrieve an Existing CDN Endpoint

To show information about an existing CDN endpoint, send a GET request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CdnEndpointsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://api.digitalocean.com";
    
    // Configure HTTP bearer authorization: bearer_auth
    configuration.token = "BEARER TOKEN";
    DigitalOcean client = new DigitalOcean(configuration);
    UUID cdnId = UUID.fromString("19f06b6a-3ace-4315-b086-499a0e521b76"); // A unique identifier for a CDN endpoint.
    try {
      CdnEndpointsCreateNewEndpointResponse result = client
              .cdnEndpoints
              .getExistingEndpoint(cdnId)
              .execute();
      System.out.println(result);
      System.out.println(result.getEndpoint());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#getExistingEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CdnEndpointsCreateNewEndpointResponse> response = client
              .cdnEndpoints
              .getExistingEndpoint(cdnId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#getExistingEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cdnId** | **UUID**| A unique identifier for a CDN endpoint. | |

### Return type

[**CdnEndpointsCreateNewEndpointResponse**](CdnEndpointsCreateNewEndpointResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listAll"></a>
# **listAll**
> CdnEndpointsListAllResponse listAll().perPage(perPage).page(page).execute();

List All CDN Endpoints

To list all of the CDN endpoints available on your account, send a GET request to &#x60;/v2/cdn/endpoints&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CdnEndpointsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://api.digitalocean.com";
    
    // Configure HTTP bearer authorization: bearer_auth
    configuration.token = "BEARER TOKEN";
    DigitalOcean client = new DigitalOcean(configuration);
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      CdnEndpointsListAllResponse result = client
              .cdnEndpoints
              .listAll()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getEndpoints());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#listAll");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CdnEndpointsListAllResponse> response = client
              .cdnEndpoints
              .listAll()
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#listAll");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**CdnEndpointsListAllResponse**](CdnEndpointsListAllResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The result will be a JSON object with an &#x60;endpoints&#x60; key. This will be set to an array of endpoint objects, each of which will contain the standard CDN endpoint attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="purgeCache"></a>
# **purgeCache**
> purgeCache(cdnId, purgeCache).execute();

Purge the Cache for an Existing CDN Endpoint

To purge cached content from a CDN endpoint, send a DELETE request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID/cache&#x60;. The body of the request should include a &#x60;files&#x60; attribute containing a list of cached file paths to be purged. A path may be for a single file or may contain a wildcard (&#x60;*&#x60;) to recursively purge all files under a directory. When only a wildcard is provided, all cached files will be purged. There is a rate limit of 50 files per 20 seconds  that can be purged. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CdnEndpointsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://api.digitalocean.com";
    
    // Configure HTTP bearer authorization: bearer_auth
    configuration.token = "BEARER TOKEN";
    DigitalOcean client = new DigitalOcean(configuration);
    List<String> files = Arrays.asList(); // An array of strings containing the path to the content to be purged from the CDN cache.
    UUID cdnId = UUID.fromString("19f06b6a-3ace-4315-b086-499a0e521b76"); // A unique identifier for a CDN endpoint.
    try {
      client
              .cdnEndpoints
              .purgeCache(files, cdnId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#purgeCache");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .cdnEndpoints
              .purgeCache(files, cdnId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#purgeCache");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cdnId** | **UUID**| A unique identifier for a CDN endpoint. | |
| **purgeCache** | [**PurgeCache**](PurgeCache.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateEndpoint"></a>
# **updateEndpoint**
> CdnEndpointsCreateNewEndpointResponse updateEndpoint(cdnId, updateEndpoint).execute();

Update a CDN Endpoint

To update the TTL, certificate ID, or the FQDN of the custom subdomain for an existing CDN endpoint, send a PUT request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.CdnEndpointsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://api.digitalocean.com";
    
    // Configure HTTP bearer authorization: bearer_auth
    configuration.token = "BEARER TOKEN";
    DigitalOcean client = new DigitalOcean(configuration);
    UUID cdnId = UUID.fromString("19f06b6a-3ace-4315-b086-499a0e521b76"); // A unique identifier for a CDN endpoint.
    Integer ttl = 60; // The amount of time the content is cached by the CDN's edge servers in seconds. TTL must be one of 60, 600, 3600, 86400, or 604800. Defaults to 3600 (one hour) when excluded.
    UUID certificateId = UUID.randomUUID(); // The ID of a DigitalOcean managed TLS certificate used for SSL when a custom subdomain is provided.
    String customDomain = "customDomain_example"; // The fully qualified domain name (FQDN) of the custom subdomain used with the CDN endpoint.
    try {
      CdnEndpointsCreateNewEndpointResponse result = client
              .cdnEndpoints
              .updateEndpoint(cdnId)
              .ttl(ttl)
              .certificateId(certificateId)
              .customDomain(customDomain)
              .execute();
      System.out.println(result);
      System.out.println(result.getEndpoint());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#updateEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<CdnEndpointsCreateNewEndpointResponse> response = client
              .cdnEndpoints
              .updateEndpoint(cdnId)
              .ttl(ttl)
              .certificateId(certificateId)
              .customDomain(customDomain)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling CdnEndpointsApi#updateEndpoint");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **cdnId** | **UUID**| A unique identifier for a CDN endpoint. | |
| **updateEndpoint** | [**UpdateEndpoint**](UpdateEndpoint.md)|  | |

### Return type

[**CdnEndpointsCreateNewEndpointResponse**](CdnEndpointsCreateNewEndpointResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

