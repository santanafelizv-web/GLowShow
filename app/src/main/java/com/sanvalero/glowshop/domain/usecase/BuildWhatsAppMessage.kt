package com.sanvalero.glowshop.domain.usecase

import com.sanvalero.glowshop.domain.model.OrderLine
import com.sanvalero.glowshop.domain.model.OrderTotals
import java.text.NumberFormat
import java.util.Locale

class BuildWhatsAppMessage {
    operator fun invoke(
        orderNumber: String,
        customerName: String,
        lines: List<OrderLine>,
        totals: OrderTotals
    ): String {
        val currency = NumberFormat.getNumberInstance(Locale("es", "DO")).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        fun money(cents: Long) = "RD$ ${currency.format(cents / 100.0)}"

        return buildString {
            appendLine("GlowShop Cosméticos - Pedido #$orderNumber")
            appendLine("Cliente: $customerName")
            appendLine()
            lines.forEach { line ->
                appendLine("${line.quantity} x ${line.productName} - ${money(line.quantity * line.unitPriceCents)}")
            }
            appendLine()
            appendLine("Subtotal: ${money(totals.subtotalCents)}")
            appendLine("ITBIS 18%: ${money(totals.taxCents)}")
            appendLine("TOTAL: ${money(totals.totalCents)}")
            append("¡Gracias por tu compra!")
        }
    }
}
