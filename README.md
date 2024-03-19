<div align="left">

[![Visit Digitalocean](./header.png)](https://digitalocean.com)

# [Digitalocean](https://digitalocean.com)

# Introduction

The DigitalOcean API allows you to manage Droplets and resources within the
DigitalOcean cloud in a simple, programmatic way using conventional HTTP requests.

All of the functionality that you are familiar with in the DigitalOcean
control panel is also available through the API, allowing you to script the
complex actions that your situation requires.

The API documentation will start with a general overview about the design
and technology that has been implemented, followed by reference information
about specific endpoints.

## Requests

Any tool that is fluent in HTTP can communicate with the API simply by
requesting the correct URI. Requests should be made using the HTTPS protocol
so that traffic is encrypted. The interface responds to different methods
depending on the action required.

|Method|Usage|
|--- |--- |
|GET|For simple retrieval of information about your account, Droplets, or environment, you should use the GET method.  The information you request will be returned to you as a JSON object. The attributes defined by the JSON object can be used to form additional requests.  Any request using the GET method is read-only and will not affect any of the objects you are querying.|
|DELETE|To destroy a resource and remove it from your account and environment, the DELETE method should be used.  This will remove the specified object if it is found.  If it is not found, the operation will return a response indicating that the object was not found. This idempotency means that you do not have to check for a resource's availability prior to issuing a delete command, the final state will be the same regardless of its existence.|
|PUT|To update the information about a resource in your account, the PUT method is available. Like the DELETE Method, the PUT method is idempotent.  It sets the state of the target using the provided values, regardless of their current values. Requests using the PUT method do not need to check the current attributes of the object.|
|PATCH|Some resources support partial modification. In these cases, the PATCH method is available. Unlike PUT which generally requires a complete representation of a resource, a PATCH request is is a set of instructions on how to modify a resource updating only specific attributes.|
|POST|To create a new object, your request should specify the POST method. The POST request includes all of the attributes necessary to create a new object.  When you wish to create a new object, send a POST request to the target endpoint.|
|HEAD|Finally, to retrieve metadata information, you should use the HEAD method to get the headers.  This returns only the header of what would be returned with an associated GET request. Response headers contain some useful information about your API access and the results that are available for your request. For instance, the headers contain your current rate-limit value and the amount of time available until the limit resets. It also contains metrics about the total number of objects found, pagination information, and the total content length.|


## HTTP Statuses

Along with the HTTP methods that the API responds to, it will also return
standard HTTP statuses, including error codes.

In the event of a problem, the status will contain the error code, while the
body of the response will usually contain additional information about the
problem that was encountered.

In general, if the status returned is in the 200 range, it indicates that
the request was fulfilled successfully and that no error was encountered.

Return codes in the 400 range typically indicate that there was an issue
with the request that was sent. Among other things, this could mean that you
did not authenticate correctly, that you are requesting an action that you
do not have authorization for, that the object you are requesting does not
exist, or that your request is malformed.

If you receive a status in the 500 range, this generally indicates a
server-side problem. This means that we are having an issue on our end and
cannot fulfill your request currently.

400 and 500 level error responses will include a JSON object in their body,
including the following attributes:

|Name|Type|Description|
|--- |--- |--- |
|id|string|A short identifier corresponding to the HTTP status code returned. For example, the ID for a response returning a 404 status code would be "not_found."|
|message|string|A message providing additional information about the error, including details to help resolve it when possible.|
|request_id|string|Optionally, some endpoints may include a request ID that should be provided when reporting bugs or opening support tickets to help identify the issue.|

### Example Error Response

```
    HTTP/1.1 403 Forbidden
    {
      "id":       "forbidden",
      "message":  "You do not have access for the attempted action."
    }
```

## Responses

When a request is successful, a response body will typically be sent back in
the form of a JSON object. An exception to this is when a DELETE request is
processed, which will result in a successful HTTP 204 status and an empty
response body.

Inside of this JSON object, the resource root that was the target of the
request will be set as the key. This will be the singular form of the word
if the request operated on a single object, and the plural form of the word
if a collection was processed.

For example, if you send a GET request to `/v2/droplets/$DROPLET_ID` you
will get back an object with a key called "`droplet`". However, if you send
the GET request to the general collection at `/v2/droplets`, you will get
back an object with a key called "`droplets`".

The value of these keys will generally be a JSON object for a request on a
single object and an array of objects for a request on a collection of
objects.

### Response for a Single Object

```
    {
        "droplet": {
            "name": "example.com"
            . . .
        }
    }
```

### Response for an Object Collection

```
    {
        "droplets": [
            {
                "name": "example.com"
                . . .
            },
            {
                "name": "second.com"
                . . .
            }
        ]
    }
```

## Meta

In addition to the main resource root, the response may also contain a
`meta` object. This object contains information about the response itself.

The `meta` object contains a `total` key that is set to the total number of
objects returned by the request. This has implications on the `links` object
and pagination.

The `meta` object will only be displayed when it has a value. Currently, the
`meta` object will have a value when a request is made on a collection (like
`droplets` or `domains`).


### Sample Meta Object

```
    {
        . . .
        "meta": {
            "total": 43
        }
        . . .
    }
```

## Links & Pagination

The `links` object is returned as part of the response body when pagination
is enabled. By default, 20 objects are returned per page. If the response
contains 20 objects or fewer, no `links` object will be returned. If the
response contains more than 20 objects, the first 20 will be returned along
with the `links` object.

You can request a different pagination limit or force pagination by
appending `?per_page=` to the request with the number of items you would
like per page. For instance, to show only two results per page, you could
add `?per_page=2` to the end of your query. The maximum number of results
per page is 200.

The `links` object contains a `pages` object. The `pages` object, in turn,
contains keys indicating the relationship of additional pages. The values of
these are the URLs of the associated pages. The keys will be one of the
following:

*   **first**: The URI of the first page of results.
*   **prev**: The URI of the previous sequential page of results.
*   **next**: The URI of the next sequential page of results.
*   **last**: The URI of the last page of results.

The `pages` object will only include the links that make sense. So for the
first page of results, no `first` or `prev` links will ever be set. This
convention holds true in other situations where a link would not make sense.

### Sample Links Object

```
    {
        . . .
        "links": {
            "pages": {
                "last": "https://api.digitalocean.com/v2/images?page=2",
                "next": "https://api.digitalocean.com/v2/images?page=2"
            }
        }
        . . .
    }
```

## Rate Limit

Requests through the API are rate limited per OAuth token. Current rate limits:

*   5,000 requests per hour
*   250 requests per minute (5% of the hourly total)

Once you exceed either limit, you will be rate limited until the next cycle
starts. Space out any requests that you would otherwise issue in bursts for
the best results.

The rate limiting information is contained within the response headers of
each request. The relevant headers are:

*   **ratelimit-limit**: The number of requests that can be made per hour.
*   **ratelimit-remaining**: The number of requests that remain before you hit your request limit. See the information below for how the request limits expire.
*   **ratelimit-reset**: This represents the time when the oldest request will expire. The value is given in [Unix epoch time](http://en.wikipedia.org/wiki/Unix_time). See below for more information about how request limits expire.

More rate limiting information is returned only within burst limit error response headers:
*   **retry-after**: The number of seconds to wait before making another request when rate limited.

As long as the `ratelimit-remaining` count is above zero, you will be able
to make additional requests.

The way that a request expires and is removed from the current limit count
is important to understand. Rather than counting all of the requests for an
hour and resetting the `ratelimit-remaining` value at the end of the hour,
each request instead has its own timer.

This means that each request contributes toward the `ratelimit-remaining`
count for one complete hour after the request is made. When that request's
timer runs out, it is no longer counted towards the request limit.

This has implications on the meaning of the `ratelimit-reset` header as
well. Because the entire rate limit is not reset at one time, the value of
this header is set to the time when the _oldest_ request will expire.

Keep this in mind if you see your `ratelimit-reset` value change, but not
move an entire hour into the future.

If the `ratelimit-remaining` reaches zero, subsequent requests will receive
a 429 error code until the request reset has been reached. 

`ratelimit-remaining` reaching zero can also indicate that the "burst limit" of 250 
requests per minute limit was met, even if the 5,000 requests per hour limit was not. 
In this case, the 429 error response will include a retry-after header to indicate how 
long to wait (in seconds) until the request may be retried.

You can see the format of the response in the examples. 

**Note:** The following endpoints have special rate limit requirements that
are independent of the limits defined above.

*   Only 12 `POST` requests to the `/v2/floating_ips` endpoint to create Floating IPs can be made per 60 seconds.
*   Only 10 `GET` requests to the `/v2/account/keys` endpoint to list SSH keys can be made per 60 seconds.
*   Only 5 requests to any and all `v2/cdn/endpoints` can be made per 10 seconds. This includes `v2/cdn/endpoints`, 
    `v2/cdn/endpoints/$ENDPOINT_ID`, and `v2/cdn/endpoints/$ENDPOINT_ID/cache`.
*   Only 50 strings within the `files` json struct in the `v2/cdn/endpoints/$ENDPOINT_ID/cache` [payload](https://docs.digitalocean.com/reference/api/api-reference/#operation/cdn_purge_cache) 
    can be requested every 20 seconds.

### Sample Rate Limit Headers

```
    . . .
    ratelimit-limit: 1200
    ratelimit-remaining: 1193
    rateLimit-reset: 1402425459
    . . .
```

  ### Sample Rate Limit Headers When Burst Limit is Reached:

```
    . . .
    ratelimit-limit: 5000
    ratelimit-remaining: 0
    rateLimit-reset: 1402425459
    retry-after: 29
    . . .
```

### Sample Rate Exceeded Response

```
    429 Too Many Requests
    {
            id: "too_many_requests",
            message: "API Rate limit exceeded."
    }
```

## Curl Examples

Throughout this document, some example API requests will be given using the
`curl` command. This will allow us to demonstrate the various endpoints in a
simple, textual format.
  
  These examples assume that you are using a Linux or macOS command line. To run
these commands on a Windows machine, you can either use cmd.exe, PowerShell, or WSL:

* For cmd.exe, use the `set VAR=VALUE` [syntax](https://docs.microsoft.com/en-us/windows-server/administration/windows-commands/set_1)
to define environment variables, call them with `%VAR%`, then replace all backslashes (`\`) in the examples with carets (`^`).

* For PowerShell, use the `$Env:VAR = "VALUE"` [syntax](https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.core/about/about_environment_variables?view=powershell-7.2)
to define environment variables, call them with `$Env:VAR`, then replace `curl` with `curl.exe` and all backslashes (`\`) in the examples with backticks (`` ` ``).

* WSL is a compatibility layer that allows you to emulate a Linux terminal on a Windows machine.
Install WSL with our [community tutorial](https://www.digitalocean.com/community/tutorials/how-to-install-the-windows-subsystem-for-linux-2-on-microsoft-windows-10), 
then follow this API documentation normally.

The names of account-specific references (like Droplet IDs, for instance)
will be represented by variables. For instance, a Droplet ID may be
represented by a variable called `$DROPLET_ID`. You can set the associated
variables in your environment if you wish to use the examples without
modification.

The first variable that you should set to get started is your OAuth
authorization token. The next section will go over the details of this, but
you can set an environmental variable for it now.

Generate a token by going to the [Apps & API](https://cloud.digitalocean.com/settings/applications)
section of the DigitalOcean control panel. Use an existing token if you have
saved one, or generate a new token with the "Generate new token" button.
Copy the generated token and use it to set and export the TOKEN variable in
your environment as the example shows.

You may also wish to set some other variables now or as you go along. For
example, you may wish to set the `DROPLET_ID` variable to one of your
Droplet IDs since this will be used frequently in the API.

If you are following along, make sure you use a Droplet ID that you control
so that your commands will execute correctly.

If you need access to the headers of a response through `curl`, you can pass
the `-i` flag to display the header information along with the body. If you
are only interested in the header, you can instead pass the `-I` flag, which
will exclude the response body entirely.


### Set and Export your OAuth Token

```
export DIGITALOCEAN_TOKEN=your_token_here
```

### Set and Export a Variable

```
export DROPLET_ID=1111111
```

## Parameters

There are two different ways to pass parameters in a request with the API.

When passing parameters to create or update an object, parameters should be
passed as a JSON object containing the appropriate attribute names and
values as key-value pairs. When you use this format, you should specify that
you are sending a JSON object in the header. This is done by setting the
`Content-Type` header to `application/json`. This ensures that your request
is interpreted correctly.

When passing parameters to filter a response on GET requests, parameters can
be passed using standard query attributes. In this case, the parameters
would be embedded into the URI itself by appending a `?` to the end of the
URI and then setting each attribute with an equal sign. Attributes can be
separated with a `&`. Tools like `curl` can create the appropriate URI when
given parameters and values; this can also be done using the `-F` flag and
then passing the key and value as an argument. The argument should take the
form of a quoted string with the attribute being set to a value with an
equal sign.

### Pass Parameters as a JSON Object

```
    curl -H "Authorization: Bearer $DIGITALOCEAN_TOKEN" \
        -H "Content-Type: application/json" \
        -d '{"name": "example.com", "ip_address": "127.0.0.1"}' \
        -X POST "https://api.digitalocean.com/v2/domains"
```

### Pass Filter Parameters as a Query String

```
     curl -H "Authorization: Bearer $DIGITALOCEAN_TOKEN" \
         -X GET \
         "https://api.digitalocean.com/v2/images?private=true"
```

## Cross Origin Resource Sharing

In order to make requests to the API from other domains, the API implements
Cross Origin Resource Sharing (CORS) support.

CORS support is generally used to create AJAX requests outside of the domain
that the request originated from. This is necessary to implement projects
like control panels utilizing the API. This tells the browser that it can
send requests to an outside domain.

The procedure that the browser initiates in order to perform these actions
(other than GET requests) begins by sending a "preflight" request. This sets
the `Origin` header and uses the `OPTIONS` method. The server will reply
back with the methods it allows and some of the limits it imposes. The
client then sends the actual request if it falls within the allowed
constraints.

This process is usually done in the background by the browser, but you can
use curl to emulate this process using the example provided. The headers
that will be set to show the constraints are:

*   **Access-Control-Allow-Origin**: This is the domain that is sent by the client or browser as the origin of the request. It is set through an `Origin` header.
*   **Access-Control-Allow-Methods**: This specifies the allowed options for requests from that domain. This will generally be all available methods.
*   **Access-Control-Expose-Headers**: This will contain the headers that will be available to requests from the origin domain.
*   **Access-Control-Max-Age**: This is the length of time that the access is considered valid. After this expires, a new preflight should be sent.
*   **Access-Control-Allow-Credentials**: This will be set to `true`. It basically allows you to send your OAuth token for authentication.

You should not need to be concerned with the details of these headers,
because the browser will typically do all of the work for you.


</div>

## Requirements

Building the API client library requires:

1. Java 1.8+
2. Maven (3.8.3+)/Gradle (7.2+)

If you are adding this library to an Android Application or Library:

3. Android 8.0+ (API Level 26+)

## Installation<a id="installation"></a>
<div align="center">
  <a href="https://konfigthis.com/sdk-sign-up?company=DigitalOcean&language=Java">
    <img src="https://raw.githubusercontent.com/konfig-dev/brand-assets/HEAD/cta-images/java-cta.png" width="70%">
  </a>
</div>

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>com.konfigthis</groupId>
  <artifactId>digital-ocean-java-sdk</artifactId>
  <version>2.0</version>
  <scope>compile</scope>
</dependency>
```

### Gradle users

Add this dependency to your `build.gradle`:

```groovy
// build.gradle
repositories {
  mavenCentral()
}

dependencies {
   implementation "com.konfigthis:digital-ocean-java-sdk:2.0"
}
```

### Android users

Make sure your `build.gradle` file as a `minSdk` version of at least 26:
```groovy
// build.gradle
android {
    defaultConfig {
        minSdk 26
    }
}
```

Also make sure your library or application has internet permissions in your `AndroidManifest.xml`:

```xml
<!--AndroidManifest.xml-->
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
    <uses-permission android:name="android.permission.INTERNET"/>
</manifest>
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/digital-ocean-java-sdk-2.0.jar`
* `target/lib/*.jar`

## Getting Started

Please follow the [installation](#installation) instruction and execute the following Java code:

```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.Class1ClickApplicationsApi;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Example {
  public static void main(String[] args) {
    Configuration configuration = new Configuration();
    configuration.host = "https://api.digitalocean.com";
    
    // Configure HTTP bearer authorization: bearer_auth
    configuration.token = "BEARER TOKEN";
    DigitalOcean client = new DigitalOcean(configuration);
    List<String> addonSlugs = Arrays.asList(); // An array of 1-Click Application slugs to be installed to the Kubernetes cluster.
    String clusterUuid = "clusterUuid_example"; // A unique ID for the Kubernetes cluster to which the 1-Click Applications will be installed.
    try {
      Model1ClickApplicationsInstallKubernetesApplicationResponse result = client
              .class1ClickApplications
              .installKubernetesApplication(addonSlugs, clusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getMessage());
    } catch (ApiException e) {
      System.err.println("Exception when calling Class1ClickApplicationsApi#installKubernetesApplication");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Model1ClickApplicationsInstallKubernetesApplicationResponse> response = client
              .class1ClickApplications
              .installKubernetesApplication(addonSlugs, clusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling Class1ClickApplicationsApi#installKubernetesApplication");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

## Documentation for API Endpoints

All URIs are relative to *https://api.digitalocean.com*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*Class1ClickApplicationsApi* | [**installKubernetesApplication**](docs/Class1ClickApplicationsApi.md#installKubernetesApplication) | **POST** /v2/1-clicks/kubernetes | Install Kubernetes 1-Click Applications
*Class1ClickApplicationsApi* | [**list**](docs/Class1ClickApplicationsApi.md#list) | **GET** /v2/1-clicks | List 1-Click Applications
*AccountApi* | [**get**](docs/AccountApi.md#get) | **GET** /v2/account | Get User Information
*ActionsApi* | [**get**](docs/ActionsApi.md#get) | **GET** /v2/actions/{action_id} | Retrieve an Existing Action
*ActionsApi* | [**list**](docs/ActionsApi.md#list) | **GET** /v2/actions | List All Actions
*AppsApi* | [**cancelDeployment**](docs/AppsApi.md#cancelDeployment) | **POST** /v2/apps/{app_id}/deployments/{deployment_id}/cancel | Cancel a Deployment
*AppsApi* | [**commitRollback**](docs/AppsApi.md#commitRollback) | **POST** /v2/apps/{app_id}/rollback/commit | Commit App Rollback
*AppsApi* | [**create**](docs/AppsApi.md#create) | **POST** /v2/apps | Create a New App
*AppsApi* | [**createDeployment**](docs/AppsApi.md#createDeployment) | **POST** /v2/apps/{app_id}/deployments | Create an App Deployment
*AppsApi* | [**delete**](docs/AppsApi.md#delete) | **DELETE** /v2/apps/{id} | Delete an App
*AppsApi* | [**get**](docs/AppsApi.md#get) | **GET** /v2/apps/{id} | Retrieve an Existing App
*AppsApi* | [**getActiveDeploymentLogs**](docs/AppsApi.md#getActiveDeploymentLogs) | **GET** /v2/apps/{app_id}/components/{component_name}/logs | Retrieve Active Deployment Logs
*AppsApi* | [**getActiveDeploymentLogs_0**](docs/AppsApi.md#getActiveDeploymentLogs_0) | **GET** /v2/apps/{app_id}/logs | Retrieve Active Deployment Aggregate Logs
*AppsApi* | [**getAggregateDeploymentLogs**](docs/AppsApi.md#getAggregateDeploymentLogs) | **GET** /v2/apps/{app_id}/deployments/{deployment_id}/logs | Retrieve Aggregate Deployment Logs
*AppsApi* | [**getAppDailyBandwidthMetrics**](docs/AppsApi.md#getAppDailyBandwidthMetrics) | **GET** /v2/apps/{app_id}/metrics/bandwidth_daily | Retrieve App Daily Bandwidth Metrics
*AppsApi* | [**getDeploymentInfo**](docs/AppsApi.md#getDeploymentInfo) | **GET** /v2/apps/{app_id}/deployments/{deployment_id} | Retrieve an App Deployment
*AppsApi* | [**getDeploymentLogs**](docs/AppsApi.md#getDeploymentLogs) | **GET** /v2/apps/{app_id}/deployments/{deployment_id}/components/{component_name}/logs | Retrieve Deployment Logs
*AppsApi* | [**getInstanceSize**](docs/AppsApi.md#getInstanceSize) | **GET** /v2/apps/tiers/instance_sizes/{slug} | Retrieve an Instance Size
*AppsApi* | [**getMultipleDailyMetrics**](docs/AppsApi.md#getMultipleDailyMetrics) | **POST** /v2/apps/metrics/bandwidth_daily | Retrieve Multiple Apps&#39; Daily Bandwidth Metrics
*AppsApi* | [**getTierInfo**](docs/AppsApi.md#getTierInfo) | **GET** /v2/apps/tiers/{slug} | Retrieve an App Tier
*AppsApi* | [**list**](docs/AppsApi.md#list) | **GET** /v2/apps | List All Apps
*AppsApi* | [**listAlerts**](docs/AppsApi.md#listAlerts) | **GET** /v2/apps/{app_id}/alerts | List all app alerts
*AppsApi* | [**listDeployments**](docs/AppsApi.md#listDeployments) | **GET** /v2/apps/{app_id}/deployments | List App Deployments
*AppsApi* | [**listInstanceSizes**](docs/AppsApi.md#listInstanceSizes) | **GET** /v2/apps/tiers/instance_sizes | List Instance Sizes
*AppsApi* | [**listRegions**](docs/AppsApi.md#listRegions) | **GET** /v2/apps/regions | List App Regions
*AppsApi* | [**listTiers**](docs/AppsApi.md#listTiers) | **GET** /v2/apps/tiers | List App Tiers
*AppsApi* | [**proposeAppSpec**](docs/AppsApi.md#proposeAppSpec) | **POST** /v2/apps/propose | Propose an App Spec
*AppsApi* | [**revertRollback**](docs/AppsApi.md#revertRollback) | **POST** /v2/apps/{app_id}/rollback/revert | Revert App Rollback
*AppsApi* | [**rollbackDeployment**](docs/AppsApi.md#rollbackDeployment) | **POST** /v2/apps/{app_id}/rollback | Rollback App
*AppsApi* | [**update**](docs/AppsApi.md#update) | **PUT** /v2/apps/{id} | Update an App
*AppsApi* | [**updateDestinationsForAlerts**](docs/AppsApi.md#updateDestinationsForAlerts) | **POST** /v2/apps/{app_id}/alerts/{alert_id}/destinations | Update destinations for alerts
*AppsApi* | [**validateRollback**](docs/AppsApi.md#validateRollback) | **POST** /v2/apps/{app_id}/rollback/validate | Validate App Rollback
*BillingApi* | [**get**](docs/BillingApi.md#get) | **GET** /v2/customers/my/balance | Get Customer Balance
*BillingApi* | [**getInvoiceByUuid**](docs/BillingApi.md#getInvoiceByUuid) | **GET** /v2/customers/my/invoices/{invoice_uuid} | Retrieve an Invoice by UUID
*BillingApi* | [**getInvoiceCsvByUuid**](docs/BillingApi.md#getInvoiceCsvByUuid) | **GET** /v2/customers/my/invoices/{invoice_uuid}/csv | Retrieve an Invoice CSV by UUID
*BillingApi* | [**getInvoiceSummaryByUuid**](docs/BillingApi.md#getInvoiceSummaryByUuid) | **GET** /v2/customers/my/invoices/{invoice_uuid}/summary | Retrieve an Invoice Summary by UUID
*BillingApi* | [**getPdfByUuid**](docs/BillingApi.md#getPdfByUuid) | **GET** /v2/customers/my/invoices/{invoice_uuid}/pdf | Retrieve an Invoice PDF by UUID
*BillingApi* | [**list**](docs/BillingApi.md#list) | **GET** /v2/customers/my/billing_history | List Billing History
*BillingApi* | [**list_0**](docs/BillingApi.md#list_0) | **GET** /v2/customers/my/invoices | List All Invoices
*BlockStorageApi* | [**create**](docs/BlockStorageApi.md#create) | **POST** /v2/volumes | Create a New Block Storage Volume
*BlockStorageApi* | [**create_0**](docs/BlockStorageApi.md#create_0) | **POST** /v2/volumes/{volume_id}/snapshots | Create Snapshot from a Volume
*BlockStorageApi* | [**delete**](docs/BlockStorageApi.md#delete) | **DELETE** /v2/volumes/{volume_id} | Delete a Block Storage Volume
*BlockStorageApi* | [**deleteByRegionAndName**](docs/BlockStorageApi.md#deleteByRegionAndName) | **DELETE** /v2/volumes | Delete a Block Storage Volume by Name
*BlockStorageApi* | [**deleteVolumeSnapshot**](docs/BlockStorageApi.md#deleteVolumeSnapshot) | **DELETE** /v2/volumes/snapshots/{snapshot_id} | Delete a Volume Snapshot
*BlockStorageApi* | [**get**](docs/BlockStorageApi.md#get) | **GET** /v2/volumes/{volume_id} | Retrieve an Existing Block Storage Volume
*BlockStorageApi* | [**getSnapshotDetails**](docs/BlockStorageApi.md#getSnapshotDetails) | **GET** /v2/volumes/snapshots/{snapshot_id} | Retrieve an Existing Volume Snapshot
*BlockStorageApi* | [**list**](docs/BlockStorageApi.md#list) | **GET** /v2/volumes | List All Block Storage Volumes
*BlockStorageApi* | [**list_0**](docs/BlockStorageApi.md#list_0) | **GET** /v2/volumes/{volume_id}/snapshots | List Snapshots for a Volume
*BlockStorageActionsApi* | [**get**](docs/BlockStorageActionsApi.md#get) | **GET** /v2/volumes/{volume_id}/actions/{action_id} | Retrieve an Existing Volume Action
*BlockStorageActionsApi* | [**initiateAttachAction**](docs/BlockStorageActionsApi.md#initiateAttachAction) | **POST** /v2/volumes/{volume_id}/actions | Initiate A Block Storage Action By Volume Id
*BlockStorageActionsApi* | [**list**](docs/BlockStorageActionsApi.md#list) | **GET** /v2/volumes/{volume_id}/actions | List All Actions for a Volume
*BlockStorageActionsApi* | [**post**](docs/BlockStorageActionsApi.md#post) | **POST** /v2/volumes/actions | Initiate A Block Storage Action By Volume Name
*CdnEndpointsApi* | [**createNewEndpoint**](docs/CdnEndpointsApi.md#createNewEndpoint) | **POST** /v2/cdn/endpoints | Create a New CDN Endpoint
*CdnEndpointsApi* | [**deleteEndpoint**](docs/CdnEndpointsApi.md#deleteEndpoint) | **DELETE** /v2/cdn/endpoints/{cdn_id} | Delete a CDN Endpoint
*CdnEndpointsApi* | [**getExistingEndpoint**](docs/CdnEndpointsApi.md#getExistingEndpoint) | **GET** /v2/cdn/endpoints/{cdn_id} | Retrieve an Existing CDN Endpoint
*CdnEndpointsApi* | [**listAll**](docs/CdnEndpointsApi.md#listAll) | **GET** /v2/cdn/endpoints | List All CDN Endpoints
*CdnEndpointsApi* | [**purgeCache**](docs/CdnEndpointsApi.md#purgeCache) | **DELETE** /v2/cdn/endpoints/{cdn_id}/cache | Purge the Cache for an Existing CDN Endpoint
*CdnEndpointsApi* | [**updateEndpoint**](docs/CdnEndpointsApi.md#updateEndpoint) | **PUT** /v2/cdn/endpoints/{cdn_id} | Update a CDN Endpoint
*CertificatesApi* | [**create**](docs/CertificatesApi.md#create) | **POST** /v2/certificates | Create a New Certificate
*CertificatesApi* | [**delete**](docs/CertificatesApi.md#delete) | **DELETE** /v2/certificates/{certificate_id} | Delete a Certificate
*CertificatesApi* | [**get**](docs/CertificatesApi.md#get) | **GET** /v2/certificates/{certificate_id} | Retrieve an Existing Certificate
*CertificatesApi* | [**list**](docs/CertificatesApi.md#list) | **GET** /v2/certificates | List All Certificates
*ContainerRegistryApi* | [**cancelGarbageCollection**](docs/ContainerRegistryApi.md#cancelGarbageCollection) | **PUT** /v2/registry/{registry_name}/garbage-collection/{garbage_collection_uuid} | Update Garbage Collection
*ContainerRegistryApi* | [**create**](docs/ContainerRegistryApi.md#create) | **POST** /v2/registry | Create Container Registry
*ContainerRegistryApi* | [**delete**](docs/ContainerRegistryApi.md#delete) | **DELETE** /v2/registry | Delete Container Registry
*ContainerRegistryApi* | [**deleteRepositoryManifestByDigest**](docs/ContainerRegistryApi.md#deleteRepositoryManifestByDigest) | **DELETE** /v2/registry/{registry_name}/repositories/{repository_name}/digests/{manifest_digest} | Delete Container Registry Repository Manifest
*ContainerRegistryApi* | [**deleteRepositoryTag**](docs/ContainerRegistryApi.md#deleteRepositoryTag) | **DELETE** /v2/registry/{registry_name}/repositories/{repository_name}/tags/{repository_tag} | Delete Container Registry Repository Tag
*ContainerRegistryApi* | [**get**](docs/ContainerRegistryApi.md#get) | **GET** /v2/registry | Get Container Registry Information
*ContainerRegistryApi* | [**getActiveGarbageCollection**](docs/ContainerRegistryApi.md#getActiveGarbageCollection) | **GET** /v2/registry/{registry_name}/garbage-collection | Get Active Garbage Collection
*ContainerRegistryApi* | [**getDockerCredentials**](docs/ContainerRegistryApi.md#getDockerCredentials) | **GET** /v2/registry/docker-credentials | Get Docker Credentials for Container Registry
*ContainerRegistryApi* | [**getSubscriptionInfo**](docs/ContainerRegistryApi.md#getSubscriptionInfo) | **GET** /v2/registry/subscription | Get Subscription Information
*ContainerRegistryApi* | [**listGarbageCollections**](docs/ContainerRegistryApi.md#listGarbageCollections) | **GET** /v2/registry/{registry_name}/garbage-collections | List Garbage Collections
*ContainerRegistryApi* | [**listOptions**](docs/ContainerRegistryApi.md#listOptions) | **GET** /v2/registry/options | List Registry Options (Subscription Tiers and Available Regions)
*ContainerRegistryApi* | [**listRepositories**](docs/ContainerRegistryApi.md#listRepositories) | **GET** /v2/registry/{registry_name}/repositories | List All Container Registry Repositories
*ContainerRegistryApi* | [**listRepositoriesV2**](docs/ContainerRegistryApi.md#listRepositoriesV2) | **GET** /v2/registry/{registry_name}/repositoriesV2 | List All Container Registry Repositories (V2)
*ContainerRegistryApi* | [**listRepositoryManifests**](docs/ContainerRegistryApi.md#listRepositoryManifests) | **GET** /v2/registry/{registry_name}/repositories/{repository_name}/digests | List All Container Registry Repository Manifests
*ContainerRegistryApi* | [**listRepositoryTags**](docs/ContainerRegistryApi.md#listRepositoryTags) | **GET** /v2/registry/{registry_name}/repositories/{repository_name}/tags | List All Container Registry Repository Tags
*ContainerRegistryApi* | [**startGarbageCollection**](docs/ContainerRegistryApi.md#startGarbageCollection) | **POST** /v2/registry/{registry_name}/garbage-collection | Start Garbage Collection
*ContainerRegistryApi* | [**updateSubscriptionTier**](docs/ContainerRegistryApi.md#updateSubscriptionTier) | **POST** /v2/registry/subscription | Update Subscription Tier
*ContainerRegistryApi* | [**validateName**](docs/ContainerRegistryApi.md#validateName) | **POST** /v2/registry/validate-name | Validate a Container Registry Name
*DatabasesApi* | [**add**](docs/DatabasesApi.md#add) | **POST** /v2/databases/{database_cluster_uuid}/dbs | Add a New Database
*DatabasesApi* | [**addNewConnectionPool**](docs/DatabasesApi.md#addNewConnectionPool) | **POST** /v2/databases/{database_cluster_uuid}/pools | Add a New Connection Pool (PostgreSQL)
*DatabasesApi* | [**addUser**](docs/DatabasesApi.md#addUser) | **POST** /v2/databases/{database_cluster_uuid}/users | Add a Database User
*DatabasesApi* | [**configureEvictionPolicy**](docs/DatabasesApi.md#configureEvictionPolicy) | **PUT** /v2/databases/{database_cluster_uuid}/eviction_policy | Configure the Eviction Policy for a Redis Cluster
*DatabasesApi* | [**configureMaintenanceWindow**](docs/DatabasesApi.md#configureMaintenanceWindow) | **PUT** /v2/databases/{database_cluster_uuid}/maintenance | Configure a Database Cluster&#39;s Maintenance Window
*DatabasesApi* | [**createCluster**](docs/DatabasesApi.md#createCluster) | **POST** /v2/databases | Create a New Database Cluster
*DatabasesApi* | [**createReadOnlyReplica**](docs/DatabasesApi.md#createReadOnlyReplica) | **POST** /v2/databases/{database_cluster_uuid}/replicas | Create a Read-only Replica
*DatabasesApi* | [**createTopicKafkaCluster**](docs/DatabasesApi.md#createTopicKafkaCluster) | **POST** /v2/databases/{database_cluster_uuid}/topics | Create Topic for a Kafka Cluster
*DatabasesApi* | [**delete**](docs/DatabasesApi.md#delete) | **DELETE** /v2/databases/{database_cluster_uuid}/dbs/{database_name} | Delete a Database
*DatabasesApi* | [**deleteConnectionPool**](docs/DatabasesApi.md#deleteConnectionPool) | **DELETE** /v2/databases/{database_cluster_uuid}/pools/{pool_name} | Delete a Connection Pool (PostgreSQL)
*DatabasesApi* | [**deleteTopicKafkaCluster**](docs/DatabasesApi.md#deleteTopicKafkaCluster) | **DELETE** /v2/databases/{database_cluster_uuid}/topics/{topic_name} | Delete Topic for a Kafka Cluster
*DatabasesApi* | [**destroyCluster**](docs/DatabasesApi.md#destroyCluster) | **DELETE** /v2/databases/{database_cluster_uuid} | Destroy a Database Cluster
*DatabasesApi* | [**destroyReadonlyReplica**](docs/DatabasesApi.md#destroyReadonlyReplica) | **DELETE** /v2/databases/{database_cluster_uuid}/replicas/{replica_name} | Destroy a Read-only Replica
*DatabasesApi* | [**get**](docs/DatabasesApi.md#get) | **GET** /v2/databases/{database_cluster_uuid}/dbs/{database_name} | Retrieve an Existing Database
*DatabasesApi* | [**getClusterConfig**](docs/DatabasesApi.md#getClusterConfig) | **GET** /v2/databases/{database_cluster_uuid}/config | Retrieve an Existing Database Cluster Configuration
*DatabasesApi* | [**getClusterInfo**](docs/DatabasesApi.md#getClusterInfo) | **GET** /v2/databases/{database_cluster_uuid} | Retrieve an Existing Database Cluster
*DatabasesApi* | [**getClustersMetricsCredentials**](docs/DatabasesApi.md#getClustersMetricsCredentials) | **GET** /v2/databases/metrics/credentials | Retrieve Database Clusters&#39; Metrics Endpoint Credentials
*DatabasesApi* | [**getConnectionPool**](docs/DatabasesApi.md#getConnectionPool) | **GET** /v2/databases/{database_cluster_uuid}/pools/{pool_name} | Retrieve Existing Connection Pool (PostgreSQL)
*DatabasesApi* | [**getEvictionPolicy**](docs/DatabasesApi.md#getEvictionPolicy) | **GET** /v2/databases/{database_cluster_uuid}/eviction_policy | Retrieve the Eviction Policy for a Redis Cluster
*DatabasesApi* | [**getExistingReadOnlyReplica**](docs/DatabasesApi.md#getExistingReadOnlyReplica) | **GET** /v2/databases/{database_cluster_uuid}/replicas/{replica_name} | Retrieve an Existing Read-only Replica
*DatabasesApi* | [**getMigrationStatus**](docs/DatabasesApi.md#getMigrationStatus) | **GET** /v2/databases/{database_cluster_uuid}/online-migration | Retrieve the Status of an Online Migration
*DatabasesApi* | [**getPublicCertificate**](docs/DatabasesApi.md#getPublicCertificate) | **GET** /v2/databases/{database_cluster_uuid}/ca | Retrieve the Public Certificate
*DatabasesApi* | [**getSqlMode**](docs/DatabasesApi.md#getSqlMode) | **GET** /v2/databases/{database_cluster_uuid}/sql_mode | Retrieve the SQL Modes for a MySQL Cluster
*DatabasesApi* | [**getTopicKafkaCluster**](docs/DatabasesApi.md#getTopicKafkaCluster) | **GET** /v2/databases/{database_cluster_uuid}/topics/{topic_name} | Get Topic for a Kafka Cluster
*DatabasesApi* | [**getUser**](docs/DatabasesApi.md#getUser) | **GET** /v2/databases/{database_cluster_uuid}/users/{username} | Retrieve an Existing Database User
*DatabasesApi* | [**list**](docs/DatabasesApi.md#list) | **GET** /v2/databases/{database_cluster_uuid}/dbs | List All Databases
*DatabasesApi* | [**listBackups**](docs/DatabasesApi.md#listBackups) | **GET** /v2/databases/{database_cluster_uuid}/backups | List Backups for a Database Cluster
*DatabasesApi* | [**listClusters**](docs/DatabasesApi.md#listClusters) | **GET** /v2/databases | List All Database Clusters
*DatabasesApi* | [**listConnectionPools**](docs/DatabasesApi.md#listConnectionPools) | **GET** /v2/databases/{database_cluster_uuid}/pools | List Connection Pools (PostgreSQL)
*DatabasesApi* | [**listEventsLogs**](docs/DatabasesApi.md#listEventsLogs) | **GET** /v2/databases/{database_cluster_uuid}/events | List all Events Logs
*DatabasesApi* | [**listFirewallRules**](docs/DatabasesApi.md#listFirewallRules) | **GET** /v2/databases/{database_cluster_uuid}/firewall | List Firewall Rules (Trusted Sources) for a Database Cluster
*DatabasesApi* | [**listOptions**](docs/DatabasesApi.md#listOptions) | **GET** /v2/databases/options | List Database Options
*DatabasesApi* | [**listReadOnlyReplicas**](docs/DatabasesApi.md#listReadOnlyReplicas) | **GET** /v2/databases/{database_cluster_uuid}/replicas | List All Read-only Replicas
*DatabasesApi* | [**listTopicsKafkaCluster**](docs/DatabasesApi.md#listTopicsKafkaCluster) | **GET** /v2/databases/{database_cluster_uuid}/topics | List Topics for a Kafka Cluster
*DatabasesApi* | [**listUsers**](docs/DatabasesApi.md#listUsers) | **GET** /v2/databases/{database_cluster_uuid}/users | List all Database Users
*DatabasesApi* | [**migrateClusterToNewRegion**](docs/DatabasesApi.md#migrateClusterToNewRegion) | **PUT** /v2/databases/{database_cluster_uuid}/migrate | Migrate a Database Cluster to a New Region
*DatabasesApi* | [**promoteReadonlyReplicaToPrimary**](docs/DatabasesApi.md#promoteReadonlyReplicaToPrimary) | **PUT** /v2/databases/{database_cluster_uuid}/replicas/{replica_name}/promote | Promote a Read-only Replica to become a Primary Cluster
*DatabasesApi* | [**removeUser**](docs/DatabasesApi.md#removeUser) | **DELETE** /v2/databases/{database_cluster_uuid}/users/{username} | Remove a Database User
*DatabasesApi* | [**resetUserAuth**](docs/DatabasesApi.md#resetUserAuth) | **POST** /v2/databases/{database_cluster_uuid}/users/{username}/reset_auth | Reset a Database User&#39;s Password or Authentication Method
*DatabasesApi* | [**resizeCluster**](docs/DatabasesApi.md#resizeCluster) | **PUT** /v2/databases/{database_cluster_uuid}/resize | Resize a Database Cluster
*DatabasesApi* | [**startOnlineMigration**](docs/DatabasesApi.md#startOnlineMigration) | **PUT** /v2/databases/{database_cluster_uuid}/online-migration | Start an Online Migration
*DatabasesApi* | [**stopOnlineMigration**](docs/DatabasesApi.md#stopOnlineMigration) | **DELETE** /v2/databases/{database_cluster_uuid}/online-migration/{migration_id} | Stop an Online Migration
*DatabasesApi* | [**updateConfigCluster**](docs/DatabasesApi.md#updateConfigCluster) | **PATCH** /v2/databases/{database_cluster_uuid}/config | Update the Database Configuration for an Existing Database
*DatabasesApi* | [**updateConnectionPoolPostgresql**](docs/DatabasesApi.md#updateConnectionPoolPostgresql) | **PUT** /v2/databases/{database_cluster_uuid}/pools/{pool_name} | Update Connection Pools (PostgreSQL)
*DatabasesApi* | [**updateFirewallRules**](docs/DatabasesApi.md#updateFirewallRules) | **PUT** /v2/databases/{database_cluster_uuid}/firewall | Update Firewall Rules (Trusted Sources) for a Database
*DatabasesApi* | [**updateMetricsCredentials**](docs/DatabasesApi.md#updateMetricsCredentials) | **PUT** /v2/databases/metrics/credentials | Update Database Clusters&#39; Metrics Endpoint Credentials
*DatabasesApi* | [**updateSettings**](docs/DatabasesApi.md#updateSettings) | **PUT** /v2/databases/{database_cluster_uuid}/users/{username} | Update a Database User
*DatabasesApi* | [**updateSqlMode**](docs/DatabasesApi.md#updateSqlMode) | **PUT** /v2/databases/{database_cluster_uuid}/sql_mode | Update SQL Mode for a Cluster
*DatabasesApi* | [**updateTopicKafkaCluster**](docs/DatabasesApi.md#updateTopicKafkaCluster) | **PUT** /v2/databases/{database_cluster_uuid}/topics/{topic_name} | Update Topic for a Kafka Cluster
*DatabasesApi* | [**upgradeMajorVersion**](docs/DatabasesApi.md#upgradeMajorVersion) | **PUT** /v2/databases/{database_cluster_uuid}/upgrade | Upgrade Major Version for a Database
*DomainRecordsApi* | [**createNewRecord**](docs/DomainRecordsApi.md#createNewRecord) | **POST** /v2/domains/{domain_name}/records | Create a New Domain Record
*DomainRecordsApi* | [**deleteById**](docs/DomainRecordsApi.md#deleteById) | **DELETE** /v2/domains/{domain_name}/records/{domain_record_id} | Delete a Domain Record
*DomainRecordsApi* | [**getExistingRecord**](docs/DomainRecordsApi.md#getExistingRecord) | **GET** /v2/domains/{domain_name}/records/{domain_record_id} | Retrieve an Existing Domain Record
*DomainRecordsApi* | [**listAllRecords**](docs/DomainRecordsApi.md#listAllRecords) | **GET** /v2/domains/{domain_name}/records | List All Domain Records
*DomainRecordsApi* | [**updateRecordById**](docs/DomainRecordsApi.md#updateRecordById) | **PUT** /v2/domains/{domain_name}/records/{domain_record_id} | Update a Domain Record
*DomainRecordsApi* | [**updateRecordById_0**](docs/DomainRecordsApi.md#updateRecordById_0) | **PATCH** /v2/domains/{domain_name}/records/{domain_record_id} | Update a Domain Record
*DomainsApi* | [**create**](docs/DomainsApi.md#create) | **POST** /v2/domains | Create a New Domain
*DomainsApi* | [**delete**](docs/DomainsApi.md#delete) | **DELETE** /v2/domains/{domain_name} | Delete a Domain
*DomainsApi* | [**get**](docs/DomainsApi.md#get) | **GET** /v2/domains/{domain_name} | Retrieve an Existing Domain
*DomainsApi* | [**list**](docs/DomainsApi.md#list) | **GET** /v2/domains | List All Domains
*DropletActionsApi* | [**actOnTaggedDroplets**](docs/DropletActionsApi.md#actOnTaggedDroplets) | **POST** /v2/droplets/actions | Acting on Tagged Droplets
*DropletActionsApi* | [**get**](docs/DropletActionsApi.md#get) | **GET** /v2/droplets/{droplet_id}/actions/{action_id} | Retrieve a Droplet Action
*DropletActionsApi* | [**list**](docs/DropletActionsApi.md#list) | **GET** /v2/droplets/{droplet_id}/actions | List Actions for a Droplet
*DropletActionsApi* | [**post**](docs/DropletActionsApi.md#post) | **POST** /v2/droplets/{droplet_id}/actions | Initiate a Droplet Action
*DropletsApi* | [**checkDestroyStatus**](docs/DropletsApi.md#checkDestroyStatus) | **GET** /v2/droplets/{droplet_id}/destroy_with_associated_resources/status | Check Status of a Droplet Destroy with Associated Resources Request
*DropletsApi* | [**create**](docs/DropletsApi.md#create) | **POST** /v2/droplets | Create a New Droplet
*DropletsApi* | [**deleteByTag**](docs/DropletsApi.md#deleteByTag) | **DELETE** /v2/droplets | Deleting Droplets by Tag
*DropletsApi* | [**deleteDangerous**](docs/DropletsApi.md#deleteDangerous) | **DELETE** /v2/droplets/{droplet_id}/destroy_with_associated_resources/dangerous | Destroy a Droplet and All of its Associated Resources (Dangerous)
*DropletsApi* | [**destroy**](docs/DropletsApi.md#destroy) | **DELETE** /v2/droplets/{droplet_id} | Delete an Existing Droplet
*DropletsApi* | [**destroyAssociatedResourcesSelective**](docs/DropletsApi.md#destroyAssociatedResourcesSelective) | **DELETE** /v2/droplets/{droplet_id}/destroy_with_associated_resources/selective | Selectively Destroy a Droplet and its Associated Resources
*DropletsApi* | [**get**](docs/DropletsApi.md#get) | **GET** /v2/droplets/{droplet_id} | Retrieve an Existing Droplet
*DropletsApi* | [**list**](docs/DropletsApi.md#list) | **GET** /v2/droplets | List All Droplets
*DropletsApi* | [**listAssociatedResources**](docs/DropletsApi.md#listAssociatedResources) | **GET** /v2/droplets/{droplet_id}/destroy_with_associated_resources | List Associated Resources for a Droplet
*DropletsApi* | [**listBackups**](docs/DropletsApi.md#listBackups) | **GET** /v2/droplets/{droplet_id}/backups | List Backups for a Droplet
*DropletsApi* | [**listDropletNeighbors**](docs/DropletsApi.md#listDropletNeighbors) | **GET** /v2/reports/droplet_neighbors_ids | List All Droplet Neighbors
*DropletsApi* | [**listFirewalls**](docs/DropletsApi.md#listFirewalls) | **GET** /v2/droplets/{droplet_id}/firewalls | List all Firewalls Applied to a Droplet
*DropletsApi* | [**listKernels**](docs/DropletsApi.md#listKernels) | **GET** /v2/droplets/{droplet_id}/kernels | List All Available Kernels for a Droplet
*DropletsApi* | [**listNeighbors**](docs/DropletsApi.md#listNeighbors) | **GET** /v2/droplets/{droplet_id}/neighbors | List Neighbors for a Droplet
*DropletsApi* | [**listSnapshots**](docs/DropletsApi.md#listSnapshots) | **GET** /v2/droplets/{droplet_id}/snapshots | List Snapshots for a Droplet
*DropletsApi* | [**retryDestroyWithAssociatedResources**](docs/DropletsApi.md#retryDestroyWithAssociatedResources) | **POST** /v2/droplets/{droplet_id}/destroy_with_associated_resources/retry | Retry a Droplet Destroy with Associated Resources Request
*FirewallsApi* | [**addDroplets**](docs/FirewallsApi.md#addDroplets) | **POST** /v2/firewalls/{firewall_id}/droplets | Add Droplets to a Firewall
*FirewallsApi* | [**addRules**](docs/FirewallsApi.md#addRules) | **POST** /v2/firewalls/{firewall_id}/rules | Add Rules to a Firewall
*FirewallsApi* | [**addTags**](docs/FirewallsApi.md#addTags) | **POST** /v2/firewalls/{firewall_id}/tags | Add Tags to a Firewall
*FirewallsApi* | [**create**](docs/FirewallsApi.md#create) | **POST** /v2/firewalls | Create a New Firewall
*FirewallsApi* | [**delete**](docs/FirewallsApi.md#delete) | **DELETE** /v2/firewalls/{firewall_id} | Delete a Firewall
*FirewallsApi* | [**get**](docs/FirewallsApi.md#get) | **GET** /v2/firewalls/{firewall_id} | Retrieve an Existing Firewall
*FirewallsApi* | [**list**](docs/FirewallsApi.md#list) | **GET** /v2/firewalls | List All Firewalls
*FirewallsApi* | [**removeDroplets**](docs/FirewallsApi.md#removeDroplets) | **DELETE** /v2/firewalls/{firewall_id}/droplets | Remove Droplets from a Firewall
*FirewallsApi* | [**removeRules**](docs/FirewallsApi.md#removeRules) | **DELETE** /v2/firewalls/{firewall_id}/rules | Remove Rules from a Firewall
*FirewallsApi* | [**removeTags**](docs/FirewallsApi.md#removeTags) | **DELETE** /v2/firewalls/{firewall_id}/tags | Remove Tags from a Firewall
*FirewallsApi* | [**update**](docs/FirewallsApi.md#update) | **PUT** /v2/firewalls/{firewall_id} | Update a Firewall
*FloatingIpActionsApi* | [**get**](docs/FloatingIpActionsApi.md#get) | **GET** /v2/floating_ips/{floating_ip}/actions/{action_id} | Retrieve an Existing Floating IP Action
*FloatingIpActionsApi* | [**list**](docs/FloatingIpActionsApi.md#list) | **GET** /v2/floating_ips/{floating_ip}/actions | List All Actions for a Floating IP
*FloatingIpActionsApi* | [**post**](docs/FloatingIpActionsApi.md#post) | **POST** /v2/floating_ips/{floating_ip}/actions | Initiate a Floating IP Action
*FloatingIpsApi* | [**create**](docs/FloatingIpsApi.md#create) | **POST** /v2/floating_ips | Create a New Floating IP
*FloatingIpsApi* | [**delete**](docs/FloatingIpsApi.md#delete) | **DELETE** /v2/floating_ips/{floating_ip} | Delete a Floating IP
*FloatingIpsApi* | [**get**](docs/FloatingIpsApi.md#get) | **GET** /v2/floating_ips/{floating_ip} | Retrieve an Existing Floating IP
*FloatingIpsApi* | [**list**](docs/FloatingIpsApi.md#list) | **GET** /v2/floating_ips | List All Floating IPs
*FunctionsApi* | [**createNamespace**](docs/FunctionsApi.md#createNamespace) | **POST** /v2/functions/namespaces | Create Namespace
*FunctionsApi* | [**createTriggerInNamespace**](docs/FunctionsApi.md#createTriggerInNamespace) | **POST** /v2/functions/namespaces/{namespace_id}/triggers | Create Trigger
*FunctionsApi* | [**deleteNamespace**](docs/FunctionsApi.md#deleteNamespace) | **DELETE** /v2/functions/namespaces/{namespace_id} | Delete Namespace
*FunctionsApi* | [**deleteTrigger**](docs/FunctionsApi.md#deleteTrigger) | **DELETE** /v2/functions/namespaces/{namespace_id}/triggers/{trigger_name} | Delete Trigger
*FunctionsApi* | [**getNamespaceDetails**](docs/FunctionsApi.md#getNamespaceDetails) | **GET** /v2/functions/namespaces/{namespace_id} | Get Namespace
*FunctionsApi* | [**getTriggerDetails**](docs/FunctionsApi.md#getTriggerDetails) | **GET** /v2/functions/namespaces/{namespace_id}/triggers/{trigger_name} | Get Trigger
*FunctionsApi* | [**listNamespaces**](docs/FunctionsApi.md#listNamespaces) | **GET** /v2/functions/namespaces | List Namespaces
*FunctionsApi* | [**listTriggers**](docs/FunctionsApi.md#listTriggers) | **GET** /v2/functions/namespaces/{namespace_id}/triggers | List Triggers
*FunctionsApi* | [**updateTriggerDetails**](docs/FunctionsApi.md#updateTriggerDetails) | **PUT** /v2/functions/namespaces/{namespace_id}/triggers/{trigger_name} | Update Trigger
*ImageActionsApi* | [**get**](docs/ImageActionsApi.md#get) | **GET** /v2/images/{image_id}/actions/{action_id} | Retrieve an Existing Action
*ImageActionsApi* | [**list**](docs/ImageActionsApi.md#list) | **GET** /v2/images/{image_id}/actions | List All Actions for an Image
*ImageActionsApi* | [**post**](docs/ImageActionsApi.md#post) | **POST** /v2/images/{image_id}/actions | Initiate an Image Action
*ImagesApi* | [**delete**](docs/ImagesApi.md#delete) | **DELETE** /v2/images/{image_id} | Delete an Image
*ImagesApi* | [**get**](docs/ImagesApi.md#get) | **GET** /v2/images/{image_id} | Retrieve an Existing Image
*ImagesApi* | [**importCustomImageFromUrl**](docs/ImagesApi.md#importCustomImageFromUrl) | **POST** /v2/images | Create a Custom Image
*ImagesApi* | [**list**](docs/ImagesApi.md#list) | **GET** /v2/images | List All Images
*ImagesApi* | [**update**](docs/ImagesApi.md#update) | **PUT** /v2/images/{image_id} | Update an Image
*KubernetesApi* | [**addContainerRegistryToClusters**](docs/KubernetesApi.md#addContainerRegistryToClusters) | **POST** /v2/kubernetes/registry | Add Container Registry to Kubernetes Clusters
*KubernetesApi* | [**addNodePool**](docs/KubernetesApi.md#addNodePool) | **POST** /v2/kubernetes/clusters/{cluster_id}/node_pools | Add a Node Pool to a Kubernetes Cluster
*KubernetesApi* | [**createNewCluster**](docs/KubernetesApi.md#createNewCluster) | **POST** /v2/kubernetes/clusters | Create a New Kubernetes Cluster
*KubernetesApi* | [**deleteCluster**](docs/KubernetesApi.md#deleteCluster) | **DELETE** /v2/kubernetes/clusters/{cluster_id} | Delete a Kubernetes Cluster
*KubernetesApi* | [**deleteClusterAssociatedResourcesDangerous**](docs/KubernetesApi.md#deleteClusterAssociatedResourcesDangerous) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources/dangerous | Delete a Cluster and All of its Associated Resources (Dangerous)
*KubernetesApi* | [**deleteNodeInNodePool**](docs/KubernetesApi.md#deleteNodeInNodePool) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}/nodes/{node_id} | Delete a Node in a Kubernetes Cluster
*KubernetesApi* | [**deleteNodePool**](docs/KubernetesApi.md#deleteNodePool) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id} | Delete a Node Pool in a Kubernetes Cluster
*KubernetesApi* | [**getAvailableUpgrades**](docs/KubernetesApi.md#getAvailableUpgrades) | **GET** /v2/kubernetes/clusters/{cluster_id}/upgrades | Retrieve Available Upgrades for an Existing Kubernetes Cluster
*KubernetesApi* | [**getClusterInfo**](docs/KubernetesApi.md#getClusterInfo) | **GET** /v2/kubernetes/clusters/{cluster_id} | Retrieve an Existing Kubernetes Cluster
*KubernetesApi* | [**getClusterLintDiagnostics**](docs/KubernetesApi.md#getClusterLintDiagnostics) | **GET** /v2/kubernetes/clusters/{cluster_id}/clusterlint | Fetch Clusterlint Diagnostics for a Kubernetes Cluster
*KubernetesApi* | [**getCredentialsByClusterId**](docs/KubernetesApi.md#getCredentialsByClusterId) | **GET** /v2/kubernetes/clusters/{cluster_id}/credentials | Retrieve Credentials for a Kubernetes Cluster
*KubernetesApi* | [**getKubeconfig**](docs/KubernetesApi.md#getKubeconfig) | **GET** /v2/kubernetes/clusters/{cluster_id}/kubeconfig | Retrieve the kubeconfig for a Kubernetes Cluster
*KubernetesApi* | [**getNodePool**](docs/KubernetesApi.md#getNodePool) | **GET** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id} | Retrieve a Node Pool for a Kubernetes Cluster
*KubernetesApi* | [**getUserInformation**](docs/KubernetesApi.md#getUserInformation) | **GET** /v2/kubernetes/clusters/{cluster_id}/user | Retrieve User Information for a Kubernetes Cluster
*KubernetesApi* | [**listAssociatedResources**](docs/KubernetesApi.md#listAssociatedResources) | **GET** /v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources | List Associated Resources for Cluster Deletion
*KubernetesApi* | [**listClusters**](docs/KubernetesApi.md#listClusters) | **GET** /v2/kubernetes/clusters | List All Kubernetes Clusters
*KubernetesApi* | [**listNodePools**](docs/KubernetesApi.md#listNodePools) | **GET** /v2/kubernetes/clusters/{cluster_id}/node_pools | List All Node Pools in a Kubernetes Clusters
*KubernetesApi* | [**listOptions**](docs/KubernetesApi.md#listOptions) | **GET** /v2/kubernetes/options | List Available Regions, Node Sizes, and Versions of Kubernetes
*KubernetesApi* | [**recycleNodePool**](docs/KubernetesApi.md#recycleNodePool) | **POST** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}/recycle | Recycle a Kubernetes Node Pool
*KubernetesApi* | [**removeRegistry**](docs/KubernetesApi.md#removeRegistry) | **DELETE** /v2/kubernetes/registry | Remove Container Registry from Kubernetes Clusters
*KubernetesApi* | [**runClusterlintChecks**](docs/KubernetesApi.md#runClusterlintChecks) | **POST** /v2/kubernetes/clusters/{cluster_id}/clusterlint | Run Clusterlint Checks on a Kubernetes Cluster
*KubernetesApi* | [**selectiveClusterDestroy**](docs/KubernetesApi.md#selectiveClusterDestroy) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources/selective | Selectively Delete a Cluster and its Associated Resources
*KubernetesApi* | [**updateCluster**](docs/KubernetesApi.md#updateCluster) | **PUT** /v2/kubernetes/clusters/{cluster_id} | Update a Kubernetes Cluster
*KubernetesApi* | [**updateNodePool**](docs/KubernetesApi.md#updateNodePool) | **PUT** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id} | Update a Node Pool in a Kubernetes Cluster
*KubernetesApi* | [**upgradeCluster**](docs/KubernetesApi.md#upgradeCluster) | **POST** /v2/kubernetes/clusters/{cluster_id}/upgrade | Upgrade a Kubernetes Cluster
*LoadBalancersApi* | [**addForwardingRules**](docs/LoadBalancersApi.md#addForwardingRules) | **POST** /v2/load_balancers/{lb_id}/forwarding_rules | Add Forwarding Rules to a Load Balancer
*LoadBalancersApi* | [**assignDroplets**](docs/LoadBalancersApi.md#assignDroplets) | **POST** /v2/load_balancers/{lb_id}/droplets | Add Droplets to a Load Balancer
*LoadBalancersApi* | [**create**](docs/LoadBalancersApi.md#create) | **POST** /v2/load_balancers | Create a New Load Balancer
*LoadBalancersApi* | [**delete**](docs/LoadBalancersApi.md#delete) | **DELETE** /v2/load_balancers/{lb_id} | Delete a Load Balancer
*LoadBalancersApi* | [**get**](docs/LoadBalancersApi.md#get) | **GET** /v2/load_balancers/{lb_id} | Retrieve an Existing Load Balancer
*LoadBalancersApi* | [**list**](docs/LoadBalancersApi.md#list) | **GET** /v2/load_balancers | List All Load Balancers
*LoadBalancersApi* | [**removeDroplets**](docs/LoadBalancersApi.md#removeDroplets) | **DELETE** /v2/load_balancers/{lb_id}/droplets | Remove Droplets from a Load Balancer
*LoadBalancersApi* | [**removeForwardingRules**](docs/LoadBalancersApi.md#removeForwardingRules) | **DELETE** /v2/load_balancers/{lb_id}/forwarding_rules | Remove Forwarding Rules from a Load Balancer
*LoadBalancersApi* | [**update**](docs/LoadBalancersApi.md#update) | **PUT** /v2/load_balancers/{lb_id} | Update a Load Balancer
*MonitoringApi* | [**createAlertPolicy**](docs/MonitoringApi.md#createAlertPolicy) | **POST** /v2/monitoring/alerts | Create Alert Policy
*MonitoringApi* | [**deleteAlertPolicy**](docs/MonitoringApi.md#deleteAlertPolicy) | **DELETE** /v2/monitoring/alerts/{alert_uuid} | Delete an Alert Policy
*MonitoringApi* | [**dropletCpuMetricsget**](docs/MonitoringApi.md#dropletCpuMetricsget) | **GET** /v2/monitoring/metrics/droplet/cpu | Get Droplet CPU Metrics
*MonitoringApi* | [**dropletLoad5MetricsGet**](docs/MonitoringApi.md#dropletLoad5MetricsGet) | **GET** /v2/monitoring/metrics/droplet/load_5 | Get Droplet Load5 Metrics
*MonitoringApi* | [**dropletMemoryCachedMetrics**](docs/MonitoringApi.md#dropletMemoryCachedMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_cached | Get Droplet Cached Memory Metrics
*MonitoringApi* | [**getAppCpuPercentageMetrics**](docs/MonitoringApi.md#getAppCpuPercentageMetrics) | **GET** /v2/monitoring/metrics/apps/cpu_percentage | Get App CPU Percentage Metrics
*MonitoringApi* | [**getAppMemoryPercentageMetrics**](docs/MonitoringApi.md#getAppMemoryPercentageMetrics) | **GET** /v2/monitoring/metrics/apps/memory_percentage | Get App Memory Percentage Metrics
*MonitoringApi* | [**getAppRestartCountMetrics**](docs/MonitoringApi.md#getAppRestartCountMetrics) | **GET** /v2/monitoring/metrics/apps/restart_count | Get App Restart Count Metrics
*MonitoringApi* | [**getDropletBandwidthMetrics**](docs/MonitoringApi.md#getDropletBandwidthMetrics) | **GET** /v2/monitoring/metrics/droplet/bandwidth | Get Droplet Bandwidth Metrics
*MonitoringApi* | [**getDropletFilesystemFreeMetrics**](docs/MonitoringApi.md#getDropletFilesystemFreeMetrics) | **GET** /v2/monitoring/metrics/droplet/filesystem_free | Get Droplet Filesystem Free Metrics
*MonitoringApi* | [**getDropletFilesystemSizeMetrics**](docs/MonitoringApi.md#getDropletFilesystemSizeMetrics) | **GET** /v2/monitoring/metrics/droplet/filesystem_size | Get Droplet Filesystem Size Metrics
*MonitoringApi* | [**getDropletLoad15Metrics**](docs/MonitoringApi.md#getDropletLoad15Metrics) | **GET** /v2/monitoring/metrics/droplet/load_15 | Get Droplet Load15 Metrics
*MonitoringApi* | [**getDropletLoad1Metrics**](docs/MonitoringApi.md#getDropletLoad1Metrics) | **GET** /v2/monitoring/metrics/droplet/load_1 | Get Droplet Load1 Metrics
*MonitoringApi* | [**getDropletMemoryAvailableMetrics**](docs/MonitoringApi.md#getDropletMemoryAvailableMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_available | Get Droplet Available Memory Metrics
*MonitoringApi* | [**getDropletMemoryFreeMetrics**](docs/MonitoringApi.md#getDropletMemoryFreeMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_free | Get Droplet Free Memory Metrics
*MonitoringApi* | [**getDropletMemoryTotalMetrics**](docs/MonitoringApi.md#getDropletMemoryTotalMetrics) | **GET** /v2/monitoring/metrics/droplet/memory_total | Get Droplet Total Memory Metrics
*MonitoringApi* | [**getExistingAlertPolicy**](docs/MonitoringApi.md#getExistingAlertPolicy) | **GET** /v2/monitoring/alerts/{alert_uuid} | Retrieve an Existing Alert Policy
*MonitoringApi* | [**listAlertPolicies**](docs/MonitoringApi.md#listAlertPolicies) | **GET** /v2/monitoring/alerts | List Alert Policies
*MonitoringApi* | [**updateAlertPolicy**](docs/MonitoringApi.md#updateAlertPolicy) | **PUT** /v2/monitoring/alerts/{alert_uuid} | Update an Alert Policy
*ProjectResourcesApi* | [**assignResourcesToDefault**](docs/ProjectResourcesApi.md#assignResourcesToDefault) | **POST** /v2/projects/default/resources | Assign Resources to Default Project
*ProjectResourcesApi* | [**assignToProject**](docs/ProjectResourcesApi.md#assignToProject) | **POST** /v2/projects/{project_id}/resources | Assign Resources to a Project
*ProjectResourcesApi* | [**list**](docs/ProjectResourcesApi.md#list) | **GET** /v2/projects/{project_id}/resources | List Project Resources
*ProjectResourcesApi* | [**listDefault**](docs/ProjectResourcesApi.md#listDefault) | **GET** /v2/projects/default/resources | List Default Project Resources
*ProjectsApi* | [**create**](docs/ProjectsApi.md#create) | **POST** /v2/projects | Create a Project
*ProjectsApi* | [**delete**](docs/ProjectsApi.md#delete) | **DELETE** /v2/projects/{project_id} | Delete an Existing Project
*ProjectsApi* | [**get**](docs/ProjectsApi.md#get) | **GET** /v2/projects/{project_id} | Retrieve an Existing Project
*ProjectsApi* | [**getDefaultProject**](docs/ProjectsApi.md#getDefaultProject) | **GET** /v2/projects/default | Retrieve the Default Project
*ProjectsApi* | [**list**](docs/ProjectsApi.md#list) | **GET** /v2/projects | List All Projects
*ProjectsApi* | [**patch**](docs/ProjectsApi.md#patch) | **PATCH** /v2/projects/{project_id} | Patch a Project
*ProjectsApi* | [**update**](docs/ProjectsApi.md#update) | **PUT** /v2/projects/{project_id} | Update a Project
*ProjectsApi* | [**updateDefaultProject**](docs/ProjectsApi.md#updateDefaultProject) | **PUT** /v2/projects/default | Update the Default Project
*ProjectsApi* | [**updateDefaultProjectAttributes**](docs/ProjectsApi.md#updateDefaultProjectAttributes) | **PATCH** /v2/projects/default | Patch the Default Project
*RegionsApi* | [**list**](docs/RegionsApi.md#list) | **GET** /v2/regions | List All Data Center Regions
*ReservedIpActionsApi* | [**get**](docs/ReservedIpActionsApi.md#get) | **GET** /v2/reserved_ips/{reserved_ip}/actions/{action_id} | Retrieve an Existing Reserved IP Action
*ReservedIpActionsApi* | [**list**](docs/ReservedIpActionsApi.md#list) | **GET** /v2/reserved_ips/{reserved_ip}/actions | List All Actions for a Reserved IP
*ReservedIpActionsApi* | [**post**](docs/ReservedIpActionsApi.md#post) | **POST** /v2/reserved_ips/{reserved_ip}/actions | Initiate a Reserved IP Action
*ReservedIpsApi* | [**create**](docs/ReservedIpsApi.md#create) | **POST** /v2/reserved_ips | Create a New Reserved IP
*ReservedIpsApi* | [**delete**](docs/ReservedIpsApi.md#delete) | **DELETE** /v2/reserved_ips/{reserved_ip} | Delete a Reserved IP
*ReservedIpsApi* | [**get**](docs/ReservedIpsApi.md#get) | **GET** /v2/reserved_ips/{reserved_ip} | Retrieve an Existing Reserved IP
*ReservedIpsApi* | [**list**](docs/ReservedIpsApi.md#list) | **GET** /v2/reserved_ips | List All Reserved IPs
*SshKeysApi* | [**create**](docs/SshKeysApi.md#create) | **POST** /v2/account/keys | Create a New SSH Key
*SshKeysApi* | [**delete**](docs/SshKeysApi.md#delete) | **DELETE** /v2/account/keys/{ssh_key_identifier} | Delete an SSH Key
*SshKeysApi* | [**get**](docs/SshKeysApi.md#get) | **GET** /v2/account/keys/{ssh_key_identifier} | Retrieve an Existing SSH Key
*SshKeysApi* | [**list**](docs/SshKeysApi.md#list) | **GET** /v2/account/keys | List All SSH Keys
*SshKeysApi* | [**update**](docs/SshKeysApi.md#update) | **PUT** /v2/account/keys/{ssh_key_identifier} | Update an SSH Key&#39;s Name
*SizesApi* | [**list**](docs/SizesApi.md#list) | **GET** /v2/sizes | List All Droplet Sizes
*SnapshotsApi* | [**delete**](docs/SnapshotsApi.md#delete) | **DELETE** /v2/snapshots/{snapshot_id} | Delete a Snapshot
*SnapshotsApi* | [**get**](docs/SnapshotsApi.md#get) | **GET** /v2/snapshots/{snapshot_id} | Retrieve an Existing Snapshot
*SnapshotsApi* | [**list**](docs/SnapshotsApi.md#list) | **GET** /v2/snapshots | List All Snapshots
*TagsApi* | [**create**](docs/TagsApi.md#create) | **POST** /v2/tags | Create a New Tag
*TagsApi* | [**delete**](docs/TagsApi.md#delete) | **DELETE** /v2/tags/{tag_id} | Delete a Tag
*TagsApi* | [**get**](docs/TagsApi.md#get) | **GET** /v2/tags/{tag_id} | Retrieve a Tag
*TagsApi* | [**list**](docs/TagsApi.md#list) | **GET** /v2/tags | List All Tags
*TagsApi* | [**tagResource**](docs/TagsApi.md#tagResource) | **POST** /v2/tags/{tag_id}/resources | Tag a Resource
*TagsApi* | [**untagResource**](docs/TagsApi.md#untagResource) | **DELETE** /v2/tags/{tag_id}/resources | Untag a Resource
*UptimeApi* | [**createCheck**](docs/UptimeApi.md#createCheck) | **POST** /v2/uptime/checks | Create a New Check
*UptimeApi* | [**createNewAlert**](docs/UptimeApi.md#createNewAlert) | **POST** /v2/uptime/checks/{check_id}/alerts | Create a New Alert
*UptimeApi* | [**deleteAlert**](docs/UptimeApi.md#deleteAlert) | **DELETE** /v2/uptime/checks/{check_id}/alerts/{alert_id} | Delete an Alert
*UptimeApi* | [**deleteCheck**](docs/UptimeApi.md#deleteCheck) | **DELETE** /v2/uptime/checks/{check_id} | Delete a Check
*UptimeApi* | [**getCheckById**](docs/UptimeApi.md#getCheckById) | **GET** /v2/uptime/checks/{check_id} | Retrieve an Existing Check
*UptimeApi* | [**getCheckState**](docs/UptimeApi.md#getCheckState) | **GET** /v2/uptime/checks/{check_id}/state | Retrieve Check State
*UptimeApi* | [**getExistingAlert**](docs/UptimeApi.md#getExistingAlert) | **GET** /v2/uptime/checks/{check_id}/alerts/{alert_id} | Retrieve an Existing Alert
*UptimeApi* | [**listAllAlerts**](docs/UptimeApi.md#listAllAlerts) | **GET** /v2/uptime/checks/{check_id}/alerts | List All Alerts
*UptimeApi* | [**listChecks**](docs/UptimeApi.md#listChecks) | **GET** /v2/uptime/checks | List All Checks
*UptimeApi* | [**updateAlertSettings**](docs/UptimeApi.md#updateAlertSettings) | **PUT** /v2/uptime/checks/{check_id}/alerts/{alert_id} | Update an Alert
*UptimeApi* | [**updateCheckSettings**](docs/UptimeApi.md#updateCheckSettings) | **PUT** /v2/uptime/checks/{check_id} | Update a Check
*VpcsApi* | [**create**](docs/VpcsApi.md#create) | **POST** /v2/vpcs | Create a New VPC
*VpcsApi* | [**delete**](docs/VpcsApi.md#delete) | **DELETE** /v2/vpcs/{vpc_id} | Delete a VPC
*VpcsApi* | [**get**](docs/VpcsApi.md#get) | **GET** /v2/vpcs/{vpc_id} | Retrieve an Existing VPC
*VpcsApi* | [**list**](docs/VpcsApi.md#list) | **GET** /v2/vpcs | List All VPCs
*VpcsApi* | [**listMembers**](docs/VpcsApi.md#listMembers) | **GET** /v2/vpcs/{vpc_id}/members | List the Member Resources of a VPC
*VpcsApi* | [**patch**](docs/VpcsApi.md#patch) | **PATCH** /v2/vpcs/{vpc_id} | Partially Update a VPC
*VpcsApi* | [**update**](docs/VpcsApi.md#update) | **PUT** /v2/vpcs/{vpc_id} | Update a VPC


## Documentation for Models

 - [Account](docs/Account.md)
 - [AccountTeam](docs/AccountTeam.md)
 - [Action](docs/Action.md)
 - [ActionLink](docs/ActionLink.md)
 - [ActionsListResponse](docs/ActionsListResponse.md)
 - [ActionsListResponseAllOf](docs/ActionsListResponseAllOf.md)
 - [Adrvolumeactionpost](docs/Adrvolumeactionpost.md)
 - [Advolumeactionpost](docs/Advolumeactionpost.md)
 - [Alert](docs/Alert.md)
 - [AlertBase](docs/AlertBase.md)
 - [AlertPolicy](docs/AlertPolicy.md)
 - [AlertPolicyRequest](docs/AlertPolicyRequest.md)
 - [AlertUpdatable](docs/AlertUpdatable.md)
 - [Alerts](docs/Alerts.md)
 - [App](docs/App.md)
 - [AppAlert](docs/AppAlert.md)
 - [AppAlertPhase](docs/AppAlertPhase.md)
 - [AppAlertProgress](docs/AppAlertProgress.md)
 - [AppAlertProgressStep](docs/AppAlertProgressStep.md)
 - [AppAlertProgressStepReason](docs/AppAlertProgressStepReason.md)
 - [AppAlertProgressStepStatus](docs/AppAlertProgressStepStatus.md)
 - [AppAlertSlackWebhook](docs/AppAlertSlackWebhook.md)
 - [AppAlertSpec](docs/AppAlertSpec.md)
 - [AppAlertSpecOperator](docs/AppAlertSpecOperator.md)
 - [AppAlertSpecRule](docs/AppAlertSpecRule.md)
 - [AppAlertSpecWindow](docs/AppAlertSpecWindow.md)
 - [AppComponentBase](docs/AppComponentBase.md)
 - [AppComponentInstanceBase](docs/AppComponentInstanceBase.md)
 - [AppComponentInstanceBaseAutoscaling](docs/AppComponentInstanceBaseAutoscaling.md)
 - [AppComponentInstanceBaseAutoscalingMetrics](docs/AppComponentInstanceBaseAutoscalingMetrics.md)
 - [AppComponentInstanceBaseAutoscalingMetricsCpu](docs/AppComponentInstanceBaseAutoscalingMetricsCpu.md)
 - [AppDatabaseSpec](docs/AppDatabaseSpec.md)
 - [AppDomainSpec](docs/AppDomainSpec.md)
 - [AppDomainValidation](docs/AppDomainValidation.md)
 - [AppFunctionsSpec](docs/AppFunctionsSpec.md)
 - [AppFunctionsSpecCors](docs/AppFunctionsSpecCors.md)
 - [AppIngressSpec](docs/AppIngressSpec.md)
 - [AppIngressSpecRule](docs/AppIngressSpecRule.md)
 - [AppIngressSpecRuleMatch](docs/AppIngressSpecRuleMatch.md)
 - [AppIngressSpecRuleRoutingComponent](docs/AppIngressSpecRuleRoutingComponent.md)
 - [AppIngressSpecRuleRoutingRedirect](docs/AppIngressSpecRuleRoutingRedirect.md)
 - [AppIngressSpecRuleStringMatch](docs/AppIngressSpecRuleStringMatch.md)
 - [AppJobSpec](docs/AppJobSpec.md)
 - [AppJobSpecAllOf](docs/AppJobSpecAllOf.md)
 - [AppLogDestinationDatadogSpec](docs/AppLogDestinationDatadogSpec.md)
 - [AppLogDestinationDefinition](docs/AppLogDestinationDefinition.md)
 - [AppLogDestinationLogtailSpec](docs/AppLogDestinationLogtailSpec.md)
 - [AppLogDestinationPapertrailSpec](docs/AppLogDestinationPapertrailSpec.md)
 - [AppMetricsBandwidthUsage](docs/AppMetricsBandwidthUsage.md)
 - [AppMetricsBandwidthUsageDetails](docs/AppMetricsBandwidthUsageDetails.md)
 - [AppMetricsBandwidthUsageRequest](docs/AppMetricsBandwidthUsageRequest.md)
 - [AppPendingDeployment](docs/AppPendingDeployment.md)
 - [AppPinnedDeployment](docs/AppPinnedDeployment.md)
 - [AppPropose](docs/AppPropose.md)
 - [AppProposeResponse](docs/AppProposeResponse.md)
 - [AppResponse](docs/AppResponse.md)
 - [AppRollbackValidationCondition](docs/AppRollbackValidationCondition.md)
 - [AppRouteSpec](docs/AppRouteSpec.md)
 - [AppServiceSpec](docs/AppServiceSpec.md)
 - [AppServiceSpecAllOf](docs/AppServiceSpecAllOf.md)
 - [AppServiceSpecHealthCheck](docs/AppServiceSpecHealthCheck.md)
 - [AppSpec](docs/AppSpec.md)
 - [AppStaticSiteSpec](docs/AppStaticSiteSpec.md)
 - [AppStaticSiteSpecAllOf](docs/AppStaticSiteSpecAllOf.md)
 - [AppVariableDefinition](docs/AppVariableDefinition.md)
 - [AppWorkerSpec](docs/AppWorkerSpec.md)
 - [AppsAlertResponse](docs/AppsAlertResponse.md)
 - [AppsAssignAppAlertDestinationsRequest](docs/AppsAssignAppAlertDestinationsRequest.md)
 - [AppsCorsPolicy](docs/AppsCorsPolicy.md)
 - [AppsCreateAppRequest](docs/AppsCreateAppRequest.md)
 - [AppsCreateDeploymentRequest](docs/AppsCreateDeploymentRequest.md)
 - [AppsDeleteAppResponse](docs/AppsDeleteAppResponse.md)
 - [AppsDeployment](docs/AppsDeployment.md)
 - [AppsDeploymentFunctions](docs/AppsDeploymentFunctions.md)
 - [AppsDeploymentJob](docs/AppsDeploymentJob.md)
 - [AppsDeploymentPhase](docs/AppsDeploymentPhase.md)
 - [AppsDeploymentProgress](docs/AppsDeploymentProgress.md)
 - [AppsDeploymentProgressStep](docs/AppsDeploymentProgressStep.md)
 - [AppsDeploymentProgressStepReason](docs/AppsDeploymentProgressStepReason.md)
 - [AppsDeploymentProgressStepStatus](docs/AppsDeploymentProgressStepStatus.md)
 - [AppsDeploymentResponse](docs/AppsDeploymentResponse.md)
 - [AppsDeploymentService](docs/AppsDeploymentService.md)
 - [AppsDeploymentStaticSite](docs/AppsDeploymentStaticSite.md)
 - [AppsDeploymentWorker](docs/AppsDeploymentWorker.md)
 - [AppsDeploymentsResponse](docs/AppsDeploymentsResponse.md)
 - [AppsDeploymentsResponseAllOf](docs/AppsDeploymentsResponseAllOf.md)
 - [AppsDomain](docs/AppsDomain.md)
 - [AppsDomainPhase](docs/AppsDomainPhase.md)
 - [AppsDomainProgress](docs/AppsDomainProgress.md)
 - [AppsGetInstanceSizeResponse](docs/AppsGetInstanceSizeResponse.md)
 - [AppsGetLogsResponse](docs/AppsGetLogsResponse.md)
 - [AppsGetTierResponse](docs/AppsGetTierResponse.md)
 - [AppsGitSourceSpec](docs/AppsGitSourceSpec.md)
 - [AppsGithubSourceSpec](docs/AppsGithubSourceSpec.md)
 - [AppsGitlabSourceSpec](docs/AppsGitlabSourceSpec.md)
 - [AppsImageSourceSpec](docs/AppsImageSourceSpec.md)
 - [AppsImageSourceSpecDeployOnPush](docs/AppsImageSourceSpecDeployOnPush.md)
 - [AppsInstanceSize](docs/AppsInstanceSize.md)
 - [AppsListAlertsResponse](docs/AppsListAlertsResponse.md)
 - [AppsListInstanceSizesResponse](docs/AppsListInstanceSizesResponse.md)
 - [AppsListRegionsResponse](docs/AppsListRegionsResponse.md)
 - [AppsListTiersResponse](docs/AppsListTiersResponse.md)
 - [AppsRegion](docs/AppsRegion.md)
 - [AppsResponse](docs/AppsResponse.md)
 - [AppsResponseAllOf](docs/AppsResponseAllOf.md)
 - [AppsRollbackAppRequest](docs/AppsRollbackAppRequest.md)
 - [AppsStringMatch](docs/AppsStringMatch.md)
 - [AppsTier](docs/AppsTier.md)
 - [AppsUpdateAppRequest](docs/AppsUpdateAppRequest.md)
 - [AppsValidateRollbackResponse](docs/AppsValidateRollbackResponse.md)
 - [AppsValidateRollbackResponseError](docs/AppsValidateRollbackResponseError.md)
 - [AssociatedKubernetesResource](docs/AssociatedKubernetesResource.md)
 - [AssociatedKubernetesResources](docs/AssociatedKubernetesResources.md)
 - [AssociatedResource](docs/AssociatedResource.md)
 - [AssociatedResourceStatus](docs/AssociatedResourceStatus.md)
 - [AssociatedResourceStatusResources](docs/AssociatedResourceStatusResources.md)
 - [Backup](docs/Backup.md)
 - [BackwardLinks](docs/BackwardLinks.md)
 - [Balance](docs/Balance.md)
 - [BillingAddress](docs/BillingAddress.md)
 - [BillingGetInvoiceByUuidResponse](docs/BillingGetInvoiceByUuidResponse.md)
 - [BillingGetInvoiceByUuidResponseAllOf](docs/BillingGetInvoiceByUuidResponseAllOf.md)
 - [BillingHistory](docs/BillingHistory.md)
 - [BillingHistoryListResponse](docs/BillingHistoryListResponse.md)
 - [BillingHistoryListResponseAllOf](docs/BillingHistoryListResponseAllOf.md)
 - [BlockStorageGetSnapshotDetailsResponse](docs/BlockStorageGetSnapshotDetailsResponse.md)
 - [Ca](docs/Ca.md)
 - [CdnEndpoint](docs/CdnEndpoint.md)
 - [CdnEndpointsCreateNewEndpointResponse](docs/CdnEndpointsCreateNewEndpointResponse.md)
 - [CdnEndpointsCreateNewEndpointResponseEndpoint](docs/CdnEndpointsCreateNewEndpointResponseEndpoint.md)
 - [CdnEndpointsListAllResponse](docs/CdnEndpointsListAllResponse.md)
 - [CdnEndpointsListAllResponseAllOf](docs/CdnEndpointsListAllResponseAllOf.md)
 - [Certificate](docs/Certificate.md)
 - [CertificateCreateBase](docs/CertificateCreateBase.md)
 - [CertificateRequestCustom](docs/CertificateRequestCustom.md)
 - [CertificateRequestCustomAllOf](docs/CertificateRequestCustomAllOf.md)
 - [CertificateRequestLetsEncrypt](docs/CertificateRequestLetsEncrypt.md)
 - [CertificateRequestLetsEncryptAllOf](docs/CertificateRequestLetsEncryptAllOf.md)
 - [CertificatesCreateResponse](docs/CertificatesCreateResponse.md)
 - [CertificatesGetResponse](docs/CertificatesGetResponse.md)
 - [CertificatesListResponse](docs/CertificatesListResponse.md)
 - [CertificatesListResponseAllOf](docs/CertificatesListResponseAllOf.md)
 - [Check](docs/Check.md)
 - [CheckBase](docs/CheckBase.md)
 - [CheckUpdatable](docs/CheckUpdatable.md)
 - [Cluster](docs/Cluster.md)
 - [ClusterRegistries](docs/ClusterRegistries.md)
 - [ClusterStatus](docs/ClusterStatus.md)
 - [ClusterUpdate](docs/ClusterUpdate.md)
 - [ClusterlintRequest](docs/ClusterlintRequest.md)
 - [ClusterlintResults](docs/ClusterlintResults.md)
 - [ClusterlintResultsDiagnosticsInner](docs/ClusterlintResultsDiagnosticsInner.md)
 - [ClusterlintResultsDiagnosticsInnerObject](docs/ClusterlintResultsDiagnosticsInnerObject.md)
 - [ConnectionPool](docs/ConnectionPool.md)
 - [ConnectionPoolUpdate](docs/ConnectionPoolUpdate.md)
 - [ConnectionPools](docs/ConnectionPools.md)
 - [ContainerRegistryGetActiveGarbageCollectionResponse](docs/ContainerRegistryGetActiveGarbageCollectionResponse.md)
 - [ContainerRegistryListGarbageCollectionsResponse](docs/ContainerRegistryListGarbageCollectionsResponse.md)
 - [ContainerRegistryListOptionsResponse](docs/ContainerRegistryListOptionsResponse.md)
 - [ContainerRegistryListOptionsResponseOptions](docs/ContainerRegistryListOptionsResponseOptions.md)
 - [ContainerRegistryListOptionsResponseOptionsSubscriptionTiersInner](docs/ContainerRegistryListOptionsResponseOptionsSubscriptionTiersInner.md)
 - [ContainerRegistryListRepositoriesResponse](docs/ContainerRegistryListRepositoriesResponse.md)
 - [ContainerRegistryListRepositoriesResponseAllOf](docs/ContainerRegistryListRepositoriesResponseAllOf.md)
 - [ContainerRegistryListRepositoriesV2Response](docs/ContainerRegistryListRepositoriesV2Response.md)
 - [ContainerRegistryListRepositoriesV2ResponseAllOf](docs/ContainerRegistryListRepositoriesV2ResponseAllOf.md)
 - [ContainerRegistryListRepositoryManifestsResponse](docs/ContainerRegistryListRepositoryManifestsResponse.md)
 - [ContainerRegistryListRepositoryManifestsResponseAllOf](docs/ContainerRegistryListRepositoryManifestsResponseAllOf.md)
 - [ContainerRegistryListRepositoryTagsResponse](docs/ContainerRegistryListRepositoryTagsResponse.md)
 - [ContainerRegistryListRepositoryTagsResponseAllOf](docs/ContainerRegistryListRepositoryTagsResponseAllOf.md)
 - [ContainerRegistryUpdateSubscriptionTierRequest](docs/ContainerRegistryUpdateSubscriptionTierRequest.md)
 - [CreateNamespace](docs/CreateNamespace.md)
 - [CreateTrigger](docs/CreateTrigger.md)
 - [Credentials](docs/Credentials.md)
 - [Database](docs/Database.md)
 - [DatabaseBackup](docs/DatabaseBackup.md)
 - [DatabaseCluster](docs/DatabaseCluster.md)
 - [DatabaseClusterConnection](docs/DatabaseClusterConnection.md)
 - [DatabaseClusterMaintenanceWindow](docs/DatabaseClusterMaintenanceWindow.md)
 - [DatabaseClusterResize](docs/DatabaseClusterResize.md)
 - [DatabaseConfig](docs/DatabaseConfig.md)
 - [DatabaseConnection](docs/DatabaseConnection.md)
 - [DatabaseLayoutOption](docs/DatabaseLayoutOption.md)
 - [DatabaseLayoutOptions](docs/DatabaseLayoutOptions.md)
 - [DatabaseMaintenanceWindow](docs/DatabaseMaintenanceWindow.md)
 - [DatabaseMetricsCredentials](docs/DatabaseMetricsCredentials.md)
 - [DatabaseRegionOptions](docs/DatabaseRegionOptions.md)
 - [DatabaseReplica](docs/DatabaseReplica.md)
 - [DatabaseReplicaConnection](docs/DatabaseReplicaConnection.md)
 - [DatabaseServiceEndpoint](docs/DatabaseServiceEndpoint.md)
 - [DatabaseUser](docs/DatabaseUser.md)
 - [DatabaseVersionAvailability](docs/DatabaseVersionAvailability.md)
 - [DatabaseVersionOptions](docs/DatabaseVersionOptions.md)
 - [DatabasesAddNewConnectionPoolResponse](docs/DatabasesAddNewConnectionPoolResponse.md)
 - [DatabasesAddResponse](docs/DatabasesAddResponse.md)
 - [DatabasesAddUserRequest](docs/DatabasesAddUserRequest.md)
 - [DatabasesAddUserRequestAllOf](docs/DatabasesAddUserRequestAllOf.md)
 - [DatabasesAddUserResponse](docs/DatabasesAddUserResponse.md)
 - [DatabasesBasicAuthCredentials](docs/DatabasesBasicAuthCredentials.md)
 - [DatabasesConfigureEvictionPolicyRequest](docs/DatabasesConfigureEvictionPolicyRequest.md)
 - [DatabasesCreateClusterRequest](docs/DatabasesCreateClusterRequest.md)
 - [DatabasesCreateClusterRequestAllOf](docs/DatabasesCreateClusterRequestAllOf.md)
 - [DatabasesCreateClusterResponse](docs/DatabasesCreateClusterResponse.md)
 - [DatabasesCreateReadOnlyReplicaResponse](docs/DatabasesCreateReadOnlyReplicaResponse.md)
 - [DatabasesCreateTopicKafkaClusterResponse](docs/DatabasesCreateTopicKafkaClusterResponse.md)
 - [DatabasesCreateTopicKafkaClusterResponseTopic](docs/DatabasesCreateTopicKafkaClusterResponseTopic.md)
 - [DatabasesCreateTopicKafkaClusterResponseTopicConfig](docs/DatabasesCreateTopicKafkaClusterResponseTopicConfig.md)
 - [DatabasesCreateTopicKafkaClusterResponseTopicPartitionsInner](docs/DatabasesCreateTopicKafkaClusterResponseTopicPartitionsInner.md)
 - [DatabasesCreateTopicKafkaClusterResponseTopicPartitionsInnerConsumerGroupsInner](docs/DatabasesCreateTopicKafkaClusterResponseTopicPartitionsInnerConsumerGroupsInner.md)
 - [DatabasesGetClusterConfigResponse](docs/DatabasesGetClusterConfigResponse.md)
 - [DatabasesGetClustersMetricsCredentialsResponse](docs/DatabasesGetClustersMetricsCredentialsResponse.md)
 - [DatabasesGetClustersMetricsCredentialsResponseCredentials](docs/DatabasesGetClustersMetricsCredentialsResponseCredentials.md)
 - [DatabasesGetEvictionPolicyResponse](docs/DatabasesGetEvictionPolicyResponse.md)
 - [DatabasesGetPublicCertificateResponse](docs/DatabasesGetPublicCertificateResponse.md)
 - [DatabasesListBackupsResponse](docs/DatabasesListBackupsResponse.md)
 - [DatabasesListClustersResponse](docs/DatabasesListClustersResponse.md)
 - [DatabasesListClustersResponseDatabasesInner](docs/DatabasesListClustersResponseDatabasesInner.md)
 - [DatabasesListClustersResponseDatabasesInnerConnection](docs/DatabasesListClustersResponseDatabasesInnerConnection.md)
 - [DatabasesListClustersResponseDatabasesInnerMaintenanceWindow](docs/DatabasesListClustersResponseDatabasesInnerMaintenanceWindow.md)
 - [DatabasesListClustersResponseDatabasesInnerPrivateConnection](docs/DatabasesListClustersResponseDatabasesInnerPrivateConnection.md)
 - [DatabasesListClustersResponseDatabasesInnerUsersInner](docs/DatabasesListClustersResponseDatabasesInnerUsersInner.md)
 - [DatabasesListEventsLogsResponse](docs/DatabasesListEventsLogsResponse.md)
 - [DatabasesListEventsLogsResponseEventsInner](docs/DatabasesListEventsLogsResponseEventsInner.md)
 - [DatabasesListFirewallRulesResponse](docs/DatabasesListFirewallRulesResponse.md)
 - [DatabasesListFirewallRulesResponseRulesInner](docs/DatabasesListFirewallRulesResponseRulesInner.md)
 - [DatabasesListReadOnlyReplicasResponse](docs/DatabasesListReadOnlyReplicasResponse.md)
 - [DatabasesListReadOnlyReplicasResponseReplicasInner](docs/DatabasesListReadOnlyReplicasResponseReplicasInner.md)
 - [DatabasesListReadOnlyReplicasResponseReplicasInnerConnection](docs/DatabasesListReadOnlyReplicasResponseReplicasInnerConnection.md)
 - [DatabasesListReadOnlyReplicasResponseReplicasInnerPrivateConnection](docs/DatabasesListReadOnlyReplicasResponseReplicasInnerPrivateConnection.md)
 - [DatabasesListResponse](docs/DatabasesListResponse.md)
 - [DatabasesListResponseDbsInner](docs/DatabasesListResponseDbsInner.md)
 - [DatabasesListTopicsKafkaClusterResponse](docs/DatabasesListTopicsKafkaClusterResponse.md)
 - [DatabasesListTopicsKafkaClusterResponseTopicsInner](docs/DatabasesListTopicsKafkaClusterResponseTopicsInner.md)
 - [DatabasesListUsersResponse](docs/DatabasesListUsersResponse.md)
 - [DatabasesListUsersResponseUsersInner](docs/DatabasesListUsersResponseUsersInner.md)
 - [DatabasesMigrateClusterToNewRegionRequest](docs/DatabasesMigrateClusterToNewRegionRequest.md)
 - [DatabasesResetUserAuthRequest](docs/DatabasesResetUserAuthRequest.md)
 - [DatabasesUpdateFirewallRulesRequest](docs/DatabasesUpdateFirewallRulesRequest.md)
 - [DatabasesUpdateSettingsRequest](docs/DatabasesUpdateSettingsRequest.md)
 - [DatabasesUpdateSettingsRequestAllOf](docs/DatabasesUpdateSettingsRequestAllOf.md)
 - [DestroyAssociatedKubernetesResources](docs/DestroyAssociatedKubernetesResources.md)
 - [DestroyedAssociatedResource](docs/DestroyedAssociatedResource.md)
 - [Distribution](docs/Distribution.md)
 - [DockerCredentials](docs/DockerCredentials.md)
 - [DockerCredentialsAuths](docs/DockerCredentialsAuths.md)
 - [DockerCredentialsAuthsRegistryDigitaloceanCom](docs/DockerCredentialsAuthsRegistryDigitaloceanCom.md)
 - [Domain](docs/Domain.md)
 - [DomainRecord](docs/DomainRecord.md)
 - [DomainRecordA](docs/DomainRecordA.md)
 - [DomainRecordAaaa](docs/DomainRecordAaaa.md)
 - [DomainRecordCaa](docs/DomainRecordCaa.md)
 - [DomainRecordCname](docs/DomainRecordCname.md)
 - [DomainRecordMx](docs/DomainRecordMx.md)
 - [DomainRecordNs](docs/DomainRecordNs.md)
 - [DomainRecordSoa](docs/DomainRecordSoa.md)
 - [DomainRecordSrv](docs/DomainRecordSrv.md)
 - [DomainRecordTxt](docs/DomainRecordTxt.md)
 - [DomainRecordsCreateNewRecordResponse](docs/DomainRecordsCreateNewRecordResponse.md)
 - [DomainRecordsGetExistingRecordResponse](docs/DomainRecordsGetExistingRecordResponse.md)
 - [DomainRecordsListAllRecordsResponse](docs/DomainRecordsListAllRecordsResponse.md)
 - [DomainRecordsListAllRecordsResponseAllOf](docs/DomainRecordsListAllRecordsResponseAllOf.md)
 - [DomainsCreateResponse](docs/DomainsCreateResponse.md)
 - [DomainsGetResponse](docs/DomainsGetResponse.md)
 - [DomainsListResponse](docs/DomainsListResponse.md)
 - [DomainsListResponseAllOf](docs/DomainsListResponseAllOf.md)
 - [Droplet](docs/Droplet.md)
 - [DropletAction](docs/DropletAction.md)
 - [DropletActionChangeKernel](docs/DropletActionChangeKernel.md)
 - [DropletActionChangeKernelAllOf](docs/DropletActionChangeKernelAllOf.md)
 - [DropletActionRebuild](docs/DropletActionRebuild.md)
 - [DropletActionRebuildAllOf](docs/DropletActionRebuildAllOf.md)
 - [DropletActionRename](docs/DropletActionRename.md)
 - [DropletActionRenameAllOf](docs/DropletActionRenameAllOf.md)
 - [DropletActionResize](docs/DropletActionResize.md)
 - [DropletActionResizeAllOf](docs/DropletActionResizeAllOf.md)
 - [DropletActionRestore](docs/DropletActionRestore.md)
 - [DropletActionRestoreAllOf](docs/DropletActionRestoreAllOf.md)
 - [DropletActionSnapshot](docs/DropletActionSnapshot.md)
 - [DropletActionSnapshotAllOf](docs/DropletActionSnapshotAllOf.md)
 - [DropletActionsListResponse](docs/DropletActionsListResponse.md)
 - [DropletCreate](docs/DropletCreate.md)
 - [DropletMultiCreate](docs/DropletMultiCreate.md)
 - [DropletMultiCreateAllOf](docs/DropletMultiCreateAllOf.md)
 - [DropletNetworks](docs/DropletNetworks.md)
 - [DropletNextBackupWindow](docs/DropletNextBackupWindow.md)
 - [DropletSingleCreate](docs/DropletSingleCreate.md)
 - [DropletSingleCreateAllOf](docs/DropletSingleCreateAllOf.md)
 - [DropletSnapshot](docs/DropletSnapshot.md)
 - [DropletSnapshotAllOf](docs/DropletSnapshotAllOf.md)
 - [DropletSnapshotAllOf1](docs/DropletSnapshotAllOf1.md)
 - [DropletsGetResponse](docs/DropletsGetResponse.md)
 - [DropletsGetResponseDroplet](docs/DropletsGetResponseDroplet.md)
 - [DropletsGetResponseDropletImage](docs/DropletsGetResponseDropletImage.md)
 - [DropletsGetResponseDropletNetworks](docs/DropletsGetResponseDropletNetworks.md)
 - [DropletsGetResponseDropletNetworksV4Inner](docs/DropletsGetResponseDropletNetworksV4Inner.md)
 - [DropletsGetResponseDropletNetworksV6Inner](docs/DropletsGetResponseDropletNetworksV6Inner.md)
 - [DropletsGetResponseDropletNextBackupWindow](docs/DropletsGetResponseDropletNextBackupWindow.md)
 - [DropletsGetResponseDropletRegion](docs/DropletsGetResponseDropletRegion.md)
 - [DropletsGetResponseDropletSize](docs/DropletsGetResponseDropletSize.md)
 - [DropletsListAssociatedResourcesResponse](docs/DropletsListAssociatedResourcesResponse.md)
 - [DropletsListAssociatedResourcesResponseAllOf](docs/DropletsListAssociatedResourcesResponseAllOf.md)
 - [DropletsListBackupsResponse](docs/DropletsListBackupsResponse.md)
 - [DropletsListBackupsResponseAllOf](docs/DropletsListBackupsResponseAllOf.md)
 - [DropletsListFirewallsResponse](docs/DropletsListFirewallsResponse.md)
 - [DropletsListFirewallsResponseAllOf](docs/DropletsListFirewallsResponseAllOf.md)
 - [DropletsListKernelsResponse](docs/DropletsListKernelsResponse.md)
 - [DropletsListKernelsResponseAllOf](docs/DropletsListKernelsResponseAllOf.md)
 - [DropletsListNeighborsResponse](docs/DropletsListNeighborsResponse.md)
 - [DropletsListResponse](docs/DropletsListResponse.md)
 - [DropletsListResponseAllOf](docs/DropletsListResponseAllOf.md)
 - [DropletsListSnapshotsResponse](docs/DropletsListSnapshotsResponse.md)
 - [DropletsListSnapshotsResponseAllOf](docs/DropletsListSnapshotsResponseAllOf.md)
 - [Error](docs/Error.md)
 - [EventsLogs](docs/EventsLogs.md)
 - [EvictionPolicyModel](docs/EvictionPolicyModel.md)
 - [Exvolumes](docs/Exvolumes.md)
 - [Firewall](docs/Firewall.md)
 - [FirewallAllOf](docs/FirewallAllOf.md)
 - [FirewallAllOfPendingChanges](docs/FirewallAllOfPendingChanges.md)
 - [FirewallRule](docs/FirewallRule.md)
 - [FirewallRuleBase](docs/FirewallRuleBase.md)
 - [FirewallRuleTarget](docs/FirewallRuleTarget.md)
 - [FirewallRules](docs/FirewallRules.md)
 - [FirewallRulesInboundRulesInner](docs/FirewallRulesInboundRulesInner.md)
 - [FirewallRulesInboundRulesInnerAllOf](docs/FirewallRulesInboundRulesInnerAllOf.md)
 - [FirewallRulesInboundRulesInnerAllOfSources](docs/FirewallRulesInboundRulesInnerAllOfSources.md)
 - [FirewallRulesOutboundRulesInner](docs/FirewallRulesOutboundRulesInner.md)
 - [FirewallRulesOutboundRulesInnerAllOf](docs/FirewallRulesOutboundRulesInnerAllOf.md)
 - [FirewallRulesOutboundRulesInnerAllOfDestinations](docs/FirewallRulesOutboundRulesInnerAllOfDestinations.md)
 - [FirewallsAddDropletsRequest](docs/FirewallsAddDropletsRequest.md)
 - [FirewallsAddRulesRequest](docs/FirewallsAddRulesRequest.md)
 - [FirewallsAddTagsRequest](docs/FirewallsAddTagsRequest.md)
 - [FirewallsCreateRequest](docs/FirewallsCreateRequest.md)
 - [FirewallsCreateResponse](docs/FirewallsCreateResponse.md)
 - [FirewallsCreateResponseFirewall](docs/FirewallsCreateResponseFirewall.md)
 - [FirewallsCreateResponseFirewallInboundRulesInner](docs/FirewallsCreateResponseFirewallInboundRulesInner.md)
 - [FirewallsCreateResponseFirewallInboundRulesInnerSources](docs/FirewallsCreateResponseFirewallInboundRulesInnerSources.md)
 - [FirewallsCreateResponseFirewallOutboundRulesInner](docs/FirewallsCreateResponseFirewallOutboundRulesInner.md)
 - [FirewallsCreateResponseFirewallOutboundRulesInnerDestinations](docs/FirewallsCreateResponseFirewallOutboundRulesInnerDestinations.md)
 - [FirewallsCreateResponseFirewallPendingChangesInner](docs/FirewallsCreateResponseFirewallPendingChangesInner.md)
 - [FirewallsGetResponse](docs/FirewallsGetResponse.md)
 - [FirewallsGetResponseFirewall](docs/FirewallsGetResponseFirewall.md)
 - [FirewallsGetResponseFirewallInboundRulesInner](docs/FirewallsGetResponseFirewallInboundRulesInner.md)
 - [FirewallsGetResponseFirewallInboundRulesInnerSources](docs/FirewallsGetResponseFirewallInboundRulesInnerSources.md)
 - [FirewallsGetResponseFirewallOutboundRulesInner](docs/FirewallsGetResponseFirewallOutboundRulesInner.md)
 - [FirewallsGetResponseFirewallOutboundRulesInnerDestinations](docs/FirewallsGetResponseFirewallOutboundRulesInnerDestinations.md)
 - [FirewallsListResponse](docs/FirewallsListResponse.md)
 - [FirewallsRemoveDropletsRequest](docs/FirewallsRemoveDropletsRequest.md)
 - [FirewallsRemoveRulesRequest](docs/FirewallsRemoveRulesRequest.md)
 - [FirewallsRemoveTagsRequest](docs/FirewallsRemoveTagsRequest.md)
 - [FirewallsUpdateRequest](docs/FirewallsUpdateRequest.md)
 - [FirewallsUpdateResponse](docs/FirewallsUpdateResponse.md)
 - [FirewallsUpdateResponseFirewall](docs/FirewallsUpdateResponseFirewall.md)
 - [FirewallsUpdateResponseFirewallInboundRulesInner](docs/FirewallsUpdateResponseFirewallInboundRulesInner.md)
 - [FirewallsUpdateResponseFirewallInboundRulesInnerSources](docs/FirewallsUpdateResponseFirewallInboundRulesInnerSources.md)
 - [FirewallsUpdateResponseFirewallOutboundRulesInner](docs/FirewallsUpdateResponseFirewallOutboundRulesInner.md)
 - [FirewallsUpdateResponseFirewallOutboundRulesInnerDestinations](docs/FirewallsUpdateResponseFirewallOutboundRulesInnerDestinations.md)
 - [FirewallsUpdateResponseFirewallPendingChangesInner](docs/FirewallsUpdateResponseFirewallPendingChangesInner.md)
 - [FloatingIPsAction](docs/FloatingIPsAction.md)
 - [FloatingIPsActionListResponse](docs/FloatingIPsActionListResponse.md)
 - [FloatingIPsActionPostResponse](docs/FloatingIPsActionPostResponse.md)
 - [FloatingIPsActionPostResponseAction](docs/FloatingIPsActionPostResponseAction.md)
 - [FloatingIPsActionPostResponseActionAllOf](docs/FloatingIPsActionPostResponseActionAllOf.md)
 - [FloatingIPsCreateResponse](docs/FloatingIPsCreateResponse.md)
 - [FloatingIPsCreateResponseLinks](docs/FloatingIPsCreateResponseLinks.md)
 - [FloatingIPsGetResponse](docs/FloatingIPsGetResponse.md)
 - [FloatingIPsListResponse](docs/FloatingIPsListResponse.md)
 - [FloatingIPsListResponseAllOf](docs/FloatingIPsListResponseAllOf.md)
 - [FloatingIp](docs/FloatingIp.md)
 - [FloatingIpActionAssign](docs/FloatingIpActionAssign.md)
 - [FloatingIpActionAssignAllOf](docs/FloatingIpActionAssignAllOf.md)
 - [FloatingIpActionUnassign](docs/FloatingIpActionUnassign.md)
 - [FloatingIpRegion](docs/FloatingIpRegion.md)
 - [ForwardLinks](docs/ForwardLinks.md)
 - [ForwardingRule](docs/ForwardingRule.md)
 - [FunctionsCreateNamespaceResponse](docs/FunctionsCreateNamespaceResponse.md)
 - [FunctionsCreateTriggerInNamespaceResponse](docs/FunctionsCreateTriggerInNamespaceResponse.md)
 - [FunctionsListNamespacesResponse](docs/FunctionsListNamespacesResponse.md)
 - [FunctionsListNamespacesResponseAllOf](docs/FunctionsListNamespacesResponseAllOf.md)
 - [FunctionsListTriggersResponse](docs/FunctionsListTriggersResponse.md)
 - [FunctionsListTriggersResponseAllOf](docs/FunctionsListTriggersResponseAllOf.md)
 - [GarbageCollection](docs/GarbageCollection.md)
 - [HealthCheck](docs/HealthCheck.md)
 - [Image](docs/Image.md)
 - [ImageActionBase](docs/ImageActionBase.md)
 - [ImageActionTransfer](docs/ImageActionTransfer.md)
 - [ImageActionTransferAllOf](docs/ImageActionTransferAllOf.md)
 - [ImageActionsListResponse](docs/ImageActionsListResponse.md)
 - [ImageNewCustom](docs/ImageNewCustom.md)
 - [ImageNewCustomAllOf](docs/ImageNewCustomAllOf.md)
 - [ImageUpdate](docs/ImageUpdate.md)
 - [ImagesGetResponse](docs/ImagesGetResponse.md)
 - [ImagesImportCustomImageFromUrlResponse](docs/ImagesImportCustomImageFromUrlResponse.md)
 - [ImagesImportCustomImageFromUrlResponseImage](docs/ImagesImportCustomImageFromUrlResponseImage.md)
 - [ImagesListResponse](docs/ImagesListResponse.md)
 - [ImagesListResponseAllOf](docs/ImagesListResponseAllOf.md)
 - [ImagesUpdateResponse](docs/ImagesUpdateResponse.md)
 - [InstanceSizeCpuType](docs/InstanceSizeCpuType.md)
 - [InvoiceItem](docs/InvoiceItem.md)
 - [InvoicePreview](docs/InvoicePreview.md)
 - [InvoiceSummary](docs/InvoiceSummary.md)
 - [InvoiceSummaryCreditsAndAdjustments](docs/InvoiceSummaryCreditsAndAdjustments.md)
 - [InvoiceSummaryOverages](docs/InvoiceSummaryOverages.md)
 - [InvoiceSummaryProductCharges](docs/InvoiceSummaryProductCharges.md)
 - [InvoiceSummaryTaxes](docs/InvoiceSummaryTaxes.md)
 - [InvoiceSummaryUserBillingAddress](docs/InvoiceSummaryUserBillingAddress.md)
 - [InvoicesListResponse](docs/InvoicesListResponse.md)
 - [InvoicesListResponseAllOf](docs/InvoicesListResponseAllOf.md)
 - [KafkaTopic](docs/KafkaTopic.md)
 - [KafkaTopicAllOf](docs/KafkaTopicAllOf.md)
 - [KafkaTopicBase](docs/KafkaTopicBase.md)
 - [KafkaTopicConfig](docs/KafkaTopicConfig.md)
 - [KafkaTopicCreate](docs/KafkaTopicCreate.md)
 - [KafkaTopicCreateAllOf](docs/KafkaTopicCreateAllOf.md)
 - [KafkaTopicPartition](docs/KafkaTopicPartition.md)
 - [KafkaTopicPartitionConsumerGroupsInner](docs/KafkaTopicPartitionConsumerGroupsInner.md)
 - [KafkaTopicUpdate](docs/KafkaTopicUpdate.md)
 - [KafkaTopicVerbose](docs/KafkaTopicVerbose.md)
 - [Kernel](docs/Kernel.md)
 - [KubernetesAddNodePoolResponse](docs/KubernetesAddNodePoolResponse.md)
 - [KubernetesCreateNewClusterResponse](docs/KubernetesCreateNewClusterResponse.md)
 - [KubernetesCreateNewClusterResponseKubernetesCluster](docs/KubernetesCreateNewClusterResponseKubernetesCluster.md)
 - [KubernetesCreateNewClusterResponseKubernetesClusterMaintenancePolicy](docs/KubernetesCreateNewClusterResponseKubernetesClusterMaintenancePolicy.md)
 - [KubernetesCreateNewClusterResponseKubernetesClusterNodePoolsInner](docs/KubernetesCreateNewClusterResponseKubernetesClusterNodePoolsInner.md)
 - [KubernetesCreateNewClusterResponseKubernetesClusterNodePoolsInnerNodesInner](docs/KubernetesCreateNewClusterResponseKubernetesClusterNodePoolsInnerNodesInner.md)
 - [KubernetesCreateNewClusterResponseKubernetesClusterNodePoolsInnerNodesInnerStatus](docs/KubernetesCreateNewClusterResponseKubernetesClusterNodePoolsInnerNodesInnerStatus.md)
 - [KubernetesCreateNewClusterResponseKubernetesClusterStatus](docs/KubernetesCreateNewClusterResponseKubernetesClusterStatus.md)
 - [KubernetesCreateNewClusterResponseKubernetesClusters](docs/KubernetesCreateNewClusterResponseKubernetesClusters.md)
 - [KubernetesCreateNewClusterResponseKubernetesClustersMaintenancePolicy](docs/KubernetesCreateNewClusterResponseKubernetesClustersMaintenancePolicy.md)
 - [KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInner](docs/KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInner.md)
 - [KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInnerLabels](docs/KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInnerLabels.md)
 - [KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInnerNodesInner](docs/KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInnerNodesInner.md)
 - [KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInnerNodesInnerStatus](docs/KubernetesCreateNewClusterResponseKubernetesClustersNodePoolsInnerNodesInnerStatus.md)
 - [KubernetesCreateNewClusterResponseKubernetesClustersStatus](docs/KubernetesCreateNewClusterResponseKubernetesClustersStatus.md)
 - [KubernetesGetAvailableUpgradesResponse](docs/KubernetesGetAvailableUpgradesResponse.md)
 - [KubernetesGetClusterInfoResponse](docs/KubernetesGetClusterInfoResponse.md)
 - [KubernetesGetClusterInfoResponseKubernetesCluster](docs/KubernetesGetClusterInfoResponseKubernetesCluster.md)
 - [KubernetesGetClusterInfoResponseKubernetesClusterMaintenancePolicy](docs/KubernetesGetClusterInfoResponseKubernetesClusterMaintenancePolicy.md)
 - [KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInner](docs/KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInner.md)
 - [KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInnerLabels](docs/KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInnerLabels.md)
 - [KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInnerNodesInner](docs/KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInnerNodesInner.md)
 - [KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInnerNodesInnerStatus](docs/KubernetesGetClusterInfoResponseKubernetesClusterNodePoolsInnerNodesInnerStatus.md)
 - [KubernetesGetClusterInfoResponseKubernetesClusterStatus](docs/KubernetesGetClusterInfoResponseKubernetesClusterStatus.md)
 - [KubernetesGetNodePoolResponse](docs/KubernetesGetNodePoolResponse.md)
 - [KubernetesListClustersResponse](docs/KubernetesListClustersResponse.md)
 - [KubernetesListClustersResponseAllOf](docs/KubernetesListClustersResponseAllOf.md)
 - [KubernetesListNodePoolsResponse](docs/KubernetesListNodePoolsResponse.md)
 - [KubernetesListNodePoolsResponseNodePoolsInner](docs/KubernetesListNodePoolsResponseNodePoolsInner.md)
 - [KubernetesListNodePoolsResponseNodePoolsInnerLabels](docs/KubernetesListNodePoolsResponseNodePoolsInnerLabels.md)
 - [KubernetesListNodePoolsResponseNodePoolsInnerNodesInner](docs/KubernetesListNodePoolsResponseNodePoolsInnerNodesInner.md)
 - [KubernetesListNodePoolsResponseNodePoolsInnerNodesInnerStatus](docs/KubernetesListNodePoolsResponseNodePoolsInnerNodesInnerStatus.md)
 - [KubernetesNodePool](docs/KubernetesNodePool.md)
 - [KubernetesNodePoolBase](docs/KubernetesNodePoolBase.md)
 - [KubernetesNodePoolSize](docs/KubernetesNodePoolSize.md)
 - [KubernetesNodePoolTaint](docs/KubernetesNodePoolTaint.md)
 - [KubernetesOptions](docs/KubernetesOptions.md)
 - [KubernetesOptionsOptions](docs/KubernetesOptionsOptions.md)
 - [KubernetesRecycleNodePoolRequest](docs/KubernetesRecycleNodePoolRequest.md)
 - [KubernetesRegion](docs/KubernetesRegion.md)
 - [KubernetesSize](docs/KubernetesSize.md)
 - [KubernetesUpdateClusterResponse](docs/KubernetesUpdateClusterResponse.md)
 - [KubernetesUpdateClusterResponseKubernetesCluster](docs/KubernetesUpdateClusterResponseKubernetesCluster.md)
 - [KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy](docs/KubernetesUpdateClusterResponseKubernetesClusterMaintenancePolicy.md)
 - [KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner](docs/KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInner.md)
 - [KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInnerLabels](docs/KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInnerLabels.md)
 - [KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInnerNodesInner](docs/KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInnerNodesInner.md)
 - [KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInnerNodesInnerStatus](docs/KubernetesUpdateClusterResponseKubernetesClusterNodePoolsInnerNodesInnerStatus.md)
 - [KubernetesUpdateClusterResponseKubernetesClusterStatus](docs/KubernetesUpdateClusterResponseKubernetesClusterStatus.md)
 - [KubernetesUpdateNodePoolResponse](docs/KubernetesUpdateNodePoolResponse.md)
 - [KubernetesUpgradeClusterRequest](docs/KubernetesUpgradeClusterRequest.md)
 - [KubernetesVersion](docs/KubernetesVersion.md)
 - [LbFirewall](docs/LbFirewall.md)
 - [LinkToFirstPage](docs/LinkToFirstPage.md)
 - [LinkToLastPage](docs/LinkToLastPage.md)
 - [LinkToNextPage](docs/LinkToNextPage.md)
 - [LinkToPrevPage](docs/LinkToPrevPage.md)
 - [ListAlertPolicy](docs/ListAlertPolicy.md)
 - [LoadBalancer](docs/LoadBalancer.md)
 - [LoadBalancerAllOf](docs/LoadBalancerAllOf.md)
 - [LoadBalancerAllOf1](docs/LoadBalancerAllOf1.md)
 - [LoadBalancerAllOf2](docs/LoadBalancerAllOf2.md)
 - [LoadBalancerBase](docs/LoadBalancerBase.md)
 - [LoadBalancersAddForwardingRulesRequest](docs/LoadBalancersAddForwardingRulesRequest.md)
 - [LoadBalancersAssignDropletsRequest](docs/LoadBalancersAssignDropletsRequest.md)
 - [LoadBalancersCreateResponse](docs/LoadBalancersCreateResponse.md)
 - [LoadBalancersCreateResponseLoadBalancer](docs/LoadBalancersCreateResponseLoadBalancer.md)
 - [LoadBalancersCreateResponseLoadBalancerFirewall](docs/LoadBalancersCreateResponseLoadBalancerFirewall.md)
 - [LoadBalancersCreateResponseLoadBalancerForwardingRulesInner](docs/LoadBalancersCreateResponseLoadBalancerForwardingRulesInner.md)
 - [LoadBalancersCreateResponseLoadBalancerHealthCheck](docs/LoadBalancersCreateResponseLoadBalancerHealthCheck.md)
 - [LoadBalancersCreateResponseLoadBalancerRegion](docs/LoadBalancersCreateResponseLoadBalancerRegion.md)
 - [LoadBalancersCreateResponseLoadBalancerStickySessions](docs/LoadBalancersCreateResponseLoadBalancerStickySessions.md)
 - [LoadBalancersGetResponse](docs/LoadBalancersGetResponse.md)
 - [LoadBalancersGetResponseLoadBalancer](docs/LoadBalancersGetResponseLoadBalancer.md)
 - [LoadBalancersGetResponseLoadBalancerFirewall](docs/LoadBalancersGetResponseLoadBalancerFirewall.md)
 - [LoadBalancersGetResponseLoadBalancerForwardingRulesInner](docs/LoadBalancersGetResponseLoadBalancerForwardingRulesInner.md)
 - [LoadBalancersGetResponseLoadBalancerHealthCheck](docs/LoadBalancersGetResponseLoadBalancerHealthCheck.md)
 - [LoadBalancersGetResponseLoadBalancerRegion](docs/LoadBalancersGetResponseLoadBalancerRegion.md)
 - [LoadBalancersGetResponseLoadBalancerStickySessions](docs/LoadBalancersGetResponseLoadBalancerStickySessions.md)
 - [LoadBalancersListResponse](docs/LoadBalancersListResponse.md)
 - [LoadBalancersListResponseAllOf](docs/LoadBalancersListResponseAllOf.md)
 - [LoadBalancersRemoveDropletsRequest](docs/LoadBalancersRemoveDropletsRequest.md)
 - [LoadBalancersRemoveForwardingRulesRequest](docs/LoadBalancersRemoveForwardingRulesRequest.md)
 - [LoadBalancersUpdateResponse](docs/LoadBalancersUpdateResponse.md)
 - [LoadBalancersUpdateResponseLoadBalancer](docs/LoadBalancersUpdateResponseLoadBalancer.md)
 - [LoadBalancersUpdateResponseLoadBalancerFirewall](docs/LoadBalancersUpdateResponseLoadBalancerFirewall.md)
 - [LoadBalancersUpdateResponseLoadBalancerForwardingRulesInner](docs/LoadBalancersUpdateResponseLoadBalancerForwardingRulesInner.md)
 - [LoadBalancersUpdateResponseLoadBalancerHealthCheck](docs/LoadBalancersUpdateResponseLoadBalancerHealthCheck.md)
 - [LoadBalancersUpdateResponseLoadBalancerRegion](docs/LoadBalancersUpdateResponseLoadBalancerRegion.md)
 - [LoadBalancersUpdateResponseLoadBalancerStickySessions](docs/LoadBalancersUpdateResponseLoadBalancerStickySessions.md)
 - [MaintenancePolicy](docs/MaintenancePolicy.md)
 - [Meta](docs/Meta.md)
 - [MetaMeta](docs/MetaMeta.md)
 - [MetaOptionalTotal](docs/MetaOptionalTotal.md)
 - [MetaProperties](docs/MetaProperties.md)
 - [Metrics](docs/Metrics.md)
 - [MetricsData](docs/MetricsData.md)
 - [MetricsResult](docs/MetricsResult.md)
 - [Model1ClickApplicationsInstallKubernetesApplicationResponse](docs/Model1ClickApplicationsInstallKubernetesApplicationResponse.md)
 - [MonitoringListAlertPoliciesResponse](docs/MonitoringListAlertPoliciesResponse.md)
 - [Mpr](docs/Mpr.md)
 - [Mpr1](docs/Mpr1.md)
 - [Mysql](docs/Mysql.md)
 - [MysqlPgbouncer](docs/MysqlPgbouncer.md)
 - [MysqlSettings](docs/MysqlSettings.md)
 - [MysqlTimescaledb](docs/MysqlTimescaledb.md)
 - [NamespaceInfo](docs/NamespaceInfo.md)
 - [NeighborIds](docs/NeighborIds.md)
 - [NetworkV4](docs/NetworkV4.md)
 - [NetworkV6](docs/NetworkV6.md)
 - [Node](docs/Node.md)
 - [NodeStatus](docs/NodeStatus.md)
 - [Notification](docs/Notification.md)
 - [NotificationSlackInner](docs/NotificationSlackInner.md)
 - [NullProperty](docs/NullProperty.md)
 - [NullProperty1](docs/NullProperty1.md)
 - [OneClicks](docs/OneClicks.md)
 - [OneClicksCreate](docs/OneClicksCreate.md)
 - [OneClicksListResponse](docs/OneClicksListResponse.md)
 - [OneClicksListResponse1ClicksInner](docs/OneClicksListResponse1ClicksInner.md)
 - [OnlineMigration](docs/OnlineMigration.md)
 - [Options](docs/Options.md)
 - [OptionsOptions](docs/OptionsOptions.md)
 - [OptionsOptionsKafka](docs/OptionsOptionsKafka.md)
 - [OptionsVersionAvailability](docs/OptionsVersionAvailability.md)
 - [PageLinks](docs/PageLinks.md)
 - [Pagination](docs/Pagination.md)
 - [Pgbouncer](docs/Pgbouncer.md)
 - [Postgres](docs/Postgres.md)
 - [PreviousOutage](docs/PreviousOutage.md)
 - [ProductChargeItem](docs/ProductChargeItem.md)
 - [ProductUsageCharges](docs/ProductUsageCharges.md)
 - [Project](docs/Project.md)
 - [ProjectAllOf](docs/ProjectAllOf.md)
 - [ProjectAssignment](docs/ProjectAssignment.md)
 - [ProjectBase](docs/ProjectBase.md)
 - [ProjectResourcesAssignToProjectResponse](docs/ProjectResourcesAssignToProjectResponse.md)
 - [ProjectResourcesListResponse](docs/ProjectResourcesListResponse.md)
 - [ProjectResourcesListResponseAllOf](docs/ProjectResourcesListResponseAllOf.md)
 - [ProjectsGetDefaultProjectResponse](docs/ProjectsGetDefaultProjectResponse.md)
 - [ProjectsListResponse](docs/ProjectsListResponse.md)
 - [ProjectsListResponseAllOf](docs/ProjectsListResponseAllOf.md)
 - [PurgeCache](docs/PurgeCache.md)
 - [Redis](docs/Redis.md)
 - [Region](docs/Region.md)
 - [RegionSlug](docs/RegionSlug.md)
 - [RegionState](docs/RegionState.md)
 - [RegionalState](docs/RegionalState.md)
 - [RegionsListResponse](docs/RegionsListResponse.md)
 - [RegionsListResponseAllOf](docs/RegionsListResponseAllOf.md)
 - [Registry](docs/Registry.md)
 - [RegistryCreate](docs/RegistryCreate.md)
 - [RegistrySubscription](docs/RegistrySubscription.md)
 - [Repository](docs/Repository.md)
 - [RepositoryBlob](docs/RepositoryBlob.md)
 - [RepositoryManifest](docs/RepositoryManifest.md)
 - [RepositoryTag](docs/RepositoryTag.md)
 - [RepositoryV2](docs/RepositoryV2.md)
 - [ReservedIPsActionsListResponse](docs/ReservedIPsActionsListResponse.md)
 - [ReservedIPsActionsPostResponse](docs/ReservedIPsActionsPostResponse.md)
 - [ReservedIPsActionsPostResponseAction](docs/ReservedIPsActionsPostResponseAction.md)
 - [ReservedIPsActionsPostResponseActionAllOf](docs/ReservedIPsActionsPostResponseActionAllOf.md)
 - [ReservedIPsCreateResponse](docs/ReservedIPsCreateResponse.md)
 - [ReservedIPsGetResponse](docs/ReservedIPsGetResponse.md)
 - [ReservedIPsListResponse](docs/ReservedIPsListResponse.md)
 - [ReservedIPsListResponseAllOf](docs/ReservedIPsListResponseAllOf.md)
 - [ReservedIp](docs/ReservedIp.md)
 - [ReservedIpActionAssign](docs/ReservedIpActionAssign.md)
 - [ReservedIpActionAssignAllOf](docs/ReservedIpActionAssignAllOf.md)
 - [ReservedIpActionType](docs/ReservedIpActionType.md)
 - [ReservedIpActionUnassign](docs/ReservedIpActionUnassign.md)
 - [ReservedIpRegion](docs/ReservedIpRegion.md)
 - [Resource](docs/Resource.md)
 - [ResourceLinks](docs/ResourceLinks.md)
 - [ScheduledDetails](docs/ScheduledDetails.md)
 - [ScheduledDetailsBody](docs/ScheduledDetailsBody.md)
 - [SelectiveDestroyAssociatedResource](docs/SelectiveDestroyAssociatedResource.md)
 - [SimpleCharge](docs/SimpleCharge.md)
 - [Size](docs/Size.md)
 - [SizesListResponse](docs/SizesListResponse.md)
 - [SizesListResponseAllOf](docs/SizesListResponseAllOf.md)
 - [SlackDetails](docs/SlackDetails.md)
 - [Snapshots](docs/Snapshots.md)
 - [SnapshotsAllOf](docs/SnapshotsAllOf.md)
 - [SnapshotsAllOf1](docs/SnapshotsAllOf1.md)
 - [SnapshotsBase](docs/SnapshotsBase.md)
 - [SnapshotsListResponse](docs/SnapshotsListResponse.md)
 - [SnapshotsListResponseAllOf](docs/SnapshotsListResponseAllOf.md)
 - [SourceDatabase](docs/SourceDatabase.md)
 - [SourceDatabaseSource](docs/SourceDatabaseSource.md)
 - [SqlMode](docs/SqlMode.md)
 - [SshKeys](docs/SshKeys.md)
 - [SshKeysListResponse](docs/SshKeysListResponse.md)
 - [SshKeysListResponseAllOf](docs/SshKeysListResponseAllOf.md)
 - [SshKeysUpdateRequest](docs/SshKeysUpdateRequest.md)
 - [State](docs/State.md)
 - [StickySessions](docs/StickySessions.md)
 - [Subscription](docs/Subscription.md)
 - [SubscriptionTierBase](docs/SubscriptionTierBase.md)
 - [SubscriptionTierExtended](docs/SubscriptionTierExtended.md)
 - [Tags](docs/Tags.md)
 - [TagsCreateResponse](docs/TagsCreateResponse.md)
 - [TagsGetResponse](docs/TagsGetResponse.md)
 - [TagsListResponse](docs/TagsListResponse.md)
 - [TagsListResponseAllOf](docs/TagsListResponseAllOf.md)
 - [TagsMetadata](docs/TagsMetadata.md)
 - [TagsResource](docs/TagsResource.md)
 - [TagsResourceResourcesInner](docs/TagsResourceResourcesInner.md)
 - [TagsResources](docs/TagsResources.md)
 - [TagsResourcesAllOf](docs/TagsResourcesAllOf.md)
 - [Timescaledb](docs/Timescaledb.md)
 - [TriggerInfo](docs/TriggerInfo.md)
 - [TriggerInfoScheduledRuns](docs/TriggerInfoScheduledRuns.md)
 - [UpdateEndpoint](docs/UpdateEndpoint.md)
 - [UpdateRegistry](docs/UpdateRegistry.md)
 - [UpdateTrigger](docs/UpdateTrigger.md)
 - [UptimeCreateCheckResponse](docs/UptimeCreateCheckResponse.md)
 - [UptimeCreateNewAlertResponse](docs/UptimeCreateNewAlertResponse.md)
 - [UptimeGetCheckStateResponse](docs/UptimeGetCheckStateResponse.md)
 - [UptimeListAllAlertsResponse](docs/UptimeListAllAlertsResponse.md)
 - [UptimeListAllAlertsResponseAllOf](docs/UptimeListAllAlertsResponseAllOf.md)
 - [UptimeListChecksResponse](docs/UptimeListChecksResponse.md)
 - [UptimeListChecksResponseAllOf](docs/UptimeListChecksResponseAllOf.md)
 - [User](docs/User.md)
 - [UserKubernetesClusterUser](docs/UserKubernetesClusterUser.md)
 - [UserSettings](docs/UserSettings.md)
 - [UserSettingsAclInner](docs/UserSettingsAclInner.md)
 - [ValidateRegistry](docs/ValidateRegistry.md)
 - [Version2](docs/Version2.md)
 - [VolumeAction](docs/VolumeAction.md)
 - [VolumeActionAllOf](docs/VolumeActionAllOf.md)
 - [VolumeActionPostAttach](docs/VolumeActionPostAttach.md)
 - [VolumeActionPostAttachAllOf](docs/VolumeActionPostAttachAllOf.md)
 - [VolumeActionPostBase](docs/VolumeActionPostBase.md)
 - [VolumeActionPostDetach](docs/VolumeActionPostDetach.md)
 - [VolumeActionPostDetachAllOf](docs/VolumeActionPostDetachAllOf.md)
 - [VolumeActionPostResize](docs/VolumeActionPostResize.md)
 - [VolumeActionPostResizeAllOf](docs/VolumeActionPostResizeAllOf.md)
 - [VolumeActionsListResponse](docs/VolumeActionsListResponse.md)
 - [VolumeActionsListResponseAllOf](docs/VolumeActionsListResponseAllOf.md)
 - [VolumeActionsPostResponse](docs/VolumeActionsPostResponse.md)
 - [VolumeActionsPostResponseAction](docs/VolumeActionsPostResponseAction.md)
 - [VolumeActionsPostResponseActionRegion](docs/VolumeActionsPostResponseActionRegion.md)
 - [VolumeBase](docs/VolumeBase.md)
 - [VolumeFull](docs/VolumeFull.md)
 - [VolumeFullAllOf](docs/VolumeFullAllOf.md)
 - [VolumeSnapshotId](docs/VolumeSnapshotId.md)
 - [VolumeSnapshotsCreateRequest](docs/VolumeSnapshotsCreateRequest.md)
 - [VolumeSnapshotsListResponse](docs/VolumeSnapshotsListResponse.md)
 - [VolumeWriteFileSystemType](docs/VolumeWriteFileSystemType.md)
 - [VolumesCreateResponse](docs/VolumesCreateResponse.md)
 - [VolumesExt4](docs/VolumesExt4.md)
 - [VolumesExt4AllOf](docs/VolumesExt4AllOf.md)
 - [VolumesListResponse](docs/VolumesListResponse.md)
 - [VolumesListResponseAllOf](docs/VolumesListResponseAllOf.md)
 - [VolumesXfs](docs/VolumesXfs.md)
 - [VolumesXfsAllOf](docs/VolumesXfsAllOf.md)
 - [VpCsListMembersResponse](docs/VpCsListMembersResponse.md)
 - [VpCsListMembersResponseAllOf](docs/VpCsListMembersResponseAllOf.md)
 - [Vpc](docs/Vpc.md)
 - [VpcBase](docs/VpcBase.md)
 - [VpcCreate](docs/VpcCreate.md)
 - [VpcDefault](docs/VpcDefault.md)
 - [VpcMember](docs/VpcMember.md)
 - [VpcUpdatable](docs/VpcUpdatable.md)
 - [VpcsCreateRequest](docs/VpcsCreateRequest.md)
 - [VpcsCreateResponse](docs/VpcsCreateResponse.md)
 - [VpcsListResponse](docs/VpcsListResponse.md)
 - [VpcsListResponseAllOf](docs/VpcsListResponseAllOf.md)
 - [VpcsPatchRequest](docs/VpcsPatchRequest.md)
 - [VpcsUpdateRequest](docs/VpcsUpdateRequest.md)


## Author
This Java package is automatically generated by [Konfig](https://konfigthis.com)
