package pt.unl.fct.iadi.orderprocessingplatform.pricing

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.orderprocessingplatform.domain.Order

@Component
@ConditionalOnProperty(name = ["pricing.promo.enabled"], havingValue = "true", matchIfMissing = false)
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