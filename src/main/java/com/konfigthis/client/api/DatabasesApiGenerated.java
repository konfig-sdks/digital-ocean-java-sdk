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


package com.konfigthis.client.api;

import com.konfigthis.client.ApiCallback;
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.Pair;
import com.konfigthis.client.ProgressRequestBody;
import com.konfigthis.client.ProgressResponseBody;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;


import com.konfigthis.client.model.ConnectionPool;
import com.konfigthis.client.model.ConnectionPoolUpdate;
import com.konfigthis.client.model.ConnectionPools;
import com.konfigthis.client.model.Database;
import com.konfigthis.client.model.DatabaseBackup;
import com.konfigthis.client.model.DatabaseClusterConnection;
import com.konfigthis.client.model.DatabaseClusterMaintenanceWindow;
import com.konfigthis.client.model.DatabaseClusterResize;
import com.konfigthis.client.model.DatabaseConfig;
import com.konfigthis.client.model.DatabaseMaintenanceWindow;
import com.konfigthis.client.model.DatabaseMetricsCredentials;
import com.konfigthis.client.model.DatabaseReplica;
import com.konfigthis.client.model.DatabaseReplicaConnection;
import com.konfigthis.client.model.DatabaseServiceEndpoint;
import com.konfigthis.client.model.DatabaseUser;
import com.konfigthis.client.model.DatabasesAddNewConnectionPoolResponse;
import com.konfigthis.client.model.DatabasesAddResponse;
import com.konfigthis.client.model.DatabasesAddUserRequest;
import com.konfigthis.client.model.DatabasesAddUserResponse;
import com.konfigthis.client.model.DatabasesBasicAuthCredentials;
import com.konfigthis.client.model.DatabasesConfigureEvictionPolicyRequest;
import com.konfigthis.client.model.DatabasesCreateClusterRequest;
import com.konfigthis.client.model.DatabasesCreateClusterResponse;
import com.konfigthis.client.model.DatabasesCreateReadOnlyReplicaResponse;
import com.konfigthis.client.model.DatabasesCreateTopicKafkaClusterResponse;
import com.konfigthis.client.model.DatabasesGetClusterConfigResponse;
import com.konfigthis.client.model.DatabasesGetClustersMetricsCredentialsResponse;
import com.konfigthis.client.model.DatabasesGetEvictionPolicyResponse;
import com.konfigthis.client.model.DatabasesGetPublicCertificateResponse;
import com.konfigthis.client.model.DatabasesListBackupsResponse;
import com.konfigthis.client.model.DatabasesListClustersResponse;
import com.konfigthis.client.model.DatabasesListEventsLogsResponse;
import com.konfigthis.client.model.DatabasesListFirewallRulesResponse;
import com.konfigthis.client.model.DatabasesListReadOnlyReplicasResponse;
import com.konfigthis.client.model.DatabasesListResponse;
import com.konfigthis.client.model.DatabasesListTopicsKafkaClusterResponse;
import com.konfigthis.client.model.DatabasesListUsersResponse;
import com.konfigthis.client.model.DatabasesMigrateClusterToNewRegionRequest;
import com.konfigthis.client.model.DatabasesResetUserAuthRequest;
import com.konfigthis.client.model.DatabasesUpdateFirewallRulesRequest;
import com.konfigthis.client.model.DatabasesUpdateSettingsRequest;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.EvictionPolicyModel;
import com.konfigthis.client.model.FirewallRule;
import com.konfigthis.client.model.KafkaTopicConfig;
import com.konfigthis.client.model.KafkaTopicCreate;
import com.konfigthis.client.model.KafkaTopicUpdate;
import com.konfigthis.client.model.Mpr;
import com.konfigthis.client.model.MysqlSettings;
import java.time.OffsetDateTime;
import com.konfigthis.client.model.OnlineMigration;
import com.konfigthis.client.model.Options;
import com.konfigthis.client.model.SourceDatabase;
import com.konfigthis.client.model.SourceDatabaseSource;
import com.konfigthis.client.model.SqlMode;
import java.util.UUID;
import com.konfigthis.client.model.UserSettings;
import com.konfigthis.client.model.Version2;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class DatabasesApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public DatabasesApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public DatabasesApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public int getHostIndex() {
        return localHostIndex;
    }

    public void setHostIndex(int hostIndex) {
        this.localHostIndex = hostIndex;
    }

    public String getCustomBaseUrl() {
        return localCustomBaseUrl;
    }

    public void setCustomBaseUrl(String customBaseUrl) {
        this.localCustomBaseUrl = customBaseUrl;
    }

    private okhttp3.Call addCall(UUID databaseClusterUuid, Database database, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = database;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/dbs"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call addValidateBeforeCall(UUID databaseClusterUuid, Database database, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling add(Async)");
        }

        // verify the required parameter 'database' is set
        if (database == null) {
            throw new ApiException("Missing the required parameter 'database' when calling add(Async)");
        }

        return addCall(databaseClusterUuid, database, _callback);

    }


    private ApiResponse<DatabasesAddResponse> addWithHttpInfo(UUID databaseClusterUuid, Database database) throws ApiException {
        okhttp3.Call localVarCall = addValidateBeforeCall(databaseClusterUuid, database, null);
        Type localVarReturnType = new TypeToken<DatabasesAddResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call addAsync(UUID databaseClusterUuid, Database database, final ApiCallback<DatabasesAddResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = addValidateBeforeCall(databaseClusterUuid, database, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AddRequestBuilder {
        private final String name;
        private final UUID databaseClusterUuid;

        private AddRequestBuilder(String name, UUID databaseClusterUuid) {
            this.name = name;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for add
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Database database = buildBodyParams();
            return addCall(databaseClusterUuid, database, _callback);
        }

        private Database buildBodyParams() {
            Database database = new Database();
            database.name(this.name);
            return database;
        }

        /**
         * Execute add request
         * @return DatabasesAddResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddResponse execute() throws ApiException {
            Database database = buildBodyParams();
            ApiResponse<DatabasesAddResponse> localVarResp = addWithHttpInfo(databaseClusterUuid, database);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute add request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddResponse> executeWithHttpInfo() throws ApiException {
            Database database = buildBodyParams();
            return addWithHttpInfo(databaseClusterUuid, database);
        }

        /**
         * Execute add request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddResponse> _callback) throws ApiException {
            Database database = buildBodyParams();
            return addAsync(databaseClusterUuid, database, _callback);
        }
    }

    /**
     * Add a New Database
     * To add a new database to an existing cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/dbs&#x60;.  Note: Database management is not supported for Redis clusters.  The response will be a JSON object with a key called &#x60;db&#x60;. The value of this will be an object that contains the standard attributes associated with a database. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param database  (required)
     * @return AddRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddRequestBuilder add(String name, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new AddRequestBuilder(name, databaseClusterUuid);
    }
    private okhttp3.Call addNewConnectionPoolCall(UUID databaseClusterUuid, ConnectionPool connectionPool, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = connectionPool;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/pools"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call addNewConnectionPoolValidateBeforeCall(UUID databaseClusterUuid, ConnectionPool connectionPool, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling addNewConnectionPool(Async)");
        }

        // verify the required parameter 'connectionPool' is set
        if (connectionPool == null) {
            throw new ApiException("Missing the required parameter 'connectionPool' when calling addNewConnectionPool(Async)");
        }

        return addNewConnectionPoolCall(databaseClusterUuid, connectionPool, _callback);

    }


    private ApiResponse<DatabasesAddNewConnectionPoolResponse> addNewConnectionPoolWithHttpInfo(UUID databaseClusterUuid, ConnectionPool connectionPool) throws ApiException {
        okhttp3.Call localVarCall = addNewConnectionPoolValidateBeforeCall(databaseClusterUuid, connectionPool, null);
        Type localVarReturnType = new TypeToken<DatabasesAddNewConnectionPoolResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call addNewConnectionPoolAsync(UUID databaseClusterUuid, ConnectionPool connectionPool, final ApiCallback<DatabasesAddNewConnectionPoolResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = addNewConnectionPoolValidateBeforeCall(databaseClusterUuid, connectionPool, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddNewConnectionPoolResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AddNewConnectionPoolRequestBuilder {
        private final String name;
        private final String mode;
        private final Integer size;
        private final String db;
        private final UUID databaseClusterUuid;
        private String user;
        private DatabaseClusterConnection connection;
        private DatabaseClusterConnection privateConnection;
        private DatabaseClusterConnection standbyConnection;
        private DatabaseClusterConnection standbyPrivateConnection;

        private AddNewConnectionPoolRequestBuilder(String name, String mode, Integer size, String db, UUID databaseClusterUuid) {
            this.name = name;
            this.mode = mode;
            this.size = size;
            this.db = db;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set user
         * @param user The name of the user for use with the connection pool. When excluded, all sessions connect to the database as the inbound user. (optional)
         * @return AddNewConnectionPoolRequestBuilder
         */
        public AddNewConnectionPoolRequestBuilder user(String user) {
            this.user = user;
            return this;
        }
        
        /**
         * Set connection
         * @param connection  (optional)
         * @return AddNewConnectionPoolRequestBuilder
         */
        public AddNewConnectionPoolRequestBuilder connection(DatabaseClusterConnection connection) {
            this.connection = connection;
            return this;
        }
        
        /**
         * Set privateConnection
         * @param privateConnection  (optional)
         * @return AddNewConnectionPoolRequestBuilder
         */
        public AddNewConnectionPoolRequestBuilder privateConnection(DatabaseClusterConnection privateConnection) {
            this.privateConnection = privateConnection;
            return this;
        }
        
        /**
         * Set standbyConnection
         * @param standbyConnection  (optional)
         * @return AddNewConnectionPoolRequestBuilder
         */
        public AddNewConnectionPoolRequestBuilder standbyConnection(DatabaseClusterConnection standbyConnection) {
            this.standbyConnection = standbyConnection;
            return this;
        }
        
        /**
         * Set standbyPrivateConnection
         * @param standbyPrivateConnection  (optional)
         * @return AddNewConnectionPoolRequestBuilder
         */
        public AddNewConnectionPoolRequestBuilder standbyPrivateConnection(DatabaseClusterConnection standbyPrivateConnection) {
            this.standbyPrivateConnection = standbyPrivateConnection;
            return this;
        }
        
        /**
         * Build call for addNewConnectionPool
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            ConnectionPool connectionPool = buildBodyParams();
            return addNewConnectionPoolCall(databaseClusterUuid, connectionPool, _callback);
        }

        private ConnectionPool buildBodyParams() {
            ConnectionPool connectionPool = new ConnectionPool();
            connectionPool.name(this.name);
            connectionPool.mode(this.mode);
            connectionPool.size(this.size);
            connectionPool.db(this.db);
            connectionPool.user(this.user);
            connectionPool.connection(this.connection);
            connectionPool.privateConnection(this.privateConnection);
            connectionPool.standbyConnection(this.standbyConnection);
            connectionPool.standbyPrivateConnection(this.standbyPrivateConnection);
            return connectionPool;
        }

        /**
         * Execute addNewConnectionPool request
         * @return DatabasesAddNewConnectionPoolResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddNewConnectionPoolResponse execute() throws ApiException {
            ConnectionPool connectionPool = buildBodyParams();
            ApiResponse<DatabasesAddNewConnectionPoolResponse> localVarResp = addNewConnectionPoolWithHttpInfo(databaseClusterUuid, connectionPool);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute addNewConnectionPool request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddNewConnectionPoolResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddNewConnectionPoolResponse> executeWithHttpInfo() throws ApiException {
            ConnectionPool connectionPool = buildBodyParams();
            return addNewConnectionPoolWithHttpInfo(databaseClusterUuid, connectionPool);
        }

        /**
         * Execute addNewConnectionPool request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddNewConnectionPoolResponse> _callback) throws ApiException {
            ConnectionPool connectionPool = buildBodyParams();
            return addNewConnectionPoolAsync(databaseClusterUuid, connectionPool, _callback);
        }
    }

    /**
     * Add a New Connection Pool (PostgreSQL)
     * For PostgreSQL database clusters, connection pools can be used to allow a database to share its idle connections. The popular PostgreSQL connection pooling utility PgBouncer is used to provide this service. [See here for more information](https://www.digitalocean.com/docs/databases/postgresql/how-to/manage-connection-pools/) about how and why to use PgBouncer connection pooling including details about the available transaction modes.  To add a new connection pool to a PostgreSQL database cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/pools&#x60; specifying a name for the pool, the user to connect with, the database to connect to, as well as its desired size and transaction mode. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param connectionPool  (required)
     * @return AddNewConnectionPoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddNewConnectionPoolRequestBuilder addNewConnectionPool(String name, String mode, Integer size, String db, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (mode == null) throw new IllegalArgumentException("\"mode\" is required but got null");
            

        if (size == null) throw new IllegalArgumentException("\"size\" is required but got null");
        if (db == null) throw new IllegalArgumentException("\"db\" is required but got null");
            

        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new AddNewConnectionPoolRequestBuilder(name, mode, size, db, databaseClusterUuid);
    }
    private okhttp3.Call addUserCall(UUID databaseClusterUuid, DatabasesAddUserRequest databasesAddUserRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databasesAddUserRequest;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/users"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call addUserValidateBeforeCall(UUID databaseClusterUuid, DatabasesAddUserRequest databasesAddUserRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling addUser(Async)");
        }

        // verify the required parameter 'databasesAddUserRequest' is set
        if (databasesAddUserRequest == null) {
            throw new ApiException("Missing the required parameter 'databasesAddUserRequest' when calling addUser(Async)");
        }

        return addUserCall(databaseClusterUuid, databasesAddUserRequest, _callback);

    }


    private ApiResponse<DatabasesAddUserResponse> addUserWithHttpInfo(UUID databaseClusterUuid, DatabasesAddUserRequest databasesAddUserRequest) throws ApiException {
        okhttp3.Call localVarCall = addUserValidateBeforeCall(databaseClusterUuid, databasesAddUserRequest, null);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call addUserAsync(UUID databaseClusterUuid, DatabasesAddUserRequest databasesAddUserRequest, final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = addUserValidateBeforeCall(databaseClusterUuid, databasesAddUserRequest, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AddUserRequestBuilder {
        private final UUID databaseClusterUuid;
        private String name;
        private String role;
        private String password;
        private String accessCert;
        private String accessKey;
        private MysqlSettings mysqlSettings;
        private UserSettings settings;
        private Boolean readonly;

        private AddUserRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set name
         * @param name The name of a database user. (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set role
         * @param role A string representing the database user&#39;s role. The value will be either \\\&quot;primary\\\&quot; or \\\&quot;normal\\\&quot;.  (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder role(String role) {
            this.role = role;
            return this;
        }
        
        /**
         * Set password
         * @param password A randomly generated password for the database user. (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder password(String password) {
            this.password = password;
            return this;
        }
        
        /**
         * Set accessCert
         * @param accessCert Access certificate for TLS client authentication. (Kafka only) (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder accessCert(String accessCert) {
            this.accessCert = accessCert;
            return this;
        }
        
        /**
         * Set accessKey
         * @param accessKey Access key for TLS client authentication. (Kafka only) (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder accessKey(String accessKey) {
            this.accessKey = accessKey;
            return this;
        }
        
        /**
         * Set mysqlSettings
         * @param mysqlSettings  (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder mysqlSettings(MysqlSettings mysqlSettings) {
            this.mysqlSettings = mysqlSettings;
            return this;
        }
        
        /**
         * Set settings
         * @param settings  (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder settings(UserSettings settings) {
            this.settings = settings;
            return this;
        }
        
        /**
         * Set readonly
         * @param readonly For MongoDB clusters, set to &#x60;true&#x60; to create a read-only user. This option is not currently supported for other database engines.               (optional)
         * @return AddUserRequestBuilder
         */
        public AddUserRequestBuilder readonly(Boolean readonly) {
            this.readonly = readonly;
            return this;
        }
        
        /**
         * Build call for addUser
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabasesAddUserRequest databasesAddUserRequest = buildBodyParams();
            return addUserCall(databaseClusterUuid, databasesAddUserRequest, _callback);
        }

        private DatabasesAddUserRequest buildBodyParams() {
            DatabasesAddUserRequest databasesAddUserRequest = new DatabasesAddUserRequest();
            return databasesAddUserRequest;
        }

        /**
         * Execute addUser request
         * @return DatabasesAddUserResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddUserResponse execute() throws ApiException {
            DatabasesAddUserRequest databasesAddUserRequest = buildBodyParams();
            ApiResponse<DatabasesAddUserResponse> localVarResp = addUserWithHttpInfo(databaseClusterUuid, databasesAddUserRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute addUser request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddUserResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddUserResponse> executeWithHttpInfo() throws ApiException {
            DatabasesAddUserRequest databasesAddUserRequest = buildBodyParams();
            return addUserWithHttpInfo(databaseClusterUuid, databasesAddUserRequest);
        }

        /**
         * Execute addUser request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {
            DatabasesAddUserRequest databasesAddUserRequest = buildBodyParams();
            return addUserAsync(databaseClusterUuid, databasesAddUserRequest, _callback);
        }
    }

    /**
     * Add a Database User
     * To add a new database user, send a POST request to &#x60;/v2/databases/$DATABASE_ID/users&#x60; with the desired username.  Note: User management is not supported for Redis clusters.  When adding a user to a MySQL cluster, additional options can be configured in the &#x60;mysql_settings&#x60; object.  When adding a user to a Kafka cluster, additional options can be configured in the &#x60;settings&#x60; object.  The response will be a JSON object with a key called &#x60;user&#x60;. The value of this will be an object that contains the standard attributes associated with a database user including its randomly generated password. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databasesAddUserRequest  (required)
     * @return AddUserRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddUserRequestBuilder addUser(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new AddUserRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call configureEvictionPolicyCall(UUID databaseClusterUuid, DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databasesConfigureEvictionPolicyRequest;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/eviction_policy"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call configureEvictionPolicyValidateBeforeCall(UUID databaseClusterUuid, DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling configureEvictionPolicy(Async)");
        }

        // verify the required parameter 'databasesConfigureEvictionPolicyRequest' is set
        if (databasesConfigureEvictionPolicyRequest == null) {
            throw new ApiException("Missing the required parameter 'databasesConfigureEvictionPolicyRequest' when calling configureEvictionPolicy(Async)");
        }

        return configureEvictionPolicyCall(databaseClusterUuid, databasesConfigureEvictionPolicyRequest, _callback);

    }


    private ApiResponse<Void> configureEvictionPolicyWithHttpInfo(UUID databaseClusterUuid, DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest) throws ApiException {
        okhttp3.Call localVarCall = configureEvictionPolicyValidateBeforeCall(databaseClusterUuid, databasesConfigureEvictionPolicyRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call configureEvictionPolicyAsync(UUID databaseClusterUuid, DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = configureEvictionPolicyValidateBeforeCall(databaseClusterUuid, databasesConfigureEvictionPolicyRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class ConfigureEvictionPolicyRequestBuilder {
        private final EvictionPolicyModel evictionPolicy;
        private final UUID databaseClusterUuid;

        private ConfigureEvictionPolicyRequestBuilder(EvictionPolicyModel evictionPolicy, UUID databaseClusterUuid) {
            this.evictionPolicy = evictionPolicy;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for configureEvictionPolicy
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest = buildBodyParams();
            return configureEvictionPolicyCall(databaseClusterUuid, databasesConfigureEvictionPolicyRequest, _callback);
        }

        private DatabasesConfigureEvictionPolicyRequest buildBodyParams() {
            DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest = new DatabasesConfigureEvictionPolicyRequest();
            databasesConfigureEvictionPolicyRequest.evictionPolicy(this.evictionPolicy);
            return databasesConfigureEvictionPolicyRequest;
        }

        /**
         * Execute configureEvictionPolicy request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest = buildBodyParams();
            configureEvictionPolicyWithHttpInfo(databaseClusterUuid, databasesConfigureEvictionPolicyRequest);
        }

        /**
         * Execute configureEvictionPolicy request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest = buildBodyParams();
            return configureEvictionPolicyWithHttpInfo(databaseClusterUuid, databasesConfigureEvictionPolicyRequest);
        }

        /**
         * Execute configureEvictionPolicy request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            DatabasesConfigureEvictionPolicyRequest databasesConfigureEvictionPolicyRequest = buildBodyParams();
            return configureEvictionPolicyAsync(databaseClusterUuid, databasesConfigureEvictionPolicyRequest, _callback);
        }
    }

    /**
     * Configure the Eviction Policy for a Redis Cluster
     * To configure an eviction policy for an existing Redis cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/eviction_policy&#x60; specifying the desired policy.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databasesConfigureEvictionPolicyRequest  (required)
     * @return ConfigureEvictionPolicyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ConfigureEvictionPolicyRequestBuilder configureEvictionPolicy(EvictionPolicyModel evictionPolicy, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (evictionPolicy == null) throw new IllegalArgumentException("\"evictionPolicy\" is required but got null");
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ConfigureEvictionPolicyRequestBuilder(evictionPolicy, databaseClusterUuid);
    }
    private okhttp3.Call configureMaintenanceWindowCall(UUID databaseClusterUuid, DatabaseMaintenanceWindow databaseMaintenanceWindow, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databaseMaintenanceWindow;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/maintenance"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call configureMaintenanceWindowValidateBeforeCall(UUID databaseClusterUuid, DatabaseMaintenanceWindow databaseMaintenanceWindow, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling configureMaintenanceWindow(Async)");
        }

        // verify the required parameter 'databaseMaintenanceWindow' is set
        if (databaseMaintenanceWindow == null) {
            throw new ApiException("Missing the required parameter 'databaseMaintenanceWindow' when calling configureMaintenanceWindow(Async)");
        }

        return configureMaintenanceWindowCall(databaseClusterUuid, databaseMaintenanceWindow, _callback);

    }


    private ApiResponse<Void> configureMaintenanceWindowWithHttpInfo(UUID databaseClusterUuid, DatabaseMaintenanceWindow databaseMaintenanceWindow) throws ApiException {
        okhttp3.Call localVarCall = configureMaintenanceWindowValidateBeforeCall(databaseClusterUuid, databaseMaintenanceWindow, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call configureMaintenanceWindowAsync(UUID databaseClusterUuid, DatabaseMaintenanceWindow databaseMaintenanceWindow, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = configureMaintenanceWindowValidateBeforeCall(databaseClusterUuid, databaseMaintenanceWindow, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class ConfigureMaintenanceWindowRequestBuilder {
        private final String day;
        private final String hour;
        private final UUID databaseClusterUuid;
        private List<String> description;
        private Boolean pending;

        private ConfigureMaintenanceWindowRequestBuilder(String day, String hour, UUID databaseClusterUuid) {
            this.day = day;
            this.hour = hour;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set description
         * @param description A list of strings, each containing information about a pending maintenance update. (optional)
         * @return ConfigureMaintenanceWindowRequestBuilder
         */
        public ConfigureMaintenanceWindowRequestBuilder description(List<String> description) {
            this.description = description;
            return this;
        }
        
        /**
         * Set pending
         * @param pending A boolean value indicating whether any maintenance is scheduled to be performed in the next window. (optional)
         * @return ConfigureMaintenanceWindowRequestBuilder
         */
        public ConfigureMaintenanceWindowRequestBuilder pending(Boolean pending) {
            this.pending = pending;
            return this;
        }
        
        /**
         * Build call for configureMaintenanceWindow
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabaseMaintenanceWindow databaseMaintenanceWindow = buildBodyParams();
            return configureMaintenanceWindowCall(databaseClusterUuid, databaseMaintenanceWindow, _callback);
        }

        private DatabaseMaintenanceWindow buildBodyParams() {
            DatabaseMaintenanceWindow databaseMaintenanceWindow = new DatabaseMaintenanceWindow();
            databaseMaintenanceWindow.description(this.description);
            databaseMaintenanceWindow.day(this.day);
            databaseMaintenanceWindow.hour(this.hour);
            databaseMaintenanceWindow.pending(this.pending);
            return databaseMaintenanceWindow;
        }

        /**
         * Execute configureMaintenanceWindow request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DatabaseMaintenanceWindow databaseMaintenanceWindow = buildBodyParams();
            configureMaintenanceWindowWithHttpInfo(databaseClusterUuid, databaseMaintenanceWindow);
        }

        /**
         * Execute configureMaintenanceWindow request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            DatabaseMaintenanceWindow databaseMaintenanceWindow = buildBodyParams();
            return configureMaintenanceWindowWithHttpInfo(databaseClusterUuid, databaseMaintenanceWindow);
        }

        /**
         * Execute configureMaintenanceWindow request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            DatabaseMaintenanceWindow databaseMaintenanceWindow = buildBodyParams();
            return configureMaintenanceWindowAsync(databaseClusterUuid, databaseMaintenanceWindow, _callback);
        }
    }

    /**
     * Configure a Database Cluster&#39;s Maintenance Window
     * To configure the window when automatic maintenance should be performed for a database cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/maintenance&#x60;. A successful request will receive a 204 No Content status code with no body in response.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databaseMaintenanceWindow  (required)
     * @return ConfigureMaintenanceWindowRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ConfigureMaintenanceWindowRequestBuilder configureMaintenanceWindow(String day, String hour, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (day == null) throw new IllegalArgumentException("\"day\" is required but got null");
            

        if (hour == null) throw new IllegalArgumentException("\"hour\" is required but got null");
            

        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ConfigureMaintenanceWindowRequestBuilder(day, hour, databaseClusterUuid);
    }
    private okhttp3.Call createClusterCall(DatabasesCreateClusterRequest databasesCreateClusterRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databasesCreateClusterRequest;

        // create path and map variables
        String localVarPath = "/v2/databases";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createClusterValidateBeforeCall(DatabasesCreateClusterRequest databasesCreateClusterRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databasesCreateClusterRequest' is set
        if (databasesCreateClusterRequest == null) {
            throw new ApiException("Missing the required parameter 'databasesCreateClusterRequest' when calling createCluster(Async)");
        }

        return createClusterCall(databasesCreateClusterRequest, _callback);

    }


    private ApiResponse<DatabasesCreateClusterResponse> createClusterWithHttpInfo(DatabasesCreateClusterRequest databasesCreateClusterRequest) throws ApiException {
        okhttp3.Call localVarCall = createClusterValidateBeforeCall(databasesCreateClusterRequest, null);
        Type localVarReturnType = new TypeToken<DatabasesCreateClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createClusterAsync(DatabasesCreateClusterRequest databasesCreateClusterRequest, final ApiCallback<DatabasesCreateClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createClusterValidateBeforeCall(databasesCreateClusterRequest, _callback);
        Type localVarReturnType = new TypeToken<DatabasesCreateClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateClusterRequestBuilder {
        private List<String> tags;
        private String version;
        private UUID id;
        private String name;
        private String engine;
        private String semanticVersion;
        private Integer numNodes;
        private String size;
        private String region;
        private String status;
        private OffsetDateTime createdAt;
        private String privateNetworkUuid;
        private List<String> dbNames;
        private DatabaseClusterConnection connection;
        private DatabaseClusterConnection privateConnection;
        private DatabaseClusterConnection standbyConnection;
        private DatabaseClusterConnection standbyPrivateConnection;
        private List<DatabaseUser> users;
        private DatabaseClusterMaintenanceWindow maintenanceWindow;
        private UUID projectId;
        private List<FirewallRule> rules;
        private String versionEndOfLife;
        private String versionEndOfAvailability;
        private Integer storageSizeMib;
        private List<DatabaseServiceEndpoint> metricsEndpoints;
        private DatabaseBackup backupRestore;

        private CreateClusterRequestBuilder() {
        }

        /**
         * Set tags
         * @param tags An array of tags that have been applied to the database cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set version
         * @param version A string representing the version of the database engine in use for the cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder version(String version) {
            this.version = version;
            return this;
        }
        
        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a database cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set name
         * @param name A unique, human-readable name referring to a database cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set engine
         * @param engine A slug representing the database engine used for the cluster. The possible values are: \\\&quot;pg\\\&quot; for PostgreSQL, \\\&quot;mysql\\\&quot; for MySQL, \\\&quot;redis\\\&quot; for Redis, \\\&quot;mongodb\\\&quot; for MongoDB, and \\\&quot;kafka\\\&quot; for Kafka. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder engine(String engine) {
            this.engine = engine;
            return this;
        }
        
        /**
         * Set semanticVersion
         * @param semanticVersion A string representing the semantic version of the database engine in use for the cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder semanticVersion(String semanticVersion) {
            this.semanticVersion = semanticVersion;
            return this;
        }
        
        /**
         * Set numNodes
         * @param numNodes The number of nodes in the database cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder numNodes(Integer numNodes) {
            this.numNodes = numNodes;
            return this;
        }
        
        /**
         * Set size
         * @param size The slug identifier representing the size of the nodes in the database cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder size(String size) {
            this.size = size;
            return this;
        }
        
        /**
         * Set region
         * @param region The slug identifier for the region where the database cluster is located. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder region(String region) {
            this.region = region;
            return this;
        }
        
        /**
         * Set status
         * @param status A string representing the current status of the database cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder status(String status) {
            this.status = status;
            return this;
        }
        
        /**
         * Set createdAt
         * @param createdAt A time value given in ISO8601 combined date and time format that represents when the database cluster was created. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        /**
         * Set privateNetworkUuid
         * @param privateNetworkUuid A string specifying the UUID of the VPC to which the database cluster will be assigned. If excluded, the cluster when creating a new database cluster, it will be assigned to your account&#39;s default VPC for the region. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder privateNetworkUuid(String privateNetworkUuid) {
            this.privateNetworkUuid = privateNetworkUuid;
            return this;
        }
        
        /**
         * Set dbNames
         * @param dbNames An array of strings containing the names of databases created in the database cluster. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder dbNames(List<String> dbNames) {
            this.dbNames = dbNames;
            return this;
        }
        
        /**
         * Set connection
         * @param connection  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder connection(DatabaseClusterConnection connection) {
            this.connection = connection;
            return this;
        }
        
        /**
         * Set privateConnection
         * @param privateConnection  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder privateConnection(DatabaseClusterConnection privateConnection) {
            this.privateConnection = privateConnection;
            return this;
        }
        
        /**
         * Set standbyConnection
         * @param standbyConnection  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder standbyConnection(DatabaseClusterConnection standbyConnection) {
            this.standbyConnection = standbyConnection;
            return this;
        }
        
        /**
         * Set standbyPrivateConnection
         * @param standbyPrivateConnection  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder standbyPrivateConnection(DatabaseClusterConnection standbyPrivateConnection) {
            this.standbyPrivateConnection = standbyPrivateConnection;
            return this;
        }
        
        /**
         * Set users
         * @param users  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder users(List<DatabaseUser> users) {
            this.users = users;
            return this;
        }
        
        /**
         * Set maintenanceWindow
         * @param maintenanceWindow  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder maintenanceWindow(DatabaseClusterMaintenanceWindow maintenanceWindow) {
            this.maintenanceWindow = maintenanceWindow;
            return this;
        }
        
        /**
         * Set projectId
         * @param projectId The ID of the project that the database cluster is assigned to. If excluded when creating a new database cluster, it will be assigned to your default project. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder projectId(UUID projectId) {
            this.projectId = projectId;
            return this;
        }
        
        /**
         * Set rules
         * @param rules  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder rules(List<FirewallRule> rules) {
            this.rules = rules;
            return this;
        }
        
        /**
         * Set versionEndOfLife
         * @param versionEndOfLife A timestamp referring to the date when the particular version will no longer be supported. If null, the version does not have an end of life timeline. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder versionEndOfLife(String versionEndOfLife) {
            this.versionEndOfLife = versionEndOfLife;
            return this;
        }
        
        /**
         * Set versionEndOfAvailability
         * @param versionEndOfAvailability A timestamp referring to the date when the particular version will no longer be available for creating new clusters. If null, the version does not have an end of availability timeline. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder versionEndOfAvailability(String versionEndOfAvailability) {
            this.versionEndOfAvailability = versionEndOfAvailability;
            return this;
        }
        
        /**
         * Set storageSizeMib
         * @param storageSizeMib Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the &#39;size&#39; and any previously added additional storage. (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder storageSizeMib(Integer storageSizeMib) {
            this.storageSizeMib = storageSizeMib;
            return this;
        }
        
        /**
         * Set metricsEndpoints
         * @param metricsEndpoints Public hostname and port of the cluster&#39;s metrics endpoint(s). Includes one record for the cluster&#39;s primary node and a second entry for the cluster&#39;s standby node(s). (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder metricsEndpoints(List<DatabaseServiceEndpoint> metricsEndpoints) {
            this.metricsEndpoints = metricsEndpoints;
            return this;
        }
        
        /**
         * Set backupRestore
         * @param backupRestore  (optional)
         * @return CreateClusterRequestBuilder
         */
        public CreateClusterRequestBuilder backupRestore(DatabaseBackup backupRestore) {
            this.backupRestore = backupRestore;
            return this;
        }
        
        /**
         * Build call for createCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabasesCreateClusterRequest databasesCreateClusterRequest = buildBodyParams();
            return createClusterCall(databasesCreateClusterRequest, _callback);
        }

        private DatabasesCreateClusterRequest buildBodyParams() {
            DatabasesCreateClusterRequest databasesCreateClusterRequest = new DatabasesCreateClusterRequest();
            return databasesCreateClusterRequest;
        }

        /**
         * Execute createCluster request
         * @return DatabasesCreateClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesCreateClusterResponse execute() throws ApiException {
            DatabasesCreateClusterRequest databasesCreateClusterRequest = buildBodyParams();
            ApiResponse<DatabasesCreateClusterResponse> localVarResp = createClusterWithHttpInfo(databasesCreateClusterRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createCluster request with HTTP info returned
         * @return ApiResponse&lt;DatabasesCreateClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesCreateClusterResponse> executeWithHttpInfo() throws ApiException {
            DatabasesCreateClusterRequest databasesCreateClusterRequest = buildBodyParams();
            return createClusterWithHttpInfo(databasesCreateClusterRequest);
        }

        /**
         * Execute createCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesCreateClusterResponse> _callback) throws ApiException {
            DatabasesCreateClusterRequest databasesCreateClusterRequest = buildBodyParams();
            return createClusterAsync(databasesCreateClusterRequest, _callback);
        }
    }

    /**
     * Create a New Database Cluster
     * To create a database cluster, send a POST request to &#x60;/v2/databases&#x60;. The response will be a JSON object with a key called &#x60;database&#x60;. The value of this will be an object that contains the standard attributes associated with a database cluster. The initial value of the database cluster&#39;s &#x60;status&#x60; attribute will be &#x60;creating&#x60;. When the cluster is ready to receive traffic, this will transition to &#x60;online&#x60;.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects will contain the information needed to connect to the cluster&#39;s standby node(s).  DigitalOcean managed PostgreSQL and MySQL database clusters take automated daily backups. To create a new database cluster based on a backup of an existing cluster, send a POST request to &#x60;/v2/databases&#x60;. In addition to the standard database cluster attributes, the JSON body must include a key named &#x60;backup_restore&#x60; with the name of the original database cluster and the timestamp of the backup to be restored. Creating a database from a backup is the same as forking a database in the control panel. Note: Backups are not supported for Redis clusters.
     * @param databasesCreateClusterRequest  (required)
     * @return CreateClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateClusterRequestBuilder createCluster() throws IllegalArgumentException {
        return new CreateClusterRequestBuilder();
    }
    private okhttp3.Call createReadOnlyReplicaCall(UUID databaseClusterUuid, DatabaseReplica databaseReplica, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databaseReplica;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/replicas"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createReadOnlyReplicaValidateBeforeCall(UUID databaseClusterUuid, DatabaseReplica databaseReplica, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling createReadOnlyReplica(Async)");
        }

        return createReadOnlyReplicaCall(databaseClusterUuid, databaseReplica, _callback);

    }


    private ApiResponse<DatabasesCreateReadOnlyReplicaResponse> createReadOnlyReplicaWithHttpInfo(UUID databaseClusterUuid, DatabaseReplica databaseReplica) throws ApiException {
        okhttp3.Call localVarCall = createReadOnlyReplicaValidateBeforeCall(databaseClusterUuid, databaseReplica, null);
        Type localVarReturnType = new TypeToken<DatabasesCreateReadOnlyReplicaResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createReadOnlyReplicaAsync(UUID databaseClusterUuid, DatabaseReplica databaseReplica, final ApiCallback<DatabasesCreateReadOnlyReplicaResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createReadOnlyReplicaValidateBeforeCall(databaseClusterUuid, databaseReplica, _callback);
        Type localVarReturnType = new TypeToken<DatabasesCreateReadOnlyReplicaResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateReadOnlyReplicaRequestBuilder {
        private final String name;
        private final UUID databaseClusterUuid;
        private List<String> tags;
        private UUID id;
        private String region;
        private String size;
        private String status;
        private OffsetDateTime createdAt;
        private String privateNetworkUuid;
        private DatabaseReplicaConnection connection;
        private DatabaseReplicaConnection privateConnection;
        private Integer storageSizeMib;

        private CreateReadOnlyReplicaRequestBuilder(String name, UUID databaseClusterUuid) {
            this.name = name;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set tags
         * @param tags A flat array of tag names as strings to apply to the read-only replica after it is created. Tag names can either be existing or new tags. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a database replica. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set region
         * @param region A slug identifier for the region where the read-only replica will be located. If excluded, the replica will be placed in the same region as the cluster. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder region(String region) {
            this.region = region;
            return this;
        }
        
        /**
         * Set size
         * @param size A slug identifier representing the size of the node for the read-only replica. The size of the replica must be at least as large as the node size for the database cluster from which it is replicating. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder size(String size) {
            this.size = size;
            return this;
        }
        
        /**
         * Set status
         * @param status A string representing the current status of the database cluster. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder status(String status) {
            this.status = status;
            return this;
        }
        
        /**
         * Set createdAt
         * @param createdAt A time value given in ISO8601 combined date and time format that represents when the database cluster was created. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        /**
         * Set privateNetworkUuid
         * @param privateNetworkUuid A string specifying the UUID of the VPC to which the read-only replica will be assigned. If excluded, the replica will be assigned to your account&#39;s default VPC for the region. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder privateNetworkUuid(String privateNetworkUuid) {
            this.privateNetworkUuid = privateNetworkUuid;
            return this;
        }
        
        /**
         * Set connection
         * @param connection  (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder connection(DatabaseReplicaConnection connection) {
            this.connection = connection;
            return this;
        }
        
        /**
         * Set privateConnection
         * @param privateConnection  (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder privateConnection(DatabaseReplicaConnection privateConnection) {
            this.privateConnection = privateConnection;
            return this;
        }
        
        /**
         * Set storageSizeMib
         * @param storageSizeMib Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the &#39;size&#39; and any previously added additional storage. (optional)
         * @return CreateReadOnlyReplicaRequestBuilder
         */
        public CreateReadOnlyReplicaRequestBuilder storageSizeMib(Integer storageSizeMib) {
            this.storageSizeMib = storageSizeMib;
            return this;
        }
        
        /**
         * Build call for createReadOnlyReplica
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabaseReplica databaseReplica = buildBodyParams();
            return createReadOnlyReplicaCall(databaseClusterUuid, databaseReplica, _callback);
        }

        private DatabaseReplica buildBodyParams() {
            DatabaseReplica databaseReplica = new DatabaseReplica();
            databaseReplica.tags(this.tags);
            databaseReplica.id(this.id);
            databaseReplica.name(this.name);
            databaseReplica.region(this.region);
            databaseReplica.size(this.size);
            if (this.status != null)
            databaseReplica.status(DatabaseReplica.StatusEnum.fromValue(this.status));
            databaseReplica.createdAt(this.createdAt);
            databaseReplica.privateNetworkUuid(this.privateNetworkUuid);
            databaseReplica.connection(this.connection);
            databaseReplica.privateConnection(this.privateConnection);
            databaseReplica.storageSizeMib(this.storageSizeMib);
            return databaseReplica;
        }

        /**
         * Execute createReadOnlyReplica request
         * @return DatabasesCreateReadOnlyReplicaResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesCreateReadOnlyReplicaResponse execute() throws ApiException {
            DatabaseReplica databaseReplica = buildBodyParams();
            ApiResponse<DatabasesCreateReadOnlyReplicaResponse> localVarResp = createReadOnlyReplicaWithHttpInfo(databaseClusterUuid, databaseReplica);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createReadOnlyReplica request with HTTP info returned
         * @return ApiResponse&lt;DatabasesCreateReadOnlyReplicaResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesCreateReadOnlyReplicaResponse> executeWithHttpInfo() throws ApiException {
            DatabaseReplica databaseReplica = buildBodyParams();
            return createReadOnlyReplicaWithHttpInfo(databaseClusterUuid, databaseReplica);
        }

        /**
         * Execute createReadOnlyReplica request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesCreateReadOnlyReplicaResponse> _callback) throws ApiException {
            DatabaseReplica databaseReplica = buildBodyParams();
            return createReadOnlyReplicaAsync(databaseClusterUuid, databaseReplica, _callback);
        }
    }

    /**
     * Create a Read-only Replica
     * To create a read-only replica for a PostgreSQL or MySQL database cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/replicas&#x60; specifying the name it should be given, the size of the node to be used, and the region where it will be located.  **Note**: Read-only replicas are not supported for Redis clusters.  The response will be a JSON object with a key called &#x60;replica&#x60;. The value of this will be an object that contains the standard attributes associated with a database replica. The initial value of the read-only replica&#39;s &#x60;status&#x60; attribute will be &#x60;forking&#x60;. When the replica is ready to receive traffic, this will transition to &#x60;active&#x60;.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return CreateReadOnlyReplicaRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateReadOnlyReplicaRequestBuilder createReadOnlyReplica(String name, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new CreateReadOnlyReplicaRequestBuilder(name, databaseClusterUuid);
    }
    private okhttp3.Call createTopicKafkaClusterCall(UUID databaseClusterUuid, KafkaTopicCreate kafkaTopicCreate, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = kafkaTopicCreate;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/topics"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call createTopicKafkaClusterValidateBeforeCall(UUID databaseClusterUuid, KafkaTopicCreate kafkaTopicCreate, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling createTopicKafkaCluster(Async)");
        }

        return createTopicKafkaClusterCall(databaseClusterUuid, kafkaTopicCreate, _callback);

    }


    private ApiResponse<DatabasesCreateTopicKafkaClusterResponse> createTopicKafkaClusterWithHttpInfo(UUID databaseClusterUuid, KafkaTopicCreate kafkaTopicCreate) throws ApiException {
        okhttp3.Call localVarCall = createTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, kafkaTopicCreate, null);
        Type localVarReturnType = new TypeToken<DatabasesCreateTopicKafkaClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createTopicKafkaClusterAsync(UUID databaseClusterUuid, KafkaTopicCreate kafkaTopicCreate, final ApiCallback<DatabasesCreateTopicKafkaClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, kafkaTopicCreate, _callback);
        Type localVarReturnType = new TypeToken<DatabasesCreateTopicKafkaClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateTopicKafkaClusterRequestBuilder {
        private final UUID databaseClusterUuid;
        private String name;
        private Integer replicationFactor;
        private Integer partitionCount;
        private KafkaTopicConfig config;

        private CreateTopicKafkaClusterRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set name
         * @param name The name of the Kafka topic. (optional)
         * @return CreateTopicKafkaClusterRequestBuilder
         */
        public CreateTopicKafkaClusterRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set replicationFactor
         * @param replicationFactor The number of nodes to replicate data across the cluster. (optional)
         * @return CreateTopicKafkaClusterRequestBuilder
         */
        public CreateTopicKafkaClusterRequestBuilder replicationFactor(Integer replicationFactor) {
            this.replicationFactor = replicationFactor;
            return this;
        }
        
        /**
         * Set partitionCount
         * @param partitionCount The number of partitions available for the topic. On update, this value can only be increased. (optional)
         * @return CreateTopicKafkaClusterRequestBuilder
         */
        public CreateTopicKafkaClusterRequestBuilder partitionCount(Integer partitionCount) {
            this.partitionCount = partitionCount;
            return this;
        }
        
        /**
         * Set config
         * @param config  (optional)
         * @return CreateTopicKafkaClusterRequestBuilder
         */
        public CreateTopicKafkaClusterRequestBuilder config(KafkaTopicConfig config) {
            this.config = config;
            return this;
        }
        
        /**
         * Build call for createTopicKafkaCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            KafkaTopicCreate kafkaTopicCreate = buildBodyParams();
            return createTopicKafkaClusterCall(databaseClusterUuid, kafkaTopicCreate, _callback);
        }

        private KafkaTopicCreate buildBodyParams() {
            KafkaTopicCreate kafkaTopicCreate = new KafkaTopicCreate();
            kafkaTopicCreate.name(this.name);
            kafkaTopicCreate.partitions(this.partitions);
            kafkaTopicCreate.replication(this.replication);
            kafkaTopicCreate.config(this.config);
            return kafkaTopicCreate;
        }

        /**
         * Execute createTopicKafkaCluster request
         * @return DatabasesCreateTopicKafkaClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesCreateTopicKafkaClusterResponse execute() throws ApiException {
            KafkaTopicCreate kafkaTopicCreate = buildBodyParams();
            ApiResponse<DatabasesCreateTopicKafkaClusterResponse> localVarResp = createTopicKafkaClusterWithHttpInfo(databaseClusterUuid, kafkaTopicCreate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createTopicKafkaCluster request with HTTP info returned
         * @return ApiResponse&lt;DatabasesCreateTopicKafkaClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesCreateTopicKafkaClusterResponse> executeWithHttpInfo() throws ApiException {
            KafkaTopicCreate kafkaTopicCreate = buildBodyParams();
            return createTopicKafkaClusterWithHttpInfo(databaseClusterUuid, kafkaTopicCreate);
        }

        /**
         * Execute createTopicKafkaCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesCreateTopicKafkaClusterResponse> _callback) throws ApiException {
            KafkaTopicCreate kafkaTopicCreate = buildBodyParams();
            return createTopicKafkaClusterAsync(databaseClusterUuid, kafkaTopicCreate, _callback);
        }
    }

    /**
     * Create Topic for a Kafka Cluster
     * To create a topic attached to a Kafka cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/topics&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return CreateTopicKafkaClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateTopicKafkaClusterRequestBuilder createTopicKafkaCluster(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new CreateTopicKafkaClusterRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call deleteCall(UUID databaseClusterUuid, String databaseName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/dbs/{database_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "database_name" + "}", localVarApiClient.escapeString(databaseName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteValidateBeforeCall(UUID databaseClusterUuid, String databaseName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling delete(Async)");
        }

        // verify the required parameter 'databaseName' is set
        if (databaseName == null) {
            throw new ApiException("Missing the required parameter 'databaseName' when calling delete(Async)");
        }

        return deleteCall(databaseClusterUuid, databaseName, _callback);

    }


    private ApiResponse<Void> deleteWithHttpInfo(UUID databaseClusterUuid, String databaseName) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(databaseClusterUuid, databaseName, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAsync(UUID databaseClusterUuid, String databaseName, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(databaseClusterUuid, databaseName, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String databaseName;

        private DeleteRequestBuilder(UUID databaseClusterUuid, String databaseName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.databaseName = databaseName;
        }

        /**
         * Build call for delete
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteCall(databaseClusterUuid, databaseName, _callback);
        }


        /**
         * Execute delete request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteWithHttpInfo(databaseClusterUuid, databaseName);
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteWithHttpInfo(databaseClusterUuid, databaseName);
        }

        /**
         * Execute delete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return deleteAsync(databaseClusterUuid, databaseName, _callback);
        }
    }

    /**
     * Delete a Database
     * To delete a specific database, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/dbs/$DB_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.  Note: Database management is not supported for Redis clusters. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databaseName The name of the database. (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(UUID databaseClusterUuid, String databaseName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (databaseName == null) throw new IllegalArgumentException("\"databaseName\" is required but got null");
            

        return new DeleteRequestBuilder(databaseClusterUuid, databaseName);
    }
    private okhttp3.Call deleteConnectionPoolCall(UUID databaseClusterUuid, String poolName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/pools/{pool_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "pool_name" + "}", localVarApiClient.escapeString(poolName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteConnectionPoolValidateBeforeCall(UUID databaseClusterUuid, String poolName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling deleteConnectionPool(Async)");
        }

        // verify the required parameter 'poolName' is set
        if (poolName == null) {
            throw new ApiException("Missing the required parameter 'poolName' when calling deleteConnectionPool(Async)");
        }

        return deleteConnectionPoolCall(databaseClusterUuid, poolName, _callback);

    }


    private ApiResponse<Void> deleteConnectionPoolWithHttpInfo(UUID databaseClusterUuid, String poolName) throws ApiException {
        okhttp3.Call localVarCall = deleteConnectionPoolValidateBeforeCall(databaseClusterUuid, poolName, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteConnectionPoolAsync(UUID databaseClusterUuid, String poolName, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteConnectionPoolValidateBeforeCall(databaseClusterUuid, poolName, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteConnectionPoolRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String poolName;

        private DeleteConnectionPoolRequestBuilder(UUID databaseClusterUuid, String poolName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.poolName = poolName;
        }

        /**
         * Build call for deleteConnectionPool
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteConnectionPoolCall(databaseClusterUuid, poolName, _callback);
        }


        /**
         * Execute deleteConnectionPool request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteConnectionPoolWithHttpInfo(databaseClusterUuid, poolName);
        }

        /**
         * Execute deleteConnectionPool request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteConnectionPoolWithHttpInfo(databaseClusterUuid, poolName);
        }

        /**
         * Execute deleteConnectionPool request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return deleteConnectionPoolAsync(databaseClusterUuid, poolName, _callback);
        }
    }

    /**
     * Delete a Connection Pool (PostgreSQL)
     * To delete a specific connection pool for a PostgreSQL database cluster, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param poolName The name used to identify the connection pool. (required)
     * @return DeleteConnectionPoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteConnectionPoolRequestBuilder deleteConnectionPool(UUID databaseClusterUuid, String poolName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (poolName == null) throw new IllegalArgumentException("\"poolName\" is required but got null");
            

        return new DeleteConnectionPoolRequestBuilder(databaseClusterUuid, poolName);
    }
    private okhttp3.Call deleteTopicKafkaClusterCall(UUID databaseClusterUuid, String topicName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/topics/{topic_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "topic_name" + "}", localVarApiClient.escapeString(topicName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteTopicKafkaClusterValidateBeforeCall(UUID databaseClusterUuid, String topicName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling deleteTopicKafkaCluster(Async)");
        }

        // verify the required parameter 'topicName' is set
        if (topicName == null) {
            throw new ApiException("Missing the required parameter 'topicName' when calling deleteTopicKafkaCluster(Async)");
        }

        return deleteTopicKafkaClusterCall(databaseClusterUuid, topicName, _callback);

    }


    private ApiResponse<Void> deleteTopicKafkaClusterWithHttpInfo(UUID databaseClusterUuid, String topicName) throws ApiException {
        okhttp3.Call localVarCall = deleteTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, topicName, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteTopicKafkaClusterAsync(UUID databaseClusterUuid, String topicName, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, topicName, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteTopicKafkaClusterRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String topicName;

        private DeleteTopicKafkaClusterRequestBuilder(UUID databaseClusterUuid, String topicName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.topicName = topicName;
        }

        /**
         * Build call for deleteTopicKafkaCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteTopicKafkaClusterCall(databaseClusterUuid, topicName, _callback);
        }


        /**
         * Execute deleteTopicKafkaCluster request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteTopicKafkaClusterWithHttpInfo(databaseClusterUuid, topicName);
        }

        /**
         * Execute deleteTopicKafkaCluster request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteTopicKafkaClusterWithHttpInfo(databaseClusterUuid, topicName);
        }

        /**
         * Execute deleteTopicKafkaCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return deleteTopicKafkaClusterAsync(databaseClusterUuid, topicName, _callback);
        }
    }

    /**
     * Delete Topic for a Kafka Cluster
     * To delete a single topic within a Kafka cluster, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param topicName The name used to identify the Kafka topic. (required)
     * @return DeleteTopicKafkaClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteTopicKafkaClusterRequestBuilder deleteTopicKafkaCluster(UUID databaseClusterUuid, String topicName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (topicName == null) throw new IllegalArgumentException("\"topicName\" is required but got null");
            

        return new DeleteTopicKafkaClusterRequestBuilder(databaseClusterUuid, topicName);
    }
    private okhttp3.Call destroyClusterCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call destroyClusterValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling destroyCluster(Async)");
        }

        return destroyClusterCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<Void> destroyClusterWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = destroyClusterValidateBeforeCall(databaseClusterUuid, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call destroyClusterAsync(UUID databaseClusterUuid, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = destroyClusterValidateBeforeCall(databaseClusterUuid, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DestroyClusterRequestBuilder {
        private final UUID databaseClusterUuid;

        private DestroyClusterRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for destroyCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return destroyClusterCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute destroyCluster request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            destroyClusterWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute destroyCluster request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return destroyClusterWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute destroyCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return destroyClusterAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * Destroy a Database Cluster
     * To destroy a specific database, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID&#x60;. A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return DestroyClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DestroyClusterRequestBuilder destroyCluster(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new DestroyClusterRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call destroyReadonlyReplicaCall(UUID databaseClusterUuid, String replicaName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/replicas/{replica_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "replica_name" + "}", localVarApiClient.escapeString(replicaName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call destroyReadonlyReplicaValidateBeforeCall(UUID databaseClusterUuid, String replicaName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling destroyReadonlyReplica(Async)");
        }

        // verify the required parameter 'replicaName' is set
        if (replicaName == null) {
            throw new ApiException("Missing the required parameter 'replicaName' when calling destroyReadonlyReplica(Async)");
        }

        return destroyReadonlyReplicaCall(databaseClusterUuid, replicaName, _callback);

    }


    private ApiResponse<Void> destroyReadonlyReplicaWithHttpInfo(UUID databaseClusterUuid, String replicaName) throws ApiException {
        okhttp3.Call localVarCall = destroyReadonlyReplicaValidateBeforeCall(databaseClusterUuid, replicaName, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call destroyReadonlyReplicaAsync(UUID databaseClusterUuid, String replicaName, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = destroyReadonlyReplicaValidateBeforeCall(databaseClusterUuid, replicaName, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DestroyReadonlyReplicaRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String replicaName;

        private DestroyReadonlyReplicaRequestBuilder(UUID databaseClusterUuid, String replicaName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.replicaName = replicaName;
        }

        /**
         * Build call for destroyReadonlyReplica
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return destroyReadonlyReplicaCall(databaseClusterUuid, replicaName, _callback);
        }


        /**
         * Execute destroyReadonlyReplica request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            destroyReadonlyReplicaWithHttpInfo(databaseClusterUuid, replicaName);
        }

        /**
         * Execute destroyReadonlyReplica request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return destroyReadonlyReplicaWithHttpInfo(databaseClusterUuid, replicaName);
        }

        /**
         * Execute destroyReadonlyReplica request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return destroyReadonlyReplicaAsync(databaseClusterUuid, replicaName, _callback);
        }
    }

    /**
     * Destroy a Read-only Replica
     * To destroy a specific read-only replica, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param replicaName The name of the database replica. (required)
     * @return DestroyReadonlyReplicaRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DestroyReadonlyReplicaRequestBuilder destroyReadonlyReplica(UUID databaseClusterUuid, String replicaName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (replicaName == null) throw new IllegalArgumentException("\"replicaName\" is required but got null");
            

        return new DestroyReadonlyReplicaRequestBuilder(databaseClusterUuid, replicaName);
    }
    private okhttp3.Call getCall(UUID databaseClusterUuid, String databaseName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/dbs/{database_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "database_name" + "}", localVarApiClient.escapeString(databaseName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getValidateBeforeCall(UUID databaseClusterUuid, String databaseName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling get(Async)");
        }

        // verify the required parameter 'databaseName' is set
        if (databaseName == null) {
            throw new ApiException("Missing the required parameter 'databaseName' when calling get(Async)");
        }

        return getCall(databaseClusterUuid, databaseName, _callback);

    }


    private ApiResponse<DatabasesAddResponse> getWithHttpInfo(UUID databaseClusterUuid, String databaseName) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(databaseClusterUuid, databaseName, null);
        Type localVarReturnType = new TypeToken<DatabasesAddResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(UUID databaseClusterUuid, String databaseName, final ApiCallback<DatabasesAddResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(databaseClusterUuid, databaseName, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String databaseName;

        private GetRequestBuilder(UUID databaseClusterUuid, String databaseName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.databaseName = databaseName;
        }

        /**
         * Build call for get
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(databaseClusterUuid, databaseName, _callback);
        }


        /**
         * Execute get request
         * @return DatabasesAddResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddResponse execute() throws ApiException {
            ApiResponse<DatabasesAddResponse> localVarResp = getWithHttpInfo(databaseClusterUuid, databaseName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddResponse> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(databaseClusterUuid, databaseName);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddResponse> _callback) throws ApiException {
            return getAsync(databaseClusterUuid, databaseName, _callback);
        }
    }

    /**
     * Retrieve an Existing Database
     * To show information about an existing database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/dbs/$DB_NAME&#x60;.  Note: Database management is not supported for Redis clusters.  The response will be a JSON object with a &#x60;db&#x60; key. This will be set to an object containing the standard database attributes. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databaseName The name of the database. (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;db&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetRequestBuilder get(UUID databaseClusterUuid, String databaseName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (databaseName == null) throw new IllegalArgumentException("\"databaseName\" is required but got null");
            

        return new GetRequestBuilder(databaseClusterUuid, databaseName);
    }
    private okhttp3.Call getClusterConfigCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/config"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getClusterConfigValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getClusterConfig(Async)");
        }

        return getClusterConfigCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesGetClusterConfigResponse> getClusterConfigWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = getClusterConfigValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesGetClusterConfigResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getClusterConfigAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesGetClusterConfigResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getClusterConfigValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesGetClusterConfigResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetClusterConfigRequestBuilder {
        private final UUID databaseClusterUuid;

        private GetClusterConfigRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for getClusterConfig
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;config&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getClusterConfigCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute getClusterConfig request
         * @return DatabasesGetClusterConfigResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;config&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesGetClusterConfigResponse execute() throws ApiException {
            ApiResponse<DatabasesGetClusterConfigResponse> localVarResp = getClusterConfigWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getClusterConfig request with HTTP info returned
         * @return ApiResponse&lt;DatabasesGetClusterConfigResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;config&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesGetClusterConfigResponse> executeWithHttpInfo() throws ApiException {
            return getClusterConfigWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute getClusterConfig request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;config&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesGetClusterConfigResponse> _callback) throws ApiException {
            return getClusterConfigAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * Retrieve an Existing Database Cluster Configuration
     * Shows configuration parameters for an existing database cluster by sending a GET request to &#x60;/v2/databases/$DATABASE_ID/config&#x60;. The response is a JSON object with a &#x60;config&#x60; key, which is set to an object containing any database configuration parameters. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return GetClusterConfigRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;config&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetClusterConfigRequestBuilder getClusterConfig(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new GetClusterConfigRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call getClusterInfoCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getClusterInfoValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getClusterInfo(Async)");
        }

        return getClusterInfoCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesCreateClusterResponse> getClusterInfoWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = getClusterInfoValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesCreateClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getClusterInfoAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesCreateClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getClusterInfoValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesCreateClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetClusterInfoRequestBuilder {
        private final UUID databaseClusterUuid;

        private GetClusterInfoRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for getClusterInfo
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getClusterInfoCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute getClusterInfo request
         * @return DatabasesCreateClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesCreateClusterResponse execute() throws ApiException {
            ApiResponse<DatabasesCreateClusterResponse> localVarResp = getClusterInfoWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getClusterInfo request with HTTP info returned
         * @return ApiResponse&lt;DatabasesCreateClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesCreateClusterResponse> executeWithHttpInfo() throws ApiException {
            return getClusterInfoWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute getClusterInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesCreateClusterResponse> _callback) throws ApiException {
            return getClusterInfoAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * Retrieve an Existing Database Cluster
     * To show information about an existing database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID&#x60;.  The response will be a JSON object with a database key. This will be set to an object containing the standard database cluster attributes.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects contain the information needed to connect to the cluster&#39;s standby node(s).  The embedded maintenance_window object will contain information about any scheduled maintenance for the database cluster.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return GetClusterInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;database&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetClusterInfoRequestBuilder getClusterInfo(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new GetClusterInfoRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call getClustersMetricsCredentialsCall(final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/metrics/credentials";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getClustersMetricsCredentialsValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return getClustersMetricsCredentialsCall(_callback);

    }


    private ApiResponse<DatabasesGetClustersMetricsCredentialsResponse> getClustersMetricsCredentialsWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = getClustersMetricsCredentialsValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<DatabasesGetClustersMetricsCredentialsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getClustersMetricsCredentialsAsync(final ApiCallback<DatabasesGetClustersMetricsCredentialsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getClustersMetricsCredentialsValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<DatabasesGetClustersMetricsCredentialsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetClustersMetricsCredentialsRequestBuilder {

        private GetClustersMetricsCredentialsRequestBuilder() {
        }

        /**
         * Build call for getClustersMetricsCredentials
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;credentials&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getClustersMetricsCredentialsCall(_callback);
        }


        /**
         * Execute getClustersMetricsCredentials request
         * @return DatabasesGetClustersMetricsCredentialsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;credentials&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesGetClustersMetricsCredentialsResponse execute() throws ApiException {
            ApiResponse<DatabasesGetClustersMetricsCredentialsResponse> localVarResp = getClustersMetricsCredentialsWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getClustersMetricsCredentials request with HTTP info returned
         * @return ApiResponse&lt;DatabasesGetClustersMetricsCredentialsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;credentials&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesGetClustersMetricsCredentialsResponse> executeWithHttpInfo() throws ApiException {
            return getClustersMetricsCredentialsWithHttpInfo();
        }

        /**
         * Execute getClustersMetricsCredentials request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;credentials&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesGetClustersMetricsCredentialsResponse> _callback) throws ApiException {
            return getClustersMetricsCredentialsAsync(_callback);
        }
    }

    /**
     * Retrieve Database Clusters&#39; Metrics Endpoint Credentials
     * To show the credentials for all database clusters&#39; metrics endpoints, send a GET request to &#x60;/v2/databases/metrics/credentials&#x60;. The result will be a JSON object with a &#x60;credentials&#x60; key.
     * @return GetClustersMetricsCredentialsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;credentials&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetClustersMetricsCredentialsRequestBuilder getClustersMetricsCredentials() throws IllegalArgumentException {
        return new GetClustersMetricsCredentialsRequestBuilder();
    }
    private okhttp3.Call getConnectionPoolCall(UUID databaseClusterUuid, String poolName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/pools/{pool_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "pool_name" + "}", localVarApiClient.escapeString(poolName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getConnectionPoolValidateBeforeCall(UUID databaseClusterUuid, String poolName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getConnectionPool(Async)");
        }

        // verify the required parameter 'poolName' is set
        if (poolName == null) {
            throw new ApiException("Missing the required parameter 'poolName' when calling getConnectionPool(Async)");
        }

        return getConnectionPoolCall(databaseClusterUuid, poolName, _callback);

    }


    private ApiResponse<DatabasesAddNewConnectionPoolResponse> getConnectionPoolWithHttpInfo(UUID databaseClusterUuid, String poolName) throws ApiException {
        okhttp3.Call localVarCall = getConnectionPoolValidateBeforeCall(databaseClusterUuid, poolName, null);
        Type localVarReturnType = new TypeToken<DatabasesAddNewConnectionPoolResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getConnectionPoolAsync(UUID databaseClusterUuid, String poolName, final ApiCallback<DatabasesAddNewConnectionPoolResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getConnectionPoolValidateBeforeCall(databaseClusterUuid, poolName, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddNewConnectionPoolResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetConnectionPoolRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String poolName;

        private GetConnectionPoolRequestBuilder(UUID databaseClusterUuid, String poolName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.poolName = poolName;
        }

        /**
         * Build call for getConnectionPool
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getConnectionPoolCall(databaseClusterUuid, poolName, _callback);
        }


        /**
         * Execute getConnectionPool request
         * @return DatabasesAddNewConnectionPoolResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddNewConnectionPoolResponse execute() throws ApiException {
            ApiResponse<DatabasesAddNewConnectionPoolResponse> localVarResp = getConnectionPoolWithHttpInfo(databaseClusterUuid, poolName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getConnectionPool request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddNewConnectionPoolResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddNewConnectionPoolResponse> executeWithHttpInfo() throws ApiException {
            return getConnectionPoolWithHttpInfo(databaseClusterUuid, poolName);
        }

        /**
         * Execute getConnectionPool request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddNewConnectionPoolResponse> _callback) throws ApiException {
            return getConnectionPoolAsync(databaseClusterUuid, poolName, _callback);
        }
    }

    /**
     * Retrieve Existing Connection Pool (PostgreSQL)
     * To show information about an existing connection pool for a PostgreSQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;. The response will be a JSON object with a &#x60;pool&#x60; key.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param poolName The name used to identify the connection pool. (required)
     * @return GetConnectionPoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;pool&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetConnectionPoolRequestBuilder getConnectionPool(UUID databaseClusterUuid, String poolName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (poolName == null) throw new IllegalArgumentException("\"poolName\" is required but got null");
            

        return new GetConnectionPoolRequestBuilder(databaseClusterUuid, poolName);
    }
    private okhttp3.Call getEvictionPolicyCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/eviction_policy"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getEvictionPolicyValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getEvictionPolicy(Async)");
        }

        return getEvictionPolicyCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesGetEvictionPolicyResponse> getEvictionPolicyWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = getEvictionPolicyValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesGetEvictionPolicyResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getEvictionPolicyAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesGetEvictionPolicyResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getEvictionPolicyValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesGetEvictionPolicyResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetEvictionPolicyRequestBuilder {
        private final UUID databaseClusterUuid;

        private GetEvictionPolicyRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for getEvictionPolicy
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;eviction_policy&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getEvictionPolicyCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute getEvictionPolicy request
         * @return DatabasesGetEvictionPolicyResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;eviction_policy&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesGetEvictionPolicyResponse execute() throws ApiException {
            ApiResponse<DatabasesGetEvictionPolicyResponse> localVarResp = getEvictionPolicyWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getEvictionPolicy request with HTTP info returned
         * @return ApiResponse&lt;DatabasesGetEvictionPolicyResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;eviction_policy&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesGetEvictionPolicyResponse> executeWithHttpInfo() throws ApiException {
            return getEvictionPolicyWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute getEvictionPolicy request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;eviction_policy&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesGetEvictionPolicyResponse> _callback) throws ApiException {
            return getEvictionPolicyAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * Retrieve the Eviction Policy for a Redis Cluster
     * To retrieve the configured eviction policy for an existing Redis cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/eviction_policy&#x60;. The response will be a JSON object with an &#x60;eviction_policy&#x60; key. This will be set to a string representing the eviction policy.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return GetEvictionPolicyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON string with a key of &#x60;eviction_policy&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetEvictionPolicyRequestBuilder getEvictionPolicy(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new GetEvictionPolicyRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call getExistingReadOnlyReplicaCall(UUID databaseClusterUuid, String replicaName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/replicas/{replica_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "replica_name" + "}", localVarApiClient.escapeString(replicaName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getExistingReadOnlyReplicaValidateBeforeCall(UUID databaseClusterUuid, String replicaName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getExistingReadOnlyReplica(Async)");
        }

        // verify the required parameter 'replicaName' is set
        if (replicaName == null) {
            throw new ApiException("Missing the required parameter 'replicaName' when calling getExistingReadOnlyReplica(Async)");
        }

        return getExistingReadOnlyReplicaCall(databaseClusterUuid, replicaName, _callback);

    }


    private ApiResponse<DatabasesCreateReadOnlyReplicaResponse> getExistingReadOnlyReplicaWithHttpInfo(UUID databaseClusterUuid, String replicaName) throws ApiException {
        okhttp3.Call localVarCall = getExistingReadOnlyReplicaValidateBeforeCall(databaseClusterUuid, replicaName, null);
        Type localVarReturnType = new TypeToken<DatabasesCreateReadOnlyReplicaResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getExistingReadOnlyReplicaAsync(UUID databaseClusterUuid, String replicaName, final ApiCallback<DatabasesCreateReadOnlyReplicaResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getExistingReadOnlyReplicaValidateBeforeCall(databaseClusterUuid, replicaName, _callback);
        Type localVarReturnType = new TypeToken<DatabasesCreateReadOnlyReplicaResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetExistingReadOnlyReplicaRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String replicaName;

        private GetExistingReadOnlyReplicaRequestBuilder(UUID databaseClusterUuid, String replicaName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.replicaName = replicaName;
        }

        /**
         * Build call for getExistingReadOnlyReplica
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getExistingReadOnlyReplicaCall(databaseClusterUuid, replicaName, _callback);
        }


        /**
         * Execute getExistingReadOnlyReplica request
         * @return DatabasesCreateReadOnlyReplicaResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesCreateReadOnlyReplicaResponse execute() throws ApiException {
            ApiResponse<DatabasesCreateReadOnlyReplicaResponse> localVarResp = getExistingReadOnlyReplicaWithHttpInfo(databaseClusterUuid, replicaName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getExistingReadOnlyReplica request with HTTP info returned
         * @return ApiResponse&lt;DatabasesCreateReadOnlyReplicaResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesCreateReadOnlyReplicaResponse> executeWithHttpInfo() throws ApiException {
            return getExistingReadOnlyReplicaWithHttpInfo(databaseClusterUuid, replicaName);
        }

        /**
         * Execute getExistingReadOnlyReplica request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesCreateReadOnlyReplicaResponse> _callback) throws ApiException {
            return getExistingReadOnlyReplicaAsync(databaseClusterUuid, replicaName, _callback);
        }
    }

    /**
     * Retrieve an Existing Read-only Replica
     * To show information about an existing database replica, send a GET request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  The response will be a JSON object with a &#x60;replica key&#x60;. This will be set to an object containing the standard database replica attributes.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param replicaName The name of the database replica. (required)
     * @return GetExistingReadOnlyReplicaRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;replica&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetExistingReadOnlyReplicaRequestBuilder getExistingReadOnlyReplica(UUID databaseClusterUuid, String replicaName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (replicaName == null) throw new IllegalArgumentException("\"replicaName\" is required but got null");
            

        return new GetExistingReadOnlyReplicaRequestBuilder(databaseClusterUuid, replicaName);
    }
    private okhttp3.Call getMigrationStatusCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/online-migration"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getMigrationStatusValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getMigrationStatus(Async)");
        }

        return getMigrationStatusCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<OnlineMigration> getMigrationStatusWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = getMigrationStatusValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<OnlineMigration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getMigrationStatusAsync(UUID databaseClusterUuid, final ApiCallback<OnlineMigration> _callback) throws ApiException {

        okhttp3.Call localVarCall = getMigrationStatusValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<OnlineMigration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetMigrationStatusRequestBuilder {
        private final UUID databaseClusterUuid;

        private GetMigrationStatusRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for getMigrationStatus
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getMigrationStatusCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute getMigrationStatus request
         * @return OnlineMigration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public OnlineMigration execute() throws ApiException {
            ApiResponse<OnlineMigration> localVarResp = getMigrationStatusWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getMigrationStatus request with HTTP info returned
         * @return ApiResponse&lt;OnlineMigration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<OnlineMigration> executeWithHttpInfo() throws ApiException {
            return getMigrationStatusWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute getMigrationStatus request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<OnlineMigration> _callback) throws ApiException {
            return getMigrationStatusAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * Retrieve the Status of an Online Migration
     * To retrieve the status of the most recent online migration, send a GET request to &#x60;/v2/databases/$DATABASE_ID/online-migration&#x60;. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return GetMigrationStatusRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetMigrationStatusRequestBuilder getMigrationStatus(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new GetMigrationStatusRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call getPublicCertificateCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/ca"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getPublicCertificateValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getPublicCertificate(Async)");
        }

        return getPublicCertificateCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesGetPublicCertificateResponse> getPublicCertificateWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = getPublicCertificateValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesGetPublicCertificateResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getPublicCertificateAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesGetPublicCertificateResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getPublicCertificateValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesGetPublicCertificateResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetPublicCertificateRequestBuilder {
        private final UUID databaseClusterUuid;

        private GetPublicCertificateRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for getPublicCertificate
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;ca&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getPublicCertificateCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute getPublicCertificate request
         * @return DatabasesGetPublicCertificateResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;ca&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesGetPublicCertificateResponse execute() throws ApiException {
            ApiResponse<DatabasesGetPublicCertificateResponse> localVarResp = getPublicCertificateWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getPublicCertificate request with HTTP info returned
         * @return ApiResponse&lt;DatabasesGetPublicCertificateResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;ca&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesGetPublicCertificateResponse> executeWithHttpInfo() throws ApiException {
            return getPublicCertificateWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute getPublicCertificate request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;ca&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesGetPublicCertificateResponse> _callback) throws ApiException {
            return getPublicCertificateAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * Retrieve the Public Certificate
     * To retrieve the public certificate used to secure the connection to the database cluster send a GET request to &#x60;/v2/databases/$DATABASE_ID/ca&#x60;.  The response will be a JSON object with a &#x60;ca&#x60; key. This will be set to an object containing the base64 encoding of the public key certificate. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return GetPublicCertificateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;ca&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetPublicCertificateRequestBuilder getPublicCertificate(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new GetPublicCertificateRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call getSqlModeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/sql_mode"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getSqlModeValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getSqlMode(Async)");
        }

        return getSqlModeCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<SqlMode> getSqlModeWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = getSqlModeValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<SqlMode>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getSqlModeAsync(UUID databaseClusterUuid, final ApiCallback<SqlMode> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSqlModeValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<SqlMode>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetSqlModeRequestBuilder {
        private final UUID databaseClusterUuid;

        private GetSqlModeRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for getSqlMode
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;sql_mode&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getSqlModeCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute getSqlMode request
         * @return SqlMode
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;sql_mode&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public SqlMode execute() throws ApiException {
            ApiResponse<SqlMode> localVarResp = getSqlModeWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getSqlMode request with HTTP info returned
         * @return ApiResponse&lt;SqlMode&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;sql_mode&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<SqlMode> executeWithHttpInfo() throws ApiException {
            return getSqlModeWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute getSqlMode request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;sql_mode&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<SqlMode> _callback) throws ApiException {
            return getSqlModeAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * Retrieve the SQL Modes for a MySQL Cluster
     * To retrieve the configured SQL modes for an existing MySQL cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/sql_mode&#x60;. The response will be a JSON object with a &#x60;sql_mode&#x60; key. This will be set to a string representing the configured SQL modes.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return GetSqlModeRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON string with a key of &#x60;sql_mode&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetSqlModeRequestBuilder getSqlMode(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new GetSqlModeRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call getTopicKafkaClusterCall(UUID databaseClusterUuid, String topicName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/topics/{topic_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "topic_name" + "}", localVarApiClient.escapeString(topicName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getTopicKafkaClusterValidateBeforeCall(UUID databaseClusterUuid, String topicName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getTopicKafkaCluster(Async)");
        }

        // verify the required parameter 'topicName' is set
        if (topicName == null) {
            throw new ApiException("Missing the required parameter 'topicName' when calling getTopicKafkaCluster(Async)");
        }

        return getTopicKafkaClusterCall(databaseClusterUuid, topicName, _callback);

    }


    private ApiResponse<DatabasesCreateTopicKafkaClusterResponse> getTopicKafkaClusterWithHttpInfo(UUID databaseClusterUuid, String topicName) throws ApiException {
        okhttp3.Call localVarCall = getTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, topicName, null);
        Type localVarReturnType = new TypeToken<DatabasesCreateTopicKafkaClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getTopicKafkaClusterAsync(UUID databaseClusterUuid, String topicName, final ApiCallback<DatabasesCreateTopicKafkaClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, topicName, _callback);
        Type localVarReturnType = new TypeToken<DatabasesCreateTopicKafkaClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetTopicKafkaClusterRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String topicName;

        private GetTopicKafkaClusterRequestBuilder(UUID databaseClusterUuid, String topicName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.topicName = topicName;
        }

        /**
         * Build call for getTopicKafkaCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getTopicKafkaClusterCall(databaseClusterUuid, topicName, _callback);
        }


        /**
         * Execute getTopicKafkaCluster request
         * @return DatabasesCreateTopicKafkaClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesCreateTopicKafkaClusterResponse execute() throws ApiException {
            ApiResponse<DatabasesCreateTopicKafkaClusterResponse> localVarResp = getTopicKafkaClusterWithHttpInfo(databaseClusterUuid, topicName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getTopicKafkaCluster request with HTTP info returned
         * @return ApiResponse&lt;DatabasesCreateTopicKafkaClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesCreateTopicKafkaClusterResponse> executeWithHttpInfo() throws ApiException {
            return getTopicKafkaClusterWithHttpInfo(databaseClusterUuid, topicName);
        }

        /**
         * Execute getTopicKafkaCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesCreateTopicKafkaClusterResponse> _callback) throws ApiException {
            return getTopicKafkaClusterAsync(databaseClusterUuid, topicName, _callback);
        }
    }

    /**
     * Get Topic for a Kafka Cluster
     * To retrieve a given topic by name from the set of a Kafka cluster&#39;s topics, send a GET request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param topicName The name used to identify the Kafka topic. (required)
     * @return GetTopicKafkaClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetTopicKafkaClusterRequestBuilder getTopicKafkaCluster(UUID databaseClusterUuid, String topicName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (topicName == null) throw new IllegalArgumentException("\"topicName\" is required but got null");
            

        return new GetTopicKafkaClusterRequestBuilder(databaseClusterUuid, topicName);
    }
    private okhttp3.Call getUserCall(UUID databaseClusterUuid, String username, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/users/{username}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "username" + "}", localVarApiClient.escapeString(username.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getUserValidateBeforeCall(UUID databaseClusterUuid, String username, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling getUser(Async)");
        }

        // verify the required parameter 'username' is set
        if (username == null) {
            throw new ApiException("Missing the required parameter 'username' when calling getUser(Async)");
        }

        return getUserCall(databaseClusterUuid, username, _callback);

    }


    private ApiResponse<DatabasesAddUserResponse> getUserWithHttpInfo(UUID databaseClusterUuid, String username) throws ApiException {
        okhttp3.Call localVarCall = getUserValidateBeforeCall(databaseClusterUuid, username, null);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getUserAsync(UUID databaseClusterUuid, String username, final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getUserValidateBeforeCall(databaseClusterUuid, username, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetUserRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String username;

        private GetUserRequestBuilder(UUID databaseClusterUuid, String username) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.username = username;
        }

        /**
         * Build call for getUser
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getUserCall(databaseClusterUuid, username, _callback);
        }


        /**
         * Execute getUser request
         * @return DatabasesAddUserResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddUserResponse execute() throws ApiException {
            ApiResponse<DatabasesAddUserResponse> localVarResp = getUserWithHttpInfo(databaseClusterUuid, username);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getUser request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddUserResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddUserResponse> executeWithHttpInfo() throws ApiException {
            return getUserWithHttpInfo(databaseClusterUuid, username);
        }

        /**
         * Execute getUser request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {
            return getUserAsync(databaseClusterUuid, username, _callback);
        }
    }

    /**
     * Retrieve an Existing Database User
     * To show information about an existing database user, send a GET request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60;.  Note: User management is not supported for Redis clusters.  The response will be a JSON object with a &#x60;user&#x60; key. This will be set to an object containing the standard database user attributes.  For MySQL clusters, additional options will be contained in the &#x60;mysql_settings&#x60; object.  For Kafka clusters, additional options will be contained in the &#x60;settings&#x60; object. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param username The name of the database user. (required)
     * @return GetUserRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetUserRequestBuilder getUser(UUID databaseClusterUuid, String username) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (username == null) throw new IllegalArgumentException("\"username\" is required but got null");
            

        return new GetUserRequestBuilder(databaseClusterUuid, username);
    }
    private okhttp3.Call listCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/dbs"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling list(Async)");
        }

        return listCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesListResponse> listWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesListResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesListResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for list
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute list request
         * @return DatabasesListResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListResponse execute() throws ApiException {
            ApiResponse<DatabasesListResponse> localVarResp = listWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListResponse> _callback) throws ApiException {
            return listAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List All Databases
     * To list all of the databases in a clusters, send a GET request to &#x60;/v2/databases/$DATABASE_ID/dbs&#x60;.  The result will be a JSON object with a &#x60;dbs&#x60; key. This will be set to an array of database objects, each of which will contain the standard database attributes.  Note: Database management is not supported for Redis clusters. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRequestBuilder list(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call listBackupsCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/backups"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listBackupsValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling listBackups(Async)");
        }

        return listBackupsCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesListBackupsResponse> listBackupsWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listBackupsValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesListBackupsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listBackupsAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesListBackupsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listBackupsValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListBackupsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListBackupsRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListBackupsRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for listBackups
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database_backups&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listBackupsCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute listBackups request
         * @return DatabasesListBackupsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database_backups&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListBackupsResponse execute() throws ApiException {
            ApiResponse<DatabasesListBackupsResponse> localVarResp = listBackupsWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listBackups request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListBackupsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database_backups&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListBackupsResponse> executeWithHttpInfo() throws ApiException {
            return listBackupsWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute listBackups request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;database_backups&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListBackupsResponse> _callback) throws ApiException {
            return listBackupsAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List Backups for a Database Cluster
     * To list all of the available backups of a PostgreSQL or MySQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/backups&#x60;. **Note**: Backups are not supported for Redis clusters. The result will be a JSON object with a &#x60;backups key&#x60;. This will be set to an array of backup objects, each of which will contain the size of the backup and the timestamp at which it was created.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListBackupsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;database_backups&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListBackupsRequestBuilder listBackups(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListBackupsRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call listClustersCall(String tagName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (tagName != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("tag_name", tagName));
        }

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listClustersValidateBeforeCall(String tagName, final ApiCallback _callback) throws ApiException {
        return listClustersCall(tagName, _callback);

    }


    private ApiResponse<DatabasesListClustersResponse> listClustersWithHttpInfo(String tagName) throws ApiException {
        okhttp3.Call localVarCall = listClustersValidateBeforeCall(tagName, null);
        Type localVarReturnType = new TypeToken<DatabasesListClustersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listClustersAsync(String tagName, final ApiCallback<DatabasesListClustersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listClustersValidateBeforeCall(tagName, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListClustersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListClustersRequestBuilder {
        private String tagName;

        private ListClustersRequestBuilder() {
        }

        /**
         * Set tagName
         * @param tagName Limits the results to database clusters with a specific tag. (optional)
         * @return ListClustersRequestBuilder
         */
        public ListClustersRequestBuilder tagName(String tagName) {
            this.tagName = tagName;
            return this;
        }
        
        /**
         * Build call for listClusters
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listClustersCall(tagName, _callback);
        }


        /**
         * Execute listClusters request
         * @return DatabasesListClustersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListClustersResponse execute() throws ApiException {
            ApiResponse<DatabasesListClustersResponse> localVarResp = listClustersWithHttpInfo(tagName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listClusters request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListClustersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListClustersResponse> executeWithHttpInfo() throws ApiException {
            return listClustersWithHttpInfo(tagName);
        }

        /**
         * Execute listClusters request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListClustersResponse> _callback) throws ApiException {
            return listClustersAsync(tagName, _callback);
        }
    }

    /**
     * List All Database Clusters
     * To list all of the database clusters available on your account, send a GET request to &#x60;/v2/databases&#x60;. To limit the results to database clusters with a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag. For example, &#x60;/v2/databases?tag_name&#x3D;$TAG_NAME&#x60;.  The result will be a JSON object with a &#x60;databases&#x60; key. This will be set to an array of database objects, each of which will contain the standard database attributes.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects will contain the information needed to connect to the cluster&#39;s standby node(s).  The embedded &#x60;maintenance_window&#x60; object will contain information about any scheduled maintenance for the database cluster.
     * @return ListClustersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;databases&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListClustersRequestBuilder listClusters() throws IllegalArgumentException {
        return new ListClustersRequestBuilder();
    }
    private okhttp3.Call listConnectionPoolsCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/pools"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listConnectionPoolsValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling listConnectionPools(Async)");
        }

        return listConnectionPoolsCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<ConnectionPools> listConnectionPoolsWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listConnectionPoolsValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<ConnectionPools>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listConnectionPoolsAsync(UUID databaseClusterUuid, final ApiCallback<ConnectionPools> _callback) throws ApiException {

        okhttp3.Call localVarCall = listConnectionPoolsValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<ConnectionPools>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListConnectionPoolsRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListConnectionPoolsRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for listConnectionPools
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pools&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listConnectionPoolsCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute listConnectionPools request
         * @return ConnectionPools
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pools&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ConnectionPools execute() throws ApiException {
            ApiResponse<ConnectionPools> localVarResp = listConnectionPoolsWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listConnectionPools request with HTTP info returned
         * @return ApiResponse&lt;ConnectionPools&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pools&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ConnectionPools> executeWithHttpInfo() throws ApiException {
            return listConnectionPoolsWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute listConnectionPools request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;pools&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ConnectionPools> _callback) throws ApiException {
            return listConnectionPoolsAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List Connection Pools (PostgreSQL)
     * To list all of the connection pools available to a PostgreSQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/pools&#x60;. The result will be a JSON object with a &#x60;pools&#x60; key. This will be set to an array of connection pool objects.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListConnectionPoolsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;pools&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListConnectionPoolsRequestBuilder listConnectionPools(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListConnectionPoolsRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call listEventsLogsCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/events"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listEventsLogsValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling listEventsLogs(Async)");
        }

        return listEventsLogsCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesListEventsLogsResponse> listEventsLogsWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listEventsLogsValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesListEventsLogsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listEventsLogsAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesListEventsLogsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listEventsLogsValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListEventsLogsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListEventsLogsRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListEventsLogsRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for listEventsLogs
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;events&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listEventsLogsCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute listEventsLogs request
         * @return DatabasesListEventsLogsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;events&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListEventsLogsResponse execute() throws ApiException {
            ApiResponse<DatabasesListEventsLogsResponse> localVarResp = listEventsLogsWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listEventsLogs request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListEventsLogsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;events&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListEventsLogsResponse> executeWithHttpInfo() throws ApiException {
            return listEventsLogsWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute listEventsLogs request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;events&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListEventsLogsResponse> _callback) throws ApiException {
            return listEventsLogsAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List all Events Logs
     * To list all of the cluster events, send a GET request to &#x60;/v2/databases/$DATABASE_ID/events&#x60;.  The result will be a JSON object with a &#x60;events&#x60; key. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListEventsLogsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;events&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListEventsLogsRequestBuilder listEventsLogs(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListEventsLogsRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call listFirewallRulesCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/firewall"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listFirewallRulesValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling listFirewallRules(Async)");
        }

        return listFirewallRulesCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesListFirewallRulesResponse> listFirewallRulesWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listFirewallRulesValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesListFirewallRulesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listFirewallRulesAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesListFirewallRulesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listFirewallRulesValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListFirewallRulesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListFirewallRulesRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListFirewallRulesRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for listFirewallRules
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;rules&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listFirewallRulesCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute listFirewallRules request
         * @return DatabasesListFirewallRulesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;rules&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListFirewallRulesResponse execute() throws ApiException {
            ApiResponse<DatabasesListFirewallRulesResponse> localVarResp = listFirewallRulesWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listFirewallRules request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListFirewallRulesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;rules&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListFirewallRulesResponse> executeWithHttpInfo() throws ApiException {
            return listFirewallRulesWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute listFirewallRules request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;rules&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListFirewallRulesResponse> _callback) throws ApiException {
            return listFirewallRulesAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List Firewall Rules (Trusted Sources) for a Database Cluster
     * To list all of a database cluster&#39;s firewall rules (known as \&quot;trusted sources\&quot; in the control panel), send a GET request to &#x60;/v2/databases/$DATABASE_ID/firewall&#x60;. The result will be a JSON object with a &#x60;rules&#x60; key.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListFirewallRulesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;rules&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListFirewallRulesRequestBuilder listFirewallRules(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListFirewallRulesRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call listOptionsCall(final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/options";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listOptionsValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return listOptionsCall(_callback);

    }


    private ApiResponse<Options> listOptionsWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listOptionsValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<Options>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listOptionsAsync(final ApiCallback<Options> _callback) throws ApiException {

        okhttp3.Call localVarCall = listOptionsValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<Options>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListOptionsRequestBuilder {

        private ListOptionsRequestBuilder() {
        }

        /**
         * Build call for listOptions
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;options&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listOptionsCall(_callback);
        }


        /**
         * Execute listOptions request
         * @return Options
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;options&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Options execute() throws ApiException {
            ApiResponse<Options> localVarResp = listOptionsWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listOptions request with HTTP info returned
         * @return ApiResponse&lt;Options&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;options&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Options> executeWithHttpInfo() throws ApiException {
            return listOptionsWithHttpInfo();
        }

        /**
         * Execute listOptions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON string with a key of &#x60;options&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Options> _callback) throws ApiException {
            return listOptionsAsync(_callback);
        }
    }

    /**
     * List Database Options
     * To list all of the options available for the offered database engines, send a GET request to &#x60;/v2/databases/options&#x60;. The result will be a JSON object with an &#x60;options&#x60; key.
     * @return ListOptionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON string with a key of &#x60;options&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListOptionsRequestBuilder listOptions() throws IllegalArgumentException {
        return new ListOptionsRequestBuilder();
    }
    private okhttp3.Call listReadOnlyReplicasCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/replicas"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listReadOnlyReplicasValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling listReadOnlyReplicas(Async)");
        }

        return listReadOnlyReplicasCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesListReadOnlyReplicasResponse> listReadOnlyReplicasWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listReadOnlyReplicasValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesListReadOnlyReplicasResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listReadOnlyReplicasAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesListReadOnlyReplicasResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listReadOnlyReplicasValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListReadOnlyReplicasResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListReadOnlyReplicasRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListReadOnlyReplicasRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for listReadOnlyReplicas
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replicas&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listReadOnlyReplicasCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute listReadOnlyReplicas request
         * @return DatabasesListReadOnlyReplicasResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replicas&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListReadOnlyReplicasResponse execute() throws ApiException {
            ApiResponse<DatabasesListReadOnlyReplicasResponse> localVarResp = listReadOnlyReplicasWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listReadOnlyReplicas request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListReadOnlyReplicasResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replicas&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListReadOnlyReplicasResponse> executeWithHttpInfo() throws ApiException {
            return listReadOnlyReplicasWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute listReadOnlyReplicas request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;replicas&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListReadOnlyReplicasResponse> _callback) throws ApiException {
            return listReadOnlyReplicasAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List All Read-only Replicas
     * To list all of the read-only replicas associated with a database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/replicas&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  The result will be a JSON object with a &#x60;replicas&#x60; key. This will be set to an array of database replica objects, each of which will contain the standard database replica attributes.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListReadOnlyReplicasRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;replicas&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListReadOnlyReplicasRequestBuilder listReadOnlyReplicas(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListReadOnlyReplicasRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call listTopicsKafkaClusterCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/topics"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listTopicsKafkaClusterValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling listTopicsKafkaCluster(Async)");
        }

        return listTopicsKafkaClusterCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesListTopicsKafkaClusterResponse> listTopicsKafkaClusterWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listTopicsKafkaClusterValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesListTopicsKafkaClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listTopicsKafkaClusterAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesListTopicsKafkaClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listTopicsKafkaClusterValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListTopicsKafkaClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListTopicsKafkaClusterRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListTopicsKafkaClusterRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for listTopicsKafkaCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topics&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listTopicsKafkaClusterCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute listTopicsKafkaCluster request
         * @return DatabasesListTopicsKafkaClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topics&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListTopicsKafkaClusterResponse execute() throws ApiException {
            ApiResponse<DatabasesListTopicsKafkaClusterResponse> localVarResp = listTopicsKafkaClusterWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listTopicsKafkaCluster request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListTopicsKafkaClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topics&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListTopicsKafkaClusterResponse> executeWithHttpInfo() throws ApiException {
            return listTopicsKafkaClusterWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute listTopicsKafkaCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topics&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListTopicsKafkaClusterResponse> _callback) throws ApiException {
            return listTopicsKafkaClusterAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List Topics for a Kafka Cluster
     * To list all of a Kafka cluster&#39;s topics, send a GET request to &#x60;/v2/databases/$DATABASE_ID/topics&#x60;.  The result will be a JSON object with a &#x60;topics&#x60; key. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListTopicsKafkaClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;topics&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListTopicsKafkaClusterRequestBuilder listTopicsKafkaCluster(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListTopicsKafkaClusterRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call listUsersCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/users"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call listUsersValidateBeforeCall(UUID databaseClusterUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling listUsers(Async)");
        }

        return listUsersCall(databaseClusterUuid, _callback);

    }


    private ApiResponse<DatabasesListUsersResponse> listUsersWithHttpInfo(UUID databaseClusterUuid) throws ApiException {
        okhttp3.Call localVarCall = listUsersValidateBeforeCall(databaseClusterUuid, null);
        Type localVarReturnType = new TypeToken<DatabasesListUsersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listUsersAsync(UUID databaseClusterUuid, final ApiCallback<DatabasesListUsersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listUsersValidateBeforeCall(databaseClusterUuid, _callback);
        Type localVarReturnType = new TypeToken<DatabasesListUsersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListUsersRequestBuilder {
        private final UUID databaseClusterUuid;

        private ListUsersRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for listUsers
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;users&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listUsersCall(databaseClusterUuid, _callback);
        }


        /**
         * Execute listUsers request
         * @return DatabasesListUsersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;users&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesListUsersResponse execute() throws ApiException {
            ApiResponse<DatabasesListUsersResponse> localVarResp = listUsersWithHttpInfo(databaseClusterUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listUsers request with HTTP info returned
         * @return ApiResponse&lt;DatabasesListUsersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;users&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesListUsersResponse> executeWithHttpInfo() throws ApiException {
            return listUsersWithHttpInfo(databaseClusterUuid);
        }

        /**
         * Execute listUsers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;users&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesListUsersResponse> _callback) throws ApiException {
            return listUsersAsync(databaseClusterUuid, _callback);
        }
    }

    /**
     * List all Database Users
     * To list all of the users for your database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/users&#x60;.  Note: User management is not supported for Redis clusters.  The result will be a JSON object with a &#x60;users&#x60; key. This will be set to an array of database user objects, each of which will contain the standard database user attributes.  For MySQL clusters, additional options will be contained in the mysql_settings object. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @return ListUsersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;users&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListUsersRequestBuilder listUsers(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ListUsersRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call migrateClusterToNewRegionCall(UUID databaseClusterUuid, DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databasesMigrateClusterToNewRegionRequest;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/migrate"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call migrateClusterToNewRegionValidateBeforeCall(UUID databaseClusterUuid, DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling migrateClusterToNewRegion(Async)");
        }

        // verify the required parameter 'databasesMigrateClusterToNewRegionRequest' is set
        if (databasesMigrateClusterToNewRegionRequest == null) {
            throw new ApiException("Missing the required parameter 'databasesMigrateClusterToNewRegionRequest' when calling migrateClusterToNewRegion(Async)");
        }

        return migrateClusterToNewRegionCall(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest, _callback);

    }


    private ApiResponse<Void> migrateClusterToNewRegionWithHttpInfo(UUID databaseClusterUuid, DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest) throws ApiException {
        okhttp3.Call localVarCall = migrateClusterToNewRegionValidateBeforeCall(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call migrateClusterToNewRegionAsync(UUID databaseClusterUuid, DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = migrateClusterToNewRegionValidateBeforeCall(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class MigrateClusterToNewRegionRequestBuilder {
        private final String region;
        private final UUID databaseClusterUuid;

        private MigrateClusterToNewRegionRequestBuilder(String region, UUID databaseClusterUuid) {
            this.region = region;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for migrateClusterToNewRegion
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest = buildBodyParams();
            return migrateClusterToNewRegionCall(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest, _callback);
        }

        private DatabasesMigrateClusterToNewRegionRequest buildBodyParams() {
            DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest = new DatabasesMigrateClusterToNewRegionRequest();
            databasesMigrateClusterToNewRegionRequest.region(this.region);
            return databasesMigrateClusterToNewRegionRequest;
        }

        /**
         * Execute migrateClusterToNewRegion request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest = buildBodyParams();
            migrateClusterToNewRegionWithHttpInfo(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest);
        }

        /**
         * Execute migrateClusterToNewRegion request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest = buildBodyParams();
            return migrateClusterToNewRegionWithHttpInfo(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest);
        }

        /**
         * Execute migrateClusterToNewRegion request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            DatabasesMigrateClusterToNewRegionRequest databasesMigrateClusterToNewRegionRequest = buildBodyParams();
            return migrateClusterToNewRegionAsync(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest, _callback);
        }
    }

    /**
     * Migrate a Database Cluster to a New Region
     * To migrate a database cluster to a new region, send a &#x60;PUT&#x60; request to &#x60;/v2/databases/$DATABASE_ID/migrate&#x60;. The body of the request must specify a &#x60;region&#x60; attribute.  A successful request will receive a 202 Accepted status code with no body in response. Querying the database cluster will show that its &#x60;status&#x60; attribute will now be set to &#x60;migrating&#x60;. This will transition back to &#x60;online&#x60; when the migration has completed. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databasesMigrateClusterToNewRegionRequest  (required)
     * @return MigrateClusterToNewRegionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public MigrateClusterToNewRegionRequestBuilder migrateClusterToNewRegion(String region, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (region == null) throw new IllegalArgumentException("\"region\" is required but got null");
            

        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new MigrateClusterToNewRegionRequestBuilder(region, databaseClusterUuid);
    }
    private okhttp3.Call promoteReadonlyReplicaToPrimaryCall(UUID databaseClusterUuid, String replicaName, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/replicas/{replica_name}/promote"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "replica_name" + "}", localVarApiClient.escapeString(replicaName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call promoteReadonlyReplicaToPrimaryValidateBeforeCall(UUID databaseClusterUuid, String replicaName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling promoteReadonlyReplicaToPrimary(Async)");
        }

        // verify the required parameter 'replicaName' is set
        if (replicaName == null) {
            throw new ApiException("Missing the required parameter 'replicaName' when calling promoteReadonlyReplicaToPrimary(Async)");
        }

        return promoteReadonlyReplicaToPrimaryCall(databaseClusterUuid, replicaName, _callback);

    }


    private ApiResponse<Void> promoteReadonlyReplicaToPrimaryWithHttpInfo(UUID databaseClusterUuid, String replicaName) throws ApiException {
        okhttp3.Call localVarCall = promoteReadonlyReplicaToPrimaryValidateBeforeCall(databaseClusterUuid, replicaName, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call promoteReadonlyReplicaToPrimaryAsync(UUID databaseClusterUuid, String replicaName, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = promoteReadonlyReplicaToPrimaryValidateBeforeCall(databaseClusterUuid, replicaName, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class PromoteReadonlyReplicaToPrimaryRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String replicaName;

        private PromoteReadonlyReplicaToPrimaryRequestBuilder(UUID databaseClusterUuid, String replicaName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.replicaName = replicaName;
        }

        /**
         * Build call for promoteReadonlyReplicaToPrimary
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return promoteReadonlyReplicaToPrimaryCall(databaseClusterUuid, replicaName, _callback);
        }


        /**
         * Execute promoteReadonlyReplicaToPrimary request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            promoteReadonlyReplicaToPrimaryWithHttpInfo(databaseClusterUuid, replicaName);
        }

        /**
         * Execute promoteReadonlyReplicaToPrimary request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return promoteReadonlyReplicaToPrimaryWithHttpInfo(databaseClusterUuid, replicaName);
        }

        /**
         * Execute promoteReadonlyReplicaToPrimary request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return promoteReadonlyReplicaToPrimaryAsync(databaseClusterUuid, replicaName, _callback);
        }
    }

    /**
     * Promote a Read-only Replica to become a Primary Cluster
     * To promote a specific read-only replica, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME/promote&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param replicaName The name of the database replica. (required)
     * @return PromoteReadonlyReplicaToPrimaryRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public PromoteReadonlyReplicaToPrimaryRequestBuilder promoteReadonlyReplicaToPrimary(UUID databaseClusterUuid, String replicaName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (replicaName == null) throw new IllegalArgumentException("\"replicaName\" is required but got null");
            

        return new PromoteReadonlyReplicaToPrimaryRequestBuilder(databaseClusterUuid, replicaName);
    }
    private okhttp3.Call removeUserCall(UUID databaseClusterUuid, String username, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/users/{username}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "username" + "}", localVarApiClient.escapeString(username.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call removeUserValidateBeforeCall(UUID databaseClusterUuid, String username, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling removeUser(Async)");
        }

        // verify the required parameter 'username' is set
        if (username == null) {
            throw new ApiException("Missing the required parameter 'username' when calling removeUser(Async)");
        }

        return removeUserCall(databaseClusterUuid, username, _callback);

    }


    private ApiResponse<Void> removeUserWithHttpInfo(UUID databaseClusterUuid, String username) throws ApiException {
        okhttp3.Call localVarCall = removeUserValidateBeforeCall(databaseClusterUuid, username, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call removeUserAsync(UUID databaseClusterUuid, String username, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = removeUserValidateBeforeCall(databaseClusterUuid, username, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RemoveUserRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String username;

        private RemoveUserRequestBuilder(UUID databaseClusterUuid, String username) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.username = username;
        }

        /**
         * Build call for removeUser
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return removeUserCall(databaseClusterUuid, username, _callback);
        }


        /**
         * Execute removeUser request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            removeUserWithHttpInfo(databaseClusterUuid, username);
        }

        /**
         * Execute removeUser request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return removeUserWithHttpInfo(databaseClusterUuid, username);
        }

        /**
         * Execute removeUser request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return removeUserAsync(databaseClusterUuid, username, _callback);
        }
    }

    /**
     * Remove a Database User
     * To remove a specific database user, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.  Note: User management is not supported for Redis clusters. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param username The name of the database user. (required)
     * @return RemoveUserRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RemoveUserRequestBuilder removeUser(UUID databaseClusterUuid, String username) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (username == null) throw new IllegalArgumentException("\"username\" is required but got null");
            

        return new RemoveUserRequestBuilder(databaseClusterUuid, username);
    }
    private okhttp3.Call resetUserAuthCall(UUID databaseClusterUuid, String username, DatabasesResetUserAuthRequest databasesResetUserAuthRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databasesResetUserAuthRequest;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/users/{username}/reset_auth"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "username" + "}", localVarApiClient.escapeString(username.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call resetUserAuthValidateBeforeCall(UUID databaseClusterUuid, String username, DatabasesResetUserAuthRequest databasesResetUserAuthRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling resetUserAuth(Async)");
        }

        // verify the required parameter 'username' is set
        if (username == null) {
            throw new ApiException("Missing the required parameter 'username' when calling resetUserAuth(Async)");
        }

        // verify the required parameter 'databasesResetUserAuthRequest' is set
        if (databasesResetUserAuthRequest == null) {
            throw new ApiException("Missing the required parameter 'databasesResetUserAuthRequest' when calling resetUserAuth(Async)");
        }

        return resetUserAuthCall(databaseClusterUuid, username, databasesResetUserAuthRequest, _callback);

    }


    private ApiResponse<DatabasesAddUserResponse> resetUserAuthWithHttpInfo(UUID databaseClusterUuid, String username, DatabasesResetUserAuthRequest databasesResetUserAuthRequest) throws ApiException {
        okhttp3.Call localVarCall = resetUserAuthValidateBeforeCall(databaseClusterUuid, username, databasesResetUserAuthRequest, null);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call resetUserAuthAsync(UUID databaseClusterUuid, String username, DatabasesResetUserAuthRequest databasesResetUserAuthRequest, final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = resetUserAuthValidateBeforeCall(databaseClusterUuid, username, databasesResetUserAuthRequest, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ResetUserAuthRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String username;
        private MysqlSettings mysqlSettings;

        private ResetUserAuthRequestBuilder(UUID databaseClusterUuid, String username) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.username = username;
        }

        /**
         * Set mysqlSettings
         * @param mysqlSettings  (optional)
         * @return ResetUserAuthRequestBuilder
         */
        public ResetUserAuthRequestBuilder mysqlSettings(MysqlSettings mysqlSettings) {
            this.mysqlSettings = mysqlSettings;
            return this;
        }
        
        /**
         * Build call for resetUserAuth
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabasesResetUserAuthRequest databasesResetUserAuthRequest = buildBodyParams();
            return resetUserAuthCall(databaseClusterUuid, username, databasesResetUserAuthRequest, _callback);
        }

        private DatabasesResetUserAuthRequest buildBodyParams() {
            DatabasesResetUserAuthRequest databasesResetUserAuthRequest = new DatabasesResetUserAuthRequest();
            databasesResetUserAuthRequest.mysqlSettings(this.mysqlSettings);
            return databasesResetUserAuthRequest;
        }

        /**
         * Execute resetUserAuth request
         * @return DatabasesAddUserResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddUserResponse execute() throws ApiException {
            DatabasesResetUserAuthRequest databasesResetUserAuthRequest = buildBodyParams();
            ApiResponse<DatabasesAddUserResponse> localVarResp = resetUserAuthWithHttpInfo(databaseClusterUuid, username, databasesResetUserAuthRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute resetUserAuth request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddUserResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddUserResponse> executeWithHttpInfo() throws ApiException {
            DatabasesResetUserAuthRequest databasesResetUserAuthRequest = buildBodyParams();
            return resetUserAuthWithHttpInfo(databaseClusterUuid, username, databasesResetUserAuthRequest);
        }

        /**
         * Execute resetUserAuth request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {
            DatabasesResetUserAuthRequest databasesResetUserAuthRequest = buildBodyParams();
            return resetUserAuthAsync(databaseClusterUuid, username, databasesResetUserAuthRequest, _callback);
        }
    }

    /**
     * Reset a Database User&#39;s Password or Authentication Method
     * To reset the password for a database user, send a POST request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME/reset_auth&#x60;.  For &#x60;mysql&#x60; databases, the authentication method can be specifying by including a key in the JSON body called &#x60;mysql_settings&#x60; with the &#x60;auth_plugin&#x60; value specified.  The response will be a JSON object with a &#x60;user&#x60; key. This will be set to an object containing the standard database user attributes. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param username The name of the database user. (required)
     * @param databasesResetUserAuthRequest  (required)
     * @return ResetUserAuthRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ResetUserAuthRequestBuilder resetUserAuth(UUID databaseClusterUuid, String username) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (username == null) throw new IllegalArgumentException("\"username\" is required but got null");
            

        return new ResetUserAuthRequestBuilder(databaseClusterUuid, username);
    }
    private okhttp3.Call resizeClusterCall(UUID databaseClusterUuid, DatabaseClusterResize databaseClusterResize, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databaseClusterResize;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/resize"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call resizeClusterValidateBeforeCall(UUID databaseClusterUuid, DatabaseClusterResize databaseClusterResize, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling resizeCluster(Async)");
        }

        // verify the required parameter 'databaseClusterResize' is set
        if (databaseClusterResize == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterResize' when calling resizeCluster(Async)");
        }

        return resizeClusterCall(databaseClusterUuid, databaseClusterResize, _callback);

    }


    private ApiResponse<Void> resizeClusterWithHttpInfo(UUID databaseClusterUuid, DatabaseClusterResize databaseClusterResize) throws ApiException {
        okhttp3.Call localVarCall = resizeClusterValidateBeforeCall(databaseClusterUuid, databaseClusterResize, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call resizeClusterAsync(UUID databaseClusterUuid, DatabaseClusterResize databaseClusterResize, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = resizeClusterValidateBeforeCall(databaseClusterUuid, databaseClusterResize, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class ResizeClusterRequestBuilder {
        private final String size;
        private final Integer numNodes;
        private final UUID databaseClusterUuid;
        private Integer storageSizeMib;

        private ResizeClusterRequestBuilder(String size, Integer numNodes, UUID databaseClusterUuid) {
            this.size = size;
            this.numNodes = numNodes;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set storageSizeMib
         * @param storageSizeMib Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the &#39;size&#39; and any previously added additional storage. (optional)
         * @return ResizeClusterRequestBuilder
         */
        public ResizeClusterRequestBuilder storageSizeMib(Integer storageSizeMib) {
            this.storageSizeMib = storageSizeMib;
            return this;
        }
        
        /**
         * Build call for resizeCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabaseClusterResize databaseClusterResize = buildBodyParams();
            return resizeClusterCall(databaseClusterUuid, databaseClusterResize, _callback);
        }

        private DatabaseClusterResize buildBodyParams() {
            DatabaseClusterResize databaseClusterResize = new DatabaseClusterResize();
            databaseClusterResize.size(this.size);
            databaseClusterResize.numNodes(this.numNodes);
            databaseClusterResize.storageSizeMib(this.storageSizeMib);
            return databaseClusterResize;
        }

        /**
         * Execute resizeCluster request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DatabaseClusterResize databaseClusterResize = buildBodyParams();
            resizeClusterWithHttpInfo(databaseClusterUuid, databaseClusterResize);
        }

        /**
         * Execute resizeCluster request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            DatabaseClusterResize databaseClusterResize = buildBodyParams();
            return resizeClusterWithHttpInfo(databaseClusterUuid, databaseClusterResize);
        }

        /**
         * Execute resizeCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            DatabaseClusterResize databaseClusterResize = buildBodyParams();
            return resizeClusterAsync(databaseClusterUuid, databaseClusterResize, _callback);
        }
    }

    /**
     * Resize a Database Cluster
     * To resize a database cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/resize&#x60;. The body of the request must specify both the size and num_nodes attributes. A successful request will receive a 202 Accepted status code with no body in response. Querying the database cluster will show that its status attribute will now be set to resizing. This will transition back to online when the resize operation has completed.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databaseClusterResize  (required)
     * @return ResizeClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ResizeClusterRequestBuilder resizeCluster(String size, Integer numNodes, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (size == null) throw new IllegalArgumentException("\"size\" is required but got null");
            

        if (numNodes == null) throw new IllegalArgumentException("\"numNodes\" is required but got null");
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new ResizeClusterRequestBuilder(size, numNodes, databaseClusterUuid);
    }
    private okhttp3.Call startOnlineMigrationCall(UUID databaseClusterUuid, SourceDatabase sourceDatabase, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = sourceDatabase;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/online-migration"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call startOnlineMigrationValidateBeforeCall(UUID databaseClusterUuid, SourceDatabase sourceDatabase, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling startOnlineMigration(Async)");
        }

        // verify the required parameter 'sourceDatabase' is set
        if (sourceDatabase == null) {
            throw new ApiException("Missing the required parameter 'sourceDatabase' when calling startOnlineMigration(Async)");
        }

        return startOnlineMigrationCall(databaseClusterUuid, sourceDatabase, _callback);

    }


    private ApiResponse<OnlineMigration> startOnlineMigrationWithHttpInfo(UUID databaseClusterUuid, SourceDatabase sourceDatabase) throws ApiException {
        okhttp3.Call localVarCall = startOnlineMigrationValidateBeforeCall(databaseClusterUuid, sourceDatabase, null);
        Type localVarReturnType = new TypeToken<OnlineMigration>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call startOnlineMigrationAsync(UUID databaseClusterUuid, SourceDatabase sourceDatabase, final ApiCallback<OnlineMigration> _callback) throws ApiException {

        okhttp3.Call localVarCall = startOnlineMigrationValidateBeforeCall(databaseClusterUuid, sourceDatabase, _callback);
        Type localVarReturnType = new TypeToken<OnlineMigration>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class StartOnlineMigrationRequestBuilder {
        private final UUID databaseClusterUuid;
        private SourceDatabaseSource source;
        private Boolean disableSsl;
        private List<String> ignoreDbs;

        private StartOnlineMigrationRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set source
         * @param source  (optional)
         * @return StartOnlineMigrationRequestBuilder
         */
        public StartOnlineMigrationRequestBuilder source(SourceDatabaseSource source) {
            this.source = source;
            return this;
        }
        
        /**
         * Set disableSsl
         * @param disableSsl Enables SSL encryption when connecting to the source database. (optional)
         * @return StartOnlineMigrationRequestBuilder
         */
        public StartOnlineMigrationRequestBuilder disableSsl(Boolean disableSsl) {
            this.disableSsl = disableSsl;
            return this;
        }
        
        /**
         * Set ignoreDbs
         * @param ignoreDbs List of databases that should be ignored during migration. (optional)
         * @return StartOnlineMigrationRequestBuilder
         */
        public StartOnlineMigrationRequestBuilder ignoreDbs(List<String> ignoreDbs) {
            this.ignoreDbs = ignoreDbs;
            return this;
        }
        
        /**
         * Build call for startOnlineMigration
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            SourceDatabase sourceDatabase = buildBodyParams();
            return startOnlineMigrationCall(databaseClusterUuid, sourceDatabase, _callback);
        }

        private SourceDatabase buildBodyParams() {
            SourceDatabase sourceDatabase = new SourceDatabase();
            sourceDatabase.source(this.source);
            sourceDatabase.disableSsl(this.disableSsl);
            sourceDatabase.ignoreDbs(this.ignoreDbs);
            return sourceDatabase;
        }

        /**
         * Execute startOnlineMigration request
         * @return OnlineMigration
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public OnlineMigration execute() throws ApiException {
            SourceDatabase sourceDatabase = buildBodyParams();
            ApiResponse<OnlineMigration> localVarResp = startOnlineMigrationWithHttpInfo(databaseClusterUuid, sourceDatabase);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute startOnlineMigration request with HTTP info returned
         * @return ApiResponse&lt;OnlineMigration&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<OnlineMigration> executeWithHttpInfo() throws ApiException {
            SourceDatabase sourceDatabase = buildBodyParams();
            return startOnlineMigrationWithHttpInfo(databaseClusterUuid, sourceDatabase);
        }

        /**
         * Execute startOnlineMigration request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<OnlineMigration> _callback) throws ApiException {
            SourceDatabase sourceDatabase = buildBodyParams();
            return startOnlineMigrationAsync(databaseClusterUuid, sourceDatabase, _callback);
        }
    }

    /**
     * Start an Online Migration
     * To start an online migration, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/online-migration&#x60; endpoint. Migrating a cluster establishes a connection with an existing cluster and replicates its contents to the target cluster. Online migration is only available for MySQL, PostgreSQL, and Redis clusters.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param sourceDatabase  (required)
     * @return StartOnlineMigrationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public StartOnlineMigrationRequestBuilder startOnlineMigration(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new StartOnlineMigrationRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call stopOnlineMigrationCall(UUID databaseClusterUuid, String migrationId, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/online-migration/{migration_id}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "migration_id" + "}", localVarApiClient.escapeString(migrationId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call stopOnlineMigrationValidateBeforeCall(UUID databaseClusterUuid, String migrationId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling stopOnlineMigration(Async)");
        }

        // verify the required parameter 'migrationId' is set
        if (migrationId == null) {
            throw new ApiException("Missing the required parameter 'migrationId' when calling stopOnlineMigration(Async)");
        }

        return stopOnlineMigrationCall(databaseClusterUuid, migrationId, _callback);

    }


    private ApiResponse<Void> stopOnlineMigrationWithHttpInfo(UUID databaseClusterUuid, String migrationId) throws ApiException {
        okhttp3.Call localVarCall = stopOnlineMigrationValidateBeforeCall(databaseClusterUuid, migrationId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call stopOnlineMigrationAsync(UUID databaseClusterUuid, String migrationId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = stopOnlineMigrationValidateBeforeCall(databaseClusterUuid, migrationId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class StopOnlineMigrationRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String migrationId;

        private StopOnlineMigrationRequestBuilder(UUID databaseClusterUuid, String migrationId) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.migrationId = migrationId;
        }

        /**
         * Build call for stopOnlineMigration
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return stopOnlineMigrationCall(databaseClusterUuid, migrationId, _callback);
        }


        /**
         * Execute stopOnlineMigration request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            stopOnlineMigrationWithHttpInfo(databaseClusterUuid, migrationId);
        }

        /**
         * Execute stopOnlineMigration request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return stopOnlineMigrationWithHttpInfo(databaseClusterUuid, migrationId);
        }

        /**
         * Execute stopOnlineMigration request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return stopOnlineMigrationAsync(databaseClusterUuid, migrationId, _callback);
        }
    }

    /**
     * Stop an Online Migration
     * To stop an online migration, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/online-migration/$MIGRATION_ID&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param migrationId A unique identifier assigned to the online migration. (required)
     * @return StopOnlineMigrationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public StopOnlineMigrationRequestBuilder stopOnlineMigration(UUID databaseClusterUuid, String migrationId) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (migrationId == null) throw new IllegalArgumentException("\"migrationId\" is required but got null");
            

        return new StopOnlineMigrationRequestBuilder(databaseClusterUuid, migrationId);
    }
    private okhttp3.Call updateConfigClusterCall(UUID databaseClusterUuid, DatabaseConfig databaseConfig, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databaseConfig;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/config"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PATCH", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateConfigClusterValidateBeforeCall(UUID databaseClusterUuid, DatabaseConfig databaseConfig, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling updateConfigCluster(Async)");
        }

        // verify the required parameter 'databaseConfig' is set
        if (databaseConfig == null) {
            throw new ApiException("Missing the required parameter 'databaseConfig' when calling updateConfigCluster(Async)");
        }

        return updateConfigClusterCall(databaseClusterUuid, databaseConfig, _callback);

    }


    private ApiResponse<Void> updateConfigClusterWithHttpInfo(UUID databaseClusterUuid, DatabaseConfig databaseConfig) throws ApiException {
        okhttp3.Call localVarCall = updateConfigClusterValidateBeforeCall(databaseClusterUuid, databaseConfig, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call updateConfigClusterAsync(UUID databaseClusterUuid, DatabaseConfig databaseConfig, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateConfigClusterValidateBeforeCall(databaseClusterUuid, databaseConfig, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UpdateConfigClusterRequestBuilder {
        private final UUID databaseClusterUuid;
        private Mpr config;

        private UpdateConfigClusterRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set config
         * @param config  (optional)
         * @return UpdateConfigClusterRequestBuilder
         */
        public UpdateConfigClusterRequestBuilder config(Mpr config) {
            this.config = config;
            return this;
        }
        
        /**
         * Build call for updateConfigCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabaseConfig databaseConfig = buildBodyParams();
            return updateConfigClusterCall(databaseClusterUuid, databaseConfig, _callback);
        }

        private DatabaseConfig buildBodyParams() {
            DatabaseConfig databaseConfig = new DatabaseConfig();
            databaseConfig.config(this.config);
            return databaseConfig;
        }

        /**
         * Execute updateConfigCluster request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DatabaseConfig databaseConfig = buildBodyParams();
            updateConfigClusterWithHttpInfo(databaseClusterUuid, databaseConfig);
        }

        /**
         * Execute updateConfigCluster request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            DatabaseConfig databaseConfig = buildBodyParams();
            return updateConfigClusterWithHttpInfo(databaseClusterUuid, databaseConfig);
        }

        /**
         * Execute updateConfigCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            DatabaseConfig databaseConfig = buildBodyParams();
            return updateConfigClusterAsync(databaseClusterUuid, databaseConfig, _callback);
        }
    }

    /**
     * Update the Database Configuration for an Existing Database
     * To update the configuration for an existing database cluster, send a PATCH request to &#x60;/v2/databases/$DATABASE_ID/config&#x60;. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databaseConfig  (required)
     * @return UpdateConfigClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateConfigClusterRequestBuilder updateConfigCluster(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new UpdateConfigClusterRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call updateConnectionPoolPostgresqlCall(UUID databaseClusterUuid, String poolName, ConnectionPoolUpdate connectionPoolUpdate, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = connectionPoolUpdate;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/pools/{pool_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "pool_name" + "}", localVarApiClient.escapeString(poolName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateConnectionPoolPostgresqlValidateBeforeCall(UUID databaseClusterUuid, String poolName, ConnectionPoolUpdate connectionPoolUpdate, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling updateConnectionPoolPostgresql(Async)");
        }

        // verify the required parameter 'poolName' is set
        if (poolName == null) {
            throw new ApiException("Missing the required parameter 'poolName' when calling updateConnectionPoolPostgresql(Async)");
        }

        // verify the required parameter 'connectionPoolUpdate' is set
        if (connectionPoolUpdate == null) {
            throw new ApiException("Missing the required parameter 'connectionPoolUpdate' when calling updateConnectionPoolPostgresql(Async)");
        }

        return updateConnectionPoolPostgresqlCall(databaseClusterUuid, poolName, connectionPoolUpdate, _callback);

    }


    private ApiResponse<Void> updateConnectionPoolPostgresqlWithHttpInfo(UUID databaseClusterUuid, String poolName, ConnectionPoolUpdate connectionPoolUpdate) throws ApiException {
        okhttp3.Call localVarCall = updateConnectionPoolPostgresqlValidateBeforeCall(databaseClusterUuid, poolName, connectionPoolUpdate, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call updateConnectionPoolPostgresqlAsync(UUID databaseClusterUuid, String poolName, ConnectionPoolUpdate connectionPoolUpdate, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateConnectionPoolPostgresqlValidateBeforeCall(databaseClusterUuid, poolName, connectionPoolUpdate, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UpdateConnectionPoolPostgresqlRequestBuilder {
        private final String mode;
        private final Integer size;
        private final String db;
        private final UUID databaseClusterUuid;
        private final String poolName;
        private String user;

        private UpdateConnectionPoolPostgresqlRequestBuilder(String mode, Integer size, String db, UUID databaseClusterUuid, String poolName) {
            this.mode = mode;
            this.size = size;
            this.db = db;
            this.databaseClusterUuid = databaseClusterUuid;
            this.poolName = poolName;
        }

        /**
         * Set user
         * @param user The name of the user for use with the connection pool. When excluded, all sessions connect to the database as the inbound user. (optional)
         * @return UpdateConnectionPoolPostgresqlRequestBuilder
         */
        public UpdateConnectionPoolPostgresqlRequestBuilder user(String user) {
            this.user = user;
            return this;
        }
        
        /**
         * Build call for updateConnectionPoolPostgresql
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            ConnectionPoolUpdate connectionPoolUpdate = buildBodyParams();
            return updateConnectionPoolPostgresqlCall(databaseClusterUuid, poolName, connectionPoolUpdate, _callback);
        }

        private ConnectionPoolUpdate buildBodyParams() {
            ConnectionPoolUpdate connectionPoolUpdate = new ConnectionPoolUpdate();
            connectionPoolUpdate.mode(this.mode);
            connectionPoolUpdate.size(this.size);
            connectionPoolUpdate.db(this.db);
            connectionPoolUpdate.user(this.user);
            return connectionPoolUpdate;
        }

        /**
         * Execute updateConnectionPoolPostgresql request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ConnectionPoolUpdate connectionPoolUpdate = buildBodyParams();
            updateConnectionPoolPostgresqlWithHttpInfo(databaseClusterUuid, poolName, connectionPoolUpdate);
        }

        /**
         * Execute updateConnectionPoolPostgresql request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            ConnectionPoolUpdate connectionPoolUpdate = buildBodyParams();
            return updateConnectionPoolPostgresqlWithHttpInfo(databaseClusterUuid, poolName, connectionPoolUpdate);
        }

        /**
         * Execute updateConnectionPoolPostgresql request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            ConnectionPoolUpdate connectionPoolUpdate = buildBodyParams();
            return updateConnectionPoolPostgresqlAsync(databaseClusterUuid, poolName, connectionPoolUpdate, _callback);
        }
    }

    /**
     * Update Connection Pools (PostgreSQL)
     * To update a connection pool for a PostgreSQL database cluster, send a PUT request to  &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param poolName The name used to identify the connection pool. (required)
     * @param connectionPoolUpdate  (required)
     * @return UpdateConnectionPoolPostgresqlRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateConnectionPoolPostgresqlRequestBuilder updateConnectionPoolPostgresql(String mode, Integer size, String db, UUID databaseClusterUuid, String poolName) throws IllegalArgumentException {
        if (mode == null) throw new IllegalArgumentException("\"mode\" is required but got null");
            

        if (size == null) throw new IllegalArgumentException("\"size\" is required but got null");
        if (db == null) throw new IllegalArgumentException("\"db\" is required but got null");
            

        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (poolName == null) throw new IllegalArgumentException("\"poolName\" is required but got null");
            

        return new UpdateConnectionPoolPostgresqlRequestBuilder(mode, size, db, databaseClusterUuid, poolName);
    }
    private okhttp3.Call updateFirewallRulesCall(UUID databaseClusterUuid, DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databasesUpdateFirewallRulesRequest;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/firewall"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateFirewallRulesValidateBeforeCall(UUID databaseClusterUuid, DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling updateFirewallRules(Async)");
        }

        // verify the required parameter 'databasesUpdateFirewallRulesRequest' is set
        if (databasesUpdateFirewallRulesRequest == null) {
            throw new ApiException("Missing the required parameter 'databasesUpdateFirewallRulesRequest' when calling updateFirewallRules(Async)");
        }

        return updateFirewallRulesCall(databaseClusterUuid, databasesUpdateFirewallRulesRequest, _callback);

    }


    private ApiResponse<Void> updateFirewallRulesWithHttpInfo(UUID databaseClusterUuid, DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest) throws ApiException {
        okhttp3.Call localVarCall = updateFirewallRulesValidateBeforeCall(databaseClusterUuid, databasesUpdateFirewallRulesRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call updateFirewallRulesAsync(UUID databaseClusterUuid, DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateFirewallRulesValidateBeforeCall(databaseClusterUuid, databasesUpdateFirewallRulesRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UpdateFirewallRulesRequestBuilder {
        private final UUID databaseClusterUuid;
        private List<FirewallRule> rules;

        private UpdateFirewallRulesRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set rules
         * @param rules  (optional)
         * @return UpdateFirewallRulesRequestBuilder
         */
        public UpdateFirewallRulesRequestBuilder rules(List<FirewallRule> rules) {
            this.rules = rules;
            return this;
        }
        
        /**
         * Build call for updateFirewallRules
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest = buildBodyParams();
            return updateFirewallRulesCall(databaseClusterUuid, databasesUpdateFirewallRulesRequest, _callback);
        }

        private DatabasesUpdateFirewallRulesRequest buildBodyParams() {
            DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest = new DatabasesUpdateFirewallRulesRequest();
            databasesUpdateFirewallRulesRequest.rules(this.rules);
            return databasesUpdateFirewallRulesRequest;
        }

        /**
         * Execute updateFirewallRules request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest = buildBodyParams();
            updateFirewallRulesWithHttpInfo(databaseClusterUuid, databasesUpdateFirewallRulesRequest);
        }

        /**
         * Execute updateFirewallRules request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest = buildBodyParams();
            return updateFirewallRulesWithHttpInfo(databaseClusterUuid, databasesUpdateFirewallRulesRequest);
        }

        /**
         * Execute updateFirewallRules request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            DatabasesUpdateFirewallRulesRequest databasesUpdateFirewallRulesRequest = buildBodyParams();
            return updateFirewallRulesAsync(databaseClusterUuid, databasesUpdateFirewallRulesRequest, _callback);
        }
    }

    /**
     * Update Firewall Rules (Trusted Sources) for a Database
     * To update a database cluster&#39;s firewall rules (known as \&quot;trusted sources\&quot; in the control panel), send a PUT request to &#x60;/v2/databases/$DATABASE_ID/firewall&#x60; specifying which resources should be able to open connections to the database. You may limit connections to specific Droplets, Kubernetes clusters, or IP addresses. When a tag is provided, any Droplet or Kubernetes node with that tag applied to it will have access. The firewall is limited to 100 rules (or trusted sources). When possible, we recommend [placing your databases into a VPC network](https://www.digitalocean.com/docs/networking/vpc/) to limit access to them instead of using a firewall. A successful
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param databasesUpdateFirewallRulesRequest  (required)
     * @return UpdateFirewallRulesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateFirewallRulesRequestBuilder updateFirewallRules(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new UpdateFirewallRulesRequestBuilder(databaseClusterUuid);
    }
    private okhttp3.Call updateMetricsCredentialsCall(DatabaseMetricsCredentials databaseMetricsCredentials, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databaseMetricsCredentials;

        // create path and map variables
        String localVarPath = "/v2/databases/metrics/credentials";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateMetricsCredentialsValidateBeforeCall(DatabaseMetricsCredentials databaseMetricsCredentials, final ApiCallback _callback) throws ApiException {
        return updateMetricsCredentialsCall(databaseMetricsCredentials, _callback);

    }


    private ApiResponse<Void> updateMetricsCredentialsWithHttpInfo(DatabaseMetricsCredentials databaseMetricsCredentials) throws ApiException {
        okhttp3.Call localVarCall = updateMetricsCredentialsValidateBeforeCall(databaseMetricsCredentials, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call updateMetricsCredentialsAsync(DatabaseMetricsCredentials databaseMetricsCredentials, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateMetricsCredentialsValidateBeforeCall(databaseMetricsCredentials, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UpdateMetricsCredentialsRequestBuilder {
        private DatabasesBasicAuthCredentials credentials;

        private UpdateMetricsCredentialsRequestBuilder() {
        }

        /**
         * Set credentials
         * @param credentials  (optional)
         * @return UpdateMetricsCredentialsRequestBuilder
         */
        public UpdateMetricsCredentialsRequestBuilder credentials(DatabasesBasicAuthCredentials credentials) {
            this.credentials = credentials;
            return this;
        }
        
        /**
         * Build call for updateMetricsCredentials
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabaseMetricsCredentials databaseMetricsCredentials = buildBodyParams();
            return updateMetricsCredentialsCall(databaseMetricsCredentials, _callback);
        }

        private DatabaseMetricsCredentials buildBodyParams() {
            DatabaseMetricsCredentials databaseMetricsCredentials = new DatabaseMetricsCredentials();
            databaseMetricsCredentials.credentials(this.credentials);
            return databaseMetricsCredentials;
        }

        /**
         * Execute updateMetricsCredentials request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DatabaseMetricsCredentials databaseMetricsCredentials = buildBodyParams();
            updateMetricsCredentialsWithHttpInfo(databaseMetricsCredentials);
        }

        /**
         * Execute updateMetricsCredentials request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            DatabaseMetricsCredentials databaseMetricsCredentials = buildBodyParams();
            return updateMetricsCredentialsWithHttpInfo(databaseMetricsCredentials);
        }

        /**
         * Execute updateMetricsCredentials request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            DatabaseMetricsCredentials databaseMetricsCredentials = buildBodyParams();
            return updateMetricsCredentialsAsync(databaseMetricsCredentials, _callback);
        }
    }

    /**
     * Update Database Clusters&#39; Metrics Endpoint Credentials
     * To update the credentials for all database clusters&#39; metrics endpoints, send a PUT request to &#x60;/v2/databases/metrics/credentials&#x60;. A successful request will receive a 204 No Content status code  with no body in response.
     * @return UpdateMetricsCredentialsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateMetricsCredentialsRequestBuilder updateMetricsCredentials() throws IllegalArgumentException {
        return new UpdateMetricsCredentialsRequestBuilder();
    }
    private okhttp3.Call updateSettingsCall(UUID databaseClusterUuid, String username, DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = databasesUpdateSettingsRequest;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/users/{username}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "username" + "}", localVarApiClient.escapeString(username.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateSettingsValidateBeforeCall(UUID databaseClusterUuid, String username, DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling updateSettings(Async)");
        }

        // verify the required parameter 'username' is set
        if (username == null) {
            throw new ApiException("Missing the required parameter 'username' when calling updateSettings(Async)");
        }

        // verify the required parameter 'databasesUpdateSettingsRequest' is set
        if (databasesUpdateSettingsRequest == null) {
            throw new ApiException("Missing the required parameter 'databasesUpdateSettingsRequest' when calling updateSettings(Async)");
        }

        return updateSettingsCall(databaseClusterUuid, username, databasesUpdateSettingsRequest, _callback);

    }


    private ApiResponse<DatabasesAddUserResponse> updateSettingsWithHttpInfo(UUID databaseClusterUuid, String username, DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest) throws ApiException {
        okhttp3.Call localVarCall = updateSettingsValidateBeforeCall(databaseClusterUuid, username, databasesUpdateSettingsRequest, null);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateSettingsAsync(UUID databaseClusterUuid, String username, DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest, final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateSettingsValidateBeforeCall(databaseClusterUuid, username, databasesUpdateSettingsRequest, _callback);
        Type localVarReturnType = new TypeToken<DatabasesAddUserResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateSettingsRequestBuilder {
        private final UserSettings settings;
        private final UUID databaseClusterUuid;
        private final String username;

        private UpdateSettingsRequestBuilder(UserSettings settings, UUID databaseClusterUuid, String username) {
            this.settings = settings;
            this.databaseClusterUuid = databaseClusterUuid;
            this.username = username;
        }

        /**
         * Build call for updateSettings
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest = buildBodyParams();
            return updateSettingsCall(databaseClusterUuid, username, databasesUpdateSettingsRequest, _callback);
        }

        private DatabasesUpdateSettingsRequest buildBodyParams() {
            DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest = new DatabasesUpdateSettingsRequest();
            return databasesUpdateSettingsRequest;
        }

        /**
         * Execute updateSettings request
         * @return DatabasesAddUserResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesAddUserResponse execute() throws ApiException {
            DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest = buildBodyParams();
            ApiResponse<DatabasesAddUserResponse> localVarResp = updateSettingsWithHttpInfo(databaseClusterUuid, username, databasesUpdateSettingsRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateSettings request with HTTP info returned
         * @return ApiResponse&lt;DatabasesAddUserResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesAddUserResponse> executeWithHttpInfo() throws ApiException {
            DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest = buildBodyParams();
            return updateSettingsWithHttpInfo(databaseClusterUuid, username, databasesUpdateSettingsRequest);
        }

        /**
         * Execute updateSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesAddUserResponse> _callback) throws ApiException {
            DatabasesUpdateSettingsRequest databasesUpdateSettingsRequest = buildBodyParams();
            return updateSettingsAsync(databaseClusterUuid, username, databasesUpdateSettingsRequest, _callback);
        }
    }

    /**
     * Update a Database User
     * To update an existing database user, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60; with the desired settings.  **Note**: only &#x60;settings&#x60; can be updated via this type of request. If you wish to change the name of a user, you must recreate a new user.  The response will be a JSON object with a key called &#x60;user&#x60;. The value of this will be an object that contains the name of the update database user, along with the &#x60;settings&#x60; object that has been updated. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param username The name of the database user. (required)
     * @param databasesUpdateSettingsRequest  (required)
     * @return UpdateSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> A JSON object with a key of &#x60;user&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateSettingsRequestBuilder updateSettings(UserSettings settings, UUID databaseClusterUuid, String username) throws IllegalArgumentException {
        if (settings == null) throw new IllegalArgumentException("\"settings\" is required but got null");
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (username == null) throw new IllegalArgumentException("\"username\" is required but got null");
            

        return new UpdateSettingsRequestBuilder(settings, databaseClusterUuid, username);
    }
    private okhttp3.Call updateSqlModeCall(UUID databaseClusterUuid, SqlMode sqlMode, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = sqlMode;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/sql_mode"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateSqlModeValidateBeforeCall(UUID databaseClusterUuid, SqlMode sqlMode, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling updateSqlMode(Async)");
        }

        // verify the required parameter 'sqlMode' is set
        if (sqlMode == null) {
            throw new ApiException("Missing the required parameter 'sqlMode' when calling updateSqlMode(Async)");
        }

        return updateSqlModeCall(databaseClusterUuid, sqlMode, _callback);

    }


    private ApiResponse<Void> updateSqlModeWithHttpInfo(UUID databaseClusterUuid, SqlMode sqlMode) throws ApiException {
        okhttp3.Call localVarCall = updateSqlModeValidateBeforeCall(databaseClusterUuid, sqlMode, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call updateSqlModeAsync(UUID databaseClusterUuid, SqlMode sqlMode, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateSqlModeValidateBeforeCall(databaseClusterUuid, sqlMode, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UpdateSqlModeRequestBuilder {
        private final String sqlMode;
        private final UUID databaseClusterUuid;

        private UpdateSqlModeRequestBuilder(String sqlMode, UUID databaseClusterUuid) {
            this.sqlMode = sqlMode;
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Build call for updateSqlMode
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            SqlMode sqlMode = buildBodyParams();
            return updateSqlModeCall(databaseClusterUuid, sqlMode, _callback);
        }

        private SqlMode buildBodyParams() {
            SqlMode sqlMode = new SqlMode();
            sqlMode.sqlMode(this.sqlMode);
            return sqlMode;
        }

        /**
         * Execute updateSqlMode request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            SqlMode sqlMode = buildBodyParams();
            updateSqlModeWithHttpInfo(databaseClusterUuid, sqlMode);
        }

        /**
         * Execute updateSqlMode request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            SqlMode sqlMode = buildBodyParams();
            return updateSqlModeWithHttpInfo(databaseClusterUuid, sqlMode);
        }

        /**
         * Execute updateSqlMode request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            SqlMode sqlMode = buildBodyParams();
            return updateSqlModeAsync(databaseClusterUuid, sqlMode, _callback);
        }
    }

    /**
     * Update SQL Mode for a Cluster
     * To configure the SQL modes for an existing MySQL cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/sql_mode&#x60; specifying the desired modes. See the official MySQL 8 documentation for a [full list of supported SQL modes](https://dev.mysql.com/doc/refman/8.0/en/sql-mode.html#sql-mode-full). A successful request will receive a 204 No Content status code with no body in response.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param sqlMode  (required)
     * @return UpdateSqlModeRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateSqlModeRequestBuilder updateSqlMode(String sqlMode, UUID databaseClusterUuid) throws IllegalArgumentException {
        if (sqlMode == null) throw new IllegalArgumentException("\"sqlMode\" is required but got null");
            

        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new UpdateSqlModeRequestBuilder(sqlMode, databaseClusterUuid);
    }
    private okhttp3.Call updateTopicKafkaClusterCall(UUID databaseClusterUuid, String topicName, KafkaTopicUpdate kafkaTopicUpdate, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = kafkaTopicUpdate;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/topics/{topic_name}"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()))
            .replace("{" + "topic_name" + "}", localVarApiClient.escapeString(topicName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateTopicKafkaClusterValidateBeforeCall(UUID databaseClusterUuid, String topicName, KafkaTopicUpdate kafkaTopicUpdate, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling updateTopicKafkaCluster(Async)");
        }

        // verify the required parameter 'topicName' is set
        if (topicName == null) {
            throw new ApiException("Missing the required parameter 'topicName' when calling updateTopicKafkaCluster(Async)");
        }

        return updateTopicKafkaClusterCall(databaseClusterUuid, topicName, kafkaTopicUpdate, _callback);

    }


    private ApiResponse<DatabasesCreateTopicKafkaClusterResponse> updateTopicKafkaClusterWithHttpInfo(UUID databaseClusterUuid, String topicName, KafkaTopicUpdate kafkaTopicUpdate) throws ApiException {
        okhttp3.Call localVarCall = updateTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, topicName, kafkaTopicUpdate, null);
        Type localVarReturnType = new TypeToken<DatabasesCreateTopicKafkaClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateTopicKafkaClusterAsync(UUID databaseClusterUuid, String topicName, KafkaTopicUpdate kafkaTopicUpdate, final ApiCallback<DatabasesCreateTopicKafkaClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateTopicKafkaClusterValidateBeforeCall(databaseClusterUuid, topicName, kafkaTopicUpdate, _callback);
        Type localVarReturnType = new TypeToken<DatabasesCreateTopicKafkaClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateTopicKafkaClusterRequestBuilder {
        private final UUID databaseClusterUuid;
        private final String topicName;
        private Integer replicationFactor;
        private Integer partitionCount;
        private KafkaTopicConfig config;

        private UpdateTopicKafkaClusterRequestBuilder(UUID databaseClusterUuid, String topicName) {
            this.databaseClusterUuid = databaseClusterUuid;
            this.topicName = topicName;
        }

        /**
         * Set replicationFactor
         * @param replicationFactor The number of nodes to replicate data across the cluster. (optional)
         * @return UpdateTopicKafkaClusterRequestBuilder
         */
        public UpdateTopicKafkaClusterRequestBuilder replicationFactor(Integer replicationFactor) {
            this.replicationFactor = replicationFactor;
            return this;
        }
        
        /**
         * Set partitionCount
         * @param partitionCount The number of partitions available for the topic. On update, this value can only be increased. (optional)
         * @return UpdateTopicKafkaClusterRequestBuilder
         */
        public UpdateTopicKafkaClusterRequestBuilder partitionCount(Integer partitionCount) {
            this.partitionCount = partitionCount;
            return this;
        }
        
        /**
         * Set config
         * @param config  (optional)
         * @return UpdateTopicKafkaClusterRequestBuilder
         */
        public UpdateTopicKafkaClusterRequestBuilder config(KafkaTopicConfig config) {
            this.config = config;
            return this;
        }
        
        /**
         * Build call for updateTopicKafkaCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            KafkaTopicUpdate kafkaTopicUpdate = buildBodyParams();
            return updateTopicKafkaClusterCall(databaseClusterUuid, topicName, kafkaTopicUpdate, _callback);
        }

        private KafkaTopicUpdate buildBodyParams() {
            KafkaTopicUpdate kafkaTopicUpdate = new KafkaTopicUpdate();
            kafkaTopicUpdate.replicationFactor(this.replicationFactor);
            kafkaTopicUpdate.partitionCount(this.partitionCount);
            kafkaTopicUpdate.config(this.config);
            return kafkaTopicUpdate;
        }

        /**
         * Execute updateTopicKafkaCluster request
         * @return DatabasesCreateTopicKafkaClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DatabasesCreateTopicKafkaClusterResponse execute() throws ApiException {
            KafkaTopicUpdate kafkaTopicUpdate = buildBodyParams();
            ApiResponse<DatabasesCreateTopicKafkaClusterResponse> localVarResp = updateTopicKafkaClusterWithHttpInfo(databaseClusterUuid, topicName, kafkaTopicUpdate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateTopicKafkaCluster request with HTTP info returned
         * @return ApiResponse&lt;DatabasesCreateTopicKafkaClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DatabasesCreateTopicKafkaClusterResponse> executeWithHttpInfo() throws ApiException {
            KafkaTopicUpdate kafkaTopicUpdate = buildBodyParams();
            return updateTopicKafkaClusterWithHttpInfo(databaseClusterUuid, topicName, kafkaTopicUpdate);
        }

        /**
         * Execute updateTopicKafkaCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DatabasesCreateTopicKafkaClusterResponse> _callback) throws ApiException {
            KafkaTopicUpdate kafkaTopicUpdate = buildBodyParams();
            return updateTopicKafkaClusterAsync(databaseClusterUuid, topicName, kafkaTopicUpdate, _callback);
        }
    }

    /**
     * Update Topic for a Kafka Cluster
     * To update a topic attached to a Kafka cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param topicName The name used to identify the Kafka topic. (required)
     * @return UpdateTopicKafkaClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;topic&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateTopicKafkaClusterRequestBuilder updateTopicKafkaCluster(UUID databaseClusterUuid, String topicName) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        if (topicName == null) throw new IllegalArgumentException("\"topicName\" is required but got null");
            

        return new UpdateTopicKafkaClusterRequestBuilder(databaseClusterUuid, topicName);
    }
    private okhttp3.Call upgradeMajorVersionCall(UUID databaseClusterUuid, Version2 version2, final ApiCallback _callback) throws ApiException {
        String basePath = null;
        // Operation Servers
        String[] localBasePaths = new String[] {  };

        // Determine Base Path to Use
        if (localCustomBaseUrl != null){
            basePath = localCustomBaseUrl;
        } else if ( localBasePaths.length > 0 ) {
            basePath = localBasePaths[localHostIndex];
        } else {
            basePath = null;
        }

        Object localVarPostBody = version2;

        // create path and map variables
        String localVarPath = "/v2/databases/{database_cluster_uuid}/upgrade"
            .replace("{" + "database_cluster_uuid" + "}", localVarApiClient.escapeString(databaseClusterUuid.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = localVarApiClient.selectHeaderContentType(localVarContentTypes);
        if (localVarContentType != null) {
            localVarHeaderParams.put("Content-Type", localVarContentType);
        }

        String[] localVarAuthNames = new String[] { "bearer_auth" };
        return localVarApiClient.buildCall(basePath, localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call upgradeMajorVersionValidateBeforeCall(UUID databaseClusterUuid, Version2 version2, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'databaseClusterUuid' is set
        if (databaseClusterUuid == null) {
            throw new ApiException("Missing the required parameter 'databaseClusterUuid' when calling upgradeMajorVersion(Async)");
        }

        // verify the required parameter 'version2' is set
        if (version2 == null) {
            throw new ApiException("Missing the required parameter 'version2' when calling upgradeMajorVersion(Async)");
        }

        return upgradeMajorVersionCall(databaseClusterUuid, version2, _callback);

    }


    private ApiResponse<Void> upgradeMajorVersionWithHttpInfo(UUID databaseClusterUuid, Version2 version2) throws ApiException {
        okhttp3.Call localVarCall = upgradeMajorVersionValidateBeforeCall(databaseClusterUuid, version2, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call upgradeMajorVersionAsync(UUID databaseClusterUuid, Version2 version2, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = upgradeMajorVersionValidateBeforeCall(databaseClusterUuid, version2, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UpgradeMajorVersionRequestBuilder {
        private final UUID databaseClusterUuid;
        private String version;

        private UpgradeMajorVersionRequestBuilder(UUID databaseClusterUuid) {
            this.databaseClusterUuid = databaseClusterUuid;
        }

        /**
         * Set version
         * @param version A string representing the version of the database engine in use for the cluster. (optional)
         * @return UpgradeMajorVersionRequestBuilder
         */
        public UpgradeMajorVersionRequestBuilder version(String version) {
            this.version = version;
            return this;
        }
        
        /**
         * Build call for upgradeMajorVersion
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Version2 version2 = buildBodyParams();
            return upgradeMajorVersionCall(databaseClusterUuid, version2, _callback);
        }

        private Version2 buildBodyParams() {
            Version2 version2 = new Version2();
            version2.version(this.version);
            return version2;
        }

        /**
         * Execute upgradeMajorVersion request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            Version2 version2 = buildBodyParams();
            upgradeMajorVersionWithHttpInfo(databaseClusterUuid, version2);
        }

        /**
         * Execute upgradeMajorVersion request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            Version2 version2 = buildBodyParams();
            return upgradeMajorVersionWithHttpInfo(databaseClusterUuid, version2);
        }

        /**
         * Execute upgradeMajorVersion request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            Version2 version2 = buildBodyParams();
            return upgradeMajorVersionAsync(databaseClusterUuid, version2, _callback);
        }
    }

    /**
     * Upgrade Major Version for a Database
     * To upgrade the major version of a database, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/upgrade&#x60;, specifying the target version. A successful request will receive a 204 No Content status code with no body in response.
     * @param databaseClusterUuid A unique identifier for a database cluster. (required)
     * @param version2  (required)
     * @return UpgradeMajorVersionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpgradeMajorVersionRequestBuilder upgradeMajorVersion(UUID databaseClusterUuid) throws IllegalArgumentException {
        if (databaseClusterUuid == null) throw new IllegalArgumentException("\"databaseClusterUuid\" is required but got null");
            

        return new UpgradeMajorVersionRequestBuilder(databaseClusterUuid);
    }
}
