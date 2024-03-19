# DropletsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**checkDestroyStatus**](DropletsApi.md#checkDestroyStatus) | **GET** /v2/droplets/{droplet_id}/destroy_with_associated_resources/status | Check Status of a Droplet Destroy with Associated Resources Request |
| [**create**](DropletsApi.md#create) | **POST** /v2/droplets | Create a New Droplet |
| [**deleteByTag**](DropletsApi.md#deleteByTag) | **DELETE** /v2/droplets | Deleting Droplets by Tag |
| [**deleteDangerous**](DropletsApi.md#deleteDangerous) | **DELETE** /v2/droplets/{droplet_id}/destroy_with_associated_resources/dangerous | Destroy a Droplet and All of its Associated Resources (Dangerous) |
| [**destroy**](DropletsApi.md#destroy) | **DELETE** /v2/droplets/{droplet_id} | Delete an Existing Droplet |
| [**destroyAssociatedResourcesSelective**](DropletsApi.md#destroyAssociatedResourcesSelective) | **DELETE** /v2/droplets/{droplet_id}/destroy_with_associated_resources/selective | Selectively Destroy a Droplet and its Associated Resources |
| [**get**](DropletsApi.md#get) | **GET** /v2/droplets/{droplet_id} | Retrieve an Existing Droplet |
| [**list**](DropletsApi.md#list) | **GET** /v2/droplets | List All Droplets |
| [**listAssociatedResources**](DropletsApi.md#listAssociatedResources) | **GET** /v2/droplets/{droplet_id}/destroy_with_associated_resources | List Associated Resources for a Droplet |
| [**listBackups**](DropletsApi.md#listBackups) | **GET** /v2/droplets/{droplet_id}/backups | List Backups for a Droplet |
| [**listDropletNeighbors**](DropletsApi.md#listDropletNeighbors) | **GET** /v2/reports/droplet_neighbors_ids | List All Droplet Neighbors |
| [**listFirewalls**](DropletsApi.md#listFirewalls) | **GET** /v2/droplets/{droplet_id}/firewalls | List all Firewalls Applied to a Droplet |
| [**listKernels**](DropletsApi.md#listKernels) | **GET** /v2/droplets/{droplet_id}/kernels | List All Available Kernels for a Droplet |
| [**listNeighbors**](DropletsApi.md#listNeighbors) | **GET** /v2/droplets/{droplet_id}/neighbors | List Neighbors for a Droplet |
| [**listSnapshots**](DropletsApi.md#listSnapshots) | **GET** /v2/droplets/{droplet_id}/snapshots | List Snapshots for a Droplet |
| [**retryDestroyWithAssociatedResources**](DropletsApi.md#retryDestroyWithAssociatedResources) | **POST** /v2/droplets/{droplet_id}/destroy_with_associated_resources/retry | Retry a Droplet Destroy with Associated Resources Request |


<a name="checkDestroyStatus"></a>
# **checkDestroyStatus**
> AssociatedResourceStatus checkDestroyStatus(dropletId).execute();

Check Status of a Droplet Destroy with Associated Resources Request

To check on the status of a request to destroy a Droplet with its associated resources, send a GET request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/status&#x60; endpoint. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    try {
      AssociatedResourceStatus result = client
              .droplets
              .checkDestroyStatus(dropletId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDroplet());
      System.out.println(result.getResources());
      System.out.println(result.getCompletedAt());
      System.out.println(result.getFailures());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#checkDestroyStatus");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AssociatedResourceStatus> response = client
              .droplets
              .checkDestroyStatus(dropletId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#checkDestroyStatus");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |

### Return type

[**AssociatedResourceStatus**](AssociatedResourceStatus.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object containing containing the status of a request to destroy a Droplet and its associated resources. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="create"></a>
# **create**
> Object create().body(body).execute();

Create a New Droplet

To create a new Droplet, send a POST request to &#x60;/v2/droplets&#x60; setting the required attributes.  A Droplet will be created using the provided information. The response body will contain a JSON object with a key called &#x60;droplet&#x60;. The value will be an object containing the standard attributes for your new Droplet. The response code, 202 Accepted, does not indicate the success or failure of the operation, just that the request has been accepted for processing. The &#x60;actions&#x60; returned as part of the response&#39;s &#x60;links&#x60; object can be used to check the status of the Droplet create event.  ### Create Multiple Droplets  Creating multiple Droplets is very similar to creating a single Droplet. Instead of sending &#x60;name&#x60; as a string, send &#x60;names&#x60; as an array of strings. A Droplet will be created for each name you send using the associated information. Up to ten Droplets may be created this way at a time.  Rather than returning a single Droplet, the response body will contain a JSON array with a key called &#x60;droplets&#x60;. This will be set to an array of JSON objects, each of which will contain the standard Droplet attributes. The response code, 202 Accepted, does not indicate the success or failure of any operation, just that the request has been accepted for processing. The array of &#x60;actions&#x60; returned as part of the response&#39;s &#x60;links&#x60; object can be used to check the status of each individual Droplet create event. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
      Object result = client
              .droplets
              .create()
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .droplets
              .create()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#create");
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
| **body** | **Object**|  | [optional] |

### Return type

**Object**

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | Accepted |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteByTag"></a>
# **deleteByTag**
> deleteByTag(tagName).execute();

Deleting Droplets by Tag

To delete **all** Droplets assigned to a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag in your DELETE request. For example,  &#x60;/v2/droplets?tag_name&#x3D;$TAG_NAME&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    String tagName = "env:test"; // Specifies Droplets to be deleted by tag.
    try {
      client
              .droplets
              .deleteByTag(tagName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#deleteByTag");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .droplets
              .deleteByTag(tagName)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#deleteByTag");
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
| **tagName** | **String**| Specifies Droplets to be deleted by tag. | |

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
| **204** | The action was successful and the response body is empty. This response has content-type set. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteDangerous"></a>
# **deleteDangerous**
> deleteDangerous(dropletId, xDangerous).execute();

Destroy a Droplet and All of its Associated Resources (Dangerous)

To destroy a Droplet along with all of its associated resources, send a DELETE request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/dangerous&#x60; endpoint. The headers of this request must include an &#x60;X-Dangerous&#x60; key set to &#x60;true&#x60;. To preview which resources will be destroyed, first query the Droplet&#39;s associated resources. This operation _can not_ be reverse and should be used with caution.  A successful response will include a 202 response code and no content. Use the status endpoint to check on the success or failure of the destruction of the individual resources. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    Boolean xDangerous = true; // Acknowledge this action will destroy the Droplet and all associated resources and _can not_ be reversed.
    try {
      client
              .droplets
              .deleteDangerous(dropletId, xDangerous)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#deleteDangerous");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .droplets
              .deleteDangerous(dropletId, xDangerous)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#deleteDangerous");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **xDangerous** | **Boolean**| Acknowledge this action will destroy the Droplet and all associated resources and _can not_ be reversed. | |

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
| **202** | The does not indicate the success or failure of any operation, just that the request has been accepted for processing. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="destroy"></a>
# **destroy**
> destroy(dropletId).execute();

Delete an Existing Droplet

To delete a Droplet, send a DELETE request to &#x60;/v2/droplets/$DROPLET_ID&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    try {
      client
              .droplets
              .destroy(dropletId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#destroy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .droplets
              .destroy(dropletId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#destroy");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |

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
| **204** | The action was successful and the response body is empty. This response has content-type set. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="destroyAssociatedResourcesSelective"></a>
# **destroyAssociatedResourcesSelective**
> destroyAssociatedResourcesSelective(dropletId).selectiveDestroyAssociatedResource(selectiveDestroyAssociatedResource).execute();

Selectively Destroy a Droplet and its Associated Resources

To destroy a Droplet along with a sub-set of its associated resources, send a DELETE request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/selective&#x60; endpoint. The JSON body of the request should include &#x60;reserved_ips&#x60;, &#x60;snapshots&#x60;, &#x60;volumes&#x60;, or &#x60;volume_snapshots&#x60; keys each set to an array of IDs for the associated resources to be destroyed. The IDs can be found by querying the Droplet&#39;s associated resources. Any associated resource not included in the request will remain and continue to accrue changes on your account.  A successful response will include a 202 response code and no content. Use the status endpoint to check on the success or failure of the destruction of the individual resources. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    List<String> floatingIps = Arrays.asList(); // An array of unique identifiers for the floating IPs to be scheduled for deletion.
    List<String> reservedIps = Arrays.asList(); // An array of unique identifiers for the reserved IPs to be scheduled for deletion.
    List<String> snapshots = Arrays.asList(); // An array of unique identifiers for the snapshots to be scheduled for deletion.
    List<String> volumes = Arrays.asList(); // An array of unique identifiers for the volumes to be scheduled for deletion.
    List<String> volumeSnapshots = Arrays.asList(); // An array of unique identifiers for the volume snapshots to be scheduled for deletion.
    try {
      client
              .droplets
              .destroyAssociatedResourcesSelective(dropletId)
              .floatingIps(floatingIps)
              .reservedIps(reservedIps)
              .snapshots(snapshots)
              .volumes(volumes)
              .volumeSnapshots(volumeSnapshots)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#destroyAssociatedResourcesSelective");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .droplets
              .destroyAssociatedResourcesSelective(dropletId)
              .floatingIps(floatingIps)
              .reservedIps(reservedIps)
              .snapshots(snapshots)
              .volumes(volumes)
              .volumeSnapshots(volumeSnapshots)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#destroyAssociatedResourcesSelective");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **selectiveDestroyAssociatedResource** | [**SelectiveDestroyAssociatedResource**](SelectiveDestroyAssociatedResource.md)|  | [optional] |

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
| **202** | The does not indicate the success or failure of any operation, just that the request has been accepted for processing. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="get"></a>
# **get**
> DropletsGetResponse get(dropletId).execute();

Retrieve an Existing Droplet

To show information about an individual Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    try {
      DropletsGetResponse result = client
              .droplets
              .get(dropletId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDroplet());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsGetResponse> response = client
              .droplets
              .get(dropletId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#get");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |

### Return type

[**DropletsGetResponse**](DropletsGetResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;droplet&#x60;. This will be set to a JSON object that contains the standard Droplet attributes.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> DropletsListResponse list().perPage(perPage).page(page).tagName(tagName).name(name).execute();

List All Droplets

To list all Droplets in your account, send a GET request to &#x60;/v2/droplets&#x60;.  The response body will be a JSON object with a key of &#x60;droplets&#x60;. This will be set to an array containing objects each representing a Droplet. These will contain the standard Droplet attributes.  ### Filtering Results by Tag  It&#39;s possible to request filtered results by including certain query parameters. To only list Droplets assigned to a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag in your GET request. For example, &#x60;/v2/droplets?tag_name&#x3D;$TAG_NAME&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    String tagName = "env:prod"; // Used to filter Droplets by a specific tag. Can not be combined with `name`.
    String name = "web-01"; // Used to filter list response by Droplet name returning only exact matches. It is case-insensitive and can not be combined with `tag_name`.
    try {
      DropletsListResponse result = client
              .droplets
              .list()
              .perPage(perPage)
              .page(page)
              .tagName(tagName)
              .name(name)
              .execute();
      System.out.println(result);
      System.out.println(result.getDroplets());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsListResponse> response = client
              .droplets
              .list()
              .perPage(perPage)
              .page(page)
              .tagName(tagName)
              .name(name)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#list");
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
| **tagName** | **String**| Used to filter Droplets by a specific tag. Can not be combined with &#x60;name&#x60;. | [optional] |
| **name** | **String**| Used to filter list response by Droplet name returning only exact matches. It is case-insensitive and can not be combined with &#x60;tag_name&#x60;. | [optional] |

### Return type

[**DropletsListResponse**](DropletsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;droplets&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listAssociatedResources"></a>
# **listAssociatedResources**
> DropletsListAssociatedResourcesResponse listAssociatedResources(dropletId).execute();

List Associated Resources for a Droplet

To list the associated billable resources that can be destroyed along with a Droplet, send a GET request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources&#x60; endpoint.  The response will be a JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing information about the associated resources. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    try {
      DropletsListAssociatedResourcesResponse result = client
              .droplets
              .listAssociatedResources(dropletId)
              .execute();
      System.out.println(result);
      System.out.println(result.getReservedIps());
      System.out.println(result.getFloatingIps());
      System.out.println(result.getSnapshots());
      System.out.println(result.getVolumes());
      System.out.println(result.getVolumeSnapshots());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listAssociatedResources");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsListAssociatedResourcesResponse> response = client
              .droplets
              .listAssociatedResources(dropletId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listAssociatedResources");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |

### Return type

[**DropletsListAssociatedResourcesResponse**](DropletsListAssociatedResourcesResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listBackups"></a>
# **listBackups**
> DropletsListBackupsResponse listBackups(dropletId).perPage(perPage).page(page).execute();

List Backups for a Droplet

To retrieve any backups associated with a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/backups&#x60;.  You will get back a JSON object that has a &#x60;backups&#x60; key. This will be set to an array of backup objects, each of which contain the standard Droplet backup attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      DropletsListBackupsResponse result = client
              .droplets
              .listBackups(dropletId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getBackups());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listBackups");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsListBackupsResponse> response = client
              .droplets
              .listBackups(dropletId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listBackups");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**DropletsListBackupsResponse**](DropletsListBackupsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with an &#x60;backups&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listDropletNeighbors"></a>
# **listDropletNeighbors**
> NeighborIds listDropletNeighbors().execute();

List All Droplet Neighbors

To retrieve a list of all Droplets that are co-located on the same physical hardware, send a GET request to &#x60;/v2/reports/droplet_neighbors_ids&#x60;.  The results will be returned as a JSON object with a key of &#x60;neighbor_ids&#x60;. This will be set to an array of arrays. Each array will contain a set of Droplet IDs for Droplets that share a physical server. An empty array indicates that all Droplets associated with your account are located on separate physical hardware. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
      NeighborIds result = client
              .droplets
              .listDropletNeighbors()
              .execute();
      System.out.println(result);
      System.out.println(result.getNeighborIds());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listDropletNeighbors");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<NeighborIds> response = client
              .droplets
              .listDropletNeighbors()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listDropletNeighbors");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters
This endpoint does not need any parameter.

### Return type

[**NeighborIds**](NeighborIds.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with an &#x60;neighbor_ids&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listFirewalls"></a>
# **listFirewalls**
> DropletsListFirewallsResponse listFirewalls(dropletId).perPage(perPage).page(page).execute();

List all Firewalls Applied to a Droplet

To retrieve a list of all firewalls available to a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/firewalls&#x60;  The response will be a JSON object that has a key called &#x60;firewalls&#x60;. This will be set to an array of &#x60;firewall&#x60; objects, each of which contain the standard &#x60;firewall&#x60; attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      DropletsListFirewallsResponse result = client
              .droplets
              .listFirewalls(dropletId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getFirewalls());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listFirewalls");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsListFirewallsResponse> response = client
              .droplets
              .listFirewalls(dropletId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listFirewalls");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**DropletsListFirewallsResponse**](DropletsListFirewallsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object that has a key called &#x60;firewalls&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listKernels"></a>
# **listKernels**
> DropletsListKernelsResponse listKernels(dropletId).perPage(perPage).page(page).execute();

List All Available Kernels for a Droplet

To retrieve a list of all kernels available to a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/kernels&#x60;  The response will be a JSON object that has a key called &#x60;kernels&#x60;. This will be set to an array of &#x60;kernel&#x60; objects, each of which contain the standard &#x60;kernel&#x60; attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      DropletsListKernelsResponse result = client
              .droplets
              .listKernels(dropletId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getKernels());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listKernels");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsListKernelsResponse> response = client
              .droplets
              .listKernels(dropletId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listKernels");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**DropletsListKernelsResponse**](DropletsListKernelsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object that has a key called &#x60;kernels&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listNeighbors"></a>
# **listNeighbors**
> DropletsListNeighborsResponse listNeighbors(dropletId).execute();

List Neighbors for a Droplet

To retrieve a list of any \&quot;neighbors\&quot; (i.e. Droplets that are co-located on the same physical hardware) for a specific Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/neighbors&#x60;.  The results will be returned as a JSON object with a key of &#x60;droplets&#x60;. This will be set to an array containing objects representing any other Droplets that share the same physical hardware. An empty array indicates that the Droplet is not co-located any other Droplets associated with your account. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    try {
      DropletsListNeighborsResponse result = client
              .droplets
              .listNeighbors(dropletId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDroplets());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listNeighbors");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsListNeighborsResponse> response = client
              .droplets
              .listNeighbors(dropletId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listNeighbors");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |

### Return type

[**DropletsListNeighborsResponse**](DropletsListNeighborsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with an &#x60;droplets&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listSnapshots"></a>
# **listSnapshots**
> DropletsListSnapshotsResponse listSnapshots(dropletId).perPage(perPage).page(page).execute();

List Snapshots for a Droplet

To retrieve the snapshots that have been created from a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/snapshots&#x60;.  You will get back a JSON object that has a &#x60;snapshots&#x60; key. This will be set to an array of snapshot objects, each of which contain the standard Droplet snapshot attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      DropletsListSnapshotsResponse result = client
              .droplets
              .listSnapshots(dropletId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getSnapshots());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listSnapshots");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletsListSnapshotsResponse> response = client
              .droplets
              .listSnapshots(dropletId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#listSnapshots");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**DropletsListSnapshotsResponse**](DropletsListSnapshotsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with an &#x60;snapshots&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="retryDestroyWithAssociatedResources"></a>
# **retryDestroyWithAssociatedResources**
> retryDestroyWithAssociatedResources(dropletId).execute();

Retry a Droplet Destroy with Associated Resources Request

If the status of a request to destroy a Droplet with its associated resources reported any errors, it can be retried by sending a POST request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/retry&#x60; endpoint.  Only one destroy can be active at a time per Droplet. If a retry is issued while another destroy is in progress for the Droplet a 409 status code will be returned. A successful response will include a 202 response code and no content. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    try {
      client
              .droplets
              .retryDestroyWithAssociatedResources(dropletId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#retryDestroyWithAssociatedResources");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .droplets
              .retryDestroyWithAssociatedResources(dropletId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletsApi#retryDestroyWithAssociatedResources");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |

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
| **202** | The does not indicate the success or failure of any operation, just that the request has been accepted for processing. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

