# ProjectResourcesApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**assignResourcesToDefault**](ProjectResourcesApi.md#assignResourcesToDefault) | **POST** /v2/projects/default/resources | Assign Resources to Default Project |
| [**assignToProject**](ProjectResourcesApi.md#assignToProject) | **POST** /v2/projects/{project_id}/resources | Assign Resources to a Project |
| [**list**](ProjectResourcesApi.md#list) | **GET** /v2/projects/{project_id}/resources | List Project Resources |
| [**listDefault**](ProjectResourcesApi.md#listDefault) | **GET** /v2/projects/default/resources | List Default Project Resources |


<a name="assignResourcesToDefault"></a>
# **assignResourcesToDefault**
> ProjectResourcesAssignToProjectResponse assignResourcesToDefault(projectAssignment).execute();

Assign Resources to Default Project

To assign resources to your default project, send a POST request to &#x60;/v2/projects/default/resources&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectResourcesApi;
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
    List<String> resources = Arrays.asList(); // A list of uniform resource names (URNs) to be added to a project.
    try {
      ProjectResourcesAssignToProjectResponse result = client
              .projectResources
              .assignResourcesToDefault()
              .resources(resources)
              .execute();
      System.out.println(result);
      System.out.println(result.getResources());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#assignResourcesToDefault");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProjectResourcesAssignToProjectResponse> response = client
              .projectResources
              .assignResourcesToDefault()
              .resources(resources)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#assignResourcesToDefault");
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
| **projectAssignment** | [**ProjectAssignment**](ProjectAssignment.md)|  | |

### Return type

[**ProjectResourcesAssignToProjectResponse**](ProjectResourcesAssignToProjectResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;resources&#x60;. The value of this will be an object with the standard resource attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="assignToProject"></a>
# **assignToProject**
> ProjectResourcesAssignToProjectResponse assignToProject(projectId, projectAssignment).execute();

Assign Resources to a Project

To assign resources to a project, send a POST request to &#x60;/v2/projects/$PROJECT_ID/resources&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectResourcesApi;
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
    UUID projectId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a project.
    List<String> resources = Arrays.asList(); // A list of uniform resource names (URNs) to be added to a project.
    try {
      ProjectResourcesAssignToProjectResponse result = client
              .projectResources
              .assignToProject(projectId)
              .resources(resources)
              .execute();
      System.out.println(result);
      System.out.println(result.getResources());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#assignToProject");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProjectResourcesAssignToProjectResponse> response = client
              .projectResources
              .assignToProject(projectId)
              .resources(resources)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#assignToProject");
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
| **projectId** | **UUID**| A unique identifier for a project. | |
| **projectAssignment** | [**ProjectAssignment**](ProjectAssignment.md)|  | |

### Return type

[**ProjectResourcesAssignToProjectResponse**](ProjectResourcesAssignToProjectResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;resources&#x60;. The value of this will be an object with the standard resource attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> ProjectResourcesListResponse list(projectId).perPage(perPage).page(page).execute();

List Project Resources

To list all your resources in a project, send a GET request to &#x60;/v2/projects/$PROJECT_ID/resources&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectResourcesApi;
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
    UUID projectId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a project.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      ProjectResourcesListResponse result = client
              .projectResources
              .list(projectId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getResources());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProjectResourcesListResponse> response = client
              .projectResources
              .list(projectId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#list");
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
| **projectId** | **UUID**| A unique identifier for a project. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**ProjectResourcesListResponse**](ProjectResourcesListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;resources&#x60;. The value of this will be an object with the standard resource attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listDefault"></a>
# **listDefault**
> ProjectResourcesListResponse listDefault().execute();

List Default Project Resources

To list all your resources in your default project, send a GET request to &#x60;/v2/projects/default/resources&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectResourcesApi;
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
      ProjectResourcesListResponse result = client
              .projectResources
              .listDefault()
              .execute();
      System.out.println(result);
      System.out.println(result.getResources());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#listDefault");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProjectResourcesListResponse> response = client
              .projectResources
              .listDefault()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectResourcesApi#listDefault");
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

[**ProjectResourcesListResponse**](ProjectResourcesListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;resources&#x60;. The value of this will be an object with the standard resource attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

