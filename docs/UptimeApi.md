# UptimeApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createCheck**](UptimeApi.md#createCheck) | **POST** /v2/uptime/checks | Create a New Check |
| [**createNewAlert**](UptimeApi.md#createNewAlert) | **POST** /v2/uptime/checks/{check_id}/alerts | Create a New Alert |
| [**deleteAlert**](UptimeApi.md#deleteAlert) | **DELETE** /v2/uptime/checks/{check_id}/alerts/{alert_id} | Delete an Alert |
| [**deleteCheck**](UptimeApi.md#deleteCheck) | **DELETE** /v2/uptime/checks/{check_id} | Delete a Check |
| [**getCheckById**](UptimeApi.md#getCheckById) | **GET** /v2/uptime/checks/{check_id} | Retrieve an Existing Check |
| [**getCheckState**](UptimeApi.md#getCheckState) | **GET** /v2/uptime/checks/{check_id}/state | Retrieve Check State |
| [**getExistingAlert**](UptimeApi.md#getExistingAlert) | **GET** /v2/uptime/checks/{check_id}/alerts/{alert_id} | Retrieve an Existing Alert |
| [**listAllAlerts**](UptimeApi.md#listAllAlerts) | **GET** /v2/uptime/checks/{check_id}/alerts | List All Alerts |
| [**listChecks**](UptimeApi.md#listChecks) | **GET** /v2/uptime/checks | List All Checks |
| [**updateAlertSettings**](UptimeApi.md#updateAlertSettings) | **PUT** /v2/uptime/checks/{check_id}/alerts/{alert_id} | Update an Alert |
| [**updateCheckSettings**](UptimeApi.md#updateCheckSettings) | **PUT** /v2/uptime/checks/{check_id} | Update a Check |


<a name="createCheck"></a>
# **createCheck**
> UptimeCreateCheckResponse createCheck(checkUpdatable).execute();

Create a New Check

To create an Uptime check, send a POST request to &#x60;/v2/uptime/checks&#x60; specifying the attributes in the table below in the JSON body. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    String name = "name_example"; // A human-friendly display name.
    String type = "ping"; // The type of health check to perform.
    String target = "target_example"; // The endpoint to perform healthchecks on.
    List<String> regions = Arrays.asList(); // An array containing the selected regions to perform healthchecks from.
    Boolean enabled = true; // A boolean value indicating whether the check is enabled/disabled.
    try {
      UptimeCreateCheckResponse result = client
              .uptime
              .createCheck()
              .name(name)
              .type(type)
              .target(target)
              .regions(regions)
              .enabled(enabled)
              .execute();
      System.out.println(result);
      System.out.println(result.getCheck());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#createCheck");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeCreateCheckResponse> response = client
              .uptime
              .createCheck()
              .name(name)
              .type(type)
              .target(target)
              .regions(regions)
              .enabled(enabled)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#createCheck");
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
| **checkUpdatable** | [**CheckUpdatable**](CheckUpdatable.md)|  | |

### Return type

[**UptimeCreateCheckResponse**](UptimeCreateCheckResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="createNewAlert"></a>
# **createNewAlert**
> UptimeCreateNewAlertResponse createNewAlert(checkId, alert).execute();

Create a New Alert

To create an Uptime alert, send a POST request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts&#x60; specifying the attributes in the table below in the JSON body. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    UUID id = UUID.randomUUID(); // A unique ID that can be used to identify and reference the alert.
    String name = "name_example"; // A human-friendly display name.
    String type = "latency"; // The type of alert.
    Integer threshold = 56; // The threshold at which the alert will enter a trigger state. The specific threshold is dependent on the alert type.
    String comparison = "greater_than"; // The comparison operator used against the alert's threshold.
    Notification notifications = new Notification();
    String period = "2m"; // Period of time the threshold must be exceeded to trigger the alert.
    try {
      UptimeCreateNewAlertResponse result = client
              .uptime
              .createNewAlert(checkId)
              .id(id)
              .name(name)
              .type(type)
              .threshold(threshold)
              .comparison(comparison)
              .notifications(notifications)
              .period(period)
              .execute();
      System.out.println(result);
      System.out.println(result.getAlert());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#createNewAlert");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeCreateNewAlertResponse> response = client
              .uptime
              .createNewAlert(checkId)
              .id(id)
              .name(name)
              .type(type)
              .threshold(threshold)
              .comparison(comparison)
              .notifications(notifications)
              .period(period)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#createNewAlert");
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
| **checkId** | **UUID**| A unique identifier for a check. | |
| **alert** | [**Alert**](Alert.md)| The &#39;&#39;type&#39;&#39; field dictates the type of alert, and hence what type of value to pass into the threshold property. Type | Description | Threshold Value -----|-------------|-------------------- &#x60;latency&#x60; | alerts on the response latency | milliseconds &#x60;down&#x60; | alerts on a target registering as down in any region | N/A (Not required) &#x60;down_global&#x60; | alerts on a target registering as down globally | N/A (Not required) &#x60;ssl_expiry&#x60; | alerts on a SSL certificate expiring within $threshold days | days  | |

### Return type

[**UptimeCreateNewAlertResponse**](UptimeCreateNewAlertResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteAlert"></a>
# **deleteAlert**
> deleteAlert(checkId, alertId).execute();

Delete an Alert

To delete an Uptime alert, send a DELETE request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts/$ALERT_ID&#x60;. A 204 status code with no body will be returned in response to a successful request. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    UUID alertId = UUID.fromString("17f0f0ae-b7e5-4ef6-86e3-aa569db58284"); // A unique identifier for an alert.
    try {
      client
              .uptime
              .deleteAlert(checkId, alertId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#deleteAlert");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .uptime
              .deleteAlert(checkId, alertId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#deleteAlert");
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
| **checkId** | **UUID**| A unique identifier for a check. | |
| **alertId** | **UUID**| A unique identifier for an alert. | |

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

<a name="deleteCheck"></a>
# **deleteCheck**
> deleteCheck(checkId).execute();

Delete a Check

To delete an Uptime check, send a DELETE request to &#x60;/v2/uptime/checks/$CHECK_ID&#x60;. A 204 status code with no body will be returned in response to a successful request.   Deleting a check will also delete alerts associated with the check. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    try {
      client
              .uptime
              .deleteCheck(checkId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#deleteCheck");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .uptime
              .deleteCheck(checkId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#deleteCheck");
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
| **checkId** | **UUID**| A unique identifier for a check. | |

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

<a name="getCheckById"></a>
# **getCheckById**
> UptimeCreateCheckResponse getCheckById(checkId).execute();

Retrieve an Existing Check

To show information about an existing check, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    try {
      UptimeCreateCheckResponse result = client
              .uptime
              .getCheckById(checkId)
              .execute();
      System.out.println(result);
      System.out.println(result.getCheck());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#getCheckById");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeCreateCheckResponse> response = client
              .uptime
              .getCheckById(checkId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#getCheckById");
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
| **checkId** | **UUID**| A unique identifier for a check. | |

### Return type

[**UptimeCreateCheckResponse**](UptimeCreateCheckResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getCheckState"></a>
# **getCheckState**
> UptimeGetCheckStateResponse getCheckState(checkId).execute();

Retrieve Check State

To show information about an existing check&#39;s state, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID/state&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    try {
      UptimeGetCheckStateResponse result = client
              .uptime
              .getCheckState(checkId)
              .execute();
      System.out.println(result);
      System.out.println(result.getState());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#getCheckState");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeGetCheckStateResponse> response = client
              .uptime
              .getCheckState(checkId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#getCheckState");
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
| **checkId** | **UUID**| A unique identifier for a check. | |

### Return type

[**UptimeGetCheckStateResponse**](UptimeGetCheckStateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;state&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check&#39;s state. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getExistingAlert"></a>
# **getExistingAlert**
> UptimeCreateNewAlertResponse getExistingAlert(checkId, alertId).execute();

Retrieve an Existing Alert

To show information about an existing alert, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts/$ALERT_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    UUID alertId = UUID.fromString("17f0f0ae-b7e5-4ef6-86e3-aa569db58284"); // A unique identifier for an alert.
    try {
      UptimeCreateNewAlertResponse result = client
              .uptime
              .getExistingAlert(checkId, alertId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAlert());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#getExistingAlert");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeCreateNewAlertResponse> response = client
              .uptime
              .getExistingAlert(checkId, alertId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#getExistingAlert");
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
| **checkId** | **UUID**| A unique identifier for a check. | |
| **alertId** | **UUID**| A unique identifier for an alert. | |

### Return type

[**UptimeCreateNewAlertResponse**](UptimeCreateNewAlertResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listAllAlerts"></a>
# **listAllAlerts**
> UptimeListAllAlertsResponse listAllAlerts(checkId).perPage(perPage).page(page).execute();

List All Alerts

To list all of the alerts for an Uptime check, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      UptimeListAllAlertsResponse result = client
              .uptime
              .listAllAlerts(checkId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getAlerts());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#listAllAlerts");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeListAllAlertsResponse> response = client
              .uptime
              .listAllAlerts(checkId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#listAllAlerts");
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
| **checkId** | **UUID**| A unique identifier for a check. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**UptimeListAllAlertsResponse**](UptimeListAllAlertsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;alerts&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime alert. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listChecks"></a>
# **listChecks**
> UptimeListChecksResponse listChecks().perPage(perPage).page(page).execute();

List All Checks

To list all of the Uptime checks on your account, send a GET request to &#x60;/v2/uptime/checks&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
      UptimeListChecksResponse result = client
              .uptime
              .listChecks()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getChecks());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#listChecks");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeListChecksResponse> response = client
              .uptime
              .listChecks()
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#listChecks");
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

[**UptimeListChecksResponse**](UptimeListChecksResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;checks&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime check |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateAlertSettings"></a>
# **updateAlertSettings**
> UptimeCreateNewAlertResponse updateAlertSettings(checkId, alertId, alertUpdatable).execute();

Update an Alert

To update the settings of an Uptime alert, send a PUT request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts/$ALERT_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    UUID alertId = UUID.fromString("17f0f0ae-b7e5-4ef6-86e3-aa569db58284"); // A unique identifier for an alert.
    String name = "name_example"; // A human-friendly display name.
    String type = "latency"; // The type of alert.
    Integer threshold = 56; // The threshold at which the alert will enter a trigger state. The specific threshold is dependent on the alert type.
    String comparison = "greater_than"; // The comparison operator used against the alert's threshold.
    Notification notifications = new Notification();
    String period = "2m"; // Period of time the threshold must be exceeded to trigger the alert.
    try {
      UptimeCreateNewAlertResponse result = client
              .uptime
              .updateAlertSettings(checkId, alertId)
              .name(name)
              .type(type)
              .threshold(threshold)
              .comparison(comparison)
              .notifications(notifications)
              .period(period)
              .execute();
      System.out.println(result);
      System.out.println(result.getAlert());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#updateAlertSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeCreateNewAlertResponse> response = client
              .uptime
              .updateAlertSettings(checkId, alertId)
              .name(name)
              .type(type)
              .threshold(threshold)
              .comparison(comparison)
              .notifications(notifications)
              .period(period)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#updateAlertSettings");
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
| **checkId** | **UUID**| A unique identifier for a check. | |
| **alertId** | **UUID**| A unique identifier for an alert. | |
| **alertUpdatable** | [**AlertUpdatable**](AlertUpdatable.md)|  | |

### Return type

[**UptimeCreateNewAlertResponse**](UptimeCreateNewAlertResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateCheckSettings"></a>
# **updateCheckSettings**
> UptimeCreateCheckResponse updateCheckSettings(checkId, checkUpdatable).execute();

Update a Check

To update the settings of an Uptime check, send a PUT request to &#x60;/v2/uptime/checks/$CHECK_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.UptimeApi;
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
    UUID checkId = UUID.fromString("4de7ac8b-495b-4884-9a69-1050c6793cd6"); // A unique identifier for a check.
    String name = "name_example"; // A human-friendly display name.
    String type = "ping"; // The type of health check to perform.
    String target = "target_example"; // The endpoint to perform healthchecks on.
    List<String> regions = Arrays.asList(); // An array containing the selected regions to perform healthchecks from.
    Boolean enabled = true; // A boolean value indicating whether the check is enabled/disabled.
    try {
      UptimeCreateCheckResponse result = client
              .uptime
              .updateCheckSettings(checkId)
              .name(name)
              .type(type)
              .target(target)
              .regions(regions)
              .enabled(enabled)
              .execute();
      System.out.println(result);
      System.out.println(result.getCheck());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#updateCheckSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<UptimeCreateCheckResponse> response = client
              .uptime
              .updateCheckSettings(checkId)
              .name(name)
              .type(type)
              .target(target)
              .regions(regions)
              .enabled(enabled)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling UptimeApi#updateCheckSettings");
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
| **checkId** | **UUID**| A unique identifier for a check. | |
| **checkUpdatable** | [**CheckUpdatable**](CheckUpdatable.md)|  | |

### Return type

[**UptimeCreateCheckResponse**](UptimeCreateCheckResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

