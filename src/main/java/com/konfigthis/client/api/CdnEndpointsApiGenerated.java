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


import com.konfigthis.client.model.CdnEndpoint;
import com.konfigthis.client.model.CdnEndpointsCreateNewEndpointResponse;
import com.konfigthis.client.model.CdnEndpointsListAllResponse;
import com.konfigthis.client.model.Error;
import java.time.OffsetDateTime;
import com.konfigthis.client.model.PurgeCache;
import java.util.UUID;
import com.konfigthis.client.model.UpdateEndpoint;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class CdnEndpointsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public CdnEndpointsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public CdnEndpointsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call createNewEndpointCall(CdnEndpoint cdnEndpoint, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = cdnEndpoint;

        // create path and map variables
        String localVarPath = "/v2/cdn/endpoints";

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
    private okhttp3.Call createNewEndpointValidateBeforeCall(CdnEndpoint cdnEndpoint, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'cdnEndpoint' is set
        if (cdnEndpoint == null) {
            throw new ApiException("Missing the required parameter 'cdnEndpoint' when calling createNewEndpoint(Async)");
        }

        return createNewEndpointCall(cdnEndpoint, _callback);

    }


    private ApiResponse<CdnEndpointsCreateNewEndpointResponse> createNewEndpointWithHttpInfo(CdnEndpoint cdnEndpoint) throws ApiException {
        okhttp3.Call localVarCall = createNewEndpointValidateBeforeCall(cdnEndpoint, null);
        Type localVarReturnType = new TypeToken<CdnEndpointsCreateNewEndpointResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createNewEndpointAsync(CdnEndpoint cdnEndpoint, final ApiCallback<CdnEndpointsCreateNewEndpointResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createNewEndpointValidateBeforeCall(cdnEndpoint, _callback);
        Type localVarReturnType = new TypeToken<CdnEndpointsCreateNewEndpointResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateNewEndpointRequestBuilder {
        private final String origin;
        private UUID id;
        private String endpoint;
        private Integer ttl;
        private UUID certificateId;
        private String customDomain;
        private OffsetDateTime createdAt;

        private CreateNewEndpointRequestBuilder(String origin) {
            this.origin = origin;
        }

        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a CDN endpoint. (optional)
         * @return CreateNewEndpointRequestBuilder
         */
        public CreateNewEndpointRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set endpoint
         * @param endpoint The fully qualified domain name (FQDN) from which the CDN-backed content is served. (optional)
         * @return CreateNewEndpointRequestBuilder
         */
        public CreateNewEndpointRequestBuilder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }
        
        /**
         * Set ttl
         * @param ttl The amount of time the content is cached by the CDN&#39;s edge servers in seconds. TTL must be one of 60, 600, 3600, 86400, or 604800. Defaults to 3600 (one hour) when excluded. (optional, default to 3600)
         * @return CreateNewEndpointRequestBuilder
         */
        public CreateNewEndpointRequestBuilder ttl(Integer ttl) {
            this.ttl = ttl;
            return this;
        }
        
        /**
         * Set certificateId
         * @param certificateId The ID of a DigitalOcean managed TLS certificate used for SSL when a custom subdomain is provided. (optional)
         * @return CreateNewEndpointRequestBuilder
         */
        public CreateNewEndpointRequestBuilder certificateId(UUID certificateId) {
            this.certificateId = certificateId;
            return this;
        }
        
        /**
         * Set customDomain
         * @param customDomain The fully qualified domain name (FQDN) of the custom subdomain used with the CDN endpoint. (optional)
         * @return CreateNewEndpointRequestBuilder
         */
        public CreateNewEndpointRequestBuilder customDomain(String customDomain) {
            this.customDomain = customDomain;
            return this;
        }
        
        /**
         * Set createdAt
         * @param createdAt A time value given in ISO8601 combined date and time format that represents when the CDN endpoint was created. (optional)
         * @return CreateNewEndpointRequestBuilder
         */
        public CreateNewEndpointRequestBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        /**
         * Build call for createNewEndpoint
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            CdnEndpoint cdnEndpoint = buildBodyParams();
            return createNewEndpointCall(cdnEndpoint, _callback);
        }

        private CdnEndpoint buildBodyParams() {
            CdnEndpoint cdnEndpoint = new CdnEndpoint();
            cdnEndpoint.id(this.id);
            cdnEndpoint.origin(this.origin);
            cdnEndpoint.endpoint(this.endpoint);
            if (this.ttl != null)
            cdnEndpoint.ttl(CdnEndpoint.TtlEnum.fromValue(this.ttl));
            cdnEndpoint.certificateId(this.certificateId);
            cdnEndpoint.customDomain(this.customDomain);
            cdnEndpoint.createdAt(this.createdAt);
            return cdnEndpoint;
        }

        /**
         * Execute createNewEndpoint request
         * @return CdnEndpointsCreateNewEndpointResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public CdnEndpointsCreateNewEndpointResponse execute() throws ApiException {
            CdnEndpoint cdnEndpoint = buildBodyParams();
            ApiResponse<CdnEndpointsCreateNewEndpointResponse> localVarResp = createNewEndpointWithHttpInfo(cdnEndpoint);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createNewEndpoint request with HTTP info returned
         * @return ApiResponse&lt;CdnEndpointsCreateNewEndpointResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<CdnEndpointsCreateNewEndpointResponse> executeWithHttpInfo() throws ApiException {
            CdnEndpoint cdnEndpoint = buildBodyParams();
            return createNewEndpointWithHttpInfo(cdnEndpoint);
        }

        /**
         * Execute createNewEndpoint request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<CdnEndpointsCreateNewEndpointResponse> _callback) throws ApiException {
            CdnEndpoint cdnEndpoint = buildBodyParams();
            return createNewEndpointAsync(cdnEndpoint, _callback);
        }
    }

    /**
     * Create a New CDN Endpoint
     * To create a new CDN endpoint, send a POST request to &#x60;/v2/cdn/endpoints&#x60;. The origin attribute must be set to the fully qualified domain name (FQDN) of a DigitalOcean Space. Optionally, the TTL may be configured by setting the &#x60;ttl&#x60; attribute.  A custom subdomain may be configured by specifying the &#x60;custom_domain&#x60; and &#x60;certificate_id&#x60; attributes. 
     * @param cdnEndpoint  (required)
     * @return CreateNewEndpointRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateNewEndpointRequestBuilder createNewEndpoint(String origin) throws IllegalArgumentException {
        if (origin == null) throw new IllegalArgumentException("\"origin\" is required but got null");
            

        return new CreateNewEndpointRequestBuilder(origin);
    }
    private okhttp3.Call deleteEndpointCall(UUID cdnId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/cdn/endpoints/{cdn_id}"
            .replace("{" + "cdn_id" + "}", localVarApiClient.escapeString(cdnId.toString()));

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
    private okhttp3.Call deleteEndpointValidateBeforeCall(UUID cdnId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'cdnId' is set
        if (cdnId == null) {
            throw new ApiException("Missing the required parameter 'cdnId' when calling deleteEndpoint(Async)");
        }

        return deleteEndpointCall(cdnId, _callback);

    }


    private ApiResponse<Void> deleteEndpointWithHttpInfo(UUID cdnId) throws ApiException {
        okhttp3.Call localVarCall = deleteEndpointValidateBeforeCall(cdnId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteEndpointAsync(UUID cdnId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteEndpointValidateBeforeCall(cdnId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteEndpointRequestBuilder {
        private final UUID cdnId;

        private DeleteEndpointRequestBuilder(UUID cdnId) {
            this.cdnId = cdnId;
        }

        /**
         * Build call for deleteEndpoint
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
            return deleteEndpointCall(cdnId, _callback);
        }


        /**
         * Execute deleteEndpoint request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteEndpointWithHttpInfo(cdnId);
        }

        /**
         * Execute deleteEndpoint request with HTTP info returned
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
            return deleteEndpointWithHttpInfo(cdnId);
        }

        /**
         * Execute deleteEndpoint request (asynchronously)
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
            return deleteEndpointAsync(cdnId, _callback);
        }
    }

    /**
     * Delete a CDN Endpoint
     * To delete a specific CDN endpoint, send a DELETE request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 
     * @param cdnId A unique identifier for a CDN endpoint. (required)
     * @return DeleteEndpointRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteEndpointRequestBuilder deleteEndpoint(UUID cdnId) throws IllegalArgumentException {
        if (cdnId == null) throw new IllegalArgumentException("\"cdnId\" is required but got null");
            

        return new DeleteEndpointRequestBuilder(cdnId);
    }
    private okhttp3.Call getExistingEndpointCall(UUID cdnId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/cdn/endpoints/{cdn_id}"
            .replace("{" + "cdn_id" + "}", localVarApiClient.escapeString(cdnId.toString()));

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
    private okhttp3.Call getExistingEndpointValidateBeforeCall(UUID cdnId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'cdnId' is set
        if (cdnId == null) {
            throw new ApiException("Missing the required parameter 'cdnId' when calling getExistingEndpoint(Async)");
        }

        return getExistingEndpointCall(cdnId, _callback);

    }


    private ApiResponse<CdnEndpointsCreateNewEndpointResponse> getExistingEndpointWithHttpInfo(UUID cdnId) throws ApiException {
        okhttp3.Call localVarCall = getExistingEndpointValidateBeforeCall(cdnId, null);
        Type localVarReturnType = new TypeToken<CdnEndpointsCreateNewEndpointResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getExistingEndpointAsync(UUID cdnId, final ApiCallback<CdnEndpointsCreateNewEndpointResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getExistingEndpointValidateBeforeCall(cdnId, _callback);
        Type localVarReturnType = new TypeToken<CdnEndpointsCreateNewEndpointResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetExistingEndpointRequestBuilder {
        private final UUID cdnId;

        private GetExistingEndpointRequestBuilder(UUID cdnId) {
            this.cdnId = cdnId;
        }

        /**
         * Build call for getExistingEndpoint
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getExistingEndpointCall(cdnId, _callback);
        }


        /**
         * Execute getExistingEndpoint request
         * @return CdnEndpointsCreateNewEndpointResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public CdnEndpointsCreateNewEndpointResponse execute() throws ApiException {
            ApiResponse<CdnEndpointsCreateNewEndpointResponse> localVarResp = getExistingEndpointWithHttpInfo(cdnId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getExistingEndpoint request with HTTP info returned
         * @return ApiResponse&lt;CdnEndpointsCreateNewEndpointResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<CdnEndpointsCreateNewEndpointResponse> executeWithHttpInfo() throws ApiException {
            return getExistingEndpointWithHttpInfo(cdnId);
        }

        /**
         * Execute getExistingEndpoint request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<CdnEndpointsCreateNewEndpointResponse> _callback) throws ApiException {
            return getExistingEndpointAsync(cdnId, _callback);
        }
    }

    /**
     * Retrieve an Existing CDN Endpoint
     * To show information about an existing CDN endpoint, send a GET request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID&#x60;.
     * @param cdnId A unique identifier for a CDN endpoint. (required)
     * @return GetExistingEndpointRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetExistingEndpointRequestBuilder getExistingEndpoint(UUID cdnId) throws IllegalArgumentException {
        if (cdnId == null) throw new IllegalArgumentException("\"cdnId\" is required but got null");
            

        return new GetExistingEndpointRequestBuilder(cdnId);
    }
    private okhttp3.Call listAllCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/cdn/endpoints";

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
    private okhttp3.Call listAllValidateBeforeCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        return listAllCall(perPage, page, _callback);

    }


    private ApiResponse<CdnEndpointsListAllResponse> listAllWithHttpInfo(Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listAllValidateBeforeCall(perPage, page, null);
        Type localVarReturnType = new TypeToken<CdnEndpointsListAllResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAllAsync(Integer perPage, Integer page, final ApiCallback<CdnEndpointsListAllResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAllValidateBeforeCall(perPage, page, _callback);
        Type localVarReturnType = new TypeToken<CdnEndpointsListAllResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAllRequestBuilder {
        private Integer perPage;
        private Integer page;

        private ListAllRequestBuilder() {
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListAllRequestBuilder
         */
        public ListAllRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listAll
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The result will be a JSON object with an &#x60;endpoints&#x60; key. This will be set to an array of endpoint objects, each of which will contain the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAllCall(perPage, page, _callback);
        }


        /**
         * Execute listAll request
         * @return CdnEndpointsListAllResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The result will be a JSON object with an &#x60;endpoints&#x60; key. This will be set to an array of endpoint objects, each of which will contain the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public CdnEndpointsListAllResponse execute() throws ApiException {
            ApiResponse<CdnEndpointsListAllResponse> localVarResp = listAllWithHttpInfo(perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAll request with HTTP info returned
         * @return ApiResponse&lt;CdnEndpointsListAllResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The result will be a JSON object with an &#x60;endpoints&#x60; key. This will be set to an array of endpoint objects, each of which will contain the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<CdnEndpointsListAllResponse> executeWithHttpInfo() throws ApiException {
            return listAllWithHttpInfo(perPage, page);
        }

        /**
         * Execute listAll request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The result will be a JSON object with an &#x60;endpoints&#x60; key. This will be set to an array of endpoint objects, each of which will contain the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<CdnEndpointsListAllResponse> _callback) throws ApiException {
            return listAllAsync(perPage, page, _callback);
        }
    }

    /**
     * List All CDN Endpoints
     * To list all of the CDN endpoints available on your account, send a GET request to &#x60;/v2/cdn/endpoints&#x60;.
     * @return ListAllRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The result will be a JSON object with an &#x60;endpoints&#x60; key. This will be set to an array of endpoint objects, each of which will contain the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListAllRequestBuilder listAll() throws IllegalArgumentException {
        return new ListAllRequestBuilder();
    }
    private okhttp3.Call purgeCacheCall(UUID cdnId, PurgeCache purgeCache, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = purgeCache;

        // create path and map variables
        String localVarPath = "/v2/cdn/endpoints/{cdn_id}/cache"
            .replace("{" + "cdn_id" + "}", localVarApiClient.escapeString(cdnId.toString()));

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
    private okhttp3.Call purgeCacheValidateBeforeCall(UUID cdnId, PurgeCache purgeCache, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'cdnId' is set
        if (cdnId == null) {
            throw new ApiException("Missing the required parameter 'cdnId' when calling purgeCache(Async)");
        }

        // verify the required parameter 'purgeCache' is set
        if (purgeCache == null) {
            throw new ApiException("Missing the required parameter 'purgeCache' when calling purgeCache(Async)");
        }

        return purgeCacheCall(cdnId, purgeCache, _callback);

    }


    private ApiResponse<Void> purgeCacheWithHttpInfo(UUID cdnId, PurgeCache purgeCache) throws ApiException {
        okhttp3.Call localVarCall = purgeCacheValidateBeforeCall(cdnId, purgeCache, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call purgeCacheAsync(UUID cdnId, PurgeCache purgeCache, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = purgeCacheValidateBeforeCall(cdnId, purgeCache, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class PurgeCacheRequestBuilder {
        private final List<String> files;
        private final UUID cdnId;

        private PurgeCacheRequestBuilder(List<String> files, UUID cdnId) {
            this.files = files;
            this.cdnId = cdnId;
        }

        /**
         * Build call for purgeCache
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
            PurgeCache purgeCache = buildBodyParams();
            return purgeCacheCall(cdnId, purgeCache, _callback);
        }

        private PurgeCache buildBodyParams() {
            PurgeCache purgeCache = new PurgeCache();
            purgeCache.files(this.files);
            return purgeCache;
        }

        /**
         * Execute purgeCache request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            PurgeCache purgeCache = buildBodyParams();
            purgeCacheWithHttpInfo(cdnId, purgeCache);
        }

        /**
         * Execute purgeCache request with HTTP info returned
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
            PurgeCache purgeCache = buildBodyParams();
            return purgeCacheWithHttpInfo(cdnId, purgeCache);
        }

        /**
         * Execute purgeCache request (asynchronously)
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
            PurgeCache purgeCache = buildBodyParams();
            return purgeCacheAsync(cdnId, purgeCache, _callback);
        }
    }

    /**
     * Purge the Cache for an Existing CDN Endpoint
     * To purge cached content from a CDN endpoint, send a DELETE request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID/cache&#x60;. The body of the request should include a &#x60;files&#x60; attribute containing a list of cached file paths to be purged. A path may be for a single file or may contain a wildcard (&#x60;*&#x60;) to recursively purge all files under a directory. When only a wildcard is provided, all cached files will be purged. There is a rate limit of 50 files per 20 seconds  that can be purged. 
     * @param cdnId A unique identifier for a CDN endpoint. (required)
     * @param purgeCache  (required)
     * @return PurgeCacheRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public PurgeCacheRequestBuilder purgeCache(List<String> files, UUID cdnId) throws IllegalArgumentException {
        if (files == null) throw new IllegalArgumentException("\"files\" is required but got null");
        if (cdnId == null) throw new IllegalArgumentException("\"cdnId\" is required but got null");
            

        return new PurgeCacheRequestBuilder(files, cdnId);
    }
    private okhttp3.Call updateEndpointCall(UUID cdnId, UpdateEndpoint updateEndpoint, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = updateEndpoint;

        // create path and map variables
        String localVarPath = "/v2/cdn/endpoints/{cdn_id}"
            .replace("{" + "cdn_id" + "}", localVarApiClient.escapeString(cdnId.toString()));

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
    private okhttp3.Call updateEndpointValidateBeforeCall(UUID cdnId, UpdateEndpoint updateEndpoint, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'cdnId' is set
        if (cdnId == null) {
            throw new ApiException("Missing the required parameter 'cdnId' when calling updateEndpoint(Async)");
        }

        // verify the required parameter 'updateEndpoint' is set
        if (updateEndpoint == null) {
            throw new ApiException("Missing the required parameter 'updateEndpoint' when calling updateEndpoint(Async)");
        }

        return updateEndpointCall(cdnId, updateEndpoint, _callback);

    }


    private ApiResponse<CdnEndpointsCreateNewEndpointResponse> updateEndpointWithHttpInfo(UUID cdnId, UpdateEndpoint updateEndpoint) throws ApiException {
        okhttp3.Call localVarCall = updateEndpointValidateBeforeCall(cdnId, updateEndpoint, null);
        Type localVarReturnType = new TypeToken<CdnEndpointsCreateNewEndpointResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateEndpointAsync(UUID cdnId, UpdateEndpoint updateEndpoint, final ApiCallback<CdnEndpointsCreateNewEndpointResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateEndpointValidateBeforeCall(cdnId, updateEndpoint, _callback);
        Type localVarReturnType = new TypeToken<CdnEndpointsCreateNewEndpointResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateEndpointRequestBuilder {
        private final UUID cdnId;
        private Integer ttl;
        private UUID certificateId;
        private String customDomain;

        private UpdateEndpointRequestBuilder(UUID cdnId) {
            this.cdnId = cdnId;
        }

        /**
         * Set ttl
         * @param ttl The amount of time the content is cached by the CDN&#39;s edge servers in seconds. TTL must be one of 60, 600, 3600, 86400, or 604800. Defaults to 3600 (one hour) when excluded. (optional, default to 3600)
         * @return UpdateEndpointRequestBuilder
         */
        public UpdateEndpointRequestBuilder ttl(Integer ttl) {
            this.ttl = ttl;
            return this;
        }
        
        /**
         * Set certificateId
         * @param certificateId The ID of a DigitalOcean managed TLS certificate used for SSL when a custom subdomain is provided. (optional)
         * @return UpdateEndpointRequestBuilder
         */
        public UpdateEndpointRequestBuilder certificateId(UUID certificateId) {
            this.certificateId = certificateId;
            return this;
        }
        
        /**
         * Set customDomain
         * @param customDomain The fully qualified domain name (FQDN) of the custom subdomain used with the CDN endpoint. (optional)
         * @return UpdateEndpointRequestBuilder
         */
        public UpdateEndpointRequestBuilder customDomain(String customDomain) {
            this.customDomain = customDomain;
            return this;
        }
        
        /**
         * Build call for updateEndpoint
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            UpdateEndpoint updateEndpoint = buildBodyParams();
            return updateEndpointCall(cdnId, updateEndpoint, _callback);
        }

        private UpdateEndpoint buildBodyParams() {
            UpdateEndpoint updateEndpoint = new UpdateEndpoint();
            if (this.ttl != null)
            updateEndpoint.ttl(UpdateEndpoint.TtlEnum.fromValue(this.ttl));
            updateEndpoint.certificateId(this.certificateId);
            updateEndpoint.customDomain(this.customDomain);
            return updateEndpoint;
        }

        /**
         * Execute updateEndpoint request
         * @return CdnEndpointsCreateNewEndpointResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public CdnEndpointsCreateNewEndpointResponse execute() throws ApiException {
            UpdateEndpoint updateEndpoint = buildBodyParams();
            ApiResponse<CdnEndpointsCreateNewEndpointResponse> localVarResp = updateEndpointWithHttpInfo(cdnId, updateEndpoint);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateEndpoint request with HTTP info returned
         * @return ApiResponse&lt;CdnEndpointsCreateNewEndpointResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<CdnEndpointsCreateNewEndpointResponse> executeWithHttpInfo() throws ApiException {
            UpdateEndpoint updateEndpoint = buildBodyParams();
            return updateEndpointWithHttpInfo(cdnId, updateEndpoint);
        }

        /**
         * Execute updateEndpoint request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<CdnEndpointsCreateNewEndpointResponse> _callback) throws ApiException {
            UpdateEndpoint updateEndpoint = buildBodyParams();
            return updateEndpointAsync(cdnId, updateEndpoint, _callback);
        }
    }

    /**
     * Update a CDN Endpoint
     * To update the TTL, certificate ID, or the FQDN of the custom subdomain for an existing CDN endpoint, send a PUT request to &#x60;/v2/cdn/endpoints/$ENDPOINT_ID&#x60;. 
     * @param cdnId A unique identifier for a CDN endpoint. (required)
     * @param updateEndpoint  (required)
     * @return UpdateEndpointRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with an &#x60;endpoint&#x60; key. This will be set to an object containing the standard CDN endpoint attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateEndpointRequestBuilder updateEndpoint(UUID cdnId) throws IllegalArgumentException {
        if (cdnId == null) throw new IllegalArgumentException("\"cdnId\" is required but got null");
            

        return new UpdateEndpointRequestBuilder(cdnId);
    }
}
