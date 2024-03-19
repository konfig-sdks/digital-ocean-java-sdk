

# EventsLogs


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **String** | ID of the particular event. |  [optional] |
|**clusterName** | **String** | The name of cluster. |  [optional] |
|**eventType** | [**EventTypeEnum**](#EventTypeEnum) | Type of the event. |  [optional] |
|**createTime** | **String** | The time of the generation of a event. |  [optional] |



## Enum: EventTypeEnum

| Name | Value |
|---- | -----|
| MAINTENANCE_PERFORM | &quot;cluster_maintenance_perform&quot; |
| MASTER_PROMOTION | &quot;cluster_master_promotion&quot; |
| CREATE | &quot;cluster_create&quot; |
| UPDATE | &quot;cluster_update&quot; |
| DELETE | &quot;cluster_delete&quot; |
| POWERON | &quot;cluster_poweron&quot; |
| POWEROFF | &quot;cluster_poweroff&quot; |



