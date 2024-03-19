

# KafkaTopicPartition


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**size** | **Integer** | Size of the topic partition in bytes. |  [optional] |
|**id** | **Integer** | An identifier for the partition. |  [optional] |
|**inSyncReplicas** | **Integer** | The number of nodes that are in-sync (have the latest data) for the given partition |  [optional] |
|**earliestOffset** | **Integer** | The earliest consumer offset amongst consumer groups. |  [optional] |
|**consumerGroups** | [**List&lt;KafkaTopicPartitionConsumerGroupsInner&gt;**](KafkaTopicPartitionConsumerGroupsInner.md) |  |  [optional] |



