

# FirewallRuleTarget


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**tags** | [**List**](List.md) |  |  [optional] |
|**addresses** | **List&lt;String&gt;** | An array of strings containing the IPv4 addresses, IPv6 addresses, IPv4 CIDRs, and/or IPv6 CIDRs to which the firewall will allow traffic. |  [optional] |
|**dropletIds** | **List&lt;Integer&gt;** | An array containing the IDs of the Droplets to which the firewall will allow traffic. |  [optional] |
|**loadBalancerUids** | **List&lt;String&gt;** | An array containing the IDs of the load balancers to which the firewall will allow traffic. |  [optional] |
|**kubernetesIds** | **List&lt;String&gt;** | An array containing the IDs of the Kubernetes clusters to which the firewall will allow traffic. |  [optional] |



