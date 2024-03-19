# ImageActionsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**get**](ImageActionsApi.md#get) | **GET** /v2/images/{image_id}/actions/{action_id} | Retrieve an Existing Action |
| [**list**](ImageActionsApi.md#list) | **GET** /v2/images/{image_id}/actions | List All Actions for an Image |
| [**post**](ImageActionsApi.md#post) | **POST** /v2/images/{image_id}/actions | Initiate an Image Action |


<a name="get"></a>
# **get**
> Action get(imageId, actionId).execute();

Retrieve an Existing Action

To retrieve the status of an image action, send a GET request to &#x60;/v2/images/$IMAGE_ID/actions/$IMAGE_ACTION_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImageActionsApi;
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
    Integer imageId = 62137902; // A unique number that can be used to identify and reference a specific image.
    Integer actionId = 36804636; // A unique numeric ID that can be used to identify and reference an action.
    try {
      Action result = client
              .imageActions
              .get(imageId, actionId)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getStatus());
      System.out.println(result.getType());
      System.out.println(result.getStartedAt());
      System.out.println(result.getCompletedAt());
      System.out.println(result.getResourceId());
      System.out.println(result.getResourceType());
      System.out.println(result.getRegion());
      System.out.println(result.getRegionSlug());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImageActionsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Action> response = client
              .imageActions
              .get(imageId, actionId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImageActionsApi#get");
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
| **imageId** | **Integer**| A unique number that can be used to identify and reference a specific image. | |
| **actionId** | **Integer**| A unique numeric ID that can be used to identify and reference an action. | |

### Return type

[**Action**](Action.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard image action attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> ImageActionsListResponse list(imageId).execute();

List All Actions for an Image

To retrieve all actions that have been executed on an image, send a GET request to &#x60;/v2/images/$IMAGE_ID/actions&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImageActionsApi;
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
    Integer imageId = 62137902; // A unique number that can be used to identify and reference a specific image.
    try {
      ImageActionsListResponse result = client
              .imageActions
              .list(imageId)
              .execute();
      System.out.println(result);
      System.out.println(result.getActions());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImageActionsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ImageActionsListResponse> response = client
              .imageActions
              .list(imageId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImageActionsApi#list");
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
| **imageId** | **Integer**| A unique number that can be used to identify and reference a specific image. | |

### Return type

[**ImageActionsListResponse**](ImageActionsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The results will be returned as a JSON object with an &#x60;actions&#x60; key. This will be set to an array filled with action objects containing the standard action attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="post"></a>
# **post**
> Action post(imageId).body(body).execute();

Initiate an Image Action

The following actions are available on an Image.  ## Convert an Image to a Snapshot  To convert an image, for example, a backup to a snapshot, send a POST request to &#x60;/v2/images/$IMAGE_ID/actions&#x60;. Set the &#x60;type&#x60; attribute to &#x60;convert&#x60;.  ## Transfer an Image  To transfer an image to another region, send a POST request to &#x60;/v2/images/$IMAGE_ID/actions&#x60;. Set the &#x60;type&#x60; attribute to &#x60;transfer&#x60; and set &#x60;region&#x60; attribute to the slug identifier of the region you wish to transfer to. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImageActionsApi;
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
    Integer imageId = 62137902; // A unique number that can be used to identify and reference a specific image.
    try {
      Action result = client
              .imageActions
              .post(imageId)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getStatus());
      System.out.println(result.getType());
      System.out.println(result.getStartedAt());
      System.out.println(result.getCompletedAt());
      System.out.println(result.getResourceId());
      System.out.println(result.getResourceType());
      System.out.println(result.getRegion());
      System.out.println(result.getRegionSlug());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImageActionsApi#post");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Action> response = client
              .imageActions
              .post(imageId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImageActionsApi#post");
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
| **imageId** | **Integer**| A unique number that can be used to identify and reference a specific image. | |
| **body** | **Object**|  | [optional] |

### Return type

[**Action**](Action.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with a key called &#x60;action&#x60;. The value of this will be an object containing the standard image action attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

