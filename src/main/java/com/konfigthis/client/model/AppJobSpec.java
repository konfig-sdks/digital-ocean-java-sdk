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
import com.konfigthis.client.model.AppComponentInstanceBaseAutoscaling;
import com.konfigthis.client.model.AppLogDestinationDefinition;
import com.konfigthis.client.model.AppVariableDefinition;
import com.konfigthis.client.model.AppsGitSourceSpec;
import com.konfigthis.client.model.AppsGithubSourceSpec;
import com.konfigthis.client.model.AppsGitlabSourceSpec;
import com.konfigthis.client.model.AppsImageSourceSpec;
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
 * AppJobSpec
 */@javax.annotation.Generated(value = "Generated by https://konfigthis.com")
public class AppJobSpec {
  public static final String SERIALIZED_NAME_NAME = "name";
  @SerializedName(SERIALIZED_NAME_NAME)
  private String name;

  public static final String SERIALIZED_NAME_GIT = "git";
  @SerializedName(SERIALIZED_NAME_GIT)
  private AppsGitSourceSpec git;

  public static final String SERIALIZED_NAME_GITHUB = "github";
  @SerializedName(SERIALIZED_NAME_GITHUB)
  private AppsGithubSourceSpec github;

  public static final String SERIALIZED_NAME_GITLAB = "gitlab";
  @SerializedName(SERIALIZED_NAME_GITLAB)
  private AppsGitlabSourceSpec gitlab;

  public static final String SERIALIZED_NAME_IMAGE = "image";
  @SerializedName(SERIALIZED_NAME_IMAGE)
  private AppsImageSourceSpec image;

  public static final String SERIALIZED_NAME_DOCKERFILE_PATH = "dockerfile_path";
  @SerializedName(SERIALIZED_NAME_DOCKERFILE_PATH)
  private String dockerfilePath;

  public static final String SERIALIZED_NAME_BUILD_COMMAND = "build_command";
  @SerializedName(SERIALIZED_NAME_BUILD_COMMAND)
  private String buildCommand;

  public static final String SERIALIZED_NAME_RUN_COMMAND = "run_command";
  @SerializedName(SERIALIZED_NAME_RUN_COMMAND)
  private String runCommand;

  public static final String SERIALIZED_NAME_SOURCE_DIR = "source_dir";
  @SerializedName(SERIALIZED_NAME_SOURCE_DIR)
  private String sourceDir;

  public static final String SERIALIZED_NAME_ENVS = "envs";
  @SerializedName(SERIALIZED_NAME_ENVS)
  private List<AppVariableDefinition> envs = null;

  public static final String SERIALIZED_NAME_ENVIRONMENT_SLUG = "environment_slug";
  @SerializedName(SERIALIZED_NAME_ENVIRONMENT_SLUG)
  private String environmentSlug;

  public static final String SERIALIZED_NAME_LOG_DESTINATIONS = "log_destinations";
  @SerializedName(SERIALIZED_NAME_LOG_DESTINATIONS)
  private AppLogDestinationDefinition logDestinations;

  public static final String SERIALIZED_NAME_INSTANCE_COUNT = "instance_count";
  @SerializedName(SERIALIZED_NAME_INSTANCE_COUNT)
  private Long instanceCount = 1l;

  /**
   * The instance size to use for this component. Default: &#x60;basic-xxs&#x60;
   */
  @JsonAdapter(InstanceSizeSlugEnum.Adapter.class)
 public enum InstanceSizeSlugEnum {
    BASIC_XXS("basic-xxs"),
    
    BASIC_XS("basic-xs"),
    
    BASIC_S("basic-s"),
    
    BASIC_M("basic-m"),
    
    PROFESSIONAL_XS("professional-xs"),
    
    PROFESSIONAL_S("professional-s"),
    
    PROFESSIONAL_M("professional-m"),
    
    PROFESSIONAL_1L("professional-1l"),
    
    PROFESSIONAL_L("professional-l"),
    
    PROFESSIONAL_XL("professional-xl");

    private String value;

    InstanceSizeSlugEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static InstanceSizeSlugEnum fromValue(String value) {
      for (InstanceSizeSlugEnum b : InstanceSizeSlugEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<InstanceSizeSlugEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final InstanceSizeSlugEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public InstanceSizeSlugEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return InstanceSizeSlugEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_INSTANCE_SIZE_SLUG = "instance_size_slug";
  @SerializedName(SERIALIZED_NAME_INSTANCE_SIZE_SLUG)
  private InstanceSizeSlugEnum instanceSizeSlug = InstanceSizeSlugEnum.BASIC_XXS;

  public static final String SERIALIZED_NAME_AUTOSCALING = "autoscaling";
  @SerializedName(SERIALIZED_NAME_AUTOSCALING)
  private AppComponentInstanceBaseAutoscaling autoscaling;

  /**
   * - UNSPECIFIED: Default job type, will auto-complete to POST_DEPLOY kind. - PRE_DEPLOY: Indicates a job that runs before an app deployment. - POST_DEPLOY: Indicates a job that runs after an app deployment. - FAILED_DEPLOY: Indicates a job that runs after a component fails to deploy.
   */
  @JsonAdapter(KindEnum.Adapter.class)
 public enum KindEnum {
    UNSPECIFIED("UNSPECIFIED"),
    
    PRE_DEPLOY("PRE_DEPLOY"),
    
    POST_DEPLOY("POST_DEPLOY"),
    
    FAILED_DEPLOY("FAILED_DEPLOY");

    private String value;

    KindEnum(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    public static KindEnum fromValue(String value) {
      for (KindEnum b : KindEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    public static class Adapter extends TypeAdapter<KindEnum> {
      @Override
      public void write(final JsonWriter jsonWriter, final KindEnum enumeration) throws IOException {
        jsonWriter.value(enumeration.getValue());
      }

      @Override
      public KindEnum read(final JsonReader jsonReader) throws IOException {
        String value =  jsonReader.nextString();
        return KindEnum.fromValue(value);
      }
    }
  }

  public static final String SERIALIZED_NAME_KIND = "kind";
  @SerializedName(SERIALIZED_NAME_KIND)
  private KindEnum kind = KindEnum.UNSPECIFIED;

  public AppJobSpec() {
  }

  public AppJobSpec name(String name) {
    
    
    if (name != null && name.length() < 2) {
      throw new IllegalArgumentException("Invalid value for name. Length must be greater than or equal to 2.");
    }
    
    this.name = name;
    return this;
  }

   /**
   * The name. Must be unique across all components within the same app.
   * @return name
  **/
  @javax.annotation.Nonnull
  @ApiModelProperty(example = "api", required = true, value = "The name. Must be unique across all components within the same app.")

  public String getName() {
    return name;
  }


  public void setName(String name) {
    
    
    if (name != null && name.length() < 2) {
      throw new IllegalArgumentException("Invalid value for name. Length must be greater than or equal to 2.");
    }
    this.name = name;
  }


  public AppJobSpec git(AppsGitSourceSpec git) {
    
    
    
    
    this.git = git;
    return this;
  }

   /**
   * Get git
   * @return git
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AppsGitSourceSpec getGit() {
    return git;
  }


  public void setGit(AppsGitSourceSpec git) {
    
    
    
    this.git = git;
  }


  public AppJobSpec github(AppsGithubSourceSpec github) {
    
    
    
    
    this.github = github;
    return this;
  }

   /**
   * Get github
   * @return github
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AppsGithubSourceSpec getGithub() {
    return github;
  }


  public void setGithub(AppsGithubSourceSpec github) {
    
    
    
    this.github = github;
  }


  public AppJobSpec gitlab(AppsGitlabSourceSpec gitlab) {
    
    
    
    
    this.gitlab = gitlab;
    return this;
  }

   /**
   * Get gitlab
   * @return gitlab
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AppsGitlabSourceSpec getGitlab() {
    return gitlab;
  }


  public void setGitlab(AppsGitlabSourceSpec gitlab) {
    
    
    
    this.gitlab = gitlab;
  }


  public AppJobSpec image(AppsImageSourceSpec image) {
    
    
    
    
    this.image = image;
    return this;
  }

   /**
   * Get image
   * @return image
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AppsImageSourceSpec getImage() {
    return image;
  }


  public void setImage(AppsImageSourceSpec image) {
    
    
    
    this.image = image;
  }


  public AppJobSpec dockerfilePath(String dockerfilePath) {
    
    
    
    
    this.dockerfilePath = dockerfilePath;
    return this;
  }

   /**
   * The path to the Dockerfile relative to the root of the repo. If set, it will be used to build this component. Otherwise, App Platform will attempt to build it using buildpacks.
   * @return dockerfilePath
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "path/to/Dockerfile", value = "The path to the Dockerfile relative to the root of the repo. If set, it will be used to build this component. Otherwise, App Platform will attempt to build it using buildpacks.")

  public String getDockerfilePath() {
    return dockerfilePath;
  }


  public void setDockerfilePath(String dockerfilePath) {
    
    
    
    this.dockerfilePath = dockerfilePath;
  }


  public AppJobSpec buildCommand(String buildCommand) {
    
    
    
    
    this.buildCommand = buildCommand;
    return this;
  }

   /**
   * An optional build command to run while building this component from source.
   * @return buildCommand
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "npm run build", value = "An optional build command to run while building this component from source.")

  public String getBuildCommand() {
    return buildCommand;
  }


  public void setBuildCommand(String buildCommand) {
    
    
    
    this.buildCommand = buildCommand;
  }


  public AppJobSpec runCommand(String runCommand) {
    
    
    
    
    this.runCommand = runCommand;
    return this;
  }

   /**
   * An optional run command to override the component&#39;s default.
   * @return runCommand
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "bin/api", value = "An optional run command to override the component's default.")

  public String getRunCommand() {
    return runCommand;
  }


  public void setRunCommand(String runCommand) {
    
    
    
    this.runCommand = runCommand;
  }


  public AppJobSpec sourceDir(String sourceDir) {
    
    
    
    
    this.sourceDir = sourceDir;
    return this;
  }

   /**
   * An optional path to the working directory to use for the build. For Dockerfile builds, this will be used as the build context. Must be relative to the root of the repo.
   * @return sourceDir
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "path/to/dir", value = "An optional path to the working directory to use for the build. For Dockerfile builds, this will be used as the build context. Must be relative to the root of the repo.")

  public String getSourceDir() {
    return sourceDir;
  }


  public void setSourceDir(String sourceDir) {
    
    
    
    this.sourceDir = sourceDir;
  }


  public AppJobSpec envs(List<AppVariableDefinition> envs) {
    
    
    
    
    this.envs = envs;
    return this;
  }

  public AppJobSpec addEnvsItem(AppVariableDefinition envsItem) {
    if (this.envs == null) {
      this.envs = new ArrayList<>();
    }
    this.envs.add(envsItem);
    return this;
  }

   /**
   * A list of environment variables made available to the component.
   * @return envs
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "A list of environment variables made available to the component.")

  public List<AppVariableDefinition> getEnvs() {
    return envs;
  }


  public void setEnvs(List<AppVariableDefinition> envs) {
    
    
    
    this.envs = envs;
  }


  public AppJobSpec environmentSlug(String environmentSlug) {
    
    
    
    
    this.environmentSlug = environmentSlug;
    return this;
  }

   /**
   * An environment slug describing the type of this app. For a full list, please refer to [the product documentation](https://www.digitalocean.com/docs/app-platform/).
   * @return environmentSlug
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "node-js", value = "An environment slug describing the type of this app. For a full list, please refer to [the product documentation](https://www.digitalocean.com/docs/app-platform/).")

  public String getEnvironmentSlug() {
    return environmentSlug;
  }


  public void setEnvironmentSlug(String environmentSlug) {
    
    
    
    this.environmentSlug = environmentSlug;
  }


  public AppJobSpec logDestinations(AppLogDestinationDefinition logDestinations) {
    
    
    
    
    this.logDestinations = logDestinations;
    return this;
  }

   /**
   * Get logDestinations
   * @return logDestinations
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AppLogDestinationDefinition getLogDestinations() {
    return logDestinations;
  }


  public void setLogDestinations(AppLogDestinationDefinition logDestinations) {
    
    
    
    this.logDestinations = logDestinations;
  }


  public AppJobSpec instanceCount(Long instanceCount) {
    if (instanceCount != null && instanceCount < 1) {
      throw new IllegalArgumentException("Invalid value for instanceCount. Must be greater than or equal to 1.");
    }
    
    
    
    this.instanceCount = instanceCount;
    return this;
  }

   /**
   * The amount of instances that this component should be scaled to. Default: 1. Must not be set if autoscaling is used.
   * minimum: 1
   * @return instanceCount
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "2", value = "The amount of instances that this component should be scaled to. Default: 1. Must not be set if autoscaling is used.")

  public Long getInstanceCount() {
    return instanceCount;
  }


  public void setInstanceCount(Long instanceCount) {
    if (instanceCount != null && instanceCount < 1) {
      throw new IllegalArgumentException("Invalid value for instanceCount. Must be greater than or equal to 1.");
    }
    
    
    this.instanceCount = instanceCount;
  }


  public AppJobSpec instanceSizeSlug(InstanceSizeSlugEnum instanceSizeSlug) {
    
    
    
    
    this.instanceSizeSlug = instanceSizeSlug;
    return this;
  }

   /**
   * The instance size to use for this component. Default: &#x60;basic-xxs&#x60;
   * @return instanceSizeSlug
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "BASIC_XXS", value = "The instance size to use for this component. Default: `basic-xxs`")

  public InstanceSizeSlugEnum getInstanceSizeSlug() {
    return instanceSizeSlug;
  }


  public void setInstanceSizeSlug(InstanceSizeSlugEnum instanceSizeSlug) {
    
    
    
    this.instanceSizeSlug = instanceSizeSlug;
  }


  public AppJobSpec autoscaling(AppComponentInstanceBaseAutoscaling autoscaling) {
    
    
    
    
    this.autoscaling = autoscaling;
    return this;
  }

   /**
   * Get autoscaling
   * @return autoscaling
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(value = "")

  public AppComponentInstanceBaseAutoscaling getAutoscaling() {
    return autoscaling;
  }


  public void setAutoscaling(AppComponentInstanceBaseAutoscaling autoscaling) {
    
    
    
    this.autoscaling = autoscaling;
  }


  public AppJobSpec kind(KindEnum kind) {
    
    
    
    
    this.kind = kind;
    return this;
  }

   /**
   * - UNSPECIFIED: Default job type, will auto-complete to POST_DEPLOY kind. - PRE_DEPLOY: Indicates a job that runs before an app deployment. - POST_DEPLOY: Indicates a job that runs after an app deployment. - FAILED_DEPLOY: Indicates a job that runs after a component fails to deploy.
   * @return kind
  **/
  @javax.annotation.Nullable
  @ApiModelProperty(example = "PRE_DEPLOY", value = "- UNSPECIFIED: Default job type, will auto-complete to POST_DEPLOY kind. - PRE_DEPLOY: Indicates a job that runs before an app deployment. - POST_DEPLOY: Indicates a job that runs after an app deployment. - FAILED_DEPLOY: Indicates a job that runs after a component fails to deploy.")

  public KindEnum getKind() {
    return kind;
  }


  public void setKind(KindEnum kind) {
    
    
    
    this.kind = kind;
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
   * @return the AppJobSpec instance itself
   */
  public AppJobSpec putAdditionalProperty(String key, Object value) {
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
    AppJobSpec appJobSpec = (AppJobSpec) o;
    return Objects.equals(this.name, appJobSpec.name) &&
        Objects.equals(this.git, appJobSpec.git) &&
        Objects.equals(this.github, appJobSpec.github) &&
        Objects.equals(this.gitlab, appJobSpec.gitlab) &&
        Objects.equals(this.image, appJobSpec.image) &&
        Objects.equals(this.dockerfilePath, appJobSpec.dockerfilePath) &&
        Objects.equals(this.buildCommand, appJobSpec.buildCommand) &&
        Objects.equals(this.runCommand, appJobSpec.runCommand) &&
        Objects.equals(this.sourceDir, appJobSpec.sourceDir) &&
        Objects.equals(this.envs, appJobSpec.envs) &&
        Objects.equals(this.environmentSlug, appJobSpec.environmentSlug) &&
        Objects.equals(this.logDestinations, appJobSpec.logDestinations) &&
        Objects.equals(this.instanceCount, appJobSpec.instanceCount) &&
        Objects.equals(this.instanceSizeSlug, appJobSpec.instanceSizeSlug) &&
        Objects.equals(this.autoscaling, appJobSpec.autoscaling) &&
        Objects.equals(this.kind, appJobSpec.kind)&&
        Objects.equals(this.additionalProperties, appJobSpec.additionalProperties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, git, github, gitlab, image, dockerfilePath, buildCommand, runCommand, sourceDir, envs, environmentSlug, logDestinations, instanceCount, instanceSizeSlug, autoscaling, kind, additionalProperties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AppJobSpec {\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    git: ").append(toIndentedString(git)).append("\n");
    sb.append("    github: ").append(toIndentedString(github)).append("\n");
    sb.append("    gitlab: ").append(toIndentedString(gitlab)).append("\n");
    sb.append("    image: ").append(toIndentedString(image)).append("\n");
    sb.append("    dockerfilePath: ").append(toIndentedString(dockerfilePath)).append("\n");
    sb.append("    buildCommand: ").append(toIndentedString(buildCommand)).append("\n");
    sb.append("    runCommand: ").append(toIndentedString(runCommand)).append("\n");
    sb.append("    sourceDir: ").append(toIndentedString(sourceDir)).append("\n");
    sb.append("    envs: ").append(toIndentedString(envs)).append("\n");
    sb.append("    environmentSlug: ").append(toIndentedString(environmentSlug)).append("\n");
    sb.append("    logDestinations: ").append(toIndentedString(logDestinations)).append("\n");
    sb.append("    instanceCount: ").append(toIndentedString(instanceCount)).append("\n");
    sb.append("    instanceSizeSlug: ").append(toIndentedString(instanceSizeSlug)).append("\n");
    sb.append("    autoscaling: ").append(toIndentedString(autoscaling)).append("\n");
    sb.append("    kind: ").append(toIndentedString(kind)).append("\n");
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
    openapiFields.add("name");
    openapiFields.add("git");
    openapiFields.add("github");
    openapiFields.add("gitlab");
    openapiFields.add("image");
    openapiFields.add("dockerfile_path");
    openapiFields.add("build_command");
    openapiFields.add("run_command");
    openapiFields.add("source_dir");
    openapiFields.add("envs");
    openapiFields.add("environment_slug");
    openapiFields.add("log_destinations");
    openapiFields.add("instance_count");
    openapiFields.add("instance_size_slug");
    openapiFields.add("autoscaling");
    openapiFields.add("kind");

    // a set of required properties/fields (JSON key names)
    openapiRequiredFields = new HashSet<String>();
    openapiRequiredFields.add("name");
  }

 /**
  * Validates the JSON Object and throws an exception if issues found
  *
  * @param jsonObj JSON Object
  * @throws IOException if the JSON Object is invalid with respect to AppJobSpec
  */
  public static void validateJsonObject(JsonObject jsonObj) throws IOException {
      if (jsonObj == null) {
        if (!AppJobSpec.openapiRequiredFields.isEmpty()) { // has required fields but JSON object is null
          throw new IllegalArgumentException(String.format("The required field(s) %s in AppJobSpec is not found in the empty JSON string", AppJobSpec.openapiRequiredFields.toString()));
        }
      }

      // check to make sure all required properties/fields are present in the JSON string
      for (String requiredField : AppJobSpec.openapiRequiredFields) {
        if (jsonObj.get(requiredField) == null) {
          throw new IllegalArgumentException(String.format("The required field `%s` is not found in the JSON string: %s", requiredField, jsonObj.toString()));
        }
      }
      if (!jsonObj.get("name").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `name` to be a primitive type in the JSON string but got `%s`", jsonObj.get("name").toString()));
      }
      // validate the optional field `git`
      if (jsonObj.get("git") != null && !jsonObj.get("git").isJsonNull()) {
        AppsGitSourceSpec.validateJsonObject(jsonObj.getAsJsonObject("git"));
      }
      // validate the optional field `github`
      if (jsonObj.get("github") != null && !jsonObj.get("github").isJsonNull()) {
        AppsGithubSourceSpec.validateJsonObject(jsonObj.getAsJsonObject("github"));
      }
      // validate the optional field `gitlab`
      if (jsonObj.get("gitlab") != null && !jsonObj.get("gitlab").isJsonNull()) {
        AppsGitlabSourceSpec.validateJsonObject(jsonObj.getAsJsonObject("gitlab"));
      }
      // validate the optional field `image`
      if (jsonObj.get("image") != null && !jsonObj.get("image").isJsonNull()) {
        AppsImageSourceSpec.validateJsonObject(jsonObj.getAsJsonObject("image"));
      }
      if ((jsonObj.get("dockerfile_path") != null && !jsonObj.get("dockerfile_path").isJsonNull()) && !jsonObj.get("dockerfile_path").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `dockerfile_path` to be a primitive type in the JSON string but got `%s`", jsonObj.get("dockerfile_path").toString()));
      }
      if ((jsonObj.get("build_command") != null && !jsonObj.get("build_command").isJsonNull()) && !jsonObj.get("build_command").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `build_command` to be a primitive type in the JSON string but got `%s`", jsonObj.get("build_command").toString()));
      }
      if ((jsonObj.get("run_command") != null && !jsonObj.get("run_command").isJsonNull()) && !jsonObj.get("run_command").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `run_command` to be a primitive type in the JSON string but got `%s`", jsonObj.get("run_command").toString()));
      }
      if ((jsonObj.get("source_dir") != null && !jsonObj.get("source_dir").isJsonNull()) && !jsonObj.get("source_dir").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `source_dir` to be a primitive type in the JSON string but got `%s`", jsonObj.get("source_dir").toString()));
      }
      if (jsonObj.get("envs") != null && !jsonObj.get("envs").isJsonNull()) {
        JsonArray jsonArrayenvs = jsonObj.getAsJsonArray("envs");
        if (jsonArrayenvs != null) {
          // ensure the json data is an array
          if (!jsonObj.get("envs").isJsonArray()) {
            throw new IllegalArgumentException(String.format("Expected the field `envs` to be an array in the JSON string but got `%s`", jsonObj.get("envs").toString()));
          }

          // validate the optional field `envs` (array)
          for (int i = 0; i < jsonArrayenvs.size(); i++) {
            AppVariableDefinition.validateJsonObject(jsonArrayenvs.get(i).getAsJsonObject());
          };
        }
      }
      if ((jsonObj.get("environment_slug") != null && !jsonObj.get("environment_slug").isJsonNull()) && !jsonObj.get("environment_slug").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `environment_slug` to be a primitive type in the JSON string but got `%s`", jsonObj.get("environment_slug").toString()));
      }
      // validate the optional field `log_destinations`
      if (jsonObj.get("log_destinations") != null && !jsonObj.get("log_destinations").isJsonNull()) {
        AppLogDestinationDefinition.validateJsonObject(jsonObj.getAsJsonObject("log_destinations"));
      }
      if ((jsonObj.get("instance_size_slug") != null && !jsonObj.get("instance_size_slug").isJsonNull()) && !jsonObj.get("instance_size_slug").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `instance_size_slug` to be a primitive type in the JSON string but got `%s`", jsonObj.get("instance_size_slug").toString()));
      }
      // validate the optional field `autoscaling`
      if (jsonObj.get("autoscaling") != null && !jsonObj.get("autoscaling").isJsonNull()) {
        AppComponentInstanceBaseAutoscaling.validateJsonObject(jsonObj.getAsJsonObject("autoscaling"));
      }
      if ((jsonObj.get("kind") != null && !jsonObj.get("kind").isJsonNull()) && !jsonObj.get("kind").isJsonPrimitive()) {
        throw new IllegalArgumentException(String.format("Expected the field `kind` to be a primitive type in the JSON string but got `%s`", jsonObj.get("kind").toString()));
      }
  }

  public static class CustomTypeAdapterFactory implements TypeAdapterFactory {
    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
       if (!AppJobSpec.class.isAssignableFrom(type.getRawType())) {
         return null; // this class only serializes 'AppJobSpec' and its subtypes
       }
       final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);
       final TypeAdapter<AppJobSpec> thisAdapter
                        = gson.getDelegateAdapter(this, TypeToken.get(AppJobSpec.class));

       return (TypeAdapter<T>) new TypeAdapter<AppJobSpec>() {
           @Override
           public void write(JsonWriter out, AppJobSpec value) throws IOException {
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
           public AppJobSpec read(JsonReader in) throws IOException {
             JsonObject jsonObj = elementAdapter.read(in).getAsJsonObject();
             validateJsonObject(jsonObj);
             // store additional fields in the deserialized instance
             AppJobSpec instance = thisAdapter.fromJsonTree(jsonObj);
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
  * Create an instance of AppJobSpec given an JSON string
  *
  * @param jsonString JSON string
  * @return An instance of AppJobSpec
  * @throws IOException if the JSON string is invalid with respect to AppJobSpec
  */
  public static AppJobSpec fromJson(String jsonString) throws IOException {
    return JSON.getGson().fromJson(jsonString, AppJobSpec.class);
  }

 /**
  * Convert an instance of AppJobSpec to an JSON string
  *
  * @return JSON string
  */
  public String toJson() {
    return JSON.getGson().toJson(this);
  }
}

