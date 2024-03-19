

# FloatingIp


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**ip** | **String** | The public IP address of the floating IP. It also serves as its identifier. |  [optional] |
|**region** | [**FloatingIpRegion**](FloatingIpRegion.md) |  |  [optional] |
|**droplet** | [**NullProperty**](NullProperty.md) |  |  [optional] |
|**locked** | **Boolean** | A boolean value indicating whether or not the floating IP has pending actions preventing new ones from being submitted. |  [optional] |
|**projectId** | **UUID** | The UUID of the project to which the reserved IP currently belongs. |  [optional] |



