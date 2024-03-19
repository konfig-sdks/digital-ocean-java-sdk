

# Mysql


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**backupHour** | **Integer** | The hour of day (in UTC) when backup for the service starts. New backup only starts if previous backup has already completed. |  [optional] |
|**backupMinute** | **Integer** | The minute of the backup hour when backup for the service starts. New backup  only starts if previous backup has already completed. |  [optional] |
|**sqlMode** | **String** | Global SQL mode. If empty, uses MySQL server defaults. Must only include uppercase alphabetic characters, underscores, and commas. |  [optional] |
|**connectTimeout** | **Integer** | The number of seconds that the mysqld server waits for a connect packet before responding with bad handshake. |  [optional] |
|**defaultTimeZone** | **String** | Default server time zone, in the form of an offset from UTC (from -12:00 to +12:00), a time zone name (EST), or &#39;SYSTEM&#39; to use the MySQL server default. |  [optional] |
|**groupConcatMaxLen** | **Integer** | The maximum permitted result length, in bytes, for the GROUP_CONCAT() function. |  [optional] |
|**informationSchemaStatsExpiry** | **Integer** | The time, in seconds, before cached statistics expire. |  [optional] |
|**innodbFtMinTokenSize** | **Integer** | The minimum length of words that an InnoDB FULLTEXT index stores. |  [optional] |
|**innodbFtServerStopwordTable** | **String** | The InnoDB FULLTEXT index stopword list for all InnoDB tables. |  [optional] |
|**innodbLockWaitTimeout** | **Integer** | The time, in seconds, that an InnoDB transaction waits for a row lock. before giving up. |  [optional] |
|**innodbLogBufferSize** | **Integer** | The size of the buffer, in bytes, that InnoDB uses to write to the log files. on disk. |  [optional] |
|**innodbOnlineAlterLogMaxSize** | **Integer** | The upper limit, in bytes, of the size of the temporary log files used during online DDL operations for InnoDB tables. |  [optional] |
|**innodbPrintAllDeadlocks** | **Boolean** | When enabled, records information about all deadlocks in InnoDB user transactions  in the error log. Disabled by default. |  [optional] |
|**innodbRollbackOnTimeout** | **Boolean** | When enabled, transaction timeouts cause InnoDB to abort and roll back the entire transaction. |  [optional] |
|**interactiveTimeout** | **Integer** | The time, in seconds, the server waits for activity on an interactive. connection before closing it. |  [optional] |
|**internalTmpMemStorageEngine** | [**InternalTmpMemStorageEngineEnum**](#InternalTmpMemStorageEngineEnum) | The storage engine for in-memory internal temporary tables. |  [optional] |
|**netReadTimeout** | **Integer** | The time, in seconds, to wait for more data from an existing connection. aborting the read. |  [optional] |
|**netWriteTimeout** | **Integer** | The number of seconds to wait for a block to be written to a connection before aborting the write. |  [optional] |
|**sqlRequirePrimaryKey** | **Boolean** | Require primary key to be defined for new tables or old tables modified with ALTER TABLE and fail if missing. It is recommended to always have primary keys because various functionality may break if any large table is missing them. |  [optional] |
|**waitTimeout** | **Integer** | The number of seconds the server waits for activity on a noninteractive connection before closing it. |  [optional] |
|**maxAllowedPacket** | **Integer** | The size of the largest message, in bytes, that can be received by the server. Default is 67108864 (64M). |  [optional] |
|**maxHeapTableSize** | **Integer** | The maximum size, in bytes, of internal in-memory tables. Also set tmp_table_size. Default is 16777216 (16M) |  [optional] |
|**sortBufferSize** | **Integer** | The sort buffer size, in bytes, for ORDER BY optimization. Default is 262144. (256K). |  [optional] |
|**tmpTableSize** | **Integer** | The maximum size, in bytes, of internal in-memory tables. Also set max_heap_table_size. Default is 16777216 (16M). |  [optional] |
|**slowQueryLog** | **Boolean** | When enabled, captures slow queries. When disabled, also truncates the mysql.slow_log table. Default is false. |  [optional] |
|**longQueryTime** | **Double** | The time, in seconds, for a query to take to execute before  being captured by slow_query_logs. Default is 10 seconds. |  [optional] |
|**binlogRetentionPeriod** | **Double** | The minimum amount of time, in seconds, to keep binlog entries before deletion.  This may be extended for services that require binlog entries for longer than the default, for example if using the MySQL Debezium Kafka connector. |  [optional] |
|**innodbChangeBufferMaxSize** | **Integer** | Specifies the maximum size of the InnoDB change buffer as a percentage of the buffer pool. |  [optional] |
|**innodbFlushNeighbors** | [**InnodbFlushNeighborsEnum**](#InnodbFlushNeighborsEnum) | Specifies whether flushing a page from the InnoDB buffer pool also flushes other dirty pages in the same extent.   - 0 &amp;mdash; disables this functionality, dirty pages in the same extent are not flushed.   - 1 &amp;mdash; flushes contiguous dirty pages in the same extent.   - 2 &amp;mdash; flushes dirty pages in the same extent. |  [optional] |
|**innodbReadIoThreads** | **Integer** | The number of I/O threads for read operations in InnoDB. Changing this parameter will lead to a restart of the MySQL service. |  [optional] |
|**innodbWriteIoThreads** | **Integer** | The number of I/O threads for write operations in InnoDB. Changing this parameter will lead to a restart of the MySQL service. |  [optional] |
|**innodbThreadConcurrency** | **Integer** | Defines the maximum number of threads permitted inside of InnoDB. A value of 0 (the default) is interpreted as infinite concurrency (no limit). This variable is intended for performance  tuning on high concurrency systems. |  [optional] |
|**netBufferLength** | **Integer** | Start sizes of connection buffer and result buffer, must be multiple of 1024. Changing this parameter will lead to a restart of the MySQL service. |  [optional] |
|**autovacuumFreezeMaxAge** | **Integer** | Specifies the maximum age (in transactions) that a table&#39;s pg_class.relfrozenxid field can attain before a VACUUM operation is forced to prevent transaction ID wraparound within the table. Note that the system will launch autovacuum processes to prevent wraparound even when autovacuum is otherwise disabled. This parameter will cause the server to be restarted. |  [optional] |
|**autovacuumMaxWorkers** | **Integer** | Specifies the maximum number of autovacuum processes (other than the autovacuum launcher) that may be running at any one time. The default is three. This parameter can only be set at server start. |  [optional] |
|**autovacuumNaptime** | **Integer** | Specifies the minimum delay, in seconds, between autovacuum runs on any given database. The default is one minute. |  [optional] |
|**autovacuumVacuumThreshold** | **Integer** | Specifies the minimum number of updated or deleted tuples needed to trigger a VACUUM in any one table. The default is 50 tuples. |  [optional] |
|**autovacuumAnalyzeThreshold** | **Integer** | Specifies the minimum number of inserted, updated, or deleted tuples needed to trigger an ANALYZE in any one table. The default is 50 tuples. |  [optional] |
|**autovacuumVacuumScaleFactor** | **Double** | Specifies a fraction, in a decimal value, of the table size to add to autovacuum_vacuum_threshold when deciding whether to trigger a VACUUM. The default is 0.2 (20% of table size). |  [optional] |
|**autovacuumAnalyzeScaleFactor** | **Double** | Specifies a fraction, in a decimal value, of the table size to add to autovacuum_analyze_threshold when deciding whether to trigger an ANALYZE. The default is 0.2 (20% of table size). |  [optional] |
|**autovacuumVacuumCostDelay** | **Integer** | Specifies the cost delay value, in milliseconds, that will be used in automatic VACUUM operations. If -1, uses the regular vacuum_cost_delay value, which is 20 milliseconds. |  [optional] |
|**autovacuumVacuumCostLimit** | **Integer** | Specifies the cost limit value that will be used in automatic VACUUM operations. If -1 is specified (which is the default), the regular vacuum_cost_limit value will be used. |  [optional] |
|**bgwriterDelay** | **Integer** | Specifies the delay, in milliseconds, between activity rounds for the background writer. Default is 200 ms. |  [optional] |
|**bgwriterFlushAfter** | **Integer** | The amount of kilobytes that need to be written by the background writer before attempting to force the OS to issue these writes to underlying storage. Specified in kilobytes, default is 512.  Setting of 0 disables forced writeback. |  [optional] |
|**bgwriterLruMaxpages** | **Integer** | The maximum number of buffers that the background writer can write. Setting this to zero disables background writing. Default is 100. |  [optional] |
|**bgwriterLruMultiplier** | **Double** | The average recent need for new buffers is multiplied by bgwriter_lru_multiplier to arrive at an estimate of the number that will be needed during the next round, (up to bgwriter_lru_maxpages). 1.0 represents a “just in time” policy of writing exactly the number of buffers predicted to be needed. Larger values provide some cushion against spikes in demand, while smaller values intentionally leave writes to be done by server processes. The default is 2.0. |  [optional] |
|**deadlockTimeout** | **Integer** | The amount of time, in milliseconds, to wait on a lock before checking to see if there is a deadlock condition. |  [optional] |
|**defaultToastCompression** | [**DefaultToastCompressionEnum**](#DefaultToastCompressionEnum) | Specifies the default TOAST compression method for values of compressible columns (the default is lz4). |  [optional] |
|**idleInTransactionSessionTimeout** | **Integer** | Time out sessions with open transactions after this number of milliseconds |  [optional] |
|**jit** | **Boolean** | Activates, in a boolean, the system-wide use of Just-in-Time Compilation (JIT). |  [optional] |
|**logAutovacuumMinDuration** | **Integer** | Causes each action executed by autovacuum to be logged if it ran for at least the specified number of milliseconds. Setting this to zero logs all autovacuum actions. Minus-one (the default) disables logging autovacuum actions. |  [optional] |
|**logErrorVerbosity** | [**LogErrorVerbosityEnum**](#LogErrorVerbosityEnum) | Controls the amount of detail written in the server log for each message that is logged. |  [optional] |
|**logLinePrefix** | [**LogLinePrefixEnum**](#LogLinePrefixEnum) | Selects one of the available log-formats. These can support popular log analyzers like pgbadger, pganalyze, etc. |  [optional] |
|**logMinDurationStatement** | **Integer** | Log statements that take more than this number of milliseconds to run. If -1, disables. |  [optional] |
|**maxFilesPerProcess** | **Integer** | PostgreSQL maximum number of files that can be open per process. |  [optional] |
|**maxPreparedTransactions** | **Integer** | PostgreSQL maximum prepared transactions. Once increased, this parameter cannot be lowered from its set value. |  [optional] |
|**maxPredLocksPerTransaction** | **Integer** | PostgreSQL maximum predicate locks per transaction. |  [optional] |
|**maxLocksPerTransaction** | **Integer** | PostgreSQL maximum locks per transaction. Once increased, this parameter cannot be lowered from its set value. |  [optional] |
|**maxStackDepth** | **Integer** | Maximum depth of the stack in bytes. |  [optional] |
|**maxStandbyArchiveDelay** | **Integer** | Max standby archive delay in milliseconds. |  [optional] |
|**maxStandbyStreamingDelay** | **Integer** | Max standby streaming delay in milliseconds. |  [optional] |
|**maxReplicationSlots** | **Integer** | PostgreSQL maximum replication slots. |  [optional] |
|**maxLogicalReplicationWorkers** | **Integer** | PostgreSQL maximum logical replication workers (taken from the pool of max_parallel_workers). |  [optional] |
|**maxParallelWorkers** | **Integer** | Sets the maximum number of workers that the system can support for parallel queries. |  [optional] |
|**maxParallelWorkersPerGather** | **Integer** | Sets the maximum number of workers that can be started by a single Gather or Gather Merge node. |  [optional] |
|**maxWorkerProcesses** | **Integer** | Sets the maximum number of background processes that the system can support. Once increased, this parameter cannot be lowered from its set value. |  [optional] |
|**pgPartmanBgwRole** | **String** | Controls which role to use for pg_partman&#39;s scheduled background tasks. Must consist of alpha-numeric characters, dots, underscores, or dashes. May not start with dash or dot. Maximum of 64 characters. |  [optional] |
|**pgPartmanBgwInterval** | **Integer** | Sets the time interval to run pg_partman&#39;s scheduled tasks. |  [optional] |
|**pgStatStatementsTrack** | [**PgStatStatementsTrackEnum**](#PgStatStatementsTrackEnum) | Controls which statements are counted. Specify &#39;top&#39; to track top-level statements (those issued directly by clients), &#39;all&#39; to also track nested statements (such as statements invoked within functions), or &#39;none&#39; to disable statement statistics collection. The default value is top. |  [optional] |
|**tempFileLimit** | **Integer** | PostgreSQL temporary file limit in KiB. If -1, sets to unlimited. |  [optional] |
|**timezone** | **String** | PostgreSQL service timezone |  [optional] |
|**trackActivityQuerySize** | **Integer** | Specifies the number of bytes reserved to track the currently executing command for each active session. |  [optional] |
|**trackCommitTimestamp** | [**TrackCommitTimestampEnum**](#TrackCommitTimestampEnum) | Record commit time of transactions. |  [optional] |
|**trackFunctions** | [**TrackFunctionsEnum**](#TrackFunctionsEnum) | Enables tracking of function call counts and time used. |  [optional] |
|**trackIoTiming** | [**TrackIoTimingEnum**](#TrackIoTimingEnum) | Enables timing of database I/O calls. This parameter is off by default, because it will repeatedly query the operating system for the current time, which may cause significant overhead on some platforms. |  [optional] |
|**maxWalSenders** | **Integer** | PostgreSQL maximum WAL senders. Once increased, this parameter cannot be lowered from its set value. |  [optional] |
|**walSenderTimeout** | **Integer** | Terminate replication connections that are inactive for longer than this amount of time, in milliseconds. Setting this value to zero disables the timeout. Must be either 0 or between 5000 and 10800000. |  [optional] |
|**walWriterDelay** | **Integer** | WAL flush interval in milliseconds. Note that setting this value to lower than the default 200ms may negatively impact performance |  [optional] |
|**sharedBuffersPercentage** | **Double** | Percentage of total RAM that the database server uses for shared memory buffers.  Valid range is 20-60 (float), which corresponds to 20% - 60%.  This setting adjusts the shared_buffers configuration value. |  [optional] |
|**pgbouncer** | [**MysqlPgbouncer**](MysqlPgbouncer.md) |  |  [optional] |
|**workMem** | **Integer** | The maximum amount of memory, in MB, used by a query operation (such as a sort or hash table) before writing to temporary disk files. Default is 1MB + 0.075% of total RAM (up to 32MB). |  [optional] |
|**timescaledb** | [**MysqlTimescaledb**](MysqlTimescaledb.md) |  |  [optional] |
|**synchronousReplication** | [**SynchronousReplicationEnum**](#SynchronousReplicationEnum) | Synchronous replication type. Note that the service plan also needs to support synchronous replication. |  [optional] |
|**statMonitorEnable** | **Boolean** | Enable the pg_stat_monitor extension. &lt;b&gt;Enabling this extension will cause the cluster to be restarted.&lt;/b&gt; When this extension is enabled, pg_stat_statements results for utility commands are unreliable. |  [optional] |
|**redisMaxmemoryPolicy** | [**RedisMaxmemoryPolicyEnum**](#RedisMaxmemoryPolicyEnum) | A string specifying the desired eviction policy for the Redis cluster.  - &#x60;noeviction&#x60;: Don&#39;t evict any data, returns error when memory limit is reached. - &#x60;allkeys_lru:&#x60; Evict any key, least recently used (LRU) first. - &#x60;allkeys_random&#x60;: Evict keys in a random order. - &#x60;volatile_lru&#x60;: Evict keys with expiration only, least recently used (LRU) first. - &#x60;volatile_random&#x60;: Evict keys with expiration only in a random order. - &#x60;volatile_ttl&#x60;: Evict keys with expiration only, shortest time-to-live (TTL) first. |  [optional] |
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



## Enum: InternalTmpMemStorageEngineEnum

| Name | Value |
|---- | -----|
| TEMPTABLE | &quot;TempTable&quot; |
| MEMORY | &quot;MEMORY&quot; |



## Enum: InnodbFlushNeighborsEnum

| Name | Value |
|---- | -----|
| NUMBER_0 | 0 |
| NUMBER_1 | 1 |
| NUMBER_2 | 2 |



## Enum: DefaultToastCompressionEnum

| Name | Value |
|---- | -----|
| LZ4 | &quot;lz4&quot; |
| PGLZ | &quot;pglz&quot; |



## Enum: LogErrorVerbosityEnum

| Name | Value |
|---- | -----|
| TERSE | &quot;TERSE&quot; |
| DEFAULT | &quot;DEFAULT&quot; |
| VERBOSE | &quot;VERBOSE&quot; |



## Enum: LogLinePrefixEnum

| Name | Value |
|---- | -----|
| PID_P_USER_U_DB_D_APP_A_CLIENT_H | &quot;pid&#x3D;%p,user&#x3D;%u,db&#x3D;%d,app&#x3D;%a,client&#x3D;%h&quot; |
| _M_P_Q_USER_U_DB_D_APP_A_ | &quot;%m [%p] %q[user&#x3D;%u,db&#x3D;%d,app&#x3D;%a]&quot; |
| _T_P_L_1_USER_U_DB_D_APP_A_CLIENT_H | &quot;%t [%p]: [%l-1] user&#x3D;%u,db&#x3D;%d,app&#x3D;%a,client&#x3D;%h&quot; |



## Enum: PgStatStatementsTrackEnum

| Name | Value |
|---- | -----|
| ALL | &quot;all&quot; |
| TOP | &quot;top&quot; |
| NONE | &quot;none&quot; |



## Enum: TrackCommitTimestampEnum

| Name | Value |
|---- | -----|
| FALSE | &quot;false&quot; |
| TRUE | &quot;true&quot; |



## Enum: TrackFunctionsEnum

| Name | Value |
|---- | -----|
| ALL | &quot;all&quot; |
| PL | &quot;pl&quot; |
| NONE | &quot;none&quot; |



## Enum: TrackIoTimingEnum

| Name | Value |
|---- | -----|
| FALSE | &quot;false&quot; |
| TRUE | &quot;true&quot; |



## Enum: SynchronousReplicationEnum

| Name | Value |
|---- | -----|
| FALSE | &quot;false&quot; |
| QUORUM | &quot;quorum&quot; |



## Enum: RedisMaxmemoryPolicyEnum

| Name | Value |
|---- | -----|
| NOEVICTION | &quot;noeviction&quot; |
| ALLKEYS_LRU | &quot;allkeys_lru&quot; |
| ALLKEYS_RANDOM | &quot;allkeys_random&quot; |
| VOLATILE_LRU | &quot;volatile_lru&quot; |
| VOLATILE_RANDOM | &quot;volatile_random&quot; |
| VOLATILE_TTL | &quot;volatile_ttl&quot; |



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



