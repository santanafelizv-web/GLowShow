package com.sanvalero.glowshop.domain.usecase

import com.sanvalero.glowshop.domain.model.OrderLine
import com.sanvalero.glowshop.domain.model.OrderTotals
import java.math.BigDecimal
import java.math.RoundingMode

class CalculateOrder {
    operator fun invoke(lines: List<OrderLine>): Result<OrderTotals> {
        if (lines.isEmpty()) return Result.failure(IllegalArgumentException("Agrega al menos un producto."))
        if (lines.any { it.quantity <= 0 }) {
            return Result.failure(IllegalArgumentException("La cantidad debe ser mayor que cero."))
        }
        if (lines.any { it.unitPriceCents < 0 }) {
            return Result.failure(IllegalArgumentException("El precio no puede ser negativo."))
        }

        val subtotal = lines.sumOf { it.quantity.toLong() * it.unitPriceCents }
        val tax = BigDecimal.valueOf(subtotal)
            .multiply(BigDecimal("0.18"))
            .setScale(0, RoundingMode.HALF_UP)
            .longValueExact()
        return Result.success(OrderTotals(subtotal, tax, subtotal + tax))
    }
}
