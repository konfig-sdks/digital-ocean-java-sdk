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


import com.konfigthis.client.model.AppAlertSlackWebhook;
import com.konfigthis.client.model.AppMetricsBandwidthUsage;
import com.konfigthis.client.model.AppMetricsBandwidthUsageRequest;
import com.konfigthis.client.model.AppPropose;
import com.konfigthis.client.model.AppProposeResponse;
import com.konfigthis.client.model.AppResponse;
import com.konfigthis.client.model.AppSpec;
import com.konfigthis.client.model.AppsAlertResponse;
import com.konfigthis.client.model.AppsAssignAppAlertDestinationsRequest;
import com.konfigthis.client.model.AppsCreateAppRequest;
import com.konfigthis.client.model.AppsCreateDeploymentRequest;
import com.konfigthis.client.model.AppsDeleteAppResponse;
import com.konfigthis.client.model.AppsDeploymentResponse;
import com.konfigthis.client.model.AppsDeploymentsResponse;
import com.konfigthis.client.model.AppsGetInstanceSizeResponse;
import com.konfigthis.client.model.AppsGetLogsResponse;
import com.konfigthis.client.model.AppsGetTierResponse;
import com.konfigthis.client.model.AppsListAlertsResponse;
import com.konfigthis.client.model.AppsListInstanceSizesResponse;
import com.konfigthis.client.model.AppsListRegionsResponse;
import com.konfigthis.client.model.AppsListTiersResponse;
import com.konfigthis.client.model.AppsResponse;
import com.konfigthis.client.model.AppsRollbackAppRequest;
import com.konfigthis.client.model.AppsUpdateAppRequest;
import com.konfigthis.client.model.AppsValidateRollbackResponse;
import com.konfigthis.client.model.Error;
import java.time.OffsetDateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class AppsApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public AppsApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public AppsApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call cancelDeploymentCall(String appId, String deploymentId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/deployments/{deployment_id}/cancel"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()))
            .replace("{" + "deployment_id" + "}", localVarApiClient.escapeString(deploymentId.toString()));

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
    private okhttp3.Call cancelDeploymentValidateBeforeCall(String appId, String deploymentId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling cancelDeployment(Async)");
        }

        // verify the required parameter 'deploymentId' is set
        if (deploymentId == null) {
            throw new ApiException("Missing the required parameter 'deploymentId' when calling cancelDeployment(Async)");
        }

        return cancelDeploymentCall(appId, deploymentId, _callback);

    }


    private ApiResponse<AppsDeploymentResponse> cancelDeploymentWithHttpInfo(String appId, String deploymentId) throws ApiException {
        okhttp3.Call localVarCall = cancelDeploymentValidateBeforeCall(appId, deploymentId, null);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call cancelDeploymentAsync(String appId, String deploymentId, final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = cancelDeploymentValidateBeforeCall(appId, deploymentId, _callback);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CancelDeploymentRequestBuilder {
        private final String appId;
        private final String deploymentId;

        private CancelDeploymentRequestBuilder(String appId, String deploymentId) {
            this.appId = appId;
            this.deploymentId = deploymentId;
        }

        /**
         * Build call for cancelDeployment
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON the &#x60;deployment&#x60; that was just cancelled. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return cancelDeploymentCall(appId, deploymentId, _callback);
        }


        /**
         * Execute cancelDeployment request
         * @return AppsDeploymentResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON the &#x60;deployment&#x60; that was just cancelled. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsDeploymentResponse execute() throws ApiException {
            ApiResponse<AppsDeploymentResponse> localVarResp = cancelDeploymentWithHttpInfo(appId, deploymentId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute cancelDeployment request with HTTP info returned
         * @return ApiResponse&lt;AppsDeploymentResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON the &#x60;deployment&#x60; that was just cancelled. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsDeploymentResponse> executeWithHttpInfo() throws ApiException {
            return cancelDeploymentWithHttpInfo(appId, deploymentId);
        }

        /**
         * Execute cancelDeployment request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON the &#x60;deployment&#x60; that was just cancelled. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {
            return cancelDeploymentAsync(appId, deploymentId, _callback);
        }
    }

    /**
     * Cancel a Deployment
     * Immediately cancel an in-progress deployment.
     * @param appId The app ID (required)
     * @param deploymentId The deployment ID (required)
     * @return CancelDeploymentRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON the &#x60;deployment&#x60; that was just cancelled. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CancelDeploymentRequestBuilder cancelDeployment(String appId, String deploymentId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (deploymentId == null) throw new IllegalArgumentException("\"deploymentId\" is required but got null");
            

        return new CancelDeploymentRequestBuilder(appId, deploymentId);
    }
    private okhttp3.Call commitRollbackCall(String appId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/rollback/commit"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

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
    private okhttp3.Call commitRollbackValidateBeforeCall(String appId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling commitRollback(Async)");
        }

        return commitRollbackCall(appId, _callback);

    }


    private ApiResponse<Void> commitRollbackWithHttpInfo(String appId) throws ApiException {
        okhttp3.Call localVarCall = commitRollbackValidateBeforeCall(appId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call commitRollbackAsync(String appId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = commitRollbackValidateBeforeCall(appId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class CommitRollbackRequestBuilder {
        private final String appId;

        private CommitRollbackRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Build call for commitRollback
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return commitRollbackCall(appId, _callback);
        }


        /**
         * Execute commitRollback request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            commitRollbackWithHttpInfo(appId);
        }

        /**
         * Execute commitRollback request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return commitRollbackWithHttpInfo(appId);
        }

        /**
         * Execute commitRollback request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return commitRollbackAsync(appId, _callback);
        }
    }

    /**
     * Commit App Rollback
     * Commit an app rollback. This action permanently applies the rollback and unpins the app to resume new deployments. 
     * @param appId The app ID (required)
     * @return CommitRollbackRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CommitRollbackRequestBuilder commitRollback(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new CommitRollbackRequestBuilder(appId);
    }
    private okhttp3.Call createCall(AppsCreateAppRequest appsCreateAppRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appsCreateAppRequest;

        // create path and map variables
        String localVarPath = "/v2/apps";

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
    private okhttp3.Call createValidateBeforeCall(AppsCreateAppRequest appsCreateAppRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appsCreateAppRequest' is set
        if (appsCreateAppRequest == null) {
            throw new ApiException("Missing the required parameter 'appsCreateAppRequest' when calling create(Async)");
        }

        return createCall(appsCreateAppRequest, _callback);

    }


    private ApiResponse<AppResponse> createWithHttpInfo(AppsCreateAppRequest appsCreateAppRequest) throws ApiException {
        okhttp3.Call localVarCall = createValidateBeforeCall(appsCreateAppRequest, null);
        Type localVarReturnType = new TypeToken<AppResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createAsync(AppsCreateAppRequest appsCreateAppRequest, final ApiCallback<AppResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createValidateBeforeCall(appsCreateAppRequest, _callback);
        Type localVarReturnType = new TypeToken<AppResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateRequestBuilder {
        private final AppSpec spec;
        private String projectId;

        private CreateRequestBuilder(AppSpec spec) {
            this.spec = spec;
        }

        /**
         * Set projectId
         * @param projectId The ID of the project the app should be assigned to. If omitted, it will be assigned to your default project. (optional)
         * @return CreateRequestBuilder
         */
        public CreateRequestBuilder projectId(String projectId) {
            this.projectId = projectId;
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
            <tr><td> 200 </td><td> A JSON or YAML of a &#x60;spec&#x60; object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppsCreateAppRequest appsCreateAppRequest = buildBodyParams();
            return createCall(appsCreateAppRequest, _callback);
        }

        private AppsCreateAppRequest buildBodyParams() {
            AppsCreateAppRequest appsCreateAppRequest = new AppsCreateAppRequest();
            appsCreateAppRequest.spec(this.spec);
            appsCreateAppRequest.projectId(this.projectId);
            return appsCreateAppRequest;
        }

        /**
         * Execute create request
         * @return AppResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON or YAML of a &#x60;spec&#x60; object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppResponse execute() throws ApiException {
            AppsCreateAppRequest appsCreateAppRequest = buildBodyParams();
            ApiResponse<AppResponse> localVarResp = createWithHttpInfo(appsCreateAppRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute create request with HTTP info returned
         * @return ApiResponse&lt;AppResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON or YAML of a &#x60;spec&#x60; object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppResponse> executeWithHttpInfo() throws ApiException {
            AppsCreateAppRequest appsCreateAppRequest = buildBodyParams();
            return createWithHttpInfo(appsCreateAppRequest);
        }

        /**
         * Execute create request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON or YAML of a &#x60;spec&#x60; object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppResponse> _callback) throws ApiException {
            AppsCreateAppRequest appsCreateAppRequest = buildBodyParams();
            return createAsync(appsCreateAppRequest, _callback);
        }
    }

    /**
     * Create a New App
     * Create a new app by submitting an app specification. For documentation on app specifications (&#x60;AppSpec&#x60; objects), please refer to [the product documentation](https://docs.digitalocean.com/products/app-platform/reference/app-spec/).
     * @param appsCreateAppRequest  (required)
     * @return CreateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON or YAML of a &#x60;spec&#x60; object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateRequestBuilder create(AppSpec spec) throws IllegalArgumentException {
        if (spec == null) throw new IllegalArgumentException("\"spec\" is required but got null");
        return new CreateRequestBuilder(spec);
    }
    private okhttp3.Call createDeploymentCall(String appId, AppsCreateDeploymentRequest appsCreateDeploymentRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appsCreateDeploymentRequest;

        // create path and map variables
        String localVarPath = "/v2/apps/{app_id}/deployments"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

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
    private okhttp3.Call createDeploymentValidateBeforeCall(String appId, AppsCreateDeploymentRequest appsCreateDeploymentRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling createDeployment(Async)");
        }

        // verify the required parameter 'appsCreateDeploymentRequest' is set
        if (appsCreateDeploymentRequest == null) {
            throw new ApiException("Missing the required parameter 'appsCreateDeploymentRequest' when calling createDeployment(Async)");
        }

        return createDeploymentCall(appId, appsCreateDeploymentRequest, _callback);

    }


    private ApiResponse<AppsDeploymentResponse> createDeploymentWithHttpInfo(String appId, AppsCreateDeploymentRequest appsCreateDeploymentRequest) throws ApiException {
        okhttp3.Call localVarCall = createDeploymentValidateBeforeCall(appId, appsCreateDeploymentRequest, null);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createDeploymentAsync(String appId, AppsCreateDeploymentRequest appsCreateDeploymentRequest, final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createDeploymentValidateBeforeCall(appId, appsCreateDeploymentRequest, _callback);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateDeploymentRequestBuilder {
        private final String appId;
        private Boolean forceBuild;

        private CreateDeploymentRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Set forceBuild
         * @param forceBuild  (optional)
         * @return CreateDeploymentRequestBuilder
         */
        public CreateDeploymentRequestBuilder forceBuild(Boolean forceBuild) {
            this.forceBuild = forceBuild;
            return this;
        }
        
        /**
         * Build call for createDeployment
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppsCreateDeploymentRequest appsCreateDeploymentRequest = buildBodyParams();
            return createDeploymentCall(appId, appsCreateDeploymentRequest, _callback);
        }

        private AppsCreateDeploymentRequest buildBodyParams() {
            AppsCreateDeploymentRequest appsCreateDeploymentRequest = new AppsCreateDeploymentRequest();
            appsCreateDeploymentRequest.forceBuild(this.forceBuild);
            return appsCreateDeploymentRequest;
        }

        /**
         * Execute createDeployment request
         * @return AppsDeploymentResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsDeploymentResponse execute() throws ApiException {
            AppsCreateDeploymentRequest appsCreateDeploymentRequest = buildBodyParams();
            ApiResponse<AppsDeploymentResponse> localVarResp = createDeploymentWithHttpInfo(appId, appsCreateDeploymentRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createDeployment request with HTTP info returned
         * @return ApiResponse&lt;AppsDeploymentResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsDeploymentResponse> executeWithHttpInfo() throws ApiException {
            AppsCreateDeploymentRequest appsCreateDeploymentRequest = buildBodyParams();
            return createDeploymentWithHttpInfo(appId, appsCreateDeploymentRequest);
        }

        /**
         * Execute createDeployment request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {
            AppsCreateDeploymentRequest appsCreateDeploymentRequest = buildBodyParams();
            return createDeploymentAsync(appId, appsCreateDeploymentRequest, _callback);
        }
    }

    /**
     * Create an App Deployment
     * Creating an app deployment will pull the latest changes from your repository and schedule a new deployment for your app.
     * @param appId The app ID (required)
     * @param appsCreateDeploymentRequest  (required)
     * @return CreateDeploymentRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateDeploymentRequestBuilder createDeployment(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new CreateDeploymentRequestBuilder(appId);
    }
    private okhttp3.Call deleteCall(String id, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{id}"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
    private okhttp3.Call deleteValidateBeforeCall(String id, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling delete(Async)");
        }

        return deleteCall(id, _callback);

    }


    private ApiResponse<AppsDeleteAppResponse> deleteWithHttpInfo(String id) throws ApiException {
        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, null);
        Type localVarReturnType = new TypeToken<AppsDeleteAppResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call deleteAsync(String id, final ApiCallback<AppsDeleteAppResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteValidateBeforeCall(id, _callback);
        Type localVarReturnType = new TypeToken<AppsDeleteAppResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class DeleteRequestBuilder {
        private final String id;

        private DeleteRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Build call for delete
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> the ID of the app deleted. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return deleteCall(id, _callback);
        }


        /**
         * Execute delete request
         * @return AppsDeleteAppResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> the ID of the app deleted. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsDeleteAppResponse execute() throws ApiException {
            ApiResponse<AppsDeleteAppResponse> localVarResp = deleteWithHttpInfo(id);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute delete request with HTTP info returned
         * @return ApiResponse&lt;AppsDeleteAppResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> the ID of the app deleted. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsDeleteAppResponse> executeWithHttpInfo() throws ApiException {
            return deleteWithHttpInfo(id);
        }

        /**
         * Execute delete request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> the ID of the app deleted. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsDeleteAppResponse> _callback) throws ApiException {
            return deleteAsync(id, _callback);
        }
    }

    /**
     * Delete an App
     * Delete an existing app. Once deleted, all active deployments will be permanently shut down and the app deleted. If needed, be sure to back up your app specification so that you may re-create it at a later time.
     * @param id The ID of the app (required)
     * @return DeleteRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> the ID of the app deleted. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteRequestBuilder delete(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new DeleteRequestBuilder(id);
    }
    private okhttp3.Call getCall(String id, String name, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{id}"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

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
    private okhttp3.Call getValidateBeforeCall(String id, String name, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling get(Async)");
        }

        return getCall(id, name, _callback);

    }


    private ApiResponse<AppResponse> getWithHttpInfo(String id, String name) throws ApiException {
        okhttp3.Call localVarCall = getValidateBeforeCall(id, name, null);
        Type localVarReturnType = new TypeToken<AppResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAsync(String id, String name, final ApiCallback<AppResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getValidateBeforeCall(id, name, _callback);
        Type localVarReturnType = new TypeToken<AppResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetRequestBuilder {
        private final String id;
        private String name;

        private GetRequestBuilder(String id) {
            this.id = id;
        }

        /**
         * Set name
         * @param name The name of the app to retrieve. (optional)
         * @return GetRequestBuilder
         */
        public GetRequestBuilder name(String name) {
            this.name = name;
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
            <tr><td> 200 </td><td> A JSON with key &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCall(id, name, _callback);
        }


        /**
         * Execute get request
         * @return AppResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppResponse execute() throws ApiException {
            ApiResponse<AppResponse> localVarResp = getWithHttpInfo(id, name);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute get request with HTTP info returned
         * @return ApiResponse&lt;AppResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppResponse> executeWithHttpInfo() throws ApiException {
            return getWithHttpInfo(id, name);
        }

        /**
         * Execute get request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppResponse> _callback) throws ApiException {
            return getAsync(id, name, _callback);
        }
    }

    /**
     * Retrieve an Existing App
     * Retrieve details about an existing app by either its ID or name. To retrieve an app by its name, do not include an ID in the request path. Information about the current active deployment as well as any in progress ones will also be included in the response.
     * @param id The ID of the app (required)
     * @return GetRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON with key &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetRequestBuilder get(String id) throws IllegalArgumentException {
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new GetRequestBuilder(id);
    }
    private okhttp3.Call getActiveDeploymentLogsCall(String appId, String componentName, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/components/{component_name}/logs"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()))
            .replace("{" + "component_name" + "}", localVarApiClient.escapeString(componentName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (follow != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("follow", follow));
        }

        if (type != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("type", type));
        }

        if (podConnectionTimeout != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("pod_connection_timeout", podConnectionTimeout));
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
    private okhttp3.Call getActiveDeploymentLogsValidateBeforeCall(String appId, String componentName, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getActiveDeploymentLogs(Async)");
        }

        // verify the required parameter 'componentName' is set
        if (componentName == null) {
            throw new ApiException("Missing the required parameter 'componentName' when calling getActiveDeploymentLogs(Async)");
        }

        // verify the required parameter 'type' is set
        if (type == null) {
            throw new ApiException("Missing the required parameter 'type' when calling getActiveDeploymentLogs(Async)");
        }

        return getActiveDeploymentLogsCall(appId, componentName, type, follow, podConnectionTimeout, _callback);

    }


    private ApiResponse<AppsGetLogsResponse> getActiveDeploymentLogsWithHttpInfo(String appId, String componentName, String type, Boolean follow, String podConnectionTimeout) throws ApiException {
        okhttp3.Call localVarCall = getActiveDeploymentLogsValidateBeforeCall(appId, componentName, type, follow, podConnectionTimeout, null);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getActiveDeploymentLogsAsync(String appId, String componentName, String type, Boolean follow, String podConnectionTimeout, final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getActiveDeploymentLogsValidateBeforeCall(appId, componentName, type, follow, podConnectionTimeout, _callback);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetActiveDeploymentLogsRequestBuilder {
        private final String appId;
        private final String componentName;
        private final String type;
        private Boolean follow;
        private String podConnectionTimeout;

        private GetActiveDeploymentLogsRequestBuilder(String appId, String componentName, String type) {
            this.appId = appId;
            this.componentName = componentName;
            this.type = type;
        }

        /**
         * Set follow
         * @param follow Whether the logs should follow live updates. (optional)
         * @return GetActiveDeploymentLogsRequestBuilder
         */
        public GetActiveDeploymentLogsRequestBuilder follow(Boolean follow) {
            this.follow = follow;
            return this;
        }
        
        /**
         * Set podConnectionTimeout
         * @param podConnectionTimeout An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. (optional)
         * @return GetActiveDeploymentLogsRequestBuilder
         */
        public GetActiveDeploymentLogsRequestBuilder podConnectionTimeout(String podConnectionTimeout) {
            this.podConnectionTimeout = podConnectionTimeout;
            return this;
        }
        
        /**
         * Build call for getActiveDeploymentLogs
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getActiveDeploymentLogsCall(appId, componentName, type, follow, podConnectionTimeout, _callback);
        }


        /**
         * Execute getActiveDeploymentLogs request
         * @return AppsGetLogsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsGetLogsResponse execute() throws ApiException {
            ApiResponse<AppsGetLogsResponse> localVarResp = getActiveDeploymentLogsWithHttpInfo(appId, componentName, type, follow, podConnectionTimeout);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getActiveDeploymentLogs request with HTTP info returned
         * @return ApiResponse&lt;AppsGetLogsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsGetLogsResponse> executeWithHttpInfo() throws ApiException {
            return getActiveDeploymentLogsWithHttpInfo(appId, componentName, type, follow, podConnectionTimeout);
        }

        /**
         * Execute getActiveDeploymentLogs request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {
            return getActiveDeploymentLogsAsync(appId, componentName, type, follow, podConnectionTimeout, _callback);
        }
    }

    /**
     * Retrieve Active Deployment Logs
     * Retrieve the logs of the active deployment if one exists. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment. Note log_type&#x3D;BUILD logs will return logs associated with the current active deployment (being served). To view build logs associated with in-progress build, the query must explicitly reference the deployment id.
     * @param appId The app ID (required)
     * @param componentName An optional component name. If set, logs will be limited to this component only. (required)
     * @param type The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime (required)
     * @return GetActiveDeploymentLogsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetActiveDeploymentLogsRequestBuilder getActiveDeploymentLogs(String appId, String componentName, String type) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (componentName == null) throw new IllegalArgumentException("\"componentName\" is required but got null");
            

        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        return new GetActiveDeploymentLogsRequestBuilder(appId, componentName, type);
    }
    private okhttp3.Call getActiveDeploymentLogs_0Call(String appId, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/logs"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (follow != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("follow", follow));
        }

        if (type != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("type", type));
        }

        if (podConnectionTimeout != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("pod_connection_timeout", podConnectionTimeout));
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
    private okhttp3.Call getActiveDeploymentLogs_0ValidateBeforeCall(String appId, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getActiveDeploymentLogs_0(Async)");
        }

        // verify the required parameter 'type' is set
        if (type == null) {
            throw new ApiException("Missing the required parameter 'type' when calling getActiveDeploymentLogs_0(Async)");
        }

        return getActiveDeploymentLogs_0Call(appId, type, follow, podConnectionTimeout, _callback);

    }


    private ApiResponse<AppsGetLogsResponse> getActiveDeploymentLogs_0WithHttpInfo(String appId, String type, Boolean follow, String podConnectionTimeout) throws ApiException {
        okhttp3.Call localVarCall = getActiveDeploymentLogs_0ValidateBeforeCall(appId, type, follow, podConnectionTimeout, null);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getActiveDeploymentLogs_0Async(String appId, String type, Boolean follow, String podConnectionTimeout, final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getActiveDeploymentLogs_0ValidateBeforeCall(appId, type, follow, podConnectionTimeout, _callback);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetActiveDeploymentLogs0RequestBuilder {
        private final String appId;
        private final String type;
        private Boolean follow;
        private String podConnectionTimeout;

        private GetActiveDeploymentLogs0RequestBuilder(String appId, String type) {
            this.appId = appId;
            this.type = type;
        }

        /**
         * Set follow
         * @param follow Whether the logs should follow live updates. (optional)
         * @return GetActiveDeploymentLogs0RequestBuilder
         */
        public GetActiveDeploymentLogs0RequestBuilder follow(Boolean follow) {
            this.follow = follow;
            return this;
        }
        
        /**
         * Set podConnectionTimeout
         * @param podConnectionTimeout An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. (optional)
         * @return GetActiveDeploymentLogs0RequestBuilder
         */
        public GetActiveDeploymentLogs0RequestBuilder podConnectionTimeout(String podConnectionTimeout) {
            this.podConnectionTimeout = podConnectionTimeout;
            return this;
        }
        
        /**
         * Build call for getActiveDeploymentLogs_0
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getActiveDeploymentLogs_0Call(appId, type, follow, podConnectionTimeout, _callback);
        }


        /**
         * Execute getActiveDeploymentLogs_0 request
         * @return AppsGetLogsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsGetLogsResponse execute() throws ApiException {
            ApiResponse<AppsGetLogsResponse> localVarResp = getActiveDeploymentLogs_0WithHttpInfo(appId, type, follow, podConnectionTimeout);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getActiveDeploymentLogs_0 request with HTTP info returned
         * @return ApiResponse&lt;AppsGetLogsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsGetLogsResponse> executeWithHttpInfo() throws ApiException {
            return getActiveDeploymentLogs_0WithHttpInfo(appId, type, follow, podConnectionTimeout);
        }

        /**
         * Execute getActiveDeploymentLogs_0 request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {
            return getActiveDeploymentLogs_0Async(appId, type, follow, podConnectionTimeout, _callback);
        }
    }

    /**
     * Retrieve Active Deployment Aggregate Logs
     * Retrieve the logs of the active deployment if one exists. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment. Note log_type&#x3D;BUILD logs will return logs associated with the current active deployment (being served). To view build logs associated with in-progress build, the query must explicitly reference the deployment id.
     * @param appId The app ID (required)
     * @param type The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime (required)
     * @return GetActiveDeploymentLogs0RequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetActiveDeploymentLogs0RequestBuilder getActiveDeploymentLogs_0(String appId, String type) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        return new GetActiveDeploymentLogs0RequestBuilder(appId, type);
    }
    private okhttp3.Call getAggregateDeploymentLogsCall(String appId, String deploymentId, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/deployments/{deployment_id}/logs"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()))
            .replace("{" + "deployment_id" + "}", localVarApiClient.escapeString(deploymentId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (follow != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("follow", follow));
        }

        if (type != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("type", type));
        }

        if (podConnectionTimeout != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("pod_connection_timeout", podConnectionTimeout));
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
    private okhttp3.Call getAggregateDeploymentLogsValidateBeforeCall(String appId, String deploymentId, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getAggregateDeploymentLogs(Async)");
        }

        // verify the required parameter 'deploymentId' is set
        if (deploymentId == null) {
            throw new ApiException("Missing the required parameter 'deploymentId' when calling getAggregateDeploymentLogs(Async)");
        }

        // verify the required parameter 'type' is set
        if (type == null) {
            throw new ApiException("Missing the required parameter 'type' when calling getAggregateDeploymentLogs(Async)");
        }

        return getAggregateDeploymentLogsCall(appId, deploymentId, type, follow, podConnectionTimeout, _callback);

    }


    private ApiResponse<AppsGetLogsResponse> getAggregateDeploymentLogsWithHttpInfo(String appId, String deploymentId, String type, Boolean follow, String podConnectionTimeout) throws ApiException {
        okhttp3.Call localVarCall = getAggregateDeploymentLogsValidateBeforeCall(appId, deploymentId, type, follow, podConnectionTimeout, null);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAggregateDeploymentLogsAsync(String appId, String deploymentId, String type, Boolean follow, String podConnectionTimeout, final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getAggregateDeploymentLogsValidateBeforeCall(appId, deploymentId, type, follow, podConnectionTimeout, _callback);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetAggregateDeploymentLogsRequestBuilder {
        private final String appId;
        private final String deploymentId;
        private final String type;
        private Boolean follow;
        private String podConnectionTimeout;

        private GetAggregateDeploymentLogsRequestBuilder(String appId, String deploymentId, String type) {
            this.appId = appId;
            this.deploymentId = deploymentId;
            this.type = type;
        }

        /**
         * Set follow
         * @param follow Whether the logs should follow live updates. (optional)
         * @return GetAggregateDeploymentLogsRequestBuilder
         */
        public GetAggregateDeploymentLogsRequestBuilder follow(Boolean follow) {
            this.follow = follow;
            return this;
        }
        
        /**
         * Set podConnectionTimeout
         * @param podConnectionTimeout An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. (optional)
         * @return GetAggregateDeploymentLogsRequestBuilder
         */
        public GetAggregateDeploymentLogsRequestBuilder podConnectionTimeout(String podConnectionTimeout) {
            this.podConnectionTimeout = podConnectionTimeout;
            return this;
        }
        
        /**
         * Build call for getAggregateDeploymentLogs
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getAggregateDeploymentLogsCall(appId, deploymentId, type, follow, podConnectionTimeout, _callback);
        }


        /**
         * Execute getAggregateDeploymentLogs request
         * @return AppsGetLogsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsGetLogsResponse execute() throws ApiException {
            ApiResponse<AppsGetLogsResponse> localVarResp = getAggregateDeploymentLogsWithHttpInfo(appId, deploymentId, type, follow, podConnectionTimeout);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getAggregateDeploymentLogs request with HTTP info returned
         * @return ApiResponse&lt;AppsGetLogsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsGetLogsResponse> executeWithHttpInfo() throws ApiException {
            return getAggregateDeploymentLogsWithHttpInfo(appId, deploymentId, type, follow, podConnectionTimeout);
        }

        /**
         * Execute getAggregateDeploymentLogs request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {
            return getAggregateDeploymentLogsAsync(appId, deploymentId, type, follow, podConnectionTimeout, _callback);
        }
    }

    /**
     * Retrieve Aggregate Deployment Logs
     * Retrieve the logs of a past, in-progress, or active deployment. If a component name is specified, the logs will be limited to only that component. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment.
     * @param appId The app ID (required)
     * @param deploymentId The deployment ID (required)
     * @param type The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime (required)
     * @return GetAggregateDeploymentLogsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetAggregateDeploymentLogsRequestBuilder getAggregateDeploymentLogs(String appId, String deploymentId, String type) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (deploymentId == null) throw new IllegalArgumentException("\"deploymentId\" is required but got null");
            

        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        return new GetAggregateDeploymentLogsRequestBuilder(appId, deploymentId, type);
    }
    private okhttp3.Call getAppDailyBandwidthMetricsCall(String appId, OffsetDateTime date, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/metrics/bandwidth_daily"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (date != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("date", date));
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
    private okhttp3.Call getAppDailyBandwidthMetricsValidateBeforeCall(String appId, OffsetDateTime date, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getAppDailyBandwidthMetrics(Async)");
        }

        return getAppDailyBandwidthMetricsCall(appId, date, _callback);

    }


    private ApiResponse<AppMetricsBandwidthUsage> getAppDailyBandwidthMetricsWithHttpInfo(String appId, OffsetDateTime date) throws ApiException {
        okhttp3.Call localVarCall = getAppDailyBandwidthMetricsValidateBeforeCall(appId, date, null);
        Type localVarReturnType = new TypeToken<AppMetricsBandwidthUsage>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAppDailyBandwidthMetricsAsync(String appId, OffsetDateTime date, final ApiCallback<AppMetricsBandwidthUsage> _callback) throws ApiException {

        okhttp3.Call localVarCall = getAppDailyBandwidthMetricsValidateBeforeCall(appId, date, _callback);
        Type localVarReturnType = new TypeToken<AppMetricsBandwidthUsage>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetAppDailyBandwidthMetricsRequestBuilder {
        private final String appId;
        private OffsetDateTime date;

        private GetAppDailyBandwidthMetricsRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Set date
         * @param date Optional day to query. Only the date component of the timestamp will be considered. Default: yesterday. (optional)
         * @return GetAppDailyBandwidthMetricsRequestBuilder
         */
        public GetAppDailyBandwidthMetricsRequestBuilder date(OffsetDateTime date) {
            this.date = date;
            return this;
        }
        
        /**
         * Build call for getAppDailyBandwidthMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getAppDailyBandwidthMetricsCall(appId, date, _callback);
        }


        /**
         * Execute getAppDailyBandwidthMetrics request
         * @return AppMetricsBandwidthUsage
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppMetricsBandwidthUsage execute() throws ApiException {
            ApiResponse<AppMetricsBandwidthUsage> localVarResp = getAppDailyBandwidthMetricsWithHttpInfo(appId, date);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getAppDailyBandwidthMetrics request with HTTP info returned
         * @return ApiResponse&lt;AppMetricsBandwidthUsage&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppMetricsBandwidthUsage> executeWithHttpInfo() throws ApiException {
            return getAppDailyBandwidthMetricsWithHttpInfo(appId, date);
        }

        /**
         * Execute getAppDailyBandwidthMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppMetricsBandwidthUsage> _callback) throws ApiException {
            return getAppDailyBandwidthMetricsAsync(appId, date, _callback);
        }
    }

    /**
     * Retrieve App Daily Bandwidth Metrics
     * Retrieve daily bandwidth usage metrics for a single app.
     * @param appId The app ID (required)
     * @return GetAppDailyBandwidthMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetAppDailyBandwidthMetricsRequestBuilder getAppDailyBandwidthMetrics(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new GetAppDailyBandwidthMetricsRequestBuilder(appId);
    }
    private okhttp3.Call getDeploymentInfoCall(String appId, String deploymentId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/deployments/{deployment_id}"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()))
            .replace("{" + "deployment_id" + "}", localVarApiClient.escapeString(deploymentId.toString()));

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
    private okhttp3.Call getDeploymentInfoValidateBeforeCall(String appId, String deploymentId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getDeploymentInfo(Async)");
        }

        // verify the required parameter 'deploymentId' is set
        if (deploymentId == null) {
            throw new ApiException("Missing the required parameter 'deploymentId' when calling getDeploymentInfo(Async)");
        }

        return getDeploymentInfoCall(appId, deploymentId, _callback);

    }


    private ApiResponse<AppsDeploymentResponse> getDeploymentInfoWithHttpInfo(String appId, String deploymentId) throws ApiException {
        okhttp3.Call localVarCall = getDeploymentInfoValidateBeforeCall(appId, deploymentId, null);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDeploymentInfoAsync(String appId, String deploymentId, final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDeploymentInfoValidateBeforeCall(appId, deploymentId, _callback);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDeploymentInfoRequestBuilder {
        private final String appId;
        private final String deploymentId;

        private GetDeploymentInfoRequestBuilder(String appId, String deploymentId) {
            this.appId = appId;
            this.deploymentId = deploymentId;
        }

        /**
         * Build call for getDeploymentInfo
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON of the requested deployment </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDeploymentInfoCall(appId, deploymentId, _callback);
        }


        /**
         * Execute getDeploymentInfo request
         * @return AppsDeploymentResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON of the requested deployment </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsDeploymentResponse execute() throws ApiException {
            ApiResponse<AppsDeploymentResponse> localVarResp = getDeploymentInfoWithHttpInfo(appId, deploymentId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDeploymentInfo request with HTTP info returned
         * @return ApiResponse&lt;AppsDeploymentResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON of the requested deployment </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsDeploymentResponse> executeWithHttpInfo() throws ApiException {
            return getDeploymentInfoWithHttpInfo(appId, deploymentId);
        }

        /**
         * Execute getDeploymentInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON of the requested deployment </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {
            return getDeploymentInfoAsync(appId, deploymentId, _callback);
        }
    }

    /**
     * Retrieve an App Deployment
     * Retrieve information about an app deployment.
     * @param appId The app ID (required)
     * @param deploymentId The deployment ID (required)
     * @return GetDeploymentInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON of the requested deployment </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDeploymentInfoRequestBuilder getDeploymentInfo(String appId, String deploymentId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (deploymentId == null) throw new IllegalArgumentException("\"deploymentId\" is required but got null");
            

        return new GetDeploymentInfoRequestBuilder(appId, deploymentId);
    }
    private okhttp3.Call getDeploymentLogsCall(String appId, String deploymentId, String componentName, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/deployments/{deployment_id}/components/{component_name}/logs"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()))
            .replace("{" + "deployment_id" + "}", localVarApiClient.escapeString(deploymentId.toString()))
            .replace("{" + "component_name" + "}", localVarApiClient.escapeString(componentName.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (follow != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("follow", follow));
        }

        if (type != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("type", type));
        }

        if (podConnectionTimeout != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("pod_connection_timeout", podConnectionTimeout));
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
    private okhttp3.Call getDeploymentLogsValidateBeforeCall(String appId, String deploymentId, String componentName, String type, Boolean follow, String podConnectionTimeout, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling getDeploymentLogs(Async)");
        }

        // verify the required parameter 'deploymentId' is set
        if (deploymentId == null) {
            throw new ApiException("Missing the required parameter 'deploymentId' when calling getDeploymentLogs(Async)");
        }

        // verify the required parameter 'componentName' is set
        if (componentName == null) {
            throw new ApiException("Missing the required parameter 'componentName' when calling getDeploymentLogs(Async)");
        }

        // verify the required parameter 'type' is set
        if (type == null) {
            throw new ApiException("Missing the required parameter 'type' when calling getDeploymentLogs(Async)");
        }

        return getDeploymentLogsCall(appId, deploymentId, componentName, type, follow, podConnectionTimeout, _callback);

    }


    private ApiResponse<AppsGetLogsResponse> getDeploymentLogsWithHttpInfo(String appId, String deploymentId, String componentName, String type, Boolean follow, String podConnectionTimeout) throws ApiException {
        okhttp3.Call localVarCall = getDeploymentLogsValidateBeforeCall(appId, deploymentId, componentName, type, follow, podConnectionTimeout, null);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getDeploymentLogsAsync(String appId, String deploymentId, String componentName, String type, Boolean follow, String podConnectionTimeout, final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getDeploymentLogsValidateBeforeCall(appId, deploymentId, componentName, type, follow, podConnectionTimeout, _callback);
        Type localVarReturnType = new TypeToken<AppsGetLogsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetDeploymentLogsRequestBuilder {
        private final String appId;
        private final String deploymentId;
        private final String componentName;
        private final String type;
        private Boolean follow;
        private String podConnectionTimeout;

        private GetDeploymentLogsRequestBuilder(String appId, String deploymentId, String componentName, String type) {
            this.appId = appId;
            this.deploymentId = deploymentId;
            this.componentName = componentName;
            this.type = type;
        }

        /**
         * Set follow
         * @param follow Whether the logs should follow live updates. (optional)
         * @return GetDeploymentLogsRequestBuilder
         */
        public GetDeploymentLogsRequestBuilder follow(Boolean follow) {
            this.follow = follow;
            return this;
        }
        
        /**
         * Set podConnectionTimeout
         * @param podConnectionTimeout An optional time duration to wait if the underlying component instance is not immediately available. Default: &#x60;3m&#x60;. (optional)
         * @return GetDeploymentLogsRequestBuilder
         */
        public GetDeploymentLogsRequestBuilder podConnectionTimeout(String podConnectionTimeout) {
            this.podConnectionTimeout = podConnectionTimeout;
            return this;
        }
        
        /**
         * Build call for getDeploymentLogs
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getDeploymentLogsCall(appId, deploymentId, componentName, type, follow, podConnectionTimeout, _callback);
        }


        /**
         * Execute getDeploymentLogs request
         * @return AppsGetLogsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsGetLogsResponse execute() throws ApiException {
            ApiResponse<AppsGetLogsResponse> localVarResp = getDeploymentLogsWithHttpInfo(appId, deploymentId, componentName, type, follow, podConnectionTimeout);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getDeploymentLogs request with HTTP info returned
         * @return ApiResponse&lt;AppsGetLogsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsGetLogsResponse> executeWithHttpInfo() throws ApiException {
            return getDeploymentLogsWithHttpInfo(appId, deploymentId, componentName, type, follow, podConnectionTimeout);
        }

        /**
         * Execute getDeploymentLogs request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsGetLogsResponse> _callback) throws ApiException {
            return getDeploymentLogsAsync(appId, deploymentId, componentName, type, follow, podConnectionTimeout, _callback);
        }
    }

    /**
     * Retrieve Deployment Logs
     * Retrieve the logs of a past, in-progress, or active deployment. The response will include links to either real-time logs of an in-progress or active deployment or archived logs of a past deployment.
     * @param appId The app ID (required)
     * @param deploymentId The deployment ID (required)
     * @param componentName An optional component name. If set, logs will be limited to this component only. (required)
     * @param type The type of logs to retrieve - BUILD: Build-time logs - DEPLOY: Deploy-time logs - RUN: Live run-time logs - RUN_RESTARTED: Logs of crashed/restarted instances during runtime (required)
     * @return GetDeploymentLogsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with urls that point to archived logs </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetDeploymentLogsRequestBuilder getDeploymentLogs(String appId, String deploymentId, String componentName, String type) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (deploymentId == null) throw new IllegalArgumentException("\"deploymentId\" is required but got null");
            

        if (componentName == null) throw new IllegalArgumentException("\"componentName\" is required but got null");
            

        if (type == null) throw new IllegalArgumentException("\"type\" is required but got null");
            

        return new GetDeploymentLogsRequestBuilder(appId, deploymentId, componentName, type);
    }
    private okhttp3.Call getInstanceSizeCall(String slug, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/tiers/instance_sizes/{slug}"
            .replace("{" + "slug" + "}", localVarApiClient.escapeString(slug.toString()));

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
    private okhttp3.Call getInstanceSizeValidateBeforeCall(String slug, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'slug' is set
        if (slug == null) {
            throw new ApiException("Missing the required parameter 'slug' when calling getInstanceSize(Async)");
        }

        return getInstanceSizeCall(slug, _callback);

    }


    private ApiResponse<AppsGetInstanceSizeResponse> getInstanceSizeWithHttpInfo(String slug) throws ApiException {
        okhttp3.Call localVarCall = getInstanceSizeValidateBeforeCall(slug, null);
        Type localVarReturnType = new TypeToken<AppsGetInstanceSizeResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getInstanceSizeAsync(String slug, final ApiCallback<AppsGetInstanceSizeResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getInstanceSizeValidateBeforeCall(slug, _callback);
        Type localVarReturnType = new TypeToken<AppsGetInstanceSizeResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetInstanceSizeRequestBuilder {
        private final String slug;

        private GetInstanceSizeRequestBuilder(String slug) {
            this.slug = slug;
        }

        /**
         * Build call for getInstanceSize
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_size&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getInstanceSizeCall(slug, _callback);
        }


        /**
         * Execute getInstanceSize request
         * @return AppsGetInstanceSizeResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_size&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsGetInstanceSizeResponse execute() throws ApiException {
            ApiResponse<AppsGetInstanceSizeResponse> localVarResp = getInstanceSizeWithHttpInfo(slug);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getInstanceSize request with HTTP info returned
         * @return ApiResponse&lt;AppsGetInstanceSizeResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_size&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsGetInstanceSizeResponse> executeWithHttpInfo() throws ApiException {
            return getInstanceSizeWithHttpInfo(slug);
        }

        /**
         * Execute getInstanceSize request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_size&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsGetInstanceSizeResponse> _callback) throws ApiException {
            return getInstanceSizeAsync(slug, _callback);
        }
    }

    /**
     * Retrieve an Instance Size
     * Retrieve information about a specific instance size for &#x60;service&#x60;, &#x60;worker&#x60;, and &#x60;job&#x60; components.
     * @param slug The slug of the instance size (required)
     * @return GetInstanceSizeRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON with key &#x60;instance_size&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetInstanceSizeRequestBuilder getInstanceSize(String slug) throws IllegalArgumentException {
        if (slug == null) throw new IllegalArgumentException("\"slug\" is required but got null");
            

        return new GetInstanceSizeRequestBuilder(slug);
    }
    private okhttp3.Call getMultipleDailyMetricsCall(AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appMetricsBandwidthUsageRequest;

        // create path and map variables
        String localVarPath = "/v2/apps/metrics/bandwidth_daily";

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
    private okhttp3.Call getMultipleDailyMetricsValidateBeforeCall(AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appMetricsBandwidthUsageRequest' is set
        if (appMetricsBandwidthUsageRequest == null) {
            throw new ApiException("Missing the required parameter 'appMetricsBandwidthUsageRequest' when calling getMultipleDailyMetrics(Async)");
        }

        return getMultipleDailyMetricsCall(appMetricsBandwidthUsageRequest, _callback);

    }


    private ApiResponse<AppMetricsBandwidthUsage> getMultipleDailyMetricsWithHttpInfo(AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest) throws ApiException {
        okhttp3.Call localVarCall = getMultipleDailyMetricsValidateBeforeCall(appMetricsBandwidthUsageRequest, null);
        Type localVarReturnType = new TypeToken<AppMetricsBandwidthUsage>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getMultipleDailyMetricsAsync(AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest, final ApiCallback<AppMetricsBandwidthUsage> _callback) throws ApiException {

        okhttp3.Call localVarCall = getMultipleDailyMetricsValidateBeforeCall(appMetricsBandwidthUsageRequest, _callback);
        Type localVarReturnType = new TypeToken<AppMetricsBandwidthUsage>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetMultipleDailyMetricsRequestBuilder {
        private final List<String> appIds;
        private OffsetDateTime date;

        private GetMultipleDailyMetricsRequestBuilder(List<String> appIds) {
            this.appIds = appIds;
        }

        /**
         * Set date
         * @param date Optional day to query. Only the date component of the timestamp will be considered. Default: yesterday. (optional)
         * @return GetMultipleDailyMetricsRequestBuilder
         */
        public GetMultipleDailyMetricsRequestBuilder date(OffsetDateTime date) {
            this.date = date;
            return this;
        }
        
        /**
         * Build call for getMultipleDailyMetrics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest = buildBodyParams();
            return getMultipleDailyMetricsCall(appMetricsBandwidthUsageRequest, _callback);
        }

        private AppMetricsBandwidthUsageRequest buildBodyParams() {
            AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest = new AppMetricsBandwidthUsageRequest();
            appMetricsBandwidthUsageRequest.appIds(this.appIds);
            appMetricsBandwidthUsageRequest.date(this.date);
            return appMetricsBandwidthUsageRequest;
        }

        /**
         * Execute getMultipleDailyMetrics request
         * @return AppMetricsBandwidthUsage
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppMetricsBandwidthUsage execute() throws ApiException {
            AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest = buildBodyParams();
            ApiResponse<AppMetricsBandwidthUsage> localVarResp = getMultipleDailyMetricsWithHttpInfo(appMetricsBandwidthUsageRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getMultipleDailyMetrics request with HTTP info returned
         * @return ApiResponse&lt;AppMetricsBandwidthUsage&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppMetricsBandwidthUsage> executeWithHttpInfo() throws ApiException {
            AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest = buildBodyParams();
            return getMultipleDailyMetricsWithHttpInfo(appMetricsBandwidthUsageRequest);
        }

        /**
         * Execute getMultipleDailyMetrics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppMetricsBandwidthUsage> _callback) throws ApiException {
            AppMetricsBandwidthUsageRequest appMetricsBandwidthUsageRequest = buildBodyParams();
            return getMultipleDailyMetricsAsync(appMetricsBandwidthUsageRequest, _callback);
        }
    }

    /**
     * Retrieve Multiple Apps&#39; Daily Bandwidth Metrics
     * Retrieve daily bandwidth usage metrics for multiple apps.
     * @param appMetricsBandwidthUsageRequest  (required)
     * @return GetMultipleDailyMetricsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;app_bandwidth_usage&#x60; key </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetMultipleDailyMetricsRequestBuilder getMultipleDailyMetrics(List<String> appIds) throws IllegalArgumentException {
        if (appIds == null) throw new IllegalArgumentException("\"appIds\" is required but got null");
        return new GetMultipleDailyMetricsRequestBuilder(appIds);
    }
    private okhttp3.Call getTierInfoCall(String slug, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/tiers/{slug}"
            .replace("{" + "slug" + "}", localVarApiClient.escapeString(slug.toString()));

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
    private okhttp3.Call getTierInfoValidateBeforeCall(String slug, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'slug' is set
        if (slug == null) {
            throw new ApiException("Missing the required parameter 'slug' when calling getTierInfo(Async)");
        }

        return getTierInfoCall(slug, _callback);

    }


    private ApiResponse<AppsGetTierResponse> getTierInfoWithHttpInfo(String slug) throws ApiException {
        okhttp3.Call localVarCall = getTierInfoValidateBeforeCall(slug, null);
        Type localVarReturnType = new TypeToken<AppsGetTierResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getTierInfoAsync(String slug, final ApiCallback<AppsGetTierResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getTierInfoValidateBeforeCall(slug, _callback);
        Type localVarReturnType = new TypeToken<AppsGetTierResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetTierInfoRequestBuilder {
        private final String slug;

        private GetTierInfoRequestBuilder(String slug) {
            this.slug = slug;
        }

        /**
         * Build call for getTierInfo
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with the key &#x60;tier&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getTierInfoCall(slug, _callback);
        }


        /**
         * Execute getTierInfo request
         * @return AppsGetTierResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with the key &#x60;tier&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsGetTierResponse execute() throws ApiException {
            ApiResponse<AppsGetTierResponse> localVarResp = getTierInfoWithHttpInfo(slug);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getTierInfo request with HTTP info returned
         * @return ApiResponse&lt;AppsGetTierResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with the key &#x60;tier&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsGetTierResponse> executeWithHttpInfo() throws ApiException {
            return getTierInfoWithHttpInfo(slug);
        }

        /**
         * Execute getTierInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with the key &#x60;tier&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsGetTierResponse> _callback) throws ApiException {
            return getTierInfoAsync(slug, _callback);
        }
    }

    /**
     * Retrieve an App Tier
     * Retrieve information about a specific app tier.
     * @param slug The slug of the tier (required)
     * @return GetTierInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON with the key &#x60;tier&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetTierInfoRequestBuilder getTierInfo(String slug) throws IllegalArgumentException {
        if (slug == null) throw new IllegalArgumentException("\"slug\" is required but got null");
            

        return new GetTierInfoRequestBuilder(slug);
    }
    private okhttp3.Call listCall(Integer page, Integer perPage, Boolean withProjects, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (page != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("page", page));
        }

        if (perPage != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("per_page", perPage));
        }

        if (withProjects != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("with_projects", withProjects));
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
    private okhttp3.Call listValidateBeforeCall(Integer page, Integer perPage, Boolean withProjects, final ApiCallback _callback) throws ApiException {
        return listCall(page, perPage, withProjects, _callback);

    }


    private ApiResponse<AppsResponse> listWithHttpInfo(Integer page, Integer perPage, Boolean withProjects) throws ApiException {
        okhttp3.Call localVarCall = listValidateBeforeCall(page, perPage, withProjects, null);
        Type localVarReturnType = new TypeToken<AppsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAsync(Integer page, Integer perPage, Boolean withProjects, final ApiCallback<AppsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listValidateBeforeCall(page, perPage, withProjects, _callback);
        Type localVarReturnType = new TypeToken<AppsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRequestBuilder {
        private Integer page;
        private Integer perPage;
        private Boolean withProjects;

        private ListRequestBuilder() {
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
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set withProjects
         * @param withProjects Whether the project_id of listed apps should be fetched and included. (optional)
         * @return ListRequestBuilder
         */
        public ListRequestBuilder withProjects(Boolean withProjects) {
            this.withProjects = withProjects;
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
            <tr><td> 200 </td><td> A JSON object with a &#x60;apps&#x60; key. This is list of object &#x60;apps&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listCall(page, perPage, withProjects, _callback);
        }


        /**
         * Execute list request
         * @return AppsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;apps&#x60; key. This is list of object &#x60;apps&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsResponse execute() throws ApiException {
            ApiResponse<AppsResponse> localVarResp = listWithHttpInfo(page, perPage, withProjects);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute list request with HTTP info returned
         * @return ApiResponse&lt;AppsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;apps&#x60; key. This is list of object &#x60;apps&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsResponse> executeWithHttpInfo() throws ApiException {
            return listWithHttpInfo(page, perPage, withProjects);
        }

        /**
         * Execute list request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;apps&#x60; key. This is list of object &#x60;apps&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsResponse> _callback) throws ApiException {
            return listAsync(page, perPage, withProjects, _callback);
        }
    }

    /**
     * List All Apps
     * List all apps on your account. Information about the current active deployment as well as any in progress ones will also be included for each app.
     * @return ListRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;apps&#x60; key. This is list of object &#x60;apps&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRequestBuilder list() throws IllegalArgumentException {
        return new ListRequestBuilder();
    }
    private okhttp3.Call listAlertsCall(String appId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/alerts"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

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
    private okhttp3.Call listAlertsValidateBeforeCall(String appId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling listAlerts(Async)");
        }

        return listAlertsCall(appId, _callback);

    }


    private ApiResponse<AppsListAlertsResponse> listAlertsWithHttpInfo(String appId) throws ApiException {
        okhttp3.Call localVarCall = listAlertsValidateBeforeCall(appId, null);
        Type localVarReturnType = new TypeToken<AppsListAlertsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAlertsAsync(String appId, final ApiCallback<AppsListAlertsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAlertsValidateBeforeCall(appId, _callback);
        Type localVarReturnType = new TypeToken<AppsListAlertsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAlertsRequestBuilder {
        private final String appId;

        private ListAlertsRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Build call for listAlerts
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;alerts&#x60; key. This is list of object &#x60;alerts&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAlertsCall(appId, _callback);
        }


        /**
         * Execute listAlerts request
         * @return AppsListAlertsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;alerts&#x60; key. This is list of object &#x60;alerts&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsListAlertsResponse execute() throws ApiException {
            ApiResponse<AppsListAlertsResponse> localVarResp = listAlertsWithHttpInfo(appId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAlerts request with HTTP info returned
         * @return ApiResponse&lt;AppsListAlertsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;alerts&#x60; key. This is list of object &#x60;alerts&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsListAlertsResponse> executeWithHttpInfo() throws ApiException {
            return listAlertsWithHttpInfo(appId);
        }

        /**
         * Execute listAlerts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;alerts&#x60; key. This is list of object &#x60;alerts&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsListAlertsResponse> _callback) throws ApiException {
            return listAlertsAsync(appId, _callback);
        }
    }

    /**
     * List all app alerts
     * List alerts associated to the app and any components. This includes configuration information about the alerts including emails, slack webhooks, and triggering events or conditions.
     * @param appId The app ID (required)
     * @return ListAlertsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;alerts&#x60; key. This is list of object &#x60;alerts&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListAlertsRequestBuilder listAlerts(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new ListAlertsRequestBuilder(appId);
    }
    private okhttp3.Call listDeploymentsCall(String appId, Integer page, Integer perPage, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/deployments"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (page != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("page", page));
        }

        if (perPage != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("per_page", perPage));
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
    private okhttp3.Call listDeploymentsValidateBeforeCall(String appId, Integer page, Integer perPage, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling listDeployments(Async)");
        }

        return listDeploymentsCall(appId, page, perPage, _callback);

    }


    private ApiResponse<AppsDeploymentsResponse> listDeploymentsWithHttpInfo(String appId, Integer page, Integer perPage) throws ApiException {
        okhttp3.Call localVarCall = listDeploymentsValidateBeforeCall(appId, page, perPage, null);
        Type localVarReturnType = new TypeToken<AppsDeploymentsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listDeploymentsAsync(String appId, Integer page, Integer perPage, final ApiCallback<AppsDeploymentsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listDeploymentsValidateBeforeCall(appId, page, perPage, _callback);
        Type localVarReturnType = new TypeToken<AppsDeploymentsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListDeploymentsRequestBuilder {
        private final String appId;
        private Integer page;
        private Integer perPage;

        private ListDeploymentsRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListDeploymentsRequestBuilder
         */
        public ListDeploymentsRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListDeploymentsRequestBuilder
         */
        public ListDeploymentsRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Build call for listDeployments
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployments&#x60; key. This will be a list of all app deployments </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listDeploymentsCall(appId, page, perPage, _callback);
        }


        /**
         * Execute listDeployments request
         * @return AppsDeploymentsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployments&#x60; key. This will be a list of all app deployments </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsDeploymentsResponse execute() throws ApiException {
            ApiResponse<AppsDeploymentsResponse> localVarResp = listDeploymentsWithHttpInfo(appId, page, perPage);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listDeployments request with HTTP info returned
         * @return ApiResponse&lt;AppsDeploymentsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployments&#x60; key. This will be a list of all app deployments </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsDeploymentsResponse> executeWithHttpInfo() throws ApiException {
            return listDeploymentsWithHttpInfo(appId, page, perPage);
        }

        /**
         * Execute listDeployments request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployments&#x60; key. This will be a list of all app deployments </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsDeploymentsResponse> _callback) throws ApiException {
            return listDeploymentsAsync(appId, page, perPage, _callback);
        }
    }

    /**
     * List App Deployments
     * List all deployments of an app.
     * @param appId The app ID (required)
     * @return ListDeploymentsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;deployments&#x60; key. This will be a list of all app deployments </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListDeploymentsRequestBuilder listDeployments(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new ListDeploymentsRequestBuilder(appId);
    }
    private okhttp3.Call listInstanceSizesCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/tiers/instance_sizes";

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
    private okhttp3.Call listInstanceSizesValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return listInstanceSizesCall(_callback);

    }


    private ApiResponse<AppsListInstanceSizesResponse> listInstanceSizesWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listInstanceSizesValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<AppsListInstanceSizesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listInstanceSizesAsync(final ApiCallback<AppsListInstanceSizesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listInstanceSizesValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<AppsListInstanceSizesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListInstanceSizesRequestBuilder {

        private ListInstanceSizesRequestBuilder() {
        }

        /**
         * Build call for listInstanceSizes
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_sizes&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listInstanceSizesCall(_callback);
        }


        /**
         * Execute listInstanceSizes request
         * @return AppsListInstanceSizesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_sizes&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsListInstanceSizesResponse execute() throws ApiException {
            ApiResponse<AppsListInstanceSizesResponse> localVarResp = listInstanceSizesWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listInstanceSizes request with HTTP info returned
         * @return ApiResponse&lt;AppsListInstanceSizesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_sizes&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsListInstanceSizesResponse> executeWithHttpInfo() throws ApiException {
            return listInstanceSizesWithHttpInfo();
        }

        /**
         * Execute listInstanceSizes request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON with key &#x60;instance_sizes&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsListInstanceSizesResponse> _callback) throws ApiException {
            return listInstanceSizesAsync(_callback);
        }
    }

    /**
     * List Instance Sizes
     * List all instance sizes for &#x60;service&#x60;, &#x60;worker&#x60;, and &#x60;job&#x60; components.
     * @return ListInstanceSizesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON with key &#x60;instance_sizes&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListInstanceSizesRequestBuilder listInstanceSizes() throws IllegalArgumentException {
        return new ListInstanceSizesRequestBuilder();
    }
    private okhttp3.Call listRegionsCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/regions";

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
    private okhttp3.Call listRegionsValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return listRegionsCall(_callback);

    }


    private ApiResponse<AppsListRegionsResponse> listRegionsWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listRegionsValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<AppsListRegionsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listRegionsAsync(final ApiCallback<AppsListRegionsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listRegionsValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<AppsListRegionsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListRegionsRequestBuilder {

        private ListRegionsRequestBuilder() {
        }

        /**
         * Build call for listRegions
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with key &#x60;regions&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listRegionsCall(_callback);
        }


        /**
         * Execute listRegions request
         * @return AppsListRegionsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with key &#x60;regions&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsListRegionsResponse execute() throws ApiException {
            ApiResponse<AppsListRegionsResponse> localVarResp = listRegionsWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listRegions request with HTTP info returned
         * @return ApiResponse&lt;AppsListRegionsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with key &#x60;regions&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsListRegionsResponse> executeWithHttpInfo() throws ApiException {
            return listRegionsWithHttpInfo();
        }

        /**
         * Execute listRegions request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with key &#x60;regions&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsListRegionsResponse> _callback) throws ApiException {
            return listRegionsAsync(_callback);
        }
    }

    /**
     * List App Regions
     * List all regions supported by App Platform.
     * @return ListRegionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with key &#x60;regions&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListRegionsRequestBuilder listRegions() throws IllegalArgumentException {
        return new ListRegionsRequestBuilder();
    }
    private okhttp3.Call listTiersCall(final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/tiers";

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
    private okhttp3.Call listTiersValidateBeforeCall(final ApiCallback _callback) throws ApiException {
        return listTiersCall(_callback);

    }


    private ApiResponse<AppsListTiersResponse> listTiersWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listTiersValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<AppsListTiersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listTiersAsync(final ApiCallback<AppsListTiersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listTiersValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<AppsListTiersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListTiersRequestBuilder {

        private ListTiersRequestBuilder() {
        }

        /**
         * Build call for listTiers
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;tiers&#x60; key. This will be a list of all app tiers </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listTiersCall(_callback);
        }


        /**
         * Execute listTiers request
         * @return AppsListTiersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;tiers&#x60; key. This will be a list of all app tiers </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsListTiersResponse execute() throws ApiException {
            ApiResponse<AppsListTiersResponse> localVarResp = listTiersWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listTiers request with HTTP info returned
         * @return ApiResponse&lt;AppsListTiersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;tiers&#x60; key. This will be a list of all app tiers </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsListTiersResponse> executeWithHttpInfo() throws ApiException {
            return listTiersWithHttpInfo();
        }

        /**
         * Execute listTiers request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;tiers&#x60; key. This will be a list of all app tiers </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsListTiersResponse> _callback) throws ApiException {
            return listTiersAsync(_callback);
        }
    }

    /**
     * List App Tiers
     * List all app tiers.
     * @return ListTiersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;tiers&#x60; key. This will be a list of all app tiers </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListTiersRequestBuilder listTiers() throws IllegalArgumentException {
        return new ListTiersRequestBuilder();
    }
    private okhttp3.Call proposeAppSpecCall(AppPropose appPropose, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appPropose;

        // create path and map variables
        String localVarPath = "/v2/apps/propose";

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
    private okhttp3.Call proposeAppSpecValidateBeforeCall(AppPropose appPropose, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appPropose' is set
        if (appPropose == null) {
            throw new ApiException("Missing the required parameter 'appPropose' when calling proposeAppSpec(Async)");
        }

        return proposeAppSpecCall(appPropose, _callback);

    }


    private ApiResponse<AppProposeResponse> proposeAppSpecWithHttpInfo(AppPropose appPropose) throws ApiException {
        okhttp3.Call localVarCall = proposeAppSpecValidateBeforeCall(appPropose, null);
        Type localVarReturnType = new TypeToken<AppProposeResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call proposeAppSpecAsync(AppPropose appPropose, final ApiCallback<AppProposeResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = proposeAppSpecValidateBeforeCall(appPropose, _callback);
        Type localVarReturnType = new TypeToken<AppProposeResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ProposeAppSpecRequestBuilder {
        private final AppSpec spec;
        private String appId;

        private ProposeAppSpecRequestBuilder(AppSpec spec) {
            this.spec = spec;
        }

        /**
         * Set appId
         * @param appId An optional ID of an existing app. If set, the spec will be treated as a proposed update to the specified app. The existing app is not modified using this method. (optional)
         * @return ProposeAppSpecRequestBuilder
         */
        public ProposeAppSpecRequestBuilder appId(String appId) {
            this.appId = appId;
            return this;
        }
        
        /**
         * Build call for proposeAppSpec
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppPropose appPropose = buildBodyParams();
            return proposeAppSpecCall(appPropose, _callback);
        }

        private AppPropose buildBodyParams() {
            AppPropose appPropose = new AppPropose();
            appPropose.spec(this.spec);
            appPropose.appId(this.appId);
            return appPropose;
        }

        /**
         * Execute proposeAppSpec request
         * @return AppProposeResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppProposeResponse execute() throws ApiException {
            AppPropose appPropose = buildBodyParams();
            ApiResponse<AppProposeResponse> localVarResp = proposeAppSpecWithHttpInfo(appPropose);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute proposeAppSpec request with HTTP info returned
         * @return ApiResponse&lt;AppProposeResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppProposeResponse> executeWithHttpInfo() throws ApiException {
            AppPropose appPropose = buildBodyParams();
            return proposeAppSpecWithHttpInfo(appPropose);
        }

        /**
         * Execute proposeAppSpec request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppProposeResponse> _callback) throws ApiException {
            AppPropose appPropose = buildBodyParams();
            return proposeAppSpecAsync(appPropose, _callback);
        }
    }

    /**
     * Propose an App Spec
     * To propose and validate a spec for a new or existing app, send a POST request to the &#x60;/v2/apps/propose&#x60; endpoint. The request returns some information about the proposed app, including app cost and upgrade cost. If an existing app ID is specified, the app spec is treated as a proposed update to the existing app.
     * @param appPropose  (required)
     * @return ProposeAppSpecRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ProposeAppSpecRequestBuilder proposeAppSpec(AppSpec spec) throws IllegalArgumentException {
        if (spec == null) throw new IllegalArgumentException("\"spec\" is required but got null");
        return new ProposeAppSpecRequestBuilder(spec);
    }
    private okhttp3.Call revertRollbackCall(String appId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/apps/{app_id}/rollback/revert"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

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
    private okhttp3.Call revertRollbackValidateBeforeCall(String appId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling revertRollback(Async)");
        }

        return revertRollbackCall(appId, _callback);

    }


    private ApiResponse<AppsDeploymentResponse> revertRollbackWithHttpInfo(String appId) throws ApiException {
        okhttp3.Call localVarCall = revertRollbackValidateBeforeCall(appId, null);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call revertRollbackAsync(String appId, final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = revertRollbackValidateBeforeCall(appId, _callback);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RevertRollbackRequestBuilder {
        private final String appId;

        private RevertRollbackRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Build call for revertRollback
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return revertRollbackCall(appId, _callback);
        }


        /**
         * Execute revertRollback request
         * @return AppsDeploymentResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsDeploymentResponse execute() throws ApiException {
            ApiResponse<AppsDeploymentResponse> localVarResp = revertRollbackWithHttpInfo(appId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute revertRollback request with HTTP info returned
         * @return ApiResponse&lt;AppsDeploymentResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsDeploymentResponse> executeWithHttpInfo() throws ApiException {
            return revertRollbackWithHttpInfo(appId);
        }

        /**
         * Execute revertRollback request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {
            return revertRollbackAsync(appId, _callback);
        }
    }

    /**
     * Revert App Rollback
     * Revert an app rollback. This action reverts the active rollback by creating a new deployment from the latest app spec prior to the rollback and unpins the app to resume new deployments. 
     * @param appId The app ID (required)
     * @return RevertRollbackRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RevertRollbackRequestBuilder revertRollback(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new RevertRollbackRequestBuilder(appId);
    }
    private okhttp3.Call rollbackDeploymentCall(String appId, AppsRollbackAppRequest appsRollbackAppRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appsRollbackAppRequest;

        // create path and map variables
        String localVarPath = "/v2/apps/{app_id}/rollback"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

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
    private okhttp3.Call rollbackDeploymentValidateBeforeCall(String appId, AppsRollbackAppRequest appsRollbackAppRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling rollbackDeployment(Async)");
        }

        // verify the required parameter 'appsRollbackAppRequest' is set
        if (appsRollbackAppRequest == null) {
            throw new ApiException("Missing the required parameter 'appsRollbackAppRequest' when calling rollbackDeployment(Async)");
        }

        return rollbackDeploymentCall(appId, appsRollbackAppRequest, _callback);

    }


    private ApiResponse<AppsDeploymentResponse> rollbackDeploymentWithHttpInfo(String appId, AppsRollbackAppRequest appsRollbackAppRequest) throws ApiException {
        okhttp3.Call localVarCall = rollbackDeploymentValidateBeforeCall(appId, appsRollbackAppRequest, null);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call rollbackDeploymentAsync(String appId, AppsRollbackAppRequest appsRollbackAppRequest, final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = rollbackDeploymentValidateBeforeCall(appId, appsRollbackAppRequest, _callback);
        Type localVarReturnType = new TypeToken<AppsDeploymentResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RollbackDeploymentRequestBuilder {
        private final String appId;
        private String deploymentId;
        private Boolean skipPin;

        private RollbackDeploymentRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Set deploymentId
         * @param deploymentId The ID of the deployment to rollback to. (optional)
         * @return RollbackDeploymentRequestBuilder
         */
        public RollbackDeploymentRequestBuilder deploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
            return this;
        }
        
        /**
         * Set skipPin
         * @param skipPin Whether to skip pinning the rollback deployment. If false, the rollback deployment will be pinned and any new deployments including Auto Deploy on Push hooks will be disabled until the rollback is either manually committed or reverted via the CommitAppRollback or RevertAppRollback endpoints respectively. If true, the rollback will be immediately committed and the app will remain unpinned. (optional)
         * @return RollbackDeploymentRequestBuilder
         */
        public RollbackDeploymentRequestBuilder skipPin(Boolean skipPin) {
            this.skipPin = skipPin;
            return this;
        }
        
        /**
         * Build call for rollbackDeployment
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            return rollbackDeploymentCall(appId, appsRollbackAppRequest, _callback);
        }

        private AppsRollbackAppRequest buildBodyParams() {
            AppsRollbackAppRequest appsRollbackAppRequest = new AppsRollbackAppRequest();
            appsRollbackAppRequest.deploymentId(this.deploymentId);
            appsRollbackAppRequest.skipPin(this.skipPin);
            return appsRollbackAppRequest;
        }

        /**
         * Execute rollbackDeployment request
         * @return AppsDeploymentResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsDeploymentResponse execute() throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            ApiResponse<AppsDeploymentResponse> localVarResp = rollbackDeploymentWithHttpInfo(appId, appsRollbackAppRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute rollbackDeployment request with HTTP info returned
         * @return ApiResponse&lt;AppsDeploymentResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsDeploymentResponse> executeWithHttpInfo() throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            return rollbackDeploymentWithHttpInfo(appId, appsRollbackAppRequest);
        }

        /**
         * Execute rollbackDeployment request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsDeploymentResponse> _callback) throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            return rollbackDeploymentAsync(appId, appsRollbackAppRequest, _callback);
        }
    }

    /**
     * Rollback App
     * Rollback an app to a previous deployment. A new deployment will be created to perform the rollback. The app will be pinned to the rollback deployment preventing any new deployments from being created, either manually or through Auto Deploy on Push webhooks. To resume deployments, the rollback must be either committed or reverted.  It is recommended to use the Validate App Rollback endpoint to double check if the rollback is valid and if there are any warnings. 
     * @param appId The app ID (required)
     * @param appsRollbackAppRequest  (required)
     * @return RollbackDeploymentRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with a &#x60;deployment&#x60; key. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RollbackDeploymentRequestBuilder rollbackDeployment(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new RollbackDeploymentRequestBuilder(appId);
    }
    private okhttp3.Call updateCall(String id, AppsUpdateAppRequest appsUpdateAppRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appsUpdateAppRequest;

        // create path and map variables
        String localVarPath = "/v2/apps/{id}"
            .replace("{" + "id" + "}", localVarApiClient.escapeString(id.toString()));

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
    private okhttp3.Call updateValidateBeforeCall(String id, AppsUpdateAppRequest appsUpdateAppRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new ApiException("Missing the required parameter 'id' when calling update(Async)");
        }

        // verify the required parameter 'appsUpdateAppRequest' is set
        if (appsUpdateAppRequest == null) {
            throw new ApiException("Missing the required parameter 'appsUpdateAppRequest' when calling update(Async)");
        }

        return updateCall(id, appsUpdateAppRequest, _callback);

    }


    private ApiResponse<AppResponse> updateWithHttpInfo(String id, AppsUpdateAppRequest appsUpdateAppRequest) throws ApiException {
        okhttp3.Call localVarCall = updateValidateBeforeCall(id, appsUpdateAppRequest, null);
        Type localVarReturnType = new TypeToken<AppResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateAsync(String id, AppsUpdateAppRequest appsUpdateAppRequest, final ApiCallback<AppResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateValidateBeforeCall(id, appsUpdateAppRequest, _callback);
        Type localVarReturnType = new TypeToken<AppResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateRequestBuilder {
        private final AppSpec spec;
        private final String id;

        private UpdateRequestBuilder(AppSpec spec, String id) {
            this.spec = spec;
            this.id = id;
        }

        /**
         * Build call for update
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object of the updated &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppsUpdateAppRequest appsUpdateAppRequest = buildBodyParams();
            return updateCall(id, appsUpdateAppRequest, _callback);
        }

        private AppsUpdateAppRequest buildBodyParams() {
            AppsUpdateAppRequest appsUpdateAppRequest = new AppsUpdateAppRequest();
            appsUpdateAppRequest.spec(this.spec);
            return appsUpdateAppRequest;
        }

        /**
         * Execute update request
         * @return AppResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object of the updated &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppResponse execute() throws ApiException {
            AppsUpdateAppRequest appsUpdateAppRequest = buildBodyParams();
            ApiResponse<AppResponse> localVarResp = updateWithHttpInfo(id, appsUpdateAppRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute update request with HTTP info returned
         * @return ApiResponse&lt;AppResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object of the updated &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppResponse> executeWithHttpInfo() throws ApiException {
            AppsUpdateAppRequest appsUpdateAppRequest = buildBodyParams();
            return updateWithHttpInfo(id, appsUpdateAppRequest);
        }

        /**
         * Execute update request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object of the updated &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppResponse> _callback) throws ApiException {
            AppsUpdateAppRequest appsUpdateAppRequest = buildBodyParams();
            return updateAsync(id, appsUpdateAppRequest, _callback);
        }
    }

    /**
     * Update an App
     * Update an existing app by submitting a new app specification. For documentation on app specifications (&#x60;AppSpec&#x60; objects), please refer to [the product documentation](https://docs.digitalocean.com/products/app-platform/reference/app-spec/).
     * @param id The ID of the app (required)
     * @param appsUpdateAppRequest  (required)
     * @return UpdateRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object of the updated &#x60;app&#x60; </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateRequestBuilder update(AppSpec spec, String id) throws IllegalArgumentException {
        if (spec == null) throw new IllegalArgumentException("\"spec\" is required but got null");
        if (id == null) throw new IllegalArgumentException("\"id\" is required but got null");
            

        return new UpdateRequestBuilder(spec, id);
    }
    private okhttp3.Call updateDestinationsForAlertsCall(String appId, String alertId, AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appsAssignAppAlertDestinationsRequest;

        // create path and map variables
        String localVarPath = "/v2/apps/{app_id}/alerts/{alert_id}/destinations"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()))
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
        return localVarApiClient.buildCall(basePath, localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarCookieParams, localVarFormParams, localVarAuthNames, _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call updateDestinationsForAlertsValidateBeforeCall(String appId, String alertId, AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling updateDestinationsForAlerts(Async)");
        }

        // verify the required parameter 'alertId' is set
        if (alertId == null) {
            throw new ApiException("Missing the required parameter 'alertId' when calling updateDestinationsForAlerts(Async)");
        }

        // verify the required parameter 'appsAssignAppAlertDestinationsRequest' is set
        if (appsAssignAppAlertDestinationsRequest == null) {
            throw new ApiException("Missing the required parameter 'appsAssignAppAlertDestinationsRequest' when calling updateDestinationsForAlerts(Async)");
        }

        return updateDestinationsForAlertsCall(appId, alertId, appsAssignAppAlertDestinationsRequest, _callback);

    }


    private ApiResponse<AppsAlertResponse> updateDestinationsForAlertsWithHttpInfo(String appId, String alertId, AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest) throws ApiException {
        okhttp3.Call localVarCall = updateDestinationsForAlertsValidateBeforeCall(appId, alertId, appsAssignAppAlertDestinationsRequest, null);
        Type localVarReturnType = new TypeToken<AppsAlertResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateDestinationsForAlertsAsync(String appId, String alertId, AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest, final ApiCallback<AppsAlertResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateDestinationsForAlertsValidateBeforeCall(appId, alertId, appsAssignAppAlertDestinationsRequest, _callback);
        Type localVarReturnType = new TypeToken<AppsAlertResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateDestinationsForAlertsRequestBuilder {
        private final String appId;
        private final String alertId;
        private List<String> emails;
        private List<AppAlertSlackWebhook> slackWebhooks;

        private UpdateDestinationsForAlertsRequestBuilder(String appId, String alertId) {
            this.appId = appId;
            this.alertId = alertId;
        }

        /**
         * Set emails
         * @param emails  (optional)
         * @return UpdateDestinationsForAlertsRequestBuilder
         */
        public UpdateDestinationsForAlertsRequestBuilder emails(List<String> emails) {
            this.emails = emails;
            return this;
        }
        
        /**
         * Set slackWebhooks
         * @param slackWebhooks  (optional)
         * @return UpdateDestinationsForAlertsRequestBuilder
         */
        public UpdateDestinationsForAlertsRequestBuilder slackWebhooks(List<AppAlertSlackWebhook> slackWebhooks) {
            this.slackWebhooks = slackWebhooks;
            return this;
        }
        
        /**
         * Build call for updateDestinationsForAlerts
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;alert&#x60; key. This is an object of type &#x60;alert&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest = buildBodyParams();
            return updateDestinationsForAlertsCall(appId, alertId, appsAssignAppAlertDestinationsRequest, _callback);
        }

        private AppsAssignAppAlertDestinationsRequest buildBodyParams() {
            AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest = new AppsAssignAppAlertDestinationsRequest();
            appsAssignAppAlertDestinationsRequest.emails(this.emails);
            appsAssignAppAlertDestinationsRequest.slackWebhooks(this.slackWebhooks);
            return appsAssignAppAlertDestinationsRequest;
        }

        /**
         * Execute updateDestinationsForAlerts request
         * @return AppsAlertResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;alert&#x60; key. This is an object of type &#x60;alert&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsAlertResponse execute() throws ApiException {
            AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest = buildBodyParams();
            ApiResponse<AppsAlertResponse> localVarResp = updateDestinationsForAlertsWithHttpInfo(appId, alertId, appsAssignAppAlertDestinationsRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateDestinationsForAlerts request with HTTP info returned
         * @return ApiResponse&lt;AppsAlertResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;alert&#x60; key. This is an object of type &#x60;alert&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsAlertResponse> executeWithHttpInfo() throws ApiException {
            AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest = buildBodyParams();
            return updateDestinationsForAlertsWithHttpInfo(appId, alertId, appsAssignAppAlertDestinationsRequest);
        }

        /**
         * Execute updateDestinationsForAlerts request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with an &#x60;alert&#x60; key. This is an object of type &#x60;alert&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsAlertResponse> _callback) throws ApiException {
            AppsAssignAppAlertDestinationsRequest appsAssignAppAlertDestinationsRequest = buildBodyParams();
            return updateDestinationsForAlertsAsync(appId, alertId, appsAssignAppAlertDestinationsRequest, _callback);
        }
    }

    /**
     * Update destinations for alerts
     * Updates the emails and slack webhook destinations for app alerts. Emails must be associated to a user with access to the app.
     * @param appId The app ID (required)
     * @param alertId The alert ID (required)
     * @param appsAssignAppAlertDestinationsRequest  (required)
     * @return UpdateDestinationsForAlertsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with an &#x60;alert&#x60; key. This is an object of type &#x60;alert&#x60;. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateDestinationsForAlertsRequestBuilder updateDestinationsForAlerts(String appId, String alertId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        if (alertId == null) throw new IllegalArgumentException("\"alertId\" is required but got null");
            

        return new UpdateDestinationsForAlertsRequestBuilder(appId, alertId);
    }
    private okhttp3.Call validateRollbackCall(String appId, AppsRollbackAppRequest appsRollbackAppRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = appsRollbackAppRequest;

        // create path and map variables
        String localVarPath = "/v2/apps/{app_id}/rollback/validate"
            .replace("{" + "app_id" + "}", localVarApiClient.escapeString(appId.toString()));

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
    private okhttp3.Call validateRollbackValidateBeforeCall(String appId, AppsRollbackAppRequest appsRollbackAppRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'appId' is set
        if (appId == null) {
            throw new ApiException("Missing the required parameter 'appId' when calling validateRollback(Async)");
        }

        // verify the required parameter 'appsRollbackAppRequest' is set
        if (appsRollbackAppRequest == null) {
            throw new ApiException("Missing the required parameter 'appsRollbackAppRequest' when calling validateRollback(Async)");
        }

        return validateRollbackCall(appId, appsRollbackAppRequest, _callback);

    }


    private ApiResponse<AppsValidateRollbackResponse> validateRollbackWithHttpInfo(String appId, AppsRollbackAppRequest appsRollbackAppRequest) throws ApiException {
        okhttp3.Call localVarCall = validateRollbackValidateBeforeCall(appId, appsRollbackAppRequest, null);
        Type localVarReturnType = new TypeToken<AppsValidateRollbackResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call validateRollbackAsync(String appId, AppsRollbackAppRequest appsRollbackAppRequest, final ApiCallback<AppsValidateRollbackResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = validateRollbackValidateBeforeCall(appId, appsRollbackAppRequest, _callback);
        Type localVarReturnType = new TypeToken<AppsValidateRollbackResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ValidateRollbackRequestBuilder {
        private final String appId;
        private String deploymentId;
        private Boolean skipPin;

        private ValidateRollbackRequestBuilder(String appId) {
            this.appId = appId;
        }

        /**
         * Set deploymentId
         * @param deploymentId The ID of the deployment to rollback to. (optional)
         * @return ValidateRollbackRequestBuilder
         */
        public ValidateRollbackRequestBuilder deploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
            return this;
        }
        
        /**
         * Set skipPin
         * @param skipPin Whether to skip pinning the rollback deployment. If false, the rollback deployment will be pinned and any new deployments including Auto Deploy on Push hooks will be disabled until the rollback is either manually committed or reverted via the CommitAppRollback or RevertAppRollback endpoints respectively. If true, the rollback will be immediately committed and the app will remain unpinned. (optional)
         * @return ValidateRollbackRequestBuilder
         */
        public ValidateRollbackRequestBuilder skipPin(Boolean skipPin) {
            this.skipPin = skipPin;
            return this;
        }
        
        /**
         * Build call for validateRollback
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with the validation results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            return validateRollbackCall(appId, appsRollbackAppRequest, _callback);
        }

        private AppsRollbackAppRequest buildBodyParams() {
            AppsRollbackAppRequest appsRollbackAppRequest = new AppsRollbackAppRequest();
            appsRollbackAppRequest.deploymentId(this.deploymentId);
            appsRollbackAppRequest.skipPin(this.skipPin);
            return appsRollbackAppRequest;
        }

        /**
         * Execute validateRollback request
         * @return AppsValidateRollbackResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with the validation results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AppsValidateRollbackResponse execute() throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            ApiResponse<AppsValidateRollbackResponse> localVarResp = validateRollbackWithHttpInfo(appId, appsRollbackAppRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute validateRollback request with HTTP info returned
         * @return ApiResponse&lt;AppsValidateRollbackResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with the validation results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AppsValidateRollbackResponse> executeWithHttpInfo() throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            return validateRollbackWithHttpInfo(appId, appsRollbackAppRequest);
        }

        /**
         * Execute validateRollback request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object with the validation results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AppsValidateRollbackResponse> _callback) throws ApiException {
            AppsRollbackAppRequest appsRollbackAppRequest = buildBodyParams();
            return validateRollbackAsync(appId, appsRollbackAppRequest, _callback);
        }
    }

    /**
     * Validate App Rollback
     * Check whether an app can be rolled back to a specific deployment. This endpoint can also be used to check if there are any warnings or validation conditions that will cause the rollback to proceed under unideal circumstances. For example, if a component must be rebuilt as part of the rollback causing it to take longer than usual. 
     * @param appId The app ID (required)
     * @param appsRollbackAppRequest  (required)
     * @return ValidateRollbackRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object with the validation results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ValidateRollbackRequestBuilder validateRollback(String appId) throws IllegalArgumentException {
        if (appId == null) throw new IllegalArgumentException("\"appId\" is required but got null");
            

        return new ValidateRollbackRequestBuilder(appId);
    }
}
