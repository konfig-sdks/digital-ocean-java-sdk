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


import com.konfigthis.client.model.CreateNamespace;
import com.konfigthis.client.model.CreateTrigger;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.FunctionsCreateNamespaceResponse;
import com.konfigthis.client.model.FunctionsCreateTriggerInNamespaceResponse;
import com.konfigthis.client.model.FunctionsListNamespacesResponse;
import com.konfigthis.client.model.FunctionsListTriggersResponse;
import com.konfigthis.client.model.ScheduledDetails;
import com.konfigthis.client.model.UpdateTrigger;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class FunctionsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public FunctionsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public FunctionsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call createNamespaceCall(CreateNamespace createNamespace, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = createNamespace;

        // create path and map variables
        String localVarPath = "/v2/functions/namespaces";

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
    private okhttp3.Call createNamespaceValidateBeforeCall(CreateNamespace createNamespace, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'createNamespace' is set
        if (createNamespace == null) {
            throw new ApiException("Missing the required parameter 'createNamespace' when calling createNamespace(Async)");
        }

        return createNamespaceCall(createNamespace, _callback);

    }


    private ApiResponse<FunctionsCreateNamespaceResponse> createNamespaceWithHttpInfo(CreateNamespace createNamespace) throws ApiException {
        okhttp3.Call localVarCall = createNamespaceValidateBeforeCall(createNamespace, null);
        Type localVarReturnType = new TypeToken<FunctionsCreateNamespaceResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createNamespaceAsync(CreateNamespace createNamespace, final ApiCallback<FunctionsCreateNamespaceResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createNamespaceValidateBeforeCall(createNamespace, _callback);
        Type localVarReturnType = new TypeToken<FunctionsCreateNamespaceResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateNamespaceRequestBuilder {
        private final String region;
        private final String label;

        private CreateNamespaceRequestBuilder(String region, String label) {
            this.region = region;
            this.label = label;
        }

        /**
         * Build call for createNamespace
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            CreateNamespace createNamespace = buildBodyParams();
            return createNamespaceCall(createNamespace, _callback);
        }

        private CreateNamespace buildBodyParams() {
            CreateNamespace createNamespace = new CreateNamespace();
            createNamespace.region(this.region);
            createNamespace.label(this.label);
            return createNamespace;
        }

        /**
         * Execute createNamespace request
         * @return FunctionsCreateNamespaceResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FunctionsCreateNamespaceResponse execute() throws ApiException {
            CreateNamespace createNamespace = buildBodyParams();
            ApiResponse<FunctionsCreateNamespaceResponse> localVarResp = createNamespaceWithHttpInfo(createNamespace);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createNamespace request with HTTP info returned
         * @return ApiResponse&lt;FunctionsCreateNamespaceResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FunctionsCreateNamespaceResponse> executeWithHttpInfo() throws ApiException {
            CreateNamespace createNamespace = buildBodyParams();
            return createNamespaceWithHttpInfo(createNamespace);
        }

        /**
         * Execute createNamespace request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FunctionsCreateNamespaceResponse> _callback) throws ApiException {
            CreateNamespace createNamespace = buildBodyParams();
            return createNamespaceAsync(createNamespace, _callback);
        }
    }

    /**
     * Create Namespace
     * Creates a new serverless functions namespace in the desired region and associates it with the provided label. A namespace is a collection of functions and their associated packages, triggers, and project specifications. To create a namespace, send a POST request to &#x60;/v2/functions/namespaces&#x60; with the &#x60;region&#x60; and &#x60;label&#x60; properties.
     * @param createNamespace  (required)
     * @return CreateNamespaceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateNamespaceRequestBuilder createNamespace(String region, String label) throws IllegalArgumentException {
        if (region == null) throw new IllegalArgumentException("\"region\" is required but got null");
            

        if (label == null) throw new IllegalArgumentException("\"label\" is required but got null");
            

        return new CreateNamespaceRequestBuilder(region, label);
    }
    private okhttp3.Call createTriggerInNamespaceCall(String namespaceId, CreateTrigger createTrigger, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = createTrigger;

        // create path and map variables
        String localVarPath = "/v2/functions/namespaces/{namespace_id}/triggers"
            .replace("{" + "namespace_id" + "}", localVarApiClient.escapeString(namespaceId.toString()));

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
    private okhttp3.Call createTriggerInNamespaceValidateBeforeCall(String namespaceId, CreateTrigger createTrigger, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'namespaceId' is set
        if (namespaceId == null) {
            throw new ApiException("Missing the required parameter 'namespaceId' when calling createTriggerInNamespace(Async)");
        }

        // verify the required parameter 'createTrigger' is set
        if (createTrigger == null) {
            throw new ApiException("Missing the required parameter 'createTrigger' when calling createTriggerInNamespace(Async)");
        }

        return createTriggerInNamespaceCall(namespaceId, createTrigger, _callback);

    }


    private ApiResponse<FunctionsCreateTriggerInNamespaceResponse> createTriggerInNamespaceWithHttpInfo(String namespaceId, CreateTrigger createTrigger) throws ApiException {
        okhttp3.Call localVarCall = createTriggerInNamespaceValidateBeforeCall(namespaceId, createTrigger, null);
        Type localVarReturnType = new TypeToken<FunctionsCreateTriggerInNamespaceResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createTriggerInNamespaceAsync(String namespaceId, CreateTrigger createTrigger, final ApiCallback<FunctionsCreateTriggerInNamespaceResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createTriggerInNamespaceValidateBeforeCall(namespaceId, createTrigger, _callback);
        Type localVarReturnType = new TypeToken<FunctionsCreateTriggerInNamespaceResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateTriggerInNamespaceRequestBuilder {
        private final String name;
        private final String function;
        private final String type;
        private final Boolean isEnabled;
        private final ScheduledDetails scheduledDetails;
        private final String namespaceId;

        private CreateTriggerInNamespaceRequestBuilder(String name, String function, String type, Boolean isEnabled, ScheduledDetails scheduledDetails, String namespaceId) {
            this.name = name;
            this.function = function;
            this.type = type;
            this.isEnabled = isEnabled;
            this.scheduledDetails = scheduledDetails;
            this.namespaceId = namespaceId;
        }

        /**
         * Build call for createTriggerInNamespace
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            CreateTrigger createTrigger = buildBodyParams();
            return createTriggerInNamespaceCall(namespaceId, createTrigger, _callback);
        }

        private CreateTrigger buildBodyParams() {
            CreateTrigger createTrigger = new CreateTrigger();
            createTrigger.name(this.name);
            createTrigger.function(this.function);
            createTrigger.type(this.type);
            createTrigger.isEnabled(this.isEnabled);
            createTrigger.scheduledDetails(this.scheduledDetails);
            return createTrigger;
        }

        /**
         * Execute createTriggerInNamespace request
         * @return FunctionsCreateTriggerInNamespaceResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FunctionsCreateTriggerInNamespaceResponse execute() throws ApiException {
            CreateTrigger createTrigger = buildBodyParams();
            ApiResponse<FunctionsCreateTriggerInNamespaceResponse> localVarResp = createTriggerInNamespaceWithHttpInfo(namespaceId, createTrigger);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createTriggerInNamespace request with HTTP info returned
         * @return ApiResponse&lt;FunctionsCreateTriggerInNamespaceResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FunctionsCreateTriggerInNamespaceResponse> executeWithHttpInfo() throws ApiException {
            CreateTrigger createTrigger = buildBodyParams();
            return createTriggerInNamespaceWithHttpInfo(namespaceId, createTrigger);
        }

        /**
         * Execute createTriggerInNamespace request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FunctionsCreateTriggerInNamespaceResponse> _callback) throws ApiException {
            CreateTrigger createTrigger = buildBodyParams();
            return createTriggerInNamespaceAsync(namespaceId, createTrigger, _callback);
        }
    }

    /**
     * Create Trigger
     * Creates a new trigger for a given function in a namespace. To create a trigger, send a POST request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers&#x60; with the &#x60;name&#x60;, &#x60;function&#x60;, &#x60;type&#x60;, &#x60;is_enabled&#x60; and &#x60;scheduled_details&#x60; properties.
     * @param namespaceId The ID of the namespace to be managed. (required)
     * @param createTrigger  (required)
     * @return CreateTriggerInNamespaceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateTriggerInNamespaceRequestBuilder createTriggerInNamespace(String name, String function, String type, Boolean isEnabled, ScheduledDetails scheduledDetails, String namespaceId) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (function == null) throw new IllegalArgumentException("\"function\" is required but got null");
            

        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        if (isEnabled == null) throw new IllegalArgumentException("\"isEnabled\" is required but got null");
        if (scheduledDetails == null) throw new IllegalArgumentException("\"scheduledDetails\" is required but got null");
        if (namespaceId == null) throw new IllegalArgumentException("\"namespaceId\" is required but got null");
            

        return new CreateTriggerInNamespaceRequestBuilder(name, function, type, isEnabled, scheduledDetails, namespaceId);
    }
    private okhttp3.Call deleteNamespaceCall(String namespaceId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/functions/namespaces/{namespace_id}"
            .replace("{" + "namespace_id" + "}", localVarApiClient.escapeString(namespaceId.toString()));

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
    private okhttp3.Call deleteNamespaceValidateBeforeCall(String namespaceId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'namespaceId' is set
        if (namespaceId == null) {
            throw new ApiException("Missing the required parameter 'namespaceId' when calling deleteNamespace(Async)");
        }

        return deleteNamespaceCall(namespaceId, _callback);

    }


    private ApiResponse<Void> deleteNamespaceWithHttpInfo(String namespaceId) throws ApiException {
        okhttp3.Call localVarCall = deleteNamespaceValidateBeforeCall(namespaceId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteNamespaceAsync(String namespaceId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteNamespaceValidateBeforeCall(namespaceId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteNamespaceRequestBuilder {
        private final String namespaceId;

        private DeleteNamespaceRequestBuilder(String namespaceId) {
            this.namespaceId = namespaceId;
        }

        /**
         * Build call for deleteNamespace
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
            return deleteNamespaceCall(namespaceId, _callback);
        }


        /**
         * Execute deleteNamespace request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteNamespaceWithHttpInfo(namespaceId);
        }

        /**
         * Execute deleteNamespace request with HTTP info returned
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
            return deleteNamespaceWithHttpInfo(namespaceId);
        }

        /**
         * Execute deleteNamespace request (asynchronously)
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
            return deleteNamespaceAsync(namespaceId, _callback);
        }
    }

    /**
     * Delete Namespace
     * Deletes the given namespace.  When a namespace is deleted all assets, in the namespace are deleted, this includes packages, functions and triggers. Deleting a namespace is a destructive operation and assets in the namespace are not recoverable after deletion. Some metadata is retained, such as activations, or soft deleted for reporting purposes. To delete namespace, send a DELETE request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID&#x60;. A successful deletion returns a 204 response.
     * @param namespaceId The ID of the namespace to be managed. (required)
     * @return DeleteNamespaceRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteNamespaceRequestBuilder deleteNamespace(String namespaceId) throws IllegalArgumentException {
        if (namespaceId == null) throw new IllegalArgumentException("\"namespaceId\" is required but got null");
            

        return new DeleteNamespaceRequestBuilder(namespaceId);
    }
    private okhttp3.Call deleteTriggerCall(String namespaceId, String triggerName, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/functions/namespaces/{namespace_id}/triggers/{trigger_name}"
            .replace("{" + "namespace_id" + "}", localVarApiClient.escapeString(namespaceId.toString()))
            .replace("{" + "trigger_name" + "}", localVarApiClient.escapeString(triggerName.toString()));

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
    private okhttp3.Call deleteTriggerValidateBeforeCall(String namespaceId, String triggerName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'namespaceId' is set
        if (namespaceId == null) {
            throw new ApiException("Missing the required parameter 'namespaceId' when calling deleteTrigger(Async)");
        }

        // verify the required parameter 'triggerName' is set
        if (triggerName == null) {
            throw new ApiException("Missing the required parameter 'triggerName' when calling deleteTrigger(Async)");
        }

        return deleteTriggerCall(namespaceId, triggerName, _callback);

    }


    private ApiResponse<Void> deleteTriggerWithHttpInfo(String namespaceId, String triggerName) throws ApiException {
        okhttp3.Call localVarCall = deleteTriggerValidateBeforeCall(namespaceId, triggerName, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteTriggerAsync(String namespaceId, String triggerName, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteTriggerValidateBeforeCall(namespaceId, triggerName, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteTriggerRequestBuilder {
        private final String namespaceId;
        private final String triggerName;

        private DeleteTriggerRequestBuilder(String namespaceId, String triggerName) {
            this.namespaceId = namespaceId;
            this.triggerName = triggerName;
        }

        /**
         * Build call for deleteTrigger
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
            return deleteTriggerCall(namespaceId, triggerName, _callback);
        }


        /**
         * Execute deleteTrigger request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteTriggerWithHttpInfo(namespaceId, triggerName);
        }

        /**
         * Execute deleteTrigger request with HTTP info returned
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
            return deleteTriggerWithHttpInfo(namespaceId, triggerName);
        }

        /**
         * Execute deleteTrigger request (asynchronously)
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
            return deleteTriggerAsync(namespaceId, triggerName, _callback);
        }
    }

    /**
     * Delete Trigger
     * Deletes the given trigger. To delete trigger, send a DELETE request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers/$TRIGGER_NAME&#x60;. A successful deletion returns a 204 response.
     * @param namespaceId The ID of the namespace to be managed. (required)
     * @param triggerName The name of the trigger to be managed. (required)
     * @return DeleteTriggerRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteTriggerRequestBuilder deleteTrigger(String namespaceId, String triggerName) throws IllegalArgumentException {
        if (namespaceId == null) throw new IllegalArgumentException("\"namespaceId\" is required but got null");
            

        if (triggerName == null) throw new IllegalArgumentException("\"triggerName\" is required but got null");
            

        return new DeleteTriggerRequestBuilder(namespaceId, triggerName);
    }
    private okhttp3.Call getNamespaceDetailsCall(String namespaceId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/functions/namespaces/{namespace_id}"
            .replace("{" + "namespace_id" + "}", localVarApiClient.escapeString(namespaceId.toString()));

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
    private okhttp3.Call getNamespaceDetailsValidateBeforeCall(String namespaceId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'namespaceId' is set
        if (namespaceId == null) {
            throw new ApiException("Missing the required parameter 'namespaceId' when calling getNamespaceDetails(Async)");
        }

        return getNamespaceDetailsCall(namespaceId, _callback);

    }


    private ApiResponse<FunctionsCreateNamespaceResponse> getNamespaceDetailsWithHttpInfo(String namespaceId) throws ApiException {
        okhttp3.Call localVarCall = getNamespaceDetailsValidateBeforeCall(namespaceId, null);
        Type localVarReturnType = new TypeToken<FunctionsCreateNamespaceResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getNamespaceDetailsAsync(String namespaceId, final ApiCallback<FunctionsCreateNamespaceResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getNamespaceDetailsValidateBeforeCall(namespaceId, _callback);
        Type localVarReturnType = new TypeToken<FunctionsCreateNamespaceResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetNamespaceDetailsRequestBuilder {
        private final String namespaceId;

        private GetNamespaceDetailsRequestBuilder(String namespaceId) {
            this.namespaceId = namespaceId;
        }

        /**
         * Build call for getNamespaceDetails
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getNamespaceDetailsCall(namespaceId, _callback);
        }


        /**
         * Execute getNamespaceDetails request
         * @return FunctionsCreateNamespaceResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FunctionsCreateNamespaceResponse execute() throws ApiException {
            ApiResponse<FunctionsCreateNamespaceResponse> localVarResp = getNamespaceDetailsWithHttpInfo(namespaceId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getNamespaceDetails request with HTTP info returned
         * @return ApiResponse&lt;FunctionsCreateNamespaceResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FunctionsCreateNamespaceResponse> executeWithHttpInfo() throws ApiException {
            return getNamespaceDetailsWithHttpInfo(namespaceId);
        }

        /**
         * Execute getNamespaceDetails request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FunctionsCreateNamespaceResponse> _callback) throws ApiException {
            return getNamespaceDetailsAsync(namespaceId, _callback);
        }
    }

    /**
     * Get Namespace
     * Gets the namespace details for the given namespace UUID. To get namespace details, send a GET request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID&#x60; with no parameters.
     * @param namespaceId The ID of the namespace to be managed. (required)
     * @return GetNamespaceDetailsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON response object with a key called &#x60;namespace&#x60;. The object contains the properties associated with the namespace. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetNamespaceDetailsRequestBuilder getNamespaceDetails(String namespaceId) throws IllegalArgumentException {
        if (namespaceId == null) throw new IllegalArgumentException("\"namespaceId\" is required but got null");
            

        return new GetNamespaceDetailsRequestBuilder(namespaceId);
    }
    private okhttp3.Call getTriggerDetailsCall(String namespaceId, String triggerName, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/functions/namespaces/{namespace_id}/triggers/{trigger_name}"
            .replace("{" + "namespace_id" + "}", localVarApiClient.escapeString(namespaceId.toString()))
            .replace("{" + "trigger_name" + "}", localVarApiClient.escapeString(triggerName.toString()));

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
    private okhttp3.Call getTriggerDetailsValidateBeforeCall(String namespaceId, String triggerName, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'namespaceId' is set
        if (namespaceId == null) {
            throw new ApiException("Missing the required parameter 'namespaceId' when calling getTriggerDetails(Async)");
        }

        // verify the required parameter 'triggerName' is set
        if (triggerName == null) {
            throw new ApiException("Missing the required parameter 'triggerName' when calling getTriggerDetails(Async)");
        }

        return getTriggerDetailsCall(namespaceId, triggerName, _callback);

    }


    private ApiResponse<FunctionsCreateTriggerInNamespaceResponse> getTriggerDetailsWithHttpInfo(String namespaceId, String triggerName) throws ApiException {
        okhttp3.Call localVarCall = getTriggerDetailsValidateBeforeCall(namespaceId, triggerName, null);
        Type localVarReturnType = new TypeToken<FunctionsCreateTriggerInNamespaceResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getTriggerDetailsAsync(String namespaceId, String triggerName, final ApiCallback<FunctionsCreateTriggerInNamespaceResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getTriggerDetailsValidateBeforeCall(namespaceId, triggerName, _callback);
        Type localVarReturnType = new TypeToken<FunctionsCreateTriggerInNamespaceResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetTriggerDetailsRequestBuilder {
        private final String namespaceId;
        private final String triggerName;

        private GetTriggerDetailsRequestBuilder(String namespaceId, String triggerName) {
            this.namespaceId = namespaceId;
            this.triggerName = triggerName;
        }

        /**
         * Build call for getTriggerDetails
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getTriggerDetailsCall(namespaceId, triggerName, _callback);
        }


        /**
         * Execute getTriggerDetails request
         * @return FunctionsCreateTriggerInNamespaceResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FunctionsCreateTriggerInNamespaceResponse execute() throws ApiException {
            ApiResponse<FunctionsCreateTriggerInNamespaceResponse> localVarResp = getTriggerDetailsWithHttpInfo(namespaceId, triggerName);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getTriggerDetails request with HTTP info returned
         * @return ApiResponse&lt;FunctionsCreateTriggerInNamespaceResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FunctionsCreateTriggerInNamespaceResponse> executeWithHttpInfo() throws ApiException {
            return getTriggerDetailsWithHttpInfo(namespaceId, triggerName);
        }

        /**
         * Execute getTriggerDetails request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FunctionsCreateTriggerInNamespaceResponse> _callback) throws ApiException {
            return getTriggerDetailsAsync(namespaceId, triggerName, _callback);
        }
    }

    /**
     * Get Trigger
     * Gets the trigger details. To get the trigger details, send a GET request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers/$TRIGGER_NAME&#x60;.
     * @param namespaceId The ID of the namespace to be managed. (required)
     * @param triggerName The name of the trigger to be managed. (required)
     * @return GetTriggerDetailsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetTriggerDetailsRequestBuilder getTriggerDetails(String namespaceId, String triggerName) throws IllegalArgumentException {
        if (namespaceId == null) throw new IllegalArgumentException("\"namespaceId\" is required but got null");
            

        if (triggerName == null) throw new IllegalArgumentException("\"triggerName\" is required but got null");
            

        return new GetTriggerDetailsRequestBuilder(namespaceId, triggerName);
    }
    private okhttp3.Call listNamespacesCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/functions/namespaces";

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
    private okhttp3.Call listNamespacesValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return listNamespacesCall(_callback);

    }


    private ApiResponse<FunctionsListNamespacesResponse> listNamespacesWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listNamespacesValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<FunctionsListNamespacesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listNamespacesAsync(final ApiCallback<FunctionsListNamespacesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listNamespacesValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<FunctionsListNamespacesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListNamespacesRequestBuilder {

        private ListNamespacesRequestBuilder() {
        }

        /**
         * Build call for listNamespaces
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listNamespacesCall(_callback);
        }


        /**
         * Execute listNamespaces request
         * @return FunctionsListNamespacesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FunctionsListNamespacesResponse execute() throws ApiException {
            ApiResponse<FunctionsListNamespacesResponse> localVarResp = listNamespacesWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listNamespaces request with HTTP info returned
         * @return ApiResponse&lt;FunctionsListNamespacesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FunctionsListNamespacesResponse> executeWithHttpInfo() throws ApiException {
            return listNamespacesWithHttpInfo();
        }

        /**
         * Execute listNamespaces request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FunctionsListNamespacesResponse> _callback) throws ApiException {
            return listNamespacesAsync(_callback);
        }
    }

    /**
     * List Namespaces
     * Returns a list of namespaces associated with the current user. To get all namespaces, send a GET request to &#x60;/v2/functions/namespaces&#x60;.
     * @return ListNamespacesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListNamespacesRequestBuilder listNamespaces() throws IllegalArgumentException {
        return new ListNamespacesRequestBuilder();
    }
    private okhttp3.Call listTriggersCall(String namespaceId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/functions/namespaces/{namespace_id}/triggers"
            .replace("{" + "namespace_id" + "}", localVarApiClient.escapeString(namespaceId.toString()));

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
    private okhttp3.Call listTriggersValidateBeforeCall(String namespaceId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'namespaceId' is set
        if (namespaceId == null) {
            throw new ApiException("Missing the required parameter 'namespaceId' when calling listTriggers(Async)");
        }

        return listTriggersCall(namespaceId, _callback);

    }


    private ApiResponse<FunctionsListTriggersResponse> listTriggersWithHttpInfo(String namespaceId) throws ApiException {
        okhttp3.Call localVarCall = listTriggersValidateBeforeCall(namespaceId, null);
        Type localVarReturnType = new TypeToken<FunctionsListTriggersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listTriggersAsync(String namespaceId, final ApiCallback<FunctionsListTriggersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listTriggersValidateBeforeCall(namespaceId, _callback);
        Type localVarReturnType = new TypeToken<FunctionsListTriggersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListTriggersRequestBuilder {
        private final String namespaceId;

        private ListTriggersRequestBuilder(String namespaceId) {
            this.namespaceId = namespaceId;
        }

        /**
         * Build call for listTriggers
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listTriggersCall(namespaceId, _callback);
        }


        /**
         * Execute listTriggers request
         * @return FunctionsListTriggersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FunctionsListTriggersResponse execute() throws ApiException {
            ApiResponse<FunctionsListTriggersResponse> localVarResp = listTriggersWithHttpInfo(namespaceId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listTriggers request with HTTP info returned
         * @return ApiResponse&lt;FunctionsListTriggersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FunctionsListTriggersResponse> executeWithHttpInfo() throws ApiException {
            return listTriggersWithHttpInfo(namespaceId);
        }

        /**
         * Execute listTriggers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FunctionsListTriggersResponse> _callback) throws ApiException {
            return listTriggersAsync(namespaceId, _callback);
        }
    }

    /**
     * List Triggers
     * Returns a list of triggers associated with the current user and namespace. To get all triggers, send a GET request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers&#x60;.
     * @param namespaceId The ID of the namespace to be managed. (required)
     * @return ListTriggersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> An array of JSON objects with a key called &#x60;namespaces&#x60;.  Each object represents a namespace and contains the properties associated with it.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListTriggersRequestBuilder listTriggers(String namespaceId) throws IllegalArgumentException {
        if (namespaceId == null) throw new IllegalArgumentException("\"namespaceId\" is required but got null");
            

        return new ListTriggersRequestBuilder(namespaceId);
    }
    private okhttp3.Call updateTriggerDetailsCall(String namespaceId, String triggerName, UpdateTrigger updateTrigger, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = updateTrigger;

        // create path and map variables
        String localVarPath = "/v2/functions/namespaces/{namespace_id}/triggers/{trigger_name}"
            .replace("{" + "namespace_id" + "}", localVarApiClient.escapeString(namespaceId.toString()))
            .replace("{" + "trigger_name" + "}", localVarApiClient.escapeString(triggerName.toString()));

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
    private okhttp3.Call updateTriggerDetailsValidateBeforeCall(String namespaceId, String triggerName, UpdateTrigger updateTrigger, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'namespaceId' is set
        if (namespaceId == null) {
            throw new ApiException("Missing the required parameter 'namespaceId' when calling updateTriggerDetails(Async)");
        }

        // verify the required parameter 'triggerName' is set
        if (triggerName == null) {
            throw new ApiException("Missing the required parameter 'triggerName' when calling updateTriggerDetails(Async)");
        }

        // verify the required parameter 'updateTrigger' is set
        if (updateTrigger == null) {
            throw new ApiException("Missing the required parameter 'updateTrigger' when calling updateTriggerDetails(Async)");
        }

        return updateTriggerDetailsCall(namespaceId, triggerName, updateTrigger, _callback);

    }


    private ApiResponse<FunctionsCreateTriggerInNamespaceResponse> updateTriggerDetailsWithHttpInfo(String namespaceId, String triggerName, UpdateTrigger updateTrigger) throws ApiException {
        okhttp3.Call localVarCall = updateTriggerDetailsValidateBeforeCall(namespaceId, triggerName, updateTrigger, null);
        Type localVarReturnType = new TypeToken<FunctionsCreateTriggerInNamespaceResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateTriggerDetailsAsync(String namespaceId, String triggerName, UpdateTrigger updateTrigger, final ApiCallback<FunctionsCreateTriggerInNamespaceResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateTriggerDetailsValidateBeforeCall(namespaceId, triggerName, updateTrigger, _callback);
        Type localVarReturnType = new TypeToken<FunctionsCreateTriggerInNamespaceResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateTriggerDetailsRequestBuilder {
        private final String namespaceId;
        private final String triggerName;
        private Boolean isEnabled;
        private ScheduledDetails scheduledDetails;

        private UpdateTriggerDetailsRequestBuilder(String namespaceId, String triggerName) {
            this.namespaceId = namespaceId;
            this.triggerName = triggerName;
        }

        /**
         * Set isEnabled
         * @param isEnabled Indicates weather the trigger is paused or unpaused. (optional)
         * @return UpdateTriggerDetailsRequestBuilder
         */
        public UpdateTriggerDetailsRequestBuilder isEnabled(Boolean isEnabled) {
            this.isEnabled = isEnabled;
            return this;
        }
        
        /**
         * Set scheduledDetails
         * @param scheduledDetails  (optional)
         * @return UpdateTriggerDetailsRequestBuilder
         */
        public UpdateTriggerDetailsRequestBuilder scheduledDetails(ScheduledDetails scheduledDetails) {
            this.scheduledDetails = scheduledDetails;
            return this;
        }
        
        /**
         * Build call for updateTriggerDetails
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            UpdateTrigger updateTrigger = buildBodyParams();
            return updateTriggerDetailsCall(namespaceId, triggerName, updateTrigger, _callback);
        }

        private UpdateTrigger buildBodyParams() {
            UpdateTrigger updateTrigger = new UpdateTrigger();
            updateTrigger.isEnabled(this.isEnabled);
            updateTrigger.scheduledDetails(this.scheduledDetails);
            return updateTrigger;
        }

        /**
         * Execute updateTriggerDetails request
         * @return FunctionsCreateTriggerInNamespaceResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public FunctionsCreateTriggerInNamespaceResponse execute() throws ApiException {
            UpdateTrigger updateTrigger = buildBodyParams();
            ApiResponse<FunctionsCreateTriggerInNamespaceResponse> localVarResp = updateTriggerDetailsWithHttpInfo(namespaceId, triggerName, updateTrigger);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateTriggerDetails request with HTTP info returned
         * @return ApiResponse&lt;FunctionsCreateTriggerInNamespaceResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<FunctionsCreateTriggerInNamespaceResponse> executeWithHttpInfo() throws ApiException {
            UpdateTrigger updateTrigger = buildBodyParams();
            return updateTriggerDetailsWithHttpInfo(namespaceId, triggerName, updateTrigger);
        }

        /**
         * Execute updateTriggerDetails request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<FunctionsCreateTriggerInNamespaceResponse> _callback) throws ApiException {
            UpdateTrigger updateTrigger = buildBodyParams();
            return updateTriggerDetailsAsync(namespaceId, triggerName, updateTrigger, _callback);
        }
    }

    /**
     * Update Trigger
     * Updates the details of the given trigger. To update a trigger, send a PUT request to &#x60;/v2/functions/namespaces/$NAMESPACE_ID/triggers/$TRIGGER_NAME&#x60; with new values for the &#x60;is_enabled &#x60; or &#x60;scheduled_details&#x60; properties.
     * @param namespaceId The ID of the namespace to be managed. (required)
     * @param triggerName The name of the trigger to be managed. (required)
     * @param updateTrigger  (required)
     * @return UpdateTriggerDetailsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON response object with a key called &#x60;trigger&#x60;. The object contains the properties associated with the trigger. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateTriggerDetailsRequestBuilder updateTriggerDetails(String namespaceId, String triggerName) throws IllegalArgumentException {
        if (namespaceId == null) throw new IllegalArgumentException("\"namespaceId\" is required but got null");
            

        if (triggerName == null) throw new IllegalArgumentException("\"triggerName\" is required but got null");
            

        return new UpdateTriggerDetailsRequestBuilder(namespaceId, triggerName);
    }
}
