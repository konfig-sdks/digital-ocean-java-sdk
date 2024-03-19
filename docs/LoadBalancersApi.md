# LoadBalancersApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addForwardingRules**](LoadBalancersApi.md#addForwardingRules) | **POST** /v2/load_balancers/{lb_id}/forwarding_rules | Add Forwarding Rules to a Load Balancer |
| [**assignDroplets**](LoadBalancersApi.md#assignDroplets) | **POST** /v2/load_balancers/{lb_id}/droplets | Add Droplets to a Load Balancer |
| [**create**](LoadBalancersApi.md#create) | **POST** /v2/load_balancers | Create a New Load Balancer |
| [**delete**](LoadBalancersApi.md#delete) | **DELETE** /v2/load_balancers/{lb_id} | Delete a Load Balancer |
| [**get**](LoadBalancersApi.md#get) | **GET** /v2/load_balancers/{lb_id} | Retrieve an Existing Load Balancer |
| [**list**](LoadBalancersApi.md#list) | **GET** /v2/load_balancers | List All Load Balancers |
| [**removeDroplets**](LoadBalancersApi.md#removeDroplets) | **DELETE** /v2/load_balancers/{lb_id}/droplets | Remove Droplets from a Load Balancer |
| [**removeForwardingRules**](LoadBalancersApi.md#removeForwardingRules) | **DELETE** /v2/load_balancers/{lb_id}/forwarding_rules | Remove Forwarding Rules from a Load Balancer |
| [**update**](LoadBalancersApi.md#update) | **PUT** /v2/load_balancers/{lb_id} | Update a Load Balancer |


<a name="addForwardingRules"></a>
# **addForwardingRules**
> addForwardingRules(lbId, loadBalancersAddForwardingRulesRequest).execute();

Add Forwarding Rules to a Load Balancer

To add an additional forwarding rule to a load balancer instance, send a POST request to &#x60;/v2/load_balancers/$LOAD_BALANCER_ID/forwarding_rules&#x60;. In the body of the request, there should be a &#x60;forwarding_rules&#x60; attribute containing an array of rules to be added.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
    List<ForwardingRule> forwardingRules = Arrays.asList();
    String lbId = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for a load balancer.
    try {
      client
              .loadBalancers
              .addForwardingRules(forwardingRules, lbId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#addForwardingRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .loadBalancers
              .addForwardingRules(forwardingRules, lbId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#addForwardingRules");
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
| **lbId** | **String**| A unique identifier for a load balancer. | |
| **loadBalancersAddForwardingRulesRequest** | [**LoadBalancersAddForwardingRulesRequest**](LoadBalancersAddForwardingRulesRequest.md)|  | |

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

<a name="assignDroplets"></a>
# **assignDroplets**
> assignDroplets(lbId, loadBalancersAssignDropletsRequest).execute();

Add Droplets to a Load Balancer

To assign a Droplet to a load balancer instance, send a POST request to &#x60;/v2/load_balancers/$LOAD_BALANCER_ID/droplets&#x60;. In the body of the request, there should be a &#x60;droplet_ids&#x60; attribute containing a list of Droplet IDs. Individual Droplets can not be added to a load balancer configured with a Droplet tag. Attempting to do so will result in a \&quot;422 Unprocessable Entity\&quot; response from the API.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
    List<Integer> dropletIds = Arrays.asList(); // An array containing the IDs of the Droplets assigned to the load balancer.
    String lbId = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for a load balancer.
    try {
      client
              .loadBalancers
              .assignDroplets(dropletIds, lbId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#assignDroplets");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .loadBalancers
              .assignDroplets(dropletIds, lbId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#assignDroplets");
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
| **lbId** | **String**| A unique identifier for a load balancer. | |
| **loadBalancersAssignDropletsRequest** | [**LoadBalancersAssignDropletsRequest**](LoadBalancersAssignDropletsRequest.md)|  | |

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
> LoadBalancersCreateResponse create(body).execute();

Create a New Load Balancer

To create a new load balancer instance, send a POST request to &#x60;/v2/load_balancers&#x60;.  You can specify the Droplets that will sit behind the load balancer using one of two methods:  * Set &#x60;droplet_ids&#x60; to a list of specific Droplet IDs. * Set &#x60;tag&#x60; to the name of a tag. All Droplets with this tag applied will be   assigned to the load balancer. Additional Droplets will be automatically   assigned as they are tagged.  These methods are mutually exclusive. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
      LoadBalancersCreateResponse result = client
              .loadBalancers
              .create()
              .execute();
      System.out.println(result);
      System.out.println(result.getLoadBalancer());
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<LoadBalancersCreateResponse> response = client
              .loadBalancers
              .create()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#create");
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
| **body** | **Object**|  | |

### Return type

[**LoadBalancersCreateResponse**](LoadBalancersCreateResponse.md)

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

<a name="delete"></a>
# **delete**
> delete(lbId).execute();

Delete a Load Balancer

To delete a load balancer instance, disassociating any Droplets assigned to it and removing it from your account, send a DELETE request to &#x60;/v2/load_balancers/$LOAD_BALANCER_ID&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
    String lbId = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for a load balancer.
    try {
      client
              .loadBalancers
              .delete(lbId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .loadBalancers
              .delete(lbId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#delete");
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
| **lbId** | **String**| A unique identifier for a load balancer. | |

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
> LoadBalancersGetResponse get(lbId).execute();

Retrieve an Existing Load Balancer

To show information about a load balancer instance, send a GET request to &#x60;/v2/load_balancers/$LOAD_BALANCER_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
    String lbId = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for a load balancer.
    try {
      LoadBalancersGetResponse result = client
              .loadBalancers
              .get(lbId)
              .execute();
      System.out.println(result);
      System.out.println(result.getLoadBalancer());
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<LoadBalancersGetResponse> response = client
              .loadBalancers
              .get(lbId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#get");
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
| **lbId** | **String**| A unique identifier for a load balancer. | |

### Return type

[**LoadBalancersGetResponse**](LoadBalancersGetResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;load_balancer&#x60;. The value of this will be an object that contains the standard attributes associated with a load balancer  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> LoadBalancersListResponse list().perPage(perPage).page(page).execute();

List All Load Balancers

To list all of the load balancer instances on your account, send a GET request to &#x60;/v2/load_balancers&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
      LoadBalancersListResponse result = client
              .loadBalancers
              .list()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getLoadBalancers());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<LoadBalancersListResponse> response = client
              .loadBalancers
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
      System.err.println("Exception when calling LoadBalancersApi#list");
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

[**LoadBalancersListResponse**](LoadBalancersListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;load_balancers&#x60;. This will be set to an array of objects, each of which will contain the standard load balancer attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="removeDroplets"></a>
# **removeDroplets**
> removeDroplets(lbId, loadBalancersRemoveDropletsRequest).execute();

Remove Droplets from a Load Balancer

To remove a Droplet from a load balancer instance, send a DELETE request to &#x60;/v2/load_balancers/$LOAD_BALANCER_ID/droplets&#x60;. In the body of the request, there should be a &#x60;droplet_ids&#x60; attribute containing a list of Droplet IDs.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
    List<Integer> dropletIds = Arrays.asList(); // An array containing the IDs of the Droplets assigned to the load balancer.
    String lbId = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for a load balancer.
    try {
      client
              .loadBalancers
              .removeDroplets(dropletIds, lbId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#removeDroplets");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .loadBalancers
              .removeDroplets(dropletIds, lbId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#removeDroplets");
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
| **lbId** | **String**| A unique identifier for a load balancer. | |
| **loadBalancersRemoveDropletsRequest** | [**LoadBalancersRemoveDropletsRequest**](LoadBalancersRemoveDropletsRequest.md)|  | |

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

<a name="removeForwardingRules"></a>
# **removeForwardingRules**
> removeForwardingRules(lbId, loadBalancersRemoveForwardingRulesRequest).execute();

Remove Forwarding Rules from a Load Balancer

To remove forwarding rules from a load balancer instance, send a DELETE request to &#x60;/v2/load_balancers/$LOAD_BALANCER_ID/forwarding_rules&#x60;. In the body of the request, there should be a &#x60;forwarding_rules&#x60; attribute containing an array of rules to be removed.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
    List<ForwardingRule> forwardingRules = Arrays.asList();
    String lbId = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for a load balancer.
    try {
      client
              .loadBalancers
              .removeForwardingRules(forwardingRules, lbId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#removeForwardingRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .loadBalancers
              .removeForwardingRules(forwardingRules, lbId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#removeForwardingRules");
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
| **lbId** | **String**| A unique identifier for a load balancer. | |
| **loadBalancersRemoveForwardingRulesRequest** | [**LoadBalancersRemoveForwardingRulesRequest**](LoadBalancersRemoveForwardingRulesRequest.md)|  | |

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
> LoadBalancersUpdateResponse update(lbId, body).execute();

Update a Load Balancer

To update a load balancer&#39;s settings, send a PUT request to &#x60;/v2/load_balancers/$LOAD_BALANCER_ID&#x60;. The request should contain a full representation of the load balancer including existing attributes. It may contain _one of_ the &#x60;droplets_ids&#x60; or &#x60;tag&#x60; attributes as they are mutually exclusive. **Note that any attribute that is not provided will be reset to its default value.** 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.LoadBalancersApi;
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
    String lbId = "4de7ac8b-495b-4884-9a69-1050c6793cd6"; // A unique identifier for a load balancer.
    try {
      LoadBalancersUpdateResponse result = client
              .loadBalancers
              .update(lbId)
              .execute();
      System.out.println(result);
      System.out.println(result.getLoadBalancer());
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<LoadBalancersUpdateResponse> response = client
              .loadBalancers
              .update(lbId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling LoadBalancersApi#update");
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
| **lbId** | **String**| A unique identifier for a load balancer. | |
| **body** | **Object**|  | |

### Return type

[**LoadBalancersUpdateResponse**](LoadBalancersUpdateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;load_balancer&#x60;. The value of this will be an object containing the standard attributes of a load balancer.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

