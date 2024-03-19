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
import com.konfigthis.client.model.MysqlPgbouncer;
import com.konfigthis.client.model.MysqlTimescaledb;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;

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
 * Mpr1
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class Mpr1 {
  public static final String SERIALIZED_NAME_BACKUP_HOUR = "backup_hour";
  @SerializedName(SERIALIZED_NAME_BACKUP_HOUR)
  private Integer backupHour;

  public static final String SERIALIZED_NAME_BACKUP_MINUTE = "backup_minute";
  @SerializedName(SERIALIZED_NAME_BACKUP_MINUTE)
  private Integer backupMinute;

  public static final String SERIALIZED_NAME_SQL_MODE = "sql_mode";
  @SerializedName(SERIALIZED_NAME_SQL_MODE)
  private String sqlMode;

  public static final String SERIALIZED_NAME_CONNECT_TIMEOUT = "connect_timeout";
  @SerializedName(SERIALIZED_NAME_CONNECT_TIMEOUT)
  private Integer connectTimeout;

  public static final String SERIALIZED_NAME_DEFAULT_TIME_ZONE = "default_time_zone";
  @SerializedName(SERIALIZED_NAME_DEFAULT_TIME_ZONE)
  private String defaultTimeZone;

  public static final String SERIALIZED_NAME_GROUP_CONCAT_MAX_LEN = "group_concat_max_len";
  @SerializedName(SERIALIZED_NAME_GROUP_CONCAT_MAX_LEN)
  private Integer groupConcatMaxLen;

  public static final String SERIALIZED_NAME_INFORMATION_SCHEMA_STATS_EXPIRY = "information_schema_stats_expiry";
  @SerializedName(SERIALIZED_NAME_INFORMATION_SCHEMA_STATS_EXPIRY)
  private Integer informationSchemaStatsExpiry;

  public static final String SERIALIZED_NAME_INNODB_FT_MIN_TOKEN_SIZE = "innodb_ft_min_token_size";
  @SerializedName(SERIALIZED_NAME_INNODB_FT_MIN_TOKEN_SIZE)
  private Integer innodbFtMinTokenSize;

  public static final String SERIALIZED_NAME_INNODB_FT_SERVER_STOPWORD_TABLE = "innodb_ft_server_stopword_table";
  @SerializedName(SERIALIZED_NAME_INNODB_FT_SERVER_STOPWORD_TABLE)
  private String innodbFtServerStopwordTable;

  public static final String SERIALIZED_NAME_INNODB_LOCK_WAIT_TIMEOUT = "innodb_lock_wait_timeout";
  @SerializedName(SERIALIZED_NAME_INNODB_LOCK_WAIT_TIMEOUT)
  private Integer innodbLockWaitTimeout;

  public static final String SERIALIZED_NAME_INNODB_LOG_BUFFER_SIZE = "innodb_log_buffer_size";
  @SerializedName(SERIALIZED_NAME_INNODB_LOG_BUFFER_SIZE)
  private Integer innodbLogBufferSize;

  public static final String SERIALIZED_NAME_INNODB_ONLINE_ALTER_LOG_MAX_SIZE = "innodb_online_alter_log_max_size";
  @SerializedName(SERIALIZED_NAME_INNODB_ONLINE_ALTER_LOG_MAX_SIZE)
  private Integer innodbOnlineAlterLogMaxSize;

  public static final String SERIALIZED_NAME_INNODB_PRINT_ALL_DEADLOCKS = "innodb_print_all_deadlocks";
  @SerializedName(SERIALIZED_NAME_INNODB_PRINT_ALL_DEADLOCKS)
  private Boolean innodbPrintAllDeadlocks;

  public static final String SERIALIZED_NAME_INNODB_ROLLBACK_ON_TIMEOUT = "innodb_rollback_on_timeout";
  @SerializedName(SERIALIZED_NAME_INNODB_ROLLBACK_ON_TIMEOUT)
  private Boolean innodbRollbackOnTimeout;

  public static final String SERIALIZED_NAME_INTERACTIVE_TIMEOUT = "interactive_timeout";
  @SerializedName(SERIALIZED_NAME_INTERACTIVE_TIMEOUT)
  private Integer interactiveTimeout;

  /**
   * The storage engine for in-memory internal temporary tables.
   */
  @JsonAdapter(InternalTmpMemStorageEngineEnum.Adapter.class)
 public enum InternalTmpMemStorageEngineEnum {
    TEMPTABLE("TempTable"),
    
    MEMORY("MEMORY");

    private String value;

    InternalTmpMemStorageEngineEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static InternalTmpMemStorageEngineEnum fromValue(String value) {
      for (InternalTmpMemStorageEngineEnum b : InternalTmpMemStorageEngineEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<InternalTmpMemStorageEngineEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final InternalTmpMemStorageEngineEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public InternalTmpMemStorageEngineEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return InternalTmpMemStorageEngineEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_INTERNAL_TMP_MEM_STORAGE_ENGINE = "internal_tmp_mem_storage_engine";
  @SerializedName(SERIALIZED_NAME_INTERNAL_TMP_MEM_STORAGE_ENGINE)
  private InternalTmpMemStorageEngineEnum internalTmpMemStorageEngine;

  public static final String SERIALIZED_NAME_NET_READ_TIMEOUT = "net_read_timeout";
  @SerializedName(SERIALIZED_NAME_NET_READ_TIMEOUT)
  private Integer netReadTimeout;

  public static final String SERIALIZED_NAME_NET_WRITE_TIMEOUT = "net_write_timeout";
  @SerializedName(SERIALIZED_NAME_NET_WRITE_TIMEOUT)
  private Integer netWriteTimeout;

  public static final String SERIALIZED_NAME_SQL_REQUIRE_PRIMARY_KEY = "sql_require_primary_key";
  @SerializedName(SERIALIZED_NAME_SQL_REQUIRE_PRIMARY_KEY)
  private Boolean sqlRequirePrimaryKey;

  public static final String SERIALIZED_NAME_WAIT_TIMEOUT = "wait_timeout";
  @SerializedName(SERIALIZED_NAME_WAIT_TIMEOUT)
  private Integer waitTimeout;

  public static final String SERIALIZED_NAME_MAX_ALLOWED_PACKET = "max_allowed_packet";
  @SerializedName(SERIALIZED_NAME_MAX_ALLOWED_PACKET)
  private Integer maxAllowedPacket;

  public static final String SERIALIZED_NAME_MAX_HEAP_TABLE_SIZE = "max_heap_table_size";
  @SerializedName(SERIALIZED_NAME_MAX_HEAP_TABLE_SIZE)
  private Integer maxHeapTableSize;

  public static final String SERIALIZED_NAME_SORT_BUFFER_SIZE = "sort_buffer_size";
  @SerializedName(SERIALIZED_NAME_SORT_BUFFER_SIZE)
  private Integer sortBufferSize;

  public static final String SERIALIZED_NAME_TMP_TABLE_SIZE = "tmp_table_size";
  @SerializedName(SERIALIZED_NAME_TMP_TABLE_SIZE)
  private Integer tmpTableSize;

  public static final String SERIALIZED_NAME_SLOW_QUERY_LOG = "slow_query_log";
  @SerializedName(SERIALIZED_NAME_SLOW_QUERY_LOG)
  private Boolean slowQueryLog;

  public static final String SERIALIZED_NAME_LONG_QUERY_TIME = "long_query_time";
  @SerializedName(SERIALIZED_NAME_LONG_QUERY_TIME)
  private Double longQueryTime;

  public static final String SERIALIZED_NAME_BINLOG_RETENTION_PERIOD = "binlog_retention_period";
  @SerializedName(SERIALIZED_NAME_BINLOG_RETENTION_PERIOD)
  private Double binlogRetentionPeriod;

  public static final String SERIALIZED_NAME_INNODB_CHANGE_BUFFER_MAX_SIZE = "innodb_change_buffer_max_size";
  @SerializedName(SERIALIZED_NAME_INNODB_CHANGE_BUFFER_MAX_SIZE)
  private Integer innodbChangeBufferMaxSize;

  /**
   * Specifies whether flushing a page from the InnoDB buffer pool also flushes other dirty pages in the same extent.   - 0 &amp;mdash; disables this functionality, dirty pages in the same extent are not flushed.   - 1 &amp;mdash; flushes contiguous dirty pages in the same extent.   - 2 &amp;mdash; flushes dirty pages in the same extent.
   */
  @JsonAdapter(InnodbFlushNeighborsEnum.Adapter.class)
 public enum InnodbFlushNeighborsEnum {
    NUMBER_0(0),
    
    NUMBER_1(1),
    
    NUMBER_2(2);

    private Integer value;

    InnodbFlushNeighborsEnum(Integer value) {
      this.value = value;
    }

    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static InnodbFlushNeighborsEnum fromValue(Integer value) {
      for (InnodbFlushNeighborsEnum b : InnodbFlushNeighborsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<InnodbFlushNeighborsEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final InnodbFlushNeighborsEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public InnodbFlushNeighborsEnum read(final JsonReader jsonReader) throws IOException {
        Integer value =  jsonReader.nextInt();
        return InnodbFlushNeighborsEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_INNODB_FLUSH_NEIGHBORS = "innodb_flush_neighbors";
  @SerializedName(SERIALIZED_NAME_INNODB_FLUSH_NEIGHBORS)
  private InnodbFlushNeighborsEnum innodbFlushNeighbors;

  public static final String SERIALIZED_NAME_INNODB_READ_IO_THREADS = "innodb_read_io_threads";
  @SerializedName(SERIALIZED_NAME_INNODB_READ_IO_THREADS)
  private Integer innodbReadIoThreads;

  public static final String SERIALIZED_NAME_INNODB_WRITE_IO_THREADS = "innodb_write_io_threads";
  @SerializedName(SERIALIZED_NAME_INNODB_WRITE_IO_THREADS)
  private Integer innodbWriteIoThreads;

  public static final String SERIALIZED_NAME_INNODB_THREAD_CONCURRENCY = "innodb_thread_concurrency";
  @SerializedName(SERIALIZED_NAME_INNODB_THREAD_CONCURRENCY)
  private Integer innodbThreadConcurrency;

  public static final String SERIALIZED_NAME_NET_BUFFER_LENGTH = "net_buffer_length";
  @SerializedName(SERIALIZED_NAME_NET_BUFFER_LENGTH)
  private Integer netBufferLength;

  public static final String SERIALIZED_NAME_AUTOVACUUM_FREEZE_MAX_AGE = "autovacuum_freeze_max_age";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_FREEZE_MAX_AGE)
  private Integer autovacuumFreezeMaxAge;

  public static final String SERIALIZED_NAME_AUTOVACUUM_MAX_WORKERS = "autovacuum_max_workers";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_MAX_WORKERS)
  private Integer autovacuumMaxWorkers;

  public static final String SERIALIZED_NAME_AUTOVACUUM_NAPTIME = "autovacuum_naptime";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_NAPTIME)
  private Integer autovacuumNaptime;

  public static final String SERIALIZED_NAME_AUTOVACUUM_VACUUM_THRESHOLD = "autovacuum_vacuum_threshold";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_VACUUM_THRESHOLD)
  private Integer autovacuumVacuumThreshold;

  public static final String SERIALIZED_NAME_AUTOVACUUM_ANALYZE_THRESHOLD = "autovacuum_analyze_threshold";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_ANALYZE_THRESHOLD)
  private Integer autovacuumAnalyzeThreshold;

  public static final String SERIALIZED_NAME_AUTOVACUUM_VACUUM_SCALE_FACTOR = "autovacuum_vacuum_scale_factor";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_VACUUM_SCALE_FACTOR)
  private Double autovacuumVacuumScaleFactor;

  public static final String SERIALIZED_NAME_AUTOVACUUM_ANALYZE_SCALE_FACTOR = "autovacuum_analyze_scale_factor";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_ANALYZE_SCALE_FACTOR)
  private Double autovacuumAnalyzeScaleFactor;

  public static final String SERIALIZED_NAME_AUTOVACUUM_VACUUM_COST_DELAY = "autovacuum_vacuum_cost_delay";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_VACUUM_COST_DELAY)
  private Integer autovacuumVacuumCostDelay;

  public static final String SERIALIZED_NAME_AUTOVACUUM_VACUUM_COST_LIMIT = "autovacuum_vacuum_cost_limit";
  @SerializedName(SERIALIZED_NAME_AUTOVACUUM_VACUUM_COST_LIMIT)
  private Integer autovacuumVacuumCostLimit;

  public static final String SERIALIZED_NAME_BGWRITER_DELAY = "bgwriter_delay";
  @SerializedName(SERIALIZED_NAME_BGWRITER_DELAY)
  private Integer bgwriterDelay;

  public static final String SERIALIZED_NAME_BGWRITER_FLUSH_AFTER = "bgwriter_flush_after";
  @SerializedName(SERIALIZED_NAME_BGWRITER_FLUSH_AFTER)
  private Integer bgwriterFlushAfter;

  public static final String SERIALIZED_NAME_BGWRITER_LRU_MAXPAGES = "bgwriter_lru_maxpages";
  @SerializedName(SERIALIZED_NAME_BGWRITER_LRU_MAXPAGES)
  private Integer bgwriterLruMaxpages;

  public static final String SERIALIZED_NAME_BGWRITER_LRU_MULTIPLIER = "bgwriter_lru_multiplier";
  @SerializedName(SERIALIZED_NAME_BGWRITER_LRU_MULTIPLIER)
  private Double bgwriterLruMultiplier;

  public static final String SERIALIZED_NAME_DEADLOCK_TIMEOUT = "deadlock_timeout";
  @SerializedName(SERIALIZED_NAME_DEADLOCK_TIMEOUT)
  private Integer deadlockTimeout;

  /**
   * Specifies the default TOAST compression method for values of compressible columns (the default is lz4).
   */
  @JsonAdapter(DefaultToastCompressionEnum.Adapter.class)
 public enum DefaultToastCompressionEnum {
    LZ4("lz4"),
    
    PGLZ("pglz");

    private String value;

    DefaultToastCompressionEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static DefaultToastCompressionEnum fromValue(String value) {
      for (DefaultToastCompressionEnum b : DefaultToastCompressionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<DefaultToastCompressionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final DefaultToastCompressionEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public DefaultToastCompressionEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return DefaultToastCompressionEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_DEFAULT_TOAST_COMPRESSION = "default_toast_compression";
  @SerializedName(SERIALIZED_NAME_DEFAULT_TOAST_COMPRESSION)
  private DefaultToastCompressionEnum defaultToastCompression;

  public static final String SERIALIZED_NAME_IDLE_IN_TRANSACTION_SESSION_TIMEOUT = "idle_in_transaction_session_timeout";
  @SerializedName(SERIALIZED_NAME_IDLE_IN_TRANSACTION_SESSION_TIMEOUT)
  private Integer idleInTransactionSessionTimeout;

  public static final String SERIALIZED_NAME_JIT = "jit";
  @SerializedName(SERIALIZED_NAME_JIT)
  private Boolean jit;

  public static final String SERIALIZED_NAME_LOG_AUTOVACUUM_MIN_DURATION = "log_autovacuum_min_duration";
  @SerializedName(SERIALIZED_NAME_LOG_AUTOVACUUM_MIN_DURATION)
  private Integer logAutovacuumMinDuration;

  /**
   * Controls the amount of detail written in the server log for each message that is logged.
   */
  @JsonAdapter(LogErrorVerbosityEnum.Adapter.class)
 public enum LogErrorVerbosityEnum {
    TERSE("TERSE"),
    
    DEFAULT("DEFAULT"),
    
    VERBOSE("VERBOSE");

    private String value;

    LogErrorVerbosityEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static LogErrorVerbosityEnum fromValue(String value) {
      for (LogErrorVerbosityEnum b : LogErrorVerbosityEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<LogErrorVerbosityEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final LogErrorVerbosityEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public LogErrorVerbosityEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return LogErrorVerbosityEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_LOG_ERROR_VERBOSITY = "log_error_verbosity";
  @SerializedName(SERIALIZED_NAME_LOG_ERROR_VERBOSITY)
  private LogErrorVerbosityEnum logErrorVerbosity;

  /**
   * Selects one of the available log-formats. These can support popular log analyzers like pgbadger, pganalyze, etc.
   */
  @JsonAdapter(LogLinePrefixEnum.Adapter.class)
 public enum LogLinePrefixEnum {
    PID_P_USER_U_DB_D_APP_A_CLIENT_H("pid=%p,user=%u,db=%d,app=%a,client=%h"),
    
    _M_P_Q_USER_U_DB_D_APP_A_("%m [%p] %q[user=%u,db=%d,app=%a]"),
    
    _T_P_L_1_USER_U_DB_D_APP_A_CLIENT_H("%t [%p]: [%l-1] user=%u,db=%d,app=%a,client=%h");

    private String value;

    LogLinePrefixEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static LogLinePrefixEnum fromValue(String value) {
      for (LogLinePrefixEnum b : LogLinePrefixEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<LogLinePrefixEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final LogLinePrefixEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public LogLinePrefixEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return LogLinePrefixEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_LOG_LINE_PREFIX = "log_line_prefix";
  @SerializedName(SERIALIZED_NAME_LOG_LINE_PREFIX)
  private LogLinePrefixEnum logLinePrefix;

  public static final String SERIALIZED_NAME_LOG_MIN_DURATION_STATEMENT = "log_min_duration_statement";
  @SerializedName(SERIALIZED_NAME_LOG_MIN_DURATION_STATEMENT)
  private Integer logMinDurationStatement;

  public static final String SERIALIZED_NAME_MAX_FILES_PER_PROCESS = "max_files_per_process";
  @SerializedName(SERIALIZED_NAME_MAX_FILES_PER_PROCESS)
  private Integer maxFilesPerProcess;

  public static final String SERIALIZED_NAME_MAX_PREPARED_TRANSACTIONS = "max_prepared_transactions";
  @SerializedName(SERIALIZED_NAME_MAX_PREPARED_TRANSACTIONS)
  private Integer maxPreparedTransactions;

  public static final String SERIALIZED_NAME_MAX_PRED_LOCKS_PER_TRANSACTION = "max_pred_locks_per_transaction";
  @SerializedName(SERIALIZED_NAME_MAX_PRED_LOCKS_PER_TRANSACTION)
  private Integer maxPredLocksPerTransaction;

  public static final String SERIALIZED_NAME_MAX_LOCKS_PER_TRANSACTION = "max_locks_per_transaction";
  @SerializedName(SERIALIZED_NAME_MAX_LOCKS_PER_TRANSACTION)
  private Integer maxLocksPerTransaction;

  public static final String SERIALIZED_NAME_MAX_STACK_DEPTH = "max_stack_depth";
  @SerializedName(SERIALIZED_NAME_MAX_STACK_DEPTH)
  private Integer maxStackDepth;

  public static final String SERIALIZED_NAME_MAX_STANDBY_ARCHIVE_DELAY = "max_standby_archive_delay";
  @SerializedName(SERIALIZED_NAME_MAX_STANDBY_ARCHIVE_DELAY)
  private Integer maxStandbyArchiveDelay;

  public static final String SERIALIZED_NAME_MAX_STANDBY_STREAMING_DELAY = "max_standby_streaming_delay";
  @SerializedName(SERIALIZED_NAME_MAX_STANDBY_STREAMING_DELAY)
  private Integer maxStandbyStreamingDelay;

  public static final String SERIALIZED_NAME_MAX_REPLICATION_SLOTS = "max_replication_slots";
  @SerializedName(SERIALIZED_NAME_MAX_REPLICATION_SLOTS)
  private Integer maxReplicationSlots;

  public static final String SERIALIZED_NAME_MAX_LOGICAL_REPLICATION_WORKERS = "max_logical_replication_workers";
  @SerializedName(SERIALIZED_NAME_MAX_LOGICAL_REPLICATION_WORKERS)
  private Integer maxLogicalReplicationWorkers;

  public static final String SERIALIZED_NAME_MAX_PARALLEL_WORKERS = "max_parallel_workers";
  @SerializedName(SERIALIZED_NAME_MAX_PARALLEL_WORKERS)
  private Integer maxParallelWorkers;

  public static final String SERIALIZED_NAME_MAX_PARALLEL_WORKERS_PER_GATHER = "max_parallel_workers_per_gather";
  @SerializedName(SERIALIZED_NAME_MAX_PARALLEL_WORKERS_PER_GATHER)
  private Integer maxParallelWorkersPerGather;

  public static final String SERIALIZED_NAME_MAX_WORKER_PROCESSES = "max_worker_processes";
  @SerializedName(SERIALIZED_NAME_MAX_WORKER_PROCESSES)
  private Integer maxWorkerProcesses;

  public static final String SERIALIZED_NAME_PG_PARTMAN_BGW_ROLE = "pg_partman_bgw.role";
  @SerializedName(SERIALIZED_NAME_PG_PARTMAN_BGW_ROLE)
  private String pgPartmanBgwRole;

  public static final String SERIALIZED_NAME_PG_PARTMAN_BGW_INTERVAL = "pg_partman_bgw.interval";
  @SerializedName(SERIALIZED_NAME_PG_PARTMAN_BGW_INTERVAL)
  private Integer pgPartmanBgwInterval;

  /**
   * Controls which statements are counted. Specify &#39;top&#39; to track top-level statements (those issued directly by clients), &#39;all&#39; to also track nested statements (such as statements invoked within functions), or &#39;none&#39; to disable statement statistics collection. The default value is top.
   */
  @JsonAdapter(PgStatStatementsTrackEnum.Adapter.class)
 public enum PgStatStatementsTrackEnum {
    ALL("all"),
    
    TOP("top"),
    
    NONE("none");

    private String value;

    PgStatStatementsTrackEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static PgStatStatementsTrackEnum fromValue(String value) {
      for (PgStatStatementsTrackEnum b : PgStatStatementsTrackEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<PgStatStatementsTrackEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final PgStatStatementsTrackEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public PgStatStatementsTrackEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return PgStatStatementsTrackEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_PG_STAT_STATEMENTS_TRACK = "pg_stat_statements.track";
  @SerializedName(SERIALIZED_NAME_PG_STAT_STATEMENTS_TRACK)
  private PgStatStatementsTrackEnum pgStatStatementsTrack;

  public static final String SERIALIZED_NAME_TEMP_FILE_LIMIT = "temp_file_limit";
  @SerializedName(SERIALIZED_NAME_TEMP_FILE_LIMIT)
  private Integer tempFileLimit;

  public static final String SERIALIZED_NAME_TIMEZONE = "timezone";
  @SerializedName(SERIALIZED_NAME_TIMEZONE)
  private String timezone;

  public static final String SERIALIZED_NAME_TRACK_ACTIVITY_QUERY_SIZE = "track_activity_query_size";
  @SerializedName(SERIALIZED_NAME_TRACK_ACTIVITY_QUERY_SIZE)
  private Integer trackActivityQuerySize;

  /**
   * Record commit time of transactions.
   */
  @JsonAdapter(TrackCommitTimestampEnum.Adapter.class)
 public enum TrackCommitTimestampEnum {
    FALSE("false"),
    
    TRUE("true");

    private String value;

    TrackCommitTimestampEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TrackCommitTimestampEnum fromValue(String value) {
      for (TrackCommitTimestampEnum b : TrackCommitTimestampEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<TrackCommitTimestampEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TrackCommitTimestampEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TrackCommitTimestampEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return TrackCommitTimestampEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_TRACK_COMMIT_TIMESTAMP = "track_commit_timestamp";
  @SerializedName(SERIALIZED_NAME_TRACK_COMMIT_TIMESTAMP)
  private TrackCommitTimestampEnum trackCommitTimestamp;

  /**
   * Enables tracking of function call counts and time used.
   */
  @JsonAdapter(TrackFunctionsEnum.Adapter.class)
 public enum TrackFunctionsEnum {
    ALL("all"),
    
    PL("pl"),
    
    NONE("none");

    private String value;

    TrackFunctionsEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TrackFunctionsEnum fromValue(String value) {
      for (TrackFunctionsEnum b : TrackFunctionsEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<TrackFunctionsEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TrackFunctionsEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TrackFunctionsEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return TrackFunctionsEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_TRACK_FUNCTIONS = "track_functions";
  @SerializedName(SERIALIZED_NAME_TRACK_FUNCTIONS)
  private TrackFunctionsEnum trackFunctions;

  /**
   * Enables timing of database I/O calls. This parameter is off by default, because it will repeatedly query the operating system for the current time, which may cause significant overhead on some platforms.
   */
  @JsonAdapter(TrackIoTimingEnum.Adapter.class)
 public enum TrackIoTimingEnum {
    FALSE("false"),
    
    TRUE("true");

    private String value;

    TrackIoTimingEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static TrackIoTimingEnum fromValue(String value) {
      for (TrackIoTimingEnum b : TrackIoTimingEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<TrackIoTimingEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final TrackIoTimingEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public TrackIoTimingEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return TrackIoTimingEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_TRACK_IO_TIMING = "track_io_timing";
  @SerializedName(SERIALIZED_NAME_TRACK_IO_TIMING)
  private TrackIoTimingEnum trackIoTiming;

  public static final String SERIALIZED_NAME_MAX_WAL_SENDERS = "max_wal_senders";
  @SerializedName(SERIALIZED_NAME_MAX_WAL_SENDERS)
  private Integer maxWalSenders;

  public static final String SERIALIZED_NAME_WAL_SENDER_TIMEOUT = "wal_sender_timeout";
  @SerializedName(SERIALIZED_NAME_WAL_SENDER_TIMEOUT)
  private Integer walSenderTimeout;

  public static final String SERIALIZED_NAME_WAL_WRITER_DELAY = "wal_writer_delay";
  @SerializedName(SERIALIZED_NAME_WAL_WRITER_DELAY)
  private Integer walWriterDelay;

  public static final String SERIALIZED_NAME_SHARED_BUFFERS_PERCENTAGE = "shared_buffers_percentage";
  @SerializedName(SERIALIZED_NAME_SHARED_BUFFERS_PERCENTAGE)
  private Double sharedBuffersPercentage;

  public static final String SERIALIZED_NAME_PGBOUNCER = "pgbouncer";
  @SerializedName(SERIALIZED_NAME_PGBOUNCER)
  private MysqlPgbouncer pgbouncer;

  public static final String SERIALIZED_NAME_WORK_MEM = "work_mem";
  @SerializedName(SERIALIZED_NAME_WORK_MEM)
  private Integer workMem;

  public static final String SERIALIZED_NAME_TIMESCALEDB = "timescaledb";
  @SerializedName(SERIALIZED_NAME_TIMESCALEDB)
  private MysqlTimescaledb timescaledb;

  /**
   * Synchronous replication type. Note that the service plan also needs to support synchronous replication.
   */
  @JsonAdapter(SynchronousReplicationEnum.Adapter.class)
 public enum SynchronousReplicationEnum {
    FALSE("false"),
    
    QUORUM("quorum");

    private String value;

    SynchronousReplicationEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static SynchronousReplicationEnum fromValue(String value) {
      for (SynchronousReplicationEnum b : SynchronousReplicationEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<SynchronousReplicationEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final SynchronousReplicationEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public SynchronousReplicationEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return SynchronousReplicationEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_SYNCHRONOUS_REPLICATION = "synchronous_replication";
  @SerializedName(SERIALIZED_NAME_SYNCHRONOUS_REPLICATION)
  private SynchronousReplicationEnum synchronousReplication;

  public static final String SERIALIZED_NAME_STAT_MONITOR_ENABLE = "stat_monitor_enable";
  @SerializedName(SERIALIZED_NAME_STAT_MONITOR_ENABLE)
  private Boolean statMonitorEnable;

  /**
   * A string specifying the desired eviction policy for the Redis cluster.  - &#x60;noeviction&#x60;: Don&#39;t evict any data, returns error when memory limit is reached. - &#x60;allkeys_lru:&#x60; Evict any key, least recently used (LRU) first. - &#x60;allkeys_random&#x60;: Evict keys in a random order. - &#x60;volatile_lru&#x60;: Evict keys with expiration only, least recently used (LRU) first. - &#x60;volatile_random&#x60;: Evict keys with expiration only in a random order. - &#x60;volatile_ttl&#x60;: Evict keys with expiration only, shortest time-to-live (TTL) first.
   */
  @JsonAdapter(RedisMaxmemoryPolicyEnum.Adapter.class)
 public enum RedisMaxmemoryPolicyEnum {
    NOEVICTION("noeviction"),
    
    ALLKEYS_LRU("allkeys_lru"),
    
    ALLKEYS_RANDOM("allkeys_random"),
    
    VOLATILE_LRU("volatile_lru"),
    
    VOLATILE_RANDOM("volatile_random"),
    
    VOLATILE_TTL("volatile_ttl");

    private String value;

    RedisMaxmemoryPolicyEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static RedisMaxmemoryPolicyEnum fromValue(String value) {
      for (RedisMaxmemoryPolicyEnum b : RedisMaxmemoryPolicyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<RedisMaxmemoryPolicyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RedisMaxmemoryPolicyEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public RedisMaxmemoryPolicyEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return RedisMaxmemoryPolicyEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_REDIS_MAXMEMORY_POLICY = "redis_maxmemory_policy";
  @SerializedName(SERIALIZED_NAME_REDIS_MAXMEMORY_POLICY)
  private RedisMaxmemoryPolicyEnum redisMaxmemoryPolicy;

  public static final String SERIALIZED_NAME_REDIS_PUBSUB_CLIENT_OUTPUT_BUFFER_LIMIT = "redis_pubsub_client_output_buffer_limit";
  @SerializedName(SERIALIZED_NAME_REDIS_PUBSUB_CLIENT_OUTPUT_BUFFER_LIMIT)
  private Integer redisPubsubClientOutputBufferLimit;

  public static final String SERIALIZED_NAME_REDIS_NUMBER_OF_DATABASES = "redis_number_of_databases";
  @SerializedName(SERIALIZED_NAME_REDIS_NUMBER_OF_DATABASES)
  private Integer redisNumberOfDatabases;

  public static final String SERIALIZED_NAME_REDIS_IO_THREADS = "redis_io_threads";
  @SerializedName(SERIALIZED_NAME_REDIS_IO_THREADS)
  private Integer redisIoThreads;

  public static final String SERIALIZED_NAME_REDIS_LFU_LOG_FACTOR = "redis_lfu_log_factor";
  @SerializedName(SERIALIZED_NAME_REDIS_LFU_LOG_FACTOR)
  private Integer redisLfuLogFactor = 10;

  public static final String SERIALIZED_NAME_REDIS_LFU_DECAY_TIME = "redis_lfu_decay_time";
  @SerializedName(SERIALIZED_NAME_REDIS_LFU_DECAY_TIME)
  private Integer redisLfuDecayTime = 1;

  public static final String SERIALIZED_NAME_REDIS_SSL = "redis_ssl";
  @SerializedName(SERIALIZED_NAME_REDIS_SSL)
  private Boolean redisSsl = true;

  public static final String SERIALIZED_NAME_REDIS_TIMEOUT = "redis_timeout";
  @SerializedName(SERIALIZED_NAME_REDIS_TIMEOUT)
  private Integer redisTimeout = 300;

  public static final String SERIALIZED_NAME_REDIS_NOTIFY_KEYSPACE_EVENTS = "redis_notify_keyspace_events";
  @SerializedName(SERIALIZED_NAME_REDIS_NOTIFY_KEYSPACE_EVENTS)
  private String redisNotifyKeyspaceEvents = "";

  /**
   * When persistence is &#39;rdb&#39;, Redis does RDB dumps each 10 minutes if any key is changed. Also RDB dumps are done according to backup schedule for backup purposes. When persistence is &#39;off&#39;, no RDB dumps and backups are done, so data can be lost at any moment if service is restarted for any reason, or if service is powered off. Also service can&#39;t be forked.
   */
  @JsonAdapter(RedisPersistenceEnum.Adapter.class)
 public enum RedisPersistenceEnum {
    FALSE("false"),
    
    RDB("rdb");

    private String value;

    RedisPersistenceEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static RedisPersistenceEnum fromValue(String value) {
      for (RedisPersistenceEnum b : RedisPersistenceEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<RedisPersistenceEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RedisPersistenceEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public RedisPersistenceEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return RedisPersistenceEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_REDIS_PERSISTENCE = "redis_persistence";
  @SerializedName(SERIALIZED_NAME_REDIS_PERSISTENCE)
  private RedisPersistenceEnum redisPersistence;

  /**
   * Determines default pub/sub channels&#39; ACL for new users if ACL is not supplied. When this option is not defined, all_channels is assumed to keep backward compatibility. This option doesn&#39;t affect Redis configuration acl-pubsub-default.
   */
  @JsonAdapter(RedisAclChannelsDefaultEnum.Adapter.class)
 public enum RedisAclChannelsDefaultEnum {
    ALLCHANNELS("allchannels"),
    
    RESETCHANNELS("resetchannels");

    private String value;

    RedisAclChannelsDefaultEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static RedisAclChannelsDefaultEnum fromValue(String value) {
      for (RedisAclChannelsDefaultEnum b : RedisAclChannelsDefaultEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<RedisAclChannelsDefaultEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final RedisAclChannelsDefaultEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public RedisAclChannelsDefaultEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return RedisAclChannelsDefaultEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_REDIS_ACL_CHANNELS_DEFAULT = "redis_acl_channels_default";
  @SerializedName(SERIALIZED_NAME_REDIS_ACL_CHANNELS_DEFAULT)
  private RedisAclChannelsDefaultEnum redisAclChannelsDefault;

  public Mpr1() {
  }

  public Mpr1 backupHour(Integer backupHour) {
    if (backupHour != null && backupHour < 0) {
      throw new IllegalArgumentException("Invalid value for backupHour. Must be greater than or equal to 0.");
    }
    if (backupHour != null && backupHour > 23) {
      throw new IllegalArgumentException("Invalid value for backupHour. Must be less than or equal to 23.");
    }
    
    
    this.backupHour = backupHour;
    return this;
  }

   /**
   * The hour of day (in UTC) when backup for the service starts. New backup only starts if previous backup has already completed.
   * minimum: 0
   * maximum: 23
   * @return backupHour
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "3", value = "The hour of day (in UTC) when backup for the service starts. New backup only starts if previous backup has already completed.")

  public Integer getBackupHour() {
    return backupHour;
  }


  public void setBackupHour(Integer backupHour) {
    if (backupHour != null && backupHour < 0) {
      throw new IllegalArgumentException("Invalid value for backupHour. Must be greater than or equal to 0.");
    }
    if (backupHour != null && backupHour > 23) {
      throw new IllegalArgumentException("Invalid value for backupHour. Must be less than or equal to 23.");
    }
    
    this.backupHour = backupHour;
  }


  public Mpr1 backupMinute(Integer backupMinute) {
    if (backupMinute != null && backupMinute < 0) {
      throw new IllegalArgumentException("Invalid value for backupMinute. Must be greater than or equal to 0.");
    }
    if (backupMinute != null && backupMinute > 59) {
      throw new IllegalArgumentException("Invalid value for backupMinute. Must be less than or equal to 59.");
    }
    
    
    this.backupMinute = backupMinute;
    return this;
  }

   /**
   * The minute of the backup hour when backup for the service starts. New backup  only starts if previous backup has already completed.
   * minimum: 0
   * maximum: 59
   * @return backupMinute
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "30", value = "The minute of the backup hour when backup for the service starts. New backup  only starts if previous backup has already completed.")

  public Integer getBackupMinute() {
    return backupMinute;
  }


  public void setBackupMinute(Integer backupMinute) {
    if (backupMinute != null && backupMinute < 0) {
      throw new IllegalArgumentException("Invalid value for backupMinute. Must be greater than or equal to 0.");
    }
    if (backupMinute != null && backupMinute > 59) {
      throw new IllegalArgumentException("Invalid value for backupMinute. Must be less than or equal to 59.");
    }
    
    this.backupMinute = backupMinute;
  }


  public Mpr1 sqlMode(String sqlMode) {
    
    
    
    
    this.sqlMode = sqlMode;
    return this;
  }

   /**
   * Global SQL mode. If empty, uses MySQL server defaults. Must only include uppercase alphabetic characters, underscores, and commas.
   * @return sqlMode
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "ANSI,TRADITIONAL", value = "Global SQL mode. If empty, uses MySQL server defaults. Must only include uppercase alphabetic characters, underscores, and commas.")

  public String getSqlMode() {
    return sqlMode;
  }


  public void setSqlMode(String sqlMode) {
    
    
    
    this.sqlMode = sqlMode;
  }


  public Mpr1 connectTimeout(Integer connectTimeout) {
    if (connectTimeout != null && connectTimeout < 2) {
      throw new IllegalArgumentException("Invalid value for connectTimeout. Must be greater than or equal to 2.");
    }
    if (connectTimeout != null && connectTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for connectTimeout. Must be less than or equal to 3600.");
    }
    
    
    this.connectTimeout = connectTimeout;
    return this;
  }

   /**
   * The number of seconds that the mysqld server waits for a connect packet before responding with bad handshake.
   * minimum: 2
   * maximum: 3600
   * @return connectTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10", value = "The number of seconds that the mysqld server waits for a connect packet before responding with bad handshake.")

  public Integer getConnectTimeout() {
    return connectTimeout;
  }


  public void setConnectTimeout(Integer connectTimeout) {
    if (connectTimeout != null && connectTimeout < 2) {
      throw new IllegalArgumentException("Invalid value for connectTimeout. Must be greater than or equal to 2.");
    }
    if (connectTimeout != null && connectTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for connectTimeout. Must be less than or equal to 3600.");
    }
    
    this.connectTimeout = connectTimeout;
  }


  public Mpr1 defaultTimeZone(String defaultTimeZone) {
    
    
    if (defaultTimeZone != null && defaultTimeZone.length() < 2) {
      throw new IllegalArgumentException("Invalid value for defaultTimeZone. Length must be greater than or equal to 2.");
    }
    
    this.defaultTimeZone = defaultTimeZone;
    return this;
  }

   /**
   * Default server time zone, in the form of an offset from UTC (from -12:00 to +12:00), a time zone name (EST), or &#39;SYSTEM&#39; to use the MySQL server default.
   * @return defaultTimeZone
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "+03:00", value = "Default server time zone, in the form of an offset from UTC (from -12:00 to +12:00), a time zone name (EST), or 'SYSTEM' to use the MySQL server default.")

  public String getDefaultTimeZone() {
    return defaultTimeZone;
  }


  public void setDefaultTimeZone(String defaultTimeZone) {
    
    
    if (defaultTimeZone != null && defaultTimeZone.length() < 2) {
      throw new IllegalArgumentException("Invalid value for defaultTimeZone. Length must be greater than or equal to 2.");
    }
    this.defaultTimeZone = defaultTimeZone;
  }


  public Mpr1 groupConcatMaxLen(Integer groupConcatMaxLen) {
    if (groupConcatMaxLen != null && groupConcatMaxLen < 4) {
      throw new IllegalArgumentException("Invalid value for groupConcatMaxLen. Must be greater than or equal to 4.");
    }
    if (groupConcatMaxLen != null && groupConcatMaxLen > 384) {
      throw new IllegalArgumentException("Invalid value for groupConcatMaxLen. Must be less than or equal to 384.");
    }
    
    
    this.groupConcatMaxLen = groupConcatMaxLen;
    return this;
  }

   /**
   * The maximum permitted result length, in bytes, for the GROUP_CONCAT() function.
   * minimum: 4
   * maximum: 384
   * @return groupConcatMaxLen
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1024", value = "The maximum permitted result length, in bytes, for the GROUP_CONCAT() function.")

  public Integer getGroupConcatMaxLen() {
    return groupConcatMaxLen;
  }


  public void setGroupConcatMaxLen(Integer groupConcatMaxLen) {
    if (groupConcatMaxLen != null && groupConcatMaxLen < 4) {
      throw new IllegalArgumentException("Invalid value for groupConcatMaxLen. Must be greater than or equal to 4.");
    }
    if (groupConcatMaxLen != null && groupConcatMaxLen > 384) {
      throw new IllegalArgumentException("Invalid value for groupConcatMaxLen. Must be less than or equal to 384.");
    }
    
    this.groupConcatMaxLen = groupConcatMaxLen;
  }


  public Mpr1 informationSchemaStatsExpiry(Integer informationSchemaStatsExpiry) {
    if (informationSchemaStatsExpiry != null && informationSchemaStatsExpiry < 900) {
      throw new IllegalArgumentException("Invalid value for informationSchemaStatsExpiry. Must be greater than or equal to 900.");
    }
    if (informationSchemaStatsExpiry != null && informationSchemaStatsExpiry > 31536000) {
      throw new IllegalArgumentException("Invalid value for informationSchemaStatsExpiry. Must be less than or equal to 31536000.");
    }
    
    
    this.informationSchemaStatsExpiry = informationSchemaStatsExpiry;
    return this;
  }

   /**
   * The time, in seconds, before cached statistics expire.
   * minimum: 900
   * maximum: 31536000
   * @return informationSchemaStatsExpiry
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "86400", value = "The time, in seconds, before cached statistics expire.")

  public Integer getInformationSchemaStatsExpiry() {
    return informationSchemaStatsExpiry;
  }


  public void setInformationSchemaStatsExpiry(Integer informationSchemaStatsExpiry) {
    if (informationSchemaStatsExpiry != null && informationSchemaStatsExpiry < 900) {
      throw new IllegalArgumentException("Invalid value for informationSchemaStatsExpiry. Must be greater than or equal to 900.");
    }
    if (informationSchemaStatsExpiry != null && informationSchemaStatsExpiry > 31536000) {
      throw new IllegalArgumentException("Invalid value for informationSchemaStatsExpiry. Must be less than or equal to 31536000.");
    }
    
    this.informationSchemaStatsExpiry = informationSchemaStatsExpiry;
  }


  public Mpr1 innodbFtMinTokenSize(Integer innodbFtMinTokenSize) {
    if (innodbFtMinTokenSize != null && innodbFtMinTokenSize < 0) {
      throw new IllegalArgumentException("Invalid value for innodbFtMinTokenSize. Must be greater than or equal to 0.");
    }
    if (innodbFtMinTokenSize != null && innodbFtMinTokenSize > 16) {
      throw new IllegalArgumentException("Invalid value for innodbFtMinTokenSize. Must be less than or equal to 16.");
    }
    
    
    this.innodbFtMinTokenSize = innodbFtMinTokenSize;
    return this;
  }

   /**
   * The minimum length of words that an InnoDB FULLTEXT index stores.
   * minimum: 0
   * maximum: 16
   * @return innodbFtMinTokenSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "3", value = "The minimum length of words that an InnoDB FULLTEXT index stores.")

  public Integer getInnodbFtMinTokenSize() {
    return innodbFtMinTokenSize;
  }


  public void setInnodbFtMinTokenSize(Integer innodbFtMinTokenSize) {
    if (innodbFtMinTokenSize != null && innodbFtMinTokenSize < 0) {
      throw new IllegalArgumentException("Invalid value for innodbFtMinTokenSize. Must be greater than or equal to 0.");
    }
    if (innodbFtMinTokenSize != null && innodbFtMinTokenSize > 16) {
      throw new IllegalArgumentException("Invalid value for innodbFtMinTokenSize. Must be less than or equal to 16.");
    }
    
    this.innodbFtMinTokenSize = innodbFtMinTokenSize;
  }


  public Mpr1 innodbFtServerStopwordTable(String innodbFtServerStopwordTable) {
    
    
    
    
    this.innodbFtServerStopwordTable = innodbFtServerStopwordTable;
    return this;
  }

   /**
   * The InnoDB FULLTEXT index stopword list for all InnoDB tables.
   * @return innodbFtServerStopwordTable
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "db_name/table_name", value = "The InnoDB FULLTEXT index stopword list for all InnoDB tables.")

  public String getInnodbFtServerStopwordTable() {
    return innodbFtServerStopwordTable;
  }


  public void setInnodbFtServerStopwordTable(String innodbFtServerStopwordTable) {
    
    
    
    this.innodbFtServerStopwordTable = innodbFtServerStopwordTable;
  }


  public Mpr1 innodbLockWaitTimeout(Integer innodbLockWaitTimeout) {
    if (innodbLockWaitTimeout != null && innodbLockWaitTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for innodbLockWaitTimeout. Must be greater than or equal to 1.");
    }
    if (innodbLockWaitTimeout != null && innodbLockWaitTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for innodbLockWaitTimeout. Must be less than or equal to 3600.");
    }
    
    
    this.innodbLockWaitTimeout = innodbLockWaitTimeout;
    return this;
  }

   /**
   * The time, in seconds, that an InnoDB transaction waits for a row lock. before giving up.
   * minimum: 1
   * maximum: 3600
   * @return innodbLockWaitTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "50", value = "The time, in seconds, that an InnoDB transaction waits for a row lock. before giving up.")

  public Integer getInnodbLockWaitTimeout() {
    return innodbLockWaitTimeout;
  }


  public void setInnodbLockWaitTimeout(Integer innodbLockWaitTimeout) {
    if (innodbLockWaitTimeout != null && innodbLockWaitTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for innodbLockWaitTimeout. Must be greater than or equal to 1.");
    }
    if (innodbLockWaitTimeout != null && innodbLockWaitTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for innodbLockWaitTimeout. Must be less than or equal to 3600.");
    }
    
    this.innodbLockWaitTimeout = innodbLockWaitTimeout;
  }


  public Mpr1 innodbLogBufferSize(Integer innodbLogBufferSize) {
    if (innodbLogBufferSize != null && innodbLogBufferSize < 1048576) {
      throw new IllegalArgumentException("Invalid value for innodbLogBufferSize. Must be greater than or equal to 1048576.");
    }
    if (innodbLogBufferSize != null && innodbLogBufferSize > 4294967295) {
      throw new IllegalArgumentException("Invalid value for innodbLogBufferSize. Must be less than or equal to 4294967295.");
    }
    
    
    this.innodbLogBufferSize = innodbLogBufferSize;
    return this;
  }

   /**
   * The size of the buffer, in bytes, that InnoDB uses to write to the log files. on disk.
   * minimum: 1048576
   * maximum: 4294967295
   * @return innodbLogBufferSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16777216", value = "The size of the buffer, in bytes, that InnoDB uses to write to the log files. on disk.")

  public Integer getInnodbLogBufferSize() {
    return innodbLogBufferSize;
  }


  public void setInnodbLogBufferSize(Integer innodbLogBufferSize) {
    if (innodbLogBufferSize != null && innodbLogBufferSize < 1048576) {
      throw new IllegalArgumentException("Invalid value for innodbLogBufferSize. Must be greater than or equal to 1048576.");
    }
    if (innodbLogBufferSize != null && innodbLogBufferSize > 4294967295) {
      throw new IllegalArgumentException("Invalid value for innodbLogBufferSize. Must be less than or equal to 4294967295.");
    }
    
    this.innodbLogBufferSize = innodbLogBufferSize;
  }


  public Mpr1 innodbOnlineAlterLogMaxSize(Integer innodbOnlineAlterLogMaxSize) {
    if (innodbOnlineAlterLogMaxSize != null && innodbOnlineAlterLogMaxSize < 65536) {
      throw new IllegalArgumentException("Invalid value for innodbOnlineAlterLogMaxSize. Must be greater than or equal to 65536.");
    }
    if (innodbOnlineAlterLogMaxSize != null && innodbOnlineAlterLogMaxSize > 1099511627776) {
      throw new IllegalArgumentException("Invalid value for innodbOnlineAlterLogMaxSize. Must be less than or equal to 1099511627776.");
    }
    
    
    this.innodbOnlineAlterLogMaxSize = innodbOnlineAlterLogMaxSize;
    return this;
  }

   /**
   * The upper limit, in bytes, of the size of the temporary log files used during online DDL operations for InnoDB tables.
   * minimum: 65536
   * maximum: 1099511627776
   * @return innodbOnlineAlterLogMaxSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "134217728", value = "The upper limit, in bytes, of the size of the temporary log files used during online DDL operations for InnoDB tables.")

  public Integer getInnodbOnlineAlterLogMaxSize() {
    return innodbOnlineAlterLogMaxSize;
  }


  public void setInnodbOnlineAlterLogMaxSize(Integer innodbOnlineAlterLogMaxSize) {
    if (innodbOnlineAlterLogMaxSize != null && innodbOnlineAlterLogMaxSize < 65536) {
      throw new IllegalArgumentException("Invalid value for innodbOnlineAlterLogMaxSize. Must be greater than or equal to 65536.");
    }
    if (innodbOnlineAlterLogMaxSize != null && innodbOnlineAlterLogMaxSize > 1099511627776) {
      throw new IllegalArgumentException("Invalid value for innodbOnlineAlterLogMaxSize. Must be less than or equal to 1099511627776.");
    }
    
    this.innodbOnlineAlterLogMaxSize = innodbOnlineAlterLogMaxSize;
  }


  public Mpr1 innodbPrintAllDeadlocks(Boolean innodbPrintAllDeadlocks) {
    
    
    
    
    this.innodbPrintAllDeadlocks = innodbPrintAllDeadlocks;
    return this;
  }

   /**
   * When enabled, records information about all deadlocks in InnoDB user transactions  in the error log. Disabled by default.
   * @return innodbPrintAllDeadlocks
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "When enabled, records information about all deadlocks in InnoDB user transactions  in the error log. Disabled by default.")

  public Boolean getInnodbPrintAllDeadlocks() {
    return innodbPrintAllDeadlocks;
  }


  public void setInnodbPrintAllDeadlocks(Boolean innodbPrintAllDeadlocks) {
    
    
    
    this.innodbPrintAllDeadlocks = innodbPrintAllDeadlocks;
  }


  public Mpr1 innodbRollbackOnTimeout(Boolean innodbRollbackOnTimeout) {
    
    
    
    
    this.innodbRollbackOnTimeout = innodbRollbackOnTimeout;
    return this;
  }

   /**
   * When enabled, transaction timeouts cause InnoDB to abort and roll back the entire transaction.
   * @return innodbRollbackOnTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "When enabled, transaction timeouts cause InnoDB to abort and roll back the entire transaction.")

  public Boolean getInnodbRollbackOnTimeout() {
    return innodbRollbackOnTimeout;
  }


  public void setInnodbRollbackOnTimeout(Boolean innodbRollbackOnTimeout) {
    
    
    
    this.innodbRollbackOnTimeout = innodbRollbackOnTimeout;
  }


  public Mpr1 interactiveTimeout(Integer interactiveTimeout) {
    if (interactiveTimeout != null && interactiveTimeout < 30) {
      throw new IllegalArgumentException("Invalid value for interactiveTimeout. Must be greater than or equal to 30.");
    }
    if (interactiveTimeout != null && interactiveTimeout > 604800) {
      throw new IllegalArgumentException("Invalid value for interactiveTimeout. Must be less than or equal to 604800.");
    }
    
    
    this.interactiveTimeout = interactiveTimeout;
    return this;
  }

   /**
   * The time, in seconds, the server waits for activity on an interactive. connection before closing it.
   * minimum: 30
   * maximum: 604800
   * @return interactiveTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "3600", value = "The time, in seconds, the server waits for activity on an interactive. connection before closing it.")

  public Integer getInteractiveTimeout() {
    return interactiveTimeout;
  }


  public void setInteractiveTimeout(Integer interactiveTimeout) {
    if (interactiveTimeout != null && interactiveTimeout < 30) {
      throw new IllegalArgumentException("Invalid value for interactiveTimeout. Must be greater than or equal to 30.");
    }
    if (interactiveTimeout != null && interactiveTimeout > 604800) {
      throw new IllegalArgumentException("Invalid value for interactiveTimeout. Must be less than or equal to 604800.");
    }
    
    this.interactiveTimeout = interactiveTimeout;
  }


  public Mpr1 internalTmpMemStorageEngine(InternalTmpMemStorageEngineEnum internalTmpMemStorageEngine) {
    
    
    
    
    this.internalTmpMemStorageEngine = internalTmpMemStorageEngine;
    return this;
  }

   /**
   * The storage engine for in-memory internal temporary tables.
   * @return internalTmpMemStorageEngine
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "TEMPTABLE", value = "The storage engine for in-memory internal temporary tables.")

  public InternalTmpMemStorageEngineEnum getInternalTmpMemStorageEngine() {
    return internalTmpMemStorageEngine;
  }


  public void setInternalTmpMemStorageEngine(InternalTmpMemStorageEngineEnum internalTmpMemStorageEngine) {
    
    
    
    this.internalTmpMemStorageEngine = internalTmpMemStorageEngine;
  }


  public Mpr1 netReadTimeout(Integer netReadTimeout) {
    if (netReadTimeout != null && netReadTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for netReadTimeout. Must be greater than or equal to 1.");
    }
    if (netReadTimeout != null && netReadTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for netReadTimeout. Must be less than or equal to 3600.");
    }
    
    
    this.netReadTimeout = netReadTimeout;
    return this;
  }

   /**
   * The time, in seconds, to wait for more data from an existing connection. aborting the read.
   * minimum: 1
   * maximum: 3600
   * @return netReadTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "30", value = "The time, in seconds, to wait for more data from an existing connection. aborting the read.")

  public Integer getNetReadTimeout() {
    return netReadTimeout;
  }


  public void setNetReadTimeout(Integer netReadTimeout) {
    if (netReadTimeout != null && netReadTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for netReadTimeout. Must be greater than or equal to 1.");
    }
    if (netReadTimeout != null && netReadTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for netReadTimeout. Must be less than or equal to 3600.");
    }
    
    this.netReadTimeout = netReadTimeout;
  }


  public Mpr1 netWriteTimeout(Integer netWriteTimeout) {
    if (netWriteTimeout != null && netWriteTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for netWriteTimeout. Must be greater than or equal to 1.");
    }
    if (netWriteTimeout != null && netWriteTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for netWriteTimeout. Must be less than or equal to 3600.");
    }
    
    
    this.netWriteTimeout = netWriteTimeout;
    return this;
  }

   /**
   * The number of seconds to wait for a block to be written to a connection before aborting the write.
   * minimum: 1
   * maximum: 3600
   * @return netWriteTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "30", value = "The number of seconds to wait for a block to be written to a connection before aborting the write.")

  public Integer getNetWriteTimeout() {
    return netWriteTimeout;
  }


  public void setNetWriteTimeout(Integer netWriteTimeout) {
    if (netWriteTimeout != null && netWriteTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for netWriteTimeout. Must be greater than or equal to 1.");
    }
    if (netWriteTimeout != null && netWriteTimeout > 3600) {
      throw new IllegalArgumentException("Invalid value for netWriteTimeout. Must be less than or equal to 3600.");
    }
    
    this.netWriteTimeout = netWriteTimeout;
  }


  public Mpr1 sqlRequirePrimaryKey(Boolean sqlRequirePrimaryKey) {
    
    
    
    
    this.sqlRequirePrimaryKey = sqlRequirePrimaryKey;
    return this;
  }

   /**
   * Require primary key to be defined for new tables or old tables modified with ALTER TABLE and fail if missing. It is recommended to always have primary keys because various functionality may break if any large table is missing them.
   * @return sqlRequirePrimaryKey
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "Require primary key to be defined for new tables or old tables modified with ALTER TABLE and fail if missing. It is recommended to always have primary keys because various functionality may break if any large table is missing them.")

  public Boolean getSqlRequirePrimaryKey() {
    return sqlRequirePrimaryKey;
  }


  public void setSqlRequirePrimaryKey(Boolean sqlRequirePrimaryKey) {
    
    
    
    this.sqlRequirePrimaryKey = sqlRequirePrimaryKey;
  }


  public Mpr1 waitTimeout(Integer waitTimeout) {
    if (waitTimeout != null && waitTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for waitTimeout. Must be greater than or equal to 1.");
    }
    if (waitTimeout != null && waitTimeout > 2147483) {
      throw new IllegalArgumentException("Invalid value for waitTimeout. Must be less than or equal to 2147483.");
    }
    
    
    this.waitTimeout = waitTimeout;
    return this;
  }

   /**
   * The number of seconds the server waits for activity on a noninteractive connection before closing it.
   * minimum: 1
   * maximum: 2147483
   * @return waitTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "28800", value = "The number of seconds the server waits for activity on a noninteractive connection before closing it.")

  public Integer getWaitTimeout() {
    return waitTimeout;
  }


  public void setWaitTimeout(Integer waitTimeout) {
    if (waitTimeout != null && waitTimeout < 1) {
      throw new IllegalArgumentException("Invalid value for waitTimeout. Must be greater than or equal to 1.");
    }
    if (waitTimeout != null && waitTimeout > 2147483) {
      throw new IllegalArgumentException("Invalid value for waitTimeout. Must be less than or equal to 2147483.");
    }
    
    this.waitTimeout = waitTimeout;
  }


  public Mpr1 maxAllowedPacket(Integer maxAllowedPacket) {
    if (maxAllowedPacket != null && maxAllowedPacket < 102400) {
      throw new IllegalArgumentException("Invalid value for maxAllowedPacket. Must be greater than or equal to 102400.");
    }
    if (maxAllowedPacket != null && maxAllowedPacket > 1073741824) {
      throw new IllegalArgumentException("Invalid value for maxAllowedPacket. Must be less than or equal to 1073741824.");
    }
    
    
    this.maxAllowedPacket = maxAllowedPacket;
    return this;
  }

   /**
   * The size of the largest message, in bytes, that can be received by the server. Default is 67108864 (64M).
   * minimum: 102400
   * maximum: 1073741824
   * @return maxAllowedPacket
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "67108864", value = "The size of the largest message, in bytes, that can be received by the server. Default is 67108864 (64M).")

  public Integer getMaxAllowedPacket() {
    return maxAllowedPacket;
  }


  public void setMaxAllowedPacket(Integer maxAllowedPacket) {
    if (maxAllowedPacket != null && maxAllowedPacket < 102400) {
      throw new IllegalArgumentException("Invalid value for maxAllowedPacket. Must be greater than or equal to 102400.");
    }
    if (maxAllowedPacket != null && maxAllowedPacket > 1073741824) {
      throw new IllegalArgumentException("Invalid value for maxAllowedPacket. Must be less than or equal to 1073741824.");
    }
    
    this.maxAllowedPacket = maxAllowedPacket;
  }


  public Mpr1 maxHeapTableSize(Integer maxHeapTableSize) {
    if (maxHeapTableSize != null && maxHeapTableSize < 1048576) {
      throw new IllegalArgumentException("Invalid value for maxHeapTableSize. Must be greater than or equal to 1048576.");
    }
    if (maxHeapTableSize != null && maxHeapTableSize > 1073741824) {
      throw new IllegalArgumentException("Invalid value for maxHeapTableSize. Must be less than or equal to 1073741824.");
    }
    
    
    this.maxHeapTableSize = maxHeapTableSize;
    return this;
  }

   /**
   * The maximum size, in bytes, of internal in-memory tables. Also set tmp_table_size. Default is 16777216 (16M)
   * minimum: 1048576
   * maximum: 1073741824
   * @return maxHeapTableSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16777216", value = "The maximum size, in bytes, of internal in-memory tables. Also set tmp_table_size. Default is 16777216 (16M)")

  public Integer getMaxHeapTableSize() {
    return maxHeapTableSize;
  }


  public void setMaxHeapTableSize(Integer maxHeapTableSize) {
    if (maxHeapTableSize != null && maxHeapTableSize < 1048576) {
      throw new IllegalArgumentException("Invalid value for maxHeapTableSize. Must be greater than or equal to 1048576.");
    }
    if (maxHeapTableSize != null && maxHeapTableSize > 1073741824) {
      throw new IllegalArgumentException("Invalid value for maxHeapTableSize. Must be less than or equal to 1073741824.");
    }
    
    this.maxHeapTableSize = maxHeapTableSize;
  }


  public Mpr1 sortBufferSize(Integer sortBufferSize) {
    if (sortBufferSize != null && sortBufferSize < 32768) {
      throw new IllegalArgumentException("Invalid value for sortBufferSize. Must be greater than or equal to 32768.");
    }
    if (sortBufferSize != null && sortBufferSize > 1073741824) {
      throw new IllegalArgumentException("Invalid value for sortBufferSize. Must be less than or equal to 1073741824.");
    }
    
    
    this.sortBufferSize = sortBufferSize;
    return this;
  }

   /**
   * The sort buffer size, in bytes, for ORDER BY optimization. Default is 262144. (256K).
   * minimum: 32768
   * maximum: 1073741824
   * @return sortBufferSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "262144", value = "The sort buffer size, in bytes, for ORDER BY optimization. Default is 262144. (256K).")

  public Integer getSortBufferSize() {
    return sortBufferSize;
  }


  public void setSortBufferSize(Integer sortBufferSize) {
    if (sortBufferSize != null && sortBufferSize < 32768) {
      throw new IllegalArgumentException("Invalid value for sortBufferSize. Must be greater than or equal to 32768.");
    }
    if (sortBufferSize != null && sortBufferSize > 1073741824) {
      throw new IllegalArgumentException("Invalid value for sortBufferSize. Must be less than or equal to 1073741824.");
    }
    
    this.sortBufferSize = sortBufferSize;
  }


  public Mpr1 tmpTableSize(Integer tmpTableSize) {
    if (tmpTableSize != null && tmpTableSize < 1048576) {
      throw new IllegalArgumentException("Invalid value for tmpTableSize. Must be greater than or equal to 1048576.");
    }
    if (tmpTableSize != null && tmpTableSize > 1073741824) {
      throw new IllegalArgumentException("Invalid value for tmpTableSize. Must be less than or equal to 1073741824.");
    }
    
    
    this.tmpTableSize = tmpTableSize;
    return this;
  }

   /**
   * The maximum size, in bytes, of internal in-memory tables. Also set max_heap_table_size. Default is 16777216 (16M).
   * minimum: 1048576
   * maximum: 1073741824
   * @return tmpTableSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16777216", value = "The maximum size, in bytes, of internal in-memory tables. Also set max_heap_table_size. Default is 16777216 (16M).")

  public Integer getTmpTableSize() {
    return tmpTableSize;
  }


  public void setTmpTableSize(Integer tmpTableSize) {
    if (tmpTableSize != null && tmpTableSize < 1048576) {
      throw new IllegalArgumentException("Invalid value for tmpTableSize. Must be greater than or equal to 1048576.");
    }
    if (tmpTableSize != null && tmpTableSize > 1073741824) {
      throw new IllegalArgumentException("Invalid value for tmpTableSize. Must be less than or equal to 1073741824.");
    }
    
    this.tmpTableSize = tmpTableSize;
  }


  public Mpr1 slowQueryLog(Boolean slowQueryLog) {
    
    
    
    
    this.slowQueryLog = slowQueryLog;
    return this;
  }

   /**
   * When enabled, captures slow queries. When disabled, also truncates the mysql.slow_log table. Default is false.
   * @return slowQueryLog
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "When enabled, captures slow queries. When disabled, also truncates the mysql.slow_log table. Default is false.")

  public Boolean getSlowQueryLog() {
    return slowQueryLog;
  }


  public void setSlowQueryLog(Boolean slowQueryLog) {
    
    
    
    this.slowQueryLog = slowQueryLog;
  }


  public Mpr1 longQueryTime(Double longQueryTime) {
    if (longQueryTime != null && longQueryTime < 0) {
      throw new IllegalArgumentException("Invalid value for longQueryTime. Must be greater than or equal to 0.");
    }
    if (longQueryTime != null && longQueryTime > 3600) {
      throw new IllegalArgumentException("Invalid value for longQueryTime. Must be less than or equal to 3600.");
    }
    
    
    this.longQueryTime = longQueryTime;
    return this;
  }

  public Mpr1 longQueryTime(Integer longQueryTime) {
    if (longQueryTime != null && longQueryTime < 0) {
      throw new IllegalArgumentException("Invalid value for longQueryTime. Must be greater than or equal to 0.");
    }
    if (longQueryTime != null && longQueryTime > 3600) {
      throw new IllegalArgumentException("Invalid value for longQueryTime. Must be less than or equal to 3600.");
    }
    
    
    this.longQueryTime = longQueryTime.doubleValue();
    return this;
  }

   /**
   * The time, in seconds, for a query to take to execute before  being captured by slow_query_logs. Default is 10 seconds.
   * minimum: 0
   * maximum: 3600
   * @return longQueryTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10", value = "The time, in seconds, for a query to take to execute before  being captured by slow_query_logs. Default is 10 seconds.")

  public Double getLongQueryTime() {
    return longQueryTime;
  }


  public void setLongQueryTime(Double longQueryTime) {
    if (longQueryTime != null && longQueryTime < 0) {
      throw new IllegalArgumentException("Invalid value for longQueryTime. Must be greater than or equal to 0.");
    }
    if (longQueryTime != null && longQueryTime > 3600) {
      throw new IllegalArgumentException("Invalid value for longQueryTime. Must be less than or equal to 3600.");
    }
    
    this.longQueryTime = longQueryTime;
  }


  public Mpr1 binlogRetentionPeriod(Double binlogRetentionPeriod) {
    if (binlogRetentionPeriod != null && binlogRetentionPeriod < 600) {
      throw new IllegalArgumentException("Invalid value for binlogRetentionPeriod. Must be greater than or equal to 600.");
    }
    if (binlogRetentionPeriod != null && binlogRetentionPeriod > 86400) {
      throw new IllegalArgumentException("Invalid value for binlogRetentionPeriod. Must be less than or equal to 86400.");
    }
    
    
    this.binlogRetentionPeriod = binlogRetentionPeriod;
    return this;
  }

  public Mpr1 binlogRetentionPeriod(Integer binlogRetentionPeriod) {
    if (binlogRetentionPeriod != null && binlogRetentionPeriod < 600) {
      throw new IllegalArgumentException("Invalid value for binlogRetentionPeriod. Must be greater than or equal to 600.");
    }
    if (binlogRetentionPeriod != null && binlogRetentionPeriod > 86400) {
      throw new IllegalArgumentException("Invalid value for binlogRetentionPeriod. Must be less than or equal to 86400.");
    }
    
    
    this.binlogRetentionPeriod = binlogRetentionPeriod.doubleValue();
    return this;
  }

   /**
   * The minimum amount of time, in seconds, to keep binlog entries before deletion.  This may be extended for services that require binlog entries for longer than the default, for example if using the MySQL Debezium Kafka connector.
   * minimum: 600
   * maximum: 86400
   * @return binlogRetentionPeriod
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "600", value = "The minimum amount of time, in seconds, to keep binlog entries before deletion.  This may be extended for services that require binlog entries for longer than the default, for example if using the MySQL Debezium Kafka connector.")

  public Double getBinlogRetentionPeriod() {
    return binlogRetentionPeriod;
  }


  public void setBinlogRetentionPeriod(Double binlogRetentionPeriod) {
    if (binlogRetentionPeriod != null && binlogRetentionPeriod < 600) {
      throw new IllegalArgumentException("Invalid value for binlogRetentionPeriod. Must be greater than or equal to 600.");
    }
    if (binlogRetentionPeriod != null && binlogRetentionPeriod > 86400) {
      throw new IllegalArgumentException("Invalid value for binlogRetentionPeriod. Must be less than or equal to 86400.");
    }
    
    this.binlogRetentionPeriod = binlogRetentionPeriod;
  }


  public Mpr1 innodbChangeBufferMaxSize(Integer innodbChangeBufferMaxSize) {
    if (innodbChangeBufferMaxSize != null && innodbChangeBufferMaxSize < 0) {
      throw new IllegalArgumentException("Invalid value for innodbChangeBufferMaxSize. Must be greater than or equal to 0.");
    }
    if (innodbChangeBufferMaxSize != null && innodbChangeBufferMaxSize > 50) {
      throw new IllegalArgumentException("Invalid value for innodbChangeBufferMaxSize. Must be less than or equal to 50.");
    }
    
    
    this.innodbChangeBufferMaxSize = innodbChangeBufferMaxSize;
    return this;
  }

   /**
   * Specifies the maximum size of the InnoDB change buffer as a percentage of the buffer pool.
   * minimum: 0
   * maximum: 50
   * @return innodbChangeBufferMaxSize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "25", value = "Specifies the maximum size of the InnoDB change buffer as a percentage of the buffer pool.")

  public Integer getInnodbChangeBufferMaxSize() {
    return innodbChangeBufferMaxSize;
  }


  public void setInnodbChangeBufferMaxSize(Integer innodbChangeBufferMaxSize) {
    if (innodbChangeBufferMaxSize != null && innodbChangeBufferMaxSize < 0) {
      throw new IllegalArgumentException("Invalid value for innodbChangeBufferMaxSize. Must be greater than or equal to 0.");
    }
    if (innodbChangeBufferMaxSize != null && innodbChangeBufferMaxSize > 50) {
      throw new IllegalArgumentException("Invalid value for innodbChangeBufferMaxSize. Must be less than or equal to 50.");
    }
    
    this.innodbChangeBufferMaxSize = innodbChangeBufferMaxSize;
  }


  public Mpr1 innodbFlushNeighbors(InnodbFlushNeighborsEnum innodbFlushNeighbors) {
    
    
    
    
    this.innodbFlushNeighbors = innodbFlushNeighbors;
    return this;
  }

   /**
   * Specifies whether flushing a page from the InnoDB buffer pool also flushes other dirty pages in the same extent.   - 0 &amp;mdash; disables this functionality, dirty pages in the same extent are not flushed.   - 1 &amp;mdash; flushes contiguous dirty pages in the same extent.   - 2 &amp;mdash; flushes dirty pages in the same extent.
   * @return innodbFlushNeighbors
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "_0", value = "Specifies whether flushing a page from the InnoDB buffer pool also flushes other dirty pages in the same extent.   - 0 &mdash; disables this functionality, dirty pages in the same extent are not flushed.   - 1 &mdash; flushes contiguous dirty pages in the same extent.   - 2 &mdash; flushes dirty pages in the same extent.")

  public InnodbFlushNeighborsEnum getInnodbFlushNeighbors() {
    return innodbFlushNeighbors;
  }


  public void setInnodbFlushNeighbors(InnodbFlushNeighborsEnum innodbFlushNeighbors) {
    
    
    
    this.innodbFlushNeighbors = innodbFlushNeighbors;
  }


  public Mpr1 innodbReadIoThreads(Integer innodbReadIoThreads) {
    if (innodbReadIoThreads != null && innodbReadIoThreads < 1) {
      throw new IllegalArgumentException("Invalid value for innodbReadIoThreads. Must be greater than or equal to 1.");
    }
    if (innodbReadIoThreads != null && innodbReadIoThreads > 64) {
      throw new IllegalArgumentException("Invalid value for innodbReadIoThreads. Must be less than or equal to 64.");
    }
    
    
    this.innodbReadIoThreads = innodbReadIoThreads;
    return this;
  }

   /**
   * The number of I/O threads for read operations in InnoDB. Changing this parameter will lead to a restart of the MySQL service.
   * minimum: 1
   * maximum: 64
   * @return innodbReadIoThreads
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16", value = "The number of I/O threads for read operations in InnoDB. Changing this parameter will lead to a restart of the MySQL service.")

  public Integer getInnodbReadIoThreads() {
    return innodbReadIoThreads;
  }


  public void setInnodbReadIoThreads(Integer innodbReadIoThreads) {
    if (innodbReadIoThreads != null && innodbReadIoThreads < 1) {
      throw new IllegalArgumentException("Invalid value for innodbReadIoThreads. Must be greater than or equal to 1.");
    }
    if (innodbReadIoThreads != null && innodbReadIoThreads > 64) {
      throw new IllegalArgumentException("Invalid value for innodbReadIoThreads. Must be less than or equal to 64.");
    }
    
    this.innodbReadIoThreads = innodbReadIoThreads;
  }


  public Mpr1 innodbWriteIoThreads(Integer innodbWriteIoThreads) {
    if (innodbWriteIoThreads != null && innodbWriteIoThreads < 1) {
      throw new IllegalArgumentException("Invalid value for innodbWriteIoThreads. Must be greater than or equal to 1.");
    }
    if (innodbWriteIoThreads != null && innodbWriteIoThreads > 64) {
      throw new IllegalArgumentException("Invalid value for innodbWriteIoThreads. Must be less than or equal to 64.");
    }
    
    
    this.innodbWriteIoThreads = innodbWriteIoThreads;
    return this;
  }

   /**
   * The number of I/O threads for write operations in InnoDB. Changing this parameter will lead to a restart of the MySQL service.
   * minimum: 1
   * maximum: 64
   * @return innodbWriteIoThreads
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16", value = "The number of I/O threads for write operations in InnoDB. Changing this parameter will lead to a restart of the MySQL service.")

  public Integer getInnodbWriteIoThreads() {
    return innodbWriteIoThreads;
  }


  public void setInnodbWriteIoThreads(Integer innodbWriteIoThreads) {
    if (innodbWriteIoThreads != null && innodbWriteIoThreads < 1) {
      throw new IllegalArgumentException("Invalid value for innodbWriteIoThreads. Must be greater than or equal to 1.");
    }
    if (innodbWriteIoThreads != null && innodbWriteIoThreads > 64) {
      throw new IllegalArgumentException("Invalid value for innodbWriteIoThreads. Must be less than or equal to 64.");
    }
    
    this.innodbWriteIoThreads = innodbWriteIoThreads;
  }


  public Mpr1 innodbThreadConcurrency(Integer innodbThreadConcurrency) {
    if (innodbThreadConcurrency != null && innodbThreadConcurrency < 0) {
      throw new IllegalArgumentException("Invalid value for innodbThreadConcurrency. Must be greater than or equal to 0.");
    }
    if (innodbThreadConcurrency != null && innodbThreadConcurrency > 1000) {
      throw new IllegalArgumentException("Invalid value for innodbThreadConcurrency. Must be less than or equal to 1000.");
    }
    
    
    this.innodbThreadConcurrency = innodbThreadConcurrency;
    return this;
  }

   /**
   * Defines the maximum number of threads permitted inside of InnoDB. A value of 0 (the default) is interpreted as infinite concurrency (no limit). This variable is intended for performance  tuning on high concurrency systems.
   * minimum: 0
   * maximum: 1000
   * @return innodbThreadConcurrency
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "0", value = "Defines the maximum number of threads permitted inside of InnoDB. A value of 0 (the default) is interpreted as infinite concurrency (no limit). This variable is intended for performance  tuning on high concurrency systems.")

  public Integer getInnodbThreadConcurrency() {
    return innodbThreadConcurrency;
  }


  public void setInnodbThreadConcurrency(Integer innodbThreadConcurrency) {
    if (innodbThreadConcurrency != null && innodbThreadConcurrency < 0) {
      throw new IllegalArgumentException("Invalid value for innodbThreadConcurrency. Must be greater than or equal to 0.");
    }
    if (innodbThreadConcurrency != null && innodbThreadConcurrency > 1000) {
      throw new IllegalArgumentException("Invalid value for innodbThreadConcurrency. Must be less than or equal to 1000.");
    }
    
    this.innodbThreadConcurrency = innodbThreadConcurrency;
  }


  public Mpr1 netBufferLength(Integer netBufferLength) {
    if (netBufferLength != null && netBufferLength < 1024) {
      throw new IllegalArgumentException("Invalid value for netBufferLength. Must be greater than or equal to 1024.");
    }
    if (netBufferLength != null && netBufferLength > 1048576) {
      throw new IllegalArgumentException("Invalid value for netBufferLength. Must be less than or equal to 1048576.");
    }
    
    
    this.netBufferLength = netBufferLength;
    return this;
  }

   /**
   * Start sizes of connection buffer and result buffer, must be multiple of 1024. Changing this parameter will lead to a restart of the MySQL service.
   * minimum: 1024
   * maximum: 1048576
   * @return netBufferLength
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "4096", value = "Start sizes of connection buffer and result buffer, must be multiple of 1024. Changing this parameter will lead to a restart of the MySQL service.")

  public Integer getNetBufferLength() {
    return netBufferLength;
  }


  public void setNetBufferLength(Integer netBufferLength) {
    if (netBufferLength != null && netBufferLength < 1024) {
      throw new IllegalArgumentException("Invalid value for netBufferLength. Must be greater than or equal to 1024.");
    }
    if (netBufferLength != null && netBufferLength > 1048576) {
      throw new IllegalArgumentException("Invalid value for netBufferLength. Must be less than or equal to 1048576.");
    }
    
    this.netBufferLength = netBufferLength;
  }


  public Mpr1 autovacuumFreezeMaxAge(Integer autovacuumFreezeMaxAge) {
    if (autovacuumFreezeMaxAge != null && autovacuumFreezeMaxAge < 200000000) {
      throw new IllegalArgumentException("Invalid value for autovacuumFreezeMaxAge. Must be greater than or equal to 200000000.");
    }
    if (autovacuumFreezeMaxAge != null && autovacuumFreezeMaxAge > 1500000000) {
      throw new IllegalArgumentException("Invalid value for autovacuumFreezeMaxAge. Must be less than or equal to 1500000000.");
    }
    
    
    this.autovacuumFreezeMaxAge = autovacuumFreezeMaxAge;
    return this;
  }

   /**
   * Specifies the maximum age (in transactions) that a table&#39;s pg_class.relfrozenxid field can attain before a VACUUM operation is forced to prevent transaction ID wraparound within the table. Note that the system will launch autovacuum processes to prevent wraparound even when autovacuum is otherwise disabled. This parameter will cause the server to be restarted.
   * minimum: 200000000
   * maximum: 1500000000
   * @return autovacuumFreezeMaxAge
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "200000000", value = "Specifies the maximum age (in transactions) that a table's pg_class.relfrozenxid field can attain before a VACUUM operation is forced to prevent transaction ID wraparound within the table. Note that the system will launch autovacuum processes to prevent wraparound even when autovacuum is otherwise disabled. This parameter will cause the server to be restarted.")

  public Integer getAutovacuumFreezeMaxAge() {
    return autovacuumFreezeMaxAge;
  }


  public void setAutovacuumFreezeMaxAge(Integer autovacuumFreezeMaxAge) {
    if (autovacuumFreezeMaxAge != null && autovacuumFreezeMaxAge < 200000000) {
      throw new IllegalArgumentException("Invalid value for autovacuumFreezeMaxAge. Must be greater than or equal to 200000000.");
    }
    if (autovacuumFreezeMaxAge != null && autovacuumFreezeMaxAge > 1500000000) {
      throw new IllegalArgumentException("Invalid value for autovacuumFreezeMaxAge. Must be less than or equal to 1500000000.");
    }
    
    this.autovacuumFreezeMaxAge = autovacuumFreezeMaxAge;
  }


  public Mpr1 autovacuumMaxWorkers(Integer autovacuumMaxWorkers) {
    if (autovacuumMaxWorkers != null && autovacuumMaxWorkers < 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumMaxWorkers. Must be greater than or equal to 1.");
    }
    if (autovacuumMaxWorkers != null && autovacuumMaxWorkers > 20) {
      throw new IllegalArgumentException("Invalid value for autovacuumMaxWorkers. Must be less than or equal to 20.");
    }
    
    
    this.autovacuumMaxWorkers = autovacuumMaxWorkers;
    return this;
  }

   /**
   * Specifies the maximum number of autovacuum processes (other than the autovacuum launcher) that may be running at any one time. The default is three. This parameter can only be set at server start.
   * minimum: 1
   * maximum: 20
   * @return autovacuumMaxWorkers
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "5", value = "Specifies the maximum number of autovacuum processes (other than the autovacuum launcher) that may be running at any one time. The default is three. This parameter can only be set at server start.")

  public Integer getAutovacuumMaxWorkers() {
    return autovacuumMaxWorkers;
  }


  public void setAutovacuumMaxWorkers(Integer autovacuumMaxWorkers) {
    if (autovacuumMaxWorkers != null && autovacuumMaxWorkers < 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumMaxWorkers. Must be greater than or equal to 1.");
    }
    if (autovacuumMaxWorkers != null && autovacuumMaxWorkers > 20) {
      throw new IllegalArgumentException("Invalid value for autovacuumMaxWorkers. Must be less than or equal to 20.");
    }
    
    this.autovacuumMaxWorkers = autovacuumMaxWorkers;
  }


  public Mpr1 autovacuumNaptime(Integer autovacuumNaptime) {
    if (autovacuumNaptime != null && autovacuumNaptime < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumNaptime. Must be greater than or equal to 0.");
    }
    if (autovacuumNaptime != null && autovacuumNaptime > 86400) {
      throw new IllegalArgumentException("Invalid value for autovacuumNaptime. Must be less than or equal to 86400.");
    }
    
    
    this.autovacuumNaptime = autovacuumNaptime;
    return this;
  }

   /**
   * Specifies the minimum delay, in seconds, between autovacuum runs on any given database. The default is one minute.
   * minimum: 0
   * maximum: 86400
   * @return autovacuumNaptime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "43200", value = "Specifies the minimum delay, in seconds, between autovacuum runs on any given database. The default is one minute.")

  public Integer getAutovacuumNaptime() {
    return autovacuumNaptime;
  }


  public void setAutovacuumNaptime(Integer autovacuumNaptime) {
    if (autovacuumNaptime != null && autovacuumNaptime < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumNaptime. Must be greater than or equal to 0.");
    }
    if (autovacuumNaptime != null && autovacuumNaptime > 86400) {
      throw new IllegalArgumentException("Invalid value for autovacuumNaptime. Must be less than or equal to 86400.");
    }
    
    this.autovacuumNaptime = autovacuumNaptime;
  }


  public Mpr1 autovacuumVacuumThreshold(Integer autovacuumVacuumThreshold) {
    if (autovacuumVacuumThreshold != null && autovacuumVacuumThreshold < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumThreshold. Must be greater than or equal to 0.");
    }
    if (autovacuumVacuumThreshold != null && autovacuumVacuumThreshold > 2147483647) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumThreshold. Must be less than or equal to 2147483647.");
    }
    
    
    this.autovacuumVacuumThreshold = autovacuumVacuumThreshold;
    return this;
  }

   /**
   * Specifies the minimum number of updated or deleted tuples needed to trigger a VACUUM in any one table. The default is 50 tuples.
   * minimum: 0
   * maximum: 2147483647
   * @return autovacuumVacuumThreshold
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "50", value = "Specifies the minimum number of updated or deleted tuples needed to trigger a VACUUM in any one table. The default is 50 tuples.")

  public Integer getAutovacuumVacuumThreshold() {
    return autovacuumVacuumThreshold;
  }


  public void setAutovacuumVacuumThreshold(Integer autovacuumVacuumThreshold) {
    if (autovacuumVacuumThreshold != null && autovacuumVacuumThreshold < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumThreshold. Must be greater than or equal to 0.");
    }
    if (autovacuumVacuumThreshold != null && autovacuumVacuumThreshold > 2147483647) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumThreshold. Must be less than or equal to 2147483647.");
    }
    
    this.autovacuumVacuumThreshold = autovacuumVacuumThreshold;
  }


  public Mpr1 autovacuumAnalyzeThreshold(Integer autovacuumAnalyzeThreshold) {
    if (autovacuumAnalyzeThreshold != null && autovacuumAnalyzeThreshold < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeThreshold. Must be greater than or equal to 0.");
    }
    if (autovacuumAnalyzeThreshold != null && autovacuumAnalyzeThreshold > 2147483647) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeThreshold. Must be less than or equal to 2147483647.");
    }
    
    
    this.autovacuumAnalyzeThreshold = autovacuumAnalyzeThreshold;
    return this;
  }

   /**
   * Specifies the minimum number of inserted, updated, or deleted tuples needed to trigger an ANALYZE in any one table. The default is 50 tuples.
   * minimum: 0
   * maximum: 2147483647
   * @return autovacuumAnalyzeThreshold
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "50", value = "Specifies the minimum number of inserted, updated, or deleted tuples needed to trigger an ANALYZE in any one table. The default is 50 tuples.")

  public Integer getAutovacuumAnalyzeThreshold() {
    return autovacuumAnalyzeThreshold;
  }


  public void setAutovacuumAnalyzeThreshold(Integer autovacuumAnalyzeThreshold) {
    if (autovacuumAnalyzeThreshold != null && autovacuumAnalyzeThreshold < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeThreshold. Must be greater than or equal to 0.");
    }
    if (autovacuumAnalyzeThreshold != null && autovacuumAnalyzeThreshold > 2147483647) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeThreshold. Must be less than or equal to 2147483647.");
    }
    
    this.autovacuumAnalyzeThreshold = autovacuumAnalyzeThreshold;
  }


  public Mpr1 autovacuumVacuumScaleFactor(Double autovacuumVacuumScaleFactor) {
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be less than or equal to 1.");
    }
    
    
    this.autovacuumVacuumScaleFactor = autovacuumVacuumScaleFactor;
    return this;
  }

  public Mpr1 autovacuumVacuumScaleFactor(Integer autovacuumVacuumScaleFactor) {
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be less than or equal to 1.");
    }
    
    
    this.autovacuumVacuumScaleFactor = autovacuumVacuumScaleFactor.doubleValue();
    return this;
  }

   /**
   * Specifies a fraction, in a decimal value, of the table size to add to autovacuum_vacuum_threshold when deciding whether to trigger a VACUUM. The default is 0.2 (20% of table size).
   * minimum: 0
   * maximum: 1
   * @return autovacuumVacuumScaleFactor
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "0.2", value = "Specifies a fraction, in a decimal value, of the table size to add to autovacuum_vacuum_threshold when deciding whether to trigger a VACUUM. The default is 0.2 (20% of table size).")

  public Double getAutovacuumVacuumScaleFactor() {
    return autovacuumVacuumScaleFactor;
  }


  public void setAutovacuumVacuumScaleFactor(Double autovacuumVacuumScaleFactor) {
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be less than or equal to 1.");
    }
    
    this.autovacuumVacuumScaleFactor = autovacuumVacuumScaleFactor;
  }


  public Mpr1 autovacuumAnalyzeScaleFactor(Double autovacuumAnalyzeScaleFactor) {
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be less than or equal to 1.");
    }
    
    
    this.autovacuumAnalyzeScaleFactor = autovacuumAnalyzeScaleFactor;
    return this;
  }

  public Mpr1 autovacuumAnalyzeScaleFactor(Integer autovacuumAnalyzeScaleFactor) {
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be less than or equal to 1.");
    }
    
    
    this.autovacuumAnalyzeScaleFactor = autovacuumAnalyzeScaleFactor.doubleValue();
    return this;
  }

   /**
   * Specifies a fraction, in a decimal value, of the table size to add to autovacuum_analyze_threshold when deciding whether to trigger an ANALYZE. The default is 0.2 (20% of table size).
   * minimum: 0
   * maximum: 1
   * @return autovacuumAnalyzeScaleFactor
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "0.2", value = "Specifies a fraction, in a decimal value, of the table size to add to autovacuum_analyze_threshold when deciding whether to trigger an ANALYZE. The default is 0.2 (20% of table size).")

  public Double getAutovacuumAnalyzeScaleFactor() {
    return autovacuumAnalyzeScaleFactor;
  }


  public void setAutovacuumAnalyzeScaleFactor(Double autovacuumAnalyzeScaleFactor) {
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be less than or equal to 1.");
    }
    
    this.autovacuumAnalyzeScaleFactor = autovacuumAnalyzeScaleFactor;
  }


  public Mpr1 autovacuumVacuumCostDelay(Integer autovacuumVacuumCostDelay) {
    if (autovacuumVacuumCostDelay != null && autovacuumVacuumCostDelay < -1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostDelay. Must be greater than or equal to -1.");
    }
    if (autovacuumVacuumCostDelay != null && autovacuumVacuumCostDelay > 100) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostDelay. Must be less than or equal to 100.");
    }
    
    
    this.autovacuumVacuumCostDelay = autovacuumVacuumCostDelay;
    return this;
  }

   /**
   * Specifies the cost delay value, in milliseconds, that will be used in automatic VACUUM operations. If -1, uses the regular vacuum_cost_delay value, which is 20 milliseconds.
   * minimum: -1
   * maximum: 100
   * @return autovacuumVacuumCostDelay
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "20", value = "Specifies the cost delay value, in milliseconds, that will be used in automatic VACUUM operations. If -1, uses the regular vacuum_cost_delay value, which is 20 milliseconds.")

  public Integer getAutovacuumVacuumCostDelay() {
    return autovacuumVacuumCostDelay;
  }


  public void setAutovacuumVacuumCostDelay(Integer autovacuumVacuumCostDelay) {
    if (autovacuumVacuumCostDelay != null && autovacuumVacuumCostDelay < -1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostDelay. Must be greater than or equal to -1.");
    }
    if (autovacuumVacuumCostDelay != null && autovacuumVacuumCostDelay > 100) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostDelay. Must be less than or equal to 100.");
    }
    
    this.autovacuumVacuumCostDelay = autovacuumVacuumCostDelay;
  }


  public Mpr1 autovacuumVacuumCostLimit(Integer autovacuumVacuumCostLimit) {
    if (autovacuumVacuumCostLimit != null && autovacuumVacuumCostLimit < -1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostLimit. Must be greater than or equal to -1.");
    }
    if (autovacuumVacuumCostLimit != null && autovacuumVacuumCostLimit > 10000) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostLimit. Must be less than or equal to 10000.");
    }
    
    
    this.autovacuumVacuumCostLimit = autovacuumVacuumCostLimit;
    return this;
  }

   /**
   * Specifies the cost limit value that will be used in automatic VACUUM operations. If -1 is specified (which is the default), the regular vacuum_cost_limit value will be used.
   * minimum: -1
   * maximum: 10000
   * @return autovacuumVacuumCostLimit
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "-1", value = "Specifies the cost limit value that will be used in automatic VACUUM operations. If -1 is specified (which is the default), the regular vacuum_cost_limit value will be used.")

  public Integer getAutovacuumVacuumCostLimit() {
    return autovacuumVacuumCostLimit;
  }


  public void setAutovacuumVacuumCostLimit(Integer autovacuumVacuumCostLimit) {
    if (autovacuumVacuumCostLimit != null && autovacuumVacuumCostLimit < -1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostLimit. Must be greater than or equal to -1.");
    }
    if (autovacuumVacuumCostLimit != null && autovacuumVacuumCostLimit > 10000) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumCostLimit. Must be less than or equal to 10000.");
    }
    
    this.autovacuumVacuumCostLimit = autovacuumVacuumCostLimit;
  }


  public Mpr1 bgwriterDelay(Integer bgwriterDelay) {
    if (bgwriterDelay != null && bgwriterDelay < 10) {
      throw new IllegalArgumentException("Invalid value for bgwriterDelay. Must be greater than or equal to 10.");
    }
    if (bgwriterDelay != null && bgwriterDelay > 10000) {
      throw new IllegalArgumentException("Invalid value for bgwriterDelay. Must be less than or equal to 10000.");
    }
    
    
    this.bgwriterDelay = bgwriterDelay;
    return this;
  }

   /**
   * Specifies the delay, in milliseconds, between activity rounds for the background writer. Default is 200 ms.
   * minimum: 10
   * maximum: 10000
   * @return bgwriterDelay
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "200", value = "Specifies the delay, in milliseconds, between activity rounds for the background writer. Default is 200 ms.")

  public Integer getBgwriterDelay() {
    return bgwriterDelay;
  }


  public void setBgwriterDelay(Integer bgwriterDelay) {
    if (bgwriterDelay != null && bgwriterDelay < 10) {
      throw new IllegalArgumentException("Invalid value for bgwriterDelay. Must be greater than or equal to 10.");
    }
    if (bgwriterDelay != null && bgwriterDelay > 10000) {
      throw new IllegalArgumentException("Invalid value for bgwriterDelay. Must be less than or equal to 10000.");
    }
    
    this.bgwriterDelay = bgwriterDelay;
  }


  public Mpr1 bgwriterFlushAfter(Integer bgwriterFlushAfter) {
    if (bgwriterFlushAfter != null && bgwriterFlushAfter < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterFlushAfter. Must be greater than or equal to 0.");
    }
    if (bgwriterFlushAfter != null && bgwriterFlushAfter > 2048) {
      throw new IllegalArgumentException("Invalid value for bgwriterFlushAfter. Must be less than or equal to 2048.");
    }
    
    
    this.bgwriterFlushAfter = bgwriterFlushAfter;
    return this;
  }

   /**
   * The amount of kilobytes that need to be written by the background writer before attempting to force the OS to issue these writes to underlying storage. Specified in kilobytes, default is 512.  Setting of 0 disables forced writeback.
   * minimum: 0
   * maximum: 2048
   * @return bgwriterFlushAfter
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "512", value = "The amount of kilobytes that need to be written by the background writer before attempting to force the OS to issue these writes to underlying storage. Specified in kilobytes, default is 512.  Setting of 0 disables forced writeback.")

  public Integer getBgwriterFlushAfter() {
    return bgwriterFlushAfter;
  }


  public void setBgwriterFlushAfter(Integer bgwriterFlushAfter) {
    if (bgwriterFlushAfter != null && bgwriterFlushAfter < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterFlushAfter. Must be greater than or equal to 0.");
    }
    if (bgwriterFlushAfter != null && bgwriterFlushAfter > 2048) {
      throw new IllegalArgumentException("Invalid value for bgwriterFlushAfter. Must be less than or equal to 2048.");
    }
    
    this.bgwriterFlushAfter = bgwriterFlushAfter;
  }


  public Mpr1 bgwriterLruMaxpages(Integer bgwriterLruMaxpages) {
    if (bgwriterLruMaxpages != null && bgwriterLruMaxpages < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMaxpages. Must be greater than or equal to 0.");
    }
    if (bgwriterLruMaxpages != null && bgwriterLruMaxpages > 1073741823) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMaxpages. Must be less than or equal to 1073741823.");
    }
    
    
    this.bgwriterLruMaxpages = bgwriterLruMaxpages;
    return this;
  }

   /**
   * The maximum number of buffers that the background writer can write. Setting this to zero disables background writing. Default is 100.
   * minimum: 0
   * maximum: 1073741823
   * @return bgwriterLruMaxpages
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "100", value = "The maximum number of buffers that the background writer can write. Setting this to zero disables background writing. Default is 100.")

  public Integer getBgwriterLruMaxpages() {
    return bgwriterLruMaxpages;
  }


  public void setBgwriterLruMaxpages(Integer bgwriterLruMaxpages) {
    if (bgwriterLruMaxpages != null && bgwriterLruMaxpages < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMaxpages. Must be greater than or equal to 0.");
    }
    if (bgwriterLruMaxpages != null && bgwriterLruMaxpages > 1073741823) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMaxpages. Must be less than or equal to 1073741823.");
    }
    
    this.bgwriterLruMaxpages = bgwriterLruMaxpages;
  }


  public Mpr1 bgwriterLruMultiplier(Double bgwriterLruMultiplier) {
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be greater than or equal to 0.");
    }
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier > 10) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be less than or equal to 10.");
    }
    
    
    this.bgwriterLruMultiplier = bgwriterLruMultiplier;
    return this;
  }

  public Mpr1 bgwriterLruMultiplier(Integer bgwriterLruMultiplier) {
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be greater than or equal to 0.");
    }
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier > 10) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be less than or equal to 10.");
    }
    
    
    this.bgwriterLruMultiplier = bgwriterLruMultiplier.doubleValue();
    return this;
  }

   /**
   * The average recent need for new buffers is multiplied by bgwriter_lru_multiplier to arrive at an estimate of the number that will be needed during the next round, (up to bgwriter_lru_maxpages). 1.0 represents a “just in time” policy of writing exactly the number of buffers predicted to be needed. Larger values provide some cushion against spikes in demand, while smaller values intentionally leave writes to be done by server processes. The default is 2.0.
   * minimum: 0
   * maximum: 10
   * @return bgwriterLruMultiplier
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2", value = "The average recent need for new buffers is multiplied by bgwriter_lru_multiplier to arrive at an estimate of the number that will be needed during the next round, (up to bgwriter_lru_maxpages). 1.0 represents a “just in time” policy of writing exactly the number of buffers predicted to be needed. Larger values provide some cushion against spikes in demand, while smaller values intentionally leave writes to be done by server processes. The default is 2.0.")

  public Double getBgwriterLruMultiplier() {
    return bgwriterLruMultiplier;
  }


  public void setBgwriterLruMultiplier(Double bgwriterLruMultiplier) {
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be greater than or equal to 0.");
    }
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier > 10) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be less than or equal to 10.");
    }
    
    this.bgwriterLruMultiplier = bgwriterLruMultiplier;
  }


  public Mpr1 deadlockTimeout(Integer deadlockTimeout) {
    if (deadlockTimeout != null && deadlockTimeout < 500) {
      throw new IllegalArgumentException("Invalid value for deadlockTimeout. Must be greater than or equal to 500.");
    }
    if (deadlockTimeout != null && deadlockTimeout > 1800000) {
      throw new IllegalArgumentException("Invalid value for deadlockTimeout. Must be less than or equal to 1800000.");
    }
    
    
    this.deadlockTimeout = deadlockTimeout;
    return this;
  }

   /**
   * The amount of time, in milliseconds, to wait on a lock before checking to see if there is a deadlock condition.
   * minimum: 500
   * maximum: 1800000
   * @return deadlockTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1000", value = "The amount of time, in milliseconds, to wait on a lock before checking to see if there is a deadlock condition.")

  public Integer getDeadlockTimeout() {
    return deadlockTimeout;
  }


  public void setDeadlockTimeout(Integer deadlockTimeout) {
    if (deadlockTimeout != null && deadlockTimeout < 500) {
      throw new IllegalArgumentException("Invalid value for deadlockTimeout. Must be greater than or equal to 500.");
    }
    if (deadlockTimeout != null && deadlockTimeout > 1800000) {
      throw new IllegalArgumentException("Invalid value for deadlockTimeout. Must be less than or equal to 1800000.");
    }
    
    this.deadlockTimeout = deadlockTimeout;
  }


  public Mpr1 defaultToastCompression(DefaultToastCompressionEnum defaultToastCompression) {
    
    
    
    
    this.defaultToastCompression = defaultToastCompression;
    return this;
  }

   /**
   * Specifies the default TOAST compression method for values of compressible columns (the default is lz4).
   * @return defaultToastCompression
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "LZ4", value = "Specifies the default TOAST compression method for values of compressible columns (the default is lz4).")

  public DefaultToastCompressionEnum getDefaultToastCompression() {
    return defaultToastCompression;
  }


  public void setDefaultToastCompression(DefaultToastCompressionEnum defaultToastCompression) {
    
    
    
    this.defaultToastCompression = defaultToastCompression;
  }


  public Mpr1 idleInTransactionSessionTimeout(Integer idleInTransactionSessionTimeout) {
    if (idleInTransactionSessionTimeout != null && idleInTransactionSessionTimeout < 0) {
      throw new IllegalArgumentException("Invalid value for idleInTransactionSessionTimeout. Must be greater than or equal to 0.");
    }
    if (idleInTransactionSessionTimeout != null && idleInTransactionSessionTimeout > 604800000) {
      throw new IllegalArgumentException("Invalid value for idleInTransactionSessionTimeout. Must be less than or equal to 604800000.");
    }
    
    
    this.idleInTransactionSessionTimeout = idleInTransactionSessionTimeout;
    return this;
  }

   /**
   * Time out sessions with open transactions after this number of milliseconds
   * minimum: 0
   * maximum: 604800000
   * @return idleInTransactionSessionTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10000", value = "Time out sessions with open transactions after this number of milliseconds")

  public Integer getIdleInTransactionSessionTimeout() {
    return idleInTransactionSessionTimeout;
  }


  public void setIdleInTransactionSessionTimeout(Integer idleInTransactionSessionTimeout) {
    if (idleInTransactionSessionTimeout != null && idleInTransactionSessionTimeout < 0) {
      throw new IllegalArgumentException("Invalid value for idleInTransactionSessionTimeout. Must be greater than or equal to 0.");
    }
    if (idleInTransactionSessionTimeout != null && idleInTransactionSessionTimeout > 604800000) {
      throw new IllegalArgumentException("Invalid value for idleInTransactionSessionTimeout. Must be less than or equal to 604800000.");
    }
    
    this.idleInTransactionSessionTimeout = idleInTransactionSessionTimeout;
  }


  public Mpr1 jit(Boolean jit) {
    
    
    
    
    this.jit = jit;
    return this;
  }

   /**
   * Activates, in a boolean, the system-wide use of Just-in-Time Compilation (JIT).
   * @return jit
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "Activates, in a boolean, the system-wide use of Just-in-Time Compilation (JIT).")

  public Boolean getJit() {
    return jit;
  }


  public void setJit(Boolean jit) {
    
    
    
    this.jit = jit;
  }


  public Mpr1 logAutovacuumMinDuration(Integer logAutovacuumMinDuration) {
    if (logAutovacuumMinDuration != null && logAutovacuumMinDuration < -1) {
      throw new IllegalArgumentException("Invalid value for logAutovacuumMinDuration. Must be greater than or equal to -1.");
    }
    if (logAutovacuumMinDuration != null && logAutovacuumMinDuration > 2147483647) {
      throw new IllegalArgumentException("Invalid value for logAutovacuumMinDuration. Must be less than or equal to 2147483647.");
    }
    
    
    this.logAutovacuumMinDuration = logAutovacuumMinDuration;
    return this;
  }

   /**
   * Causes each action executed by autovacuum to be logged if it ran for at least the specified number of milliseconds. Setting this to zero logs all autovacuum actions. Minus-one (the default) disables logging autovacuum actions.
   * minimum: -1
   * maximum: 2147483647
   * @return logAutovacuumMinDuration
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "-1", value = "Causes each action executed by autovacuum to be logged if it ran for at least the specified number of milliseconds. Setting this to zero logs all autovacuum actions. Minus-one (the default) disables logging autovacuum actions.")

  public Integer getLogAutovacuumMinDuration() {
    return logAutovacuumMinDuration;
  }


  public void setLogAutovacuumMinDuration(Integer logAutovacuumMinDuration) {
    if (logAutovacuumMinDuration != null && logAutovacuumMinDuration < -1) {
      throw new IllegalArgumentException("Invalid value for logAutovacuumMinDuration. Must be greater than or equal to -1.");
    }
    if (logAutovacuumMinDuration != null && logAutovacuumMinDuration > 2147483647) {
      throw new IllegalArgumentException("Invalid value for logAutovacuumMinDuration. Must be less than or equal to 2147483647.");
    }
    
    this.logAutovacuumMinDuration = logAutovacuumMinDuration;
  }


  public Mpr1 logErrorVerbosity(LogErrorVerbosityEnum logErrorVerbosity) {
    
    
    
    
    this.logErrorVerbosity = logErrorVerbosity;
    return this;
  }

   /**
   * Controls the amount of detail written in the server log for each message that is logged.
   * @return logErrorVerbosity
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "VERBOSE", value = "Controls the amount of detail written in the server log for each message that is logged.")

  public LogErrorVerbosityEnum getLogErrorVerbosity() {
    return logErrorVerbosity;
  }


  public void setLogErrorVerbosity(LogErrorVerbosityEnum logErrorVerbosity) {
    
    
    
    this.logErrorVerbosity = logErrorVerbosity;
  }


  public Mpr1 logLinePrefix(LogLinePrefixEnum logLinePrefix) {
    
    
    
    
    this.logLinePrefix = logLinePrefix;
    return this;
  }

   /**
   * Selects one of the available log-formats. These can support popular log analyzers like pgbadger, pganalyze, etc.
   * @return logLinePrefix
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "PID_P_USER_U_DB_D_APP_A_CLIENT_H", value = "Selects one of the available log-formats. These can support popular log analyzers like pgbadger, pganalyze, etc.")

  public LogLinePrefixEnum getLogLinePrefix() {
    return logLinePrefix;
  }


  public void setLogLinePrefix(LogLinePrefixEnum logLinePrefix) {
    
    
    
    this.logLinePrefix = logLinePrefix;
  }


  public Mpr1 logMinDurationStatement(Integer logMinDurationStatement) {
    if (logMinDurationStatement != null && logMinDurationStatement < -1) {
      throw new IllegalArgumentException("Invalid value for logMinDurationStatement. Must be greater than or equal to -1.");
    }
    if (logMinDurationStatement != null && logMinDurationStatement > 86400000) {
      throw new IllegalArgumentException("Invalid value for logMinDurationStatement. Must be less than or equal to 86400000.");
    }
    
    
    this.logMinDurationStatement = logMinDurationStatement;
    return this;
  }

   /**
   * Log statements that take more than this number of milliseconds to run. If -1, disables.
   * minimum: -1
   * maximum: 86400000
   * @return logMinDurationStatement
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "-1", value = "Log statements that take more than this number of milliseconds to run. If -1, disables.")

  public Integer getLogMinDurationStatement() {
    return logMinDurationStatement;
  }


  public void setLogMinDurationStatement(Integer logMinDurationStatement) {
    if (logMinDurationStatement != null && logMinDurationStatement < -1) {
      throw new IllegalArgumentException("Invalid value for logMinDurationStatement. Must be greater than or equal to -1.");
    }
    if (logMinDurationStatement != null && logMinDurationStatement > 86400000) {
      throw new IllegalArgumentException("Invalid value for logMinDurationStatement. Must be less than or equal to 86400000.");
    }
    
    this.logMinDurationStatement = logMinDurationStatement;
  }


  public Mpr1 maxFilesPerProcess(Integer maxFilesPerProcess) {
    if (maxFilesPerProcess != null && maxFilesPerProcess < 1000) {
      throw new IllegalArgumentException("Invalid value for maxFilesPerProcess. Must be greater than or equal to 1000.");
    }
    if (maxFilesPerProcess != null && maxFilesPerProcess > 4096) {
      throw new IllegalArgumentException("Invalid value for maxFilesPerProcess. Must be less than or equal to 4096.");
    }
    
    
    this.maxFilesPerProcess = maxFilesPerProcess;
    return this;
  }

   /**
   * PostgreSQL maximum number of files that can be open per process.
   * minimum: 1000
   * maximum: 4096
   * @return maxFilesPerProcess
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2048", value = "PostgreSQL maximum number of files that can be open per process.")

  public Integer getMaxFilesPerProcess() {
    return maxFilesPerProcess;
  }


  public void setMaxFilesPerProcess(Integer maxFilesPerProcess) {
    if (maxFilesPerProcess != null && maxFilesPerProcess < 1000) {
      throw new IllegalArgumentException("Invalid value for maxFilesPerProcess. Must be greater than or equal to 1000.");
    }
    if (maxFilesPerProcess != null && maxFilesPerProcess > 4096) {
      throw new IllegalArgumentException("Invalid value for maxFilesPerProcess. Must be less than or equal to 4096.");
    }
    
    this.maxFilesPerProcess = maxFilesPerProcess;
  }


  public Mpr1 maxPreparedTransactions(Integer maxPreparedTransactions) {
    if (maxPreparedTransactions != null && maxPreparedTransactions < 0) {
      throw new IllegalArgumentException("Invalid value for maxPreparedTransactions. Must be greater than or equal to 0.");
    }
    if (maxPreparedTransactions != null && maxPreparedTransactions > 10000) {
      throw new IllegalArgumentException("Invalid value for maxPreparedTransactions. Must be less than or equal to 10000.");
    }
    
    
    this.maxPreparedTransactions = maxPreparedTransactions;
    return this;
  }

   /**
   * PostgreSQL maximum prepared transactions. Once increased, this parameter cannot be lowered from its set value.
   * minimum: 0
   * maximum: 10000
   * @return maxPreparedTransactions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "20", value = "PostgreSQL maximum prepared transactions. Once increased, this parameter cannot be lowered from its set value.")

  public Integer getMaxPreparedTransactions() {
    return maxPreparedTransactions;
  }


  public void setMaxPreparedTransactions(Integer maxPreparedTransactions) {
    if (maxPreparedTransactions != null && maxPreparedTransactions < 0) {
      throw new IllegalArgumentException("Invalid value for maxPreparedTransactions. Must be greater than or equal to 0.");
    }
    if (maxPreparedTransactions != null && maxPreparedTransactions > 10000) {
      throw new IllegalArgumentException("Invalid value for maxPreparedTransactions. Must be less than or equal to 10000.");
    }
    
    this.maxPreparedTransactions = maxPreparedTransactions;
  }


  public Mpr1 maxPredLocksPerTransaction(Integer maxPredLocksPerTransaction) {
    if (maxPredLocksPerTransaction != null && maxPredLocksPerTransaction < 64) {
      throw new IllegalArgumentException("Invalid value for maxPredLocksPerTransaction. Must be greater than or equal to 64.");
    }
    if (maxPredLocksPerTransaction != null && maxPredLocksPerTransaction > 640) {
      throw new IllegalArgumentException("Invalid value for maxPredLocksPerTransaction. Must be less than or equal to 640.");
    }
    
    
    this.maxPredLocksPerTransaction = maxPredLocksPerTransaction;
    return this;
  }

   /**
   * PostgreSQL maximum predicate locks per transaction.
   * minimum: 64
   * maximum: 640
   * @return maxPredLocksPerTransaction
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "128", value = "PostgreSQL maximum predicate locks per transaction.")

  public Integer getMaxPredLocksPerTransaction() {
    return maxPredLocksPerTransaction;
  }


  public void setMaxPredLocksPerTransaction(Integer maxPredLocksPerTransaction) {
    if (maxPredLocksPerTransaction != null && maxPredLocksPerTransaction < 64) {
      throw new IllegalArgumentException("Invalid value for maxPredLocksPerTransaction. Must be greater than or equal to 64.");
    }
    if (maxPredLocksPerTransaction != null && maxPredLocksPerTransaction > 640) {
      throw new IllegalArgumentException("Invalid value for maxPredLocksPerTransaction. Must be less than or equal to 640.");
    }
    
    this.maxPredLocksPerTransaction = maxPredLocksPerTransaction;
  }


  public Mpr1 maxLocksPerTransaction(Integer maxLocksPerTransaction) {
    if (maxLocksPerTransaction != null && maxLocksPerTransaction < 64) {
      throw new IllegalArgumentException("Invalid value for maxLocksPerTransaction. Must be greater than or equal to 64.");
    }
    if (maxLocksPerTransaction != null && maxLocksPerTransaction > 6400) {
      throw new IllegalArgumentException("Invalid value for maxLocksPerTransaction. Must be less than or equal to 6400.");
    }
    
    
    this.maxLocksPerTransaction = maxLocksPerTransaction;
    return this;
  }

   /**
   * PostgreSQL maximum locks per transaction. Once increased, this parameter cannot be lowered from its set value.
   * minimum: 64
   * maximum: 6400
   * @return maxLocksPerTransaction
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "128", value = "PostgreSQL maximum locks per transaction. Once increased, this parameter cannot be lowered from its set value.")

  public Integer getMaxLocksPerTransaction() {
    return maxLocksPerTransaction;
  }


  public void setMaxLocksPerTransaction(Integer maxLocksPerTransaction) {
    if (maxLocksPerTransaction != null && maxLocksPerTransaction < 64) {
      throw new IllegalArgumentException("Invalid value for maxLocksPerTransaction. Must be greater than or equal to 64.");
    }
    if (maxLocksPerTransaction != null && maxLocksPerTransaction > 6400) {
      throw new IllegalArgumentException("Invalid value for maxLocksPerTransaction. Must be less than or equal to 6400.");
    }
    
    this.maxLocksPerTransaction = maxLocksPerTransaction;
  }


  public Mpr1 maxStackDepth(Integer maxStackDepth) {
    if (maxStackDepth != null && maxStackDepth < 2097152) {
      throw new IllegalArgumentException("Invalid value for maxStackDepth. Must be greater than or equal to 2097152.");
    }
    if (maxStackDepth != null && maxStackDepth > 6291456) {
      throw new IllegalArgumentException("Invalid value for maxStackDepth. Must be less than or equal to 6291456.");
    }
    
    
    this.maxStackDepth = maxStackDepth;
    return this;
  }

   /**
   * Maximum depth of the stack in bytes.
   * minimum: 2097152
   * maximum: 6291456
   * @return maxStackDepth
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2097152", value = "Maximum depth of the stack in bytes.")

  public Integer getMaxStackDepth() {
    return maxStackDepth;
  }


  public void setMaxStackDepth(Integer maxStackDepth) {
    if (maxStackDepth != null && maxStackDepth < 2097152) {
      throw new IllegalArgumentException("Invalid value for maxStackDepth. Must be greater than or equal to 2097152.");
    }
    if (maxStackDepth != null && maxStackDepth > 6291456) {
      throw new IllegalArgumentException("Invalid value for maxStackDepth. Must be less than or equal to 6291456.");
    }
    
    this.maxStackDepth = maxStackDepth;
  }


  public Mpr1 maxStandbyArchiveDelay(Integer maxStandbyArchiveDelay) {
    if (maxStandbyArchiveDelay != null && maxStandbyArchiveDelay < 1) {
      throw new IllegalArgumentException("Invalid value for maxStandbyArchiveDelay. Must be greater than or equal to 1.");
    }
    if (maxStandbyArchiveDelay != null && maxStandbyArchiveDelay > 43200000) {
      throw new IllegalArgumentException("Invalid value for maxStandbyArchiveDelay. Must be less than or equal to 43200000.");
    }
    
    
    this.maxStandbyArchiveDelay = maxStandbyArchiveDelay;
    return this;
  }

   /**
   * Max standby archive delay in milliseconds.
   * minimum: 1
   * maximum: 43200000
   * @return maxStandbyArchiveDelay
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "43200", value = "Max standby archive delay in milliseconds.")

  public Integer getMaxStandbyArchiveDelay() {
    return maxStandbyArchiveDelay;
  }


  public void setMaxStandbyArchiveDelay(Integer maxStandbyArchiveDelay) {
    if (maxStandbyArchiveDelay != null && maxStandbyArchiveDelay < 1) {
      throw new IllegalArgumentException("Invalid value for maxStandbyArchiveDelay. Must be greater than or equal to 1.");
    }
    if (maxStandbyArchiveDelay != null && maxStandbyArchiveDelay > 43200000) {
      throw new IllegalArgumentException("Invalid value for maxStandbyArchiveDelay. Must be less than or equal to 43200000.");
    }
    
    this.maxStandbyArchiveDelay = maxStandbyArchiveDelay;
  }


  public Mpr1 maxStandbyStreamingDelay(Integer maxStandbyStreamingDelay) {
    if (maxStandbyStreamingDelay != null && maxStandbyStreamingDelay < 1) {
      throw new IllegalArgumentException("Invalid value for maxStandbyStreamingDelay. Must be greater than or equal to 1.");
    }
    if (maxStandbyStreamingDelay != null && maxStandbyStreamingDelay > 43200000) {
      throw new IllegalArgumentException("Invalid value for maxStandbyStreamingDelay. Must be less than or equal to 43200000.");
    }
    
    
    this.maxStandbyStreamingDelay = maxStandbyStreamingDelay;
    return this;
  }

   /**
   * Max standby streaming delay in milliseconds.
   * minimum: 1
   * maximum: 43200000
   * @return maxStandbyStreamingDelay
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "43200", value = "Max standby streaming delay in milliseconds.")

  public Integer getMaxStandbyStreamingDelay() {
    return maxStandbyStreamingDelay;
  }


  public void setMaxStandbyStreamingDelay(Integer maxStandbyStreamingDelay) {
    if (maxStandbyStreamingDelay != null && maxStandbyStreamingDelay < 1) {
      throw new IllegalArgumentException("Invalid value for maxStandbyStreamingDelay. Must be greater than or equal to 1.");
    }
    if (maxStandbyStreamingDelay != null && maxStandbyStreamingDelay > 43200000) {
      throw new IllegalArgumentException("Invalid value for maxStandbyStreamingDelay. Must be less than or equal to 43200000.");
    }
    
    this.maxStandbyStreamingDelay = maxStandbyStreamingDelay;
  }


  public Mpr1 maxReplicationSlots(Integer maxReplicationSlots) {
    if (maxReplicationSlots != null && maxReplicationSlots < 8) {
      throw new IllegalArgumentException("Invalid value for maxReplicationSlots. Must be greater than or equal to 8.");
    }
    if (maxReplicationSlots != null && maxReplicationSlots > 64) {
      throw new IllegalArgumentException("Invalid value for maxReplicationSlots. Must be less than or equal to 64.");
    }
    
    
    this.maxReplicationSlots = maxReplicationSlots;
    return this;
  }

   /**
   * PostgreSQL maximum replication slots.
   * minimum: 8
   * maximum: 64
   * @return maxReplicationSlots
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16", value = "PostgreSQL maximum replication slots.")

  public Integer getMaxReplicationSlots() {
    return maxReplicationSlots;
  }


  public void setMaxReplicationSlots(Integer maxReplicationSlots) {
    if (maxReplicationSlots != null && maxReplicationSlots < 8) {
      throw new IllegalArgumentException("Invalid value for maxReplicationSlots. Must be greater than or equal to 8.");
    }
    if (maxReplicationSlots != null && maxReplicationSlots > 64) {
      throw new IllegalArgumentException("Invalid value for maxReplicationSlots. Must be less than or equal to 64.");
    }
    
    this.maxReplicationSlots = maxReplicationSlots;
  }


  public Mpr1 maxLogicalReplicationWorkers(Integer maxLogicalReplicationWorkers) {
    if (maxLogicalReplicationWorkers != null && maxLogicalReplicationWorkers < 4) {
      throw new IllegalArgumentException("Invalid value for maxLogicalReplicationWorkers. Must be greater than or equal to 4.");
    }
    if (maxLogicalReplicationWorkers != null && maxLogicalReplicationWorkers > 64) {
      throw new IllegalArgumentException("Invalid value for maxLogicalReplicationWorkers. Must be less than or equal to 64.");
    }
    
    
    this.maxLogicalReplicationWorkers = maxLogicalReplicationWorkers;
    return this;
  }

   /**
   * PostgreSQL maximum logical replication workers (taken from the pool of max_parallel_workers).
   * minimum: 4
   * maximum: 64
   * @return maxLogicalReplicationWorkers
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16", value = "PostgreSQL maximum logical replication workers (taken from the pool of max_parallel_workers).")

  public Integer getMaxLogicalReplicationWorkers() {
    return maxLogicalReplicationWorkers;
  }


  public void setMaxLogicalReplicationWorkers(Integer maxLogicalReplicationWorkers) {
    if (maxLogicalReplicationWorkers != null && maxLogicalReplicationWorkers < 4) {
      throw new IllegalArgumentException("Invalid value for maxLogicalReplicationWorkers. Must be greater than or equal to 4.");
    }
    if (maxLogicalReplicationWorkers != null && maxLogicalReplicationWorkers > 64) {
      throw new IllegalArgumentException("Invalid value for maxLogicalReplicationWorkers. Must be less than or equal to 64.");
    }
    
    this.maxLogicalReplicationWorkers = maxLogicalReplicationWorkers;
  }


  public Mpr1 maxParallelWorkers(Integer maxParallelWorkers) {
    if (maxParallelWorkers != null && maxParallelWorkers < 0) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkers. Must be greater than or equal to 0.");
    }
    if (maxParallelWorkers != null && maxParallelWorkers > 96) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkers. Must be less than or equal to 96.");
    }
    
    
    this.maxParallelWorkers = maxParallelWorkers;
    return this;
  }

   /**
   * Sets the maximum number of workers that the system can support for parallel queries.
   * minimum: 0
   * maximum: 96
   * @return maxParallelWorkers
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "12", value = "Sets the maximum number of workers that the system can support for parallel queries.")

  public Integer getMaxParallelWorkers() {
    return maxParallelWorkers;
  }


  public void setMaxParallelWorkers(Integer maxParallelWorkers) {
    if (maxParallelWorkers != null && maxParallelWorkers < 0) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkers. Must be greater than or equal to 0.");
    }
    if (maxParallelWorkers != null && maxParallelWorkers > 96) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkers. Must be less than or equal to 96.");
    }
    
    this.maxParallelWorkers = maxParallelWorkers;
  }


  public Mpr1 maxParallelWorkersPerGather(Integer maxParallelWorkersPerGather) {
    if (maxParallelWorkersPerGather != null && maxParallelWorkersPerGather < 0) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkersPerGather. Must be greater than or equal to 0.");
    }
    if (maxParallelWorkersPerGather != null && maxParallelWorkersPerGather > 96) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkersPerGather. Must be less than or equal to 96.");
    }
    
    
    this.maxParallelWorkersPerGather = maxParallelWorkersPerGather;
    return this;
  }

   /**
   * Sets the maximum number of workers that can be started by a single Gather or Gather Merge node.
   * minimum: 0
   * maximum: 96
   * @return maxParallelWorkersPerGather
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16", value = "Sets the maximum number of workers that can be started by a single Gather or Gather Merge node.")

  public Integer getMaxParallelWorkersPerGather() {
    return maxParallelWorkersPerGather;
  }


  public void setMaxParallelWorkersPerGather(Integer maxParallelWorkersPerGather) {
    if (maxParallelWorkersPerGather != null && maxParallelWorkersPerGather < 0) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkersPerGather. Must be greater than or equal to 0.");
    }
    if (maxParallelWorkersPerGather != null && maxParallelWorkersPerGather > 96) {
      throw new IllegalArgumentException("Invalid value for maxParallelWorkersPerGather. Must be less than or equal to 96.");
    }
    
    this.maxParallelWorkersPerGather = maxParallelWorkersPerGather;
  }


  public Mpr1 maxWorkerProcesses(Integer maxWorkerProcesses) {
    if (maxWorkerProcesses != null && maxWorkerProcesses < 8) {
      throw new IllegalArgumentException("Invalid value for maxWorkerProcesses. Must be greater than or equal to 8.");
    }
    if (maxWorkerProcesses != null && maxWorkerProcesses > 96) {
      throw new IllegalArgumentException("Invalid value for maxWorkerProcesses. Must be less than or equal to 96.");
    }
    
    
    this.maxWorkerProcesses = maxWorkerProcesses;
    return this;
  }

   /**
   * Sets the maximum number of background processes that the system can support. Once increased, this parameter cannot be lowered from its set value.
   * minimum: 8
   * maximum: 96
   * @return maxWorkerProcesses
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16", value = "Sets the maximum number of background processes that the system can support. Once increased, this parameter cannot be lowered from its set value.")

  public Integer getMaxWorkerProcesses() {
    return maxWorkerProcesses;
  }


  public void setMaxWorkerProcesses(Integer maxWorkerProcesses) {
    if (maxWorkerProcesses != null && maxWorkerProcesses < 8) {
      throw new IllegalArgumentException("Invalid value for maxWorkerProcesses. Must be greater than or equal to 8.");
    }
    if (maxWorkerProcesses != null && maxWorkerProcesses > 96) {
      throw new IllegalArgumentException("Invalid value for maxWorkerProcesses. Must be less than or equal to 96.");
    }
    
    this.maxWorkerProcesses = maxWorkerProcesses;
  }


  public Mpr1 pgPartmanBgwRole(String pgPartmanBgwRole) {
    
    
    
    
    this.pgPartmanBgwRole = pgPartmanBgwRole;
    return this;
  }

   /**
   * Controls which role to use for pg_partman&#39;s scheduled background tasks. Must consist of alpha-numeric characters, dots, underscores, or dashes. May not start with dash or dot. Maximum of 64 characters.
   * @return pgPartmanBgwRole
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "myrolename", value = "Controls which role to use for pg_partman's scheduled background tasks. Must consist of alpha-numeric characters, dots, underscores, or dashes. May not start with dash or dot. Maximum of 64 characters.")

  public String getPgPartmanBgwRole() {
    return pgPartmanBgwRole;
  }


  public void setPgPartmanBgwRole(String pgPartmanBgwRole) {
    
    
    
    this.pgPartmanBgwRole = pgPartmanBgwRole;
  }


  public Mpr1 pgPartmanBgwInterval(Integer pgPartmanBgwInterval) {
    if (pgPartmanBgwInterval != null && pgPartmanBgwInterval < 3600) {
      throw new IllegalArgumentException("Invalid value for pgPartmanBgwInterval. Must be greater than or equal to 3600.");
    }
    if (pgPartmanBgwInterval != null && pgPartmanBgwInterval > 604800) {
      throw new IllegalArgumentException("Invalid value for pgPartmanBgwInterval. Must be less than or equal to 604800.");
    }
    
    
    this.pgPartmanBgwInterval = pgPartmanBgwInterval;
    return this;
  }

   /**
   * Sets the time interval to run pg_partman&#39;s scheduled tasks.
   * minimum: 3600
   * maximum: 604800
   * @return pgPartmanBgwInterval
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "3600", value = "Sets the time interval to run pg_partman's scheduled tasks.")

  public Integer getPgPartmanBgwInterval() {
    return pgPartmanBgwInterval;
  }


  public void setPgPartmanBgwInterval(Integer pgPartmanBgwInterval) {
    if (pgPartmanBgwInterval != null && pgPartmanBgwInterval < 3600) {
      throw new IllegalArgumentException("Invalid value for pgPartmanBgwInterval. Must be greater than or equal to 3600.");
    }
    if (pgPartmanBgwInterval != null && pgPartmanBgwInterval > 604800) {
      throw new IllegalArgumentException("Invalid value for pgPartmanBgwInterval. Must be less than or equal to 604800.");
    }
    
    this.pgPartmanBgwInterval = pgPartmanBgwInterval;
  }


  public Mpr1 pgStatStatementsTrack(PgStatStatementsTrackEnum pgStatStatementsTrack) {
    
    
    
    
    this.pgStatStatementsTrack = pgStatStatementsTrack;
    return this;
  }

   /**
   * Controls which statements are counted. Specify &#39;top&#39; to track top-level statements (those issued directly by clients), &#39;all&#39; to also track nested statements (such as statements invoked within functions), or &#39;none&#39; to disable statement statistics collection. The default value is top.
   * @return pgStatStatementsTrack
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "ALL", value = "Controls which statements are counted. Specify 'top' to track top-level statements (those issued directly by clients), 'all' to also track nested statements (such as statements invoked within functions), or 'none' to disable statement statistics collection. The default value is top.")

  public PgStatStatementsTrackEnum getPgStatStatementsTrack() {
    return pgStatStatementsTrack;
  }


  public void setPgStatStatementsTrack(PgStatStatementsTrackEnum pgStatStatementsTrack) {
    
    
    
    this.pgStatStatementsTrack = pgStatStatementsTrack;
  }


  public Mpr1 tempFileLimit(Integer tempFileLimit) {
    if (tempFileLimit != null && tempFileLimit < -1) {
      throw new IllegalArgumentException("Invalid value for tempFileLimit. Must be greater than or equal to -1.");
    }
    if (tempFileLimit != null && tempFileLimit > 2147483647) {
      throw new IllegalArgumentException("Invalid value for tempFileLimit. Must be less than or equal to 2147483647.");
    }
    
    
    this.tempFileLimit = tempFileLimit;
    return this;
  }

   /**
   * PostgreSQL temporary file limit in KiB. If -1, sets to unlimited.
   * minimum: -1
   * maximum: 2147483647
   * @return tempFileLimit
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "5000000", value = "PostgreSQL temporary file limit in KiB. If -1, sets to unlimited.")

  public Integer getTempFileLimit() {
    return tempFileLimit;
  }


  public void setTempFileLimit(Integer tempFileLimit) {
    if (tempFileLimit != null && tempFileLimit < -1) {
      throw new IllegalArgumentException("Invalid value for tempFileLimit. Must be greater than or equal to -1.");
    }
    if (tempFileLimit != null && tempFileLimit > 2147483647) {
      throw new IllegalArgumentException("Invalid value for tempFileLimit. Must be less than or equal to 2147483647.");
    }
    
    this.tempFileLimit = tempFileLimit;
  }


  public Mpr1 timezone(String timezone) {
    
    
    
    
    this.timezone = timezone;
    return this;
  }

   /**
   * PostgreSQL service timezone
   * @return timezone
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "Europe/Helsinki", value = "PostgreSQL service timezone")

  public String getTimezone() {
    return timezone;
  }


  public void setTimezone(String timezone) {
    
    
    
    this.timezone = timezone;
  }


  public Mpr1 trackActivityQuerySize(Integer trackActivityQuerySize) {
    if (trackActivityQuerySize != null && trackActivityQuerySize < 1024) {
      throw new IllegalArgumentException("Invalid value for trackActivityQuerySize. Must be greater than or equal to 1024.");
    }
    if (trackActivityQuerySize != null && trackActivityQuerySize > 10240) {
      throw new IllegalArgumentException("Invalid value for trackActivityQuerySize. Must be less than or equal to 10240.");
    }
    
    
    this.trackActivityQuerySize = trackActivityQuerySize;
    return this;
  }

   /**
   * Specifies the number of bytes reserved to track the currently executing command for each active session.
   * minimum: 1024
   * maximum: 10240
   * @return trackActivityQuerySize
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1024", value = "Specifies the number of bytes reserved to track the currently executing command for each active session.")

  public Integer getTrackActivityQuerySize() {
    return trackActivityQuerySize;
  }


  public void setTrackActivityQuerySize(Integer trackActivityQuerySize) {
    if (trackActivityQuerySize != null && trackActivityQuerySize < 1024) {
      throw new IllegalArgumentException("Invalid value for trackActivityQuerySize. Must be greater than or equal to 1024.");
    }
    if (trackActivityQuerySize != null && trackActivityQuerySize > 10240) {
      throw new IllegalArgumentException("Invalid value for trackActivityQuerySize. Must be less than or equal to 10240.");
    }
    
    this.trackActivityQuerySize = trackActivityQuerySize;
  }


  public Mpr1 trackCommitTimestamp(TrackCommitTimestampEnum trackCommitTimestamp) {
    
    
    
    
    this.trackCommitTimestamp = trackCommitTimestamp;
    return this;
  }

   /**
   * Record commit time of transactions.
   * @return trackCommitTimestamp
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "FALSE", value = "Record commit time of transactions.")

  public TrackCommitTimestampEnum getTrackCommitTimestamp() {
    return trackCommitTimestamp;
  }


  public void setTrackCommitTimestamp(TrackCommitTimestampEnum trackCommitTimestamp) {
    
    
    
    this.trackCommitTimestamp = trackCommitTimestamp;
  }


  public Mpr1 trackFunctions(TrackFunctionsEnum trackFunctions) {
    
    
    
    
    this.trackFunctions = trackFunctions;
    return this;
  }

   /**
   * Enables tracking of function call counts and time used.
   * @return trackFunctions
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "ALL", value = "Enables tracking of function call counts and time used.")

  public TrackFunctionsEnum getTrackFunctions() {
    return trackFunctions;
  }


  public void setTrackFunctions(TrackFunctionsEnum trackFunctions) {
    
    
    
    this.trackFunctions = trackFunctions;
  }


  public Mpr1 trackIoTiming(TrackIoTimingEnum trackIoTiming) {
    
    
    
    
    this.trackIoTiming = trackIoTiming;
    return this;
  }

   /**
   * Enables timing of database I/O calls. This parameter is off by default, because it will repeatedly query the operating system for the current time, which may cause significant overhead on some platforms.
   * @return trackIoTiming
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "FALSE", value = "Enables timing of database I/O calls. This parameter is off by default, because it will repeatedly query the operating system for the current time, which may cause significant overhead on some platforms.")

  public TrackIoTimingEnum getTrackIoTiming() {
    return trackIoTiming;
  }


  public void setTrackIoTiming(TrackIoTimingEnum trackIoTiming) {
    
    
    
    this.trackIoTiming = trackIoTiming;
  }


  public Mpr1 maxWalSenders(Integer maxWalSenders) {
    if (maxWalSenders != null && maxWalSenders < 20) {
      throw new IllegalArgumentException("Invalid value for maxWalSenders. Must be greater than or equal to 20.");
    }
    if (maxWalSenders != null && maxWalSenders > 64) {
      throw new IllegalArgumentException("Invalid value for maxWalSenders. Must be less than or equal to 64.");
    }
    
    
    this.maxWalSenders = maxWalSenders;
    return this;
  }

   /**
   * PostgreSQL maximum WAL senders. Once increased, this parameter cannot be lowered from its set value.
   * minimum: 20
   * maximum: 64
   * @return maxWalSenders
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "32", value = "PostgreSQL maximum WAL senders. Once increased, this parameter cannot be lowered from its set value.")

  public Integer getMaxWalSenders() {
    return maxWalSenders;
  }


  public void setMaxWalSenders(Integer maxWalSenders) {
    if (maxWalSenders != null && maxWalSenders < 20) {
      throw new IllegalArgumentException("Invalid value for maxWalSenders. Must be greater than or equal to 20.");
    }
    if (maxWalSenders != null && maxWalSenders > 64) {
      throw new IllegalArgumentException("Invalid value for maxWalSenders. Must be less than or equal to 64.");
    }
    
    this.maxWalSenders = maxWalSenders;
  }


  public Mpr1 walSenderTimeout(Integer walSenderTimeout) {
    if (walSenderTimeout != null && walSenderTimeout < 0) {
      throw new IllegalArgumentException("Invalid value for walSenderTimeout. Must be greater than or equal to 0.");
    }
    if (walSenderTimeout != null && walSenderTimeout > 10800000) {
      throw new IllegalArgumentException("Invalid value for walSenderTimeout. Must be less than or equal to 10800000.");
    }
    
    
    this.walSenderTimeout = walSenderTimeout;
    return this;
  }

   /**
   * Terminate replication connections that are inactive for longer than this amount of time, in milliseconds. Setting this value to zero disables the timeout. Must be either 0 or between 5000 and 10800000.
   * minimum: 0
   * maximum: 10800000
   * @return walSenderTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "60000", value = "Terminate replication connections that are inactive for longer than this amount of time, in milliseconds. Setting this value to zero disables the timeout. Must be either 0 or between 5000 and 10800000.")

  public Integer getWalSenderTimeout() {
    return walSenderTimeout;
  }


  public void setWalSenderTimeout(Integer walSenderTimeout) {
    if (walSenderTimeout != null && walSenderTimeout < 0) {
      throw new IllegalArgumentException("Invalid value for walSenderTimeout. Must be greater than or equal to 0.");
    }
    if (walSenderTimeout != null && walSenderTimeout > 10800000) {
      throw new IllegalArgumentException("Invalid value for walSenderTimeout. Must be less than or equal to 10800000.");
    }
    
    this.walSenderTimeout = walSenderTimeout;
  }


  public Mpr1 walWriterDelay(Integer walWriterDelay) {
    if (walWriterDelay != null && walWriterDelay < 10) {
      throw new IllegalArgumentException("Invalid value for walWriterDelay. Must be greater than or equal to 10.");
    }
    if (walWriterDelay != null && walWriterDelay > 200) {
      throw new IllegalArgumentException("Invalid value for walWriterDelay. Must be less than or equal to 200.");
    }
    
    
    this.walWriterDelay = walWriterDelay;
    return this;
  }

   /**
   * WAL flush interval in milliseconds. Note that setting this value to lower than the default 200ms may negatively impact performance
   * minimum: 10
   * maximum: 200
   * @return walWriterDelay
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "50", value = "WAL flush interval in milliseconds. Note that setting this value to lower than the default 200ms may negatively impact performance")

  public Integer getWalWriterDelay() {
    return walWriterDelay;
  }


  public void setWalWriterDelay(Integer walWriterDelay) {
    if (walWriterDelay != null && walWriterDelay < 10) {
      throw new IllegalArgumentException("Invalid value for walWriterDelay. Must be greater than or equal to 10.");
    }
    if (walWriterDelay != null && walWriterDelay > 200) {
      throw new IllegalArgumentException("Invalid value for walWriterDelay. Must be less than or equal to 200.");
    }
    
    this.walWriterDelay = walWriterDelay;
  }


  public Mpr1 sharedBuffersPercentage(Double sharedBuffersPercentage) {
    if (sharedBuffersPercentage != null && sharedBuffersPercentage < 20) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be greater than or equal to 20.");
    }
    if (sharedBuffersPercentage != null && sharedBuffersPercentage > 60) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be less than or equal to 60.");
    }
    
    
    this.sharedBuffersPercentage = sharedBuffersPercentage;
    return this;
  }

  public Mpr1 sharedBuffersPercentage(Integer sharedBuffersPercentage) {
    if (sharedBuffersPercentage != null && sharedBuffersPercentage < 20) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be greater than or equal to 20.");
    }
    if (sharedBuffersPercentage != null && sharedBuffersPercentage > 60) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be less than or equal to 60.");
    }
    
    
    this.sharedBuffersPercentage = sharedBuffersPercentage.doubleValue();
    return this;
  }

   /**
   * Percentage of total RAM that the database server uses for shared memory buffers.  Valid range is 20-60 (float), which corresponds to 20% - 60%.  This setting adjusts the shared_buffers configuration value.
   * minimum: 20
   * maximum: 60
   * @return sharedBuffersPercentage
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "41.5", value = "Percentage of total RAM that the database server uses for shared memory buffers.  Valid range is 20-60 (float), which corresponds to 20% - 60%.  This setting adjusts the shared_buffers configuration value.")

  public Double getSharedBuffersPercentage() {
    return sharedBuffersPercentage;
  }


  public void setSharedBuffersPercentage(Double sharedBuffersPercentage) {
    if (sharedBuffersPercentage != null && sharedBuffersPercentage < 20) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be greater than or equal to 20.");
    }
    if (sharedBuffersPercentage != null && sharedBuffersPercentage > 60) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be less than or equal to 60.");
    }
    
    this.sharedBuffersPercentage = sharedBuffersPercentage;
  }


  public Mpr1 pgbouncer(MysqlPgbouncer pgbouncer) {
    
    
    
    
    this.pgbouncer = pgbouncer;
    return this;
  }

   /**
   * Get pgbouncer
   * @return pgbouncer
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public MysqlPgbouncer getPgbouncer() {
    return pgbouncer;
  }


  public void setPgbouncer(MysqlPgbouncer pgbouncer) {
    
    
    
    this.pgbouncer = pgbouncer;
  }


  public Mpr1 workMem(Integer workMem) {
    if (workMem != null && workMem < 1) {
      throw new IllegalArgumentException("Invalid value for workMem. Must be greater than or equal to 1.");
    }
    if (workMem != null && workMem > 1024) {
      throw new IllegalArgumentException("Invalid value for workMem. Must be less than or equal to 1024.");
    }
    
    
    this.workMem = workMem;
    return this;
  }

   /**
   * The maximum amount of memory, in MB, used by a query operation (such as a sort or hash table) before writing to temporary disk files. Default is 1MB + 0.075% of total RAM (up to 32MB).
   * minimum: 1
   * maximum: 1024
   * @return workMem
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "4", value = "The maximum amount of memory, in MB, used by a query operation (such as a sort or hash table) before writing to temporary disk files. Default is 1MB + 0.075% of total RAM (up to 32MB).")

  public Integer getWorkMem() {
    return workMem;
  }


  public void setWorkMem(Integer workMem) {
    if (workMem != null && workMem < 1) {
      throw new IllegalArgumentException("Invalid value for workMem. Must be greater than or equal to 1.");
    }
    if (workMem != null && workMem > 1024) {
      throw new IllegalArgumentException("Invalid value for workMem. Must be less than or equal to 1024.");
    }
    
    this.workMem = workMem;
  }


  public Mpr1 timescaledb(MysqlTimescaledb timescaledb) {
    
    
    
    
    this.timescaledb = timescaledb;
    return this;
  }

   /**
   * Get timescaledb
   * @return timescaledb
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public MysqlTimescaledb getTimescaledb() {
    return timescaledb;
  }


  public void setTimescaledb(MysqlTimescaledb timescaledb) {
    
    
    
    this.timescaledb = timescaledb;
  }


  public Mpr1 synchronousReplication(SynchronousReplicationEnum synchronousReplication) {
    
    
    
    
    this.synchronousReplication = synchronousReplication;
    return this;
  }

   /**
   * Synchronous replication type. Note that the service plan also needs to support synchronous replication.
   * @return synchronousReplication
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "FALSE", value = "Synchronous replication type. Note that the service plan also needs to support synchronous replication.")

  public SynchronousReplicationEnum getSynchronousReplication() {
    return synchronousReplication;
  }


  public void setSynchronousReplication(SynchronousReplicationEnum synchronousReplication) {
    
    
    
    this.synchronousReplication = synchronousReplication;
  }


  public Mpr1 statMonitorEnable(Boolean statMonitorEnable) {
    
    
    
    
    this.statMonitorEnable = statMonitorEnable;
    return this;
  }

   /**
   * Enable the pg_stat_monitor extension. &lt;b&gt;Enabling this extension will cause the cluster to be restarted.&lt;/b&gt; When this extension is enabled, pg_stat_statements results for utility commands are unreliable.
   * @return statMonitorEnable
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "false", value = "Enable the pg_stat_monitor extension. <b>Enabling this extension will cause the cluster to be restarted.</b> When this extension is enabled, pg_stat_statements results for utility commands are unreliable.")

  public Boolean getStatMonitorEnable() {
    return statMonitorEnable;
  }


  public void setStatMonitorEnable(Boolean statMonitorEnable) {
    
    
    
    this.statMonitorEnable = statMonitorEnable;
  }


  public Mpr1 redisMaxmemoryPolicy(RedisMaxmemoryPolicyEnum redisMaxmemoryPolicy) {
    
    
    
    
    this.redisMaxmemoryPolicy = redisMaxmemoryPolicy;
    return this;
  }

   /**
   * A string specifying the desired eviction policy for the Redis cluster.  - &#x60;noeviction&#x60;: Don&#39;t evict any data, returns error when memory limit is reached. - &#x60;allkeys_lru:&#x60; Evict any key, least recently used (LRU) first. - &#x60;allkeys_random&#x60;: Evict keys in a random order. - &#x60;volatile_lru&#x60;: Evict keys with expiration only, least recently used (LRU) first. - &#x60;volatile_random&#x60;: Evict keys with expiration only in a random order. - &#x60;volatile_ttl&#x60;: Evict keys with expiration only, shortest time-to-live (TTL) first.
   * @return redisMaxmemoryPolicy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "ALLKEYS_LRU", value = "A string specifying the desired eviction policy for the Redis cluster.  - `noeviction`: Don't evict any data, returns error when memory limit is reached. - `allkeys_lru:` Evict any key, least recently used (LRU) first. - `allkeys_random`: Evict keys in a random order. - `volatile_lru`: Evict keys with expiration only, least recently used (LRU) first. - `volatile_random`: Evict keys with expiration only in a random order. - `volatile_ttl`: Evict keys with expiration only, shortest time-to-live (TTL) first.")

  public RedisMaxmemoryPolicyEnum getRedisMaxmemoryPolicy() {
    return redisMaxmemoryPolicy;
  }


  public void setRedisMaxmemoryPolicy(RedisMaxmemoryPolicyEnum redisMaxmemoryPolicy) {
    
    
    
    this.redisMaxmemoryPolicy = redisMaxmemoryPolicy;
  }


  public Mpr1 redisPubsubClientOutputBufferLimit(Integer redisPubsubClientOutputBufferLimit) {
    if (redisPubsubClientOutputBufferLimit != null && redisPubsubClientOutputBufferLimit < 32) {
      throw new IllegalArgumentException("Invalid value for redisPubsubClientOutputBufferLimit. Must be greater than or equal to 32.");
    }
    if (redisPubsubClientOutputBufferLimit != null && redisPubsubClientOutputBufferLimit > 512) {
      throw new IllegalArgumentException("Invalid value for redisPubsubClientOutputBufferLimit. Must be less than or equal to 512.");
    }
    
    
    this.redisPubsubClientOutputBufferLimit = redisPubsubClientOutputBufferLimit;
    return this;
  }

   /**
   * Set output buffer limit for pub / sub clients in MB. The value is the hard limit, the soft limit is 1/4 of the hard limit. When setting the limit, be mindful of the available memory in the selected service plan.
   * minimum: 32
   * maximum: 512
   * @return redisPubsubClientOutputBufferLimit
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "64", value = "Set output buffer limit for pub / sub clients in MB. The value is the hard limit, the soft limit is 1/4 of the hard limit. When setting the limit, be mindful of the available memory in the selected service plan.")

  public Integer getRedisPubsubClientOutputBufferLimit() {
    return redisPubsubClientOutputBufferLimit;
  }


  public void setRedisPubsubClientOutputBufferLimit(Integer redisPubsubClientOutputBufferLimit) {
    if (redisPubsubClientOutputBufferLimit != null && redisPubsubClientOutputBufferLimit < 32) {
      throw new IllegalArgumentException("Invalid value for redisPubsubClientOutputBufferLimit. Must be greater than or equal to 32.");
    }
    if (redisPubsubClientOutputBufferLimit != null && redisPubsubClientOutputBufferLimit > 512) {
      throw new IllegalArgumentException("Invalid value for redisPubsubClientOutputBufferLimit. Must be less than or equal to 512.");
    }
    
    this.redisPubsubClientOutputBufferLimit = redisPubsubClientOutputBufferLimit;
  }


  public Mpr1 redisNumberOfDatabases(Integer redisNumberOfDatabases) {
    if (redisNumberOfDatabases != null && redisNumberOfDatabases < 1) {
      throw new IllegalArgumentException("Invalid value for redisNumberOfDatabases. Must be greater than or equal to 1.");
    }
    if (redisNumberOfDatabases != null && redisNumberOfDatabases > 128) {
      throw new IllegalArgumentException("Invalid value for redisNumberOfDatabases. Must be less than or equal to 128.");
    }
    
    
    this.redisNumberOfDatabases = redisNumberOfDatabases;
    return this;
  }

   /**
   * Set number of redis databases. Changing this will cause a restart of redis service.
   * minimum: 1
   * maximum: 128
   * @return redisNumberOfDatabases
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "16", value = "Set number of redis databases. Changing this will cause a restart of redis service.")

  public Integer getRedisNumberOfDatabases() {
    return redisNumberOfDatabases;
  }


  public void setRedisNumberOfDatabases(Integer redisNumberOfDatabases) {
    if (redisNumberOfDatabases != null && redisNumberOfDatabases < 1) {
      throw new IllegalArgumentException("Invalid value for redisNumberOfDatabases. Must be greater than or equal to 1.");
    }
    if (redisNumberOfDatabases != null && redisNumberOfDatabases > 128) {
      throw new IllegalArgumentException("Invalid value for redisNumberOfDatabases. Must be less than or equal to 128.");
    }
    
    this.redisNumberOfDatabases = redisNumberOfDatabases;
  }


  public Mpr1 redisIoThreads(Integer redisIoThreads) {
    if (redisIoThreads != null && redisIoThreads < 1) {
      throw new IllegalArgumentException("Invalid value for redisIoThreads. Must be greater than or equal to 1.");
    }
    if (redisIoThreads != null && redisIoThreads > 32) {
      throw new IllegalArgumentException("Invalid value for redisIoThreads. Must be less than or equal to 32.");
    }
    
    
    this.redisIoThreads = redisIoThreads;
    return this;
  }

   /**
   * Redis IO thread count
   * minimum: 1
   * maximum: 32
   * @return redisIoThreads
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "Redis IO thread count")

  public Integer getRedisIoThreads() {
    return redisIoThreads;
  }


  public void setRedisIoThreads(Integer redisIoThreads) {
    if (redisIoThreads != null && redisIoThreads < 1) {
      throw new IllegalArgumentException("Invalid value for redisIoThreads. Must be greater than or equal to 1.");
    }
    if (redisIoThreads != null && redisIoThreads > 32) {
      throw new IllegalArgumentException("Invalid value for redisIoThreads. Must be less than or equal to 32.");
    }
    
    this.redisIoThreads = redisIoThreads;
  }


  public Mpr1 redisLfuLogFactor(Integer redisLfuLogFactor) {
    if (redisLfuLogFactor != null && redisLfuLogFactor < 0) {
      throw new IllegalArgumentException("Invalid value for redisLfuLogFactor. Must be greater than or equal to 0.");
    }
    if (redisLfuLogFactor != null && redisLfuLogFactor > 100) {
      throw new IllegalArgumentException("Invalid value for redisLfuLogFactor. Must be less than or equal to 100.");
    }
    
    
    this.redisLfuLogFactor = redisLfuLogFactor;
    return this;
  }

   /**
   * Counter logarithm factor for volatile-lfu and allkeys-lfu maxmemory-policies
   * minimum: 0
   * maximum: 100
   * @return redisLfuLogFactor
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "10", value = "Counter logarithm factor for volatile-lfu and allkeys-lfu maxmemory-policies")

  public Integer getRedisLfuLogFactor() {
    return redisLfuLogFactor;
  }


  public void setRedisLfuLogFactor(Integer redisLfuLogFactor) {
    if (redisLfuLogFactor != null && redisLfuLogFactor < 0) {
      throw new IllegalArgumentException("Invalid value for redisLfuLogFactor. Must be greater than or equal to 0.");
    }
    if (redisLfuLogFactor != null && redisLfuLogFactor > 100) {
      throw new IllegalArgumentException("Invalid value for redisLfuLogFactor. Must be less than or equal to 100.");
    }
    
    this.redisLfuLogFactor = redisLfuLogFactor;
  }


  public Mpr1 redisLfuDecayTime(Integer redisLfuDecayTime) {
    if (redisLfuDecayTime != null && redisLfuDecayTime < 1) {
      throw new IllegalArgumentException("Invalid value for redisLfuDecayTime. Must be greater than or equal to 1.");
    }
    if (redisLfuDecayTime != null && redisLfuDecayTime > 120) {
      throw new IllegalArgumentException("Invalid value for redisLfuDecayTime. Must be less than or equal to 120.");
    }
    
    
    this.redisLfuDecayTime = redisLfuDecayTime;
    return this;
  }

   /**
   * LFU maxmemory-policy counter decay time in minutes
   * minimum: 1
   * maximum: 120
   * @return redisLfuDecayTime
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "LFU maxmemory-policy counter decay time in minutes")

  public Integer getRedisLfuDecayTime() {
    return redisLfuDecayTime;
  }


  public void setRedisLfuDecayTime(Integer redisLfuDecayTime) {
    if (redisLfuDecayTime != null && redisLfuDecayTime < 1) {
      throw new IllegalArgumentException("Invalid value for redisLfuDecayTime. Must be greater than or equal to 1.");
    }
    if (redisLfuDecayTime != null && redisLfuDecayTime > 120) {
      throw new IllegalArgumentException("Invalid value for redisLfuDecayTime. Must be less than or equal to 120.");
    }
    
    this.redisLfuDecayTime = redisLfuDecayTime;
  }


  public Mpr1 redisSsl(Boolean redisSsl) {
    
    
    
    
    this.redisSsl = redisSsl;
    return this;
  }

   /**
   * Require SSL to access Redis
   * @return redisSsl
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "Require SSL to access Redis")

  public Boolean getRedisSsl() {
    return redisSsl;
  }


  public void setRedisSsl(Boolean redisSsl) {
    
    
    
    this.redisSsl = redisSsl;
  }


  public Mpr1 redisTimeout(Integer redisTimeout) {
    if (redisTimeout != null && redisTimeout < 0) {
      throw new IllegalArgumentException("Invalid value for redisTimeout. Must be greater than or equal to 0.");
    }
    if (redisTimeout != null && redisTimeout > 31536000) {
      throw new IllegalArgumentException("Invalid value for redisTimeout. Must be less than or equal to 31536000.");
    }
    
    
    this.redisTimeout = redisTimeout;
    return this;
  }

   /**
   * Redis idle connection timeout in seconds
   * minimum: 0
   * maximum: 31536000
   * @return redisTimeout
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "300", value = "Redis idle connection timeout in seconds")

  public Integer getRedisTimeout() {
    return redisTimeout;
  }


  public void setRedisTimeout(Integer redisTimeout) {
    if (redisTimeout != null && redisTimeout < 0) {
      throw new IllegalArgumentException("Invalid value for redisTimeout. Must be greater than or equal to 0.");
    }
    if (redisTimeout != null && redisTimeout > 31536000) {
      throw new IllegalArgumentException("Invalid value for redisTimeout. Must be less than or equal to 31536000.");
    }
    
    this.redisTimeout = redisTimeout;
  }


  public Mpr1 redisNotifyKeyspaceEvents(String redisNotifyKeyspaceEvents) {
    
    
    
    
    this.redisNotifyKeyspaceEvents = redisNotifyKeyspaceEvents;
    return this;
  }

   /**
   * Set notify-keyspace-events option. Requires at least &#x60;K&#x60; or &#x60;E&#x60; and accepts any combination of the following options. Setting the parameter to &#x60;\&quot;\&quot;&#x60; disables notifications. - &#x60;K&#x60; &amp;mdash; Keyspace events - &#x60;E&#x60; &amp;mdash; Keyevent events - &#x60;g&#x60; &amp;mdash; Generic commands (e.g. &#x60;DEL&#x60;, &#x60;EXPIRE&#x60;, &#x60;RENAME&#x60;, ...) - &#x60;$&#x60; &amp;mdash; String commands - &#x60;l&#x60; &amp;mdash; List commands - &#x60;s&#x60; &amp;mdash; Set commands - &#x60;h&#x60; &amp;mdash; Hash commands - &#x60;z&#x60; &amp;mdash; Sorted set commands - &#x60;t&#x60; &amp;mdash; Stream commands - &#x60;d&#x60; &amp;mdash; Module key type events - &#x60;x&#x60; &amp;mdash; Expired events - &#x60;e&#x60; &amp;mdash; Evicted events - &#x60;m&#x60; &amp;mdash; Key miss events - &#x60;n&#x60; &amp;mdash; New key events - &#x60;A&#x60; &amp;mdash; Alias for &#x60;\&quot;g$lshztxed\&quot;&#x60;
   * @return redisNotifyKeyspaceEvents
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "K", value = "Set notify-keyspace-events option. Requires at least `K` or `E` and accepts any combination of the following options. Setting the parameter to `\"\"` disables notifications. - `K` &mdash; Keyspace events - `E` &mdash; Keyevent events - `g` &mdash; Generic commands (e.g. `DEL`, `EXPIRE`, `RENAME`, ...) - `$` &mdash; String commands - `l` &mdash; List commands - `s` &mdash; Set commands - `h` &mdash; Hash commands - `z` &mdash; Sorted set commands - `t` &mdash; Stream commands - `d` &mdash; Module key type events - `x` &mdash; Expired events - `e` &mdash; Evicted events - `m` &mdash; Key miss events - `n` &mdash; New key events - `A` &mdash; Alias for `\"g$lshztxed\"`")

  public String getRedisNotifyKeyspaceEvents() {
    return redisNotifyKeyspaceEvents;
  }


  public void setRedisNotifyKeyspaceEvents(String redisNotifyKeyspaceEvents) {
    
    
    
    this.redisNotifyKeyspaceEvents = redisNotifyKeyspaceEvents;
  }


  public Mpr1 redisPersistence(RedisPersistenceEnum redisPersistence) {
    
    
    
    
    this.redisPersistence = redisPersistence;
    return this;
  }

   /**
   * When persistence is &#39;rdb&#39;, Redis does RDB dumps each 10 minutes if any key is changed. Also RDB dumps are done according to backup schedule for backup purposes. When persistence is &#39;off&#39;, no RDB dumps and backups are done, so data can be lost at any moment if service is restarted for any reason, or if service is powered off. Also service can&#39;t be forked.
   * @return redisPersistence
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "RDB", value = "When persistence is 'rdb', Redis does RDB dumps each 10 minutes if any key is changed. Also RDB dumps are done according to backup schedule for backup purposes. When persistence is 'off', no RDB dumps and backups are done, so data can be lost at any moment if service is restarted for any reason, or if service is powered off. Also service can't be forked.")

  public RedisPersistenceEnum getRedisPersistence() {
    return redisPersistence;
  }


  public void setRedisPersistence(RedisPersistenceEnum redisPersistence) {
    
    
    
    this.redisPersistence = redisPersistence;
  }


  public Mpr1 redisAclChannelsDefault(RedisAclChannelsDefaultEnum redisAclChannelsDefault) {
    
    
    
    
    this.redisAclChannelsDefault = redisAclChannelsDefault;
    return this;
  }

   /**
   * Determines default pub/sub channels&#39; ACL for new users if ACL is not supplied. When this option is not defined, all_channels is assumed to keep backward compatibility. This option doesn&#39;t affect Redis configuration acl-pubsub-default.
   * @return redisAclChannelsDefault
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "ALLCHANNELS", value = "Determines default pub/sub channels' ACL for new users if ACL is not supplied. When this option is not defined, all_channels is assumed to keep backward compatibility. This option doesn't affect Redis configuration acl-pubsub-default.")

  public RedisAclChannelsDefaultEnum getRedisAclChannelsDefault() {
    return redisAclChannelsDefault;
  }


  public void setRedisAclChannelsDefault(RedisAclChannelsDefaultEnum redisAclChannelsDefault) {
    
    
    
    this.redisAclChannelsDefault = redisAclChannelsDefault;
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
   * @return the Mpr1 instance itself
   */
  public Mpr1 putAdditionalProperty(String key, Object value) {
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
    Mpr1 mpr1 = (Mpr1) o;
    return Objects.equals(this.backupHour, mpr1.backupHour) &&
        Objects.equals(this.backupMinute, mpr1.backupMinute) &&
        Objects.equals(this.sqlMode, mpr1.sqlMode) &&
        Objects.equals(this.connectTimeout, mpr1.connectTimeout) &&
        Objects.equals(this.defaultTimeZone, mpr1.defaultTimeZone) &&
        Objects.equals(this.groupConcatMaxLen, mpr1.groupConcatMaxLen) &&
        Objects.equals(this.informationSchemaStatsExpiry, mpr1.informationSchemaStatsExpiry) &&
        Objects.equals(this.innodbFtMinTokenSize, mpr1.innodbFtMinTokenSize) &&
        Objects.equals(this.innodbFtServerStopwordTable, mpr1.innodbFtServerStopwordTable) &&
        Objects.equals(this.innodbLockWaitTimeout, mpr1.innodbLockWaitTimeout) &&
        Objects.equals(this.innodbLogBufferSize, mpr1.innodbLogBufferSize) &&
        Objects.equals(this.innodbOnlineAlterLogMaxSize, mpr1.innodbOnlineAlterLogMaxSize) &&
        Objects.equals(this.innodbPrintAllDeadlocks, mpr1.innodbPrintAllDeadlocks) &&
        Objects.equals(this.innodbRollbackOnTimeout, mpr1.innodbRollbackOnTimeout) &&
        Objects.equals(this.interactiveTimeout, mpr1.interactiveTimeout) &&
        Objects.equals(this.internalTmpMemStorageEngine, mpr1.internalTmpMemStorageEngine) &&
        Objects.equals(this.netReadTimeout, mpr1.netReadTimeout) &&
        Objects.equals(this.netWriteTimeout, mpr1.netWriteTimeout) &&
        Objects.equals(this.sqlRequirePrimaryKey, mpr1.sqlRequirePrimaryKey) &&
        Objects.equals(this.waitTimeout, mpr1.waitTimeout) &&
        Objects.equals(this.maxAllowedPacket, mpr1.maxAllowedPacket) &&
        Objects.equals(this.maxHeapTableSize, mpr1.maxHeapTableSize) &&
        Objects.equals(this.sortBufferSize, mpr1.sortBufferSize) &&
        Objects.equals(this.tmpTableSize, mpr1.tmpTableSize) &&
        Objects.equals(this.slowQueryLog, mpr1.slowQueryLog) &&
        Objects.equals(this.longQueryTime, mpr1.longQueryTime) &&
        Objects.equals(this.binlogRetentionPeriod, mpr1.binlogRetentionPeriod) &&
        Objects.equals(this.innodbChangeBufferMaxSize, mpr1.innodbChangeBufferMaxSize) &&
        Objects.equals(this.innodbFlushNeighbors, mpr1.innodbFlushNeighbors) &&
        Objects.equals(this.innodbReadIoThreads, mpr1.innodbReadIoThreads) &&
        Objects.equals(this.innodbWriteIoThreads, mpr1.innodbWriteIoThreads) &&
        Objects.equals(this.innodbThreadConcurrency, mpr1.innodbThreadConcurrency) &&
        Objects.equals(this.netBufferLength, mpr1.netBufferLength) &&
        Objects.equals(this.autovacuumFreezeMaxAge, mpr1.autovacuumFreezeMaxAge) &&
        Objects.equals(this.autovacuumMaxWorkers, mpr1.autovacuumMaxWorkers) &&
        Objects.equals(this.autovacuumNaptime, mpr1.autovacuumNaptime) &&
        Objects.equals(this.autovacuumVacuumThreshold, mpr1.autovacuumVacuumThreshold) &&
        Objects.equals(this.autovacuumAnalyzeThreshold, mpr1.autovacuumAnalyzeThreshold) &&
        Objects.equals(this.autovacuumVacuumScaleFactor, mpr1.autovacuumVacuumScaleFactor) &&
        Objects.equals(this.autovacuumAnalyzeScaleFactor, mpr1.autovacuumAnalyzeScaleFactor) &&
        Objects.equals(this.autovacuumVacuumCostDelay, mpr1.autovacuumVacuumCostDelay) &&
        Objects.equals(this.autovacuumVacuumCostLimit, mpr1.autovacuumVacuumCostLimit) &&
        Objects.equals(this.bgwriterDelay, mpr1.bgwriterDelay) &&
        Objects.equals(this.bgwriterFlushAfter, mpr1.bgwriterFlushAfter) &&
        Objects.equals(this.bgwriterLruMaxpages, mpr1.bgwriterLruMaxpages) &&
        Objects.equals(this.bgwriterLruMultiplier, mpr1.bgwriterLruMultiplier) &&
        Objects.equals(this.deadlockTimeout, mpr1.deadlockTimeout) &&
        Objects.equals(this.defaultToastCompression, mpr1.defaultToastCompression) &&
        Objects.equals(this.idleInTransactionSessionTimeout, mpr1.idleInTransactionSessionTimeout) &&
        Objects.equals(this.jit, mpr1.jit) &&
        Objects.equals(this.logAutovacuumMinDuration, mpr1.logAutovacuumMinDuration) &&
        Objects.equals(this.logErrorVerbosity, mpr1.logErrorVerbosity) &&
        Objects.equals(this.logLinePrefix, mpr1.logLinePrefix) &&
        Objects.equals(this.logMinDurationStatement, mpr1.logMinDurationStatement) &&
        Objects.equals(this.maxFilesPerProcess, mpr1.maxFilesPerProcess) &&
        Objects.equals(this.maxPreparedTransactions, mpr1.maxPreparedTransactions) &&
        Objects.equals(this.maxPredLocksPerTransaction, mpr1.maxPredLocksPerTransaction) &&
        Objects.equals(this.maxLocksPerTransaction, mpr1.maxLocksPerTransaction) &&
        Objects.equals(this.maxStackDepth, mpr1.maxStackDepth) &&
        Objects.equals(this.maxStandbyArchiveDelay, mpr1.maxStandbyArchiveDelay) &&
        Objects.equals(this.maxStandbyStreamingDelay, mpr1.maxStandbyStreamingDelay) &&
        Objects.equals(this.maxReplicationSlots, mpr1.maxReplicationSlots) &&
        Objects.equals(this.maxLogicalReplicationWorkers, mpr1.maxLogicalReplicationWorkers) &&
        Objects.equals(this.maxParallelWorkers, mpr1.maxParallelWorkers) &&
        Objects.equals(this.maxParallelWorkersPerGather, mpr1.maxParallelWorkersPerGather) &&
        Objects.equals(this.maxWorkerProcesses, mpr1.maxWorkerProcesses) &&
        Objects.equals(this.pgPartmanBgwRole, mpr1.pgPartmanBgwRole) &&
        Objects.equals(this.pgPartmanBgwInterval, mpr1.pgPartmanBgwInterval) &&
        Objects.equals(this.pgStatStatementsTrack, mpr1.pgStatStatementsTrack) &&
        Objects.equals(this.tempFileLimit, mpr1.tempFileLimit) &&
        Objects.equals(this.timezone, mpr1.timezone) &&
        Objects.equals(this.trackActivityQuerySize, mpr1.trackActivityQuerySize) &&
        Objects.equals(this.trackCommitTimestamp, mpr1.trackCommitTimestamp) &&
        Objects.equals(this.trackFunctions, mpr1.trackFunctions) &&
        Objects.equals(this.trackIoTiming, mpr1.trackIoTiming) &&
        Objects.equals(this.maxWalSenders, mpr1.maxWalSenders) &&
        Objects.equals(this.walSenderTimeout, mpr1.walSenderTimeout) &&
        Objects.equals(this.walWriterDelay, mpr1.walWriterDelay) &&
        Objects.equals(this.sharedBuffersPercentage, mpr1.sharedBuffersPercentage) &&
        Objects.equals(this.pgbouncer, mpr1.pgbouncer) &&
        Objects.equals(this.workMem, mpr1.workMem) &&
        Objects.equals(this.timescaledb, mpr1.timescaledb) &&
        Objects.equals(this.synchronousReplication, mpr1.synchronousReplication) &&
        Objects.equals(this.statMonitorEnable, mpr1.statMonitorEnable) &&
        Objects.equals(this.redisMaxmemoryPolicy, mpr1.redisMaxmemoryPolicy) &&
        Objects.equals(this.redisPubsubClientOutputBufferLimit, mpr1.redisPubsubClientOutputBufferLimit) &&
        Objects.equals(this.redisNumberOfDatabases, mpr1.redisNumberOfDatabases) &&
        Objects.equals(this.redisIoThreads, mpr1.redisIoThreads) &&
        Objects.equals(this.redisLfuLogFactor, mpr1.redisLfuLogFactor) &&
        Objects.equals(this.redisLfuDecayTime, mpr1.redisLfuDecayTime) &&
        Objects.equals(this.redisSsl, mpr1.redisSsl) &&
        Objects.equals(this.redisTimeout, mpr1.redisTimeout) &&
        Objects.equals(this.redisNotifyKeyspaceEvents, mpr1.redisNotifyKeyspaceEvents) &&
        Objects.equals(this.redisPersistence, mpr1.redisPersistence) &&
        Objects.equals(this.redisAclChannelsDefault, mpr1.redisAclChannelsDefault)&&
        Objects.equals(this.additionalProperties, mpr1.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(backupHour, backupMinute, sqlMode, connectTimeout, defaultTimeZone, groupConcatMaxLen, informationSchemaStatsExpiry, innodbFtMinTokenSize, innodbFtServerStopwordTable, innodbLockWaitTimeout, innodbLogBufferSize, innodbOnlineAlterLogMaxSize, innodbPrintAllDeadlocks, innodbRollbackOnTimeout, interactiveTimeout, internalTmpMemStorageEngine, netReadTimeout, netWriteTimeout, sqlRequirePrimaryKey, waitTimeout, maxAllowedPacket, maxHeapTableSize, sortBufferSize, tmpTableSize, slowQueryLog, longQueryTime, binlogRetentionPeriod, innodbChangeBufferMaxSize, innodbFlushNeighbors, innodbReadIoThreads, innodbWriteIoThreads, innodbThreadConcurrency, netBufferLength, autovacuumFreezeMaxAge, autovacuumMaxWorkers, autovacuumNaptime, autovacuumVacuumThreshold, autovacuumAnalyzeThreshold, autovacuumVacuumScaleFactor, autovacuumAnalyzeScaleFactor, autovacuumVacuumCostDelay, autovacuumVacuumCostLimit, bgwriterDelay, bgwriterFlushAfter, bgwriterLruMaxpages, bgwriterLruMultiplier, deadlockTimeout, defaultToastCompression, idleInTransactionSessionTimeout, jit, logAutovacuumMinDuration, logErrorVerbosity, logLinePrefix, logMinDurationStatement, maxFilesPerProcess, maxPreparedTransactions, maxPredLocksPerTransaction, maxLocksPerTransaction, maxStackDepth, maxStandbyArchiveDelay, maxStandbyStreamingDelay, maxReplicationSlots, maxLogicalReplicationWorkers, maxParallelWorkers, maxParallelWorkersPerGather, maxWorkerProcesses, pgPartmanBgwRole, pgPartmanBgwInterval, pgStatStatementsTrack, tempFileLimit, timezone, trackActivityQuerySize, trackCommitTimestamp, trackFunctions, trackIoTiming, maxWalSenders, walSenderTimeout, walWriterDelay, sharedBuffersPercentage, pgbouncer, workMem, timescaledb, synchronousReplication, statMonitorEnable, redisMaxmemoryPolicy, redisPubsubClientOutputBufferLimit, redisNumberOfDatabases, redisIoThreads, redisLfuLogFactor, redisLfuDecayTime, redisSsl, redisTimeout, redisNotifyKeyspaceEvents, redisPersistence, redisAclChannelsDefault, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Mpr1 {\n");
    sb.append("    backupHour: ").append(toIndentedString(backupHour)).append("\n");
    sb.append("    backupMinute: ").append(toIndentedString(backupMinute)).append("\n");
    sb.append("    sqlMode: ").append(toIndentedString(sqlMode)).append("\n");
    sb.append("    connectTimeout: ").append(toIndentedString(connectTimeout)).append("\n");
    sb.append("    defaultTimeZone: ").append(toIndentedString(defaultTimeZone)).append("\n");
    sb.append("    groupConcatMaxLen: ").append(toIndentedString(groupConcatMaxLen)).append("\n");
    sb.append("    informationSchemaStatsExpiry: ").append(toIndentedString(informationSchemaStatsExpiry)).append("\n");
    sb.append("    innodbFtMinTokenSize: ").append(toIndentedString(innodbFtMinTokenSize)).append("\n");
    sb.append("    innodbFtServerStopwordTable: ").append(toIndentedString(innodbFtServerStopwordTable)).append("\n");
    sb.append("    innodbLockWaitTimeout: ").append(toIndentedString(innodbLockWaitTimeout)).append("\n");
    sb.append("    innodbLogBufferSize: ").append(toIndentedString(innodbLogBufferSize)).append("\n");
    sb.append("    innodbOnlineAlterLogMaxSize: ").append(toIndentedString(innodbOnlineAlterLogMaxSize)).append("\n");
    sb.append("    innodbPrintAllDeadlocks: ").append(toIndentedString(innodbPrintAllDeadlocks)).append("\n");
    sb.append("    innodbRollbackOnTimeout: ").append(toIndentedString(innodbRollbackOnTimeout)).append("\n");
    sb.append("    interactiveTimeout: ").append(toIndentedString(interactiveTimeout)).append("\n");
    sb.append("    internalTmpMemStorageEngine: ").append(toIndentedString(internalTmpMemStorageEngine)).append("\n");
    sb.append("    netReadTimeout: ").append(toIndentedString(netReadTimeout)).append("\n");
    sb.append("    netWriteTimeout: ").append(toIndentedString(netWriteTimeout)).append("\n");
    sb.append("    sqlRequirePrimaryKey: ").append(toIndentedString(sqlRequirePrimaryKey)).append("\n");
    sb.append("    waitTimeout: ").append(toIndentedString(waitTimeout)).append("\n");
    sb.append("    maxAllowedPacket: ").append(toIndentedString(maxAllowedPacket)).append("\n");
    sb.append("    maxHeapTableSize: ").append(toIndentedString(maxHeapTableSize)).append("\n");
    sb.append("    sortBufferSize: ").append(toIndentedString(sortBufferSize)).append("\n");
    sb.append("    tmpTableSize: ").append(toIndentedString(tmpTableSize)).append("\n");
    sb.append("    slowQueryLog: ").append(toIndentedString(slowQueryLog)).append("\n");
    sb.append("    longQueryTime: ").append(toIndentedString(longQueryTime)).append("\n");
    sb.append("    binlogRetentionPeriod: ").append(toIndentedString(binlogRetentionPeriod)).append("\n");
    sb.append("    innodbChangeBufferMaxSize: ").append(toIndentedString(innodbChangeBufferMaxSize)).append("\n");
    sb.append("    innodbFlushNeighbors: ").append(toIndentedString(innodbFlushNeighbors)).append("\n");
    sb.append("    innodbReadIoThreads: ").append(toIndentedString(innodbReadIoThreads)).append("\n");
    sb.append("    innodbWriteIoThreads: ").append(toIndentedString(innodbWriteIoThreads)).append("\n");
    sb.append("    innodbThreadConcurrency: ").append(toIndentedString(innodbThreadConcurrency)).append("\n");
    sb.append("    netBufferLength: ").append(toIndentedString(netBufferLength)).append("\n");
    sb.append("    autovacuumFreezeMaxAge: ").append(toIndentedString(autovacuumFreezeMaxAge)).append("\n");
    sb.append("    autovacuumMaxWorkers: ").append(toIndentedString(autovacuumMaxWorkers)).append("\n");
    sb.append("    autovacuumNaptime: ").append(toIndentedString(autovacuumNaptime)).append("\n");
    sb.append("    autovacuumVacuumThreshold: ").append(toIndentedString(autovacuumVacuumThreshold)).append("\n");
    sb.append("    autovacuumAnalyzeThreshold: ").append(toIndentedString(autovacuumAnalyzeThreshold)).append("\n");
    sb.append("    autovacuumVacuumScaleFactor: ").append(toIndentedString(autovacuumVacuumScaleFactor)).append("\n");
    sb.append("    autovacuumAnalyzeScaleFactor: ").append(toIndentedString(autovacuumAnalyzeScaleFactor)).append("\n");
    sb.append("    autovacuumVacuumCostDelay: ").append(toIndentedString(autovacuumVacuumCostDelay)).append("\n");
    sb.append("    autovacuumVacuumCostLimit: ").append(toIndentedString(autovacuumVacuumCostLimit)).append("\n");
    sb.append("    bgwriterDelay: ").append(toIndentedString(bgwriterDelay)).append("\n");
    sb.append("    bgwriterFlushAfter: ").append(toIndentedString(bgwriterFlushAfter)).append("\n");
    sb.append("    bgwriterLruMaxpages: ").append(toIndentedString(bgwriterLruMaxpages)).append("\n");
    sb.append("    bgwriterLruMultiplier: ").append(toIndentedString(bgwriterLruMultiplier)).append("\n");
    sb.append("    deadlockTimeout: ").append(toIndentedString(deadlockTimeout)).append("\n");
    sb.append("    defaultToastCompression: ").append(toIndentedString(defaultToastCompression)).append("\n");
    sb.append("    idleInTransactionSessionTimeout: ").append(toIndentedString(idleInTransactionSessionTimeout)).append("\n");
    sb.append("    jit: ").append(toIndentedString(jit)).append("\n");
    sb.append("    logAutovacuumMinDuration: ").append(toIndentedString(logAutovacuumMinDuration)).append("\n");
    sb.append("    logErrorVerbosity: ").append(toIndentedString(logErrorVerbosity)).append("\n");
    sb.append("    logLinePrefix: ").append(toIndentedString(logLinePrefix)).append("\n");
    sb.append("    logMinDurationStatement: ").append(toIndentedString(logMinDurationStatement)).append("\n");
    sb.append("    maxFilesPerProcess: ").append(toIndentedString(maxFilesPerProcess)).append("\n");
    sb.append("    maxPreparedTransactions: ").append(toIndentedString(maxPreparedTransactions)).append("\n");
    sb.append("    maxPredLocksPerTransaction: ").append(toIndentedString(maxPredLocksPerTransaction)).append("\n");
    sb.append("    maxLocksPerTransaction: ").append(toIndentedString(maxLocksPerTransaction)).append("\n");
    sb.append("    maxStackDepth: ").append(toIndentedString(maxStackDepth)).append("\n");
    sb.append("    maxStandbyArchiveDelay: ").append(toIndentedString(maxStandbyArchiveDelay)).append("\n");
    sb.append("    maxStandbyStreamingDelay: ").append(toIndentedString(maxStandbyStreamingDelay)).append("\n");
    sb.append("    maxReplicationSlots: ").append(toIndentedString(maxReplicationSlots)).append("\n");
    sb.append("    maxLogicalReplicationWorkers: ").append(toIndentedString(maxLogicalReplicationWorkers)).append("\n");
    sb.append("    maxParallelWorkers: ").append(toIndentedString(maxParallelWorkers)).append("\n");
    sb.append("    maxParallelWorkersPerGather: ").append(toIndentedString(maxParallelWorkersPerGather)).append("\n");
    sb.append("    maxWorkerProcesses: ").append(toIndentedString(maxWorkerProcesses)).append("\n");
    sb.append("    pgPartmanBgwRole: ").append(toIndentedString(pgPartmanBgwRole)).append("\n");
    sb.append("    pgPartmanBgwInterval: ").append(toIndentedString(pgPartmanBgwInterval)).append("\n");
    sb.append("    pgStatStatementsTrack: ").append(toIndentedString(pgStatStatementsTrack)).append("\n");
    sb.append("    tempFileLimit: ").append(toIndentedString(tempFileLimit)).append("\n");
    sb.append("    timezone: ").append(toIndentedString(timezone)).append("\n");
    sb.append("    trackActivityQuerySize: ").append(toIndentedString(trackActivityQuerySize)).append("\n");
    sb.append("    trackCommitTimestamp: ").append(toIndentedString(trackCommitTimestamp)).append("\n");
    sb.append("    trackFunctions: ").append(toIndentedString(trackFunctions)).append("\n");
    sb.append("    trackIoTiming: ").append(toIndentedString(trackIoTiming)).append("\n");
    sb.append("    maxWalSenders: ").append(toIndentedString(maxWalSenders)).append("\n");
    sb.append("    walSenderTimeout: ").append(toIndentedString(walSenderTimeout)).append("\n");
    sb.append("    walWriterDelay: ").append(toIndentedString(walWriterDelay)).append("\n");
    sb.append("    sharedBuffersPercentage: ").append(toIndentedString(sharedBuffersPercentage)).append("\n");
    sb.append("    pgbouncer: ").append(toIndentedString(pgbouncer)).append("\n");
    sb.append("    workMem: ").append(toIndentedString(workMem)).append("\n");
    sb.append("    timescaledb: ").append(toIndentedString(timescaledb)).append("\n");
    sb.append("    synchronousReplication: ").append(toIndentedString(synchronousReplication)).append("\n");
    sb.append("    statMonitorEnable: ").append(toIndentedString(statMonitorEnable)).append("\n");
    sb.append("    redisMaxmemoryPolicy: ").append(toIndentedString(redisMaxmemoryPolicy)).append("\n");
    sb.append("    redisPubsubClientOutputBufferLimit: ").append(toIndentedString(redisPubsubClientOutputBufferLimit)).append("\n");
    sb.append("    redisNumberOfDatabases: ").append(toIndentedString(redisNumberOfDatabases)).append("\n");
    sb.append("    redisIoThreads: ").append(toIndentedString(redisIoThreads)).append("\n");
    sb.append("    redisLfuLogFactor: ").append(toIndentedString(redisLfuLogFactor)).append("\n");
    sb.append("    redisLfuDecayTime: ").append(toIndentedString(redisLfuDecayTime)).append("\n");
    sb.append("    redisSsl: ").append(toIndentedString(redisSsl)).append("\n");
    sb.append("    redisTimeout: ").append(toIndentedString(redisTimeout)).append("\n");
    sb.append("    redisNotifyKeyspaceEvents: ").append(toIndentedString(redisNotifyKeyspaceEvents)).append("\n");
    sb.append("    redisPersistence: ").append(toIndentedString(redisPersistence)).append("\n");
    sb.append("    redisAclChannelsDefault: ").append(toIndentedString(redisAclChannelsDefault)).append("\n");
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
    openapiFields.add("backup_hour");
    openapiFields.add("backup_minute");
    openapiFields.add("sql_mode");
    openapiFields.add("connect_timeout");
    openapiFields.add("default_time_zone");
    openapiFields.add("group_concat_max_len");
    openapiFields.add("information_schema_stats_expiry");
    openapiFields.add("innodb_ft_min_token_size");
    openapiFields.add("innodb_ft_server_stopword_table");
    openapiFields.add("innodb_lock_wait_timeout");
    openapiFields.add("innodb_log_buffer_size");
    openapiFields.add("innodb_online_alter_log_max_size");
    openapiFields.add("innodb_print_all_deadlocks");
    openapiFields.add("innodb_rollback_on_timeout");
    openapiFields.add("interactive_timeout");
    openapiFields.add("internal_tmp_mem_storage_engine");
    openapiFields.add("net_read_timeout");
    openapiFields.add("net_write_timeout");
    openapiFields.add("sql_require_primary_key");
    openapiFields.add("wait_timeout");
    openapiFields.add("max_allowed_packet");
    openapiFields.add("max_heap_table_size");
    openapiFields.add("sort_buffer_size");
    openapiFields.add("tmp_table_size");
    openapiFields.add("slow_query_log");
    openapiFields.add("long_query_time");
    openapiFields.add("binlog_retention_period");
    openapiFields.add("innodb_change_buffer_max_size");
    openapiFields.add("innodb_flush_neighbors");
    openapiFields.add("innodb_read_io_threads");
    openapiFields.add("innodb_write_io_threads");
    openapiFields.add("innodb_thread_concurrency");
    openapiFields.add("net_buffer_length");
    openapiFields.add("autovacuum_freeze_max_age");
    openapiFields.add("autovacuum_max_workers");
    openapiFields.add("autovacuum_naptime");
    openapiFields.add("autovacuum_vacuum_threshold");
    openapiFields.add("autovacuum_analyze_threshold");
    openapiFields.add("autovacuum_vacuum_scale_factor");
    openapiFields.add("autovacuum_analyze_scale_factor");
    openapiFields.add("autovacuum_vacuum_cost_delay");
    openapiFields.add("autovacuum_vacuum_cost_limit");
    openapiFields.add("bgwriter_delay");
    openapiFields.add("bgwriter_flush_after");
    openapiFields.add("bgwriter_lru_maxpages");
    openapiFields.add("bgwriter_lru_multiplier");
    openapiFields.add("deadlock_timeout");
    openapiFields.add("default_toast_compression");
    openapiFields.add("idle_in_transaction_session_timeout");
    openapiFields.add("jit");
    openapiFields.add("log_autovacuum_min_duration");
    openapiFields.add("log_error_verbosity");
    openapiFields.add("log_line_prefix");
    openapiFields.add("log_min_duration_statement");
    openapiFields.add("max_files_per_process");
    openapiFields.add("max_prepared_transactions");
    openapiFields.add("max_pred_locks_per_transaction");
    openapiFields.add("max_locks_per_transaction");
    openapiFields.add("max_stack_depth");
    openapiFields.add("max_standby_archive_delay");
    openapiFields.add("max_standby_streaming_delay");
    openapiFields.add("max_replication_slots");
    openapiFields.add("max_logical_replication_workers");
    openapiFields.add("max_parallel_workers");
    openapiFields.add("max_parallel_workers_per_gather");
    openapiFields.add("max_worker_processes");
    openapiFields.add("pg_partman_bgw.role");
    openapiFields.add("pg_partman_bgw.interval");
    openapiFields.add("pg_stat_statements.track");
    openapiFields.add("temp_file_limit");
    openapiFields.add("timezone");
    openapiFields.add("track_activity_query_size");
    openapiFields.add("track_commit_timestamp");
    openapiFields.add("track_functions");
    openapiFields.add("track_io_timing");
    openapiFields.add("max_wal_senders");
    openapiFields.add("wal_sender_timeout");
    openapiFields.add("wal_writer_delay");
    openapiFields.add("shared_buffers_percentage");
    openapiFields.add("pgbouncer");
    openapiFields.add("work_mem");
    openapiFields.add("timescaledb");
    openapiFields.add("synchronous_replication");
    openapiFields.add("stat_monitor_enable");
    openapiFields.add("redis_maxmemory_policy");
    openapiFields.add("redis_pubsub_client_output_buffer_limit");
    openapiFields.add("redis_number_of_databases");
    openapiFields.add("redis_io_threads");
    openapiFields.add("redis_lfu_log_factor");
    openapiFields.add("redis_lfu_decay_time");
    openapiFields.add("redis_ssl");
    openapiFields.add("redis_timeout");
    openapiFields.add("redis_notify_keyspace_events");
    openapiFields.add("redis_persistence");
    openapiFields.add("redis_acl_channels_default");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to Mpr1
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!Mpr1.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in Mpr1 is not found in the empty JSON string", Mpr1.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("sql_mode") != null && !jsonObj.get("sql_mode").isJsonNull()) && !jsonObj.get("sql_mode").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `sql_mode` to be a primitive type in the JSON string but got `%s`", jsonObj.get("sql_mode").toString()));
      }
      if ((jsonObj.get("default_time_zone") != null && !jsonObj.get("default_time_zone").isJsonNull()) && !jsonObj.get("default_time_zone").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `default_time_zone` to be a primitive type in the JSON string but got `%s`", jsonObj.get("default_time_zone").toString()));
      }
      if ((jsonObj.get("innodb_ft_server_stopword_table") != null && !jsonObj.get("innodb_ft_server_stopword_table").isJsonNull()) && !jsonObj.get("innodb_ft_server_stopword_table").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `innodb_ft_server_stopword_table` to be a primitive type in the JSON string but got `%s`", jsonObj.get("innodb_ft_server_stopword_table").toString()));
      }
      if ((jsonObj.get("internal_tmp_mem_storage_engine") != null && !jsonObj.get("internal_tmp_mem_storage_engine").isJsonNull()) && !jsonObj.get("internal_tmp_mem_storage_engine").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `internal_tmp_mem_storage_engine` to be a primitive type in the JSON string but got `%s`", jsonObj.get("internal_tmp_mem_storage_engine").toString()));
      }
      if ((jsonObj.get("default_toast_compression") != null && !jsonObj.get("default_toast_compression").isJsonNull()) && !jsonObj.get("default_toast_compression").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `default_toast_compression` to be a primitive type in the JSON string but got `%s`", jsonObj.get("default_toast_compression").toString()));
      }
      if ((jsonObj.get("log_error_verbosity") != null && !jsonObj.get("log_error_verbosity").isJsonNull()) && !jsonObj.get("log_error_verbosity").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `log_error_verbosity` to be a primitive type in the JSON string but got `%s`", jsonObj.get("log_error_verbosity").toString()));
      }
      if ((jsonObj.get("log_line_prefix") != null && !jsonObj.get("log_line_prefix").isJsonNull()) && !jsonObj.get("log_line_prefix").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `log_line_prefix` to be a primitive type in the JSON string but got `%s`", jsonObj.get("log_line_prefix").toString()));
      }
      if ((jsonObj.get("pg_partman_bgw.role") != null && !jsonObj.get("pg_partman_bgw.role").isJsonNull()) && !jsonObj.get("pg_partman_bgw.role").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `pg_partman_bgw.role` to be a primitive type in the JSON string but got `%s`", jsonObj.get("pg_partman_bgw.role").toString()));
      }
      if ((jsonObj.get("pg_stat_statements.track") != null && !jsonObj.get("pg_stat_statements.track").isJsonNull()) && !jsonObj.get("pg_stat_statements.track").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `pg_stat_statements.track` to be a primitive type in the JSON string but got `%s`", jsonObj.get("pg_stat_statements.track").toString()));
      }
      if ((jsonObj.get("timezone") != null && !jsonObj.get("timezone").isJsonNull()) && !jsonObj.get("timezone").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `timezone` to be a primitive type in the JSON string but got `%s`", jsonObj.get("timezone").toString()));
      }
      if ((jsonObj.get("track_commit_timestamp") != null && !jsonObj.get("track_commit_timestamp").isJsonNull()) && !jsonObj.get("track_commit_timestamp").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `track_commit_timestamp` to be a primitive type in the JSON string but got `%s`", jsonObj.get("track_commit_timestamp").toString()));
      }
      if ((jsonObj.get("track_functions") != null && !jsonObj.get("track_functions").isJsonNull()) && !jsonObj.get("track_functions").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `track_functions` to be a primitive type in the JSON string but got `%s`", jsonObj.get("track_functions").toString()));
      }
      if ((jsonObj.get("track_io_timing") != null && !jsonObj.get("track_io_timing").isJsonNull()) && !jsonObj.get("track_io_timing").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `track_io_timing` to be a primitive type in the JSON string but got `%s`", jsonObj.get("track_io_timing").toString()));
      }
      // validate the optional field `pgbouncer`
      if (jsonObj.get("pgbouncer") != null && !jsonObj.get("pgbouncer").isJsonNull()) {
        MysqlPgbouncer.validateJsonObject(jsonObj.getAsJsonObject("pgbouncer"));
      }
      // validate the optional field `timescaledb`
      if (jsonObj.get("timescaledb") != null && !jsonObj.get("timescaledb").isJsonNull()) {
        MysqlTimescaledb.validateJsonObject(jsonObj.getAsJsonObject("timescaledb"));
      }
      if ((jsonObj.get("synchronous_replication") != null && !jsonObj.get("synchronous_replication").isJsonNull()) && !jsonObj.get("synchronous_replication").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `synchronous_replication` to be a primitive type in the JSON string but got `%s`", jsonObj.get("synchronous_replication").toString()));
      }
      if ((jsonObj.get("redis_maxmemory_policy") != null && !jsonObj.get("redis_maxmemory_policy").isJsonNull()) && !jsonObj.get("redis_maxmemory_policy").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `redis_maxmemory_policy` to be a primitive type in the JSON string but got `%s`", jsonObj.get("redis_maxmemory_policy").toString()));
      }
      if ((jsonObj.get("redis_notify_keyspace_events") != null && !jsonObj.get("redis_notify_keyspace_events").isJsonNull()) && !jsonObj.get("redis_notify_keyspace_events").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `redis_notify_keyspace_events` to be a primitive type in the JSON string but got `%s`", jsonObj.get("redis_notify_keyspace_events").toString()));
      }
      if ((jsonObj.get("redis_persistence") != null && !jsonObj.get("redis_persistence").isJsonNull()) && !jsonObj.get("redis_persistence").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `redis_persistence` to be a primitive type in the JSON string but got `%s`", jsonObj.get("redis_persistence").toString()));
      }
      if ((jsonObj.get("redis_acl_channels_default") != null && !jsonObj.get("redis_acl_channels_default").isJsonNull()) && !jsonObj.get("redis_acl_channels_default").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `redis_acl_channels_default` to be a primitive type in the JSON string but got `%s`", jsonObj.get("redis_acl_channels_default").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!Mpr1.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'Mpr1' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<Mpr1> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(Mpr1.class));

       return (TypeAdapter<T>) new TypeAdapter<Mpr1>() {
           @Override
           public void write(JsonWriter out, Mpr1 value) throws IOException {
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
           public Mpr1 read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             Mpr1 instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of Mpr1 given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of Mpr1
  * @throws IOException if the JSON string is invalid with respect to Mpr1
  */
  public static Mpr1 fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, Mpr1.class);
  }

 /**
  * Convert an instance of Mpr1 to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

