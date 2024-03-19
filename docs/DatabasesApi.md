# DatabasesApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**add**](DatabasesApi.md#add) | **POST** /v2/databases/{database_cluster_uuid}/dbs | Add a New Database |
| [**addNewConnectionPool**](DatabasesApi.md#addNewConnectionPool) | **POST** /v2/databases/{database_cluster_uuid}/pools | Add a New Connection Pool (PostgreSQL) |
| [**addUser**](DatabasesApi.md#addUser) | **POST** /v2/databases/{database_cluster_uuid}/users | Add a Database User |
| [**configureEvictionPolicy**](DatabasesApi.md#configureEvictionPolicy) | **PUT** /v2/databases/{database_cluster_uuid}/eviction_policy | Configure the Eviction Policy for a Redis Cluster |
| [**configureMaintenanceWindow**](DatabasesApi.md#configureMaintenanceWindow) | **PUT** /v2/databases/{database_cluster_uuid}/maintenance | Configure a Database Cluster&#39;s Maintenance Window |
| [**createCluster**](DatabasesApi.md#createCluster) | **POST** /v2/databases | Create a New Database Cluster |
| [**createReadOnlyReplica**](DatabasesApi.md#createReadOnlyReplica) | **POST** /v2/databases/{database_cluster_uuid}/replicas | Create a Read-only Replica |
| [**createTopicKafkaCluster**](DatabasesApi.md#createTopicKafkaCluster) | **POST** /v2/databases/{database_cluster_uuid}/topics | Create Topic for a Kafka Cluster |
| [**delete**](DatabasesApi.md#delete) | **DELETE** /v2/databases/{database_cluster_uuid}/dbs/{database_name} | Delete a Database |
| [**deleteConnectionPool**](DatabasesApi.md#deleteConnectionPool) | **DELETE** /v2/databases/{database_cluster_uuid}/pools/{pool_name} | Delete a Connection Pool (PostgreSQL) |
| [**deleteTopicKafkaCluster**](DatabasesApi.md#deleteTopicKafkaCluster) | **DELETE** /v2/databases/{database_cluster_uuid}/topics/{topic_name} | Delete Topic for a Kafka Cluster |
| [**destroyCluster**](DatabasesApi.md#destroyCluster) | **DELETE** /v2/databases/{database_cluster_uuid} | Destroy a Database Cluster |
| [**destroyReadonlyReplica**](DatabasesApi.md#destroyReadonlyReplica) | **DELETE** /v2/databases/{database_cluster_uuid}/replicas/{replica_name} | Destroy a Read-only Replica |
| [**get**](DatabasesApi.md#get) | **GET** /v2/databases/{database_cluster_uuid}/dbs/{database_name} | Retrieve an Existing Database |
| [**getClusterConfig**](DatabasesApi.md#getClusterConfig) | **GET** /v2/databases/{database_cluster_uuid}/config | Retrieve an Existing Database Cluster Configuration |
| [**getClusterInfo**](DatabasesApi.md#getClusterInfo) | **GET** /v2/databases/{database_cluster_uuid} | Retrieve an Existing Database Cluster |
| [**getClustersMetricsCredentials**](DatabasesApi.md#getClustersMetricsCredentials) | **GET** /v2/databases/metrics/credentials | Retrieve Database Clusters&#39; Metrics Endpoint Credentials |
| [**getConnectionPool**](DatabasesApi.md#getConnectionPool) | **GET** /v2/databases/{database_cluster_uuid}/pools/{pool_name} | Retrieve Existing Connection Pool (PostgreSQL) |
| [**getEvictionPolicy**](DatabasesApi.md#getEvictionPolicy) | **GET** /v2/databases/{database_cluster_uuid}/eviction_policy | Retrieve the Eviction Policy for a Redis Cluster |
| [**getExistingReadOnlyReplica**](DatabasesApi.md#getExistingReadOnlyReplica) | **GET** /v2/databases/{database_cluster_uuid}/replicas/{replica_name} | Retrieve an Existing Read-only Replica |
| [**getMigrationStatus**](DatabasesApi.md#getMigrationStatus) | **GET** /v2/databases/{database_cluster_uuid}/online-migration | Retrieve the Status of an Online Migration |
| [**getPublicCertificate**](DatabasesApi.md#getPublicCertificate) | **GET** /v2/databases/{database_cluster_uuid}/ca | Retrieve the Public Certificate |
| [**getSqlMode**](DatabasesApi.md#getSqlMode) | **GET** /v2/databases/{database_cluster_uuid}/sql_mode | Retrieve the SQL Modes for a MySQL Cluster |
| [**getTopicKafkaCluster**](DatabasesApi.md#getTopicKafkaCluster) | **GET** /v2/databases/{database_cluster_uuid}/topics/{topic_name} | Get Topic for a Kafka Cluster |
| [**getUser**](DatabasesApi.md#getUser) | **GET** /v2/databases/{database_cluster_uuid}/users/{username} | Retrieve an Existing Database User |
| [**list**](DatabasesApi.md#list) | **GET** /v2/databases/{database_cluster_uuid}/dbs | List All Databases |
| [**listBackups**](DatabasesApi.md#listBackups) | **GET** /v2/databases/{database_cluster_uuid}/backups | List Backups for a Database Cluster |
| [**listClusters**](DatabasesApi.md#listClusters) | **GET** /v2/databases | List All Database Clusters |
| [**listConnectionPools**](DatabasesApi.md#listConnectionPools) | **GET** /v2/databases/{database_cluster_uuid}/pools | List Connection Pools (PostgreSQL) |
| [**listEventsLogs**](DatabasesApi.md#listEventsLogs) | **GET** /v2/databases/{database_cluster_uuid}/events | List all Events Logs |
| [**listFirewallRules**](DatabasesApi.md#listFirewallRules) | **GET** /v2/databases/{database_cluster_uuid}/firewall | List Firewall Rules (Trusted Sources) for a Database Cluster |
| [**listOptions**](DatabasesApi.md#listOptions) | **GET** /v2/databases/options | List Database Options |
| [**listReadOnlyReplicas**](DatabasesApi.md#listReadOnlyReplicas) | **GET** /v2/databases/{database_cluster_uuid}/replicas | List All Read-only Replicas |
| [**listTopicsKafkaCluster**](DatabasesApi.md#listTopicsKafkaCluster) | **GET** /v2/databases/{database_cluster_uuid}/topics | List Topics for a Kafka Cluster |
| [**listUsers**](DatabasesApi.md#listUsers) | **GET** /v2/databases/{database_cluster_uuid}/users | List all Database Users |
| [**migrateClusterToNewRegion**](DatabasesApi.md#migrateClusterToNewRegion) | **PUT** /v2/databases/{database_cluster_uuid}/migrate | Migrate a Database Cluster to a New Region |
| [**promoteReadonlyReplicaToPrimary**](DatabasesApi.md#promoteReadonlyReplicaToPrimary) | **PUT** /v2/databases/{database_cluster_uuid}/replicas/{replica_name}/promote | Promote a Read-only Replica to become a Primary Cluster |
| [**removeUser**](DatabasesApi.md#removeUser) | **DELETE** /v2/databases/{database_cluster_uuid}/users/{username} | Remove a Database User |
| [**resetUserAuth**](DatabasesApi.md#resetUserAuth) | **POST** /v2/databases/{database_cluster_uuid}/users/{username}/reset_auth | Reset a Database User&#39;s Password or Authentication Method |
| [**resizeCluster**](DatabasesApi.md#resizeCluster) | **PUT** /v2/databases/{database_cluster_uuid}/resize | Resize a Database Cluster |
| [**startOnlineMigration**](DatabasesApi.md#startOnlineMigration) | **PUT** /v2/databases/{database_cluster_uuid}/online-migration | Start an Online Migration |
| [**stopOnlineMigration**](DatabasesApi.md#stopOnlineMigration) | **DELETE** /v2/databases/{database_cluster_uuid}/online-migration/{migration_id} | Stop an Online Migration |
| [**updateConfigCluster**](DatabasesApi.md#updateConfigCluster) | **PATCH** /v2/databases/{database_cluster_uuid}/config | Update the Database Configuration for an Existing Database |
| [**updateConnectionPoolPostgresql**](DatabasesApi.md#updateConnectionPoolPostgresql) | **PUT** /v2/databases/{database_cluster_uuid}/pools/{pool_name} | Update Connection Pools (PostgreSQL) |
| [**updateFirewallRules**](DatabasesApi.md#updateFirewallRules) | **PUT** /v2/databases/{database_cluster_uuid}/firewall | Update Firewall Rules (Trusted Sources) for a Database |
| [**updateMetricsCredentials**](DatabasesApi.md#updateMetricsCredentials) | **PUT** /v2/databases/metrics/credentials | Update Database Clusters&#39; Metrics Endpoint Credentials |
| [**updateSettings**](DatabasesApi.md#updateSettings) | **PUT** /v2/databases/{database_cluster_uuid}/users/{username} | Update a Database User |
| [**updateSqlMode**](DatabasesApi.md#updateSqlMode) | **PUT** /v2/databases/{database_cluster_uuid}/sql_mode | Update SQL Mode for a Cluster |
| [**updateTopicKafkaCluster**](DatabasesApi.md#updateTopicKafkaCluster) | **PUT** /v2/databases/{database_cluster_uuid}/topics/{topic_name} | Update Topic for a Kafka Cluster |
| [**upgradeMajorVersion**](DatabasesApi.md#upgradeMajorVersion) | **PUT** /v2/databases/{database_cluster_uuid}/upgrade | Upgrade Major Version for a Database |


<a name="add"></a>
# **add**
> DatabasesAddResponse add(databaseClusterUuid, database).execute();

Add a New Database

To add a new database to an existing cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/dbs&#x60;.  Note: Database management is not supported for Redis clusters.  The response will be a JSON object with a key called &#x60;db&#x60;. The value of this will be an object that contains the standard attributes associated with a database. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String name = "name_example"; // The name of the database.
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesAddResponse result = client
              .databases
              .add(name, databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getDb());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#add");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddResponse> response = client
              .databases
              .add(name, databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#add");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **database** | [**Database**](Database.md)|  | |

### Return type

[**DatabasesAddResponse**](DatabasesAddResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | A JSON object with a key of &#x60;db&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="addNewConnectionPool"></a>
# **addNewConnectionPool**
> DatabasesAddNewConnectionPoolResponse addNewConnectionPool(databaseClusterUuid, connectionPool).execute();

Add a New Connection Pool (PostgreSQL)

For PostgreSQL database clusters, connection pools can be used to allow a database to share its idle connections. The popular PostgreSQL connection pooling utility PgBouncer is used to provide this service. [See here for more information](https://www.digitalocean.com/docs/databases/postgresql/how-to/manage-connection-pools/) about how and why to use PgBouncer connection pooling including details about the available transaction modes.  To add a new connection pool to a PostgreSQL database cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/pools&#x60; specifying a name for the pool, the user to connect with, the database to connect to, as well as its desired size and transaction mode. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String name = "name_example"; // A unique name for the connection pool. Must be between 3 and 60 characters.
    String mode = "mode_example"; // The PGBouncer transaction mode for the connection pool. The allowed values are session, transaction, and statement.
    Integer size = 56; // The desired size of the PGBouncer connection pool. The maximum allowed size is determined by the size of the cluster's primary node. 25 backend server connections are allowed for every 1GB of RAM. Three are reserved for maintenance. For example, a primary node with 1 GB of RAM allows for a maximum of 22 backend server connections while one with 4 GB would allow for 97. Note that these are shared across all connection pools in a cluster.
    String db = "db_example"; // The database for use with the connection pool.
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String user = "user_example"; // The name of the user for use with the connection pool. When excluded, all sessions connect to the database as the inbound user.
    DatabaseClusterConnection connection = new DatabaseClusterConnection();
    DatabaseClusterConnection privateConnection = new DatabaseClusterConnection();
    DatabaseClusterConnection standbyConnection = new DatabaseClusterConnection();
    DatabaseClusterConnection standbyPrivateConnection = new DatabaseClusterConnection();
    try {
      DatabasesAddNewConnectionPoolResponse result = client
              .databases
              .addNewConnectionPool(name, mode, size, db, databaseClusterUuid)
              .user(user)
              .connection(connection)
              .privateConnection(privateConnection)
              .standbyConnection(standbyConnection)
              .standbyPrivateConnection(standbyPrivateConnection)
              .execute();
      System.out.println(result);
      System.out.println(result.getPool());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#addNewConnectionPool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddNewConnectionPoolResponse> response = client
              .databases
              .addNewConnectionPool(name, mode, size, db, databaseClusterUuid)
              .user(user)
              .connection(connection)
              .privateConnection(privateConnection)
              .standbyConnection(standbyConnection)
              .standbyPrivateConnection(standbyPrivateConnection)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#addNewConnectionPool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **connectionPool** | [**ConnectionPool**](ConnectionPool.md)|  | |

### Return type

[**DatabasesAddNewConnectionPoolResponse**](DatabasesAddNewConnectionPoolResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | A JSON object with a key of &#x60;pool&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="addUser"></a>
# **addUser**
> DatabasesAddUserResponse addUser(databaseClusterUuid, databasesAddUserRequest).execute();

Add a Database User

To add a new database user, send a POST request to &#x60;/v2/databases/$DATABASE_ID/users&#x60; with the desired username.  Note: User management is not supported for Redis clusters.  When adding a user to a MySQL cluster, additional options can be configured in the &#x60;mysql_settings&#x60; object.  When adding a user to a Kafka cluster, additional options can be configured in the &#x60;settings&#x60; object.  The response will be a JSON object with a key called &#x60;user&#x60;. The value of this will be an object that contains the standard attributes associated with a database user including its randomly generated password. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String name = "name_example"; // The name of a database user.
    String role = "primary"; // A string representing the database user's role. The value will be either \\\"primary\\\" or \\\"normal\\\". 
    String password = "password_example"; // A randomly generated password for the database user.
    String accessCert = "accessCert_example"; // Access certificate for TLS client authentication. (Kafka only)
    String accessKey = "accessKey_example"; // Access key for TLS client authentication. (Kafka only)
    MysqlSettings mysqlSettings = new MysqlSettings();
    UserSettings settings = new UserSettings();
    Boolean readonly = true; // For MongoDB clusters, set to `true` to create a read-only user. This option is not currently supported for other database engines.              
    try {
      DatabasesAddUserResponse result = client
              .databases
              .addUser(databaseClusterUuid)
              .name(name)
              .role(role)
              .password(password)
              .accessCert(accessCert)
              .accessKey(accessKey)
              .mysqlSettings(mysqlSettings)
              .settings(settings)
              .readonly(readonly)
              .execute();
      System.out.println(result);
      System.out.println(result.getUser());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#addUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddUserResponse> response = client
              .databases
              .addUser(databaseClusterUuid)
              .name(name)
              .role(role)
              .password(password)
              .accessCert(accessCert)
              .accessKey(accessKey)
              .mysqlSettings(mysqlSettings)
              .settings(settings)
              .readonly(readonly)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#addUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databasesAddUserRequest** | [**DatabasesAddUserRequest**](DatabasesAddUserRequest.md)|  | |

### Return type

[**DatabasesAddUserResponse**](DatabasesAddUserResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | A JSON object with a key of &#x60;user&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="configureEvictionPolicy"></a>
# **configureEvictionPolicy**
> configureEvictionPolicy(databaseClusterUuid, databasesConfigureEvictionPolicyRequest).execute();

Configure the Eviction Policy for a Redis Cluster

To configure an eviction policy for an existing Redis cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/eviction_policy&#x60; specifying the desired policy.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    EvictionPolicyModel evictionPolicy = EvictionPolicyModel.fromValue("noeviction");
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      client
              .databases
              .configureEvictionPolicy(evictionPolicy, databaseClusterUuid)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#configureEvictionPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .configureEvictionPolicy(evictionPolicy, databaseClusterUuid)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#configureEvictionPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databasesConfigureEvictionPolicyRequest** | [**DatabasesConfigureEvictionPolicyRequest**](DatabasesConfigureEvictionPolicyRequest.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="configureMaintenanceWindow"></a>
# **configureMaintenanceWindow**
> configureMaintenanceWindow(databaseClusterUuid, databaseMaintenanceWindow).execute();

Configure a Database Cluster&#39;s Maintenance Window

To configure the window when automatic maintenance should be performed for a database cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/maintenance&#x60;. A successful request will receive a 204 No Content status code with no body in response.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String day = "day_example"; // The day of the week on which to apply maintenance updates.
    String hour = "hour_example"; // The hour in UTC at which maintenance updates will be applied in 24 hour format.
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    List<String> description = Arrays.asList(); // A list of strings, each containing information about a pending maintenance update.
    Boolean pending = true; // A boolean value indicating whether any maintenance is scheduled to be performed in the next window.
    try {
      client
              .databases
              .configureMaintenanceWindow(day, hour, databaseClusterUuid)
              .description(description)
              .pending(pending)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#configureMaintenanceWindow");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .configureMaintenanceWindow(day, hour, databaseClusterUuid)
              .description(description)
              .pending(pending)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#configureMaintenanceWindow");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databaseMaintenanceWindow** | [**DatabaseMaintenanceWindow**](DatabaseMaintenanceWindow.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="createCluster"></a>
# **createCluster**
> DatabasesCreateClusterResponse createCluster(databasesCreateClusterRequest).execute();

Create a New Database Cluster

To create a database cluster, send a POST request to &#x60;/v2/databases&#x60;. The response will be a JSON object with a key called &#x60;database&#x60;. The value of this will be an object that contains the standard attributes associated with a database cluster. The initial value of the database cluster&#39;s &#x60;status&#x60; attribute will be &#x60;creating&#x60;. When the cluster is ready to receive traffic, this will transition to &#x60;online&#x60;.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects will contain the information needed to connect to the cluster&#39;s standby node(s).  DigitalOcean managed PostgreSQL and MySQL database clusters take automated daily backups. To create a new database cluster based on a backup of an existing cluster, send a POST request to &#x60;/v2/databases&#x60;. In addition to the standard database cluster attributes, the JSON body must include a key named &#x60;backup_restore&#x60; with the name of the original database cluster and the timestamp of the backup to be restored. Creating a database from a backup is the same as forking a database in the control panel. Note: Backups are not supported for Redis clusters.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    List<String> tags = Arrays.asList(); // An array of tags that have been applied to the database cluster.
    String version = "version_example"; // A string representing the version of the database engine in use for the cluster.
    UUID id = UUID.randomUUID(); // A unique ID that can be used to identify and reference a database cluster.
    String name = "name_example"; // A unique, human-readable name referring to a database cluster.
    String engine = "pg"; // A slug representing the database engine used for the cluster. The possible values are: \\\"pg\\\" for PostgreSQL, \\\"mysql\\\" for MySQL, \\\"redis\\\" for Redis, \\\"mongodb\\\" for MongoDB, and \\\"kafka\\\" for Kafka.
    String semanticVersion = "semanticVersion_example"; // A string representing the semantic version of the database engine in use for the cluster.
    Integer numNodes = 56; // The number of nodes in the database cluster.
    String size = "size_example"; // The slug identifier representing the size of the nodes in the database cluster.
    String region = "region_example"; // The slug identifier for the region where the database cluster is located.
    String status = "creating"; // A string representing the current status of the database cluster.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the database cluster was created.
    String privateNetworkUuid = "privateNetworkUuid_example"; // A string specifying the UUID of the VPC to which the database cluster will be assigned. If excluded, the cluster when creating a new database cluster, it will be assigned to your account's default VPC for the region.
    List<String> dbNames = Arrays.asList(); // An array of strings containing the names of databases created in the database cluster.
    DatabaseClusterConnection connection = new DatabaseClusterConnection();
    DatabaseClusterConnection privateConnection = new DatabaseClusterConnection();
    DatabaseClusterConnection standbyConnection = new DatabaseClusterConnection();
    DatabaseClusterConnection standbyPrivateConnection = new DatabaseClusterConnection();
    List<DatabaseUser> users = Arrays.asList();
    DatabaseClusterMaintenanceWindow maintenanceWindow = new DatabaseClusterMaintenanceWindow();
    UUID projectId = UUID.randomUUID(); // The ID of the project that the database cluster is assigned to. If excluded when creating a new database cluster, it will be assigned to your default project.
    List<FirewallRule> rules = Arrays.asList();
    String versionEndOfLife = "versionEndOfLife_example"; // A timestamp referring to the date when the particular version will no longer be supported. If null, the version does not have an end of life timeline.
    String versionEndOfAvailability = "versionEndOfAvailability_example"; // A timestamp referring to the date when the particular version will no longer be available for creating new clusters. If null, the version does not have an end of availability timeline.
    Integer storageSizeMib = 56; // Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the 'size' and any previously added additional storage.
    List<DatabaseServiceEndpoint> metricsEndpoints = Arrays.asList(); // Public hostname and port of the cluster's metrics endpoint(s). Includes one record for the cluster's primary node and a second entry for the cluster's standby node(s).
    DatabaseBackup backupRestore = new DatabaseBackup();
    try {
      DatabasesCreateClusterResponse result = client
              .databases
              .createCluster()
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
      System.out.println(result);
      System.out.println(result.getDatabase());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#createCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesCreateClusterResponse> response = client
              .databases
              .createCluster()
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
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#createCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databasesCreateClusterRequest** | [**DatabasesCreateClusterRequest**](DatabasesCreateClusterRequest.md)|  | |

### Return type

[**DatabasesCreateClusterResponse**](DatabasesCreateClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | A JSON object with a key of &#x60;database&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="createReadOnlyReplica"></a>
# **createReadOnlyReplica**
> DatabasesCreateReadOnlyReplicaResponse createReadOnlyReplica(databaseClusterUuid).databaseReplica(databaseReplica).execute();

Create a Read-only Replica

To create a read-only replica for a PostgreSQL or MySQL database cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/replicas&#x60; specifying the name it should be given, the size of the node to be used, and the region where it will be located.  **Note**: Read-only replicas are not supported for Redis clusters.  The response will be a JSON object with a key called &#x60;replica&#x60;. The value of this will be an object that contains the standard attributes associated with a database replica. The initial value of the read-only replica&#39;s &#x60;status&#x60; attribute will be &#x60;forking&#x60;. When the replica is ready to receive traffic, this will transition to &#x60;active&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String name = "name_example"; // The name to give the read-only replicating
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    List<String> tags = Arrays.asList(); // A flat array of tag names as strings to apply to the read-only replica after it is created. Tag names can either be existing or new tags.
    UUID id = UUID.randomUUID(); // A unique ID that can be used to identify and reference a database replica.
    String region = "region_example"; // A slug identifier for the region where the read-only replica will be located. If excluded, the replica will be placed in the same region as the cluster.
    String size = "size_example"; // A slug identifier representing the size of the node for the read-only replica. The size of the replica must be at least as large as the node size for the database cluster from which it is replicating.
    String status = "creating"; // A string representing the current status of the database cluster.
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the database cluster was created.
    String privateNetworkUuid = "privateNetworkUuid_example"; // A string specifying the UUID of the VPC to which the read-only replica will be assigned. If excluded, the replica will be assigned to your account's default VPC for the region.
    DatabaseReplicaConnection connection = new DatabaseReplicaConnection();
    DatabaseReplicaConnection privateConnection = new DatabaseReplicaConnection();
    Integer storageSizeMib = 56; // Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the 'size' and any previously added additional storage.
    try {
      DatabasesCreateReadOnlyReplicaResponse result = client
              .databases
              .createReadOnlyReplica(name, databaseClusterUuid)
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
      System.out.println(result);
      System.out.println(result.getReplica());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#createReadOnlyReplica");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesCreateReadOnlyReplicaResponse> response = client
              .databases
              .createReadOnlyReplica(name, databaseClusterUuid)
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
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#createReadOnlyReplica");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databaseReplica** | [**DatabaseReplica**](DatabaseReplica.md)|  | [optional] |

### Return type

[**DatabasesCreateReadOnlyReplicaResponse**](DatabasesCreateReadOnlyReplicaResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | A JSON object with a key of &#x60;replica&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="createTopicKafkaCluster"></a>
# **createTopicKafkaCluster**
> DatabasesCreateTopicKafkaClusterResponse createTopicKafkaCluster(databaseClusterUuid).kafkaTopicCreate(kafkaTopicCreate).execute();

Create Topic for a Kafka Cluster

To create a topic attached to a Kafka cluster, send a POST request to &#x60;/v2/databases/$DATABASE_ID/topics&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String name = "name_example"; // The name of the Kafka topic.
    Integer replicationFactor = 56; // The number of nodes to replicate data across the cluster.
    Integer partitionCount = 56; // The number of partitions available for the topic. On update, this value can only be increased.
    KafkaTopicConfig config = new KafkaTopicConfig();
    try {
      DatabasesCreateTopicKafkaClusterResponse result = client
              .databases
              .createTopicKafkaCluster(databaseClusterUuid)
              .name(name)
              .replicationFactor(replicationFactor)
              .partitionCount(partitionCount)
              .config(config)
              .execute();
      System.out.println(result);
      System.out.println(result.getTopic());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#createTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesCreateTopicKafkaClusterResponse> response = client
              .databases
              .createTopicKafkaCluster(databaseClusterUuid)
              .name(name)
              .replicationFactor(replicationFactor)
              .partitionCount(partitionCount)
              .config(config)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#createTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **kafkaTopicCreate** | [**KafkaTopicCreate**](KafkaTopicCreate.md)|  | [optional] |

### Return type

[**DatabasesCreateTopicKafkaClusterResponse**](DatabasesCreateTopicKafkaClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | A JSON object with a key of &#x60;topic&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="delete"></a>
# **delete**
> delete(databaseClusterUuid, databaseName).execute();

Delete a Database

To delete a specific database, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/dbs/$DB_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.  Note: Database management is not supported for Redis clusters. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String databaseName = "alpha"; // The name of the database.
    try {
      client
              .databases
              .delete(databaseClusterUuid, databaseName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .delete(databaseClusterUuid, databaseName)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#delete");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databaseName** | **String**| The name of the database. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteConnectionPool"></a>
# **deleteConnectionPool**
> deleteConnectionPool(databaseClusterUuid, poolName).execute();

Delete a Connection Pool (PostgreSQL)

To delete a specific connection pool for a PostgreSQL database cluster, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String poolName = "backend-pool"; // The name used to identify the connection pool.
    try {
      client
              .databases
              .deleteConnectionPool(databaseClusterUuid, poolName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#deleteConnectionPool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .deleteConnectionPool(databaseClusterUuid, poolName)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#deleteConnectionPool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **poolName** | **String**| The name used to identify the connection pool. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteTopicKafkaCluster"></a>
# **deleteTopicKafkaCluster**
> deleteTopicKafkaCluster(databaseClusterUuid, topicName).execute();

Delete Topic for a Kafka Cluster

To delete a single topic within a Kafka cluster, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String topicName = "customer-events"; // The name used to identify the Kafka topic.
    try {
      client
              .databases
              .deleteTopicKafkaCluster(databaseClusterUuid, topicName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#deleteTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .deleteTopicKafkaCluster(databaseClusterUuid, topicName)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#deleteTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **topicName** | **String**| The name used to identify the Kafka topic. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="destroyCluster"></a>
# **destroyCluster**
> destroyCluster(databaseClusterUuid).execute();

Destroy a Database Cluster

To destroy a specific database, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID&#x60;. A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      client
              .databases
              .destroyCluster(databaseClusterUuid)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#destroyCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .destroyCluster(databaseClusterUuid)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#destroyCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="destroyReadonlyReplica"></a>
# **destroyReadonlyReplica**
> destroyReadonlyReplica(databaseClusterUuid, replicaName).execute();

Destroy a Read-only Replica

To destroy a specific read-only replica, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String replicaName = "read-nyc3-01"; // The name of the database replica.
    try {
      client
              .databases
              .destroyReadonlyReplica(databaseClusterUuid, replicaName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#destroyReadonlyReplica");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .destroyReadonlyReplica(databaseClusterUuid, replicaName)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#destroyReadonlyReplica");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **replicaName** | **String**| The name of the database replica. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="get"></a>
# **get**
> DatabasesAddResponse get(databaseClusterUuid, databaseName).execute();

Retrieve an Existing Database

To show information about an existing database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/dbs/$DB_NAME&#x60;.  Note: Database management is not supported for Redis clusters.  The response will be a JSON object with a &#x60;db&#x60; key. This will be set to an object containing the standard database attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String databaseName = "alpha"; // The name of the database.
    try {
      DatabasesAddResponse result = client
              .databases
              .get(databaseClusterUuid, databaseName)
              .execute();
      System.out.println(result);
      System.out.println(result.getDb());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddResponse> response = client
              .databases
              .get(databaseClusterUuid, databaseName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#get");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databaseName** | **String**| The name of the database. | |

### Return type

[**DatabasesAddResponse**](DatabasesAddResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;db&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getClusterConfig"></a>
# **getClusterConfig**
> DatabasesGetClusterConfigResponse getClusterConfig(databaseClusterUuid).execute();

Retrieve an Existing Database Cluster Configuration

Shows configuration parameters for an existing database cluster by sending a GET request to &#x60;/v2/databases/$DATABASE_ID/config&#x60;. The response is a JSON object with a &#x60;config&#x60; key, which is set to an object containing any database configuration parameters. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesGetClusterConfigResponse result = client
              .databases
              .getClusterConfig(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getConfig());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getClusterConfig");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesGetClusterConfigResponse> response = client
              .databases
              .getClusterConfig(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getClusterConfig");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesGetClusterConfigResponse**](DatabasesGetClusterConfigResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;config&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getClusterInfo"></a>
# **getClusterInfo**
> DatabasesCreateClusterResponse getClusterInfo(databaseClusterUuid).execute();

Retrieve an Existing Database Cluster

To show information about an existing database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID&#x60;.  The response will be a JSON object with a database key. This will be set to an object containing the standard database cluster attributes.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects contain the information needed to connect to the cluster&#39;s standby node(s).  The embedded maintenance_window object will contain information about any scheduled maintenance for the database cluster.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesCreateClusterResponse result = client
              .databases
              .getClusterInfo(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getDatabase());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getClusterInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesCreateClusterResponse> response = client
              .databases
              .getClusterInfo(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getClusterInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesCreateClusterResponse**](DatabasesCreateClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;database&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getClustersMetricsCredentials"></a>
# **getClustersMetricsCredentials**
> DatabasesGetClustersMetricsCredentialsResponse getClustersMetricsCredentials().execute();

Retrieve Database Clusters&#39; Metrics Endpoint Credentials

To show the credentials for all database clusters&#39; metrics endpoints, send a GET request to &#x60;/v2/databases/metrics/credentials&#x60;. The result will be a JSON object with a &#x60;credentials&#x60; key.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    try {
      DatabasesGetClustersMetricsCredentialsResponse result = client
              .databases
              .getClustersMetricsCredentials()
              .execute();
      System.out.println(result);
      System.out.println(result.getCredentials());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getClustersMetricsCredentials");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesGetClustersMetricsCredentialsResponse> response = client
              .databases
              .getClustersMetricsCredentials()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getClustersMetricsCredentials");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters
This endpoint does not need any parameter.

### Return type

[**DatabasesGetClustersMetricsCredentialsResponse**](DatabasesGetClustersMetricsCredentialsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;credentials&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getConnectionPool"></a>
# **getConnectionPool**
> DatabasesAddNewConnectionPoolResponse getConnectionPool(databaseClusterUuid, poolName).execute();

Retrieve Existing Connection Pool (PostgreSQL)

To show information about an existing connection pool for a PostgreSQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;. The response will be a JSON object with a &#x60;pool&#x60; key.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String poolName = "backend-pool"; // The name used to identify the connection pool.
    try {
      DatabasesAddNewConnectionPoolResponse result = client
              .databases
              .getConnectionPool(databaseClusterUuid, poolName)
              .execute();
      System.out.println(result);
      System.out.println(result.getPool());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getConnectionPool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddNewConnectionPoolResponse> response = client
              .databases
              .getConnectionPool(databaseClusterUuid, poolName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getConnectionPool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **poolName** | **String**| The name used to identify the connection pool. | |

### Return type

[**DatabasesAddNewConnectionPoolResponse**](DatabasesAddNewConnectionPoolResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;pool&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getEvictionPolicy"></a>
# **getEvictionPolicy**
> DatabasesGetEvictionPolicyResponse getEvictionPolicy(databaseClusterUuid).execute();

Retrieve the Eviction Policy for a Redis Cluster

To retrieve the configured eviction policy for an existing Redis cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/eviction_policy&#x60;. The response will be a JSON object with an &#x60;eviction_policy&#x60; key. This will be set to a string representing the eviction policy.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesGetEvictionPolicyResponse result = client
              .databases
              .getEvictionPolicy(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getEvictionPolicy());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getEvictionPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesGetEvictionPolicyResponse> response = client
              .databases
              .getEvictionPolicy(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getEvictionPolicy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesGetEvictionPolicyResponse**](DatabasesGetEvictionPolicyResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON string with a key of &#x60;eviction_policy&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getExistingReadOnlyReplica"></a>
# **getExistingReadOnlyReplica**
> DatabasesCreateReadOnlyReplicaResponse getExistingReadOnlyReplica(databaseClusterUuid, replicaName).execute();

Retrieve an Existing Read-only Replica

To show information about an existing database replica, send a GET request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  The response will be a JSON object with a &#x60;replica key&#x60;. This will be set to an object containing the standard database replica attributes.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String replicaName = "read-nyc3-01"; // The name of the database replica.
    try {
      DatabasesCreateReadOnlyReplicaResponse result = client
              .databases
              .getExistingReadOnlyReplica(databaseClusterUuid, replicaName)
              .execute();
      System.out.println(result);
      System.out.println(result.getReplica());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getExistingReadOnlyReplica");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesCreateReadOnlyReplicaResponse> response = client
              .databases
              .getExistingReadOnlyReplica(databaseClusterUuid, replicaName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getExistingReadOnlyReplica");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **replicaName** | **String**| The name of the database replica. | |

### Return type

[**DatabasesCreateReadOnlyReplicaResponse**](DatabasesCreateReadOnlyReplicaResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;replica&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getMigrationStatus"></a>
# **getMigrationStatus**
> OnlineMigration getMigrationStatus(databaseClusterUuid).execute();

Retrieve the Status of an Online Migration

To retrieve the status of the most recent online migration, send a GET request to &#x60;/v2/databases/$DATABASE_ID/online-migration&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      OnlineMigration result = client
              .databases
              .getMigrationStatus(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getStatus());
      System.out.println(result.getCreatedAt());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getMigrationStatus");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<OnlineMigration> response = client
              .databases
              .getMigrationStatus(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getMigrationStatus");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**OnlineMigration**](OnlineMigration.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getPublicCertificate"></a>
# **getPublicCertificate**
> DatabasesGetPublicCertificateResponse getPublicCertificate(databaseClusterUuid).execute();

Retrieve the Public Certificate

To retrieve the public certificate used to secure the connection to the database cluster send a GET request to &#x60;/v2/databases/$DATABASE_ID/ca&#x60;.  The response will be a JSON object with a &#x60;ca&#x60; key. This will be set to an object containing the base64 encoding of the public key certificate. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesGetPublicCertificateResponse result = client
              .databases
              .getPublicCertificate(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getCa());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getPublicCertificate");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesGetPublicCertificateResponse> response = client
              .databases
              .getPublicCertificate(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getPublicCertificate");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesGetPublicCertificateResponse**](DatabasesGetPublicCertificateResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;ca&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getSqlMode"></a>
# **getSqlMode**
> SqlMode getSqlMode(databaseClusterUuid).execute();

Retrieve the SQL Modes for a MySQL Cluster

To retrieve the configured SQL modes for an existing MySQL cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/sql_mode&#x60;. The response will be a JSON object with a &#x60;sql_mode&#x60; key. This will be set to a string representing the configured SQL modes.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      SqlMode result = client
              .databases
              .getSqlMode(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getSqlMode());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getSqlMode");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<SqlMode> response = client
              .databases
              .getSqlMode(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getSqlMode");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**SqlMode**](SqlMode.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON string with a key of &#x60;sql_mode&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getTopicKafkaCluster"></a>
# **getTopicKafkaCluster**
> DatabasesCreateTopicKafkaClusterResponse getTopicKafkaCluster(databaseClusterUuid, topicName).execute();

Get Topic for a Kafka Cluster

To retrieve a given topic by name from the set of a Kafka cluster&#39;s topics, send a GET request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String topicName = "customer-events"; // The name used to identify the Kafka topic.
    try {
      DatabasesCreateTopicKafkaClusterResponse result = client
              .databases
              .getTopicKafkaCluster(databaseClusterUuid, topicName)
              .execute();
      System.out.println(result);
      System.out.println(result.getTopic());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesCreateTopicKafkaClusterResponse> response = client
              .databases
              .getTopicKafkaCluster(databaseClusterUuid, topicName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **topicName** | **String**| The name used to identify the Kafka topic. | |

### Return type

[**DatabasesCreateTopicKafkaClusterResponse**](DatabasesCreateTopicKafkaClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;topic&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getUser"></a>
# **getUser**
> DatabasesAddUserResponse getUser(databaseClusterUuid, username).execute();

Retrieve an Existing Database User

To show information about an existing database user, send a GET request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60;.  Note: User management is not supported for Redis clusters.  The response will be a JSON object with a &#x60;user&#x60; key. This will be set to an object containing the standard database user attributes.  For MySQL clusters, additional options will be contained in the &#x60;mysql_settings&#x60; object.  For Kafka clusters, additional options will be contained in the &#x60;settings&#x60; object. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String username = "app-01"; // The name of the database user.
    try {
      DatabasesAddUserResponse result = client
              .databases
              .getUser(databaseClusterUuid, username)
              .execute();
      System.out.println(result);
      System.out.println(result.getUser());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddUserResponse> response = client
              .databases
              .getUser(databaseClusterUuid, username)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#getUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **username** | **String**| The name of the database user. | |

### Return type

[**DatabasesAddUserResponse**](DatabasesAddUserResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;user&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="list"></a>
# **list**
> DatabasesListResponse list(databaseClusterUuid).execute();

List All Databases

To list all of the databases in a clusters, send a GET request to &#x60;/v2/databases/$DATABASE_ID/dbs&#x60;.  The result will be a JSON object with a &#x60;dbs&#x60; key. This will be set to an array of database objects, each of which will contain the standard database attributes.  Note: Database management is not supported for Redis clusters. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesListResponse result = client
              .databases
              .list(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getDbs());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListResponse> response = client
              .databases
              .list(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#list");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesListResponse**](DatabasesListResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;databases&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listBackups"></a>
# **listBackups**
> DatabasesListBackupsResponse listBackups(databaseClusterUuid).execute();

List Backups for a Database Cluster

To list all of the available backups of a PostgreSQL or MySQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/backups&#x60;. **Note**: Backups are not supported for Redis clusters. The result will be a JSON object with a &#x60;backups key&#x60;. This will be set to an array of backup objects, each of which will contain the size of the backup and the timestamp at which it was created.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesListBackupsResponse result = client
              .databases
              .listBackups(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getBackups());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listBackups");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListBackupsResponse> response = client
              .databases
              .listBackups(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listBackups");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesListBackupsResponse**](DatabasesListBackupsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;database_backups&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listClusters"></a>
# **listClusters**
> DatabasesListClustersResponse listClusters().tagName(tagName).execute();

List All Database Clusters

To list all of the database clusters available on your account, send a GET request to &#x60;/v2/databases&#x60;. To limit the results to database clusters with a specific tag, include the &#x60;tag_name&#x60; query parameter set to the name of the tag. For example, &#x60;/v2/databases?tag_name&#x3D;$TAG_NAME&#x60;.  The result will be a JSON object with a &#x60;databases&#x60; key. This will be set to an array of database objects, each of which will contain the standard database attributes.  The embedded &#x60;connection&#x60; and &#x60;private_connection&#x60; objects will contain the information needed to access the database cluster. For multi-node clusters, the &#x60;standby_connection&#x60; and &#x60;standby_private_connection&#x60; objects will contain the information needed to connect to the cluster&#39;s standby node(s).  The embedded &#x60;maintenance_window&#x60; object will contain information about any scheduled maintenance for the database cluster.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String tagName = "production"; // Limits the results to database clusters with a specific tag.
    try {
      DatabasesListClustersResponse result = client
              .databases
              .listClusters()
              .tagName(tagName)
              .execute();
      System.out.println(result);
      System.out.println(result.getDatabases());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listClusters");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListClustersResponse> response = client
              .databases
              .listClusters()
              .tagName(tagName)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listClusters");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **tagName** | **String**| Limits the results to database clusters with a specific tag. | [optional] |

### Return type

[**DatabasesListClustersResponse**](DatabasesListClustersResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;databases&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listConnectionPools"></a>
# **listConnectionPools**
> ConnectionPools listConnectionPools(databaseClusterUuid).execute();

List Connection Pools (PostgreSQL)

To list all of the connection pools available to a PostgreSQL database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/pools&#x60;. The result will be a JSON object with a &#x60;pools&#x60; key. This will be set to an array of connection pool objects.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      ConnectionPools result = client
              .databases
              .listConnectionPools(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getPools());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listConnectionPools");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ConnectionPools> response = client
              .databases
              .listConnectionPools(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listConnectionPools");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**ConnectionPools**](ConnectionPools.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;pools&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listEventsLogs"></a>
# **listEventsLogs**
> DatabasesListEventsLogsResponse listEventsLogs(databaseClusterUuid).execute();

List all Events Logs

To list all of the cluster events, send a GET request to &#x60;/v2/databases/$DATABASE_ID/events&#x60;.  The result will be a JSON object with a &#x60;events&#x60; key. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesListEventsLogsResponse result = client
              .databases
              .listEventsLogs(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getEvents());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listEventsLogs");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListEventsLogsResponse> response = client
              .databases
              .listEventsLogs(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listEventsLogs");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesListEventsLogsResponse**](DatabasesListEventsLogsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;events&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listFirewallRules"></a>
# **listFirewallRules**
> DatabasesListFirewallRulesResponse listFirewallRules(databaseClusterUuid).execute();

List Firewall Rules (Trusted Sources) for a Database Cluster

To list all of a database cluster&#39;s firewall rules (known as \&quot;trusted sources\&quot; in the control panel), send a GET request to &#x60;/v2/databases/$DATABASE_ID/firewall&#x60;. The result will be a JSON object with a &#x60;rules&#x60; key.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesListFirewallRulesResponse result = client
              .databases
              .listFirewallRules(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getRules());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listFirewallRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListFirewallRulesResponse> response = client
              .databases
              .listFirewallRules(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listFirewallRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesListFirewallRulesResponse**](DatabasesListFirewallRulesResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;rules&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listOptions"></a>
# **listOptions**
> Options listOptions().execute();

List Database Options

To list all of the options available for the offered database engines, send a GET request to &#x60;/v2/databases/options&#x60;. The result will be a JSON object with an &#x60;options&#x60; key.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    try {
      Options result = client
              .databases
              .listOptions()
              .execute();
      System.out.println(result);
      System.out.println(result.getOptions());
      System.out.println(result.getVersionAvailability());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listOptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Options> response = client
              .databases
              .listOptions()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listOptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Options**](Options.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON string with a key of &#x60;options&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listReadOnlyReplicas"></a>
# **listReadOnlyReplicas**
> DatabasesListReadOnlyReplicasResponse listReadOnlyReplicas(databaseClusterUuid).execute();

List All Read-only Replicas

To list all of the read-only replicas associated with a database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/replicas&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  The result will be a JSON object with a &#x60;replicas&#x60; key. This will be set to an array of database replica objects, each of which will contain the standard database replica attributes.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesListReadOnlyReplicasResponse result = client
              .databases
              .listReadOnlyReplicas(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getReplicas());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listReadOnlyReplicas");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListReadOnlyReplicasResponse> response = client
              .databases
              .listReadOnlyReplicas(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listReadOnlyReplicas");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesListReadOnlyReplicasResponse**](DatabasesListReadOnlyReplicasResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;replicas&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listTopicsKafkaCluster"></a>
# **listTopicsKafkaCluster**
> DatabasesListTopicsKafkaClusterResponse listTopicsKafkaCluster(databaseClusterUuid).execute();

List Topics for a Kafka Cluster

To list all of a Kafka cluster&#39;s topics, send a GET request to &#x60;/v2/databases/$DATABASE_ID/topics&#x60;.  The result will be a JSON object with a &#x60;topics&#x60; key. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesListTopicsKafkaClusterResponse result = client
              .databases
              .listTopicsKafkaCluster(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getTopics());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listTopicsKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListTopicsKafkaClusterResponse> response = client
              .databases
              .listTopicsKafkaCluster(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listTopicsKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesListTopicsKafkaClusterResponse**](DatabasesListTopicsKafkaClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;topics&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listUsers"></a>
# **listUsers**
> DatabasesListUsersResponse listUsers(databaseClusterUuid).execute();

List all Database Users

To list all of the users for your database cluster, send a GET request to &#x60;/v2/databases/$DATABASE_ID/users&#x60;.  Note: User management is not supported for Redis clusters.  The result will be a JSON object with a &#x60;users&#x60; key. This will be set to an array of database user objects, each of which will contain the standard database user attributes.  For MySQL clusters, additional options will be contained in the mysql_settings object. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      DatabasesListUsersResponse result = client
              .databases
              .listUsers(databaseClusterUuid)
              .execute();
      System.out.println(result);
      System.out.println(result.getUsers());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesListUsersResponse> response = client
              .databases
              .listUsers(databaseClusterUuid)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#listUsers");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |

### Return type

[**DatabasesListUsersResponse**](DatabasesListUsersResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;users&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="migrateClusterToNewRegion"></a>
# **migrateClusterToNewRegion**
> migrateClusterToNewRegion(databaseClusterUuid, databasesMigrateClusterToNewRegionRequest).execute();

Migrate a Database Cluster to a New Region

To migrate a database cluster to a new region, send a &#x60;PUT&#x60; request to &#x60;/v2/databases/$DATABASE_ID/migrate&#x60;. The body of the request must specify a &#x60;region&#x60; attribute.  A successful request will receive a 202 Accepted status code with no body in response. Querying the database cluster will show that its &#x60;status&#x60; attribute will now be set to &#x60;migrating&#x60;. This will transition back to &#x60;online&#x60; when the migration has completed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String region = "region_example"; // A slug identifier for the region to which the database cluster will be migrated.
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      client
              .databases
              .migrateClusterToNewRegion(region, databaseClusterUuid)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#migrateClusterToNewRegion");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .migrateClusterToNewRegion(region, databaseClusterUuid)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#migrateClusterToNewRegion");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databasesMigrateClusterToNewRegionRequest** | [**DatabasesMigrateClusterToNewRegionRequest**](DatabasesMigrateClusterToNewRegionRequest.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The does not indicate the success or failure of any operation, just that the request has been accepted for processing. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="promoteReadonlyReplicaToPrimary"></a>
# **promoteReadonlyReplicaToPrimary**
> promoteReadonlyReplicaToPrimary(databaseClusterUuid, replicaName).execute();

Promote a Read-only Replica to become a Primary Cluster

To promote a specific read-only replica, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/replicas/$REPLICA_NAME/promote&#x60;.  **Note**: Read-only replicas are not supported for Redis clusters.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String replicaName = "read-nyc3-01"; // The name of the database replica.
    try {
      client
              .databases
              .promoteReadonlyReplicaToPrimary(databaseClusterUuid, replicaName)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#promoteReadonlyReplicaToPrimary");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .promoteReadonlyReplicaToPrimary(databaseClusterUuid, replicaName)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#promoteReadonlyReplicaToPrimary");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **replicaName** | **String**| The name of the database replica. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="removeUser"></a>
# **removeUser**
> removeUser(databaseClusterUuid, username).execute();

Remove a Database User

To remove a specific database user, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed.  Note: User management is not supported for Redis clusters. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String username = "app-01"; // The name of the database user.
    try {
      client
              .databases
              .removeUser(databaseClusterUuid, username)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#removeUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .removeUser(databaseClusterUuid, username)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#removeUser");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **username** | **String**| The name of the database user. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="resetUserAuth"></a>
# **resetUserAuth**
> DatabasesAddUserResponse resetUserAuth(databaseClusterUuid, username, databasesResetUserAuthRequest).execute();

Reset a Database User&#39;s Password or Authentication Method

To reset the password for a database user, send a POST request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME/reset_auth&#x60;.  For &#x60;mysql&#x60; databases, the authentication method can be specifying by including a key in the JSON body called &#x60;mysql_settings&#x60; with the &#x60;auth_plugin&#x60; value specified.  The response will be a JSON object with a &#x60;user&#x60; key. This will be set to an object containing the standard database user attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String username = "app-01"; // The name of the database user.
    MysqlSettings mysqlSettings = new MysqlSettings();
    try {
      DatabasesAddUserResponse result = client
              .databases
              .resetUserAuth(databaseClusterUuid, username)
              .mysqlSettings(mysqlSettings)
              .execute();
      System.out.println(result);
      System.out.println(result.getUser());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#resetUserAuth");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddUserResponse> response = client
              .databases
              .resetUserAuth(databaseClusterUuid, username)
              .mysqlSettings(mysqlSettings)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#resetUserAuth");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **username** | **String**| The name of the database user. | |
| **databasesResetUserAuthRequest** | [**DatabasesResetUserAuthRequest**](DatabasesResetUserAuthRequest.md)|  | |

### Return type

[**DatabasesAddUserResponse**](DatabasesAddUserResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;user&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="resizeCluster"></a>
# **resizeCluster**
> resizeCluster(databaseClusterUuid, databaseClusterResize).execute();

Resize a Database Cluster

To resize a database cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/resize&#x60;. The body of the request must specify both the size and num_nodes attributes. A successful request will receive a 202 Accepted status code with no body in response. Querying the database cluster will show that its status attribute will now be set to resizing. This will transition back to online when the resize operation has completed.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String size = "size_example"; // A slug identifier representing desired the size of the nodes in the database cluster.
    Integer numNodes = 56; // The number of nodes in the database cluster. Valid values are are 1-3. In addition to the primary node, up to two standby nodes may be added for highly available configurations.
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    Integer storageSizeMib = 56; // Additional storage added to the cluster, in MiB. If null, no additional storage is added to the cluster, beyond what is provided as a base amount from the 'size' and any previously added additional storage.
    try {
      client
              .databases
              .resizeCluster(size, numNodes, databaseClusterUuid)
              .storageSizeMib(storageSizeMib)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#resizeCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .resizeCluster(size, numNodes, databaseClusterUuid)
              .storageSizeMib(storageSizeMib)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#resizeCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databaseClusterResize** | [**DatabaseClusterResize**](DatabaseClusterResize.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="startOnlineMigration"></a>
# **startOnlineMigration**
> OnlineMigration startOnlineMigration(databaseClusterUuid, sourceDatabase).execute();

Start an Online Migration

To start an online migration, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/online-migration&#x60; endpoint. Migrating a cluster establishes a connection with an existing cluster and replicates its contents to the target cluster. Online migration is only available for MySQL, PostgreSQL, and Redis clusters.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    SourceDatabaseSource source = new SourceDatabaseSource();
    Boolean disableSsl = true; // Enables SSL encryption when connecting to the source database.
    List<String> ignoreDbs = Arrays.asList(); // List of databases that should be ignored during migration.
    try {
      OnlineMigration result = client
              .databases
              .startOnlineMigration(databaseClusterUuid)
              .source(source)
              .disableSsl(disableSsl)
              .ignoreDbs(ignoreDbs)
              .execute();
      System.out.println(result);
      System.out.println(result.getId());
      System.out.println(result.getStatus());
      System.out.println(result.getCreatedAt());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#startOnlineMigration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<OnlineMigration> response = client
              .databases
              .startOnlineMigration(databaseClusterUuid)
              .source(source)
              .disableSsl(disableSsl)
              .ignoreDbs(ignoreDbs)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#startOnlineMigration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **sourceDatabase** | [**SourceDatabase**](SourceDatabase.md)|  | |

### Return type

[**OnlineMigration**](OnlineMigration.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="stopOnlineMigration"></a>
# **stopOnlineMigration**
> stopOnlineMigration(databaseClusterUuid, migrationId).execute();

Stop an Online Migration

To stop an online migration, send a DELETE request to &#x60;/v2/databases/$DATABASE_ID/online-migration/$MIGRATION_ID&#x60;.  A status of 204 will be given. This indicates that the request was processed successfully, but that no response body is needed. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String migrationId = "77b28fc8-19ff-11eb-8c9c-c68e24557488"; // A unique identifier assigned to the online migration.
    try {
      client
              .databases
              .stopOnlineMigration(databaseClusterUuid, migrationId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#stopOnlineMigration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .stopOnlineMigration(databaseClusterUuid, migrationId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#stopOnlineMigration");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **migrationId** | **String**| A unique identifier assigned to the online migration. | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateConfigCluster"></a>
# **updateConfigCluster**
> updateConfigCluster(databaseClusterUuid, databaseConfig).execute();

Update the Database Configuration for an Existing Database

To update the configuration for an existing database cluster, send a PATCH request to &#x60;/v2/databases/$DATABASE_ID/config&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    Mpr config = new Mpr();
    try {
      client
              .databases
              .updateConfigCluster(databaseClusterUuid)
              .config(config)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateConfigCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .updateConfigCluster(databaseClusterUuid)
              .config(config)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateConfigCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databaseConfig** | [**DatabaseConfig**](DatabaseConfig.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateConnectionPoolPostgresql"></a>
# **updateConnectionPoolPostgresql**
> updateConnectionPoolPostgresql(databaseClusterUuid, poolName, connectionPoolUpdate).execute();

Update Connection Pools (PostgreSQL)

To update a connection pool for a PostgreSQL database cluster, send a PUT request to  &#x60;/v2/databases/$DATABASE_ID/pools/$POOL_NAME&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String mode = "mode_example"; // The PGBouncer transaction mode for the connection pool. The allowed values are session, transaction, and statement.
    Integer size = 56; // The desired size of the PGBouncer connection pool. The maximum allowed size is determined by the size of the cluster's primary node. 25 backend server connections are allowed for every 1GB of RAM. Three are reserved for maintenance. For example, a primary node with 1 GB of RAM allows for a maximum of 22 backend server connections while one with 4 GB would allow for 97. Note that these are shared across all connection pools in a cluster.
    String db = "db_example"; // The database for use with the connection pool.
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String poolName = "backend-pool"; // The name used to identify the connection pool.
    String user = "user_example"; // The name of the user for use with the connection pool. When excluded, all sessions connect to the database as the inbound user.
    try {
      client
              .databases
              .updateConnectionPoolPostgresql(mode, size, db, databaseClusterUuid, poolName)
              .user(user)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateConnectionPoolPostgresql");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .updateConnectionPoolPostgresql(mode, size, db, databaseClusterUuid, poolName)
              .user(user)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateConnectionPoolPostgresql");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **poolName** | **String**| The name used to identify the connection pool. | |
| **connectionPoolUpdate** | [**ConnectionPoolUpdate**](ConnectionPoolUpdate.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateFirewallRules"></a>
# **updateFirewallRules**
> updateFirewallRules(databaseClusterUuid, databasesUpdateFirewallRulesRequest).execute();

Update Firewall Rules (Trusted Sources) for a Database

To update a database cluster&#39;s firewall rules (known as \&quot;trusted sources\&quot; in the control panel), send a PUT request to &#x60;/v2/databases/$DATABASE_ID/firewall&#x60; specifying which resources should be able to open connections to the database. You may limit connections to specific Droplets, Kubernetes clusters, or IP addresses. When a tag is provided, any Droplet or Kubernetes node with that tag applied to it will have access. The firewall is limited to 100 rules (or trusted sources). When possible, we recommend [placing your databases into a VPC network](https://www.digitalocean.com/docs/networking/vpc/) to limit access to them instead of using a firewall. A successful

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    List<FirewallRule> rules = Arrays.asList();
    try {
      client
              .databases
              .updateFirewallRules(databaseClusterUuid)
              .rules(rules)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateFirewallRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .updateFirewallRules(databaseClusterUuid)
              .rules(rules)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateFirewallRules");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **databasesUpdateFirewallRulesRequest** | [**DatabasesUpdateFirewallRulesRequest**](DatabasesUpdateFirewallRulesRequest.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateMetricsCredentials"></a>
# **updateMetricsCredentials**
> updateMetricsCredentials().databaseMetricsCredentials(databaseMetricsCredentials).execute();

Update Database Clusters&#39; Metrics Endpoint Credentials

To update the credentials for all database clusters&#39; metrics endpoints, send a PUT request to &#x60;/v2/databases/metrics/credentials&#x60;. A successful request will receive a 204 No Content status code  with no body in response.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    DatabasesBasicAuthCredentials credentials = new DatabasesBasicAuthCredentials();
    try {
      client
              .databases
              .updateMetricsCredentials()
              .credentials(credentials)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateMetricsCredentials");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .updateMetricsCredentials()
              .credentials(credentials)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateMetricsCredentials");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseMetricsCredentials** | [**DatabaseMetricsCredentials**](DatabaseMetricsCredentials.md)|  | [optional] |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateSettings"></a>
# **updateSettings**
> DatabasesAddUserResponse updateSettings(databaseClusterUuid, username, databasesUpdateSettingsRequest).execute();

Update a Database User

To update an existing database user, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/users/$USERNAME&#x60; with the desired settings.  **Note**: only &#x60;settings&#x60; can be updated via this type of request. If you wish to change the name of a user, you must recreate a new user.  The response will be a JSON object with a key called &#x60;user&#x60;. The value of this will be an object that contains the name of the update database user, along with the &#x60;settings&#x60; object that has been updated. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UserSettings settings = new UserSettings();
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String username = "app-01"; // The name of the database user.
    try {
      DatabasesAddUserResponse result = client
              .databases
              .updateSettings(settings, databaseClusterUuid, username)
              .execute();
      System.out.println(result);
      System.out.println(result.getUser());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesAddUserResponse> response = client
              .databases
              .updateSettings(settings, databaseClusterUuid, username)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateSettings");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **username** | **String**| The name of the database user. | |
| **databasesUpdateSettingsRequest** | [**DatabasesUpdateSettingsRequest**](DatabasesUpdateSettingsRequest.md)|  | |

### Return type

[**DatabasesAddUserResponse**](DatabasesAddUserResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | A JSON object with a key of &#x60;user&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateSqlMode"></a>
# **updateSqlMode**
> updateSqlMode(databaseClusterUuid, sqlMode).execute();

Update SQL Mode for a Cluster

To configure the SQL modes for an existing MySQL cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/sql_mode&#x60; specifying the desired modes. See the official MySQL 8 documentation for a [full list of supported SQL modes](https://dev.mysql.com/doc/refman/8.0/en/sql-mode.html#sql-mode-full). A successful request will receive a 204 No Content status code with no body in response.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    String sqlMode = "sqlMode_example"; // A string specifying the configured SQL modes for the MySQL cluster.
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    try {
      client
              .databases
              .updateSqlMode(sqlMode, databaseClusterUuid)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateSqlMode");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .updateSqlMode(sqlMode, databaseClusterUuid)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateSqlMode");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **sqlMode** | [**SqlMode**](SqlMode.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateTopicKafkaCluster"></a>
# **updateTopicKafkaCluster**
> DatabasesCreateTopicKafkaClusterResponse updateTopicKafkaCluster(databaseClusterUuid, topicName).kafkaTopicUpdate(kafkaTopicUpdate).execute();

Update Topic for a Kafka Cluster

To update a topic attached to a Kafka cluster, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/topics/$TOPIC_NAME&#x60;.  The result will be a JSON object with a &#x60;topic&#x60; key. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String topicName = "customer-events"; // The name used to identify the Kafka topic.
    Integer replicationFactor = 56; // The number of nodes to replicate data across the cluster.
    Integer partitionCount = 56; // The number of partitions available for the topic. On update, this value can only be increased.
    KafkaTopicConfig config = new KafkaTopicConfig();
    try {
      DatabasesCreateTopicKafkaClusterResponse result = client
              .databases
              .updateTopicKafkaCluster(databaseClusterUuid, topicName)
              .replicationFactor(replicationFactor)
              .partitionCount(partitionCount)
              .config(config)
              .execute();
      System.out.println(result);
      System.out.println(result.getTopic());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<DatabasesCreateTopicKafkaClusterResponse> response = client
              .databases
              .updateTopicKafkaCluster(databaseClusterUuid, topicName)
              .replicationFactor(replicationFactor)
              .partitionCount(partitionCount)
              .config(config)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#updateTopicKafkaCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **topicName** | **String**| The name used to identify the Kafka topic. | |
| **kafkaTopicUpdate** | [**KafkaTopicUpdate**](KafkaTopicUpdate.md)|  | [optional] |

### Return type

[**DatabasesCreateTopicKafkaClusterResponse**](DatabasesCreateTopicKafkaClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object with a key of &#x60;topic&#x60;. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="upgradeMajorVersion"></a>
# **upgradeMajorVersion**
> upgradeMajorVersion(databaseClusterUuid, version2).execute();

Upgrade Major Version for a Database

To upgrade the major version of a database, send a PUT request to &#x60;/v2/databases/$DATABASE_ID/upgrade&#x60;, specifying the target version. A successful request will receive a 204 No Content status code with no body in response.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.DatabasesApi;
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
    UUID databaseClusterUuid = UUID.fromString("9cc10173-e9ea-4176-9dbc-a4cee4c4ff30"); // A unique identifier for a database cluster.
    String version = "version_example"; // A string representing the version of the database engine in use for the cluster.
    try {
      client
              .databases
              .upgradeMajorVersion(databaseClusterUuid)
              .version(version)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#upgradeMajorVersion");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .databases
              .upgradeMajorVersion(databaseClusterUuid)
              .version(version)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling DatabasesApi#upgradeMajorVersion");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}

```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **databaseClusterUuid** | **UUID**| A unique identifier for a database cluster. | |
| **version2** | [**Version2**](Version2.md)|  | |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **204** | The action was successful and the response body is empty. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

