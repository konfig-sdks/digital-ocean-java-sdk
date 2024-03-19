

# DropletMultiCreate


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**names** | **List&lt;String&gt;** | An array of human human-readable strings you wish to use when displaying the Droplet name. Each name, if set to a domain name managed in the DigitalOcean DNS management system, will configure a PTR record for the Droplet. Each name set during creation will also determine the hostname for the Droplet in its internal configuration. |  |
|**tags** | **List&lt;String&gt;** | A flat array of tag names as strings to apply to the Droplet after it is created. Tag names can either be existing or new tags. |  [optional] |
|**region** | **String** | The slug identifier for the region that you wish to deploy the Droplet in. If the specific datacenter is not not important, a slug prefix (e.g. &#x60;nyc&#x60;) can be used to deploy the Droplet in any of the that region&#39;s locations (&#x60;nyc1&#x60;, &#x60;nyc2&#x60;, or &#x60;nyc3&#x60;). If the region is omitted from the create request completely, the Droplet may deploy in any region. |  [optional] |
|**size** | **String** | The slug identifier for the size that you wish to select for this Droplet. |  |
|**image** | **Object** |  |  |
|**sshKeys** | **List&lt;Object&gt;** | An array containing the IDs or fingerprints of the SSH keys that you wish to embed in the Droplet&#39;s root account upon creation. |  [optional] |
|**backups** | **Boolean** | A boolean indicating whether automated backups should be enabled for the Droplet. |  [optional] |
|**ipv6** | **Boolean** | A boolean indicating whether to enable IPv6 on the Droplet. |  [optional] |
|**monitoring** | **Boolean** | A boolean indicating whether to install the DigitalOcean agent for monitoring. |  [optional] |
|**userData** | **String** | A string containing &#39;user data&#39; which may be used to configure the Droplet on first boot, often a &#39;cloud-config&#39; file or Bash script. It must be plain text and may not exceed 64 KiB in size. |  [optional] |
|**privateNetworking** | **Boolean** | This parameter has been deprecated. Use &#x60;vpc_uuid&#x60; instead to specify a VPC network for the Droplet. If no &#x60;vpc_uuid&#x60; is provided, the Droplet will be placed in your account&#39;s default VPC for the region. |  [optional] |
|**volumes** | **List&lt;String&gt;** | An array of IDs for block storage volumes that will be attached to the Droplet once created. The volumes must not already be attached to an existing Droplet. |  [optional] |
|**vpcUuid** | **String** | A string specifying the UUID of the VPC to which the Droplet will be assigned. If excluded, the Droplet will be assigned to your account&#39;s default VPC for the region. |  [optional] |
|**withDropletAgent** | **Boolean** | A boolean indicating whether to install the DigitalOcean agent used for providing access to the Droplet web console in the control panel. By default, the agent is installed on new Droplets but installation errors (i.e. OS not supported) are ignored. To prevent it from being installed, set to &#x60;false&#x60;. To make installation errors fatal, explicitly set it to &#x60;true&#x60;. |  [optional] |



