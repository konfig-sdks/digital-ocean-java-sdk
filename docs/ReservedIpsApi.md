# ReservedIpsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create**](ReservedIpsApi.md#create) | **POST** /v2/reserved_ips | Create a New Reserved IP |
| [**delete**](ReservedIpsApi.md#delete) | **DELETE** /v2/reserved_ips/{reserved_ip} | Delete a Reserved IP |
| [**get**](ReservedIpsApi.md#get) | **GET** /v2/reserved_ips/{reserved_ip} | Retrieve an Existing Reserved IP |
| [**list**](ReservedIpsApi.md#list) | **GET** /v2/reserved_ips | List All Reserved IPs |


<a name="create"></a>
# **create**
> ReservedIPsCreateResponse create(body).execute();

Create a New Reserved IP

On creation, a reserved IP must be either assigned to a Droplet or reserved to a region. * To create a new reserved IP assigned to a Droplet, send a POST   request to &#x60;/v2/reserved_ips&#x60; with the &#x60;droplet_id&#x60; attribute.  * To create a new reserved IP reserved to a region, send a POST request to   &#x60;/v2/reserved_ips&#x60; with the &#x60;region&#x60; attribute.  **Note**:  In addition to the standard rate limiting, only 12 reserved IPs may be created per 60 seconds.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ReservedIpsApi;
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
    try {
      ReservedIPsCreateResponse result = client
              .reservedIps
              .create()
              .execute();
      System.out.println(result);
      System.out.println(result.getReservedIp());
      System.out.println(result.getLinks());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ReservedIPsCreateResponse> response = client
              .reservedIps
              .create()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#create");
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
| **body** | **Object**|  | |

### Return type

[**ReservedIPsCreateResponse**](ReservedIPsCreateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The response will be a JSON object with a key called &#x60;reserved_ip&#x60;. The value of this will be an object that contains the standard attributes associated with a reserved IP. When assigning a reserved IP to a Droplet at same time as it created, the response&#39;s &#x60;links&#x60; object will contain links to both the Droplet and the assignment action. The latter can be used to check the status of the action. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> delete(reservedIp).execute();

Delete a Reserved IP

To delete a reserved IP and remove it from your account, send a DELETE request to &#x60;/v2/reserved_ips/$RESERVED_IP_ADDR&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ReservedIpsApi;
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
    String reservedIp = "45.55.96.47"; // A reserved IP address.
    try {
      client
              .reservedIps
              .delete(reservedIp)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .reservedIps
              .delete(reservedIp)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#delete");
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
| **reservedIp** | **String**| A reserved IP address. | |

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

<a name="get"></a>
# **get**
> ReservedIPsGetResponse get(reservedIp).execute();

Retrieve an Existing Reserved IP

To show information about a reserved IP, send a GET request to &#x60;/v2/reserved_ips/$RESERVED_IP_ADDR&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ReservedIpsApi;
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
    String reservedIp = "45.55.96.47"; // A reserved IP address.
    try {
      ReservedIPsGetResponse result = client
              .reservedIps
              .get(reservedIp)
              .execute();
      System.out.println(result);
      System.out.println(result.getReservedIp());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ReservedIPsGetResponse> response = client
              .reservedIps
              .get(reservedIp)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#get");
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
| **reservedIp** | **String**| A reserved IP address. | |

### Return type

[**ReservedIPsGetResponse**](ReservedIPsGetResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;reserved_ip&#x60;. The value of this will be an object that contains the standard attributes associated with a reserved IP. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> ReservedIPsListResponse list().perPage(perPage).page(page).execute();

List All Reserved IPs

To list all of the reserved IPs available on your account, send a GET request to &#x60;/v2/reserved_ips&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ReservedIpsApi;
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
      ReservedIPsListResponse result = client
              .reservedIps
              .list()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getReservedIps());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ReservedIPsListResponse> response = client
              .reservedIps
              .list()
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpsApi#list");
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

[**ReservedIPsListResponse**](ReservedIPsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;reserved_ips&#x60;. This will be set to an array of reserved IP objects, each of which will contain the standard reserved IP attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

