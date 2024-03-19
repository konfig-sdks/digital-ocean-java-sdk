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


import com.konfigthis.client.model.ContainerRegistryGetActiveGarbageCollectionResponse;
import com.konfigthis.client.model.ContainerRegistryListGarbageCollectionsResponse;
import com.konfigthis.client.model.ContainerRegistryListOptionsResponse;
import com.konfigthis.client.model.ContainerRegistryListRepositoriesResponse;
import com.konfigthis.client.model.ContainerRegistryListRepositoriesV2Response;
import com.konfigthis.client.model.ContainerRegistryListRepositoryManifestsResponse;
import com.konfigthis.client.model.ContainerRegistryListRepositoryTagsResponse;
import com.konfigthis.client.model.ContainerRegistryUpdateSubscriptionTierRequest;
import com.konfigthis.client.model.DockerCredentials;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.RegistryCreate;
import com.konfigthis.client.model.UpdateRegistry;
import com.konfigthis.client.model.ValidateRegistry;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class ContainerRegistryApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ContainerRegistryApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public ContainerRegistryApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call cancelGarbageCollectionCall(String registryName, String garbageCollectionUuid, UpdateRegistry updateRegistry, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = updateRegistry;

        // create path and map variables
        String localVarPath = "/v2/registry/{registry_name}/garbage-collection/{garbage_collection_uuid}"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()))
            .replace("{" + "garbage_collection_uuid" + "}", localVarApiClient.escapeString(garbageCollectionUuid.toString()));

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
    private okhttp3.Call cancelGarbageCollectionValidateBeforeCall(String registryName, String garbageCollectionUuid, UpdateRegistry updateRegistry, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling cancelGarbageCollection(Async)");
        }

        // verify the required parameter 'garbageCollectionUuid' is set
        if (garbageCollectionUuid == null) {
            throw new ApiException("Missing the required parameter 'garbageCollectionUuid' when calling cancelGarbageCollection(Async)");
        }

        // verify the required parameter 'updateRegistry' is set
        if (updateRegistry == null) {
            throw new ApiException("Missing the required parameter 'updateRegistry' when calling cancelGarbageCollection(Async)");
        }

        return cancelGarbageCollectionCall(registryName, garbageCollectionUuid, updateRegistry, _callback);

    }


    private ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> cancelGarbageCollectionWithHttpInfo(String registryName, String garbageCollectionUuid, UpdateRegistry updateRegistry) throws ApiException {
        okhttp3.Call localVarCall = cancelGarbageCollectionValidateBeforeCall(registryName, garbageCollectionUuid, updateRegistry, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryGetActiveGarbageCollectionResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call cancelGarbageCollectionAsync(String registryName, String garbageCollectionUuid, UpdateRegistry updateRegistry, final ApiCallback<ContainerRegistryGetActiveGarbageCollectionResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = cancelGarbageCollectionValidateBeforeCall(registryName, garbageCollectionUuid, updateRegistry, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryGetActiveGarbageCollectionResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CancelGarbageCollectionRequestBuilder {
        private final String registryName;
        private final String garbageCollectionUuid;
        private Boolean cancel;

        private CancelGarbageCollectionRequestBuilder(String registryName, String garbageCollectionUuid) {
            this.registryName = registryName;
            this.garbageCollectionUuid = garbageCollectionUuid;
        }

        /**
         * Set cancel
         * @param cancel A boolean value indicating that the garbage collection should be cancelled. (optional)
         * @return CancelGarbageCollectionRequestBuilder
         */
        public CancelGarbageCollectionRequestBuilder cancel(Boolean cancel) {
            this.cancel = cancel;
            return this;
        }
        
        /**
         * Build call for cancelGarbageCollection
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            UpdateRegistry updateRegistry = buildBodyParams();
            return cancelGarbageCollectionCall(registryName, garbageCollectionUuid, updateRegistry, _callback);
        }

        private UpdateRegistry buildBodyParams() {
            UpdateRegistry updateRegistry = new UpdateRegistry();
            updateRegistry.cancel(this.cancel);
            return updateRegistry;
        }

        /**
         * Execute cancelGarbageCollection request
         * @return ContainerRegistryGetActiveGarbageCollectionResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryGetActiveGarbageCollectionResponse execute() throws ApiException {
            UpdateRegistry updateRegistry = buildBodyParams();
            ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> localVarResp = cancelGarbageCollectionWithHttpInfo(registryName, garbageCollectionUuid, updateRegistry);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute cancelGarbageCollection request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryGetActiveGarbageCollectionResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> executeWithHttpInfo() throws ApiException {
            UpdateRegistry updateRegistry = buildBodyParams();
            return cancelGarbageCollectionWithHttpInfo(registryName, garbageCollectionUuid, updateRegistry);
        }

        /**
         * Execute cancelGarbageCollection request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryGetActiveGarbageCollectionResponse> _callback) throws ApiException {
            UpdateRegistry updateRegistry = buildBodyParams();
            return cancelGarbageCollectionAsync(registryName, garbageCollectionUuid, updateRegistry, _callback);
        }
    }

    /**
     * Update Garbage Collection
     * To cancel the currently-active garbage collection for a registry, send a PUT request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collection/$GC_UUID&#x60; and specify one or more of the attributes below.
     * @param registryName The name of a container registry. (required)
     * @param garbageCollectionUuid The UUID of a garbage collection run. (required)
     * @param updateRegistry  (required)
     * @return CancelGarbageCollectionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CancelGarbageCollectionRequestBuilder cancelGarbageCollection(String registryName, String garbageCollectionUuid) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        if (garbageCollectionUuid == null) throw new IllegalArgumentException("\"garbageCollectionUuid\" is required but got null");
            

        return new CancelGarbageCollectionRequestBuilder(registryName, garbageCollectionUuid);
    }
    private okhttp3.Call createCall(RegistryCreate registryCreate, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = registryCreate;

        // create path and map variables
        String localVarPath = "/v2/registry";

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
    private okhttp3.Call createValidateBeforeCall(RegistryCreate registryCreate, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryCreate' is set
        if (registryCreate == null) {
            throw new ApiException("Missing the required parameter 'registryCreate' when calling create(Async)");
        }

        return createCall(registryCreate, _callback);

    }


    private ApiResponse<Object> createWithHttpInfo(RegistryCreate registryCreate) throws ApiException {
        okhttp3.Call localVarCall = createValidateBeforeCall(registryCreate, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createAsync(RegistryCreate registryCreate, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = createValidateBeforeCall(registryCreate, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateRequestBuilder {
        private final String name;
        private final String subscriptionTierSlug;
        private String region;

        private CreateRequestBuilder(String name, String subscriptionTierSlug) {
            this.name = name;
            this.subscriptionTierSlug = subscriptionTierSlug;
        }

        /**
         * Set region
         * @param region Slug of the region where registry data is stored. When not provided, a region will be selected. (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder region(String region) {
            this.region = region;
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
            <tr><td> 201 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            RegistryCreate registryCreate = buildBodyParams();
            return createCall(registryCreate, _callback);
        }

        private RegistryCreate buildBodyParams() {
            RegistryCreate registryCreate = new RegistryCreate();
            registryCreate.name(this.name);
            if (this.subscriptionTierSlug != null)
            registryCreate.subscriptionTierSlug(RegistryCreate.SubscriptionTierSlugEnum.fromValue(this.subscriptionTierSlug));
            if (this.region != null)
            registryCreate.region(RegistryCreate.RegionEnum.fromValue(this.region));
            return registryCreate;
        }

        /**
         * Execute create request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            RegistryCreate registryCreate = buildBodyParams();
            ApiResponse<Object> localVarResp = createWithHttpInfo(registryCreate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute create request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            RegistryCreate registryCreate = buildBodyParams();
            return createWithHttpInfo(registryCreate);
        }

        /**
         * Execute create request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            RegistryCreate registryCreate = buildBodyParams();
            return createAsync(registryCreate, _callback);
        }
    }

    /**
     * Create Container Registry
     * To create your container registry, send a POST request to &#x60;/v2/registry&#x60;.  The &#x60;name&#x60; becomes part of the URL for images stored in the registry. For example, if your registry is called &#x60;example&#x60;, an image in it will have the URL &#x60;registry.digitalocean.com/example/image:tag&#x60;. 
     * @param registryCreate  (required)
     * @return CreateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateRequestBuilder create(String name, String subscriptionTierSlug) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (subscriptionTierSlug == null) throw new IllegalArgumentException("\"subscriptionTierSlug\" is required but got null");
            

        return new CreateRequestBuilder(name, subscriptionTierSlug);
    }
    private okhttp3.Call deleteCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry";

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
    private okhttp3.Call deleteValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return deleteCall(_callback);

    }


    private ApiResponse<Void> deleteWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAsync(final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(_callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {

        private DeleteRequestBuilder() {
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
            return deleteCall(_callback);
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
            deleteWithHttpInfo();
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
            return deleteWithHttpInfo();
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
            return deleteAsync(_callback);
        }
    }

    /**
     * Delete Container Registry
     * To delete your container registry, destroying all container image data stored in it, send a DELETE request to &#x60;/v2/registry&#x60;.
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete() throws IllegalArgumentException {
        return new DeleteRequestBuilder();
    }
    private okhttp3.Call deleteRepositoryManifestByDigestCall(String registryName, String repositoryName, String manifestDigest, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/repositories/{repository_name}/digests/{manifest_digest}"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()))
            .replace("{" + "repository_name" + "}", localVarApiClient.escapeString(repositoryName.toString()))
            .replace("{" + "manifest_digest" + "}", localVarApiClient.escapeString(manifestDigest.toString()));

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
    private okhttp3.Call deleteRepositoryManifestByDigestValidateBeforeCall(String registryName, String repositoryName, String manifestDigest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling deleteRepositoryManifestByDigest(Async)");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null) {
            throw new ApiException("Missing the required parameter 'repositoryName' when calling deleteRepositoryManifestByDigest(Async)");
        }

        // verify the required parameter 'manifestDigest' is set
        if (manifestDigest == null) {
            throw new ApiException("Missing the required parameter 'manifestDigest' when calling deleteRepositoryManifestByDigest(Async)");
        }

        return deleteRepositoryManifestByDigestCall(registryName, repositoryName, manifestDigest, _callback);

    }


    private ApiResponse<Void> deleteRepositoryManifestByDigestWithHttpInfo(String registryName, String repositoryName, String manifestDigest) throws ApiException {
        okhttp3.Call localVarCall = deleteRepositoryManifestByDigestValidateBeforeCall(registryName, repositoryName, manifestDigest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteRepositoryManifestByDigestAsync(String registryName, String repositoryName, String manifestDigest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteRepositoryManifestByDigestValidateBeforeCall(registryName, repositoryName, manifestDigest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteRepositoryManifestByDigestRequestBuilder {
        private final String registryName;
        private final String repositoryName;
        private final String manifestDigest;

        private DeleteRepositoryManifestByDigestRequestBuilder(String registryName, String repositoryName, String manifestDigest) {
            this.registryName = registryName;
            this.repositoryName = repositoryName;
            this.manifestDigest = manifestDigest;
        }

        /**
         * Build call for deleteRepositoryManifestByDigest
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
            return deleteRepositoryManifestByDigestCall(registryName, repositoryName, manifestDigest, _callback);
        }


        /**
         * Execute deleteRepositoryManifestByDigest request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteRepositoryManifestByDigestWithHttpInfo(registryName, repositoryName, manifestDigest);
        }

        /**
         * Execute deleteRepositoryManifestByDigest request with HTTP info returned
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
            return deleteRepositoryManifestByDigestWithHttpInfo(registryName, repositoryName, manifestDigest);
        }

        /**
         * Execute deleteRepositoryManifestByDigest request (asynchronously)
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
            return deleteRepositoryManifestByDigestAsync(registryName, repositoryName, manifestDigest, _callback);
        }
    }

    /**
     * Delete Container Registry Repository Manifest
     * To delete a container repository manifest by digest, send a DELETE request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/digests/$MANIFEST_DIGEST&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to delete &#x60;registry.digitalocean.com/example/my/repo@sha256:abcd&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/digests/sha256:abcd&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 
     * @param registryName The name of a container registry. (required)
     * @param repositoryName The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. (required)
     * @param manifestDigest The manifest digest of a container registry repository tag. (required)
     * @return DeleteRepositoryManifestByDigestRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteRepositoryManifestByDigestRequestBuilder deleteRepositoryManifestByDigest(String registryName, String repositoryName, String manifestDigest) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        if (repositoryName == null) throw new IllegalArgumentException("\"repositoryName\" is required but got null");
            

        if (manifestDigest == null) throw new IllegalArgumentException("\"manifestDigest\" is required but got null");
            

        return new DeleteRepositoryManifestByDigestRequestBuilder(registryName, repositoryName, manifestDigest);
    }
    private okhttp3.Call deleteRepositoryTagCall(String registryName, String repositoryName, String repositoryTag, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/repositories/{repository_name}/tags/{repository_tag}"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()))
            .replace("{" + "repository_name" + "}", localVarApiClient.escapeString(repositoryName.toString()))
            .replace("{" + "repository_tag" + "}", localVarApiClient.escapeString(repositoryTag.toString()));

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
    private okhttp3.Call deleteRepositoryTagValidateBeforeCall(String registryName, String repositoryName, String repositoryTag, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling deleteRepositoryTag(Async)");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null) {
            throw new ApiException("Missing the required parameter 'repositoryName' when calling deleteRepositoryTag(Async)");
        }

        // verify the required parameter 'repositoryTag' is set
        if (repositoryTag == null) {
            throw new ApiException("Missing the required parameter 'repositoryTag' when calling deleteRepositoryTag(Async)");
        }

        return deleteRepositoryTagCall(registryName, repositoryName, repositoryTag, _callback);

    }


    private ApiResponse<Void> deleteRepositoryTagWithHttpInfo(String registryName, String repositoryName, String repositoryTag) throws ApiException {
        okhttp3.Call localVarCall = deleteRepositoryTagValidateBeforeCall(registryName, repositoryName, repositoryTag, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteRepositoryTagAsync(String registryName, String repositoryName, String repositoryTag, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteRepositoryTagValidateBeforeCall(registryName, repositoryName, repositoryTag, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteRepositoryTagRequestBuilder {
        private final String registryName;
        private final String repositoryName;
        private final String repositoryTag;

        private DeleteRepositoryTagRequestBuilder(String registryName, String repositoryName, String repositoryTag) {
            this.registryName = registryName;
            this.repositoryName = repositoryName;
            this.repositoryTag = repositoryTag;
        }

        /**
         * Build call for deleteRepositoryTag
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
            return deleteRepositoryTagCall(registryName, repositoryName, repositoryTag, _callback);
        }


        /**
         * Execute deleteRepositoryTag request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteRepositoryTagWithHttpInfo(registryName, repositoryName, repositoryTag);
        }

        /**
         * Execute deleteRepositoryTag request with HTTP info returned
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
            return deleteRepositoryTagWithHttpInfo(registryName, repositoryName, repositoryTag);
        }

        /**
         * Execute deleteRepositoryTag request (asynchronously)
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
            return deleteRepositoryTagAsync(registryName, repositoryName, repositoryTag, _callback);
        }
    }

    /**
     * Delete Container Registry Repository Tag
     * To delete a container repository tag, send a DELETE request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/tags/$TAG&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to delete &#x60;registry.digitalocean.com/example/my/repo:mytag&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/tags/mytag&#x60;.  A successful request will receive a 204 status code with no body in response. This indicates that the request was processed successfully. 
     * @param registryName The name of a container registry. (required)
     * @param repositoryName The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. (required)
     * @param repositoryTag The name of a container registry repository tag. (required)
     * @return DeleteRepositoryTagRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteRepositoryTagRequestBuilder deleteRepositoryTag(String registryName, String repositoryName, String repositoryTag) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        if (repositoryName == null) throw new IllegalArgumentException("\"repositoryName\" is required but got null");
            

        if (repositoryTag == null) throw new IllegalArgumentException("\"repositoryTag\" is required but got null");
            

        return new DeleteRepositoryTagRequestBuilder(registryName, repositoryName, repositoryTag);
    }
    private okhttp3.Call getCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry";

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
    private okhttp3.Call getValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return getCall(_callback);

    }


    private ApiResponse<Object> getWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {

        private GetRequestBuilder() {
        }

        /**
         * Build call for get
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(_callback);
        }


        /**
         * Execute get request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ApiResponse<Object> localVarResp = getWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo();
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            return getAsync(_callback);
        }
    }

    /**
     * Get Container Registry Information
     * To get information about your container registry, send a GET request to &#x60;/v2/registry&#x60;.
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with the key &#x60;registry&#x60; containing information about your registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetRequestBuilder get() throws IllegalArgumentException {
        return new GetRequestBuilder();
    }
    private okhttp3.Call getActiveGarbageCollectionCall(String registryName, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/garbage-collection"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()));

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
    private okhttp3.Call getActiveGarbageCollectionValidateBeforeCall(String registryName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling getActiveGarbageCollection(Async)");
        }

        return getActiveGarbageCollectionCall(registryName, _callback);

    }


    private ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> getActiveGarbageCollectionWithHttpInfo(String registryName) throws ApiException {
        okhttp3.Call localVarCall = getActiveGarbageCollectionValidateBeforeCall(registryName, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryGetActiveGarbageCollectionResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getActiveGarbageCollectionAsync(String registryName, final ApiCallback<ContainerRegistryGetActiveGarbageCollectionResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getActiveGarbageCollectionValidateBeforeCall(registryName, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryGetActiveGarbageCollectionResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetActiveGarbageCollectionRequestBuilder {
        private final String registryName;

        private GetActiveGarbageCollectionRequestBuilder(String registryName) {
            this.registryName = registryName;
        }

        /**
         * Build call for getActiveGarbageCollection
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getActiveGarbageCollectionCall(registryName, _callback);
        }


        /**
         * Execute getActiveGarbageCollection request
         * @return ContainerRegistryGetActiveGarbageCollectionResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryGetActiveGarbageCollectionResponse execute() throws ApiException {
            ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> localVarResp = getActiveGarbageCollectionWithHttpInfo(registryName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getActiveGarbageCollection request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryGetActiveGarbageCollectionResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> executeWithHttpInfo() throws ApiException {
            return getActiveGarbageCollectionWithHttpInfo(registryName);
        }

        /**
         * Execute getActiveGarbageCollection request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryGetActiveGarbageCollectionResponse> _callback) throws ApiException {
            return getActiveGarbageCollectionAsync(registryName, _callback);
        }
    }

    /**
     * Get Active Garbage Collection
     * To get information about the currently-active garbage collection for a registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collection&#x60;.
     * @param registryName The name of a container registry. (required)
     * @return GetActiveGarbageCollectionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetActiveGarbageCollectionRequestBuilder getActiveGarbageCollection(String registryName) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        return new GetActiveGarbageCollectionRequestBuilder(registryName);
    }
    private okhttp3.Call getDockerCredentialsCall(Integer expirySeconds, Boolean readWrite, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/docker-credentials";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (expirySeconds != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("expiry_seconds", expirySeconds));
        }

        if (readWrite != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("read_write", readWrite));
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
    private okhttp3.Call getDockerCredentialsValidateBeforeCall(Integer expirySeconds, Boolean readWrite, final ApiCallback _callback) throws ApiException {
        return getDockerCredentialsCall(expirySeconds, readWrite, _callback);

    }


    private ApiResponse<DockerCredentials> getDockerCredentialsWithHttpInfo(Integer expirySeconds, Boolean readWrite) throws ApiException {
        okhttp3.Call localVarCall = getDockerCredentialsValidateBeforeCall(expirySeconds, readWrite, null);
        Type localVarReturnType = new TypeToken<DockerCredentials>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDockerCredentialsAsync(Integer expirySeconds, Boolean readWrite, final ApiCallback<DockerCredentials> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDockerCredentialsValidateBeforeCall(expirySeconds, readWrite, _callback);
        Type localVarReturnType = new TypeToken<DockerCredentials>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDockerCredentialsRequestBuilder {
        private Integer expirySeconds;
        private Boolean readWrite;

        private GetDockerCredentialsRequestBuilder() {
        }

        /**
         * Set expirySeconds
         * @param expirySeconds The duration in seconds that the returned registry credentials will be valid. If not set or 0, the credentials will not expire. (optional, default to 0)
         * @return GetDockerCredentialsRequestBuilder
         */
        public GetDockerCredentialsRequestBuilder expirySeconds(Integer expirySeconds) {
            this.expirySeconds = expirySeconds;
            return this;
        }
        
        /**
         * Set readWrite
         * @param readWrite By default, the registry credentials allow for read-only access. Set this query parameter to &#x60;true&#x60; to obtain read-write credentials. (optional, default to false)
         * @return GetDockerCredentialsRequestBuilder
         */
        public GetDockerCredentialsRequestBuilder readWrite(Boolean readWrite) {
            this.readWrite = readWrite;
            return this;
        }
        
        /**
         * Build call for getDockerCredentials
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A Docker &#x60;config.json&#x60; file for the container registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDockerCredentialsCall(expirySeconds, readWrite, _callback);
        }


        /**
         * Execute getDockerCredentials request
         * @return DockerCredentials
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A Docker &#x60;config.json&#x60; file for the container registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DockerCredentials execute() throws ApiException {
            ApiResponse<DockerCredentials> localVarResp = getDockerCredentialsWithHttpInfo(expirySeconds, readWrite);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDockerCredentials request with HTTP info returned
         * @return ApiResponse&lt;DockerCredentials&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A Docker &#x60;config.json&#x60; file for the container registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DockerCredentials> executeWithHttpInfo() throws ApiException {
            return getDockerCredentialsWithHttpInfo(expirySeconds, readWrite);
        }

        /**
         * Execute getDockerCredentials request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A Docker &#x60;config.json&#x60; file for the container registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DockerCredentials> _callback) throws ApiException {
            return getDockerCredentialsAsync(expirySeconds, readWrite, _callback);
        }
    }

    /**
     * Get Docker Credentials for Container Registry
     * In order to access your container registry with the Docker client or from a Kubernetes cluster, you will need to configure authentication. The necessary JSON configuration can be retrieved by sending a GET request to &#x60;/v2/registry/docker-credentials&#x60;.  The response will be in the format of a Docker &#x60;config.json&#x60; file. To use the config in your Kubernetes cluster, create a Secret with:      kubectl create secret generic docr \\       --from-file&#x3D;.dockerconfigjson&#x3D;config.json \\       --type&#x3D;kubernetes.io/dockerconfigjson  By default, the returned credentials have read-only access to your registry and cannot be used to push images. This is appropriate for most Kubernetes clusters. To retrieve read/write credentials, suitable for use with the Docker client or in a CI system, read_write may be provided as query parameter. For example: &#x60;/v2/registry/docker-credentials?read_write&#x3D;true&#x60;  By default, the returned credentials will not expire. To retrieve credentials with an expiry set, expiry_seconds may be provided as a query parameter. For example: &#x60;/v2/registry/docker-credentials?expiry_seconds&#x3D;3600&#x60; will return credentials that expire after one hour. 
     * @return GetDockerCredentialsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A Docker &#x60;config.json&#x60; file for the container registry. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDockerCredentialsRequestBuilder getDockerCredentials() throws IllegalArgumentException {
        return new GetDockerCredentialsRequestBuilder();
    }
    private okhttp3.Call getSubscriptionInfoCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/subscription";

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
    private okhttp3.Call getSubscriptionInfoValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return getSubscriptionInfoCall(_callback);

    }


    private ApiResponse<Object> getSubscriptionInfoWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = getSubscriptionInfoValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getSubscriptionInfoAsync(final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = getSubscriptionInfoValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetSubscriptionInfoRequestBuilder {

        private GetSubscriptionInfoRequestBuilder() {
        }

        /**
         * Build call for getSubscriptionInfo
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getSubscriptionInfoCall(_callback);
        }


        /**
         * Execute getSubscriptionInfo request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ApiResponse<Object> localVarResp = getSubscriptionInfoWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getSubscriptionInfo request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            return getSubscriptionInfoWithHttpInfo();
        }

        /**
         * Execute getSubscriptionInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            return getSubscriptionInfoAsync(_callback);
        }
    }

    /**
     * Get Subscription Information
     * A subscription is automatically created when you configure your container registry. To get information about your subscription, send a GET request to &#x60;/v2/registry/subscription&#x60;.
     * @return GetSubscriptionInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetSubscriptionInfoRequestBuilder getSubscriptionInfo() throws IllegalArgumentException {
        return new GetSubscriptionInfoRequestBuilder();
    }
    private okhttp3.Call listGarbageCollectionsCall(String registryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/garbage-collections"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()));

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
    private okhttp3.Call listGarbageCollectionsValidateBeforeCall(String registryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling listGarbageCollections(Async)");
        }

        return listGarbageCollectionsCall(registryName, perPage, page, _callback);

    }


    private ApiResponse<ContainerRegistryListGarbageCollectionsResponse> listGarbageCollectionsWithHttpInfo(String registryName, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listGarbageCollectionsValidateBeforeCall(registryName, perPage, page, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryListGarbageCollectionsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listGarbageCollectionsAsync(String registryName, Integer perPage, Integer page, final ApiCallback<ContainerRegistryListGarbageCollectionsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listGarbageCollectionsValidateBeforeCall(registryName, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryListGarbageCollectionsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListGarbageCollectionsRequestBuilder {
        private final String registryName;
        private Integer perPage;
        private Integer page;

        private ListGarbageCollectionsRequestBuilder(String registryName) {
            this.registryName = registryName;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListGarbageCollectionsRequestBuilder
         */
        public ListGarbageCollectionsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListGarbageCollectionsRequestBuilder
         */
        public ListGarbageCollectionsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listGarbageCollections
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collections&#x60;. This will be set to an array containing objects representing each past garbage collection. Each will contain the standard Garbage Collection attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listGarbageCollectionsCall(registryName, perPage, page, _callback);
        }


        /**
         * Execute listGarbageCollections request
         * @return ContainerRegistryListGarbageCollectionsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collections&#x60;. This will be set to an array containing objects representing each past garbage collection. Each will contain the standard Garbage Collection attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryListGarbageCollectionsResponse execute() throws ApiException {
            ApiResponse<ContainerRegistryListGarbageCollectionsResponse> localVarResp = listGarbageCollectionsWithHttpInfo(registryName, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listGarbageCollections request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryListGarbageCollectionsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collections&#x60;. This will be set to an array containing objects representing each past garbage collection. Each will contain the standard Garbage Collection attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryListGarbageCollectionsResponse> executeWithHttpInfo() throws ApiException {
            return listGarbageCollectionsWithHttpInfo(registryName, perPage, page);
        }

        /**
         * Execute listGarbageCollections request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collections&#x60;. This will be set to an array containing objects representing each past garbage collection. Each will contain the standard Garbage Collection attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryListGarbageCollectionsResponse> _callback) throws ApiException {
            return listGarbageCollectionsAsync(registryName, perPage, page, _callback);
        }
    }

    /**
     * List Garbage Collections
     * To get information about past garbage collections for a registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collections&#x60;.
     * @param registryName The name of a container registry. (required)
     * @return ListGarbageCollectionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key of &#x60;garbage_collections&#x60;. This will be set to an array containing objects representing each past garbage collection. Each will contain the standard Garbage Collection attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListGarbageCollectionsRequestBuilder listGarbageCollections(String registryName) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        return new ListGarbageCollectionsRequestBuilder(registryName);
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
        String localVarPath = "/v2/registry/options";

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


    private ApiResponse<ContainerRegistryListOptionsResponse> listOptionsWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listOptionsValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<ContainerRegistryListOptionsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listOptionsAsync(final ApiCallback<ContainerRegistryListOptionsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listOptionsValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryListOptionsResponse>(){}.getType();
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
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains a key called &#x60;subscription_tiers&#x60; listing the available tiers. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listOptionsCall(_callback);
        }


        /**
         * Execute listOptions request
         * @return ContainerRegistryListOptionsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains a key called &#x60;subscription_tiers&#x60; listing the available tiers. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryListOptionsResponse execute() throws ApiException {
            ApiResponse<ContainerRegistryListOptionsResponse> localVarResp = listOptionsWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listOptions request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryListOptionsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains a key called &#x60;subscription_tiers&#x60; listing the available tiers. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryListOptionsResponse> executeWithHttpInfo() throws ApiException {
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
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains a key called &#x60;subscription_tiers&#x60; listing the available tiers. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryListOptionsResponse> _callback) throws ApiException {
            return listOptionsAsync(_callback);
        }
    }

    /**
     * List Registry Options (Subscription Tiers and Available Regions)
     * This endpoint serves to provide additional information as to which option values are available when creating a container registry. There are multiple subscription tiers available for container registry. Each tier allows a different number of image repositories to be created in your registry, and has a different amount of storage and transfer included. There are multiple regions available for container registry and controls where your data is stored. To list the available options, send a GET request to &#x60;/v2/registry/options&#x60;.
     * @return ListOptionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains a key called &#x60;subscription_tiers&#x60; listing the available tiers. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListOptionsRequestBuilder listOptions() throws IllegalArgumentException {
        return new ListOptionsRequestBuilder();
    }
    private okhttp3.Call listRepositoriesCall(String registryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/repositories"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()));

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

    @Deprecated
    @SuppressWarnings("rawtypes")
    private okhttp3.Call listRepositoriesValidateBeforeCall(String registryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling listRepositories(Async)");
        }

        return listRepositoriesCall(registryName, perPage, page, _callback);

    }


    private ApiResponse<ContainerRegistryListRepositoriesResponse> listRepositoriesWithHttpInfo(String registryName, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listRepositoriesValidateBeforeCall(registryName, perPage, page, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoriesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listRepositoriesAsync(String registryName, Integer perPage, Integer page, final ApiCallback<ContainerRegistryListRepositoriesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listRepositoriesValidateBeforeCall(registryName, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoriesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRepositoriesRequestBuilder {
        private final String registryName;
        private Integer perPage;
        private Integer page;

        private ListRepositoriesRequestBuilder(String registryName) {
            this.registryName = registryName;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListRepositoriesRequestBuilder
         */
        public ListRepositoriesRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListRepositoriesRequestBuilder
         */
        public ListRepositoriesRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listRepositories
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listRepositoriesCall(registryName, perPage, page, _callback);
        }


        /**
         * Execute listRepositories request
         * @return ContainerRegistryListRepositoriesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public ContainerRegistryListRepositoriesResponse execute() throws ApiException {
            ApiResponse<ContainerRegistryListRepositoriesResponse> localVarResp = listRepositoriesWithHttpInfo(registryName, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listRepositories request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryListRepositoriesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public ApiResponse<ContainerRegistryListRepositoriesResponse> executeWithHttpInfo() throws ApiException {
            return listRepositoriesWithHttpInfo(registryName, perPage, page);
        }

        /**
         * Execute listRepositories request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryListRepositoriesResponse> _callback) throws ApiException {
            return listRepositoriesAsync(registryName, perPage, page, _callback);
        }
    }

    /**
     * List All Container Registry Repositories
     * This endpoint has been deprecated in favor of the _List All Container Registry Repositories [V2]_ endpoint.  To list all repositories in your container registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositories&#x60;. 
     * @param registryName The name of a container registry. (required)
     * @return ListRepositoriesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     * @deprecated
     */
    @Deprecated
    public ListRepositoriesRequestBuilder listRepositories(String registryName) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        return new ListRepositoriesRequestBuilder(registryName);
    }
    private okhttp3.Call listRepositoriesV2Call(String registryName, Integer perPage, Integer page, String pageToken, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/repositoriesV2"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()));

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

        if (pageToken != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("page_token", pageToken));
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
    private okhttp3.Call listRepositoriesV2ValidateBeforeCall(String registryName, Integer perPage, Integer page, String pageToken, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling listRepositoriesV2(Async)");
        }

        return listRepositoriesV2Call(registryName, perPage, page, pageToken, _callback);

    }


    private ApiResponse<ContainerRegistryListRepositoriesV2Response> listRepositoriesV2WithHttpInfo(String registryName, Integer perPage, Integer page, String pageToken) throws ApiException {
        okhttp3.Call localVarCall = listRepositoriesV2ValidateBeforeCall(registryName, perPage, page, pageToken, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoriesV2Response>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listRepositoriesV2Async(String registryName, Integer perPage, Integer page, String pageToken, final ApiCallback<ContainerRegistryListRepositoriesV2Response> _callback) throws ApiException {

        okhttp3.Call localVarCall = listRepositoriesV2ValidateBeforeCall(registryName, perPage, page, pageToken, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoriesV2Response>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRepositoriesV2RequestBuilder {
        private final String registryName;
        private Integer perPage;
        private Integer page;
        private String pageToken;

        private ListRepositoriesV2RequestBuilder(String registryName) {
            this.registryName = registryName;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListRepositoriesV2RequestBuilder
         */
        public ListRepositoriesV2RequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. Ignored when &#39;page_token&#39; is provided. (optional, default to 1)
         * @return ListRepositoriesV2RequestBuilder
         */
        public ListRepositoriesV2RequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Set pageToken
         * @param pageToken Token to retrieve of the next or previous set of results more quickly than using &#39;page&#39;. (optional)
         * @return ListRepositoriesV2RequestBuilder
         */
        public ListRepositoriesV2RequestBuilder pageToken(String pageToken) {
            this.pageToken = pageToken;
            return this;
        }
        
        /**
         * Build call for listRepositoriesV2
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listRepositoriesV2Call(registryName, perPage, page, pageToken, _callback);
        }


        /**
         * Execute listRepositoriesV2 request
         * @return ContainerRegistryListRepositoriesV2Response
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryListRepositoriesV2Response execute() throws ApiException {
            ApiResponse<ContainerRegistryListRepositoriesV2Response> localVarResp = listRepositoriesV2WithHttpInfo(registryName, perPage, page, pageToken);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listRepositoriesV2 request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryListRepositoriesV2Response&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryListRepositoriesV2Response> executeWithHttpInfo() throws ApiException {
            return listRepositoriesV2WithHttpInfo(registryName, perPage, page, pageToken);
        }

        /**
         * Execute listRepositoriesV2 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryListRepositoriesV2Response> _callback) throws ApiException {
            return listRepositoriesV2Async(registryName, perPage, page, pageToken, _callback);
        }
    }

    /**
     * List All Container Registry Repositories (V2)
     * To list all repositories in your container registry, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositoriesV2&#x60;.
     * @param registryName The name of a container registry. (required)
     * @return ListRepositoriesV2RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;repositories&#x60;. This will be set to an array containing objects each representing a repository. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRepositoriesV2RequestBuilder listRepositoriesV2(String registryName) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        return new ListRepositoriesV2RequestBuilder(registryName);
    }
    private okhttp3.Call listRepositoryManifestsCall(String registryName, String repositoryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/repositories/{repository_name}/digests"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()))
            .replace("{" + "repository_name" + "}", localVarApiClient.escapeString(repositoryName.toString()));

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
    private okhttp3.Call listRepositoryManifestsValidateBeforeCall(String registryName, String repositoryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling listRepositoryManifests(Async)");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null) {
            throw new ApiException("Missing the required parameter 'repositoryName' when calling listRepositoryManifests(Async)");
        }

        return listRepositoryManifestsCall(registryName, repositoryName, perPage, page, _callback);

    }


    private ApiResponse<ContainerRegistryListRepositoryManifestsResponse> listRepositoryManifestsWithHttpInfo(String registryName, String repositoryName, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listRepositoryManifestsValidateBeforeCall(registryName, repositoryName, perPage, page, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoryManifestsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listRepositoryManifestsAsync(String registryName, String repositoryName, Integer perPage, Integer page, final ApiCallback<ContainerRegistryListRepositoryManifestsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listRepositoryManifestsValidateBeforeCall(registryName, repositoryName, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoryManifestsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRepositoryManifestsRequestBuilder {
        private final String registryName;
        private final String repositoryName;
        private Integer perPage;
        private Integer page;

        private ListRepositoryManifestsRequestBuilder(String registryName, String repositoryName) {
            this.registryName = registryName;
            this.repositoryName = repositoryName;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListRepositoryManifestsRequestBuilder
         */
        public ListRepositoryManifestsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListRepositoryManifestsRequestBuilder
         */
        public ListRepositoryManifestsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listRepositoryManifests
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;manifests&#x60;. This will be set to an array containing objects each representing a manifest. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listRepositoryManifestsCall(registryName, repositoryName, perPage, page, _callback);
        }


        /**
         * Execute listRepositoryManifests request
         * @return ContainerRegistryListRepositoryManifestsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;manifests&#x60;. This will be set to an array containing objects each representing a manifest. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryListRepositoryManifestsResponse execute() throws ApiException {
            ApiResponse<ContainerRegistryListRepositoryManifestsResponse> localVarResp = listRepositoryManifestsWithHttpInfo(registryName, repositoryName, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listRepositoryManifests request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryListRepositoryManifestsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;manifests&#x60;. This will be set to an array containing objects each representing a manifest. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryListRepositoryManifestsResponse> executeWithHttpInfo() throws ApiException {
            return listRepositoryManifestsWithHttpInfo(registryName, repositoryName, perPage, page);
        }

        /**
         * Execute listRepositoryManifests request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;manifests&#x60;. This will be set to an array containing objects each representing a manifest. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryListRepositoryManifestsResponse> _callback) throws ApiException {
            return listRepositoryManifestsAsync(registryName, repositoryName, perPage, page, _callback);
        }
    }

    /**
     * List All Container Registry Repository Manifests
     * To list all manifests in your container registry repository, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/digests&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to list manifests for &#x60;registry.digitalocean.com/example/my/repo&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/digests&#x60;. 
     * @param registryName The name of a container registry. (required)
     * @param repositoryName The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. (required)
     * @return ListRepositoryManifestsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;manifests&#x60;. This will be set to an array containing objects each representing a manifest. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRepositoryManifestsRequestBuilder listRepositoryManifests(String registryName, String repositoryName) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        if (repositoryName == null) throw new IllegalArgumentException("\"repositoryName\" is required but got null");
            

        return new ListRepositoryManifestsRequestBuilder(registryName, repositoryName);
    }
    private okhttp3.Call listRepositoryTagsCall(String registryName, String repositoryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/repositories/{repository_name}/tags"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()))
            .replace("{" + "repository_name" + "}", localVarApiClient.escapeString(repositoryName.toString()));

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
    private okhttp3.Call listRepositoryTagsValidateBeforeCall(String registryName, String repositoryName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling listRepositoryTags(Async)");
        }

        // verify the required parameter 'repositoryName' is set
        if (repositoryName == null) {
            throw new ApiException("Missing the required parameter 'repositoryName' when calling listRepositoryTags(Async)");
        }

        return listRepositoryTagsCall(registryName, repositoryName, perPage, page, _callback);

    }


    private ApiResponse<ContainerRegistryListRepositoryTagsResponse> listRepositoryTagsWithHttpInfo(String registryName, String repositoryName, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listRepositoryTagsValidateBeforeCall(registryName, repositoryName, perPage, page, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoryTagsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listRepositoryTagsAsync(String registryName, String repositoryName, Integer perPage, Integer page, final ApiCallback<ContainerRegistryListRepositoryTagsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listRepositoryTagsValidateBeforeCall(registryName, repositoryName, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryListRepositoryTagsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRepositoryTagsRequestBuilder {
        private final String registryName;
        private final String repositoryName;
        private Integer perPage;
        private Integer page;

        private ListRepositoryTagsRequestBuilder(String registryName, String repositoryName) {
            this.registryName = registryName;
            this.repositoryName = repositoryName;
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListRepositoryTagsRequestBuilder
         */
        public ListRepositoryTagsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListRepositoryTagsRequestBuilder
         */
        public ListRepositoryTagsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listRepositoryTags
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;tags&#x60;. This will be set to an array containing objects each representing a tag. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listRepositoryTagsCall(registryName, repositoryName, perPage, page, _callback);
        }


        /**
         * Execute listRepositoryTags request
         * @return ContainerRegistryListRepositoryTagsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;tags&#x60;. This will be set to an array containing objects each representing a tag. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryListRepositoryTagsResponse execute() throws ApiException {
            ApiResponse<ContainerRegistryListRepositoryTagsResponse> localVarResp = listRepositoryTagsWithHttpInfo(registryName, repositoryName, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listRepositoryTags request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryListRepositoryTagsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;tags&#x60;. This will be set to an array containing objects each representing a tag. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryListRepositoryTagsResponse> executeWithHttpInfo() throws ApiException {
            return listRepositoryTagsWithHttpInfo(registryName, repositoryName, perPage, page);
        }

        /**
         * Execute listRepositoryTags request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;tags&#x60;. This will be set to an array containing objects each representing a tag. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryListRepositoryTagsResponse> _callback) throws ApiException {
            return listRepositoryTagsAsync(registryName, repositoryName, perPage, page, _callback);
        }
    }

    /**
     * List All Container Registry Repository Tags
     * To list all tags in your container registry repository, send a GET request to &#x60;/v2/registry/$REGISTRY_NAME/repositories/$REPOSITORY_NAME/tags&#x60;.  Note that if your repository name contains &#x60;/&#x60; characters, it must be URL-encoded in the request URL. For example, to list tags for &#x60;registry.digitalocean.com/example/my/repo&#x60;, the path would be &#x60;/v2/registry/example/repositories/my%2Frepo/tags&#x60;. 
     * @param registryName The name of a container registry. (required)
     * @param repositoryName The name of a container registry repository. If the name contains &#x60;/&#x60; characters, they must be URL-encoded, e.g. &#x60;%2F&#x60;. (required)
     * @return ListRepositoryTagsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response body will be a JSON object with a key of &#x60;tags&#x60;. This will be set to an array containing objects each representing a tag. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRepositoryTagsRequestBuilder listRepositoryTags(String registryName, String repositoryName) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        if (repositoryName == null) throw new IllegalArgumentException("\"repositoryName\" is required but got null");
            

        return new ListRepositoryTagsRequestBuilder(registryName, repositoryName);
    }
    private okhttp3.Call startGarbageCollectionCall(String registryName, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/registry/{registry_name}/garbage-collection"
            .replace("{" + "registry_name" + "}", localVarApiClient.escapeString(registryName.toString()));

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
    private okhttp3.Call startGarbageCollectionValidateBeforeCall(String registryName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'registryName' is set
        if (registryName == null) {
            throw new ApiException("Missing the required parameter 'registryName' when calling startGarbageCollection(Async)");
        }

        return startGarbageCollectionCall(registryName, _callback);

    }


    private ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> startGarbageCollectionWithHttpInfo(String registryName) throws ApiException {
        okhttp3.Call localVarCall = startGarbageCollectionValidateBeforeCall(registryName, null);
        Type localVarReturnType = new TypeToken<ContainerRegistryGetActiveGarbageCollectionResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call startGarbageCollectionAsync(String registryName, final ApiCallback<ContainerRegistryGetActiveGarbageCollectionResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = startGarbageCollectionValidateBeforeCall(registryName, _callback);
        Type localVarReturnType = new TypeToken<ContainerRegistryGetActiveGarbageCollectionResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class StartGarbageCollectionRequestBuilder {
        private final String registryName;

        private StartGarbageCollectionRequestBuilder(String registryName) {
            this.registryName = registryName;
        }

        /**
         * Build call for startGarbageCollection
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return startGarbageCollectionCall(registryName, _callback);
        }


        /**
         * Execute startGarbageCollection request
         * @return ContainerRegistryGetActiveGarbageCollectionResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ContainerRegistryGetActiveGarbageCollectionResponse execute() throws ApiException {
            ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> localVarResp = startGarbageCollectionWithHttpInfo(registryName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute startGarbageCollection request with HTTP info returned
         * @return ApiResponse&lt;ContainerRegistryGetActiveGarbageCollectionResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ContainerRegistryGetActiveGarbageCollectionResponse> executeWithHttpInfo() throws ApiException {
            return startGarbageCollectionWithHttpInfo(registryName);
        }

        /**
         * Execute startGarbageCollection request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ContainerRegistryGetActiveGarbageCollectionResponse> _callback) throws ApiException {
            return startGarbageCollectionAsync(registryName, _callback);
        }
    }

    /**
     * Start Garbage Collection
     * Garbage collection enables users to clear out unreferenced blobs (layer &amp; manifest data) after deleting one or more manifests from a repository. If there are no unreferenced blobs resulting from the deletion of one or more manifests, garbage collection is effectively a noop. [See here for more information](https://www.digitalocean.com/docs/container-registry/how-to/clean-up-container-registry/) about how and why you should clean up your container registry periodically.  To request a garbage collection run on your registry, send a POST request to &#x60;/v2/registry/$REGISTRY_NAME/garbage-collection&#x60;. This will initiate the following sequence of events on your registry.  * Set the registry to read-only mode, meaning no further write-scoped   JWTs will be issued to registry clients. Existing write-scoped JWTs will   continue to work until they expire which can take up to 15 minutes. * Wait until all existing write-scoped JWTs have expired. * Scan all registry manifests to determine which blobs are unreferenced. * Delete all unreferenced blobs from the registry. * Record the number of blobs deleted and bytes freed, mark the garbage   collection status as &#x60;success&#x60;. * Remove the read-only mode restriction from the registry, meaning write-scoped   JWTs will once again be issued to registry clients. 
     * @param registryName The name of a container registry. (required)
     * @return StartGarbageCollectionRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response will be a JSON object with a key of &#x60;garbage_collection&#x60;. This will be a json object with attributes representing the currently-active garbage collection. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public StartGarbageCollectionRequestBuilder startGarbageCollection(String registryName) throws IllegalArgumentException {
        if (registryName == null) throw new IllegalArgumentException("\"registryName\" is required but got null");
            

        return new StartGarbageCollectionRequestBuilder(registryName);
    }
    private okhttp3.Call updateSubscriptionTierCall(ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = containerRegistryUpdateSubscriptionTierRequest;

        // create path and map variables
        String localVarPath = "/v2/registry/subscription";

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
    private okhttp3.Call updateSubscriptionTierValidateBeforeCall(ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest, final ApiCallback _callback) throws ApiException {
        return updateSubscriptionTierCall(containerRegistryUpdateSubscriptionTierRequest, _callback);

    }


    private ApiResponse<Object> updateSubscriptionTierWithHttpInfo(ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest) throws ApiException {
        okhttp3.Call localVarCall = updateSubscriptionTierValidateBeforeCall(containerRegistryUpdateSubscriptionTierRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateSubscriptionTierAsync(ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateSubscriptionTierValidateBeforeCall(containerRegistryUpdateSubscriptionTierRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateSubscriptionTierRequestBuilder {
        private String tierSlug;

        private UpdateSubscriptionTierRequestBuilder() {
        }

        /**
         * Set tierSlug
         * @param tierSlug The slug of the subscription tier to sign up for. (optional)
         * @return UpdateSubscriptionTierRequestBuilder
         */
        public UpdateSubscriptionTierRequestBuilder tierSlug(String tierSlug) {
            this.tierSlug = tierSlug;
            return this;
        }
        
        /**
         * Build call for updateSubscriptionTier
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest = buildBodyParams();
            return updateSubscriptionTierCall(containerRegistryUpdateSubscriptionTierRequest, _callback);
        }

        private ContainerRegistryUpdateSubscriptionTierRequest buildBodyParams() {
            ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest = new ContainerRegistryUpdateSubscriptionTierRequest();
            if (this.tierSlug != null)
            containerRegistryUpdateSubscriptionTierRequest.tierSlug(ContainerRegistryUpdateSubscriptionTierRequest.TierSlugEnum.fromValue(this.tierSlug));
            return containerRegistryUpdateSubscriptionTierRequest;
        }

        /**
         * Execute updateSubscriptionTier request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = updateSubscriptionTierWithHttpInfo(containerRegistryUpdateSubscriptionTierRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateSubscriptionTier request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest = buildBodyParams();
            return updateSubscriptionTierWithHttpInfo(containerRegistryUpdateSubscriptionTierRequest);
        }

        /**
         * Execute updateSubscriptionTier request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            ContainerRegistryUpdateSubscriptionTierRequest containerRegistryUpdateSubscriptionTierRequest = buildBodyParams();
            return updateSubscriptionTierAsync(containerRegistryUpdateSubscriptionTierRequest, _callback);
        }
    }

    /**
     * Update Subscription Tier
     * After creating your registry, you can switch to a different subscription tier to better suit your needs. To do this, send a POST request to &#x60;/v2/registry/subscription&#x60;.
     * @return UpdateSubscriptionTierRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;subscription&#x60; containing information about your subscription. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateSubscriptionTierRequestBuilder updateSubscriptionTier() throws IllegalArgumentException {
        return new UpdateSubscriptionTierRequestBuilder();
    }
    private okhttp3.Call validateNameCall(ValidateRegistry validateRegistry, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = validateRegistry;

        // create path and map variables
        String localVarPath = "/v2/registry/validate-name";

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
    private okhttp3.Call validateNameValidateBeforeCall(ValidateRegistry validateRegistry, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'validateRegistry' is set
        if (validateRegistry == null) {
            throw new ApiException("Missing the required parameter 'validateRegistry' when calling validateName(Async)");
        }

        return validateNameCall(validateRegistry, _callback);

    }


    private ApiResponse<Void> validateNameWithHttpInfo(ValidateRegistry validateRegistry) throws ApiException {
        okhttp3.Call localVarCall = validateNameValidateBeforeCall(validateRegistry, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call validateNameAsync(ValidateRegistry validateRegistry, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = validateNameValidateBeforeCall(validateRegistry, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class ValidateNameRequestBuilder {
        private final String name;

        private ValidateNameRequestBuilder(String name) {
            this.name = name;
        }

        /**
         * Build call for validateName
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
            ValidateRegistry validateRegistry = buildBodyParams();
            return validateNameCall(validateRegistry, _callback);
        }

        private ValidateRegistry buildBodyParams() {
            ValidateRegistry validateRegistry = new ValidateRegistry();
            validateRegistry.name(this.name);
            return validateRegistry;
        }

        /**
         * Execute validateName request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ValidateRegistry validateRegistry = buildBodyParams();
            validateNameWithHttpInfo(validateRegistry);
        }

        /**
         * Execute validateName request with HTTP info returned
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
            ValidateRegistry validateRegistry = buildBodyParams();
            return validateNameWithHttpInfo(validateRegistry);
        }

        /**
         * Execute validateName request (asynchronously)
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
            ValidateRegistry validateRegistry = buildBodyParams();
            return validateNameAsync(validateRegistry, _callback);
        }
    }

    /**
     * Validate a Container Registry Name
     * To validate that a container registry name is available for use, send a POST request to &#x60;/v2/registry/validate-name&#x60;.  If the name is both formatted correctly and available, the response code will be 204 and contain no body. If the name is already in use, the response will be a 409 Conflict. 
     * @param validateRegistry  (required)
     * @return ValidateNameRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ValidateNameRequestBuilder validateName(String name) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        return new ValidateNameRequestBuilder(name);
    }
}
