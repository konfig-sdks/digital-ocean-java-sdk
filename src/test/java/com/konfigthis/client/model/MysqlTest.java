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


package com.konfigthis.client.model;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.konfigthis.client.model.MysqlPgbouncer;
import com.konfigthis.client.model.MysqlTimescaledb;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.IOException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 * Model tests for Mysql
 */
public class MysqlTest {
    private final Mysql model = new Mysql();

    /**
     * Model tests for Mysql
     */
    @Test
    public void testMysql() {
        // TODO: test Mysql
    }

    /**
     * Test the property 'backupHour'
     */
    @Test
    public void backupHourTest() {
        // TODO: test backupHour
    }

    /**
     * Test the property 'backupMinute'
     */
    @Test
    public void backupMinuteTest() {
        // TODO: test backupMinute
    }

    /**
     * Test the property 'sqlMode'
     */
    @Test
    public void sqlModeTest() {
        // TODO: test sqlMode
    }

    /**
     * Test the property 'connectTimeout'
     */
    @Test
    public void connectTimeoutTest() {
        // TODO: test connectTimeout
    }

    /**
     * Test the property 'defaultTimeZone'
     */
    @Test
    public void defaultTimeZoneTest() {
        // TODO: test defaultTimeZone
    }

    /**
     * Test the property 'groupConcatMaxLen'
     */
    @Test
    public void groupConcatMaxLenTest() {
        // TODO: test groupConcatMaxLen
    }

    /**
     * Test the property 'informationSchemaStatsExpiry'
     */
    @Test
    public void informationSchemaStatsExpiryTest() {
        // TODO: test informationSchemaStatsExpiry
    }

    /**
     * Test the property 'innodbFtMinTokenSize'
     */
    @Test
    public void innodbFtMinTokenSizeTest() {
        // TODO: test innodbFtMinTokenSize
    }

    /**
     * Test the property 'innodbFtServerStopwordTable'
     */
    @Test
    public void innodbFtServerStopwordTableTest() {
        // TODO: test innodbFtServerStopwordTable
    }

    /**
     * Test the property 'innodbLockWaitTimeout'
     */
    @Test
    public void innodbLockWaitTimeoutTest() {
        // TODO: test innodbLockWaitTimeout
    }

    /**
     * Test the property 'innodbLogBufferSize'
     */
    @Test
    public void innodbLogBufferSizeTest() {
        // TODO: test innodbLogBufferSize
    }

    /**
     * Test the property 'innodbOnlineAlterLogMaxSize'
     */
    @Test
    public void innodbOnlineAlterLogMaxSizeTest() {
        // TODO: test innodbOnlineAlterLogMaxSize
    }

    /**
     * Test the property 'innodbPrintAllDeadlocks'
     */
    @Test
    public void innodbPrintAllDeadlocksTest() {
        // TODO: test innodbPrintAllDeadlocks
    }

    /**
     * Test the property 'innodbRollbackOnTimeout'
     */
    @Test
    public void innodbRollbackOnTimeoutTest() {
        // TODO: test innodbRollbackOnTimeout
    }

    /**
     * Test the property 'interactiveTimeout'
     */
    @Test
    public void interactiveTimeoutTest() {
        // TODO: test interactiveTimeout
    }

    /**
     * Test the property 'internalTmpMemStorageEngine'
     */
    @Test
    public void internalTmpMemStorageEngineTest() {
        // TODO: test internalTmpMemStorageEngine
    }

    /**
     * Test the property 'netReadTimeout'
     */
    @Test
    public void netReadTimeoutTest() {
        // TODO: test netReadTimeout
    }

    /**
     * Test the property 'netWriteTimeout'
     */
    @Test
    public void netWriteTimeoutTest() {
        // TODO: test netWriteTimeout
    }

    /**
     * Test the property 'sqlRequirePrimaryKey'
     */
    @Test
    public void sqlRequirePrimaryKeyTest() {
        // TODO: test sqlRequirePrimaryKey
    }

    /**
     * Test the property 'waitTimeout'
     */
    @Test
    public void waitTimeoutTest() {
        // TODO: test waitTimeout
    }

    /**
     * Test the property 'maxAllowedPacket'
     */
    @Test
    public void maxAllowedPacketTest() {
        // TODO: test maxAllowedPacket
    }

    /**
     * Test the property 'maxHeapTableSize'
     */
    @Test
    public void maxHeapTableSizeTest() {
        // TODO: test maxHeapTableSize
    }

    /**
     * Test the property 'sortBufferSize'
     */
    @Test
    public void sortBufferSizeTest() {
        // TODO: test sortBufferSize
    }

    /**
     * Test the property 'tmpTableSize'
     */
    @Test
    public void tmpTableSizeTest() {
        // TODO: test tmpTableSize
    }

    /**
     * Test the property 'slowQueryLog'
     */
    @Test
    public void slowQueryLogTest() {
        // TODO: test slowQueryLog
    }

    /**
     * Test the property 'longQueryTime'
     */
    @Test
    public void longQueryTimeTest() {
        // TODO: test longQueryTime
    }

    /**
     * Test the property 'binlogRetentionPeriod'
     */
    @Test
    public void binlogRetentionPeriodTest() {
        // TODO: test binlogRetentionPeriod
    }

    /**
     * Test the property 'innodbChangeBufferMaxSize'
     */
    @Test
    public void innodbChangeBufferMaxSizeTest() {
        // TODO: test innodbChangeBufferMaxSize
    }

    /**
     * Test the property 'innodbFlushNeighbors'
     */
    @Test
    public void innodbFlushNeighborsTest() {
        // TODO: test innodbFlushNeighbors
    }

    /**
     * Test the property 'innodbReadIoThreads'
     */
    @Test
    public void innodbReadIoThreadsTest() {
        // TODO: test innodbReadIoThreads
    }

    /**
     * Test the property 'innodbWriteIoThreads'
     */
    @Test
    public void innodbWriteIoThreadsTest() {
        // TODO: test innodbWriteIoThreads
    }

    /**
     * Test the property 'innodbThreadConcurrency'
     */
    @Test
    public void innodbThreadConcurrencyTest() {
        // TODO: test innodbThreadConcurrency
    }

    /**
     * Test the property 'netBufferLength'
     */
    @Test
    public void netBufferLengthTest() {
        // TODO: test netBufferLength
    }

    /**
     * Test the property 'autovacuumFreezeMaxAge'
     */
    @Test
    public void autovacuumFreezeMaxAgeTest() {
        // TODO: test autovacuumFreezeMaxAge
    }

    /**
     * Test the property 'autovacuumMaxWorkers'
     */
    @Test
    public void autovacuumMaxWorkersTest() {
        // TODO: test autovacuumMaxWorkers
    }

    /**
     * Test the property 'autovacuumNaptime'
     */
    @Test
    public void autovacuumNaptimeTest() {
        // TODO: test autovacuumNaptime
    }

    /**
     * Test the property 'autovacuumVacuumThreshold'
     */
    @Test
    public void autovacuumVacuumThresholdTest() {
        // TODO: test autovacuumVacuumThreshold
    }

    /**
     * Test the property 'autovacuumAnalyzeThreshold'
     */
    @Test
    public void autovacuumAnalyzeThresholdTest() {
        // TODO: test autovacuumAnalyzeThreshold
    }

    /**
     * Test the property 'autovacuumVacuumScaleFactor'
     */
    @Test
    public void autovacuumVacuumScaleFactorTest() {
        // TODO: test autovacuumVacuumScaleFactor
    }

    /**
     * Test the property 'autovacuumAnalyzeScaleFactor'
     */
    @Test
    public void autovacuumAnalyzeScaleFactorTest() {
        // TODO: test autovacuumAnalyzeScaleFactor
    }

    /**
     * Test the property 'autovacuumVacuumCostDelay'
     */
    @Test
    public void autovacuumVacuumCostDelayTest() {
        // TODO: test autovacuumVacuumCostDelay
    }

    /**
     * Test the property 'autovacuumVacuumCostLimit'
     */
    @Test
    public void autovacuumVacuumCostLimitTest() {
        // TODO: test autovacuumVacuumCostLimit
    }

    /**
     * Test the property 'bgwriterDelay'
     */
    @Test
    public void bgwriterDelayTest() {
        // TODO: test bgwriterDelay
    }

    /**
     * Test the property 'bgwriterFlushAfter'
     */
    @Test
    public void bgwriterFlushAfterTest() {
        // TODO: test bgwriterFlushAfter
    }

    /**
     * Test the property 'bgwriterLruMaxpages'
     */
    @Test
    public void bgwriterLruMaxpagesTest() {
        // TODO: test bgwriterLruMaxpages
    }

    /**
     * Test the property 'bgwriterLruMultiplier'
     */
    @Test
    public void bgwriterLruMultiplierTest() {
        // TODO: test bgwriterLruMultiplier
    }

    /**
     * Test the property 'deadlockTimeout'
     */
    @Test
    public void deadlockTimeoutTest() {
        // TODO: test deadlockTimeout
    }

    /**
     * Test the property 'defaultToastCompression'
     */
    @Test
    public void defaultToastCompressionTest() {
        // TODO: test defaultToastCompression
    }

    /**
     * Test the property 'idleInTransactionSessionTimeout'
     */
    @Test
    public void idleInTransactionSessionTimeoutTest() {
        // TODO: test idleInTransactionSessionTimeout
    }

    /**
     * Test the property 'jit'
     */
    @Test
    public void jitTest() {
        // TODO: test jit
    }

    /**
     * Test the property 'logAutovacuumMinDuration'
     */
    @Test
    public void logAutovacuumMinDurationTest() {
        // TODO: test logAutovacuumMinDuration
    }

    /**
     * Test the property 'logErrorVerbosity'
     */
    @Test
    public void logErrorVerbosityTest() {
        // TODO: test logErrorVerbosity
    }

    /**
     * Test the property 'logLinePrefix'
     */
    @Test
    public void logLinePrefixTest() {
        // TODO: test logLinePrefix
    }

    /**
     * Test the property 'logMinDurationStatement'
     */
    @Test
    public void logMinDurationStatementTest() {
        // TODO: test logMinDurationStatement
    }

    /**
     * Test the property 'maxFilesPerProcess'
     */
    @Test
    public void maxFilesPerProcessTest() {
        // TODO: test maxFilesPerProcess
    }

    /**
     * Test the property 'maxPreparedTransactions'
     */
    @Test
    public void maxPreparedTransactionsTest() {
        // TODO: test maxPreparedTransactions
    }

    /**
     * Test the property 'maxPredLocksPerTransaction'
     */
    @Test
    public void maxPredLocksPerTransactionTest() {
        // TODO: test maxPredLocksPerTransaction
    }

    /**
     * Test the property 'maxLocksPerTransaction'
     */
    @Test
    public void maxLocksPerTransactionTest() {
        // TODO: test maxLocksPerTransaction
    }

    /**
     * Test the property 'maxStackDepth'
     */
    @Test
    public void maxStackDepthTest() {
        // TODO: test maxStackDepth
    }

    /**
     * Test the property 'maxStandbyArchiveDelay'
     */
    @Test
    public void maxStandbyArchiveDelayTest() {
        // TODO: test maxStandbyArchiveDelay
    }

    /**
     * Test the property 'maxStandbyStreamingDelay'
     */
    @Test
    public void maxStandbyStreamingDelayTest() {
        // TODO: test maxStandbyStreamingDelay
    }

    /**
     * Test the property 'maxReplicationSlots'
     */
    @Test
    public void maxReplicationSlotsTest() {
        // TODO: test maxReplicationSlots
    }

    /**
     * Test the property 'maxLogicalReplicationWorkers'
     */
    @Test
    public void maxLogicalReplicationWorkersTest() {
        // TODO: test maxLogicalReplicationWorkers
    }

    /**
     * Test the property 'maxParallelWorkers'
     */
    @Test
    public void maxParallelWorkersTest() {
        // TODO: test maxParallelWorkers
    }

    /**
     * Test the property 'maxParallelWorkersPerGather'
     */
    @Test
    public void maxParallelWorkersPerGatherTest() {
        // TODO: test maxParallelWorkersPerGather
    }

    /**
     * Test the property 'maxWorkerProcesses'
     */
    @Test
    public void maxWorkerProcessesTest() {
        // TODO: test maxWorkerProcesses
    }

    /**
     * Test the property 'pgPartmanBgwRole'
     */
    @Test
    public void pgPartmanBgwRoleTest() {
        // TODO: test pgPartmanBgwRole
    }

    /**
     * Test the property 'pgPartmanBgwInterval'
     */
    @Test
    public void pgPartmanBgwIntervalTest() {
        // TODO: test pgPartmanBgwInterval
    }

    /**
     * Test the property 'pgStatStatementsTrack'
     */
    @Test
    public void pgStatStatementsTrackTest() {
        // TODO: test pgStatStatementsTrack
    }

    /**
     * Test the property 'tempFileLimit'
     */
    @Test
    public void tempFileLimitTest() {
        // TODO: test tempFileLimit
    }

    /**
     * Test the property 'timezone'
     */
    @Test
    public void timezoneTest() {
        // TODO: test timezone
    }

    /**
     * Test the property 'trackActivityQuerySize'
     */
    @Test
    public void trackActivityQuerySizeTest() {
        // TODO: test trackActivityQuerySize
    }

    /**
     * Test the property 'trackCommitTimestamp'
     */
    @Test
    public void trackCommitTimestampTest() {
        // TODO: test trackCommitTimestamp
    }

    /**
     * Test the property 'trackFunctions'
     */
    @Test
    public void trackFunctionsTest() {
        // TODO: test trackFunctions
    }

    /**
     * Test the property 'trackIoTiming'
     */
    @Test
    public void trackIoTimingTest() {
        // TODO: test trackIoTiming
    }

    /**
     * Test the property 'maxWalSenders'
     */
    @Test
    public void maxWalSendersTest() {
        // TODO: test maxWalSenders
    }

    /**
     * Test the property 'walSenderTimeout'
     */
    @Test
    public void walSenderTimeoutTest() {
        // TODO: test walSenderTimeout
    }

    /**
     * Test the property 'walWriterDelay'
     */
    @Test
    public void walWriterDelayTest() {
        // TODO: test walWriterDelay
    }

    /**
     * Test the property 'sharedBuffersPercentage'
     */
    @Test
    public void sharedBuffersPercentageTest() {
        // TODO: test sharedBuffersPercentage
    }

    /**
     * Test the property 'pgbouncer'
     */
    @Test
    public void pgbouncerTest() {
        // TODO: test pgbouncer
    }

    /**
     * Test the property 'workMem'
     */
    @Test
    public void workMemTest() {
        // TODO: test workMem
    }

    /**
     * Test the property 'timescaledb'
     */
    @Test
    public void timescaledbTest() {
        // TODO: test timescaledb
    }

    /**
     * Test the property 'synchronousReplication'
     */
    @Test
    public void synchronousReplicationTest() {
        // TODO: test synchronousReplication
    }

    /**
     * Test the property 'statMonitorEnable'
     */
    @Test
    public void statMonitorEnableTest() {
        // TODO: test statMonitorEnable
    }

    /**
     * Test the property 'redisMaxmemoryPolicy'
     */
    @Test
    public void redisMaxmemoryPolicyTest() {
        // TODO: test redisMaxmemoryPolicy
    }

    /**
     * Test the property 'redisPubsubClientOutputBufferLimit'
     */
    @Test
    public void redisPubsubClientOutputBufferLimitTest() {
        // TODO: test redisPubsubClientOutputBufferLimit
    }

    /**
     * Test the property 'redisNumberOfDatabases'
     */
    @Test
    public void redisNumberOfDatabasesTest() {
        // TODO: test redisNumberOfDatabases
    }

    /**
     * Test the property 'redisIoThreads'
     */
    @Test
    public void redisIoThreadsTest() {
        // TODO: test redisIoThreads
    }

    /**
     * Test the property 'redisLfuLogFactor'
     */
    @Test
    public void redisLfuLogFactorTest() {
        // TODO: test redisLfuLogFactor
    }

    /**
     * Test the property 'redisLfuDecayTime'
     */
    @Test
    public void redisLfuDecayTimeTest() {
        // TODO: test redisLfuDecayTime
    }

    /**
     * Test the property 'redisSsl'
     */
    @Test
    public void redisSslTest() {
        // TODO: test redisSsl
    }

    /**
     * Test the property 'redisTimeout'
     */
    @Test
    public void redisTimeoutTest() {
        // TODO: test redisTimeout
    }

    /**
     * Test the property 'redisNotifyKeyspaceEvents'
     */
    @Test
    public void redisNotifyKeyspaceEventsTest() {
        // TODO: test redisNotifyKeyspaceEvents
    }

    /**
     * Test the property 'redisPersistence'
     */
    @Test
    public void redisPersistenceTest() {
        // TODO: test redisPersistence
    }

    /**
     * Test the property 'redisAclChannelsDefault'
     */
    @Test
    public void redisAclChannelsDefaultTest() {
        // TODO: test redisAclChannelsDefault
    }

}
