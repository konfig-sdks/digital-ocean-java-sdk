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


import com.konfigthis.client.model.AssociatedKubernetesResources;
import com.konfigthis.client.model.Cluster;
import com.konfigthis.client.model.ClusterRegistries;
import com.konfigthis.client.model.ClusterStatus;
import com.konfigthis.client.model.ClusterUpdate;
import com.konfigthis.client.model.ClusterlintRequest;
import com.konfigthis.client.model.ClusterlintResults;
import com.konfigthis.client.model.Credentials;
import com.konfigthis.client.model.DestroyAssociatedKubernetesResources;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.KubernetesAddNodePoolResponse;
import com.konfigthis.client.model.KubernetesCreateNewClusterResponse;
import com.konfigthis.client.model.KubernetesGetAvailableUpgradesResponse;
import com.konfigthis.client.model.KubernetesGetClusterInfoResponse;
import com.konfigthis.client.model.KubernetesGetNodePoolResponse;
import com.konfigthis.client.model.KubernetesListClustersResponse;
import com.konfigthis.client.model.KubernetesListNodePoolsResponse;
import com.konfigthis.client.model.KubernetesNodePool;
import com.konfigthis.client.model.KubernetesNodePoolBase;
import com.konfigthis.client.model.KubernetesNodePoolTaint;
import com.konfigthis.client.model.KubernetesOptions;
import com.konfigthis.client.model.KubernetesRecycleNodePoolRequest;
import com.konfigthis.client.model.KubernetesUpdateClusterResponse;
import com.konfigthis.client.model.KubernetesUpdateNodePoolResponse;
import com.konfigthis.client.model.KubernetesUpgradeClusterRequest;
import com.konfigthis.client.model.MaintenancePolicy;
import com.konfigthis.client.model.Node;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.konfigthis.client.model.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;

public class KubernetesApiGenerated {
    private ApiClient localVarApiClient;
    private int localHostIndex;
    private String localCustomBaseUrl;

    public KubernetesApiGenerated() throws IllegalArgumentException {
        this(Configuration.getDefaultApiClient());
    }

    public KubernetesApiGenerated(ApiClient apiClient) throws IllegalArgumentException {
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

    private okhttp3.Call addContainerRegistryToClustersCall(ClusterRegistries clusterRegistries, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = clusterRegistries;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/registry";

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
    private okhttp3.Call addContainerRegistryToClustersValidateBeforeCall(ClusterRegistries clusterRegistries, final ApiCallback _callback) throws ApiException {
        return addContainerRegistryToClustersCall(clusterRegistries, _callback);

    }


    private ApiResponse<Void> addContainerRegistryToClustersWithHttpInfo(ClusterRegistries clusterRegistries) throws ApiException {
        okhttp3.Call localVarCall = addContainerRegistryToClustersValidateBeforeCall(clusterRegistries, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call addContainerRegistryToClustersAsync(ClusterRegistries clusterRegistries, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = addContainerRegistryToClustersValidateBeforeCall(clusterRegistries, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class AddContainerRegistryToClustersRequestBuilder {
        private List<String> clusterUuids;

        private AddContainerRegistryToClustersRequestBuilder() {
        }

        /**
         * Set clusterUuids
         * @param clusterUuids An array containing the UUIDs of Kubernetes clusters. (optional)
         * @return AddContainerRegistryToClustersRequestBuilder
         */
        public AddContainerRegistryToClustersRequestBuilder clusterUuids(List<String> clusterUuids) {
            this.clusterUuids = clusterUuids;
            return this;
        }
        
        /**
         * Build call for addContainerRegistryToClusters
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
            ClusterRegistries clusterRegistries = buildBodyParams();
            return addContainerRegistryToClustersCall(clusterRegistries, _callback);
        }

        private ClusterRegistries buildBodyParams() {
            ClusterRegistries clusterRegistries = new ClusterRegistries();
            clusterRegistries.clusterUuids(this.clusterUuids);
            return clusterRegistries;
        }

        /**
         * Execute addContainerRegistryToClusters request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ClusterRegistries clusterRegistries = buildBodyParams();
            addContainerRegistryToClustersWithHttpInfo(clusterRegistries);
        }

        /**
         * Execute addContainerRegistryToClusters request with HTTP info returned
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
            ClusterRegistries clusterRegistries = buildBodyParams();
            return addContainerRegistryToClustersWithHttpInfo(clusterRegistries);
        }

        /**
         * Execute addContainerRegistryToClusters request (asynchronously)
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
            ClusterRegistries clusterRegistries = buildBodyParams();
            return addContainerRegistryToClustersAsync(clusterRegistries, _callback);
        }
    }

    /**
     * Add Container Registry to Kubernetes Clusters
     * To integrate the container registry with Kubernetes clusters, send a POST request to &#x60;/v2/kubernetes/registry&#x60;.
     * @return AddContainerRegistryToClustersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddContainerRegistryToClustersRequestBuilder addContainerRegistryToClusters() throws IllegalArgumentException {
        return new AddContainerRegistryToClustersRequestBuilder();
    }
    private okhttp3.Call addNodePoolCall(UUID clusterId, KubernetesNodePool kubernetesNodePool, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = kubernetesNodePool;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/node_pools"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call addNodePoolValidateBeforeCall(UUID clusterId, KubernetesNodePool kubernetesNodePool, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling addNodePool(Async)");
        }

        // verify the required parameter 'kubernetesNodePool' is set
        if (kubernetesNodePool == null) {
            throw new ApiException("Missing the required parameter 'kubernetesNodePool' when calling addNodePool(Async)");
        }

        return addNodePoolCall(clusterId, kubernetesNodePool, _callback);

    }


    private ApiResponse<KubernetesAddNodePoolResponse> addNodePoolWithHttpInfo(UUID clusterId, KubernetesNodePool kubernetesNodePool) throws ApiException {
        okhttp3.Call localVarCall = addNodePoolValidateBeforeCall(clusterId, kubernetesNodePool, null);
        Type localVarReturnType = new TypeToken<KubernetesAddNodePoolResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call addNodePoolAsync(UUID clusterId, KubernetesNodePool kubernetesNodePool, final ApiCallback<KubernetesAddNodePoolResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = addNodePoolValidateBeforeCall(clusterId, kubernetesNodePool, _callback);
        Type localVarReturnType = new TypeToken<KubernetesAddNodePoolResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class AddNodePoolRequestBuilder {
        private final String size;
        private final String name;
        private final Integer count;
        private final UUID clusterId;
        private List<String> tags;
        private UUID id;
        private Object labels;
        private List<KubernetesNodePoolTaint> taints;
        private Boolean autoScale;
        private Integer minNodes;
        private Integer maxNodes;
        private List<Node> nodes;

        private AddNodePoolRequestBuilder(String size, String name, Integer count, UUID clusterId) {
            this.size = size;
            this.name = name;
            this.count = count;
            this.clusterId = clusterId;
        }

        /**
         * Set tags
         * @param tags An array containing the tags applied to the node pool. All node pools are automatically tagged &#x60;k8s&#x60;, &#x60;k8s-worker&#x60;, and &#x60;k8s:$K8S_CLUSTER_ID&#x60;. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a specific node pool. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set labels
         * @param labels An object of key/value mappings specifying labels to apply to all nodes in a pool. Labels will automatically be applied to all existing nodes and any subsequent nodes added to the pool. Note that when a label is removed, it is not deleted from the nodes in the pool. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder labels(Object labels) {
            this.labels = labels;
            return this;
        }
        
        /**
         * Set taints
         * @param taints An array of taints to apply to all nodes in a pool. Taints will automatically be applied to all existing nodes and any subsequent nodes added to the pool. When a taint is removed, it is deleted from all nodes in the pool. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder taints(List<KubernetesNodePoolTaint> taints) {
            this.taints = taints;
            return this;
        }
        
        /**
         * Set autoScale
         * @param autoScale A boolean value indicating whether auto-scaling is enabled for this node pool. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder autoScale(Boolean autoScale) {
            this.autoScale = autoScale;
            return this;
        }
        
        /**
         * Set minNodes
         * @param minNodes The minimum number of nodes that this node pool can be auto-scaled to. The value will be &#x60;0&#x60; if &#x60;auto_scale&#x60; is set to &#x60;false&#x60;. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder minNodes(Integer minNodes) {
            this.minNodes = minNodes;
            return this;
        }
        
        /**
         * Set maxNodes
         * @param maxNodes The maximum number of nodes that this node pool can be auto-scaled to. The value will be &#x60;0&#x60; if &#x60;auto_scale&#x60; is set to &#x60;false&#x60;. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder maxNodes(Integer maxNodes) {
            this.maxNodes = maxNodes;
            return this;
        }
        
        /**
         * Set nodes
         * @param nodes An object specifying the details of a specific worker node in a node pool. (optional)
         * @return AddNodePoolRequestBuilder
         */
        public AddNodePoolRequestBuilder nodes(List<Node> nodes) {
            this.nodes = nodes;
            return this;
        }
        
        /**
         * Build call for addNodePool
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            KubernetesNodePool kubernetesNodePool = buildBodyParams();
            return addNodePoolCall(clusterId, kubernetesNodePool, _callback);
        }

        private KubernetesNodePool buildBodyParams() {
            KubernetesNodePool kubernetesNodePool = new KubernetesNodePool();
            kubernetesNodePool.tags(this.tags);
            kubernetesNodePool.size(this.size);
            kubernetesNodePool.count(this.count);
            kubernetesNodePool.name(this.name);
            kubernetesNodePool.autoScale(this.autoScale);
            kubernetesNodePool.minNodes(this.minNodes);
            kubernetesNodePool.maxNodes(this.maxNodes);
            return kubernetesNodePool;
        }

        /**
         * Execute addNodePool request
         * @return KubernetesAddNodePoolResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesAddNodePoolResponse execute() throws ApiException {
            KubernetesNodePool kubernetesNodePool = buildBodyParams();
            ApiResponse<KubernetesAddNodePoolResponse> localVarResp = addNodePoolWithHttpInfo(clusterId, kubernetesNodePool);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute addNodePool request with HTTP info returned
         * @return ApiResponse&lt;KubernetesAddNodePoolResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesAddNodePoolResponse> executeWithHttpInfo() throws ApiException {
            KubernetesNodePool kubernetesNodePool = buildBodyParams();
            return addNodePoolWithHttpInfo(clusterId, kubernetesNodePool);
        }

        /**
         * Execute addNodePool request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesAddNodePoolResponse> _callback) throws ApiException {
            KubernetesNodePool kubernetesNodePool = buildBodyParams();
            return addNodePoolAsync(clusterId, kubernetesNodePool, _callback);
        }
    }

    /**
     * Add a Node Pool to a Kubernetes Cluster
     * To add an additional node pool to a Kubernetes clusters, send a POST request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools&#x60; with the following attributes. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param kubernetesNodePool  (required)
     * @return AddNodePoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public AddNodePoolRequestBuilder addNodePool(String size, String name, Integer count, UUID clusterId) throws IllegalArgumentException {
        if (size == null) throw new IllegalArgumentException("\"size\" is required but got null");
            

        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (count == null) throw new IllegalArgumentException("\"count\" is required but got null");
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new AddNodePoolRequestBuilder(size, name, count, clusterId);
    }
    private okhttp3.Call createNewClusterCall(Cluster cluster, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = cluster;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters";

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
    private okhttp3.Call createNewClusterValidateBeforeCall(Cluster cluster, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'cluster' is set
        if (cluster == null) {
            throw new ApiException("Missing the required parameter 'cluster' when calling createNewCluster(Async)");
        }

        return createNewClusterCall(cluster, _callback);

    }


    private ApiResponse<KubernetesCreateNewClusterResponse> createNewClusterWithHttpInfo(Cluster cluster) throws ApiException {
        okhttp3.Call localVarCall = createNewClusterValidateBeforeCall(cluster, null);
        Type localVarReturnType = new TypeToken<KubernetesCreateNewClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call createNewClusterAsync(Cluster cluster, final ApiCallback<KubernetesCreateNewClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = createNewClusterValidateBeforeCall(cluster, _callback);
        Type localVarReturnType = new TypeToken<KubernetesCreateNewClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class CreateNewClusterRequestBuilder {
        private final String version;
        private final String name;
        private final String region;
        private final List<KubernetesNodePool> nodePools;
        private List<String> tags;
        private UUID id;
        private String clusterSubnet;
        private String serviceSubnet;
        private UUID vpcUuid;
        private String ipv4;
        private String endpoint;
        private MaintenancePolicy maintenancePolicy;
        private Boolean autoUpgrade;
        private ClusterStatus status;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
        private Boolean surgeUpgrade;
        private Boolean ha;
        private Boolean registryEnabled;

        private CreateNewClusterRequestBuilder(String version, String name, String region, List<KubernetesNodePool> nodePools) {
            this.version = version;
            this.name = name;
            this.region = region;
            this.nodePools = nodePools;
        }

        /**
         * Set tags
         * @param tags An array of tags applied to the Kubernetes cluster. All clusters are automatically tagged &#x60;k8s&#x60; and &#x60;k8s:$K8S_CLUSTER_ID&#x60;. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a Kubernetes cluster. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set clusterSubnet
         * @param clusterSubnet The range of IP addresses in the overlay network of the Kubernetes cluster in CIDR notation. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder clusterSubnet(String clusterSubnet) {
            this.clusterSubnet = clusterSubnet;
            return this;
        }
        
        /**
         * Set serviceSubnet
         * @param serviceSubnet The range of assignable IP addresses for services running in the Kubernetes cluster in CIDR notation. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder serviceSubnet(String serviceSubnet) {
            this.serviceSubnet = serviceSubnet;
            return this;
        }
        
        /**
         * Set vpcUuid
         * @param vpcUuid A string specifying the UUID of the VPC to which the Kubernetes cluster is assigned. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder vpcUuid(UUID vpcUuid) {
            this.vpcUuid = vpcUuid;
            return this;
        }
        
        /**
         * Set ipv4
         * @param ipv4 The public IPv4 address of the Kubernetes master node. This will not be set if high availability is configured on the cluster (v1.21+) (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder ipv4(String ipv4) {
            this.ipv4 = ipv4;
            return this;
        }
        
        /**
         * Set endpoint
         * @param endpoint The base URL of the API server on the Kubernetes master node. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder endpoint(String endpoint) {
            this.endpoint = endpoint;
            return this;
        }
        
        /**
         * Set maintenancePolicy
         * @param maintenancePolicy  (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder maintenancePolicy(MaintenancePolicy maintenancePolicy) {
            this.maintenancePolicy = maintenancePolicy;
            return this;
        }
        
        /**
         * Set autoUpgrade
         * @param autoUpgrade A boolean value indicating whether the cluster will be automatically upgraded to new patch releases during its maintenance window. (optional, default to false)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder autoUpgrade(Boolean autoUpgrade) {
            this.autoUpgrade = autoUpgrade;
            return this;
        }
        
        /**
         * Set status
         * @param status  (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder status(ClusterStatus status) {
            this.status = status;
            return this;
        }
        
        /**
         * Set createdAt
         * @param createdAt A time value given in ISO8601 combined date and time format that represents when the Kubernetes cluster was created. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder createdAt(OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        /**
         * Set updatedAt
         * @param updatedAt A time value given in ISO8601 combined date and time format that represents when the Kubernetes cluster was last updated. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder updatedAt(OffsetDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        /**
         * Set surgeUpgrade
         * @param surgeUpgrade A boolean value indicating whether surge upgrade is enabled/disabled for the cluster. Surge upgrade makes cluster upgrades fast and reliable by bringing up new nodes before destroying the outdated nodes. (optional, default to false)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder surgeUpgrade(Boolean surgeUpgrade) {
            this.surgeUpgrade = surgeUpgrade;
            return this;
        }
        
        /**
         * Set ha
         * @param ha A boolean value indicating whether the control plane is run in a highly available configuration in the cluster. Highly available control planes incur less downtime. The property cannot be disabled. (optional, default to false)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder ha(Boolean ha) {
            this.ha = ha;
            return this;
        }
        
        /**
         * Set registryEnabled
         * @param registryEnabled A read-only boolean value indicating if a container registry is integrated with the cluster. (optional)
         * @return CreateNewClusterRequestBuilder
         */
        public CreateNewClusterRequestBuilder registryEnabled(Boolean registryEnabled) {
            this.registryEnabled = registryEnabled;
            return this;
        }
        
        /**
         * Build call for createNewCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  The IP address and cluster API server endpoint will not be available until the cluster has finished provisioning. The initial value of the cluster&#39;s &#x60;status.state&#x60; attribute will be &#x60;provisioning&#x60;. When the cluster is ready, this will transition to &#x60;running&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            Cluster cluster = buildBodyParams();
            return createNewClusterCall(cluster, _callback);
        }

        private Cluster buildBodyParams() {
            Cluster cluster = new Cluster();
            cluster.tags(this.tags);
            cluster.version(this.version);
            cluster.id(this.id);
            cluster.name(this.name);
            cluster.region(this.region);
            cluster.clusterSubnet(this.clusterSubnet);
            cluster.serviceSubnet(this.serviceSubnet);
            cluster.vpcUuid(this.vpcUuid);
            cluster.ipv4(this.ipv4);
            cluster.endpoint(this.endpoint);
            cluster.nodePools(this.nodePools);
            cluster.maintenancePolicy(this.maintenancePolicy);
            cluster.autoUpgrade(this.autoUpgrade);
            cluster.status(this.status);
            cluster.createdAt(this.createdAt);
            cluster.updatedAt(this.updatedAt);
            cluster.surgeUpgrade(this.surgeUpgrade);
            cluster.ha(this.ha);
            cluster.registryEnabled(this.registryEnabled);
            return cluster;
        }

        /**
         * Execute createNewCluster request
         * @return KubernetesCreateNewClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  The IP address and cluster API server endpoint will not be available until the cluster has finished provisioning. The initial value of the cluster&#39;s &#x60;status.state&#x60; attribute will be &#x60;provisioning&#x60;. When the cluster is ready, this will transition to &#x60;running&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesCreateNewClusterResponse execute() throws ApiException {
            Cluster cluster = buildBodyParams();
            ApiResponse<KubernetesCreateNewClusterResponse> localVarResp = createNewClusterWithHttpInfo(cluster);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute createNewCluster request with HTTP info returned
         * @return ApiResponse&lt;KubernetesCreateNewClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  The IP address and cluster API server endpoint will not be available until the cluster has finished provisioning. The initial value of the cluster&#39;s &#x60;status.state&#x60; attribute will be &#x60;provisioning&#x60;. When the cluster is ready, this will transition to &#x60;running&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesCreateNewClusterResponse> executeWithHttpInfo() throws ApiException {
            Cluster cluster = buildBodyParams();
            return createNewClusterWithHttpInfo(cluster);
        }

        /**
         * Execute createNewCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  The IP address and cluster API server endpoint will not be available until the cluster has finished provisioning. The initial value of the cluster&#39;s &#x60;status.state&#x60; attribute will be &#x60;provisioning&#x60;. When the cluster is ready, this will transition to &#x60;running&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesCreateNewClusterResponse> _callback) throws ApiException {
            Cluster cluster = buildBodyParams();
            return createNewClusterAsync(cluster, _callback);
        }
    }

    /**
     * Create a New Kubernetes Cluster
     * To create a new Kubernetes cluster, send a POST request to &#x60;/v2/kubernetes/clusters&#x60;. The request must contain at least one node pool with at least one worker.  The request may contain a maintenance window policy describing a time period when disruptive maintenance tasks may be carried out. Omitting the policy implies that a window will be chosen automatically. See [here](https://www.digitalocean.com/docs/kubernetes/how-to/upgrade-cluster/) for details. 
     * @param cluster  (required)
     * @return CreateNewClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 201 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  The IP address and cluster API server endpoint will not be available until the cluster has finished provisioning. The initial value of the cluster&#39;s &#x60;status.state&#x60; attribute will be &#x60;provisioning&#x60;. When the cluster is ready, this will transition to &#x60;running&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public CreateNewClusterRequestBuilder createNewCluster(String version, String name, String region, List<KubernetesNodePool> nodePools) throws IllegalArgumentException {
        if (version == null) throw new IllegalArgumentException("\"version\" is required but got null");
            

        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (region == null) throw new IllegalArgumentException("\"region\" is required but got null");
            

        if (nodePools == null) throw new IllegalArgumentException("\"nodePools\" is required but got null");
        return new CreateNewClusterRequestBuilder(version, name, region, nodePools);
    }
    private okhttp3.Call deleteClusterCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call deleteClusterValidateBeforeCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling deleteCluster(Async)");
        }

        return deleteClusterCall(clusterId, _callback);

    }


    private ApiResponse<Void> deleteClusterWithHttpInfo(UUID clusterId) throws ApiException {
        okhttp3.Call localVarCall = deleteClusterValidateBeforeCall(clusterId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteClusterAsync(UUID clusterId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteClusterValidateBeforeCall(clusterId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteClusterRequestBuilder {
        private final UUID clusterId;

        private DeleteClusterRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Build call for deleteCluster
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
            return deleteClusterCall(clusterId, _callback);
        }


        /**
         * Execute deleteCluster request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteClusterWithHttpInfo(clusterId);
        }

        /**
         * Execute deleteCluster request with HTTP info returned
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
            return deleteClusterWithHttpInfo(clusterId);
        }

        /**
         * Execute deleteCluster request (asynchronously)
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
            return deleteClusterAsync(clusterId, _callback);
        }
    }

    /**
     * Delete a Kubernetes Cluster
     * To delete a Kubernetes cluster and all services deployed to it, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID&#x60;.  A 204 status code with no body will be returned in response to a successful request. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return DeleteClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteClusterRequestBuilder deleteCluster(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new DeleteClusterRequestBuilder(clusterId);
    }
    private okhttp3.Call deleteClusterAssociatedResourcesDangerousCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources/dangerous"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call deleteClusterAssociatedResourcesDangerousValidateBeforeCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling deleteClusterAssociatedResourcesDangerous(Async)");
        }

        return deleteClusterAssociatedResourcesDangerousCall(clusterId, _callback);

    }


    private ApiResponse<Void> deleteClusterAssociatedResourcesDangerousWithHttpInfo(UUID clusterId) throws ApiException {
        okhttp3.Call localVarCall = deleteClusterAssociatedResourcesDangerousValidateBeforeCall(clusterId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteClusterAssociatedResourcesDangerousAsync(UUID clusterId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteClusterAssociatedResourcesDangerousValidateBeforeCall(clusterId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteClusterAssociatedResourcesDangerousRequestBuilder {
        private final UUID clusterId;

        private DeleteClusterAssociatedResourcesDangerousRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Build call for deleteClusterAssociatedResourcesDangerous
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
            return deleteClusterAssociatedResourcesDangerousCall(clusterId, _callback);
        }


        /**
         * Execute deleteClusterAssociatedResourcesDangerous request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteClusterAssociatedResourcesDangerousWithHttpInfo(clusterId);
        }

        /**
         * Execute deleteClusterAssociatedResourcesDangerous request with HTTP info returned
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
            return deleteClusterAssociatedResourcesDangerousWithHttpInfo(clusterId);
        }

        /**
         * Execute deleteClusterAssociatedResourcesDangerous request (asynchronously)
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
            return deleteClusterAssociatedResourcesDangerousAsync(clusterId, _callback);
        }
    }

    /**
     * Delete a Cluster and All of its Associated Resources (Dangerous)
     * To delete a Kubernetes cluster with all of its associated resources, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/destroy_with_associated_resources/dangerous&#x60;. A 204 status code with no body will be returned in response to a successful request. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return DeleteClusterAssociatedResourcesDangerousRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteClusterAssociatedResourcesDangerousRequestBuilder deleteClusterAssociatedResourcesDangerous(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new DeleteClusterAssociatedResourcesDangerousRequestBuilder(clusterId);
    }
    private okhttp3.Call deleteNodeInNodePoolCall(UUID clusterId, UUID nodePoolId, UUID nodeId, Integer skipDrain, Integer replace, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}/nodes/{node_id}"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()))
            .replace("{" + "node_pool_id" + "}", localVarApiClient.escapeString(nodePoolId.toString()))
            .replace("{" + "node_id" + "}", localVarApiClient.escapeString(nodeId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (skipDrain != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("skip_drain", skipDrain));
        }

        if (replace != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("replace", replace));
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
    private okhttp3.Call deleteNodeInNodePoolValidateBeforeCall(UUID clusterId, UUID nodePoolId, UUID nodeId, Integer skipDrain, Integer replace, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling deleteNodeInNodePool(Async)");
        }

        // verify the required parameter 'nodePoolId' is set
        if (nodePoolId == null) {
            throw new ApiException("Missing the required parameter 'nodePoolId' when calling deleteNodeInNodePool(Async)");
        }

        // verify the required parameter 'nodeId' is set
        if (nodeId == null) {
            throw new ApiException("Missing the required parameter 'nodeId' when calling deleteNodeInNodePool(Async)");
        }

        return deleteNodeInNodePoolCall(clusterId, nodePoolId, nodeId, skipDrain, replace, _callback);

    }


    private ApiResponse<Void> deleteNodeInNodePoolWithHttpInfo(UUID clusterId, UUID nodePoolId, UUID nodeId, Integer skipDrain, Integer replace) throws ApiException {
        okhttp3.Call localVarCall = deleteNodeInNodePoolValidateBeforeCall(clusterId, nodePoolId, nodeId, skipDrain, replace, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteNodeInNodePoolAsync(UUID clusterId, UUID nodePoolId, UUID nodeId, Integer skipDrain, Integer replace, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteNodeInNodePoolValidateBeforeCall(clusterId, nodePoolId, nodeId, skipDrain, replace, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteNodeInNodePoolRequestBuilder {
        private final UUID clusterId;
        private final UUID nodePoolId;
        private final UUID nodeId;
        private Integer skipDrain;
        private Integer replace;

        private DeleteNodeInNodePoolRequestBuilder(UUID clusterId, UUID nodePoolId, UUID nodeId) {
            this.clusterId = clusterId;
            this.nodePoolId = nodePoolId;
            this.nodeId = nodeId;
        }

        /**
         * Set skipDrain
         * @param skipDrain Specifies whether or not to drain workloads from a node before it is deleted. Setting it to &#x60;1&#x60; causes node draining to be skipped. Omitting the query parameter or setting its value to &#x60;0&#x60; carries out draining prior to deletion. (optional, default to 0)
         * @return DeleteNodeInNodePoolRequestBuilder
         */
        public DeleteNodeInNodePoolRequestBuilder skipDrain(Integer skipDrain) {
            this.skipDrain = skipDrain;
            return this;
        }
        
        /**
         * Set replace
         * @param replace Specifies whether or not to replace a node after it has been deleted. Setting it to &#x60;1&#x60; causes the node to be replaced by a new one after deletion. Omitting the query parameter or setting its value to &#x60;0&#x60; deletes without replacement. (optional, default to 0)
         * @return DeleteNodeInNodePoolRequestBuilder
         */
        public DeleteNodeInNodePoolRequestBuilder replace(Integer replace) {
            this.replace = replace;
            return this;
        }
        
        /**
         * Build call for deleteNodeInNodePool
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
            return deleteNodeInNodePoolCall(clusterId, nodePoolId, nodeId, skipDrain, replace, _callback);
        }


        /**
         * Execute deleteNodeInNodePool request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteNodeInNodePoolWithHttpInfo(clusterId, nodePoolId, nodeId, skipDrain, replace);
        }

        /**
         * Execute deleteNodeInNodePool request with HTTP info returned
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
            return deleteNodeInNodePoolWithHttpInfo(clusterId, nodePoolId, nodeId, skipDrain, replace);
        }

        /**
         * Execute deleteNodeInNodePool request (asynchronously)
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
            return deleteNodeInNodePoolAsync(clusterId, nodePoolId, nodeId, skipDrain, replace, _callback);
        }
    }

    /**
     * Delete a Node in a Kubernetes Cluster
     * To delete a single node in a pool, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID/nodes/$NODE_ID&#x60;.  Appending the &#x60;skip_drain&#x3D;1&#x60; query parameter to the request causes node draining to be skipped. Omitting the query parameter or setting its value to &#x60;0&#x60; carries out draining prior to deletion.  Appending the &#x60;replace&#x3D;1&#x60; query parameter to the request causes the node to be replaced by a new one after deletion. Omitting the query parameter or setting its value to &#x60;0&#x60; deletes without replacement. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param nodePoolId A unique ID that can be used to reference a Kubernetes node pool. (required)
     * @param nodeId A unique ID that can be used to reference a node in a Kubernetes node pool. (required)
     * @return DeleteNodeInNodePoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteNodeInNodePoolRequestBuilder deleteNodeInNodePool(UUID clusterId, UUID nodePoolId, UUID nodeId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        if (nodePoolId == null) throw new IllegalArgumentException("\"nodePoolId\" is required but got null");
            

        if (nodeId == null) throw new IllegalArgumentException("\"nodeId\" is required but got null");
            

        return new DeleteNodeInNodePoolRequestBuilder(clusterId, nodePoolId, nodeId);
    }
    private okhttp3.Call deleteNodePoolCall(UUID clusterId, UUID nodePoolId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()))
            .replace("{" + "node_pool_id" + "}", localVarApiClient.escapeString(nodePoolId.toString()));

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
    private okhttp3.Call deleteNodePoolValidateBeforeCall(UUID clusterId, UUID nodePoolId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling deleteNodePool(Async)");
        }

        // verify the required parameter 'nodePoolId' is set
        if (nodePoolId == null) {
            throw new ApiException("Missing the required parameter 'nodePoolId' when calling deleteNodePool(Async)");
        }

        return deleteNodePoolCall(clusterId, nodePoolId, _callback);

    }


    private ApiResponse<Void> deleteNodePoolWithHttpInfo(UUID clusterId, UUID nodePoolId) throws ApiException {
        okhttp3.Call localVarCall = deleteNodePoolValidateBeforeCall(clusterId, nodePoolId, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call deleteNodePoolAsync(UUID clusterId, UUID nodePoolId, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = deleteNodePoolValidateBeforeCall(clusterId, nodePoolId, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class DeleteNodePoolRequestBuilder {
        private final UUID clusterId;
        private final UUID nodePoolId;

        private DeleteNodePoolRequestBuilder(UUID clusterId, UUID nodePoolId) {
            this.clusterId = clusterId;
            this.nodePoolId = nodePoolId;
        }

        /**
         * Build call for deleteNodePool
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
            return deleteNodePoolCall(clusterId, nodePoolId, _callback);
        }


        /**
         * Execute deleteNodePool request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            deleteNodePoolWithHttpInfo(clusterId, nodePoolId);
        }

        /**
         * Execute deleteNodePool request with HTTP info returned
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
            return deleteNodePoolWithHttpInfo(clusterId, nodePoolId);
        }

        /**
         * Execute deleteNodePool request (asynchronously)
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
            return deleteNodePoolAsync(clusterId, nodePoolId, _callback);
        }
    }

    /**
     * Delete a Node Pool in a Kubernetes Cluster
     * To delete a node pool, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID&#x60;.  A 204 status code with no body will be returned in response to a successful request. Nodes in the pool will subsequently be drained and deleted. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param nodePoolId A unique ID that can be used to reference a Kubernetes node pool. (required)
     * @return DeleteNodePoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public DeleteNodePoolRequestBuilder deleteNodePool(UUID clusterId, UUID nodePoolId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        if (nodePoolId == null) throw new IllegalArgumentException("\"nodePoolId\" is required but got null");
            

        return new DeleteNodePoolRequestBuilder(clusterId, nodePoolId);
    }
    private okhttp3.Call getAvailableUpgradesCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/upgrades"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call getAvailableUpgradesValidateBeforeCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling getAvailableUpgrades(Async)");
        }

        return getAvailableUpgradesCall(clusterId, _callback);

    }


    private ApiResponse<KubernetesGetAvailableUpgradesResponse> getAvailableUpgradesWithHttpInfo(UUID clusterId) throws ApiException {
        okhttp3.Call localVarCall = getAvailableUpgradesValidateBeforeCall(clusterId, null);
        Type localVarReturnType = new TypeToken<KubernetesGetAvailableUpgradesResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getAvailableUpgradesAsync(UUID clusterId, final ApiCallback<KubernetesGetAvailableUpgradesResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getAvailableUpgradesValidateBeforeCall(clusterId, _callback);
        Type localVarReturnType = new TypeToken<KubernetesGetAvailableUpgradesResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetAvailableUpgradesRequestBuilder {
        private final UUID clusterId;

        private GetAvailableUpgradesRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Build call for getAvailableUpgrades
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;available_upgrade_versions&#x60;. The value of this will be an array of objects, representing the upgrade versions currently available for this cluster.  If the cluster is up-to-date (i.e. there are no upgrades currently available) &#x60;available_upgrade_versions&#x60; will be &#x60;null&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getAvailableUpgradesCall(clusterId, _callback);
        }


        /**
         * Execute getAvailableUpgrades request
         * @return KubernetesGetAvailableUpgradesResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;available_upgrade_versions&#x60;. The value of this will be an array of objects, representing the upgrade versions currently available for this cluster.  If the cluster is up-to-date (i.e. there are no upgrades currently available) &#x60;available_upgrade_versions&#x60; will be &#x60;null&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesGetAvailableUpgradesResponse execute() throws ApiException {
            ApiResponse<KubernetesGetAvailableUpgradesResponse> localVarResp = getAvailableUpgradesWithHttpInfo(clusterId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getAvailableUpgrades request with HTTP info returned
         * @return ApiResponse&lt;KubernetesGetAvailableUpgradesResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;available_upgrade_versions&#x60;. The value of this will be an array of objects, representing the upgrade versions currently available for this cluster.  If the cluster is up-to-date (i.e. there are no upgrades currently available) &#x60;available_upgrade_versions&#x60; will be &#x60;null&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesGetAvailableUpgradesResponse> executeWithHttpInfo() throws ApiException {
            return getAvailableUpgradesWithHttpInfo(clusterId);
        }

        /**
         * Execute getAvailableUpgrades request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;available_upgrade_versions&#x60;. The value of this will be an array of objects, representing the upgrade versions currently available for this cluster.  If the cluster is up-to-date (i.e. there are no upgrades currently available) &#x60;available_upgrade_versions&#x60; will be &#x60;null&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesGetAvailableUpgradesResponse> _callback) throws ApiException {
            return getAvailableUpgradesAsync(clusterId, _callback);
        }
    }

    /**
     * Retrieve Available Upgrades for an Existing Kubernetes Cluster
     * To determine whether a cluster can be upgraded, and the versions to which it can be upgraded, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/upgrades&#x60;. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return GetAvailableUpgradesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;available_upgrade_versions&#x60;. The value of this will be an array of objects, representing the upgrade versions currently available for this cluster.  If the cluster is up-to-date (i.e. there are no upgrades currently available) &#x60;available_upgrade_versions&#x60; will be &#x60;null&#x60;.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetAvailableUpgradesRequestBuilder getAvailableUpgrades(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new GetAvailableUpgradesRequestBuilder(clusterId);
    }
    private okhttp3.Call getClusterInfoCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call getClusterInfoValidateBeforeCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling getClusterInfo(Async)");
        }

        return getClusterInfoCall(clusterId, _callback);

    }


    private ApiResponse<KubernetesGetClusterInfoResponse> getClusterInfoWithHttpInfo(UUID clusterId) throws ApiException {
        okhttp3.Call localVarCall = getClusterInfoValidateBeforeCall(clusterId, null);
        Type localVarReturnType = new TypeToken<KubernetesGetClusterInfoResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getClusterInfoAsync(UUID clusterId, final ApiCallback<KubernetesGetClusterInfoResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getClusterInfoValidateBeforeCall(clusterId, _callback);
        Type localVarReturnType = new TypeToken<KubernetesGetClusterInfoResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetClusterInfoRequestBuilder {
        private final UUID clusterId;

        private GetClusterInfoRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Build call for getClusterInfo
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getClusterInfoCall(clusterId, _callback);
        }


        /**
         * Execute getClusterInfo request
         * @return KubernetesGetClusterInfoResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesGetClusterInfoResponse execute() throws ApiException {
            ApiResponse<KubernetesGetClusterInfoResponse> localVarResp = getClusterInfoWithHttpInfo(clusterId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getClusterInfo request with HTTP info returned
         * @return ApiResponse&lt;KubernetesGetClusterInfoResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesGetClusterInfoResponse> executeWithHttpInfo() throws ApiException {
            return getClusterInfoWithHttpInfo(clusterId);
        }

        /**
         * Execute getClusterInfo request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesGetClusterInfoResponse> _callback) throws ApiException {
            return getClusterInfoAsync(clusterId, _callback);
        }
    }

    /**
     * Retrieve an Existing Kubernetes Cluster
     * To show information about an existing Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID&#x60;. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return GetClusterInfoRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetClusterInfoRequestBuilder getClusterInfo(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new GetClusterInfoRequestBuilder(clusterId);
    }
    private okhttp3.Call getClusterLintDiagnosticsCall(UUID clusterId, UUID runId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/clusterlint"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (runId != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("run_id", runId));
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
    private okhttp3.Call getClusterLintDiagnosticsValidateBeforeCall(UUID clusterId, UUID runId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling getClusterLintDiagnostics(Async)");
        }

        return getClusterLintDiagnosticsCall(clusterId, runId, _callback);

    }


    private ApiResponse<ClusterlintResults> getClusterLintDiagnosticsWithHttpInfo(UUID clusterId, UUID runId) throws ApiException {
        okhttp3.Call localVarCall = getClusterLintDiagnosticsValidateBeforeCall(clusterId, runId, null);
        Type localVarReturnType = new TypeToken<ClusterlintResults>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getClusterLintDiagnosticsAsync(UUID clusterId, UUID runId, final ApiCallback<ClusterlintResults> _callback) throws ApiException {

        okhttp3.Call localVarCall = getClusterLintDiagnosticsValidateBeforeCall(clusterId, runId, _callback);
        Type localVarReturnType = new TypeToken<ClusterlintResults>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetClusterLintDiagnosticsRequestBuilder {
        private final UUID clusterId;
        private UUID runId;

        private GetClusterLintDiagnosticsRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Set runId
         * @param runId Specifies the clusterlint run whose results will be retrieved. (optional)
         * @return GetClusterLintDiagnosticsRequestBuilder
         */
        public GetClusterLintDiagnosticsRequestBuilder runId(UUID runId) {
            this.runId = runId;
            return this;
        }
        
        /**
         * Build call for getClusterLintDiagnostics
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response is a JSON object which contains the diagnostics on Kubernetes objects in the cluster. Each diagnostic will contain some metadata information about the object and feedback for users to act upon.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getClusterLintDiagnosticsCall(clusterId, runId, _callback);
        }


        /**
         * Execute getClusterLintDiagnostics request
         * @return ClusterlintResults
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response is a JSON object which contains the diagnostics on Kubernetes objects in the cluster. Each diagnostic will contain some metadata information about the object and feedback for users to act upon.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ClusterlintResults execute() throws ApiException {
            ApiResponse<ClusterlintResults> localVarResp = getClusterLintDiagnosticsWithHttpInfo(clusterId, runId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getClusterLintDiagnostics request with HTTP info returned
         * @return ApiResponse&lt;ClusterlintResults&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response is a JSON object which contains the diagnostics on Kubernetes objects in the cluster. Each diagnostic will contain some metadata information about the object and feedback for users to act upon.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<ClusterlintResults> executeWithHttpInfo() throws ApiException {
            return getClusterLintDiagnosticsWithHttpInfo(clusterId, runId);
        }

        /**
         * Execute getClusterLintDiagnostics request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response is a JSON object which contains the diagnostics on Kubernetes objects in the cluster. Each diagnostic will contain some metadata information about the object and feedback for users to act upon.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<ClusterlintResults> _callback) throws ApiException {
            return getClusterLintDiagnosticsAsync(clusterId, runId, _callback);
        }
    }

    /**
     * Fetch Clusterlint Diagnostics for a Kubernetes Cluster
     * To request clusterlint diagnostics for your cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/clusterlint&#x60;. If the &#x60;run_id&#x60; query parameter is provided, then the diagnostics for the specific run is fetched. By default, the latest results are shown.  To find out how to address clusterlint feedback, please refer to [the clusterlint check documentation](https://github.com/digitalocean/clusterlint/blob/master/checks.md). 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return GetClusterLintDiagnosticsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response is a JSON object which contains the diagnostics on Kubernetes objects in the cluster. Each diagnostic will contain some metadata information about the object and feedback for users to act upon.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetClusterLintDiagnosticsRequestBuilder getClusterLintDiagnostics(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new GetClusterLintDiagnosticsRequestBuilder(clusterId);
    }
    private okhttp3.Call getCredentialsByClusterIdCall(UUID clusterId, Integer expirySeconds, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/credentials"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (expirySeconds != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("expiry_seconds", expirySeconds));
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
    private okhttp3.Call getCredentialsByClusterIdValidateBeforeCall(UUID clusterId, Integer expirySeconds, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling getCredentialsByClusterId(Async)");
        }

        return getCredentialsByClusterIdCall(clusterId, expirySeconds, _callback);

    }


    private ApiResponse<Credentials> getCredentialsByClusterIdWithHttpInfo(UUID clusterId, Integer expirySeconds) throws ApiException {
        okhttp3.Call localVarCall = getCredentialsByClusterIdValidateBeforeCall(clusterId, expirySeconds, null);
        Type localVarReturnType = new TypeToken<Credentials>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getCredentialsByClusterIdAsync(UUID clusterId, Integer expirySeconds, final ApiCallback<Credentials> _callback) throws ApiException {

        okhttp3.Call localVarCall = getCredentialsByClusterIdValidateBeforeCall(clusterId, expirySeconds, _callback);
        Type localVarReturnType = new TypeToken<Credentials>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetCredentialsByClusterIdRequestBuilder {
        private final UUID clusterId;
        private Integer expirySeconds;

        private GetCredentialsByClusterIdRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Set expirySeconds
         * @param expirySeconds The duration in seconds that the returned Kubernetes credentials will be valid. If not set or 0, the credentials will have a 7 day expiry. (optional, default to 0)
         * @return GetCredentialsByClusterIdRequestBuilder
         */
        public GetCredentialsByClusterIdRequestBuilder expirySeconds(Integer expirySeconds) {
            this.expirySeconds = expirySeconds;
            return this;
        }
        
        /**
         * Build call for getCredentialsByClusterId
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing credentials for a cluster. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getCredentialsByClusterIdCall(clusterId, expirySeconds, _callback);
        }


        /**
         * Execute getCredentialsByClusterId request
         * @return Credentials
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing credentials for a cluster. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Credentials execute() throws ApiException {
            ApiResponse<Credentials> localVarResp = getCredentialsByClusterIdWithHttpInfo(clusterId, expirySeconds);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getCredentialsByClusterId request with HTTP info returned
         * @return ApiResponse&lt;Credentials&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing credentials for a cluster. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Credentials> executeWithHttpInfo() throws ApiException {
            return getCredentialsByClusterIdWithHttpInfo(clusterId, expirySeconds);
        }

        /**
         * Execute getCredentialsByClusterId request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A JSON object containing credentials for a cluster. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Credentials> _callback) throws ApiException {
            return getCredentialsByClusterIdAsync(clusterId, expirySeconds, _callback);
        }
    }

    /**
     * Retrieve Credentials for a Kubernetes Cluster
     * This endpoint returns a JSON object . It can be used to programmatically construct Kubernetes clients which cannot parse kubeconfig files.  The resulting JSON object contains token-based authentication for clusters supporting it, and certificate-based authentication otherwise. For a list of supported versions and more information, see \&quot;[How to Connect to a DigitalOcean Kubernetes Cluster with kubectl](https://www.digitalocean.com/docs/kubernetes/how-to/connect-with-kubectl/)\&quot;.  To retrieve credentials for accessing a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/credentials&#x60;.  Clusters supporting token-based authentication may define an expiration by passing a duration in seconds as a query parameter to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/credentials?expiry_seconds&#x3D;$DURATION_IN_SECONDS&#x60;. If not set or 0, then the token will have a 7 day expiry. The query parameter has no impact in certificate-based authentication. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return GetCredentialsByClusterIdRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A JSON object containing credentials for a cluster. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetCredentialsByClusterIdRequestBuilder getCredentialsByClusterId(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new GetCredentialsByClusterIdRequestBuilder(clusterId);
    }
    private okhttp3.Call getKubeconfigCall(UUID clusterId, Integer expirySeconds, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/kubeconfig"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, String> localVarCookieParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        if (expirySeconds != null) {
            localVarQueryParams.addAll(localVarApiClient.parameterToPair("expiry_seconds", expirySeconds));
        }

        final String[] localVarAccepts = {
            "application/yaml",
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
    private okhttp3.Call getKubeconfigValidateBeforeCall(UUID clusterId, Integer expirySeconds, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling getKubeconfig(Async)");
        }

        return getKubeconfigCall(clusterId, expirySeconds, _callback);

    }


    private ApiResponse<Void> getKubeconfigWithHttpInfo(UUID clusterId, Integer expirySeconds) throws ApiException {
        okhttp3.Call localVarCall = getKubeconfigValidateBeforeCall(clusterId, expirySeconds, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call getKubeconfigAsync(UUID clusterId, Integer expirySeconds, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = getKubeconfigValidateBeforeCall(clusterId, expirySeconds, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class GetKubeconfigRequestBuilder {
        private final UUID clusterId;
        private Integer expirySeconds;

        private GetKubeconfigRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Set expirySeconds
         * @param expirySeconds The duration in seconds that the returned Kubernetes credentials will be valid. If not set or 0, the credentials will have a 7 day expiry. (optional, default to 0)
         * @return GetKubeconfigRequestBuilder
         */
        public GetKubeconfigRequestBuilder expirySeconds(Integer expirySeconds) {
            this.expirySeconds = expirySeconds;
            return this;
        }
        
        /**
         * Build call for getKubeconfig
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A kubeconfig file for the cluster in YAML format. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getKubeconfigCall(clusterId, expirySeconds, _callback);
        }


        /**
         * Execute getKubeconfig request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A kubeconfig file for the cluster in YAML format. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            getKubeconfigWithHttpInfo(clusterId, expirySeconds);
        }

        /**
         * Execute getKubeconfig request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A kubeconfig file for the cluster in YAML format. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            return getKubeconfigWithHttpInfo(clusterId, expirySeconds);
        }

        /**
         * Execute getKubeconfig request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> A kubeconfig file for the cluster in YAML format. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            return getKubeconfigAsync(clusterId, expirySeconds, _callback);
        }
    }

    /**
     * Retrieve the kubeconfig for a Kubernetes Cluster
     * This endpoint returns a kubeconfig file in YAML format. It can be used to connect to and administer the cluster using the Kubernetes command line tool, &#x60;kubectl&#x60;, or other programs supporting kubeconfig files (e.g., client libraries).  The resulting kubeconfig file uses token-based authentication for clusters supporting it, and certificate-based authentication otherwise. For a list of supported versions and more information, see \&quot;[How to Connect to a DigitalOcean Kubernetes Cluster with kubectl](https://www.digitalocean.com/docs/kubernetes/how-to/connect-with-kubectl/)\&quot;.  To retrieve a kubeconfig file for use with a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/kubeconfig&#x60;.  Clusters supporting token-based authentication may define an expiration by passing a duration in seconds as a query parameter to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/kubeconfig?expiry_seconds&#x3D;$DURATION_IN_SECONDS&#x60;. If not set or 0, then the token will have a 7 day expiry. The query parameter has no impact in certificate-based authentication. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return GetKubeconfigRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> A kubeconfig file for the cluster in YAML format. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetKubeconfigRequestBuilder getKubeconfig(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new GetKubeconfigRequestBuilder(clusterId);
    }
    private okhttp3.Call getNodePoolCall(UUID clusterId, UUID nodePoolId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()))
            .replace("{" + "node_pool_id" + "}", localVarApiClient.escapeString(nodePoolId.toString()));

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
    private okhttp3.Call getNodePoolValidateBeforeCall(UUID clusterId, UUID nodePoolId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling getNodePool(Async)");
        }

        // verify the required parameter 'nodePoolId' is set
        if (nodePoolId == null) {
            throw new ApiException("Missing the required parameter 'nodePoolId' when calling getNodePool(Async)");
        }

        return getNodePoolCall(clusterId, nodePoolId, _callback);

    }


    private ApiResponse<KubernetesGetNodePoolResponse> getNodePoolWithHttpInfo(UUID clusterId, UUID nodePoolId) throws ApiException {
        okhttp3.Call localVarCall = getNodePoolValidateBeforeCall(clusterId, nodePoolId, null);
        Type localVarReturnType = new TypeToken<KubernetesGetNodePoolResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getNodePoolAsync(UUID clusterId, UUID nodePoolId, final ApiCallback<KubernetesGetNodePoolResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = getNodePoolValidateBeforeCall(clusterId, nodePoolId, _callback);
        Type localVarReturnType = new TypeToken<KubernetesGetNodePoolResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetNodePoolRequestBuilder {
        private final UUID clusterId;
        private final UUID nodePoolId;

        private GetNodePoolRequestBuilder(UUID clusterId, UUID nodePoolId) {
            this.clusterId = clusterId;
            this.nodePoolId = nodePoolId;
        }

        /**
         * Build call for getNodePool
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getNodePoolCall(clusterId, nodePoolId, _callback);
        }


        /**
         * Execute getNodePool request
         * @return KubernetesGetNodePoolResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesGetNodePoolResponse execute() throws ApiException {
            ApiResponse<KubernetesGetNodePoolResponse> localVarResp = getNodePoolWithHttpInfo(clusterId, nodePoolId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getNodePool request with HTTP info returned
         * @return ApiResponse&lt;KubernetesGetNodePoolResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesGetNodePoolResponse> executeWithHttpInfo() throws ApiException {
            return getNodePoolWithHttpInfo(clusterId, nodePoolId);
        }

        /**
         * Execute getNodePool request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesGetNodePoolResponse> _callback) throws ApiException {
            return getNodePoolAsync(clusterId, nodePoolId, _callback);
        }
    }

    /**
     * Retrieve a Node Pool for a Kubernetes Cluster
     * To show information about a specific node pool in a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID&#x60;. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param nodePoolId A unique ID that can be used to reference a Kubernetes node pool. (required)
     * @return GetNodePoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetNodePoolRequestBuilder getNodePool(UUID clusterId, UUID nodePoolId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        if (nodePoolId == null) throw new IllegalArgumentException("\"nodePoolId\" is required but got null");
            

        return new GetNodePoolRequestBuilder(clusterId, nodePoolId);
    }
    private okhttp3.Call getUserInformationCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/user"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call getUserInformationValidateBeforeCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling getUserInformation(Async)");
        }

        return getUserInformationCall(clusterId, _callback);

    }


    private ApiResponse<User> getUserInformationWithHttpInfo(UUID clusterId) throws ApiException {
        okhttp3.Call localVarCall = getUserInformationValidateBeforeCall(clusterId, null);
        Type localVarReturnType = new TypeToken<User>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call getUserInformationAsync(UUID clusterId, final ApiCallback<User> _callback) throws ApiException {

        okhttp3.Call localVarCall = getUserInformationValidateBeforeCall(clusterId, _callback);
        Type localVarReturnType = new TypeToken<User>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class GetUserInformationRequestBuilder {
        private final UUID clusterId;

        private GetUserInformationRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Build call for getUserInformation
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster_user&#x60; containing the username and in-cluster groups that it belongs to.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return getUserInformationCall(clusterId, _callback);
        }


        /**
         * Execute getUserInformation request
         * @return User
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster_user&#x60; containing the username and in-cluster groups that it belongs to.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public User execute() throws ApiException {
            ApiResponse<User> localVarResp = getUserInformationWithHttpInfo(clusterId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute getUserInformation request with HTTP info returned
         * @return ApiResponse&lt;User&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster_user&#x60; containing the username and in-cluster groups that it belongs to.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<User> executeWithHttpInfo() throws ApiException {
            return getUserInformationWithHttpInfo(clusterId);
        }

        /**
         * Execute getUserInformation request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster_user&#x60; containing the username and in-cluster groups that it belongs to.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<User> _callback) throws ApiException {
            return getUserInformationAsync(clusterId, _callback);
        }
    }

    /**
     * Retrieve User Information for a Kubernetes Cluster
     * To show information the user associated with a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/user&#x60;. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return GetUserInformationRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster_user&#x60; containing the username and in-cluster groups that it belongs to.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public GetUserInformationRequestBuilder getUserInformation(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new GetUserInformationRequestBuilder(clusterId);
    }
    private okhttp3.Call listAssociatedResourcesCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call listAssociatedResourcesValidateBeforeCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling listAssociatedResources(Async)");
        }

        return listAssociatedResourcesCall(clusterId, _callback);

    }


    private ApiResponse<AssociatedKubernetesResources> listAssociatedResourcesWithHttpInfo(UUID clusterId) throws ApiException {
        okhttp3.Call localVarCall = listAssociatedResourcesValidateBeforeCall(clusterId, null);
        Type localVarReturnType = new TypeToken<AssociatedKubernetesResources>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listAssociatedResourcesAsync(UUID clusterId, final ApiCallback<AssociatedKubernetesResources> _callback) throws ApiException {

        okhttp3.Call localVarCall = listAssociatedResourcesValidateBeforeCall(clusterId, _callback);
        Type localVarReturnType = new TypeToken<AssociatedKubernetesResources>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListAssociatedResourcesRequestBuilder {
        private final UUID clusterId;

        private ListAssociatedResourcesRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Build call for listAssociatedResources
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object containing &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing the standard attributes for associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listAssociatedResourcesCall(clusterId, _callback);
        }


        /**
         * Execute listAssociatedResources request
         * @return AssociatedKubernetesResources
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object containing &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing the standard attributes for associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public AssociatedKubernetesResources execute() throws ApiException {
            ApiResponse<AssociatedKubernetesResources> localVarResp = listAssociatedResourcesWithHttpInfo(clusterId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listAssociatedResources request with HTTP info returned
         * @return ApiResponse&lt;AssociatedKubernetesResources&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object containing &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing the standard attributes for associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<AssociatedKubernetesResources> executeWithHttpInfo() throws ApiException {
            return listAssociatedResourcesWithHttpInfo(clusterId);
        }

        /**
         * Execute listAssociatedResources request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object containing &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing the standard attributes for associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<AssociatedKubernetesResources> _callback) throws ApiException {
            return listAssociatedResourcesAsync(clusterId, _callback);
        }
    }

    /**
     * List Associated Resources for Cluster Deletion
     * To list the associated billable resources that can be destroyed along with a cluster, send a GET request to the &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/destroy_with_associated_resources&#x60; endpoint.
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return ListAssociatedResourcesRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object containing &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing the standard attributes for associated resources. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListAssociatedResourcesRequestBuilder listAssociatedResources(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new ListAssociatedResourcesRequestBuilder(clusterId);
    }
    private okhttp3.Call listClustersCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters";

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
    private okhttp3.Call listClustersValidateBeforeCall(Integer perPage, Integer page, final ApiCallback _callback) throws ApiException {
        return listClustersCall(perPage, page, _callback);

    }


    private ApiResponse<KubernetesListClustersResponse> listClustersWithHttpInfo(Integer perPage, Integer page) throws ApiException {
        okhttp3.Call localVarCall = listClustersValidateBeforeCall(perPage, page, null);
        Type localVarReturnType = new TypeToken<KubernetesListClustersResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listClustersAsync(Integer perPage, Integer page, final ApiCallback<KubernetesListClustersResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listClustersValidateBeforeCall(perPage, page, _callback);
        Type localVarReturnType = new TypeToken<KubernetesListClustersResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListClustersRequestBuilder {
        private Integer perPage;
        private Integer page;

        private ListClustersRequestBuilder() {
        }

        /**
         * Set perPage
         * @param perPage Number of items returned per page (optional, default to 20)
         * @return ListClustersRequestBuilder
         */
        public ListClustersRequestBuilder perPage(Integer perPage) {
            this.perPage = perPage;
            return this;
        }
        
        /**
         * Set page
         * @param page Which &#39;page&#39; of paginated results to return. (optional, default to 1)
         * @return ListClustersRequestBuilder
         */
        public ListClustersRequestBuilder page(Integer page) {
            this.page = page;
            return this;
        }
        
        /**
         * Build call for listClusters
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_clusters&#x60;. This will be set to an array of objects, each of which will contain the standard Kubernetes cluster attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listClustersCall(perPage, page, _callback);
        }


        /**
         * Execute listClusters request
         * @return KubernetesListClustersResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_clusters&#x60;. This will be set to an array of objects, each of which will contain the standard Kubernetes cluster attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesListClustersResponse execute() throws ApiException {
            ApiResponse<KubernetesListClustersResponse> localVarResp = listClustersWithHttpInfo(perPage, page);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listClusters request with HTTP info returned
         * @return ApiResponse&lt;KubernetesListClustersResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_clusters&#x60;. This will be set to an array of objects, each of which will contain the standard Kubernetes cluster attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesListClustersResponse> executeWithHttpInfo() throws ApiException {
            return listClustersWithHttpInfo(perPage, page);
        }

        /**
         * Execute listClusters request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_clusters&#x60;. This will be set to an array of objects, each of which will contain the standard Kubernetes cluster attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesListClustersResponse> _callback) throws ApiException {
            return listClustersAsync(perPage, page, _callback);
        }
    }

    /**
     * List All Kubernetes Clusters
     * To list all of the Kubernetes clusters on your account, send a GET request to &#x60;/v2/kubernetes/clusters&#x60;. 
     * @return ListClustersRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_clusters&#x60;. This will be set to an array of objects, each of which will contain the standard Kubernetes cluster attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListClustersRequestBuilder listClusters() throws IllegalArgumentException {
        return new ListClustersRequestBuilder();
    }
    private okhttp3.Call listNodePoolsCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
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
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/node_pools"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call listNodePoolsValidateBeforeCall(UUID clusterId, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling listNodePools(Async)");
        }

        return listNodePoolsCall(clusterId, _callback);

    }


    private ApiResponse<KubernetesListNodePoolsResponse> listNodePoolsWithHttpInfo(UUID clusterId) throws ApiException {
        okhttp3.Call localVarCall = listNodePoolsValidateBeforeCall(clusterId, null);
        Type localVarReturnType = new TypeToken<KubernetesListNodePoolsResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listNodePoolsAsync(UUID clusterId, final ApiCallback<KubernetesListNodePoolsResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = listNodePoolsValidateBeforeCall(clusterId, _callback);
        Type localVarReturnType = new TypeToken<KubernetesListNodePoolsResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class ListNodePoolsRequestBuilder {
        private final UUID clusterId;

        private ListNodePoolsRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Build call for listNodePools
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pools&#x60;. This will be set to an array of objects, each of which will contain the standard node pool attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listNodePoolsCall(clusterId, _callback);
        }


        /**
         * Execute listNodePools request
         * @return KubernetesListNodePoolsResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pools&#x60;. This will be set to an array of objects, each of which will contain the standard node pool attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesListNodePoolsResponse execute() throws ApiException {
            ApiResponse<KubernetesListNodePoolsResponse> localVarResp = listNodePoolsWithHttpInfo(clusterId);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listNodePools request with HTTP info returned
         * @return ApiResponse&lt;KubernetesListNodePoolsResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pools&#x60;. This will be set to an array of objects, each of which will contain the standard node pool attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesListNodePoolsResponse> executeWithHttpInfo() throws ApiException {
            return listNodePoolsWithHttpInfo(clusterId);
        }

        /**
         * Execute listNodePools request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pools&#x60;. This will be set to an array of objects, each of which will contain the standard node pool attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesListNodePoolsResponse> _callback) throws ApiException {
            return listNodePoolsAsync(clusterId, _callback);
        }
    }

    /**
     * List All Node Pools in a Kubernetes Clusters
     * To list all of the node pools in a Kubernetes clusters, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools&#x60;. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return ListNodePoolsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;node_pools&#x60;. This will be set to an array of objects, each of which will contain the standard node pool attributes.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListNodePoolsRequestBuilder listNodePools(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new ListNodePoolsRequestBuilder(clusterId);
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
        String localVarPath = "/v2/kubernetes/options";

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


    private ApiResponse<KubernetesOptions> listOptionsWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = listOptionsValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<KubernetesOptions>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call listOptionsAsync(final ApiCallback<KubernetesOptions> _callback) throws ApiException {

        okhttp3.Call localVarCall = listOptionsValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<KubernetesOptions>(){}.getType();
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
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains &#x60;regions&#x60;, &#x60;versions&#x60;, and &#x60;sizes&#x60; objects listing the available options and the matching slugs for use when creating a new cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            return listOptionsCall(_callback);
        }


        /**
         * Execute listOptions request
         * @return KubernetesOptions
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains &#x60;regions&#x60;, &#x60;versions&#x60;, and &#x60;sizes&#x60; objects listing the available options and the matching slugs for use when creating a new cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesOptions execute() throws ApiException {
            ApiResponse<KubernetesOptions> localVarResp = listOptionsWithHttpInfo();
            return localVarResp.getResponseBody();
        }

        /**
         * Execute listOptions request with HTTP info returned
         * @return ApiResponse&lt;KubernetesOptions&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains &#x60;regions&#x60;, &#x60;versions&#x60;, and &#x60;sizes&#x60; objects listing the available options and the matching slugs for use when creating a new cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesOptions> executeWithHttpInfo() throws ApiException {
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
            <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains &#x60;regions&#x60;, &#x60;versions&#x60;, and &#x60;sizes&#x60; objects listing the available options and the matching slugs for use when creating a new cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesOptions> _callback) throws ApiException {
            return listOptionsAsync(_callback);
        }
    }

    /**
     * List Available Regions, Node Sizes, and Versions of Kubernetes
     * To list the versions of Kubernetes available for use, the regions that support Kubernetes, and the available node sizes, send a GET request to &#x60;/v2/kubernetes/options&#x60;.
     * @return ListOptionsRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 200 </td><td> The response will be a JSON object with a key called &#x60;options&#x60; which contains &#x60;regions&#x60;, &#x60;versions&#x60;, and &#x60;sizes&#x60; objects listing the available options and the matching slugs for use when creating a new cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public ListOptionsRequestBuilder listOptions() throws IllegalArgumentException {
        return new ListOptionsRequestBuilder();
    }
    private okhttp3.Call recycleNodePoolCall(UUID clusterId, UUID nodePoolId, KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = kubernetesRecycleNodePoolRequest;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}/recycle"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()))
            .replace("{" + "node_pool_id" + "}", localVarApiClient.escapeString(nodePoolId.toString()));

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

    @Deprecated
    @SuppressWarnings("rawtypes")
    private okhttp3.Call recycleNodePoolValidateBeforeCall(UUID clusterId, UUID nodePoolId, KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling recycleNodePool(Async)");
        }

        // verify the required parameter 'nodePoolId' is set
        if (nodePoolId == null) {
            throw new ApiException("Missing the required parameter 'nodePoolId' when calling recycleNodePool(Async)");
        }

        // verify the required parameter 'kubernetesRecycleNodePoolRequest' is set
        if (kubernetesRecycleNodePoolRequest == null) {
            throw new ApiException("Missing the required parameter 'kubernetesRecycleNodePoolRequest' when calling recycleNodePool(Async)");
        }

        return recycleNodePoolCall(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest, _callback);

    }


    private ApiResponse<Void> recycleNodePoolWithHttpInfo(UUID clusterId, UUID nodePoolId, KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest) throws ApiException {
        okhttp3.Call localVarCall = recycleNodePoolValidateBeforeCall(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call recycleNodePoolAsync(UUID clusterId, UUID nodePoolId, KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = recycleNodePoolValidateBeforeCall(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RecycleNodePoolRequestBuilder {
        private final UUID clusterId;
        private final UUID nodePoolId;
        private List<String> nodes;

        private RecycleNodePoolRequestBuilder(UUID clusterId, UUID nodePoolId) {
            this.clusterId = clusterId;
            this.nodePoolId = nodePoolId;
        }

        /**
         * Set nodes
         * @param nodes  (optional)
         * @return RecycleNodePoolRequestBuilder
         */
        public RecycleNodePoolRequestBuilder nodes(List<String> nodes) {
            this.nodes = nodes;
            return this;
        }
        
        /**
         * Build call for recycleNodePool
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest = buildBodyParams();
            return recycleNodePoolCall(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest, _callback);
        }

        private KubernetesRecycleNodePoolRequest buildBodyParams() {
            KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest = new KubernetesRecycleNodePoolRequest();
            kubernetesRecycleNodePoolRequest.nodes(this.nodes);
            return kubernetesRecycleNodePoolRequest;
        }

        /**
         * Execute recycleNodePool request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public void execute() throws ApiException {
            KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest = buildBodyParams();
            recycleNodePoolWithHttpInfo(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest);
        }

        /**
         * Execute recycleNodePool request with HTTP info returned
         * @return ApiResponse&lt;Void&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public ApiResponse<Void> executeWithHttpInfo() throws ApiException {
            KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest = buildBodyParams();
            return recycleNodePoolWithHttpInfo(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest);
        }

        /**
         * Execute recycleNodePool request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         * @deprecated
         */
        @Deprecated
        public okhttp3.Call executeAsync(final ApiCallback<Void> _callback) throws ApiException {
            KubernetesRecycleNodePoolRequest kubernetesRecycleNodePoolRequest = buildBodyParams();
            return recycleNodePoolAsync(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest, _callback);
        }
    }

    /**
     * Recycle a Kubernetes Node Pool
     * The endpoint has been deprecated. Please use the DELETE &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID/nodes/$NODE_ID&#x60; method instead. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param nodePoolId A unique ID that can be used to reference a Kubernetes node pool. (required)
     * @param kubernetesRecycleNodePoolRequest  (required)
     * @return RecycleNodePoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     * @deprecated
     */
    @Deprecated
    public RecycleNodePoolRequestBuilder recycleNodePool(UUID clusterId, UUID nodePoolId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        if (nodePoolId == null) throw new IllegalArgumentException("\"nodePoolId\" is required but got null");
            

        return new RecycleNodePoolRequestBuilder(clusterId, nodePoolId);
    }
    private okhttp3.Call removeRegistryCall(ClusterRegistries clusterRegistries, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = clusterRegistries;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/registry";

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
    private okhttp3.Call removeRegistryValidateBeforeCall(ClusterRegistries clusterRegistries, final ApiCallback _callback) throws ApiException {
        return removeRegistryCall(clusterRegistries, _callback);

    }


    private ApiResponse<Void> removeRegistryWithHttpInfo(ClusterRegistries clusterRegistries) throws ApiException {
        okhttp3.Call localVarCall = removeRegistryValidateBeforeCall(clusterRegistries, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call removeRegistryAsync(ClusterRegistries clusterRegistries, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = removeRegistryValidateBeforeCall(clusterRegistries, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class RemoveRegistryRequestBuilder {
        private List<String> clusterUuids;

        private RemoveRegistryRequestBuilder() {
        }

        /**
         * Set clusterUuids
         * @param clusterUuids An array containing the UUIDs of Kubernetes clusters. (optional)
         * @return RemoveRegistryRequestBuilder
         */
        public RemoveRegistryRequestBuilder clusterUuids(List<String> clusterUuids) {
            this.clusterUuids = clusterUuids;
            return this;
        }
        
        /**
         * Build call for removeRegistry
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
            ClusterRegistries clusterRegistries = buildBodyParams();
            return removeRegistryCall(clusterRegistries, _callback);
        }

        private ClusterRegistries buildBodyParams() {
            ClusterRegistries clusterRegistries = new ClusterRegistries();
            clusterRegistries.clusterUuids(this.clusterUuids);
            return clusterRegistries;
        }

        /**
         * Execute removeRegistry request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            ClusterRegistries clusterRegistries = buildBodyParams();
            removeRegistryWithHttpInfo(clusterRegistries);
        }

        /**
         * Execute removeRegistry request with HTTP info returned
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
            ClusterRegistries clusterRegistries = buildBodyParams();
            return removeRegistryWithHttpInfo(clusterRegistries);
        }

        /**
         * Execute removeRegistry request (asynchronously)
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
            ClusterRegistries clusterRegistries = buildBodyParams();
            return removeRegistryAsync(clusterRegistries, _callback);
        }
    }

    /**
     * Remove Container Registry from Kubernetes Clusters
     * To remove the container registry from Kubernetes clusters, send a DELETE request to &#x60;/v2/kubernetes/registry&#x60;.
     * @return RemoveRegistryRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RemoveRegistryRequestBuilder removeRegistry() throws IllegalArgumentException {
        return new RemoveRegistryRequestBuilder();
    }
    private okhttp3.Call runClusterlintChecksCall(UUID clusterId, ClusterlintRequest clusterlintRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = clusterlintRequest;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/clusterlint"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call runClusterlintChecksValidateBeforeCall(UUID clusterId, ClusterlintRequest clusterlintRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling runClusterlintChecks(Async)");
        }

        return runClusterlintChecksCall(clusterId, clusterlintRequest, _callback);

    }


    private ApiResponse<Object> runClusterlintChecksWithHttpInfo(UUID clusterId, ClusterlintRequest clusterlintRequest) throws ApiException {
        okhttp3.Call localVarCall = runClusterlintChecksValidateBeforeCall(clusterId, clusterlintRequest, null);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call runClusterlintChecksAsync(UUID clusterId, ClusterlintRequest clusterlintRequest, final ApiCallback<Object> _callback) throws ApiException {

        okhttp3.Call localVarCall = runClusterlintChecksValidateBeforeCall(clusterId, clusterlintRequest, _callback);
        Type localVarReturnType = new TypeToken<Object>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class RunClusterlintChecksRequestBuilder {
        private final UUID clusterId;
        private List<String> includeGroups;
        private List<String> includeChecks;
        private List<String> excludeGroups;
        private List<String> excludeChecks;

        private RunClusterlintChecksRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Set includeGroups
         * @param includeGroups An array of check groups that will be run when clusterlint executes checks. (optional)
         * @return RunClusterlintChecksRequestBuilder
         */
        public RunClusterlintChecksRequestBuilder includeGroups(List<String> includeGroups) {
            this.includeGroups = includeGroups;
            return this;
        }
        
        /**
         * Set includeChecks
         * @param includeChecks An array of checks that will be run when clusterlint executes checks. (optional)
         * @return RunClusterlintChecksRequestBuilder
         */
        public RunClusterlintChecksRequestBuilder includeChecks(List<String> includeChecks) {
            this.includeChecks = includeChecks;
            return this;
        }
        
        /**
         * Set excludeGroups
         * @param excludeGroups An array of check groups that will be omitted when clusterlint executes checks. (optional)
         * @return RunClusterlintChecksRequestBuilder
         */
        public RunClusterlintChecksRequestBuilder excludeGroups(List<String> excludeGroups) {
            this.excludeGroups = excludeGroups;
            return this;
        }
        
        /**
         * Set excludeChecks
         * @param excludeChecks An array of checks that will be run when clusterlint executes checks. (optional)
         * @return RunClusterlintChecksRequestBuilder
         */
        public RunClusterlintChecksRequestBuilder excludeChecks(List<String> excludeChecks) {
            this.excludeChecks = excludeChecks;
            return this;
        }
        
        /**
         * Build call for runClusterlintChecks
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response is a JSON object with a key called &#x60;run_id&#x60; that you can later use to fetch the run results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            ClusterlintRequest clusterlintRequest = buildBodyParams();
            return runClusterlintChecksCall(clusterId, clusterlintRequest, _callback);
        }

        private ClusterlintRequest buildBodyParams() {
            ClusterlintRequest clusterlintRequest = new ClusterlintRequest();
            clusterlintRequest.includeGroups(this.includeGroups);
            clusterlintRequest.includeChecks(this.includeChecks);
            clusterlintRequest.excludeGroups(this.excludeGroups);
            clusterlintRequest.excludeChecks(this.excludeChecks);
            return clusterlintRequest;
        }

        /**
         * Execute runClusterlintChecks request
         * @return Object
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response is a JSON object with a key called &#x60;run_id&#x60; that you can later use to fetch the run results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public Object execute() throws ApiException {
            ClusterlintRequest clusterlintRequest = buildBodyParams();
            ApiResponse<Object> localVarResp = runClusterlintChecksWithHttpInfo(clusterId, clusterlintRequest);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute runClusterlintChecks request with HTTP info returned
         * @return ApiResponse&lt;Object&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response is a JSON object with a key called &#x60;run_id&#x60; that you can later use to fetch the run results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<Object> executeWithHttpInfo() throws ApiException {
            ClusterlintRequest clusterlintRequest = buildBodyParams();
            return runClusterlintChecksWithHttpInfo(clusterId, clusterlintRequest);
        }

        /**
         * Execute runClusterlintChecks request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response is a JSON object with a key called &#x60;run_id&#x60; that you can later use to fetch the run results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<Object> _callback) throws ApiException {
            ClusterlintRequest clusterlintRequest = buildBodyParams();
            return runClusterlintChecksAsync(clusterId, clusterlintRequest, _callback);
        }
    }

    /**
     * Run Clusterlint Checks on a Kubernetes Cluster
     * Clusterlint helps operators conform to Kubernetes best practices around resources, security and reliability to avoid common problems while operating or upgrading the clusters.  To request a clusterlint run on your cluster, send a POST request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/clusterlint&#x60;. This will run all checks present in the &#x60;doks&#x60; group by default, if a request body is not specified. Optionally specify the below attributes.  For information about the available checks, please refer to [the clusterlint check documentation](https://github.com/digitalocean/clusterlint/blob/master/checks.md). 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @return RunClusterlintChecksRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The response is a JSON object with a key called &#x60;run_id&#x60; that you can later use to fetch the run results. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public RunClusterlintChecksRequestBuilder runClusterlintChecks(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new RunClusterlintChecksRequestBuilder(clusterId);
    }
    private okhttp3.Call selectiveClusterDestroyCall(UUID clusterId, DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = destroyAssociatedKubernetesResources;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources/selective"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call selectiveClusterDestroyValidateBeforeCall(UUID clusterId, DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling selectiveClusterDestroy(Async)");
        }

        // verify the required parameter 'destroyAssociatedKubernetesResources' is set
        if (destroyAssociatedKubernetesResources == null) {
            throw new ApiException("Missing the required parameter 'destroyAssociatedKubernetesResources' when calling selectiveClusterDestroy(Async)");
        }

        return selectiveClusterDestroyCall(clusterId, destroyAssociatedKubernetesResources, _callback);

    }


    private ApiResponse<Void> selectiveClusterDestroyWithHttpInfo(UUID clusterId, DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources) throws ApiException {
        okhttp3.Call localVarCall = selectiveClusterDestroyValidateBeforeCall(clusterId, destroyAssociatedKubernetesResources, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call selectiveClusterDestroyAsync(UUID clusterId, DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = selectiveClusterDestroyValidateBeforeCall(clusterId, destroyAssociatedKubernetesResources, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class SelectiveClusterDestroyRequestBuilder {
        private final UUID clusterId;
        private List<String> loadBalancers;
        private List<String> volumes;
        private List<String> volumeSnapshots;

        private SelectiveClusterDestroyRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Set loadBalancers
         * @param loadBalancers A list of IDs for associated load balancers to destroy along with the cluster. (optional)
         * @return SelectiveClusterDestroyRequestBuilder
         */
        public SelectiveClusterDestroyRequestBuilder loadBalancers(List<String> loadBalancers) {
            this.loadBalancers = loadBalancers;
            return this;
        }
        
        /**
         * Set volumes
         * @param volumes A list of IDs for associated volumes to destroy along with the cluster. (optional)
         * @return SelectiveClusterDestroyRequestBuilder
         */
        public SelectiveClusterDestroyRequestBuilder volumes(List<String> volumes) {
            this.volumes = volumes;
            return this;
        }
        
        /**
         * Set volumeSnapshots
         * @param volumeSnapshots A list of IDs for associated volume snapshots to destroy along with the cluster. (optional)
         * @return SelectiveClusterDestroyRequestBuilder
         */
        public SelectiveClusterDestroyRequestBuilder volumeSnapshots(List<String> volumeSnapshots) {
            this.volumeSnapshots = volumeSnapshots;
            return this;
        }
        
        /**
         * Build call for selectiveClusterDestroy
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
            DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources = buildBodyParams();
            return selectiveClusterDestroyCall(clusterId, destroyAssociatedKubernetesResources, _callback);
        }

        private DestroyAssociatedKubernetesResources buildBodyParams() {
            DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources = new DestroyAssociatedKubernetesResources();
            destroyAssociatedKubernetesResources.loadBalancers(this.loadBalancers);
            destroyAssociatedKubernetesResources.volumes(this.volumes);
            destroyAssociatedKubernetesResources.volumeSnapshots(this.volumeSnapshots);
            return destroyAssociatedKubernetesResources;
        }

        /**
         * Execute selectiveClusterDestroy request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources = buildBodyParams();
            selectiveClusterDestroyWithHttpInfo(clusterId, destroyAssociatedKubernetesResources);
        }

        /**
         * Execute selectiveClusterDestroy request with HTTP info returned
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
            DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources = buildBodyParams();
            return selectiveClusterDestroyWithHttpInfo(clusterId, destroyAssociatedKubernetesResources);
        }

        /**
         * Execute selectiveClusterDestroy request (asynchronously)
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
            DestroyAssociatedKubernetesResources destroyAssociatedKubernetesResources = buildBodyParams();
            return selectiveClusterDestroyAsync(clusterId, destroyAssociatedKubernetesResources, _callback);
        }
    }

    /**
     * Selectively Delete a Cluster and its Associated Resources
     * To delete a Kubernetes cluster along with a subset of its associated resources, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/destroy_with_associated_resources/selective&#x60;.  The JSON body of the request should include &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, or &#x60;volume_snapshots&#x60; keys each set to an array of IDs for the associated resources to be destroyed.  The IDs can be found by querying the cluster&#39;s associated resources endpoint. Any associated resource not included in the request will remain and continue to accrue changes on your account. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param destroyAssociatedKubernetesResources  (required)
     * @return SelectiveClusterDestroyRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 204 </td><td> The action was successful and the response body is empty. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public SelectiveClusterDestroyRequestBuilder selectiveClusterDestroy(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new SelectiveClusterDestroyRequestBuilder(clusterId);
    }
    private okhttp3.Call updateClusterCall(UUID clusterId, ClusterUpdate clusterUpdate, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = clusterUpdate;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call updateClusterValidateBeforeCall(UUID clusterId, ClusterUpdate clusterUpdate, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling updateCluster(Async)");
        }

        // verify the required parameter 'clusterUpdate' is set
        if (clusterUpdate == null) {
            throw new ApiException("Missing the required parameter 'clusterUpdate' when calling updateCluster(Async)");
        }

        return updateClusterCall(clusterId, clusterUpdate, _callback);

    }


    private ApiResponse<KubernetesUpdateClusterResponse> updateClusterWithHttpInfo(UUID clusterId, ClusterUpdate clusterUpdate) throws ApiException {
        okhttp3.Call localVarCall = updateClusterValidateBeforeCall(clusterId, clusterUpdate, null);
        Type localVarReturnType = new TypeToken<KubernetesUpdateClusterResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateClusterAsync(UUID clusterId, ClusterUpdate clusterUpdate, final ApiCallback<KubernetesUpdateClusterResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateClusterValidateBeforeCall(clusterId, clusterUpdate, _callback);
        Type localVarReturnType = new TypeToken<KubernetesUpdateClusterResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateClusterRequestBuilder {
        private final String name;
        private final UUID clusterId;
        private List<String> tags;
        private MaintenancePolicy maintenancePolicy;
        private Boolean autoUpgrade;
        private Boolean surgeUpgrade;
        private Boolean ha;

        private UpdateClusterRequestBuilder(String name, UUID clusterId) {
            this.name = name;
            this.clusterId = clusterId;
        }

        /**
         * Set tags
         * @param tags An array of tags applied to the Kubernetes cluster. All clusters are automatically tagged &#x60;k8s&#x60; and &#x60;k8s:$K8S_CLUSTER_ID&#x60;. (optional)
         * @return UpdateClusterRequestBuilder
         */
        public UpdateClusterRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set maintenancePolicy
         * @param maintenancePolicy  (optional)
         * @return UpdateClusterRequestBuilder
         */
        public UpdateClusterRequestBuilder maintenancePolicy(MaintenancePolicy maintenancePolicy) {
            this.maintenancePolicy = maintenancePolicy;
            return this;
        }
        
        /**
         * Set autoUpgrade
         * @param autoUpgrade A boolean value indicating whether the cluster will be automatically upgraded to new patch releases during its maintenance window. (optional, default to false)
         * @return UpdateClusterRequestBuilder
         */
        public UpdateClusterRequestBuilder autoUpgrade(Boolean autoUpgrade) {
            this.autoUpgrade = autoUpgrade;
            return this;
        }
        
        /**
         * Set surgeUpgrade
         * @param surgeUpgrade A boolean value indicating whether surge upgrade is enabled/disabled for the cluster. Surge upgrade makes cluster upgrades fast and reliable by bringing up new nodes before destroying the outdated nodes. (optional, default to false)
         * @return UpdateClusterRequestBuilder
         */
        public UpdateClusterRequestBuilder surgeUpgrade(Boolean surgeUpgrade) {
            this.surgeUpgrade = surgeUpgrade;
            return this;
        }
        
        /**
         * Set ha
         * @param ha A boolean value indicating whether the control plane is run in a highly available configuration in the cluster. Highly available control planes incur less downtime. The property cannot be disabled. (optional, default to false)
         * @return UpdateClusterRequestBuilder
         */
        public UpdateClusterRequestBuilder ha(Boolean ha) {
            this.ha = ha;
            return this;
        }
        
        /**
         * Build call for updateCluster
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            ClusterUpdate clusterUpdate = buildBodyParams();
            return updateClusterCall(clusterId, clusterUpdate, _callback);
        }

        private ClusterUpdate buildBodyParams() {
            ClusterUpdate clusterUpdate = new ClusterUpdate();
            clusterUpdate.tags(this.tags);
            clusterUpdate.name(this.name);
            clusterUpdate.maintenancePolicy(this.maintenancePolicy);
            clusterUpdate.autoUpgrade(this.autoUpgrade);
            clusterUpdate.surgeUpgrade(this.surgeUpgrade);
            clusterUpdate.ha(this.ha);
            return clusterUpdate;
        }

        /**
         * Execute updateCluster request
         * @return KubernetesUpdateClusterResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesUpdateClusterResponse execute() throws ApiException {
            ClusterUpdate clusterUpdate = buildBodyParams();
            ApiResponse<KubernetesUpdateClusterResponse> localVarResp = updateClusterWithHttpInfo(clusterId, clusterUpdate);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateCluster request with HTTP info returned
         * @return ApiResponse&lt;KubernetesUpdateClusterResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesUpdateClusterResponse> executeWithHttpInfo() throws ApiException {
            ClusterUpdate clusterUpdate = buildBodyParams();
            return updateClusterWithHttpInfo(clusterId, clusterUpdate);
        }

        /**
         * Execute updateCluster request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesUpdateClusterResponse> _callback) throws ApiException {
            ClusterUpdate clusterUpdate = buildBodyParams();
            return updateClusterAsync(clusterId, clusterUpdate, _callback);
        }
    }

    /**
     * Update a Kubernetes Cluster
     * To update a Kubernetes cluster, send a PUT request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID&#x60; and specify one or more of the attributes below. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param clusterUpdate  (required)
     * @return UpdateClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateClusterRequestBuilder updateCluster(String name, UUID clusterId) throws IllegalArgumentException {
        if (name == null) throw new IllegalArgumentException("\"name\" is required but got null");
            

        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new UpdateClusterRequestBuilder(name, clusterId);
    }
    private okhttp3.Call updateNodePoolCall(UUID clusterId, UUID nodePoolId, KubernetesNodePoolBase kubernetesNodePoolBase, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = kubernetesNodePoolBase;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()))
            .replace("{" + "node_pool_id" + "}", localVarApiClient.escapeString(nodePoolId.toString()));

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
    private okhttp3.Call updateNodePoolValidateBeforeCall(UUID clusterId, UUID nodePoolId, KubernetesNodePoolBase kubernetesNodePoolBase, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling updateNodePool(Async)");
        }

        // verify the required parameter 'nodePoolId' is set
        if (nodePoolId == null) {
            throw new ApiException("Missing the required parameter 'nodePoolId' when calling updateNodePool(Async)");
        }

        // verify the required parameter 'kubernetesNodePoolBase' is set
        if (kubernetesNodePoolBase == null) {
            throw new ApiException("Missing the required parameter 'kubernetesNodePoolBase' when calling updateNodePool(Async)");
        }

        return updateNodePoolCall(clusterId, nodePoolId, kubernetesNodePoolBase, _callback);

    }


    private ApiResponse<KubernetesUpdateNodePoolResponse> updateNodePoolWithHttpInfo(UUID clusterId, UUID nodePoolId, KubernetesNodePoolBase kubernetesNodePoolBase) throws ApiException {
        okhttp3.Call localVarCall = updateNodePoolValidateBeforeCall(clusterId, nodePoolId, kubernetesNodePoolBase, null);
        Type localVarReturnType = new TypeToken<KubernetesUpdateNodePoolResponse>(){}.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    private okhttp3.Call updateNodePoolAsync(UUID clusterId, UUID nodePoolId, KubernetesNodePoolBase kubernetesNodePoolBase, final ApiCallback<KubernetesUpdateNodePoolResponse> _callback) throws ApiException {

        okhttp3.Call localVarCall = updateNodePoolValidateBeforeCall(clusterId, nodePoolId, kubernetesNodePoolBase, _callback);
        Type localVarReturnType = new TypeToken<KubernetesUpdateNodePoolResponse>(){}.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    public class UpdateNodePoolRequestBuilder {
        private final UUID clusterId;
        private final UUID nodePoolId;
        private List<String> tags;
        private UUID id;
        private String name;
        private Integer count;
        private Object labels;
        private List<KubernetesNodePoolTaint> taints;
        private Boolean autoScale;
        private Integer minNodes;
        private Integer maxNodes;
        private List<Node> nodes;

        private UpdateNodePoolRequestBuilder(UUID clusterId, UUID nodePoolId) {
            this.clusterId = clusterId;
            this.nodePoolId = nodePoolId;
        }

        /**
         * Set tags
         * @param tags An array containing the tags applied to the node pool. All node pools are automatically tagged &#x60;k8s&#x60;, &#x60;k8s-worker&#x60;, and &#x60;k8s:$K8S_CLUSTER_ID&#x60;. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }
        
        /**
         * Set id
         * @param id A unique ID that can be used to identify and reference a specific node pool. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder id(UUID id) {
            this.id = id;
            return this;
        }
        
        /**
         * Set name
         * @param name A human-readable name for the node pool. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Set count
         * @param count The number of Droplet instances in the node pool. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder count(Integer count) {
            this.count = count;
            return this;
        }
        
        /**
         * Set labels
         * @param labels An object of key/value mappings specifying labels to apply to all nodes in a pool. Labels will automatically be applied to all existing nodes and any subsequent nodes added to the pool. Note that when a label is removed, it is not deleted from the nodes in the pool. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder labels(Object labels) {
            this.labels = labels;
            return this;
        }
        
        /**
         * Set taints
         * @param taints An array of taints to apply to all nodes in a pool. Taints will automatically be applied to all existing nodes and any subsequent nodes added to the pool. When a taint is removed, it is deleted from all nodes in the pool. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder taints(List<KubernetesNodePoolTaint> taints) {
            this.taints = taints;
            return this;
        }
        
        /**
         * Set autoScale
         * @param autoScale A boolean value indicating whether auto-scaling is enabled for this node pool. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder autoScale(Boolean autoScale) {
            this.autoScale = autoScale;
            return this;
        }
        
        /**
         * Set minNodes
         * @param minNodes The minimum number of nodes that this node pool can be auto-scaled to. The value will be &#x60;0&#x60; if &#x60;auto_scale&#x60; is set to &#x60;false&#x60;. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder minNodes(Integer minNodes) {
            this.minNodes = minNodes;
            return this;
        }
        
        /**
         * Set maxNodes
         * @param maxNodes The maximum number of nodes that this node pool can be auto-scaled to. The value will be &#x60;0&#x60; if &#x60;auto_scale&#x60; is set to &#x60;false&#x60;. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder maxNodes(Integer maxNodes) {
            this.maxNodes = maxNodes;
            return this;
        }
        
        /**
         * Set nodes
         * @param nodes An object specifying the details of a specific worker node in a node pool. (optional)
         * @return UpdateNodePoolRequestBuilder
         */
        public UpdateNodePoolRequestBuilder nodes(List<Node> nodes) {
            this.nodes = nodes;
            return this;
        }
        
        /**
         * Build call for updateNodePool
         * @param _callback ApiCallback API callback
         * @return Call to execute
         * @throws ApiException If fail to serialize the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call buildCall(final ApiCallback _callback) throws ApiException {
            KubernetesNodePoolBase kubernetesNodePoolBase = buildBodyParams();
            return updateNodePoolCall(clusterId, nodePoolId, kubernetesNodePoolBase, _callback);
        }

        private KubernetesNodePoolBase buildBodyParams() {
            KubernetesNodePoolBase kubernetesNodePoolBase = new KubernetesNodePoolBase();
            kubernetesNodePoolBase.tags(this.tags);
            kubernetesNodePoolBase.id(this.id);
            kubernetesNodePoolBase.name(this.name);
            kubernetesNodePoolBase.count(this.count);
            kubernetesNodePoolBase.labels(this.labels);
            kubernetesNodePoolBase.taints(this.taints);
            kubernetesNodePoolBase.autoScale(this.autoScale);
            kubernetesNodePoolBase.minNodes(this.minNodes);
            kubernetesNodePoolBase.maxNodes(this.maxNodes);
            kubernetesNodePoolBase.nodes(this.nodes);
            return kubernetesNodePoolBase;
        }

        /**
         * Execute updateNodePool request
         * @return KubernetesUpdateNodePoolResponse
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public KubernetesUpdateNodePoolResponse execute() throws ApiException {
            KubernetesNodePoolBase kubernetesNodePoolBase = buildBodyParams();
            ApiResponse<KubernetesUpdateNodePoolResponse> localVarResp = updateNodePoolWithHttpInfo(clusterId, nodePoolId, kubernetesNodePoolBase);
            return localVarResp.getResponseBody();
        }

        /**
         * Execute updateNodePool request with HTTP info returned
         * @return ApiResponse&lt;KubernetesUpdateNodePoolResponse&gt;
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public ApiResponse<KubernetesUpdateNodePoolResponse> executeWithHttpInfo() throws ApiException {
            KubernetesNodePoolBase kubernetesNodePoolBase = buildBodyParams();
            return updateNodePoolWithHttpInfo(clusterId, nodePoolId, kubernetesNodePoolBase);
        }

        /**
         * Execute updateNodePool request (asynchronously)
         * @param _callback The callback to be executed when the API call finishes
         * @return The request call
         * @throws ApiException If fail to process the API call, e.g. serializing the request body object
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public okhttp3.Call executeAsync(final ApiCallback<KubernetesUpdateNodePoolResponse> _callback) throws ApiException {
            KubernetesNodePoolBase kubernetesNodePoolBase = buildBodyParams();
            return updateNodePoolAsync(clusterId, nodePoolId, kubernetesNodePoolBase, _callback);
        }
    }

    /**
     * Update a Node Pool in a Kubernetes Cluster
     * To update the name of a node pool, edit the tags applied to it, or adjust its number of nodes, send a PUT request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID&#x60; with the following attributes. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param nodePoolId A unique ID that can be used to reference a Kubernetes node pool. (required)
     * @param kubernetesNodePoolBase  (required)
     * @return UpdateNodePoolRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpdateNodePoolRequestBuilder updateNodePool(UUID clusterId, UUID nodePoolId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        if (nodePoolId == null) throw new IllegalArgumentException("\"nodePoolId\" is required but got null");
            

        return new UpdateNodePoolRequestBuilder(clusterId, nodePoolId);
    }
    private okhttp3.Call upgradeClusterCall(UUID clusterId, KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest, final ApiCallback _callback) throws ApiException {
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

        Object localVarPostBody = kubernetesUpgradeClusterRequest;

        // create path and map variables
        String localVarPath = "/v2/kubernetes/clusters/{cluster_id}/upgrade"
            .replace("{" + "cluster_id" + "}", localVarApiClient.escapeString(clusterId.toString()));

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
    private okhttp3.Call upgradeClusterValidateBeforeCall(UUID clusterId, KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest, final ApiCallback _callback) throws ApiException {
        // verify the required parameter 'clusterId' is set
        if (clusterId == null) {
            throw new ApiException("Missing the required parameter 'clusterId' when calling upgradeCluster(Async)");
        }

        // verify the required parameter 'kubernetesUpgradeClusterRequest' is set
        if (kubernetesUpgradeClusterRequest == null) {
            throw new ApiException("Missing the required parameter 'kubernetesUpgradeClusterRequest' when calling upgradeCluster(Async)");
        }

        return upgradeClusterCall(clusterId, kubernetesUpgradeClusterRequest, _callback);

    }


    private ApiResponse<Void> upgradeClusterWithHttpInfo(UUID clusterId, KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest) throws ApiException {
        okhttp3.Call localVarCall = upgradeClusterValidateBeforeCall(clusterId, kubernetesUpgradeClusterRequest, null);
        return localVarApiClient.execute(localVarCall);
    }

    private okhttp3.Call upgradeClusterAsync(UUID clusterId, KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest, final ApiCallback<Void> _callback) throws ApiException {

        okhttp3.Call localVarCall = upgradeClusterValidateBeforeCall(clusterId, kubernetesUpgradeClusterRequest, _callback);
        localVarApiClient.executeAsync(localVarCall, _callback);
        return localVarCall;
    }

    public class UpgradeClusterRequestBuilder {
        private final UUID clusterId;
        private String version;

        private UpgradeClusterRequestBuilder(UUID clusterId) {
            this.clusterId = clusterId;
        }

        /**
         * Set version
         * @param version The slug identifier for the version of Kubernetes that the cluster will be upgraded to. (optional)
         * @return UpgradeClusterRequestBuilder
         */
        public UpgradeClusterRequestBuilder version(String version) {
            this.version = version;
            return this;
        }
        
        /**
         * Build call for upgradeCluster
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
            KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest = buildBodyParams();
            return upgradeClusterCall(clusterId, kubernetesUpgradeClusterRequest, _callback);
        }

        private KubernetesUpgradeClusterRequest buildBodyParams() {
            KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest = new KubernetesUpgradeClusterRequest();
            kubernetesUpgradeClusterRequest.version(this.version);
            return kubernetesUpgradeClusterRequest;
        }

        /**
         * Execute upgradeCluster request
         * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
         * @http.response.details
         <table summary="Response Details" border="1">
            <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
            <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
            <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
         </table>
         */
        public void execute() throws ApiException {
            KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest = buildBodyParams();
            upgradeClusterWithHttpInfo(clusterId, kubernetesUpgradeClusterRequest);
        }

        /**
         * Execute upgradeCluster request with HTTP info returned
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
            KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest = buildBodyParams();
            return upgradeClusterWithHttpInfo(clusterId, kubernetesUpgradeClusterRequest);
        }

        /**
         * Execute upgradeCluster request (asynchronously)
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
            KubernetesUpgradeClusterRequest kubernetesUpgradeClusterRequest = buildBodyParams();
            return upgradeClusterAsync(clusterId, kubernetesUpgradeClusterRequest, _callback);
        }
    }

    /**
     * Upgrade a Kubernetes Cluster
     * To immediately upgrade a Kubernetes cluster to a newer patch release of Kubernetes, send a POST request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/upgrade&#x60;. The body of the request must specify a version attribute.  Available upgrade versions for a cluster can be fetched from &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/upgrades&#x60;. 
     * @param clusterId A unique ID that can be used to reference a Kubernetes cluster. (required)
     * @param kubernetesUpgradeClusterRequest  (required)
     * @return UpgradeClusterRequestBuilder
     * @http.response.details
     <table summary="Response Details" border="1">
        <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
        <tr><td> 202 </td><td> The does not indicate the success or failure of any operation, just that the request has been accepted for processing. </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
        <tr><td> 0 </td><td> Unexpected error </td><td>  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  </td></tr>
     </table>
     */
    public UpgradeClusterRequestBuilder upgradeCluster(UUID clusterId) throws IllegalArgumentException {
        if (clusterId == null) throw new IllegalArgumentException("\"clusterId\" is required but got null");
            

        return new UpgradeClusterRequestBuilder(clusterId);
    }
}
