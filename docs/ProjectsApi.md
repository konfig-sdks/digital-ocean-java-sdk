# ProjectsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create**](ProjectsApi.md#create) | **POST** /v2/projects | Create a Project |
| [**delete**](ProjectsApi.md#delete) | **DELETE** /v2/projects/{project_id} | Delete an Existing Project |
| [**get**](ProjectsApi.md#get) | **GET** /v2/projects/{project_id} | Retrieve an Existing Project |
| [**getDefaultProject**](ProjectsApi.md#getDefaultProject) | **GET** /v2/projects/default | Retrieve the Default Project |
| [**list**](ProjectsApi.md#list) | **GET** /v2/projects | List All Projects |
| [**patch**](ProjectsApi.md#patch) | **PATCH** /v2/projects/{project_id} | Patch a Project |
| [**update**](ProjectsApi.md#update) | **PUT** /v2/projects/{project_id} | Update a Project |
| [**updateDefaultProject**](ProjectsApi.md#updateDefaultProject) | **PUT** /v2/projects/default | Update the Default Project |
| [**updateDefaultProjectAttributes**](ProjectsApi.md#updateDefaultProjectAttributes) | **PATCH** /v2/projects/default | Patch the Default Project |


<a name="create"></a>
# **create**
> Object create(projectBase).execute();

Create a Project

To create a project, send a POST request to &#x60;/v2/projects&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
    String description = "description_example"; // The description of the project. The maximum length is 255 characters.
    UUID id = UUID.randomUUID(); // The unique universal identifier of this project.
    String ownerUuid = "ownerUuid_example"; // The unique universal identifier of the project owner.
    Integer ownerId = 56; // The integer id of the project owner.
    String name = "name_example"; // The human-readable name for the project. The maximum length is 175 characters and the name must be unique.
    String purpose = "purpose_example"; // The purpose of the project. The maximum length is 255 characters. It can have one of the following values:  - Just trying out DigitalOcean - Class project / Educational purposes - Website or blog - Web Application - Service or API - Mobile Application - Machine learning / AI / Data processing - IoT - Operational / Developer tooling  If another value for purpose is specified, for example, \\\"your custom purpose\\\", your purpose will be stored as `Other: your custom purpose`. 
    String environment = "Development"; // The environment of the project's resources.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was created.
    OffsetDateTime updatedAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was updated.
    try {
      Object result = client
              .projects
              .create()
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .projects
              .create()
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#create");
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
| **projectBase** | [**ProjectBase**](ProjectBase.md)|  | |

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
| **201** | The response will be a JSON object with a key called &#x60;project&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> delete(projectId).execute();

Delete an Existing Project

To delete a project, send a DELETE request to &#x60;/v2/projects/$PROJECT_ID&#x60;. To be deleted, a project must not have any resources assigned to it. Any existing resources must first be reassigned or destroyed, or you will receive a 412 error.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
    try {
      client
              .projects
              .delete(projectId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .projects
              .delete(projectId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#delete");
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
> Object get(projectId).execute();

Retrieve an Existing Project

To get a project, send a GET request to &#x60;/v2/projects/$PROJECT_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
    try {
      Object result = client
              .projects
              .get(projectId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .projects
              .get(projectId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#get");
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
| **200** | The response will be a JSON object with a key called &#x60;project&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDefaultProject"></a>
# **getDefaultProject**
> ProjectsGetDefaultProjectResponse getDefaultProject().execute();

Retrieve the Default Project

To get your default project, send a GET request to &#x60;/v2/projects/default&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
      ProjectsGetDefaultProjectResponse result = client
              .projects
              .getDefaultProject()
              .execute();
      System.out.println(result);
      System.out.println(result.getProject());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#getDefaultProject");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProjectsGetDefaultProjectResponse> response = client
              .projects
              .getDefaultProject()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#getDefaultProject");
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

[**ProjectsGetDefaultProjectResponse**](ProjectsGetDefaultProjectResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;project&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> ProjectsListResponse list().perPage(perPage).page(page).execute();

List All Projects

To list all your projects, send a GET request to &#x60;/v2/projects&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
      ProjectsListResponse result = client
              .projects
              .list()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getProjects());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ProjectsListResponse> response = client
              .projects
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
      System.err.println("Exception when calling ProjectsApi#list");
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

[**ProjectsListResponse**](ProjectsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;projects&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="patch"></a>
# **patch**
> Object patch(projectId, project).execute();

Patch a Project

To update only specific attributes of a project, send a PATCH request to &#x60;/v2/projects/$PROJECT_ID&#x60;. At least one of the following attributes needs to be sent.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
    String description = "description_example"; // The description of the project. The maximum length is 255 characters.
    UUID id = UUID.randomUUID(); // The unique universal identifier of this project.
    String ownerUuid = "ownerUuid_example"; // The unique universal identifier of the project owner.
    Integer ownerId = 56; // The integer id of the project owner.
    String name = "name_example"; // The human-readable name for the project. The maximum length is 175 characters and the name must be unique.
    String purpose = "purpose_example"; // The purpose of the project. The maximum length is 255 characters. It can have one of the following values:  - Just trying out DigitalOcean - Class project / Educational purposes - Website or blog - Web Application - Service or API - Mobile Application - Machine learning / AI / Data processing - IoT - Operational / Developer tooling  If another value for purpose is specified, for example, \\\"your custom purpose\\\", your purpose will be stored as `Other: your custom purpose`. 
    String environment = "Development"; // The environment of the project's resources.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was created.
    OffsetDateTime updatedAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was updated.
    Boolean isDefault = true; // If true, all resources will be added to this project if no project is specified.
    try {
      Object result = client
              .projects
              .patch(projectId)
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#patch");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .projects
              .patch(projectId)
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#patch");
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
| **project** | [**Project**](Project.md)|  | |

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
| **200** | The response will be a JSON object with a key called &#x60;project&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="update"></a>
# **update**
> Object update(projectId, project).execute();

Update a Project

To update a project, send a PUT request to &#x60;/v2/projects/$PROJECT_ID&#x60;. All of the following attributes must be sent.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
    String description = "description_example"; // The description of the project. The maximum length is 255 characters.
    UUID id = UUID.randomUUID(); // The unique universal identifier of this project.
    String ownerUuid = "ownerUuid_example"; // The unique universal identifier of the project owner.
    Integer ownerId = 56; // The integer id of the project owner.
    String name = "name_example"; // The human-readable name for the project. The maximum length is 175 characters and the name must be unique.
    String purpose = "purpose_example"; // The purpose of the project. The maximum length is 255 characters. It can have one of the following values:  - Just trying out DigitalOcean - Class project / Educational purposes - Website or blog - Web Application - Service or API - Mobile Application - Machine learning / AI / Data processing - IoT - Operational / Developer tooling  If another value for purpose is specified, for example, \\\"your custom purpose\\\", your purpose will be stored as `Other: your custom purpose`. 
    String environment = "Development"; // The environment of the project's resources.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was created.
    OffsetDateTime updatedAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was updated.
    Boolean isDefault = true; // If true, all resources will be added to this project if no project is specified.
    try {
      Object result = client
              .projects
              .update(projectId)
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .projects
              .update(projectId)
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#update");
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
| **project** | [**Project**](Project.md)|  | |

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
| **200** | The response will be a JSON object with a key called &#x60;project&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateDefaultProject"></a>
# **updateDefaultProject**
> Object updateDefaultProject(project).execute();

Update the Default Project

To update you default project, send a PUT request to &#x60;/v2/projects/default&#x60;. All of the following attributes must be sent.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
    String description = "description_example"; // The description of the project. The maximum length is 255 characters.
    UUID id = UUID.randomUUID(); // The unique universal identifier of this project.
    String ownerUuid = "ownerUuid_example"; // The unique universal identifier of the project owner.
    Integer ownerId = 56; // The integer id of the project owner.
    String name = "name_example"; // The human-readable name for the project. The maximum length is 175 characters and the name must be unique.
    String purpose = "purpose_example"; // The purpose of the project. The maximum length is 255 characters. It can have one of the following values:  - Just trying out DigitalOcean - Class project / Educational purposes - Website or blog - Web Application - Service or API - Mobile Application - Machine learning / AI / Data processing - IoT - Operational / Developer tooling  If another value for purpose is specified, for example, \\\"your custom purpose\\\", your purpose will be stored as `Other: your custom purpose`. 
    String environment = "Development"; // The environment of the project's resources.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was created.
    OffsetDateTime updatedAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was updated.
    Boolean isDefault = true; // If true, all resources will be added to this project if no project is specified.
    try {
      Object result = client
              .projects
              .updateDefaultProject()
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#updateDefaultProject");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .projects
              .updateDefaultProject()
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#updateDefaultProject");
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
| **project** | [**Project**](Project.md)|  | |

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
| **200** | The response will be a JSON object with a key called &#x60;project&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateDefaultProjectAttributes"></a>
# **updateDefaultProjectAttributes**
> Object updateDefaultProjectAttributes(project).execute();

Patch the Default Project

To update only specific attributes of your default project, send a PATCH request to &#x60;/v2/projects/default&#x60;. At least one of the following attributes needs to be sent.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ProjectsApi;
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
    String description = "description_example"; // The description of the project. The maximum length is 255 characters.
    UUID id = UUID.randomUUID(); // The unique universal identifier of this project.
    String ownerUuid = "ownerUuid_example"; // The unique universal identifier of the project owner.
    Integer ownerId = 56; // The integer id of the project owner.
    String name = "name_example"; // The human-readable name for the project. The maximum length is 175 characters and the name must be unique.
    String purpose = "purpose_example"; // The purpose of the project. The maximum length is 255 characters. It can have one of the following values:  - Just trying out DigitalOcean - Class project / Educational purposes - Website or blog - Web Application - Service or API - Mobile Application - Machine learning / AI / Data processing - IoT - Operational / Developer tooling  If another value for purpose is specified, for example, \\\"your custom purpose\\\", your purpose will be stored as `Other: your custom purpose`. 
    String environment = "Development"; // The environment of the project's resources.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was created.
    OffsetDateTime updatedAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the project was updated.
    Boolean isDefault = true; // If true, all resources will be added to this project if no project is specified.
    try {
      Object result = client
              .projects
              .updateDefaultProjectAttributes()
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#updateDefaultProjectAttributes");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .projects
              .updateDefaultProjectAttributes()
              .description(description)
              .id(id)
              .ownerUuid(ownerUuid)
              .ownerId(ownerId)
              .name(name)
              .purpose(purpose)
              .environment(environment)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .isDefault(isDefault)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ProjectsApi#updateDefaultProjectAttributes");
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
| **project** | [**Project**](Project.md)|  | |

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
| **200** | The response will be a JSON object with a key called &#x60;project&#x60;. The value of this will be an object with the standard project attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

