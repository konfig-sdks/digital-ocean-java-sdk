

# DropletNetworks

The details of the network that are configured for the Droplet instance.  This is an object that contains keys for IPv4 and IPv6.  The value of each of these is an array that contains objects describing an individual IP resource allocated to the Droplet.  These will define attributes like the IP address, netmask, and gateway of the specific network depending on the type of network it is.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**v4** | [**List&lt;NetworkV4&gt;**](NetworkV4.md) |  |  [optional] |
|**v6** | [**List&lt;NetworkV6&gt;**](NetworkV6.md) |  |  [optional] |



