/*
 * DigitalOcean API
 * # Introduction  The DigitalOcean API allows you to manage Droplets and resources within the DigitalOcean cloud in a simple, programmatic way using conventional HTTP requests.  All of the functionality that you are familiar with in the DigitalOcean control panel is also available through the API, allowing you to script the complex actions that your situation requires.  The API documentation will start with a general overview about the design and technology that has been implemented, followed by reference information about specific endpoints.  ## Requests  Any tool that is fluent in HTTP can communicate with the API simply by requesting the correct URI. Requests should be made using the HTTPS protocol so that traffic is encrypted. The interface responds to different methods depending on the action required.  |Method|Usage| |--- |--- | |GET|For simple retrieval of information about your account, Droplets, or environment, you should use the GET method.  The information you request will be returned to you as a JSON object. The attributes defined by the JSON object can be used to form additional requests.  Any request using the GET method is read-only and will not affect any of the objects you are querying.| |DELETE|To destroy a resource and remove it from your account and environment, the DELETE method should be used.  This will remove the specified object if it is found.  If it is not found, the operation will return a response indicating that the object was not found. This idempotency means that you do not have to check for a resource's availability prior to issuing a delete command, the final state will be the same regardless of its existence.| |PUT|To update the information about a resource in your account, the PUT method is available. Like the DELETE Method, the PUT method is idempotent.  It sets the state of the target using the provided values, regardless of their current values. Requests using the PUT method do not need to check the current attributes of the object.| |PATCH|Some resources support partial modification. In these cases, the PATCH method is available. Unlike PUT which generally requires a complete representation of a resource, a PATCH request is is a set of instructions on how to modify a resource updating only specific attributes.| |POST|To create a new object, your request should specify the POST method. The POST request includes all of the attributes necessary to create a new object.  When you wish to create a new object, send a POST request to the target endpoint.| |HEAD|Finally, to retrieve metadata information, you should use the HEAD method to get the headers.  This returns only the header of what would be returned with an associated GET request. Response headers contain some useful information about your API access and the results that are available for your request. For instance, the headers contain your current rate-limit value and the amount of time available until the limit resets. It also contains metrics about the total number of objects found, pagination information, and the total content length.|   ## HTTP Statuses  Along with the HTTP methods that the API responds to, it will also return standard HTTP statuses, including error codes.  In the event of a problem, the status will contain the error code, while the body of the response will usually contain additional information about the problem that was encountered.  In general, if the status returned is in the 200 range, it indicates that the request was fulfilled successfully and that no error was encountered.  Return codes in the 400 range typically indicate that there was an issue with the request that was sent. Among other things, this could mean that you did not authenticate correctly, that you are requesting an action that you do not have authorization for, that the object you are requesting does not exist, or that your request is malformed.  If you receive a status in the 500 range, this generally indicates a server-side problem. This means that we are having an issue on our end and cannot fulfill your request currently.  400 and 500 level error responses will include a JSON object in their body, including the following attributes:  |Name|Type|Description| |--- |--- |--- | |id|string|A short identifier corresponding to the HTTP status code returned. For example, the ID for a response returning a 404 status code would be \"not_found.\"| |message|string|A message providing additional information about the error, including details to help resolve it when possible.| |request_id|string|Optionally, some endpoints may include a request ID that should be provided when reporting bugs or opening support tickets to help identify the issue.|  ### Example Error Response  ```     HTTP/1.1 403 Forbidden     {       \"id\":       \"forbidden\",       \"message\":  \"You do not have access for the attempted action.\"     } ```  ## Responses  When a request is successful, a response body will typically be sent back in the form of a JSON object. An exception to this is when a DELETE request is processed, which will result in a successful HTTP 204 status and an empty response body.  Inside of this JSON object, the resource root that was the target of the request will be set as the key. This will be the singular form of the word if the request operated on a single object, and the plural form of the word if a collection was processed.  For example, if you send a GET request to `/v2/droplets/$DROPLET_ID` you will get back an object with a key called \"`droplet`\". However, if you send the GET request to the general collection at `/v2/droplets`, you will get back an object with a key called \"`droplets`\".  The value of these keys will generally be a JSON object for a request on a single object and an array of objects for a request on a collection of objects.  ### Response for a Single Object  ```     {         \"droplet\": {             \"name\": \"example.com\"             . . .         }     } ```  ### Response for an Object Collection  ```     {         \"droplets\": [             {                 \"name\": \"example.com\"                 . . .             },             {                 \"name\": \"second.com\"                 . . .             }         ]     } ```  ## Meta  In addition to the main resource root, the response may also contain a `meta` object. This object contains information about the response itself.  The `meta` object contains a `total` key that is set to the total number of objects returned by the request. This has implications on the `links` object and pagination.  The `meta` object will only be displayed when it has a value. Currently, the `meta` object will have a value when a request is made on a collection (like `droplets` or `domains`).   ### Sample Meta Object  ```     {         . . .         \"meta\": {             \"total\": 43         }         . . .     } ```  ## Links & Pagination  The `links` object is returned as part of the response body when pagination is enabled. By default, 20 objects are returned per page. If the response contains 20 objects or fewer, no `links` object will be returned. If the response contains more than 20 objects, the first 20 will be returned along with the `links` object.  You can request a different pagination limit or force pagination by appending `?per_page=` to the request with the number of items you would like per page. For instance, to show only two results per page, you could add `?per_page=2` to the end of your query. The maximum number of results per page is 200.  The `links` object contains a `pages` object. The `pages` object, in turn, contains keys indicating the relationship of additional pages. The values of these are the URLs of the associated pages. The keys will be one of the following:  *   **first**: The URI of the first page of results. *   **prev**: The URI of the previous sequential page of results. *   **next**: The URI of the next sequential page of results. *   **last**: The URI of the last page of results.  The `pages` object will only include the links that make sense. So for the first page of results, no `first` or `prev` links will ever be set. This convention holds true in other situations where a link would not make sense.  ### Sample Links Object  ```     {         . . .         \"links\": {             \"pages\": {                 \"last\": \"https://api.digitalocean.com/v2/images?page=2\",                 \"next\": \"https://api.digitalocean.com/v2/images?page=2\"             }         }         . . .     } ```  ## Rate Limit  Requests through the API are rate limited per OAuth token. Current rate limits:  *   5,000 requests per hour *   250 requests per minute (5% of the hourly total)  Once you exceed either limit, you will be rate limited until the next cycle starts. Space out any requests that you would otherwise issue in bursts for the best results.  The rate limiting information is contained within the response headers of each request. The relevant headers are:  *   **ratelimit-limit**: The number of requests that can be made per hour. *   **ratelimit-remaining**: The number of requests that remain before you hit your request limit. See the information below for how the request limits expire. *   **ratelimit-reset**: This represents the time when the oldest request will expire. The value is given in [Unix epoch time](http://en.wikipedia.org/wiki/Unix_time). See below for more information about how request limits expire.  More rate limiting information is returned only within burst limit error response headers: *   **retry-after**: The number of seconds to wait before making another request when rate limited.  As long as the `ratelimit-remaining` count is above zero, you will be able to make additional requests.  The way that a request expires and is removed from the current limit count is important to understand. Rather than counting all of the requests for an hour and resetting the `ratelimit-remaining` value at the end of the hour, each request instead has its own timer.  This means that each request contributes toward the `ratelimit-remaining` count for one complete hour after the request is made. When that request's timer runs out, it is no longer counted towards the request limit.  This has implications on the meaning of the `ratelimit-reset` header as well. Because the entire rate limit is not reset at one time, the value of this header is set to the time when the _oldest_ request will expire.  Keep this in mind if you see your `ratelimit-reset` value change, but not move an entire hour into the future.  If the `ratelimit-remaining` reaches zero, subsequent requests will receive a 429 error code until the request reset has been reached.   `ratelimit-remaining` reaching zero can also indicate that the \"burst limit\" of 250  requests per minute limit was met, even if the 5,000 requests per hour limit was not.  In this case, the 429 error response will include a retry-after header to indicate how  long to wait (in seconds) until the request may be retried.  You can see the format of the response in the examples.   **Note:** The following endpoints have special rate limit requirements that are independent of the limits defined above.  *   Only 12 `POST` requests to the `/v2/floating_ips` endpoint to create Floating IPs can be made per 60 seconds. *   Only 10 `GET` requests to the `/v2/account/keys` endpoint to list SSH keys can be made per 60 seconds. *   Only 5 requests to any and all `v2/cdn/endpoints` can be made per 10 seconds. This includes `v2/cdn/endpoints`,      `v2/cdn/endpoints/$ENDPOINT_ID`, and `v2/cdn/endpoints/$ENDPOINT_ID/cache`. *   Only 50 strings within the `files` json struct in the `v2/cdn/endpoints/$ENDPOINT_ID/cache` [payload](https://docs.digitalocean.com/reference/api/api-reference/#operation/cdn_purge_cache)      can be requested every 20 seconds.  ### Sample Rate Limit Headers  ```     . . .     ratelimit-limit: 1200     ratelimit-remaining: 1193     rateLimit-reset: 1402425459     . . . ```    ### Sample Rate Limit Headers When Burst Limit is Reached:  ```     . . .     ratelimit-limit: 5000     ratelimit-remaining: 0     rateLimit-reset: 1402425459     retry-after: 29     . . . ```  ### Sample Rate Exceeded Response  ```     429 Too Many Requests     {             id: \"too_many_requests\",             message: \"API Rate limit exceeded.\"     } ```  ## Curl Examples  Throughout this document, some example API requests will be given using the `curl` command. This will allow us to demonstrate the various endpoints in a simple, textual format.      These examples assume that you are using a Linux or macOS command line. To run these commands on a Windows machine, you can either use cmd.exe, PowerShell, or WSL:  * For cmd.exe, use the `set VAR=VALUE` [syntax](https://docs.microsoft.com/en-us/windows-server/administration/windows-commands/set_1) to define environment variables, call them with `%VAR%`, then replace all backslashes (`\\`) in the examples with carets (`^`).  * For PowerShell, use the `$Env:VAR = \"VALUE\"` [syntax](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_environment_variables?view=powershell-7.2) to define environment variables, call them with `$Env:VAR`, then replace `curl` with `curl.exe` and all backslashes (`\\`) in the examples with backticks (`` ` ``).  * WSL is a compatibility layer that allows you to emulate a Linux terminal on a Windows machine. Install WSL with our [community tutorial](https://www.digitalocean.com/community/tutorials/how-to-install-the-windows-subsystem-for-linux-2-on-microsoft-windows-10),  then follow this API documentation normally.  The names of account-specific references (like Droplet IDs, for instance) will be represented by variables. For instance, a Droplet ID may be represented by a variable called `$DROPLET_ID`. You can set the associated variables in your environment if you wish to use the examples without modification.  The first variable that you should set to get started is your OAuth authorization token. The next section will go over the details of this, but you can set an environmental variable for it now.  Generate a token by going to the [Apps & API](https://cloud.digitalocean.com/settings/applications) section of the DigitalOcean control panel. Use an existing token if you have saved one, or generate a new token with the \"Generate new token\" button. Copy the generated token and use it to set and export the TOKEN variable in your environment as the example shows.  You may also wish to set some other variables now or as you go along. For example, you may wish to set the `DROPLET_ID` variable to one of your Droplet IDs since this will be used frequently in the API.  If you are following along, make sure you use a Droplet ID that you control so that your commands will execute correctly.  If you need access to the headers of a response through `curl`, you can pass the `-i` flag to display the header information along with the body. If you are only interested in the header, you can instead pass the `-I` flag, which will exclude the response body entirely.   ### Set and Export your OAuth Token  ``` export DIGITALOCEAN_TOKEN=your_token_here ```  ### Set and Export a Variable  ``` export DROPLET_ID=1111111 ```  ## Parameters  There are two different ways to pass parameters in a request with the API.  When passing parameters to create or update an object, parameters should be passed as a JSON object containing the appropriate attribute names and values as key-value pairs. When you use this format, you should specify that you are sending a JSON object in the header. This is done by setting the `Content-Type` header to `application/json`. This ensures that your request is interpreted correctly.  When passing parameters to filter a response on GET requests, parameters can be passed using standard query attributes. In this case, the parameters would be embedded into the URI itself by appending a `?` to the end of the URI and then setting each attribute with an equal sign. Attributes can be separated with a `&`. Tools like `curl` can create the appropriate URI when given parameters and values; this can also be done using the `-F` flag and then passing the key and value as an argument. The argument should take the form of a quoted string with the attribute being set to a value with an equal sign.  ### Pass Parameters as a JSON Object  ```     curl -H \"Authorization: Bearer $DIGITALOCEAN_TOKEN\" \\         -H \"Content-Type: application/json\" \\         -d '{\"name\": \"example.com\", \"ip_address\": \"127.0.0.1\"}' \\         -X POST \"https://api.digitalocean.com/v2/domains\" ```  ### Pass Filter Parameters as a Query String  ```      curl -H \"Authorization: Bearer $DIGITALOCEAN_TOKEN\" \\          -X GET \\          \"https://api.digitalocean.com/v2/images?private=true\" ```  ## Cross Origin Resource Sharing  In order to make requests to the API from other domains, the API implements Cross Origin Resource Sharing (CORS) support.  CORS support is generally used to create AJAX requests outside of the domain that the request originated from. This is necessary to implement projects like control panels utilizing the API. This tells the browser that it can send requests to an outside domain.  The procedure that the browser initiates in order to perform these actions (other than GET requests) begins by sending a \"preflight\" request. This sets the `Origin` header and uses the `OPTIONS` method. The server will reply back with the methods it allows and some of the limits it imposes. The client then sends the actual request if it falls within the allowed constraints.  This process is usually done in the background by the browser, but you can use curl to emulate this process using the example provided. The headers that will be set to show the constraints are:  *   **Access-Control-Allow-Origin**: This is the domain that is sent by the client or browser as the origin of the request. It is set through an `Origin` header. *   **Access-Control-Allow-Methods**: This specifies the allowed options for requests from that domain. This will generally be all available methods. *   **Access-Control-Expose-Headers**: This will contain the headers that will be available to requests from the origin domain. *   **Access-Control-Max-Age**: This is the length of time that the access is considered valid. After this expires, a new preflight should be sent. *   **Access-Control-Allow-Credentials**: This will be set to `true`. It basically allows you to send your OAuth token for authentication.  You should not need to be concerned with the details of these headers, because the browser will typically do all of the work for you. 
 *
 * The version of the OpenAPI document: 2.0
 * Contact: api-engineering@digitalocean.com
 *
 * NOTE: This class is auto generated by Konfig (https://konfigthis.com).
 * Do not edit the class manually.
 */


package com.konfigthis.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.konfigthis.client.model.KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy;
import com.konfigthis.client.model.KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner;
import com.konfigthis.client.model.KubernetesUpdateClusterResponseKubernetesClusterStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.konfigthis.client.JSON;

/**
 * KubernetesUpdateClusterResponseKubernetesCluster
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class KubernetesUpdateClusterResponseKubernetesCluster {
  public static final String SERIALIZED_NAME_TAGS = "tags";
  @SerializedName(SERIALIZED_NAME_TAGS)
  private List<String> tags = null;

  public static final String SERIALIZED_NAME_VERSION = "version";
  @SerializedName(SERIALIZED_NAME_VERSION)
  private String version;

  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private String id;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_REGION = "region";
  @SerializedName(SERIALIZED_NAME_REGION)
  private String region;

  public static final String SERIALIZED_NAME_CLUSTER_SUBNET = "cluster_subnet";
  @SerializedName(SERIALIZED_NAME_CLUSTER_SUBNET)
  private String clusterSubnet;

  public static final String SERIALIZED_NAME_SERVICE_SUBNET = "service_subnet";
  @SerializedName(SERIALIZED_NAME_SERVICE_SUBNET)
  private String serviceSubnet;

  public static final String SERIALIZED_NAME_VPC_UUID = "vpc_uuid";
  @SerializedName(SERIALIZED_NAME_VPC_UUID)
  private String vpcUuid;

  public static final String SERIALIZED_NAME_IPV4 = "ipv4";
  @SerializedName(SERIALIZED_NAME_IPV4)
  private String ipv4;

  public static final String SERIALIZED_NAME_ENDPOINT = "endpoint";
  @SerializedName(SERIALIZED_NAME_ENDPOINT)
  private String endpoint;

  public static final String SERIALIZED_NAME_NODE_POOLS = "node_pools";
  @SerializedName(SERIALIZED_NAME_NODE_POOLS)
  private List<KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner> nodePools = null;

  public static final String SERIALIZED_NAME_MAINTENANCE_POLICY = "maintenance_policy";
  @SerializedName(SERIALIZED_NAME_MAINTENANCE_POLICY)
  private KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy maintenancePolicy;

  public static final String SERIALIZED_NAME_AUTO_UPGRADE = "auto_upgrade";
  @SerializedName(SERIALIZED_NAME_AUTO_UPGRADE)
  private Boolean autoUpgrade;

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  private KubernetesUpdateClusterResponseKubernetesClusterStatus status;

  public static final String SERIALIZED_NAME_CREATED_AT = "created_at";
  @SerializedName(SERIALIZED_NAME_CREATED_AT)
  private String createdAt;

  public static final String SERIALIZED_NAME_UPDATED_AT = "updated_at";
  @SerializedName(SERIALIZED_NAME_UPDATED_AT)
  private String updatedAt;

  public static final String SERIALIZED_NAME_SURGE_UPGRADE = "surge_upgrade";
  @SerializedName(SERIALIZED_NAME_SURGE_UPGRADE)
  private Boolean surgeUpgrade;

  public static final String SERIALIZED_NAME_REGISTRY_ENABLED = "registry_enabled";
  @SerializedName(SERIALIZED_NAME_REGISTRY_ENABLED)
  private Boolean registryEnabled;

  public static final String SERIALIZED_NAME_HA = "ha";
  @SerializedName(SERIALIZED_NAME_HA)
  private Boolean ha;

  public KubernetesUpdateClusterResponseKubernetesCluster() {
  }

  public KubernetesUpdateClusterResponseKubernetesCluster tags(List<String> tags) {
    
    
    
    
    this.tags = tags;
    return this;
  }

  public KubernetesUpdateClusterResponseKubernetesCluster addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * Get tags
   * @return tags
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<String> getTags() {
    return tags;
  }


  public void setTags(List<String> tags) {
    
    
    
    this.tags = tags;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster version(String version) {
    
    
    
    
    this.version = version;
    return this;
  }

   /**
   * Get version
   * @return version
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1.18.6-do.0", value = "")

  public String getVersion() {
    return version;
  }


  public void setVersion(String version) {
    
    
    
    this.version = version;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster id(String id) {
    
    
    
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "bd5f5959-5e1e-4205-a714-a914373942af", value = "")

  public String getId() {
    return id;
  }


  public void setId(String id) {
    
    
    
    this.id = id;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster name(String name) {
    
    
    
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "prod-cluster-01", value = "")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    
    
    
    this.name = name;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster region(String region) {
    
    
    
    
    this.region = region;
    return this;
  }

   /**
   * Get region
   * @return region
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "nyc1", value = "")

  public String getRegion() {
    return region;
  }


  public void setRegion(String region) {
    
    
    
    this.region = region;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster clusterSubnet(String clusterSubnet) {
    
    
    
    
    this.clusterSubnet = clusterSubnet;
    return this;
  }

   /**
   * Get clusterSubnet
   * @return clusterSubnet
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10.244.0.0/16", value = "")

  public String getClusterSubnet() {
    return clusterSubnet;
  }


  public void setClusterSubnet(String clusterSubnet) {
    
    
    
    this.clusterSubnet = clusterSubnet;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster serviceSubnet(String serviceSubnet) {
    
    
    
    
    this.serviceSubnet = serviceSubnet;
    return this;
  }

   /**
   * Get serviceSubnet
   * @return serviceSubnet
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10.245.0.0/16", value = "")

  public String getServiceSubnet() {
    return serviceSubnet;
  }


  public void setServiceSubnet(String serviceSubnet) {
    
    
    
    this.serviceSubnet = serviceSubnet;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster vpcUuid(String vpcUuid) {
    
    
    
    
    this.vpcUuid = vpcUuid;
    return this;
  }

   /**
   * Get vpcUuid
   * @return vpcUuid
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "c33931f2-a26a-4e61-b85c-4e95a2ec431b", value = "")

  public String getVpcUuid() {
    return vpcUuid;
  }


  public void setVpcUuid(String vpcUuid) {
    
    
    
    this.vpcUuid = vpcUuid;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster ipv4(String ipv4) {
    
    
    
    
    this.ipv4 = ipv4;
    return this;
  }

   /**
   * Get ipv4
   * @return ipv4
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "68.183.121.157", value = "")

  public String getIpv4() {
    return ipv4;
  }


  public void setIpv4(String ipv4) {
    
    
    
    this.ipv4 = ipv4;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster endpoint(String endpoint) {
    
    
    
    
    this.endpoint = endpoint;
    return this;
  }

   /**
   * Get endpoint
   * @return endpoint
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "https://bd5f5959-5e1e-4205-a714-a914373942af.k8s.ondigitalocean.com", value = "")

  public String getEndpoint() {
    return endpoint;
  }


  public void setEndpoint(String endpoint) {
    
    
    
    this.endpoint = endpoint;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster nodePools(List<KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner> nodePools) {
    
    
    
    
    this.nodePools = nodePools;
    return this;
  }

  public KubernetesUpdateClusterResponseKubernetesCluster addNodePoolsItem(KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner nodePoolsItem) {
    if (this.nodePools == null) {
      this.nodePools = new ArrayList<>();
    }
    this.nodePools.add(nodePoolsItem);
    return this;
  }

   /**
   * Get nodePools
   * @return nodePools
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner> getNodePools() {
    return nodePools;
  }


  public void setNodePools(List<KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner> nodePools) {
    
    
    
    this.nodePools = nodePools;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster maintenancePolicy(KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy maintenancePolicy) {
    
    
    
    
    this.maintenancePolicy = maintenancePolicy;
    return this;
  }

   /**
   * Get maintenancePolicy
   * @return maintenancePolicy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy getMaintenancePolicy() {
    return maintenancePolicy;
  }


  public void setMaintenancePolicy(KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy maintenancePolicy) {
    
    
    
    this.maintenancePolicy = maintenancePolicy;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster autoUpgrade(Boolean autoUpgrade) {
    
    
    
    
    this.autoUpgrade = autoUpgrade;
    return this;
  }

   /**
   * Get autoUpgrade
   * @return autoUpgrade
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "")

  public Boolean getAutoUpgrade() {
    return autoUpgrade;
  }


  public void setAutoUpgrade(Boolean autoUpgrade) {
    
    
    
    this.autoUpgrade = autoUpgrade;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster status(KubernetesUpdateClusterResponseKubernetesClusterStatus status) {
    
    
    
    
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public KubernetesUpdateClusterResponseKubernetesClusterStatus getStatus() {
    return status;
  }


  public void setStatus(KubernetesUpdateClusterResponseKubernetesClusterStatus status) {
    
    
    
    this.status = status;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster createdAt(String createdAt) {
    
    
    
    
    this.createdAt = createdAt;
    return this;
  }

   /**
   * Get createdAt
   * @return createdAt
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2018-11-15T16:00:11Z", value = "")

  public String getCreatedAt() {
    return createdAt;
  }


  public void setCreatedAt(String createdAt) {
    
    
    
    this.createdAt = createdAt;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster updatedAt(String updatedAt) {
    
    
    
    
    this.updatedAt = updatedAt;
    return this;
  }

   /**
   * Get updatedAt
   * @return updatedAt
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2018-11-15T16:00:11Z", value = "")

  public String getUpdatedAt() {
    return updatedAt;
  }


  public void setUpdatedAt(String updatedAt) {
    
    
    
    this.updatedAt = updatedAt;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster surgeUpgrade(Boolean surgeUpgrade) {
    
    
    
    
    this.surgeUpgrade = surgeUpgrade;
    return this;
  }

   /**
   * Get surgeUpgrade
   * @return surgeUpgrade
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "")

  public Boolean getSurgeUpgrade() {
    return surgeUpgrade;
  }


  public void setSurgeUpgrade(Boolean surgeUpgrade) {
    
    
    
    this.surgeUpgrade = surgeUpgrade;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster registryEnabled(Boolean registryEnabled) {
    
    
    
    
    this.registryEnabled = registryEnabled;
    return this;
  }

   /**
   * Get registryEnabled
   * @return registryEnabled
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "false", value = "")

  public Boolean getRegistryEnabled() {
    return registryEnabled;
  }


  public void setRegistryEnabled(Boolean registryEnabled) {
    
    
    
    this.registryEnabled = registryEnabled;
  }


  public KubernetesUpdateClusterResponseKubernetesCluster ha(Boolean ha) {
    
    
    
    
    this.ha = ha;
    return this;
  }

   /**
   * Get ha
   * @return ha
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "false", value = "")

  public Boolean getHa() {
    return ha;
  }


  public void setHa(Boolean ha) {
    
    
    
    this.ha = ha;
  }

  /**
   * A container for additional, undeclared properties.
   * This is a holder for any undeclared properties as specified with
   * the 'additionalProperties' keyword in the OAS document.
   */
  private Map<String, Object> additionalProperties;

  /**
   * Set the additional (undeclared) property with the specified name and value.
   * If the property does not already exist, create it otherwise replace it.
   *
   * @param key name of the property
   * @param value value of the property
   * @return the KubernetesUpdateClusterResponseKubernetesCluster instance itself
   */
  public KubernetesUpdateClusterResponseKubernetesCluster putAdditionalProperty(String key, Object value) {
    if (this.additionalProperties == null) {
        this.additionalProperties = new HashMap<String, Object>();
    }
    this.additionalProperties.put(key, value);
    return this;
  }

  /**
   * Return the additional (undeclared) property.
   *
   * @return a map of objects
   */
  public Map<String, Object> getAdditionalProperties() {
    return additionalProperties;
  }

  /**
   * Return the additional (undeclared) property with the specified name.
   *
   * @param key name of the property
   * @return an object
   */
  public Object getAdditionalProperty(String key) {
    if (this.additionalProperties == null) {
        return null;
    }
    return this.additionalProperties.get(key);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KubernetesUpdateClusterResponseKubernetesCluster kubernetesUpdateClusterResponseKubernetesCluster = (KubernetesUpdateClusterResponseKubernetesCluster) o;
    return Objects.equals(this.tags, kubernetesUpdateClusterResponseKubernetesCluster.tags) &&
        Objects.equals(this.version, kubernetesUpdateClusterResponseKubernetesCluster.version) &&
        Objects.equals(this.id, kubernetesUpdateClusterResponseKubernetesCluster.id) &&
        Objects.equals(this.name, kubernetesUpdateClusterResponseKubernetesCluster.name) &&
        Objects.equals(this.region, kubernetesUpdateClusterResponseKubernetesCluster.region) &&
        Objects.equals(this.clusterSubnet, kubernetesUpdateClusterResponseKubernetesCluster.clusterSubnet) &&
        Objects.equals(this.serviceSubnet, kubernetesUpdateClusterResponseKubernetesCluster.serviceSubnet) &&
        Objects.equals(this.vpcUuid, kubernetesUpdateClusterResponseKubernetesCluster.vpcUuid) &&
        Objects.equals(this.ipv4, kubernetesUpdateClusterResponseKubernetesCluster.ipv4) &&
        Objects.equals(this.endpoint, kubernetesUpdateClusterResponseKubernetesCluster.endpoint) &&
        Objects.equals(this.nodePools, kubernetesUpdateClusterResponseKubernetesCluster.nodePools) &&
        Objects.equals(this.maintenancePolicy, kubernetesUpdateClusterResponseKubernetesCluster.maintenancePolicy) &&
        Objects.equals(this.autoUpgrade, kubernetesUpdateClusterResponseKubernetesCluster.autoUpgrade) &&
        Objects.equals(this.status, kubernetesUpdateClusterResponseKubernetesCluster.status) &&
        Objects.equals(this.createdAt, kubernetesUpdateClusterResponseKubernetesCluster.createdAt) &&
        Objects.equals(this.updatedAt, kubernetesUpdateClusterResponseKubernetesCluster.updatedAt) &&
        Objects.equals(this.surgeUpgrade, kubernetesUpdateClusterResponseKubernetesCluster.surgeUpgrade) &&
        Objects.equals(this.registryEnabled, kubernetesUpdateClusterResponseKubernetesCluster.registryEnabled) &&
        Objects.equals(this.ha, kubernetesUpdateClusterResponseKubernetesCluster.ha)&&
        Objects.equals(this.additionalProperties, kubernetesUpdateClusterResponseKubernetesCluster.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tags, version, id, name, region, clusterSubnet, serviceSubnet, vpcUuid, ipv4, endpoint, nodePools, maintenancePolicy, autoUpgrade, status, createdAt, updatedAt, surgeUpgrade, registryEnabled, ha, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KubernetesUpdateClusterResponseKubernetesCluster {\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    clusterSubnet: ").append(toIndentedString(clusterSubnet)).append("\n");
    sb.append("    serviceSubnet: ").append(toIndentedString(serviceSubnet)).append("\n");
    sb.append("    vpcUuid: ").append(toIndentedString(vpcUuid)).append("\n");
    sb.append("    ipv4: ").append(toIndentedString(ipv4)).append("\n");
    sb.append("    endpoint: ").append(toIndentedString(endpoint)).append("\n");
    sb.append("    nodePools: ").append(toIndentedString(nodePools)).append("\n");
    sb.append("    maintenancePolicy: ").append(toIndentedString(maintenancePolicy)).append("\n");
    sb.append("    autoUpgrade: ").append(toIndentedString(autoUpgrade)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    updatedAt: ").append(toIndentedString(updatedAt)).append("\n");
    sb.append("    surgeUpgrade: ").append(toIndentedString(surgeUpgrade)).append("\n");
    sb.append("    registryEnabled: ").append(toIndentedString(registryEnabled)).append("\n");
    sb.append("    ha: ").append(toIndentedString(ha)).append("\n");
    sb.append("    additionalProperties: ").append(toIndentedString(additionalProperties)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static HashSet<String> openapiFields;
  public static HashSet<String> openapiRequiredFields;

  static {
    // a set of all properties/fields (JSON key names)
    openapiFields = new HashSet<String>();
    openapiFields.add("tags");
    openapiFields.add("version");
    openapiFields.add("id");
    openapiFields.add("name");
    openapiFields.add("region");
    openapiFields.add("cluster_subnet");
    openapiFields.add("service_subnet");
    openapiFields.add("vpc_uuid");
    openapiFields.add("ipv4");
    openapiFields.add("endpoint");
    openapiFields.add("node_pools");
    openapiFields.add("maintenance_policy");
    openapiFields.add("auto_upgrade");
    openapiFields.add("status");
    openapiFields.add("created_at");
    openapiFields.add("updated_at");
    openapiFields.add("surge_upgrade");
    openapiFields.add("registry_enabled");
    openapiFields.add("ha");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to KubernetesUpdateClusterResponseKubernetesCluster
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!KubernetesUpdateClusterResponseKubernetesCluster.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in KubernetesUpdateClusterResponseKubernetesCluster is not found in the empty JSON string", KubernetesUpdateClusterResponseKubernetesCluster.openapiRequiredFields.toString()));
        }
      }
      // ensure the optional json data is an array if present
      if (jsonObj.get("tags") != null && !jsonObj.get("tags").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `tags` to be an array in the JSON string but got `%s`", jsonObj.get("tags").toString()));
      }
      if ((jsonObj.get("version") != null && !jsonObj.get("version").isJsonNull()) && !jsonObj.get("version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("version").toString()));
      }
      if ((jsonObj.get("id") != null && !jsonObj.get("id").isJsonNull()) && !jsonObj.get("id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
      }
      if ((jsonObj.get("name") != null && !jsonObj.get("name").isJsonNull()) && !jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if ((jsonObj.get("region") != null && !jsonObj.get("region").isJsonNull()) && !jsonObj.get("region").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `region` to be a primitive type in the JSON string but got `%s`", jsonObj.get("region").toString()));
      }
      if ((jsonObj.get("cluster_subnet") != null && !jsonObj.get("cluster_subnet").isJsonNull()) && !jsonObj.get("cluster_subnet").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `cluster_subnet` to be a primitive type in the JSON string but got `%s`", jsonObj.get("cluster_subnet").toString()));
      }
      if ((jsonObj.get("service_subnet") != null && !jsonObj.get("service_subnet").isJsonNull()) && !jsonObj.get("service_subnet").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `service_subnet` to be a primitive type in the JSON string but got `%s`", jsonObj.get("service_subnet").toString()));
      }
      if ((jsonObj.get("vpc_uuid") != null && !jsonObj.get("vpc_uuid").isJsonNull()) && !jsonObj.get("vpc_uuid").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `vpc_uuid` to be a primitive type in the JSON string but got `%s`", jsonObj.get("vpc_uuid").toString()));
      }
      if ((jsonObj.get("ipv4") != null && !jsonObj.get("ipv4").isJsonNull()) && !jsonObj.get("ipv4").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `ipv4` to be a primitive type in the JSON string but got `%s`", jsonObj.get("ipv4").toString()));
      }
      if ((jsonObj.get("endpoint") != null && !jsonObj.get("endpoint").isJsonNull()) && !jsonObj.get("endpoint").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `endpoint` to be a primitive type in the JSON string but got `%s`", jsonObj.get("endpoint").toString()));
      }
      if (jsonObj.get("node_pools") != null && !jsonObj.get("node_pools").isJsonNull()) {
        JsonArray jsonArraynodePools = jsonObj.getAsJsonArray("node_pools");
        if (jsonArraynodePools != null) {
          // ensure the json data is an array
          if (!jsonObj.get("node_pools").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `node_pools` to be an array in the JSON string but got `%s`", jsonObj.get("node_pools").toString()));
          }

          // validate the optional field `node_pools` (array)
          for (int i = 0; i < jsonArraynodePools.size(); i++) {
            KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner.validateJsonObject(jsonArraynodePools.get(i).getAsJsonObject());
          };
        }
      }
      // validate the optional field `maintenance_policy`
      if (jsonObj.get("maintenance_policy") != null && !jsonObj.get("maintenance_policy").isJsonNull()) {
        KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy.validateJsonObject(jsonObj.getAsJsonObject("maintenance_policy"));
      }
      // validate the optional field `status`
      if (jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) {
        KubernetesUpdateClusterResponseKubernetesClusterStatus.validateJsonObject(jsonObj.getAsJsonObject("status"));
      }
      if ((jsonObj.get("created_at") != null && !jsonObj.get("created_at").isJsonNull()) && !jsonObj.get("created_at").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `created_at` to be a primitive type in the JSON string but got `%s`", jsonObj.get("created_at").toString()));
      }
      if ((jsonObj.get("updated_at") != null && !jsonObj.get("updated_at").isJsonNull()) && !jsonObj.get("updated_at").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `updated_at` to be a primitive type in the JSON string but got `%s`", jsonObj.get("updated_at").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!KubernetesUpdateClusterResponseKubernetesCluster.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'KubernetesUpdateClusterResponseKubernetesCluster' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<KubernetesUpdateClusterResponseKubernetesCluster> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(KubernetesUpdateClusterResponseKubernetesCluster.class));

       return (TypeAdapter<T>) new TypeAdapter<KubernetesUpdateClusterResponseKubernetesCluster>() {
           @Override
           public void write(JsonWriter out, KubernetesUpdateClusterResponseKubernetesCluster value) throws IOException {
             JsonObject obj = thisAdapter.toJsonTree(value).getAsJsonObject();
             obj.remove("additionalProperties");
             // serialize additonal properties
             if (value.getAdditionalProperties() != null) {
               for (Map.Entry<String, Object> entry : value.getAdditionalProperties().entrySet()) {
                 if (entry.getValue() instanceof String)
                   obj.addProperty(entry.getKey(), (String) entry.getValue());
                 else if (entry.getValue() instanceof Number)
                   obj.addProperty(entry.getKey(), (Number) entry.getValue());
                 else if (entry.getValue() instanceof Boolean)
                   obj.addProperty(entry.getKey(), (Boolean) entry.getValue());
                 else if (entry.getValue() instanceof Character)
                   obj.addProperty(entry.getKey(), (Character) entry.getValue());
                 else {
                   obj.add(entry.getKey(), gson.toJsonTree(entry.getValue()).getAsJsonObject());
                 }
               }
             }
             elementAdapter.write(out, obj);
           }

           @Override
           public KubernetesUpdateClusterResponseKubernetesCluster read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             KubernetesUpdateClusterResponseKubernetesCluster instance = thisAdapter.fromJsonTree(jsonObj);
             for (Map.Entry<String, JsonElement> entry : jsonObj.entrySet()) {
               if (!openapiFields.contains(entry.getKey())) {
                 if (entry.getValue().isJsonPrimitive()) { // primitive type
                   if (entry.getValue().getAsJsonPrimitive().isString())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsString());
                   else if (entry.getValue().getAsJsonPrimitive().isNumber())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsNumber());
                   else if (entry.getValue().getAsJsonPrimitive().isBoolean())
                     instance.putAdditionalProperty(entry.getKey(), entry.getValue().getAsBoolean());
                   else
                     throw new IllegalArgumentException(String.format("The field `%s` has unknown primitive type. Value: %s", entry.getKey(), entry.getValue().toString()));
                 } else if (entry.getValue().isJsonArray()) {
                     instance.putAdditionalProperty(entry.getKey(), gson.fromJson(entry.getValue(), List.class));
                 } else { // JSON object
                     instance.putAdditionalProperty(entry.getKey(), gson.fromJson(entry.getValue(), HashMap.class));
                 }
               }
             }
             return instance;
           }

       }.nullSafe();
    }
  }

 /**
  * Create an instance of KubernetesUpdateClusterResponseKubernetesCluster given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of KubernetesUpdateClusterResponseKubernetesCluster
  * @throws IOException if the JSON string is invalid with respect to KubernetesUpdateClusterResponseKubernetesCluster
  */
  public static KubernetesUpdateClusterResponseKubernetesCluster fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, KubernetesUpdateClusterResponseKubernetesCluster.class);
  }

 /**
  * Convert an instance of KubernetesUpdateClusterResponseKubernetesCluster to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

