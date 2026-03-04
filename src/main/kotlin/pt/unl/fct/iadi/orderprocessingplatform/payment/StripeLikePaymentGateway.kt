package pt.unl.fct.iadi.orderprocessingplatform.payment

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.orderprocessingplatform.domain.PaymentRequest
import pt.unl.fct.iadi.orderprocessingplatform.domain.Receipt
import pt.unl.fct.iadi.orderprocessingplatform.domain.ReceiptStatus
import java.util.UUID.randomUUID

@Component
@Profile("prod")
class StripeLikePaymentGateway : PaymentGateway {

    override fun processPayment(paymentRequest: PaymentRequest): Receipt {

        val amount = paymentRequest.amount

        val (status, metadata) = if (amount <= 0.0) {
            ReceiptStatus.REJECTED to mapOf(
                "gateway" to "stripe-like",
                "reason" to "Invalid amount",
                "amount" to amount
            )
        } else if (amount > 10000.0) {
            ReceiptStatus.FLAGGED_FOR_REVIEW to mapOf(
                "gateway" to "stripe-like",
                "reason" to "High value transaction requires review",
                "amount" to amount
            )
        } else {
            ReceiptStatus.PAID to mapOf(
                "gateway" to "stripe-like",
                "transactionId" to randomUUID().toString(),
                "amount" to amount
            )
        }

        return Receipt(paymentRequest.orderId, status, metadata)
    }
}