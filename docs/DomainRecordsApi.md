# DomainRecordsApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**createNewRecord**](DomainRecordsApi.md#createNewRecord) | **POST** /v2/domains/{domain_name}/records | Create a New Domain Record |
| [**deleteById**](DomainRecordsApi.md#deleteById) | **DELETE** /v2/domains/{domain_name}/records/{domain_record_id} | Delete a Domain Record |
| [**getExistingRecord**](DomainRecordsApi.md#getExistingRecord) | **GET** /v2/domains/{domain_name}/records/{domain_record_id} | Retrieve an Existing Domain Record |
| [**listAllRecords**](DomainRecordsApi.md#listAllRecords) | **GET** /v2/domains/{domain_name}/records | List All Domain Records |
| [**updateRecordById**](DomainRecordsApi.md#updateRecordById) | **PUT** /v2/domains/{domain_name}/records/{domain_record_id} | Update a Domain Record |
| [**updateRecordById_0**](DomainRecordsApi.md#updateRecordById_0) | **PATCH** /v2/domains/{domain_name}/records/{domain_record_id} | Update a Domain Record |


<a name="createNewRecord"></a>
# **createNewRecord**
> DomainRecordsCreateNewRecordResponse createNewRecord(domainName).body(body).execute();

Create a New Domain Record

To create a new record to a domain, send a POST request to &#x60;/v2/domains/$DOMAIN_NAME/records&#x60;.  The request must include all of the required fields for the domain record type being added.  See the [attribute table](https://api-engineering.nyc3.cdn.digitaloceanspaces.com) for details regarding record types and their respective required attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DomainRecordsApi;
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
    String domainName = "example.com"; // The name of the domain itself.
    try {
      DomainRecordsCreateNewRecordResponse result = client
              .domainRecords
              .createNewRecord(domainName)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomainRecord());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#createNewRecord");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DomainRecordsCreateNewRecordResponse> response = client
              .domainRecords
              .createNewRecord(domainName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#createNewRecord");
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
| **domainName** | **String**| The name of the domain itself. | |
| **body** | **Object**|  | [optional] |

### Return type

[**DomainRecordsCreateNewRecordResponse**](DomainRecordsCreateNewRecordResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response body will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be an object representing the new record. Attributes that are not applicable for the record type will be set to &#x60;null&#x60;. An &#x60;id&#x60; attribute is generated for each record as part of the object. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteById"></a>
# **deleteById**
> deleteById(domainName, domainRecordId).execute();

Delete a Domain Record

To delete a record for a domain, send a DELETE request to &#x60;/v2/domains/$DOMAIN_NAME/records/$DOMAIN_RECORD_ID&#x60;.  The record will be deleted and the response status will be a 204. This indicates a successful request with no body returned. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DomainRecordsApi;
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
    String domainName = "example.com"; // The name of the domain itself.
    Integer domainRecordId = 3352896; // The unique identifier of the domain record.
    try {
      client
              .domainRecords
              .deleteById(domainName, domainRecordId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#deleteById");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .domainRecords
              .deleteById(domainName, domainRecordId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#deleteById");
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
| **domainName** | **String**| The name of the domain itself. | |
| **domainRecordId** | **Integer**| The unique identifier of the domain record. | |

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

<a name="getExistingRecord"></a>
# **getExistingRecord**
> DomainRecordsGetExistingRecordResponse getExistingRecord(domainName, domainRecordId).execute();

Retrieve an Existing Domain Record

To retrieve a specific domain record, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records/$RECORD_ID&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DomainRecordsApi;
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
    String domainName = "example.com"; // The name of the domain itself.
    Integer domainRecordId = 3352896; // The unique identifier of the domain record.
    try {
      DomainRecordsGetExistingRecordResponse result = client
              .domainRecords
              .getExistingRecord(domainName, domainRecordId)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomainRecord());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#getExistingRecord");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DomainRecordsGetExistingRecordResponse> response = client
              .domainRecords
              .getExistingRecord(domainName, domainRecordId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#getExistingRecord");
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
| **domainName** | **String**| The name of the domain itself. | |
| **domainRecordId** | **Integer**| The unique identifier of the domain record. | |

### Return type

[**DomainRecordsGetExistingRecordResponse**](DomainRecordsGetExistingRecordResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listAllRecords"></a>
# **listAllRecords**
> DomainRecordsListAllRecordsResponse listAllRecords(domainName).name(name).type(type).perPage(perPage).page(page).execute();

List All Domain Records

To get a listing of all records configured for a domain, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records&#x60;. The list of records returned can be filtered by using the &#x60;name&#x60; and &#x60;type&#x60; query parameters. For example, to only include A records for a domain, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records?type&#x3D;A&#x60;. &#x60;name&#x60; must be a fully qualified record name. For example, to only include records matching &#x60;sub.example.com&#x60;, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records?name&#x3D;sub.example.com&#x60;. Both name and type may be used together.  

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DomainRecordsApi;
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
    String domainName = "example.com"; // The name of the domain itself.
    String name = "sub.example.com"; // A fully qualified record name. For example, to only include records matching sub.example.com, send a GET request to `/v2/domains/$DOMAIN_NAME/records?name=sub.example.com`.
    String type = "A"; // The type of the DNS record. For example: A, CNAME, TXT, ...
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      DomainRecordsListAllRecordsResponse result = client
              .domainRecords
              .listAllRecords(domainName)
              .name(name)
              .type(type)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomainRecords());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#listAllRecords");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DomainRecordsListAllRecordsResponse> response = client
              .domainRecords
              .listAllRecords(domainName)
              .name(name)
              .type(type)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#listAllRecords");
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
| **domainName** | **String**| The name of the domain itself. | |
| **name** | **String**| A fully qualified record name. For example, to only include records matching sub.example.com, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records?name&#x3D;sub.example.com&#x60;. | [optional] |
| **type** | **String**| The type of the DNS record. For example: A, CNAME, TXT, ... | [optional] [enum: A, AAAA, CAA, CNAME, MX, NS, SOA, SRV, TXT] |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**DomainRecordsListAllRecordsResponse**](DomainRecordsListAllRecordsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;domain_records&#x60;. The value of this will be an array of domain record objects, each of which contains the standard domain record attributes. For attributes that are not used by a specific record type, a value of &#x60;null&#x60; will be returned. For instance, all records other than SRV will have &#x60;null&#x60; for the &#x60;weight&#x60; and &#x60;port&#x60; attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateRecordById"></a>
# **updateRecordById**
> DomainRecordsGetExistingRecordResponse updateRecordById(domainName, domainRecordId).domainRecord(domainRecord).execute();

Update a Domain Record

To update an existing record, send a PUT request to &#x60;/v2/domains/$DOMAIN_NAME/records/$DOMAIN_RECORD_ID&#x60;. Any attribute valid for the record type can be set to a new value for the record.  See the [attribute table](https://api-engineering.nyc3.cdn.digitaloceanspaces.com) for details regarding record types and their respective attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DomainRecordsApi;
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
    String type = "type_example"; // The type of the DNS record. For example: A, CNAME, TXT, ...
    String domainName = "example.com"; // The name of the domain itself.
    Integer domainRecordId = 3352896; // The unique identifier of the domain record.
    Integer id = 56; // A unique identifier for each domain record.
    String name = "name_example"; // The host name, alias, or service being defined by the record.
    String data = "data_example"; // Variable data depending on record type. For example, the \\\"data\\\" value for an A record would be the IPv4 address to which the domain will be mapped. For a CAA record, it would contain the domain name of the CA being granted permission to issue certificates.
    Integer priority = 56; // The priority for SRV and MX records.
    Integer port = 56; // The port for SRV records.
    Integer ttl = 56; // This value is the time to live for the record, in seconds. This defines the time frame that clients can cache queried information before a refresh should be requested.
    Integer weight = 56; // The weight for SRV records.
    Integer flags = 56; // An unsigned integer between 0-255 used for CAA records.
    String tag = "tag_example"; // The parameter tag for CAA records. Valid values are \\\"issue\\\", \\\"issuewild\\\", or \\\"iodef\\\"
    try {
      DomainRecordsGetExistingRecordResponse result = client
              .domainRecords
              .updateRecordById(type, domainName, domainRecordId)
              .id(id)
              .name(name)
              .data(data)
              .priority(priority)
              .port(port)
              .ttl(ttl)
              .weight(weight)
              .flags(flags)
              .tag(tag)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomainRecord());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#updateRecordById");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DomainRecordsGetExistingRecordResponse> response = client
              .domainRecords
              .updateRecordById(type, domainName, domainRecordId)
              .id(id)
              .name(name)
              .data(data)
              .priority(priority)
              .port(port)
              .ttl(ttl)
              .weight(weight)
              .flags(flags)
              .tag(tag)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#updateRecordById");
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
| **domainName** | **String**| The name of the domain itself. | |
| **domainRecordId** | **Integer**| The unique identifier of the domain record. | |
| **domainRecord** | [**DomainRecord**](DomainRecord.md)|  | [optional] |

### Return type

[**DomainRecordsGetExistingRecordResponse**](DomainRecordsGetExistingRecordResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateRecordById_0"></a>
# **updateRecordById_0**
> DomainRecordsGetExistingRecordResponse updateRecordById_0(domainName, domainRecordId).domainRecord(domainRecord).execute();

Update a Domain Record

To update an existing record, send a PATCH request to &#x60;/v2/domains/$DOMAIN_NAME/records/$DOMAIN_RECORD_ID&#x60;. Any attribute valid for the record type can be set to a new value for the record.  See the [attribute table](https://api-engineering.nyc3.cdn.digitaloceanspaces.com) for details regarding record types and their respective attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DomainRecordsApi;
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
    String type = "type_example"; // The type of the DNS record. For example: A, CNAME, TXT, ...
    String domainName = "example.com"; // The name of the domain itself.
    Integer domainRecordId = 3352896; // The unique identifier of the domain record.
    Integer id = 56; // A unique identifier for each domain record.
    String name = "name_example"; // The host name, alias, or service being defined by the record.
    String data = "data_example"; // Variable data depending on record type. For example, the \\\"data\\\" value for an A record would be the IPv4 address to which the domain will be mapped. For a CAA record, it would contain the domain name of the CA being granted permission to issue certificates.
    Integer priority = 56; // The priority for SRV and MX records.
    Integer port = 56; // The port for SRV records.
    Integer ttl = 56; // This value is the time to live for the record, in seconds. This defines the time frame that clients can cache queried information before a refresh should be requested.
    Integer weight = 56; // The weight for SRV records.
    Integer flags = 56; // An unsigned integer between 0-255 used for CAA records.
    String tag = "tag_example"; // The parameter tag for CAA records. Valid values are \\\"issue\\\", \\\"issuewild\\\", or \\\"iodef\\\"
    try {
      DomainRecordsGetExistingRecordResponse result = client
              .domainRecords
              .updateRecordById_0(type, domainName, domainRecordId)
              .id(id)
              .name(name)
              .data(data)
              .priority(priority)
              .port(port)
              .ttl(ttl)
              .weight(weight)
              .flags(flags)
              .tag(tag)
              .execute();
      System.out.println(result);
      System.out.println(result.getDomainRecord());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#updateRecordById_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DomainRecordsGetExistingRecordResponse> response = client
              .domainRecords
              .updateRecordById_0(type, domainName, domainRecordId)
              .id(id)
              .name(name)
              .data(data)
              .priority(priority)
              .port(port)
              .ttl(ttl)
              .weight(weight)
              .flags(flags)
              .tag(tag)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DomainRecordsApi#updateRecordById_0");
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
| **domainName** | **String**| The name of the domain itself. | |
| **domainRecordId** | **Integer**| The unique identifier of the domain record. | |
| **domainRecord** | [**DomainRecord**](DomainRecord.md)|  | [optional] |

### Return type

[**DomainRecordsGetExistingRecordResponse**](DomainRecordsGetExistingRecordResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

