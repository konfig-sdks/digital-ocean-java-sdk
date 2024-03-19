# Class1ClickApplicationsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**installKubernetesApplication**](Class1ClickApplicationsApi.md#installKubernetesApplication) | **POST** /v2/1-clicks/kubernetes | Install Kubernetes 1-Click Applications |
| [**list**](Class1ClickApplicationsApi.md#list) | **GET** /v2/1-clicks | List 1-Click Applications |


<a name="installKubernetesApplication"></a>
# **installKubernetesApplication**
> Model1ClickApplicationsInstallKubernetesApplicationResponse installKubernetesApplication(oneClicksCreate).execute();

Install Kubernetes 1-Click Applications

To install a Kubernetes 1-Click application on a cluster, send a POST request to &#x60;/v2/1-clicks/kubernetes&#x60;. The &#x60;addon_slugs&#x60; and &#x60;cluster_uuid&#x60; must be provided as body parameter in order to specify which 1-Click application(s) to install. To list all available 1-Click Kubernetes applications, send a request to &#x60;/v2/1-clicks?type&#x3D;kubernetes&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Class1ClickApplicationsApi;
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
    List<String> addonSlugs = Arrays.asList(); // An array of 1-Click Application slugs to be installed to the Kubernetes cluster.
    String clusterUuid = "clusterUuid_example"; // A unique ID for the Kubernetes cluster to which the 1-Click Applications will be installed.
    try {
      Model1ClickApplicationsInstallKubernetesApplicationResponse result = client
              .class1ClickApplications
              .installKubernetesApplication(addonSlugs, clusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getMessage());
    } catch (ApiException e) {
      System.err.println("Exception when calling Class1ClickApplicationsApi#installKubernetesApplication");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Model1ClickApplicationsInstallKubernetesApplicationResponse> response = client
              .class1ClickApplications
              .installKubernetesApplication(addonSlugs, clusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Class1ClickApplicationsApi#installKubernetesApplication");
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
| **oneClicksCreate** | [**OneClicksCreate**](OneClicksCreate.md)|  | |

### Return type

[**Model1ClickApplicationsInstallKubernetesApplicationResponse**](Model1ClickApplicationsInstallKubernetesApplicationResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will verify that a job has been successfully created to install a 1-Click. The post-installation lifecycle of a 1-Click application can not be managed via the DigitalOcean API. For additional details specific to the 1-Click, find and view its [DigitalOcean Marketplace](https://marketplace.digitalocean.com) page.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> OneClicksListResponse list().type(type).execute();

List 1-Click Applications

To list all available 1-Click applications, send a GET request to &#x60;/v2/1-clicks&#x60;. The &#x60;type&#x60; may be provided as query paramater in order to restrict results to a certain type of 1-Click, for example: &#x60;/v2/1-clicks?type&#x3D;droplet&#x60;. Current supported types are &#x60;kubernetes&#x60; and &#x60;droplet&#x60;.  The response will be a JSON object with a key called &#x60;1_clicks&#x60;. This will be set to an array of 1-Click application data, each of which will contain the the slug and type for the 1-Click. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Class1ClickApplicationsApi;
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
    String type = "droplet"; // Restrict results to a certain type of 1-Click.
    try {
      OneClicksListResponse result = client
              .class1ClickApplications
              .list()
              .type(type)
              .execute();
      System.out.println(result);
      System.out.println(result.get1clicks());
    } catch (ApiException e) {
      System.err.println("Exception when calling Class1ClickApplicationsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<OneClicksListResponse> response = client
              .class1ClickApplications
              .list()
              .type(type)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Class1ClickApplicationsApi#list");
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
| **type** | **String**| Restrict results to a certain type of 1-Click. | [optional] [enum: droplet, kubernetes] |

### Return type

[**OneClicksListResponse**](OneClicksListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;1_clicks&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

