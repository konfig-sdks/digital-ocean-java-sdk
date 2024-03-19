

# LbFirewall

An object specifying allow and deny rules to control traffic to the load balancer.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**deny** | **List&lt;String&gt;** | the rules for denying traffic to the load balancer (in the form &#39;ip:1.2.3.4&#39; or &#39;cidr:1.2.0.0/16&#39;) |  [optional] |
|**allow** | **List&lt;String&gt;** | the rules for allowing traffic to the load balancer (in the form &#39;ip:1.2.3.4&#39; or &#39;cidr:1.2.0.0/16&#39;) |  [optional] |



