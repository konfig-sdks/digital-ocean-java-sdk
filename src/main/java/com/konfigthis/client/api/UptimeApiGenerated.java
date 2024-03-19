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


import com.konfigthis.client.model.Alert;
import com.konfigthis.client.model.AlertUpdatable;
import com.konfigthis.client.model.CheckUpdatable;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.Notification;
import java.util.UUID;
import com.konfigthis.client.model.UptimeCreateCheckResponse;
import com.konfigthis.client.model.UptimeCreateNewAlertResponse;
import com.konfigthis.client.model.UptimeGetCheckStateResponse;
import com.konfigthis.client.model.UptimeListAllAlertsResponse;
import com.konfigthis.client.model.UptimeListChecksResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class UptimeApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public UptimeApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public UptimeApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call createCheckCall(CheckUpdatable checkUpdatable, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = checkUpdatable;

        // create path and map variables
        String localVarPath = "/v2/uptime/checks";

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
    private okhttp3.Call createCheckValidateBeforeCall(CheckUpdatable checkUpdatable, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkUpdatable' is set
        if (checkUpdatable == null) {
            throw new ApiException("Missing the required parameter 'checkUpdatable' when calling createCheck(Async)");
        }

        return createCheckCall(checkUpdatable, _callback);

    }


    private ApiResponse<UptimeCreateCheckResponse> createCheckWithHttpInfo(CheckUpdatable checkUpdatable) throws ApiException {
        okhttp3.Call localVarCall = createCheckValidateBeforeCall(checkUpdatable, null);
        Type localVarReturnType = new TypeToken<UptimeCreateCheckResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createCheckAsync(CheckUpdatable checkUpdatable, final ApiCallback<UptimeCreateCheckResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createCheckValidateBeforeCall(checkUpdatable, _callback);
        Type localVarReturnType = new TypeToken<UptimeCreateCheckResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateCheckRequestBuilder {
        private String name;
        private String type;
        private String target;
        private List<String> regions;
        private Boolean enabled;

        private CreateCheckRequestBuilder() {
        }

        /**
         * Set name
         * @param name A human-friendly display name. (optional)
         * @return CreateCheckRequestBuilder
         */
        public CreateCheckRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set type
         * @param type The type of health check to perform. (optional)
         * @return CreateCheckRequestBuilder
         */
        public CreateCheckRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set target
         * @param target The endpoint to perform healthchecks on. (optional)
         * @return CreateCheckRequestBuilder
         */
        public CreateCheckRequestBuilder target(String target) {
            this.target = target;
            return this;
        }
        
        /**
         * Set regions
         * @param regions An array containing the selected regions to perform healthchecks from. (optional)
         * @return CreateCheckRequestBuilder
         */
        public CreateCheckRequestBuilder regions(List<String> regions) {
            this.regions = regions;
            return this;
        }
        
        /**
         * Set enabled
         * @param enabled A boolean value indicating whether the check is enabled/disabled. (optional, default to true)
         * @return CreateCheckRequestBuilder
         */
        public CreateCheckRequestBuilder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        /**
         * Build call for createCheck
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            return createCheckCall(checkUpdatable, _callback);
        }

        private CheckUpdatable buildBodyParams() {
            CheckUpdatable checkUpdatable = new CheckUpdatable();
            checkUpdatable.name(this.name);
            if (this.type != null)
            checkUpdatable.type(CheckUpdatable.TypeEnum.fromValue(this.type));
            checkUpdatable.target(this.target);
            if (this.regions != null)
            checkUpdatable.regions(CheckUpdatable.RegionsEnum.fromValue(this.regions));
            checkUpdatable.enabled(this.enabled);
            return checkUpdatable;
        }

        /**
         * Execute createCheck request
         * @return UptimeCreateCheckResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeCreateCheckResponse execute() throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            ApiResponse<UptimeCreateCheckResponse> localVarResp = createCheckWithHttpInfo(checkUpdatable);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createCheck request with HTTP info returned
         * @return ApiResponse&lt;UptimeCreateCheckResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeCreateCheckResponse> executeWithHttpInfo() throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            return createCheckWithHttpInfo(checkUpdatable);
        }

        /**
         * Execute createCheck request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeCreateCheckResponse> _callback) throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            return createCheckAsync(checkUpdatable, _callback);
        }
    }

    /**
     * Create a New Check
     * To create an Uptime check, send a POST request to &#x60;/v2/uptime/checks&#x60; specifying the attributes in the table below in the JSON body. 
     * @param checkUpdatable  (required)
     * @return CreateCheckRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateCheckRequestBuilder createCheck() throws IllegalArgumentException {
        return new CreateCheckRequestBuilder();
    }
    private okhttp3.Call createNewAlertCall(UUID checkId, Alert alert, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = alert;

        // create path and map variables
        String localVarPath = "/v2/uptime/checks/{check_id}/alerts"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()));

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
    private okhttp3.Call createNewAlertValidateBeforeCall(UUID checkId, Alert alert, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling createNewAlert(Async)");
        }

        // verify the required parameter 'alert' is set
        if (alert == null) {
            throw new ApiException("Missing the required parameter 'alert' when calling createNewAlert(Async)");
        }

        return createNewAlertCall(checkId, alert, _callback);

    }


    private ApiResponse<UptimeCreateNewAlertResponse> createNewAlertWithHttpInfo(UUID checkId, Alert alert) throws ApiException {
        okhttp3.Call localVarCall = createNewAlertValidateBeforeCall(checkId, alert, null);
        Type localVarReturnType = new TypeToken<UptimeCreateNewAlertResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createNewAlertAsync(UUID checkId, Alert alert, final ApiCallback<UptimeCreateNewAlertResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createNewAlertValidateBeforeCall(checkId, alert, _callback);
        Type localVarReturnType = new TypeToken<UptimeCreateNewAlertResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateNewAlertRequestBuilder {
        private final UUID checkId;
        private UUID id;
        private String name;
        private String type;
        private Integer threshold;
        private String comparison;
        private Notification notifications;
        private String period;

        private CreateNewAlertRequestBuilder(UUID checkId) {
            this.checkId = checkId;
        }

        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference the alert. (optional)
         * @return CreateNewAlertRequestBuilder
         */
        public CreateNewAlertRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set name
         * @param name A human-friendly display name. (optional)
         * @return CreateNewAlertRequestBuilder
         */
        public CreateNewAlertRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set type
         * @param type The type of alert. (optional)
         * @return CreateNewAlertRequestBuilder
         */
        public CreateNewAlertRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set threshold
         * @param threshold The threshold at which the alert will enter a trigger state. The specific threshold is dependent on the alert type. (optional)
         * @return CreateNewAlertRequestBuilder
         */
        public CreateNewAlertRequestBuilder threshold(Integer threshold) {
            this.threshold = threshold;
            return this;
        }
        
        /**
         * Set comparison
         * @param comparison The comparison operator used against the alert&#39;s threshold. (optional)
         * @return CreateNewAlertRequestBuilder
         */
        public CreateNewAlertRequestBuilder comparison(String comparison) {
            this.comparison = comparison;
            return this;
        }
        
        /**
         * Set notifications
         * @param notifications  (optional)
         * @return CreateNewAlertRequestBuilder
         */
        public CreateNewAlertRequestBuilder notifications(Notification notifications) {
            this.notifications = notifications;
            return this;
        }
        
        /**
         * Set period
         * @param period Period of time the threshold must be exceeded to trigger the alert. (optional)
         * @return CreateNewAlertRequestBuilder
         */
        public CreateNewAlertRequestBuilder period(String period) {
            this.period = period;
            return this;
        }
        
        /**
         * Build call for createNewAlert
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Alert alert = buildBodyParams();
            return createNewAlertCall(checkId, alert, _callback);
        }

        private Alert buildBodyParams() {
            Alert alert = new Alert();
            return alert;
        }

        /**
         * Execute createNewAlert request
         * @return UptimeCreateNewAlertResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeCreateNewAlertResponse execute() throws ApiException {
            Alert alert = buildBodyParams();
            ApiResponse<UptimeCreateNewAlertResponse> localVarResp = createNewAlertWithHttpInfo(checkId, alert);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createNewAlert request with HTTP info returned
         * @return ApiResponse&lt;UptimeCreateNewAlertResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeCreateNewAlertResponse> executeWithHttpInfo() throws ApiException {
            Alert alert = buildBodyParams();
            return createNewAlertWithHttpInfo(checkId, alert);
        }

        /**
         * Execute createNewAlert request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeCreateNewAlertResponse> _callback) throws ApiException {
            Alert alert = buildBodyParams();
            return createNewAlertAsync(checkId, alert, _callback);
        }
    }

    /**
     * Create a New Alert
     * To create an Uptime alert, send a POST request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts&#x60; specifying the attributes in the table below in the JSON body. 
     * @param checkId A unique identifier for a check. (required)
     * @param alert The &#39;&#39;type&#39;&#39; field dictates the type of alert, and hence what type of value to pass into the threshold property. Type | Description | Threshold Value -----|-------------|-------------------- &#x60;latency&#x60; | alerts on the response latency | milliseconds &#x60;down&#x60; | alerts on a target registering as down in any region | N/A (Not required) &#x60;down_global&#x60; | alerts on a target registering as down globally | N/A (Not required) &#x60;ssl_expiry&#x60; | alerts on a SSL certificate expiring within $threshold days | days  (required)
     * @return CreateNewAlertRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateNewAlertRequestBuilder createNewAlert(UUID checkId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        return new CreateNewAlertRequestBuilder(checkId);
    }
    private okhttp3.Call deleteAlertCall(UUID checkId, UUID alertId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/uptime/checks/{check_id}/alerts/{alert_id}"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()))
            .replace("{" + "alert_id" + "}", localVarApiClient.escapeString(alertId.toString()));

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
    private okhttp3.Call deleteAlertValidateBeforeCall(UUID checkId, UUID alertId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling deleteAlert(Async)");
        }

        // verify the required parameter 'alertId' is set
        if (alertId == null) {
            throw new ApiException("Missing the required parameter 'alertId' when calling deleteAlert(Async)");
        }

        return deleteAlertCall(checkId, alertId, _callback);

    }


    private ApiResponse<Void> deleteAlertWithHttpInfo(UUID checkId, UUID alertId) throws ApiException {
        okhttp3.Call localVarCall = deleteAlertValidateBeforeCall(checkId, alertId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAlertAsync(UUID checkId, UUID alertId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteAlertValidateBeforeCall(checkId, alertId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteAlertRequestBuilder {
        private final UUID checkId;
        private final UUID alertId;

        private DeleteAlertRequestBuilder(UUID checkId, UUID alertId) {
            this.checkId = checkId;
            this.alertId = alertId;
        }

        /**
         * Build call for deleteAlert
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
            return deleteAlertCall(checkId, alertId, _callback);
        }


        /**
         * Execute deleteAlert request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteAlertWithHttpInfo(checkId, alertId);
        }

        /**
         * Execute deleteAlert request with HTTP info returned
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
            return deleteAlertWithHttpInfo(checkId, alertId);
        }

        /**
         * Execute deleteAlert request (asynchronously)
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
            return deleteAlertAsync(checkId, alertId, _callback);
        }
    }

    /**
     * Delete an Alert
     * To delete an Uptime alert, send a DELETE request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts/$ALERT_ID&#x60;. A 204 status code with no body will be returned in response to a successful request. 
     * @param checkId A unique identifier for a check. (required)
     * @param alertId A unique identifier for an alert. (required)
     * @return DeleteAlertRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteAlertRequestBuilder deleteAlert(UUID checkId, UUID alertId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        if (alertId == null) throw new IllegalArgumentException("\"alertId\" is required but got null");
            

        return new DeleteAlertRequestBuilder(checkId, alertId);
    }
    private okhttp3.Call deleteCheckCall(UUID checkId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/uptime/checks/{check_id}"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()));

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
    private okhttp3.Call deleteCheckValidateBeforeCall(UUID checkId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling deleteCheck(Async)");
        }

        return deleteCheckCall(checkId, _callback);

    }


    private ApiResponse<Void> deleteCheckWithHttpInfo(UUID checkId) throws ApiException {
        okhttp3.Call localVarCall = deleteCheckValidateBeforeCall(checkId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteCheckAsync(UUID checkId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteCheckValidateBeforeCall(checkId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteCheckRequestBuilder {
        private final UUID checkId;

        private DeleteCheckRequestBuilder(UUID checkId) {
            this.checkId = checkId;
        }

        /**
         * Build call for deleteCheck
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
            return deleteCheckCall(checkId, _callback);
        }


        /**
         * Execute deleteCheck request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteCheckWithHttpInfo(checkId);
        }

        /**
         * Execute deleteCheck request with HTTP info returned
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
            return deleteCheckWithHttpInfo(checkId);
        }

        /**
         * Execute deleteCheck request (asynchronously)
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
            return deleteCheckAsync(checkId, _callback);
        }
    }

    /**
     * Delete a Check
     * To delete an Uptime check, send a DELETE request to &#x60;/v2/uptime/checks/$CHECK_ID&#x60;. A 204 status code with no body will be returned in response to a successful request.   Deleting a check will also delete alerts associated with the check. 
     * @param checkId A unique identifier for a check. (required)
     * @return DeleteCheckRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteCheckRequestBuilder deleteCheck(UUID checkId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        return new DeleteCheckRequestBuilder(checkId);
    }
    private okhttp3.Call getCheckByIdCall(UUID checkId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/uptime/checks/{check_id}"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()));

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
    private okhttp3.Call getCheckByIdValidateBeforeCall(UUID checkId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling getCheckById(Async)");
        }

        return getCheckByIdCall(checkId, _callback);

    }


    private ApiResponse<UptimeCreateCheckResponse> getCheckByIdWithHttpInfo(UUID checkId) throws ApiException {
        okhttp3.Call localVarCall = getCheckByIdValidateBeforeCall(checkId, null);
        Type localVarReturnType = new TypeToken<UptimeCreateCheckResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getCheckByIdAsync(UUID checkId, final ApiCallback<UptimeCreateCheckResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getCheckByIdValidateBeforeCall(checkId, _callback);
        Type localVarReturnType = new TypeToken<UptimeCreateCheckResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetCheckByIdRequestBuilder {
        private final UUID checkId;

        private GetCheckByIdRequestBuilder(UUID checkId) {
            this.checkId = checkId;
        }

        /**
         * Build call for getCheckById
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCheckByIdCall(checkId, _callback);
        }


        /**
         * Execute getCheckById request
         * @return UptimeCreateCheckResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeCreateCheckResponse execute() throws ApiException {
            ApiResponse<UptimeCreateCheckResponse> localVarResp = getCheckByIdWithHttpInfo(checkId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getCheckById request with HTTP info returned
         * @return ApiResponse&lt;UptimeCreateCheckResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeCreateCheckResponse> executeWithHttpInfo() throws ApiException {
            return getCheckByIdWithHttpInfo(checkId);
        }

        /**
         * Execute getCheckById request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeCreateCheckResponse> _callback) throws ApiException {
            return getCheckByIdAsync(checkId, _callback);
        }
    }

    /**
     * Retrieve an Existing Check
     * To show information about an existing check, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID&#x60;.
     * @param checkId A unique identifier for a check. (required)
     * @return GetCheckByIdRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetCheckByIdRequestBuilder getCheckById(UUID checkId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        return new GetCheckByIdRequestBuilder(checkId);
    }
    private okhttp3.Call getCheckStateCall(UUID checkId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/uptime/checks/{check_id}/state"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()));

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
    private okhttp3.Call getCheckStateValidateBeforeCall(UUID checkId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling getCheckState(Async)");
        }

        return getCheckStateCall(checkId, _callback);

    }


    private ApiResponse<UptimeGetCheckStateResponse> getCheckStateWithHttpInfo(UUID checkId) throws ApiException {
        okhttp3.Call localVarCall = getCheckStateValidateBeforeCall(checkId, null);
        Type localVarReturnType = new TypeToken<UptimeGetCheckStateResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getCheckStateAsync(UUID checkId, final ApiCallback<UptimeGetCheckStateResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getCheckStateValidateBeforeCall(checkId, _callback);
        Type localVarReturnType = new TypeToken<UptimeGetCheckStateResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetCheckStateRequestBuilder {
        private final UUID checkId;

        private GetCheckStateRequestBuilder(UUID checkId) {
            this.checkId = checkId;
        }

        /**
         * Build call for getCheckState
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;state&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check&#39;s state. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCheckStateCall(checkId, _callback);
        }


        /**
         * Execute getCheckState request
         * @return UptimeGetCheckStateResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;state&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check&#39;s state. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeGetCheckStateResponse execute() throws ApiException {
            ApiResponse<UptimeGetCheckStateResponse> localVarResp = getCheckStateWithHttpInfo(checkId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getCheckState request with HTTP info returned
         * @return ApiResponse&lt;UptimeGetCheckStateResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;state&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check&#39;s state. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeGetCheckStateResponse> executeWithHttpInfo() throws ApiException {
            return getCheckStateWithHttpInfo(checkId);
        }

        /**
         * Execute getCheckState request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;state&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check&#39;s state. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeGetCheckStateResponse> _callback) throws ApiException {
            return getCheckStateAsync(checkId, _callback);
        }
    }

    /**
     * Retrieve Check State
     * To show information about an existing check&#39;s state, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID/state&#x60;.
     * @param checkId A unique identifier for a check. (required)
     * @return GetCheckStateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;state&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check&#39;s state. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetCheckStateRequestBuilder getCheckState(UUID checkId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        return new GetCheckStateRequestBuilder(checkId);
    }
    private okhttp3.Call getExistingAlertCall(UUID checkId, UUID alertId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/uptime/checks/{check_id}/alerts/{alert_id}"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()))
            .replace("{" + "alert_id" + "}", localVarApiClient.escapeString(alertId.toString()));

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
    private okhttp3.Call getExistingAlertValidateBeforeCall(UUID checkId, UUID alertId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling getExistingAlert(Async)");
        }

        // verify the required parameter 'alertId' is set
        if (alertId == null) {
            throw new ApiException("Missing the required parameter 'alertId' when calling getExistingAlert(Async)");
        }

        return getExistingAlertCall(checkId, alertId, _callback);

    }


    private ApiResponse<UptimeCreateNewAlertResponse> getExistingAlertWithHttpInfo(UUID checkId, UUID alertId) throws ApiException {
        okhttp3.Call localVarCall = getExistingAlertValidateBeforeCall(checkId, alertId, null);
        Type localVarReturnType = new TypeToken<UptimeCreateNewAlertResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getExistingAlertAsync(UUID checkId, UUID alertId, final ApiCallback<UptimeCreateNewAlertResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getExistingAlertValidateBeforeCall(checkId, alertId, _callback);
        Type localVarReturnType = new TypeToken<UptimeCreateNewAlertResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetExistingAlertRequestBuilder {
        private final UUID checkId;
        private final UUID alertId;

        private GetExistingAlertRequestBuilder(UUID checkId, UUID alertId) {
            this.checkId = checkId;
            this.alertId = alertId;
        }

        /**
         * Build call for getExistingAlert
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getExistingAlertCall(checkId, alertId, _callback);
        }


        /**
         * Execute getExistingAlert request
         * @return UptimeCreateNewAlertResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeCreateNewAlertResponse execute() throws ApiException {
            ApiResponse<UptimeCreateNewAlertResponse> localVarResp = getExistingAlertWithHttpInfo(checkId, alertId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getExistingAlert request with HTTP info returned
         * @return ApiResponse&lt;UptimeCreateNewAlertResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeCreateNewAlertResponse> executeWithHttpInfo() throws ApiException {
            return getExistingAlertWithHttpInfo(checkId, alertId);
        }

        /**
         * Execute getExistingAlert request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeCreateNewAlertResponse> _callback) throws ApiException {
            return getExistingAlertAsync(checkId, alertId, _callback);
        }
    }

    /**
     * Retrieve an Existing Alert
     * To show information about an existing alert, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts/$ALERT_ID&#x60;.
     * @param checkId A unique identifier for a check. (required)
     * @param alertId A unique identifier for an alert. (required)
     * @return GetExistingAlertRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetExistingAlertRequestBuilder getExistingAlert(UUID checkId, UUID alertId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        if (alertId == null) throw new IllegalArgumentException("\"alertId\" is required but got null");
            

        return new GetExistingAlertRequestBuilder(checkId, alertId);
    }
    private okhttp3.Call listAllAlertsCall(UUID checkId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/uptime/checks/{check_id}/alerts"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (perPage != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("per_page", perPage));
        }

        if (page != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("page", page));
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
    private okhttp3.Call listAllAlertsValidateBeforeCall(UUID checkId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling listAllAlerts(Async)");
        }

        return listAllAlertsCall(checkId, perPage, page, _callback);

    }


    private ApiResponse<UptimeListAllAlertsResponse> listAllAlertsWithHttpInfo(UUID checkId, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listAllAlertsValidateBeforeCall(checkId, perPage, page, null);
        Type localVarReturnType = new TypeToken<UptimeListAllAlertsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAllAlertsAsync(UUID checkId, Integer perPage, Integer page, final ApiCallback<UptimeListAllAlertsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAllAlertsValidateBeforeCall(checkId, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<UptimeListAllAlertsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAllAlertsRequestBuilder {
        private final UUID checkId;
        private Integer perPage;
        private Integer page;

        private ListAllAlertsRequestBuilder(UUID checkId) {
            this.checkId = checkId;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListAllAlertsRequestBuilder
         */
        public ListAllAlertsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListAllAlertsRequestBuilder
         */
        public ListAllAlertsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listAllAlerts
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alerts&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAllAlertsCall(checkId, perPage, page, _callback);
        }


        /**
         * Execute listAllAlerts request
         * @return UptimeListAllAlertsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alerts&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeListAllAlertsResponse execute() throws ApiException {
            ApiResponse<UptimeListAllAlertsResponse> localVarResp = listAllAlertsWithHttpInfo(checkId, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAllAlerts request with HTTP info returned
         * @return ApiResponse&lt;UptimeListAllAlertsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alerts&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeListAllAlertsResponse> executeWithHttpInfo() throws ApiException {
            return listAllAlertsWithHttpInfo(checkId, perPage, page);
        }

        /**
         * Execute listAllAlerts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alerts&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeListAllAlertsResponse> _callback) throws ApiException {
            return listAllAlertsAsync(checkId, perPage, page, _callback);
        }
    }

    /**
     * List All Alerts
     * To list all of the alerts for an Uptime check, send a GET request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts&#x60;.
     * @param checkId A unique identifier for a check. (required)
     * @return ListAllAlertsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alerts&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListAllAlertsRequestBuilder listAllAlerts(UUID checkId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        return new ListAllAlertsRequestBuilder(checkId);
    }
    private okhttp3.Call listChecksCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/uptime/checks";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (perPage != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("per_page", perPage));
        }

        if (page != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("page", page));
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
    private okhttp3.Call listChecksValidateBeforeCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        return listChecksCall(perPage, page, _callback);

    }


    private ApiResponse<UptimeListChecksResponse> listChecksWithHttpInfo(Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listChecksValidateBeforeCall(perPage, page, null);
        Type localVarReturnType = new TypeToken<UptimeListChecksResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listChecksAsync(Integer perPage, Integer page, final ApiCallback<UptimeListChecksResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listChecksValidateBeforeCall(perPage, page, _callback);
        Type localVarReturnType = new TypeToken<UptimeListChecksResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListChecksRequestBuilder {
        private Integer perPage;
        private Integer page;

        private ListChecksRequestBuilder() {
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListChecksRequestBuilder
         */
        public ListChecksRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListChecksRequestBuilder
         */
        public ListChecksRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listChecks
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;checks&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime check </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listChecksCall(perPage, page, _callback);
        }


        /**
         * Execute listChecks request
         * @return UptimeListChecksResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;checks&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime check </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeListChecksResponse execute() throws ApiException {
            ApiResponse<UptimeListChecksResponse> localVarResp = listChecksWithHttpInfo(perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listChecks request with HTTP info returned
         * @return ApiResponse&lt;UptimeListChecksResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;checks&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime check </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeListChecksResponse> executeWithHttpInfo() throws ApiException {
            return listChecksWithHttpInfo(perPage, page);
        }

        /**
         * Execute listChecks request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;checks&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime check </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeListChecksResponse> _callback) throws ApiException {
            return listChecksAsync(perPage, page, _callback);
        }
    }

    /**
     * List All Checks
     * To list all of the Uptime checks on your account, send a GET request to &#x60;/v2/uptime/checks&#x60;.
     * @return ListChecksRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;checks&#x60;. This will be set to an array of objects, each of which will contain the standard attributes associated with an uptime check </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListChecksRequestBuilder listChecks() throws IllegalArgumentException {
        return new ListChecksRequestBuilder();
    }
    private okhttp3.Call updateAlertSettingsCall(UUID checkId, UUID alertId, AlertUpdatable alertUpdatable, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = alertUpdatable;

        // create path and map variables
        String localVarPath = "/v2/uptime/checks/{check_id}/alerts/{alert_id}"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()))
            .replace("{" + "alert_id" + "}", localVarApiClient.escapeString(alertId.toString()));

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
    private okhttp3.Call updateAlertSettingsValidateBeforeCall(UUID checkId, UUID alertId, AlertUpdatable alertUpdatable, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling updateAlertSettings(Async)");
        }

        // verify the required parameter 'alertId' is set
        if (alertId == null) {
            throw new ApiException("Missing the required parameter 'alertId' when calling updateAlertSettings(Async)");
        }

        // verify the required parameter 'alertUpdatable' is set
        if (alertUpdatable == null) {
            throw new ApiException("Missing the required parameter 'alertUpdatable' when calling updateAlertSettings(Async)");
        }

        return updateAlertSettingsCall(checkId, alertId, alertUpdatable, _callback);

    }


    private ApiResponse<UptimeCreateNewAlertResponse> updateAlertSettingsWithHttpInfo(UUID checkId, UUID alertId, AlertUpdatable alertUpdatable) throws ApiException {
        okhttp3.Call localVarCall = updateAlertSettingsValidateBeforeCall(checkId, alertId, alertUpdatable, null);
        Type localVarReturnType = new TypeToken<UptimeCreateNewAlertResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAlertSettingsAsync(UUID checkId, UUID alertId, AlertUpdatable alertUpdatable, final ApiCallback<UptimeCreateNewAlertResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateAlertSettingsValidateBeforeCall(checkId, alertId, alertUpdatable, _callback);
        Type localVarReturnType = new TypeToken<UptimeCreateNewAlertResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateAlertSettingsRequestBuilder {
        private final UUID checkId;
        private final UUID alertId;
        private String name;
        private String type;
        private Integer threshold;
        private String comparison;
        private Notification notifications;
        private String period;

        private UpdateAlertSettingsRequestBuilder(UUID checkId, UUID alertId) {
            this.checkId = checkId;
            this.alertId = alertId;
        }

        /**
         * Set name
         * @param name A human-friendly display name. (optional)
         * @return UpdateAlertSettingsRequestBuilder
         */
        public UpdateAlertSettingsRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set type
         * @param type The type of alert. (optional)
         * @return UpdateAlertSettingsRequestBuilder
         */
        public UpdateAlertSettingsRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set threshold
         * @param threshold The threshold at which the alert will enter a trigger state. The specific threshold is dependent on the alert type. (optional)
         * @return UpdateAlertSettingsRequestBuilder
         */
        public UpdateAlertSettingsRequestBuilder threshold(Integer threshold) {
            this.threshold = threshold;
            return this;
        }
        
        /**
         * Set comparison
         * @param comparison The comparison operator used against the alert&#39;s threshold. (optional)
         * @return UpdateAlertSettingsRequestBuilder
         */
        public UpdateAlertSettingsRequestBuilder comparison(String comparison) {
            this.comparison = comparison;
            return this;
        }
        
        /**
         * Set notifications
         * @param notifications  (optional)
         * @return UpdateAlertSettingsRequestBuilder
         */
        public UpdateAlertSettingsRequestBuilder notifications(Notification notifications) {
            this.notifications = notifications;
            return this;
        }
        
        /**
         * Set period
         * @param period Period of time the threshold must be exceeded to trigger the alert. (optional)
         * @return UpdateAlertSettingsRequestBuilder
         */
        public UpdateAlertSettingsRequestBuilder period(String period) {
            this.period = period;
            return this;
        }
        
        /**
         * Build call for updateAlertSettings
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AlertUpdatable alertUpdatable = buildBodyParams();
            return updateAlertSettingsCall(checkId, alertId, alertUpdatable, _callback);
        }

        private AlertUpdatable buildBodyParams() {
            AlertUpdatable alertUpdatable = new AlertUpdatable();
            alertUpdatable.name(this.name);
            if (this.type != null)
            alertUpdatable.type(AlertUpdatable.TypeEnum.fromValue(this.type));
            alertUpdatable.threshold(this.threshold);
            if (this.comparison != null)
            alertUpdatable.comparison(AlertUpdatable.ComparisonEnum.fromValue(this.comparison));
            alertUpdatable.notifications(this.notifications);
            if (this.period != null)
            alertUpdatable.period(AlertUpdatable.PeriodEnum.fromValue(this.period));
            return alertUpdatable;
        }

        /**
         * Execute updateAlertSettings request
         * @return UptimeCreateNewAlertResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeCreateNewAlertResponse execute() throws ApiException {
            AlertUpdatable alertUpdatable = buildBodyParams();
            ApiResponse<UptimeCreateNewAlertResponse> localVarResp = updateAlertSettingsWithHttpInfo(checkId, alertId, alertUpdatable);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateAlertSettings request with HTTP info returned
         * @return ApiResponse&lt;UptimeCreateNewAlertResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeCreateNewAlertResponse> executeWithHttpInfo() throws ApiException {
            AlertUpdatable alertUpdatable = buildBodyParams();
            return updateAlertSettingsWithHttpInfo(checkId, alertId, alertUpdatable);
        }

        /**
         * Execute updateAlertSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeCreateNewAlertResponse> _callback) throws ApiException {
            AlertUpdatable alertUpdatable = buildBodyParams();
            return updateAlertSettingsAsync(checkId, alertId, alertUpdatable, _callback);
        }
    }

    /**
     * Update an Alert
     * To update the settings of an Uptime alert, send a PUT request to &#x60;/v2/uptime/checks/$CHECK_ID/alerts/$ALERT_ID&#x60;. 
     * @param checkId A unique identifier for a check. (required)
     * @param alertId A unique identifier for an alert. (required)
     * @param alertUpdatable  (required)
     * @return UpdateAlertSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;alert&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime alert. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateAlertSettingsRequestBuilder updateAlertSettings(UUID checkId, UUID alertId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        if (alertId == null) throw new IllegalArgumentException("\"alertId\" is required but got null");
            

        return new UpdateAlertSettingsRequestBuilder(checkId, alertId);
    }
    private okhttp3.Call updateCheckSettingsCall(UUID checkId, CheckUpdatable checkUpdatable, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = checkUpdatable;

        // create path and map variables
        String localVarPath = "/v2/uptime/checks/{check_id}"
            .replace("{" + "check_id" + "}", localVarApiClient.escapeString(checkId.toString()));

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
    private okhttp3.Call updateCheckSettingsValidateBeforeCall(UUID checkId, CheckUpdatable checkUpdatable, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'checkId' is set
        if (checkId == null) {
            throw new ApiException("Missing the required parameter 'checkId' when calling updateCheckSettings(Async)");
        }

        // verify the required parameter 'checkUpdatable' is set
        if (checkUpdatable == null) {
            throw new ApiException("Missing the required parameter 'checkUpdatable' when calling updateCheckSettings(Async)");
        }

        return updateCheckSettingsCall(checkId, checkUpdatable, _callback);

    }


    private ApiResponse<UptimeCreateCheckResponse> updateCheckSettingsWithHttpInfo(UUID checkId, CheckUpdatable checkUpdatable) throws ApiException {
        okhttp3.Call localVarCall = updateCheckSettingsValidateBeforeCall(checkId, checkUpdatable, null);
        Type localVarReturnType = new TypeToken<UptimeCreateCheckResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateCheckSettingsAsync(UUID checkId, CheckUpdatable checkUpdatable, final ApiCallback<UptimeCreateCheckResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateCheckSettingsValidateBeforeCall(checkId, checkUpdatable, _callback);
        Type localVarReturnType = new TypeToken<UptimeCreateCheckResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateCheckSettingsRequestBuilder {
        private final UUID checkId;
        private String name;
        private String type;
        private String target;
        private List<String> regions;
        private Boolean enabled;

        private UpdateCheckSettingsRequestBuilder(UUID checkId) {
            this.checkId = checkId;
        }

        /**
         * Set name
         * @param name A human-friendly display name. (optional)
         * @return UpdateCheckSettingsRequestBuilder
         */
        public UpdateCheckSettingsRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set type
         * @param type The type of health check to perform. (optional)
         * @return UpdateCheckSettingsRequestBuilder
         */
        public UpdateCheckSettingsRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set target
         * @param target The endpoint to perform healthchecks on. (optional)
         * @return UpdateCheckSettingsRequestBuilder
         */
        public UpdateCheckSettingsRequestBuilder target(String target) {
            this.target = target;
            return this;
        }
        
        /**
         * Set regions
         * @param regions An array containing the selected regions to perform healthchecks from. (optional)
         * @return UpdateCheckSettingsRequestBuilder
         */
        public UpdateCheckSettingsRequestBuilder regions(List<String> regions) {
            this.regions = regions;
            return this;
        }
        
        /**
         * Set enabled
         * @param enabled A boolean value indicating whether the check is enabled/disabled. (optional, default to true)
         * @return UpdateCheckSettingsRequestBuilder
         */
        public UpdateCheckSettingsRequestBuilder enabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }
        
        /**
         * Build call for updateCheckSettings
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            return updateCheckSettingsCall(checkId, checkUpdatable, _callback);
        }

        private CheckUpdatable buildBodyParams() {
            CheckUpdatable checkUpdatable = new CheckUpdatable();
            checkUpdatable.name(this.name);
            if (this.type != null)
            checkUpdatable.type(CheckUpdatable.TypeEnum.fromValue(this.type));
            checkUpdatable.target(this.target);
            if (this.regions != null)
            checkUpdatable.regions(CheckUpdatable.RegionsEnum.fromValue(this.regions));
            checkUpdatable.enabled(this.enabled);
            return checkUpdatable;
        }

        /**
         * Execute updateCheckSettings request
         * @return UptimeCreateCheckResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public UptimeCreateCheckResponse execute() throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            ApiResponse<UptimeCreateCheckResponse> localVarResp = updateCheckSettingsWithHttpInfo(checkId, checkUpdatable);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateCheckSettings request with HTTP info returned
         * @return ApiResponse&lt;UptimeCreateCheckResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<UptimeCreateCheckResponse> executeWithHttpInfo() throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            return updateCheckSettingsWithHttpInfo(checkId, checkUpdatable);
        }

        /**
         * Execute updateCheckSettings request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<UptimeCreateCheckResponse> _callback) throws ApiException {
            CheckUpdatable checkUpdatable = buildBodyParams();
            return updateCheckSettingsAsync(checkId, checkUpdatable, _callback);
        }
    }

    /**
     * Update a Check
     * To update the settings of an Uptime check, send a PUT request to &#x60;/v2/uptime/checks/$CHECK_ID&#x60;. 
     * @param checkId A unique identifier for a check. (required)
     * @param checkUpdatable  (required)
     * @return UpdateCheckSettingsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;check&#x60;. The value of this will be an object that contains the standard attributes associated with an uptime check. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateCheckSettingsRequestBuilder updateCheckSettings(UUID checkId) throws IllegalArgumentException {
        if (checkId == null) throw new IllegalArgumentException("\"checkId\" is required but got null");
            

        return new UpdateCheckSettingsRequestBuilder(checkId);
    }
}
