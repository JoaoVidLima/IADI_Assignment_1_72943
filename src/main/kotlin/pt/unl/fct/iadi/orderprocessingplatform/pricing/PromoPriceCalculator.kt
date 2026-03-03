package pt.unl.fct.iadi.orderprocessingplatform.pricing

import pt.unl.fct.iadi.orderprocessingplatform.domain.Order

class PromoPriceCalculator : PriceCalculator {

    override fun calculateTotalPrice(order: Order): Double {

        var total = 0.0
        for(item in order.items) {

            if(item.quantity > 5){
                total += item.quantity * item.price * 0.8
            }
            else{
                total += item.quantity * item.price
            }
        }
        return total
    }
}