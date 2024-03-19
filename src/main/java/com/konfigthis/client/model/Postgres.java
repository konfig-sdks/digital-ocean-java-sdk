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
import com.konfigthis.client.model.Pgbouncer;
import com.konfigthis.client.model.Timescaledb;
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
 * Postgres
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class Postgres {
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

  public static final String SERIALIZED_NAME_BACKUP_HOUR = "backup_hour";
  @SerializedName(SERIALIZED_NAME_BACKUP_HOUR)
  private Integer backupHour;

  public static final String SERIALIZED_NAME_BACKUP_MINUTE = "backup_minute";
  @SerializedName(SERIALIZED_NAME_BACKUP_MINUTE)
  private Integer backupMinute;

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
  private Pgbouncer pgbouncer;

  public static final String SERIALIZED_NAME_WORK_MEM = "work_mem";
  @SerializedName(SERIALIZED_NAME_WORK_MEM)
  private Integer workMem;

  public static final String SERIALIZED_NAME_TIMESCALEDB = "timescaledb";
  @SerializedName(SERIALIZED_NAME_TIMESCALEDB)
  private Timescaledb timescaledb;

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

  public Postgres() {
  }

  public Postgres autovacuumFreezeMaxAge(Integer autovacuumFreezeMaxAge) {
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


  public Postgres autovacuumMaxWorkers(Integer autovacuumMaxWorkers) {
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


  public Postgres autovacuumNaptime(Integer autovacuumNaptime) {
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


  public Postgres autovacuumVacuumThreshold(Integer autovacuumVacuumThreshold) {
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


  public Postgres autovacuumAnalyzeThreshold(Integer autovacuumAnalyzeThreshold) {
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


  public Postgres autovacuumVacuumScaleFactor(Double autovacuumVacuumScaleFactor) {
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumVacuumScaleFactor != null && autovacuumVacuumScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumVacuumScaleFactor. Must be less than or equal to 1.");
    }
    
    
    this.autovacuumVacuumScaleFactor = autovacuumVacuumScaleFactor;
    return this;
  }

  public Postgres autovacuumVacuumScaleFactor(Integer autovacuumVacuumScaleFactor) {
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


  public Postgres autovacuumAnalyzeScaleFactor(Double autovacuumAnalyzeScaleFactor) {
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor < 0) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be greater than or equal to 0.");
    }
    if (autovacuumAnalyzeScaleFactor != null && autovacuumAnalyzeScaleFactor > 1) {
      throw new IllegalArgumentException("Invalid value for autovacuumAnalyzeScaleFactor. Must be less than or equal to 1.");
    }
    
    
    this.autovacuumAnalyzeScaleFactor = autovacuumAnalyzeScaleFactor;
    return this;
  }

  public Postgres autovacuumAnalyzeScaleFactor(Integer autovacuumAnalyzeScaleFactor) {
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


  public Postgres autovacuumVacuumCostDelay(Integer autovacuumVacuumCostDelay) {
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


  public Postgres autovacuumVacuumCostLimit(Integer autovacuumVacuumCostLimit) {
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


  public Postgres backupHour(Integer backupHour) {
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


  public Postgres backupMinute(Integer backupMinute) {
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
   * The minute of the backup hour when backup for the service starts. New backup is only started if previous backup has already completed.
   * minimum: 0
   * maximum: 59
   * @return backupMinute
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "30", value = "The minute of the backup hour when backup for the service starts. New backup is only started if previous backup has already completed.")

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


  public Postgres bgwriterDelay(Integer bgwriterDelay) {
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


  public Postgres bgwriterFlushAfter(Integer bgwriterFlushAfter) {
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


  public Postgres bgwriterLruMaxpages(Integer bgwriterLruMaxpages) {
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


  public Postgres bgwriterLruMultiplier(Double bgwriterLruMultiplier) {
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier < 0) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be greater than or equal to 0.");
    }
    if (bgwriterLruMultiplier != null && bgwriterLruMultiplier > 10) {
      throw new IllegalArgumentException("Invalid value for bgwriterLruMultiplier. Must be less than or equal to 10.");
    }
    
    
    this.bgwriterLruMultiplier = bgwriterLruMultiplier;
    return this;
  }

  public Postgres bgwriterLruMultiplier(Integer bgwriterLruMultiplier) {
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


  public Postgres deadlockTimeout(Integer deadlockTimeout) {
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


  public Postgres defaultToastCompression(DefaultToastCompressionEnum defaultToastCompression) {
    
    
    
    
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


  public Postgres idleInTransactionSessionTimeout(Integer idleInTransactionSessionTimeout) {
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


  public Postgres jit(Boolean jit) {
    
    
    
    
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


  public Postgres logAutovacuumMinDuration(Integer logAutovacuumMinDuration) {
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


  public Postgres logErrorVerbosity(LogErrorVerbosityEnum logErrorVerbosity) {
    
    
    
    
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


  public Postgres logLinePrefix(LogLinePrefixEnum logLinePrefix) {
    
    
    
    
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


  public Postgres logMinDurationStatement(Integer logMinDurationStatement) {
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


  public Postgres maxFilesPerProcess(Integer maxFilesPerProcess) {
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


  public Postgres maxPreparedTransactions(Integer maxPreparedTransactions) {
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


  public Postgres maxPredLocksPerTransaction(Integer maxPredLocksPerTransaction) {
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


  public Postgres maxLocksPerTransaction(Integer maxLocksPerTransaction) {
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


  public Postgres maxStackDepth(Integer maxStackDepth) {
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


  public Postgres maxStandbyArchiveDelay(Integer maxStandbyArchiveDelay) {
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


  public Postgres maxStandbyStreamingDelay(Integer maxStandbyStreamingDelay) {
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


  public Postgres maxReplicationSlots(Integer maxReplicationSlots) {
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


  public Postgres maxLogicalReplicationWorkers(Integer maxLogicalReplicationWorkers) {
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


  public Postgres maxParallelWorkers(Integer maxParallelWorkers) {
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


  public Postgres maxParallelWorkersPerGather(Integer maxParallelWorkersPerGather) {
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


  public Postgres maxWorkerProcesses(Integer maxWorkerProcesses) {
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


  public Postgres pgPartmanBgwRole(String pgPartmanBgwRole) {
    
    
    
    
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


  public Postgres pgPartmanBgwInterval(Integer pgPartmanBgwInterval) {
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


  public Postgres pgStatStatementsTrack(PgStatStatementsTrackEnum pgStatStatementsTrack) {
    
    
    
    
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


  public Postgres tempFileLimit(Integer tempFileLimit) {
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


  public Postgres timezone(String timezone) {
    
    
    
    
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


  public Postgres trackActivityQuerySize(Integer trackActivityQuerySize) {
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


  public Postgres trackCommitTimestamp(TrackCommitTimestampEnum trackCommitTimestamp) {
    
    
    
    
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


  public Postgres trackFunctions(TrackFunctionsEnum trackFunctions) {
    
    
    
    
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


  public Postgres trackIoTiming(TrackIoTimingEnum trackIoTiming) {
    
    
    
    
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


  public Postgres maxWalSenders(Integer maxWalSenders) {
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


  public Postgres walSenderTimeout(Integer walSenderTimeout) {
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


  public Postgres walWriterDelay(Integer walWriterDelay) {
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


  public Postgres sharedBuffersPercentage(Double sharedBuffersPercentage) {
    if (sharedBuffersPercentage != null && sharedBuffersPercentage < 20) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be greater than or equal to 20.");
    }
    if (sharedBuffersPercentage != null && sharedBuffersPercentage > 60) {
      throw new IllegalArgumentException("Invalid value for sharedBuffersPercentage. Must be less than or equal to 60.");
    }
    
    
    this.sharedBuffersPercentage = sharedBuffersPercentage;
    return this;
  }

  public Postgres sharedBuffersPercentage(Integer sharedBuffersPercentage) {
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


  public Postgres pgbouncer(Pgbouncer pgbouncer) {
    
    
    
    
    this.pgbouncer = pgbouncer;
    return this;
  }

   /**
   * Get pgbouncer
   * @return pgbouncer
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Pgbouncer getPgbouncer() {
    return pgbouncer;
  }


  public void setPgbouncer(Pgbouncer pgbouncer) {
    
    
    
    this.pgbouncer = pgbouncer;
  }


  public Postgres workMem(Integer workMem) {
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


  public Postgres timescaledb(Timescaledb timescaledb) {
    
    
    
    
    this.timescaledb = timescaledb;
    return this;
  }

   /**
   * Get timescaledb
   * @return timescaledb
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public Timescaledb getTimescaledb() {
    return timescaledb;
  }


  public void setTimescaledb(Timescaledb timescaledb) {
    
    
    
    this.timescaledb = timescaledb;
  }


  public Postgres synchronousReplication(SynchronousReplicationEnum synchronousReplication) {
    
    
    
    
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


  public Postgres statMonitorEnable(Boolean statMonitorEnable) {
    
    
    
    
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
   * @return the Postgres instance itself
   */
  public Postgres putAdditionalProperty(String key, Object value) {
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
    Postgres postgres = (Postgres) o;
    return Objects.equals(this.autovacuumFreezeMaxAge, postgres.autovacuumFreezeMaxAge) &&
        Objects.equals(this.autovacuumMaxWorkers, postgres.autovacuumMaxWorkers) &&
        Objects.equals(this.autovacuumNaptime, postgres.autovacuumNaptime) &&
        Objects.equals(this.autovacuumVacuumThreshold, postgres.autovacuumVacuumThreshold) &&
        Objects.equals(this.autovacuumAnalyzeThreshold, postgres.autovacuumAnalyzeThreshold) &&
        Objects.equals(this.autovacuumVacuumScaleFactor, postgres.autovacuumVacuumScaleFactor) &&
        Objects.equals(this.autovacuumAnalyzeScaleFactor, postgres.autovacuumAnalyzeScaleFactor) &&
        Objects.equals(this.autovacuumVacuumCostDelay, postgres.autovacuumVacuumCostDelay) &&
        Objects.equals(this.autovacuumVacuumCostLimit, postgres.autovacuumVacuumCostLimit) &&
        Objects.equals(this.backupHour, postgres.backupHour) &&
        Objects.equals(this.backupMinute, postgres.backupMinute) &&
        Objects.equals(this.bgwriterDelay, postgres.bgwriterDelay) &&
        Objects.equals(this.bgwriterFlushAfter, postgres.bgwriterFlushAfter) &&
        Objects.equals(this.bgwriterLruMaxpages, postgres.bgwriterLruMaxpages) &&
        Objects.equals(this.bgwriterLruMultiplier, postgres.bgwriterLruMultiplier) &&
        Objects.equals(this.deadlockTimeout, postgres.deadlockTimeout) &&
        Objects.equals(this.defaultToastCompression, postgres.defaultToastCompression) &&
        Objects.equals(this.idleInTransactionSessionTimeout, postgres.idleInTransactionSessionTimeout) &&
        Objects.equals(this.jit, postgres.jit) &&
        Objects.equals(this.logAutovacuumMinDuration, postgres.logAutovacuumMinDuration) &&
        Objects.equals(this.logErrorVerbosity, postgres.logErrorVerbosity) &&
        Objects.equals(this.logLinePrefix, postgres.logLinePrefix) &&
        Objects.equals(this.logMinDurationStatement, postgres.logMinDurationStatement) &&
        Objects.equals(this.maxFilesPerProcess, postgres.maxFilesPerProcess) &&
        Objects.equals(this.maxPreparedTransactions, postgres.maxPreparedTransactions) &&
        Objects.equals(this.maxPredLocksPerTransaction, postgres.maxPredLocksPerTransaction) &&
        Objects.equals(this.maxLocksPerTransaction, postgres.maxLocksPerTransaction) &&
        Objects.equals(this.maxStackDepth, postgres.maxStackDepth) &&
        Objects.equals(this.maxStandbyArchiveDelay, postgres.maxStandbyArchiveDelay) &&
        Objects.equals(this.maxStandbyStreamingDelay, postgres.maxStandbyStreamingDelay) &&
        Objects.equals(this.maxReplicationSlots, postgres.maxReplicationSlots) &&
        Objects.equals(this.maxLogicalReplicationWorkers, postgres.maxLogicalReplicationWorkers) &&
        Objects.equals(this.maxParallelWorkers, postgres.maxParallelWorkers) &&
        Objects.equals(this.maxParallelWorkersPerGather, postgres.maxParallelWorkersPerGather) &&
        Objects.equals(this.maxWorkerProcesses, postgres.maxWorkerProcesses) &&
        Objects.equals(this.pgPartmanBgwRole, postgres.pgPartmanBgwRole) &&
        Objects.equals(this.pgPartmanBgwInterval, postgres.pgPartmanBgwInterval) &&
        Objects.equals(this.pgStatStatementsTrack, postgres.pgStatStatementsTrack) &&
        Objects.equals(this.tempFileLimit, postgres.tempFileLimit) &&
        Objects.equals(this.timezone, postgres.timezone) &&
        Objects.equals(this.trackActivityQuerySize, postgres.trackActivityQuerySize) &&
        Objects.equals(this.trackCommitTimestamp, postgres.trackCommitTimestamp) &&
        Objects.equals(this.trackFunctions, postgres.trackFunctions) &&
        Objects.equals(this.trackIoTiming, postgres.trackIoTiming) &&
        Objects.equals(this.maxWalSenders, postgres.maxWalSenders) &&
        Objects.equals(this.walSenderTimeout, postgres.walSenderTimeout) &&
        Objects.equals(this.walWriterDelay, postgres.walWriterDelay) &&
        Objects.equals(this.sharedBuffersPercentage, postgres.sharedBuffersPercentage) &&
        Objects.equals(this.pgbouncer, postgres.pgbouncer) &&
        Objects.equals(this.workMem, postgres.workMem) &&
        Objects.equals(this.timescaledb, postgres.timescaledb) &&
        Objects.equals(this.synchronousReplication, postgres.synchronousReplication) &&
        Objects.equals(this.statMonitorEnable, postgres.statMonitorEnable)&&
        Objects.equals(this.additionalProperties, postgres.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(autovacuumFreezeMaxAge, autovacuumMaxWorkers, autovacuumNaptime, autovacuumVacuumThreshold, autovacuumAnalyzeThreshold, autovacuumVacuumScaleFactor, autovacuumAnalyzeScaleFactor, autovacuumVacuumCostDelay, autovacuumVacuumCostLimit, backupHour, backupMinute, bgwriterDelay, bgwriterFlushAfter, bgwriterLruMaxpages, bgwriterLruMultiplier, deadlockTimeout, defaultToastCompression, idleInTransactionSessionTimeout, jit, logAutovacuumMinDuration, logErrorVerbosity, logLinePrefix, logMinDurationStatement, maxFilesPerProcess, maxPreparedTransactions, maxPredLocksPerTransaction, maxLocksPerTransaction, maxStackDepth, maxStandbyArchiveDelay, maxStandbyStreamingDelay, maxReplicationSlots, maxLogicalReplicationWorkers, maxParallelWorkers, maxParallelWorkersPerGather, maxWorkerProcesses, pgPartmanBgwRole, pgPartmanBgwInterval, pgStatStatementsTrack, tempFileLimit, timezone, trackActivityQuerySize, trackCommitTimestamp, trackFunctions, trackIoTiming, maxWalSenders, walSenderTimeout, walWriterDelay, sharedBuffersPercentage, pgbouncer, workMem, timescaledb, synchronousReplication, statMonitorEnable, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Postgres {\n");
    sb.append("    autovacuumFreezeMaxAge: ").append(toIndentedString(autovacuumFreezeMaxAge)).append("\n");
    sb.append("    autovacuumMaxWorkers: ").append(toIndentedString(autovacuumMaxWorkers)).append("\n");
    sb.append("    autovacuumNaptime: ").append(toIndentedString(autovacuumNaptime)).append("\n");
    sb.append("    autovacuumVacuumThreshold: ").append(toIndentedString(autovacuumVacuumThreshold)).append("\n");
    sb.append("    autovacuumAnalyzeThreshold: ").append(toIndentedString(autovacuumAnalyzeThreshold)).append("\n");
    sb.append("    autovacuumVacuumScaleFactor: ").append(toIndentedString(autovacuumVacuumScaleFactor)).append("\n");
    sb.append("    autovacuumAnalyzeScaleFactor: ").append(toIndentedString(autovacuumAnalyzeScaleFactor)).append("\n");
    sb.append("    autovacuumVacuumCostDelay: ").append(toIndentedString(autovacuumVacuumCostDelay)).append("\n");
    sb.append("    autovacuumVacuumCostLimit: ").append(toIndentedString(autovacuumVacuumCostLimit)).append("\n");
    sb.append("    backupHour: ").append(toIndentedString(backupHour)).append("\n");
    sb.append("    backupMinute: ").append(toIndentedString(backupMinute)).append("\n");
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
    openapiFields.add("autovacuum_freeze_max_age");
    openapiFields.add("autovacuum_max_workers");
    openapiFields.add("autovacuum_naptime");
    openapiFields.add("autovacuum_vacuum_threshold");
    openapiFields.add("autovacuum_analyze_threshold");
    openapiFields.add("autovacuum_vacuum_scale_factor");
    openapiFields.add("autovacuum_analyze_scale_factor");
    openapiFields.add("autovacuum_vacuum_cost_delay");
    openapiFields.add("autovacuum_vacuum_cost_limit");
    openapiFields.add("backup_hour");
    openapiFields.add("backup_minute");
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

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to Postgres
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!Postgres.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in Postgres is not found in the empty JSON string", Postgres.openapiRequiredFields.toString()));
        }
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
        Pgbouncer.validateJsonObject(jsonObj.getAsJsonObject("pgbouncer"));
      }
      // validate the optional field `timescaledb`
      if (jsonObj.get("timescaledb") != null && !jsonObj.get("timescaledb").isJsonNull()) {
        Timescaledb.validateJsonObject(jsonObj.getAsJsonObject("timescaledb"));
      }
      if ((jsonObj.get("synchronous_replication") != null && !jsonObj.get("synchronous_replication").isJsonNull()) && !jsonObj.get("synchronous_replication").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `synchronous_replication` to be a primitive type in the JSON string but got `%s`", jsonObj.get("synchronous_replication").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!Postgres.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'Postgres' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<Postgres> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(Postgres.class));

       return (TypeAdapter<T>) new TypeAdapter<Postgres>() {
           @Override
           public void write(JsonWriter out, Postgres value) throws IOException {
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
           public Postgres read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             Postgres instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of Postgres given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of Postgres
  * @throws IOException if the JSON string is invalid with respect to Postgres
  */
  public static Postgres fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, Postgres.class);
  }

 /**
  * Convert an instance of Postgres to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

