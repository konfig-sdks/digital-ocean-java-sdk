

# Postgres


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**autovacuumFreezeMaxAge** | **Integer** | Specifies the maximum age (in transactions) that a table&#39;s pg_class.relfrozenxid field can attain before a VACUUM operation is forced to prevent transaction ID wraparound within the table. Note that the system will launch autovacuum processes to prevent wraparound even when autovacuum is otherwise disabled. This parameter will cause the server to be restarted. |  [optional] |
|**autovacuumMaxWorkers** | **Integer** | Specifies the maximum number of autovacuum processes (other than the autovacuum launcher) that may be running at any one time. The default is three. This parameter can only be set at server start. |  [optional] |
|**autovacuumNaptime** | **Integer** | Specifies the minimum delay, in seconds, between autovacuum runs on any given database. The default is one minute. |  [optional] |
|**autovacuumVacuumThreshold** | **Integer** | Specifies the minimum number of updated or deleted tuples needed to trigger a VACUUM in any one table. The default is 50 tuples. |  [optional] |
|**autovacuumAnalyzeThreshold** | **Integer** | Specifies the minimum number of inserted, updated, or deleted tuples needed to trigger an ANALYZE in any one table. The default is 50 tuples. |  [optional] |
|**autovacuumVacuumScaleFactor** | **Double** | Specifies a fraction, in a decimal value, of the table size to add to autovacuum_vacuum_threshold when deciding whether to trigger a VACUUM. The default is 0.2 (20% of table size). |  [optional] |
|**autovacuumAnalyzeScaleFactor** | **Double** | Specifies a fraction, in a decimal value, of the table size to add to autovacuum_analyze_threshold when deciding whether to trigger an ANALYZE. The default is 0.2 (20% of table size). |  [optional] |
|**autovacuumVacuumCostDelay** | **Integer** | Specifies the cost delay value, in milliseconds, that will be used in automatic VACUUM operations. If -1, uses the regular vacuum_cost_delay value, which is 20 milliseconds. |  [optional] |
|**autovacuumVacuumCostLimit** | **Integer** | Specifies the cost limit value that will be used in automatic VACUUM operations. If -1 is specified (which is the default), the regular vacuum_cost_limit value will be used. |  [optional] |
|**backupHour** | **Integer** | The hour of day (in UTC) when backup for the service starts. New backup only starts if previous backup has already completed. |  [optional] |
|**backupMinute** | **Integer** | The minute of the backup hour when backup for the service starts. New backup is only started if previous backup has already completed. |  [optional] |
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
|**pgbouncer** | [**Pgbouncer**](Pgbouncer.md) |  |  [optional] |
|**workMem** | **Integer** | The maximum amount of memory, in MB, used by a query operation (such as a sort or hash table) before writing to temporary disk files. Default is 1MB + 0.075% of total RAM (up to 32MB). |  [optional] |
|**timescaledb** | [**Timescaledb**](Timescaledb.md) |  |  [optional] |
|**synchronousReplication** | [**SynchronousReplicationEnum**](#SynchronousReplicationEnum) | Synchronous replication type. Note that the service plan also needs to support synchronous replication. |  [optional] |
|**statMonitorEnable** | **Boolean** | Enable the pg_stat_monitor extension. &lt;b&gt;Enabling this extension will cause the cluster to be restarted.&lt;/b&gt; When this extension is enabled, pg_stat_statements results for utility commands are unreliable. |  [optional] |



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



