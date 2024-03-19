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


import com.konfigthis.client.model.Distribution;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.ImageNewCustom;
import com.konfigthis.client.model.ImageUpdate;
import com.konfigthis.client.model.ImagesGetResponse;
import com.konfigthis.client.model.ImagesImportCustomImageFromUrlResponse;
import com.konfigthis.client.model.ImagesListResponse;
import com.konfigthis.client.model.ImagesUpdateResponse;
import com.konfigthis.client.model.RegionSlug;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class ImagesApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public ImagesApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public ImagesApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call deleteCall(Integer imageId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/images/{image_id}"
            .replace("{" + "image_id" + "}", localVarApiClient.escapeString(imageId.toString()));

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
    private okhttp3.Call deleteValidateBeforeCall(Integer imageId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'imageId' is set
        if (imageId == null) {
            throw new ApiException("Missing the required parameter 'imageId' when calling delete(Async)");
        }

        return deleteCall(imageId, _callback);

    }


    private ApiResponse<Void> deleteWithHttpInfo(Integer imageId) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(imageId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteAsync(Integer imageId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(imageId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final Integer imageId;

        private DeleteRequestBuilder(Integer imageId) {
            this.imageId = imageId;
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
            return deleteCall(imageId, _callback);
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
            deleteWithHttpInfo(imageId);
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
            return deleteWithHttpInfo(imageId);
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
            return deleteAsync(imageId, _callback);
        }
    }

    /**
     * Delete an Image
     * To delete a snapshot or custom image, send a &#x60;DELETE&#x60; request to &#x60;/v2/images/$IMAGE_ID&#x60;. 
     * @param imageId A unique number that can be used to identify and reference a specific image. (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(Integer imageId) throws IllegalArgumentException {
        if (imageId == null) throw new IllegalArgumentException("\"imageId\" is required but got null");
        return new DeleteRequestBuilder(imageId);
    }
    private okhttp3.Call getCall(Object imageId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/images/{image_id}"
            .replace("{" + "image_id" + "}", localVarApiClient.escapeString(imageId.toString()));

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
    private okhttp3.Call getValidateBeforeCall(Object imageId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'imageId' is set
        if (imageId == null) {
            throw new ApiException("Missing the required parameter 'imageId' when calling get(Async)");
        }

        return getCall(imageId, _callback);

    }


    private ApiResponse<ImagesGetResponse> getWithHttpInfo(Object imageId) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(imageId, null);
        Type localVarReturnType = new TypeToken<ImagesGetResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(Object imageId, final ApiCallback<ImagesGetResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(imageId, _callback);
        Type localVarReturnType = new TypeToken<ImagesGetResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final Object imageId;

        private GetRequestBuilder(Object imageId) {
            this.imageId = imageId;
        }

        /**
         * Build call for get
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(imageId, _callback);
        }


        /**
         * Execute get request
         * @return ImagesGetResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ImagesGetResponse execute() throws ApiException {
            ApiResponse<ImagesGetResponse> localVarResp = getWithHttpInfo(imageId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;ImagesGetResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ImagesGetResponse> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(imageId);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ImagesGetResponse> _callback) throws ApiException {
            return getAsync(imageId, _callback);
        }
    }

    /**
     * Retrieve an Existing Image
     * To retrieve information about an image, send a &#x60;GET&#x60; request to &#x60;/v2/images/$IDENTIFIER&#x60;. 
     * @param imageId A unique number (id) or string (slug) used to identify and reference a specific image.  **Public** images can be identified by image &#x60;id&#x60; or &#x60;slug&#x60;.  **Private** images *must* be identified by image &#x60;id&#x60;.  (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetRequestBuilder get(Object imageId) throws IllegalArgumentException {
        if (imageId == null) throw new IllegalArgumentException("\"imageId\" is required but got null");
        return new GetRequestBuilder(imageId);
    }
    private okhttp3.Call importCustomImageFromUrlCall(ImageNewCustom imageNewCustom, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = imageNewCustom;

        // create path and map variables
        String localVarPath = "/v2/images";

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
    private okhttp3.Call importCustomImageFromUrlValidateBeforeCall(ImageNewCustom imageNewCustom, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'imageNewCustom' is set
        if (imageNewCustom == null) {
            throw new ApiException("Missing the required parameter 'imageNewCustom' when calling importCustomImageFromUrl(Async)");
        }

        return importCustomImageFromUrlCall(imageNewCustom, _callback);

    }


    private ApiResponse<ImagesImportCustomImageFromUrlResponse> importCustomImageFromUrlWithHttpInfo(ImageNewCustom imageNewCustom) throws ApiException {
        okhttp3.Call localVarCall = importCustomImageFromUrlValidateBeforeCall(imageNewCustom, null);
        Type localVarReturnType = new TypeToken<ImagesImportCustomImageFromUrlResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call importCustomImageFromUrlAsync(ImageNewCustom imageNewCustom, final ApiCallback<ImagesImportCustomImageFromUrlResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = importCustomImageFromUrlValidateBeforeCall(imageNewCustom, _callback);
        Type localVarReturnType = new TypeToken<ImagesImportCustomImageFromUrlResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ImportCustomImageFromUrlRequestBuilder {
        private final String name;
        private final String url;
        private final RegionSlug region;
        private String description;
        private Distribution distribution;
        private List<String> tags;

        private ImportCustomImageFromUrlRequestBuilder(String name, String url, RegionSlug region) {
            this.name = name;
            this.url = url;
            this.region = region;
        }

        /**
         * Set description
         * @param description An optional free-form text field to describe an image. (optional)
         * @return ImportCustomImageFromUrlRequestBuilder
         */
        public ImportCustomImageFromUrlRequestBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Set distribution
         * @param distribution  (optional)
         * @return ImportCustomImageFromUrlRequestBuilder
         */
        public ImportCustomImageFromUrlRequestBuilder distribution(Distribution distribution) {
            this.distribution = distribution;
            return this;
        }
        
        /**
         * Set tags
         * @param tags A flat array of tag names as strings to be applied to the resource. Tag names may be for either existing or new tags. (optional)
         * @return ImportCustomImageFromUrlRequestBuilder
         */
        public ImportCustomImageFromUrlRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Build call for importCustomImageFromUrl
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing a subset of the standard  image attributes as listed below, including the image&#39;s &#x60;id&#x60; and &#x60;status&#x60;.  After initial creation, the &#x60;status&#x60; will be &#x60;NEW&#x60;. Using the image&#39;s id, you  may query the image&#39;s status by sending a &#x60;GET&#x60; request to the  &#x60;/v2/images/$IMAGE_ID&#x60; endpoint.  When the &#x60;status&#x60; changes to &#x60;available&#x60;, the image will be ready for use. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            ImageNewCustom imageNewCustom = buildBodyParams();
            return importCustomImageFromUrlCall(imageNewCustom, _callback);
        }

        private ImageNewCustom buildBodyParams() {
            ImageNewCustom imageNewCustom = new ImageNewCustom();
            imageNewCustom.tags(this.tags);
            imageNewCustom.description(this.description);
            imageNewCustom.name(this.name);
            imageNewCustom.url(this.url);
            imageNewCustom.distribution(this.distribution);
            imageNewCustom.region(this.region);
            return imageNewCustom;
        }

        /**
         * Execute importCustomImageFromUrl request
         * @return ImagesImportCustomImageFromUrlResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing a subset of the standard  image attributes as listed below, including the image&#39;s &#x60;id&#x60; and &#x60;status&#x60;.  After initial creation, the &#x60;status&#x60; will be &#x60;NEW&#x60;. Using the image&#39;s id, you  may query the image&#39;s status by sending a &#x60;GET&#x60; request to the  &#x60;/v2/images/$IMAGE_ID&#x60; endpoint.  When the &#x60;status&#x60; changes to &#x60;available&#x60;, the image will be ready for use. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ImagesImportCustomImageFromUrlResponse execute() throws ApiException {
            ImageNewCustom imageNewCustom = buildBodyParams();
            ApiResponse<ImagesImportCustomImageFromUrlResponse> localVarResp = importCustomImageFromUrlWithHttpInfo(imageNewCustom);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute importCustomImageFromUrl request with HTTP info returned
         * @return ApiResponse&lt;ImagesImportCustomImageFromUrlResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing a subset of the standard  image attributes as listed below, including the image&#39;s &#x60;id&#x60; and &#x60;status&#x60;.  After initial creation, the &#x60;status&#x60; will be &#x60;NEW&#x60;. Using the image&#39;s id, you  may query the image&#39;s status by sending a &#x60;GET&#x60; request to the  &#x60;/v2/images/$IMAGE_ID&#x60; endpoint.  When the &#x60;status&#x60; changes to &#x60;available&#x60;, the image will be ready for use. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ImagesImportCustomImageFromUrlResponse> executeWithHttpInfo() throws ApiException {
            ImageNewCustom imageNewCustom = buildBodyParams();
            return importCustomImageFromUrlWithHttpInfo(imageNewCustom);
        }

        /**
         * Execute importCustomImageFromUrl request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing a subset of the standard  image attributes as listed below, including the image&#39;s &#x60;id&#x60; and &#x60;status&#x60;.  After initial creation, the &#x60;status&#x60; will be &#x60;NEW&#x60;. Using the image&#39;s id, you  may query the image&#39;s status by sending a &#x60;GET&#x60; request to the  &#x60;/v2/images/$IMAGE_ID&#x60; endpoint.  When the &#x60;status&#x60; changes to &#x60;available&#x60;, the image will be ready for use. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ImagesImportCustomImageFromUrlResponse> _callback) throws ApiException {
            ImageNewCustom imageNewCustom = buildBodyParams();
            return importCustomImageFromUrlAsync(imageNewCustom, _callback);
        }
    }

    /**
     * Create a Custom Image
     * To create a new custom image, send a POST request to /v2/images. The body must contain a url attribute pointing to a Linux virtual machine image to be imported into DigitalOcean. The image must be in the raw, qcow2, vhdx, vdi, or vmdk format. It may be compressed using gzip or bzip2 and must be smaller than 100 GB after  being decompressed. 
     * @param imageNewCustom  (required)
     * @return ImportCustomImageFromUrlRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing a subset of the standard  image attributes as listed below, including the image&#39;s &#x60;id&#x60; and &#x60;status&#x60;.  After initial creation, the &#x60;status&#x60; will be &#x60;NEW&#x60;. Using the image&#39;s id, you  may query the image&#39;s status by sending a &#x60;GET&#x60; request to the  &#x60;/v2/images/$IMAGE_ID&#x60; endpoint.  When the &#x60;status&#x60; changes to &#x60;available&#x60;, the image will be ready for use. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ImportCustomImageFromUrlRequestBuilder importCustomImageFromUrl(String name, String url, RegionSlug region) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (url == null) throw new IllegalArgumentException("\"url\" is required but got null");
            

        if (region == null) throw new IllegalArgumentException("\"region\" is required but got null");
        return new ImportCustomImageFromUrlRequestBuilder(name, url, region);
    }
    private okhttp3.Call listCall(String type, Boolean _private, String tagName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/images";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (type != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("type", type));
        }

        if (_private != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("private", _private));
        }

        if (tagName != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("tag_name", tagName));
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
    private okhttp3.Call listValidateBeforeCall(String type, Boolean _private, String tagName, Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        return listCall(type, _private, tagName, perPage, page, _callback);

    }


    private ApiResponse<ImagesListResponse> listWithHttpInfo(String type, Boolean _private, String tagName, Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(type, _private, tagName, perPage, page, null);
        Type localVarReturnType = new TypeToken<ImagesListResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(String type, Boolean _private, String tagName, Integer perPage, Integer page, final ApiCallback<ImagesListResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(type, _private, tagName, perPage, page, _callback);
        Type localVarReturnType = new TypeToken<ImagesListResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private String type;
        private Boolean _private;
        private String tagName;
        private Integer perPage;
        private Integer page;

        private ListRequestBuilder() {
        }

        /**
         * Set type
         * @param type Filters results based on image type which can be either &#x60;application&#x60; or &#x60;distribution&#x60;. (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder type(String type) {
            this.type = type;
            return this;
        }
        
        /**
         * Set _private
         * @param _private Used to filter only user images. (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder _private(Boolean _private) {
            this._private = _private;
            return this;
        }
        
        /**
         * Set tagName
         * @param tagName Used to filter images by a specific tag. (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder tagName(String tagName) {
            this.tagName = tagName;
            return this;
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
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;images&#x60;.  This will be set to an array of image objects, each of which will contain the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(type, _private, tagName, perPage, page, _callback);
        }


        /**
         * Execute list request
         * @return ImagesListResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;images&#x60;.  This will be set to an array of image objects, each of which will contain the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ImagesListResponse execute() throws ApiException {
            ApiResponse<ImagesListResponse> localVarResp = listWithHttpInfo(type, _private, tagName, perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;ImagesListResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;images&#x60;.  This will be set to an array of image objects, each of which will contain the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ImagesListResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(type, _private, tagName, perPage, page);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;images&#x60;.  This will be set to an array of image objects, each of which will contain the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ImagesListResponse> _callback) throws ApiException {
            return listAsync(type, _private, tagName, perPage, page, _callback);
        }
    }

    /**
     * List All Images
     * To list all of the images available on your account, send a GET request to /v2/images.  ## Filtering Results -----  It&#39;s possible to request filtered results by including certain query parameters.  **Image Type**  Either 1-Click Application or OS Distribution images can be filtered by using the &#x60;type&#x60; query parameter.  &gt; Important: The &#x60;type&#x60; query parameter does not directly relate to the &#x60;type&#x60; attribute.  To retrieve only ***distribution*** images, include the &#x60;type&#x60; query parameter set to distribution, &#x60;/v2/images?type&#x3D;distribution&#x60;.  To retrieve only ***application*** images, include the &#x60;type&#x60; query parameter set to application, &#x60;/v2/images?type&#x3D;application&#x60;.  **User Images**  To retrieve only the private images of a user, include the &#x60;private&#x60; query parameter set to true, &#x60;/v2/images?private&#x3D;true&#x60;.  **Tags**  To list all images assigned to a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag in your GET request. For example, &#x60;/v2/images?tag_name&#x3D;$TAG_NAME&#x60;. 
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;images&#x60;.  This will be set to an array of image objects, each of which will contain the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRequestBuilder list() throws IllegalArgumentException {
        return new ListRequestBuilder();
    }
    private okhttp3.Call updateCall(Integer imageId, ImageUpdate imageUpdate, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = imageUpdate;

        // create path and map variables
        String localVarPath = "/v2/images/{image_id}"
            .replace("{" + "image_id" + "}", localVarApiClient.escapeString(imageId.toString()));

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
    private okhttp3.Call updateValidateBeforeCall(Integer imageId, ImageUpdate imageUpdate, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'imageId' is set
        if (imageId == null) {
            throw new ApiException("Missing the required parameter 'imageId' when calling update(Async)");
        }

        // verify the required parameter 'imageUpdate' is set
        if (imageUpdate == null) {
            throw new ApiException("Missing the required parameter 'imageUpdate' when calling update(Async)");
        }

        return updateCall(imageId, imageUpdate, _callback);

    }


    private ApiResponse<ImagesUpdateResponse> updateWithHttpInfo(Integer imageId, ImageUpdate imageUpdate) throws ApiException {
        okhttp3.Call localVarCall = updateValidateBeforeCall(imageId, imageUpdate, null);
        Type localVarReturnType = new TypeToken<ImagesUpdateResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAsync(Integer imageId, ImageUpdate imageUpdate, final ApiCallback<ImagesUpdateResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateValidateBeforeCall(imageId, imageUpdate, _callback);
        Type localVarReturnType = new TypeToken<ImagesUpdateResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateRequestBuilder {
        private final Integer imageId;
        private String description;
        private String name;
        private Distribution distribution;

        private UpdateRequestBuilder(Integer imageId) {
            this.imageId = imageId;
        }

        /**
         * Set description
         * @param description An optional free-form text field to describe an image. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Set name
         * @param name The display name that has been given to an image.  This is what is shown in the control panel and is generally a descriptive title for the image in question. (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set distribution
         * @param distribution  (optional)
         * @return UpdateRequestBuilder
         */
        public UpdateRequestBuilder distribution(Distribution distribution) {
            this.distribution = distribution;
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
            <tr><td> 200 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            ImageUpdate imageUpdate = buildBodyParams();
            return updateCall(imageId, imageUpdate, _callback);
        }

        private ImageUpdate buildBodyParams() {
            ImageUpdate imageUpdate = new ImageUpdate();
            imageUpdate.description(this.description);
            imageUpdate.name(this.name);
            imageUpdate.distribution(this.distribution);
            return imageUpdate;
        }

        /**
         * Execute update request
         * @return ImagesUpdateResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ImagesUpdateResponse execute() throws ApiException {
            ImageUpdate imageUpdate = buildBodyParams();
            ApiResponse<ImagesUpdateResponse> localVarResp = updateWithHttpInfo(imageId, imageUpdate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute update request with HTTP info returned
         * @return ApiResponse&lt;ImagesUpdateResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ImagesUpdateResponse> executeWithHttpInfo() throws ApiException {
            ImageUpdate imageUpdate = buildBodyParams();
            return updateWithHttpInfo(imageId, imageUpdate);
        }

        /**
         * Execute update request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ImagesUpdateResponse> _callback) throws ApiException {
            ImageUpdate imageUpdate = buildBodyParams();
            return updateAsync(imageId, imageUpdate, _callback);
        }
    }

    /**
     * Update an Image
     * To update an image, send a &#x60;PUT&#x60; request to &#x60;/v2/images/$IMAGE_ID&#x60;. Set the &#x60;name&#x60; attribute to the new value you would like to use. For custom images, the &#x60;description&#x60; and &#x60;distribution&#x60; attributes may also be updated. 
     * @param imageId A unique number that can be used to identify and reference a specific image. (required)
     * @param imageUpdate  (required)
     * @return UpdateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key set to &#x60;image&#x60;.  The value of this will be an image object containing the standard image attributes. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateRequestBuilder update(Integer imageId) throws IllegalArgumentException {
        if (imageId == null) throw new IllegalArgumentException("\"imageId\" is required but got null");
        return new UpdateRequestBuilder(imageId);
    }
}
