

# KafkaTopic


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name of the Kafka topic. |  [optional] |
|**replicationFactor** | **Integer** | The number of nodes to replicate data across the cluster. |  [optional] |
|**partitionCount** | **Integer** | The number of partitions available for the topic. On update, this value can only be increased. |  [optional] |
|**state** | [**StateEnum**](#StateEnum) | The state of the Kafka topic. |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;active&quot; |
| CONFIGURING | &quot;configuring&quot; |
| DELETING | &quot;deleting&quot; |
| UNKNOWN | &quot;unknown&quot; |



