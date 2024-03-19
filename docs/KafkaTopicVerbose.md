

# KafkaTopicVerbose


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name of the Kafka topic. |  [optional] |
|**state** | [**StateEnum**](#StateEnum) | The state of the Kafka topic. |  [optional] |
|**replicationFactor** | **Integer** | The number of nodes to replicate data across the cluster. |  [optional] |
|**partitions** | [**List&lt;KafkaTopicPartition&gt;**](KafkaTopicPartition.md) |  |  [optional] |
|**config** | [**KafkaTopicConfig**](KafkaTopicConfig.md) |  |  [optional] |



## Enum: StateEnum

| Name | Value |
|---- | -----|
| ACTIVE | &quot;active&quot; |
| CONFIGURING | &quot;configuring&quot; |
| DELETING | &quot;deleting&quot; |
| UNKNOWN | &quot;unknown&quot; |



