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


import com.konfigthis.client.model.AssociatedResourceStatus;
import com.konfigthis.client.model.DropletsGetResponse;
import com.konfigthis.client.model.DropletsListAssociatedResourcesResponse;
import com.konfigthis.client.model.DropletsListBackupsResponse;
import com.konfigthis.client.model.DropletsListFirewallsResponse;
import com.konfigthis.client.model.DropletsListKernelsResponse;
import com.konfigthis.client.model.DropletsListNeighborsResponse;
import com.konfigthis.client.model.DropletsListResponse;
import com.konfigthis.client.model.DropletsListSnapshotsResponse;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.NeighborIds;
import com.konfigthis.client.model.SelectiveDestroyAssociatedResource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class DropletsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public DropletsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public DropletsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call checkDestroyStatusCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/destroy_with_associated_resources/status"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call checkDestroyStatusValidateBeforeCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling checkDestroyStatus(Async)");
        }

        return checkDestroyStatusCall(dropletId, _callback);

    }


    private ApiResponse<AssociatedResourceStatus> checkDestroyStatusWithHttpInfo(Integer dropletId) throws ApiException {
        okhttp3.Call localVarCall = checkDestroyStatusValidateBeforeCall(dropletId, null);
        Type localVarReturnType = new TypeToken<AssociatedResourceStatus>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call checkDestroyStatusAsync(Integer dropletId, final ApiCallback<AssociatedResourceStatus> _callback) throws ApiException {

        okhttp3.Call localVarCall = checkDestroyStatusValidateBeforeCall(dropletId, _callback);
        Type localVarReturnType = new TypeToken<AssociatedResourceStatus>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CheckDestroyStatusRequestBuilder {
        private final Integer dropletId;

        private CheckDestroyStatusRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Build call for checkDestroyStatus
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing containing the status of a request to destroy a Droplet and its associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return checkDestroyStatusCall(dropletId, _callback);
        }


        /**
         * Execute checkDestroyStatus request
         * @return AssociatedResourceStatus
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing containing the status of a request to destroy a Droplet and its associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AssociatedResourceStatus execute() throws ApiException {
            ApiResponse<AssociatedResourceStatus> localVarResp = checkDestroyStatusWithHttpInfo(dropletId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute checkDestroyStatus request with HTTP info returned
         * @return ApiResponse&lt;AssociatedResourceStatus&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing containing the status of a request to destroy a Droplet and its associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AssociatedResourceStatus> executeWithHttpInfo() throws ApiException {
            return checkDestroyStatusWithHttpInfo(dropletId);
        }

        /**
         * Execute checkDestroyStatus request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing containing the status of a request to destroy a Droplet and its associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AssociatedResourceStatus> _callback) throws ApiException {
            return checkDestroyStatusAsync(dropletId, _callback);
        }
    }

    /**
     * Check Status of a Droplet Destroy with Associated Resources Request
     * To check on the status of a request to destroy a Droplet with its associated resources, send a GET request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/status&#x60; endpoint. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return CheckDestroyStatusRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object containing containing the status of a request to destroy a Droplet and its associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CheckDestroyStatusRequestBuilder checkDestroyStatus(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new CheckDestroyStatusRequestBuilder(dropletId);
    }
    private okhttp3.Call createCall(Object body, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = body;

        // create path and map variables
        String localVarPath = "/v2/droplets";

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
    private okhttp3.Call createValidateBeforeCall(Object body, final ApiCallback _callback) throws ApiException {
        return createCall(body, _callback);

    }


    private ApiResponse<Object> createWithHttpInfo(Object body) throws ApiException {
        okhttp3.Call localVarCall = createValidateBeforeCall(body, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createAsync(Object body, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = createValidateBeforeCall(body, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateRequestBuilder {
        private Object body;

        private CreateRequestBuilder() {
        }

        /**
         * Set body
         * @param body  (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder body(Object body) {
            this.body = body;
            return this;
        }

        /**
         * Build call for create
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> Accepted </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Object body = buildBodyParams();
            return createCall(body, _callback);
        }

        private Object buildBodyParams() {
            return this.body;
        }

        /**
         * Execute create request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> Accepted </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            Object body = buildBodyParams();
            ApiResponse<Object> localVarResp = createWithHttpInfo(body);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute create request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> Accepted </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            Object body = buildBodyParams();
            return createWithHttpInfo(body);
        }

        /**
         * Execute create request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> Accepted </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            Object body = buildBodyParams();
            return createAsync(body, _callback);
        }
    }

    /**
     * Create a New Droplet
     * To create a new Droplet, send a POST request to &#x60;/v2/droplets&#x60; setting the required attributes.  A Droplet will be created using the provided information. The response body will contain a JSON object with a key called &#x60;droplet&#x60;. The value will be an object containing the standard attributes for your new Droplet. The response code, 202 Accepted, does not indicate the success or failure of the operation, just that the request has been accepted for processing. The &#x60;actions&#x60; returned as part of the response&#39;s &#x60;links&#x60; object can be used to check the status of the Droplet create event.  ### Create Multiple Droplets  Creating multiple Droplets is very similar to creating a single Droplet. Instead of sending &#x60;name&#x60; as a string, send &#x60;names&#x60; as an array of strings. A Droplet will be created for each name you send using the associated information. Up to ten Droplets may be created this way at a time.  Rather than returning a single Droplet, the response body will contain a JSON array with a key called &#x60;droplets&#x60;. This will be set to an array of JSON objects, each of which will contain the standard Droplet attributes. The response code, 202 Accepted, does not indicate the success or failure of any operation, just that the request has been accepted for processing. The array of &#x60;actions&#x60; returned as part of the response&#39;s &#x60;links&#x60; object can be used to check the status of each individual Droplet create event. 
     * @return CreateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> Accepted </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateRequestBuilder create() throws IllegalArgumentException {
        return new CreateRequestBuilder();
    }
    private okhttp3.Call deleteByTagCall(String tagName, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets";

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
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteByTagValidateBeforeCall(String tagName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'tagName' is set
        if (tagName == null) {
            throw new ApiException("Missing the required parameter 'tagName' when calling deleteByTag(Async)");
        }

        return deleteByTagCall(tagName, _callback);

    }


    private ApiResponse<Void> deleteByTagWithHttpInfo(String tagName) throws ApiException {
        okhttp3.Call localVarCall = deleteByTagValidateBeforeCall(tagName, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteByTagAsync(String tagName, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteByTagValidateBeforeCall(tagName, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteByTagRequestBuilder {
        private final String tagName;

        private DeleteByTagRequestBuilder(String tagName) {
            this.tagName = tagName;
        }

        /**
         * Build call for deleteByTag
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteByTagCall(tagName, _callback);
        }


        /**
         * Execute deleteByTag request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteByTagWithHttpInfo(tagName);
        }

        /**
         * Execute deleteByTag request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return deleteByTagWithHttpInfo(tagName);
        }

        /**
         * Execute deleteByTag request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return deleteByTagAsync(tagName, _callback);
        }
    }

    /**
     * Deleting Droplets by Tag
     * To delete **all** Droplets assigned to a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag in your DELETE request. For example,  &#x60;/v2/droplets?tag_name&#x3D;$TAG_NAME&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 
     * @param tagName Specifies Droplets to be deleted by tag. (required)
     * @return DeleteByTagRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteByTagRequestBuilder deleteByTag(String tagName) throws IllegalArgumentException {
        if (tagName == null) throw new IllegalArgumentException("\"tagName\" is required but got null");
            

        return new DeleteByTagRequestBuilder(tagName);
    }
    private okhttp3.Call deleteDangerousCall(Integer dropletId, Boolean xDangerous, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/destroy_with_associated_resources/dangerous"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (xDangerous != null) {
            localVarHeaderParams.put("X-Dangerous", localVarApiClient.parameterToString(xDangerous));
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
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call deleteDangerousValidateBeforeCall(Integer dropletId, Boolean xDangerous, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling deleteDangerous(Async)");
        }

        // verify the required parameter 'xDangerous' is set
        if (xDangerous == null) {
            throw new ApiException("Missing the required parameter 'xDangerous' when calling deleteDangerous(Async)");
        }

        return deleteDangerousCall(dropletId, xDangerous, _callback);

    }


    private ApiResponse<Void> deleteDangerousWithHttpInfo(Integer dropletId, Boolean xDangerous) throws ApiException {
        okhttp3.Call localVarCall = deleteDangerousValidateBeforeCall(dropletId, xDangerous, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteDangerousAsync(Integer dropletId, Boolean xDangerous, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteDangerousValidateBeforeCall(dropletId, xDangerous, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteDangerousRequestBuilder {
        private final Integer dropletId;
        private final Boolean xDangerous;

        private DeleteDangerousRequestBuilder(Integer dropletId, Boolean xDangerous) {
            this.dropletId = dropletId;
            this.xDangerous = xDangerous;
        }

        /**
         * Build call for deleteDangerous
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
            return deleteDangerousCall(dropletId, xDangerous, _callback);
        }


        /**
         * Execute deleteDangerous request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteDangerousWithHttpInfo(dropletId, xDangerous);
        }

        /**
         * Execute deleteDangerous request with HTTP info returned
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
            return deleteDangerousWithHttpInfo(dropletId, xDangerous);
        }

        /**
         * Execute deleteDangerous request (asynchronously)
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
            return deleteDangerousAsync(dropletId, xDangerous, _callback);
        }
    }

    /**
     * Destroy a Droplet and All of its Associated Resources (Dangerous)
     * To destroy a Droplet along with all of its associated resources, send a DELETE request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/dangerous&#x60; endpoint. The headers of this request must include an &#x60;X-Dangerous&#x60; key set to &#x60;true&#x60;. To preview which resources will be destroyed, first query the Droplet&#39;s associated resources. This operation _can not_ be reverse and should be used with caution.  A successful response will include a 202 response code and no content. Use the status endpoint to check on the success or failure of the destruction of the individual resources. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @param xDangerous Acknowledge this action will destroy the Droplet and all associated resources and _can not_ be reversed. (required)
     * @return DeleteDangerousRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteDangerousRequestBuilder deleteDangerous(Integer dropletId, Boolean xDangerous) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        if (xDangerous == null) throw new IllegalArgumentException("\"xDangerous\" is required but got null");
        return new DeleteDangerousRequestBuilder(dropletId, xDangerous);
    }
    private okhttp3.Call destroyCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call destroyValidateBeforeCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling destroy(Async)");
        }

        return destroyCall(dropletId, _callback);

    }


    private ApiResponse<Void> destroyWithHttpInfo(Integer dropletId) throws ApiException {
        okhttp3.Call localVarCall = destroyValidateBeforeCall(dropletId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call destroyAsync(Integer dropletId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = destroyValidateBeforeCall(dropletId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DestroyRequestBuilder {
        private final Integer dropletId;

        private DestroyRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Build call for destroy
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return destroyCall(dropletId, _callback);
        }


        /**
         * Execute destroy request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            destroyWithHttpInfo(dropletId);
        }

        /**
         * Execute destroy request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return destroyWithHttpInfo(dropletId);
        }

        /**
         * Execute destroy request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return destroyAsync(dropletId, _callback);
        }
    }

    /**
     * Delete an Existing Droplet
     * To delete a Droplet, send a DELETE request to &#x60;/v2/droplets/$DROPLET_ID&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return DestroyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. This response has content-type set. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  * content-type -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DestroyRequestBuilder destroy(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new DestroyRequestBuilder(dropletId);
    }
    private okhttp3.Call destroyAssociatedResourcesSelectiveCall(Integer dropletId, SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = selectiveDestroyAssociatedResource;

        // create path and map variables
        String localVarPath = "/v2/droplets/{droplet_id}/destroy_with_associated_resources/selective"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call destroyAssociatedResourcesSelectiveValidateBeforeCall(Integer dropletId, SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling destroyAssociatedResourcesSelective(Async)");
        }

        return destroyAssociatedResourcesSelectiveCall(dropletId, selectiveDestroyAssociatedResource, _callback);

    }


    private ApiResponse<Void> destroyAssociatedResourcesSelectiveWithHttpInfo(Integer dropletId, SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource) throws ApiException {
        okhttp3.Call localVarCall = destroyAssociatedResourcesSelectiveValidateBeforeCall(dropletId, selectiveDestroyAssociatedResource, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call destroyAssociatedResourcesSelectiveAsync(Integer dropletId, SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = destroyAssociatedResourcesSelectiveValidateBeforeCall(dropletId, selectiveDestroyAssociatedResource, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DestroyAssociatedResourcesSelectiveRequestBuilder {
        private final Integer dropletId;
        private List<String> floatingIps;
        private List<String> reservedIps;
        private List<String> snapshots;
        private List<String> volumes;
        private List<String> volumeSnapshots;

        private DestroyAssociatedResourcesSelectiveRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Set floatingIps
         * @param floatingIps An array of unique identifiers for the floating IPs to be scheduled for deletion. (optional)
         * @return DestroyAssociatedResourcesSelectiveRequestBuilder
         */
        public DestroyAssociatedResourcesSelectiveRequestBuilder floatingIps(List<String> floatingIps) {
            this.floatingIps = floatingIps;
            return this;
        }
        
        /**
         * Set reservedIps
         * @param reservedIps An array of unique identifiers for the reserved IPs to be scheduled for deletion. (optional)
         * @return DestroyAssociatedResourcesSelectiveRequestBuilder
         */
        public DestroyAssociatedResourcesSelectiveRequestBuilder reservedIps(List<String> reservedIps) {
            this.reservedIps = reservedIps;
            return this;
        }
        
        /**
         * Set snapshots
         * @param snapshots An array of unique identifiers for the snapshots to be scheduled for deletion. (optional)
         * @return DestroyAssociatedResourcesSelectiveRequestBuilder
         */
        public DestroyAssociatedResourcesSelectiveRequestBuilder snapshots(List<String> snapshots) {
            this.snapshots = snapshots;
            return this;
        }
        
        /**
         * Set volumes
         * @param volumes An array of unique identifiers for the volumes to be scheduled for deletion. (optional)
         * @return DestroyAssociatedResourcesSelectiveRequestBuilder
         */
        public DestroyAssociatedResourcesSelectiveRequestBuilder volumes(List<String> volumes) {
            this.volumes = volumes;
            return this;
        }
        
        /**
         * Set volumeSnapshots
         * @param volumeSnapshots An array of unique identifiers for the volume snapshots to be scheduled for deletion. (optional)
         * @return DestroyAssociatedResourcesSelectiveRequestBuilder
         */
        public DestroyAssociatedResourcesSelectiveRequestBuilder volumeSnapshots(List<String> volumeSnapshots) {
            this.volumeSnapshots = volumeSnapshots;
            return this;
        }
        
        /**
         * Build call for destroyAssociatedResourcesSelective
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
            SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource = buildBodyParams();
            return destroyAssociatedResourcesSelectiveCall(dropletId, selectiveDestroyAssociatedResource, _callback);
        }

        private SelectiveDestroyAssociatedResource buildBodyParams() {
            SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource = new SelectiveDestroyAssociatedResource();
            selectiveDestroyAssociatedResource.floatingIps(this.floatingIps);
            selectiveDestroyAssociatedResource.reservedIps(this.reservedIps);
            selectiveDestroyAssociatedResource.snapshots(this.snapshots);
            selectiveDestroyAssociatedResource.volumes(this.volumes);
            selectiveDestroyAssociatedResource.volumeSnapshots(this.volumeSnapshots);
            return selectiveDestroyAssociatedResource;
        }

        /**
         * Execute destroyAssociatedResourcesSelective request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource = buildBodyParams();
            destroyAssociatedResourcesSelectiveWithHttpInfo(dropletId, selectiveDestroyAssociatedResource);
        }

        /**
         * Execute destroyAssociatedResourcesSelective request with HTTP info returned
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
            SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource = buildBodyParams();
            return destroyAssociatedResourcesSelectiveWithHttpInfo(dropletId, selectiveDestroyAssociatedResource);
        }

        /**
         * Execute destroyAssociatedResourcesSelective request (asynchronously)
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
            SelectiveDestroyAssociatedResource selectiveDestroyAssociatedResource = buildBodyParams();
            return destroyAssociatedResourcesSelectiveAsync(dropletId, selectiveDestroyAssociatedResource, _callback);
        }
    }

    /**
     * Selectively Destroy a Droplet and its Associated Resources
     * To destroy a Droplet along with a sub-set of its associated resources, send a DELETE request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/selective&#x60; endpoint. The JSON body of the request should include &#x60;reserved_ips&#x60;, &#x60;snapshots&#x60;, &#x60;volumes&#x60;, or &#x60;volume_snapshots&#x60; keys each set to an array of IDs for the associated resources to be destroyed. The IDs can be found by querying the Droplet&#39;s associated resources. Any associated resource not included in the request will remain and continue to accrue changes on your account.  A successful response will include a 202 response code and no content. Use the status endpoint to check on the success or failure of the destruction of the individual resources. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return DestroyAssociatedResourcesSelectiveRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DestroyAssociatedResourcesSelectiveRequestBuilder destroyAssociatedResourcesSelective(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new DestroyAssociatedResourcesSelectiveRequestBuilder(dropletId);
    }
    private okhttp3.Call getCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call getValidateBeforeCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling get(Async)");
        }

        return getCall(dropletId, _callback);

    }


    private ApiResponse<DropletsGetResponse> getWithHttpInfo(Integer dropletId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(dropletId, null);
        Type localVarReturnType = new TypeToken<DropletsGetResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(Integer dropletId, final ApiCallback<DropletsGetResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(dropletId, _callback);
        Type localVarReturnType = new TypeToken<DropletsGetResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final Integer dropletId;

        private GetRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Build call for get
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;droplet&#x60;. This will be set to a JSON object that contains the standard Droplet attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(dropletId, _callback);
        }


        /**
         * Execute get request
         * @return DropletsGetResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;droplet&#x60;. This will be set to a JSON object that contains the standard Droplet attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsGetResponse execute() throws ApiException {
            ApiResponse<DropletsGetResponse> localVarResp = getWithHttpInfo(dropletId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;DropletsGetResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;droplet&#x60;. This will be set to a JSON object that contains the standard Droplet attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsGetResponse> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(dropletId);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;droplet&#x60;. This will be set to a JSON object that contains the standard Droplet attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsGetResponse> _callback) throws ApiException {
            return getAsync(dropletId, _callback);
        }
    }

    /**
     * Retrieve an Existing Droplet
     * To show information about an individual Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID&#x60;. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;droplet&#x60;. This will be set to a JSON object that contains the standard Droplet attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetRequestBuilder get(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new GetRequestBuilder(dropletId);
    }
    private okhttp3.Call listCall(Integer perPage, Integer page, String tagName, String name, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets";

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

        if (tagName != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("tag_name", tagName));
        }

        if (name != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("name", name));
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
    private okhttp3.Call listValidateBeforeCall(Integer perPage, Integer page, String tagName, String name, final ApiCallback _callback) throws ApiException {
        return listCall(perPage, page, tagName, name, _callback);

    }


    private ApiResponse<DropletsListResponse> listWithHttpInfo(Integer perPage, Integer page, String tagName, String name) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(perPage, page, tagName, name, null);
        Type localVarReturnType = new TypeToken<DropletsListResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(Integer perPage, Integer page, String tagName, String name, final ApiCallback<DropletsListResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(perPage, page, tagName, name, _callback);
        Type localVarReturnType = new TypeToken<DropletsListResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private Integer perPage;
        private Integer page;
        private String tagName;
        private String name;

        private ListRequestBuilder() {
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Set tagName
         * @param tagName Used to filter Droplets by a specific tag. Can not be combined with &#x60;name&#x60;. (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder tagName(String tagName) {
            this.tagName = tagName;
            return this;
        }
        
        /**
         * Set name
         * @param name Used to filter list response by Droplet name returning only exact matches. It is case-insensitive and can not be combined with &#x60;tag_name&#x60;. (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Build call for list
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;droplets&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(perPage, page, tagName, name, _callback);
        }


        /**
         * Execute list request
         * @return DropletsListResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;droplets&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsListResponse execute() throws ApiException {
            ApiResponse<DropletsListResponse> localVarResp = listWithHttpInfo(perPage, page, tagName, name);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;DropletsListResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;droplets&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsListResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(perPage, page, tagName, name);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a key of &#x60;droplets&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsListResponse> _callback) throws ApiException {
            return listAsync(perPage, page, tagName, name, _callback);
        }
    }

    /**
     * List All Droplets
     * To list all Droplets in your account, send a GET request to &#x60;/v2/droplets&#x60;.  The response body will be a JSON object with a key of &#x60;droplets&#x60;. This will be set to an array containing objects each representing a Droplet. These will contain the standard Droplet attributes.  ### Filtering Results by Tag  It&#39;s possible to request filtered results by including certain query parameters. To only list Droplets assigned to a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag in your GET request. For example, &#x60;/v2/droplets?tag_name&#x3D;$TAG_NAME&#x60;. 
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a key of &#x60;droplets&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRequestBuilder list() throws IllegalArgumentException {
        return new ListRequestBuilder();
    }
    private okhttp3.Call listAssociatedResourcesCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/destroy_with_associated_resources"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call listAssociatedResourcesValidateBeforeCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling listAssociatedResources(Async)");
        }

        return listAssociatedResourcesCall(dropletId, _callback);

    }


    private ApiResponse<DropletsListAssociatedResourcesResponse> listAssociatedResourcesWithHttpInfo(Integer dropletId) throws ApiException {
        okhttp3.Call localVarCall = listAssociatedResourcesValidateBeforeCall(dropletId, null);
        Type localVarReturnType = new TypeToken<DropletsListAssociatedResourcesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAssociatedResourcesAsync(Integer dropletId, final ApiCallback<DropletsListAssociatedResourcesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAssociatedResourcesValidateBeforeCall(dropletId, _callback);
        Type localVarReturnType = new TypeToken<DropletsListAssociatedResourcesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAssociatedResourcesRequestBuilder {
        private final Integer dropletId;

        private ListAssociatedResourcesRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Build call for listAssociatedResources
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAssociatedResourcesCall(dropletId, _callback);
        }


        /**
         * Execute listAssociatedResources request
         * @return DropletsListAssociatedResourcesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsListAssociatedResourcesResponse execute() throws ApiException {
            ApiResponse<DropletsListAssociatedResourcesResponse> localVarResp = listAssociatedResourcesWithHttpInfo(dropletId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAssociatedResources request with HTTP info returned
         * @return ApiResponse&lt;DropletsListAssociatedResourcesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsListAssociatedResourcesResponse> executeWithHttpInfo() throws ApiException {
            return listAssociatedResourcesWithHttpInfo(dropletId);
        }

        /**
         * Execute listAssociatedResources request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsListAssociatedResourcesResponse> _callback) throws ApiException {
            return listAssociatedResourcesAsync(dropletId, _callback);
        }
    }

    /**
     * List Associated Resources for a Droplet
     * To list the associated billable resources that can be destroyed along with a Droplet, send a GET request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources&#x60; endpoint.  The response will be a JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing information about the associated resources. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return ListAssociatedResourcesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object containing &#x60;snapshots&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListAssociatedResourcesRequestBuilder listAssociatedResources(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new ListAssociatedResourcesRequestBuilder(dropletId);
    }
    private okhttp3.Call listBackupsCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/backups"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call listBackupsValidateBeforeCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling listBackups(Async)");
        }

        return listBackupsCall(dropletId, perPage, page, _callback);

    }


    private ApiResponse<DropletsListBackupsResponse> listBackupsWithHttpInfo(Integer dropletId, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listBackupsValidateBeforeCall(dropletId, perPage, page, null);
        Type localVarReturnType = new TypeToken<DropletsListBackupsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listBackupsAsync(Integer dropletId, Integer perPage, Integer page, final ApiCallback<DropletsListBackupsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listBackupsValidateBeforeCall(dropletId, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<DropletsListBackupsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListBackupsRequestBuilder {
        private final Integer dropletId;
        private Integer perPage;
        private Integer page;

        private ListBackupsRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListBackupsRequestBuilder
         */
        public ListBackupsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListBackupsRequestBuilder
         */
        public ListBackupsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listBackups
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;backups&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listBackupsCall(dropletId, perPage, page, _callback);
        }


        /**
         * Execute listBackups request
         * @return DropletsListBackupsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;backups&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsListBackupsResponse execute() throws ApiException {
            ApiResponse<DropletsListBackupsResponse> localVarResp = listBackupsWithHttpInfo(dropletId, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listBackups request with HTTP info returned
         * @return ApiResponse&lt;DropletsListBackupsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;backups&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsListBackupsResponse> executeWithHttpInfo() throws ApiException {
            return listBackupsWithHttpInfo(dropletId, perPage, page);
        }

        /**
         * Execute listBackups request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;backups&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsListBackupsResponse> _callback) throws ApiException {
            return listBackupsAsync(dropletId, perPage, page, _callback);
        }
    }

    /**
     * List Backups for a Droplet
     * To retrieve any backups associated with a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/backups&#x60;.  You will get back a JSON object that has a &#x60;backups&#x60; key. This will be set to an array of backup objects, each of which contain the standard Droplet backup attributes. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return ListBackupsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with an &#x60;backups&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListBackupsRequestBuilder listBackups(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new ListBackupsRequestBuilder(dropletId);
    }
    private okhttp3.Call listDropletNeighborsCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/reports/droplet_neighbors_ids";

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
    private okhttp3.Call listDropletNeighborsValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return listDropletNeighborsCall(_callback);

    }


    private ApiResponse<NeighborIds> listDropletNeighborsWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listDropletNeighborsValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<NeighborIds>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listDropletNeighborsAsync(final ApiCallback<NeighborIds> _callback) throws ApiException {

        okhttp3.Call localVarCall = listDropletNeighborsValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<NeighborIds>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListDropletNeighborsRequestBuilder {

        private ListDropletNeighborsRequestBuilder() {
        }

        /**
         * Build call for listDropletNeighbors
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;neighbor_ids&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listDropletNeighborsCall(_callback);
        }


        /**
         * Execute listDropletNeighbors request
         * @return NeighborIds
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;neighbor_ids&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public NeighborIds execute() throws ApiException {
            ApiResponse<NeighborIds> localVarResp = listDropletNeighborsWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listDropletNeighbors request with HTTP info returned
         * @return ApiResponse&lt;NeighborIds&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;neighbor_ids&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<NeighborIds> executeWithHttpInfo() throws ApiException {
            return listDropletNeighborsWithHttpInfo();
        }

        /**
         * Execute listDropletNeighbors request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;neighbor_ids&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<NeighborIds> _callback) throws ApiException {
            return listDropletNeighborsAsync(_callback);
        }
    }

    /**
     * List All Droplet Neighbors
     * To retrieve a list of all Droplets that are co-located on the same physical hardware, send a GET request to &#x60;/v2/reports/droplet_neighbors_ids&#x60;.  The results will be returned as a JSON object with a key of &#x60;neighbor_ids&#x60;. This will be set to an array of arrays. Each array will contain a set of Droplet IDs for Droplets that share a physical server. An empty array indicates that all Droplets associated with your account are located on separate physical hardware. 
     * @return ListDropletNeighborsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with an &#x60;neighbor_ids&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListDropletNeighborsRequestBuilder listDropletNeighbors() throws IllegalArgumentException {
        return new ListDropletNeighborsRequestBuilder();
    }
    private okhttp3.Call listFirewallsCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/firewalls"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call listFirewallsValidateBeforeCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling listFirewalls(Async)");
        }

        return listFirewallsCall(dropletId, perPage, page, _callback);

    }


    private ApiResponse<DropletsListFirewallsResponse> listFirewallsWithHttpInfo(Integer dropletId, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listFirewallsValidateBeforeCall(dropletId, perPage, page, null);
        Type localVarReturnType = new TypeToken<DropletsListFirewallsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listFirewallsAsync(Integer dropletId, Integer perPage, Integer page, final ApiCallback<DropletsListFirewallsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listFirewallsValidateBeforeCall(dropletId, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<DropletsListFirewallsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListFirewallsRequestBuilder {
        private final Integer dropletId;
        private Integer perPage;
        private Integer page;

        private ListFirewallsRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListFirewallsRequestBuilder
         */
        public ListFirewallsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListFirewallsRequestBuilder
         */
        public ListFirewallsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listFirewalls
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listFirewallsCall(dropletId, perPage, page, _callback);
        }


        /**
         * Execute listFirewalls request
         * @return DropletsListFirewallsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsListFirewallsResponse execute() throws ApiException {
            ApiResponse<DropletsListFirewallsResponse> localVarResp = listFirewallsWithHttpInfo(dropletId, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listFirewalls request with HTTP info returned
         * @return ApiResponse&lt;DropletsListFirewallsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsListFirewallsResponse> executeWithHttpInfo() throws ApiException {
            return listFirewallsWithHttpInfo(dropletId, perPage, page);
        }

        /**
         * Execute listFirewalls request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsListFirewallsResponse> _callback) throws ApiException {
            return listFirewallsAsync(dropletId, perPage, page, _callback);
        }
    }

    /**
     * List all Firewalls Applied to a Droplet
     * To retrieve a list of all firewalls available to a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/firewalls&#x60;  The response will be a JSON object that has a key called &#x60;firewalls&#x60;. This will be set to an array of &#x60;firewall&#x60; objects, each of which contain the standard &#x60;firewall&#x60; attributes. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return ListFirewallsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object that has a key called &#x60;firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListFirewallsRequestBuilder listFirewalls(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new ListFirewallsRequestBuilder(dropletId);
    }
    private okhttp3.Call listKernelsCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/kernels"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call listKernelsValidateBeforeCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling listKernels(Async)");
        }

        return listKernelsCall(dropletId, perPage, page, _callback);

    }


    private ApiResponse<DropletsListKernelsResponse> listKernelsWithHttpInfo(Integer dropletId, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listKernelsValidateBeforeCall(dropletId, perPage, page, null);
        Type localVarReturnType = new TypeToken<DropletsListKernelsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listKernelsAsync(Integer dropletId, Integer perPage, Integer page, final ApiCallback<DropletsListKernelsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listKernelsValidateBeforeCall(dropletId, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<DropletsListKernelsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListKernelsRequestBuilder {
        private final Integer dropletId;
        private Integer perPage;
        private Integer page;

        private ListKernelsRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListKernelsRequestBuilder
         */
        public ListKernelsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListKernelsRequestBuilder
         */
        public ListKernelsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listKernels
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;kernels&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listKernelsCall(dropletId, perPage, page, _callback);
        }


        /**
         * Execute listKernels request
         * @return DropletsListKernelsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;kernels&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsListKernelsResponse execute() throws ApiException {
            ApiResponse<DropletsListKernelsResponse> localVarResp = listKernelsWithHttpInfo(dropletId, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listKernels request with HTTP info returned
         * @return ApiResponse&lt;DropletsListKernelsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;kernels&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsListKernelsResponse> executeWithHttpInfo() throws ApiException {
            return listKernelsWithHttpInfo(dropletId, perPage, page);
        }

        /**
         * Execute listKernels request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object that has a key called &#x60;kernels&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsListKernelsResponse> _callback) throws ApiException {
            return listKernelsAsync(dropletId, perPage, page, _callback);
        }
    }

    /**
     * List All Available Kernels for a Droplet
     * To retrieve a list of all kernels available to a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/kernels&#x60;  The response will be a JSON object that has a key called &#x60;kernels&#x60;. This will be set to an array of &#x60;kernel&#x60; objects, each of which contain the standard &#x60;kernel&#x60; attributes. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return ListKernelsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object that has a key called &#x60;kernels&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListKernelsRequestBuilder listKernels(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new ListKernelsRequestBuilder(dropletId);
    }
    private okhttp3.Call listNeighborsCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/neighbors"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call listNeighborsValidateBeforeCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling listNeighbors(Async)");
        }

        return listNeighborsCall(dropletId, _callback);

    }


    private ApiResponse<DropletsListNeighborsResponse> listNeighborsWithHttpInfo(Integer dropletId) throws ApiException {
        okhttp3.Call localVarCall = listNeighborsValidateBeforeCall(dropletId, null);
        Type localVarReturnType = new TypeToken<DropletsListNeighborsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listNeighborsAsync(Integer dropletId, final ApiCallback<DropletsListNeighborsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listNeighborsValidateBeforeCall(dropletId, _callback);
        Type localVarReturnType = new TypeToken<DropletsListNeighborsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListNeighborsRequestBuilder {
        private final Integer dropletId;

        private ListNeighborsRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Build call for listNeighbors
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;droplets&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listNeighborsCall(dropletId, _callback);
        }


        /**
         * Execute listNeighbors request
         * @return DropletsListNeighborsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;droplets&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsListNeighborsResponse execute() throws ApiException {
            ApiResponse<DropletsListNeighborsResponse> localVarResp = listNeighborsWithHttpInfo(dropletId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listNeighbors request with HTTP info returned
         * @return ApiResponse&lt;DropletsListNeighborsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;droplets&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsListNeighborsResponse> executeWithHttpInfo() throws ApiException {
            return listNeighborsWithHttpInfo(dropletId);
        }

        /**
         * Execute listNeighbors request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;droplets&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsListNeighborsResponse> _callback) throws ApiException {
            return listNeighborsAsync(dropletId, _callback);
        }
    }

    /**
     * List Neighbors for a Droplet
     * To retrieve a list of any \&quot;neighbors\&quot; (i.e. Droplets that are co-located on the same physical hardware) for a specific Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/neighbors&#x60;.  The results will be returned as a JSON object with a key of &#x60;droplets&#x60;. This will be set to an array containing objects representing any other Droplets that share the same physical hardware. An empty array indicates that the Droplet is not co-located any other Droplets associated with your account. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return ListNeighborsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with an &#x60;droplets&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListNeighborsRequestBuilder listNeighbors(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new ListNeighborsRequestBuilder(dropletId);
    }
    private okhttp3.Call listSnapshotsCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/snapshots"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
    private okhttp3.Call listSnapshotsValidateBeforeCall(Integer dropletId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling listSnapshots(Async)");
        }

        return listSnapshotsCall(dropletId, perPage, page, _callback);

    }


    private ApiResponse<DropletsListSnapshotsResponse> listSnapshotsWithHttpInfo(Integer dropletId, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listSnapshotsValidateBeforeCall(dropletId, perPage, page, null);
        Type localVarReturnType = new TypeToken<DropletsListSnapshotsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listSnapshotsAsync(Integer dropletId, Integer perPage, Integer page, final ApiCallback<DropletsListSnapshotsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listSnapshotsValidateBeforeCall(dropletId, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<DropletsListSnapshotsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListSnapshotsRequestBuilder {
        private final Integer dropletId;
        private Integer perPage;
        private Integer page;

        private ListSnapshotsRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListSnapshotsRequestBuilder
         */
        public ListSnapshotsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListSnapshotsRequestBuilder
         */
        public ListSnapshotsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listSnapshots
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;snapshots&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listSnapshotsCall(dropletId, perPage, page, _callback);
        }


        /**
         * Execute listSnapshots request
         * @return DropletsListSnapshotsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;snapshots&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DropletsListSnapshotsResponse execute() throws ApiException {
            ApiResponse<DropletsListSnapshotsResponse> localVarResp = listSnapshotsWithHttpInfo(dropletId, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listSnapshots request with HTTP info returned
         * @return ApiResponse&lt;DropletsListSnapshotsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;snapshots&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DropletsListSnapshotsResponse> executeWithHttpInfo() throws ApiException {
            return listSnapshotsWithHttpInfo(dropletId, perPage, page);
        }

        /**
         * Execute listSnapshots request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;snapshots&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DropletsListSnapshotsResponse> _callback) throws ApiException {
            return listSnapshotsAsync(dropletId, perPage, page, _callback);
        }
    }

    /**
     * List Snapshots for a Droplet
     * To retrieve the snapshots that have been created from a Droplet, send a GET request to &#x60;/v2/droplets/$DROPLET_ID/snapshots&#x60;.  You will get back a JSON object that has a &#x60;snapshots&#x60; key. This will be set to an array of snapshot objects, each of which contain the standard Droplet snapshot attributes. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return ListSnapshotsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with an &#x60;snapshots&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListSnapshotsRequestBuilder listSnapshots(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new ListSnapshotsRequestBuilder(dropletId);
    }
    private okhttp3.Call retryDestroyWithAssociatedResourcesCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/droplets/{droplet_id}/destroy_with_associated_resources/retry"
            .replace("{" + "droplet_id" + "}", localVarApiClient.escapeString(dropletId.toString()));

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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call retryDestroyWithAssociatedResourcesValidateBeforeCall(Integer dropletId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'dropletId' is set
        if (dropletId == null) {
            throw new ApiException("Missing the required parameter 'dropletId' when calling retryDestroyWithAssociatedResources(Async)");
        }

        return retryDestroyWithAssociatedResourcesCall(dropletId, _callback);

    }


    private ApiResponse<Void> retryDestroyWithAssociatedResourcesWithHttpInfo(Integer dropletId) throws ApiException {
        okhttp3.Call localVarCall = retryDestroyWithAssociatedResourcesValidateBeforeCall(dropletId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call retryDestroyWithAssociatedResourcesAsync(Integer dropletId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = retryDestroyWithAssociatedResourcesValidateBeforeCall(dropletId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RetryDestroyWithAssociatedResourcesRequestBuilder {
        private final Integer dropletId;

        private RetryDestroyWithAssociatedResourcesRequestBuilder(Integer dropletId) {
            this.dropletId = dropletId;
        }

        /**
         * Build call for retryDestroyWithAssociatedResources
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
            return retryDestroyWithAssociatedResourcesCall(dropletId, _callback);
        }


        /**
         * Execute retryDestroyWithAssociatedResources request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            retryDestroyWithAssociatedResourcesWithHttpInfo(dropletId);
        }

        /**
         * Execute retryDestroyWithAssociatedResources request with HTTP info returned
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
            return retryDestroyWithAssociatedResourcesWithHttpInfo(dropletId);
        }

        /**
         * Execute retryDestroyWithAssociatedResources request (asynchronously)
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
            return retryDestroyWithAssociatedResourcesAsync(dropletId, _callback);
        }
    }

    /**
     * Retry a Droplet Destroy with Associated Resources Request
     * If the status of a request to destroy a Droplet with its associated resources reported any errors, it can be retried by sending a POST request to the &#x60;/v2/droplets/$DROPLET_ID/destroy_with_associated_resources/retry&#x60; endpoint.  Only one destroy can be active at a time per Droplet. If a retry is issued while another destroy is in progress for the Droplet a 409 status code will be returned. A successful response will include a 202 response code and no content. 
     * @param dropletId A unique identifier for a Droplet instance. (required)
     * @return RetryDestroyWithAssociatedResourcesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RetryDestroyWithAssociatedResourcesRequestBuilder retryDestroyWithAssociatedResources(Integer dropletId) throws IllegalArgumentException {
        if (dropletId == null) throw new IllegalArgumentException("\"dropletId\" is required but got null");
        return new RetryDestroyWithAssociatedResourcesRequestBuilder(dropletId);
    }
}
