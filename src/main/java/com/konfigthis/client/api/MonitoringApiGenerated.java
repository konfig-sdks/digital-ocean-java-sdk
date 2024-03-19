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


import com.konfigthis.client.model.AlertPolicyRequest;
import com.konfigthis.client.model.Alerts;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.Metrics;
import com.konfigthis.client.model.MonitoringListAlertPoliciesResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class MonitoringApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public MonitoringApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public MonitoringApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call createAlertPolicyCall(AlertPolicyRequest alertPolicyRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = alertPolicyRequest;

        // create path and map variables
        String localVarPath = "/v2/monitoring/alerts";

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
    private okhttp3.Call createAlertPolicyValidateBeforeCall(AlertPolicyRequest alertPolicyRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'alertPolicyRequest' is set
        if (alertPolicyRequest == null) {
            throw new ApiException("Missing the required parameter 'alertPolicyRequest' when calling createAlertPolicy(Async)");
        }

        return createAlertPolicyCall(alertPolicyRequest, _callback);

    }


    private ApiResponse<Object> createAlertPolicyWithHttpInfo(AlertPolicyRequest alertPolicyRequest) throws ApiException {
        okhttp3.Call localVarCall = createAlertPolicyValidateBeforeCall(alertPolicyRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createAlertPolicyAsync(AlertPolicyRequest alertPolicyRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = createAlertPolicyValidateBeforeCall(alertPolicyRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateAlertPolicyRequestBuilder {
        private final List<String> tags;
        private final String description;
        private final Alerts alerts;
        private final String compare;
        private final Boolean enabled;
        private final List<String> entities;
        private final String type;
        private final Float value;
        private final String window;

        private CreateAlertPolicyRequestBuilder(List<String> tags, String description, Alerts alerts, String compare, Boolean enabled, List<String> entities, String type, Float value, String window) {
            this.tags = tags;
            this.description = description;
            this.alerts = alerts;
            this.compare = compare;
            this.enabled = enabled;
            this.entities = entities;
            this.type = type;
            this.value = value;
            this.window = window;
        }

        /**
         * Build call for createAlertPolicy
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            return createAlertPolicyCall(alertPolicyRequest, _callback);
        }

        private AlertPolicyRequest buildBodyParams() {
            AlertPolicyRequest alertPolicyRequest = new AlertPolicyRequest();
            alertPolicyRequest.tags(this.tags);
            alertPolicyRequest.description(this.description);
            alertPolicyRequest.alerts(this.alerts);
            if (this.compare != null)
            alertPolicyRequest.compare(AlertPolicyRequest.CompareEnum.fromValue(this.compare));
            alertPolicyRequest.enabled(this.enabled);
            alertPolicyRequest.entities(this.entities);
            if (this.type != null)
            alertPolicyRequest.type(AlertPolicyRequest.TypeEnum.fromValue(this.type));
            alertPolicyRequest.value(this.value);
            if (this.window != null)
            alertPolicyRequest.window(AlertPolicyRequest.WindowEnum.fromValue(this.window));
            return alertPolicyRequest;
        }

        /**
         * Execute createAlertPolicy request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = createAlertPolicyWithHttpInfo(alertPolicyRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createAlertPolicy request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            return createAlertPolicyWithHttpInfo(alertPolicyRequest);
        }

        /**
         * Execute createAlertPolicy request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            return createAlertPolicyAsync(alertPolicyRequest, _callback);
        }
    }

    /**
     * Create Alert Policy
     * To create a new alert, send a POST request to &#x60;/v2/monitoring/alerts&#x60;.
     * @param alertPolicyRequest The &#x60;type&#x60; field dictates what type of entity that the alert policy applies to and hence what type of entity is passed in the &#x60;entities&#x60; array. If both the &#x60;tags&#x60; array and &#x60;entities&#x60; array are empty the alert policy applies to all entities of the relevant type that are owned by the user account. Otherwise the following table shows the valid entity types for each type of alert policy:  Type | Description | Valid Entity Type -----|-------------|-------------------- &#x60;v1/insights/droplet/memory_utilization_percent&#x60; | alert on the percent of memory utilization | Droplet ID &#x60;v1/insights/droplet/disk_read&#x60; | alert on the rate of disk read I/O in MBps | Droplet ID &#x60;v1/insights/droplet/load_5&#x60; | alert on the 5 minute load average | Droplet ID &#x60;v1/insights/droplet/load_15&#x60; | alert on the 15 minute load average | Droplet ID &#x60;v1/insights/droplet/disk_utilization_percent&#x60; | alert on the percent of disk utilization | Droplet ID &#x60;v1/insights/droplet/cpu&#x60; | alert on the percent of CPU utilization | Droplet ID &#x60;v1/insights/droplet/disk_write&#x60; | alert on the rate of disk write I/O in MBps | Droplet ID &#x60;v1/insights/droplet/public_outbound_bandwidth&#x60; | alert on the rate of public outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/public_inbound_bandwidth&#x60; | alert on the rate of public inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_outbound_bandwidth&#x60; | alert on the rate of private outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_inbound_bandwidth&#x60; | alert on the rate of private inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/load_1&#x60; | alert on the 1 minute load average | Droplet ID &#x60;v1/insights/lbaas/avg_cpu_utilization_percent&#x60;|alert on the percent of CPU utilization|load balancer ID &#x60;v1/insights/lbaas/connection_utilization_percent&#x60;|alert on the percent of connection utilization|load balancer ID &#x60;v1/insights/lbaas/droplet_health&#x60;|alert on Droplet health status changes|load balancer ID &#x60;v1/insights/lbaas/tls_connections_per_second_utilization_percent&#x60;|alert on the percent of TLS connections per second utilization|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_5xx&#x60;|alert on the percent increase of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_4xx&#x60;|alert on the percent increase of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_5xx&#x60;|alert on the count of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_4xx&#x60;|alert on the count of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time&#x60;|alert on high average http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_50p&#x60;|alert on high 50th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_95p&#x60;|alert on high 95th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_99p&#x60;|alert on high 99th percentile http response time|load balancer ID &#x60;v1/dbaas/alerts/load_15_alerts&#x60; | alert on 15 minute load average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/memory_utilization_alerts&#x60; | alert on the percent memory utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/disk_utilization_alerts&#x60; | alert on the percent disk utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/cpu_alerts&#x60; | alert on the percent CPU usage average across the database cluster | database cluster UUID  (required)
     * @return CreateAlertPolicyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateAlertPolicyRequestBuilder createAlertPolicy(List<String> tags, String description, Alerts alerts, String compare, Boolean enabled, List<String> entities, String type, Float value, String window) throws IllegalArgumentException {
        if (tags == null) throw new IllegalArgumentException("\"tags\" is required but got null");
        if (description == null) throw new IllegalArgumentException("\"description\" is required but got null");
            

        if (alerts == null) throw new IllegalArgumentException("\"alerts\" is required but got null");
        if (compare == null) throw new IllegalArgumentException("\"compare\" is required but got null");
            

        if (enabled == null) throw new IllegalArgumentException("\"enabled\" is required but got null");
        if (entities == null) throw new IllegalArgumentException("\"entities\" is required but got null");
        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        if (value == null) throw new IllegalArgumentException("\"value\" is required but got null");
        if (window == null) throw new IllegalArgumentException("\"window\" is required but got null");
            

        return new CreateAlertPolicyRequestBuilder(tags, description, alerts, compare, enabled, entities, type, value, window);
    }
    private okhttp3.Call deleteAlertPolicyCall(String alertUuid, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/alerts/{alert_uuid}"
            .replace("{" + "alert_uuid" + "}", localVarApiClient.escapeString(alertUuid.toString()));

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
    private okhttp3.Call deleteAlertPolicyValidateBeforeCall(String alertUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'alertUuid' is set
        if (alertUuid == null) {
            throw new ApiException("Missing the required parameter 'alertUuid' when calling deleteAlertPolicy(Async)");
        }

        return deleteAlertPolicyCall(alertUuid, _callback);

    }


    private ApiResponse<Void> deleteAlertPolicyWithHttpInfo(String alertUuid) throws ApiException {
        okhttp3.Call localVarCall = deleteAlertPolicyValidateBeforeCall(alertUuid, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAlertPolicyAsync(String alertUuid, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteAlertPolicyValidateBeforeCall(alertUuid, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteAlertPolicyRequestBuilder {
        private final String alertUuid;

        private DeleteAlertPolicyRequestBuilder(String alertUuid) {
            this.alertUuid = alertUuid;
        }

        /**
         * Build call for deleteAlertPolicy
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
            return deleteAlertPolicyCall(alertUuid, _callback);
        }


        /**
         * Execute deleteAlertPolicy request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteAlertPolicyWithHttpInfo(alertUuid);
        }

        /**
         * Execute deleteAlertPolicy request with HTTP info returned
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
            return deleteAlertPolicyWithHttpInfo(alertUuid);
        }

        /**
         * Execute deleteAlertPolicy request (asynchronously)
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
            return deleteAlertPolicyAsync(alertUuid, _callback);
        }
    }

    /**
     * Delete an Alert Policy
     * To delete an alert policy, send a DELETE request to &#x60;/v2/monitoring/alerts/{alert_uuid}&#x60;
     * @param alertUuid A unique identifier for an alert policy. (required)
     * @return DeleteAlertPolicyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteAlertPolicyRequestBuilder deleteAlertPolicy(String alertUuid) throws IllegalArgumentException {
        if (alertUuid == null) throw new IllegalArgumentException("\"alertUuid\" is required but got null");
            

        return new DeleteAlertPolicyRequestBuilder(alertUuid);
    }
    private okhttp3.Call dropletCpuMetricsgetCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/cpu";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call dropletCpuMetricsgetValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling dropletCpuMetricsget(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling dropletCpuMetricsget(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling dropletCpuMetricsget(Async)");
        }

        return dropletCpuMetricsgetCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> dropletCpuMetricsgetWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = dropletCpuMetricsgetValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call dropletCpuMetricsgetAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = dropletCpuMetricsgetValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DropletCpuMetricsgetRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private DropletCpuMetricsgetRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for dropletCpuMetricsget
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return dropletCpuMetricsgetCall(hostId, start, end, _callback);
        }


        /**
         * Execute dropletCpuMetricsget request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = dropletCpuMetricsgetWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute dropletCpuMetricsget request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return dropletCpuMetricsgetWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute dropletCpuMetricsget request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return dropletCpuMetricsgetAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet CPU Metrics
     * To retrieve CPU metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/cpu&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return DropletCpuMetricsgetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DropletCpuMetricsgetRequestBuilder dropletCpuMetricsget(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new DropletCpuMetricsgetRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call dropletLoad5MetricsGetCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/load_5";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call dropletLoad5MetricsGetValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling dropletLoad5MetricsGet(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling dropletLoad5MetricsGet(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling dropletLoad5MetricsGet(Async)");
        }

        return dropletLoad5MetricsGetCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> dropletLoad5MetricsGetWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = dropletLoad5MetricsGetValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call dropletLoad5MetricsGetAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = dropletLoad5MetricsGetValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DropletLoad5MetricsGetRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private DropletLoad5MetricsGetRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for dropletLoad5MetricsGet
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return dropletLoad5MetricsGetCall(hostId, start, end, _callback);
        }


        /**
         * Execute dropletLoad5MetricsGet request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = dropletLoad5MetricsGetWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute dropletLoad5MetricsGet request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return dropletLoad5MetricsGetWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute dropletLoad5MetricsGet request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return dropletLoad5MetricsGetAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Load5 Metrics
     * To retrieve 5 minute load average metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/load_5&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return DropletLoad5MetricsGetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DropletLoad5MetricsGetRequestBuilder dropletLoad5MetricsGet(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new DropletLoad5MetricsGetRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call dropletMemoryCachedMetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/memory_cached";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call dropletMemoryCachedMetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling dropletMemoryCachedMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling dropletMemoryCachedMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling dropletMemoryCachedMetrics(Async)");
        }

        return dropletMemoryCachedMetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> dropletMemoryCachedMetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = dropletMemoryCachedMetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call dropletMemoryCachedMetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = dropletMemoryCachedMetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DropletMemoryCachedMetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private DropletMemoryCachedMetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for dropletMemoryCachedMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return dropletMemoryCachedMetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute dropletMemoryCachedMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = dropletMemoryCachedMetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute dropletMemoryCachedMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return dropletMemoryCachedMetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute dropletMemoryCachedMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return dropletMemoryCachedMetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Cached Memory Metrics
     * To retrieve cached memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_cached&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return DropletMemoryCachedMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DropletMemoryCachedMetricsRequestBuilder dropletMemoryCachedMetrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new DropletMemoryCachedMetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getAppCpuPercentageMetricsCall(String appId, String start, String end, String appComponent, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/apps/cpu_percentage";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (appId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("app_id", appId));
        }

        if (appComponent != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("app_component", appComponent));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getAppCpuPercentageMetricsValidateBeforeCall(String appId, String start, String end, String appComponent, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getAppCpuPercentageMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getAppCpuPercentageMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getAppCpuPercentageMetrics(Async)");
        }

        return getAppCpuPercentageMetricsCall(appId, start, end, appComponent, _callback);

    }


    private ApiResponse<Metrics> getAppCpuPercentageMetricsWithHttpInfo(String appId, String start, String end, String appComponent) throws ApiException {
        okhttp3.Call localVarCall = getAppCpuPercentageMetricsValidateBeforeCall(appId, start, end, appComponent, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAppCpuPercentageMetricsAsync(String appId, String start, String end, String appComponent, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getAppCpuPercentageMetricsValidateBeforeCall(appId, start, end, appComponent, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetAppCpuPercentageMetricsRequestBuilder {
        private final String appId;
        private final String start;
        private final String end;
        private String appComponent;

        private GetAppCpuPercentageMetricsRequestBuilder(String appId, String start, String end) {
            this.appId = appId;
            this.start = start;
            this.end = end;
        }

        /**
         * Set appComponent
         * @param appComponent The app component name. (optional)
         * @return GetAppCpuPercentageMetricsRequestBuilder
         */
        public GetAppCpuPercentageMetricsRequestBuilder appComponent(String appComponent) {
            this.appComponent = appComponent;
            return this;
        }
        
        /**
         * Build call for getAppCpuPercentageMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getAppCpuPercentageMetricsCall(appId, start, end, appComponent, _callback);
        }


        /**
         * Execute getAppCpuPercentageMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getAppCpuPercentageMetricsWithHttpInfo(appId, start, end, appComponent);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getAppCpuPercentageMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getAppCpuPercentageMetricsWithHttpInfo(appId, start, end, appComponent);
        }

        /**
         * Execute getAppCpuPercentageMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getAppCpuPercentageMetricsAsync(appId, start, end, appComponent, _callback);
        }
    }

    /**
     * Get App CPU Percentage Metrics
     * To retrieve cpu percentage metrics for a given app, send a GET request to &#x60;/v2/monitoring/metrics/apps/cpu_percentage&#x60;.
     * @param appId The app UUID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetAppCpuPercentageMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetAppCpuPercentageMetricsRequestBuilder getAppCpuPercentageMetrics(String appId, String start, String end) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetAppCpuPercentageMetricsRequestBuilder(appId, start, end);
    }
    private okhttp3.Call getAppMemoryPercentageMetricsCall(String appId, String start, String end, String appComponent, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/apps/memory_percentage";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (appId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("app_id", appId));
        }

        if (appComponent != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("app_component", appComponent));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getAppMemoryPercentageMetricsValidateBeforeCall(String appId, String start, String end, String appComponent, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getAppMemoryPercentageMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getAppMemoryPercentageMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getAppMemoryPercentageMetrics(Async)");
        }

        return getAppMemoryPercentageMetricsCall(appId, start, end, appComponent, _callback);

    }


    private ApiResponse<Metrics> getAppMemoryPercentageMetricsWithHttpInfo(String appId, String start, String end, String appComponent) throws ApiException {
        okhttp3.Call localVarCall = getAppMemoryPercentageMetricsValidateBeforeCall(appId, start, end, appComponent, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAppMemoryPercentageMetricsAsync(String appId, String start, String end, String appComponent, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getAppMemoryPercentageMetricsValidateBeforeCall(appId, start, end, appComponent, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetAppMemoryPercentageMetricsRequestBuilder {
        private final String appId;
        private final String start;
        private final String end;
        private String appComponent;

        private GetAppMemoryPercentageMetricsRequestBuilder(String appId, String start, String end) {
            this.appId = appId;
            this.start = start;
            this.end = end;
        }

        /**
         * Set appComponent
         * @param appComponent The app component name. (optional)
         * @return GetAppMemoryPercentageMetricsRequestBuilder
         */
        public GetAppMemoryPercentageMetricsRequestBuilder appComponent(String appComponent) {
            this.appComponent = appComponent;
            return this;
        }
        
        /**
         * Build call for getAppMemoryPercentageMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getAppMemoryPercentageMetricsCall(appId, start, end, appComponent, _callback);
        }


        /**
         * Execute getAppMemoryPercentageMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getAppMemoryPercentageMetricsWithHttpInfo(appId, start, end, appComponent);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getAppMemoryPercentageMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getAppMemoryPercentageMetricsWithHttpInfo(appId, start, end, appComponent);
        }

        /**
         * Execute getAppMemoryPercentageMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getAppMemoryPercentageMetricsAsync(appId, start, end, appComponent, _callback);
        }
    }

    /**
     * Get App Memory Percentage Metrics
     * To retrieve memory percentage metrics for a given app, send a GET request to &#x60;/v2/monitoring/metrics/apps/memory_percentage&#x60;.
     * @param appId The app UUID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetAppMemoryPercentageMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetAppMemoryPercentageMetricsRequestBuilder getAppMemoryPercentageMetrics(String appId, String start, String end) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetAppMemoryPercentageMetricsRequestBuilder(appId, start, end);
    }
    private okhttp3.Call getAppRestartCountMetricsCall(String appId, String start, String end, String appComponent, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/apps/restart_count";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (appId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("app_id", appId));
        }

        if (appComponent != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("app_component", appComponent));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getAppRestartCountMetricsValidateBeforeCall(String appId, String start, String end, String appComponent, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getAppRestartCountMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getAppRestartCountMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getAppRestartCountMetrics(Async)");
        }

        return getAppRestartCountMetricsCall(appId, start, end, appComponent, _callback);

    }


    private ApiResponse<Metrics> getAppRestartCountMetricsWithHttpInfo(String appId, String start, String end, String appComponent) throws ApiException {
        okhttp3.Call localVarCall = getAppRestartCountMetricsValidateBeforeCall(appId, start, end, appComponent, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAppRestartCountMetricsAsync(String appId, String start, String end, String appComponent, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getAppRestartCountMetricsValidateBeforeCall(appId, start, end, appComponent, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetAppRestartCountMetricsRequestBuilder {
        private final String appId;
        private final String start;
        private final String end;
        private String appComponent;

        private GetAppRestartCountMetricsRequestBuilder(String appId, String start, String end) {
            this.appId = appId;
            this.start = start;
            this.end = end;
        }

        /**
         * Set appComponent
         * @param appComponent The app component name. (optional)
         * @return GetAppRestartCountMetricsRequestBuilder
         */
        public GetAppRestartCountMetricsRequestBuilder appComponent(String appComponent) {
            this.appComponent = appComponent;
            return this;
        }
        
        /**
         * Build call for getAppRestartCountMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getAppRestartCountMetricsCall(appId, start, end, appComponent, _callback);
        }


        /**
         * Execute getAppRestartCountMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getAppRestartCountMetricsWithHttpInfo(appId, start, end, appComponent);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getAppRestartCountMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getAppRestartCountMetricsWithHttpInfo(appId, start, end, appComponent);
        }

        /**
         * Execute getAppRestartCountMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getAppRestartCountMetricsAsync(appId, start, end, appComponent, _callback);
        }
    }

    /**
     * Get App Restart Count Metrics
     * To retrieve restart count metrics for a given app, send a GET request to &#x60;/v2/monitoring/metrics/apps/restart_count&#x60;.
     * @param appId The app UUID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetAppRestartCountMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetAppRestartCountMetricsRequestBuilder getAppRestartCountMetrics(String appId, String start, String end) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetAppRestartCountMetricsRequestBuilder(appId, start, end);
    }
    private okhttp3.Call getDropletBandwidthMetricsCall(String hostId, String _interface, String direction, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/bandwidth";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (_interface != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("interface", _interface));
        }

        if (direction != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("direction", direction));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletBandwidthMetricsValidateBeforeCall(String hostId, String _interface, String direction, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletBandwidthMetrics(Async)");
        }

        // verify the required parameter '_interface' is set
        if (_interface == null) {
            throw new ApiException("Missing the required parameter '_interface' when calling getDropletBandwidthMetrics(Async)");
        }

        // verify the required parameter 'direction' is set
        if (direction == null) {
            throw new ApiException("Missing the required parameter 'direction' when calling getDropletBandwidthMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletBandwidthMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletBandwidthMetrics(Async)");
        }

        return getDropletBandwidthMetricsCall(hostId, _interface, direction, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletBandwidthMetricsWithHttpInfo(String hostId, String _interface, String direction, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletBandwidthMetricsValidateBeforeCall(hostId, _interface, direction, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletBandwidthMetricsAsync(String hostId, String _interface, String direction, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletBandwidthMetricsValidateBeforeCall(hostId, _interface, direction, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletBandwidthMetricsRequestBuilder {
        private final String hostId;
        private final String _interface;
        private final String direction;
        private final String start;
        private final String end;

        private GetDropletBandwidthMetricsRequestBuilder(String hostId, String _interface, String direction, String start, String end) {
            this.hostId = hostId;
            this._interface = _interface;
            this.direction = direction;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletBandwidthMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletBandwidthMetricsCall(hostId, _interface, direction, start, end, _callback);
        }


        /**
         * Execute getDropletBandwidthMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletBandwidthMetricsWithHttpInfo(hostId, _interface, direction, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletBandwidthMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletBandwidthMetricsWithHttpInfo(hostId, _interface, direction, start, end);
        }

        /**
         * Execute getDropletBandwidthMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletBandwidthMetricsAsync(hostId, _interface, direction, start, end, _callback);
        }
    }

    /**
     * Get Droplet Bandwidth Metrics
     * To retrieve bandwidth metrics for a given Droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/bandwidth&#x60;. Use the &#x60;interface&#x60; query parameter to specify if the results should be for the &#x60;private&#x60; or &#x60;public&#x60; interface. Use the &#x60;direction&#x60; query parameter to specify if the results should be for &#x60;inbound&#x60; or &#x60;outbound&#x60; traffic.
     * @param hostId The droplet ID. (required)
     * @param _interface The network interface. (required)
     * @param direction The traffic direction. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletBandwidthMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletBandwidthMetricsRequestBuilder getDropletBandwidthMetrics(String hostId, String _interface, String direction, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (_interface == null) throw new IllegalArgumentException("\"_interface\" is required but got null");
            

        if (direction == null) throw new IllegalArgumentException("\"direction\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletBandwidthMetricsRequestBuilder(hostId, _interface, direction, start, end);
    }
    private okhttp3.Call getDropletFilesystemFreeMetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/filesystem_free";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletFilesystemFreeMetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletFilesystemFreeMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletFilesystemFreeMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletFilesystemFreeMetrics(Async)");
        }

        return getDropletFilesystemFreeMetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletFilesystemFreeMetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletFilesystemFreeMetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletFilesystemFreeMetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletFilesystemFreeMetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletFilesystemFreeMetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private GetDropletFilesystemFreeMetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletFilesystemFreeMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletFilesystemFreeMetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute getDropletFilesystemFreeMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletFilesystemFreeMetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletFilesystemFreeMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletFilesystemFreeMetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute getDropletFilesystemFreeMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletFilesystemFreeMetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Filesystem Free Metrics
     * To retrieve filesystem free metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/filesystem_free&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletFilesystemFreeMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletFilesystemFreeMetricsRequestBuilder getDropletFilesystemFreeMetrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletFilesystemFreeMetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getDropletFilesystemSizeMetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/filesystem_size";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletFilesystemSizeMetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletFilesystemSizeMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletFilesystemSizeMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletFilesystemSizeMetrics(Async)");
        }

        return getDropletFilesystemSizeMetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletFilesystemSizeMetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletFilesystemSizeMetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletFilesystemSizeMetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletFilesystemSizeMetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletFilesystemSizeMetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private GetDropletFilesystemSizeMetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletFilesystemSizeMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletFilesystemSizeMetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute getDropletFilesystemSizeMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletFilesystemSizeMetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletFilesystemSizeMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletFilesystemSizeMetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute getDropletFilesystemSizeMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletFilesystemSizeMetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Filesystem Size Metrics
     * To retrieve filesystem size metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/filesystem_size&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletFilesystemSizeMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletFilesystemSizeMetricsRequestBuilder getDropletFilesystemSizeMetrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletFilesystemSizeMetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getDropletLoad15MetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/load_15";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletLoad15MetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletLoad15Metrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletLoad15Metrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletLoad15Metrics(Async)");
        }

        return getDropletLoad15MetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletLoad15MetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletLoad15MetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletLoad15MetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletLoad15MetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletLoad15MetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private GetDropletLoad15MetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletLoad15Metrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletLoad15MetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute getDropletLoad15Metrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletLoad15MetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletLoad15Metrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletLoad15MetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute getDropletLoad15Metrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletLoad15MetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Load15 Metrics
     * To retrieve 15 minute load average metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/load_15&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletLoad15MetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletLoad15MetricsRequestBuilder getDropletLoad15Metrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletLoad15MetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getDropletLoad1MetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/load_1";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletLoad1MetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletLoad1Metrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletLoad1Metrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletLoad1Metrics(Async)");
        }

        return getDropletLoad1MetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletLoad1MetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletLoad1MetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletLoad1MetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletLoad1MetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletLoad1MetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private GetDropletLoad1MetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletLoad1Metrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletLoad1MetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute getDropletLoad1Metrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletLoad1MetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletLoad1Metrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletLoad1MetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute getDropletLoad1Metrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletLoad1MetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Load1 Metrics
     * To retrieve 1 minute load average metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/load_1&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletLoad1MetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletLoad1MetricsRequestBuilder getDropletLoad1Metrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletLoad1MetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getDropletMemoryAvailableMetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/memory_available";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletMemoryAvailableMetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletMemoryAvailableMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletMemoryAvailableMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletMemoryAvailableMetrics(Async)");
        }

        return getDropletMemoryAvailableMetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletMemoryAvailableMetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletMemoryAvailableMetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletMemoryAvailableMetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletMemoryAvailableMetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletMemoryAvailableMetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private GetDropletMemoryAvailableMetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletMemoryAvailableMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletMemoryAvailableMetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute getDropletMemoryAvailableMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletMemoryAvailableMetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletMemoryAvailableMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletMemoryAvailableMetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute getDropletMemoryAvailableMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletMemoryAvailableMetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Available Memory Metrics
     * To retrieve available memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_available&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletMemoryAvailableMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletMemoryAvailableMetricsRequestBuilder getDropletMemoryAvailableMetrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletMemoryAvailableMetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getDropletMemoryFreeMetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/memory_free";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletMemoryFreeMetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletMemoryFreeMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletMemoryFreeMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletMemoryFreeMetrics(Async)");
        }

        return getDropletMemoryFreeMetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletMemoryFreeMetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletMemoryFreeMetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletMemoryFreeMetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletMemoryFreeMetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletMemoryFreeMetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private GetDropletMemoryFreeMetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletMemoryFreeMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletMemoryFreeMetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute getDropletMemoryFreeMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletMemoryFreeMetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletMemoryFreeMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletMemoryFreeMetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute getDropletMemoryFreeMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletMemoryFreeMetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Free Memory Metrics
     * To retrieve free memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_free&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletMemoryFreeMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletMemoryFreeMetricsRequestBuilder getDropletMemoryFreeMetrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletMemoryFreeMetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getDropletMemoryTotalMetricsCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/metrics/droplet/memory_total";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (hostId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("host_id", hostId));
        }

        if (start != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("start", start));
        }

        if (end != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("end", end));
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
    private okhttp3.Call getDropletMemoryTotalMetricsValidateBeforeCall(String hostId, String start, String end, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'hostId' is set
        if (hostId == null) {
            throw new ApiException("Missing the required parameter 'hostId' when calling getDropletMemoryTotalMetrics(Async)");
        }

        // verify the required parameter 'start' is set
        if (start == null) {
            throw new ApiException("Missing the required parameter 'start' when calling getDropletMemoryTotalMetrics(Async)");
        }

        // verify the required parameter 'end' is set
        if (end == null) {
            throw new ApiException("Missing the required parameter 'end' when calling getDropletMemoryTotalMetrics(Async)");
        }

        return getDropletMemoryTotalMetricsCall(hostId, start, end, _callback);

    }


    private ApiResponse<Metrics> getDropletMemoryTotalMetricsWithHttpInfo(String hostId, String start, String end) throws ApiException {
        okhttp3.Call localVarCall = getDropletMemoryTotalMetricsValidateBeforeCall(hostId, start, end, null);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDropletMemoryTotalMetricsAsync(String hostId, String start, String end, final ApiCallback<Metrics> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDropletMemoryTotalMetricsValidateBeforeCall(hostId, start, end, _callback);
        Type localVarReturnType = new TypeToken<Metrics>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDropletMemoryTotalMetricsRequestBuilder {
        private final String hostId;
        private final String start;
        private final String end;

        private GetDropletMemoryTotalMetricsRequestBuilder(String hostId, String start, String end) {
            this.hostId = hostId;
            this.start = start;
            this.end = end;
        }

        /**
         * Build call for getDropletMemoryTotalMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDropletMemoryTotalMetricsCall(hostId, start, end, _callback);
        }


        /**
         * Execute getDropletMemoryTotalMetrics request
         * @return Metrics
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Metrics execute() throws ApiException {
            ApiResponse<Metrics> localVarResp = getDropletMemoryTotalMetricsWithHttpInfo(hostId, start, end);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDropletMemoryTotalMetrics request with HTTP info returned
         * @return ApiResponse&lt;Metrics&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Metrics> executeWithHttpInfo() throws ApiException {
            return getDropletMemoryTotalMetricsWithHttpInfo(hostId, start, end);
        }

        /**
         * Execute getDropletMemoryTotalMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Metrics> _callback) throws ApiException {
            return getDropletMemoryTotalMetricsAsync(hostId, start, end, _callback);
        }
    }

    /**
     * Get Droplet Total Memory Metrics
     * To retrieve total memory metrics for a given droplet, send a GET request to &#x60;/v2/monitoring/metrics/droplet/memory_total&#x60;.
     * @param hostId The droplet ID. (required)
     * @param start Timestamp to start metric window. (required)
     * @param end Timestamp to end metric window. (required)
     * @return GetDropletMemoryTotalMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;data&#x60; and &#x60;status&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDropletMemoryTotalMetricsRequestBuilder getDropletMemoryTotalMetrics(String hostId, String start, String end) throws IllegalArgumentException {
        if (hostId == null) throw new IllegalArgumentException("\"hostId\" is required but got null");
            

        if (start == null) throw new IllegalArgumentException("\"start\" is required but got null");
            

        if (end == null) throw new IllegalArgumentException("\"end\" is required but got null");
            

        return new GetDropletMemoryTotalMetricsRequestBuilder(hostId, start, end);
    }
    private okhttp3.Call getExistingAlertPolicyCall(String alertUuid, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/alerts/{alert_uuid}"
            .replace("{" + "alert_uuid" + "}", localVarApiClient.escapeString(alertUuid.toString()));

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
    private okhttp3.Call getExistingAlertPolicyValidateBeforeCall(String alertUuid, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'alertUuid' is set
        if (alertUuid == null) {
            throw new ApiException("Missing the required parameter 'alertUuid' when calling getExistingAlertPolicy(Async)");
        }

        return getExistingAlertPolicyCall(alertUuid, _callback);

    }


    private ApiResponse<Object> getExistingAlertPolicyWithHttpInfo(String alertUuid) throws ApiException {
        okhttp3.Call localVarCall = getExistingAlertPolicyValidateBeforeCall(alertUuid, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getExistingAlertPolicyAsync(String alertUuid, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = getExistingAlertPolicyValidateBeforeCall(alertUuid, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetExistingAlertPolicyRequestBuilder {
        private final String alertUuid;

        private GetExistingAlertPolicyRequestBuilder(String alertUuid) {
            this.alertUuid = alertUuid;
        }

        /**
         * Build call for getExistingAlertPolicy
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getExistingAlertPolicyCall(alertUuid, _callback);
        }


        /**
         * Execute getExistingAlertPolicy request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ApiResponse<Object> localVarResp = getExistingAlertPolicyWithHttpInfo(alertUuid);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getExistingAlertPolicy request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            return getExistingAlertPolicyWithHttpInfo(alertUuid);
        }

        /**
         * Execute getExistingAlertPolicy request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            return getExistingAlertPolicyAsync(alertUuid, _callback);
        }
    }

    /**
     * Retrieve an Existing Alert Policy
     * To retrieve a given alert policy, send a GET request to &#x60;/v2/monitoring/alerts/{alert_uuid}&#x60;
     * @param alertUuid A unique identifier for an alert policy. (required)
     * @return GetExistingAlertPolicyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetExistingAlertPolicyRequestBuilder getExistingAlertPolicy(String alertUuid) throws IllegalArgumentException {
        if (alertUuid == null) throw new IllegalArgumentException("\"alertUuid\" is required but got null");
            

        return new GetExistingAlertPolicyRequestBuilder(alertUuid);
    }
    private okhttp3.Call listAlertPoliciesCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/monitoring/alerts";

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
    private okhttp3.Call listAlertPoliciesValidateBeforeCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        return listAlertPoliciesCall(perPage, page, _callback);

    }


    private ApiResponse<MonitoringListAlertPoliciesResponse> listAlertPoliciesWithHttpInfo(Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listAlertPoliciesValidateBeforeCall(perPage, page, null);
        Type localVarReturnType = new TypeToken<MonitoringListAlertPoliciesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAlertPoliciesAsync(Integer perPage, Integer page, final ApiCallback<MonitoringListAlertPoliciesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAlertPoliciesValidateBeforeCall(perPage, page, _callback);
        Type localVarReturnType = new TypeToken<MonitoringListAlertPoliciesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAlertPoliciesRequestBuilder {
        private Integer perPage;
        private Integer page;

        private ListAlertPoliciesRequestBuilder() {
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListAlertPoliciesRequestBuilder
         */
        public ListAlertPoliciesRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListAlertPoliciesRequestBuilder
         */
        public ListAlertPoliciesRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listAlertPolicies
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A list of alert policies. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAlertPoliciesCall(perPage, page, _callback);
        }


        /**
         * Execute listAlertPolicies request
         * @return MonitoringListAlertPoliciesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A list of alert policies. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public MonitoringListAlertPoliciesResponse execute() throws ApiException {
            ApiResponse<MonitoringListAlertPoliciesResponse> localVarResp = listAlertPoliciesWithHttpInfo(perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAlertPolicies request with HTTP info returned
         * @return ApiResponse&lt;MonitoringListAlertPoliciesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A list of alert policies. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<MonitoringListAlertPoliciesResponse> executeWithHttpInfo() throws ApiException {
            return listAlertPoliciesWithHttpInfo(perPage, page);
        }

        /**
         * Execute listAlertPolicies request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A list of alert policies. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<MonitoringListAlertPoliciesResponse> _callback) throws ApiException {
            return listAlertPoliciesAsync(perPage, page, _callback);
        }
    }

    /**
     * List Alert Policies
     * Returns all alert policies that are configured for the given account. To List all alert policies, send a GET request to &#x60;/v2/monitoring/alerts&#x60;.
     * @return ListAlertPoliciesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A list of alert policies. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListAlertPoliciesRequestBuilder listAlertPolicies() throws IllegalArgumentException {
        return new ListAlertPoliciesRequestBuilder();
    }
    private okhttp3.Call updateAlertPolicyCall(String alertUuid, AlertPolicyRequest alertPolicyRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = alertPolicyRequest;

        // create path and map variables
        String localVarPath = "/v2/monitoring/alerts/{alert_uuid}"
            .replace("{" + "alert_uuid" + "}", localVarApiClient.escapeString(alertUuid.toString()));

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
    private okhttp3.Call updateAlertPolicyValidateBeforeCall(String alertUuid, AlertPolicyRequest alertPolicyRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'alertUuid' is set
        if (alertUuid == null) {
            throw new ApiException("Missing the required parameter 'alertUuid' when calling updateAlertPolicy(Async)");
        }

        // verify the required parameter 'alertPolicyRequest' is set
        if (alertPolicyRequest == null) {
            throw new ApiException("Missing the required parameter 'alertPolicyRequest' when calling updateAlertPolicy(Async)");
        }

        return updateAlertPolicyCall(alertUuid, alertPolicyRequest, _callback);

    }


    private ApiResponse<Object> updateAlertPolicyWithHttpInfo(String alertUuid, AlertPolicyRequest alertPolicyRequest) throws ApiException {
        okhttp3.Call localVarCall = updateAlertPolicyValidateBeforeCall(alertUuid, alertPolicyRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAlertPolicyAsync(String alertUuid, AlertPolicyRequest alertPolicyRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateAlertPolicyValidateBeforeCall(alertUuid, alertPolicyRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateAlertPolicyRequestBuilder {
        private final List<String> tags;
        private final String description;
        private final Alerts alerts;
        private final String compare;
        private final Boolean enabled;
        private final List<String> entities;
        private final String type;
        private final Float value;
        private final String window;
        private final String alertUuid;

        private UpdateAlertPolicyRequestBuilder(List<String> tags, String description, Alerts alerts, String compare, Boolean enabled, List<String> entities, String type, Float value, String window, String alertUuid) {
            this.tags = tags;
            this.description = description;
            this.alerts = alerts;
            this.compare = compare;
            this.enabled = enabled;
            this.entities = entities;
            this.type = type;
            this.value = value;
            this.window = window;
            this.alertUuid = alertUuid;
        }

        /**
         * Build call for updateAlertPolicy
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            return updateAlertPolicyCall(alertUuid, alertPolicyRequest, _callback);
        }

        private AlertPolicyRequest buildBodyParams() {
            AlertPolicyRequest alertPolicyRequest = new AlertPolicyRequest();
            alertPolicyRequest.tags(this.tags);
            alertPolicyRequest.description(this.description);
            alertPolicyRequest.alerts(this.alerts);
            if (this.compare != null)
            alertPolicyRequest.compare(AlertPolicyRequest.CompareEnum.fromValue(this.compare));
            alertPolicyRequest.enabled(this.enabled);
            alertPolicyRequest.entities(this.entities);
            if (this.type != null)
            alertPolicyRequest.type(AlertPolicyRequest.TypeEnum.fromValue(this.type));
            alertPolicyRequest.value(this.value);
            if (this.window != null)
            alertPolicyRequest.window(AlertPolicyRequest.WindowEnum.fromValue(this.window));
            return alertPolicyRequest;
        }

        /**
         * Execute updateAlertPolicy request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = updateAlertPolicyWithHttpInfo(alertUuid, alertPolicyRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateAlertPolicy request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            return updateAlertPolicyWithHttpInfo(alertUuid, alertPolicyRequest);
        }

        /**
         * Execute updateAlertPolicy request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            AlertPolicyRequest alertPolicyRequest = buildBodyParams();
            return updateAlertPolicyAsync(alertUuid, alertPolicyRequest, _callback);
        }
    }

    /**
     * Update an Alert Policy
     * To update en existing policy, send a PUT request to &#x60;v2/monitoring/alerts/{alert_uuid}&#x60;.
     * @param alertUuid A unique identifier for an alert policy. (required)
     * @param alertPolicyRequest The &#x60;type&#x60; field dictates what type of entity that the alert policy applies to and hence what type of entity is passed in the &#x60;entities&#x60; array. If both the &#x60;tags&#x60; array and &#x60;entities&#x60; array are empty the alert policy applies to all entities of the relevant type that are owned by the user account. Otherwise the following table shows the valid entity types for each type of alert policy:  Type | Description | Valid Entity Type -----|-------------|-------------------- &#x60;v1/insights/droplet/memory_utilization_percent&#x60; | alert on the percent of memory utilization | Droplet ID &#x60;v1/insights/droplet/disk_read&#x60; | alert on the rate of disk read I/O in MBps | Droplet ID &#x60;v1/insights/droplet/load_5&#x60; | alert on the 5 minute load average | Droplet ID &#x60;v1/insights/droplet/load_15&#x60; | alert on the 15 minute load average | Droplet ID &#x60;v1/insights/droplet/disk_utilization_percent&#x60; | alert on the percent of disk utilization | Droplet ID &#x60;v1/insights/droplet/cpu&#x60; | alert on the percent of CPU utilization | Droplet ID &#x60;v1/insights/droplet/disk_write&#x60; | alert on the rate of disk write I/O in MBps | Droplet ID &#x60;v1/insights/droplet/public_outbound_bandwidth&#x60; | alert on the rate of public outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/public_inbound_bandwidth&#x60; | alert on the rate of public inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_outbound_bandwidth&#x60; | alert on the rate of private outbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/private_inbound_bandwidth&#x60; | alert on the rate of private inbound bandwidth in Mbps | Droplet ID &#x60;v1/insights/droplet/load_1&#x60; | alert on the 1 minute load average | Droplet ID &#x60;v1/insights/lbaas/avg_cpu_utilization_percent&#x60;|alert on the percent of CPU utilization|load balancer ID &#x60;v1/insights/lbaas/connection_utilization_percent&#x60;|alert on the percent of connection utilization|load balancer ID &#x60;v1/insights/lbaas/droplet_health&#x60;|alert on Droplet health status changes|load balancer ID &#x60;v1/insights/lbaas/tls_connections_per_second_utilization_percent&#x60;|alert on the percent of TLS connections per second utilization|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_5xx&#x60;|alert on the percent increase of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_percentage_4xx&#x60;|alert on the percent increase of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_5xx&#x60;|alert on the count of 5xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/increase_in_http_error_rate_count_4xx&#x60;|alert on the count of 4xx level http errors over 5m|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time&#x60;|alert on high average http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_50p&#x60;|alert on high 50th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_95p&#x60;|alert on high 95th percentile http response time|load balancer ID &#x60;v1/insights/lbaas/high_http_request_response_time_99p&#x60;|alert on high 99th percentile http response time|load balancer ID &#x60;v1/dbaas/alerts/load_15_alerts&#x60; | alert on 15 minute load average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/memory_utilization_alerts&#x60; | alert on the percent memory utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/disk_utilization_alerts&#x60; | alert on the percent disk utilization average across the database cluster | database cluster UUID &#x60;v1/dbaas/alerts/cpu_alerts&#x60; | alert on the percent CPU usage average across the database cluster | database cluster UUID  (required)
     * @return UpdateAlertPolicyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> An alert policy. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateAlertPolicyRequestBuilder updateAlertPolicy(List<String> tags, String description, Alerts alerts, String compare, Boolean enabled, List<String> entities, String type, Float value, String window, String alertUuid) throws IllegalArgumentException {
        if (tags == null) throw new IllegalArgumentException("\"tags\" is required but got null");
        if (description == null) throw new IllegalArgumentException("\"description\" is required but got null");
            

        if (alerts == null) throw new IllegalArgumentException("\"alerts\" is required but got null");
        if (compare == null) throw new IllegalArgumentException("\"compare\" is required but got null");
            

        if (enabled == null) throw new IllegalArgumentException("\"enabled\" is required but got null");
        if (entities == null) throw new IllegalArgumentException("\"entities\" is required but got null");
        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        if (value == null) throw new IllegalArgumentException("\"value\" is required but got null");
        if (window == null) throw new IllegalArgumentException("\"window\" is required but got null");
            

        if (alertUuid == null) throw new IllegalArgumentException("\"alertUuid\" is required but got null");
            

        return new UpdateAlertPolicyRequestBuilder(tags, description, alerts, compare, enabled, entities, type, value, window, alertUuid);
    }
}
