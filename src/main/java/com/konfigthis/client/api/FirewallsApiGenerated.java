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


import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.FirewallAllOfPendingChanges;
import com.konfigthis.client.model.FirewallRulesInboundRulesInner;
import com.konfigthis.client.model.FirewallRulesOutboundRulesInner;
import com.konfigthis.client.model.FirewallsAddDropletsRequest;
import com.konfigthis.client.model.FirewallsAddRulesRequest;
import com.konfigthis.client.model.FirewallsAddTagsRequest;
import com.konfigthis.client.model.FirewallsCreateRequest;
import com.konfigthis.client.model.FirewallsCreateResponse;
import com.konfigthis.client.model.FirewallsGetResponse;
import com.konfigthis.client.model.FirewallsListResponse;
import com.konfigthis.client.model.FirewallsRemoveDropletsRequest;
import com.konfigthis.client.model.FirewallsRemoveRulesRequest;
import com.konfigthis.client.model.FirewallsRemoveTagsRequest;
import com.konfigthis.client.model.FirewallsUpdateRequest;
import com.konfigthis.client.model.FirewallsUpdateResponse;
import java.time.OffsetDateTime;
import java.util.UUID;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class FirewallsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public FirewallsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public FirewallsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call addDropletsCall(UUID firewallId, FirewallsAddDropletsRequest firewallsAddDropletsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsAddDropletsRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls/{firewall_id}/droplets"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call addDropletsValidateBeforeCall(UUID firewallId, FirewallsAddDropletsRequest firewallsAddDropletsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling addDroplets(Async)");
        }

        return addDropletsCall(firewallId, firewallsAddDropletsRequest, _callback);

    }


    private ApiResponse<Void> addDropletsWithHttpInfo(UUID firewallId, FirewallsAddDropletsRequest firewallsAddDropletsRequest) throws ApiException {
        okhttp3.Call localVarCall = addDropletsValidateBeforeCall(firewallId, firewallsAddDropletsRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call addDropletsAsync(UUID firewallId, FirewallsAddDropletsRequest firewallsAddDropletsRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = addDropletsValidateBeforeCall(firewallId, firewallsAddDropletsRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class AddDropletsRequestBuilder {
        private final List<Integer> dropletIds;
        private final UUID firewallId;

        private AddDropletsRequestBuilder(List<Integer> dropletIds, UUID firewallId) {
            this.dropletIds = dropletIds;
            this.firewallId = firewallId;
        }

        /**
         * Build call for addDroplets
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
            FirewallsAddDropletsRequest firewallsAddDropletsRequest = buildBodyParams();
            return addDropletsCall(firewallId, firewallsAddDropletsRequest, _callback);
        }

        private FirewallsAddDropletsRequest buildBodyParams() {
            FirewallsAddDropletsRequest firewallsAddDropletsRequest = new FirewallsAddDropletsRequest();
            firewallsAddDropletsRequest.dropletIds(this.dropletIds);
            return firewallsAddDropletsRequest;
        }

        /**
         * Execute addDroplets request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            FirewallsAddDropletsRequest firewallsAddDropletsRequest = buildBodyParams();
            addDropletsWithHttpInfo(firewallId, firewallsAddDropletsRequest);
        }

        /**
         * Execute addDroplets request with HTTP info returned
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
            FirewallsAddDropletsRequest firewallsAddDropletsRequest = buildBodyParams();
            return addDropletsWithHttpInfo(firewallId, firewallsAddDropletsRequest);
        }

        /**
         * Execute addDroplets request (asynchronously)
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
            FirewallsAddDropletsRequest firewallsAddDropletsRequest = buildBodyParams();
            return addDropletsAsync(firewallId, firewallsAddDropletsRequest, _callback);
        }
    }

    /**
     * Add Droplets to a Firewall
     * To assign a Droplet to a firewall, send a POST request to &#x60;/v2/firewalls/$FIREWALL_ID/droplets&#x60;. In the body of the request, there should be a &#x60;droplet_ids&#x60; attribute containing a list of Droplet IDs.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return AddDropletsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddDropletsRequestBuilder addDroplets(List<Integer> dropletIds, UUID firewallId) throws IllegalArgumentException {
        if (dropletIds == null) throw new IllegalArgumentException("\"dropletIds\" is required but got null");
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new AddDropletsRequestBuilder(dropletIds, firewallId);
    }
    private okhttp3.Call addRulesCall(UUID firewallId, FirewallsAddRulesRequest firewallsAddRulesRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsAddRulesRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls/{firewall_id}/rules"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call addRulesValidateBeforeCall(UUID firewallId, FirewallsAddRulesRequest firewallsAddRulesRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling addRules(Async)");
        }

        return addRulesCall(firewallId, firewallsAddRulesRequest, _callback);

    }


    private ApiResponse<Void> addRulesWithHttpInfo(UUID firewallId, FirewallsAddRulesRequest firewallsAddRulesRequest) throws ApiException {
        okhttp3.Call localVarCall = addRulesValidateBeforeCall(firewallId, firewallsAddRulesRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call addRulesAsync(UUID firewallId, FirewallsAddRulesRequest firewallsAddRulesRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = addRulesValidateBeforeCall(firewallId, firewallsAddRulesRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class AddRulesRequestBuilder {
        private final UUID firewallId;
        private List<FirewallRulesInboundRulesInner> inboundRules;
        private List<FirewallRulesOutboundRulesInner> outboundRules;
        private FirewallsAddRulesRequest firewallsAddRulesRequest;

        private AddRulesRequestBuilder(UUID firewallId) {
            this.firewallId = firewallId;
        }

        /**
         * Set firewallsAddRulesRequest
         * @param firewallsAddRulesRequest  (optional)
         * @return AddRulesRequestBuilder
         */
        public AddRulesRequestBuilder firewallsAddRulesRequest(FirewallsAddRulesRequest firewallsAddRulesRequest) {
            this.firewallsAddRulesRequest = firewallsAddRulesRequest;
            return this;
        }

        /**
         * Set inboundRules
         * @param inboundRules  (optional)
         * @return AddRulesRequestBuilder
         */
        public AddRulesRequestBuilder inboundRules(List<FirewallRulesInboundRulesInner> inboundRules) {
            this.inboundRules = inboundRules;
            return this;
        }
        
        /**
         * Set outboundRules
         * @param outboundRules  (optional)
         * @return AddRulesRequestBuilder
         */
        public AddRulesRequestBuilder outboundRules(List<FirewallRulesOutboundRulesInner> outboundRules) {
            this.outboundRules = outboundRules;
            return this;
        }
        
        /**
         * Build call for addRules
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
            FirewallsAddRulesRequest firewallsAddRulesRequest = buildBodyParams();
            return addRulesCall(firewallId, firewallsAddRulesRequest, _callback);
        }

        private FirewallsAddRulesRequest buildBodyParams() {
            return this.firewallsAddRulesRequest;
        }

        /**
         * Execute addRules request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            FirewallsAddRulesRequest firewallsAddRulesRequest = buildBodyParams();
            addRulesWithHttpInfo(firewallId, firewallsAddRulesRequest);
        }

        /**
         * Execute addRules request with HTTP info returned
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
            FirewallsAddRulesRequest firewallsAddRulesRequest = buildBodyParams();
            return addRulesWithHttpInfo(firewallId, firewallsAddRulesRequest);
        }

        /**
         * Execute addRules request (asynchronously)
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
            FirewallsAddRulesRequest firewallsAddRulesRequest = buildBodyParams();
            return addRulesAsync(firewallId, firewallsAddRulesRequest, _callback);
        }
    }

    /**
     * Add Rules to a Firewall
     * To add additional access rules to a firewall, send a POST request to &#x60;/v2/firewalls/$FIREWALL_ID/rules&#x60;. The body of the request may include an inbound_rules and/or outbound_rules attribute containing an array of rules to be added.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return AddRulesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddRulesRequestBuilder addRules(UUID firewallId) throws IllegalArgumentException {
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new AddRulesRequestBuilder(firewallId);
    }
    private okhttp3.Call addTagsCall(UUID firewallId, FirewallsAddTagsRequest firewallsAddTagsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsAddTagsRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls/{firewall_id}/tags"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call addTagsValidateBeforeCall(UUID firewallId, FirewallsAddTagsRequest firewallsAddTagsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling addTags(Async)");
        }

        return addTagsCall(firewallId, firewallsAddTagsRequest, _callback);

    }


    private ApiResponse<Void> addTagsWithHttpInfo(UUID firewallId, FirewallsAddTagsRequest firewallsAddTagsRequest) throws ApiException {
        okhttp3.Call localVarCall = addTagsValidateBeforeCall(firewallId, firewallsAddTagsRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call addTagsAsync(UUID firewallId, FirewallsAddTagsRequest firewallsAddTagsRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = addTagsValidateBeforeCall(firewallId, firewallsAddTagsRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class AddTagsRequestBuilder {
        private final List tags;
        private final UUID firewallId;

        private AddTagsRequestBuilder(List tags, UUID firewallId) {
            this.tags = tags;
            this.firewallId = firewallId;
        }

        /**
         * Build call for addTags
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
            FirewallsAddTagsRequest firewallsAddTagsRequest = buildBodyParams();
            return addTagsCall(firewallId, firewallsAddTagsRequest, _callback);
        }

        private FirewallsAddTagsRequest buildBodyParams() {
            FirewallsAddTagsRequest firewallsAddTagsRequest = new FirewallsAddTagsRequest();
            firewallsAddTagsRequest.tags(this.tags);
            return firewallsAddTagsRequest;
        }

        /**
         * Execute addTags request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            FirewallsAddTagsRequest firewallsAddTagsRequest = buildBodyParams();
            addTagsWithHttpInfo(firewallId, firewallsAddTagsRequest);
        }

        /**
         * Execute addTags request with HTTP info returned
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
            FirewallsAddTagsRequest firewallsAddTagsRequest = buildBodyParams();
            return addTagsWithHttpInfo(firewallId, firewallsAddTagsRequest);
        }

        /**
         * Execute addTags request (asynchronously)
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
            FirewallsAddTagsRequest firewallsAddTagsRequest = buildBodyParams();
            return addTagsAsync(firewallId, firewallsAddTagsRequest, _callback);
        }
    }

    /**
     * Add Tags to a Firewall
     * To assign a tag representing a group of Droplets to a firewall, send a POST request to &#x60;/v2/firewalls/$FIREWALL_ID/tags&#x60;. In the body of the request, there should be a &#x60;tags&#x60; attribute containing a list of tag names.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return AddTagsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddTagsRequestBuilder addTags(List tags, UUID firewallId) throws IllegalArgumentException {
        if (tags == null) throw new IllegalArgumentException("\"tags\" is required but got null");
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new AddTagsRequestBuilder(tags, firewallId);
    }
    private okhttp3.Call createCall(FirewallsCreateRequest firewallsCreateRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsCreateRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls";

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
    private okhttp3.Call createValidateBeforeCall(FirewallsCreateRequest firewallsCreateRequest, final ApiCallback _callback) throws ApiException {
        return createCall(firewallsCreateRequest, _callback);

    }


    private ApiResponse<FirewallsCreateResponse> createWithHttpInfo(FirewallsCreateRequest firewallsCreateRequest) throws ApiException {
        okhttp3.Call localVarCall = createValidateBeforeCall(firewallsCreateRequest, null);
        Type localVarReturnType = new TypeToken<FirewallsCreateResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createAsync(FirewallsCreateRequest firewallsCreateRequest, final ApiCallback<FirewallsCreateResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createValidateBeforeCall(firewallsCreateRequest, _callback);
        Type localVarReturnType = new TypeToken<FirewallsCreateResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateRequestBuilder {
        private List tags;
        private String id;
        private String status;
        private OffsetDateTime createdAt;
        private List<FirewallAllOfPendingChanges> pendingChanges;
        private String name;
        private List<Integer> dropletIds;
        private List<FirewallRulesInboundRulesInner> inboundRules;
        private List<FirewallRulesOutboundRulesInner> outboundRules;
        private FirewallsCreateRequest firewallsCreateRequest;

        private CreateRequestBuilder() {
        }

        /**
         * Set firewallsCreateRequest
         * @param firewallsCreateRequest  (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder firewallsCreateRequest(FirewallsCreateRequest firewallsCreateRequest) {
            this.firewallsCreateRequest = firewallsCreateRequest;
            return this;
        }

        /**
         * Set tags
         * @param tags  (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder tags(List tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a firewall. (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set status
         * @param status A status string indicating the current state of the firewall. This can be \\\&quot;waiting\\\&quot;, \\\&quot;succeeded\\\&quot;, or \\\&quot;failed\\\&quot;. (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder status(String status) {
            this.status = status;
            return this;
        }
        
        /**
         * Set createdAt
         * @param createdAt A time value given in ISO8601 combined date and time format that represents when the firewall was created. (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        /**
         * Set pendingChanges
         * @param pendingChanges An array of objects each containing the fields \\\&quot;droplet_id\\\&quot;, \\\&quot;removing\\\&quot;, and \\\&quot;status\\\&quot;. It is provided to detail exactly which Droplets are having their security policies updated. When empty, all changes have been successfully applied. (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder pendingChanges(List<FirewallAllOfPendingChanges> pendingChanges) {
            this.pendingChanges = pendingChanges;
            return this;
        }
        
        /**
         * Set name
         * @param name A human-readable name for a firewall. The name must begin with an alphanumeric character. Subsequent characters must either be alphanumeric characters, a period (.), or a dash (-). (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set dropletIds
         * @param dropletIds An array containing the IDs of the Droplets assigned to the firewall. (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder dropletIds(List<Integer> dropletIds) {
            this.dropletIds = dropletIds;
            return this;
        }
        
        /**
         * Set inboundRules
         * @param inboundRules  (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder inboundRules(List<FirewallRulesInboundRulesInner> inboundRules) {
            this.inboundRules = inboundRules;
            return this;
        }
        
        /**
         * Set outboundRules
         * @param outboundRules  (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder outboundRules(List<FirewallRulesOutboundRulesInner> outboundRules) {
            this.outboundRules = outboundRules;
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
            <tr><td> 202 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            FirewallsCreateRequest firewallsCreateRequest = buildBodyParams();
            return createCall(firewallsCreateRequest, _callback);
        }

        private FirewallsCreateRequest buildBodyParams() {
            return this.firewallsCreateRequest;
        }

        /**
         * Execute create request
         * @return FirewallsCreateResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FirewallsCreateResponse execute() throws ApiException {
            FirewallsCreateRequest firewallsCreateRequest = buildBodyParams();
            ApiResponse<FirewallsCreateResponse> localVarResp = createWithHttpInfo(firewallsCreateRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute create request with HTTP info returned
         * @return ApiResponse&lt;FirewallsCreateResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FirewallsCreateResponse> executeWithHttpInfo() throws ApiException {
            FirewallsCreateRequest firewallsCreateRequest = buildBodyParams();
            return createWithHttpInfo(firewallsCreateRequest);
        }

        /**
         * Execute create request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FirewallsCreateResponse> _callback) throws ApiException {
            FirewallsCreateRequest firewallsCreateRequest = buildBodyParams();
            return createAsync(firewallsCreateRequest, _callback);
        }
    }

    /**
     * Create a New Firewall
     * To create a new firewall, send a POST request to &#x60;/v2/firewalls&#x60;. The request must contain at least one inbound or outbound access rule. 
     * @return CreateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateRequestBuilder create() throws IllegalArgumentException {
        return new CreateRequestBuilder();
    }
    private okhttp3.Call deleteCall(UUID firewallId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/firewalls/{firewall_id}"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call deleteValidateBeforeCall(UUID firewallId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling delete(Async)");
        }

        return deleteCall(firewallId, _callback);

    }


    private ApiResponse<Void> deleteWithHttpInfo(UUID firewallId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(firewallId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAsync(UUID firewallId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(firewallId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final UUID firewallId;

        private DeleteRequestBuilder(UUID firewallId) {
            this.firewallId = firewallId;
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
            return deleteCall(firewallId, _callback);
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
            deleteWithHttpInfo(firewallId);
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
            return deleteWithHttpInfo(firewallId);
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
            return deleteAsync(firewallId, _callback);
        }
    }

    /**
     * Delete a Firewall
     * To delete a firewall send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID&#x60;.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(UUID firewallId) throws IllegalArgumentException {
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new DeleteRequestBuilder(firewallId);
    }
    private okhttp3.Call getCall(UUID firewallId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/firewalls/{firewall_id}"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call getValidateBeforeCall(UUID firewallId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling get(Async)");
        }

        return getCall(firewallId, _callback);

    }


    private ApiResponse<FirewallsGetResponse> getWithHttpInfo(UUID firewallId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(firewallId, null);
        Type localVarReturnType = new TypeToken<FirewallsGetResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(UUID firewallId, final ApiCallback<FirewallsGetResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(firewallId, _callback);
        Type localVarReturnType = new TypeToken<FirewallsGetResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final UUID firewallId;

        private GetRequestBuilder(UUID firewallId) {
            this.firewallId = firewallId;
        }

        /**
         * Build call for get
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(firewallId, _callback);
        }


        /**
         * Execute get request
         * @return FirewallsGetResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FirewallsGetResponse execute() throws ApiException {
            ApiResponse<FirewallsGetResponse> localVarResp = getWithHttpInfo(firewallId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;FirewallsGetResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FirewallsGetResponse> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(firewallId);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FirewallsGetResponse> _callback) throws ApiException {
            return getAsync(firewallId, _callback);
        }
    }

    /**
     * Retrieve an Existing Firewall
     * To show information about an existing firewall, send a GET request to &#x60;/v2/firewalls/$FIREWALL_ID&#x60;.
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a firewall key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetRequestBuilder get(UUID firewallId) throws IllegalArgumentException {
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new GetRequestBuilder(firewallId);
    }
    private okhttp3.Call listCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/firewalls";

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
    private okhttp3.Call listValidateBeforeCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        return listCall(perPage, page, _callback);

    }


    private ApiResponse<FirewallsListResponse> listWithHttpInfo(Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(perPage, page, null);
        Type localVarReturnType = new TypeToken<FirewallsListResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(Integer perPage, Integer page, final ApiCallback<FirewallsListResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(perPage, page, _callback);
        Type localVarReturnType = new TypeToken<FirewallsListResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private Integer perPage;
        private Integer page;

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
         * Build call for list
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(perPage, page, _callback);
        }


        /**
         * Execute list request
         * @return FirewallsListResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FirewallsListResponse execute() throws ApiException {
            ApiResponse<FirewallsListResponse> localVarResp = listWithHttpInfo(perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;FirewallsListResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FirewallsListResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(perPage, page);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FirewallsListResponse> _callback) throws ApiException {
            return listAsync(perPage, page, _callback);
        }
    }

    /**
     * List All Firewalls
     * To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;.
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> To list all of the firewalls available on your account, send a GET request to &#x60;/v2/firewalls&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRequestBuilder list() throws IllegalArgumentException {
        return new ListRequestBuilder();
    }
    private okhttp3.Call removeDropletsCall(UUID firewallId, FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsRemoveDropletsRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls/{firewall_id}/droplets"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call removeDropletsValidateBeforeCall(UUID firewallId, FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling removeDroplets(Async)");
        }

        return removeDropletsCall(firewallId, firewallsRemoveDropletsRequest, _callback);

    }


    private ApiResponse<Void> removeDropletsWithHttpInfo(UUID firewallId, FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest) throws ApiException {
        okhttp3.Call localVarCall = removeDropletsValidateBeforeCall(firewallId, firewallsRemoveDropletsRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call removeDropletsAsync(UUID firewallId, FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = removeDropletsValidateBeforeCall(firewallId, firewallsRemoveDropletsRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RemoveDropletsRequestBuilder {
        private final List<Integer> dropletIds;
        private final UUID firewallId;

        private RemoveDropletsRequestBuilder(List<Integer> dropletIds, UUID firewallId) {
            this.dropletIds = dropletIds;
            this.firewallId = firewallId;
        }

        /**
         * Build call for removeDroplets
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
            FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest = buildBodyParams();
            return removeDropletsCall(firewallId, firewallsRemoveDropletsRequest, _callback);
        }

        private FirewallsRemoveDropletsRequest buildBodyParams() {
            FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest = new FirewallsRemoveDropletsRequest();
            firewallsRemoveDropletsRequest.dropletIds(this.dropletIds);
            return firewallsRemoveDropletsRequest;
        }

        /**
         * Execute removeDroplets request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest = buildBodyParams();
            removeDropletsWithHttpInfo(firewallId, firewallsRemoveDropletsRequest);
        }

        /**
         * Execute removeDroplets request with HTTP info returned
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
            FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest = buildBodyParams();
            return removeDropletsWithHttpInfo(firewallId, firewallsRemoveDropletsRequest);
        }

        /**
         * Execute removeDroplets request (asynchronously)
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
            FirewallsRemoveDropletsRequest firewallsRemoveDropletsRequest = buildBodyParams();
            return removeDropletsAsync(firewallId, firewallsRemoveDropletsRequest, _callback);
        }
    }

    /**
     * Remove Droplets from a Firewall
     * To remove a Droplet from a firewall, send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID/droplets&#x60;. In the body of the request, there should be a &#x60;droplet_ids&#x60; attribute containing a list of Droplet IDs.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return RemoveDropletsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RemoveDropletsRequestBuilder removeDroplets(List<Integer> dropletIds, UUID firewallId) throws IllegalArgumentException {
        if (dropletIds == null) throw new IllegalArgumentException("\"dropletIds\" is required but got null");
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new RemoveDropletsRequestBuilder(dropletIds, firewallId);
    }
    private okhttp3.Call removeRulesCall(UUID firewallId, FirewallsRemoveRulesRequest firewallsRemoveRulesRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsRemoveRulesRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls/{firewall_id}/rules"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call removeRulesValidateBeforeCall(UUID firewallId, FirewallsRemoveRulesRequest firewallsRemoveRulesRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling removeRules(Async)");
        }

        return removeRulesCall(firewallId, firewallsRemoveRulesRequest, _callback);

    }


    private ApiResponse<Void> removeRulesWithHttpInfo(UUID firewallId, FirewallsRemoveRulesRequest firewallsRemoveRulesRequest) throws ApiException {
        okhttp3.Call localVarCall = removeRulesValidateBeforeCall(firewallId, firewallsRemoveRulesRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call removeRulesAsync(UUID firewallId, FirewallsRemoveRulesRequest firewallsRemoveRulesRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = removeRulesValidateBeforeCall(firewallId, firewallsRemoveRulesRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RemoveRulesRequestBuilder {
        private final UUID firewallId;
        private List<FirewallRulesInboundRulesInner> inboundRules;
        private List<FirewallRulesOutboundRulesInner> outboundRules;
        private FirewallsRemoveRulesRequest firewallsRemoveRulesRequest;

        private RemoveRulesRequestBuilder(UUID firewallId) {
            this.firewallId = firewallId;
        }

        /**
         * Set firewallsRemoveRulesRequest
         * @param firewallsRemoveRulesRequest  (optional)
         * @return RemoveRulesRequestBuilder
         */
        public RemoveRulesRequestBuilder firewallsRemoveRulesRequest(FirewallsRemoveRulesRequest firewallsRemoveRulesRequest) {
            this.firewallsRemoveRulesRequest = firewallsRemoveRulesRequest;
            return this;
        }

        /**
         * Set inboundRules
         * @param inboundRules  (optional)
         * @return RemoveRulesRequestBuilder
         */
        public RemoveRulesRequestBuilder inboundRules(List<FirewallRulesInboundRulesInner> inboundRules) {
            this.inboundRules = inboundRules;
            return this;
        }
        
        /**
         * Set outboundRules
         * @param outboundRules  (optional)
         * @return RemoveRulesRequestBuilder
         */
        public RemoveRulesRequestBuilder outboundRules(List<FirewallRulesOutboundRulesInner> outboundRules) {
            this.outboundRules = outboundRules;
            return this;
        }
        
        /**
         * Build call for removeRules
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
            FirewallsRemoveRulesRequest firewallsRemoveRulesRequest = buildBodyParams();
            return removeRulesCall(firewallId, firewallsRemoveRulesRequest, _callback);
        }

        private FirewallsRemoveRulesRequest buildBodyParams() {
            return this.firewallsRemoveRulesRequest;
        }

        /**
         * Execute removeRules request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            FirewallsRemoveRulesRequest firewallsRemoveRulesRequest = buildBodyParams();
            removeRulesWithHttpInfo(firewallId, firewallsRemoveRulesRequest);
        }

        /**
         * Execute removeRules request with HTTP info returned
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
            FirewallsRemoveRulesRequest firewallsRemoveRulesRequest = buildBodyParams();
            return removeRulesWithHttpInfo(firewallId, firewallsRemoveRulesRequest);
        }

        /**
         * Execute removeRules request (asynchronously)
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
            FirewallsRemoveRulesRequest firewallsRemoveRulesRequest = buildBodyParams();
            return removeRulesAsync(firewallId, firewallsRemoveRulesRequest, _callback);
        }
    }

    /**
     * Remove Rules from a Firewall
     * To remove access rules from a firewall, send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID/rules&#x60;. The body of the request may include an &#x60;inbound_rules&#x60; and/or &#x60;outbound_rules&#x60; attribute containing an array of rules to be removed.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return RemoveRulesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RemoveRulesRequestBuilder removeRules(UUID firewallId) throws IllegalArgumentException {
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new RemoveRulesRequestBuilder(firewallId);
    }
    private okhttp3.Call removeTagsCall(UUID firewallId, FirewallsRemoveTagsRequest firewallsRemoveTagsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsRemoveTagsRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls/{firewall_id}/tags"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call removeTagsValidateBeforeCall(UUID firewallId, FirewallsRemoveTagsRequest firewallsRemoveTagsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling removeTags(Async)");
        }

        return removeTagsCall(firewallId, firewallsRemoveTagsRequest, _callback);

    }


    private ApiResponse<Void> removeTagsWithHttpInfo(UUID firewallId, FirewallsRemoveTagsRequest firewallsRemoveTagsRequest) throws ApiException {
        okhttp3.Call localVarCall = removeTagsValidateBeforeCall(firewallId, firewallsRemoveTagsRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call removeTagsAsync(UUID firewallId, FirewallsRemoveTagsRequest firewallsRemoveTagsRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = removeTagsValidateBeforeCall(firewallId, firewallsRemoveTagsRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RemoveTagsRequestBuilder {
        private final List tags;
        private final UUID firewallId;

        private RemoveTagsRequestBuilder(List tags, UUID firewallId) {
            this.tags = tags;
            this.firewallId = firewallId;
        }

        /**
         * Build call for removeTags
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
            FirewallsRemoveTagsRequest firewallsRemoveTagsRequest = buildBodyParams();
            return removeTagsCall(firewallId, firewallsRemoveTagsRequest, _callback);
        }

        private FirewallsRemoveTagsRequest buildBodyParams() {
            FirewallsRemoveTagsRequest firewallsRemoveTagsRequest = new FirewallsRemoveTagsRequest();
            firewallsRemoveTagsRequest.tags(this.tags);
            return firewallsRemoveTagsRequest;
        }

        /**
         * Execute removeTags request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            FirewallsRemoveTagsRequest firewallsRemoveTagsRequest = buildBodyParams();
            removeTagsWithHttpInfo(firewallId, firewallsRemoveTagsRequest);
        }

        /**
         * Execute removeTags request with HTTP info returned
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
            FirewallsRemoveTagsRequest firewallsRemoveTagsRequest = buildBodyParams();
            return removeTagsWithHttpInfo(firewallId, firewallsRemoveTagsRequest);
        }

        /**
         * Execute removeTags request (asynchronously)
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
            FirewallsRemoveTagsRequest firewallsRemoveTagsRequest = buildBodyParams();
            return removeTagsAsync(firewallId, firewallsRemoveTagsRequest, _callback);
        }
    }

    /**
     * Remove Tags from a Firewall
     * To remove a tag representing a group of Droplets from a firewall, send a DELETE request to &#x60;/v2/firewalls/$FIREWALL_ID/tags&#x60;. In the body of the request, there should be a &#x60;tags&#x60; attribute containing a list of tag names.  No response body will be sent back, but the response code will indicate success. Specifically, the response code will be a 204, which means that the action was successful with no returned body data. 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return RemoveTagsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RemoveTagsRequestBuilder removeTags(List tags, UUID firewallId) throws IllegalArgumentException {
        if (tags == null) throw new IllegalArgumentException("\"tags\" is required but got null");
        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new RemoveTagsRequestBuilder(tags, firewallId);
    }
    private okhttp3.Call updateCall(UUID firewallId, FirewallsUpdateRequest firewallsUpdateRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = firewallsUpdateRequest;

        // create path and map variables
        String localVarPath = "/v2/firewalls/{firewall_id}"
            .replace("{" + "firewall_id" + "}", localVarApiClient.escapeString(firewallId.toString()));

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
    private okhttp3.Call updateValidateBeforeCall(UUID firewallId, FirewallsUpdateRequest firewallsUpdateRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'firewallId' is set
        if (firewallId == null) {
            throw new ApiException("Missing the required parameter 'firewallId' when calling update(Async)");
        }

        return updateCall(firewallId, firewallsUpdateRequest, _callback);

    }


    private ApiResponse<FirewallsUpdateResponse> updateWithHttpInfo(UUID firewallId, FirewallsUpdateRequest firewallsUpdateRequest) throws ApiException {
        okhttp3.Call localVarCall = updateValidateBeforeCall(firewallId, firewallsUpdateRequest, null);
        Type localVarReturnType = new TypeToken<FirewallsUpdateResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAsync(UUID firewallId, FirewallsUpdateRequest firewallsUpdateRequest, final ApiCallback<FirewallsUpdateResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateValidateBeforeCall(firewallId, firewallsUpdateRequest, _callback);
        Type localVarReturnType = new TypeToken<FirewallsUpdateResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateRequestBuilder {
        private final String name;
        private final UUID firewallId;
        private List tags;
        private String id;
        private String status;
        private OffsetDateTime createdAt;
        private List<FirewallAllOfPendingChanges> pendingChanges;
        private List<Integer> dropletIds;
        private List<FirewallRulesInboundRulesInner> inboundRules;
        private List<FirewallRulesOutboundRulesInner> outboundRules;
        private FirewallsUpdateRequest firewallsUpdateRequest;

        private UpdateRequestBuilder(String name, UUID firewallId) {
            this.name = name;
            this.firewallId = firewallId;
        }

        /**
         * Set firewallsUpdateRequest
         * @param firewallsUpdateRequest  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder firewallsUpdateRequest(FirewallsUpdateRequest firewallsUpdateRequest) {
            this.firewallsUpdateRequest = firewallsUpdateRequest;
            return this;
        }

        /**
         * Set tags
         * @param tags  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder tags(List tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a firewall. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set status
         * @param status A status string indicating the current state of the firewall. This can be \\\&quot;waiting\\\&quot;, \\\&quot;succeeded\\\&quot;, or \\\&quot;failed\\\&quot;. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder status(String status) {
            this.status = status;
            return this;
        }
        
        /**
         * Set createdAt
         * @param createdAt A time value given in ISO8601 combined date and time format that represents when the firewall was created. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        /**
         * Set pendingChanges
         * @param pendingChanges An array of objects each containing the fields \\\&quot;droplet_id\\\&quot;, \\\&quot;removing\\\&quot;, and \\\&quot;status\\\&quot;. It is provided to detail exactly which Droplets are having their security policies updated. When empty, all changes have been successfully applied. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder pendingChanges(List<FirewallAllOfPendingChanges> pendingChanges) {
            this.pendingChanges = pendingChanges;
            return this;
        }
        
        /**
         * Set dropletIds
         * @param dropletIds An array containing the IDs of the Droplets assigned to the firewall. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder dropletIds(List<Integer> dropletIds) {
            this.dropletIds = dropletIds;
            return this;
        }
        
        /**
         * Set inboundRules
         * @param inboundRules  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder inboundRules(List<FirewallRulesInboundRulesInner> inboundRules) {
            this.inboundRules = inboundRules;
            return this;
        }
        
        /**
         * Set outboundRules
         * @param outboundRules  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder outboundRules(List<FirewallRulesOutboundRulesInner> outboundRules) {
            this.outboundRules = outboundRules;
            return this;
        }
        
        /**
         * Build call for update
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a &#x60;firewall&#x60; key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            FirewallsUpdateRequest firewallsUpdateRequest = buildBodyParams();
            return updateCall(firewallId, firewallsUpdateRequest, _callback);
        }

        private FirewallsUpdateRequest buildBodyParams() {
            return this.firewallsUpdateRequest;
        }

        /**
         * Execute update request
         * @return FirewallsUpdateResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a &#x60;firewall&#x60; key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FirewallsUpdateResponse execute() throws ApiException {
            FirewallsUpdateRequest firewallsUpdateRequest = buildBodyParams();
            ApiResponse<FirewallsUpdateResponse> localVarResp = updateWithHttpInfo(firewallId, firewallsUpdateRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute update request with HTTP info returned
         * @return ApiResponse&lt;FirewallsUpdateResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a &#x60;firewall&#x60; key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FirewallsUpdateResponse> executeWithHttpInfo() throws ApiException {
            FirewallsUpdateRequest firewallsUpdateRequest = buildBodyParams();
            return updateWithHttpInfo(firewallId, firewallsUpdateRequest);
        }

        /**
         * Execute update request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a &#x60;firewall&#x60; key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FirewallsUpdateResponse> _callback) throws ApiException {
            FirewallsUpdateRequest firewallsUpdateRequest = buildBodyParams();
            return updateAsync(firewallId, firewallsUpdateRequest, _callback);
        }
    }

    /**
     * Update a Firewall
     * To update the configuration of an existing firewall, send a PUT request to &#x60;/v2/firewalls/$FIREWALL_ID&#x60;. The request should contain a full representation of the firewall including existing attributes. **Note that any attributes that are not provided will be reset to their default values.** 
     * @param firewallId A unique ID that can be used to identify and reference a firewall. (required)
     * @return UpdateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a &#x60;firewall&#x60; key. This will be set to an object containing the standard firewall attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateRequestBuilder update(String name, UUID firewallId) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (firewallId == null) throw new IllegalArgumentException("\"firewallId\" is required but got null");
            

        return new UpdateRequestBuilder(name, firewallId);
    }
}
