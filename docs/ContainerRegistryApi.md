# ContainerRegistryApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**cancelGarbageCollection**](ContainerRegistryApi.md#cancelGarbageCollection) | **PUT** /v2/registry/{registry_name}/garbage-collection/{garbage_collection_uuid} | Update Garbage Collection |
| [**create**](ContainerRegistryApi.md#create) | **POST** /v2/registry | Create Container Registry |
| [**delete**](ContainerRegistryApi.md#delete) | **DELETE** /v2/registry | Delete Container Registry |
| [**deleteRepositoryManifestByDigest**](ContainerRegistryApi.md#deleteRepositoryManifestByDigest) | **DELETE** /v2/registry/{registry_name}/repositories/{repository_name}/digests/{manifest_digest} | Delete Container Registry Repository Manifest |
| [**deleteRepositoryTag**](ContainerRegistryApi.md#deleteRepositoryTag) | **DELETE** /v2/registry/{registry_name}/repositories/{repository_name}/tags/{repository_tag} | Delete Container Registry Repository Tag |
| [**get**](ContainerRegistryApi.md#get) | **GET** /v2/registry | Get Container Registry Information |
| [**getActiveGarbageCollection**](ContainerRegistryApi.md#getActiveGarbageCollection) | **GET** /v2/registry/{registry_name}/garbage-collection | Get Active Garbage Collection |
| [**getDockerCredentials**](ContainerRegistryApi.md#getDockerCredentials) | **GET** /v2/registry/docker-credentials | Get Docker Credentials for Container Registry |
| [**getSubscriptionInfo**](ContainerRegistryApi.md#getSubscriptionInfo) | **GET** /v2/registry/subscription | Get Subscription Information |
| [**listGarbageCollections**](ContainerRegistryApi.md#listGarbageCollections) | **GET** /v2/registry/{registry_name}/garbage-collections | List Garbage Collections |
| [**listOptions**](ContainerRegistryApi.md#listOptions) | **GET** /v2/registry/options | List Registry Options (Subscription Tiers and Available Regions) |
| [**listRepositories**](ContainerRegistryApi.md#listRepositories) | **GET** /v2/registry/{registry_name}/repositories | List All Container Registry Repositories |
| [**listRepositoriesV2**](ContainerRegistryApi.md#listRepositoriesV2) | **GET** /v2/registry/{registry_name}/repositoriesV2 | List All Container Registry Repositories (V2) |
| [**listRepositoryManifests**](ContainerRegistryApi.md#listRepositoryManifests) | **GET** /v2/registry/{registry_name}/repositories/{repository_name}/digests | List All Container Registry Repository Manifests |
| [**listRepositoryTags**](ContainerRegistryApi.md#listRepositoryTags) | **GET** /v2/registry/{registry_name}/repositories/{repository_name}/tags | List All Container Registry Repository Tags |
| [**startGarbageCollection**](ContainerRegistryApi.md#startGarbageCollection) | **POST** /v2/registry/{registry_name}/garbage-collection | Start Garbage Collection |
| [**updateSubscriptionTier**](ContainerRegistryApi.md#updateSubscriptionTier) | **POST** /v2/registry/subscription | Update Subscription Tier |
| [**validateName**](ContainerRegistryApi.md#validateName) | **POST** /v2/registry/validate-name | Validate a Container Registry Name |


<a name="cancelGarbageCollection"></a>
# **cancelGarbageCollection**
> ContainerRegistryGetActiveGarbageCollectionResponse cancelGarbageCollection(registryName, garbageCollectionUuid, updateRegistry).execute();

Update Garbage Collection

To cancel the currently-active garbage collection for a registry, send a PUT request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collection/$GC_UUID&#x60; and specify one or more of the attributes below.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    String garbageCollectionUuid = "eff0feee-49c7-4e8f-ba5c-a320c109c8a8"; // The UUID of a garbage collection run.
    Boolean cancel = true; // A boolean value indicating that the garbage collection should be cancelled.
    try {
      ContainerRegistryGetActiveGarbageCollectionResponse result = client
              .containerRegistry
              .cancelGarbageCollection(registryName, garbageCollectionUuid)
              .cancel(cancel)
              .execute();
      System.out.println(result);
      System.out.println(result.getGarbageCollection());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#cancelGarbageCollection");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> response = client
              .containerRegistry
              .cancelGarbageCollection(registryName, garbageCollectionUuid)
              .cancel(cancel)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#cancelGarbageCollection");
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
| **registryName** | **String**| The name of a container registry. | |
| **garbageCollectionUuid** | **String**| The UUID of a garbage collection run. | |
| **updateRegistry** | [**UpdateRegistry**](UpdateRegistry.md)|  | |

### Return type

[**ContainerRegistryGetActiveGarbageCollectionResponse**](ContainerRegistryGetActiveGarbageCollectionResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="create"></a>
# **create**
> Object create(registryCreate).execute();

Create Container Registry

To create your container registry, send a POST request to &#x60;/v2/registry&#x60;.  The &#x60;name&#x60; becomes part of the URL for images stored in the registry. For example, if your registry is called &#x60;example&#x60;, an image in it will have the URL &#x60;registry.digitalocean.com/example/image:tag&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String name = "name_example"; // A globally unique name for the container registry. Must be lowercase and be composed only of numbers, letters and `-`, up to a limit of 63 characters.
    String subscriptionTierSlug = "starter"; // The slug of the subscription tier to sign up for. Valid values can be retrieved using the options endpoint.
    String region = "nyc3"; // Slug of the region where registry data is stored. When not provided, a region will be selected.
    try {
      Object result = client
              .containerRegistry
              .create(name, subscriptionTierSlug)
              .region(region)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .containerRegistry
              .create(name, subscriptionTierSlug)
              .region(region)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#create");
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
| **registryCreate** | [**RegistryCreate**](RegistryCreate.md)|  | |

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
| **201** | The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> delete().execute();

Delete Container Registry

To delete your container registry, destroying all container image data stored in it, send a DELETE request to &#x60;/v2/registry&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
      client
              .containerRegistry
              .delete()
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .containerRegistry
              .delete()
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#delete");
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

<a name="deleteRepositoryManifestByDigest"></a>
# **deleteRepositoryManifestByDigest**
> deleteRepositoryManifestByDigest(registryName, repositoryName, manifestDigest).execute();

Delete Container Registry Repository Manifest

To delete a container repository manifest by digest, send a DELETE request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/digests/$MANIFEST_DIGEST&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to delete &#x60;registry.digitalocean.com/example/my/repo@sha256:abcd&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/digests/sha256:abcd&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    String repositoryName = "repo-1"; // The name of a container registry repository. If the name contains `/` characters, they must be URL-encoded, e.g. `%2F`.
    String manifestDigest = "sha256:cb8a924afdf0229ef7515d9e5b3024e23b3eb03ddbba287f4a19c6ac90b8d221"; // The manifest digest of a container registry repository tag.
    try {
      client
              .containerRegistry
              .deleteRepositoryManifestByDigest(registryName, repositoryName, manifestDigest)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#deleteRepositoryManifestByDigest");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .containerRegistry
              .deleteRepositoryManifestByDigest(registryName, repositoryName, manifestDigest)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#deleteRepositoryManifestByDigest");
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
| **registryName** | **String**| The name of a container registry. | |
| **repositoryName** | **String**| The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. | |
| **manifestDigest** | **String**| The manifest digest of a container registry repository tag. | |

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

<a name="deleteRepositoryTag"></a>
# **deleteRepositoryTag**
> deleteRepositoryTag(registryName, repositoryName, repositoryTag).execute();

Delete Container Registry Repository Tag

To delete a container repository tag, send a DELETE request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/tags/$TAG&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to delete &#x60;registry.digitalocean.com/example/my/repo:mytag&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/tags/mytag&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    String repositoryName = "repo-1"; // The name of a container registry repository. If the name contains `/` characters, they must be URL-encoded, e.g. `%2F`.
    String repositoryTag = "06a447a"; // The name of a container registry repository tag.
    try {
      client
              .containerRegistry
              .deleteRepositoryTag(registryName, repositoryName, repositoryTag)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#deleteRepositoryTag");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .containerRegistry
              .deleteRepositoryTag(registryName, repositoryName, repositoryTag)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#deleteRepositoryTag");
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
| **registryName** | **String**| The name of a container registry. | |
| **repositoryName** | **String**| The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. | |
| **repositoryTag** | **String**| The name of a container registry repository tag. | |

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
> Object get().execute();

Get Container Registry Information

To get information about your container registry, send a GET request to &#x60;/v2/registry&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
              .containerRegistry
              .get()
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .containerRegistry
              .get()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#get");
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

**Object**

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getActiveGarbageCollection"></a>
# **getActiveGarbageCollection**
> ContainerRegistryGetActiveGarbageCollectionResponse getActiveGarbageCollection(registryName).execute();

Get Active Garbage Collection

To get information about the currently-active garbage collection for a registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collection&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    try {
      ContainerRegistryGetActiveGarbageCollectionResponse result = client
              .containerRegistry
              .getActiveGarbageCollection(registryName)
              .execute();
      System.out.println(result);
      System.out.println(result.getGarbageCollection());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#getActiveGarbageCollection");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> response = client
              .containerRegistry
              .getActiveGarbageCollection(registryName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#getActiveGarbageCollection");
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
| **registryName** | **String**| The name of a container registry. | |

### Return type

[**ContainerRegistryGetActiveGarbageCollectionResponse**](ContainerRegistryGetActiveGarbageCollectionResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDockerCredentials"></a>
# **getDockerCredentials**
> DockerCredentials getDockerCredentials().expirySeconds(expirySeconds).readWrite(readWrite).execute();

Get Docker Credentials for Container Registry

In order to access your container registry with the Docker client or from a Kubernetes cluster, you will need to configure authentication. The necessary JSON configuration can be retrieved by sending a GET request to &#x60;/v2/registry/docker-credentials&#x60;.  The response will be in the format of a Docker &#x60;config.json&#x60; file. To use the config in your Kubernetes cluster, create a Secret with:      kubectl create secret generic docr \\       --from-file&#x3D;.dockerconfigjson&#x3D;config.json \\       --type&#x3D;kubernetes.io/dockerconfigjson  By default, the returned credentials have read-only access to your registry and cannot be used to push images. This is appropriate for most Kubernetes clusters. To retrieve read/write credentials, suitable for use with the Docker client or in a CI system, read_write may be provided as query parameter. For example: &#x60;/v2/registry/docker-credentials?read_write&#x3D;true&#x60;  By default, the returned credentials will not expire. To retrieve credentials with an expiry set, expiry_seconds may be provided as a query parameter. For example: &#x60;/v2/registry/docker-credentials?expiry_seconds&#x3D;3600&#x60; will return credentials that expire after one hour. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    Integer expirySeconds = 0; // The duration in seconds that the returned registry credentials will be valid. If not set or 0, the credentials will not expire.
    Boolean readWrite = false; // By default, the registry credentials allow for read-only access. Set this query parameter to `true` to obtain read-write credentials.
    try {
      DockerCredentials result = client
              .containerRegistry
              .getDockerCredentials()
              .expirySeconds(expirySeconds)
              .readWrite(readWrite)
              .execute();
      System.out.println(result);
      System.out.println(result.getAuths());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#getDockerCredentials");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DockerCredentials> response = client
              .containerRegistry
              .getDockerCredentials()
              .expirySeconds(expirySeconds)
              .readWrite(readWrite)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#getDockerCredentials");
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
| **expirySeconds** | **Integer**| The duration in seconds that the returned registry credentials will be valid. If not set or 0, the credentials will not expire. | [optional] [default to 0] |
| **readWrite** | **Boolean**| By default, the registry credentials allow for read-only access. Set this query parameter to &#x60;true&#x60; to obtain read-write credentials. | [optional] [default to false] |

### Return type

[**DockerCredentials**](DockerCredentials.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A Docker &#x60;config.json&#x60; file for the container registry. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getSubscriptionInfo"></a>
# **getSubscriptionInfo**
> Object getSubscriptionInfo().execute();

Get Subscription Information

A subscription is automatically created when you configure your container registry. To get information about your subscription, send a GET request to &#x60;/v2/registry/subscription&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
              .containerRegistry
              .getSubscriptionInfo()
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#getSubscriptionInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .containerRegistry
              .getSubscriptionInfo()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#getSubscriptionInfo");
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

**Object**

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listGarbageCollections"></a>
# **listGarbageCollections**
> ContainerRegistryListGarbageCollectionsResponse listGarbageCollections(registryName).perPage(perPage).page(page).execute();

List Garbage Collections

To get information about past garbage collections for a registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collections&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      ContainerRegistryListGarbageCollectionsResponse result = client
              .containerRegistry
              .listGarbageCollections(registryName)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getGarbageCollections());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listGarbageCollections");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryListGarbageCollectionsResponse> response = client
              .containerRegistry
              .listGarbageCollections(registryName)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listGarbageCollections");
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
| **registryName** | **String**| The name of a container registry. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**ContainerRegistryListGarbageCollectionsResponse**](ContainerRegistryListGarbageCollectionsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key of &#x60;garbage_collections&#x60;. This will be set to an array containing objects representing each past garbage collection. Each will contain the standard Garbage Collection attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listOptions"></a>
# **listOptions**
> ContainerRegistryListOptionsResponse listOptions().execute();

List Registry Options (Subscription Tiers and Available Regions)

This endpoint serves to provide additional information as to which option values are available when creating a container registry. There are multiple subscription tiers available for container registry. Each tier allows a different number of image repositories to be created in your registry, and has a different amount of storage and transfer included. There are multiple regions available for container registry and controls where your data is stored. To list the available options, send a GET request to &#x60;/v2/registry/options&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
      ContainerRegistryListOptionsResponse result = client
              .containerRegistry
              .listOptions()
              .execute();
      System.out.println(result);
      System.out.println(result.getOptions());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listOptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryListOptionsResponse> response = client
              .containerRegistry
              .listOptions()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listOptions");
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

[**ContainerRegistryListOptionsResponse**](ContainerRegistryListOptionsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;options&#x60; which contains a key called &#x60;subscription_tiers&#x60; listing the available tiers. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listRepositories"></a>
# **listRepositories**
> ContainerRegistryListRepositoriesResponse listRepositories(registryName).perPage(perPage).page(page).execute();

List All Container Registry Repositories

This endpoint has been deprecated in favor of the _List All Container Registry Repositories [V2]_ endpoint.  To list all repositories in your container registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositories&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      ContainerRegistryListRepositoriesResponse result = client
              .containerRegistry
              .listRepositories(registryName)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getRepositories());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositories");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryListRepositoriesResponse> response = client
              .containerRegistry
              .listRepositories(registryName)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositories");
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
| **registryName** | **String**| The name of a container registry. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**ContainerRegistryListRepositoriesResponse**](ContainerRegistryListRepositoriesResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listRepositoriesV2"></a>
# **listRepositoriesV2**
> ContainerRegistryListRepositoriesV2Response listRepositoriesV2(registryName).perPage(perPage).page(page).pageToken(pageToken).execute();

List All Container Registry Repositories (V2)

To list all repositories in your container registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositoriesV2&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return. Ignored when 'page_token' is provided.
    String pageToken = "eyJUb2tlbiI6IkNnZGpiMjlz"; // Token to retrieve of the next or previous set of results more quickly than using 'page'.
    try {
      ContainerRegistryListRepositoriesV2Response result = client
              .containerRegistry
              .listRepositoriesV2(registryName)
              .perPage(perPage)
              .page(page)
              .pageToken(pageToken)
              .execute();
      System.out.println(result);
      System.out.println(result.getRepositories());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositoriesV2");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryListRepositoriesV2Response> response = client
              .containerRegistry
              .listRepositoriesV2(registryName)
              .perPage(perPage)
              .page(page)
              .pageToken(pageToken)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositoriesV2");
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
| **registryName** | **String**| The name of a container registry. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. Ignored when &#39;page_token&#39; is provided. | [optional] [default to 1] |
| **pageToken** | **String**| Token to retrieve of the next or previous set of results more quickly than using &#39;page&#39;. | [optional] |

### Return type

[**ContainerRegistryListRepositoriesV2Response**](ContainerRegistryListRepositoriesV2Response.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listRepositoryManifests"></a>
# **listRepositoryManifests**
> ContainerRegistryListRepositoryManifestsResponse listRepositoryManifests(registryName, repositoryName).perPage(perPage).page(page).execute();

List All Container Registry Repository Manifests

To list all manifests in your container registry repository, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/digests&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to list manifests for &#x60;registry.digitalocean.com/example/my/repo&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/digests&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    String repositoryName = "repo-1"; // The name of a container registry repository. If the name contains `/` characters, they must be URL-encoded, e.g. `%2F`.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      ContainerRegistryListRepositoryManifestsResponse result = client
              .containerRegistry
              .listRepositoryManifests(registryName, repositoryName)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getManifests());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositoryManifests");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryListRepositoryManifestsResponse> response = client
              .containerRegistry
              .listRepositoryManifests(registryName, repositoryName)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositoryManifests");
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
| **registryName** | **String**| The name of a container registry. | |
| **repositoryName** | **String**| The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**ContainerRegistryListRepositoryManifestsResponse**](ContainerRegistryListRepositoryManifestsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response body will be a JSON object with a key of &#x60;manifests&#x60;. This will be set to an array containing objects each representing a manifest. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listRepositoryTags"></a>
# **listRepositoryTags**
> ContainerRegistryListRepositoryTagsResponse listRepositoryTags(registryName, repositoryName).perPage(perPage).page(page).execute();

List All Container Registry Repository Tags

To list all tags in your container registry repository, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/tags&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to list tags for &#x60;registry.digitalocean.com/example/my/repo&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/tags&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    String repositoryName = "repo-1"; // The name of a container registry repository. If the name contains `/` characters, they must be URL-encoded, e.g. `%2F`.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      ContainerRegistryListRepositoryTagsResponse result = client
              .containerRegistry
              .listRepositoryTags(registryName, repositoryName)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getTags());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositoryTags");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryListRepositoryTagsResponse> response = client
              .containerRegistry
              .listRepositoryTags(registryName, repositoryName)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#listRepositoryTags");
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
| **registryName** | **String**| The name of a container registry. | |
| **repositoryName** | **String**| The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**ContainerRegistryListRepositoryTagsResponse**](ContainerRegistryListRepositoryTagsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response body will be a JSON object with a key of &#x60;tags&#x60;. This will be set to an array containing objects each representing a tag. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="startGarbageCollection"></a>
# **startGarbageCollection**
> ContainerRegistryGetActiveGarbageCollectionResponse startGarbageCollection(registryName).execute();

Start Garbage Collection

Garbage collection enables users to clear out unreferenced blobs (layer &amp; manifest data) after deleting one or more manifests from a repository. If there are no unreferenced blobs resulting from the deletion of one or more manifests, garbage collection is effectively a noop. [See here for more information](https://www.digitalocean.com/docs/container-registry/how-to/clean-up-container-registry/) about how and why you should clean up your container registry periodically.  To request a garbage collection run on your registry, send a POST request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collection&#x60;. This will initiate the following sequence of events on your registry.  * Set the registry to read-only mode, meaning no further write-scoped   JWTs will be issued to registry clients. Existing write-scoped JWTs will   continue to work until they expire which can take up to 15 minutes. * Wait until all existing write-scoped JWTs have expired. * Scan all registry manifests to determine which blobs are unreferenced. * Delete all unreferenced blobs from the registry. * Record the number of blobs deleted and bytes freed, mark the garbage   collection status as &#x60;success&#x60;. * Remove the read-only mode restriction from the registry, meaning write-scoped   JWTs will once again be issued to registry clients. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String registryName = "example"; // The name of a container registry.
    try {
      ContainerRegistryGetActiveGarbageCollectionResponse result = client
              .containerRegistry
              .startGarbageCollection(registryName)
              .execute();
      System.out.println(result);
      System.out.println(result.getGarbageCollection());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#startGarbageCollection");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> response = client
              .containerRegistry
              .startGarbageCollection(registryName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#startGarbageCollection");
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
| **registryName** | **String**| The name of a container registry. | |

### Return type

[**ContainerRegistryGetActiveGarbageCollectionResponse**](ContainerRegistryGetActiveGarbageCollectionResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateSubscriptionTier"></a>
# **updateSubscriptionTier**
> Object updateSubscriptionTier().containerRegistryUpdateSubscriptionTierRequest(containerRegistryUpdateSubscriptionTierRequest).execute();

Update Subscription Tier

After creating your registry, you can switch to a different subscription tier to better suit your needs. To do this, send a POST request to &#x60;/v2/registry/subscription&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String tierSlug = "starter"; // The slug of the subscription tier to sign up for.
    try {
      Object result = client
              .containerRegistry
              .updateSubscriptionTier()
              .tierSlug(tierSlug)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#updateSubscriptionTier");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .containerRegistry
              .updateSubscriptionTier()
              .tierSlug(tierSlug)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#updateSubscriptionTier");
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
| **containerRegistryUpdateSubscriptionTierRequest** | [**ContainerRegistryUpdateSubscriptionTierRequest**](ContainerRegistryUpdateSubscriptionTierRequest.md)|  | [optional] |

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
| **200** | The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="validateName"></a>
# **validateName**
> validateName(validateRegistry).execute();

Validate a Container Registry Name

To validate that a container registry name is available for use, send a POST request to &#x60;/v2/registry/validate-name&#x60;.  If the name is both formatted correctly and available, the response code will be 204 and contain no body. If the name is already in use, the response will be a 409 Conflict. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ContainerRegistryApi;
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
    String name = "name_example"; // A globally unique name for the container registry. Must be lowercase and be composed only of numbers, letters and `-`, up to a limit of 63 characters.
    try {
      client
              .containerRegistry
              .validateName(name)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#validateName");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .containerRegistry
              .validateName(name)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ContainerRegistryApi#validateName");
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
| **validateRegistry** | [**ValidateRegistry**](ValidateRegistry.md)|  | |

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

