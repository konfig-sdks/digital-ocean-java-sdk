

# KubernetesNodePoolTaint


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**key** | **String** | An arbitrary string. The &#x60;key&#x60; and &#x60;value&#x60; fields of the &#x60;taint&#x60; object form a key-value pair. For example, if the value of the &#x60;key&#x60; field is \&quot;special\&quot; and the value of the &#x60;value&#x60; field is \&quot;gpu\&quot;, the key value pair would be &#x60;special&#x3D;gpu&#x60;. |  [optional] |
|**value** | **String** | An arbitrary string. The &#x60;key&#x60; and &#x60;value&#x60; fields of the &#x60;taint&#x60; object form a key-value pair. For example, if the value of the &#x60;key&#x60; field is \&quot;special\&quot; and the value of the &#x60;value&#x60; field is \&quot;gpu\&quot;, the key value pair would be &#x60;special&#x3D;gpu&#x60;. |  [optional] |
|**effect** | [**EffectEnum**](#EffectEnum) | How the node reacts to pods that it won&#39;t tolerate. Available effect values are &#x60;NoSchedule&#x60;, &#x60;PreferNoSchedule&#x60;, and &#x60;NoExecute&#x60;. |  [optional] |



## Enum: EffectEnum

| Name | Value |
|---- | -----|
| NOSCHEDULE | &quot;NoSchedule&quot; |
| PREFERNOSCHEDULE | &quot;PreferNoSchedule&quot; |
| NOEXECUTE | &quot;NoExecute&quot; |



