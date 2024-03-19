# ReservedIpActionsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**get**](ReservedIpActionsApi.md#get) | **GET** /v2/reserved_ips/{reserved_ip}/actions/{action_id} | Retrieve an Existing Reserved IP Action |
| [**list**](ReservedIpActionsApi.md#list) | **GET** /v2/reserved_ips/{reserved_ip}/actions | List All Actions for a Reserved IP |
| [**post**](ReservedIpActionsApi.md#post) | **POST** /v2/reserved_ips/{reserved_ip}/actions | Initiate a Reserved IP Action |


<a name="get"></a>
# **get**
> ReservedIPsActionsPostResponse get(reservedIp, actionId).execute();

Retrieve an Existing Reserved IP Action

To retrieve the status of a reserved IP action, send a GET request to &#x60;/v2/reserved_ips/$RESERVED_IP/actions/$ACTION_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ReservedIpActionsApi;
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
    Integer actionId = 36804636; // A unique numeric ID that can be used to identify and reference an action.
    try {
      ReservedIPsActionsPostResponse result = client
              .reservedIpActions
              .get(reservedIp, actionId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAction());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpActionsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ReservedIPsActionsPostResponse> response = client
              .reservedIpActions
              .get(reservedIp, actionId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpActionsApi#get");
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
| **actionId** | **Integer**| A unique numeric ID that can be used to identify and reference an action. | |

### Return type

[**ReservedIPsActionsPostResponse**](ReservedIPsActionsPostResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard reserved IP action attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> ReservedIPsActionsListResponse list(reservedIp).execute();

List All Actions for a Reserved IP

To retrieve all actions that have been executed on a reserved IP, send a GET request to &#x60;/v2/reserved_ips/$RESERVED_IP/actions&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ReservedIpActionsApi;
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
      ReservedIPsActionsListResponse result = client
              .reservedIpActions
              .list(reservedIp)
              .execute();
      System.out.println(result);
      System.out.println(result.getActions());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpActionsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ReservedIPsActionsListResponse> response = client
              .reservedIpActions
              .list(reservedIp)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpActionsApi#list");
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

[**ReservedIPsActionsListResponse**](ReservedIPsActionsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The results will be returned as a JSON object with an &#x60;actions&#x60; key. This will be set to an array filled with action objects containing the standard reserved IP action attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="post"></a>
# **post**
> ReservedIPsActionsPostResponse post(reservedIp).body(body).execute();

Initiate a Reserved IP Action

To initiate an action on a reserved IP send a POST request to &#x60;/v2/reserved_ips/$RESERVED_IP/actions&#x60;. In the JSON body to the request, set the &#x60;type&#x60; attribute to on of the supported action types:  | Action     | Details |------------|-------- | &#x60;assign&#x60;   | Assigns a reserved IP to a Droplet | &#x60;unassign&#x60; | Unassign a reserved IP from a Droplet 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ReservedIpActionsApi;
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
      ReservedIPsActionsPostResponse result = client
              .reservedIpActions
              .post(reservedIp)
              .execute();
      System.out.println(result);
      System.out.println(result.getAction());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpActionsApi#post");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ReservedIPsActionsPostResponse> response = client
              .reservedIpActions
              .post(reservedIp)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ReservedIpActionsApi#post");
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
| **body** | **Object**| The &#x60;type&#x60; attribute set in the request body will specify the action that will be taken on the reserved IP.  | [optional] |

### Return type

[**ReservedIPsActionsPostResponse**](ReservedIPsActionsPostResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard reserved IP action attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

