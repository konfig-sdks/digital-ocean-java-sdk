# DropletActionsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**actOnTaggedDroplets**](DropletActionsApi.md#actOnTaggedDroplets) | **POST** /v2/droplets/actions | Acting on Tagged Droplets |
| [**get**](DropletActionsApi.md#get) | **GET** /v2/droplets/{droplet_id}/actions/{action_id} | Retrieve a Droplet Action |
| [**list**](DropletActionsApi.md#list) | **GET** /v2/droplets/{droplet_id}/actions | List Actions for a Droplet |
| [**post**](DropletActionsApi.md#post) | **POST** /v2/droplets/{droplet_id}/actions | Initiate a Droplet Action |


<a name="actOnTaggedDroplets"></a>
# **actOnTaggedDroplets**
> Object actOnTaggedDroplets().tagName(tagName).body(body).execute();

Acting on Tagged Droplets

Some actions can be performed in bulk on tagged Droplets. The actions can be initiated by sending a POST to &#x60;/v2/droplets/actions?tag_name&#x3D;$TAG_NAME&#x60; with the action arguments.  Only a sub-set of action types are supported:  - &#x60;power_cycle&#x60; - &#x60;power_on&#x60; - &#x60;power_off&#x60; - &#x60;shutdown&#x60; - &#x60;enable_ipv6&#x60; - &#x60;enable_backups&#x60; - &#x60;disable_backups&#x60; - &#x60;snapshot&#x60; 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletActionsApi;
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
    String tagName = "env:prod"; // Used to filter Droplets by a specific tag. Can not be combined with `name`.
    try {
      Object result = client
              .dropletActions
              .actOnTaggedDroplets()
              .tagName(tagName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#actOnTaggedDroplets");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .dropletActions
              .actOnTaggedDroplets()
              .tagName(tagName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#actOnTaggedDroplets");
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
| **tagName** | **String**| Used to filter Droplets by a specific tag. Can not be combined with &#x60;name&#x60;. | [optional] |
| **body** | **Object**| The &#x60;type&#x60; attribute set in the request body will specify the  action that will be taken on the Droplet. Some actions will require additional attributes to be set as well.  | [optional] |

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
| **201** | The response will be a JSON object with a key called &#x60;actions&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="get"></a>
# **get**
> Object get(dropletId, actionId).execute();

Retrieve a Droplet Action

To retrieve a Droplet action, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/actions/$ACTION_ID&#x60;.  The response will be a JSON object with a key called &#x60;action&#x60;. The value will be a Droplet action object. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletActionsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    Integer actionId = 36804636; // A unique numeric ID that can be used to identify and reference an action.
    try {
      Object result = client
              .dropletActions
              .get(dropletId, actionId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .dropletActions
              .get(dropletId, actionId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#get");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **actionId** | **Integer**| A unique numeric ID that can be used to identify and reference an action. | |

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
| **200** | The result will be a JSON object with an action key.  This will be set to an action object containing the standard action attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> DropletActionsListResponse list(dropletId).perPage(perPage).page(page).execute();

List Actions for a Droplet

To retrieve a list of all actions that have been executed for a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/actions&#x60;.  The results will be returned as a JSON object with an &#x60;actions&#x60; key. This will be set to an array filled with &#x60;action&#x60; objects containing the standard &#x60;action&#x60; attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletActionsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      DropletActionsListResponse result = client
              .dropletActions
              .list(dropletId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getActions());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DropletActionsListResponse> response = client
              .dropletActions
              .list(dropletId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#list");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**DropletActionsListResponse**](DropletActionsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with an &#x60;actions&#x60; key. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="post"></a>
# **post**
> Object post(dropletId).body(body).execute();

Initiate a Droplet Action

To initiate an action on a Droplet send a POST request to &#x60;/v2/droplets/$DROPLET_ID/actions&#x60;. In the JSON body to the request, set the &#x60;type&#x60; attribute to on of the supported action types:  | Action                                   | Details | | ---------------------------------------- | ----------- | | &lt;nobr&gt;&#x60;enable_backups&#x60;&lt;/nobr&gt;            | Enables backups for a Droplet | | &lt;nobr&gt;&#x60;disable_backups&#x60;&lt;/nobr&gt;           | Disables backups for a Droplet | | &lt;nobr&gt;&#x60;reboot&#x60;&lt;/nobr&gt;                    | Reboots a Droplet. A &#x60;reboot&#x60; action is an attempt to reboot the Droplet in a graceful way, similar to using the &#x60;reboot&#x60; command from the console. | | &lt;nobr&gt;&#x60;power_cycle&#x60;&lt;/nobr&gt;               | Power cycles a Droplet. A &#x60;powercycle&#x60; action is similar to pushing the reset button on a physical machine, it&#39;s similar to booting from scratch. | | &lt;nobr&gt;&#x60;shutdown&#x60;&lt;/nobr&gt;                  | Shutsdown a Droplet. A shutdown action is an attempt to shutdown the Droplet in a graceful way, similar to using the &#x60;shutdown&#x60; command from the console. Since a &#x60;shutdown&#x60; command can fail, this action guarantees that the command is issued, not that it succeeds. The preferred way to turn off a Droplet is to attempt a shutdown, with a reasonable timeout, followed by a &#x60;power_off&#x60; action to ensure the Droplet is off. | | &lt;nobr&gt;&#x60;power_off&#x60;&lt;/nobr&gt;                 | Powers off a Droplet. A &#x60;power_off&#x60; event is a hard shutdown and should only be used if the &#x60;shutdown&#x60; action is not successful. It is similar to cutting the power on a server and could lead to complications. | | &lt;nobr&gt;&#x60;power_on&#x60;&lt;/nobr&gt;                  | Powers on a Droplet. | | &lt;nobr&gt;&#x60;restore&#x60;&lt;/nobr&gt;                   | Restore a Droplet using a backup image. The image ID that is passed in must be a backup of the current Droplet instance. The operation will leave any embedded SSH keys intact. | | &lt;nobr&gt;&#x60;password_reset&#x60;&lt;/nobr&gt;            | Resets the root password for a Droplet. A new password will be provided via email. It must be changed after first use. | | &lt;nobr&gt;&#x60;resize&#x60;&lt;/nobr&gt;                    | Resizes a Droplet. Set the &#x60;size&#x60; attribute to a size slug. If a permanent resize with disk changes included is desired, set the &#x60;disk&#x60; attribute to &#x60;true&#x60;. | | &lt;nobr&gt;&#x60;rebuild&#x60;&lt;/nobr&gt;                   | Rebuilds a Droplet from a new base image. Set the &#x60;image&#x60; attribute to an image ID or slug. | | &lt;nobr&gt;&#x60;rename&#x60;&lt;/nobr&gt;                    | Renames a Droplet. | | &lt;nobr&gt;&#x60;change_kernel&#x60;&lt;/nobr&gt;             | Changes a Droplet&#39;s kernel. Only applies to Droplets with externally managed kernels. All Droplets created after March 2017 use internal kernels by default. | | &lt;nobr&gt;&#x60;enable_ipv6&#x60;&lt;/nobr&gt;               | Enables IPv6 for a Droplet. Once enabled for a Droplet, IPv6 can not be disabled. When enabling IPv6 on an existing Droplet, [additional OS-level configuration](https://docs.digitalocean.com/products/networking/ipv6/how-to/enable/#on-existing-droplets) is required. | | &lt;nobr&gt;&#x60;snapshot&#x60;&lt;/nobr&gt;                  | Takes a snapshot of a Droplet. | 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DropletActionsApi;
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
    Integer dropletId = 3164444; // A unique identifier for a Droplet instance.
    try {
      Object result = client
              .dropletActions
              .post(dropletId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#post");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .dropletActions
              .post(dropletId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DropletActionsApi#post");
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
| **dropletId** | **Integer**| A unique identifier for a Droplet instance. | |
| **body** | **Object**| The &#x60;type&#x60; attribute set in the request body will specify the  action that will be taken on the Droplet. Some actions will require additional attributes to be set as well.  | [optional] |

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
| **201** | The response will be a JSON object with a key called &#x60;action&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

