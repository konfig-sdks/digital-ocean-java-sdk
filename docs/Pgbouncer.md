

# Pgbouncer

PGBouncer connection pooling settings

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**serverResetQueryAlways** | **Boolean** | Run server_reset_query (DISCARD ALL) in all pooling modes. |  [optional] |
|**ignoreStartupParameters** | [**List&lt;IgnoreStartupParametersEnum&gt;**](#List&lt;IgnoreStartupParametersEnum&gt;) | List of parameters to ignore when given in startup packet. |  [optional] |
|**minPoolSize** | **Integer** | If current server connections are below this number, adds more. Improves behavior when usual load comes suddenly back after period of total inactivity. The value is effectively capped at the pool size. |  [optional] |
|**serverLifetime** | **Integer** | The pooler closes any unused server connection that has been connected longer than this amount of seconds. |  [optional] |
|**serverIdleTimeout** | **Integer** | Drops server connections if they have been idle more than this many seconds.  If 0, timeout is disabled.  |  [optional] |
|**autodbPoolSize** | **Integer** | If non-zero, automatically creates a pool of that size per user when a pool doesn&#39;t exist. |  [optional] |
|**autodbPoolMode** | [**AutodbPoolModeEnum**](#AutodbPoolModeEnum) | PGBouncer pool mode |  [optional] |
|**autodbMaxDbConnections** | **Integer** | Only allows a maximum this many server connections per database (regardless of user). If 0, allows unlimited connections. |  [optional] |
|**autodbIdleTimeout** | **Integer** | If the automatically-created database pools have been unused this many seconds, they are freed. If 0, timeout is disabled. |  [optional] |



## Enum: List&lt;IgnoreStartupParametersEnum&gt;

| Name | Value |
|---- | -----|
| EXTRA_FLOAT_DIGITS | &quot;extra_float_digits&quot; |
| SEARCH_PATH | &quot;search_path&quot; |



## Enum: AutodbPoolModeEnum

| Name | Value |
|---- | -----|
| SESSION | &quot;session&quot; |
| TRANSACTION | &quot;transaction&quot; |
| STATEMENT | &quot;statement&quot; |



