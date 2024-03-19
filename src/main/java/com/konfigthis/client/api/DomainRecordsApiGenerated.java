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


import com.konfigthis.client.model.DomainRecord;
import com.konfigthis.client.model.DomainRecordsCreateNewRecordResponse;
import com.konfigthis.client.model.DomainRecordsGetExistingRecordResponse;
import com.konfigthis.client.model.DomainRecordsListAllRecordsResponse;
import com.konfigthis.client.model.Error;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class DomainRecordsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public DomainRecordsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public DomainRecordsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call createNewRecordCall(String domainName, Object body, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/domains/{domain_name}/records"
            .replace("{" + "domain_name" + "}", localVarApiClient.escapeString(domainName.toString()));

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
    private okhttp3.Call createNewRecordValidateBeforeCall(String domainName, Object body, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'domainName' is set
        if (domainName == null) {
            throw new ApiException("Missing the required parameter 'domainName' when calling createNewRecord(Async)");
        }

        return createNewRecordCall(domainName, body, _callback);

    }


    private ApiResponse<DomainRecordsCreateNewRecordResponse> createNewRecordWithHttpInfo(String domainName, Object body) throws ApiException {
        okhttp3.Call localVarCall = createNewRecordValidateBeforeCall(domainName, body, null);
        Type localVarReturnType = new TypeToken<DomainRecordsCreateNewRecordResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createNewRecordAsync(String domainName, Object body, final ApiCallback<DomainRecordsCreateNewRecordResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createNewRecordValidateBeforeCall(domainName, body, _callback);
        Type localVarReturnType = new TypeToken<DomainRecordsCreateNewRecordResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateNewRecordRequestBuilder {
        private final String domainName;
        private Object body;

        private CreateNewRecordRequestBuilder(String domainName) {
            this.domainName = domainName;
        }

        /**
         * Set body
         * @param body  (optional)
         * @return CreateNewRecordRequestBuilder
         */
        public CreateNewRecordRequestBuilder body(Object body) {
            this.body = body;
            return this;
        }

        /**
         * Build call for createNewRecord
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response body will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be an object representing the new record. Attributes that are not applicable for the record type will be set to &#x60;null&#x60;. An &#x60;id&#x60; attribute is generated for each record as part of the object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Object body = buildBodyParams();
            return createNewRecordCall(domainName, body, _callback);
        }

        private Object buildBodyParams() {
            return this.body;
        }

        /**
         * Execute createNewRecord request
         * @return DomainRecordsCreateNewRecordResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response body will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be an object representing the new record. Attributes that are not applicable for the record type will be set to &#x60;null&#x60;. An &#x60;id&#x60; attribute is generated for each record as part of the object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DomainRecordsCreateNewRecordResponse execute() throws ApiException {
            Object body = buildBodyParams();
            ApiResponse<DomainRecordsCreateNewRecordResponse> localVarResp = createNewRecordWithHttpInfo(domainName, body);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createNewRecord request with HTTP info returned
         * @return ApiResponse&lt;DomainRecordsCreateNewRecordResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response body will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be an object representing the new record. Attributes that are not applicable for the record type will be set to &#x60;null&#x60;. An &#x60;id&#x60; attribute is generated for each record as part of the object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DomainRecordsCreateNewRecordResponse> executeWithHttpInfo() throws ApiException {
            Object body = buildBodyParams();
            return createNewRecordWithHttpInfo(domainName, body);
        }

        /**
         * Execute createNewRecord request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response body will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be an object representing the new record. Attributes that are not applicable for the record type will be set to &#x60;null&#x60;. An &#x60;id&#x60; attribute is generated for each record as part of the object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DomainRecordsCreateNewRecordResponse> _callback) throws ApiException {
            Object body = buildBodyParams();
            return createNewRecordAsync(domainName, body, _callback);
        }
    }

    /**
     * Create a New Domain Record
     * To create a new record to a domain, send a POST request to &#x60;/v2/domains/$DOMAIN_NAME/records&#x60;.  The request must include all of the required fields for the domain record type being added.  See the [attribute table](https://api-engineering.nyc3.cdn.digitaloceanspaces.com) for details regarding record types and their respective required attributes. 
     * @param domainName The name of the domain itself. (required)
     * @return CreateNewRecordRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response body will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be an object representing the new record. Attributes that are not applicable for the record type will be set to &#x60;null&#x60;. An &#x60;id&#x60; attribute is generated for each record as part of the object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateNewRecordRequestBuilder createNewRecord(String domainName) throws IllegalArgumentException {
        if (domainName == null) throw new IllegalArgumentException("\"domainName\" is required but got null");
            

        return new CreateNewRecordRequestBuilder(domainName);
    }
    private okhttp3.Call deleteByIdCall(String domainName, Integer domainRecordId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/domains/{domain_name}/records/{domain_record_id}"
            .replace("{" + "domain_name" + "}", localVarApiClient.escapeString(domainName.toString()))
            .replace("{" + "domain_record_id" + "}", localVarApiClient.escapeString(domainRecordId.toString()));

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
    private okhttp3.Call deleteByIdValidateBeforeCall(String domainName, Integer domainRecordId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'domainName' is set
        if (domainName == null) {
            throw new ApiException("Missing the required parameter 'domainName' when calling deleteById(Async)");
        }

        // verify the required parameter 'domainRecordId' is set
        if (domainRecordId == null) {
            throw new ApiException("Missing the required parameter 'domainRecordId' when calling deleteById(Async)");
        }

        return deleteByIdCall(domainName, domainRecordId, _callback);

    }


    private ApiResponse<Void> deleteByIdWithHttpInfo(String domainName, Integer domainRecordId) throws ApiException {
        okhttp3.Call localVarCall = deleteByIdValidateBeforeCall(domainName, domainRecordId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteByIdAsync(String domainName, Integer domainRecordId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteByIdValidateBeforeCall(domainName, domainRecordId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteByIdRequestBuilder {
        private final String domainName;
        private final Integer domainRecordId;

        private DeleteByIdRequestBuilder(String domainName, Integer domainRecordId) {
            this.domainName = domainName;
            this.domainRecordId = domainRecordId;
        }

        /**
         * Build call for deleteById
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
            return deleteByIdCall(domainName, domainRecordId, _callback);
        }


        /**
         * Execute deleteById request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteByIdWithHttpInfo(domainName, domainRecordId);
        }

        /**
         * Execute deleteById request with HTTP info returned
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
            return deleteByIdWithHttpInfo(domainName, domainRecordId);
        }

        /**
         * Execute deleteById request (asynchronously)
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
            return deleteByIdAsync(domainName, domainRecordId, _callback);
        }
    }

    /**
     * Delete a Domain Record
     * To delete a record for a domain, send a DELETE request to &#x60;/v2/domains/$DOMAIN_NAME/records/$DOMAIN_RECORD_ID&#x60;.  The record will be deleted and the response status will be a 204. This indicates a successful request with no body returned. 
     * @param domainName The name of the domain itself. (required)
     * @param domainRecordId The unique identifier of the domain record. (required)
     * @return DeleteByIdRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteByIdRequestBuilder deleteById(String domainName, Integer domainRecordId) throws IllegalArgumentException {
        if (domainName == null) throw new IllegalArgumentException("\"domainName\" is required but got null");
            

        if (domainRecordId == null) throw new IllegalArgumentException("\"domainRecordId\" is required but got null");
        return new DeleteByIdRequestBuilder(domainName, domainRecordId);
    }
    private okhttp3.Call getExistingRecordCall(String domainName, Integer domainRecordId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/domains/{domain_name}/records/{domain_record_id}"
            .replace("{" + "domain_name" + "}", localVarApiClient.escapeString(domainName.toString()))
            .replace("{" + "domain_record_id" + "}", localVarApiClient.escapeString(domainRecordId.toString()));

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
    private okhttp3.Call getExistingRecordValidateBeforeCall(String domainName, Integer domainRecordId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'domainName' is set
        if (domainName == null) {
            throw new ApiException("Missing the required parameter 'domainName' when calling getExistingRecord(Async)");
        }

        // verify the required parameter 'domainRecordId' is set
        if (domainRecordId == null) {
            throw new ApiException("Missing the required parameter 'domainRecordId' when calling getExistingRecord(Async)");
        }

        return getExistingRecordCall(domainName, domainRecordId, _callback);

    }


    private ApiResponse<DomainRecordsGetExistingRecordResponse> getExistingRecordWithHttpInfo(String domainName, Integer domainRecordId) throws ApiException {
        okhttp3.Call localVarCall = getExistingRecordValidateBeforeCall(domainName, domainRecordId, null);
        Type localVarReturnType = new TypeToken<DomainRecordsGetExistingRecordResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getExistingRecordAsync(String domainName, Integer domainRecordId, final ApiCallback<DomainRecordsGetExistingRecordResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getExistingRecordValidateBeforeCall(domainName, domainRecordId, _callback);
        Type localVarReturnType = new TypeToken<DomainRecordsGetExistingRecordResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetExistingRecordRequestBuilder {
        private final String domainName;
        private final Integer domainRecordId;

        private GetExistingRecordRequestBuilder(String domainName, Integer domainRecordId) {
            this.domainName = domainName;
            this.domainRecordId = domainRecordId;
        }

        /**
         * Build call for getExistingRecord
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getExistingRecordCall(domainName, domainRecordId, _callback);
        }


        /**
         * Execute getExistingRecord request
         * @return DomainRecordsGetExistingRecordResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DomainRecordsGetExistingRecordResponse execute() throws ApiException {
            ApiResponse<DomainRecordsGetExistingRecordResponse> localVarResp = getExistingRecordWithHttpInfo(domainName, domainRecordId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getExistingRecord request with HTTP info returned
         * @return ApiResponse&lt;DomainRecordsGetExistingRecordResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DomainRecordsGetExistingRecordResponse> executeWithHttpInfo() throws ApiException {
            return getExistingRecordWithHttpInfo(domainName, domainRecordId);
        }

        /**
         * Execute getExistingRecord request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DomainRecordsGetExistingRecordResponse> _callback) throws ApiException {
            return getExistingRecordAsync(domainName, domainRecordId, _callback);
        }
    }

    /**
     * Retrieve an Existing Domain Record
     * To retrieve a specific domain record, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records/$RECORD_ID&#x60;.
     * @param domainName The name of the domain itself. (required)
     * @param domainRecordId The unique identifier of the domain record. (required)
     * @return GetExistingRecordRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetExistingRecordRequestBuilder getExistingRecord(String domainName, Integer domainRecordId) throws IllegalArgumentException {
        if (domainName == null) throw new IllegalArgumentException("\"domainName\" is required but got null");
            

        if (domainRecordId == null) throw new IllegalArgumentException("\"domainRecordId\" is required but got null");
        return new GetExistingRecordRequestBuilder(domainName, domainRecordId);
    }
    private okhttp3.Call listAllRecordsCall(String domainName, String name, String type, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/domains/{domain_name}/records"
            .replace("{" + "domain_name" + "}", localVarApiClient.escapeString(domainName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (name != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("name", name));
        }

        if (type != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("type", type));
        }

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
    private okhttp3.Call listAllRecordsValidateBeforeCall(String domainName, String name, String type, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'domainName' is set
        if (domainName == null) {
            throw new ApiException("Missing the required parameter 'domainName' when calling listAllRecords(Async)");
        }

        return listAllRecordsCall(domainName, name, type, perPage, page, _callback);

    }


    private ApiResponse<DomainRecordsListAllRecordsResponse> listAllRecordsWithHttpInfo(String domainName, String name, String type, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listAllRecordsValidateBeforeCall(domainName, name, type, perPage, page, null);
        Type localVarReturnType = new TypeToken<DomainRecordsListAllRecordsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAllRecordsAsync(String domainName, String name, String type, Integer perPage, Integer page, final ApiCallback<DomainRecordsListAllRecordsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAllRecordsValidateBeforeCall(domainName, name, type, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<DomainRecordsListAllRecordsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAllRecordsRequestBuilder {
        private final String domainName;
        private String name;
        private String type;
        private Integer perPage;
        private Integer page;

        private ListAllRecordsRequestBuilder(String domainName) {
            this.domainName = domainName;
        }

        /**
         * Set name
         * @param name A fully qualified record name. For example, to only include records matching sub.example.com, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records?name&#x3D;sub.example.com&#x60;. (optional)
         * @return ListAllRecordsRequestBuilder
         */
        public ListAllRecordsRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set type
         * @param type The type of the DNS record. For example: A, CNAME, TXT, ... (optional)
         * @return ListAllRecordsRequestBuilder
         */
        public ListAllRecordsRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListAllRecordsRequestBuilder
         */
        public ListAllRecordsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListAllRecordsRequestBuilder
         */
        public ListAllRecordsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listAllRecords
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_records&#x60;. The value of this will be an array of domain record objects, each of which contains the standard domain record attributes. For attributes that are not used by a specific record type, a value of &#x60;null&#x60; will be returned. For instance, all records other than SRV will have &#x60;null&#x60; for the &#x60;weight&#x60; and &#x60;port&#x60; attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAllRecordsCall(domainName, name, type, perPage, page, _callback);
        }


        /**
         * Execute listAllRecords request
         * @return DomainRecordsListAllRecordsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_records&#x60;. The value of this will be an array of domain record objects, each of which contains the standard domain record attributes. For attributes that are not used by a specific record type, a value of &#x60;null&#x60; will be returned. For instance, all records other than SRV will have &#x60;null&#x60; for the &#x60;weight&#x60; and &#x60;port&#x60; attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DomainRecordsListAllRecordsResponse execute() throws ApiException {
            ApiResponse<DomainRecordsListAllRecordsResponse> localVarResp = listAllRecordsWithHttpInfo(domainName, name, type, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAllRecords request with HTTP info returned
         * @return ApiResponse&lt;DomainRecordsListAllRecordsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_records&#x60;. The value of this will be an array of domain record objects, each of which contains the standard domain record attributes. For attributes that are not used by a specific record type, a value of &#x60;null&#x60; will be returned. For instance, all records other than SRV will have &#x60;null&#x60; for the &#x60;weight&#x60; and &#x60;port&#x60; attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DomainRecordsListAllRecordsResponse> executeWithHttpInfo() throws ApiException {
            return listAllRecordsWithHttpInfo(domainName, name, type, perPage, page);
        }

        /**
         * Execute listAllRecords request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_records&#x60;. The value of this will be an array of domain record objects, each of which contains the standard domain record attributes. For attributes that are not used by a specific record type, a value of &#x60;null&#x60; will be returned. For instance, all records other than SRV will have &#x60;null&#x60; for the &#x60;weight&#x60; and &#x60;port&#x60; attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DomainRecordsListAllRecordsResponse> _callback) throws ApiException {
            return listAllRecordsAsync(domainName, name, type, perPage, page, _callback);
        }
    }

    /**
     * List All Domain Records
     * To get a listing of all records configured for a domain, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records&#x60;. The list of records returned can be filtered by using the &#x60;name&#x60; and &#x60;type&#x60; query parameters. For example, to only include A records for a domain, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records?type&#x3D;A&#x60;. &#x60;name&#x60; must be a fully qualified record name. For example, to only include records matching &#x60;sub.example.com&#x60;, send a GET request to &#x60;/v2/domains/$DOMAIN_NAME/records?name&#x3D;sub.example.com&#x60;. Both name and type may be used together.  
     * @param domainName The name of the domain itself. (required)
     * @return ListAllRecordsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_records&#x60;. The value of this will be an array of domain record objects, each of which contains the standard domain record attributes. For attributes that are not used by a specific record type, a value of &#x60;null&#x60; will be returned. For instance, all records other than SRV will have &#x60;null&#x60; for the &#x60;weight&#x60; and &#x60;port&#x60; attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListAllRecordsRequestBuilder listAllRecords(String domainName) throws IllegalArgumentException {
        if (domainName == null) throw new IllegalArgumentException("\"domainName\" is required but got null");
            

        return new ListAllRecordsRequestBuilder(domainName);
    }
    private okhttp3.Call updateRecordByIdCall(String domainName, Integer domainRecordId, DomainRecord domainRecord, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = domainRecord;

        // create path and map variables
        String localVarPath = "/v2/domains/{domain_name}/records/{domain_record_id}"
            .replace("{" + "domain_name" + "}", localVarApiClient.escapeString(domainName.toString()))
            .replace("{" + "domain_record_id" + "}", localVarApiClient.escapeString(domainRecordId.toString()));

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
    private okhttp3.Call updateRecordByIdValidateBeforeCall(String domainName, Integer domainRecordId, DomainRecord domainRecord, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'domainName' is set
        if (domainName == null) {
            throw new ApiException("Missing the required parameter 'domainName' when calling updateRecordById(Async)");
        }

        // verify the required parameter 'domainRecordId' is set
        if (domainRecordId == null) {
            throw new ApiException("Missing the required parameter 'domainRecordId' when calling updateRecordById(Async)");
        }

        return updateRecordByIdCall(domainName, domainRecordId, domainRecord, _callback);

    }


    private ApiResponse<DomainRecordsGetExistingRecordResponse> updateRecordByIdWithHttpInfo(String domainName, Integer domainRecordId, DomainRecord domainRecord) throws ApiException {
        okhttp3.Call localVarCall = updateRecordByIdValidateBeforeCall(domainName, domainRecordId, domainRecord, null);
        Type localVarReturnType = new TypeToken<DomainRecordsGetExistingRecordResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateRecordByIdAsync(String domainName, Integer domainRecordId, DomainRecord domainRecord, final ApiCallback<DomainRecordsGetExistingRecordResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateRecordByIdValidateBeforeCall(domainName, domainRecordId, domainRecord, _callback);
        Type localVarReturnType = new TypeToken<DomainRecordsGetExistingRecordResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateRecordByIdRequestBuilder {
        private final String type;
        private final String domainName;
        private final Integer domainRecordId;
        private Integer id;
        private String name;
        private String data;
        private Integer priority;
        private Integer port;
        private Integer ttl;
        private Integer weight;
        private Integer flags;
        private String tag;

        private UpdateRecordByIdRequestBuilder(String type, String domainName, Integer domainRecordId) {
            this.type = type;
            this.domainName = domainName;
            this.domainRecordId = domainRecordId;
        }

        /**
         * Set id
         * @param id A unique identifier for each domain record. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder id(Integer id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set name
         * @param name The host name, alias, or service being defined by the record. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set data
         * @param data Variable data depending on record type. For example, the \\\&quot;data\\\&quot; value for an A record would be the IPv4 address to which the domain will be mapped. For a CAA record, it would contain the domain name of the CA being granted permission to issue certificates. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder data(String data) {
            this.data = data;
            return this;
        }
        
        /**
         * Set priority
         * @param priority The priority for SRV and MX records. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder priority(Integer priority) {
            this.priority = priority;
            return this;
        }
        
        /**
         * Set port
         * @param port The port for SRV records. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder port(Integer port) {
            this.port = port;
            return this;
        }
        
        /**
         * Set ttl
         * @param ttl This value is the time to live for the record, in seconds. This defines the time frame that clients can cache queried information before a refresh should be requested. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder ttl(Integer ttl) {
            this.ttl = ttl;
            return this;
        }
        
        /**
         * Set weight
         * @param weight The weight for SRV records. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder weight(Integer weight) {
            this.weight = weight;
            return this;
        }
        
        /**
         * Set flags
         * @param flags An unsigned integer between 0-255 used for CAA records. (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder flags(Integer flags) {
            this.flags = flags;
            return this;
        }
        
        /**
         * Set tag
         * @param tag The parameter tag for CAA records. Valid values are \\\&quot;issue\\\&quot;, \\\&quot;issuewild\\\&quot;, or \\\&quot;iodef\\\&quot; (optional)
         * @return UpdateRecordByIdRequestBuilder
         */
        public UpdateRecordByIdRequestBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }
        
        /**
         * Build call for updateRecordById
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            return updateRecordByIdCall(domainName, domainRecordId, domainRecord, _callback);
        }

        private DomainRecord buildBodyParams() {
            DomainRecord domainRecord = new DomainRecord();
            domainRecord.id(this.id);
            domainRecord.type(this.type);
            domainRecord.name(this.name);
            domainRecord.data(this.data);
            domainRecord.priority(this.priority);
            domainRecord.port(this.port);
            domainRecord.ttl(this.ttl);
            domainRecord.weight(this.weight);
            domainRecord.flags(this.flags);
            domainRecord.tag(this.tag);
            return domainRecord;
        }

        /**
         * Execute updateRecordById request
         * @return DomainRecordsGetExistingRecordResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DomainRecordsGetExistingRecordResponse execute() throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            ApiResponse<DomainRecordsGetExistingRecordResponse> localVarResp = updateRecordByIdWithHttpInfo(domainName, domainRecordId, domainRecord);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateRecordById request with HTTP info returned
         * @return ApiResponse&lt;DomainRecordsGetExistingRecordResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DomainRecordsGetExistingRecordResponse> executeWithHttpInfo() throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            return updateRecordByIdWithHttpInfo(domainName, domainRecordId, domainRecord);
        }

        /**
         * Execute updateRecordById request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DomainRecordsGetExistingRecordResponse> _callback) throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            return updateRecordByIdAsync(domainName, domainRecordId, domainRecord, _callback);
        }
    }

    /**
     * Update a Domain Record
     * To update an existing record, send a PUT request to &#x60;/v2/domains/$DOMAIN_NAME/records/$DOMAIN_RECORD_ID&#x60;. Any attribute valid for the record type can be set to a new value for the record.  See the [attribute table](https://api-engineering.nyc3.cdn.digitaloceanspaces.com) for details regarding record types and their respective attributes. 
     * @param domainName The name of the domain itself. (required)
     * @param domainRecordId The unique identifier of the domain record. (required)
     * @return UpdateRecordByIdRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateRecordByIdRequestBuilder updateRecordById(String type, String domainName, Integer domainRecordId) throws IllegalArgumentException {
        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        if (domainName == null) throw new IllegalArgumentException("\"domainName\" is required but got null");
            

        if (domainRecordId == null) throw new IllegalArgumentException("\"domainRecordId\" is required but got null");
        return new UpdateRecordByIdRequestBuilder(type, domainName, domainRecordId);
    }
    private okhttp3.Call updateRecordById_0Call(String domainName, Integer domainRecordId, DomainRecord domainRecord, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = domainRecord;

        // create path and map variables
        String localVarPath = "/v2/domains/{domain_name}/records/{domain_record_id}"
            .replace("{" + "domain_name" + "}", localVarApiClient.escapeString(domainName.toString()))
            .replace("{" + "domain_record_id" + "}", localVarApiClient.escapeString(domainRecordId.toString()));

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
    private okhttp3.Call updateRecordById_0ValidateBeforeCall(String domainName, Integer domainRecordId, DomainRecord domainRecord, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'domainName' is set
        if (domainName == null) {
            throw new ApiException("Missing the required parameter 'domainName' when calling updateRecordById_0(Async)");
        }

        // verify the required parameter 'domainRecordId' is set
        if (domainRecordId == null) {
            throw new ApiException("Missing the required parameter 'domainRecordId' when calling updateRecordById_0(Async)");
        }

        return updateRecordById_0Call(domainName, domainRecordId, domainRecord, _callback);

    }


    private ApiResponse<DomainRecordsGetExistingRecordResponse> updateRecordById_0WithHttpInfo(String domainName, Integer domainRecordId, DomainRecord domainRecord) throws ApiException {
        okhttp3.Call localVarCall = updateRecordById_0ValidateBeforeCall(domainName, domainRecordId, domainRecord, null);
        Type localVarReturnType = new TypeToken<DomainRecordsGetExistingRecordResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateRecordById_0Async(String domainName, Integer domainRecordId, DomainRecord domainRecord, final ApiCallback<DomainRecordsGetExistingRecordResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateRecordById_0ValidateBeforeCall(domainName, domainRecordId, domainRecord, _callback);
        Type localVarReturnType = new TypeToken<DomainRecordsGetExistingRecordResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateRecordById0RequestBuilder {
        private final String type;
        private final String domainName;
        private final Integer domainRecordId;
        private Integer id;
        private String name;
        private String data;
        private Integer priority;
        private Integer port;
        private Integer ttl;
        private Integer weight;
        private Integer flags;
        private String tag;

        private UpdateRecordById0RequestBuilder(String type, String domainName, Integer domainRecordId) {
            this.type = type;
            this.domainName = domainName;
            this.domainRecordId = domainRecordId;
        }

        /**
         * Set id
         * @param id A unique identifier for each domain record. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder id(Integer id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set name
         * @param name The host name, alias, or service being defined by the record. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set data
         * @param data Variable data depending on record type. For example, the \\\&quot;data\\\&quot; value for an A record would be the IPv4 address to which the domain will be mapped. For a CAA record, it would contain the domain name of the CA being granted permission to issue certificates. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder data(String data) {
            this.data = data;
            return this;
        }
        
        /**
         * Set priority
         * @param priority The priority for SRV and MX records. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder priority(Integer priority) {
            this.priority = priority;
            return this;
        }
        
        /**
         * Set port
         * @param port The port for SRV records. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder port(Integer port) {
            this.port = port;
            return this;
        }
        
        /**
         * Set ttl
         * @param ttl This value is the time to live for the record, in seconds. This defines the time frame that clients can cache queried information before a refresh should be requested. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder ttl(Integer ttl) {
            this.ttl = ttl;
            return this;
        }
        
        /**
         * Set weight
         * @param weight The weight for SRV records. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder weight(Integer weight) {
            this.weight = weight;
            return this;
        }
        
        /**
         * Set flags
         * @param flags An unsigned integer between 0-255 used for CAA records. (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder flags(Integer flags) {
            this.flags = flags;
            return this;
        }
        
        /**
         * Set tag
         * @param tag The parameter tag for CAA records. Valid values are \\\&quot;issue\\\&quot;, \\\&quot;issuewild\\\&quot;, or \\\&quot;iodef\\\&quot; (optional)
         * @return UpdateRecordById0RequestBuilder
         */
        public UpdateRecordById0RequestBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }
        
        /**
         * Build call for updateRecordById_0
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            return updateRecordById_0Call(domainName, domainRecordId, domainRecord, _callback);
        }

        private DomainRecord buildBodyParams() {
            DomainRecord domainRecord = new DomainRecord();
            domainRecord.id(this.id);
            domainRecord.type(this.type);
            domainRecord.name(this.name);
            domainRecord.data(this.data);
            domainRecord.priority(this.priority);
            domainRecord.port(this.port);
            domainRecord.ttl(this.ttl);
            domainRecord.weight(this.weight);
            domainRecord.flags(this.flags);
            domainRecord.tag(this.tag);
            return domainRecord;
        }

        /**
         * Execute updateRecordById_0 request
         * @return DomainRecordsGetExistingRecordResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public DomainRecordsGetExistingRecordResponse execute() throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            ApiResponse<DomainRecordsGetExistingRecordResponse> localVarResp = updateRecordById_0WithHttpInfo(domainName, domainRecordId, domainRecord);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateRecordById_0 request with HTTP info returned
         * @return ApiResponse&lt;DomainRecordsGetExistingRecordResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<DomainRecordsGetExistingRecordResponse> executeWithHttpInfo() throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            return updateRecordById_0WithHttpInfo(domainName, domainRecordId, domainRecord);
        }

        /**
         * Execute updateRecordById_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<DomainRecordsGetExistingRecordResponse> _callback) throws ApiException {
            DomainRecord domainRecord = buildBodyParams();
            return updateRecordById_0Async(domainName, domainRecordId, domainRecord, _callback);
        }
    }

    /**
     * Update a Domain Record
     * To update an existing record, send a PATCH request to &#x60;/v2/domains/$DOMAIN_NAME/records/$DOMAIN_RECORD_ID&#x60;. Any attribute valid for the record type can be set to a new value for the record.  See the [attribute table](https://api-engineering.nyc3.cdn.digitaloceanspaces.com) for details regarding record types and their respective attributes. 
     * @param domainName The name of the domain itself. (required)
     * @param domainRecordId The unique identifier of the domain record. (required)
     * @return UpdateRecordById0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;domain_record&#x60;. The value of this will be a domain record object which contains the standard domain record attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateRecordById0RequestBuilder updateRecordById_0(String type, String domainName, Integer domainRecordId) throws IllegalArgumentException {
        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        if (domainName == null) throw new IllegalArgumentException("\"domainName\" is required but got null");
            

        if (domainRecordId == null) throw new IllegalArgumentException("\"domainRecordId\" is required but got null");
        return new UpdateRecordById0RequestBuilder(type, domainName, domainRecordId);
    }
}