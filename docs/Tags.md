

# Tags

A tag is a label that can be applied to a resource (currently Droplets, Images, Volumes, Volume Snapshots, and Database clusters) in order to better organize or facilitate the lookups and actions on it. Tags have two attributes: a user defined `name` attribute and an embedded `resources` attribute with information about resources that have been tagged.

## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**name** | **String** | The name of the tag. Tags may contain letters, numbers, colons, dashes, and underscores. There is a limit of 255 characters per tag.  **Note:** Tag names are case stable, which means the capitalization you use when you first create a tag is canonical.  When working with tags in the API, you must use the tag&#39;s canonical capitalization. For example, if you create a tag named \&quot;PROD\&quot;, the URL to add that tag to a resource would be &#x60;https://api.digitalocean.com/v2/tags/PROD/resources&#x60; (not &#x60;/v2/tags/prod/resources&#x60;).  Tagged resources in the control panel will always display the canonical capitalization. For example, if you create a tag named \&quot;PROD\&quot;, you can tag resources in the control panel by entering \&quot;prod\&quot;. The tag will still display with its canonical capitalization, \&quot;PROD\&quot;.  |  [optional] |
|**resources** | [**TagsResources**](TagsResources.md) |  |  [optional] |



