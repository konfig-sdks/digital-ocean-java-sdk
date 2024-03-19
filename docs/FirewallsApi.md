# FirewallsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addDroplets**](FirewallsApi.md#addDroplets) | **POST** /v2/firewalls/{firewall_id}/droplets | Add Droplets to a Firewall |
| [**addRules**](FirewallsApi.md#addRules) | **POST** /v2/firewalls/{firewall_id}/rules | Add Rules to a Firewall |
| [**addTags**](FirewallsApi.md#addTags) | **POST** /v2/firewalls/{firewall_id}/tags | Add Tags to a Firewall |
| [**create**](FirewallsApi.md#create) | **POST** /v2/firewalls | Create a New Firewall |
| [**delete**](FirewallsApi.md#delete) | **DELETE** /v2/firewalls/{firewall_id} | Delete a Firewall |
| [**get**](FirewallsApi.md#get) | **GET** /v2/firewalls/{firewall_id} | Retrieve an Existing Firewall |
| [**list**](FirewallsApi.md#list) | **GET** /v2/firewalls | List All Firewalls |
| [**removeDroplets**](FirewallsApi.md#removeDroplets) | **DELETE** /v2/firewalls/{firewall_id}/droplets | Remove Droplets from a Firewall |
| [**removeRules**](FirewallsApi.md#removeRules) | **DELETE** /v2/firewalls/{firewall_id}/rules | Remove Rules from a Firewall |
| [**removeTags**](FirewallsApi.md#removeTags) | **DELETE** /v2/firewalls/{firewall_id}/tags | Remove Tags from a Firewall |
| [**update**](FirewallsApi.md#update) | **PUT** /v2/firewalls/{firewall_id} | Update a Firewall |


<a name="addDroplets"></a>
# **addDroplets**
> addDroplets(firewallId).firewallsAddDropletsRequest(firewallsAddDropletsRequest).execute();

Add Droplets to a Firewall

To assign a Droplet to a firewall, send a POST request to &#x60;/v2/firewalls/$FIREWALL_ID/droplets&#x60;. In the body of the request, there should be a &#x60;droplet_ids&#x60; attribute containing a list of Droplet IDs.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    List<Integer> dropletIds = Arrays.asList(); // An array containing the IDs of the Droplets to be assigned to the firewall.
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    try {
      client
              .firewalls
              .addDroplets(dropletIds, firewallId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#addDroplets");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .firewalls
              .addDroplets(dropletIds, firewallId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#addDroplets");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |
| **firewallsAddDropletsRequest** | [**FirewallsAddDropletsRequest**](FirewallsAddDropletsRequest.md)|  | [optional] |

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

<a name="addRules"></a>
# **addRules**
> addRules(firewallId).firewallsAddRulesRequest(firewallsAddRulesRequest).execute();

Add Rules to a Firewall

To add additional access rules to a firewall, send a POST request to &#x60;/v2/firewalls/$FIREWALL_ID/rules&#x60;. The body of the request may include an inbound_rules and/or outbound_rules attribute containing an array of rules to be added.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    List<FirewallRulesInboundRulesInner> inboundRules = Arrays.asList();
    List<FirewallRulesOutboundRulesInner> outboundRules = Arrays.asList();
    try {
      client
              .firewalls
              .addRules(firewallId)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#addRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .firewalls
              .addRules(firewallId)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#addRules");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |
| **firewallsAddRulesRequest** | [**FirewallsAddRulesRequest**](FirewallsAddRulesRequest.md)|  | [optional] |

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

<a name="addTags"></a>
# **addTags**
> addTags(firewallId).firewallsAddTagsRequest(firewallsAddTagsRequest).execute();

Add Tags to a Firewall

To assign a tag representing a group of Droplets to a firewall, send a POST request to &#x60;/v2/firewalls/$FIREWALL_ID/tags&#x60;. In the body of the request, there should be a &#x60;tags&#x60; attribute containing a list of tag names.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    List tags = new List();
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    try {
      client
              .firewalls
              .addTags(tags, firewallId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#addTags");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .firewalls
              .addTags(tags, firewallId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#addTags");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |
| **firewallsAddTagsRequest** | [**FirewallsAddTagsRequest**](FirewallsAddTagsRequest.md)|  | [optional] |

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

<a name="create"></a>
# **create**
> FirewallsCreateResponse create().firewallsCreateRequest(firewallsCreateRequest).execute();

Create a New Firewall

To create a new firewall, send a POST request to &#x60;/v2/firewalls&#x60;. The request must contain at least one inbound or outbound access rule. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    List tags = new List();
    String id = "id_example"; // A unique ID that can be used to identify and reference a firewall.
    String status = "waiting"; // A status string indicating the current state of the firewall. This can be \\\"waiting\\\", \\\"succeeded\\\", or \\\"failed\\\".
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the firewall was created.
    List<FirewallAllOfPendingChanges> pendingChanges = Arrays.asList(); // An array of objects each containing the fields \\\"droplet_id\\\", \\\"removing\\\", and \\\"status\\\". It is provided to detail exactly which Droplets are having their security policies updated. When empty, all changes have been successfully applied.
    String name = "name_example"; // A human-readable name for a firewall. The name must begin with an alphanumeric character. Subsequent characters must either be alphanumeric characters, a period (.), or a dash (-).
    List<Integer> dropletIds = Arrays.asList(); // An array containing the IDs of the Droplets assigned to the firewall.
    List<FirewallRulesInboundRulesInner> inboundRules = Arrays.asList();
    List<FirewallRulesOutboundRulesInner> outboundRules = Arrays.asList();
    try {
      FirewallsCreateResponse result = client
              .firewalls
              .create()
              .tags(tags)
              .id(id)
              .status(status)
              .createdAt(createdAt)
              .pendingChanges(pendingChanges)
              .name(name)
              .dropletIds(dropletIds)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .execute();
      System.out.println(result);
      System.out.println(result.getFirewall());
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FirewallsCreateResponse> response = client
              .firewalls
              .create()
              .tags(tags)
              .id(id)
              .status(status)
              .createdAt(createdAt)
              .pendingChanges(pendingChanges)
              .name(name)
              .dropletIds(dropletIds)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#create");
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
| **firewallsCreateRequest** | [**FirewallsCreateRequest**](FirewallsCreateRequest.md)|  | [optional] |

### Return type

[**FirewallsCreateResponse**](FirewallsCreateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> delete(firewallId).execute();

Delete a Firewall

To delete a firewall send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID&#x60;.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    try {
      client
              .firewalls
              .delete(firewallId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .firewalls
              .delete(firewallId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#delete");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |

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
> FirewallsGetResponse get(firewallId).execute();

Retrieve an Existing Firewall

To show information about an existing firewall, send a GET request to &#x60;/v2/firewalls/$FIREWALL_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    try {
      FirewallsGetResponse result = client
              .firewalls
              .get(firewallId)
              .execute();
      System.out.println(result);
      System.out.println(result.getFirewall());
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FirewallsGetResponse> response = client
              .firewalls
              .get(firewallId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#get");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |

### Return type

[**FirewallsGetResponse**](FirewallsGetResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> FirewallsListResponse list().perPage(perPage).page(page).execute();

List All Firewalls

To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
      FirewallsListResponse result = client
              .firewalls
              .list()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getFirewalls());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FirewallsListResponse> response = client
              .firewalls
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
      System.err.println("Exception when calling FirewallsApi#list");
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

[**FirewallsListResponse**](FirewallsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="removeDroplets"></a>
# **removeDroplets**
> removeDroplets(firewallId).firewallsRemoveDropletsRequest(firewallsRemoveDropletsRequest).execute();

Remove Droplets from a Firewall

To remove a Droplet from a firewall, send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID/droplets&#x60;. In the body of the request, there should be a &#x60;droplet_ids&#x60; attribute containing a list of Droplet IDs.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    List<Integer> dropletIds = Arrays.asList(); // An array containing the IDs of the Droplets to be removed from the firewall.
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    try {
      client
              .firewalls
              .removeDroplets(dropletIds, firewallId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#removeDroplets");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .firewalls
              .removeDroplets(dropletIds, firewallId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#removeDroplets");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |
| **firewallsRemoveDropletsRequest** | [**FirewallsRemoveDropletsRequest**](FirewallsRemoveDropletsRequest.md)|  | [optional] |

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

<a name="removeRules"></a>
# **removeRules**
> removeRules(firewallId).firewallsRemoveRulesRequest(firewallsRemoveRulesRequest).execute();

Remove Rules from a Firewall

To remove access rules from a firewall, send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID/rules&#x60;. The body of the request may include an &#x60;inbound_rules&#x60; and/or &#x60;outbound_rules&#x60; attribute containing an array of rules to be removed.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    List<FirewallRulesInboundRulesInner> inboundRules = Arrays.asList();
    List<FirewallRulesOutboundRulesInner> outboundRules = Arrays.asList();
    try {
      client
              .firewalls
              .removeRules(firewallId)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#removeRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .firewalls
              .removeRules(firewallId)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#removeRules");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |
| **firewallsRemoveRulesRequest** | [**FirewallsRemoveRulesRequest**](FirewallsRemoveRulesRequest.md)|  | [optional] |

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

<a name="removeTags"></a>
# **removeTags**
> removeTags(firewallId).firewallsRemoveTagsRequest(firewallsRemoveTagsRequest).execute();

Remove Tags from a Firewall

To remove a tag representing a group of Droplets from a firewall, send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID/tags&#x60;. In the body of the request, there should be a &#x60;tags&#x60; attribute containing a list of tag names.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    List tags = new List();
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    try {
      client
              .firewalls
              .removeTags(tags, firewallId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#removeTags");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .firewalls
              .removeTags(tags, firewallId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#removeTags");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |
| **firewallsRemoveTagsRequest** | [**FirewallsRemoveTagsRequest**](FirewallsRemoveTagsRequest.md)|  | [optional] |

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

<a name="update"></a>
# **update**
> FirewallsUpdateResponse update(firewallId).firewallsUpdateRequest(firewallsUpdateRequest).execute();

Update a Firewall

To update the configuration of an existing firewall, send a PUT request to &#x60;/v2/firewalls/$FIREWALL_ID&#x60;. The request should contain a full representation of the firewall including existing attributes. **Note that any attributes that are not provided will be reset to their default values.** 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.FirewallsApi;
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
    String name = "name_example"; // A human-readable name for a firewall. The name must begin with an alphanumeric character. Subsequent characters must either be alphanumeric characters, a period (.), or a dash (-).
    UUID firewallId = UUID.fromString("bb4b2611-3d72-467b-8602-280330ecd65c"); // A unique ID that can be used to identify and reference a firewall.
    List tags = new List();
    String id = "id_example"; // A unique ID that can be used to identify and reference a firewall.
    String status = "waiting"; // A status string indicating the current state of the firewall. This can be \\\"waiting\\\", \\\"succeeded\\\", or \\\"failed\\\".
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the firewall was created.
    List<FirewallAllOfPendingChanges> pendingChanges = Arrays.asList(); // An array of objects each containing the fields \\\"droplet_id\\\", \\\"removing\\\", and \\\"status\\\". It is provided to detail exactly which Droplets are having their security policies updated. When empty, all changes have been successfully applied.
    List<Integer> dropletIds = Arrays.asList(); // An array containing the IDs of the Droplets assigned to the firewall.
    List<FirewallRulesInboundRulesInner> inboundRules = Arrays.asList();
    List<FirewallRulesOutboundRulesInner> outboundRules = Arrays.asList();
    try {
      FirewallsUpdateResponse result = client
              .firewalls
              .update(name, firewallId)
              .tags(tags)
              .id(id)
              .status(status)
              .createdAt(createdAt)
              .pendingChanges(pendingChanges)
              .dropletIds(dropletIds)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .execute();
      System.out.println(result);
      System.out.println(result.getFirewall());
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<FirewallsUpdateResponse> response = client
              .firewalls
              .update(name, firewallId)
              .tags(tags)
              .id(id)
              .status(status)
              .createdAt(createdAt)
              .pendingChanges(pendingChanges)
              .dropletIds(dropletIds)
              .inboundRules(inboundRules)
              .outboundRules(outboundRules)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling FirewallsApi#update");
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
| **firewallId** | **UUID**| A unique ID that can be used to identify and reference a firewall. | |
| **firewallsUpdateRequest** | [**FirewallsUpdateRequest**](FirewallsUpdateRequest.md)|  | [optional] |

### Return type

[**FirewallsUpdateResponse**](FirewallsUpdateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a &#x60;firewall&#x60; key. This will be set to an object containing the standard firewall attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

