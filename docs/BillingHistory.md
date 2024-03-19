

# BillingHistory


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**description** | **String** | Description of the billing history entry. |  [optional] |
|**amount** | **String** | Amount of the billing history entry. |  [optional] |
|**invoiceId** | **String** | ID of the invoice associated with the billing history entry, if  applicable. |  [optional] |
|**invoiceUuid** | **String** | UUID of the invoice associated with the billing history entry, if  applicable. |  [optional] |
|**date** | **OffsetDateTime** | Time the billing history entry occurred. |  [optional] |
|**type** | [**TypeEnum**](#TypeEnum) | Type of billing history entry. |  [optional] |



## Enum: TypeEnum

| Name | Value |
|---- | -----|
| ACHFAILURE | &quot;ACHFailure&quot; |
| ADJUSTMENT | &quot;Adjustment&quot; |
| ATTEMPTFAILED | &quot;AttemptFailed&quot; |
| CHARGEBACK | &quot;Chargeback&quot; |
| CREDIT | &quot;Credit&quot; |
| CREDITEXPIRATION | &quot;CreditExpiration&quot; |
| INVOICE | &quot;Invoice&quot; |
| PAYMENT | &quot;Payment&quot; |
| REFUND | &quot;Refund&quot; |
| REVERSAL | &quot;Reversal&quot; |



