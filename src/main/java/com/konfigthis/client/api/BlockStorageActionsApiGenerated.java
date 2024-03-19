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


import com.konfigthis.client.model.Adrvolumeactionpost;
import com.konfigthis.client.model.Advolumeactionpost;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.RegionSlug;
import java.util.UUID;
import com.konfigthis.client.model.VolumeActionsListResponse;
import com.konfigthis.client.model.VolumeActionsPostResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class BlockStorageActionsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public BlockStorageActionsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public BlockStorageActionsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call getCall(UUID volumeId, Integer actionId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/volumes/{volume_id}/actions/{action_id}"
            .replace("{" + "volume_id" + "}", localVarApiClient.escapeString(volumeId.toString()))
            .replace("{" + "action_id" + "}", localVarApiClient.escapeString(actionId.toString()));

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
    private okhttp3.Call getValidateBeforeCall(UUID volumeId, Integer actionId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'volumeId' is set
        if (volumeId == null) {
            throw new ApiException("Missing the required parameter 'volumeId' when calling get(Async)");
        }

        // verify the required parameter 'actionId' is set
        if (actionId == null) {
            throw new ApiException("Missing the required parameter 'actionId' when calling get(Async)");
        }

        return getCall(volumeId, actionId, perPage, page, _callback);

    }


    private ApiResponse<VolumeActionsPostResponse> getWithHttpInfo(UUID volumeId, Integer actionId, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(volumeId, actionId, perPage, page, null);
        Type localVarReturnType = new TypeToken<VolumeActionsPostResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(UUID volumeId, Integer actionId, Integer perPage, Integer page, final ApiCallback<VolumeActionsPostResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(volumeId, actionId, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<VolumeActionsPostResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final UUID volumeId;
        private final Integer actionId;
        private Integer perPage;
        private Integer page;

        private GetRequestBuilder(UUID volumeId, Integer actionId) {
            this.volumeId = volumeId;
            this.actionId = actionId;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return GetRequestBuilder
         */
        public GetRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return GetRequestBuilder
         */
        public GetRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for get
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(volumeId, actionId, perPage, page, _callback);
        }


        /**
         * Execute get request
         * @return VolumeActionsPostResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public VolumeActionsPostResponse execute() throws ApiException {
            ApiResponse<VolumeActionsPostResponse> localVarResp = getWithHttpInfo(volumeId, actionId, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;VolumeActionsPostResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<VolumeActionsPostResponse> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(volumeId, actionId, perPage, page);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<VolumeActionsPostResponse> _callback) throws ApiException {
            return getAsync(volumeId, actionId, perPage, page, _callback);
        }
    }

    /**
     * Retrieve an Existing Volume Action
     * To retrieve the status of a volume action, send a GET request to &#x60;/v2/volumes/$VOLUME_ID/actions/$ACTION_ID&#x60;.  
     * @param volumeId The ID of the block storage volume. (required)
     * @param actionId A unique numeric ID that can be used to identify and reference an action. (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetRequestBuilder get(UUID volumeId, Integer actionId) throws IllegalArgumentException {
        if (volumeId == null) throw new IllegalArgumentException("\"volumeId\" is required but got null");
            

        if (actionId == null) throw new IllegalArgumentException("\"actionId\" is required but got null");
        return new GetRequestBuilder(volumeId, actionId);
    }
    private okhttp3.Call initiateAttachActionCall(UUID volumeId, Adrvolumeactionpost body, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/volumes/{volume_id}/actions"
            .replace("{" + "volume_id" + "}", localVarApiClient.escapeString(volumeId.toString()));

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
    private okhttp3.Call initiateAttachActionValidateBeforeCall(UUID volumeId, Adrvolumeactionpost body, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'volumeId' is set
        if (volumeId == null) {
            throw new ApiException("Missing the required parameter 'volumeId' when calling initiateAttachAction(Async)");
        }

        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling initiateAttachAction(Async)");
        }

        return initiateAttachActionCall(volumeId, body, perPage, page, _callback);

    }


    private ApiResponse<VolumeActionsPostResponse> initiateAttachActionWithHttpInfo(UUID volumeId, Adrvolumeactionpost body, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = initiateAttachActionValidateBeforeCall(volumeId, body, perPage, page, null);
        Type localVarReturnType = new TypeToken<VolumeActionsPostResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call initiateAttachActionAsync(UUID volumeId, Adrvolumeactionpost body, Integer perPage, Integer page, final ApiCallback<VolumeActionsPostResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = initiateAttachActionValidateBeforeCall(volumeId, body, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<VolumeActionsPostResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class InitiateAttachActionRequestBuilder {
        private final UUID volumeId;
        private String type;
        private RegionSlug region;
        private List<String> tags;
        private Integer dropletId;
        private Integer perPage;
        private Integer page;

        private InitiateAttachActionRequestBuilder(UUID volumeId) {
            this.volumeId = volumeId;
        }

        /**
         * Set type
         * @param type The volume action to initiate. (optional)
         * @return InitiateAttachActionRequestBuilder
         */
        public InitiateAttachActionRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set region
         * @param region  (optional)
         * @return InitiateAttachActionRequestBuilder
         */
        public InitiateAttachActionRequestBuilder region(RegionSlug region) {
            this.region = region;
            return this;
        }
        
        /**
         * Set tags
         * @param tags A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags. (optional)
         * @return InitiateAttachActionRequestBuilder
         */
        public InitiateAttachActionRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set dropletId
         * @param dropletId The unique identifier for the Droplet the volume will be attached or detached from. (optional)
         * @return InitiateAttachActionRequestBuilder
         */
        public InitiateAttachActionRequestBuilder dropletId(Integer dropletId) {
            this.dropletId = dropletId;
            return this;
        }
        
        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return InitiateAttachActionRequestBuilder
         */
        public InitiateAttachActionRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return InitiateAttachActionRequestBuilder
         */
        public InitiateAttachActionRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for initiateAttachAction
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Adrvolumeactionpost body = buildBodyParams();
            return initiateAttachActionCall(volumeId, body, perPage, page, _callback);
        }

        private Adrvolumeactionpost buildBodyParams() {
            Adrvolumeactionpost body = new Adrvolumeactionpost();
            return body;
        }

        /**
         * Execute initiateAttachAction request
         * @return VolumeActionsPostResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public VolumeActionsPostResponse execute() throws ApiException {
            Adrvolumeactionpost body = buildBodyParams();
            ApiResponse<VolumeActionsPostResponse> localVarResp = initiateAttachActionWithHttpInfo(volumeId, body, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute initiateAttachAction request with HTTP info returned
         * @return ApiResponse&lt;VolumeActionsPostResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<VolumeActionsPostResponse> executeWithHttpInfo() throws ApiException {
            Adrvolumeactionpost body = buildBodyParams();
            return initiateAttachActionWithHttpInfo(volumeId, body, perPage, page);
        }

        /**
         * Execute initiateAttachAction request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<VolumeActionsPostResponse> _callback) throws ApiException {
            Adrvolumeactionpost body = buildBodyParams();
            return initiateAttachActionAsync(volumeId, body, perPage, page, _callback);
        }
    }

    /**
     * Initiate A Block Storage Action By Volume Id
     * To initiate an action on a block storage volume by Id, send a POST request to &#x60;~/v2/volumes/$VOLUME_ID/actions&#x60;. The body should contain the appropriate attributes for the respective action.  ## Attach a Block Storage Volume to a Droplet  | Attribute  | Details                                                             | | ---------- | ------------------------------------------------------------------- | | type       | This must be &#x60;attach&#x60;                                               | | droplet_id | Set to the Droplet&#39;s ID                                             | | region     | Set to the slug representing the region where the volume is located |  Each volume may only be attached to a single Droplet. However, up to seven volumes may be attached to a Droplet at a time. Pre-formatted volumes will be automatically mounted to Ubuntu, Debian, Fedora, Fedora Atomic, and CentOS Droplets created on or after April 26, 2018 when attached. On older Droplets, [additional configuration](https://www.digitalocean.com/community/tutorials/how-to-partition-and-format-digitalocean-block-storage-volumes-in-linux#mounting-the-filesystems) is required.  ## Remove a Block Storage Volume from a Droplet  | Attribute  | Details                                                             | | ---------- | ------------------------------------------------------------------- | | type       | This must be &#x60;detach&#x60;                                               | | droplet_id | Set to the Droplet&#39;s ID                                             | | region     | Set to the slug representing the region where the volume is located |  ## Resize a Volume  | Attribute      | Details                                                             | | -------------- | ------------------------------------------------------------------- | | type           | This must be &#x60;resize&#x60;                                               | | size_gigabytes | The new size of the block storage volume in GiB (1024^3)            | | region         | Set to the slug representing the region where the volume is located |  Volumes may only be resized upwards. The maximum size for a volume is 16TiB. 
     * @param volumeId The ID of the block storage volume. (required)
     * @param body  (required)
     * @return InitiateAttachActionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public InitiateAttachActionRequestBuilder initiateAttachAction(UUID volumeId) throws IllegalArgumentException {
        if (volumeId == null) throw new IllegalArgumentException("\"volumeId\" is required but got null");
            

        return new InitiateAttachActionRequestBuilder(volumeId);
    }
    private okhttp3.Call listCall(UUID volumeId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/volumes/{volume_id}/actions"
            .replace("{" + "volume_id" + "}", localVarApiClient.escapeString(volumeId.toString()));

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
    private okhttp3.Call listValidateBeforeCall(UUID volumeId, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'volumeId' is set
        if (volumeId == null) {
            throw new ApiException("Missing the required parameter 'volumeId' when calling list(Async)");
        }

        return listCall(volumeId, perPage, page, _callback);

    }


    private ApiResponse<VolumeActionsListResponse> listWithHttpInfo(UUID volumeId, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(volumeId, perPage, page, null);
        Type localVarReturnType = new TypeToken<VolumeActionsListResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(UUID volumeId, Integer perPage, Integer page, final ApiCallback<VolumeActionsListResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(volumeId, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<VolumeActionsListResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private final UUID volumeId;
        private Integer perPage;
        private Integer page;

        private ListRequestBuilder(UUID volumeId) {
            this.volumeId = volumeId;
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
         * Build call for list
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(volumeId, perPage, page, _callback);
        }


        /**
         * Execute list request
         * @return VolumeActionsListResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public VolumeActionsListResponse execute() throws ApiException {
            ApiResponse<VolumeActionsListResponse> localVarResp = listWithHttpInfo(volumeId, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;VolumeActionsListResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<VolumeActionsListResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(volumeId, perPage, page);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<VolumeActionsListResponse> _callback) throws ApiException {
            return listAsync(volumeId, perPage, page, _callback);
        }
    }

    /**
     * List All Actions for a Volume
     * To retrieve all actions that have been executed on a volume, send a GET request to &#x60;/v2/volumes/$VOLUME_ID/actions&#x60;.  
     * @param volumeId The ID of the block storage volume. (required)
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRequestBuilder list(UUID volumeId) throws IllegalArgumentException {
        if (volumeId == null) throw new IllegalArgumentException("\"volumeId\" is required but got null");
            

        return new ListRequestBuilder(volumeId);
    }
    private okhttp3.Call postCall(Advolumeactionpost body, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/volumes/actions";

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
    private okhttp3.Call postValidateBeforeCall(Advolumeactionpost body, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'body' is set
        if (body == null) {
            throw new ApiException("Missing the required parameter 'body' when calling post(Async)");
        }

        return postCall(body, perPage, page, _callback);

    }


    private ApiResponse<VolumeActionsPostResponse> postWithHttpInfo(Advolumeactionpost body, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = postValidateBeforeCall(body, perPage, page, null);
        Type localVarReturnType = new TypeToken<VolumeActionsPostResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call postAsync(Advolumeactionpost body, Integer perPage, Integer page, final ApiCallback<VolumeActionsPostResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = postValidateBeforeCall(body, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<VolumeActionsPostResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class PostRequestBuilder {
        private String type;
        private RegionSlug region;
        private List<String> tags;
        private Integer dropletId;
        private Integer perPage;
        private Integer page;

        private PostRequestBuilder() {
        }

        /**
         * Set type
         * @param type The volume action to initiate. (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set region
         * @param region  (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder region(RegionSlug region) {
            this.region = region;
            return this;
        }
        
        /**
         * Set tags
         * @param tags A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags. (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set dropletId
         * @param dropletId The unique identifier for the Droplet the volume will be attached or detached from. (optional)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder dropletId(Integer dropletId) {
            this.dropletId = dropletId;
            return this;
        }
        
        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return PostRequestBuilder
         */
        public PostRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for post
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Advolumeactionpost body = buildBodyParams();
            return postCall(body, perPage, page, _callback);
        }

        private Advolumeactionpost buildBodyParams() {
            Advolumeactionpost body = new Advolumeactionpost();
            return body;
        }

        /**
         * Execute post request
         * @return VolumeActionsPostResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public VolumeActionsPostResponse execute() throws ApiException {
            Advolumeactionpost body = buildBodyParams();
            ApiResponse<VolumeActionsPostResponse> localVarResp = postWithHttpInfo(body, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute post request with HTTP info returned
         * @return ApiResponse&lt;VolumeActionsPostResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<VolumeActionsPostResponse> executeWithHttpInfo() throws ApiException {
            Advolumeactionpost body = buildBodyParams();
            return postWithHttpInfo(body, perPage, page);
        }

        /**
         * Execute post request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<VolumeActionsPostResponse> _callback) throws ApiException {
            Advolumeactionpost body = buildBodyParams();
            return postAsync(body, perPage, page, _callback);
        }
    }

    /**
     * Initiate A Block Storage Action By Volume Name
     * To initiate an action on a block storage volume by Name, send a POST request to &#x60;~/v2/volumes/actions&#x60;. The body should contain the appropriate attributes for the respective action.  ## Attach a Block Storage Volume to a Droplet  | Attribute   | Details                                                             | | ----------- | ------------------------------------------------------------------- | | type        | This must be &#x60;attach&#x60;                                               | | volume_name | The name of the block storage volume                                | | droplet_id  | Set to the Droplet&#39;s ID                                             | | region      | Set to the slug representing the region where the volume is located |  Each volume may only be attached to a single Droplet. However, up to five volumes may be attached to a Droplet at a time. Pre-formatted volumes will be automatically mounted to Ubuntu, Debian, Fedora, Fedora Atomic, and CentOS Droplets created on or after April 26, 2018 when attached. On older Droplets, [additional configuration](https://www.digitalocean.com/community/tutorials/how-to-partition-and-format-digitalocean-block-storage-volumes-in-linux#mounting-the-filesystems) is required.  ## Remove a Block Storage Volume from a Droplet  | Attribute   | Details                                                             | | ----------- | ------------------------------------------------------------------- | | type        | This must be &#x60;detach&#x60;                                               | | volume_name | The name of the block storage volume                                | | droplet_id  | Set to the Droplet&#39;s ID                                             | | region      | Set to the slug representing the region where the volume is located | 
     * @param body  (required)
     * @return PostRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The response will be an object with a key called &#x60;action&#x60;. The value of this will be an object that contains the standard volume action attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public PostRequestBuilder post() throws IllegalArgumentException {
        return new PostRequestBuilder();
    }
}
