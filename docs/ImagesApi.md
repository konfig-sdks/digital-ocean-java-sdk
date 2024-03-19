# ImagesApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**delete**](ImagesApi.md#delete) | **DELETE** /v2/images/{image_id} | Delete an Image |
| [**get**](ImagesApi.md#get) | **GET** /v2/images/{image_id} | Retrieve an Existing Image |
| [**importCustomImageFromUrl**](ImagesApi.md#importCustomImageFromUrl) | **POST** /v2/images | Create a Custom Image |
| [**list**](ImagesApi.md#list) | **GET** /v2/images | List All Images |
| [**update**](ImagesApi.md#update) | **PUT** /v2/images/{image_id} | Update an Image |


<a name="delete"></a>
# **delete**
> delete(imageId).execute();

Delete an Image

To delete a snapshot or custom image, send a &#x60;DELETE&#x60; request to &#x60;/v2/images/$IMAGE_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImagesApi;
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
    Integer imageId = 62137902; // A unique number that can be used to identify and reference a specific image.
    try {
      client
              .images
              .delete(imageId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .images
              .delete(imageId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#delete");
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
| **imageId** | **Integer**| A unique number that can be used to identify and reference a specific image. | |

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
> ImagesGetResponse get(imageId).execute();

Retrieve an Existing Image

To retrieve information about an image, send a &#x60;GET&#x60; request to &#x60;/v2/images/$IDENTIFIER&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImagesApi;
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
    Object imageId = 62137902; // A unique number (id) or string (slug) used to identify and reference a specific image.  **Public** images can be identified by image `id` or `slug`.  **Private** images *must* be identified by image `id`. 
    try {
      ImagesGetResponse result = client
              .images
              .get(imageId)
              .execute();
      System.out.println(result);
      System.out.println(result.getImage());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ImagesGetResponse> response = client
              .images
              .get(imageId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#get");
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
| **imageId** | [**Object**](.md)| A unique number (id) or string (slug) used to identify and reference a specific image.  **Public** images can be identified by image &#x60;id&#x60; or &#x60;slug&#x60;.  **Private** images *must* be identified by image &#x60;id&#x60;.  | |

### Return type

[**ImagesGetResponse**](ImagesGetResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="importCustomImageFromUrl"></a>
# **importCustomImageFromUrl**
> ImagesImportCustomImageFromUrlResponse importCustomImageFromUrl(imageNewCustom).execute();

Create a Custom Image

To create a new custom image, send a POST request to /v2/images. The body must contain a url attribute pointing to a Linux virtual machine image to be imported into DigitalOcean. The image must be in the raw, qcow2, vhdx, vdi, or vmdk format. It may be compressed using gzip or bzip2 and must be smaller than 100 GB after  being decompressed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImagesApi;
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
    String name = "name_example"; // The display name that has been given to an image.  This is what is shown in the control panel and is generally a descriptive title for the image in question.
    String url = "url_example"; // A URL from which the custom Linux virtual machine image may be retrieved.  The image it points to must be in the raw, qcow2, vhdx, vdi, or vmdk format.  It may be compressed using gzip or bzip2 and must be smaller than 100 GB after being decompressed.
    RegionSlug region = RegionSlug.fromValue("ams1");
    String description = "description_example"; // An optional free-form text field to describe an image.
    Distribution distribution = Distribution.fromValue("Arch Linux");
    List<String> tags = Arrays.asList(); // A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags.
    try {
      ImagesImportCustomImageFromUrlResponse result = client
              .images
              .importCustomImageFromUrl(name, url, region)
              .description(description)
              .distribution(distribution)
              .tags(tags)
              .execute();
      System.out.println(result);
      System.out.println(result.getImage());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#importCustomImageFromUrl");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ImagesImportCustomImageFromUrlResponse> response = client
              .images
              .importCustomImageFromUrl(name, url, region)
              .description(description)
              .distribution(distribution)
              .tags(tags)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#importCustomImageFromUrl");
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
| **imageNewCustom** | [**ImageNewCustom**](ImageNewCustom.md)|  | |

### Return type

[**ImagesImportCustomImageFromUrlResponse**](ImagesImportCustomImageFromUrlResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing a subset of the standard  image attributes as listed below, including the image&#39;s &#x60;id&#x60; and &#x60;status&#x60;.  After initial creation, the &#x60;status&#x60; will be &#x60;NEW&#x60;. Using the image&#39;s id, you  may query the image&#39;s status by sending a &#x60;GET&#x60; request to the  &#x60;/v2/images/$IMAGE_ID&#x60; endpoint.  When the &#x60;status&#x60; changes to &#x60;available&#x60;, the image will be ready for use. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> ImagesListResponse list().type(type)._private(_private).tagName(tagName).perPage(perPage).page(page).execute();

List All Images

To list all of the images available on your account, send a GET request to /v2/images.  ## Filtering Results -----  It&#39;s possible to request filtered results by including certain query parameters.  **Image Type**  Either 1-Click Application or OS Distribution images can be filtered by using the &#x60;type&#x60; query parameter.  &gt; Important: The &#x60;type&#x60; query parameter does not directly relate to the &#x60;type&#x60; attribute.  To retrieve only ***distribution*** images, include the &#x60;type&#x60; query parameter set to distribution, &#x60;/v2/images?type&#x3D;distribution&#x60;.  To retrieve only ***application*** images, include the &#x60;type&#x60; query parameter set to application, &#x60;/v2/images?type&#x3D;application&#x60;.  **User Images**  To retrieve only the private images of a user, include the &#x60;private&#x60; query parameter set to true, &#x60;/v2/images?private&#x3D;true&#x60;.  **Tags**  To list all images assigned to a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag in your GET request. For example, &#x60;/v2/images?tag_name&#x3D;$TAG_NAME&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImagesApi;
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
    String type = "application"; // Filters results based on image type which can be either `application` or `distribution`.
    Boolean _private = true; // Used to filter only user images.
    String tagName = "base-image"; // Used to filter images by a specific tag.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      ImagesListResponse result = client
              .images
              .list()
              .type(type)
              ._private(_private)
              .tagName(tagName)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getImages());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ImagesListResponse> response = client
              .images
              .list()
              .type(type)
              ._private(_private)
              .tagName(tagName)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#list");
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
| **type** | **String**| Filters results based on image type which can be either &#x60;application&#x60; or &#x60;distribution&#x60;. | [optional] [enum: application, distribution] |
| **_private** | **Boolean**| Used to filter only user images. | [optional] |
| **tagName** | **String**| Used to filter images by a specific tag. | [optional] |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**ImagesListResponse**](ImagesListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;images&#x60;.  This will be set to an array of image objects, each of which will contain the standard image attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="update"></a>
# **update**
> ImagesUpdateResponse update(imageId, imageUpdate).execute();

Update an Image

To update an image, send a &#x60;PUT&#x60; request to &#x60;/v2/images/$IMAGE_ID&#x60;. Set the &#x60;name&#x60; attribute to the new value you would like to use. For custom images, the &#x60;description&#x60; and &#x60;distribution&#x60; attributes may also be updated. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.ImagesApi;
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
    Integer imageId = 62137902; // A unique number that can be used to identify and reference a specific image.
    String description = "description_example"; // An optional free-form text field to describe an image.
    String name = "name_example"; // The display name that has been given to an image.  This is what is shown in the control panel and is generally a descriptive title for the image in question.
    Distribution distribution = Distribution.fromValue("Arch Linux");
    try {
      ImagesUpdateResponse result = client
              .images
              .update(imageId)
              .description(description)
              .name(name)
              .distribution(distribution)
              .execute();
      System.out.println(result);
      System.out.println(result.getImage());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#update");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ImagesUpdateResponse> response = client
              .images
              .update(imageId)
              .description(description)
              .name(name)
              .distribution(distribution)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling ImagesApi#update");
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
| **imageId** | **Integer**| A unique number that can be used to identify and reference a specific image. | |
| **imageUpdate** | [**ImageUpdate**](ImageUpdate.md)|  | |

### Return type

[**ImagesUpdateResponse**](ImagesUpdateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

