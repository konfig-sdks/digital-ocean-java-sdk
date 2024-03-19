# BlockStorageApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**create**](BlockStorageApi.md#create) | **POST** /v2/volumes | Create a New Block Storage Volume |
| [**create_0**](BlockStorageApi.md#create_0) | **POST** /v2/volumes/{volume_id}/snapshots | Create Snapshot from a Volume |
| [**delete**](BlockStorageApi.md#delete) | **DELETE** /v2/volumes/{volume_id} | Delete a Block Storage Volume |
| [**deleteByRegionAndName**](BlockStorageApi.md#deleteByRegionAndName) | **DELETE** /v2/volumes | Delete a Block Storage Volume by Name |
| [**deleteVolumeSnapshot**](BlockStorageApi.md#deleteVolumeSnapshot) | **DELETE** /v2/volumes/snapshots/{snapshot_id} | Delete a Volume Snapshot |
| [**get**](BlockStorageApi.md#get) | **GET** /v2/volumes/{volume_id} | Retrieve an Existing Block Storage Volume |
| [**getSnapshotDetails**](BlockStorageApi.md#getSnapshotDetails) | **GET** /v2/volumes/snapshots/{snapshot_id} | Retrieve an Existing Volume Snapshot |
| [**list**](BlockStorageApi.md#list) | **GET** /v2/volumes | List All Block Storage Volumes |
| [**list_0**](BlockStorageApi.md#list_0) | **GET** /v2/volumes/{volume_id}/snapshots | List Snapshots for a Volume |


<a name="create"></a>
# **create**
> VolumesCreateResponse create(body).execute();

Create a New Block Storage Volume

To create a new volume, send a POST request to &#x60;/v2/volumes&#x60;. Optionally, a &#x60;filesystem_type&#x60; attribute may be provided in order to automatically format the volume&#39;s filesystem. Pre-formatted volumes are automatically mounted when attached to Ubuntu, Debian, Fedora, Fedora Atomic, and CentOS Droplets created on or after April 26, 2018. Attaching pre-formatted volumes to Droplets without support for auto-mounting is not recommended.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    List<String> tags = Arrays.asList(); // A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags.
    String description = "description_example"; // An optional free-form text field to describe a block storage volume.
    String id = "id_example"; // The unique identifier for the block storage volume.
    List<Integer> dropletIds = Arrays.asList(); // An array containing the IDs of the Droplets the volume is attached to. Note that at this time, a volume can only be attached to a single Droplet.
    String name = "name_example"; // A human-readable name for the block storage volume. Must be lowercase and be composed only of numbers, letters and \\\"-\\\", up to a limit of 64 characters. The name must begin with a letter.
    Integer sizeGigabytes = 56; // The size of the block storage volume in GiB (1024^3). This field does not apply  when creating a volume from a snapshot.
    String createdAt = "createdAt_example"; // A time value given in ISO8601 combined date and time format that represents when the block storage volume was created.
    String snapshotId = "snapshotId_example"; // The unique identifier for the volume snapshot from which to create the volume.
    String filesystemType = "filesystemType_example"; // The name of the filesystem type to be used on the volume. When provided, the volume will automatically be formatted to the specified filesystem type. Currently, the available options are `ext4` and `xfs`. Pre-formatted volumes are automatically mounted when attached to Ubuntu, Debian, Fedora, Fedora Atomic, and CentOS Droplets created on or after April 26, 2018. Attaching pre-formatted volumes to other Droplets is not recommended.
    RegionSlug region = RegionSlug.fromValue("ams1");
    String filesystemLabel = "filesystemLabel_example";
    try {
      VolumesCreateResponse result = client
              .blockStorage
              .create()
              .tags(tags)
              .description(description)
              .id(id)
              .dropletIds(dropletIds)
              .name(name)
              .sizeGigabytes(sizeGigabytes)
              .createdAt(createdAt)
              .snapshotId(snapshotId)
              .filesystemType(filesystemType)
              .region(region)
              .filesystemLabel(filesystemLabel)
              .execute();
      System.out.println(result);
      System.out.println(result.getVolume());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#create");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<VolumesCreateResponse> response = client
              .blockStorage
              .create()
              .tags(tags)
              .description(description)
              .id(id)
              .dropletIds(dropletIds)
              .name(name)
              .sizeGigabytes(sizeGigabytes)
              .createdAt(createdAt)
              .snapshotId(snapshotId)
              .filesystemType(filesystemType)
              .region(region)
              .filesystemLabel(filesystemLabel)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#create");
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
| **body** | **Exvolumes**|  | |

### Return type

[**VolumesCreateResponse**](VolumesCreateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with a key called &#x60;volume&#x60;. The value will be an object containing the standard attributes associated with a volume. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="create_0"></a>
# **create_0**
> BlockStorageGetSnapshotDetailsResponse create_0(volumeId, volumeSnapshotsCreateRequest).execute();

Create Snapshot from a Volume

To create a snapshot from a volume, sent a POST request to &#x60;/v2/volumes/$VOLUME_ID/snapshots&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    String name = "name_example"; // A human-readable name for the volume snapshot.
    UUID volumeId = UUID.fromString("7724db7c-e098-11e5-b522-000f53304e51"); // The ID of the block storage volume.
    List<String> tags = Arrays.asList(); // A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags.
    try {
      BlockStorageGetSnapshotDetailsResponse result = client
              .blockStorage
              .create_0(name, volumeId)
              .tags(tags)
              .execute();
      System.out.println(result);
      System.out.println(result.getSnapshot());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#create_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<BlockStorageGetSnapshotDetailsResponse> response = client
              .blockStorage
              .create_0(name, volumeId)
              .tags(tags)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#create_0");
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
| **volumeId** | **UUID**| The ID of the block storage volume. | |
| **volumeSnapshotsCreateRequest** | [**VolumeSnapshotsCreateRequest**](VolumeSnapshotsCreateRequest.md)|  | |

### Return type

[**BlockStorageGetSnapshotDetailsResponse**](BlockStorageGetSnapshotDetailsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | You will get back a JSON object that has a &#x60;snapshot&#x60; key. This will contain the standard snapshot attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> delete(volumeId).execute();

Delete a Block Storage Volume

To delete a block storage volume, destroying all data and removing it from your account, send a DELETE request to &#x60;/v2/volumes/$VOLUME_ID&#x60;. No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data.  

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    UUID volumeId = UUID.fromString("7724db7c-e098-11e5-b522-000f53304e51"); // The ID of the block storage volume.
    try {
      client
              .blockStorage
              .delete(volumeId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .blockStorage
              .delete(volumeId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#delete");
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
| **volumeId** | **UUID**| The ID of the block storage volume. | |

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

<a name="deleteByRegionAndName"></a>
# **deleteByRegionAndName**
> deleteByRegionAndName().name(name).region(region).execute();

Delete a Block Storage Volume by Name

Block storage volumes may also be deleted by name by sending a DELETE request with the volume&#39;s **name** and the **region slug** for the region it is located in as query parameters to &#x60;/v2/volumes?name&#x3D;$VOLUME_NAME&amp;region&#x3D;nyc1&#x60;. No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data.  

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    String name = "example"; // The block storage volume's name.
    RegionSlug region = RegionSlug.fromValue("ams1"); // The slug identifier for the region where the resource is available.
    try {
      client
              .blockStorage
              .deleteByRegionAndName()
              .name(name)
              .region(region)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#deleteByRegionAndName");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .blockStorage
              .deleteByRegionAndName()
              .name(name)
              .region(region)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#deleteByRegionAndName");
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
| **name** | **String**| The block storage volume&#39;s name. | [optional] |
| **region** | [**RegionSlug**](.md)| The slug identifier for the region where the resource is available. | [optional] [enum: ams1, ams2, ams3, blr1, fra1, lon1, nyc1, nyc2, nyc3, sfo1, sfo2, sfo3, sgp1, tor1] |

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

<a name="deleteVolumeSnapshot"></a>
# **deleteVolumeSnapshot**
> deleteVolumeSnapshot(snapshotId).execute();

Delete a Volume Snapshot

To delete a volume snapshot, send a DELETE request to &#x60;/v2/snapshots/$SNAPSHOT_ID&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    Object snapshotId = 6372321; // Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot.
    try {
      client
              .blockStorage
              .deleteVolumeSnapshot(snapshotId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#deleteVolumeSnapshot");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .blockStorage
              .deleteVolumeSnapshot(snapshotId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#deleteVolumeSnapshot");
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
| **snapshotId** | [**Object**](.md)| Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot. | |

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
> VolumesCreateResponse get(volumeId).execute();

Retrieve an Existing Block Storage Volume

To show information about a block storage volume, send a GET request to &#x60;/v2/volumes/$VOLUME_ID&#x60;.  

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    UUID volumeId = UUID.fromString("7724db7c-e098-11e5-b522-000f53304e51"); // The ID of the block storage volume.
    try {
      VolumesCreateResponse result = client
              .blockStorage
              .get(volumeId)
              .execute();
      System.out.println(result);
      System.out.println(result.getVolume());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<VolumesCreateResponse> response = client
              .blockStorage
              .get(volumeId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#get");
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
| **volumeId** | **UUID**| The ID of the block storage volume. | |

### Return type

[**VolumesCreateResponse**](VolumesCreateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;volume&#x60;. The value will be an object containing the standard attributes associated with a volume. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getSnapshotDetails"></a>
# **getSnapshotDetails**
> BlockStorageGetSnapshotDetailsResponse getSnapshotDetails(snapshotId).execute();

Retrieve an Existing Volume Snapshot

To retrieve the details of a snapshot that has been created from a volume, send a GET request to &#x60;/v2/volumes/snapshots/$SNAPSHOT_ID&#x60;.  

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    Object snapshotId = 6372321; // Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot.
    try {
      BlockStorageGetSnapshotDetailsResponse result = client
              .blockStorage
              .getSnapshotDetails(snapshotId)
              .execute();
      System.out.println(result);
      System.out.println(result.getSnapshot());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#getSnapshotDetails");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<BlockStorageGetSnapshotDetailsResponse> response = client
              .blockStorage
              .getSnapshotDetails(snapshotId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#getSnapshotDetails");
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
| **snapshotId** | [**Object**](.md)| Either the ID of an existing snapshot. This will be an integer for a Droplet snapshot or a string for a volume snapshot. | |

### Return type

[**BlockStorageGetSnapshotDetailsResponse**](BlockStorageGetSnapshotDetailsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | You will get back a JSON object that has a &#x60;snapshot&#x60; key. This will contain the standard snapshot attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> VolumesListResponse list().name(name).region(region).perPage(perPage).page(page).execute();

List All Block Storage Volumes

To list all of the block storage volumes available on your account, send a GET request to &#x60;/v2/volumes&#x60;. ## Filtering Results ### By Region The &#x60;region&#x60; may be provided as query parameter in order to restrict results to volumes available in a specific region. For example: &#x60;/v2/volumes?region&#x3D;nyc1&#x60; ### By Name It is also possible to list volumes on your account that match a specified name. To do so, send a GET request with the volume&#39;s name as a query parameter to &#x60;/v2/volumes?name&#x3D;$VOLUME_NAME&#x60;. **Note:** You can only create one volume per region with the same name. ### By Name and Region It is also possible to retrieve information about a block storage volume by name. To do so, send a GET request with the volume&#39;s name and the region slug for the region it is located in as query parameters to &#x60;/v2/volumes?name&#x3D;$VOLUME_NAME&amp;region&#x3D;nyc1&#x60;.   

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    String name = "example"; // The block storage volume's name.
    RegionSlug region = RegionSlug.fromValue("ams1"); // The slug identifier for the region where the resource is available.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      VolumesListResponse result = client
              .blockStorage
              .list()
              .name(name)
              .region(region)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getVolumes());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<VolumesListResponse> response = client
              .blockStorage
              .list()
              .name(name)
              .region(region)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#list");
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
| **name** | **String**| The block storage volume&#39;s name. | [optional] |
| **region** | [**RegionSlug**](.md)| The slug identifier for the region where the resource is available. | [optional] [enum: ams1, ams2, ams3, blr1, fra1, lon1, nyc1, nyc2, nyc3, sfo1, sfo2, sfo3, sgp1, tor1] |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**VolumesListResponse**](VolumesListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;volumes&#x60;. This will be set to an array of volume objects, each of which will contain the standard volume attributes. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list_0"></a>
# **list_0**
> VolumeSnapshotsListResponse list_0(volumeId).perPage(perPage).page(page).execute();

List Snapshots for a Volume

To retrieve the snapshots that have been created from a volume, send a GET request to &#x60;/v2/volumes/$VOLUME_ID/snapshots&#x60;.  

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.BlockStorageApi;
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
    UUID volumeId = UUID.fromString("7724db7c-e098-11e5-b522-000f53304e51"); // The ID of the block storage volume.
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      VolumeSnapshotsListResponse result = client
              .blockStorage
              .list_0(volumeId)
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getSnapshots());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#list_0");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<VolumeSnapshotsListResponse> response = client
              .blockStorage
              .list_0(volumeId)
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling BlockStorageApi#list_0");
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
| **volumeId** | **UUID**| The ID of the block storage volume. | |
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**VolumeSnapshotsListResponse**](VolumeSnapshotsListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | You will get back a JSON object that has a &#x60;snapshots&#x60; key. This will be set to an array of snapshot objects, each of which contain the standard snapshot attributes |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

