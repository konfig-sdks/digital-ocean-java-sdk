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
 * KafkaTopicConfig
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class KafkaTopicConfig {
  /**
   * The cleanup_policy sets the retention policy to use on log segments. &#39;delete&#39; will discard old segments when retention time/size limits are reached. &#39;compact&#39; will enable log compaction, resulting in retention of the latest value for each key.
   */
  @JsonAdapter(CleanupPolicyEnum.Adapter.class)
 public enum CleanupPolicyEnum {
    DELETE("delete"),
    
    COMPACT("compact"),
    
    COMPACT_DELETE("compact_delete");

    private String value;

    CleanupPolicyEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static CleanupPolicyEnum fromValue(String value) {
      for (CleanupPolicyEnum b : CleanupPolicyEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<CleanupPolicyEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CleanupPolicyEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public CleanupPolicyEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return CleanupPolicyEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_CLEANUP_POLICY = "cleanup_policy";
  @SerializedName(SERIALIZED_NAME_CLEANUP_POLICY)
  private CleanupPolicyEnum cleanupPolicy = CleanupPolicyEnum.DELETE;

  /**
   * The compression_type specifies the compression type of the topic.
   */
  @JsonAdapter(CompressionTypeEnum.Adapter.class)
 public enum CompressionTypeEnum {
    PRODUCER("producer"),
    
    GZIP("gzip"),
    
    SNAPPY("snappy"),
    
    IZ4("Iz4"),
    
    ZSTD("zstd"),
    
    UNCOMPRESSED("uncompressed");

    private String value;

    CompressionTypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static CompressionTypeEnum fromValue(String value) {
      for (CompressionTypeEnum b : CompressionTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<CompressionTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final CompressionTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public CompressionTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return CompressionTypeEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_COMPRESSION_TYPE = "compression_type";
  @SerializedName(SERIALIZED_NAME_COMPRESSION_TYPE)
  private CompressionTypeEnum compressionType = CompressionTypeEnum.PRODUCER;

  public static final String SERIALIZED_NAME_DELETE_RETENTION_MS = "delete_retention_ms";
  @SerializedName(SERIALIZED_NAME_DELETE_RETENTION_MS)
  private Integer deleteRetentionMs = 86400000;

  public static final String SERIALIZED_NAME_FILE_DELETE_DELAY_MS = "file_delete_delay_ms";
  @SerializedName(SERIALIZED_NAME_FILE_DELETE_DELAY_MS)
  private Integer fileDeleteDelayMs = 60000;

  public static final String SERIALIZED_NAME_FLUSH_MESSAGES = "flush_messages";
  @SerializedName(SERIALIZED_NAME_FLUSH_MESSAGES)
  private Integer flushMessages;

  public static final String SERIALIZED_NAME_FLUSH_MS = "flush_ms";
  @SerializedName(SERIALIZED_NAME_FLUSH_MS)
  private Integer flushMs;

  public static final String SERIALIZED_NAME_INDEX_INTERVAL_BYTES = "index_interval_bytes";
  @SerializedName(SERIALIZED_NAME_INDEX_INTERVAL_BYTES)
  private Integer indexIntervalBytes = 4096;

  public static final String SERIALIZED_NAME_MAX_COMPACTION_LAG_MS = "max_compaction_lag_ms";
  @SerializedName(SERIALIZED_NAME_MAX_COMPACTION_LAG_MS)
  private Integer maxCompactionLagMs;

  public static final String SERIALIZED_NAME_MAX_MESSAGE_BYTES = "max_message_bytes";
  @SerializedName(SERIALIZED_NAME_MAX_MESSAGE_BYTES)
  private Integer maxMessageBytes = 1048588;

  public static final String SERIALIZED_NAME_MESSAGE_DOWN_CONVERSION_ENABLE = "message_down_conversion_enable";
  @SerializedName(SERIALIZED_NAME_MESSAGE_DOWN_CONVERSION_ENABLE)
  private Boolean messageDownConversionEnable = true;

  /**
   * The message_format_version specifies the message format version used by the broker to append messages to the logs. The value of this setting is assumed to be 3.0-IV1 if the broker protocol version is 3.0 or higher. By setting a  particular message format version, all existing messages on disk must be smaller or equal to the specified version.
   */
  @JsonAdapter(MessageFormatVersionEnum.Adapter.class)
 public enum MessageFormatVersionEnum {
    _0_8_0("0.8.0"),
    
    _0_8_1("0.8.1"),
    
    _0_8_2("0.8.2"),
    
    _0_9_0("0.9.0"),
    
    _0_10_0_IV0("0.10.0-IV0"),
    
    _0_10_0_IV1("0.10.0-IV1"),
    
    _0_10_1_IV0("0.10.1-IV0"),
    
    _0_10_1_IV1("0.10.1-IV1"),
    
    _0_10_1_IV2("0.10.1-IV2"),
    
    _0_10_2_IV0("0.10.2-IV0"),
    
    _0_11_0_IV0("0.11.0-IV0"),
    
    _0_11_0_IV1("0.11.0-IV1"),
    
    _0_11_0_IV2("0.11.0-IV2"),
    
    _1_0_IV0("1.0-IV0"),
    
    _1_1_IV0("1.1-IV0"),
    
    _2_0_IV0("2.0-IV0"),
    
    _2_0_IV1("2.0-IV1"),
    
    _2_1_IV0("2.1-IV0"),
    
    _2_1_IV1("2.1-IV1"),
    
    _2_1_IV2("2.1-IV2"),
    
    _2_2_IV0("2.2-IV0"),
    
    _2_2_IV1("2.2-IV1"),
    
    _2_3_IV0("2.3-IV0"),
    
    _2_3_IV1("2.3-IV1"),
    
    _2_4_IV0("2.4-IV0"),
    
    _2_4_IV1("2.4-IV1"),
    
    _2_5_IV0("2.5-IV0"),
    
    _2_6_IV0("2.6-IV0"),
    
    _2_7_IV0("2.7-IV0"),
    
    _2_7_IV1("2.7-IV1"),
    
    _2_7_IV2("2.7-IV2"),
    
    _2_8_IV0("2.8-IV0"),
    
    _2_8_IV1("2.8-IV1"),
    
    _3_0_IV0("3.0-IV0"),
    
    _3_0_IV1("3.0-IV1"),
    
    _3_1_IV0("3.1-IV0"),
    
    _3_2_IV0("3.2-IV0"),
    
    _3_3_IV0("3.3-IV0"),
    
    _3_3_IV1("3.3-IV1"),
    
    _3_3_IV2("3.3-IV2"),
    
    _3_3_IV3("3.3-IV3");

    private String value;

    MessageFormatVersionEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static MessageFormatVersionEnum fromValue(String value) {
      for (MessageFormatVersionEnum b : MessageFormatVersionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<MessageFormatVersionEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MessageFormatVersionEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public MessageFormatVersionEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return MessageFormatVersionEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_MESSAGE_FORMAT_VERSION = "message_format_version";
  @SerializedName(SERIALIZED_NAME_MESSAGE_FORMAT_VERSION)
  private MessageFormatVersionEnum messageFormatVersion = MessageFormatVersionEnum._3_0_IV1;

  /**
   * The message_timestamp_type specifies whether to use the message create time or log append time as the timestamp on a message.
   */
  @JsonAdapter(MessageTimestampTypeEnum.Adapter.class)
 public enum MessageTimestampTypeEnum {
    CREATE_TIME("create_time"),
    
    LOG_APPEND_TIME("log_append_time");

    private String value;

    MessageTimestampTypeEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static MessageTimestampTypeEnum fromValue(String value) {
      for (MessageTimestampTypeEnum b : MessageTimestampTypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<MessageTimestampTypeEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final MessageTimestampTypeEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public MessageTimestampTypeEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return MessageTimestampTypeEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_MESSAGE_TIMESTAMP_TYPE = "message_timestamp_type";
  @SerializedName(SERIALIZED_NAME_MESSAGE_TIMESTAMP_TYPE)
  private MessageTimestampTypeEnum messageTimestampType = MessageTimestampTypeEnum.CREATE_TIME;

  public static final String SERIALIZED_NAME_MIN_CLEANABLE_DIRTY_RATIO = "min_cleanable_dirty_ratio";
  @SerializedName(SERIALIZED_NAME_MIN_CLEANABLE_DIRTY_RATIO)
  private Float minCleanableDirtyRatio = 0.5f;

  public static final String SERIALIZED_NAME_MIN_COMPACTION_LAG_MS = "min_compaction_lag_ms";
  @SerializedName(SERIALIZED_NAME_MIN_COMPACTION_LAG_MS)
  private Integer minCompactionLagMs = 0;

  public static final String SERIALIZED_NAME_MIN_INSYNC_REPLICAS = "min_insync_replicas";
  @SerializedName(SERIALIZED_NAME_MIN_INSYNC_REPLICAS)
  private Integer minInsyncReplicas = 1;

  public static final String SERIALIZED_NAME_PREALLOCATE = "preallocate";
  @SerializedName(SERIALIZED_NAME_PREALLOCATE)
  private Boolean preallocate = false;

  public static final String SERIALIZED_NAME_RETENTION_BYTES = "retention_bytes";
  @SerializedName(SERIALIZED_NAME_RETENTION_BYTES)
  private Integer retentionBytes = -1;

  public static final String SERIALIZED_NAME_RETENTION_MS = "retention_ms";
  @SerializedName(SERIALIZED_NAME_RETENTION_MS)
  private Integer retentionMs = 604800000;

  public static final String SERIALIZED_NAME_SEGMENT_BYTES = "segment_bytes";
  @SerializedName(SERIALIZED_NAME_SEGMENT_BYTES)
  private Integer segmentBytes = 209715200;

  public static final String SERIALIZED_NAME_SEGMENT_JITTER_MS = "segment_jitter_ms";
  @SerializedName(SERIALIZED_NAME_SEGMENT_JITTER_MS)
  private Integer segmentJitterMs = 0;

  public static final String SERIALIZED_NAME_SEGMENT_MS = "segment_ms";
  @SerializedName(SERIALIZED_NAME_SEGMENT_MS)
  private Integer segmentMs = 604800000;

  public KafkaTopicConfig() {
  }

  public KafkaTopicConfig cleanupPolicy(CleanupPolicyEnum cleanupPolicy) {
    
    
    
    
    this.cleanupPolicy = cleanupPolicy;
    return this;
  }

   /**
   * The cleanup_policy sets the retention policy to use on log segments. &#39;delete&#39; will discard old segments when retention time/size limits are reached. &#39;compact&#39; will enable log compaction, resulting in retention of the latest value for each key.
   * @return cleanupPolicy
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "DELETE", value = "The cleanup_policy sets the retention policy to use on log segments. 'delete' will discard old segments when retention time/size limits are reached. 'compact' will enable log compaction, resulting in retention of the latest value for each key.")

  public CleanupPolicyEnum getCleanupPolicy() {
    return cleanupPolicy;
  }


  public void setCleanupPolicy(CleanupPolicyEnum cleanupPolicy) {
    
    
    
    this.cleanupPolicy = cleanupPolicy;
  }


  public KafkaTopicConfig compressionType(CompressionTypeEnum compressionType) {
    
    
    
    
    this.compressionType = compressionType;
    return this;
  }

   /**
   * The compression_type specifies the compression type of the topic.
   * @return compressionType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "PRODUCER", value = "The compression_type specifies the compression type of the topic.")

  public CompressionTypeEnum getCompressionType() {
    return compressionType;
  }


  public void setCompressionType(CompressionTypeEnum compressionType) {
    
    
    
    this.compressionType = compressionType;
  }


  public KafkaTopicConfig deleteRetentionMs(Integer deleteRetentionMs) {
    
    
    
    
    this.deleteRetentionMs = deleteRetentionMs;
    return this;
  }

   /**
   * The delete_retention_ms specifies how long (in ms) to retain delete tombstone markers for topics.
   * @return deleteRetentionMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "86400000", value = "The delete_retention_ms specifies how long (in ms) to retain delete tombstone markers for topics.")

  public Integer getDeleteRetentionMs() {
    return deleteRetentionMs;
  }


  public void setDeleteRetentionMs(Integer deleteRetentionMs) {
    
    
    
    this.deleteRetentionMs = deleteRetentionMs;
  }


  public KafkaTopicConfig fileDeleteDelayMs(Integer fileDeleteDelayMs) {
    
    
    
    
    this.fileDeleteDelayMs = fileDeleteDelayMs;
    return this;
  }

   /**
   * The file_delete_delay_ms specifies the time (in ms) to wait before deleting a file from the filesystem.
   * @return fileDeleteDelayMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "60000", value = "The file_delete_delay_ms specifies the time (in ms) to wait before deleting a file from the filesystem.")

  public Integer getFileDeleteDelayMs() {
    return fileDeleteDelayMs;
  }


  public void setFileDeleteDelayMs(Integer fileDeleteDelayMs) {
    
    
    
    this.fileDeleteDelayMs = fileDeleteDelayMs;
  }


  public KafkaTopicConfig flushMessages(Integer flushMessages) {
    
    
    
    
    this.flushMessages = flushMessages;
    return this;
  }

   /**
   * The flush_messages specifies the number of messages to accumulate on a log partition before messages are flushed to disk.
   * @return flushMessages
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The flush_messages specifies the number of messages to accumulate on a log partition before messages are flushed to disk.")

  public Integer getFlushMessages() {
    return flushMessages;
  }


  public void setFlushMessages(Integer flushMessages) {
    
    
    
    this.flushMessages = flushMessages;
  }


  public KafkaTopicConfig flushMs(Integer flushMs) {
    
    
    
    
    this.flushMs = flushMs;
    return this;
  }

   /**
   * The flush_ms specifies the maximum time (in ms) that a message is kept in memory before being flushed to disk.
   * @return flushMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The flush_ms specifies the maximum time (in ms) that a message is kept in memory before being flushed to disk.")

  public Integer getFlushMs() {
    return flushMs;
  }


  public void setFlushMs(Integer flushMs) {
    
    
    
    this.flushMs = flushMs;
  }


  public KafkaTopicConfig indexIntervalBytes(Integer indexIntervalBytes) {
    
    
    
    
    this.indexIntervalBytes = indexIntervalBytes;
    return this;
  }

   /**
   * The index_interval_bytes specifies the number of bytes between entries being added into te offset index.
   * @return indexIntervalBytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "4096", value = "The index_interval_bytes specifies the number of bytes between entries being added into te offset index.")

  public Integer getIndexIntervalBytes() {
    return indexIntervalBytes;
  }


  public void setIndexIntervalBytes(Integer indexIntervalBytes) {
    
    
    
    this.indexIntervalBytes = indexIntervalBytes;
  }


  public KafkaTopicConfig maxCompactionLagMs(Integer maxCompactionLagMs) {
    
    
    
    
    this.maxCompactionLagMs = maxCompactionLagMs;
    return this;
  }

   /**
   * The max_compaction_lag_ms specifies the maximum amount of time (in ms) that a message will remain uncompacted. This is only applicable if the logs are have compaction enabled.
   * @return maxCompactionLagMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "The max_compaction_lag_ms specifies the maximum amount of time (in ms) that a message will remain uncompacted. This is only applicable if the logs are have compaction enabled.")

  public Integer getMaxCompactionLagMs() {
    return maxCompactionLagMs;
  }


  public void setMaxCompactionLagMs(Integer maxCompactionLagMs) {
    
    
    
    this.maxCompactionLagMs = maxCompactionLagMs;
  }


  public KafkaTopicConfig maxMessageBytes(Integer maxMessageBytes) {
    
    
    
    
    this.maxMessageBytes = maxMessageBytes;
    return this;
  }

   /**
   * The max_messages_bytes specifies the largest record batch size (in bytes) that can be sent to the server.  This is calculated after compression if compression is enabled.
   * @return maxMessageBytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1048588", value = "The max_messages_bytes specifies the largest record batch size (in bytes) that can be sent to the server.  This is calculated after compression if compression is enabled.")

  public Integer getMaxMessageBytes() {
    return maxMessageBytes;
  }


  public void setMaxMessageBytes(Integer maxMessageBytes) {
    
    
    
    this.maxMessageBytes = maxMessageBytes;
  }


  public KafkaTopicConfig messageDownConversionEnable(Boolean messageDownConversionEnable) {
    
    
    
    
    this.messageDownConversionEnable = messageDownConversionEnable;
    return this;
  }

   /**
   * The message_down_conversion_enable specifies whether down-conversion of message formats is enabled to satisfy consumer requests. When &#39;false&#39;, the broker will not perform conversion for consumers expecting older message formats. The broker will respond with an &#x60;UNSUPPORTED_VERSION&#x60; error for consume requests from these older clients.
   * @return messageDownConversionEnable
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "true", value = "The message_down_conversion_enable specifies whether down-conversion of message formats is enabled to satisfy consumer requests. When 'false', the broker will not perform conversion for consumers expecting older message formats. The broker will respond with an `UNSUPPORTED_VERSION` error for consume requests from these older clients.")

  public Boolean getMessageDownConversionEnable() {
    return messageDownConversionEnable;
  }


  public void setMessageDownConversionEnable(Boolean messageDownConversionEnable) {
    
    
    
    this.messageDownConversionEnable = messageDownConversionEnable;
  }


  public KafkaTopicConfig messageFormatVersion(MessageFormatVersionEnum messageFormatVersion) {
    
    
    
    
    this.messageFormatVersion = messageFormatVersion;
    return this;
  }

   /**
   * The message_format_version specifies the message format version used by the broker to append messages to the logs. The value of this setting is assumed to be 3.0-IV1 if the broker protocol version is 3.0 or higher. By setting a  particular message format version, all existing messages on disk must be smaller or equal to the specified version.
   * @return messageFormatVersion
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "_3_0_IV1", value = "The message_format_version specifies the message format version used by the broker to append messages to the logs. The value of this setting is assumed to be 3.0-IV1 if the broker protocol version is 3.0 or higher. By setting a  particular message format version, all existing messages on disk must be smaller or equal to the specified version.")

  public MessageFormatVersionEnum getMessageFormatVersion() {
    return messageFormatVersion;
  }


  public void setMessageFormatVersion(MessageFormatVersionEnum messageFormatVersion) {
    
    
    
    this.messageFormatVersion = messageFormatVersion;
  }


  public KafkaTopicConfig messageTimestampType(MessageTimestampTypeEnum messageTimestampType) {
    
    
    
    
    this.messageTimestampType = messageTimestampType;
    return this;
  }

   /**
   * The message_timestamp_type specifies whether to use the message create time or log append time as the timestamp on a message.
   * @return messageTimestampType
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "CREATE_TIME", value = "The message_timestamp_type specifies whether to use the message create time or log append time as the timestamp on a message.")

  public MessageTimestampTypeEnum getMessageTimestampType() {
    return messageTimestampType;
  }


  public void setMessageTimestampType(MessageTimestampTypeEnum messageTimestampType) {
    
    
    
    this.messageTimestampType = messageTimestampType;
  }


  public KafkaTopicConfig minCleanableDirtyRatio(Float minCleanableDirtyRatio) {
    if (minCleanableDirtyRatio != null && minCleanableDirtyRatio < 0) {
      throw new IllegalArgumentException("Invalid value for minCleanableDirtyRatio. Must be greater than or equal to 0.");
    }
    if (minCleanableDirtyRatio != null && minCleanableDirtyRatio > 1) {
      throw new IllegalArgumentException("Invalid value for minCleanableDirtyRatio. Must be less than or equal to 1.");
    }
    
    
    this.minCleanableDirtyRatio = minCleanableDirtyRatio;
    return this;
  }

   /**
   * The min_cleanable_dirty_ratio specifies the frequency of log compaction (if enabled) in relation to duplicates present in the logs. For example, at 0.5, at most 50% of the log could be duplicates before compaction would begin.
   * minimum: 0
   * maximum: 1
   * @return minCleanableDirtyRatio
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "0.5", value = "The min_cleanable_dirty_ratio specifies the frequency of log compaction (if enabled) in relation to duplicates present in the logs. For example, at 0.5, at most 50% of the log could be duplicates before compaction would begin.")

  public Float getMinCleanableDirtyRatio() {
    return minCleanableDirtyRatio;
  }


  public void setMinCleanableDirtyRatio(Float minCleanableDirtyRatio) {
    if (minCleanableDirtyRatio != null && minCleanableDirtyRatio < 0) {
      throw new IllegalArgumentException("Invalid value for minCleanableDirtyRatio. Must be greater than or equal to 0.");
    }
    if (minCleanableDirtyRatio != null && minCleanableDirtyRatio > 1) {
      throw new IllegalArgumentException("Invalid value for minCleanableDirtyRatio. Must be less than or equal to 1.");
    }
    
    this.minCleanableDirtyRatio = minCleanableDirtyRatio;
  }


  public KafkaTopicConfig minCompactionLagMs(Integer minCompactionLagMs) {
    
    
    
    
    this.minCompactionLagMs = minCompactionLagMs;
    return this;
  }

   /**
   * The min_compaction_lag_ms specifies the minimum time (in ms) that a message will remain uncompacted in the log. Only relevant if log compaction is enabled.
   * @return minCompactionLagMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "0", value = "The min_compaction_lag_ms specifies the minimum time (in ms) that a message will remain uncompacted in the log. Only relevant if log compaction is enabled.")

  public Integer getMinCompactionLagMs() {
    return minCompactionLagMs;
  }


  public void setMinCompactionLagMs(Integer minCompactionLagMs) {
    
    
    
    this.minCompactionLagMs = minCompactionLagMs;
  }


  public KafkaTopicConfig minInsyncReplicas(Integer minInsyncReplicas) {
    if (minInsyncReplicas != null && minInsyncReplicas < 1) {
      throw new IllegalArgumentException("Invalid value for minInsyncReplicas. Must be greater than or equal to 1.");
    }
    
    
    
    this.minInsyncReplicas = minInsyncReplicas;
    return this;
  }

   /**
   * The min_insync_replicas specifies the number of replicas that must ACK a write for the write to be considered successful.
   * minimum: 1
   * @return minInsyncReplicas
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1", value = "The min_insync_replicas specifies the number of replicas that must ACK a write for the write to be considered successful.")

  public Integer getMinInsyncReplicas() {
    return minInsyncReplicas;
  }


  public void setMinInsyncReplicas(Integer minInsyncReplicas) {
    if (minInsyncReplicas != null && minInsyncReplicas < 1) {
      throw new IllegalArgumentException("Invalid value for minInsyncReplicas. Must be greater than or equal to 1.");
    }
    
    
    this.minInsyncReplicas = minInsyncReplicas;
  }


  public KafkaTopicConfig preallocate(Boolean preallocate) {
    
    
    
    
    this.preallocate = preallocate;
    return this;
  }

   /**
   * The preallocate specifies whether a file should be preallocated on disk when creating a new log segment.
   * @return preallocate
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "false", value = "The preallocate specifies whether a file should be preallocated on disk when creating a new log segment.")

  public Boolean getPreallocate() {
    return preallocate;
  }


  public void setPreallocate(Boolean preallocate) {
    
    
    
    this.preallocate = preallocate;
  }


  public KafkaTopicConfig retentionBytes(Integer retentionBytes) {
    
    
    
    
    this.retentionBytes = retentionBytes;
    return this;
  }

   /**
   * The retention_bytes specifies the maximum size of the log (in bytes) before deleting messages. -1 indicates that there is no limit.
   * @return retentionBytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "1000000", value = "The retention_bytes specifies the maximum size of the log (in bytes) before deleting messages. -1 indicates that there is no limit.")

  public Integer getRetentionBytes() {
    return retentionBytes;
  }


  public void setRetentionBytes(Integer retentionBytes) {
    
    
    
    this.retentionBytes = retentionBytes;
  }


  public KafkaTopicConfig retentionMs(Integer retentionMs) {
    
    
    
    
    this.retentionMs = retentionMs;
    return this;
  }

   /**
   * The retention_ms specifies the maximum amount of time (in ms) to keep a message before deleting it.
   * @return retentionMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "604800000", value = "The retention_ms specifies the maximum amount of time (in ms) to keep a message before deleting it.")

  public Integer getRetentionMs() {
    return retentionMs;
  }


  public void setRetentionMs(Integer retentionMs) {
    
    
    
    this.retentionMs = retentionMs;
  }


  public KafkaTopicConfig segmentBytes(Integer segmentBytes) {
    if (segmentBytes != null && segmentBytes < 14) {
      throw new IllegalArgumentException("Invalid value for segmentBytes. Must be greater than or equal to 14.");
    }
    
    
    
    this.segmentBytes = segmentBytes;
    return this;
  }

   /**
   * The segment_bytes specifies the maximum size of a single log file (in bytes).
   * minimum: 14
   * @return segmentBytes
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "209715200", value = "The segment_bytes specifies the maximum size of a single log file (in bytes).")

  public Integer getSegmentBytes() {
    return segmentBytes;
  }


  public void setSegmentBytes(Integer segmentBytes) {
    if (segmentBytes != null && segmentBytes < 14) {
      throw new IllegalArgumentException("Invalid value for segmentBytes. Must be greater than or equal to 14.");
    }
    
    
    this.segmentBytes = segmentBytes;
  }


  public KafkaTopicConfig segmentJitterMs(Integer segmentJitterMs) {
    
    
    
    
    this.segmentJitterMs = segmentJitterMs;
    return this;
  }

   /**
   * The segment_jitter_ms specifies the maximum random jitter subtracted from the scheduled segment roll time to avoid thundering herds of segment rolling.
   * @return segmentJitterMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "0", value = "The segment_jitter_ms specifies the maximum random jitter subtracted from the scheduled segment roll time to avoid thundering herds of segment rolling.")

  public Integer getSegmentJitterMs() {
    return segmentJitterMs;
  }


  public void setSegmentJitterMs(Integer segmentJitterMs) {
    
    
    
    this.segmentJitterMs = segmentJitterMs;
  }


  public KafkaTopicConfig segmentMs(Integer segmentMs) {
    if (segmentMs != null && segmentMs < 1) {
      throw new IllegalArgumentException("Invalid value for segmentMs. Must be greater than or equal to 1.");
    }
    
    
    
    this.segmentMs = segmentMs;
    return this;
  }

   /**
   * The segment_ms specifies the period of time after which the log will be forced to roll if the segment file isn&#39;t full. This ensures that retention can delete or compact old data.
   * minimum: 1
   * @return segmentMs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "604800000", value = "The segment_ms specifies the period of time after which the log will be forced to roll if the segment file isn't full. This ensures that retention can delete or compact old data.")

  public Integer getSegmentMs() {
    return segmentMs;
  }


  public void setSegmentMs(Integer segmentMs) {
    if (segmentMs != null && segmentMs < 1) {
      throw new IllegalArgumentException("Invalid value for segmentMs. Must be greater than or equal to 1.");
    }
    
    
    this.segmentMs = segmentMs;
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
   * @return the KafkaTopicConfig instance itself
   */
  public KafkaTopicConfig putAdditionalProperty(String key, Object value) {
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
    KafkaTopicConfig kafkaTopicConfig = (KafkaTopicConfig) o;
    return Objects.equals(this.cleanupPolicy, kafkaTopicConfig.cleanupPolicy) &&
        Objects.equals(this.compressionType, kafkaTopicConfig.compressionType) &&
        Objects.equals(this.deleteRetentionMs, kafkaTopicConfig.deleteRetentionMs) &&
        Objects.equals(this.fileDeleteDelayMs, kafkaTopicConfig.fileDeleteDelayMs) &&
        Objects.equals(this.flushMessages, kafkaTopicConfig.flushMessages) &&
        Objects.equals(this.flushMs, kafkaTopicConfig.flushMs) &&
        Objects.equals(this.indexIntervalBytes, kafkaTopicConfig.indexIntervalBytes) &&
        Objects.equals(this.maxCompactionLagMs, kafkaTopicConfig.maxCompactionLagMs) &&
        Objects.equals(this.maxMessageBytes, kafkaTopicConfig.maxMessageBytes) &&
        Objects.equals(this.messageDownConversionEnable, kafkaTopicConfig.messageDownConversionEnable) &&
        Objects.equals(this.messageFormatVersion, kafkaTopicConfig.messageFormatVersion) &&
        Objects.equals(this.messageTimestampType, kafkaTopicConfig.messageTimestampType) &&
        Objects.equals(this.minCleanableDirtyRatio, kafkaTopicConfig.minCleanableDirtyRatio) &&
        Objects.equals(this.minCompactionLagMs, kafkaTopicConfig.minCompactionLagMs) &&
        Objects.equals(this.minInsyncReplicas, kafkaTopicConfig.minInsyncReplicas) &&
        Objects.equals(this.preallocate, kafkaTopicConfig.preallocate) &&
        Objects.equals(this.retentionBytes, kafkaTopicConfig.retentionBytes) &&
        Objects.equals(this.retentionMs, kafkaTopicConfig.retentionMs) &&
        Objects.equals(this.segmentBytes, kafkaTopicConfig.segmentBytes) &&
        Objects.equals(this.segmentJitterMs, kafkaTopicConfig.segmentJitterMs) &&
        Objects.equals(this.segmentMs, kafkaTopicConfig.segmentMs)&&
        Objects.equals(this.additionalProperties, kafkaTopicConfig.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cleanupPolicy, compressionType, deleteRetentionMs, fileDeleteDelayMs, flushMessages, flushMs, indexIntervalBytes, maxCompactionLagMs, maxMessageBytes, messageDownConversionEnable, messageFormatVersion, messageTimestampType, minCleanableDirtyRatio, minCompactionLagMs, minInsyncReplicas, preallocate, retentionBytes, retentionMs, segmentBytes, segmentJitterMs, segmentMs, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KafkaTopicConfig {\n");
    sb.append("    cleanupPolicy: ").append(toIndentedString(cleanupPolicy)).append("\n");
    sb.append("    compressionType: ").append(toIndentedString(compressionType)).append("\n");
    sb.append("    deleteRetentionMs: ").append(toIndentedString(deleteRetentionMs)).append("\n");
    sb.append("    fileDeleteDelayMs: ").append(toIndentedString(fileDeleteDelayMs)).append("\n");
    sb.append("    flushMessages: ").append(toIndentedString(flushMessages)).append("\n");
    sb.append("    flushMs: ").append(toIndentedString(flushMs)).append("\n");
    sb.append("    indexIntervalBytes: ").append(toIndentedString(indexIntervalBytes)).append("\n");
    sb.append("    maxCompactionLagMs: ").append(toIndentedString(maxCompactionLagMs)).append("\n");
    sb.append("    maxMessageBytes: ").append(toIndentedString(maxMessageBytes)).append("\n");
    sb.append("    messageDownConversionEnable: ").append(toIndentedString(messageDownConversionEnable)).append("\n");
    sb.append("    messageFormatVersion: ").append(toIndentedString(messageFormatVersion)).append("\n");
    sb.append("    messageTimestampType: ").append(toIndentedString(messageTimestampType)).append("\n");
    sb.append("    minCleanableDirtyRatio: ").append(toIndentedString(minCleanableDirtyRatio)).append("\n");
    sb.append("    minCompactionLagMs: ").append(toIndentedString(minCompactionLagMs)).append("\n");
    sb.append("    minInsyncReplicas: ").append(toIndentedString(minInsyncReplicas)).append("\n");
    sb.append("    preallocate: ").append(toIndentedString(preallocate)).append("\n");
    sb.append("    retentionBytes: ").append(toIndentedString(retentionBytes)).append("\n");
    sb.append("    retentionMs: ").append(toIndentedString(retentionMs)).append("\n");
    sb.append("    segmentBytes: ").append(toIndentedString(segmentBytes)).append("\n");
    sb.append("    segmentJitterMs: ").append(toIndentedString(segmentJitterMs)).append("\n");
    sb.append("    segmentMs: ").append(toIndentedString(segmentMs)).append("\n");
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
    openapiFields.add("cleanup_policy");
    openapiFields.add("compression_type");
    openapiFields.add("delete_retention_ms");
    openapiFields.add("file_delete_delay_ms");
    openapiFields.add("flush_messages");
    openapiFields.add("flush_ms");
    openapiFields.add("index_interval_bytes");
    openapiFields.add("max_compaction_lag_ms");
    openapiFields.add("max_message_bytes");
    openapiFields.add("message_down_conversion_enable");
    openapiFields.add("message_format_version");
    openapiFields.add("message_timestamp_type");
    openapiFields.add("min_cleanable_dirty_ratio");
    openapiFields.add("min_compaction_lag_ms");
    openapiFields.add("min_insync_replicas");
    openapiFields.add("preallocate");
    openapiFields.add("retention_bytes");
    openapiFields.add("retention_ms");
    openapiFields.add("segment_bytes");
    openapiFields.add("segment_jitter_ms");
    openapiFields.add("segment_ms");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to KafkaTopicConfig
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!KafkaTopicConfig.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in KafkaTopicConfig is not found in the empty JSON string", KafkaTopicConfig.openapiRequiredFields.toString()));
        }
      }
      if ((jsonObj.get("cleanup_policy") != null && !jsonObj.get("cleanup_policy").isJsonNull()) && !jsonObj.get("cleanup_policy").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `cleanup_policy` to be a primitive type in the JSON string but got `%s`", jsonObj.get("cleanup_policy").toString()));
      }
      if ((jsonObj.get("compression_type") != null && !jsonObj.get("compression_type").isJsonNull()) && !jsonObj.get("compression_type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `compression_type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("compression_type").toString()));
      }
      if ((jsonObj.get("message_format_version") != null && !jsonObj.get("message_format_version").isJsonNull()) && !jsonObj.get("message_format_version").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `message_format_version` to be a primitive type in the JSON string but got `%s`", jsonObj.get("message_format_version").toString()));
      }
      if ((jsonObj.get("message_timestamp_type") != null && !jsonObj.get("message_timestamp_type").isJsonNull()) && !jsonObj.get("message_timestamp_type").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `message_timestamp_type` to be a primitive type in the JSON string but got `%s`", jsonObj.get("message_timestamp_type").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!KafkaTopicConfig.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'KafkaTopicConfig' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<KafkaTopicConfig> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(KafkaTopicConfig.class));

       return (TypeAdapter<T>) new TypeAdapter<KafkaTopicConfig>() {
           @Override
           public void write(JsonWriter out, KafkaTopicConfig value) throws IOException {
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
           public KafkaTopicConfig read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             KafkaTopicConfig instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of KafkaTopicConfig given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of KafkaTopicConfig
  * @throws IOException if the JSON string is invalid with respect to KafkaTopicConfig
  */
  public static KafkaTopicConfig fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, KafkaTopicConfig.class);
  }

 /**
  * Convert an instance of KafkaTopicConfig to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

