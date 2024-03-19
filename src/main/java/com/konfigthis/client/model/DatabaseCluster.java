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
import com.konfigthis.client.model.DatabaseClusterConnection;
import com.konfigthis.client.model.DatabaseClusterMaintenanceWindow;
import com.konfigthis.client.model.DatabaseServiceEndpoint;
import com.konfigthis.client.model.DatabaseUser;
import com.konfigthis.client.model.FirewallRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.openapitools.jackson.nullable.JsonNullable;

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
 * DatabaseCluster
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class DatabaseCluster {
  public static final String SERIALIZED_NAME_TAGS = "tags";
  @SerializedName(SERIALIZED_NAME_TAGS)
  private List<String> tags = null;

  public static final String SERIALIZED_NAME_VERSION = "version";
  @SerializedName(SERIALIZED_NAME_VERSION)
  private String version;

  public static final String SERIALIZED_NAME_ID = "id";
  @SerializedName(SERIALIZED_NAME_ID)
  private UUID id;

  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  /**
   * A slug representing the database engine used for the cluster. The possible values are: \&quot;pg\&quot; for PostgreSQL, \&quot;mysql\&quot; for MySQL, \&quot;redis\&quot; for Redis, \&quot;mongodb\&quot; for MongoDB, and \&quot;kafka\&quot; for Kafka.
   */
  @JsonAdapter(EngineEnum.Adapter.class)
 public enum EngineEnum {
    PG("pg"),
    
    MYSQL("mysql"),
    
    REDIS("redis"),
    
    MONGODB("mongodb"),
    
    KAFKA("kafka");

    private String value;

    EngineEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static EngineEnum fromValue(String value) {
      for (EngineEnum b : EngineEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<EngineEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final EngineEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public EngineEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return EngineEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_ENGINE = "engine";
  @SerializedName(SERIALIZED_NAME_ENGINE)
  private EngineEnum engine;

  public static final String SERIALIZED_NAME_SEMANTIC_VERSION = "semantic_version";
  @SerializedName(SERIALIZED_NAME_SEMANTIC_VERSION)
  private String semanticVersion;

  public static final String SERIALIZED_NAME_NUM_NODES = "num_nodes";
  @SerializedName(SERIALIZED_NAME_NUM_NODES)
  private Integer numNodes;

  public static final String SERIALIZED_NAME_SIZE = "size";
  @SerializedName(SERIALIZED_NAME_SIZE)
  private String size;

  public static final String SERIALIZED_NAME_REGION = "region";
  @SerializedName(SERIALIZED_NAME_REGION)
  private String region;

  /**
   * A string representing the current status of the database cluster.
   */
  @JsonAdapter(StatusEnum.Adapter.class)
 public enum StatusEnum {
    CREATING("creating"),
    
    ONLINE("online"),
    
    RESIZING("resizing"),
    
    MIGRATING("migrating"),
    
    FORKING("forking");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<StatusEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final StatusEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public StatusEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return StatusEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_STATUS = "status";
  @SerializedName(SERIALIZED_NAME_STATUS)
  private StatusEnum status;

  public static final String SERIALIZED_NAME_CREATED_AT = "created_at";
  @SerializedName(SERIALIZED_NAME_CREATED_AT)
  private OffsetDateTime createdAt;

  public static final String SERIALIZED_NAME_PRIVATE_NETWORK_UUID = "private_network_uuid";
  @SerializedName(SERIALIZED_NAME_PRIVATE_NETWORK_UUID)
  private String privateNetworkUuid;

  public static final String SERIALIZED_NAME_DB_NAMES = "db_names";
  @SerializedName(SERIALIZED_NAME_DB_NAMES)
  private List<String> dbNames = null;

  public static final String SERIALIZED_NAME_CONNECTION = "connection";
  @SerializedName(SERIALIZED_NAME_CONNECTION)
  private DatabaseClusterConnection connection;

  public static final String SERIALIZED_NAME_PRIVATE_CONNECTION = "private_connection";
  @SerializedName(SERIALIZED_NAME_PRIVATE_CONNECTION)
  private DatabaseClusterConnection privateConnection;

  public static final String SERIALIZED_NAME_STANDBY_CONNECTION = "standby_connection";
  @SerializedName(SERIALIZED_NAME_STANDBY_CONNECTION)
  private DatabaseClusterConnection standbyConnection;

  public static final String SERIALIZED_NAME_STANDBY_PRIVATE_CONNECTION = "standby_private_connection";
  @SerializedName(SERIALIZED_NAME_STANDBY_PRIVATE_CONNECTION)
  private DatabaseClusterConnection standbyPrivateConnection;

  public static final String SERIALIZED_NAME_USERS = "users";
  @SerializedName(SERIALIZED_NAME_USERS)
  private List<DatabaseUser> users = null;

  public static final String SERIALIZED_NAME_MAINTENANCE_WINDOW = "maintenance_window";
  @SerializedName(SERIALIZED_NAME_MAINTENANCE_WINDOW)
  private DatabaseClusterMaintenanceWindow maintenanceWindow;

  public static final String SERIALIZED_NAME_PROJECT_ID = "project_id";
  @SerializedName(SERIALIZED_NAME_PROJECT_ID)
  private UUID projectId;

  public static final String SERIALIZED_NAME_RULES = "rules";
  @SerializedName(SERIALIZED_NAME_RULES)
  private List<FirewallRule> rules = null;

  public static final String SERIALIZED_NAME_VERSION_END_OF_LIFE = "version_end_of_life";
  @SerializedName(SERIALIZED_NAME_VERSION_END_OF_LIFE)
  private String versionEndOfLife;

  public static final String SERIALIZED_NAME_VERSION_END_OF_AVAILABILITY = "version_end_of_availability";
  @SerializedName(SERIALIZED_NAME_VERSION_END_OF_AVAILABILITY)
  private String versionEndOfAvailability;

  public static final String SERIALIZED_NAME_STORAGE_SIZE_MIB = "storage_size_mib";
  @SerializedName(SERIALIZED_NAME_STORAGE_SIZE_MIB)
  private Integer storageSizeMib;

  public static final String SERIALIZED_NAME_METRICS_ENDPOINTS = "metrics_endpoints";
  @SerializedName(SERIALIZED_NAME_METRICS_ENDPOINTS)
  private List<DatabaseServiceEndpoint> metricsEndpoints = null;

  public DatabaseCluster() {
  }

  
  public DatabaseCluster(
     UUID id, 
     String semanticVersion, 
     StatusEnum status, 
     OffsetDateTime createdAt, 
     List<String> dbNames, 
     List<DatabaseUser> users, 
     String versionEndOfLife, 
     String versionEndOfAvailability
  ) {
    this();
    this.id = id;
    this.semanticVersion = semanticVersion;
    this.status = status;
    this.createdAt = createdAt;
    this.dbNames = dbNames;
    this.users = users;
    this.versionEndOfLife = versionEndOfLife;
    this.versionEndOfAvailability = versionEndOfAvailability;
  }

  public DatabaseCluster tags(List<String> tags) {
    
    
    
    
    this.tags = tags;
    return this;
  }

  public DatabaseCluster addTagsItem(String tagsItem) {
    if (this.tags == null) {
      this.tags = new ArrayList<>();
    }
    this.tags.add(tagsItem);
    return this;
  }

   /**
   * An array of tags that have been applied to the database cluster.
   * @return tags
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "[\"production\"]", value = "An array of tags that have been applied to the database cluster.")

  public List<String> getTags() {
    return tags;
  }


  public void setTags(List<String> tags) {
    
    
    
    this.tags = tags;
  }


  public DatabaseCluster version(String version) {
    
    
    
    
    this.version = version;
    return this;
  }

   /**
   * A string representing the version of the database engine in use for the cluster.
   * @return version
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "8", value = "A string representing the version of the database engine in use for the cluster.")

  public String getVersion() {
    return version;
  }


  public void setVersion(String version) {
    
    
    
    this.version = version;
  }


   /**
   * A unique ID that can be used to identify and reference a database cluster.
   * @return id
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "9cc10173-e9ea-4176-9dbc-a4cee4c4ff30", value = "A unique ID that can be used to identify and reference a database cluster.")

  public UUID getId() {
    return id;
  }




  public DatabaseCluster name(String name) {
    
    
    
    
    this.name = name;
    return this;
  }

   /**
   * A unique, human-readable name referring to a database cluster.
   * @return name
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "backend", required = true, value = "A unique, human-readable name referring to a database cluster.")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    
    
    
    this.name = name;
  }


  public DatabaseCluster engine(EngineEnum engine) {
    
    
    
    
    this.engine = engine;
    return this;
  }

   /**
   * A slug representing the database engine used for the cluster. The possible values are: \&quot;pg\&quot; for PostgreSQL, \&quot;mysql\&quot; for MySQL, \&quot;redis\&quot; for Redis, \&quot;mongodb\&quot; for MongoDB, and \&quot;kafka\&quot; for Kafka.
   * @return engine
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "MYSQL", required = true, value = "A slug representing the database engine used for the cluster. The possible values are: \"pg\" for PostgreSQL, \"mysql\" for MySQL, \"redis\" for Redis, \"mongodb\" for MongoDB, and \"kafka\" for Kafka.")

  public EngineEnum getEngine() {
    return engine;
  }


  public void setEngine(EngineEnum engine) {
    
    
    
    this.engine = engine;
  }


   /**
   * A string representing the semantic version of the database engine in use for the cluster.
   * @return semanticVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "8.0.28", value = "A string representing the semantic version of the database engine in use for the cluster.")

  public String getSemanticVersion() {
    return semanticVersion;
  }




  public DatabaseCluster numNodes(Integer numNodes) {
    
    
    
    
    this.numNodes = numNodes;
    return this;
  }

   /**
   * The number of nodes in the database cluster.
   * @return numNodes
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "2", required = true, value = "The number of nodes in the database cluster.")

  public Integer getNumNodes() {
    return numNodes;
  }


  public void setNumNodes(Integer numNodes) {
    
    
    
    this.numNodes = numNodes;
  }


  public DatabaseCluster size(String size) {
    
    
    
    
    this.size = size;
    return this;
  }

   /**
   * The slug identifier representing the size of the nodes in the database cluster.
   * @return size
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "db-s-2vcpu-4gb", required = true, value = "The slug identifier representing the size of the nodes in the database cluster.")

  public String getSize() {
    return size;
  }


  public void setSize(String size) {
    
    
    
    this.size = size;
  }


  public DatabaseCluster region(String region) {
    
    
    
    
    this.region = region;
    return this;
  }

   /**
   * The slug identifier for the region where the database cluster is located.
   * @return region
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "nyc3", required = true, value = "The slug identifier for the region where the database cluster is located.")

  public String getRegion() {
    return region;
  }


  public void setRegion(String region) {
    
    
    
    this.region = region;
  }


   /**
   * A string representing the current status of the database cluster.
   * @return status
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "CREATING", value = "A string representing the current status of the database cluster.")

  public StatusEnum getStatus() {
    return status;
  }




   /**
   * A time value given in ISO8601 combined date and time format that represents when the database cluster was created.
   * @return createdAt
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2019-01-11T18:37:36Z", value = "A time value given in ISO8601 combined date and time format that represents when the database cluster was created.")

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }




  public DatabaseCluster privateNetworkUuid(String privateNetworkUuid) {
    
    
    
    
    this.privateNetworkUuid = privateNetworkUuid;
    return this;
  }

   /**
   * A string specifying the UUID of the VPC to which the database cluster will be assigned. If excluded, the cluster when creating a new database cluster, it will be assigned to your account&#39;s default VPC for the region.
   * @return privateNetworkUuid
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "d455e75d-4858-4eec-8c95-da2f0a5f93a7", value = "A string specifying the UUID of the VPC to which the database cluster will be assigned. If excluded, the cluster when creating a new database cluster, it will be assigned to your account's default VPC for the region.")

  public String getPrivateNetworkUuid() {
    return privateNetworkUuid;
  }


  public void setPrivateNetworkUuid(String privateNetworkUuid) {
    
    
    
    this.privateNetworkUuid = privateNetworkUuid;
  }


   /**
   * An array of strings containing the names of databases created in the database cluster.
   * @return dbNames
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "[\"doadmin\"]", value = "An array of strings containing the names of databases created in the database cluster.")

  public List<String> getDbNames() {
    return dbNames;
  }




  public DatabaseCluster connection(DatabaseClusterConnection connection) {
    
    
    
    
    this.connection = connection;
    return this;
  }

   /**
   * Get connection
   * @return connection
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public DatabaseClusterConnection getConnection() {
    return connection;
  }


  public void setConnection(DatabaseClusterConnection connection) {
    
    
    
    this.connection = connection;
  }


  public DatabaseCluster privateConnection(DatabaseClusterConnection privateConnection) {
    
    
    
    
    this.privateConnection = privateConnection;
    return this;
  }

   /**
   * Get privateConnection
   * @return privateConnection
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public DatabaseClusterConnection getPrivateConnection() {
    return privateConnection;
  }


  public void setPrivateConnection(DatabaseClusterConnection privateConnection) {
    
    
    
    this.privateConnection = privateConnection;
  }


  public DatabaseCluster standbyConnection(DatabaseClusterConnection standbyConnection) {
    
    
    
    
    this.standbyConnection = standbyConnection;
    return this;
  }

   /**
   * Get standbyConnection
   * @return standbyConnection
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public DatabaseClusterConnection getStandbyConnection() {
    return standbyConnection;
  }


  public void setStandbyConnection(DatabaseClusterConnection standbyConnection) {
    
    
    
    this.standbyConnection = standbyConnection;
  }


  public DatabaseCluster standbyPrivateConnection(DatabaseClusterConnection standbyPrivateConnection) {
    
    
    
    
    this.standbyPrivateConnection = standbyPrivateConnection;
    return this;
  }

   /**
   * Get standbyPrivateConnection
   * @return standbyPrivateConnection
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public DatabaseClusterConnection getStandbyPrivateConnection() {
    return standbyPrivateConnection;
  }


  public void setStandbyPrivateConnection(DatabaseClusterConnection standbyPrivateConnection) {
    
    
    
    this.standbyPrivateConnection = standbyPrivateConnection;
  }


   /**
   * Get users
   * @return users
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<DatabaseUser> getUsers() {
    return users;
  }




  public DatabaseCluster maintenanceWindow(DatabaseClusterMaintenanceWindow maintenanceWindow) {
    
    
    
    
    this.maintenanceWindow = maintenanceWindow;
    return this;
  }

   /**
   * Get maintenanceWindow
   * @return maintenanceWindow
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public DatabaseClusterMaintenanceWindow getMaintenanceWindow() {
    return maintenanceWindow;
  }


  public void setMaintenanceWindow(DatabaseClusterMaintenanceWindow maintenanceWindow) {
    
    
    
    this.maintenanceWindow = maintenanceWindow;
  }


  public DatabaseCluster projectId(UUID projectId) {
    
    
    
    
    this.projectId = projectId;
    return this;
  }

   /**
   * The ID of the project that the database cluster is assigned to. If excluded when creating a new database cluster, it will be assigned to your default project.
   * @return projectId
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "9cc10173-e9ea-4176-9dbc-a4cee4c4ff30", value = "The ID of the project that the database cluster is assigned to. If excluded when creating a new database cluster, it will be assigned to your default project.")

  public UUID getProjectId() {
    return projectId;
  }


  public void setProjectId(UUID projectId) {
    
    
    
    this.projectId = projectId;
  }


  public DatabaseCluster rules(List<FirewallRule> rules) {
    
    
    
    
    this.rules = rules;
    return this;
  }

  public DatabaseCluster addRulesItem(FirewallRule rulesItem) {
    if (this.rules == null) {
      this.rules = new ArrayList<>();
    }
    this.rules.add(rulesItem);
    return this;
  }

   /**
   * Get rules
   * @return rules
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public List<FirewallRule> getRules() {
    return rules;
  }


  public void setRules(List<FirewallRule> rules) {
    
    
    
    this.rules = rules;
  }


   /**
   * A timestamp referring to the date when the particular version will no longer be supported. If null, the version does not have an end of life timeline.
   * @return versionEndOfLife
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2023-11-09T00:00:00Z", value = "A timestamp referring to the date when the particular version will no longer be supported. If null, the version does not have an end of life timeline.")

  public String getVersionEndOfLife() {
    return versionEndOfLife;
  }




   /**
   * A timestamp referring to the date when the particular version will no longer be available for creating new clusters. If null, the version does not have an end of availability timeline.
   * @return versionEndOfAvailability
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2023-05-09T00:00:00Z", value = "A timestamp referring to the date when the particular version will no longer be available for creating new clusters. If null, the version does not have an end of availability timeline.")

  public String getVersionEndOfAvailability() {
    return versionEndOfAvailability;
  }




  public DatabaseCluster storageSizeMib(Integer storageSizeMib) {
    
    
    
    
    this.storageSizeMib = storageSizeMib;
    return this;
  }

   /**
   * Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the &#39;size&#39; and any previously added additional storage.
   * @return storageSizeMib
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "61440", value = "Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the 'size' and any previously added additional storage.")

  public Integer getStorageSizeMib() {
    return storageSizeMib;
  }


  public void setStorageSizeMib(Integer storageSizeMib) {
    
    
    
    this.storageSizeMib = storageSizeMib;
  }


  public DatabaseCluster metricsEndpoints(List<DatabaseServiceEndpoint> metricsEndpoints) {
    
    
    
    
    this.metricsEndpoints = metricsEndpoints;
    return this;
  }

  public DatabaseCluster addMetricsEndpointsItem(DatabaseServiceEndpoint metricsEndpointsItem) {
    if (this.metricsEndpoints == null) {
      this.metricsEndpoints = new ArrayList<>();
    }
    this.metricsEndpoints.add(metricsEndpointsItem);
    return this;
  }

   /**
   * Public hostname and port of the cluster&#39;s metrics endpoint(s). Includes one record for the cluster&#39;s primary node and a second entry for the cluster&#39;s standby node(s).
   * @return metricsEndpoints
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "Public hostname and port of the cluster's metrics endpoint(s). Includes one record for the cluster's primary node and a second entry for the cluster's standby node(s).")

  public List<DatabaseServiceEndpoint> getMetricsEndpoints() {
    return metricsEndpoints;
  }


  public void setMetricsEndpoints(List<DatabaseServiceEndpoint> metricsEndpoints) {
    
    
    
    this.metricsEndpoints = metricsEndpoints;
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
   * @return the DatabaseCluster instance itself
   */
  public DatabaseCluster putAdditionalProperty(String key, Object value) {
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
    DatabaseCluster databaseCluster = (DatabaseCluster) o;
    return Objects.equals(this.tags, databaseCluster.tags) &&
        Objects.equals(this.version, databaseCluster.version) &&
        Objects.equals(this.id, databaseCluster.id) &&
        Objects.equals(this.name, databaseCluster.name) &&
        Objects.equals(this.engine, databaseCluster.engine) &&
        Objects.equals(this.semanticVersion, databaseCluster.semanticVersion) &&
        Objects.equals(this.numNodes, databaseCluster.numNodes) &&
        Objects.equals(this.size, databaseCluster.size) &&
        Objects.equals(this.region, databaseCluster.region) &&
        Objects.equals(this.status, databaseCluster.status) &&
        Objects.equals(this.createdAt, databaseCluster.createdAt) &&
        Objects.equals(this.privateNetworkUuid, databaseCluster.privateNetworkUuid) &&
        Objects.equals(this.dbNames, databaseCluster.dbNames) &&
        Objects.equals(this.connection, databaseCluster.connection) &&
        Objects.equals(this.privateConnection, databaseCluster.privateConnection) &&
        Objects.equals(this.standbyConnection, databaseCluster.standbyConnection) &&
        Objects.equals(this.standbyPrivateConnection, databaseCluster.standbyPrivateConnection) &&
        Objects.equals(this.users, databaseCluster.users) &&
        Objects.equals(this.maintenanceWindow, databaseCluster.maintenanceWindow) &&
        Objects.equals(this.projectId, databaseCluster.projectId) &&
        Objects.equals(this.rules, databaseCluster.rules) &&
        Objects.equals(this.versionEndOfLife, databaseCluster.versionEndOfLife) &&
        Objects.equals(this.versionEndOfAvailability, databaseCluster.versionEndOfAvailability) &&
        Objects.equals(this.storageSizeMib, databaseCluster.storageSizeMib) &&
        Objects.equals(this.metricsEndpoints, databaseCluster.metricsEndpoints)&&
        Objects.equals(this.additionalProperties, databaseCluster.additionalProperties);
  }

  private static <T> boolean equalsNullable(JsonNullable<T> a, JsonNullable<T> b) {
    return a == b || (a != null && b != null && a.isPresent() && b.isPresent() && Objects.deepEquals(a.get(), b.get()));
  }

  @Override
  public int hashCode() {
    return Objects.hash(tags, version, id, name, engine, semanticVersion, numNodes, size, region, status, createdAt, privateNetworkUuid, dbNames, connection, privateConnection, standbyConnection, standbyPrivateConnection, users, maintenanceWindow, projectId, rules, versionEndOfLife, versionEndOfAvailability, storageSizeMib, metricsEndpoints, additionalProperties);
  }

  private static <T> int hashCodeNullable(JsonNullable<T> a) {
    if (a == null) {
      return 1;
    }
    return a.isPresent() ? Arrays.deepHashCode(new Object[]{a.get()}) : 31;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatabaseCluster {\n");
    sb.append("    tags: ").append(toIndentedString(tags)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    engine: ").append(toIndentedString(engine)).append("\n");
    sb.append("    semanticVersion: ").append(toIndentedString(semanticVersion)).append("\n");
    sb.append("    numNodes: ").append(toIndentedString(numNodes)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
    sb.append("    region: ").append(toIndentedString(region)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("    privateNetworkUuid: ").append(toIndentedString(privateNetworkUuid)).append("\n");
    sb.append("    dbNames: ").append(toIndentedString(dbNames)).append("\n");
    sb.append("    connection: ").append(toIndentedString(connection)).append("\n");
    sb.append("    privateConnection: ").append(toIndentedString(privateConnection)).append("\n");
    sb.append("    standbyConnection: ").append(toIndentedString(standbyConnection)).append("\n");
    sb.append("    standbyPrivateConnection: ").append(toIndentedString(standbyPrivateConnection)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("    maintenanceWindow: ").append(toIndentedString(maintenanceWindow)).append("\n");
    sb.append("    projectId: ").append(toIndentedString(projectId)).append("\n");
    sb.append("    rules: ").append(toIndentedString(rules)).append("\n");
    sb.append("    versionEndOfLife: ").append(toIndentedString(versionEndOfLife)).append("\n");
    sb.append("    versionEndOfAvailability: ").append(toIndentedString(versionEndOfAvailability)).append("\n");
    sb.append("    storageSizeMib: ").append(toIndentedString(storageSizeMib)).append("\n");
    sb.append("    metricsEndpoints: ").append(toIndentedString(metricsEndpoints)).append("\n");
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
    openapiFields.add("engine");
    openapiFields.add("semantic_version");
    openapiFields.add("num_nodes");
    openapiFields.add("size");
    openapiFields.add("region");
    openapiFields.add("status");
    openapiFields.add("created_at");
    openapiFields.add("private_network_uuid");
    openapiFields.add("db_names");
    openapiFields.add("connection");
    openapiFields.add("private_connection");
    openapiFields.add("standby_connection");
    openapiFields.add("standby_private_connection");
    openapiFields.add("users");
    openapiFields.add("maintenance_window");
    openapiFields.add("project_id");
    openapiFields.add("rules");
    openapiFields.add("version_end_of_life");
    openapiFields.add("version_end_of_availability");
    openapiFields.add("storage_size_mib");
    openapiFields.add("metrics_endpoints");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
    openapiRequiredFields.add("name");
    openapiRequiredFields.add("engine");
    openapiRequiredFields.add("num_nodes");
    openapiRequiredFields.add("size");
    openapiRequiredFields.add("region");
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to DatabaseCluster
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!DatabaseCluster.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in DatabaseCluster is not found in the empty JSON string", DatabaseCluster.openapiRequiredFields.toString()));
        }
      }

      // check to make sure all required properties/fields are present in the JSON string
      for (String requiredField : DatabaseCluster.openapiRequiredFields) {
        if (jsonObj.get(requiredField) == null) {
          throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonObj.toString()));
        }
      }
      // ensure the optional json data is an array if present (nullable)
      if (jsonObj.get("tags") != null && !jsonObj.get("tags").isJsonNull() && !jsonObj.get("tags").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `tags` to be an array in the JSON string or null but got `%s`", jsonObj.get("tags").toString()));
      }
      if ((jsonObj.get("version") != null && !jsonObj.get("version").isJsonNull()) && !jsonObj.get("version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("version").toString()));
      }
      if ((jsonObj.get("id") != null && !jsonObj.get("id").isJsonNull()) && !jsonObj.get("id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("id").toString()));
      }
      if (!jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      if (!jsonObj.get("engine").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `engine` to be a primitive type in the JSON string but got `%s`", jsonObj.get("engine").toString()));
      }
      if ((jsonObj.get("semantic_version") != null && !jsonObj.get("semantic_version").isJsonNull()) && !jsonObj.get("semantic_version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `semantic_version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("semantic_version").toString()));
      }
      if (!jsonObj.get("size").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `size` to be a primitive type in the JSON string but got `%s`", jsonObj.get("size").toString()));
      }
      if (!jsonObj.get("region").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `region` to be a primitive type in the JSON string but got `%s`", jsonObj.get("region").toString()));
      }
      if ((jsonObj.get("status") != null && !jsonObj.get("status").isJsonNull()) && !jsonObj.get("status").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `status` to be a primitive type in the JSON string but got `%s`", jsonObj.get("status").toString()));
      }
      if ((jsonObj.get("private_network_uuid") != null && !jsonObj.get("private_network_uuid").isJsonNull()) && !jsonObj.get("private_network_uuid").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `private_network_uuid` to be a primitive type in the JSON string but got `%s`", jsonObj.get("private_network_uuid").toString()));
      }
      // ensure the optional json data is an array if present (nullable)
      if (jsonObj.get("db_names") != null && !jsonObj.get("db_names").isJsonNull() && !jsonObj.get("db_names").isJsonArray()) {
        throw new IllegalArgumentException(String.format("Expected the field `db_names` to be an array in the JSON string or null but got `%s`", jsonObj.get("db_names").toString()));
      }
      // validate the optional field `connection`
      if (jsonObj.get("connection") != null && !jsonObj.get("connection").isJsonNull()) {
        DatabaseClusterConnection.validateJsonObject(jsonObj.getAsJsonObject("connection"));
      }
      // validate the optional field `private_connection`
      if (jsonObj.get("private_connection") != null && !jsonObj.get("private_connection").isJsonNull()) {
        DatabaseClusterConnection.validateJsonObject(jsonObj.getAsJsonObject("private_connection"));
      }
      // validate the optional field `standby_connection`
      if (jsonObj.get("standby_connection") != null && !jsonObj.get("standby_connection").isJsonNull()) {
        DatabaseClusterConnection.validateJsonObject(jsonObj.getAsJsonObject("standby_connection"));
      }
      // validate the optional field `standby_private_connection`
      if (jsonObj.get("standby_private_connection") != null && !jsonObj.get("standby_private_connection").isJsonNull()) {
        DatabaseClusterConnection.validateJsonObject(jsonObj.getAsJsonObject("standby_private_connection"));
      }
      if (jsonObj.get("users") != null && !jsonObj.get("users").isJsonNull()) {
        JsonArray jsonArrayusers = jsonObj.getAsJsonArray("users");
        if (jsonArrayusers != null) {
          // ensure the json data is an array
          if (!jsonObj.get("users").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `users` to be an array in the JSON string but got `%s`", jsonObj.get("users").toString()));
          }

          // validate the optional field `users` (array)
          for (int i = 0; i < jsonArrayusers.size(); i++) {
            DatabaseUser.validateJsonObject(jsonArrayusers.get(i).getAsJsonObject());
          };
        }
      }
      // validate the optional field `maintenance_window`
      if (jsonObj.get("maintenance_window") != null && !jsonObj.get("maintenance_window").isJsonNull()) {
        DatabaseClusterMaintenanceWindow.validateJsonObject(jsonObj.getAsJsonObject("maintenance_window"));
      }
      if ((jsonObj.get("project_id") != null && !jsonObj.get("project_id").isJsonNull()) && !jsonObj.get("project_id").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `project_id` to be a primitive type in the JSON string but got `%s`", jsonObj.get("project_id").toString()));
      }
      if (jsonObj.get("rules") != null && !jsonObj.get("rules").isJsonNull()) {
        JsonArray jsonArrayrules = jsonObj.getAsJsonArray("rules");
        if (jsonArrayrules != null) {
          // ensure the json data is an array
          if (!jsonObj.get("rules").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `rules` to be an array in the JSON string but got `%s`", jsonObj.get("rules").toString()));
          }

          // validate the optional field `rules` (array)
          for (int i = 0; i < jsonArrayrules.size(); i++) {
            FirewallRule.validateJsonObject(jsonArrayrules.get(i).getAsJsonObject());
          };
        }
      }
      if ((jsonObj.get("version_end_of_life") != null && !jsonObj.get("version_end_of_life").isJsonNull()) && !jsonObj.get("version_end_of_life").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `version_end_of_life` to be a primitive type in the JSON string but got `%s`", jsonObj.get("version_end_of_life").toString()));
      }
      if ((jsonObj.get("version_end_of_availability") != null && !jsonObj.get("version_end_of_availability").isJsonNull()) && !jsonObj.get("version_end_of_availability").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `version_end_of_availability` to be a primitive type in the JSON string but got `%s`", jsonObj.get("version_end_of_availability").toString()));
      }
      if (jsonObj.get("metrics_endpoints") != null && !jsonObj.get("metrics_endpoints").isJsonNull()) {
        JsonArray jsonArraymetricsEndpoints = jsonObj.getAsJsonArray("metrics_endpoints");
        if (jsonArraymetricsEndpoints != null) {
          // ensure the json data is an array
          if (!jsonObj.get("metrics_endpoints").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `metrics_endpoints` to be an array in the JSON string but got `%s`", jsonObj.get("metrics_endpoints").toString()));
          }

          // validate the optional field `metrics_endpoints` (array)
          for (int i = 0; i < jsonArraymetricsEndpoints.size(); i++) {
            DatabaseServiceEndpoint.validateJsonObject(jsonArraymetricsEndpoints.get(i).getAsJsonObject());
          };
        }
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!DatabaseCluster.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'DatabaseCluster' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<DatabaseCluster> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(DatabaseCluster.class));

       return (TypeAdapter<T>) new TypeAdapter<DatabaseCluster>() {
           @Override
           public void write(JsonWriter out, DatabaseCluster value) throws IOException {
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
           public DatabaseCluster read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             DatabaseCluster instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of DatabaseCluster given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of DatabaseCluster
  * @throws IOException if the JSON string is invalid with respect to DatabaseCluster
  */
  public static DatabaseCluster fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, DatabaseCluster.class);
  }

 /**
  * Convert an instance of DatabaseCluster to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

