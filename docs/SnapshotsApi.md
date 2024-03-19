# SnapshotsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**delete**](SnapshotsApi.md#delete) | **DELETE** /v2/snapshots/{snapshot_id} | Delete a Snapshot |
| [**get**](SnapshotsApi.md#get) | **GET** /v2/snapshots/{snapshot_id} | Retrieve an Existing Snapshot |
| [**list**](SnapshotsApi.md#list) | **GET** /v2/snapshots | List All Snapshots |


<a name="delete"></a>
# **delete**
> delete(snapshotId).execute();

Delete a Snapshot

Both Droplet and volume snapshots are managed through the &#x60;/v2/snapshots/&#x60; endpoint. To delete a snapshot, send a DELETE request to &#x60;/v2/snapshots/$SNAPSHOT_ID&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SnapshotsApi;
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
    Object snapshotId = 6372321; // Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot.
    try {
      client
              .snapshots
              .delete(snapshotId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SnapshotsApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .snapshots
              .delete(snapshotId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling SnapshotsApi#delete");
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
| **snapshotId** | [**Object**](.md)| Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot. | |

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
> Object get(snapshotId).execute();

Retrieve an Existing Snapshot

To retrieve information about a snapshot, send a GET request to &#x60;/v2/snapshots/$SNAPSHOT_ID&#x60;.  The response will be a JSON object with a key called &#x60;snapshot&#x60;. The value of this will be an snapshot object containing the standard snapshot attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SnapshotsApi;
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
    Object snapshotId = 6372321; // Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot.
    try {
      Object result = client
              .snapshots
              .get(snapshotId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SnapshotsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .snapshots
              .get(snapshotId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SnapshotsApi#get");
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
| **snapshotId** | [**Object**](.md)| Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot. | |

### Return type

**Object**

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key called &#x60;snapshot&#x60;.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> SnapshotsListResponse list().perPage(perPage).page(page).resourceType(resourceType).execute();

List All Snapshots

To list all of the snapshots available on your account, send a GET request to &#x60;/v2/snapshots&#x60;.  The response will be a JSON object with a key called &#x60;snapshots&#x60;. This will be set to an array of &#x60;snapshot&#x60; objects, each of which will contain the standard snapshot attributes.  ### Filtering Results by Resource Type  It&#39;s possible to request filtered results by including certain query parameters.  #### List Droplet Snapshots  To retrieve only snapshots based on Droplets, include the &#x60;resource_type&#x60; query parameter set to &#x60;droplet&#x60;. For example, &#x60;/v2/snapshots?resource_type&#x3D;droplet&#x60;.  #### List Volume Snapshots  To retrieve only snapshots based on volumes, include the &#x60;resource_type&#x60; query parameter set to &#x60;volume&#x60;. For example, &#x60;/v2/snapshots?resource_type&#x3D;volume&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SnapshotsApi;
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
    String resourceType = "droplet"; // Used to filter snapshots by a resource type.
    try {
      SnapshotsListResponse result = client
              .snapshots
              .list()
              .perPage(perPage)
              .page(page)
              .resourceType(resourceType)
              .execute();
      System.out.println(result);
      System.out.println(result.getSnapshots());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling SnapshotsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SnapshotsListResponse> response = client
              .snapshots
              .list()
              .perPage(perPage)
              .page(page)
              .resourceType(resourceType)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SnapshotsApi#list");
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
| **resourceType** | **String**| Used to filter snapshots by a resource type. | [optional] [enum: droplet, volume] |

### Return type

[**SnapshotsListResponse**](SnapshotsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;snapshots&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

