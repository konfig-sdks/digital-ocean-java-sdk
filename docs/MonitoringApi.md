# MonitoringApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createAlertPolicy**](MonitoringApi.md#createAlertPolicy) | **POST** /v2/monitoring/alerts | Create Alert Policy |
| [**deleteAlertPolicy**](MonitoringApi.md#deleteAlertPolicy) | **DELETE** /v2/monitoring/alerts/{alert_uuid} | Delete an Alert Policy |
| [**dropletCpuMetricsget**](MonitoringApi.md#dropletCpuMetricsget) | **GET** /v2/monitoring/metrics/droplet/cpu | Get Droplet CPU Metrics |
| [**dropletLoad5MetricsGet**](MonitoringApi.md#dropletLoad5MetricsGet) | **GET** /v2/monitoring/metrics/droplet/load_5 | Get Droplet Load5 Metrics |
| [**dropletMemoryCachedMetrics**](MonitoringApi.md#dropletMemoryCachedMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_cached | Get Droplet Cached Memory Metrics |
| [**getAppCpuPercentageMetrics**](MonitoringApi.md#getAppCpuPercentageMetrics) | **GET** /v2/monitoring/metrics/apps/cpu_percentage | Get App CPU Percentage Metrics |
| [**getAppMemoryPercentageMetrics**](MonitoringApi.md#getAppMemoryPercentageMetrics) | **GET** /v2/monitoring/metrics/apps/memory_percentage | Get App Memory Percentage Metrics |
| [**getAppRestartCountMetrics**](MonitoringApi.md#getAppRestartCountMetrics) | **GET** /v2/monitoring/metrics/apps/restart_count | Get App Restart Count Metrics |
| [**getDropletBandwidthMetrics**](MonitoringApi.md#getDropletBandwidthMetrics) | **GET** /v2/monitoring/metrics/droplet/bandwidth | Get Droplet Bandwidth Metrics |
| [**getDropletFilesystemFreeMetrics**](MonitoringApi.md#getDropletFilesystemFreeMetrics) | **GET** /v2/monitoring/metrics/droplet/filesystem_free | Get Droplet Filesystem Free Metrics |
| [**getDropletFilesystemSizeMetrics**](MonitoringApi.md#getDropletFilesystemSizeMetrics) | **GET** /v2/monitoring/metrics/droplet/filesystem_size | Get Droplet Filesystem Size Metrics |
| [**getDropletLoad15Metrics**](MonitoringApi.md#getDropletLoad15Metrics) | **GET** /v2/monitoring/metrics/droplet/load_15 | Get Droplet Load15 Metrics |
| [**getDropletLoad1Metrics**](MonitoringApi.md#getDropletLoad1Metrics) | **GET** /v2/monitoring/metrics/droplet/load_1 | Get Droplet Load1 Metrics |
| [**getDropletMemoryAvailableMetrics**](MonitoringApi.md#getDropletMemoryAvailableMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_available | Get Droplet Available Memory Metrics |
| [**getDropletMemoryFreeMetrics**](MonitoringApi.md#getDropletMemoryFreeMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_free | Get Droplet Free Memory Metrics |
| [**getDropletMemoryTotalMetrics**](MonitoringApi.md#getDropletMemoryTotalMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_total | Get Droplet Total Memory Metrics |
| [**getExistingAlertPolicy**](MonitoringApi.md#getExistingAlertPolicy) | **GET** /v2/monitoring/alerts/{alert_uuid} | Retrieve an Existing Alert Policy |
| [**listAlertPolicies**](MonitoringApi.md#listAlertPolicies) | **GET** /v2/monitoring/alerts | List Alert Policies |
| [**updateAlertPolicy**](MonitoringApi.md#updateAlertPolicy) | **PUT** /v2/monitoring/alerts/{alert_uuid} | Update an Alert Policy |


<a name="createAlertPolicy"></a>
# **createAlertPolicy**
> Object createAlertPolicy(alertPolicyRequest).execute();

Create Alert Policy

To create a new alert, send a POST request to &#x60;/v2/monitoring/alerts&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    List<String> tags = Arrays.asList();
    String description = "description_example";
    Alerts alerts = new Alerts();
    String compare = "GreaterThan";
    Boolean enabled = true;
    List<String> entities = Arrays.asList();
    String type = "v1/insights/droplet/load_1";
    Float value = 3.4F;
    String window = "5m";
    try {
      Object result = client
              .monitoring
              .createAlertPolicy(tags, description, alerts, compare, enabled, entities, type, value, window)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#createAlertPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .monitoring
              .createAlertPolicy(tags, description, alerts, compare, enabled, entities, type, value, window)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#createAlertPolicy");
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
| **alertPolicyRequest** | [**AlertPolicyRequest**](AlertPolicyRequest.md)| The &#x60;type&#x60; field dictates what type of entity that the alert policy applies to and hence what type of entity is passed in the &#x60;entities&#x60; array. If both the &#x60;tags&#x60; array and &#x60;entities&#x60; array are empty the alert policy applies to all entities of the relevant type that are owned by the user account. Otherwise the following table shows the valid entity types for each type of alert policy:  Type | Description | Valid Entity Type -----|-------------|-------------------- &#x60;v1/insights/droplet/memory_utilization_percent&#x60; | alert on the percent of memory utilization | Droplet ID &#x60;v1/insights/droplet/disk_read&#x60; | alert on the rate of disk read I/O in MBps | Droplet ID &#x60;v1/insights/droplet/load_5&#x60; | alert on the 5 minute load average | Droplet ID &#x60;v1/insights/droplet/load_15&#x60; | alert on the 15 minute load average | Droplet ID &#x60;v1/insights/droplet/disk_utilization_percent&#x60; | alert on the percent of disk utilization | Droplet ID &#x60;v1/insights/droplet/cpu&#x60; | alert on the percent of CPU utilization | Droplet ID &#x60;v1/insights/droplet/disk_write&#x60; | alert on the rate of disk write I/O in MBps | Droplet ID &#x60;v1/insights/droplet/public_outbound_bandwidth&#x60; | alert on the rate of public outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/public_inbound_bandwidth&#x60; | alert on the rate of public inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_outbound_bandwidth&#x60; | alert on the rate of private outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_inbound_bandwidth&#x60; | alert on the rate of private inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/load_1&#x60; | alert on the 1 minute load average | Droplet ID &#x60;v1/insights/lbaas/avg_cpu_utilization_percent&#x60;|alert on the percent of CPU utilization|load balancer ID &#x60;v1/insights/lbaas/connection_utilization_percent&#x60;|alert on the percent of connection utilization|load balancer ID &#x60;v1/insights/lbaas/droplet_health&#x60;|alert on Droplet health status changes|load balancer ID &#x60;v1/insights/lbaas/tls_connections_per_second_utilization_percent&#x60;|alert on the percent of TLS connections per second utilization|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_5xx&#x60;|alert on the percent increase of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_4xx&#x60;|alert on the percent increase of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_5xx&#x60;|alert on the count of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_4xx&#x60;|alert on the count of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time&#x60;|alert on high average http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_50p&#x60;|alert on high 50th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_95p&#x60;|alert on high 95th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_99p&#x60;|alert on high 99th percentile http response time|load balancer ID &#x60;v1/dbaas/alerts/load_15_alerts&#x60; | alert on 15 minute load average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/memory_utilization_alerts&#x60; | alert on the percent memory utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/disk_utilization_alerts&#x60; | alert on the percent disk utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/cpu_alerts&#x60; | alert on the percent CPU usage average across the database cluster | database cluster UUID  | |

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
| **200** | An alert policy. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteAlertPolicy"></a>
# **deleteAlertPolicy**
> deleteAlertPolicy(alertUuid).execute();

Delete an Alert Policy

To delete an alert policy, send a DELETE request to &#x60;/v2/monitoring/alerts/{alert_uuid}&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String alertUuid = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for an alert policy.
    try {
      client
              .monitoring
              .deleteAlertPolicy(alertUuid)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#deleteAlertPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .monitoring
              .deleteAlertPolicy(alertUuid)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#deleteAlertPolicy");
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
| **alertUuid** | **String**| A unique identifier for an alert policy. | |

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

<a name="dropletCpuMetricsget"></a>
# **dropletCpuMetricsget**
> Metrics dropletCpuMetricsget(hostId, start, end).execute();

Get Droplet CPU Metrics

To retrieve CPU metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/cpu&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .dropletCpuMetricsget(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#dropletCpuMetricsget");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .dropletCpuMetricsget(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#dropletCpuMetricsget");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="dropletLoad5MetricsGet"></a>
# **dropletLoad5MetricsGet**
> Metrics dropletLoad5MetricsGet(hostId, start, end).execute();

Get Droplet Load5 Metrics

To retrieve 5 minute load average metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/load_5&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .dropletLoad5MetricsGet(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#dropletLoad5MetricsGet");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .dropletLoad5MetricsGet(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#dropletLoad5MetricsGet");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="dropletMemoryCachedMetrics"></a>
# **dropletMemoryCachedMetrics**
> Metrics dropletMemoryCachedMetrics(hostId, start, end).execute();

Get Droplet Cached Memory Metrics

To retrieve cached memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_cached&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .dropletMemoryCachedMetrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#dropletMemoryCachedMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .dropletMemoryCachedMetrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#dropletMemoryCachedMetrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getAppCpuPercentageMetrics"></a>
# **getAppCpuPercentageMetrics**
> Metrics getAppCpuPercentageMetrics(appId, start, end).appComponent(appComponent).execute();

Get App CPU Percentage Metrics

To retrieve cpu percentage metrics for a given app, send a GET request to &#x60;/v2/monitoring/metrics/apps/cpu_percentage&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String appId = "2db3c021-15ad-4088-bfe8-99dc972b9cf6"; // The app UUID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    String appComponent = "sample-application"; // The app component name.
    try {
      Metrics result = client
              .monitoring
              .getAppCpuPercentageMetrics(appId, start, end)
              .appComponent(appComponent)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getAppCpuPercentageMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getAppCpuPercentageMetrics(appId, start, end)
              .appComponent(appComponent)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getAppCpuPercentageMetrics");
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
| **appId** | **String**| The app UUID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |
| **appComponent** | **String**| The app component name. | [optional] |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getAppMemoryPercentageMetrics"></a>
# **getAppMemoryPercentageMetrics**
> Metrics getAppMemoryPercentageMetrics(appId, start, end).appComponent(appComponent).execute();

Get App Memory Percentage Metrics

To retrieve memory percentage metrics for a given app, send a GET request to &#x60;/v2/monitoring/metrics/apps/memory_percentage&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String appId = "2db3c021-15ad-4088-bfe8-99dc972b9cf6"; // The app UUID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    String appComponent = "sample-application"; // The app component name.
    try {
      Metrics result = client
              .monitoring
              .getAppMemoryPercentageMetrics(appId, start, end)
              .appComponent(appComponent)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getAppMemoryPercentageMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getAppMemoryPercentageMetrics(appId, start, end)
              .appComponent(appComponent)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getAppMemoryPercentageMetrics");
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
| **appId** | **String**| The app UUID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |
| **appComponent** | **String**| The app component name. | [optional] |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getAppRestartCountMetrics"></a>
# **getAppRestartCountMetrics**
> Metrics getAppRestartCountMetrics(appId, start, end).appComponent(appComponent).execute();

Get App Restart Count Metrics

To retrieve restart count metrics for a given app, send a GET request to &#x60;/v2/monitoring/metrics/apps/restart_count&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String appId = "2db3c021-15ad-4088-bfe8-99dc972b9cf6"; // The app UUID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    String appComponent = "sample-application"; // The app component name.
    try {
      Metrics result = client
              .monitoring
              .getAppRestartCountMetrics(appId, start, end)
              .appComponent(appComponent)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getAppRestartCountMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getAppRestartCountMetrics(appId, start, end)
              .appComponent(appComponent)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getAppRestartCountMetrics");
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
| **appId** | **String**| The app UUID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |
| **appComponent** | **String**| The app component name. | [optional] |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletBandwidthMetrics"></a>
# **getDropletBandwidthMetrics**
> Metrics getDropletBandwidthMetrics(hostId, _interface, direction, start, end).execute();

Get Droplet Bandwidth Metrics

To retrieve bandwidth metrics for a given Droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/bandwidth&#x60;. Use the &#x60;interface&#x60; query parameter to specify if the results should be for the &#x60;private&#x60; or &#x60;public&#x60; interface. Use the &#x60;direction&#x60; query parameter to specify if the results should be for &#x60;inbound&#x60; or &#x60;outbound&#x60; traffic.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String _interface = "private"; // The network interface.
    String direction = "inbound"; // The traffic direction.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletBandwidthMetrics(hostId, _interface, direction, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletBandwidthMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletBandwidthMetrics(hostId, _interface, direction, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletBandwidthMetrics");
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
| **hostId** | **String**| The droplet ID. | |
| **_interface** | **String**| The network interface. | [enum: private, public] |
| **direction** | **String**| The traffic direction. | [enum: inbound, outbound] |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletFilesystemFreeMetrics"></a>
# **getDropletFilesystemFreeMetrics**
> Metrics getDropletFilesystemFreeMetrics(hostId, start, end).execute();

Get Droplet Filesystem Free Metrics

To retrieve filesystem free metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/filesystem_free&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletFilesystemFreeMetrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletFilesystemFreeMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletFilesystemFreeMetrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletFilesystemFreeMetrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletFilesystemSizeMetrics"></a>
# **getDropletFilesystemSizeMetrics**
> Metrics getDropletFilesystemSizeMetrics(hostId, start, end).execute();

Get Droplet Filesystem Size Metrics

To retrieve filesystem size metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/filesystem_size&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletFilesystemSizeMetrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletFilesystemSizeMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletFilesystemSizeMetrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletFilesystemSizeMetrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletLoad15Metrics"></a>
# **getDropletLoad15Metrics**
> Metrics getDropletLoad15Metrics(hostId, start, end).execute();

Get Droplet Load15 Metrics

To retrieve 15 minute load average metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/load_15&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletLoad15Metrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletLoad15Metrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletLoad15Metrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletLoad15Metrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletLoad1Metrics"></a>
# **getDropletLoad1Metrics**
> Metrics getDropletLoad1Metrics(hostId, start, end).execute();

Get Droplet Load1 Metrics

To retrieve 1 minute load average metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/load_1&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletLoad1Metrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletLoad1Metrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletLoad1Metrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletLoad1Metrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletMemoryAvailableMetrics"></a>
# **getDropletMemoryAvailableMetrics**
> Metrics getDropletMemoryAvailableMetrics(hostId, start, end).execute();

Get Droplet Available Memory Metrics

To retrieve available memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_available&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletMemoryAvailableMetrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletMemoryAvailableMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletMemoryAvailableMetrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletMemoryAvailableMetrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletMemoryFreeMetrics"></a>
# **getDropletMemoryFreeMetrics**
> Metrics getDropletMemoryFreeMetrics(hostId, start, end).execute();

Get Droplet Free Memory Metrics

To retrieve free memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_free&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletMemoryFreeMetrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletMemoryFreeMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletMemoryFreeMetrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletMemoryFreeMetrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getDropletMemoryTotalMetrics"></a>
# **getDropletMemoryTotalMetrics**
> Metrics getDropletMemoryTotalMetrics(hostId, start, end).execute();

Get Droplet Total Memory Metrics

To retrieve total memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_total&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String hostId = "17209102"; // The droplet ID.
    String start = "1620683817"; // Timestamp to start metric window.
    String end = "1620705417"; // Timestamp to end metric window.
    try {
      Metrics result = client
              .monitoring
              .getDropletMemoryTotalMetrics(hostId, start, end)
              .execute();
      System.out.println(result);
      System.out.println(result.getData());
      System.out.println(result.getStatus());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletMemoryTotalMetrics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Metrics> response = client
              .monitoring
              .getDropletMemoryTotalMetrics(hostId, start, end)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getDropletMemoryTotalMetrics");
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
| **hostId** | **String**| The droplet ID. | |
| **start** | **String**| Timestamp to start metric window. | |
| **end** | **String**| Timestamp to end metric window. | |

### Return type

[**Metrics**](Metrics.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getExistingAlertPolicy"></a>
# **getExistingAlertPolicy**
> Object getExistingAlertPolicy(alertUuid).execute();

Retrieve an Existing Alert Policy

To retrieve a given alert policy, send a GET request to &#x60;/v2/monitoring/alerts/{alert_uuid}&#x60;

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    String alertUuid = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for an alert policy.
    try {
      Object result = client
              .monitoring
              .getExistingAlertPolicy(alertUuid)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getExistingAlertPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .monitoring
              .getExistingAlertPolicy(alertUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#getExistingAlertPolicy");
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
| **alertUuid** | **String**| A unique identifier for an alert policy. | |

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
| **200** | An alert policy. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listAlertPolicies"></a>
# **listAlertPolicies**
> MonitoringListAlertPoliciesResponse listAlertPolicies().perPage(perPage).page(page).execute();

List Alert Policies

Returns all alert policies that are configured for the given account. To List all alert policies, send a GET request to &#x60;/v2/monitoring/alerts&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
      MonitoringListAlertPoliciesResponse result = client
              .monitoring
              .listAlertPolicies()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getPolicies());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#listAlertPolicies");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<MonitoringListAlertPoliciesResponse> response = client
              .monitoring
              .listAlertPolicies()
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#listAlertPolicies");
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

[**MonitoringListAlertPoliciesResponse**](MonitoringListAlertPoliciesResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A list of alert policies. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateAlertPolicy"></a>
# **updateAlertPolicy**
> Object updateAlertPolicy(alertUuid, alertPolicyRequest).execute();

Update an Alert Policy

To update en existing policy, send a PUT request to &#x60;v2/monitoring/alerts/{alert_uuid}&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.MonitoringApi;
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
    List<String> tags = Arrays.asList();
    String description = "description_example";
    Alerts alerts = new Alerts();
    String compare = "GreaterThan";
    Boolean enabled = true;
    List<String> entities = Arrays.asList();
    String type = "v1/insights/droplet/load_1";
    Float value = 3.4F;
    String window = "5m";
    String alertUuid = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for an alert policy.
    try {
      Object result = client
              .monitoring
              .updateAlertPolicy(tags, description, alerts, compare, enabled, entities, type, value, window, alertUuid)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#updateAlertPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .monitoring
              .updateAlertPolicy(tags, description, alerts, compare, enabled, entities, type, value, window, alertUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling MonitoringApi#updateAlertPolicy");
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
| **alertUuid** | **String**| A unique identifier for an alert policy. | |
| **alertPolicyRequest** | [**AlertPolicyRequest**](AlertPolicyRequest.md)| The &#x60;type&#x60; field dictates what type of entity that the alert policy applies to and hence what type of entity is passed in the &#x60;entities&#x60; array. If both the &#x60;tags&#x60; array and &#x60;entities&#x60; array are empty the alert policy applies to all entities of the relevant type that are owned by the user account. Otherwise the following table shows the valid entity types for each type of alert policy:  Type | Description | Valid Entity Type -----|-------------|-------------------- &#x60;v1/insights/droplet/memory_utilization_percent&#x60; | alert on the percent of memory utilization | Droplet ID &#x60;v1/insights/droplet/disk_read&#x60; | alert on the rate of disk read I/O in MBps | Droplet ID &#x60;v1/insights/droplet/load_5&#x60; | alert on the 5 minute load average | Droplet ID &#x60;v1/insights/droplet/load_15&#x60; | alert on the 15 minute load average | Droplet ID &#x60;v1/insights/droplet/disk_utilization_percent&#x60; | alert on the percent of disk utilization | Droplet ID &#x60;v1/insights/droplet/cpu&#x60; | alert on the percent of CPU utilization | Droplet ID &#x60;v1/insights/droplet/disk_write&#x60; | alert on the rate of disk write I/O in MBps | Droplet ID &#x60;v1/insights/droplet/public_outbound_bandwidth&#x60; | alert on the rate of public outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/public_inbound_bandwidth&#x60; | alert on the rate of public inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_outbound_bandwidth&#x60; | alert on the rate of private outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_inbound_bandwidth&#x60; | alert on the rate of private inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/load_1&#x60; | alert on the 1 minute load average | Droplet ID &#x60;v1/insights/lbaas/avg_cpu_utilization_percent&#x60;|alert on the percent of CPU utilization|load balancer ID &#x60;v1/insights/lbaas/connection_utilization_percent&#x60;|alert on the percent of connection utilization|load balancer ID &#x60;v1/insights/lbaas/droplet_health&#x60;|alert on Droplet health status changes|load balancer ID &#x60;v1/insights/lbaas/tls_connections_per_second_utilization_percent&#x60;|alert on the percent of TLS connections per second utilization|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_5xx&#x60;|alert on the percent increase of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_4xx&#x60;|alert on the percent increase of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_5xx&#x60;|alert on the count of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_4xx&#x60;|alert on the count of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time&#x60;|alert on high average http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_50p&#x60;|alert on high 50th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_95p&#x60;|alert on high 95th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_99p&#x60;|alert on high 99th percentile http response time|load balancer ID &#x60;v1/dbaas/alerts/load_15_alerts&#x60; | alert on 15 minute load average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/memory_utilization_alerts&#x60; | alert on the percent memory utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/disk_utilization_alerts&#x60; | alert on the percent disk utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/cpu_alerts&#x60; | alert on the percent CPU usage average across the database cluster | database cluster UUID  | |

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
| **200** | An alert policy. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

