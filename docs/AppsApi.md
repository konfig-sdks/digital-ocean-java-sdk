# AppsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**cancelDeployment**](AppsApi.md#cancelDeployment) | **POST** /v2/apps/{app_id}/deployments/{deployment_id}/cancel | Cancel a Deployment |
| [**commitRollback**](AppsApi.md#commitRollback) | **POST** /v2/apps/{app_id}/rollback/commit | Commit App Rollback |
| [**create**](AppsApi.md#create) | **POST** /v2/apps | Create a New App |
| [**createDeployment**](AppsApi.md#createDeployment) | **POST** /v2/apps/{app_id}/deployments | Create an App Deployment |
| [**delete**](AppsApi.md#delete) | **DELETE** /v2/apps/{id} | Delete an App |
| [**get**](AppsApi.md#get) | **GET** /v2/apps/{id} | Retrieve an Existing App |
| [**getActiveDeploymentLogs**](AppsApi.md#getActiveDeploymentLogs) | **GET** /v2/apps/{app_id}/components/{component_name}/logs | Retrieve Active Deployment Logs |
| [**getActiveDeploymentLogs_0**](AppsApi.md#getActiveDeploymentLogs_0) | **GET** /v2/apps/{app_id}/logs | Retrieve Active Deployment Aggregate Logs |
| [**getAggregateDeploymentLogs**](AppsApi.md#getAggregateDeploymentLogs) | **GET** /v2/apps/{app_id}/deployments/{deployment_id}/logs | Retrieve Aggregate Deployment Logs |
| [**getAppDailyBandwidthMetrics**](AppsApi.md#getAppDailyBandwidthMetrics) | **GET** /v2/apps/{app_id}/metrics/bandwidth_daily | Retrieve App Daily Bandwidth Metrics |
| [**getDeploymentInfo**](AppsApi.md#getDeploymentInfo) | **GET** /v2/apps/{app_id}/deployments/{deployment_id} | Retrieve an App Deployment |
| [**getDeploymentLogs**](AppsApi.md#getDeploymentLogs) | **GET** /v2/apps/{app_id}/deployments/{deployment_id}/components/{component_name}/logs | Retrieve Deployment Logs |
| [**getInstanceSize**](AppsApi.md#getInstanceSize) | **GET** /v2/apps/tiers/instance_sizes/{slug} | Retrieve an Instance Size |
| [**getMultipleDailyMetrics**](AppsApi.md#getMultipleDailyMetrics) | **POST** /v2/apps/metrics/bandwidth_daily | Retrieve Multiple Apps&#39; Daily Bandwidth Metrics |
| [**getTierInfo**](AppsApi.md#getTierInfo) | **GET** /v2/apps/tiers/{slug} | Retrieve an App Tier |
| [**list**](AppsApi.md#list) | **GET** /v2/apps | List All Apps |
| [**listAlerts**](AppsApi.md#listAlerts) | **GET** /v2/apps/{app_id}/alerts | List all app alerts |
| [**listDeployments**](AppsApi.md#listDeployments) | **GET** /v2/apps/{app_id}/deployments | List App Deployments |
| [**listInstanceSizes**](AppsApi.md#listInstanceSizes) | **GET** /v2/apps/tiers/instance_sizes | List Instance Sizes |
| [**listRegions**](AppsApi.md#listRegions) | **GET** /v2/apps/regions | List App Regions |
| [**listTiers**](AppsApi.md#listTiers) | **GET** /v2/apps/tiers | List App Tiers |
| [**proposeAppSpec**](AppsApi.md#proposeAppSpec) | **POST** /v2/apps/propose | Propose an App Spec |
| [**revertRollback**](AppsApi.md#revertRollback) | **POST** /v2/apps/{app_id}/rollback/revert | Revert App Rollback |
| [**rollbackDeployment**](AppsApi.md#rollbackDeployment) | **POST** /v2/apps/{app_id}/rollback | Rollback App |
| [**update**](AppsApi.md#update) | **PUT** /v2/apps/{id} | Update an App |
| [**updateDestinationsForAlerts**](AppsApi.md#updateDestinationsForAlerts) | **POST** /v2/apps/{app_id}/alerts/{alert_id}/destinations | Update destinations for alerts |
| [**validateRollback**](AppsApi.md#validateRollback) | **POST** /v2/apps/{app_id}/rollback/validate | Validate App Rollback |


<a name="cancelDeployment"></a>
# **cancelDeployment**
> AppsDeploymentResponse cancelDeployment(appId, deploymentId).execute();

Cancel a Deployment

Immediately cancel an in-progress deployment.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String deploymentId = "3aa4d20e-5527-4c00-b496-601fbd22520a"; // The deployment ID
    try {
      AppsDeploymentResponse result = client
              .apps
              .cancelDeployment(appId, deploymentId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDeployment());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#cancelDeployment");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsDeploymentResponse> response = client
              .apps
              .cancelDeployment(appId, deploymentId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#cancelDeployment");
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
| **appId** | **String**| The app ID | |
| **deploymentId** | **String**| The deployment ID | |

### Return type

[**AppsDeploymentResponse**](AppsDeploymentResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON the &#x60;deployment&#x60; that was just cancelled. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="commitRollback"></a>
# **commitRollback**
> commitRollback(appId).execute();

Commit App Rollback

Commit an app rollback. This action permanently applies the rollback and unpins the app to resume new deployments. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    try {
      client
              .apps
              .commitRollback(appId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#commitRollback");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .apps
              .commitRollback(appId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#commitRollback");
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
| **appId** | **String**| The app ID | |

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
| **200** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="create"></a>
# **create**
> AppResponse create(appsCreateAppRequest).execute();

Create a New App

Create a new app by submitting an app specification. For documentation on app specifications (&#x60;AppSpec&#x60; objects), please refer to [the product documentation](https://docs.digitalocean.com/products/app-platform/reference/app-spec/).

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    AppSpec spec = new AppSpec();
    String projectId = "projectId_example"; // The ID of the project the app should be assigned to. If omitted, it will be assigned to your default project.
    try {
      AppResponse result = client
              .apps
              .create(spec)
              .projectId(projectId)
              .execute();
      System.out.println(result);
      System.out.println(result.getApp());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppResponse> response = client
              .apps
              .create(spec)
              .projectId(projectId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#create");
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
| **appsCreateAppRequest** | [**AppsCreateAppRequest**](AppsCreateAppRequest.md)|  | |

### Return type

[**AppResponse**](AppResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON or YAML of a &#x60;spec&#x60; object. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="createDeployment"></a>
# **createDeployment**
> AppsDeploymentResponse createDeployment(appId, appsCreateDeploymentRequest).execute();

Create an App Deployment

Creating an app deployment will pull the latest changes from your repository and schedule a new deployment for your app.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    Boolean forceBuild = true;
    try {
      AppsDeploymentResponse result = client
              .apps
              .createDeployment(appId)
              .forceBuild(forceBuild)
              .execute();
      System.out.println(result);
      System.out.println(result.getDeployment());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#createDeployment");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsDeploymentResponse> response = client
              .apps
              .createDeployment(appId)
              .forceBuild(forceBuild)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#createDeployment");
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
| **appId** | **String**| The app ID | |
| **appsCreateDeploymentRequest** | [**AppsCreateDeploymentRequest**](AppsCreateDeploymentRequest.md)|  | |

### Return type

[**AppsDeploymentResponse**](AppsDeploymentResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;deployment&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> AppsDeleteAppResponse delete(id).execute();

Delete an App

Delete an existing app. Once deleted, all active deployments will be permanently shut down and the app deleted. If needed, be sure to back up your app specification so that you may re-create it at a later time.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String id = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The ID of the app
    try {
      AppsDeleteAppResponse result = client
              .apps
              .delete(id)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsDeleteAppResponse> response = client
              .apps
              .delete(id)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#delete");
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
| **id** | **String**| The ID of the app | |

### Return type

[**AppsDeleteAppResponse**](AppsDeleteAppResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | the ID of the app deleted. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="get"></a>
# **get**
> AppResponse get(id).name(name).execute();

Retrieve an Existing App

Retrieve details about an existing app by either its ID or name. To retrieve an app by its name, do not include an ID in the request path. Information about the current active deployment as well as any in progress ones will also be included in the response.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String id = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The ID of the app
    String name = "myApp"; // The name of the app to retrieve.
    try {
      AppResponse result = client
              .apps
              .get(id)
              .name(name)
              .execute();
      System.out.println(result);
      System.out.println(result.getApp());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppResponse> response = client
              .apps
              .get(id)
              .name(name)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#get");
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
| **id** | **String**| The ID of the app | |
| **name** | **String**| The name of the app to retrieve. | [optional] |

### Return type

[**AppResponse**](AppResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON with key &#x60;app&#x60; |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getActiveDeploymentLogs"></a>
# **getActiveDeploymentLogs**
> AppsGetLogsResponse getActiveDeploymentLogs(appId, componentName, type).follow(follow).podConnectionTimeout(podConnectionTimeout).execute();

Retrieve Active Deployment Logs

Retrieve the logs of the active deployment if one exists. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment. Note log_type&#x3D;BUILD logs will return logs associated with the current active deployment (being served). To view build logs associated with in-progress build, the query must explicitly reference the deployment id.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String componentName = "component"; // An optional component name. If set, logs will be limited to this component only.
    String type = "UNSPECIFIED"; // The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime
    Boolean follow = true; // Whether the logs should follow live updates.
    String podConnectionTimeout = "3m"; // An optional time duration to wait if the underlying component instance is not immediately available. Default: `3m`.
    try {
      AppsGetLogsResponse result = client
              .apps
              .getActiveDeploymentLogs(appId, componentName, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .execute();
      System.out.println(result);
      System.out.println(result.getHistoricUrls());
      System.out.println(result.getLiveUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getActiveDeploymentLogs");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsGetLogsResponse> response = client
              .apps
              .getActiveDeploymentLogs(appId, componentName, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getActiveDeploymentLogs");
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
| **appId** | **String**| The app ID | |
| **componentName** | **String**| An optional component name. If set, logs will be limited to this component only. | |
| **type** | **String**| The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime | [default to UNSPECIFIED] [enum: UNSPECIFIED, BUILD, DEPLOY, RUN, RUN_RESTARTED] |
| **follow** | **Boolean**| Whether the logs should follow live updates. | [optional] |
| **podConnectionTimeout** | **String**| An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. | [optional] |

### Return type

[**AppsGetLogsResponse**](AppsGetLogsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with urls that point to archived logs |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getActiveDeploymentLogs_0"></a>
# **getActiveDeploymentLogs_0**
> AppsGetLogsResponse getActiveDeploymentLogs_0(appId, type).follow(follow).podConnectionTimeout(podConnectionTimeout).execute();

Retrieve Active Deployment Aggregate Logs

Retrieve the logs of the active deployment if one exists. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment. Note log_type&#x3D;BUILD logs will return logs associated with the current active deployment (being served). To view build logs associated with in-progress build, the query must explicitly reference the deployment id.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String type = "UNSPECIFIED"; // The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime
    Boolean follow = true; // Whether the logs should follow live updates.
    String podConnectionTimeout = "3m"; // An optional time duration to wait if the underlying component instance is not immediately available. Default: `3m`.
    try {
      AppsGetLogsResponse result = client
              .apps
              .getActiveDeploymentLogs_0(appId, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .execute();
      System.out.println(result);
      System.out.println(result.getHistoricUrls());
      System.out.println(result.getLiveUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getActiveDeploymentLogs_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsGetLogsResponse> response = client
              .apps
              .getActiveDeploymentLogs_0(appId, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getActiveDeploymentLogs_0");
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
| **appId** | **String**| The app ID | |
| **type** | **String**| The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime | [default to UNSPECIFIED] [enum: UNSPECIFIED, BUILD, DEPLOY, RUN, RUN_RESTARTED] |
| **follow** | **Boolean**| Whether the logs should follow live updates. | [optional] |
| **podConnectionTimeout** | **String**| An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. | [optional] |

### Return type

[**AppsGetLogsResponse**](AppsGetLogsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with urls that point to archived logs |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getAggregateDeploymentLogs"></a>
# **getAggregateDeploymentLogs**
> AppsGetLogsResponse getAggregateDeploymentLogs(appId, deploymentId, type).follow(follow).podConnectionTimeout(podConnectionTimeout).execute();

Retrieve Aggregate Deployment Logs

Retrieve the logs of a past, in-progress, or active deployment. If a component name is specified, the logs will be limited to only that component. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String deploymentId = "3aa4d20e-5527-4c00-b496-601fbd22520a"; // The deployment ID
    String type = "UNSPECIFIED"; // The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime
    Boolean follow = true; // Whether the logs should follow live updates.
    String podConnectionTimeout = "3m"; // An optional time duration to wait if the underlying component instance is not immediately available. Default: `3m`.
    try {
      AppsGetLogsResponse result = client
              .apps
              .getAggregateDeploymentLogs(appId, deploymentId, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .execute();
      System.out.println(result);
      System.out.println(result.getHistoricUrls());
      System.out.println(result.getLiveUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getAggregateDeploymentLogs");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsGetLogsResponse> response = client
              .apps
              .getAggregateDeploymentLogs(appId, deploymentId, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getAggregateDeploymentLogs");
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
| **appId** | **String**| The app ID | |
| **deploymentId** | **String**| The deployment ID | |
| **type** | **String**| The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime | [default to UNSPECIFIED] [enum: UNSPECIFIED, BUILD, DEPLOY, RUN, RUN_RESTARTED] |
| **follow** | **Boolean**| Whether the logs should follow live updates. | [optional] |
| **podConnectionTimeout** | **String**| An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. | [optional] |

### Return type

[**AppsGetLogsResponse**](AppsGetLogsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with urls that point to archived logs |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getAppDailyBandwidthMetrics"></a>
# **getAppDailyBandwidthMetrics**
> AppMetricsBandwidthUsage getAppDailyBandwidthMetrics(appId).date(date).execute();

Retrieve App Daily Bandwidth Metrics

Retrieve daily bandwidth usage metrics for a single app.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    OffsetDateTime date = OffsetDateTime.parse("2023-01-17T00:00:00Z"); // Optional day to query. Only the date component of the timestamp will be considered. Default: yesterday.
    try {
      AppMetricsBandwidthUsage result = client
              .apps
              .getAppDailyBandwidthMetrics(appId)
              .date(date)
              .execute();
      System.out.println(result);
      System.out.println(result.getAppBandwidthUsage());
      System.out.println(result.getDate());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getAppDailyBandwidthMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppMetricsBandwidthUsage> response = client
              .apps
              .getAppDailyBandwidthMetrics(appId)
              .date(date)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getAppDailyBandwidthMetrics");
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
| **appId** | **String**| The app ID | |
| **date** | **OffsetDateTime**| Optional day to query. Only the date component of the timestamp will be considered. Default: yesterday. | [optional] |

### Return type

[**AppMetricsBandwidthUsage**](AppMetricsBandwidthUsage.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;app_bandwidth_usage&#x60; key |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDeploymentInfo"></a>
# **getDeploymentInfo**
> AppsDeploymentResponse getDeploymentInfo(appId, deploymentId).execute();

Retrieve an App Deployment

Retrieve information about an app deployment.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String deploymentId = "3aa4d20e-5527-4c00-b496-601fbd22520a"; // The deployment ID
    try {
      AppsDeploymentResponse result = client
              .apps
              .getDeploymentInfo(appId, deploymentId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDeployment());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getDeploymentInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsDeploymentResponse> response = client
              .apps
              .getDeploymentInfo(appId, deploymentId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getDeploymentInfo");
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
| **appId** | **String**| The app ID | |
| **deploymentId** | **String**| The deployment ID | |

### Return type

[**AppsDeploymentResponse**](AppsDeploymentResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON of the requested deployment |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDeploymentLogs"></a>
# **getDeploymentLogs**
> AppsGetLogsResponse getDeploymentLogs(appId, deploymentId, componentName, type).follow(follow).podConnectionTimeout(podConnectionTimeout).execute();

Retrieve Deployment Logs

Retrieve the logs of a past, in-progress, or active deployment. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String deploymentId = "3aa4d20e-5527-4c00-b496-601fbd22520a"; // The deployment ID
    String componentName = "component"; // An optional component name. If set, logs will be limited to this component only.
    String type = "UNSPECIFIED"; // The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime
    Boolean follow = true; // Whether the logs should follow live updates.
    String podConnectionTimeout = "3m"; // An optional time duration to wait if the underlying component instance is not immediately available. Default: `3m`.
    try {
      AppsGetLogsResponse result = client
              .apps
              .getDeploymentLogs(appId, deploymentId, componentName, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .execute();
      System.out.println(result);
      System.out.println(result.getHistoricUrls());
      System.out.println(result.getLiveUrl());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getDeploymentLogs");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsGetLogsResponse> response = client
              .apps
              .getDeploymentLogs(appId, deploymentId, componentName, type)
              .follow(follow)
              .podConnectionTimeout(podConnectionTimeout)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getDeploymentLogs");
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
| **appId** | **String**| The app ID | |
| **deploymentId** | **String**| The deployment ID | |
| **componentName** | **String**| An optional component name. If set, logs will be limited to this component only. | |
| **type** | **String**| The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime | [default to UNSPECIFIED] [enum: UNSPECIFIED, BUILD, DEPLOY, RUN, RUN_RESTARTED] |
| **follow** | **Boolean**| Whether the logs should follow live updates. | [optional] |
| **podConnectionTimeout** | **String**| An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. | [optional] |

### Return type

[**AppsGetLogsResponse**](AppsGetLogsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with urls that point to archived logs |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getInstanceSize"></a>
# **getInstanceSize**
> AppsGetInstanceSizeResponse getInstanceSize(slug).execute();

Retrieve an Instance Size

Retrieve information about a specific instance size for &#x60;service&#x60;, &#x60;worker&#x60;, and &#x60;job&#x60; components.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String slug = "basic-xxs"; // The slug of the instance size
    try {
      AppsGetInstanceSizeResponse result = client
              .apps
              .getInstanceSize(slug)
              .execute();
      System.out.println(result);
      System.out.println(result.getInstanceSize());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getInstanceSize");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsGetInstanceSizeResponse> response = client
              .apps
              .getInstanceSize(slug)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getInstanceSize");
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
| **slug** | **String**| The slug of the instance size | |

### Return type

[**AppsGetInstanceSizeResponse**](AppsGetInstanceSizeResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON with key &#x60;instance_size&#x60; |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getMultipleDailyMetrics"></a>
# **getMultipleDailyMetrics**
> AppMetricsBandwidthUsage getMultipleDailyMetrics(appMetricsBandwidthUsageRequest).execute();

Retrieve Multiple Apps&#39; Daily Bandwidth Metrics

Retrieve daily bandwidth usage metrics for multiple apps.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    List<String> appIds = Arrays.asList(); // A list of app IDs to query bandwidth metrics for.
    OffsetDateTime date = OffsetDateTime.now(); // Optional day to query. Only the date component of the timestamp will be considered. Default: yesterday.
    try {
      AppMetricsBandwidthUsage result = client
              .apps
              .getMultipleDailyMetrics(appIds)
              .date(date)
              .execute();
      System.out.println(result);
      System.out.println(result.getAppBandwidthUsage());
      System.out.println(result.getDate());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getMultipleDailyMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppMetricsBandwidthUsage> response = client
              .apps
              .getMultipleDailyMetrics(appIds)
              .date(date)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getMultipleDailyMetrics");
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
| **appMetricsBandwidthUsageRequest** | [**AppMetricsBandwidthUsageRequest**](AppMetricsBandwidthUsageRequest.md)|  | |

### Return type

[**AppMetricsBandwidthUsage**](AppMetricsBandwidthUsage.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;app_bandwidth_usage&#x60; key |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getTierInfo"></a>
# **getTierInfo**
> AppsGetTierResponse getTierInfo(slug).execute();

Retrieve an App Tier

Retrieve information about a specific app tier.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String slug = "basic"; // The slug of the tier
    try {
      AppsGetTierResponse result = client
              .apps
              .getTierInfo(slug)
              .execute();
      System.out.println(result);
      System.out.println(result.getTier());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getTierInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsGetTierResponse> response = client
              .apps
              .getTierInfo(slug)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#getTierInfo");
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
| **slug** | **String**| The slug of the tier | |

### Return type

[**AppsGetTierResponse**](AppsGetTierResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON with the key &#x60;tier&#x60; |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> AppsResponse list().page(page).perPage(perPage).withProjects(withProjects).execute();

List All Apps

List all apps on your account. Information about the current active deployment as well as any in progress ones will also be included for each app.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    Integer page = 1; // Which 'page' of paginated results to return.
    Integer perPage = 20; // Number of items returned per page
    Boolean withProjects = true; // Whether the project_id of listed apps should be fetched and included.
    try {
      AppsResponse result = client
              .apps
              .list()
              .page(page)
              .perPage(perPage)
              .withProjects(withProjects)
              .execute();
      System.out.println(result);
      System.out.println(result.getApps());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsResponse> response = client
              .apps
              .list()
              .page(page)
              .perPage(perPage)
              .withProjects(withProjects)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#list");
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
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **withProjects** | **Boolean**| Whether the project_id of listed apps should be fetched and included. | [optional] |

### Return type

[**AppsResponse**](AppsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;apps&#x60; key. This is list of object &#x60;apps&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listAlerts"></a>
# **listAlerts**
> AppsListAlertsResponse listAlerts(appId).execute();

List all app alerts

List alerts associated to the app and any components. This includes configuration information about the alerts including emails, slack webhooks, and triggering events or conditions.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    try {
      AppsListAlertsResponse result = client
              .apps
              .listAlerts(appId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAlerts());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listAlerts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsListAlertsResponse> response = client
              .apps
              .listAlerts(appId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listAlerts");
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
| **appId** | **String**| The app ID | |

### Return type

[**AppsListAlertsResponse**](AppsListAlertsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;alerts&#x60; key. This is list of object &#x60;alerts&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listDeployments"></a>
# **listDeployments**
> AppsDeploymentsResponse listDeployments(appId).page(page).perPage(perPage).execute();

List App Deployments

List all deployments of an app.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    Integer page = 1; // Which 'page' of paginated results to return.
    Integer perPage = 20; // Number of items returned per page
    try {
      AppsDeploymentsResponse result = client
              .apps
              .listDeployments(appId)
              .page(page)
              .perPage(perPage)
              .execute();
      System.out.println(result);
      System.out.println(result.getDeployments());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listDeployments");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsDeploymentsResponse> response = client
              .apps
              .listDeployments(appId)
              .page(page)
              .perPage(perPage)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listDeployments");
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
| **appId** | **String**| The app ID | |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |

### Return type

[**AppsDeploymentsResponse**](AppsDeploymentsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;deployments&#x60; key. This will be a list of all app deployments |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listInstanceSizes"></a>
# **listInstanceSizes**
> AppsListInstanceSizesResponse listInstanceSizes().execute();

List Instance Sizes

List all instance sizes for &#x60;service&#x60;, &#x60;worker&#x60;, and &#x60;job&#x60; components.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
      AppsListInstanceSizesResponse result = client
              .apps
              .listInstanceSizes()
              .execute();
      System.out.println(result);
      System.out.println(result.getDiscountPercent());
      System.out.println(result.getInstanceSizes());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listInstanceSizes");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsListInstanceSizesResponse> response = client
              .apps
              .listInstanceSizes()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listInstanceSizes");
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

[**AppsListInstanceSizesResponse**](AppsListInstanceSizesResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON with key &#x60;instance_sizes&#x60; |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listRegions"></a>
# **listRegions**
> AppsListRegionsResponse listRegions().execute();

List App Regions

List all regions supported by App Platform.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
      AppsListRegionsResponse result = client
              .apps
              .listRegions()
              .execute();
      System.out.println(result);
      System.out.println(result.getRegions());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listRegions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsListRegionsResponse> response = client
              .apps
              .listRegions()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listRegions");
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

[**AppsListRegionsResponse**](AppsListRegionsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with key &#x60;regions&#x60; |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listTiers"></a>
# **listTiers**
> AppsListTiersResponse listTiers().execute();

List App Tiers

List all app tiers.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
      AppsListTiersResponse result = client
              .apps
              .listTiers()
              .execute();
      System.out.println(result);
      System.out.println(result.getTiers());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listTiers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsListTiersResponse> response = client
              .apps
              .listTiers()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#listTiers");
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

[**AppsListTiersResponse**](AppsListTiersResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;tiers&#x60; key. This will be a list of all app tiers |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="proposeAppSpec"></a>
# **proposeAppSpec**
> AppProposeResponse proposeAppSpec(appPropose).execute();

Propose an App Spec

To propose and validate a spec for a new or existing app, send a POST request to the &#x60;/v2/apps/propose&#x60; endpoint. The request returns some information about the proposed app, including app cost and upgrade cost. If an existing app ID is specified, the app spec is treated as a proposed update to the existing app.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    AppSpec spec = new AppSpec();
    String appId = "appId_example"; // An optional ID of an existing app. If set, the spec will be treated as a proposed update to the specified app. The existing app is not modified using this method.
    try {
      AppProposeResponse result = client
              .apps
              .proposeAppSpec(spec)
              .appId(appId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAppIsStatic());
      System.out.println(result.getAppNameAvailable());
      System.out.println(result.getAppNameSuggestion());
      System.out.println(result.getExistingStaticApps());
      System.out.println(result.getSpec());
      System.out.println(result.getAppCost());
      System.out.println(result.getAppTierDowngradeCost());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#proposeAppSpec");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppProposeResponse> response = client
              .apps
              .proposeAppSpec(spec)
              .appId(appId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#proposeAppSpec");
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
| **appPropose** | [**AppPropose**](AppPropose.md)|  | |

### Return type

[**AppProposeResponse**](AppProposeResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="revertRollback"></a>
# **revertRollback**
> AppsDeploymentResponse revertRollback(appId).execute();

Revert App Rollback

Revert an app rollback. This action reverts the active rollback by creating a new deployment from the latest app spec prior to the rollback and unpins the app to resume new deployments. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    try {
      AppsDeploymentResponse result = client
              .apps
              .revertRollback(appId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDeployment());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#revertRollback");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsDeploymentResponse> response = client
              .apps
              .revertRollback(appId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#revertRollback");
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
| **appId** | **String**| The app ID | |

### Return type

[**AppsDeploymentResponse**](AppsDeploymentResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;deployment&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="rollbackDeployment"></a>
# **rollbackDeployment**
> AppsDeploymentResponse rollbackDeployment(appId, appsRollbackAppRequest).execute();

Rollback App

Rollback an app to a previous deployment. A new deployment will be created to perform the rollback. The app will be pinned to the rollback deployment preventing any new deployments from being created, either manually or through Auto Deploy on Push webhooks. To resume deployments, the rollback must be either committed or reverted.  It is recommended to use the Validate App Rollback endpoint to double check if the rollback is valid and if there are any warnings. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String deploymentId = "deploymentId_example"; // The ID of the deployment to rollback to.
    Boolean skipPin = true; // Whether to skip pinning the rollback deployment. If false, the rollback deployment will be pinned and any new deployments including Auto Deploy on Push hooks will be disabled until the rollback is either manually committed or reverted via the CommitAppRollback or RevertAppRollback endpoints respectively. If true, the rollback will be immediately committed and the app will remain unpinned.
    try {
      AppsDeploymentResponse result = client
              .apps
              .rollbackDeployment(appId)
              .deploymentId(deploymentId)
              .skipPin(skipPin)
              .execute();
      System.out.println(result);
      System.out.println(result.getDeployment());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#rollbackDeployment");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsDeploymentResponse> response = client
              .apps
              .rollbackDeployment(appId)
              .deploymentId(deploymentId)
              .skipPin(skipPin)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#rollbackDeployment");
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
| **appId** | **String**| The app ID | |
| **appsRollbackAppRequest** | [**AppsRollbackAppRequest**](AppsRollbackAppRequest.md)|  | |

### Return type

[**AppsDeploymentResponse**](AppsDeploymentResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a &#x60;deployment&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="update"></a>
# **update**
> AppResponse update(id, appsUpdateAppRequest).execute();

Update an App

Update an existing app by submitting a new app specification. For documentation on app specifications (&#x60;AppSpec&#x60; objects), please refer to [the product documentation](https://docs.digitalocean.com/products/app-platform/reference/app-spec/).

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    AppSpec spec = new AppSpec();
    String id = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The ID of the app
    try {
      AppResponse result = client
              .apps
              .update(spec, id)
              .execute();
      System.out.println(result);
      System.out.println(result.getApp());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppResponse> response = client
              .apps
              .update(spec, id)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#update");
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
| **id** | **String**| The ID of the app | |
| **appsUpdateAppRequest** | [**AppsUpdateAppRequest**](AppsUpdateAppRequest.md)|  | |

### Return type

[**AppResponse**](AppResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object of the updated &#x60;app&#x60; |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateDestinationsForAlerts"></a>
# **updateDestinationsForAlerts**
> AppsAlertResponse updateDestinationsForAlerts(appId, alertId, appsAssignAppAlertDestinationsRequest).execute();

Update destinations for alerts

Updates the emails and slack webhook destinations for app alerts. Emails must be associated to a user with access to the app.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String alertId = "5a624ab5-dd58-4b39-b7dd-8b7c36e8a91d"; // The alert ID
    List<String> emails = Arrays.asList("""");
    List<AppAlertSlackWebhook> slackWebhooks = Arrays.asList();
    try {
      AppsAlertResponse result = client
              .apps
              .updateDestinationsForAlerts(appId, alertId)
              .emails(emails)
              .slackWebhooks(slackWebhooks)
              .execute();
      System.out.println(result);
      System.out.println(result.getAlert());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#updateDestinationsForAlerts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsAlertResponse> response = client
              .apps
              .updateDestinationsForAlerts(appId, alertId)
              .emails(emails)
              .slackWebhooks(slackWebhooks)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#updateDestinationsForAlerts");
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
| **appId** | **String**| The app ID | |
| **alertId** | **String**| The alert ID | |
| **appsAssignAppAlertDestinationsRequest** | [**AppsAssignAppAlertDestinationsRequest**](AppsAssignAppAlertDestinationsRequest.md)|  | |

### Return type

[**AppsAlertResponse**](AppsAlertResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with an &#x60;alert&#x60; key. This is an object of type &#x60;alert&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="validateRollback"></a>
# **validateRollback**
> AppsValidateRollbackResponse validateRollback(appId, appsRollbackAppRequest).execute();

Validate App Rollback

Check whether an app can be rolled back to a specific deployment. This endpoint can also be used to check if there are any warnings or validation conditions that will cause the rollback to proceed under unideal circumstances. For example, if a component must be rebuilt as part of the rollback causing it to take longer than usual. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.AppsApi;
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
    String appId = "4f6c71e2-1e90-4762-9fee-6cc4a0a9f2cf"; // The app ID
    String deploymentId = "deploymentId_example"; // The ID of the deployment to rollback to.
    Boolean skipPin = true; // Whether to skip pinning the rollback deployment. If false, the rollback deployment will be pinned and any new deployments including Auto Deploy on Push hooks will be disabled until the rollback is either manually committed or reverted via the CommitAppRollback or RevertAppRollback endpoints respectively. If true, the rollback will be immediately committed and the app will remain unpinned.
    try {
      AppsValidateRollbackResponse result = client
              .apps
              .validateRollback(appId)
              .deploymentId(deploymentId)
              .skipPin(skipPin)
              .execute();
      System.out.println(result);
      System.out.println(result.getValid());
      System.out.println(result.getError());
      System.out.println(result.getWarnings());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#validateRollback");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AppsValidateRollbackResponse> response = client
              .apps
              .validateRollback(appId)
              .deploymentId(deploymentId)
              .skipPin(skipPin)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling AppsApi#validateRollback");
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
| **appId** | **String**| The app ID | |
| **appsRollbackAppRequest** | [**AppsRollbackAppRequest**](AppsRollbackAppRequest.md)|  | |

### Return type

[**AppsValidateRollbackResponse**](AppsValidateRollbackResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with the validation results. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

