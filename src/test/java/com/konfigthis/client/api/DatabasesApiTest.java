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

import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.model.ConnectionPool;
import com.konfigthis.client.model.ConnectionPoolUpdate;
import com.konfigthis.client.model.ConnectionPools;
import com.konfigthis.client.model.Database;
import com.konfigthis.client.model.DatabaseBackup;
import com.konfigthis.client.model.DatabaseClusterConnection;
import com.konfigthis.client.model.DatabaseClusterMaintenanceWindow;
import com.konfigthis.client.model.DatabaseClusterResize;
import com.konfigthis.client.model.DatabaseConfig;
import com.konfigthis.client.model.DatabaseMaintenanceWindow;
import com.konfigthis.client.model.DatabaseMetricsCredentials;
import com.konfigthis.client.model.DatabaseReplica;
import com.konfigthis.client.model.DatabaseReplicaConnection;
import com.konfigthis.client.model.DatabaseServiceEndpoint;
import com.konfigthis.client.model.DatabaseUser;
import com.konfigthis.client.model.DatabasesAddNewConnectionPoolResponse;
import com.konfigthis.client.model.DatabasesAddResponse;
import com.konfigthis.client.model.DatabasesAddUserRequest;
import com.konfigthis.client.model.DatabasesAddUserResponse;
import com.konfigthis.client.model.DatabasesBasicAuthCredentials;
import com.konfigthis.client.model.DatabasesConfigureEvictionPolicyRequest;
import com.konfigthis.client.model.DatabasesCreateClusterRequest;
import com.konfigthis.client.model.DatabasesCreateClusterResponse;
import com.konfigthis.client.model.DatabasesCreateReadOnlyReplicaResponse;
import com.konfigthis.client.model.DatabasesCreateTopicKafkaClusterResponse;
import com.konfigthis.client.model.DatabasesGetClusterConfigResponse;
import com.konfigthis.client.model.DatabasesGetClustersMetricsCredentialsResponse;
import com.konfigthis.client.model.DatabasesGetEvictionPolicyResponse;
import com.konfigthis.client.model.DatabasesGetPublicCertificateResponse;
import com.konfigthis.client.model.DatabasesListBackupsResponse;
import com.konfigthis.client.model.DatabasesListClustersResponse;
import com.konfigthis.client.model.DatabasesListEventsLogsResponse;
import com.konfigthis.client.model.DatabasesListFirewallRulesResponse;
import com.konfigthis.client.model.DatabasesListReadOnlyReplicasResponse;
import com.konfigthis.client.model.DatabasesListResponse;
import com.konfigthis.client.model.DatabasesListTopicsKafkaClusterResponse;
import com.konfigthis.client.model.DatabasesListUsersResponse;
import com.konfigthis.client.model.DatabasesMigrateClusterToNewRegionRequest;
import com.konfigthis.client.model.DatabasesResetUserAuthRequest;
import com.konfigthis.client.model.DatabasesUpdateFirewallRulesRequest;
import com.konfigthis.client.model.DatabasesUpdateSettingsRequest;
import com.konfigthis.client.model.Error;
import com.konfigthis.client.model.EvictionPolicyModel;
import com.konfigthis.client.model.FirewallRule;
import com.konfigthis.client.model.KafkaTopicConfig;
import com.konfigthis.client.model.KafkaTopicCreate;
import com.konfigthis.client.model.KafkaTopicUpdate;
import com.konfigthis.client.model.Mpr;
import com.konfigthis.client.model.MysqlSettings;
import java.time.OffsetDateTime;
import com.konfigthis.client.model.OnlineMigration;
import com.konfigthis.client.model.Options;
import com.konfigthis.client.model.SourceDatabase;
import com.konfigthis.client.model.SourceDatabaseSource;
import com.konfigthis.client.model.SqlMode;
import java.util.UUID;
import com.konfigthis.client.model.UserSettings;
import com.konfigthis.client.model.Version2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DatabasesApi
 */
@Disabled
public class DatabasesApiTest {

    private static DatabasesApi api;

    
    @BeforeAll
    public static void beforeClass() {
        ApiClient apiClient = Configuration.getDefaultApiClient();
        api = new DatabasesApi(apiClient);
    }

    /**
     * Add a New Database
     *
     * To add a new database to an existing cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/dbs&#x60;.  Note: Database management is not supported for Redis clusters.  The response will be a JSON object with a key called &#x60;db&#x60;. The value of this will be an object that contains the standard attributes associated with a database. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void addTest() throws ApiException {
        String name = null;
        UUID databaseClusterUuid = null;
        DatabasesAddResponse response = api.add(name, databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Add a New Connection Pool (PostgreSQL)
     *
     * For PostgreSQL database clusters, connection pools can be used to allow a database to share its idle connections. The popular PostgreSQL connection pooling utility PgBouncer is used to provide this service. [See here for more information](https://www.digitalocean.com/docs/databases/postgresql/how-to/manage-connection-pools/) about how and why to use PgBouncer connection pooling including details about the available transaction modes.  To add a new connection pool to a PostgreSQL database cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/pools&#x60; specifying a name for the pool, the user to connect with, the database to connect to, as well as its desired size and transaction mode. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void addNewConnectionPoolTest() throws ApiException {
        String name = null;
        String mode = null;
        Integer size = null;
        String db = null;
        UUID databaseClusterUuid = null;
        String user = null;
        DatabaseClusterConnection connection = null;
        DatabaseClusterConnection privateConnection = null;
        DatabaseClusterConnection standbyConnection = null;
        DatabaseClusterConnection standbyPrivateConnection = null;
        DatabasesAddNewConnectionPoolResponse response = api.addNewConnectionPool(name, mode, size, db, databaseClusterUuid)
                .user(user)
                .connection(connection)
                .privateConnection(privateConnection)
                .standbyConnection(standbyConnection)
                .standbyPrivateConnection(standbyPrivateConnection)
                .execute();
        // TODO: test validations
    }

    /**
     * Add a Database User
     *
     * To add a new database user, send a POST request to &#x60;/v2/databases/$DATABASE_ID/users&#x60; with the desired username.  Note: User management is not supported for Redis clusters.  When adding a user to a MySQL cluster, additional options can be configured in the &#x60;mysql_settings&#x60; object.  When adding a user to a Kafka cluster, additional options can be configured in the &#x60;settings&#x60; object.  The response will be a JSON object with a key called &#x60;user&#x60;. The value of this will be an object that contains the standard attributes associated with a database user including its randomly generated password. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void addUserTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String name = null;
        String role = null;
        String password = null;
        String accessCert = null;
        String accessKey = null;
        MysqlSettings mysqlSettings = null;
        UserSettings settings = null;
        Boolean readonly = null;
        DatabasesAddUserResponse response = api.addUser(databaseClusterUuid)
                .name(name)
                .role(role)
                .password(password)
                .accessCert(accessCert)
                .accessKey(accessKey)
                .mysqlSettings(mysqlSettings)
                .settings(settings)
                .readonly(readonly)
                .execute();
        // TODO: test validations
    }

    /**
     * Configure the Eviction Policy for a Redis Cluster
     *
     * To configure an eviction policy for an existing Redis cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/eviction_policy&#x60; specifying the desired policy.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void configureEvictionPolicyTest() throws ApiException {
        EvictionPolicyModel evictionPolicy = null;
        UUID databaseClusterUuid = null;
        api.configureEvictionPolicy(evictionPolicy, databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Configure a Database Cluster&#39;s Maintenance Window
     *
     * To configure the window when automatic maintenance should be performed for a database cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/maintenance&#x60;. A successful request will receive a 204 No Content status code with no body in response.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void configureMaintenanceWindowTest() throws ApiException {
        String day = null;
        String hour = null;
        UUID databaseClusterUuid = null;
        List<String> description = null;
        Boolean pending = null;
        api.configureMaintenanceWindow(day, hour, databaseClusterUuid)
                .description(description)
                .pending(pending)
                .execute();
        // TODO: test validations
    }

    /**
     * Create a New Database Cluster
     *
     * To create a database cluster, send a POST request to &#x60;/v2/databases&#x60;. The response will be a JSON object with a key called &#x60;database&#x60;. The value of this will be an object that contains the standard attributes associated with a database cluster. The initial value of the database cluster&#39;s &#x60;status&#x60; attribute will be &#x60;creating&#x60;. When the cluster is ready to receive traffic, this will transition to &#x60;online&#x60;.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects will contain the information needed to connect to the cluster&#39;s standby node(s).  DigitalOcean managed PostgreSQL and MySQL database clusters take automated daily backups. To create a new database cluster based on a backup of an existing cluster, send a POST request to &#x60;/v2/databases&#x60;. In addition to the standard database cluster attributes, the JSON body must include a key named &#x60;backup_restore&#x60; with the name of the original database cluster and the timestamp of the backup to be restored. Creating a database from a backup is the same as forking a database in the control panel. Note: Backups are not supported for Redis clusters.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createClusterTest() throws ApiException {
        List<String> tags = null;
        String version = null;
        UUID id = null;
        String name = null;
        String engine = null;
        String semanticVersion = null;
        Integer numNodes = null;
        String size = null;
        String region = null;
        String status = null;
        OffsetDateTime createdAt = null;
        String privateNetworkUuid = null;
        List<String> dbNames = null;
        DatabaseClusterConnection connection = null;
        DatabaseClusterConnection privateConnection = null;
        DatabaseClusterConnection standbyConnection = null;
        DatabaseClusterConnection standbyPrivateConnection = null;
        List<DatabaseUser> users = null;
        DatabaseClusterMaintenanceWindow maintenanceWindow = null;
        UUID projectId = null;
        List<FirewallRule> rules = null;
        String versionEndOfLife = null;
        String versionEndOfAvailability = null;
        Integer storageSizeMib = null;
        List<DatabaseServiceEndpoint> metricsEndpoints = null;
        DatabaseBackup backupRestore = null;
        DatabasesCreateClusterResponse response = api.createCluster()
                .tags(tags)
                .version(version)
                .id(id)
                .name(name)
                .engine(engine)
                .semanticVersion(semanticVersion)
                .numNodes(numNodes)
                .size(size)
                .region(region)
                .status(status)
                .createdAt(createdAt)
                .privateNetworkUuid(privateNetworkUuid)
                .dbNames(dbNames)
                .connection(connection)
                .privateConnection(privateConnection)
                .standbyConnection(standbyConnection)
                .standbyPrivateConnection(standbyPrivateConnection)
                .users(users)
                .maintenanceWindow(maintenanceWindow)
                .projectId(projectId)
                .rules(rules)
                .versionEndOfLife(versionEndOfLife)
                .versionEndOfAvailability(versionEndOfAvailability)
                .storageSizeMib(storageSizeMib)
                .metricsEndpoints(metricsEndpoints)
                .backupRestore(backupRestore)
                .execute();
        // TODO: test validations
    }

    /**
     * Create a Read-only Replica
     *
     * To create a read-only replica for a PostgreSQL or MySQL database cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/replicas&#x60; specifying the name it should be given, the size of the node to be used, and the region where it will be located.  **Note**: Read-only replicas are not supported for Redis clusters.  The response will be a JSON object with a key called &#x60;replica&#x60;. The value of this will be an object that contains the standard attributes associated with a database replica. The initial value of the read-only replica&#39;s &#x60;status&#x60; attribute will be &#x60;forking&#x60;. When the replica is ready to receive traffic, this will transition to &#x60;active&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createReadOnlyReplicaTest() throws ApiException {
        String name = null;
        UUID databaseClusterUuid = null;
        List<String> tags = null;
        UUID id = null;
        String region = null;
        String size = null;
        String status = null;
        OffsetDateTime createdAt = null;
        String privateNetworkUuid = null;
        DatabaseReplicaConnection connection = null;
        DatabaseReplicaConnection privateConnection = null;
        Integer storageSizeMib = null;
        DatabasesCreateReadOnlyReplicaResponse response = api.createReadOnlyReplica(name, databaseClusterUuid)
                .tags(tags)
                .id(id)
                .region(region)
                .size(size)
                .status(status)
                .createdAt(createdAt)
                .privateNetworkUuid(privateNetworkUuid)
                .connection(connection)
                .privateConnection(privateConnection)
                .storageSizeMib(storageSizeMib)
                .execute();
        // TODO: test validations
    }

    /**
     * Create Topic for a Kafka Cluster
     *
     * To create a topic attached to a Kafka cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/topics&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void createTopicKafkaClusterTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String name = null;
        Integer replicationFactor = null;
        Integer partitionCount = null;
        KafkaTopicConfig config = null;
        DatabasesCreateTopicKafkaClusterResponse response = api.createTopicKafkaCluster(databaseClusterUuid)
                .name(name)
                .replicationFactor(replicationFactor)
                .partitionCount(partitionCount)
                .config(config)
                .execute();
        // TODO: test validations
    }

    /**
     * Delete a Database
     *
     * To delete a specific database, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/dbs/$DB_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.  Note: Database management is not supported for Redis clusters. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void deleteTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String databaseName = null;
        api.delete(databaseClusterUuid, databaseName)
                .execute();
        // TODO: test validations
    }

    /**
     * Delete a Connection Pool (PostgreSQL)
     *
     * To delete a specific connection pool for a PostgreSQL database cluster, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void deleteConnectionPoolTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String poolName = null;
        api.deleteConnectionPool(databaseClusterUuid, poolName)
                .execute();
        // TODO: test validations
    }

    /**
     * Delete Topic for a Kafka Cluster
     *
     * To delete a single topic within a Kafka cluster, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void deleteTopicKafkaClusterTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String topicName = null;
        api.deleteTopicKafkaCluster(databaseClusterUuid, topicName)
                .execute();
        // TODO: test validations
    }

    /**
     * Destroy a Database Cluster
     *
     * To destroy a specific database, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID&#x60;. A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void destroyClusterTest() throws ApiException {
        UUID databaseClusterUuid = null;
        api.destroyCluster(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Destroy a Read-only Replica
     *
     * To destroy a specific read-only replica, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void destroyReadonlyReplicaTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String replicaName = null;
        api.destroyReadonlyReplica(databaseClusterUuid, replicaName)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve an Existing Database
     *
     * To show information about an existing database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/dbs/$DB_NAME&#x60;.  Note: Database management is not supported for Redis clusters.  The response will be a JSON object with a &#x60;db&#x60; key. This will be set to an object containing the standard database attributes. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String databaseName = null;
        DatabasesAddResponse response = api.get(databaseClusterUuid, databaseName)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve an Existing Database Cluster Configuration
     *
     * Shows configuration parameters for an existing database cluster by sending a GET request to &#x60;/v2/databases/$DATABASE_ID/config&#x60;. The response is a JSON object with a &#x60;config&#x60; key, which is set to an object containing any database configuration parameters. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getClusterConfigTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesGetClusterConfigResponse response = api.getClusterConfig(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve an Existing Database Cluster
     *
     * To show information about an existing database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID&#x60;.  The response will be a JSON object with a database key. This will be set to an object containing the standard database cluster attributes.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects contain the information needed to connect to the cluster&#39;s standby node(s).  The embedded maintenance_window object will contain information about any scheduled maintenance for the database cluster.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getClusterInfoTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesCreateClusterResponse response = api.getClusterInfo(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve Database Clusters&#39; Metrics Endpoint Credentials
     *
     * To show the credentials for all database clusters&#39; metrics endpoints, send a GET request to &#x60;/v2/databases/metrics/credentials&#x60;. The result will be a JSON object with a &#x60;credentials&#x60; key.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getClustersMetricsCredentialsTest() throws ApiException {
        DatabasesGetClustersMetricsCredentialsResponse response = api.getClustersMetricsCredentials()
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve Existing Connection Pool (PostgreSQL)
     *
     * To show information about an existing connection pool for a PostgreSQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;. The response will be a JSON object with a &#x60;pool&#x60; key.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getConnectionPoolTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String poolName = null;
        DatabasesAddNewConnectionPoolResponse response = api.getConnectionPool(databaseClusterUuid, poolName)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve the Eviction Policy for a Redis Cluster
     *
     * To retrieve the configured eviction policy for an existing Redis cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/eviction_policy&#x60;. The response will be a JSON object with an &#x60;eviction_policy&#x60; key. This will be set to a string representing the eviction policy.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getEvictionPolicyTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesGetEvictionPolicyResponse response = api.getEvictionPolicy(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve an Existing Read-only Replica
     *
     * To show information about an existing database replica, send a GET request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  The response will be a JSON object with a &#x60;replica key&#x60;. This will be set to an object containing the standard database replica attributes.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getExistingReadOnlyReplicaTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String replicaName = null;
        DatabasesCreateReadOnlyReplicaResponse response = api.getExistingReadOnlyReplica(databaseClusterUuid, replicaName)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve the Status of an Online Migration
     *
     * To retrieve the status of the most recent online migration, send a GET request to &#x60;/v2/databases/$DATABASE_ID/online-migration&#x60;. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getMigrationStatusTest() throws ApiException {
        UUID databaseClusterUuid = null;
        OnlineMigration response = api.getMigrationStatus(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve the Public Certificate
     *
     * To retrieve the public certificate used to secure the connection to the database cluster send a GET request to &#x60;/v2/databases/$DATABASE_ID/ca&#x60;.  The response will be a JSON object with a &#x60;ca&#x60; key. This will be set to an object containing the base64 encoding of the public key certificate. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getPublicCertificateTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesGetPublicCertificateResponse response = api.getPublicCertificate(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve the SQL Modes for a MySQL Cluster
     *
     * To retrieve the configured SQL modes for an existing MySQL cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/sql_mode&#x60;. The response will be a JSON object with a &#x60;sql_mode&#x60; key. This will be set to a string representing the configured SQL modes.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getSqlModeTest() throws ApiException {
        UUID databaseClusterUuid = null;
        SqlMode response = api.getSqlMode(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Get Topic for a Kafka Cluster
     *
     * To retrieve a given topic by name from the set of a Kafka cluster&#39;s topics, send a GET request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getTopicKafkaClusterTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String topicName = null;
        DatabasesCreateTopicKafkaClusterResponse response = api.getTopicKafkaCluster(databaseClusterUuid, topicName)
                .execute();
        // TODO: test validations
    }

    /**
     * Retrieve an Existing Database User
     *
     * To show information about an existing database user, send a GET request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60;.  Note: User management is not supported for Redis clusters.  The response will be a JSON object with a &#x60;user&#x60; key. This will be set to an object containing the standard database user attributes.  For MySQL clusters, additional options will be contained in the &#x60;mysql_settings&#x60; object.  For Kafka clusters, additional options will be contained in the &#x60;settings&#x60; object. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void getUserTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String username = null;
        DatabasesAddUserResponse response = api.getUser(databaseClusterUuid, username)
                .execute();
        // TODO: test validations
    }

    /**
     * List All Databases
     *
     * To list all of the databases in a clusters, send a GET request to &#x60;/v2/databases/$DATABASE_ID/dbs&#x60;.  The result will be a JSON object with a &#x60;dbs&#x60; key. This will be set to an array of database objects, each of which will contain the standard database attributes.  Note: Database management is not supported for Redis clusters. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesListResponse response = api.list(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * List Backups for a Database Cluster
     *
     * To list all of the available backups of a PostgreSQL or MySQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/backups&#x60;. **Note**: Backups are not supported for Redis clusters. The result will be a JSON object with a &#x60;backups key&#x60;. This will be set to an array of backup objects, each of which will contain the size of the backup and the timestamp at which it was created.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listBackupsTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesListBackupsResponse response = api.listBackups(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * List All Database Clusters
     *
     * To list all of the database clusters available on your account, send a GET request to &#x60;/v2/databases&#x60;. To limit the results to database clusters with a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag. For example, &#x60;/v2/databases?tag_name&#x3D;$TAG_NAME&#x60;.  The result will be a JSON object with a &#x60;databases&#x60; key. This will be set to an array of database objects, each of which will contain the standard database attributes.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects will contain the information needed to connect to the cluster&#39;s standby node(s).  The embedded &#x60;maintenance_window&#x60; object will contain information about any scheduled maintenance for the database cluster.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listClustersTest() throws ApiException {
        String tagName = null;
        DatabasesListClustersResponse response = api.listClusters()
                .tagName(tagName)
                .execute();
        // TODO: test validations
    }

    /**
     * List Connection Pools (PostgreSQL)
     *
     * To list all of the connection pools available to a PostgreSQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/pools&#x60;. The result will be a JSON object with a &#x60;pools&#x60; key. This will be set to an array of connection pool objects.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listConnectionPoolsTest() throws ApiException {
        UUID databaseClusterUuid = null;
        ConnectionPools response = api.listConnectionPools(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * List all Events Logs
     *
     * To list all of the cluster events, send a GET request to &#x60;/v2/databases/$DATABASE_ID/events&#x60;.  The result will be a JSON object with a &#x60;events&#x60; key. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listEventsLogsTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesListEventsLogsResponse response = api.listEventsLogs(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * List Firewall Rules (Trusted Sources) for a Database Cluster
     *
     * To list all of a database cluster&#39;s firewall rules (known as \&quot;trusted sources\&quot; in the control panel), send a GET request to &#x60;/v2/databases/$DATABASE_ID/firewall&#x60;. The result will be a JSON object with a &#x60;rules&#x60; key.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listFirewallRulesTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesListFirewallRulesResponse response = api.listFirewallRules(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * List Database Options
     *
     * To list all of the options available for the offered database engines, send a GET request to &#x60;/v2/databases/options&#x60;. The result will be a JSON object with an &#x60;options&#x60; key.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listOptionsTest() throws ApiException {
        Options response = api.listOptions()
                .execute();
        // TODO: test validations
    }

    /**
     * List All Read-only Replicas
     *
     * To list all of the read-only replicas associated with a database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/replicas&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  The result will be a JSON object with a &#x60;replicas&#x60; key. This will be set to an array of database replica objects, each of which will contain the standard database replica attributes.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listReadOnlyReplicasTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesListReadOnlyReplicasResponse response = api.listReadOnlyReplicas(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * List Topics for a Kafka Cluster
     *
     * To list all of a Kafka cluster&#39;s topics, send a GET request to &#x60;/v2/databases/$DATABASE_ID/topics&#x60;.  The result will be a JSON object with a &#x60;topics&#x60; key. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listTopicsKafkaClusterTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesListTopicsKafkaClusterResponse response = api.listTopicsKafkaCluster(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * List all Database Users
     *
     * To list all of the users for your database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/users&#x60;.  Note: User management is not supported for Redis clusters.  The result will be a JSON object with a &#x60;users&#x60; key. This will be set to an array of database user objects, each of which will contain the standard database user attributes.  For MySQL clusters, additional options will be contained in the mysql_settings object. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void listUsersTest() throws ApiException {
        UUID databaseClusterUuid = null;
        DatabasesListUsersResponse response = api.listUsers(databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Migrate a Database Cluster to a New Region
     *
     * To migrate a database cluster to a new region, send a &#x60;PUT&#x60; request to &#x60;/v2/databases/$DATABASE_ID/migrate&#x60;. The body of the request must specify a &#x60;region&#x60; attribute.  A successful request will receive a 202 Accepted status code with no body in response. Querying the database cluster will show that its &#x60;status&#x60; attribute will now be set to &#x60;migrating&#x60;. This will transition back to &#x60;online&#x60; when the migration has completed. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void migrateClusterToNewRegionTest() throws ApiException {
        String region = null;
        UUID databaseClusterUuid = null;
        api.migrateClusterToNewRegion(region, databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Promote a Read-only Replica to become a Primary Cluster
     *
     * To promote a specific read-only replica, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME/promote&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void promoteReadonlyReplicaToPrimaryTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String replicaName = null;
        api.promoteReadonlyReplicaToPrimary(databaseClusterUuid, replicaName)
                .execute();
        // TODO: test validations
    }

    /**
     * Remove a Database User
     *
     * To remove a specific database user, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.  Note: User management is not supported for Redis clusters. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void removeUserTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String username = null;
        api.removeUser(databaseClusterUuid, username)
                .execute();
        // TODO: test validations
    }

    /**
     * Reset a Database User&#39;s Password or Authentication Method
     *
     * To reset the password for a database user, send a POST request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME/reset_auth&#x60;.  For &#x60;mysql&#x60; databases, the authentication method can be specifying by including a key in the JSON body called &#x60;mysql_settings&#x60; with the &#x60;auth_plugin&#x60; value specified.  The response will be a JSON object with a &#x60;user&#x60; key. This will be set to an object containing the standard database user attributes. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void resetUserAuthTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String username = null;
        MysqlSettings mysqlSettings = null;
        DatabasesAddUserResponse response = api.resetUserAuth(databaseClusterUuid, username)
                .mysqlSettings(mysqlSettings)
                .execute();
        // TODO: test validations
    }

    /**
     * Resize a Database Cluster
     *
     * To resize a database cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/resize&#x60;. The body of the request must specify both the size and num_nodes attributes. A successful request will receive a 202 Accepted status code with no body in response. Querying the database cluster will show that its status attribute will now be set to resizing. This will transition back to online when the resize operation has completed.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void resizeClusterTest() throws ApiException {
        String size = null;
        Integer numNodes = null;
        UUID databaseClusterUuid = null;
        Integer storageSizeMib = null;
        api.resizeCluster(size, numNodes, databaseClusterUuid)
                .storageSizeMib(storageSizeMib)
                .execute();
        // TODO: test validations
    }

    /**
     * Start an Online Migration
     *
     * To start an online migration, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/online-migration&#x60; endpoint. Migrating a cluster establishes a connection with an existing cluster and replicates its contents to the target cluster. Online migration is only available for MySQL, PostgreSQL, and Redis clusters.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void startOnlineMigrationTest() throws ApiException {
        UUID databaseClusterUuid = null;
        SourceDatabaseSource source = null;
        Boolean disableSsl = null;
        List<String> ignoreDbs = null;
        OnlineMigration response = api.startOnlineMigration(databaseClusterUuid)
                .source(source)
                .disableSsl(disableSsl)
                .ignoreDbs(ignoreDbs)
                .execute();
        // TODO: test validations
    }

    /**
     * Stop an Online Migration
     *
     * To stop an online migration, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/online-migration/$MIGRATION_ID&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void stopOnlineMigrationTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String migrationId = null;
        api.stopOnlineMigration(databaseClusterUuid, migrationId)
                .execute();
        // TODO: test validations
    }

    /**
     * Update the Database Configuration for an Existing Database
     *
     * To update the configuration for an existing database cluster, send a PATCH request to &#x60;/v2/databases/$DATABASE_ID/config&#x60;. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateConfigClusterTest() throws ApiException {
        UUID databaseClusterUuid = null;
        Mpr config = null;
        api.updateConfigCluster(databaseClusterUuid)
                .config(config)
                .execute();
        // TODO: test validations
    }

    /**
     * Update Connection Pools (PostgreSQL)
     *
     * To update a connection pool for a PostgreSQL database cluster, send a PUT request to  &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateConnectionPoolPostgresqlTest() throws ApiException {
        String mode = null;
        Integer size = null;
        String db = null;
        UUID databaseClusterUuid = null;
        String poolName = null;
        String user = null;
        api.updateConnectionPoolPostgresql(mode, size, db, databaseClusterUuid, poolName)
                .user(user)
                .execute();
        // TODO: test validations
    }

    /**
     * Update Firewall Rules (Trusted Sources) for a Database
     *
     * To update a database cluster&#39;s firewall rules (known as \&quot;trusted sources\&quot; in the control panel), send a PUT request to &#x60;/v2/databases/$DATABASE_ID/firewall&#x60; specifying which resources should be able to open connections to the database. You may limit connections to specific Droplets, Kubernetes clusters, or IP addresses. When a tag is provided, any Droplet or Kubernetes node with that tag applied to it will have access. The firewall is limited to 100 rules (or trusted sources). When possible, we recommend [placing your databases into a VPC network](https://www.digitalocean.com/docs/networking/vpc/) to limit access to them instead of using a firewall. A successful
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateFirewallRulesTest() throws ApiException {
        UUID databaseClusterUuid = null;
        List<FirewallRule> rules = null;
        api.updateFirewallRules(databaseClusterUuid)
                .rules(rules)
                .execute();
        // TODO: test validations
    }

    /**
     * Update Database Clusters&#39; Metrics Endpoint Credentials
     *
     * To update the credentials for all database clusters&#39; metrics endpoints, send a PUT request to &#x60;/v2/databases/metrics/credentials&#x60;. A successful request will receive a 204 No Content status code  with no body in response.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateMetricsCredentialsTest() throws ApiException {
        DatabasesBasicAuthCredentials credentials = null;
        api.updateMetricsCredentials()
                .credentials(credentials)
                .execute();
        // TODO: test validations
    }

    /**
     * Update a Database User
     *
     * To update an existing database user, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60; with the desired settings.  **Note**: only &#x60;settings&#x60; can be updated via this type of request. If you wish to change the name of a user, you must recreate a new user.  The response will be a JSON object with a key called &#x60;user&#x60;. The value of this will be an object that contains the name of the update database user, along with the &#x60;settings&#x60; object that has been updated. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateSettingsTest() throws ApiException {
        UserSettings settings = null;
        UUID databaseClusterUuid = null;
        String username = null;
        DatabasesAddUserResponse response = api.updateSettings(settings, databaseClusterUuid, username)
                .execute();
        // TODO: test validations
    }

    /**
     * Update SQL Mode for a Cluster
     *
     * To configure the SQL modes for an existing MySQL cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/sql_mode&#x60; specifying the desired modes. See the official MySQL 8 documentation for a [full list of supported SQL modes](https://dev.mysql.com/doc/refman/8.0/en/sql-mode.html#sql-mode-full). A successful request will receive a 204 No Content status code with no body in response.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateSqlModeTest() throws ApiException {
        String sqlMode = null;
        UUID databaseClusterUuid = null;
        api.updateSqlMode(sqlMode, databaseClusterUuid)
                .execute();
        // TODO: test validations
    }

    /**
     * Update Topic for a Kafka Cluster
     *
     * To update a topic attached to a Kafka cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void updateTopicKafkaClusterTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String topicName = null;
        Integer replicationFactor = null;
        Integer partitionCount = null;
        KafkaTopicConfig config = null;
        DatabasesCreateTopicKafkaClusterResponse response = api.updateTopicKafkaCluster(databaseClusterUuid, topicName)
                .replicationFactor(replicationFactor)
                .partitionCount(partitionCount)
                .config(config)
                .execute();
        // TODO: test validations
    }

    /**
     * Upgrade Major Version for a Database
     *
     * To upgrade the major version of a database, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/upgrade&#x60;, specifying the target version. A successful request will receive a 204 No Content status code with no body in response.
     *
     * @throws ApiException if the Api call fails
     */
    @Test
    public void upgradeMajorVersionTest() throws ApiException {
        UUID databaseClusterUuid = null;
        String version = null;
        api.upgradeMajorVersion(databaseClusterUuid)
                .version(version)
                .execute();
        // TODO: test validations
    }

}
