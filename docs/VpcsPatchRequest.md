

# VpcsPatchRequest


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | A free-form text field for describing the VPC&#39;s purpose. It may be a maximum of 255 characters. |  [optional] |
|**name** | **String** | The name of the VPC. Must be unique and may only contain alphanumeric characters, dashes, and periods. |  [optional] |
|**_default** | **Boolean** | A boolean value indicating whether or not the VPC is the default network for the region. All applicable resources are placed into the default VPC network unless otherwise specified during their creation. The &#x60;default&#x60; field cannot be unset from &#x60;true&#x60;. If you want to set a new default VPC network, update the &#x60;default&#x60; field of another VPC network in the same region. The previous network&#39;s &#x60;default&#x60; field will be set to &#x60;false&#x60; when a new default VPC has been defined. |  [optional] |



