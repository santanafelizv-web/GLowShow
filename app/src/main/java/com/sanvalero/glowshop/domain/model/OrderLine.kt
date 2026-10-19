package com.sanvalero.glowshop.domain.model

/** Money is kept in cents so totals never depend on floating-point rounding. */
data class OrderLine(
    val productName: String,
    val quantity: Int,
    val unitPriceCents: Long
)

data class OrderTotals(
    val subtotalCents: Long,
    val taxCents: Long,
    val totalCents: Long
)
