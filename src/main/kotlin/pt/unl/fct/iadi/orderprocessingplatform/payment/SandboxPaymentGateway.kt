package pt.unl.fct.iadi.orderprocessingplatform.payment

import pt.unl.fct.iadi.orderprocessingplatform.domain.PaymentRequest
import pt.unl.fct.iadi.orderprocessingplatform.domain.Receipt
import pt.unl.fct.iadi.orderprocessingplatform.domain.ReceiptStatus

class SandboxPaymentGateway : PaymentGateway {

    override fun processPayment(paymentRequest: PaymentRequest): Receipt {

        val metadata: MutableMap<String, Any> = HashMap<String, Any>()
        metadata["gateway"] = "sandbox"
        metadata["amount"] = paymentRequest.amount

        return Receipt(paymentRequest.orderId, ReceiptStatus.PAID, metadata)

    }
}