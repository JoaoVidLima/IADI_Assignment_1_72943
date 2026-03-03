package pt.unl.fct.iadi.orderprocessingplatform.pricing

import pt.unl.fct.iadi.orderprocessingplatform.domain.Order

class BasicPriceCalculator : PriceCalculator {

    override fun calculateTotalPrice(order: Order): Double {

        var total = 0.0
        for (item in order.items) {
            total += item.quantity * item.price
        }

        return total
    }
}