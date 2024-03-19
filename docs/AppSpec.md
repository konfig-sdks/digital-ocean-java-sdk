

# AppSpec

The desired configuration of an application.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name of the app. Must be unique across all apps in the same account. |  |
|**region** | [**RegionEnum**](#RegionEnum) | The slug form of the geographical origin of the app. Default: &#x60;nearest available&#x60; |  [optional] |
|**domains** | [**List&lt;AppDomainSpec&gt;**](AppDomainSpec.md) | A set of hostnames where the application will be available. |  [optional] |
|**services** | [**List&lt;AppServiceSpec&gt;**](AppServiceSpec.md) | Workloads which expose publicly-accessible HTTP services. |  [optional] |
|**staticSites** | [**List&lt;AppStaticSiteSpec&gt;**](AppStaticSiteSpec.md) | Content which can be rendered to static web assets. |  [optional] |
|**jobs** | [**List&lt;AppJobSpec&gt;**](AppJobSpec.md) | Pre and post deployment workloads which do not expose publicly-accessible HTTP routes. |  [optional] |
|**workers** | [**List&lt;AppWorkerSpec&gt;**](AppWorkerSpec.md) | Workloads which do not expose publicly-accessible HTTP services. |  [optional] |
|**functions** | [**List&lt;AppFunctionsSpec&gt;**](AppFunctionsSpec.md) | Workloads which expose publicly-accessible HTTP services via Functions Components. |  [optional] |
|**databases** | [**List&lt;AppDatabaseSpec&gt;**](AppDatabaseSpec.md) | Database instances which can provide persistence to workloads within the application. |  [optional] |
|**ingress** | [**AppIngressSpec**](AppIngressSpec.md) |  |  [optional] |



## Enum: RegionEnum

| Name | Value |
|---- | -----|
| AMS | &quot;ams&quot; |
| NYC | &quot;nyc&quot; |
| FRA | &quot;fra&quot; |
| SFO | &quot;sfo&quot; |
| SGP | &quot;sgp&quot; |
| BLR | &quot;blr&quot; |
| TOR | &quot;tor&quot; |
| LON | &quot;lon&quot; |
| SYD | &quot;syd&quot; |



