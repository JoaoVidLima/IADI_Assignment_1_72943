package pt.unl.fct.iadi.orderprocessingplatform

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.orderprocessingplatform.domain.Order
import pt.unl.fct.iadi.orderprocessingplatform.domain.PaymentRequest
import pt.unl.fct.iadi.orderprocessingplatform.domain.Receipt
import pt.unl.fct.iadi.orderprocessingplatform.payment.PaymentGateway
import pt.unl.fct.iadi.orderprocessingplatform.pricing.PriceCalculator
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Instant

@Component
class OrderProcessor (
    private val priceCalculator: PriceCalculator,
    private val paymentGateway: PaymentGateway
) : CommandLineRunner {


    override fun run(vararg args: String?) {

        val items = listOf(
            Order.OrderItem("PROD-1", 2, 10.0),
            Order.OrderItem("PROD-2", 6, 20.0), // Este deve ativar o desconto se o Promo estiver ligado!
            Order.OrderItem("PROD-3", 1, 50.0),
            Order.OrderItem("PROD-4", 1, 9999.0)
        )

        val order = Order("ORD-001", items, "user-42", Instant.now())
        val resultLines = processOrder(order)

        resultLines.forEach { line ->
            println(line)
        }

    }

    fun processOrder(order: Order): List<String> {

        val totalPrice: Double = priceCalculator.calculateTotalPrice(order)
        val calculatorName: String = priceCalculator.javaClass.simpleName
        val totalPriceRounded: Double = BigDecimal(totalPrice).setScale(2, RoundingMode.HALF_UP).toDouble()
        val receipt: Receipt = paymentGateway.processPayment(PaymentRequest(order.id, totalPriceRounded))

        val itemLines = order.items.map { item ->
            val itemTotal = item.quantity * item.price
            "  - ${item.productId}: ${item.quantity} x $${item.price} = $${String.format("%.2f", itemTotal)}"
        }

        return listOf(
                "Order ID: ${order.id}",
                "User ID: ${order.id}",
                "Created at: ${order.createdAt}",
                "",
                "Items:"
            ) + itemLines +
            listOfNotNull(
    "",
                "Total Price: $$totalPriceRounded",
                "Calculator Used: $calculatorName",
                "",
                "Payment Status: ${receipt.status}",
                (receipt.metadata["gateway"] ?: "gateway desconhecido").let{ "Payment Gateway: $it"},
                receipt.metadata["id"]?.let { "Transaction ID: $it" },
                receipt.metadata["id"]?.let { "Rejection/flag reason: $it" },
                "",
                "=== Processing Complete ==="
            )
    }
}