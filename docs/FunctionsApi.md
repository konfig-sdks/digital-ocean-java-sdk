# FunctionsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createNamespace**](FunctionsApi.md#createNamespace) | **POST** /v2/functions/namespaces | Create Namespace |
| [**createTriggerInNamespace**](FunctionsApi.md#createTriggerInNamespace) | **POST** /v2/functions/namespaces/{namespace_id}/triggers | Create Trigger |
| [**deleteNamespace**](FunctionsApi.md#deleteNamespace) | **DELETE** /v2/functions/namespaces/{namespace_id} | Delete Namespace |
| [**deleteTrigger**](FunctionsApi.md#deleteTrigger) | **DELETE** /v2/functions/namespaces/{namespace_id}/triggers/{trigger_name} | Delete Trigger |
| [**getNamespaceDetails**](FunctionsApi.md#getNamespaceDetails) | **GET** /v2/functions/namespaces/{namespace_id} | Get Namespace |
| [**getTriggerDetails**](FunctionsApi.md#getTriggerDetails) | **GET** /v2/functions/namespaces/{namespace_id}/triggers/{trigger_name} | Get Trigger |
| [**listNamespaces**](FunctionsApi.md#listNamespaces) | **GET** /v2/functions/namespaces | List Namespaces |
| [**listTriggers**](FunctionsApi.md#listTriggers) | **GET** /v2/functions/namespaces/{namespace_id}/triggers | List Triggers |
| [**updateTriggerDetails**](FunctionsApi.md#updateTriggerDetails) | **PUT** /v2/functions/namespaces/{namespace_id}/triggers/{trigger_name} | Update Trigger |


<a name="createNamespace"></a>
# **createNamespace**
> FunctionsCreateNamespaceResponse createNamespace(createNamespace).execute();

Create Namespace

Creates a new serverless functions namespace in the desired region and associates it with the provided label. A namespace is a collection of functions and their associated packages, triggers, and project specifications. To create a namespace, send a POST request to &#x60;/v2/functions/namespaces&#x60; with the &#x60;region&#x60; and &#x60;label&#x60; properties.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String region = "region_example"; // The [datacenter region](https://docs.digitalocean.com/products/platform/availability-matrix/#available-datacenters) in which to create the namespace.
    String label = "label_example"; // The namespace's unique name.
    try {
      FunctionsCreateNamespaceResponse result = client
              .functions
              .createNamespace(region, label)
              .execute();
      System.out.println(result);
      System.out.println(result.getNamespace());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#createNamespace");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FunctionsCreateNamespaceResponse> response = client
              .functions
              .createNamespace(region, label)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#createNamespace");
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
| **createNamespace** | [**CreateNamespace**](CreateNamespace.md)|  | |

### Return type

[**FunctionsCreateNamespaceResponse**](FunctionsCreateNamespaceResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="createTriggerInNamespace"></a>
# **createTriggerInNamespace**
> FunctionsCreateTriggerInNamespaceResponse createTriggerInNamespace(namespaceId, createTrigger).execute();

Create Trigger

Creates a new trigger for a given function in a namespace. To create a trigger, send a POST request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers&#x60; with the &#x60;name&#x60;, &#x60;function&#x60;, &#x60;type&#x60;, &#x60;is_enabled&#x60; and &#x60;scheduled_details&#x60; properties.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String name = "name_example"; // The trigger's unique name within the namespace.
    String function = "function_example"; // Name of function(action) that exists in the given namespace.
    String type = "type_example"; // One of different type of triggers. Currently only SCHEDULED is supported.
    Boolean isEnabled = true; // Indicates weather the trigger is paused or unpaused.
    ScheduledDetails scheduledDetails = new ScheduledDetails();
    String namespaceId = "fn-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; // The ID of the namespace to be managed.
    try {
      FunctionsCreateTriggerInNamespaceResponse result = client
              .functions
              .createTriggerInNamespace(name, function, type, isEnabled, scheduledDetails, namespaceId)
              .execute();
      System.out.println(result);
      System.out.println(result.getTrigger());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#createTriggerInNamespace");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FunctionsCreateTriggerInNamespaceResponse> response = client
              .functions
              .createTriggerInNamespace(name, function, type, isEnabled, scheduledDetails, namespaceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#createTriggerInNamespace");
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
| **namespaceId** | **String**| The ID of the namespace to be managed. | |
| **createTrigger** | [**CreateTrigger**](CreateTrigger.md)|  | |

### Return type

[**FunctionsCreateTriggerInNamespaceResponse**](FunctionsCreateTriggerInNamespaceResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteNamespace"></a>
# **deleteNamespace**
> deleteNamespace(namespaceId).execute();

Delete Namespace

Deletes the given namespace.  When a namespace is deleted all assets, in the namespace are deleted, this includes packages, functions and triggers. Deleting a namespace is a destructive operation and assets in the namespace are not recoverable after deletion. Some metadata is retained, such as activations, or soft deleted for reporting purposes. To delete namespace, send a DELETE request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID&#x60;. A successful deletion returns a 204 response.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String namespaceId = "fn-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; // The ID of the namespace to be managed.
    try {
      client
              .functions
              .deleteNamespace(namespaceId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#deleteNamespace");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .functions
              .deleteNamespace(namespaceId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#deleteNamespace");
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
| **namespaceId** | **String**| The ID of the namespace to be managed. | |

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

<a name="deleteTrigger"></a>
# **deleteTrigger**
> deleteTrigger(namespaceId, triggerName).execute();

Delete Trigger

Deletes the given trigger. To delete trigger, send a DELETE request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers/$TRIGGER_NAME&#x60;. A successful deletion returns a 204 response.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String namespaceId = "fn-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; // The ID of the namespace to be managed.
    String triggerName = "my trigger"; // The name of the trigger to be managed.
    try {
      client
              .functions
              .deleteTrigger(namespaceId, triggerName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#deleteTrigger");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .functions
              .deleteTrigger(namespaceId, triggerName)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#deleteTrigger");
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
| **namespaceId** | **String**| The ID of the namespace to be managed. | |
| **triggerName** | **String**| The name of the trigger to be managed. | |

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

<a name="getNamespaceDetails"></a>
# **getNamespaceDetails**
> FunctionsCreateNamespaceResponse getNamespaceDetails(namespaceId).execute();

Get Namespace

Gets the namespace details for the given namespace UUID. To get namespace details, send a GET request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID&#x60; with no parameters.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String namespaceId = "fn-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; // The ID of the namespace to be managed.
    try {
      FunctionsCreateNamespaceResponse result = client
              .functions
              .getNamespaceDetails(namespaceId)
              .execute();
      System.out.println(result);
      System.out.println(result.getNamespace());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#getNamespaceDetails");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FunctionsCreateNamespaceResponse> response = client
              .functions
              .getNamespaceDetails(namespaceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#getNamespaceDetails");
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
| **namespaceId** | **String**| The ID of the namespace to be managed. | |

### Return type

[**FunctionsCreateNamespaceResponse**](FunctionsCreateNamespaceResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getTriggerDetails"></a>
# **getTriggerDetails**
> FunctionsCreateTriggerInNamespaceResponse getTriggerDetails(namespaceId, triggerName).execute();

Get Trigger

Gets the trigger details. To get the trigger details, send a GET request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers/$TRIGGER_NAME&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String namespaceId = "fn-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; // The ID of the namespace to be managed.
    String triggerName = "my trigger"; // The name of the trigger to be managed.
    try {
      FunctionsCreateTriggerInNamespaceResponse result = client
              .functions
              .getTriggerDetails(namespaceId, triggerName)
              .execute();
      System.out.println(result);
      System.out.println(result.getTrigger());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#getTriggerDetails");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FunctionsCreateTriggerInNamespaceResponse> response = client
              .functions
              .getTriggerDetails(namespaceId, triggerName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#getTriggerDetails");
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
| **namespaceId** | **String**| The ID of the namespace to be managed. | |
| **triggerName** | **String**| The name of the trigger to be managed. | |

### Return type

[**FunctionsCreateTriggerInNamespaceResponse**](FunctionsCreateTriggerInNamespaceResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listNamespaces"></a>
# **listNamespaces**
> FunctionsListNamespacesResponse listNamespaces().execute();

List Namespaces

Returns a list of namespaces associated with the current user. To get all namespaces, send a GET request to &#x60;/v2/functions/namespaces&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
      FunctionsListNamespacesResponse result = client
              .functions
              .listNamespaces()
              .execute();
      System.out.println(result);
      System.out.println(result.getNamespaces());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#listNamespaces");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FunctionsListNamespacesResponse> response = client
              .functions
              .listNamespaces()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#listNamespaces");
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

[**FunctionsListNamespacesResponse**](FunctionsListNamespacesResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listTriggers"></a>
# **listTriggers**
> FunctionsListTriggersResponse listTriggers(namespaceId).execute();

List Triggers

Returns a list of triggers associated with the current user and namespace. To get all triggers, send a GET request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String namespaceId = "fn-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; // The ID of the namespace to be managed.
    try {
      FunctionsListTriggersResponse result = client
              .functions
              .listTriggers(namespaceId)
              .execute();
      System.out.println(result);
      System.out.println(result.getTriggers());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#listTriggers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FunctionsListTriggersResponse> response = client
              .functions
              .listTriggers(namespaceId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#listTriggers");
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
| **namespaceId** | **String**| The ID of the namespace to be managed. | |

### Return type

[**FunctionsListTriggersResponse**](FunctionsListTriggersResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateTriggerDetails"></a>
# **updateTriggerDetails**
> FunctionsCreateTriggerInNamespaceResponse updateTriggerDetails(namespaceId, triggerName, updateTrigger).execute();

Update Trigger

Updates the details of the given trigger. To update a trigger, send a PUT request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers/$TRIGGER_NAME&#x60; with new values for the &#x60;is_enabled &#x60; or &#x60;scheduled_details&#x60; properties.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FunctionsApi;
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
    String namespaceId = "fn-xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"; // The ID of the namespace to be managed.
    String triggerName = "my trigger"; // The name of the trigger to be managed.
    Boolean isEnabled = true; // Indicates weather the trigger is paused or unpaused.
    ScheduledDetails scheduledDetails = new ScheduledDetails();
    try {
      FunctionsCreateTriggerInNamespaceResponse result = client
              .functions
              .updateTriggerDetails(namespaceId, triggerName)
              .isEnabled(isEnabled)
              .scheduledDetails(scheduledDetails)
              .execute();
      System.out.println(result);
      System.out.println(result.getTrigger());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#updateTriggerDetails");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FunctionsCreateTriggerInNamespaceResponse> response = client
              .functions
              .updateTriggerDetails(namespaceId, triggerName)
              .isEnabled(isEnabled)
              .scheduledDetails(scheduledDetails)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FunctionsApi#updateTriggerDetails");
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
| **namespaceId** | **String**| The ID of the namespace to be managed. | |
| **triggerName** | **String**| The name of the trigger to be managed. | |
| **updateTrigger** | [**UpdateTrigger**](UpdateTrigger.md)|  | |

### Return type

[**FunctionsCreateTriggerInNamespaceResponse**](FunctionsCreateTriggerInNamespaceResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

