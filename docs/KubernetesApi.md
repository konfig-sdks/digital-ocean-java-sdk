# KubernetesApi

All URIs are relative to *https://api.digitalocean.com*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**addContainerRegistryToClusters**](KubernetesApi.md#addContainerRegistryToClusters) | **POST** /v2/kubernetes/registry | Add Container Registry to Kubernetes Clusters |
| [**addNodePool**](KubernetesApi.md#addNodePool) | **POST** /v2/kubernetes/clusters/{cluster_id}/node_pools | Add a Node Pool to a Kubernetes Cluster |
| [**createNewCluster**](KubernetesApi.md#createNewCluster) | **POST** /v2/kubernetes/clusters | Create a New Kubernetes Cluster |
| [**deleteCluster**](KubernetesApi.md#deleteCluster) | **DELETE** /v2/kubernetes/clusters/{cluster_id} | Delete a Kubernetes Cluster |
| [**deleteClusterAssociatedResourcesDangerous**](KubernetesApi.md#deleteClusterAssociatedResourcesDangerous) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources/dangerous | Delete a Cluster and All of its Associated Resources (Dangerous) |
| [**deleteNodeInNodePool**](KubernetesApi.md#deleteNodeInNodePool) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}/nodes/{node_id} | Delete a Node in a Kubernetes Cluster |
| [**deleteNodePool**](KubernetesApi.md#deleteNodePool) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id} | Delete a Node Pool in a Kubernetes Cluster |
| [**getAvailableUpgrades**](KubernetesApi.md#getAvailableUpgrades) | **GET** /v2/kubernetes/clusters/{cluster_id}/upgrades | Retrieve Available Upgrades for an Existing Kubernetes Cluster |
| [**getClusterInfo**](KubernetesApi.md#getClusterInfo) | **GET** /v2/kubernetes/clusters/{cluster_id} | Retrieve an Existing Kubernetes Cluster |
| [**getClusterLintDiagnostics**](KubernetesApi.md#getClusterLintDiagnostics) | **GET** /v2/kubernetes/clusters/{cluster_id}/clusterlint | Fetch Clusterlint Diagnostics for a Kubernetes Cluster |
| [**getCredentialsByClusterId**](KubernetesApi.md#getCredentialsByClusterId) | **GET** /v2/kubernetes/clusters/{cluster_id}/credentials | Retrieve Credentials for a Kubernetes Cluster |
| [**getKubeconfig**](KubernetesApi.md#getKubeconfig) | **GET** /v2/kubernetes/clusters/{cluster_id}/kubeconfig | Retrieve the kubeconfig for a Kubernetes Cluster |
| [**getNodePool**](KubernetesApi.md#getNodePool) | **GET** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id} | Retrieve a Node Pool for a Kubernetes Cluster |
| [**getUserInformation**](KubernetesApi.md#getUserInformation) | **GET** /v2/kubernetes/clusters/{cluster_id}/user | Retrieve User Information for a Kubernetes Cluster |
| [**listAssociatedResources**](KubernetesApi.md#listAssociatedResources) | **GET** /v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources | List Associated Resources for Cluster Deletion |
| [**listClusters**](KubernetesApi.md#listClusters) | **GET** /v2/kubernetes/clusters | List All Kubernetes Clusters |
| [**listNodePools**](KubernetesApi.md#listNodePools) | **GET** /v2/kubernetes/clusters/{cluster_id}/node_pools | List All Node Pools in a Kubernetes Clusters |
| [**listOptions**](KubernetesApi.md#listOptions) | **GET** /v2/kubernetes/options | List Available Regions, Node Sizes, and Versions of Kubernetes |
| [**recycleNodePool**](KubernetesApi.md#recycleNodePool) | **POST** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id}/recycle | Recycle a Kubernetes Node Pool |
| [**removeRegistry**](KubernetesApi.md#removeRegistry) | **DELETE** /v2/kubernetes/registry | Remove Container Registry from Kubernetes Clusters |
| [**runClusterlintChecks**](KubernetesApi.md#runClusterlintChecks) | **POST** /v2/kubernetes/clusters/{cluster_id}/clusterlint | Run Clusterlint Checks on a Kubernetes Cluster |
| [**selectiveClusterDestroy**](KubernetesApi.md#selectiveClusterDestroy) | **DELETE** /v2/kubernetes/clusters/{cluster_id}/destroy_with_associated_resources/selective | Selectively Delete a Cluster and its Associated Resources |
| [**updateCluster**](KubernetesApi.md#updateCluster) | **PUT** /v2/kubernetes/clusters/{cluster_id} | Update a Kubernetes Cluster |
| [**updateNodePool**](KubernetesApi.md#updateNodePool) | **PUT** /v2/kubernetes/clusters/{cluster_id}/node_pools/{node_pool_id} | Update a Node Pool in a Kubernetes Cluster |
| [**upgradeCluster**](KubernetesApi.md#upgradeCluster) | **POST** /v2/kubernetes/clusters/{cluster_id}/upgrade | Upgrade a Kubernetes Cluster |


<a name="addContainerRegistryToClusters"></a>
# **addContainerRegistryToClusters**
> addContainerRegistryToClusters().clusterRegistries(clusterRegistries).execute();

Add Container Registry to Kubernetes Clusters

To integrate the container registry with Kubernetes clusters, send a POST request to &#x60;/v2/kubernetes/registry&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    List<String> clusterUuids = Arrays.asList(); // An array containing the UUIDs of Kubernetes clusters.
    try {
      client
              .kubernetes
              .addContainerRegistryToClusters()
              .clusterUuids(clusterUuids)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#addContainerRegistryToClusters");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .addContainerRegistryToClusters()
              .clusterUuids(clusterUuids)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#addContainerRegistryToClusters");
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
| **clusterRegistries** | [**ClusterRegistries**](ClusterRegistries.md)|  | [optional] |

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

<a name="addNodePool"></a>
# **addNodePool**
> KubernetesAddNodePoolResponse addNodePool(clusterId, kubernetesNodePool).execute();

Add a Node Pool to a Kubernetes Cluster

To add an additional node pool to a Kubernetes clusters, send a POST request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools&#x60; with the following attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    String size = "size_example"; // The slug identifier for the type of Droplet used as workers in the node pool.
    String name = "name_example"; // A human-readable name for the node pool.
    Integer count = 56; // The number of Droplet instances in the node pool.
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    List<String> tags = Arrays.asList(); // An array containing the tags applied to the node pool. All node pools are automatically tagged `k8s`, `k8s-worker`, and `k8s:$K8S_CLUSTER_ID`.
    UUID id = UUID.randomUUID(); // A unique ID that can be used to identify and reference a specific node pool.
    Object labels = null; // An object of key/value mappings specifying labels to apply to all nodes in a pool. Labels will automatically be applied to all existing nodes and any subsequent nodes added to the pool. Note that when a label is removed, it is not deleted from the nodes in the pool.
    List<KubernetesNodePoolTaint> taints = Arrays.asList(); // An array of taints to apply to all nodes in a pool. Taints will automatically be applied to all existing nodes and any subsequent nodes added to the pool. When a taint is removed, it is deleted from all nodes in the pool.
    Boolean autoScale = true; // A boolean value indicating whether auto-scaling is enabled for this node pool.
    Integer minNodes = 56; // The minimum number of nodes that this node pool can be auto-scaled to. The value will be `0` if `auto_scale` is set to `false`.
    Integer maxNodes = 56; // The maximum number of nodes that this node pool can be auto-scaled to. The value will be `0` if `auto_scale` is set to `false`.
    List<Node> nodes = Arrays.asList(); // An object specifying the details of a specific worker node in a node pool.
    try {
      KubernetesAddNodePoolResponse result = client
              .kubernetes
              .addNodePool(size, name, count, clusterId)
              .tags(tags)
              .id(id)
              .labels(labels)
              .taints(taints)
              .autoScale(autoScale)
              .minNodes(minNodes)
              .maxNodes(maxNodes)
              .nodes(nodes)
              .execute();
      System.out.println(result);
      System.out.println(result.getNodePool());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#addNodePool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesAddNodePoolResponse> response = client
              .kubernetes
              .addNodePool(size, name, count, clusterId)
              .tags(tags)
              .id(id)
              .labels(labels)
              .taints(taints)
              .autoScale(autoScale)
              .minNodes(minNodes)
              .maxNodes(maxNodes)
              .nodes(nodes)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#addNodePool");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **kubernetesNodePool** | [**KubernetesNodePool**](KubernetesNodePool.md)|  | |

### Return type

[**KubernetesAddNodePoolResponse**](KubernetesAddNodePoolResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="createNewCluster"></a>
# **createNewCluster**
> KubernetesCreateNewClusterResponse createNewCluster(cluster).execute();

Create a New Kubernetes Cluster

To create a new Kubernetes cluster, send a POST request to &#x60;/v2/kubernetes/clusters&#x60;. The request must contain at least one node pool with at least one worker.  The request may contain a maintenance window policy describing a time period when disruptive maintenance tasks may be carried out. Omitting the policy implies that a window will be chosen automatically. See [here](https://www.digitalocean.com/docs/kubernetes/how-to/upgrade-cluster/) for details. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    String version = "version_example"; // The slug identifier for the version of Kubernetes used for the cluster. If set to a minor version (e.g. \\\"1.14\\\"), the latest version within it will be used (e.g. \\\"1.14.6-do.1\\\"); if set to \\\"latest\\\", the latest published version will be used. See the `/v2/kubernetes/options` endpoint to find all currently available versions.
    String name = "name_example"; // A human-readable name for a Kubernetes cluster.
    String region = "region_example"; // The slug identifier for the region where the Kubernetes cluster is located.
    List<KubernetesNodePool> nodePools = Arrays.asList(); // An object specifying the details of the worker nodes available to the Kubernetes cluster.
    List<String> tags = Arrays.asList(); // An array of tags applied to the Kubernetes cluster. All clusters are automatically tagged `k8s` and `k8s:$K8S_CLUSTER_ID`.
    UUID id = UUID.randomUUID(); // A unique ID that can be used to identify and reference a Kubernetes cluster.
    String clusterSubnet = "clusterSubnet_example"; // The range of IP addresses in the overlay network of the Kubernetes cluster in CIDR notation.
    String serviceSubnet = "serviceSubnet_example"; // The range of assignable IP addresses for services running in the Kubernetes cluster in CIDR notation.
    UUID vpcUuid = UUID.randomUUID(); // A string specifying the UUID of the VPC to which the Kubernetes cluster is assigned.
    String ipv4 = "ipv4_example"; // The public IPv4 address of the Kubernetes master node. This will not be set if high availability is configured on the cluster (v1.21+)
    String endpoint = "endpoint_example"; // The base URL of the API server on the Kubernetes master node.
    MaintenancePolicy maintenancePolicy = new MaintenancePolicy();
    Boolean autoUpgrade = false; // A boolean value indicating whether the cluster will be automatically upgraded to new patch releases during its maintenance window.
    ClusterStatus status = new ClusterStatus();
    OffsetDateTime createdAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the Kubernetes cluster was created.
    OffsetDateTime updatedAt = OffsetDateTime.now(); // A time value given in ISO8601 combined date and time format that represents when the Kubernetes cluster was last updated.
    Boolean surgeUpgrade = false; // A boolean value indicating whether surge upgrade is enabled/disabled for the cluster. Surge upgrade makes cluster upgrades fast and reliable by bringing up new nodes before destroying the outdated nodes.
    Boolean ha = false; // A boolean value indicating whether the control plane is run in a highly available configuration in the cluster. Highly available control planes incur less downtime. The property cannot be disabled.
    Boolean registryEnabled = true; // A read-only boolean value indicating if a container registry is integrated with the cluster.
    try {
      KubernetesCreateNewClusterResponse result = client
              .kubernetes
              .createNewCluster(version, name, region, nodePools)
              .tags(tags)
              .id(id)
              .clusterSubnet(clusterSubnet)
              .serviceSubnet(serviceSubnet)
              .vpcUuid(vpcUuid)
              .ipv4(ipv4)
              .endpoint(endpoint)
              .maintenancePolicy(maintenancePolicy)
              .autoUpgrade(autoUpgrade)
              .status(status)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .surgeUpgrade(surgeUpgrade)
              .ha(ha)
              .registryEnabled(registryEnabled)
              .execute();
      System.out.println(result);
      System.out.println(result.getKubernetesCluster());
      System.out.println(result.getKubernetesClusters());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#createNewCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesCreateNewClusterResponse> response = client
              .kubernetes
              .createNewCluster(version, name, region, nodePools)
              .tags(tags)
              .id(id)
              .clusterSubnet(clusterSubnet)
              .serviceSubnet(serviceSubnet)
              .vpcUuid(vpcUuid)
              .ipv4(ipv4)
              .endpoint(endpoint)
              .maintenancePolicy(maintenancePolicy)
              .autoUpgrade(autoUpgrade)
              .status(status)
              .createdAt(createdAt)
              .updatedAt(updatedAt)
              .surgeUpgrade(surgeUpgrade)
              .ha(ha)
              .registryEnabled(registryEnabled)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#createNewCluster");
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
| **cluster** | [**Cluster**](Cluster.md)|  | |

### Return type

[**KubernetesCreateNewClusterResponse**](KubernetesCreateNewClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **201** | The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  The IP address and cluster API server endpoint will not be available until the cluster has finished provisioning. The initial value of the cluster&#39;s &#x60;status.state&#x60; attribute will be &#x60;provisioning&#x60;. When the cluster is ready, this will transition to &#x60;running&#x60;.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteCluster"></a>
# **deleteCluster**
> deleteCluster(clusterId).execute();

Delete a Kubernetes Cluster

To delete a Kubernetes cluster and all services deployed to it, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID&#x60;.  A 204 status code with no body will be returned in response to a successful request. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    try {
      client
              .kubernetes
              .deleteCluster(clusterId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .deleteCluster(clusterId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteCluster");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |

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

<a name="deleteClusterAssociatedResourcesDangerous"></a>
# **deleteClusterAssociatedResourcesDangerous**
> deleteClusterAssociatedResourcesDangerous(clusterId).execute();

Delete a Cluster and All of its Associated Resources (Dangerous)

To delete a Kubernetes cluster with all of its associated resources, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/destroy_with_associated_resources/dangerous&#x60;. A 204 status code with no body will be returned in response to a successful request. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    try {
      client
              .kubernetes
              .deleteClusterAssociatedResourcesDangerous(clusterId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteClusterAssociatedResourcesDangerous");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .deleteClusterAssociatedResourcesDangerous(clusterId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteClusterAssociatedResourcesDangerous");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |

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

<a name="deleteNodeInNodePool"></a>
# **deleteNodeInNodePool**
> deleteNodeInNodePool(clusterId, nodePoolId, nodeId).skipDrain(skipDrain).replace(replace).execute();

Delete a Node in a Kubernetes Cluster

To delete a single node in a pool, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID/nodes/$NODE_ID&#x60;.  Appending the &#x60;skip_drain&#x3D;1&#x60; query parameter to the request causes node draining to be skipped. Omitting the query parameter or setting its value to &#x60;0&#x60; carries out draining prior to deletion.  Appending the &#x60;replace&#x3D;1&#x60; query parameter to the request causes the node to be replaced by a new one after deletion. Omitting the query parameter or setting its value to &#x60;0&#x60; deletes without replacement. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    UUID nodePoolId = UUID.fromString("cdda885e-7663-40c8-bc74-3a036c66545d"); // A unique ID that can be used to reference a Kubernetes node pool.
    UUID nodeId = UUID.fromString("478247f8-b1bb-4f7a-8db9-2a5f8d4b8f8f"); // A unique ID that can be used to reference a node in a Kubernetes node pool.
    Integer skipDrain = 0; // Specifies whether or not to drain workloads from a node before it is deleted. Setting it to `1` causes node draining to be skipped. Omitting the query parameter or setting its value to `0` carries out draining prior to deletion.
    Integer replace = 0; // Specifies whether or not to replace a node after it has been deleted. Setting it to `1` causes the node to be replaced by a new one after deletion. Omitting the query parameter or setting its value to `0` deletes without replacement.
    try {
      client
              .kubernetes
              .deleteNodeInNodePool(clusterId, nodePoolId, nodeId)
              .skipDrain(skipDrain)
              .replace(replace)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteNodeInNodePool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .deleteNodeInNodePool(clusterId, nodePoolId, nodeId)
              .skipDrain(skipDrain)
              .replace(replace)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteNodeInNodePool");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **nodePoolId** | **UUID**| A unique ID that can be used to reference a Kubernetes node pool. | |
| **nodeId** | **UUID**| A unique ID that can be used to reference a node in a Kubernetes node pool. | |
| **skipDrain** | **Integer**| Specifies whether or not to drain workloads from a node before it is deleted. Setting it to &#x60;1&#x60; causes node draining to be skipped. Omitting the query parameter or setting its value to &#x60;0&#x60; carries out draining prior to deletion. | [optional] [default to 0] |
| **replace** | **Integer**| Specifies whether or not to replace a node after it has been deleted. Setting it to &#x60;1&#x60; causes the node to be replaced by a new one after deletion. Omitting the query parameter or setting its value to &#x60;0&#x60; deletes without replacement. | [optional] [default to 0] |

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
| **202** | The does not indicate the success or failure of any operation, just that the request has been accepted for processing. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="deleteNodePool"></a>
# **deleteNodePool**
> deleteNodePool(clusterId, nodePoolId).execute();

Delete a Node Pool in a Kubernetes Cluster

To delete a node pool, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID&#x60;.  A 204 status code with no body will be returned in response to a successful request. Nodes in the pool will subsequently be drained and deleted. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    UUID nodePoolId = UUID.fromString("cdda885e-7663-40c8-bc74-3a036c66545d"); // A unique ID that can be used to reference a Kubernetes node pool.
    try {
      client
              .kubernetes
              .deleteNodePool(clusterId, nodePoolId)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteNodePool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .deleteNodePool(clusterId, nodePoolId)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#deleteNodePool");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **nodePoolId** | **UUID**| A unique ID that can be used to reference a Kubernetes node pool. | |

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

<a name="getAvailableUpgrades"></a>
# **getAvailableUpgrades**
> KubernetesGetAvailableUpgradesResponse getAvailableUpgrades(clusterId).execute();

Retrieve Available Upgrades for an Existing Kubernetes Cluster

To determine whether a cluster can be upgraded, and the versions to which it can be upgraded, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/upgrades&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    try {
      KubernetesGetAvailableUpgradesResponse result = client
              .kubernetes
              .getAvailableUpgrades(clusterId)
              .execute();
      System.out.println(result);
      System.out.println(result.getAvailableUpgradeVersions());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getAvailableUpgrades");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesGetAvailableUpgradesResponse> response = client
              .kubernetes
              .getAvailableUpgrades(clusterId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getAvailableUpgrades");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |

### Return type

[**KubernetesGetAvailableUpgradesResponse**](KubernetesGetAvailableUpgradesResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;available_upgrade_versions&#x60;. The value of this will be an array of objects, representing the upgrade versions currently available for this cluster.  If the cluster is up-to-date (i.e. there are no upgrades currently available) &#x60;available_upgrade_versions&#x60; will be &#x60;null&#x60;.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getClusterInfo"></a>
# **getClusterInfo**
> KubernetesGetClusterInfoResponse getClusterInfo(clusterId).execute();

Retrieve an Existing Kubernetes Cluster

To show information about an existing Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    try {
      KubernetesGetClusterInfoResponse result = client
              .kubernetes
              .getClusterInfo(clusterId)
              .execute();
      System.out.println(result);
      System.out.println(result.getKubernetesCluster());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getClusterInfo");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesGetClusterInfoResponse> response = client
              .kubernetes
              .getClusterInfo(clusterId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getClusterInfo");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |

### Return type

[**KubernetesGetClusterInfoResponse**](KubernetesGetClusterInfoResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getClusterLintDiagnostics"></a>
# **getClusterLintDiagnostics**
> ClusterlintResults getClusterLintDiagnostics(clusterId).runId(runId).execute();

Fetch Clusterlint Diagnostics for a Kubernetes Cluster

To request clusterlint diagnostics for your cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/clusterlint&#x60;. If the &#x60;run_id&#x60; query parameter is provided, then the diagnostics for the specific run is fetched. By default, the latest results are shown.  To find out how to address clusterlint feedback, please refer to [the clusterlint check documentation](https://github.com/digitalocean/clusterlint/blob/master/checks.md). 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    UUID runId = UUID.fromString("50c2f44c-011d-493e-aee5-361a4a0d1844"); // Specifies the clusterlint run whose results will be retrieved.
    try {
      ClusterlintResults result = client
              .kubernetes
              .getClusterLintDiagnostics(clusterId)
              .runId(runId)
              .execute();
      System.out.println(result);
      System.out.println(result.getRunId());
      System.out.println(result.getRequestedAt());
      System.out.println(result.getCompletedAt());
      System.out.println(result.getDiagnostics());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getClusterLintDiagnostics");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<ClusterlintResults> response = client
              .kubernetes
              .getClusterLintDiagnostics(clusterId)
              .runId(runId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getClusterLintDiagnostics");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **runId** | **UUID**| Specifies the clusterlint run whose results will be retrieved. | [optional] |

### Return type

[**ClusterlintResults**](ClusterlintResults.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response is a JSON object which contains the diagnostics on Kubernetes objects in the cluster. Each diagnostic will contain some metadata information about the object and feedback for users to act upon.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getCredentialsByClusterId"></a>
# **getCredentialsByClusterId**
> Credentials getCredentialsByClusterId(clusterId).expirySeconds(expirySeconds).execute();

Retrieve Credentials for a Kubernetes Cluster

This endpoint returns a JSON object . It can be used to programmatically construct Kubernetes clients which cannot parse kubeconfig files.  The resulting JSON object contains token-based authentication for clusters supporting it, and certificate-based authentication otherwise. For a list of supported versions and more information, see \&quot;[How to Connect to a DigitalOcean Kubernetes Cluster with kubectl](https://www.digitalocean.com/docs/kubernetes/how-to/connect-with-kubectl/)\&quot;.  To retrieve credentials for accessing a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/credentials&#x60;.  Clusters supporting token-based authentication may define an expiration by passing a duration in seconds as a query parameter to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/credentials?expiry_seconds&#x3D;$DURATION_IN_SECONDS&#x60;. If not set or 0, then the token will have a 7 day expiry. The query parameter has no impact in certificate-based authentication. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    Integer expirySeconds = 0; // The duration in seconds that the returned Kubernetes credentials will be valid. If not set or 0, the credentials will have a 7 day expiry.
    try {
      Credentials result = client
              .kubernetes
              .getCredentialsByClusterId(clusterId)
              .expirySeconds(expirySeconds)
              .execute();
      System.out.println(result);
      System.out.println(result.getServer());
      System.out.println(result.getCertificateAuthorityData());
      System.out.println(result.getClientCertificateData());
      System.out.println(result.getClientKeyData());
      System.out.println(result.getToken());
      System.out.println(result.getExpiresAt());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getCredentialsByClusterId");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Credentials> response = client
              .kubernetes
              .getCredentialsByClusterId(clusterId)
              .expirySeconds(expirySeconds)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getCredentialsByClusterId");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **expirySeconds** | **Integer**| The duration in seconds that the returned Kubernetes credentials will be valid. If not set or 0, the credentials will have a 7 day expiry. | [optional] [default to 0] |

### Return type

[**Credentials**](Credentials.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A JSON object containing credentials for a cluster. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getKubeconfig"></a>
# **getKubeconfig**
> getKubeconfig(clusterId).expirySeconds(expirySeconds).execute();

Retrieve the kubeconfig for a Kubernetes Cluster

This endpoint returns a kubeconfig file in YAML format. It can be used to connect to and administer the cluster using the Kubernetes command line tool, &#x60;kubectl&#x60;, or other programs supporting kubeconfig files (e.g., client libraries).  The resulting kubeconfig file uses token-based authentication for clusters supporting it, and certificate-based authentication otherwise. For a list of supported versions and more information, see \&quot;[How to Connect to a DigitalOcean Kubernetes Cluster with kubectl](https://www.digitalocean.com/docs/kubernetes/how-to/connect-with-kubectl/)\&quot;.  To retrieve a kubeconfig file for use with a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/kubeconfig&#x60;.  Clusters supporting token-based authentication may define an expiration by passing a duration in seconds as a query parameter to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/kubeconfig?expiry_seconds&#x3D;$DURATION_IN_SECONDS&#x60;. If not set or 0, then the token will have a 7 day expiry. The query parameter has no impact in certificate-based authentication. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    Integer expirySeconds = 0; // The duration in seconds that the returned Kubernetes credentials will be valid. If not set or 0, the credentials will have a 7 day expiry.
    try {
      client
              .kubernetes
              .getKubeconfig(clusterId)
              .expirySeconds(expirySeconds)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getKubeconfig");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .getKubeconfig(clusterId)
              .expirySeconds(expirySeconds)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getKubeconfig");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **expirySeconds** | **Integer**| The duration in seconds that the returned Kubernetes credentials will be valid. If not set or 0, the credentials will have a 7 day expiry. | [optional] [default to 0] |

### Return type

null (empty response body)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/yaml, application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | A kubeconfig file for the cluster in YAML format. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getNodePool"></a>
# **getNodePool**
> KubernetesGetNodePoolResponse getNodePool(clusterId, nodePoolId).execute();

Retrieve a Node Pool for a Kubernetes Cluster

To show information about a specific node pool in a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    UUID nodePoolId = UUID.fromString("cdda885e-7663-40c8-bc74-3a036c66545d"); // A unique ID that can be used to reference a Kubernetes node pool.
    try {
      KubernetesGetNodePoolResponse result = client
              .kubernetes
              .getNodePool(clusterId, nodePoolId)
              .execute();
      System.out.println(result);
      System.out.println(result.getNodePool());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getNodePool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesGetNodePoolResponse> response = client
              .kubernetes
              .getNodePool(clusterId, nodePoolId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getNodePool");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **nodePoolId** | **UUID**| A unique ID that can be used to reference a Kubernetes node pool. | |

### Return type

[**KubernetesGetNodePoolResponse**](KubernetesGetNodePoolResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="getUserInformation"></a>
# **getUserInformation**
> User getUserInformation(clusterId).execute();

Retrieve User Information for a Kubernetes Cluster

To show information the user associated with a Kubernetes cluster, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/user&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    try {
      User result = client
              .kubernetes
              .getUserInformation(clusterId)
              .execute();
      System.out.println(result);
      System.out.println(result.getKubernetesClusterUser());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getUserInformation");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<User> response = client
              .kubernetes
              .getUserInformation(clusterId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#getUserInformation");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |

### Return type

[**User**](User.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;kubernetes_cluster_user&#x60; containing the username and in-cluster groups that it belongs to.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listAssociatedResources"></a>
# **listAssociatedResources**
> AssociatedKubernetesResources listAssociatedResources(clusterId).execute();

List Associated Resources for Cluster Deletion

To list the associated billable resources that can be destroyed along with a cluster, send a GET request to the &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/destroy_with_associated_resources&#x60; endpoint.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    try {
      AssociatedKubernetesResources result = client
              .kubernetes
              .listAssociatedResources(clusterId)
              .execute();
      System.out.println(result);
      System.out.println(result.getLoadBalancers());
      System.out.println(result.getVolumes());
      System.out.println(result.getVolumeSnapshots());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listAssociatedResources");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<AssociatedKubernetesResources> response = client
              .kubernetes
              .listAssociatedResources(clusterId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listAssociatedResources");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |

### Return type

[**AssociatedKubernetesResources**](AssociatedKubernetesResources.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object containing &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, and &#x60;volume_snapshots&#x60; keys. Each will be set to an array of objects containing the standard attributes for associated resources. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listClusters"></a>
# **listClusters**
> KubernetesListClustersResponse listClusters().perPage(perPage).page(page).execute();

List All Kubernetes Clusters

To list all of the Kubernetes clusters on your account, send a GET request to &#x60;/v2/kubernetes/clusters&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    Integer perPage = 20; // Number of items returned per page
    Integer page = 1; // Which 'page' of paginated results to return.
    try {
      KubernetesListClustersResponse result = client
              .kubernetes
              .listClusters()
              .perPage(perPage)
              .page(page)
              .execute();
      System.out.println(result);
      System.out.println(result.getKubernetesClusters());
      System.out.println(result.getLinks());
      System.out.println(result.getMeta());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listClusters");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesListClustersResponse> response = client
              .kubernetes
              .listClusters()
              .perPage(perPage)
              .page(page)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listClusters");
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
| **perPage** | **Integer**| Number of items returned per page | [optional] [default to 20] |
| **page** | **Integer**| Which &#39;page&#39; of paginated results to return. | [optional] [default to 1] |

### Return type

[**KubernetesListClustersResponse**](KubernetesListClustersResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;kubernetes_clusters&#x60;. This will be set to an array of objects, each of which will contain the standard Kubernetes cluster attributes.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listNodePools"></a>
# **listNodePools**
> KubernetesListNodePoolsResponse listNodePools(clusterId).execute();

List All Node Pools in a Kubernetes Clusters

To list all of the node pools in a Kubernetes clusters, send a GET request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    try {
      KubernetesListNodePoolsResponse result = client
              .kubernetes
              .listNodePools(clusterId)
              .execute();
      System.out.println(result);
      System.out.println(result.getNodePools());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listNodePools");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesListNodePoolsResponse> response = client
              .kubernetes
              .listNodePools(clusterId)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listNodePools");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |

### Return type

[**KubernetesListNodePoolsResponse**](KubernetesListNodePoolsResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;node_pools&#x60;. This will be set to an array of objects, each of which will contain the standard node pool attributes.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="listOptions"></a>
# **listOptions**
> KubernetesOptions listOptions().execute();

List Available Regions, Node Sizes, and Versions of Kubernetes

To list the versions of Kubernetes available for use, the regions that support Kubernetes, and the available node sizes, send a GET request to &#x60;/v2/kubernetes/options&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
      KubernetesOptions result = client
              .kubernetes
              .listOptions()
              .execute();
      System.out.println(result);
      System.out.println(result.getOptions());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listOptions");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesOptions> response = client
              .kubernetes
              .listOptions()
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#listOptions");
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

[**KubernetesOptions**](KubernetesOptions.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | The response will be a JSON object with a key called &#x60;options&#x60; which contains &#x60;regions&#x60;, &#x60;versions&#x60;, and &#x60;sizes&#x60; objects listing the available options and the matching slugs for use when creating a new cluster.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="recycleNodePool"></a>
# **recycleNodePool**
> recycleNodePool(clusterId, nodePoolId, kubernetesRecycleNodePoolRequest).execute();

Recycle a Kubernetes Node Pool

The endpoint has been deprecated. Please use the DELETE &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID/nodes/$NODE_ID&#x60; method instead. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    UUID nodePoolId = UUID.fromString("cdda885e-7663-40c8-bc74-3a036c66545d"); // A unique ID that can be used to reference a Kubernetes node pool.
    List<String> nodes = Arrays.asList();
    try {
      client
              .kubernetes
              .recycleNodePool(clusterId, nodePoolId)
              .nodes(nodes)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#recycleNodePool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .recycleNodePool(clusterId, nodePoolId)
              .nodes(nodes)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#recycleNodePool");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **nodePoolId** | **UUID**| A unique ID that can be used to reference a Kubernetes node pool. | |
| **kubernetesRecycleNodePoolRequest** | [**KubernetesRecycleNodePoolRequest**](KubernetesRecycleNodePoolRequest.md)|  | |

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

<a name="removeRegistry"></a>
# **removeRegistry**
> removeRegistry().clusterRegistries(clusterRegistries).execute();

Remove Container Registry from Kubernetes Clusters

To remove the container registry from Kubernetes clusters, send a DELETE request to &#x60;/v2/kubernetes/registry&#x60;.

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    List<String> clusterUuids = Arrays.asList(); // An array containing the UUIDs of Kubernetes clusters.
    try {
      client
              .kubernetes
              .removeRegistry()
              .clusterUuids(clusterUuids)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#removeRegistry");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .removeRegistry()
              .clusterUuids(clusterUuids)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#removeRegistry");
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
| **clusterRegistries** | [**ClusterRegistries**](ClusterRegistries.md)|  | [optional] |

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

<a name="runClusterlintChecks"></a>
# **runClusterlintChecks**
> Object runClusterlintChecks(clusterId).clusterlintRequest(clusterlintRequest).execute();

Run Clusterlint Checks on a Kubernetes Cluster

Clusterlint helps operators conform to Kubernetes best practices around resources, security and reliability to avoid common problems while operating or upgrading the clusters.  To request a clusterlint run on your cluster, send a POST request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/clusterlint&#x60;. This will run all checks present in the &#x60;doks&#x60; group by default, if a request body is not specified. Optionally specify the below attributes.  For information about the available checks, please refer to [the clusterlint check documentation](https://github.com/digitalocean/clusterlint/blob/master/checks.md). 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    List<String> includeGroups = Arrays.asList(); // An array of check groups that will be run when clusterlint executes checks.
    List<String> includeChecks = Arrays.asList(); // An array of checks that will be run when clusterlint executes checks.
    List<String> excludeGroups = Arrays.asList(); // An array of check groups that will be omitted when clusterlint executes checks.
    List<String> excludeChecks = Arrays.asList(); // An array of checks that will be run when clusterlint executes checks.
    try {
      Object result = client
              .kubernetes
              .runClusterlintChecks(clusterId)
              .includeGroups(includeGroups)
              .includeChecks(includeChecks)
              .excludeGroups(excludeGroups)
              .excludeChecks(excludeChecks)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#runClusterlintChecks");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<Object> response = client
              .kubernetes
              .runClusterlintChecks(clusterId)
              .includeGroups(includeGroups)
              .includeChecks(includeChecks)
              .excludeGroups(excludeGroups)
              .excludeChecks(excludeChecks)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#runClusterlintChecks");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **clusterlintRequest** | [**ClusterlintRequest**](ClusterlintRequest.md)|  | [optional] |

### Return type

**Object**

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The response is a JSON object with a key called &#x60;run_id&#x60; that you can later use to fetch the run results. |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="selectiveClusterDestroy"></a>
# **selectiveClusterDestroy**
> selectiveClusterDestroy(clusterId, destroyAssociatedKubernetesResources).execute();

Selectively Delete a Cluster and its Associated Resources

To delete a Kubernetes cluster along with a subset of its associated resources, send a DELETE request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/destroy_with_associated_resources/selective&#x60;.  The JSON body of the request should include &#x60;load_balancers&#x60;, &#x60;volumes&#x60;, or &#x60;volume_snapshots&#x60; keys each set to an array of IDs for the associated resources to be destroyed.  The IDs can be found by querying the cluster&#39;s associated resources endpoint. Any associated resource not included in the request will remain and continue to accrue changes on your account. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    List<String> loadBalancers = Arrays.asList(); // A list of IDs for associated load balancers to destroy along with the cluster.
    List<String> volumes = Arrays.asList(); // A list of IDs for associated volumes to destroy along with the cluster.
    List<String> volumeSnapshots = Arrays.asList(); // A list of IDs for associated volume snapshots to destroy along with the cluster.
    try {
      client
              .kubernetes
              .selectiveClusterDestroy(clusterId)
              .loadBalancers(loadBalancers)
              .volumes(volumes)
              .volumeSnapshots(volumeSnapshots)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#selectiveClusterDestroy");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .selectiveClusterDestroy(clusterId)
              .loadBalancers(loadBalancers)
              .volumes(volumes)
              .volumeSnapshots(volumeSnapshots)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#selectiveClusterDestroy");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **destroyAssociatedKubernetesResources** | [**DestroyAssociatedKubernetesResources**](DestroyAssociatedKubernetesResources.md)|  | |

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

<a name="updateCluster"></a>
# **updateCluster**
> KubernetesUpdateClusterResponse updateCluster(clusterId, clusterUpdate).execute();

Update a Kubernetes Cluster

To update a Kubernetes cluster, send a PUT request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID&#x60; and specify one or more of the attributes below. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    String name = "name_example"; // A human-readable name for a Kubernetes cluster.
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    List<String> tags = Arrays.asList(); // An array of tags applied to the Kubernetes cluster. All clusters are automatically tagged `k8s` and `k8s:$K8S_CLUSTER_ID`.
    MaintenancePolicy maintenancePolicy = new MaintenancePolicy();
    Boolean autoUpgrade = false; // A boolean value indicating whether the cluster will be automatically upgraded to new patch releases during its maintenance window.
    Boolean surgeUpgrade = false; // A boolean value indicating whether surge upgrade is enabled/disabled for the cluster. Surge upgrade makes cluster upgrades fast and reliable by bringing up new nodes before destroying the outdated nodes.
    Boolean ha = false; // A boolean value indicating whether the control plane is run in a highly available configuration in the cluster. Highly available control planes incur less downtime. The property cannot be disabled.
    try {
      KubernetesUpdateClusterResponse result = client
              .kubernetes
              .updateCluster(name, clusterId)
              .tags(tags)
              .maintenancePolicy(maintenancePolicy)
              .autoUpgrade(autoUpgrade)
              .surgeUpgrade(surgeUpgrade)
              .ha(ha)
              .execute();
      System.out.println(result);
      System.out.println(result.getKubernetesCluster());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#updateCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesUpdateClusterResponse> response = client
              .kubernetes
              .updateCluster(name, clusterId)
              .tags(tags)
              .maintenancePolicy(maintenancePolicy)
              .autoUpgrade(autoUpgrade)
              .surgeUpgrade(surgeUpgrade)
              .ha(ha)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#updateCluster");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **clusterUpdate** | [**ClusterUpdate**](ClusterUpdate.md)|  | |

### Return type

[**KubernetesUpdateClusterResponse**](KubernetesUpdateClusterResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The response will be a JSON object with a key called &#x60;kubernetes_cluster&#x60;. The value of this will be an object containing the standard attributes of a Kubernetes cluster.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="updateNodePool"></a>
# **updateNodePool**
> KubernetesUpdateNodePoolResponse updateNodePool(clusterId, nodePoolId, kubernetesNodePoolBase).execute();

Update a Node Pool in a Kubernetes Cluster

To update the name of a node pool, edit the tags applied to it, or adjust its number of nodes, send a PUT request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/node_pools/$NODE_POOL_ID&#x60; with the following attributes. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    UUID nodePoolId = UUID.fromString("cdda885e-7663-40c8-bc74-3a036c66545d"); // A unique ID that can be used to reference a Kubernetes node pool.
    List<String> tags = Arrays.asList(); // An array containing the tags applied to the node pool. All node pools are automatically tagged `k8s`, `k8s-worker`, and `k8s:$K8S_CLUSTER_ID`.
    UUID id = UUID.randomUUID(); // A unique ID that can be used to identify and reference a specific node pool.
    String name = "name_example"; // A human-readable name for the node pool.
    Integer count = 56; // The number of Droplet instances in the node pool.
    Object labels = null; // An object of key/value mappings specifying labels to apply to all nodes in a pool. Labels will automatically be applied to all existing nodes and any subsequent nodes added to the pool. Note that when a label is removed, it is not deleted from the nodes in the pool.
    List<KubernetesNodePoolTaint> taints = Arrays.asList(); // An array of taints to apply to all nodes in a pool. Taints will automatically be applied to all existing nodes and any subsequent nodes added to the pool. When a taint is removed, it is deleted from all nodes in the pool.
    Boolean autoScale = true; // A boolean value indicating whether auto-scaling is enabled for this node pool.
    Integer minNodes = 56; // The minimum number of nodes that this node pool can be auto-scaled to. The value will be `0` if `auto_scale` is set to `false`.
    Integer maxNodes = 56; // The maximum number of nodes that this node pool can be auto-scaled to. The value will be `0` if `auto_scale` is set to `false`.
    List<Node> nodes = Arrays.asList(); // An object specifying the details of a specific worker node in a node pool.
    try {
      KubernetesUpdateNodePoolResponse result = client
              .kubernetes
              .updateNodePool(clusterId, nodePoolId)
              .tags(tags)
              .id(id)
              .name(name)
              .count(count)
              .labels(labels)
              .taints(taints)
              .autoScale(autoScale)
              .minNodes(minNodes)
              .maxNodes(maxNodes)
              .nodes(nodes)
              .execute();
      System.out.println(result);
      System.out.println(result.getNodePool());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#updateNodePool");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      ApiResponse<KubernetesUpdateNodePoolResponse> response = client
              .kubernetes
              .updateNodePool(clusterId, nodePoolId)
              .tags(tags)
              .id(id)
              .name(name)
              .count(count)
              .labels(labels)
              .taints(taints)
              .autoScale(autoScale)
              .minNodes(minNodes)
              .maxNodes(maxNodes)
              .nodes(nodes)
              .executeWithHttpInfo();
      System.out.println(response.getResponseBody());
      System.out.println(response.getResponseHeaders());
      System.out.println(response.getStatusCode());
      System.out.println(response.getRoundTripTime());
      System.out.println(response.getRequest());
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#updateNodePool");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **nodePoolId** | **UUID**| A unique ID that can be used to reference a Kubernetes node pool. | |
| **kubernetesNodePoolBase** | [**KubernetesNodePoolBase**](KubernetesNodePoolBase.md)|  | |

### Return type

[**KubernetesUpdateNodePoolResponse**](KubernetesUpdateNodePoolResponse.md)

### Authorization

[bearer_auth](../README.md#bearer_auth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **202** | The response will be a JSON object with a key called &#x60;node_pool&#x60;. The value of this will be an object containing the standard attributes of a node pool.  |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |
| **0** | Unexpected error |  * ratelimit-limit -  <br>  * ratelimit-remaining -  <br>  * ratelimit-reset -  <br>  |

<a name="upgradeCluster"></a>
# **upgradeCluster**
> upgradeCluster(clusterId, kubernetesUpgradeClusterRequest).execute();

Upgrade a Kubernetes Cluster

To immediately upgrade a Kubernetes cluster to a newer patch release of Kubernetes, send a POST request to &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/upgrade&#x60;. The body of the request must specify a version attribute.  Available upgrade versions for a cluster can be fetched from &#x60;/v2/kubernetes/clusters/$K8S_CLUSTER_ID/upgrades&#x60;. 

### Example
```java
import com.konfigthis.client.ApiClient;
import com.konfigthis.client.ApiException;
import com.konfigthis.client.ApiResponse;
import com.konfigthis.client.DigitalOcean;
import com.konfigthis.client.Configuration;
import com.konfigthis.client.auth.*;
import com.konfigthis.client.model.*;
import com.konfigthis.client.api.KubernetesApi;
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
    UUID clusterId = UUID.fromString("bd5f5959-5e1e-4205-a714-a914373942af"); // A unique ID that can be used to reference a Kubernetes cluster.
    String version = "version_example"; // The slug identifier for the version of Kubernetes that the cluster will be upgraded to.
    try {
      client
              .kubernetes
              .upgradeCluster(clusterId)
              .version(version)
              .execute();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#upgradeCluster");
      System.err.println("Status code: " + e.getStatusCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }

    // Use .executeWithHttpInfo() to retrieve HTTP Status Code, Headers and Request
    try {
      client
              .kubernetes
              .upgradeCluster(clusterId)
              .version(version)
              .executeWithHttpInfo();
    } catch (ApiException e) {
      System.err.println("Exception when calling KubernetesApi#upgradeCluster");
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
| **clusterId** | **UUID**| A unique ID that can be used to reference a Kubernetes cluster. | |
| **kubernetesUpgradeClusterRequest** | [**KubernetesUpgradeClusterRequest**](KubernetesUpgradeClusterRequest.md)|  | |

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

