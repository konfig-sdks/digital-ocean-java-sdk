

# Redis


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**redisMaxmemoryPolicy** | **EvictionPolicyModel** |  |  [optional] |
|**redisPubsubClientOutputBufferLimit** | **Integer** | Set output buffer limit for pub / sub clients in MB. The value is the hard limit, the soft limit is 1/4 of the hard limit. When setting the limit, be mindful of the available memory in the selected service plan. |  [optional] |
|**redisNumberOfDatabases** | **Integer** | Set number of redis databases. Changing this will cause a restart of redis service. |  [optional] |
|**redisIoThreads** | **Integer** | Redis IO thread count |  [optional] |
|**redisLfuLogFactor** | **Integer** | Counter logarithm factor for volatile-lfu and allkeys-lfu maxmemory-policies |  [optional] |
|**redisLfuDecayTime** | **Integer** | LFU maxmemory-policy counter decay time in minutes |  [optional] |
|**redisSsl** | **Boolean** | Require SSL to access Redis |  [optional] |
|**redisTimeout** | **Integer** | Redis idle connection timeout in seconds |  [optional] |
|**redisNotifyKeyspaceEvents** | **String** | Set notify-keyspace-events option. Requires at least &#x60;K&#x60; or &#x60;E&#x60; and accepts any combination of the following options. Setting the parameter to &#x60;\&quot;\&quot;&#x60; disables notifications. - &#x60;K&#x60; &amp;mdash; Keyspace events - &#x60;E&#x60; &amp;mdash; Keyevent events - &#x60;g&#x60; &amp;mdash; Generic commands (e.g. &#x60;DEL&#x60;, &#x60;EXPIRE&#x60;, &#x60;RENAME&#x60;, ...) - &#x60;$&#x60; &amp;mdash; String commands - &#x60;l&#x60; &amp;mdash; List commands - &#x60;s&#x60; &amp;mdash; Set commands - &#x60;h&#x60; &amp;mdash; Hash commands - &#x60;z&#x60; &amp;mdash; Sorted set commands - &#x60;t&#x60; &amp;mdash; Stream commands - &#x60;d&#x60; &amp;mdash; Module key type events - &#x60;x&#x60; &amp;mdash; Expired events - &#x60;e&#x60; &amp;mdash; Evicted events - &#x60;m&#x60; &amp;mdash; Key miss events - &#x60;n&#x60; &amp;mdash; New key events - &#x60;A&#x60; &amp;mdash; Alias for &#x60;\&quot;g$lshztxed\&quot;&#x60; |  [optional] |
|**redisPersistence** | [**RedisPersistenceEnum**](#RedisPersistenceEnum) | When persistence is &#39;rdb&#39;, Redis does RDB dumps each 10 minutes if any key is changed. Also RDB dumps are done according to backup schedule for backup purposes. When persistence is &#39;off&#39;, no RDB dumps and backups are done, so data can be lost at any moment if service is restarted for any reason, or if service is powered off. Also service can&#39;t be forked. |  [optional] |
|**redisAclChannelsDefault** | [**RedisAclChannelsDefaultEnum**](#RedisAclChannelsDefaultEnum) | Determines default pub/sub channels&#39; ACL for new users if ACL is not supplied. When this option is not defined, all_channels is assumed to keep backward compatibility. This option doesn&#39;t affect Redis configuration acl-pubsub-default. |  [optional] |



## Enum: RedisPersistenceEnum

| Name | Value |
|---- | -----|
| FALSE | &quot;false&quot; |
| RDB | &quot;rdb&quot; |



## Enum: RedisAclChannelsDefaultEnum

| Name | Value |
|---- | -----|
| ALLCHANNELS | &quot;allchannels&quot; |
| RESETCHANNELS | &quot;resetchannels&quot; |



