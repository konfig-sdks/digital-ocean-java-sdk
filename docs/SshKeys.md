

# SshKeys


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**id** | **Integer** | A unique identification number for this key. Can be used to embed a  specific SSH key into a Droplet. |  [optional] [readonly] |
|**fingerprint** | **String** | A unique identifier that differentiates this key from other keys using  a format that SSH recognizes. The fingerprint is created when the key is added to your account. |  [optional] [readonly] |
|**publicKey** | **String** | The entire public key string that was uploaded. Embedded into the root user&#39;s &#x60;authorized_keys&#x60; file if you include this key during Droplet creation. |  |
|**name** | **String** | A human-readable display name for this key, used to easily identify the SSH keys when they are displayed. |  |



