

# KafkaTopicConfig


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**cleanupPolicy** | [**CleanupPolicyEnum**](#CleanupPolicyEnum) | The cleanup_policy sets the retention policy to use on log segments. &#39;delete&#39; will discard old segments when retention time/size limits are reached. &#39;compact&#39; will enable log compaction, resulting in retention of the latest value for each key. |  [optional] |
|**compressionType** | [**CompressionTypeEnum**](#CompressionTypeEnum) | The compression_type specifies the compression type of the topic. |  [optional] |
|**deleteRetentionMs** | **Integer** | The delete_retention_ms specifies how long (in ms) to retain delete tombstone markers for topics. |  [optional] |
|**fileDeleteDelayMs** | **Integer** | The file_delete_delay_ms specifies the time (in ms) to wait before deleting a file from the filesystem. |  [optional] |
|**flushMessages** | **Integer** | The flush_messages specifies the number of messages to accumulate on a log partition before messages are flushed to disk. |  [optional] |
|**flushMs** | **Integer** | The flush_ms specifies the maximum time (in ms) that a message is kept in memory before being flushed to disk. |  [optional] |
|**indexIntervalBytes** | **Integer** | The index_interval_bytes specifies the number of bytes between entries being added into te offset index. |  [optional] |
|**maxCompactionLagMs** | **Integer** | The max_compaction_lag_ms specifies the maximum amount of time (in ms) that a message will remain uncompacted. This is only applicable if the logs are have compaction enabled. |  [optional] |
|**maxMessageBytes** | **Integer** | The max_messages_bytes specifies the largest record batch size (in bytes) that can be sent to the server.  This is calculated after compression if compression is enabled. |  [optional] |
|**messageDownConversionEnable** | **Boolean** | The message_down_conversion_enable specifies whether down-conversion of message formats is enabled to satisfy consumer requests. When &#39;false&#39;, the broker will not perform conversion for consumers expecting older message formats. The broker will respond with an &#x60;UNSUPPORTED_VERSION&#x60; error for consume requests from these older clients. |  [optional] |
|**messageFormatVersion** | [**MessageFormatVersionEnum**](#MessageFormatVersionEnum) | The message_format_version specifies the message format version used by the broker to append messages to the logs. The value of this setting is assumed to be 3.0-IV1 if the broker protocol version is 3.0 or higher. By setting a  particular message format version, all existing messages on disk must be smaller or equal to the specified version. |  [optional] |
|**messageTimestampType** | [**MessageTimestampTypeEnum**](#MessageTimestampTypeEnum) | The message_timestamp_type specifies whether to use the message create time or log append time as the timestamp on a message. |  [optional] |
|**minCleanableDirtyRatio** | **Float** | The min_cleanable_dirty_ratio specifies the frequency of log compaction (if enabled) in relation to duplicates present in the logs. For example, at 0.5, at most 50% of the log could be duplicates before compaction would begin. |  [optional] |
|**minCompactionLagMs** | **Integer** | The min_compaction_lag_ms specifies the minimum time (in ms) that a message will remain uncompacted in the log. Only relevant if log compaction is enabled. |  [optional] |
|**minInsyncReplicas** | **Integer** | The min_insync_replicas specifies the number of replicas that must ACK a write for the write to be considered successful. |  [optional] |
|**preallocate** | **Boolean** | The preallocate specifies whether a file should be preallocated on disk when creating a new log segment. |  [optional] |
|**retentionBytes** | **Integer** | The retention_bytes specifies the maximum size of the log (in bytes) before deleting messages. -1 indicates that there is no limit. |  [optional] |
|**retentionMs** | **Integer** | The retention_ms specifies the maximum amount of time (in ms) to keep a message before deleting it. |  [optional] |
|**segmentBytes** | **Integer** | The segment_bytes specifies the maximum size of a single log file (in bytes). |  [optional] |
|**segmentJitterMs** | **Integer** | The segment_jitter_ms specifies the maximum random jitter subtracted from the scheduled segment roll time to avoid thundering herds of segment rolling. |  [optional] |
|**segmentMs** | **Integer** | The segment_ms specifies the period of time after which the log will be forced to roll if the segment file isn&#39;t full. This ensures that retention can delete or compact old data. |  [optional] |



## Enum: CleanupPolicyEnum

| Name | Value |
|---- | -----|
| DELETE | &quot;delete&quot; |
| COMPACT | &quot;compact&quot; |
| COMPACT_DELETE | &quot;compact_delete&quot; |



## Enum: CompressionTypeEnum

| Name | Value |
|---- | -----|
| PRODUCER | &quot;producer&quot; |
| GZIP | &quot;gzip&quot; |
| SNAPPY | &quot;snappy&quot; |
| IZ4 | &quot;Iz4&quot; |
| ZSTD | &quot;zstd&quot; |
| UNCOMPRESSED | &quot;uncompressed&quot; |



## Enum: MessageFormatVersionEnum

| Name | Value |
|---- | -----|
| _0_8_0 | &quot;0.8.0&quot; |
| _0_8_1 | &quot;0.8.1&quot; |
| _0_8_2 | &quot;0.8.2&quot; |
| _0_9_0 | &quot;0.9.0&quot; |
| _0_10_0_IV0 | &quot;0.10.0-IV0&quot; |
| _0_10_0_IV1 | &quot;0.10.0-IV1&quot; |
| _0_10_1_IV0 | &quot;0.10.1-IV0&quot; |
| _0_10_1_IV1 | &quot;0.10.1-IV1&quot; |
| _0_10_1_IV2 | &quot;0.10.1-IV2&quot; |
| _0_10_2_IV0 | &quot;0.10.2-IV0&quot; |
| _0_11_0_IV0 | &quot;0.11.0-IV0&quot; |
| _0_11_0_IV1 | &quot;0.11.0-IV1&quot; |
| _0_11_0_IV2 | &quot;0.11.0-IV2&quot; |
| _1_0_IV0 | &quot;1.0-IV0&quot; |
| _1_1_IV0 | &quot;1.1-IV0&quot; |
| _2_0_IV0 | &quot;2.0-IV0&quot; |
| _2_0_IV1 | &quot;2.0-IV1&quot; |
| _2_1_IV0 | &quot;2.1-IV0&quot; |
| _2_1_IV1 | &quot;2.1-IV1&quot; |
| _2_1_IV2 | &quot;2.1-IV2&quot; |
| _2_2_IV0 | &quot;2.2-IV0&quot; |
| _2_2_IV1 | &quot;2.2-IV1&quot; |
| _2_3_IV0 | &quot;2.3-IV0&quot; |
| _2_3_IV1 | &quot;2.3-IV1&quot; |
| _2_4_IV0 | &quot;2.4-IV0&quot; |
| _2_4_IV1 | &quot;2.4-IV1&quot; |
| _2_5_IV0 | &quot;2.5-IV0&quot; |
| _2_6_IV0 | &quot;2.6-IV0&quot; |
| _2_7_IV0 | &quot;2.7-IV0&quot; |
| _2_7_IV1 | &quot;2.7-IV1&quot; |
| _2_7_IV2 | &quot;2.7-IV2&quot; |
| _2_8_IV0 | &quot;2.8-IV0&quot; |
| _2_8_IV1 | &quot;2.8-IV1&quot; |
| _3_0_IV0 | &quot;3.0-IV0&quot; |
| _3_0_IV1 | &quot;3.0-IV1&quot; |
| _3_1_IV0 | &quot;3.1-IV0&quot; |
| _3_2_IV0 | &quot;3.2-IV0&quot; |
| _3_3_IV0 | &quot;3.3-IV0&quot; |
| _3_3_IV1 | &quot;3.3-IV1&quot; |
| _3_3_IV2 | &quot;3.3-IV2&quot; |
| _3_3_IV3 | &quot;3.3-IV3&quot; |



## Enum: MessageTimestampTypeEnum

| Name | Value |
|---- | -----|
| CREATE_TIME | &quot;create_time&quot; |
| LOG_APPEND_TIME | &quot;log_append_time&quot; |



