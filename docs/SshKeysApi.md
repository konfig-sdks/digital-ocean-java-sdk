# SshKeysApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create**](SshKeysApi.md#create) | **POST** /v2/account/keys | Create a New SSH Key |
| [**delete**](SshKeysApi.md#delete) | **DELETE** /v2/account/keys/{ssh_key_identifier} | Delete an SSH Key |
| [**get**](SshKeysApi.md#get) | **GET** /v2/account/keys/{ssh_key_identifier} | Retrieve an Existing SSH Key |
| [**list**](SshKeysApi.md#list) | **GET** /v2/account/keys | List All SSH Keys |
| [**update**](SshKeysApi.md#update) | **PUT** /v2/account/keys/{ssh_key_identifier} | Update an SSH Key&#39;s Name |


<a name="create"></a>
# **create**
> Object create(sshKeys).execute();

Create a New SSH Key

To add a new SSH public key to your DigitalOcean account, send a POST request to &#x60;/v2/account/keys&#x60;. Set the &#x60;name&#x60; attribute to the name you wish to use and the &#x60;public_key&#x60; attribute to the full public key you are adding.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SshKeysApi;
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
    String publicKey = "publicKey_example"; // The entire public key string that was uploaded. Embedded into the root user's `authorized_keys` file if you include this key during Droplet creation.
    String name = "name_example"; // A human-readable display name for this key, used to easily identify the SSH keys when they are displayed.
    Integer id = 56; // A unique identification number for this key. Can be used to embed a  specific SSH key into a Droplet.
    String fingerprint = "fingerprint_example"; // A unique identifier that differentiates this key from other keys using  a format that SSH recognizes. The fingerprint is created when the key is added to your account.
    try {
      Object result = client
              .sshKeys
              .create(publicKey, name)
              .id(id)
              .fingerprint(fingerprint)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .sshKeys
              .create(publicKey, name)
              .id(id)
              .fingerprint(fingerprint)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#create");
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
| **sshKeys** | [**SshKeys**](SshKeys.md)|  | |

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
| **201** | The response body will be a JSON object with a key set to &#x60;ssh_key&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> delete(sshKeyIdentifier).execute();

Delete an SSH Key

To destroy a public SSH key that you have in your account, send a DELETE request to &#x60;/v2/account/keys/$KEY_ID&#x60; or &#x60;/v2/account/keys/$KEY_FINGERPRINT&#x60;. A 204 status will be returned, indicating that the action was successful and that the response body is empty.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SshKeysApi;
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
    Object sshKeyIdentifier = 512189; // Either the ID or the fingerprint of an existing SSH key.
    try {
      client
              .sshKeys
              .delete(sshKeyIdentifier)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .sshKeys
              .delete(sshKeyIdentifier)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#delete");
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
| **sshKeyIdentifier** | [**Object**](.md)| Either the ID or the fingerprint of an existing SSH key. | |

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
> Object get(sshKeyIdentifier).execute();

Retrieve an Existing SSH Key

To get information about a key, send a GET request to &#x60;/v2/account/keys/$KEY_ID&#x60; or &#x60;/v2/account/keys/$KEY_FINGERPRINT&#x60;. The response will be a JSON object with the key &#x60;ssh_key&#x60; and value an ssh_key object which contains the standard ssh_key attributes.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SshKeysApi;
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
    Object sshKeyIdentifier = 512189; // Either the ID or the fingerprint of an existing SSH key.
    try {
      Object result = client
              .sshKeys
              .get(sshKeyIdentifier)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .sshKeys
              .get(sshKeyIdentifier)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#get");
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
| **sshKeyIdentifier** | [**Object**](.md)| Either the ID or the fingerprint of an existing SSH key. | |

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
| **200** | A JSON object with the key set to &#x60;ssh_key&#x60;. The value is an &#x60;ssh_key&#x60; object containing the standard &#x60;ssh_key&#x60; attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> SshKeysListResponse list().perPage(perPage).page(page).execute();

List All SSH Keys

To list all of the keys in your account, send a GET request to &#x60;/v2/account/keys&#x60;. The response will be a JSON object with a key set to &#x60;ssh_keys&#x60;. The value of this will be an array of ssh_key objects, each of which contains the standard ssh_key attributes.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SshKeysApi;
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
      SshKeysListResponse result = client
              .sshKeys
              .list()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getSshKeys());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SshKeysListResponse> response = client
              .sshKeys
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
      System.err.println("Exception when calling SshKeysApi#list");
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

[**SshKeysListResponse**](SshKeysListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with the key set to &#x60;ssh_keys&#x60;. The value is an array of &#x60;ssh_key&#x60; objects, each of which contains the standard &#x60;ssh_key&#x60; attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="update"></a>
# **update**
> Object update(sshKeyIdentifier, sshKeysUpdateRequest).execute();

Update an SSH Key&#39;s Name

To update the name of an SSH key, send a PUT request to either &#x60;/v2/account/keys/$SSH_KEY_ID&#x60; or &#x60;/v2/account/keys/$SSH_KEY_FINGERPRINT&#x60;. Set the &#x60;name&#x60; attribute to the new name you want to use.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.SshKeysApi;
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
    Object sshKeyIdentifier = 512189; // Either the ID or the fingerprint of an existing SSH key.
    String name = "name_example"; // A human-readable display name for this key, used to easily identify the SSH keys when they are displayed.
    try {
      Object result = client
              .sshKeys
              .update(sshKeyIdentifier)
              .name(name)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .sshKeys
              .update(sshKeyIdentifier)
              .name(name)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling SshKeysApi#update");
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
| **sshKeyIdentifier** | [**Object**](.md)| Either the ID or the fingerprint of an existing SSH key. | |
| **sshKeysUpdateRequest** | [**SshKeysUpdateRequest**](SshKeysUpdateRequest.md)| Set the &#x60;name&#x60; attribute to the new name you want to use. | |

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
| **200** | A JSON object with the key set to &#x60;ssh_key&#x60;. The value is an &#x60;ssh_key&#x60; object containing the standard &#x60;ssh_key&#x60; attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

